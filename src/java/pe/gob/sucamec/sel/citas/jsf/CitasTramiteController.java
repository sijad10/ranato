/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.jsf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
import pe.gob.sucamec.bdintegrado.data.AmaLicenciaDeUso;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.bdintegrado.data.AmaTipoLicencia;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbCsCertifsalud;
import pe.gob.sucamec.bdintegrado.data.SbExpVirtualSolicitud;
import pe.gob.sucamec.bdintegrado.data.SbProcesoTupa;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.renagi.jsf.util.ReportUtil;
import pe.gob.sucamec.sel.citas.data.CitaHora;
import pe.gob.sucamec.sel.citas.data.CitaSbFeriado;
import pe.gob.sucamec.sel.citas.data.CitaSbNumeracion;
import pe.gob.sucamec.sel.citas.data.CitaSbTurnoExpediente;
import pe.gob.sucamec.sel.citas.data.CitaTipoBase;
import pe.gob.sucamec.sel.citas.data.CitaTipoGamac;
import pe.gob.sucamec.sel.citas.data.CitaTurComprobante;
import pe.gob.sucamec.sel.citas.data.CitaTurConstancia;
import pe.gob.sucamec.sel.citas.data.CitaTurLicenciaReg;
import pe.gob.sucamec.sel.citas.data.CitaTurPersona;
import pe.gob.sucamec.sel.citas.data.CitaTurProgramacion;
import pe.gob.sucamec.sel.citas.data.CitaTurTurno;
import pe.gob.sucamec.sel.citas.jsf.util.Captcha;
import pe.gob.sucamec.sel.citas.jsf.util.EstadoCrud;
import pe.gob.sucamec.sel.citas.jsf.util.JsfUtil;
import pe.gob.sucamec.sel.citas.jsf.util.RenagiUploadFilesController;
import pe.gob.sucamec.sel.citas.ws.CitaWsPide;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

/**
 *
 * @author msalinas
 */
@Named("citasTramiteController")
@SessionScoped
public class CitasTramiteController implements Serializable {

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
    CitaTurTurno registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<CitaTurTurno> resultados = null;

    //variables
    private String tipoProg;
    private Date hoy = new Date();
    private Date fechaIni;
    private Date maxFecha;
    private List<CitaTurProgramacion> listaProgramaciones = new ArrayList();
    private List<CitaTurConstancia> listaTipoLicencia = new ArrayList();
    private List<CitaHora> listaHoras = new ArrayList();
    private CitaTurConstancia tipoLic;
    private boolean entroTipoarma = false;
    private CitaTurPersona perExamen;
    private CitaTurPersona perPago;
    private boolean visibleNroDocVal = false;
    private int maxNroDoc;
    private String placeholder = "";
    private boolean visibleNroRuc = false;
    private boolean visibleAgrComprobante = false;
    private List<CitaTurComprobante> listaComprobantes = new ArrayList();
//    private boolean visibleCodVer = false;
//    private String codVerificacion;
    private CitaTurComprobante turComprobante = new CitaTurComprobante();
    private List<CitaTipoGamac> listaCalibres;
    private boolean visibleCalibre = false;
    private String horaProgra;
    private String horaPresen;
    private String horaTolera;
    private String nroConsulta;
    private String nroDoc;
    private String fechaTurnoAnt;
    //confirmar cita
    private String hora;
    private String tolerancia;
    private String dniCe;
    private String nomApe;
    private String color;
    private boolean visibleRegistrar = false;
    private boolean visibleConfirmar = false;
    //listar programaciones
    private CitaTurProgramacion programacion;
    private List<Map> listaTurnosMap = new ArrayList();
    private int nroExamen = 0;
    private Map selectedMap = new HashMap();
    private CitaTurConstancia selectedConstancia;
    private CitaTurConstancia turConstancia1;
    private CitaTurConstancia turConstancia2;
    private CitaTurConstancia turConstancia3;
    private String selectedNroDoc;
    private String nroExpediente;
    private String nroSolicitud="";
    //foto
    private String fileName;
    private StreamedContent fotoImg;
    private RenagiUploadFilesController managefile;
    private byte[] data;

    private String archivo;
    private byte[] fotoByte;
    private UploadedFile file;
    private StreamedContent foto;

    //variables
    private boolean fotoGuardada = false;
    private boolean exaTeoGuardado = false;
    private boolean exaTiroGuardado = false;
    private boolean edicionGuardada = false;
    private CitaTipoBase tipoDoc;
    private String nroDocVal;
    private CitaTipoGamac tipoTramite;
    private String nroRuc;
    private String rznSocial;
    private String rucRznSocial;
    private CitaTipoGamac tipoArma;
    private CitaTipoGamac tipoCalibre;
    private CitaTipoBase tipoBanco;
    private String voucherTasa;
    private String voucherSeq;
    private Date fechaPago;
    private String voucherCta;
    private String voucherAgencia;
    private String voucherAutentica;
    private Date fechaTurno;
    private String paso = "paso1";
    private CitaTurTurno turnoRep = new CitaTurTurno();
    private boolean visibleSiguiente3 = false;

    private boolean visibleMultimodal = false;
    private List<CitaTipoGamac> modalidadesSelected = new ArrayList();
    private String codVoucher;

    private boolean visibleCalendarCentral = false;
    private boolean visibleCalendarZonal = false;
    private boolean visibleCalendarTacna = false;

    private Date fechaProcess;

    //TasasTupa
    private boolean solicitarRecibo;
    private boolean solicitarVoucherTemp;
    private SbProcesoTupa parametrizacionTupa;
    private Date fechaMinDeposito;
    private SbRecibos reciboBn;
    private String strNroSecuencia;
    private String strFechaMovimiento;
    private String strImporte;
    private boolean disableReciboBn = false;

    // Renovación Licencia
    private CitaTipoGamac subTramite;
    private boolean opeLicIni = false;
    private boolean opeLicRen = false;
    private List<AmaLicenciaDeUso> listaLicencia = new ArrayList();
    private AmaLicenciaDeUso licencia;
    private String licenciaModalidad;
    private List<CitaTipoGamac> lstTipOpe = new ArrayList();

    //captcha
    Captcha captcha;
    boolean terminos = false;

    /// ELEGIR VOUCHER
    private boolean eligeReciboLista;

    //MSALINAS
    private List<SbExpVirtualSolicitud> listaSolExpedientes = new ArrayList();    
    private List<CitaTipoGamac> listaSubTramites = new ArrayList();
    
    //ECELIZ
    private List<SbExpVirtualSolicitud> listaSolExpedientesSelected = new ArrayList();

    @Inject
    private LoginController loginController;
    @Inject
    private CitaTipoGamacController tipoGamacController;
    @Inject
    private CitaWsPide wsPideController;
    //@Inject
    //private WsTramDoc wsTramDocController;
    @Inject
    private RenagiUploadFilesController upFilesCont;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurTurnoFacade ejbFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurProgramacionFacade ejbTurProgramacionFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTipoGamacFacade ejbTipoGamacFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurConstanciaFacade ejbTurConstanciaFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurComprobanteFacade ejbTurComprobanteFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurPersonaFacade ejbPersonaFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaRma1369Facade ejbRma1369Facade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitasbFeriadoFacade ejbSbFeriadoFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaSbNumeracionFacade ejbSbNumeracionFacade;
    //@EJB
    //private pe.gob.sucamec.sel.gamac.beans.GamacAmaLicenciaDeUsoFacade ejbGamacAmaLicenciaDeUsoFacade;
    //@EJB
    //private pe.gob.sucamec.bdintegrado.bean.AmaGuiaTransitoFacade ejbAmaGuiaTransitoFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaSbRecibosFacade ejbSbRecibosFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaSbReciboRegistroFacade ejbSbReciboRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaLicenciaDeUsoFacade ejbAmaLicenciaDeUsoFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbPersonaFacade ejbSbPersona;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbCsCertifsaludFacade ejbSbCsCertifsaludFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbProcesoTupaFacade ejbSbProcesoTupaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade sbRecibosFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.GestionCitasFacade gestionCitasFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurLicenciaRegFacade ejbTurLicenciaRegFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurPersonaFacade ejbCitaTurPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbExpVirtualSolicitudFacade ejbSbExpVirtualSolicitudFacade;

    //Getters & Setters
    public Date getFechaIni() {
        Date maxFechaAux = new GregorianCalendar(2019, Calendar.JANUARY, 7).getTime();

        fechaIni = hoy;
        if (maxFechaAux.compareTo(hoy) > 0) {
            fechaIni = maxFechaAux;
        }

        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public byte[] getFotoByte() {
        return fotoByte;
    }

    public void setFotoByte(byte[] fotoByte) {
        this.fotoByte = fotoByte;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getFoto() {
        return foto;
    }

    public void setFoto(StreamedContent foto) {
        this.foto = foto;
    }

    public String getTipoProg() {
        return tipoProg;
    }

    public void setTipoProg(String tipoProg) {
        this.tipoProg = tipoProg;
    }

    public Date getHoy() {
        return hoy;
    }

    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }

    public Date getMaxFecha() {
//        List<CitaSbFeriado> feriadosList = ejbSbFeriadoFacade.listaFeriados();

//        maxFecha = new Date();
        Date fechaIni = new Date();
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(fechaIni);

//        if (fechaIni.getDay() < 4) {
//            fecha.add(Calendar.DAY_OF_MONTH, 9);
//            maxFecha = fecha.getTime();
//            while (!Objects.equals(fechaIni, maxFecha)) {
//                for (CitaSbFeriado feriado : feriadosList) {
//                    if (JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 2).equals(JsfUtil.obtenerFechaXCriterio(fechaIni, 2))
//                            && JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 3).equals(JsfUtil.obtenerFechaXCriterio(fechaIni, 3))) {
//                        fecha.setTime(maxFecha);
//                        fecha.add(Calendar.DAY_OF_MONTH, 1);
//                        maxFecha = fecha.getTime();
//                        if (maxFecha.getDay() == 6) {
//                            fecha.setTime(maxFecha);
//                            fecha.add(Calendar.DAY_OF_MONTH, 2);
//                            maxFecha = fecha.getTime();
//                        } else if (maxFecha.getDay() == 7) {
//                            fecha.setTime(maxFecha);
//                            fecha.add(Calendar.DAY_OF_MONTH, 1);
//                            maxFecha = fecha.getTime();
//                        }
//                    }
//                }
//                fecha.setTime(fechaIni);
//                fecha.add(Calendar.DAY_OF_MONTH, 1);
//                fechaIni = fecha.getTime();
//            }
//        } else {
//            fecha.add(Calendar.DAY_OF_MONTH, 11);
//            maxFecha = fecha.getTime();
//            while (!Objects.equals(fechaIni, maxFecha)) {
//                for (CitaSbFeriado feriado : feriadosList) {
//                    if (JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 2).equals(JsfUtil.obtenerFechaXCriterio(fechaIni, 2))
//                            && JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 3).equals(JsfUtil.obtenerFechaXCriterio(fechaIni, 3))) {
//                        fecha.setTime(maxFecha);
//                        fecha.add(Calendar.DAY_OF_MONTH, 1);
//                        maxFecha = fecha.getTime();
//                        if (maxFecha.getDay() == 6) {
//                            fecha.setTime(maxFecha);
//                            fecha.add(Calendar.DAY_OF_MONTH, 2);
//                            maxFecha = fecha.getTime();
//                        } else if (maxFecha.getDay() == 7) {
//                            fecha.setTime(maxFecha);
//                            fecha.add(Calendar.DAY_OF_MONTH, 1);
//                            maxFecha = fecha.getTime();
//                        }
//                    }
//                }
//                fecha.setTime(fechaIni);
//                fecha.add(Calendar.DAY_OF_MONTH, 1);
//                fechaIni = fecha.getTime();
//            }
//        }
        //CAMBIO A 15 DIAS CALENDARIO
        maxFecha = fecha.getTime();
        fecha.setTime(maxFecha);
        fecha.add(Calendar.DAY_OF_MONTH, 14);
        maxFecha = fecha.getTime();

//        Date maxFechaAux = new GregorianCalendar(2016, Calendar.OCTOBER, 31).getTime();
//
//        if (maxFechaAux.compareTo(maxFecha) > 0) {
//            maxFecha = maxFechaAux;
//        }
        return maxFecha;
    }

    public Date getMaxFechaZonal() {
        List<CitaSbFeriado> feriadosList = ejbSbFeriadoFacade.listaFeriados();

//        maxFecha = new Date();
        Date fechaIni = new Date();
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(fechaIni);

        if (fechaIni.getDay() < 4) {
//            fecha.add(Calendar.DAY_OF_MONTH, 9);
            fecha.add(Calendar.DAY_OF_MONTH, 12);
            maxFecha = fecha.getTime();
            while (!Objects.equals(fechaIni, maxFecha)) {
                for (CitaSbFeriado feriado : feriadosList) {
                    if (JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 2).equals(JsfUtil.obtenerFechaXCriterio(fechaIni, 2))
                            && JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 3).equals(JsfUtil.obtenerFechaXCriterio(fechaIni, 3))) {
                        fecha.setTime(maxFecha);
                        fecha.add(Calendar.DAY_OF_MONTH, 1);
                        maxFecha = fecha.getTime();
                        if (maxFecha.getDay() == 6) {
                            fecha.setTime(maxFecha);
                            fecha.add(Calendar.DAY_OF_MONTH, 2);
                            maxFecha = fecha.getTime();
                        } else if (maxFecha.getDay() == 7) {
                            fecha.setTime(maxFecha);
                            fecha.add(Calendar.DAY_OF_MONTH, 1);
                            maxFecha = fecha.getTime();
                        }
                    }
                }
                fecha.setTime(fechaIni);
                fecha.add(Calendar.DAY_OF_MONTH, 1);
                fechaIni = fecha.getTime();
            }
        } else {
//            fecha.add(Calendar.DAY_OF_MONTH, 11);
            fecha.add(Calendar.DAY_OF_MONTH, 14);
            maxFecha = fecha.getTime();
            while (!Objects.equals(fechaIni, maxFecha)) {
                for (CitaSbFeriado feriado : feriadosList) {
                    if (JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 2).equals(JsfUtil.obtenerFechaXCriterio(fechaIni, 2))
                            && JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 3).equals(JsfUtil.obtenerFechaXCriterio(fechaIni, 3))) {
                        fecha.setTime(maxFecha);
                        fecha.add(Calendar.DAY_OF_MONTH, 1);
                        maxFecha = fecha.getTime();
                        if (maxFecha.getDay() == 6) {
                            fecha.setTime(maxFecha);
                            fecha.add(Calendar.DAY_OF_MONTH, 2);
                            maxFecha = fecha.getTime();
                        } else if (maxFecha.getDay() == 7) {
                            fecha.setTime(maxFecha);
                            fecha.add(Calendar.DAY_OF_MONTH, 1);
                            maxFecha = fecha.getTime();
                        }
                    }
                }
                fecha.setTime(fechaIni);
                fecha.add(Calendar.DAY_OF_MONTH, 1);
                fechaIni = fecha.getTime();
            }
        }

//        maxFecha = fecha.getTime();
        Date maxFechaAux = new GregorianCalendar(2016, Calendar.OCTOBER, 31).getTime();

        if (maxFechaAux.compareTo(maxFecha) > 0) {
            maxFecha = maxFechaAux;
        }

        return maxFecha;
    }

    public void setMaxFecha(Date maxFecha) {
        this.maxFecha = maxFecha;
    }

    public List<CitaTurProgramacion> getListaProgramaciones() {
        return listaProgramaciones;
    }

    public void setListaProgramaciones(List<CitaTurProgramacion> listaProgramaciones) {
        this.listaProgramaciones = listaProgramaciones;
    }

    public List<CitaTurConstancia> getListaTipoLicencia() {
        return listaTipoLicencia;
    }

    public void setListaTipoLicencia(List<CitaTurConstancia> listaTipoLicencia) {
        this.listaTipoLicencia = listaTipoLicencia;
    }

    public List<CitaHora> getListaHoras() {
        return listaHoras;
    }

    public void setListaHoras(List<CitaHora> listaHoras) {
        this.listaHoras = listaHoras;
    }

    public CitaTurConstancia getTipoLic() {
        return tipoLic;
    }

    public void setTipoLic(CitaTurConstancia tipoLic) {
        this.tipoLic = tipoLic;
    }

    public CitaTurPersona getPerExamen() {
        return perExamen;
    }

    public void setPerExamen(CitaTurPersona perExamen) {
        this.perExamen = perExamen;
    }

    public CitaTurPersona getPerPago() {
        return perPago;
    }

    public void setPerPago(CitaTurPersona perPago) {
        this.perPago = perPago;
    }

    public boolean isVisibleNroDocVal() {
        return visibleNroDocVal;
    }

    public void setVisibleNroDocVal(boolean visibleNroDocVal) {
        this.visibleNroDocVal = visibleNroDocVal;
    }

    public int getMaxNroDoc() {
        return maxNroDoc;
    }

    public void setMaxNroDoc(int maxNroDoc) {
        this.maxNroDoc = maxNroDoc;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public boolean isVisibleNroRuc() {
        return visibleNroRuc;
    }

    public void setVisibleNroRuc(boolean visibleNroRuc) {
        this.visibleNroRuc = visibleNroRuc;
    }

    public String getRznSocial() {
        return rznSocial;
    }

    public void setRznSocial(String rznSocial) {
        this.rznSocial = rznSocial;
    }

    public String getRucRznSocial() {
        return rucRznSocial;
    }

    public void setRucRznSocial(String rucRznSocial) {
        this.rucRznSocial = rucRznSocial;
    }

    public boolean isVisibleAgrComprobante() {
        return visibleAgrComprobante;
    }

    public void setVisibleAgrComprobante(boolean visibleAgrComprobante) {
        this.visibleAgrComprobante = visibleAgrComprobante;
    }

    public List<CitaTurComprobante> getListaComprobantes() {
        return listaComprobantes;
    }

    public void setListaComprobantes(List<CitaTurComprobante> listaComprobantes) {
        this.listaComprobantes = listaComprobantes;
    }

//    public boolean isVisibleCodVer() {
//        return visibleCodVer;
//    }
//
//    public void setVisibleCodVer(boolean visibleCodVer) {
//        this.visibleCodVer = visibleCodVer;
//    }
//    public String getCodVerificacion() {
//        return codVerificacion;
//    }
//
//    public void setCodVerificacion(String codVerificacion) {
//        this.codVerificacion = codVerificacion;
//    }
    public CitaTurComprobante getTurComprobante() {
        return turComprobante;
    }

    public void setTurComprobante(CitaTurComprobante turComprobante) {
        this.turComprobante = turComprobante;
    }

    public List<CitaTipoGamac> getListaCalibres() {
        return listaCalibres;
    }

    public void setListaCalibres(List<CitaTipoGamac> listaCalibres) {
        this.listaCalibres = listaCalibres;
    }

    public boolean isVisibleCalibre() {
        return visibleCalibre;
    }

    public void setVisibleCalibre(boolean visibleCalibre) {
        this.visibleCalibre = visibleCalibre;
    }

    public String getHoraProgra() {
        return horaProgra;
    }

    public void setHoraProgra(String horaProgra) {
        this.horaProgra = horaProgra;
    }

    public String getHoraPresen() {
        return horaPresen;
    }

    public void setHoraPresen(String horaPresen) {
        this.horaPresen = horaPresen;
    }

    public String getHoraTolera() {
        return horaTolera;
    }

    public void setHoraTolera(String horaTolera) {
        this.horaTolera = horaTolera;
    }

    public String getNroConsulta() {
        return nroConsulta;
    }

    public void setNroConsulta(String nroConsulta) {
        this.nroConsulta = nroConsulta;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getFechaTurnoAnt() {
        return fechaTurnoAnt;
    }

    public void setFechaTurnoAnt(String fechaTurnoAnt) {
        this.fechaTurnoAnt = fechaTurnoAnt;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTolerancia() {
        return tolerancia;
    }

    public void setTolerancia(String tolerancia) {
        this.tolerancia = tolerancia;
    }

    public String getDniCe() {
        return dniCe;
    }

    public void setDniCe(String dniCe) {
        this.dniCe = dniCe;
    }

    public String getNomApe() {
        return nomApe;
    }

    public void setNomApe(String nomApe) {
        this.nomApe = nomApe;
    }

    public boolean isVisibleRegistrar() {
        return visibleRegistrar;
    }

    public void setVisibleRegistrar(boolean visibleRegistrar) {
        this.visibleRegistrar = visibleRegistrar;
    }

    public boolean isVisibleConfirmar() {
        return visibleConfirmar;
    }

    public void setVisibleConfirmar(boolean visibleConfirmar) {
        this.visibleConfirmar = visibleConfirmar;
    }

    public CitaTurProgramacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(CitaTurProgramacion programacion) {
        this.programacion = programacion;
    }

    public List<Map> getListaTurnosMap() {
        return listaTurnosMap;
    }

    public void setListaTurnosMap(List<Map> listaTurnosMap) {
        this.listaTurnosMap = listaTurnosMap;
    }

    public int getNroExamen() {
        return nroExamen;
    }

    public void setNroExamen(int nroExamen) {
        this.nroExamen = nroExamen;
    }

    public Map getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(Map selectedMap) {
        this.selectedMap = selectedMap;
    }

    public CitaTurConstancia getSelectedConstancia() {
        return selectedConstancia;
    }

    public void setSelectedConstancia(CitaTurConstancia selectedConstancia) {
        this.selectedConstancia = selectedConstancia;
    }

    public CitaTurConstancia getTurConstancia1() {
        return turConstancia1;
    }

    public void setTurConstancia1(CitaTurConstancia turConstancia1) {
        this.turConstancia1 = turConstancia1;
    }

    public CitaTurConstancia getTurConstancia2() {
        return turConstancia2;
    }

    public void setTurConstancia2(CitaTurConstancia turConstancia2) {
        this.turConstancia2 = turConstancia2;
    }

    public CitaTurConstancia getTurConstancia3() {
        return turConstancia3;
    }

    public void setTurConstancia3(CitaTurConstancia turConstancia3) {
        this.turConstancia3 = turConstancia3;
    }

    public String getSelectedNroDoc() {
        return selectedNroDoc;
    }

    public void setSelectedNroDoc(String selectedNroDoc) {
        this.selectedNroDoc = selectedNroDoc;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public StreamedContent getFotoImg() {
        return fotoImg;
    }

    public void setFotoImg(StreamedContent fotoImg) {
        this.fotoImg = fotoImg;
    }

    public RenagiUploadFilesController getManagefile() {
        return managefile;
    }

    public void setManagefile(RenagiUploadFilesController managefile) {
        this.managefile = managefile;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isFotoGuardada() {
        return fotoGuardada;
    }

    public void setFotoGuardada(boolean fotoGuardada) {
        this.fotoGuardada = fotoGuardada;
    }

    public boolean isExaTeoGuardado() {
        return exaTeoGuardado;
    }

    public void setExaTeoGuardado(boolean exaTeoGuardado) {
        this.exaTeoGuardado = exaTeoGuardado;
    }

    public boolean isExaTiroGuardado() {
        return exaTiroGuardado;
    }

    public void setExaTiroGuardado(boolean exaTiroGuardado) {
        this.exaTiroGuardado = exaTiroGuardado;
    }

    public boolean isEdicionGuardada() {
        return edicionGuardada;
    }

    public void setEdicionGuardada(boolean edicionGuardada) {
        this.edicionGuardada = edicionGuardada;
    }

    public CitaTipoBase getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(CitaTipoBase tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNroDocVal() {
        return nroDocVal;
    }

    public void setNroDocVal(String nroDocVal) {
        this.nroDocVal = nroDocVal;
    }

    public CitaTipoGamac getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(CitaTipoGamac tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public String getNroRuc() {
        return nroRuc;
    }

    public void setNroRuc(String nroRuc) {
        this.nroRuc = nroRuc;
    }

    public boolean isEligeReciboLista() {
        return eligeReciboLista;
    }

    public void setEligeReciboLista(boolean eligeReciboLista) {
        this.eligeReciboLista = eligeReciboLista;
    }

    public List<SbExpVirtualSolicitud> getListaSolExpedientes() {
        return listaSolExpedientes;
    }

    public void setListaSolExpedientes(List<SbExpVirtualSolicitud> listaSolExpedientes) {
        this.listaSolExpedientes = listaSolExpedientes;
    }

    public CitaTipoGamac getTipoArma() {
        return tipoArma;
    }

    public void setTipoArma(CitaTipoGamac tipoArma) {
        this.tipoArma = tipoArma;
    }

    public CitaTipoGamac getTipoCalibre() {
        return tipoCalibre;
    }

    public void setTipoCalibre(CitaTipoGamac tipoCalibre) {
        this.tipoCalibre = tipoCalibre;
    }

    public CitaTipoBase getTipoBanco() {
        tipoBanco = ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_BCO_NAC");
        return tipoBanco;
    }

    public void setTipoBanco(CitaTipoBase tipoBanco) {
        this.tipoBanco = tipoBanco;
    }

    public String getVoucherTasa() {
        return voucherTasa;
    }

    public void setVoucherTasa(String voucherTasa) {
        this.voucherTasa = voucherTasa;
    }

    public String getVoucherSeq() {
        return voucherSeq;
    }

    public void setVoucherSeq(String voucherSeq) {
        this.voucherSeq = voucherSeq;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getVoucherCta() {
        return voucherCta;
    }

    public void setVoucherCta(String voucherCta) {
        this.voucherCta = voucherCta;
    }

    public String getVoucherAgencia() {
        return voucherAgencia;
    }

    public void setVoucherAgencia(String voucherAgencia) {
        this.voucherAgencia = voucherAgencia;
    }

    public String getVoucherAutentica() {
        return voucherAutentica;
    }

    public void setVoucherAutentica(String voucherAutentica) {
        this.voucherAutentica = voucherAutentica;
    }

    public Date getFechaTurno() {
        return fechaTurno;
    }

    public void setFechaTurno(Date fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public String getPaso() {
        return paso;
    }

    public void setPaso(String paso) {
        this.paso = paso;
    }

    public CitaTurTurno getTurnoRep() {
        return turnoRep;
    }

    public void setTurnoRep(CitaTurTurno turnoRep) {
        this.turnoRep = turnoRep;
    }

    public boolean isVisibleSiguiente3() {
        return visibleSiguiente3;
    }

    public void setVisibleSiguiente3(boolean visibleSiguiente3) {
        this.visibleSiguiente3 = visibleSiguiente3;
    }

    public boolean isVisibleMultimodal() {
        return visibleMultimodal;
    }

    public void setVisibleMultimodal(boolean visibleMultimodal) {
        this.visibleMultimodal = visibleMultimodal;
    }

    public List<CitaTipoGamac> getModalidadesSelected() {
        return modalidadesSelected;
    }

    public void setModalidadesSelected(List<CitaTipoGamac> modalidadesSelected) {
        this.modalidadesSelected = modalidadesSelected;
    }

    public String getCodVoucher() {
        return codVoucher;
    }

    public void setCodVoucher(String codVoucher) {
        this.codVoucher = codVoucher;
    }

    public String getCpSede() {
        if (registro != null) {
            if (registro.getTipoSedeId() != null) {
                return registro.getTipoSedeId().getCodProg();
            }
        }
        return "null";
    }

    public boolean isVisibleCalendarZonal() {
        return visibleCalendarZonal;
    }

    public void setVisibleCalendarZonal(boolean visibleCalendarZonal) {
        this.visibleCalendarZonal = visibleCalendarZonal;
    }

    public boolean isVisibleCalendarTacna() {
        return visibleCalendarTacna;
    }

    public void setVisibleCalendarTacna(boolean visibleCalendarTacna) {
        this.visibleCalendarTacna = visibleCalendarTacna;
    }

    public boolean isVisibleCalendarCentral() {
        return visibleCalendarCentral;
    }

    public void setVisibleCalendarCentral(boolean visibleCalendarCentral) {
        this.visibleCalendarCentral = visibleCalendarCentral;
    }

    public String getFeriadosStrList() {
        List<CitaSbFeriado> feriadosList = ejbSbFeriadoFacade.listaFeriados();
        String s = "[";
        for (CitaSbFeriado feriado : feriadosList) {
            s = s
                    + "\"" + JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 2)
                    + "-" + JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 3)
                    + "-" + JsfUtil.obtenerFechaXCriterio(feriado.getFechaFeriado(), 1) + "\",";
        }
        s = s.substring(0, s.length() - 1);
        s += "]";
        return s;
    }

    public String getActivadosStrList() {
        List<CitaSbFeriado> activadosList = ejbSbFeriadoFacade.listaFeriados();
        String s = "[";
        for (CitaSbFeriado activado : activadosList) {
            s = s
                    + "\"" + JsfUtil.obtenerFechaXCriterio(activado.getFechaFeriado(), 2)
                    + "-" + JsfUtil.obtenerFechaXCriterio(activado.getFechaFeriado(), 3)
                    + "-" + JsfUtil.obtenerFechaXCriterio(activado.getFechaFeriado(), 1) + "\",";
        }
        s = s.substring(0, s.length() - 1);
        s += "]";
        return s;
    }

    public boolean isSolicitarRecibo() {
        return solicitarRecibo;
    }

    public void setSolicitarRecibo(boolean solicitarRecibo) {
        this.solicitarRecibo = solicitarRecibo;
    }

    public boolean isSolicitarVoucherTemp() {
        return solicitarVoucherTemp;
    }

    public void setSolicitarVoucherTemp(boolean solicitarVoucherTemp) {
        this.solicitarVoucherTemp = solicitarVoucherTemp;
    }

    public SbProcesoTupa getParametrizacionTupa() {
        return parametrizacionTupa;
    }

    public void setParametrizacionTupa(SbProcesoTupa parametrizacionTupa) {
        this.parametrizacionTupa = parametrizacionTupa;
    }

    public Date getFechaMinDeposito() {
        return fechaMinDeposito;
    }

    public void setFechaMinDeposito(Date fechaMinDeposito) {
        this.fechaMinDeposito = fechaMinDeposito;
    }

    public SbRecibos getReciboBn() {
        return reciboBn;
    }

    public void setReciboBn(SbRecibos reciboBn) {
        this.reciboBn = reciboBn;
    }

    public String getStrNroSecuencia() {
        return strNroSecuencia;
    }

    public void setStrNroSecuencia(String strNroSecuencia) {
        this.strNroSecuencia = strNroSecuencia;
    }

    public String getStrFechaMovimiento() {
        return strFechaMovimiento;
    }

    public void setStrFechaMovimiento(String strFechaMovimiento) {
        this.strFechaMovimiento = strFechaMovimiento;
    }

    public String getStrImporte() {
        return strImporte;
    }

    public void setStrImporte(String strImporte) {
        this.strImporte = strImporte;
    }

    public boolean isDisableReciboBn() {
        return disableReciboBn;
    }

    public void setDisableReciboBn(boolean disableReciboBn) {
        this.disableReciboBn = disableReciboBn;
    }

    public CitaTipoGamac getSubTramite() {
        return subTramite;
    }

    public void setSubTramite(CitaTipoGamac subTramite) {
        this.subTramite = subTramite;
    }

    public boolean isOpeLicIni() {
        return opeLicIni;
    }

    public void setOpeLicIni(boolean opeLicIni) {
        this.opeLicIni = opeLicIni;
    }

    public boolean isOpeLicRen() {
        return opeLicRen;
    }

    public void setOpeLicRen(boolean opeLicRen) {
        this.opeLicRen = opeLicRen;
    }

    public List<AmaLicenciaDeUso> getListaLicencia() {
        return listaLicencia;
    }

    public void setListaLicencia(List<AmaLicenciaDeUso> listaLicencia) {
        this.listaLicencia = listaLicencia;
    }

    public AmaLicenciaDeUso getLicencia() {
        return licencia;
    }

    public void setLicencia(AmaLicenciaDeUso licencia) {
        this.licencia = licencia;
    }

    public String getLicenciaModalidad() {
        return licenciaModalidad;
    }

    public void setLicenciaModalidad(String licenciaModalidad) {
        this.licenciaModalidad = licenciaModalidad;
    }

    public List<CitaTipoGamac> getLstTipOpe() {
        return lstTipOpe;
    }

    public void setLstTipOpe(List<CitaTipoGamac> lstTipOpe) {
        this.lstTipOpe = lstTipOpe;
    }

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

    public String getNroSolicitud() {
        return nroSolicitud;
    }

    public void setNroSolicitud(String nroSolicitud) {
        this.nroSolicitud = nroSolicitud;
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
     * Evento tipo 'actionListener' para cambiar el estado a activo en un
     * registro de una lista.
     *
     */
    public void activar() {
        try {
            registro = (CitaTurTurno) resultados.getRowData();
            registro.setActivo((short) 1);
            ejbFacade.edit(registro);
            JsfUtil.addSuccessMessage(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a anulado en un
     * registro de una lista.
     *
     */
    public void anular() {
        try {
            registro = (CitaTurTurno) resultados.getRowData();
            registro.setActivo((short) 0);
            ejbFacade.edit(registro);
            JsfUtil.addSuccessMessage(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public CitaTurTurno getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(CitaTurTurno registro) {
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
    public ListDataModel<CitaTurTurno> getResultados() {
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
     * Devuelve una lista de las solicitudes seleccionadas    
     */
    public List<SbExpVirtualSolicitud> getListaSolExpedientesSelected() {
        return listaSolExpedientesSelected;
    }

    public void setListaSolExpedientesSelected(List<SbExpVirtualSolicitud> listaSolExpedientesSelected) {
        this.listaSolExpedientesSelected = listaSolExpedientesSelected;
    }
    
    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<CitaTurTurno> getSelectItems() {
        return ejbFacade.findAll();
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
//        resultados = new ListDataModel(turTurnoFacade.selectLike(filtro));
    }
    /**
     * Realiza una búsqueda de las solicitudes con estado CREADO.
     */
    public void buscarSolicitudes() {
        listaSolExpedientes=ejbSbExpVirtualSolicitudFacade.listarSolicitudesXCriterios(nroSolicitud, "nroSolicitud", loginController.getUsuario().getPersona().getId(), "'TP_EXVIEST_CRE'",registro.getTipoSedeId().getId());
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (CitaTurTurno) resultados.getRowData();
        estado = EstadoCrud.VER;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     */
    public void mostrarEditar() {
        registro = (CitaTurTurno) resultados.getRowData();
        estado = EstadoCrud.EDITAR;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     */
    public void mostrarCrear() {
        estado = EstadoCrud.CREAR;
        registro = new CitaTurTurno();
        registro.setActivo((short) 1);
    }

    /**
     * Evento para guardar la información al editar un registro.
     *
     */
    public void editar() {
        try {
            registro = (CitaTurTurno) JsfUtil.entidadMayusculas(registro, "");
            ejbFacade.edit(registro);
            estado = EstadoCrud.BUSCAR;
            JsfUtil.addSuccessMessage(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     *
     */
    public void crear() {
        if (validaCaptchaTC()) {
            if (validarCreate()) {
               
                try {
                    //ADMINISTRADO
                    if (loginController.getUsuario().getPersona().getTipoId().getCodProg().equals("TP_PER_NAT")) {
                        //PER_NAT
                        if (ejbPersonaFacade.buscarPersonaXNroDoc(loginController.getUsuario().getNumDoc().trim()).isEmpty()) {
                            if (perExamen.getTipoDoc().getCodProg().equals("TP_DOCID_DNI")) {
                                //WEB SERVICE PARA CREAR PERSONA DESDE RENIEC
                                perExamen = wsPideController.buscarReniec(loginController.getUsuario().getNumDoc().trim());
                                perExamen = (CitaTurPersona) JsfUtil.entidadMayusculas(perExamen, "");
                                ejbPersonaFacade.create(perExamen);
                            } else {
                                perExamen.setActivo((short) 1);
                                perExamen.setAudLogin("WEB");
                                perExamen.setAudNumIp(JsfUtil.getNumIP());
                                perExamen.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
                                perExamen = (CitaTurPersona) JsfUtil.entidadMayusculas(perExamen, "");
                                ejbPersonaFacade.create(perExamen);
                            }
                        } else {
                            perExamen = ejbPersonaFacade.buscarPersonaXNroDoc(loginController.getUsuario().getNumDoc().trim()).get(0);
                        }
                    } else{ //PER_JUR
                        if (ejbPersonaFacade.buscarPersonaXNroRuc(loginController.getUsuario().getNumDoc().trim()).isEmpty()) {
                               if (perExamen.getTipoDoc().getCodProg().equals("TP_DOCID_RUC")) {
                                   //WEB SERVICE PARA CREAR PERSONA DESDE RENIEC
                                   perExamen = wsPideController.buscarSunat(loginController.getUsuario().getNumDoc().trim());
                                   perExamen = (CitaTurPersona) JsfUtil.entidadMayusculas(perExamen, "");
                                   ejbPersonaFacade.create(perExamen);
                               } else {
                                   perExamen.setActivo((short) 1);
                                   perExamen.setAudLogin("WEB");
                                   perExamen.setAudNumIp(JsfUtil.getNumIP());
                                   perExamen.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_JUR"));
                                   perExamen = (CitaTurPersona) JsfUtil.entidadMayusculas(perExamen, "");
                                   ejbPersonaFacade.create(perExamen);
                               }
                        } else {
                            perExamen = ejbPersonaFacade.buscarPersonaXNroRuc(loginController.getUsuario().getNumDoc().trim()).get(0);
                        }
                    }
                    
                    perPago=perExamen;
                    registro.setPerPagoId(perExamen);
                    registro.setPerExamenId(perExamen);
                    registro.setActivo((short) 1);
                    registro.setAudLoginTurno(JsfUtil.getLoggedUser().getLogin());
                    registro.setAudNumIpTurno(JsfUtil.getNumIP());
                    registro.setEstado(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_EST_PEN"));
                    registro.setFechaPrograma(new Date());
                    registro.setTipoTramiteId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_TRAM_TD"));

                    List<CitaTurTurno> lstTurno = ejbFacade.listarTurnosXFechaTurno(registro.getFechaTurno(), registro.getProgramacionId().getId(), registro.getTipoSedeId().getId());
                    if (lstTurno.isEmpty()) {
                        registro.setNroTurno(1);
                    } else {
                        registro.setNroTurno(lstTurno.size() + 1);
                    }
                    //registro.setNroTurno(ejbTurProgramacionFacade.lstTurProgramacion().get(0).getCantCupos() + 1 - registro.getProgramacionId().getCantCupos());                    
                    registro.setVezTurno(1);

                    //CLAVE CONSULTA
                    Long numero = ejbSbNumeracionFacade.selectNumero("TP_NUM_PC");

                    String claveTemp;
                    int inicio = 1;
                    int fin = 5;

                    do {
                        String hash = JsfUtil.crearHash("" + numero);

                        claveTemp = StringUtils.substring(hash, inicio, fin).toUpperCase();
                        inicio++;
                        fin++;
                        if (inicio >= 11) {
                            inicio = 1;
                            fin = 5;
                            numero = ejbSbNumeracionFacade.selectNumero("TP_NUM_PC");
                        }
                    } while (!ejbFacade.listarTurnosXNroDocYNroConsulta(perExamen.getNumDoc(), claveTemp).isEmpty());

                    registro.setClaveConsulta(claveTemp);

                    List<CitaSbTurnoExpediente> turExpedienteList = new ArrayList();
                    //EXPEDIENTES
                    for (SbExpVirtualSolicitud solExp : listaSolExpedientesSelected) {
                        CitaSbTurnoExpediente turExpediente = new CitaSbTurnoExpediente();
                        turExpediente.setActivo((short) 1);
                        turExpediente.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        turExpediente.setAudNumIp(JsfUtil.getNumIP());
                        turExpediente.setTurnoId(registro);
                        turExpediente.setNroExpediente(solExp.getNroExpediente());
                        turExpediente.setExpVirtualSolicitudId(solExp);

                        turExpedienteList.add(turExpediente);
                    }
                    registro.setSbTurnoExpedienteList(turExpedienteList);

                    registro = (CitaTurTurno) JsfUtil.entidadMayusculas(registro, "");
                    ejbFacade.create(registro);

                    if (registro.getId() != null) {
                        JsfUtil.addSuccessMessage("Cita reservada correctamente");
                        siguiente("paso5");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleCitas").getString("ErrorDePersistenciaPoligono"));

                    inicializar();
                }
            }
        }
    }

    public void reprogramarTurno() {
        if (validaCaptchaTC()) {
            if (validarReprogramacion()) {
                turnoRep = ejbFacade.listarTurnosXNroDocYNroConsulta(registro.getPerExamenId().getNumDoc(), registro.getClaveConsulta()).get(0);

                //programando nuevo cita
                turnoRep.setId(null);
                turnoRep.setFechaTurno(JsfUtil.getFechaSinHora(registro.getFechaTurno()));
                turnoRep.setFechaPrograma(new Date());
                turnoRep.setNroTurno(ejbTurProgramacionFacade.lstTurProgramacion().get(0).getCantCupos() + 1 - registro.getProgramacionId().getCantCupos());
                turnoRep.setProgramacionId(registro.getProgramacionId());
                if (registro.getNroReprogramacion() == null) {
                    registro.setNroReprogramacion(0);
                }
                turnoRep.setNroReprogramacion(registro.getNroReprogramacion() + 1);
                turnoRep.setTurnoId(registro.getTurnoId());

                //COMENTAR
                CitaSbNumeracion numero = ejbSbNumeracionFacade.selectNumeroXcodProg("TP_NUM_PC").get(0);

                String claveTemp;
                int inicio = 1;
                int fin = 5;

                do {
                    claveTemp = StringUtils.substring(JsfUtil.crearHash("" + numero.getValor()), inicio, fin).toUpperCase();
                    inicio++;
                    fin++;
                } while (!ejbFacade.buscarTurnoXClaveConsulta(claveTemp).isEmpty());

                registro.setClaveConsulta(claveTemp);

                turnoRep = (CitaTurTurno) JsfUtil.entidadMayusculas(turnoRep, "");
                ejbFacade.create(turnoRep);

                numero.setValor(numero.getValor() + 1);
                ejbSbNumeracionFacade.edit(numero);

                registro = ejbFacade.listarTurnosXId(registro.getId()).get(0);
                //desactivando cita antiguo
                registro.setActivo((short) 0);
                registro.setEstado(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_EST_CAN"));

                registro = (CitaTurTurno) JsfUtil.entidadMayusculas(registro, "");
                ejbFacade.edit(registro);

                registro = turnoRep;

                if (registro.getId() != null) {
                    JsfUtil.addSuccessMessage("Cita reprogramada correctamente");
                    siguiente("paso4");
                }
            }
        }
    }

    public boolean validarReprogramacion() {
        boolean flagValidar = true;
        turnoRep = ejbFacade.listarTurnosXNroDocYNroConsulta(registro.getPerExamenId().getNumDoc(), registro.getClaveConsulta()).get(0);
        hoy = new Date();
        int nroReps = 0;

        Date hace10Dias;
        Calendar cal = Calendar.getInstance();
        cal.setTime(hoy);
        cal.add(Calendar.DAY_OF_MONTH, -10);
        hace10Dias = cal.getTime();

        for (CitaTurTurno tt : ejbFacade.listarTurnosReprogramadosUlt10D(hace10Dias, turnoRep.getPerExamenId())) {
            nroReps = nroReps + tt.getNroReprogramacion();
        }
        if (nroReps == 2) {
            JsfUtil.addWarningMessage("Usted ya ha hecho 2 reprogramaciones en los últimos 10 días, no puede hacer uso de otra Reprogramación");
            flagValidar = false;
        }
//        long daysBetween = TimeUnit.MILLISECONDS.toDays(turnoRep.getFechaTurno().compareTo(hoy));
        if (JsfUtil.getFechaSinHora(turnoRep.getFechaTurno()).compareTo(JsfUtil.getFechaSinHora(hoy)) == 0) {

            String horaTurno = turnoRep.getProgramacionId().getHora();
            String horaT = "";
            String minutoT = "";
            int contador = 0;
            for (int i = 0; i < horaTurno.length(); i++) {
                String caracter = "" + horaTurno.charAt(i);
                if (caracter.equals(":")) {
                    contador = 1;
                }
                if (contador == 0) {
                    horaT = horaT + horaTurno.charAt(i);
                }
                if (contador == 1 && !caracter.equals(":") && !caracter.equals("-")) {
                    minutoT = minutoT + horaTurno.charAt(i);
                }
                if (caracter.equals("-")) {
                    break;
                }
            }
            Date hoyD = new Date();
            int horaHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 4));
            int minHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 5));

            int horaInt = Integer.parseInt(horaT);
            int minutoInt = Integer.parseInt(minutoT);

            if (horaInt - horaHoy == 1) {
                JsfUtil.addWarningMessage("Las reprogramaciones deben ser antes de 2 horas de la cita programada o posteriores a la cita, no se puede reprogramar la cita");
                flagValidar = false;
            } else if (horaInt - horaHoy == 2 && minutoInt != 0) {
                if (minutoInt - minHoy < 0) {
                    JsfUtil.addWarningMessage("Las reprogramaciones deben ser antes de 2 horas de la cita programada o posteriores a la cita, no se puede reprogramar la cita");
                    flagValidar = false;
                }
            } else if (horaInt - horaHoy == 0 && minutoInt != 0) {
                if (minutoInt - minHoy > 0) {
                    JsfUtil.addWarningMessage("Las reprogramaciones deben ser antes de 2 horas de la cita programada o posteriores a la cita, no se puede reprogramar la cita");
                    flagValidar = false;
                }
            }

        }
        if (flagValidar == true) {
            for (CitaTurTurno tt : ejbFacade.listarTurnosXPersonaYDia(registro.getPerExamenId().getId(), JsfUtil.getFechaSinHora(registro.getFechaTurno()))) {
                if (JsfUtil.getFechaSinHora(registro.getFechaTurno()).compareTo(JsfUtil.getFechaSinHora(tt.getFechaTurno())) == 0
                        || JsfUtil.getFechaSinHora(registro.getFechaTurno()).toString().equals(JsfUtil.getFechaSinHora(tt.getFechaTurno()))) {
                    if (!Objects.equals(registro.getId(), tt.getId())) {
                        flagValidar = false;
                    }
                }
            }
            if (!flagValidar) {
                JsfUtil.addWarningMessage("Ya tiene una cita programada en el mismo día");
            }

            List<CitaTurTurno> lstTurno = ejbFacade.listarTurnosXFechaTurno(registro.getFechaTurno(), registro.getProgramacionId().getId(), registro.getTipoSedeId().getId());
            CitaTurProgramacion progra = ejbTurProgramacionFacade.buscarTurProgramacionXId(registro.getProgramacionId().getId());
            if (lstTurno.size() >= progra.getCantCupos()) {
                JsfUtil.addWarningMessage("Los cupos para el turno seleccionado ya han sido ocupados, seleccione otro horario");
                flagValidar = false;
                cargarTurnosDisponibles();
            }
        }
        return flagValidar;
    }

    public void restaurarReprogramacion() {
        registro = new CitaTurTurno();
        turnoRep = new CitaTurTurno();
        listaComprobantes = new ArrayList();
        fechaTurno = null;
        listaProgramaciones = new ArrayList();
        nroConsulta = null;
        nroDoc = null;
        tipoTramite = null;
        nroRuc = null;
        visibleNroRuc = false;
        fechaTurnoAnt = null;
        terminos = false;

        paso = "paso1";
    }

    public void dialogReturnTurno() {
        if (registro.getId() != null) {
            JsfUtil.addSuccessMessage("Cita reservada correctamente");
            siguiente("paso6");
        }
    }

    public void dialogReturnReprogramaTurno() {
        if (registro.getId() != null) {
            JsfUtil.addSuccessMessage("Cita reprogramada correctamente");
            siguiente("paso4");
        }
    }

    public boolean validarCreate() {
        boolean flagValidar = true;
        if (registro.getFechaTurno() != null) {
            hoy = new Date();
            if (JsfUtil.getFechaSinHora(registro.getFechaTurno()) == JsfUtil.getFechaSinHora(hoy)) {
                String horaTurno = registro.getProgramacionId().getHora();
                String horaT = "";
                String minutoT = "";
                int contador = 0;
                for (int i = 0; i < horaTurno.length(); i++) {
                    String caracter = "" + horaTurno.charAt(i);
                    if (caracter.equals(":")) {
                        contador = 1;
                    }
                    if (contador == 0) {
                        horaT = horaTurno.charAt(i) + horaT;
                    }
                    if (contador == 1) {
                        minutoT = horaTurno.charAt(i) + minutoT;
                    }
                    if (caracter.equals("-")) {
                        break;
                    }
                }
                if (hoy.getHours() < Integer.parseInt(horaT)) {
                    JsfUtil.addWarningMessage("La hora seleccionada para la Cita ya ha pasado");
                    flagValidar = false;
                } else if (hoy.getHours() == Integer.parseInt(horaT)) {
                    if (hoy.getMinutes() < Integer.parseInt(minutoT)) {
                        JsfUtil.addWarningMessage("La hora seleccionada para la Cita ya ha pasado");
                        flagValidar = false;
                    }
                }
            }
        }

        if (flagValidar == true) {
            List<CitaTurTurno> lstTurno = ejbFacade.listarTurnosXFechaTurno(registro.getFechaTurno(), registro.getProgramacionId().getId(), registro.getTipoSedeId().getId());
            CitaTurProgramacion progra = ejbTurProgramacionFacade.buscarTurProgramacionXId(registro.getProgramacionId().getId());
            if (lstTurno.size() >= progra.getCantCupos()) {
                JsfUtil.addWarningMessage("Los cupos para el turno seleccionado ya han sido ocupados, seleccione otro horario");
                flagValidar = false;
                cargarTurnosDisponibles();
            }
        }
        return flagValidar;
    }

    public void restaurarProgramacion() {
        registro = new CitaTurTurno();
        tipoLic = new CitaTurConstancia();
        listaProgramaciones = new ArrayList();
        listaTipoLicencia = new ArrayList();
//        codVerificacion = null;
        perExamen = new CitaTurPersona();
        perPago = new CitaTurPersona();

        cargarTasasTupa();
        borrarValoresRecibo();
        reciboBn = null;
        setDisableReciboBn(false);
        limpiarComprobante();
        listaComprobantes = new ArrayList();
        //variables
        listaCalibres = new ArrayList();
        visibleCalibre = false;
        visibleNroRuc = false;
//        visibleCodVer = false;
        visibleNroDocVal = false;
        visibleAgrComprobante = false;
//        visibleTipoTram = false;
        terminos = false;

        paso = "paso1";
        visibleSiguiente3 = false;
        modalidadesSelected = new ArrayList();
        visibleMultimodal = false;

        visibleCalendarZonal = false;
        visibleCalendarCentral = false;
        visibleCalendarTacna = false;
        rucRznSocial = nroRuc = rznSocial = "";

        setOpeLicIni(false);
        setOpeLicRen(false);
        licencia = null;
        listaLicencia = new ArrayList<>();
        licenciaModalidad = "";

        perExamen = new CitaTurPersona();
        perPago = new CitaTurPersona();
        if (loginController.getUsuario().getPersona().getTipoId().getCodProg().equals("TP_PER_JUR")) {
            perExamen.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_RUC"));
            perExamen.setRuc(loginController.getUsuario().getPersona().getRuc());
            perExamen.setRznSocial(loginController.getUsuario().getPersona().getRznSocial());
        } else {
            perExamen.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_DNI"));
            perExamen.setNumDoc(loginController.getUsuario().getNumDoc().trim());
            perExamen.setApePat(loginController.getUsuario().getApePat());
            perExamen.setApeMat(loginController.getUsuario().getApeMat());
            perExamen.setNombres(loginController.getUsuario().getNombres());

            try {
                SbPersona per = ejbSbPersona.findByNumDoc(loginController.getUsuario().getNumDoc().trim());
                perExamen.setNumDocVal(per.getNumDocVal());
            } catch (Exception e) {
            }
        }
    }

    public void restaurarVerificacion() {
//        codVerificacion = null;
        perExamen = new CitaTurPersona();
        perPago = new CitaTurPersona();

        registro.setTipoTramiteId(null);

        limpiarComprobante();
        listaComprobantes = new ArrayList();
        //variables
        listaCalibres = new ArrayList();
        visibleCalibre = false;
        visibleNroRuc = false;
//        visibleCodVer = false;
        visibleNroDocVal = false;
        visibleAgrComprobante = false;

        paso = "paso1";
        visibleSiguiente3 = false;
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public CitaTurTurno getTurTurno(Long id) {
        return ejbFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public CitasTramiteController() {
        
        estado = EstadoCrud.BUSCAR;
    }

    @PostConstruct
    public void inicializar() {
        restaurarProgramacion();
        //restaurarReprogramacion();
        registro = new CitaTurTurno();
        registro.setAudLogin(loginController.getUsuario().getLogin());
        registro.setAudNumIp(JsfUtil.getNumIP());
        registro.setActivo((short) 1);

        turComprobante = new CitaTurComprobante();
        turComprobante.setBancoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_BCO_NAC"));
        turComprobante.setAudLogin(loginController.getUsuario().getLogin());
        turComprobante.setAudNumIp(JsfUtil.getNumIP());

        //perExamen = new CitaTurPersona();
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = CitaTurTurno.class)
    public static class TurTurnoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CitasTramiteController c = (CitasTramiteController) JsfUtil.obtenerBean("citasPoligonoController", CitasTramiteController.class);
            return c.getTurTurno(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CitaTurTurno) {
                CitaTurTurno o = (CitaTurTurno) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + CitaTurTurno.class.getName());
            }
        }
    }

    //MSALINAS
    //WIZARD
    public String onFlowProcess(FlowEvent event) {
        String current = event.getOldStep();
        String next = event.getNewStep();

        boolean proceed = true;
        if (current.equals("paso1") && next.equals("paso2") && (registro.getProgramacionId() == null)) {
            //proceed only when data was selected and user is moving to the next step
            JsfUtil.addErrorMessage("Debe seleccionar la hora de la cita");
            proceed = false;
        }
        if (current.equals("paso3") && next.equals("paso4")) {
            if (listaComprobantes.isEmpty()) {
                JsfUtil.addErrorMessage("Debe ingresar al menos un arma para la cita; ingrese los datos del arma y el comprobante de pago y haga click en el botón \"Agregar\"");
                proceed = false;
            } else {
                String hora = registro.getProgramacionId().getHora();
                Integer horaInt;
                horaInt = Integer.parseInt(hora.substring(0, 2));
//                String car1 = hora.substring(0, 1);
                String min;
//                if (car1.equals("1")) {
                horaProgra = hora.substring(0, 5);
                horaTolera = hora.substring(0, 4) + "5";
                min = hora.substring(3, 5);
//                } else {
//                    horaProgra = hora.substring(0, 4);
//                    horaTolera = hora.substring(0, 3) + "5";
//                    min = hora.substring(2, 4);
//                }
                switch (min) {
                    case "00":
                        horaInt = horaInt - 1;
                        horaPresen = horaInt + ":45";
                        break;
                    case "15":
                        horaPresen = horaInt + ":00";
                        break;
                    default:
                        horaPresen = horaInt + ":15";
                        break;
                }
            }
        }
        return proceed ? next : current;
    }

    public void siguiente(String p) {
        RequestContext context = RequestContext.getCurrentInstance();

        switch (p) {
            case "paso12":
                validaPaso1();

                context.update("tabGestion:creaCitaTramForm");
                //personas
                //perPago = perExamen;
                perPago = new CitaTurPersona();
                perExamen = new CitaTurPersona();
        
                listaSolExpedientes = new ArrayList();
                listaSolExpedientes = ejbSbExpVirtualSolicitudFacade.listarSolicitudesXCriterios("", "nroSolicitud", loginController.getUsuario().getPersona().getId(), "TP_EXVIEST_CRE",registro.getTipoSedeId().getId());
               
                break;
            case "paso34":
                validaPaso3();
                break;
            case "paso43":
                paso = "paso3";
                break;
            default:
                paso = p;
                break;
        }
    }

    public void validaPaso1() {
        Date fecha1 = JsfUtil.getFechaSinHora(fechaProcess);
        Date fecha2 = JsfUtil.getFechaSinHora(JsfUtil.getFechaSinHora(registro.getFechaTurno()));

        Date fecha3 = JsfUtil.getFechaSinHora(maxFecha);

        if (fecha3.compareTo(fecha2) < 0) {
            JsfUtil.addErrorMessage("El turno seleccionado no es válido");
        } else if ((registro.getProgramacionId() == null)) {
            JsfUtil.addErrorMessage("Debe seleccionar la hora de la cita");
        } else if (!Objects.equals(fecha1, fecha2)) {
            JsfUtil.addErrorMessage("El turno seleccionado no es válido");
        } else if (!Objects.equals(registro.getProgramacionId().getSedeId(), registro.getTipoSedeId())) {
            JsfUtil.addErrorMessage("El turno seleccionado no es válido");
            registro.setTipoSedeId(null);
            registro.setFechaTurno(null);
            listaProgramaciones = new ArrayList();
//        } else if (perExamen.getNumDoc() != null) {
//            if (perExamen.getNumDoc().length() >= 8) {
//
//                // ACTIVAR SIN VALIDAR CITAS PENDIENTES
//                visibleSiguiente3 = true;
//                paso = "paso3";                
//            } else {
//                JsfUtil.addErrorMessage("El Número de documento no es válido.");
//            }
        } else {
            paso = "paso3";
        }
    }

    public void validaPaso3() {
        if (registro.getSubTramiteId().getCodProg().equals("TP_TRAM_REQ")) {
            if (listaSolExpedientesSelected.size()>0) {
                irPaso3();
            }else
                JsfUtil.mensajeError("Seleccione al menos una solicitud");
        } else {
            irPaso3();
        }
    }
    public void irPaso3(){
        String horaProg = registro.getProgramacionId().getHora();
        Integer horaInt;
        String min;

        horaProgra = horaProg.substring(0, 5);
        horaTolera = horaProg.substring(0, 4) + "5";
        min = horaProg.substring(3, 5);
        horaInt = Integer.parseInt(horaProg.substring(0, 2));

        switch (min) {
            case "00":
                horaInt = horaInt - 1;
                horaPresen = horaInt + ":45";
                break;
            case "15":
                horaPresen = horaInt + ":00";
                break;
            default:
                horaPresen = horaInt + ":15";
                break;
        }
        captcha = new Captcha();
        terminos = false;
        paso = "paso4";

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("tabGestion:creaCitaTramForm");
    }

    public boolean validaPersona() {
        if (ejbCitaTurPersonaFacade.buscarPersonaXNroDoc(perPago.getNumDoc()).isEmpty()) {
            perPago = wsPideController.buscarReniec(perPago.getNumDoc());
            if (perPago.getApePat() == null) {
                return false;
            }
            perPago = (CitaTurPersona) JsfUtil.entidadMayusculas(perPago, "");
            ejbPersonaFacade.create(perPago);
        } else {
            perPago = ejbCitaTurPersonaFacade.buscarPersonaXNroDoc(perPago.getNumDoc()).get(0);
        }
        return true;
    }

    public void onRowSelectHora(SelectEvent event) {
        if (event != null) {
            //CitaTurProgramacion turP = (CitaTurProgramacion) event.getObject();
            CitaHora turH = (CitaHora) event.getObject();
            if (mostrarSelectorHora(turH)) {
                CitaTurProgramacion turP = ejbTurProgramacionFacade.find(turH.getId());
                registro.setProgramacionId(turP);
            } else {
                registro.setProgramacionId(null);
            }
        }
    }

    public boolean validarVoucherUso(Long secuencia, BigDecimal importe, Date fecha) {
        Date hoy = new Date();
        hoy = JsfUtil.getFechaSinHora(fecha);
        if (ejbFacade.listarTurnosXVoucher(secuencia, importe, fecha).isEmpty()) {
            return false;
        }
        CitaTurTurno tur = ejbFacade.listarTurnosXVoucher(secuencia, importe, fecha).get(0);
        Date fechaTur = JsfUtil.getFechaSinHora(tur.getFechaTurno());
        if (tur.getEstado().getCodProg().equals("TP_EST_CON")) {
            return true;
        } else if (hoy.compareTo(fechaTur) > 0) {
            return false;
        } else if (hoy.compareTo(fechaTur) < 0) {
            return true;
        }

        String horaTurno = tur.getProgramacionId().getHora();
        String horaT = "";
        String minutoT = "";
        int contador = 0;
        for (int i = 0; i < horaTurno.length(); i++) {
            String caracter = "" + horaTurno.charAt(i);
            if (caracter.equals(":")) {
                contador = 1;
            }
            if (contador == 0) {
                horaT = horaT + horaTurno.charAt(i);
            }
            if (contador == 1 && !caracter.equals(":") && !caracter.equals("-")) {
                minutoT = minutoT + horaTurno.charAt(i);
            }
            if (caracter.equals("-")) {
                break;
            }
        }
        Date hoyD = new Date();
        int horaHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 4));
        int minHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 5));

        if (horaHoy > Integer.parseInt(horaT)) {
            //LLEGO TARDE
            return false;
        } else if (horaHoy == Integer.parseInt(horaT)) {
            if (minHoy - Integer.parseInt(minutoT) > 0) {
                //LLEGO TARDE
                return false;
            } else if (minutoT.equals("30")) {
                if (Integer.parseInt(minutoT) - minHoy > 0) {
                    //LLEGO MUY TEMPRANO
                    return true;
                } else {
                    //LLEGO A TIEMPO
                    return false;
                }
            } else {
                //LLEGO A TIEMPO
                return false;
            }
        } else if (minutoT.equals("30")) {
            //LLEGO MUY TEMPRANO
            return true;
        } else if (Integer.parseInt(horaT) - horaHoy > 1) {
            //LLEGO MUY TEMPRANO
            return true;
        } else if (minHoy - Integer.parseInt(minutoT) >= 45) {
            //LLEGO A TIEMPO
            return false;
        } else {
            //LLEGO MUY TEMPRANO
            return true;
        }
    }

    public boolean validarVoucherTupa2018() {
        Date fecHoy = new Date();
        //fecHoy = JsfUtil.getFechaSinHora(fecha);
        boolean res = false;
        boolean validaDia = true;
        boolean validaHora = false;
        if (solicitarRecibo || solicitarVoucherTemp) {
            List<CitaTurTurno> lstTurnosVoucher = new ArrayList<>();
            if (solicitarRecibo) {
                lstTurnosVoucher = ejbFacade.listarTurnosXVoucherCta(reciboBn.getNroSecuencia(), BigDecimal.valueOf(reciboBn.getImporte()), reciboBn.getFechaMovimiento());
            } else if (solicitarVoucherTemp) {
                lstTurnosVoucher = ejbFacade.listarTurnosXVoucherCta(registro.getSecuenciaEmpoce(), registro.getMontoEmpoce(), registro.getFechaEmpoce());
            }

            CitaTurTurno tur = null;
            if (lstTurnosVoucher.isEmpty()) {
                res = false;
                validaDia = false;
            } else {
                tur = lstTurnosVoucher.get(0);
                for (CitaTurConstancia consta : tur.getTurConstanciaList()) {
                    if (consta.getComprobanteId().getNroExpediente() != null) {
                        res = true;
                        validaDia = false;
                        break;
                    } else {
                        res = false;
                    }
                }
            }

            if (validaDia) {
                if (tur != null) {
                    Date fechaTur = JsfUtil.getFechaSinHora(tur.getFechaTurno());
                    if (tur.getEstado().getCodProg().equals("TP_EST_CON")) {
                        res = true;
                    } else if (fecHoy.compareTo(fechaTur) > 0) {
                        res = false;
                    } else if (fecHoy.compareTo(fechaTur) < 0) {
                        res = true;
                    } else {
                        validaHora = true;
                    }

                    if (validaHora) {
                        String horaTurno = tur.getProgramacionId().getHora();
                        String horaT = "";
                        String minutoT = "";
                        int contador = 0;
                        for (int i = 0; i < horaTurno.length(); i++) {
                            String caracter = "" + horaTurno.charAt(i);
                            if (caracter.equals(":")) {
                                contador = 1;
                            }
                            if (contador == 0) {
                                horaT = horaT + horaTurno.charAt(i);
                            }
                            if (contador == 1 && !caracter.equals(":") && !caracter.equals("-")) {
                                minutoT = minutoT + horaTurno.charAt(i);
                            }
                            if (caracter.equals("-")) {
                                break;
                            }
                        }
                        Date hoyD = new Date();
                        int horaHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 4));
                        int minHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 5));

                        if (horaHoy > Integer.parseInt(horaT)) {
                            //LLEGO TARDE
                            res = false;
                        } else if (horaHoy == Integer.parseInt(horaT)) {
                            if (minHoy - Integer.parseInt(minutoT) > 0) {
                                //LLEGO TARDE
                                res = false;
                            } else if (minutoT.equals("30")) {
                                if (Integer.parseInt(minutoT) - minHoy > 0) {
                                    //LLEGO MUY TEMPRANO
                                    res = true;
                                } else {
                                    //LLEGO A TIEMPO
                                    res = false;
                                }
                            } else {
                                //LLEGO A TIEMPO
                                res = false;
                            }
                        } else if (minutoT.equals("30")) {
                            //LLEGO MUY TEMPRANO
                            res = true;
                        } else if (Integer.parseInt(horaT) - horaHoy > 1) {
                            //LLEGO MUY TEMPRANO
                            res = true;
                        } else if (minHoy - Integer.parseInt(minutoT) >= 45) {
                            //LLEGO A TIEMPO
                            res = false;
                        } else {
                            //LLEGO MUY TEMPRANO
                            res = true;
                        }
                    }

                    /*if (!res) {
                        if (solicitarRecibo) {
                            for (CitaTurConstancia consta : tur.getTurConstanciaList()) {
                                //Syso("consta: " + consta.getComprobanteId().getId() + " / " + tur.getProgramacionId().getHora());
                                if (consta.getComprobanteId().getVoucherCta().equals(reciboBn.getId().toString())) {
                                    res = true;
                                    break;
                                }
                            }                                    
                        }                    
                    }*/
                }
            }

        } else {
            res = true;
        }
        return res;
    }

    public boolean validarReciboElegido() {
        boolean res = false;
        if (reciboBn != null) {
            if (registro.getTipoTramiteId() != null) {
                HashMap mMap = new HashMap();
                mMap.put("tipo", null);
                mMap.put("importe", parametrizacionTupa.getImporte());
                mMap.put("codigo", parametrizacionTupa.getCodTributo());
                mMap.put("reciboId", reciboBn.getId());
                mMap.put("nroSecuencia", reciboBn.getNroSecuencia());

                switch (registro.getTipoTramiteId().getCodProg()) {
                    case "TP_TRA_DEF":
                    case "TP_TRA_CAZ":
                    case "TP_TRA_DEP":
                    case "TP_TRA_COL":
                    case "TP_TRA_VP":
                        mMap.put("numDoc", (perExamen.getNumDoc() != null) ? perExamen.getNumDoc() : perExamen.getRuc());
                        break;
                    case "TP_TRA_SIS":
                        mMap.put("numDoc", perPago.getRuc());
                        break;
                }
                //Syso("Param " + parametrizacionTupa.getImporte() + " / " + parametrizacionTupa.getCodTributo() + " / " + mMap.get("numDoc"));
                //Syso("Recibo " + reciboBn.getId() + " / " + reciboBn.getNroSecuencia());
                List<SbRecibos> lstUniv = sbRecibosFacade.validaRecibosByFiltro(mMap);
                if (lstUniv != null) {
                    if (!lstUniv.isEmpty()) {
                        res = true;
                    } else {
                        res = false;
                    }
                } else {
                    res = false;
                }
            } else {
                JsfUtil.mensajeAdvertencia("Seleccione tipo de Licencia!");
                res = false;
            }

        }

        return res;
    }

    public boolean validarCertSalud(CitaTurTurno reg, CitaTurPersona perExa) {
        boolean res = false;
        List<String> sList = Arrays.asList("NINGUNA");  // MODALIDADES SIN CERT SALUD
        if (!sList.contains(reg.getTipoTramiteId().getCodProg())) {
            SbCsCertifsalud certSalud = ejbSbCsCertifsaludFacade.obtenerCertificadosXSolicitante(perExa.getNumDoc().trim(), reg.getFechaTurno());
            if (certSalud != null) {
                if (certSalud.getCondicionObtenidaId().getCodProg().equals("TP_COB_APT")) {
                    res = true;
                } else {
                    res = false;
                }
            } else {
                res = false;
            }
        } else {
            res = false;
        }

        return res;
    }

    /**
     * Funcion para validar si administrado tiene citas pendientes o examen
     * rendido
     *
     * @return boolean
     */
    public boolean validarCitasPendientes() {

        boolean sinPendiente = false;
        visibleSiguiente3 = true;
        List<CitaTurTurno> ttList = ejbFacade.listarTurnosXNroDocXTipo(perExamen.getNumDoc(), "POL");

        boolean existeProgramado = false;
        boolean existeTomado = false;
        Date hoyD = new Date();

        for (CitaTurTurno tt : ttList) {
            //VALIDAR QUE LA PERSONA NO TENGA OTRAS CITAS PENDIENTES
            String horaTur = tt.getProgramacionId().getHora();
            Date fechaTur = JsfUtil.getFechaSinHora(tt.getFechaTurno());
            Date fechaHoy = JsfUtil.getFechaSinHora(hoyD);

            //Syso("fecha hora Turno: " + fechaTurnoVal);
            if (!existeProgramado) {
                if (!tt.getEstado().getCodProg().equals("TP_EST_CON") && !tt.getEstado().getCodProg().equals("TP_ESTUR_CON")) {
                    if (fechaTur.compareTo(fechaHoy) > 0) {
                        existeProgramado = true;
                    } else if (fechaTur.compareTo(fechaHoy) == 0) {
                        existeProgramado = validarHorario(tt);
                    }
                } else {
                    for (CitaTurConstancia tcon : tt.getTurConstanciaList()) {
                        if (tcon.getResulGral() != null && tcon.getActivo() == 1 && tcon.getResulGral().getCodProg().equals("TP_RES_DES")) {
                            //Calendar c = Calendar.getInstance();
                            //c.setTime(fechaTur);
                            //c.add(Calendar.DATE, 2);  // number of days to add
                            //Date fechaTurLimite = c.getTime();  // dt is now the new date

                            //if (fechaTurLimite.compareTo(registro.getFechaTurno()) >= 0) {
                            //if (fechaTurLimite.compareTo(tcon.getTurnoId().getFechaTurno()) >= 0) {
                            //existeTomado = true;
                            //}
                            fechaHoy = JsfUtil.getFechaConHora(hoyD);
                            String fechaTurnoVal = JsfUtil.dateToString(fechaTur, "yyyyMMdd") + " " + horaTur.substring(0, 5) + ":00";
                            fechaTur = JsfUtil.stringToDate(fechaTurnoVal, "yyyyMMdd HH:mm:ss");
                            //Syso("fecha hora Turno: " + fechaTur);
                            Calendar c = Calendar.getInstance();
                            c.setTime(fechaTur);
                            c.add(Calendar.HOUR_OF_DAY, 48);  //  adds 48 hour
                            Date fechaTurLimite = c.getTime();  // dt is now the new date

                            //Syso("fecha hora Límte: " + fechaTurLimite);
                            //Syso("fecha hora hoy: " + fechaHoy);
                            if (fechaTurLimite.compareTo(fechaHoy) >= 0) {
                                existeTomado = true;
                            }

                        }
                    }

                }
            }
        }
        if (existeProgramado) {
            JsfUtil.addErrorMessage("Ya existe un turno registrado para la misma persona");
            //paso = "paso2";
            sinPendiente = false;
        } else if (existeTomado) {
            JsfUtil.addErrorMessage("No esta permitido reservar una cita con fecha anterior a las 48 horas de rendido el examen (Directiva N°022-2017-SUCAMEC numeral 6.8.2)");
            //paso = "paso2";
            sinPendiente = false;
        } else {
            //paso = "paso3";
            sinPendiente = true;
        }

        return sinPendiente;
    }

    /**
     * Funcion para validar si administrado tiene citas pendientes x MODALIDAD o
     * examen rendido
     *
     * @return boolean
     */
    public boolean validarCitasPendientesxModalidad() {

        boolean sinPendiente = false;
        visibleSiguiente3 = true;
        List<CitaTurTurno> ttList = ejbFacade.listarTurnosXNroDocXTipo(perExamen.getNumDoc(), "POL");

        boolean existeProgramado = false;
        boolean existeTomado = false;
        Date hoyD = new Date();

        for (CitaTurTurno tt : ttList) {
            //VALIDAR QUE LA PERSONA NO TENGA OTRAS CITAS PENDIENTES
            String horaTur = tt.getProgramacionId().getHora();
            Date fechaTur = JsfUtil.getFechaSinHora(tt.getFechaTurno());
            Date fechaHoy = JsfUtil.getFechaSinHora(hoyD);

            //Syso("fecha hora Turno: " + fechaTurnoVal);
            if (tt.getTipoTramiteId().equals(registro.getTipoTramiteId())) {
                if (!existeProgramado) {
                    if (!tt.getEstado().getCodProg().equals("TP_EST_CON") && !tt.getEstado().getCodProg().equals("TP_ESTUR_CON")) {
                        if (fechaTur.compareTo(fechaHoy) > 0) {
                            existeProgramado = true;
                        } else if (fechaTur.compareTo(fechaHoy) == 0) {
                            existeProgramado = validarHorario(tt);
                        }
                    } else {
                        for (CitaTurConstancia tcon : tt.getTurConstanciaList()) {
                            if (tcon.getResulGral() != null && tcon.getActivo() == 1 && tcon.getResulGral().getCodProg().equals("TP_RES_DES")) {
                                //Calendar c = Calendar.getInstance();
                                //c.setTime(fechaTur);
                                //c.add(Calendar.DATE, 2);  // number of days to add
                                //Date fechaTurLimite = c.getTime();  // dt is now the new date

                                //if (fechaTurLimite.compareTo(registro.getFechaTurno()) >= 0) {
                                //if (fechaTurLimite.compareTo(tcon.getTurnoId().getFechaTurno()) >= 0) {
                                //existeTomado = true;
                                //}
                                fechaHoy = JsfUtil.getFechaConHora(hoyD);
                                String fechaTurnoVal = JsfUtil.dateToString(fechaTur, "yyyyMMdd") + " " + horaTur.substring(0, 5) + ":00";
                                fechaTur = JsfUtil.stringToDate(fechaTurnoVal, "yyyyMMdd HH:mm:ss");
                                //Syso("fecha hora Turno: " + fechaTur);
                                Calendar c = Calendar.getInstance();
                                c.setTime(fechaTur);
                                c.add(Calendar.HOUR_OF_DAY, 48);  //  adds 48 hour
                                Date fechaTurLimite = c.getTime();  // dt is now the new date

                                //Syso("fecha hora Límte: " + fechaTurLimite);
                                //Syso("fecha hora hoy: " + fechaHoy);
                                if (fechaTurLimite.compareTo(fechaHoy) >= 0) {
                                    existeTomado = true;
                                }

                            }
                        }

                    }
                }
            }
        }
        if (existeProgramado) {
            JsfUtil.addErrorMessage("Ya existe un turno registrado para la misma Persona y tipo de Licencia");
            //paso = "paso2";
            sinPendiente = false;
        } else if (existeTomado) {
            JsfUtil.addErrorMessage("No esta permitido reservar una cita con fecha anterior a las 48 horas de rendido el examen (Directiva N°022-2017-SUCAMEC numeral 6.8.2)");
            //paso = "paso2";
            sinPendiente = false;
        } else {
            //paso = "paso3";
            sinPendiente = true;
        }

        return sinPendiente;
    }

    /**
     * Funcion para validar si se ingreso ARMAS en la lista Modalidades
     *
     * @return boolean
     */
    public boolean validarArmasConstancia() {
        List<String> sList = Arrays.asList("TP_TRA_VP", "TP_TRA_SIS");
        boolean armasValidadas = false;
        boolean hayArmas;
        String errorArmas = "";
        if (sList.contains(registro.getTipoTramiteId().getCodProg())) {
            //# Si es "TP_TRA_VP", "TP_TRA_SIS" debe validar por lo menos un arma#
            hayArmas = false;
            errorArmas = "Debe selecconar por lo menos un Arma!";
            for (CitaTurConstancia row : listaTipoLicencia) {
                if (row.getArmaTeoId() != null) {
                    hayArmas = true;
                }
            }
        } else {
            //# Si NO es "TP_TRA_VP", "TP_TRA_SIS" debe validar todas las armas#
            hayArmas = true;
            errorArmas = "Debe seleccionar el arma para cada modalidad!";
            for (CitaTurConstancia row : listaTipoLicencia) {
                if (row.getArmaTeoId() == null) {
                    hayArmas = false;
                }
            }
        }

        if (hayArmas) {
            armasValidadas = true;
        } else {
            JsfUtil.addWarningMessage(errorArmas);
        }

        return armasValidadas;
    }

    /**
     * Función para validar si la persona vigilante tiene Licencia o Examen de
     * tiro vigente
     *
     * @return boolean
     */
    public boolean validarLicenciaExamen() {
        boolean tieneLicExa = false;
        List<CitaTurTurno> ttList = ejbFacade.listarTurnosXNroDocXTipo(perExamen.getNumDoc(), "POL");
        List<AmaLicenciaDeUso> luList = ejbAmaLicenciaDeUsoFacade.listarAmaLicenciaVigenteXPersonaId(perExamen.getNumDoc());

        boolean modalidadConLic = false;
        boolean modalidadAprobadaVig = false;
        Date hoyD = new Date();
        for (AmaLicenciaDeUso alu : luList) {
            for (AmaTipoLicencia atl : alu.getAmaTipoLicenciaList()) {
                if (atl.getActivo() == 1) {
                    //# VALIDACION DE estado "ANULADO POR OFICIO" "NULIDAD" #//
                    boolean validaMod = true;
                    if (atl.getEstadoId() != null) {
                        List<String> sList = Arrays.asList("TP_EST_ANU_OFI", "TP_EST_NULIDAD", "TP_EST_ANU");
                        if (sList.contains(atl.getEstadoId().getCodProg())) {
                            //if (atl.getEstadoId().getCodProg().equals("TP_EST_ANU_OFI") || atl.getEstadoId().getCodProg().equals("TP_EST_NULIDAD")) {
                            validaMod = false;
                        }
                    }
                    //# #//
                    if (validaMod) {
                        String modPoligono = "";
                        switch (atl.getModalidadId().getCodProg()) {
                            case "TP_MOD_CAZ":
                                modPoligono = "TP_TRA_CAZ";
                                break;
                            case "TP_MOD_DEF":
                                modPoligono = "TP_TRA_DEF";
                                break;
                            case "TP_MOD_DEP":
                                modPoligono = "TP_TRA_DEP";
                                break;
                            case "TP_MOD_SIS":
                                modPoligono = "TP_TRA_SIS";
                                break;
                            case "TP_MOD_SEG":
                                modPoligono = "TP_TRA_VP";
                                break;
                            case "TP_MOD_COL":
                                modPoligono = "TP_TRA_COL";
                                break;
                        }
                        if (Objects.equals(registro.getTipoTramiteId().getCodProg(), "TP_TRA_MUL")) {
                            for (CitaTipoGamac mod : modalidadesSelected) {
                                if (Objects.equals(mod.getCodProg(), modPoligono)) {
                                    modalidadConLic = true;
                                }
                            }
                        } else if (Objects.equals(registro.getTipoTramiteId().getCodProg(), modPoligono)) {
                            modalidadConLic = true;
                        }
                    }
                }
            }
        }

        //# Reserva de cita para la modalidad de "Defensa Personal" con constancia aprobada con diferente serie #//
        boolean licAproDefPer = false;
        if (registro.getTipoTramiteId().getCodProg().equals("TP_TRA_DEF")) { //TP_TRA_DEP
            licAproDefPer = true;
        }
        //# #//
        for (CitaTurTurno tt : ttList) {
            for (CitaTurConstancia tCons : tt.getTurConstanciaList()) {
                if (tCons.getActivo() == 1 && tCons.getFechaVencimiento() != null) {
                    if (Objects.equals(tCons.getResulGral().getCodProg(), "TP_RES_APR") && tCons.getFechaVencimiento().compareTo(hoyD) > 0) {
                        if (Objects.equals(registro.getTipoTramiteId().getCodProg(), "TP_TRA_MUL")) {
                            for (CitaTipoGamac mod : modalidadesSelected) {
                                if (Objects.equals(mod.getCodProg(), tCons.getModalidadId().getCodProg())) {
                                    modalidadAprobadaVig = true;
                                    if (licAproDefPer) {
                                        for (CitaTurConstancia cTCons : listaTipoLicencia) {
                                            if (cTCons.getTipoArmaId().getId().equals(tCons.getTipoArmaId().getId())) {
                                                modalidadAprobadaVig = false;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (Objects.equals(tCons.getModalidadId().getCodProg(), registro.getTipoTramiteId().getCodProg())) {
                            modalidadAprobadaVig = true;
                            if (licAproDefPer) {
                                for (CitaTurConstancia cTCons : listaTipoLicencia) {
                                    if (cTCons.getTipoArmaId() == null || tCons.getTipoArmaId() == null) {
                                        modalidadAprobadaVig = false;
                                        //break;
                                    } else if (cTCons.getTipoArmaId().getId().equals(tCons.getTipoArmaId().getId())) {
                                        modalidadAprobadaVig = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //Syso("modalidadAprobadaVig: " + modalidadAprobadaVig);
        if (modalidadConLic && isOpeLicIni()) { // && !registro.getTipoTramiteId().getCodProg().equals("TP_TRA_DEF")
            JsfUtil.addErrorMessage("Usted ya cuenta con una licencia de uso para la modalidad seleccionada");
            paso = "paso3";
            tieneLicExa = true;
            // COMENTTAR | DESCOMENTAR PARA VALIDAR EXAMEN DE TIRO APROBADO
            /*
            } else if (modalidadAprobadaVig) {
            JsfUtil.addErrorMessage("Usted cuenta con un examen de tiro aprobado y vigente para la modalidad seleccionada");
            paso = "paso3";
            tieneLicExa = true;
             */
        }

        if (!tieneLicExa) {
            return true;
        } else {
            return false;
        }

    }

    public void cargarTurnosDisponibles() {
        try {
            fechaProcess = registro.getFechaTurno();
            listaProgramaciones = new ArrayList();
            if (registro.getSubTramiteId() != null) {
                String codProgSubTram = "";
                switch (registro.getSubTramiteId().getCodProg()) {
                    case "TP_TRAM_REC":
                        codProgSubTram = "TP_PROG_REC";
                        break;
                    case "TP_TRAM_VEF":
                        codProgSubTram = "TP_PROG_VEN";
                        break;
                    case "TP_TRAM_OTR":
                        codProgSubTram = "TP_TRAM_OTR";
                    break;
                    case "TP_TRAM_REQ":
                        codProgSubTram = "TP_TRAM_REQ";
                    break;
                    default:
                        break;
                }
                codProgSubTram="TP_PROG_TD";                
                listaProgramaciones = ejbTurProgramacionFacade.lstTurProgramacionXSedeYSubTramite(registro.getTipoSedeId().getId(), codProgSubTram);
            } else {
                JsfUtil.mensajeError("Debe seleccionar un tipo de sub trámite");
                return;
            }

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateInString = "26/07/2019";
            Date fechaFormateada = formatter.parse(dateInString);

            listaHoras = new ArrayList();
            boolean medioDia = false;
            if (Objects.equals(JsfUtil.getFechaSinHora(registro.getFechaTurno()), JsfUtil.getFechaSinHora(fechaFormateada))) {
                medioDia = true;
            }
            for (CitaTurProgramacion tp : listaProgramaciones) {
                List<CitaTurTurno> lstTurno = new ArrayList();
                lstTurno = ejbFacade.listarTurnosXFechaTurno(registro.getFechaTurno(), tp.getId(), registro.getTipoSedeId().getId());
                CitaHora cHora = new CitaHora();
                cHora.setId(tp.getId());
                cHora.setHora(tp.getHora());
                //tp.setCantCupos(tp.getCantCupos() - lstTurno.size());
                cHora.setCantCupos(tp.getCantCupos());
                cHora.setCantCuposAct(tp.getCantCupos() - lstTurno.size());
                cHora.setSedeId(tp.getSedeId());
                listaHoras.add(cHora);
                if (medioDia) {
                    if (tp.getHora().trim().equals("11:30-12:00")) {
                        break;
                    }
                }
            }

        } catch (ParseException ex) {
            Logger.getLogger(CitasTramiteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean mostrarSelectorHora(CitaHora item) {
        boolean flagValido = true;
        List<CitaTurTurno> lstTurno = new ArrayList();
        if (item != null) {
            if (item.getId() != null) {
                //item = ejbTurProgramacionFacade.buscarTurProgramacionXId(item.getId());
                lstTurno = ejbFacade.listarTurnosXFechaTurno(registro.getFechaTurno(), item.getId(), registro.getTipoSedeId().getId());
                long canCupos = item.getCantCupos() - lstTurno.size();
                item.setCantCuposAct(item.getCantCupos() - lstTurno.size());
                if (canCupos <= 0) {
                    flagValido = false;
                }
                if (flagValido) {
                    Date hoyD = new Date();
                    if (JsfUtil.getFechaSinHora(registro.getFechaTurno()).compareTo(JsfUtil.getFechaSinHora(hoyD)) == 0) {
                        String horaTurno = item.getHora();
                        String horaT = "";
                        String minutoT = "";
                        int contador = 0;
                        for (int i = 0; i < horaTurno.length(); i++) {
                            String caracter = "" + horaTurno.charAt(i);
                            if (caracter.equals(":")) {
                                contador = 1;
                            }
                            if (contador == 0) {
                                horaT = horaT + horaTurno.charAt(i);
                            }
                            if (contador == 1 && !caracter.equals(":") && !caracter.equals("-")) {
                                minutoT = minutoT + horaTurno.charAt(i);
                            }
                            if (caracter.equals("-")) {
                                break;
                            }
                        }
                        int horaTInt = Integer.parseInt(horaT);
                        int minutoTInt = Integer.parseInt(minutoT);
                        int horaHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 4));
                        int minutoHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 5));
                        if (horaHoy - horaTInt > 0) {
                            flagValido = false;
                        } else if (horaHoy - horaTInt == 0) {
                            if (minutoHoy - minutoTInt > 0) {
                                flagValido = false;
                            }
                        }
                    }
                }
            }
        }
        return flagValido;
    }

    public boolean mostrarSelector(CitaTurProgramacion item) {
        boolean flagValido = true;
        List<CitaTurTurno> lstTurno = new ArrayList();
        if (item != null) {
            if (item.getId() != null) {
                try {
                    //                if(item.getCantCupos() == 0){
//                    return false;
//                }

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String dateInString = "10/11/2017";
                    Date fechaFormateada = formatter.parse(dateInString);

                    if (registro.getTipoSedeId().getCodProg().equals("TP_AREA_TRAM")
                            && Objects.equals(JsfUtil.getFechaSinHora(registro.getFechaTurno()), JsfUtil.getFechaSinHora(fechaFormateada))) {
                        if (item.getId() == 12
                                || item.getId() == 13
                                || item.getId() == 14
                                || item.getId() == 15
                                || item.getId() == 16
                                || item.getId() == 18) {
                            return false;
                        }
                    }

                    item = ejbTurProgramacionFacade.buscarTurProgramacionXId(item.getId());
                    lstTurno = ejbFacade.listarTurnosXFechaTurno(registro.getFechaTurno(), item.getId(), registro.getTipoSedeId().getId());
                    long canCupos = item.getCantCupos() - lstTurno.size();
                    //item.setCantCupos(item.getCantCupos() - lstTurno.size());
                    if (canCupos <= 0) {
                        flagValido = false;
                    }
                    if (flagValido) {
                        Date hoyD = new Date();
                        if (JsfUtil.getFechaSinHora(registro.getFechaTurno()).compareTo(JsfUtil.getFechaSinHora(hoyD)) == 0) {
                            String horaTurno = item.getHora();
                            String horaT = "";
                            String minutoT = "";
                            int contador = 0;
                            for (int i = 0; i < horaTurno.length(); i++) {
                                String caracter = "" + horaTurno.charAt(i);
                                if (caracter.equals(":")) {
                                    contador = 1;
                                }
                                if (contador == 0) {
                                    horaT = horaT + horaTurno.charAt(i);
                                }
                                if (contador == 1 && !caracter.equals(":") && !caracter.equals("-")) {
                                    minutoT = minutoT + horaTurno.charAt(i);
                                }
                                if (caracter.equals("-")) {
                                    break;
                                }
                            }
                            int horaTInt = Integer.parseInt(horaT);
                            int minutoTInt = Integer.parseInt(minutoT);
                            int horaHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 4));
                            int minutoHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 5));
                            if (horaHoy - horaTInt > 0) {
                                flagValido = false;
                            } else if (horaHoy - horaTInt == 0) {
                                if (minutoHoy - minutoTInt > 0) {
                                    flagValido = false;
                                }
                            }
                        }
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(CitasTramiteController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return flagValido;
    }

    public String mostrarProgramarTurno() {
        restaurarProgramacion();
        registro = new CitaTurTurno();
        registro.setAudLogin(loginController.getUsuario().getLogin());
        registro.setAudNumIp(JsfUtil.getNumIP());
        registro.setActivo((short) 1);

        tipoLic = new CitaTurConstancia();
        //perExamen = new CitaTurPersona();
        //perPago = new CitaTurPersona();

        return "/aplicacion/citas/poligono/ProgramarTurno";
    }

    public String mostrarReprogramarTurno() {
//        restaurarProgramacion();
        return "/aplicacion/turTurno/ReprogramarTurno";
    }

    public String mostrarConfirmarTurno() {
        restaurarProgramacion();
        return "/aplicacion/turTurno/ConfirmarTurno";
    }

    public String mostrarListadoTurnos() {
        restaurarProgramacion();
        return "/aplicacion/turTurno/ListarProgramacionTurno";
    }

    public String mostrarReporteTurnos() {
        restaurarProgramacion();
        return "/aplicacion/turTurno/ListarTurnoCC";
    }

    public void cargarNroDocVal() {
        if (perExamen.getTipoDoc() != null) {
            perExamen.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXId(perExamen.getTipoDoc().getId()));
            visibleNroDocVal = perExamen.getTipoDoc().getCodProg().equals("TP_DOCID_DNI");
            switch (perExamen.getTipoDoc().getCodProg()) {
                case "TP_DOCID_DNI":
                    maxNroDoc = 8;
                    placeholder = "A";
                    break;
                case "TP_DOCID_CE":
                    maxNroDoc = 9;
                    placeholder = "";
                    break;
                case "TP_DOCID_PAS":
                    maxNroDoc = 20;
                    placeholder = "";
                    break;
            }
        } else {
            visibleNroDocVal = false;
        }
        perExamen.setNumDoc(null);
        perExamen.setNumDocVal(null);
    }

    public void cargarNroRuc() {
        visibleNroRuc = false;
        visibleMultimodal = false;
        if (registro.getTipoTramiteId() != null) {
            registro.setTipoTramiteId(ejbTipoGamacFacade.buscarTipoGamacXId(registro.getTipoTramiteId().getId()));
            if (registro.getTipoTramiteId().getCodProg().equals("TP_TRA_VP") || registro.getTipoTramiteId().getCodProg().equals("TP_TRA_SIS")) {
                visibleNroRuc = true;
            } else if (registro.getTipoTramiteId().getCodProg().equals("TP_TRA_MUL")) {
                visibleMultimodal = true;
            }
        }
        modalidadesSelected = null;
        listaComprobantes = new ArrayList();
        perPago = new CitaTurPersona();
    }

    public void selectTipoLicencia() {
        visibleNroRuc = false;
        visibleMultimodal = false;
        if (registro.getTipoTramiteId() != null) {
            borrarReciboSeleccionado();
            registro.setTipoTramiteId(ejbTipoGamacFacade.buscarTipoGamacXId(registro.getTipoTramiteId().getId()));
            if (registro.getTipoTramiteId().getCodProg().equals("TP_TRA_VP") || registro.getTipoTramiteId().getCodProg().equals("TP_TRA_SIS")) {
                visibleNroRuc = true;
            } else if (registro.getTipoTramiteId().getCodProg().equals("TP_TRA_MUL")) {
                visibleMultimodal = true;
            }

            switch (registro.getTipoSedeId().getCodProg()) {
                case "TP_AREA_GAMAC":
                case "TP_AREA_TRAM":
                case "TP_AREA_OGTIC":
                    parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaIdByTipoProceso("TP_TUP_SUC1", registro.getTipoTramiteId().getCodProg());  // TUPA 2018, Proceso                
                    break;
                default:
                    parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaIdByTipoProceso("TP_TUP_SUC1", registro.getTipoTramiteId().getCodProg() + "_OD");  // TUPA 2018, Proceso                
                    break;
            }

        }
        modalidadesSelected = null;
        listaComprobantes = new ArrayList();
        rucRznSocial = "";
        perPago = new CitaTurPersona();

        /* DETALLE TIPO LICENCIA */
        // TP_TRA_DEF | TP_TRA_PP | TP_TRA_TDV | TP_TRA_SIS | TP_TRA_VP | TP_TRA_CAZ | TP_TRA_DEP | TP_TRA_COL | TP_TRA_MUL
        listaTipoLicencia = new ArrayList();
        tipoLic = new CitaTurConstancia();

        if (registro.getTipoTramiteId() != null) {
            switch (registro.getTipoTramiteId().getCodProg()) {
                case "TP_TRA_DEF":
                case "TP_TRA_CAZ":
                case "TP_TRA_DEP":
                case "TP_TRA_COL":
                    tipoLic.setTurnoId(registro);
                    tipoLic.setModalidadId(registro.getTipoTramiteId());
                    listaTipoLicencia.add(tipoLic);
                    break;
                case "TP_TRA_VP":
                case "TP_TRA_SIS":
                    tipoLic.setTurnoId(registro);
                    tipoLic.setModalidadId(registro.getTipoTramiteId());
                    tipoLic.setTipoArmaId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_TIPO_AR_COR"));
                    listaTipoLicencia.add(tipoLic);
                    tipoLic = new CitaTurConstancia();
                    tipoLic.setTurnoId(registro);
                    tipoLic.setModalidadId(registro.getTipoTramiteId());
                    tipoLic.setTipoArmaId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_TIPO_AR_LAR"));
                    listaTipoLicencia.add(tipoLic);
                    break;
            }
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("$(\".contRuc, .contSRuc\").addClass(\"ui-helper-hidden\")");
        context.execute("$(\".contNroLic\").addClass(\"ui-helper-hidden\")");

        if (isVisibleNroRuc()) {
            context.execute("$(\".contRuc\").removeClass(\"ui-helper-hidden\")");
        }
        if (isVisibleMultimodal()) {
            context.execute("$(\".contSRuc\").removeClass(\"ui-helper-hidden\")");
        }
    }

    public void ActionEvent() {
        //Syso("multimodal: " + modalidadesSelected);
        listaTipoLicencia = new ArrayList();
        for (CitaTipoGamac row : modalidadesSelected) {
            tipoLic = new CitaTurConstancia();
            tipoLic.setTurnoId(registro);
            tipoLic.setModalidadId(row);
            listaTipoLicencia.add(tipoLic);
        }
    }

    public void buscarRuc() {
        //Syso("Buscando ruc...");
    }

    public void cargarCodVerificacion() {
        listaComprobantes = new ArrayList();
        visibleNroRuc = false;
        visibleAgrComprobante = true;
//        visibleTipoTram = false;
        registro.setTipoTramiteId(null);
//        codVerificacion = null;

        if (perExamen.getNumDoc() != null) {
            if (perExamen.getNumDoc().length() >= 8) {
//                visibleCodVer = false;
                visibleSiguiente3 = false;
                if (perExamen.getTipoDoc().getCodProg().equals("TP_DOCID_DNI")
                        && !JsfUtil.ValidarDniRuc(perExamen.getNumDoc() + perExamen.getNumDocVal(), "DNI")) {
                    JsfUtil.addErrorMessage("El DNI no es válido.");
                    paso = "paso2";
                } else if (perExamen.getTipoDoc().getCodProg().equals("TP_DOCID_CE")
                        && perExamen.getNumDoc().length() != 9) {
                    JsfUtil.addErrorMessage("El Carnet de Extranjería no es válido.");
                    paso = "paso2";
                } else {
//                    visibleTipoTram = true;
                    visibleSiguiente3 = true;
                    List<CitaTurTurno> ttList = ejbFacade.listarTurnosXNroDocXTipo(perExamen.getNumDoc(), "POL");

                    boolean existeProgramado = false;
                    boolean existeTomado = false;
                    Date hoyD = new Date();

                    for (CitaTurTurno tt : ttList) {
                        //VALIDAR QUE LA PERSONA NO TENGA OTRAS CITAS PENDIENTES
                        Date fechaTur = JsfUtil.getFechaSinHora(tt.getFechaTurno());
                        Date fechaHoy = JsfUtil.getFechaSinHora(hoyD);
                        if (!existeProgramado) {
                            if (!tt.getEstado().getCodProg().equals("TP_EST_CON") && !tt.getEstado().getCodProg().equals("TP_ESTUR_CON")) {
                                if (fechaTur.compareTo(fechaHoy) > 0) {
                                    existeProgramado = true;
                                } else if (fechaTur.compareTo(fechaHoy) == 0) {
                                    existeProgramado = validarHorario(tt);
                                }
                            } else {
                                for (CitaTurConstancia tcon : tt.getTurConstanciaList()) {
                                    if (tcon.getResulGral() != null && tcon.getActivo() == 1 && tcon.getResulGral().getCodProg().equals("TP_RES_DES")) {
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(fechaTur);
                                        c.add(Calendar.DATE, 2);  // number of days to add
                                        Date fechaTurLimite = c.getTime();  // dt is now the new date
                                        if (fechaTurLimite.compareTo(registro.getFechaTurno()) >= 0) {
                                            existeTomado = true;
                                        }
                                    }
                                }

                            }
                        }
                    }
                    if (existeProgramado) {
                        JsfUtil.addErrorMessage("Ya existe un turno registrado para la misma persona");
                        paso = "paso2";
                    } else if (existeTomado) {
                        JsfUtil.addErrorMessage("No esta permitido reservar una cita con fecha anterior a las 48 horas de rendido el examen (Directiva N°022-2017-SUCAMEC numeral 6.8.2)");
                        paso = "paso2";
                    } else {
                        paso = "paso3";
                    }

//                    if (validaJudicial(perExamen.getNumDoc(), null)) {
//                        JsfUtil.mensajeError("Tiene una observación del Poder Judicial apersonarse a la SUCAMEC");
//                        paso = "paso2";
//                    }
                }

            } else {
                JsfUtil.addErrorMessage("El Número de documento no es válido.");
            }
        }
    }

    public boolean validarHorario(CitaTurTurno tur) {
//        Date hoy = new Date();
//        hoy = JsfUtil.getFechaSinHora(fecha);
//        if (ejbFacade.listarTurnosXVoucher(secuencia, importe, fecha).isEmpty()) {
//            return false;
//        }
//        CitaTurTurno tur = ejbFacade.listarTurnosXVoucher(secuencia, importe, fecha).get(0);
//        Date fechaTur = JsfUtil.getFechaSinHora(tur.getFechaTurno());
//        if (tur.getEstado().getCodProg().equals("TP_EST_CON")) {
//            return true;
//        } else if (hoy.compareTo(fechaTur) > 0) {
//            return false;
//        } else if (hoy.compareTo(fechaTur) < 0) {
//            return true;
//        }

        String horaTurno = tur.getProgramacionId().getHora();
        String horaT = "";
        String minutoT = "";
        int contador = 0;
        for (int i = 0; i < horaTurno.length(); i++) {
            String caracter = "" + horaTurno.charAt(i);
            if (caracter.equals(":")) {
                contador = 1;
            }
            if (contador == 0) {
                horaT = horaT + horaTurno.charAt(i);
            }
            if (contador == 1 && !caracter.equals(":") && !caracter.equals("-")) {
                minutoT = minutoT + horaTurno.charAt(i);
            }
            if (caracter.equals("-")) {
                break;
            }
        }
        Date hoyD = new Date();
        int horaHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 4));
        int minHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 5));

        if (horaHoy > Integer.parseInt(horaT)) {
            //LLEGO TARDE
            return false;
        } else if (horaHoy == Integer.parseInt(horaT)) {
            if (minHoy - Integer.parseInt(minutoT) > 0) {
                //LLEGO TARDE
                return false;
            } else if (minutoT.equals("30")) {
                if (Integer.parseInt(minutoT) - minHoy > 0) {
                    //LLEGO MUY TEMPRANO
                    return true;
                } else {
                    //LLEGO A TIEMPO
                    return false;
                }
            } else {
                //LLEGO A TIEMPO
                return false;
            }
        } else if (minutoT.equals("30")) {
            //LLEGO MUY TEMPRANO
            return true;
        } else if (Integer.parseInt(horaT) - horaHoy > 1) {
            //LLEGO MUY TEMPRANO
            return true;
        } else if (minHoy - Integer.parseInt(minutoT) >= 45) {
            //LLEGO A TIEMPO
            return false;
        } else {
            //LLEGO MUY TEMPRANO
            return true;
        }
    }

    //cambio poder judicial
//    public boolean validaJudicial(String nroDoc, String nroExp) {
//        return wsPideController.buscarAntecedentes("ADMIN", nroDoc, nroExp);
//    }
    public void cargarComprobantes() {
//        restaurarVerificacion();
        listaComprobantes = new ArrayList();
        visibleNroRuc = false;
        visibleAgrComprobante = true;
        registro.setTipoTramiteId(null);

//        if (codVerificacion != null) {
//            if (codVerificacion.length() == 5) {
//                codVerificacion = codVerificacion.toUpperCase();
//                if (ejbFacade.listarTurnosXCodVerificacion(codVerificacion).isEmpty()) {
//                    List<TurConstancia> constancias = ejbTurConstanciaFacade.listarConstanciasXCodVerifica(codVerificacion);
//                    if (!constancias.isEmpty()) {
//
//                        Collections.sort(constancias, new Comparator<TurConstancia>() {
//                            @Override
//                            public int compare(TurConstancia p1, TurConstancia p2) {
//                                return (int) (p1.getId() - p2.getId());
//                            }
//
//                        });
//
//                        for (TurConstancia constancia : constancias) {
//
//                            List<CitaTurTurno> ttList = constancia.getComprobanteId().getTurTurnoList();
//
//                            Collections.sort(ttList, new Comparator<CitaTurTurno>() {
//                                @Override
//                                public int compare(CitaTurTurno p1, CitaTurTurno p2) {
//                                    return (int) (p1.getId() - p2.getId());
//                                }
//
//                            });
//
//                            CitaTurTurno tt = ttList.get(ttList.size() - 1);
//                            registro.setTipoTramiteId(tt.getTipoTramiteId());
//                            registro.setVezTurno(tt.getVezTurno());
////                        if (!Objects.equals(tt.getPerExamenId(), tt.getPerPagoId())) {
//                            if (!tt.getTipoTramiteId().getCodProg().equals("TP_TRA_DEF")) {
////                            perPago.setRuc(tt.getPerPagoId().getRuc());
//                                visibleNroRuc = true;
//                            }
//                            perExamen = tt.getPerExamenId();
//                            perPago = tt.getPerPagoId();
//                            listaComprobantes.add(constancia.getComprobanteId());
//                        }
//                        paso = "paso4";
//                        visibleAgrComprobante = false;
//                        JsfUtil.addSuccessMessage("Se cargaron los datos de manera exitosa");
//                    } else {
//                        paso = "paso3";
//                        listaComprobantes = new ArrayList();
//                        visibleAgrComprobante = true;
//                        JsfUtil.addErrorMessage("El código de verificación es incorrecto");
//                    }
//                } else {
//                    paso = "paso3";
//                    JsfUtil.addErrorMessage("El código de verificación ya ha sido usado para generar una cita anteriormente, por favor realice una reprogramación con la clave de consulta");
//                }
//            } else {
//                paso = "paso3";
//                JsfUtil.addErrorMessage("Ingrese un código de verificación válido");
//            }
//        } else {
        paso = "paso3";
        listaComprobantes = new ArrayList();
        visibleAgrComprobante = true;
        JsfUtil.addErrorMessage("Ingrese el código de verificación");
//        }
    }

    public void openDlgVoucher() {
        RequestContext context = RequestContext.getCurrentInstance();
        /*Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("closeOnEscape", false);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 480);
        options.put("contentHeight", 430);
        // Modificar directorio y agregar archivo ListDialog //
        context.openDialog("dlgVoucher.xhtml", options, null);*/
        context.execute("PF('wvDlgHlpVoucher').show();");

    }

    public void openDlgDni() {
        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("closeOnEscape", false);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 570);
        options.put("contentHeight", 440);
        // Modificar directorio y agregar archivo ListDialog //
        context.openDialog("/aplicacion/citas/poligono/dlgDni.xhtml", options, null);
    }

    public void cargarCalibres() {
        if (turComprobante.getTipoArmaId() != null) {
            turComprobante.setTipoArmaId(ejbTipoGamacFacade.buscarTipoGamacXId(turComprobante.getTipoArmaId().getId()));
            visibleCalibre = true;
            listaCalibres = tipoGamacController.getItemsTipoCalibre(turComprobante.getTipoArmaId());
        } else {
            visibleCalibre = false;
            listaCalibres = new ArrayList();
        }
    }

    public void agregarComprobante() {
        if (validarAgrComprobante()) {
//            visibleCodVer = false;
            turComprobante.setId(new Long(listaComprobantes.size() + 1));
            turComprobante.setTipoArmaId(ejbTipoGamacFacade.buscarTipoGamacXId(turComprobante.getTipoArmaId().getId()));
            turComprobante.setCalibreId(ejbTipoGamacFacade.buscarTipoGamacXId(turComprobante.getCalibreId().getId()));
            turComprobante.setBancoId(ejbTipoBaseFacade.buscarTipoBaseXId(turComprobante.getBancoId().getId()));

            turComprobante.setActivo((short) 1);
            turComprobante.setAudLogin(loginController.getUsuario().getLogin());
            turComprobante.setAudLoginTurno(loginController.getUsuario().getLogin());
            turComprobante.setAudNumIp(JsfUtil.getNumIP());
            turComprobante.setAudNumIpTurno(JsfUtil.getNumIP());
            turComprobante.setBancoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_BCO_NAC"));
//            if (turComprobante.getFechaPago() == null) {
//                turComprobante.setFechaPago(new Date());
//            }
//            if (turComprobante.getVoucherTasa() == null || "".equals(turComprobante.getVoucherTasa())) {
//                turComprobante.setVoucherTasa("0");
//            }
//            if (turComprobante.getVoucherAgencia() == null || "".equals(turComprobante.getVoucherAgencia())) {
//                turComprobante.setVoucherAgencia("0");
//            }
//            if (turComprobante.getVoucherAutentica() == null || "".equals(turComprobante.getVoucherAutentica())) {
//                turComprobante.setVoucherAutentica("0");
//            }
//            if (turComprobante.getVoucherCta() == null || "".equals(turComprobante.getVoucherCta())) {
//                turComprobante.setVoucherCta("0");
//            }
//            if (turComprobante.getVoucherSeq() == null || "".equals(turComprobante.getVoucherSeq())) {
//                turComprobante.setVoucherSeq("0");
//            }
            turComprobante = (CitaTurComprobante) JsfUtil.entidadMayusculas(turComprobante, "");
            listaComprobantes.add(turComprobante);
            limpiarComprobante();
        }
    }

    public boolean validarAgrComprobante() {
        boolean flagValidar = true;
//        if (registro.getTipoTramiteId() == null) {
//            JsfUtil.addWarningMessage("Debe ingresar el Tipo de Tramite");
//            flagValidar = false;
//        } else {
        registro.setTipoTramiteId(ejbTipoGamacFacade.buscarTipoGamacXId(registro.getTipoTramiteId().getId()));
//        if (registro.getTipoTramiteId().getCodProg().equals("TP_TRA_DEF")) {
//            if (listaComprobantes.size() == 2) {
//                JsfUtil.addWarningMessage("El límite de armas por cita es de 2 para Defensa Personal");
//                flagValidar = false;
//            }
//        } else {
        if (listaComprobantes.size() == 10) {
            JsfUtil.addWarningMessage("El límite de armas por cita es de 10");// para " + registro.getTipoTramiteId().getNombre());
            flagValidar = false;
        }
//        }
//        }
        if (turComprobante.getTipoArmaId() == null) {
            JsfUtil.addWarningMessage("Debe ingresar el Tipo de Arma");
            flagValidar = false;
        }
//        else {
//            for (TurComprobante tc : listaComprobantes) {
//                if (!Objects.equals(tc.getTipoArmaId(), tipoArma)) {
//                    JsfUtil.addWarningMessage("El tipo de arma debe ser el mismo para todas las armas que registre en el Cita");
//                    flagValidar = false;
//                }
//            }
//        }
        if (turComprobante.getCalibreId() == null) {
            JsfUtil.addWarningMessage("Debe ingresar el Calibre");
            flagValidar = false;
        }
//        else {
//            tipoCalibre = ejbTipoGamacFacade.buscarTipoGamacXId(tipoCalibre.getId());
//            for (TurComprobante comp : listaComprobantes) {
//                if (Objects.equals(comp.getCalibreId(), tipoCalibre)) {
//                    JsfUtil.addWarningMessage("Ya ingresó un arma del mismo calibre");
//                    flagValidar = false;
//                }
//            }
//        }

//NUEVA LEY
//        if (turComprobante.getBancoId() == null) {
//            JsfUtil.addWarningMessage("Debe ingresar el Banco");
//            flagValidar = false;
//        }
//        if (turComprobante.getVoucherTasa().length() == 0) {
//            JsfUtil.addWarningMessage("Debe ingresar el Concepto del Comprobante");
//            flagValidar = false;
//        }
//        if (turComprobante.getVoucherSeq().length() == 0) {
//            JsfUtil.addWarningMessage("Debe ingresar la Secuencia del Comprobante");
//            flagValidar = false;
//        }
//        if (turComprobante.getFechaPago() == null) {
//            JsfUtil.addWarningMessage("Debe ingresar Fecha de Pago del Comprobante");
//            flagValidar = false;
//        }
//        if (turComprobante.getVoucherCta().length() == 0) {
//            JsfUtil.addWarningMessage("Debe ingresar el Código de Cuenta del Comprobante");
//            flagValidar = false;
//        }
//        if (turComprobante.getVoucherAgencia().length() == 0) {
//            JsfUtil.addWarningMessage("Debe ingresar el Código de Agencia del Comprobante");
//            flagValidar = false;
//        }
//        if (turComprobante.getVoucherAutentica().length() == 0) {
//            JsfUtil.addWarningMessage("Debe ingresar el Código de Verificación del Comprobante");
//            flagValidar = false;
//        }
        if (flagValidar == true) {

            /**
             * VALIDACION VOUCHER
             */
////            String compTemp = turComprobante.getVoucherTasa() + turComprobante.getVoucherSeq() + turComprobante.getVoucherCta() + turComprobante.getVoucherAgencia() + turComprobante.getVoucherAutentica();
////            for (TurComprobante tc : listaComprobantes) {
////                if (tc.getComprobanteCompleto().equals(compTemp.toUpperCase())
////                        && (JsfUtil.getFechaSinHora(tc.getFechaPago()).compareTo(JsfUtil.getFechaSinHora(turComprobante.getFechaPago())) == 0)) {
////                    JsfUtil.addWarningMessage("Debe ingresar un comprobante distinto a los ya ingresados");
////                    flagValidar = false;
////                }
////            }
////            if (!ejbTurComprobanteFacade.listarComprobantesXVoucher(turComprobante.getVoucherTasa().toUpperCase(), turComprobante.getVoucherSeq().toUpperCase(), turComprobante.getVoucherCta().toUpperCase(), turComprobante.getVoucherAgencia().toUpperCase(), turComprobante.getVoucherAutentica().toUpperCase()).isEmpty()) {
////                List<TurComprobante> tcomp = ejbTurComprobanteFacade.listarComprobantesXVoucher(turComprobante.getVoucherTasa().toUpperCase(), turComprobante.getVoucherSeq().toUpperCase(), turComprobante.getVoucherCta().toUpperCase(), turComprobante.getVoucherAgencia().toUpperCase(), turComprobante.getVoucherAutentica().toUpperCase());
////                boolean repetido = false;
////                for (TurComprobante tc : tcomp) {
////                    if (JsfUtil.getFechaSinHora(tc.getFechaPago()).compareTo(JsfUtil.getFechaSinHora(turComprobante.getFechaPago())) == 0) {
////                        repetido = true;
////                    }
////                }
////                if (repetido) {
////                    JsfUtil.addWarningMessage("El comprobante ya ha sido usado anteriormente en otra cita, por favor registre una reprogramación o caso contrario ingrese un nuevo voucher de pago con datos válidos");
////                    flagValidar = false;
////                }
////            }
            /**
             * VALIDACION VOUCHER
             */
            registro.setTipoTramiteId(ejbTipoGamacFacade.buscarTipoGamacXId(registro.getTipoTramiteId().getId()));
            turComprobante.setTipoArmaId(ejbTipoGamacFacade.buscarTipoGamacXId(turComprobante.getTipoArmaId().getId()));
//            if (registro.getTipoTramiteId().getCodProg().equals("TP_TRA_DEF") && 
//                    !(turComprobante.getTipoArmaId().getCodProg().equals("TP_ARM_PIS") || turComprobante.getTipoArmaId().getCodProg().equals("TP_ARM_REV"))) {
//                JsfUtil.addWarningMessage("Solamente se permite el uso de pistolas y revolveres para defensa personal");
//                flagValidar = false;
//            }
        }
        return flagValidar;
    }

    public void limpiarComprobante() {
        turComprobante = new CitaTurComprobante();
        turComprobante.setBancoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_BCO_NAC"));
        turComprobante.setAudLogin(loginController.getUsuario().getLogin());
        turComprobante.setAudNumIp(JsfUtil.getNumIP());
        visibleCalibre = false;
        listaCalibres = null;
    }

    public void borrarComprobante(CitaTurComprobante comp) {
        listaComprobantes.remove(comp);
    }

    public void closeDlgVoucher() {
        JsfUtil.ListDialogParams p = new JsfUtil.ListDialogParams();
        RequestContext.getCurrentInstance().closeDialog(p);
    }

    public void closeDlgDni() {
        JsfUtil.ListDialogParams p = new JsfUtil.ListDialogParams();
        RequestContext.getCurrentInstance().closeDialog(p);
    }

    public void buscarTurnoProgramado() {
        if (validarTurnoProgramado()) {
            nroConsulta = nroConsulta.toUpperCase();
            if (!ejbFacade.listarTurnosXNroDocYNroConsulta(nroDoc, nroConsulta).isEmpty()) {
                registro = ejbFacade.listarTurnosXNroDocYNroConsulta(nroDoc, nroConsulta).get(0);
//                if (!Objects.equals(registro.getPerExamenId(), registro.getPerPagoId())) {
                if (!registro.getTipoTramiteId().getCodProg().equals("TP_TRA_DEF")
                        && !registro.getTipoTramiteId().getCodProg().equals("TP_TRA_DEP")
                        && !registro.getTipoTramiteId().getCodProg().equals("TP_TRA_CAZ")) {
                    visibleNroRuc = true;
                }
                fechaTurnoAnt = JsfUtil.obtenerFechaXCriterio(registro.getFechaTurno(), 3)
                        + "/" + JsfUtil.obtenerFechaXCriterio(registro.getFechaTurno(), 2)
                        + "/" + JsfUtil.obtenerFechaXCriterio(registro.getFechaTurno(), 1)
                        + " (" + registro.getProgramacionId().getHora() + ")";
                listaComprobantes = registro.getTurComprobanteList();
                listaProgramaciones = new ArrayList();

                registro.setProgramacionId(null);
                registro.setFechaTurno(null);
            } else {
                JsfUtil.addWarningMessage("No existe cita programada con los datos ingresados");
            }
        }
    }

    public boolean validarTurnoProgramado() {
        boolean flagValidar = true;
        if (nroConsulta == null) {
            JsfUtil.addWarningMessage("Ingrese número de consulta");
            flagValidar = false;
        }

        if (nroDoc == null) {
            JsfUtil.addWarningMessage("Ingrese su DNI/CE");
            flagValidar = false;
        }
        return flagValidar;
    }

    public void buscarTurno() {
        String numD = nroDoc;
        restaurarProgramacion();
        if (numD != null) {
            if (!"".equals(numD)) {
                CitaTurPersona administrado = new CitaTurPersona();
                if (ejbPersonaFacade.buscarPersonaXNroDoc(numD).isEmpty()) {
                    //rojo
                    color = "#ff0000";
                    //LA PERSONA NO ESTA REGISTRADA EN EL SISTEMA
                    JsfUtil.addSuccessMessage("La persona no se encuentra registrada en el sistema");
                } else {
                    administrado = ejbPersonaFacade.buscarPersonaXNroDoc(numD).get(0);
                    if (ejbFacade.listarTurnosXPersonaYDia(administrado.getId(), JsfUtil.getFechaSinHora(hoy)).isEmpty()) {
                        //rojo
                        color = "#ff0000";
                        //LA PERSONA NO TIENE TURNO EN EL DIA
                        JsfUtil.addSuccessMessage("La persona no tiene cita en el día");
                    } else {
                        registro = ejbFacade.listarTurnosXPersonaYDia(administrado.getId(), JsfUtil.getFechaSinHora(hoy)).get(0);
                        String horaTurno = registro.getProgramacionId().getHora();
                        String horaT = "";
                        String minutoT = "";
                        int contador = 0;
                        for (int i = 0; i < horaTurno.length(); i++) {
                            String caracter = "" + horaTurno.charAt(i);
                            if (caracter.equals(":")) {
                                contador = 1;
                            }
                            if (contador == 0) {
                                horaT = horaT + horaTurno.charAt(i);
                            }
                            if (contador == 1 && !caracter.equals(":") && !caracter.equals("-")) {
                                minutoT = minutoT + horaTurno.charAt(i);
                            }
                            if (caracter.equals("-")) {
                                break;
                            }
                        }
                        Date hoyD = new Date();
                        int horaHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 4));
                        int minHoy = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(hoyD, 5));

                        if (!ejbFacade.listarTurnosXPersonaDiaConfirmado(administrado.getId(), JsfUtil.getFechaSinHora(hoyD)).isEmpty()) {
                            //rojo
                            color = "#ff0000";
                            //TURNO YA HA SIDO CONFIRMADO
                            visibleRegistrar = true;
                            hora = horaT + ":" + minutoT;
                            //calculando tolerancia
                            int minutoI = Integer.parseInt(minutoT) + 5;
                            if (minutoI != 5) {
                                tolerancia = horaT + ":" + minutoI;
                            } else {
                                tolerancia = horaT + ":0" + minutoI;
                            }
                            dniCe = administrado.getNumDoc();
                            nomApe = administrado.getNombreCompleto();
                            JsfUtil.addSuccessMessage("La cita ya ha sido confirmada en el día");
                        } else if (horaHoy > Integer.parseInt(horaT)) {
                            //rojo
                            color = "#ff0000";
                            //LLEGO TARDE
                            visibleRegistrar = true;
                            hora = horaT + ":" + minutoT;
                            //calculando tolerancia
                            int minutoI = Integer.parseInt(minutoT) + 5;
                            if (minutoI != 5) {
                                tolerancia = horaT + ":" + minutoI;
                            } else {
                                tolerancia = horaT + ":0" + minutoI;
                            }
                            dniCe = administrado.getNumDoc();
                            nomApe = administrado.getNombreCompleto();
                            JsfUtil.addSuccessMessage("Cita fuera de hora");
                        } else if (horaHoy == Integer.parseInt(horaT)) {
                            if (minHoy - Integer.parseInt(minutoT) > 5) {
                                //rojo
                                color = "#ff0000";
                                //LLEGO TARDE
                                visibleRegistrar = true;
                                hora = horaT + ":" + minutoT;
                                //calculando tolerancia
                                int minutoI = Integer.parseInt(minutoT) + 5;
                                if (minutoI != 5) {
                                    tolerancia = horaT + ":" + minutoI;
                                } else {
                                    tolerancia = horaT + ":0" + minutoI;
                                }
                                dniCe = administrado.getNumDoc();
                                nomApe = administrado.getNombreCompleto();

                                JsfUtil.addSuccessMessage("Cita fuera de hora");
                            } else if (minutoT.equals("30")) {
                                if (Integer.parseInt(minutoT) - minHoy > 15) {
                                    //ambar
                                    color = "#ffff00";
                                    //LLEGO MUY TEMPRANO
                                    visibleRegistrar = true;
                                    hora = horaT + ":" + minutoT;
                                    //calculando tolerancia
                                    int minutoI = Integer.parseInt(minutoT) + 5;
                                    if (minutoI != 5) {
                                        tolerancia = horaT + ":" + minutoI;
                                    } else {
                                        tolerancia = horaT + ":0" + minutoI;
                                    }
                                    dniCe = administrado.getNumDoc();
                                    nomApe = administrado.getNombreCompleto();
                                    JsfUtil.addSuccessMessage("Cita fuera de hora");
                                } else {
                                    //verde
                                    color = "#00cc33";
                                    visibleRegistrar = true;
                                    visibleConfirmar = true;
                                    //LLEGO A TIEMPO
                                    hora = horaT + ":" + minutoT;
                                    //calculando tolerancia
                                    int minutoI = Integer.parseInt(minutoT) + 5;
                                    if (minutoI != 5) {
                                        tolerancia = horaT + ":" + minutoI;
                                    } else {
                                        tolerancia = horaT + ":0" + minutoI;
                                    }
                                    dniCe = administrado.getNumDoc();
                                    nomApe = administrado.getNombreCompleto();
                                }
                            } else {
                                //verde
                                color = "#00cc33";
                                visibleRegistrar = true;
                                visibleConfirmar = true;
                                //LLEGO A TIEMPO
                                hora = horaT + ":" + minutoT;
                                //calculando tolerancia
                                int minutoI = Integer.parseInt(minutoT) + 5;
                                if (minutoI != 5) {
                                    tolerancia = horaT + ":" + minutoI;
                                } else {
                                    tolerancia = horaT + ":0" + minutoI;
                                }
                                dniCe = administrado.getNumDoc();
                                nomApe = administrado.getNombreCompleto();
                            }
                        } else if (minutoT.equals("30")) {
                            //ambar
                            color = "#ffff00";
                            //LLEGO MUY TEMPRANO
                            visibleRegistrar = true;
                            hora = horaT + ":" + minutoT;
                            //calculando tolerancia
                            int minutoI = Integer.parseInt(minutoT) + 5;
                            if (minutoI != 5) {
                                tolerancia = horaT + ":" + minutoI;
                            } else {
                                tolerancia = horaT + ":0" + minutoI;
                            }
                            dniCe = administrado.getNumDoc();
                            nomApe = administrado.getNombreCompleto();
                            JsfUtil.addSuccessMessage("Cita fuera de hora");
                        } else if (Integer.parseInt(horaT) - horaHoy > 1) {
                            //ambar
                            color = "#ffff00";
                            //LLEGO MUY TEMPRANO
                            visibleRegistrar = true;
                            hora = horaT + ":" + minutoT;
                            //calculando tolerancia
                            int minutoI = Integer.parseInt(minutoT) + 5;
                            if (minutoI != 5) {
                                tolerancia = horaT + ":" + minutoI;
                            } else {
                                tolerancia = horaT + ":0" + minutoI;
                            }
                            dniCe = administrado.getNumDoc();
                            nomApe = administrado.getNombreCompleto();
                            JsfUtil.addSuccessMessage("Cita fuera de hora");
                        } else if (minHoy - Integer.parseInt(minutoT) >= 45) {
                            //verde
                            color = "#00cc33";
                            visibleRegistrar = true;
                            visibleConfirmar = true;
                            //LLEGO A TIEMPO
                            hora = horaT + ":" + minutoT;
                            //calculando tolerancia
                            int minutoI = Integer.parseInt(minutoT) + 5;
                            if (minutoI != 5) {
                                tolerancia = horaT + ":" + minutoI;
                            } else {
                                tolerancia = horaT + ":0" + minutoI;
                            }
                            dniCe = administrado.getNumDoc();
                            nomApe = administrado.getNombreCompleto();
                        } else {
                            //ambar
                            color = "#ffff00";
                            //LLEGO MUY TEMPRANO
                            visibleRegistrar = true;
                            hora = horaT + ":" + minutoT;
                            //calculando tolerancia
                            int minutoI = Integer.parseInt(minutoT) + 5;
                            if (minutoI != 5) {
                                tolerancia = horaT + ":" + minutoI;
                            } else {
                                tolerancia = horaT + ":0" + minutoI;
                            }
                            dniCe = administrado.getNumDoc();
                            nomApe = administrado.getNombreCompleto();
                            JsfUtil.addSuccessMessage("Cita fuera de hora");
                        }
                    }
                }
            } else {
                JsfUtil.addWarningMessage("Ingrese un número de documento");
            }
        } else {
            JsfUtil.addWarningMessage("Ingrese un número de documento");
        }
    }

    public void restaurarConfirmacion() {
        hora = null;
        tolerancia = null;
        nroDoc = null;
        nomApe = null;
        visibleRegistrar = false;
        visibleConfirmar = false;
        color = "#FFFFFF";
        registro = new CitaTurTurno();
    }

    public List<CitaTurProgramacion> listarProgramacion() {
        List<CitaTurProgramacion> resultado = ejbTurProgramacionFacade.lstTurProgramacion();
        return ejbTurProgramacionFacade.lstTurProgramacion();
    }

    public void cargarTurnosDia() {
        listaTurnosMap = new ArrayList();
        List<CitaTurTurno> turnos = new ArrayList();
        turnos = ejbFacade.listarTurnosConfirmadosXProgramacionYDia(programacion.getId(), JsfUtil.getFechaSinHora(hoy));
        //List<TurComprobante> comps = new ArrayList();
        for (CitaTurTurno tt : turnos) {
            for (CitaTurComprobante tc : tt.getTurComprobanteList()) {
                boolean visibleFoto = false;
                boolean visibleConstancia = false;
                boolean visibleExaTeoPra = false;
                boolean visibleExaTiro = false;
                boolean visibleEditar = true;
                boolean visibleTomarFoto = true;

                for (CitaTurTurno ttVar : turnos) {
                    if (Objects.equals(tt, ttVar)) {
                        for (CitaTurComprobante tcVar : ttVar.getTurComprobanteList()) {
                            for (CitaTurConstancia tConVar : tcVar.getTurConstanciaList()) {
                                if (Objects.equals(tConVar.getFechaEmision(), JsfUtil.getFechaSinHora(hoy)) && (tConVar.getResulGral() != null)) {
                                    visibleTomarFoto = false;
                                }
                            }
                        }
                    }
                }

                Map mapa = new HashMap();
                mapa.put("comprobante", tc);
                mapa.put("turno", tt);
//                mapa.put("nroVeces", tc.getTurConstanciaList().size() + 1);
                String est = "";
                CitaTurConstancia tcUlt = new CitaTurConstancia();
                int contVeces = 1;
                for (CitaTurConstancia tcon : tc.getTurConstanciaList()) {
                    if (Objects.equals(tcon.getFechaEmision(), JsfUtil.getFechaSinHora(hoy))) {
                        tcUlt = tcon;
                    } else {
                        contVeces = contVeces + 1;
                    }
                    visibleEditar = false;
                }
                mapa.put("nroVeces", contVeces);
                if (tcUlt.getId() != null) {
                    if (tcUlt.getResulGral() != null) {
                        visibleConstancia = true;
                        switch (tcUlt.getResulGral().getCodProg()) {
                            case "TP_RES_APR":
                                est = "APROBADO";
                                break;
                            case "TP_RES_DES":
                                est = "DESAPROBADO";
                                break;
                        }
                    }
//                    visibleFoto = true;
                    visibleExaTeoPra = true;
                    if (tcUlt.getExaTeoPrac() != null) {
                        if (tcUlt.getExaTeoPrac().getCodProg().equals("TP_RES_APR")
                                && (tcUlt.getComprobanteId().getTipoArmaId().getCodProg().equals("TP_ARM_PIS")
                                || tcUlt.getComprobanteId().getTipoArmaId().getCodProg().equals("TP_ARM_REV"))) {
                            visibleExaTiro = true;
                        }
                    }
                    mapa.put("constancia", tcUlt);
                }
                if (tcUlt.getRutaFoto() != null) {
                    visibleFoto = true;
                }
                if (tcUlt.getResulGral() != null) {
//                    visibleTomarFoto = false;
                    visibleExaTeoPra = false;
                    visibleExaTiro = false;
                }
                mapa.put("visibleTomarFoto", visibleTomarFoto);
                mapa.put("visibleFoto", visibleFoto);
                mapa.put("visibleConstancia", visibleConstancia);
                mapa.put("visibleExaTeoPra", visibleExaTeoPra);
                mapa.put("visibleExaTiro", visibleExaTiro);
                mapa.put("visibleEditar", visibleEditar);
                mapa.put("estado", est);
                listaTurnosMap.add(mapa);
            }
        }
    }

    public void mostrarFoto(Map item) {
        CitaTurConstancia tt = (CitaTurConstancia) item.get("constancia");
        fotoImg = getManagefile().FotoDownloadPoli(tt.getRutaFoto());

        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("closeOnEscape", false);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 480);
        options.put("contentHeight", 440);
        context.openDialog("dlgMostrarFoto.xhtml", options, null);
    }

    public String mostrarConstancia(Map item) throws IOException, Exception {
        CitaTurConstancia tc = (CitaTurConstancia) item.get("constancia");

        //BAJAR PDF DEL SERVIDOR
        if (!upFilesCont.findFiles(tc.getId() + ".pdf")) {
            //verRg(p_rau.getRegistro(), true,true);
            mostrarReporte(tc.getId().toString(), tc.getRutaFoto(), tc.getHashQr(), true);
            upFilesCont.PdfDownload(tc.getId() + ".pdf");
        } else {
            //BAJAR PDF DEL SERVIDOR
            //upFilesCont.PdfDownload(p_rau.getRegistro().getId() + ".pdf");
            upFilesCont.PdfDownload(tc.getId() + ".pdf");
        }
        return null;
    }

    /**
     * MOSTRAR REPORTE
     *
     * @param constanciaId
     * @param hash
     * @param p_regenera
     * @throws java.lang.Exception
     */
    public void mostrarReporte(String constanciaId, String rutaFoto, String hash, boolean p_regenera) throws Exception {
        String rutaReporteMaster = "";
        List<Map> constancias = new ArrayList<>();
        constancias = ejbFacade.listadoConstanciaArma(constanciaId);
        rutaReporteMaster = "/aplicacion/turTurno/rep/constArma.jrxml";
        verPdf(constanciaId, rutaFoto, hash, "36", constancias, rutaReporteMaster, p_regenera);
    }

    /**
     * MOSTRAR REPORTE
     *
     * @param constanciaId
     * @param rutaFoto
     * @param hash
     * @param idproceso
     * @param constancias
     * @param rutaReporteMaster
     * @param p_regenera
     * @throws java.lang.Exception
     */
    public void verPdf(String constanciaId, String rutaFoto, String hash, String idproceso, List<Map> constancias, String rutaReporteMaster, boolean p_regenera) throws Exception {
        Connection connection = null;
        connection = JsfUtil.obtenerConexion();
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String rutaImagen = "" + servletContext.getRealPath("/resources/imagenes");
        Map mapa = new HashMap();
        mapa.put("PARAMIDREG", constanciaId);
        mapa.put("P_ID", constanciaId);
        mapa.put("P_HASHQR", hash.length() == 0 ? null : ("https://www.sucamec.gob.pe/sel/faces/qr/resTurPoli.xhtml?h=" + hash));
        mapa.put("P_CONEXION", connection);
        mapa.put("P_IMG_DIR", rutaImagen + "/");
        mapa.put("P_FOTO_DIR", ResourceBundle.getBundle("/BundleCitas").getString("Documentos_pathUpload_foto_poligono") + rutaFoto);
        if (!constancias.isEmpty()) {
            if (p_regenera) {
                JsfUtil.generaPdfNoEncontrado(mapa, rutaReporteMaster, constancias, "ReporteGeneral");
            } else {
                JsfUtil.mostrarReporte(mapa, rutaReporteMaster, constancias, "ReporteGeneral");
            }
        } else {
            JsfUtil.mostrarMensajeDialog("Ocurrio un error al procesar el expediente", 3);
        }
        connection.close();
    }

    public void openTomarFoto(Map item) {
        selectedMap = item;
        fileName = null;
        data = null;
        fotoImg = null;
        fotoGuardada = false;
        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("closeOnEscape", false);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 800);
        options.put("contentHeight", 370);
        context.openDialog("dlgTomarFoto.xhtml", options, null);
    }

    public void openExaTeoPra(Map item) {
        selectedMap = item;
        exaTeoGuardado = false;
        CitaTurComprobante tComp = (CitaTurComprobante) item.get("comprobante");
        nroExamen = tComp.getTurConstanciaList().size();
        CitaTurTurno tt = (CitaTurTurno) item.get("turno");
        selectedNroDoc = tt.getPerExamenId().getNumDoc();
        int cont = 0;
        turConstancia1 = null;
        turConstancia2 = null;
        turConstancia3 = null;
        for (CitaTurConstancia tc : tComp.getTurConstanciaList()) {
            cont = cont + 1;
            if (cont == 1) {
                turConstancia1 = tc;
            } else if (cont == 2) {
                turConstancia2 = tc;
            } else {
                turConstancia3 = tc;
            }
        }

        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("closeOnEscape", false);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 480);
        options.put("contentHeight", 440);
        context.openDialog("dlgExaTeoPra.xhtml", options, null);
    }

    public void guardarFoto() {
        if (fileName != null && data != null) {
            CitaTurComprobante tcomp = (CitaTurComprobante) selectedMap.get("comprobante");
            CitaTurTurno ttur = (CitaTurTurno) selectedMap.get("turno");

            if (tcomp.getTurConstanciaList().isEmpty()) {

                for (Map mapa : listaTurnosMap) {
                    CitaTurTurno ttMap = (CitaTurTurno) mapa.get("turno");
                    CitaTurComprobante tcMap = (CitaTurComprobante) mapa.get("comprobante");

                    if (Objects.equals(ttMap, ttur)) {
                        CitaTurConstancia tc = new CitaTurConstancia();
                        tc.setActivo((short) 1);
                        tc.setFechaEmision(JsfUtil.getFechaSinHora(new Date()));
                        tc.setComprobanteId(tcMap);
                        tc.setRutaFoto(fileName);

                        tc = (CitaTurConstancia) JsfUtil.entidadMayusculas(tc, "");
                        ejbTurConstanciaFacade.create(tc);
                    }
                }

            } else {
                boolean sinConstancia = true;
                for (CitaTurConstancia tcon : tcomp.getTurConstanciaList()) {
                    if (Objects.equals(tcon.getFechaEmision(), JsfUtil.getFechaSinHora(hoy))) {
                        sinConstancia = false;
                    }
                }
                if (sinConstancia) {
                    for (Map mapa : listaTurnosMap) {
                        CitaTurTurno ttMap = (CitaTurTurno) mapa.get("turno");
                        CitaTurComprobante tcMap = (CitaTurComprobante) mapa.get("comprobante");

                        if (Objects.equals(ttMap, ttur)) {
                            CitaTurConstancia tc = new CitaTurConstancia();
                            tc.setActivo((short) 1);
                            tc.setFechaEmision(JsfUtil.getFechaSinHora(new Date()));
                            tc.setComprobanteId(tcMap);
                            tc.setRutaFoto(fileName);

                            tc = (CitaTurConstancia) JsfUtil.entidadMayusculas(tc, "");
                            ejbTurConstanciaFacade.create(tc);

                            //6digitos
                            tc.setCodVerifica(StringUtils.substring(JsfUtil.crearHash("POLIGONO_CONSTANCIA_" + tc.getId()), 1, 6));
                            tc.setHashQr(JsfUtil.crearHash("POLIGONO_CONSTANCIA_" + tc.getId()));

                            tc = (CitaTurConstancia) JsfUtil.entidadMayusculas(tc, "");
                            ejbTurConstanciaFacade.edit(tc);
                        }
                    }
                }
            }
            //guardar foto en repositorio
            getManagefile().uploadFotoPoli(data, fileName);
            fotoGuardada = true;
            cargarTurnosDia();

            JsfUtil.ListDialogParams p = new JsfUtil.ListDialogParams();
            RequestContext.getCurrentInstance().closeDialog(p);
//            JsfUtil.addSuccessMessage("Foto Guardada de manera exitosa");
        } else {
            JsfUtil.addWarningMessage("Tome la foto antes de guardar");
        }
    }

    public void returnDialogFoto() {
        if (fotoGuardada) {
            JsfUtil.addSuccessMessage("Foto Guardada de manera exitosa");
        }
    }

    public void returnDialogExaTeo() {
        if (exaTeoGuardado) {
            JsfUtil.addSuccessMessage("Exámen Teórico/Práctico Guardado de manera exitosa");
        }
    }

    public void returnDialogExaTiro() {
        if (exaTiroGuardado) {
            JsfUtil.addSuccessMessage("Exámen de Tiro Guardado de manera exitosa");
        }
    }

    public void returnDialogEdit() {
        if (edicionGuardada) {
            JsfUtil.addSuccessMessage("Datos editados de manera exitosa");
        }
    }

    public void guardarExaTeoPra() {
        if (validarExaTeoPra()) {
            if (turConstancia3 != null) {
                selectedConstancia = turConstancia3;
            } else if (turConstancia2 != null) {
                selectedConstancia = turConstancia2;
            } else if (turConstancia1 != null) {
                selectedConstancia = turConstancia1;
            }
            Calendar cal = Calendar.getInstance();
            Date fechaVenc;
            cal.setTime(hoy);
            cal.add(Calendar.DAY_OF_MONTH, 87);
            fechaVenc = cal.getTime();
            if (selectedConstancia.getComprobanteId().getTipoArmaId().getCodProg().equals("TP_ARM_CAR")
                    || selectedConstancia.getComprobanteId().getTipoArmaId().getCodProg().equals("TP_ARM_ESC")) {
                selectedConstancia.setResulGral(ejbTipoGamacFacade.buscarTipoGamacXId(selectedConstancia.getExaTeoPrac().getId()));
                if (selectedConstancia.getResulGral().getCodProg().equals("TP_RES_APR")) {
                    selectedConstancia.setFechaVencimiento(fechaVenc);
                }
            } else {
                selectedConstancia.setExaTeoPrac(ejbTipoGamacFacade.buscarTipoGamacXId(selectedConstancia.getExaTeoPrac().getId()));
                if (selectedConstancia.getExaTeoPrac().getCodProg().equals("TP_RES_DES")) {
                    selectedConstancia.setResulGral(selectedConstancia.getExaTeoPrac());

                    String codVer = "";
                    CitaTurTurno tt = (CitaTurTurno) selectedMap.get("turno");
                    int conta = 0;
                    for (CitaTurComprobante tc : tt.getTurComprobanteList()) {
                        for (CitaTurConstancia tCon : tc.getTurConstanciaList()) {
                            if (Objects.equals(selectedConstancia.getComprobanteId(), tc)) {
                                conta = conta + 1;
                            }
                            if (Objects.equals(tCon.getFechaEmision(), JsfUtil.getFechaSinHora(hoy)) && tCon.getCodVerifica() != null) {
                                codVer = tCon.getCodVerifica();
                            }
                        }
                    }
                    if (conta < 2) {
                        if (codVer.equals("")) {
                            selectedConstancia.setCodVerifica(StringUtils.substring(JsfUtil.crearHash("POLIGONO_CONSTANCIA_" + selectedConstancia.getId()), 1, 6));
                        } else {
                            selectedConstancia.setCodVerifica(codVer);
                        }
                    }

                }
            }
            selectedConstancia.setHashQr(JsfUtil.crearHash("POLIGONO_CONSTANCIA_" + selectedConstancia.getId()));

            selectedConstancia = (CitaTurConstancia) JsfUtil.entidadMayusculas(selectedConstancia, "");
            ejbTurConstanciaFacade.edit(selectedConstancia);
            exaTeoGuardado = true;
            //BUSCAR LISTA DENUEVO
            cargarTurnosDia();

            JsfUtil.ListDialogParams p = new JsfUtil.ListDialogParams();
            RequestContext.getCurrentInstance().closeDialog(p);
        }
    }

    public boolean validarExaTeoPra() {
        boolean flagValidar = true;
        if (turConstancia3 != null) {
            if (turConstancia3.getExaTeoPrac() == null) {
                JsfUtil.addWarningMessage("Ingrese la calificación del examen teórico / práctico");
                flagValidar = false;
            }
        } else if (turConstancia2 != null) {
            if (turConstancia2.getExaTeoPrac() == null) {
                JsfUtil.addWarningMessage("Ingrese la calificación del examen teórico / práctico");
                flagValidar = false;
            }
        } else if (turConstancia1 != null) {
            if (turConstancia1.getExaTeoPrac() == null) {
                JsfUtil.addWarningMessage("Ingrese la calificación del examen teórico / práctico");
                flagValidar = false;
            }
        }
        return flagValidar;
    }

    public void openExaTiro(Map item) {
        selectedMap = item;
        exaTiroGuardado = false;
        CitaTurComprobante tComp = (CitaTurComprobante) item.get("comprobante");
        nroExamen = tComp.getTurConstanciaList().size();
        CitaTurTurno tt = (CitaTurTurno) item.get("turno");
        selectedNroDoc = tt.getPerExamenId().getNumDoc();
        int cont = 0;
        turConstancia1 = null;
        turConstancia2 = null;
        turConstancia3 = null;
        for (CitaTurConstancia tc : tComp.getTurConstanciaList()) {
            cont = cont + 1;
            if (cont == 1) {
                turConstancia1 = tc;
            } else if (cont == 2) {
                turConstancia2 = tc;
            } else {
                turConstancia3 = tc;
            }
        }

        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("closeOnEscape", false);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 550);
        options.put("contentHeight", 440);
        context.openDialog("dlgExaTiro.xhtml", options, null);
    }

    public void guardarExaTiro() {
        if (validarExaTiro()) {
            if (turConstancia3 != null) {
                selectedConstancia = turConstancia3;
            } else if (turConstancia2 != null) {
                selectedConstancia = turConstancia2;
            } else if (turConstancia1 != null) {
                selectedConstancia = turConstancia1;
            }
            selectedConstancia.setExaTiro(ejbTipoGamacFacade.buscarTipoGamacXId(selectedConstancia.getExaTiro().getId()));
            selectedConstancia.setResulGral(selectedConstancia.getExaTiro());

            if (selectedConstancia.getResulGral().getCodProg().equals("TP_RES_DES")) {
                selectedConstancia.setCodVerifica(StringUtils.substring(JsfUtil.crearHash("POLIGONO_CONSTANCIA_" + selectedConstancia.getId()), 1, 6));
            } else {
                Calendar cal = Calendar.getInstance();
                Date fechaVenc;
                cal.setTime(hoy);
                cal.add(Calendar.DAY_OF_MONTH, 87);
                fechaVenc = cal.getTime();
                selectedConstancia.setFechaVencimiento(fechaVenc);
            }
            selectedConstancia.setHashQr(JsfUtil.crearHash("POLIGONO_CONSTANCIA_" + selectedConstancia.getId()));

            selectedConstancia = (CitaTurConstancia) JsfUtil.entidadMayusculas(selectedConstancia, "");
            ejbTurConstanciaFacade.edit(selectedConstancia);

            exaTiroGuardado = true;
            //BUSCAR LISTA DENUEVO
            cargarTurnosDia();

            JsfUtil.ListDialogParams p = new JsfUtil.ListDialogParams();
            RequestContext.getCurrentInstance().closeDialog(p);
        }
    }

    public boolean validarExaTiro() {
        boolean flagValidar = true;
        if (turConstancia3 != null) {
            if (turConstancia3.getExaTiro() == null) {
                JsfUtil.addWarningMessage("Ingrese la calificación del examen de tiro");
                flagValidar = false;
            }
            if (turConstancia3.getCantMunicion() == null) {
                JsfUtil.addWarningMessage("Ingrese la cantidad de munición");
                flagValidar = false;
            } else if (turConstancia3.getCantMunicion() > 5) {
                JsfUtil.addWarningMessage("La cantidad de munición debe ser igual o menor a 5");
                flagValidar = false;
            }
            if (turConstancia3.getCantTiro() == null) {
                JsfUtil.addWarningMessage("Ingrese la cantidad de tiros");
                flagValidar = false;
            }
            if (turConstancia3.getCantImpacto() == null) {
                JsfUtil.addWarningMessage("Ingrese la cantidad de impactos");
                flagValidar = false;
            } else if (turConstancia3.getCantImpacto() > 5) {
                JsfUtil.addWarningMessage("La cantidad de impactos debe ser igual o menor a 5");
                flagValidar = false;
            }
            if (turConstancia3.getNota() == null) {
                JsfUtil.addWarningMessage("Ingrese la Nota del examen de tiro");
                flagValidar = false;
            } else if (turConstancia3.getNota() > 50) {
                JsfUtil.addWarningMessage("La nota debe ser igual o menor a 50");
                flagValidar = false;
            }
            if (flagValidar == true) {
                if (turConstancia3.getCantImpacto() > turConstancia3.getCantMunicion()) {
                    JsfUtil.addWarningMessage("La cantidad de impactos no p3uede ser mayor a la cantidad de munición");
                    flagValidar = false;
                }
            }
        } else if (turConstancia2 != null) {
            if (turConstancia2.getExaTiro() == null) {
                JsfUtil.addWarningMessage("Ingrese la calificación del examen de tiro");
                flagValidar = false;
            }
            if (turConstancia2.getCantMunicion() == null) {
                JsfUtil.addWarningMessage("Ingrese la cantidad de munición");
                flagValidar = false;
            } else if (turConstancia2.getCantMunicion() > 5) {
                JsfUtil.addWarningMessage("La cantidad de munición debe ser igual o menor a 5");
                flagValidar = false;
            }
            if (turConstancia2.getCantTiro() == null) {
                JsfUtil.addWarningMessage("Ingrese la cantidad de tiros");
                flagValidar = false;
            }
            if (turConstancia2.getCantImpacto() == null) {
                JsfUtil.addWarningMessage("Ingrese la cantidad de impactos");
                flagValidar = false;
            } else if (turConstancia2.getCantImpacto() > 5) {
                JsfUtil.addWarningMessage("La cantidad de impactos debe ser igual o menor a 5");
                flagValidar = false;
            }
            if (turConstancia2.getNota() == null) {
                JsfUtil.addWarningMessage("Ingrese la Nota del examen de tiro");
                flagValidar = false;
            } else if (turConstancia2.getNota() > 50) {
                JsfUtil.addWarningMessage("La nota debe ser igual o menor a 50");
                flagValidar = false;
            }
            if (flagValidar == true) {
                if (turConstancia2.getCantImpacto() > turConstancia2.getCantMunicion()) {
                    JsfUtil.addWarningMessage("La cantidad de impactos no puede ser mayor a la cantidad de munición");
                    flagValidar = false;
                }
            }
        } else if (turConstancia1 != null) {
            if (turConstancia1.getExaTiro() == null) {
                JsfUtil.addWarningMessage("Ingrese la calificación del examen de tiro");
                flagValidar = false;
            }
            if (turConstancia1.getCantMunicion() == null) {
                JsfUtil.addWarningMessage("Ingrese la cantidad de munición");
                flagValidar = false;
            } else if (turConstancia1.getCantMunicion() > 5) {
                JsfUtil.addWarningMessage("La cantidad de munición debe ser igual o menor a 5");
                flagValidar = false;
            }
            if (turConstancia1.getCantTiro() == null) {
                JsfUtil.addWarningMessage("Ingrese la cantidad de tiros");
                flagValidar = false;
            }
            if (turConstancia1.getCantImpacto() == null) {
                JsfUtil.addWarningMessage("Ingrese la cantidad de impactos");
                flagValidar = false;
            } else if (turConstancia1.getCantImpacto() > 5) {
                JsfUtil.addWarningMessage("La cantidad de impactos debe ser igual o menor a 5");
                flagValidar = false;
            }
            if (turConstancia1.getNota() == null) {
                JsfUtil.addWarningMessage("Ingrese la Nota del examen de tiro");
                flagValidar = false;
            } else if (turConstancia1.getNota() > 50) {
                JsfUtil.addWarningMessage("La nota debe ser igual o menor a 50");
                flagValidar = false;
            }
            if (flagValidar == true) {
                if (turConstancia1.getCantImpacto() > turConstancia1.getCantMunicion()) {
                    JsfUtil.addWarningMessage("La cantidad de impactos no puede ser mayor a la cantidad de munición");
                    flagValidar = false;
                }
            }
        }
        return flagValidar;
    }

    public void openEditar(Map item) {
        selectedMap = item;
        edicionGuardada = false;

        CitaTurComprobante tComp = (CitaTurComprobante) item.get("comprobante");
        CitaTurTurno tt = (CitaTurTurno) item.get("turno");

        visibleNroRuc = false;
        tipoDoc = tt.getPerExamenId().getTipoDoc();
        if (tipoDoc.getCodProg().equals("TP_DOCID_DNI")) {
            visibleNroDocVal = true;
            nroDocVal = tt.getPerExamenId().getNumDocVal();
        }
        nroDoc = tt.getPerExamenId().getNumDoc();
        nroDocVal = tt.getPerExamenId().getNumDocVal();
        nroExpediente = tComp.getNroExpediente();
        tipoTramite = tt.getTipoTramiteId();
        if (!tipoTramite.getCodProg().equals("TP_TRA_DEF")
                && !tipoTramite.getCodProg().equals("TP_TRA_DEP")
                && !tipoTramite.getCodProg().equals("TP_TRA_CAZ")) {
            nroRuc = tt.getPerPagoId().getRuc();
            visibleNroRuc = true;
        }
        tipoArma = tComp.getTipoArmaId();
        cargarCalibres();
        tipoCalibre = tComp.getCalibreId();
        tipoBanco = tComp.getBancoId();
        voucherTasa = tComp.getVoucherTasa();
        voucherSeq = tComp.getVoucherSeq();
        fechaPago = tComp.getFechaPago();
        voucherCta = tComp.getVoucherCta();
        voucherAgencia = tComp.getVoucherAgencia();
        voucherAutentica = tComp.getVoucherAutentica();

        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("closeOnEscape", false);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 600);
        options.put("contentHeight", 460);
        context.openDialog("dlgEditarDatos.xhtml", options, null);
    }

    public void guardarEditarDatos() {
        if (validarEditarDatos()) {
            CitaTurComprobante tComp = (CitaTurComprobante) selectedMap.get("comprobante");
            tComp.setCalibreId(tipoCalibre);
            tComp.setBancoId(tipoBanco);
            tComp.setVoucherTasa(voucherTasa.toUpperCase());
            tComp.setVoucherSeq(voucherSeq.toUpperCase());
            tComp.setFechaPago(JsfUtil.getFechaSinHora(fechaPago));
            tComp.setVoucherCta(voucherCta.toUpperCase());
            tComp.setVoucherAgencia(voucherAgencia.toUpperCase());
            tComp.setVoucherAutentica(voucherAutentica.toUpperCase());

            tComp = (CitaTurComprobante) JsfUtil.entidadMayusculas(tComp, "");
            ejbTurComprobanteFacade.edit(tComp);

            edicionGuardada = true;
            cargarTurnosDia();

            JsfUtil.ListDialogParams p = new JsfUtil.ListDialogParams();
            RequestContext.getCurrentInstance().closeDialog(p);
        }
    }

    public boolean validarEditarDatos() {
        boolean flagValidar = true;
        if (tipoCalibre == null) {
            JsfUtil.addWarningMessage("Debe seleccionar el calibre");
            flagValidar = false;
        }
        if (tipoBanco == null) {
            JsfUtil.addWarningMessage("Debe seleccionar el banco");
            flagValidar = false;
        }
        if (voucherTasa == null || voucherTasa.equals("")) {
            JsfUtil.addWarningMessage("Debe ingresar la tasa del voucher");
            flagValidar = false;
        }
        if (voucherSeq == null || voucherSeq.equals("")) {
            JsfUtil.addWarningMessage("Debe ingresar la secuencia del voucher");
            flagValidar = false;
        }
        if (fechaPago == null) {
            JsfUtil.addWarningMessage("Debe ingresar la fecha de pago");
            flagValidar = false;
        }
        if (voucherCta == null || voucherCta.equals("")) {
            JsfUtil.addWarningMessage("Debe ingresar la cuenta del voucher");
            flagValidar = false;
        }
        if (voucherAgencia == null || voucherAgencia.equals("")) {
            JsfUtil.addWarningMessage("Debe ingresar la agencia del voucher");
            flagValidar = false;
        }
        if (voucherAutentica == null || voucherAutentica.equals("")) {
            JsfUtil.addWarningMessage("Debe ingresar la autenticación del voucher");
            flagValidar = false;
        }
        if (flagValidar == true) {

//            String compTemp = turComprobante.getVoucherTasa() + turComprobante.getVoucherSeq() + turComprobante.getVoucherCta() + turComprobante.getVoucherAgencia() + turComprobante.getVoucherAutentica();
            if (!ejbTurComprobanteFacade.listarComprobantesXVoucher(turComprobante.getVoucherTasa().toUpperCase(), turComprobante.getVoucherSeq().toUpperCase(), turComprobante.getVoucherCta().toUpperCase(), turComprobante.getVoucherAgencia().toUpperCase(), turComprobante.getVoucherAutentica().toUpperCase()).isEmpty()) {
                List<CitaTurComprobante> tcomp = ejbTurComprobanteFacade.listarComprobantesXVoucher(turComprobante.getVoucherTasa().toUpperCase(), turComprobante.getVoucherSeq().toUpperCase(), turComprobante.getVoucherCta().toUpperCase(), turComprobante.getVoucherAgencia().toUpperCase(), turComprobante.getVoucherAutentica().toUpperCase());
                boolean repetido = false;
                for (CitaTurComprobante tc : tcomp) {
                    if (JsfUtil.getFechaSinHora(tc.getFechaPago()).compareTo(JsfUtil.getFechaSinHora(turComprobante.getFechaPago())) == 0) {
                        repetido = true;
                    }
                }
                if (repetido) {
                    JsfUtil.addWarningMessage("El comprobante que esta tratando de ingresar ya ha sido usado anteriormente");
                    flagValidar = false;
                }
            }

        }

        return flagValidar;
    }

    public void openVer(Map item) {
        selectedMap = item;
        CitaTurComprobante tComp = (CitaTurComprobante) item.get("comprobante");
        nroExamen = tComp.getTurConstanciaList().size();
        CitaTurTurno tt = (CitaTurTurno) item.get("turno");

        visibleNroRuc = false;
        tipoDoc = tt.getPerExamenId().getTipoDoc();
        if (tipoDoc.getCodProg().equals("TP_DOCID_DNI")) {
            visibleNroDocVal = true;
            nroDocVal = tt.getPerExamenId().getNumDocVal();
        }
        nroDoc = tt.getPerExamenId().getNumDoc();
        nroExpediente = tComp.getNroExpediente();
        tipoTramite = tt.getTipoTramiteId();
        if (!tipoTramite.getCodProg().equals("TP_TRA_DEF")
                && !tipoTramite.getCodProg().equals("TP_TRA_DEP")
                && !tipoTramite.getCodProg().equals("TP_TRA_CAZ")) {
            nroRuc = tt.getPerPagoId().getRuc();
            visibleNroRuc = true;
        }
        tipoArma = tComp.getTipoArmaId();
        cargarCalibres();
        tipoCalibre = tComp.getCalibreId();
        tipoBanco = tComp.getBancoId();
        voucherTasa = tComp.getVoucherTasa();
        voucherSeq = tComp.getVoucherSeq();
        fechaPago = tComp.getFechaPago();
        voucherCta = tComp.getVoucherCta();
        voucherAgencia = tComp.getVoucherAgencia();
        voucherAutentica = tComp.getVoucherAutentica();

        selectedNroDoc = tt.getPerExamenId().getNumDoc();
        int cont = 0;
        turConstancia1 = null;
        turConstancia2 = null;
        turConstancia3 = null;
        for (CitaTurConstancia tc : tComp.getTurConstanciaList()) {
            cont = cont + 1;
            if (cont == 1) {
                turConstancia1 = tc;
            } else if (cont == 2) {
                turConstancia2 = tc;
            } else {
                turConstancia3 = tc;
            }
        }

        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("closeOnEscape", false);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 1400);
        options.put("contentHeight", 440);
        context.openDialog("dlgVerTurno.xhtml", options, null);
    }

    public void oncapture(CaptureEvent captureEvent) {
        CitaTurTurno tt = (CitaTurTurno) selectedMap.get("turno");
        fileName = tt.getPerExamenId().getNumDoc() + ".jpg";
        data = captureEvent.getData();

        InputStream is = new ByteArrayInputStream(data);
        fotoImg = new DefaultStreamedContent(is, "image/jpeg", fileName);

    }

    public void buscarTurnoCC() {
        listaTurnosMap = new ArrayList();
        List<CitaTurTurno> ttList = ejbFacade.listarTurnosCC(nroConsulta, nroDoc, fechaTurno);
        for (CitaTurTurno tt : ttList) {
            for (CitaTurComprobante tc : tt.getTurComprobanteList()) {
                Map turnosMap = new HashMap();
                turnosMap.put("turno", tt);
                turnosMap.put("comprobante", tc);
                turnosMap.put("nroVeces", tc.getTurConstanciaList().size());
                listaTurnosMap.add(turnosMap);
            }
        }
        nroConsulta = null;
        nroDoc = null;
        fechaTurno = null;
    }

    public void limpiarTurnosCC() {
        nroConsulta = null;
        nroDoc = null;
        fechaTurno = null;
        listaTurnosMap = new ArrayList();
    }

    public void openDlgCaptcha() {
        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("closeOnEscape", false);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 750);
        options.put("contentHeight", 120);
        captcha = new Captcha();
        // Modificar directorio y agregar archivo ListDialog //
        context.openDialog("dlgCaptcha.xhtml", options, null);
    }

    public void openDlgCaptchaRep() {
        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("closeOnEscape", false);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 380);
        options.put("contentHeight", 250);
        captcha = new Captcha();
        // Modificar directorio y agregar archivo ListDialog //
        context.openDialog("dlgCaptchaRep.xhtml", options, null);
    }

    public void nuevasArmas() {
//        visibleCodVer = false;
//        visibleTipoTram = true;
//        codVerificacion = null;
        registro.setTipoTramiteId(null);
        registro.setVezTurno(null);
        listaComprobantes = new ArrayList();
        visibleAgrComprobante = true;
    }

    public boolean isTerminos() {
        return terminos;
    }

    public void setTerminos(boolean terminos) {
        this.terminos = terminos;
    }

    public Captcha getCaptcha() {
        return captcha;
    }

    public boolean validaCaptchaTC() {
        boolean error = false;
        // Validar Terminos //
        if (!terminos) {
            JsfUtil.addErrorMessage("Se requiere que acepte los términos y condiciones");
            JsfUtil.invalidar("tabGestion:creaCitaPolForm:terminos");
            error = true;
        }
        // Validar Captcha //
        if (!captcha.ok()) {
            JsfUtil.addErrorMessage("Error el captcha es incorrecto");
            JsfUtil.invalidar("tabGestion:creaCitaPolForm:textoCaptcha");
            error = true;
        }
        if (error) {
            captcha.generarCaptcha();
        }
        return !error;
    }

    public void seleccionarSede() {
        listaProgramaciones = new ArrayList();
        registro.setFechaTurno(null);
        registro.setProgramacionId(null);
        if (registro.getTipoSedeId() != null) {
            visibleCalendarZonal = registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_ANCASH")
                    || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_AQP")
                    || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_CHICLA")
                    || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_CUSCO")
                    || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_LIBERTA")
                    || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_PIURA")
                    || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_PUNO")
                    || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_ICA")
                    || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_CAJAMARCA")
                    || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_LOR");
            visibleCalendarTacna = registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_TACNA");
            visibleCalendarCentral = !visibleCalendarZonal && !visibleCalendarTacna;
            parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaCodProgByCydocProcesoId("TP_TUP_SUC1", 1140);  // TUPA 2018, Proceso
        } else {
            visibleCalendarZonal = false;
            visibleCalendarCentral = false;
            visibleCalendarTacna = false;
            parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaCodProgByCydocProcesoId("TP_TUP_SUC1", 858);  // TUPA 2018, Proceso
        }

//        if (registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_PUC")) {
//            Date hoyVal = new Date();
//
//            hoy = new Date(117, 2, 20);
//
//            if (hoyVal.compareTo(hoy) > 0) {
//                hoy = hoyVal;
//            }
//
//            maxFecha = new Date(117, 2, 24);
//        }
    }

    public List<CitaTipoBase> listarSedes() {
        List<CitaTipoBase> sedes = ejbTipoBaseFacade.listarSedes();
        for (CitaTipoBase sede : sedes) {
            if (sede.getCodProg().equals("TP_AREA_TRAM")) {
                sede.setNombre("LIMA");
            }
        }
        return sedes;
    }

    public String getValidarTacna() {
        Date anhoactual = new Date();
        int anhoFC = Integer.parseInt(JsfUtil.obtenerFechaXCriterio(anhoactual, 1));
        List<Integer> años = new ArrayList();
        años.add(anhoFC);
        años.add(anhoFC + 1);

        String resultado = "[";

        for (int año : años) {
            for (int mes = 1; mes <= 12; mes++) {
                int juevesCount = 0;
                for (int dia = 1; dia <= 21; dia++) {
                    try {
                        String fechaAuxStr = dia + "/" + mes + "/" + año;
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date fechaAux = JsfUtil.getFechaSinHora(formatter.parse(fechaAuxStr));

                        if (fechaAux.getDay() == 4) {
                            if (juevesCount == 0 || juevesCount == 2) {
                                resultado = resultado
                                        + "\"" + JsfUtil.obtenerFechaXCriterio(fechaAux, 2)
                                        + "-" + JsfUtil.obtenerFechaXCriterio(fechaAux, 3)
                                        + "-" + JsfUtil.obtenerFechaXCriterio(fechaAux, 1) + "\",";

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(formatter.parse(fechaAuxStr));
                                cal.add(Calendar.DATE, -2);
                                Date dateMartes = cal.getTime();

                                resultado = resultado
                                        + "\"" + JsfUtil.obtenerFechaXCriterio(dateMartes, 2)
                                        + "-" + JsfUtil.obtenerFechaXCriterio(dateMartes, 3)
                                        + "-" + JsfUtil.obtenerFechaXCriterio(dateMartes, 1) + "\",";

                            }
                            juevesCount++;
                        }

                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        resultado = resultado.substring(0, resultado.length() - 1);
        resultado += "]";
        //Syso("resultado: " + resultado);
        return resultado;
    }

    public String descripcionTipoArma(CitaTurConstancia item) {
        if (!entroTipoarma) {
            entroTipoarma = true;
            return "Corta";
        } else {
            entroTipoarma = false;
            return "Larga";
        }
    }

    public List<String> autcompleteRuc(String query) {
        try {
            List<ArrayRecord> list = ejbRma1369Facade.buscarEmpresaVigilanteIntegrado(query);
            List<String> lstString = new ArrayList<>();
            for (ArrayRecord row : list) {
                String cadena = row.get("ID").toString();
                lstString.add(cadena);
            }
            rucRznSocial = nroRuc = rznSocial = "";
            perPago = new CitaTurPersona();
            RequestContext.getCurrentInstance().update("tabGestion:creaCitaPolForm:rznSocial");
            return lstString;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void eligeEmpresaVig(SelectEvent event) {
        if (event.getObject() != null) {
            rucRznSocial = (String) event.getObject().toString();
            ////Syso("elegido:" + event.getObject());
            String[] ids = rucRznSocial.split(",");
            nroRuc = ids[0];
            rznSocial = ids[1];

            //rucRznSocial = (ArrayRecord) event.getObject();           
            //nroRuc = rucRznSocial.get("RUC").toString();
            //rznSocial = rucRznSocial.get("EMPRESA").toString();
            perPago.setRuc(nroRuc);
            perPago.setRznSocial(rznSocial);

            //RequestContext.getCurrentInstance().update("tabGestion:creaCitaPolForm:rznSocial");        
        } else {
            rucRznSocial = nroRuc = rznSocial = "";
            perPago = new CitaTurPersona();
        }
    }

    public String obtenerRuc(String value) {
        if (value != null) {
            try {
                String[] ids = value.split(",");
                nroRuc = ids[0];
                rznSocial = ids[1];

                return nroRuc;
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public void handleFileUploadAdjunto(FileUploadEvent event) {
        try {
            if (event != null) {
                file = event.getFile();
                fotoByte = IOUtils.toByteArray(file.getInputstream());
                boolean valido = false;
                if (JsfUtil.verificarJPG(file)) {
                    foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
                    valido = true;
                } else if (JsfUtil.verificarPNG(file)) {
                    foto = new DefaultStreamedContent(file.getInputstream(), "image/png");
                    valido = true;
                } else if (JsfUtil.verificarPDF(file)) {
                    foto = new DefaultStreamedContent(file.getInputstream(), "application/pdf");
                    valido = true;
                }

                if (valido) {
                    archivo = file.getFileName();
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("$(\"div[id*='pfileUpload']\").children('.ui-fileupload-content').children('.ui-fileupload-files').find('tbody').append('<tr><td class=\"txtArchivo\">" + archivo + "</td></tr>');");
                } else {
                    file = null;
                    fotoByte = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de cargar no tiene formato PDF, ni PNG/JPG/JPEG");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarTasasTupa() {
        try {
            solicitarRecibo = Boolean.parseBoolean(ejbSbParametroFacade.obtenerParametroXCodProg("TP_BOOL_TUPA_POL").getValor());
            solicitarVoucherTemp = Boolean.parseBoolean(ejbSbParametroFacade.obtenerParametroXCodProg("TP_BOOL_VCTMP_POL").getValor());
        } catch (Exception e) {
            solicitarRecibo = false;
            solicitarVoucherTemp = false;
        }
        //parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaCodProgByCydocProcesoId("TP_TUP_SUC1", 710);  // TUPA 2018, Proceso
        /*if (solicitarVoucherTemp) {
            fechaMinDeposito = JsfUtil.stringToDate(ejbSbParametroFacade.obtenerParametroXNombre("eppRegistroGuiaTransito_fechaMinVc").getValor(), "dd/MM/yyyy");
        }*/
    }

    /**
     * Autocompletar para la busqueda de recibos
     *
     * @param query
     * @return
     */
    public List<SbRecibos> autoCompleteRecibos(String query) {
        try {
            borrarValoresRecibo();
            List<SbRecibos> filteredList = new ArrayList();

            if (registro.getTipoTramiteId() != null) {
                HashMap mMap = new HashMap();
                mMap.put("tipo", null);
                mMap.put("importe", parametrizacionTupa.getImporte());
                mMap.put("codigo", parametrizacionTupa.getCodTributo());
                //mMap.put("ruc", perExamen.getRuc() );

                switch (registro.getTipoTramiteId().getCodProg()) {
                    case "TP_TRA_DEF":
                    case "TP_TRA_CAZ":
                    case "TP_TRA_DEP":
                    case "TP_TRA_COL":
                    case "TP_TRA_VP":
                        mMap.put("numDoc", (perExamen.getNumDoc() != null) ? perExamen.getNumDoc() : perExamen.getRuc());
                        break;
                    case "TP_TRA_SIS":
                        mMap.put("numDoc", perPago.getRuc());
                        break;
                }

                Long nroSec = Long.valueOf(query.trim());
                if (nroSec.toString().trim().length() >= 2) {
                    List<SbRecibos> lstUniv = sbRecibosFacade.listarRecibosByFiltro(mMap);
                    if (lstUniv != null) {
                        for (SbRecibos x : lstUniv) {
                            if (x.getSbReciboRegistroList().isEmpty()) {
                                if (x.getNroSecuencia().toString().contains(nroSec.toString().trim())) {
                                    //if (x.getNroSecuencia().equals(nroSec)) {
                                    //if (query.trim().contains(x.getNroSecuencia().toString())) {
                                    filteredList.add(x);
                                }
                            }
                        }
                    }
                    return filteredList;
                } else {
                    return null;
                }
            } else {
                JsfUtil.mensajeAdvertencia("Seleccione tipo de Licencia!");
                return null;
            }
        } catch (Exception e) {
            borrarValoresRecibo();
            e.printStackTrace();
            return null;
        }
    }

    public void lstnrSelectRecibo(SelectEvent event) {
        try {
            if (event != null) {
                borrarValoresRecibo();
                reciboBn = (SbRecibos) event.getObject();
                strNroSecuencia = "<b>" + reciboBn.getNroSecuencia() + "</b>";
                strFechaMovimiento = "<b>" + JsfUtil.formatoFechaDdMmYyyy(reciboBn.getFechaMovimiento()) + "</b>";
                strImporte = "<b>" + reciboBn.getImporte() + "</b>";
                setDisableReciboBn(true);
                setEligeReciboLista(true);
                JsfUtil.mensaje("Recibo " + reciboBn.getNroSecuencia() + " seleccionado correctamente.");
            }
        } catch (Exception ex) {
            JsfUtil.mensajeError(ex.getMessage());
        }

    }

    private void borrarValoresRecibo() {
        reciboBn = null;
        strNroSecuencia = null;
        strFechaMovimiento = null;
        strImporte = null;
        setEligeReciboLista(false);
    }

    public void borrarReciboSeleccionado() {
        borrarValoresRecibo();
        setDisableReciboBn(false);
    }

    //# RENOVACION #//
    public void selectTipoOperacion() {
        if (registro.getSubTramiteId() != null) {
            setOpeLicIni(false);
            setOpeLicRen(false);
            borrarReciboSeleccionado();
            switch (registro.getSubTramiteId().getCodProg()) {
                case "TP_TRAM_POL_INI":
                    setOpeLicIni(true);
                    break;
                case "TP_TRAM_POL_REN":
                    listarLicencias();
                    setOpeLicRen(true);
                    break;
            }
            setVisibleNroRuc(false);
            setVisibleMultimodal(false);

            licencia = null;
            licenciaModalidad = "";
            registro.setTipoTramiteId(null);
            modalidadesSelected = null;
            listaComprobantes = new ArrayList();
            rucRznSocial = "";
            perPago = new CitaTurPersona();
            listaTipoLicencia = new ArrayList();
            tipoLic = new CitaTurConstancia();

            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("$(\".contTipoLic, .contNroLic\").addClass(\"ui-helper-hidden\")");
            if (isOpeLicIni()) {
                context.execute("$(\".contTipoLic\").removeClass(\"ui-helper-hidden\")");
            }
            if (isOpeLicRen()) {
                context.execute("$(\".contNroLic\").removeClass(\"ui-helper-hidden\")");
            }

        }
    }

    public void listarLicencias() {
        if (perExamen != null) {
            listaLicencia = new ArrayList<>();
            SbPersona per = ejbSbPersona.findByNumDoc(loginController.getUsuario().getNumDoc().trim());
            listaLicencia = ejbAmaLicenciaDeUsoFacade.listarLicenciaXPerLicenciaXRenovar(per.getId());
        }
    }

    public void selectNroLicencia() {
        if (licencia != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            List<String> sList = Arrays.asList("TP_EST_VIG", "TP_EST_VEN");
            //Syso("LIC: " + licencia.getEstadoId().getCodProg());                
            if (sList.contains(licencia.getEstadoId().getCodProg())) {
                boolean mostrarLic = true;
                licenciaModalidad = "Modalidad: ";
                for (AmaTipoLicencia tip : licencia.getAmaTipoLicenciaList()) {
                    //Syso("LIC TIPO: " + tip.getModalidadId().getCodProg());
                    if (tip.getModalidadId().getCodProg().equals("TP_MOD_SEG")) {
                        registro.setTipoTramiteId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_TRA_VP"));
                        licenciaModalidad += tip.getModalidadId().getNombre() + "";
                        break;
                    }
                    if (tip.getModalidadId().getCodProg().equals("TP_MOD_SIS")) {
                        if (listaArmasVerRen()) {
                            registro.setTipoTramiteId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_TRA_SIS"));
                            licenciaModalidad += tip.getModalidadId().getNombre();
                        } else {
                            JsfUtil.addErrorMessage("Para iniciar el trámite de renovación de licencia de uso, previamente las armas de fuego deben pasar por verificación!");
                            //selectTipoLicencia();
                            licenciaModalidad = "";
                            context.execute("$(\".contTipoLic\").addClass(\"ui-helper-hidden\")");
                            licencia = null;
                            mostrarLic = false;
                        }
                        break;
                    }
                }

                if (mostrarLic) {
                    setVisibleNroRuc(true);

                    borrarReciboSeleccionado();
                    switch (registro.getTipoSedeId().getCodProg()) {
                        case "TP_AREA_GAMAC":
                        case "TP_AREA_TRAM":
                        case "TP_AREA_OGTIC":
                            parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaIdByTipoProceso("TP_TUP_SUC1", registro.getTipoTramiteId().getCodProg() + "_R");  // TUPA 2018, Proceso                
                            break;
                        default:
                            parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaIdByTipoProceso("TP_TUP_SUC1", registro.getTipoTramiteId().getCodProg() + "_R_OD");  // TUPA 2018, Proceso                
                            break;
                    }
                    //Syso("TipoTramiteId(): " + registro.getTipoTramiteId().getCodProg());       
                    //Syso("parametrizacionTupa(): " + parametrizacionTupa.getImporte());                                   

                    listaTipoLicencia = new ArrayList<>();
                    tipoLic = new CitaTurConstancia();
                    tipoLic.setTurnoId(registro);
                    tipoLic.setModalidadId(registro.getTipoTramiteId());
                    tipoLic.setTipoArmaId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_TIPO_AR_COR"));
                    listaTipoLicencia.add(tipoLic);
                    tipoLic = new CitaTurConstancia();
                    tipoLic.setTurnoId(registro);
                    tipoLic.setModalidadId(registro.getTipoTramiteId());
                    tipoLic.setTipoArmaId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_TIPO_AR_LAR"));
                    listaTipoLicencia.add(tipoLic);

                    context.execute("$(\".contRuc, .contSRuc\").addClass(\"ui-helper-hidden\")");
                    context.execute("$(\".contTipoLic\").addClass(\"ui-helper-hidden\")");
                    if (isVisibleNroRuc()) {
                        context.execute("$(\".contRuc\").removeClass(\"ui-helper-hidden\")");
                    }
                    if (isVisibleMultimodal()) {
                        context.execute("$(\".contSRuc\").removeClass(\"ui-helper-hidden\")");
                    }
                }
            } else {
                JsfUtil.addWarningMessage("La Licencia elegida debe estar VIGENTE o VENCIDA!");
                context.execute("$(\".contTipoLic\").addClass(\"ui-helper-hidden\")");
                licencia = null;
            }
        }
    }

    public boolean listaArmasVerRen() {
        boolean res = true;
        SbPersona per = ejbSbPersona.findByNumDoc(loginController.getUsuario().getNumDoc().trim());
        //Syso("Persona: " + per.getId() + " - " + per.getNumDoc());
        List<AmaTarjetaPropiedad> lstTarjetas = gestionCitasFacade.listTarjetaSispePropietario(per.getId());
        if (!lstTarjetas.isEmpty()) {
            if (lstTarjetas.size() > 0) {
                for (AmaTarjetaPropiedad tar : lstTarjetas) {
                    //Syso("arma: " + tar.getArmaId().getSerie());
                    boolean agregar = false;
                    List<String> intListT1 = Arrays.asList("TP_SITU_POS", "TP_SITU_POS_EP", "TP_SITU_POS_LC");
                    if (intListT1.contains(tar.getArmaId().getSituacionId().getCodProg())) {
                        agregar = true;
                    }

                    if (agregar) {
                        CitaTurLicenciaReg verifacion = validarVerificacionArmaRen(tar.getArmaId());
                        if (verifacion == null) {
                            res = false;
                            break;
                        }
                    }
                }
            }
        }
        return res;

    }

    public CitaTurLicenciaReg validarVerificacionArmaRen(AmaArma detArma) {
        CitaTurLicenciaReg veri;

        List<CitaTurLicenciaReg> listAprobadas = new ArrayList<>();
        if (detArma != null) {
            listAprobadas = ejbTurLicenciaRegFacade.listarArmasVerificaRen(detArma.getId());
            //} else {
            //listAprobadas = ejbTurLicenciaRegFacade.listarArmasVerificaAprobXSerie(detArma.getNroSerie());
        }

        if (listAprobadas.isEmpty()) {
            veri = null;
        } else {
            veri = listAprobadas.get(0);
            //Syso("veri: " + veri.getSerie());
            if (veri.getTurnoId().getSubTramiteId() != null) {
                if (!JsfUtil.validaFechaMayorMenor(registro.getFechaTurno(), veri.getFecVen())) {
                    veri = null;
                }
            }
        }

        return veri;
    }

    //MSALINAS
    public String mostrarProgramarTurnoTramite() {
        restaurarProgramacion();
        registro = new CitaTurTurno();
        registro.setAudLogin(loginController.getUsuario().getLogin());
        registro.setAudNumIp(JsfUtil.getNumIP());
        registro.setActivo((short) 1);

        tipoLic = new CitaTurConstancia();
        //perExamen = new CitaTurPersona();
        //perPago = new CitaTurPersona();

        return "/aplicacion/citas/tramite/ReservarCitaTramite";
    }

    public List<CitaTipoGamac> getListaSubTramites() {
        listaSubTramites = ejbTipoGamacFacade.lstTipoSubTramite();
        return listaSubTramites;
    }

}
