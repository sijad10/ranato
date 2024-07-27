/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.jsf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import pe.gob.sucamec.sel.citas.data.CitaTurTurno;
import pe.gob.sucamec.sel.citas.jsf.util.JsfUtil;
import pe.gob.sucamec.sel.citas.jsf.util.EstadoCrud;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.bdintegrado.data.SbParametro;
import pe.gob.sucamec.bdintegrado.data.SbProcesoTupa;
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.sel.citas.data.CitaAmaTurnoActa;
import pe.gob.sucamec.sel.citas.data.CitaDetalleArma;
import pe.gob.sucamec.sel.citas.data.CitaHora;
import pe.gob.sucamec.sel.citas.data.CitaSbFeriado;
import pe.gob.sucamec.sel.citas.data.CitaSbNumeracion;
import pe.gob.sucamec.sel.citas.data.CitaTurPersona;
import pe.gob.sucamec.sel.citas.data.CitaTipoBase;
import pe.gob.sucamec.sel.citas.data.CitaTipoGamac;
import pe.gob.sucamec.sel.citas.data.CitaTurComprobante;
import pe.gob.sucamec.sel.citas.data.CitaTurConstancia;
import pe.gob.sucamec.sel.citas.data.CitaTurLicenciaReg;
import pe.gob.sucamec.sel.citas.data.CitaTurProgramacion;
import pe.gob.sucamec.sel.citas.jsf.util.Captcha;
//import pe.gob.sucamec.sel.ctias.jsf.util.UploadFilesController;
import pe.gob.sucamec.sel.citas.jsf.util.RenagiUploadFilesController;
import pe.gob.sucamec.sel.citas.jsf.util.ReportUtil;
import pe.gob.sucamec.sel.citas.ws.CitaWsPide;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

/**
 *
 * @author rarevalo
 */
@Named("citasVerificacionJurController")
@SessionScoped
public class CitasVerificacionJurController implements Serializable {

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
    private Date fechaIni;
    private Date hoy = new Date();
    private Date maxFecha;
    private List<CitaTurProgramacion> listaProgramaciones = new ArrayList();
    private List<CitaHora> listaHoras = new ArrayList();
    private CitaTurPersona perExamen;
    private CitaTurPersona perPago;
    private SbPersona citaRepresentante;
    private List<SbPersona> citaLstRepresentantes;
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
    private List<CitaTipoGamac> listaTramites;
    private List<CitaTipoGamac> listaSubTramites;
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
    //foto
    private String fileName;
    private StreamedContent fotoImg;
    private RenagiUploadFilesController managefile;
    private byte[] data;
    //variables
    private boolean fotoGuardada = false;
    private boolean exaTeoGuardado = false;
    private boolean exaTiroGuardado = false;
    private boolean edicionGuardada = false;
    private CitaTipoBase tipoDoc;
    private String nroDocVal;
    private CitaTipoGamac tipoTramite;
    private CitaTipoGamac tipoSubTramite;
    private String nroRuc;
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

    private boolean visibleCalendarZonal = false;
    private boolean visibleCalendarCentral = false;
    private boolean visibleCalendarNoPresencial = false;
    private boolean visibleTipoSede = false;
    
    private Date fechaProcess;

    private List<CitaDetalleArma> lstDetalleArma;
    private List<CitaDetalleArma> lstDetalleArmaSelected;
    private CitaDetalleArma detalleArma;

    List<Map> listArmas = new ArrayList();

    private boolean visibleBtnBuscarArma;
    private boolean visibleBtnCargarArma;

    private boolean disabledTramite;

    //dlg buscar arma
    private String filtroArmas;
    private ListDataModel listaTarjetasPropiedad;
    private List<AmaTarjetaPropiedad> datosTarjetasPropiedad;
    private AmaTarjetaPropiedad tarjetaSelected;
    private List<Map> armasMap = new ArrayList();
    private List<Map> discaMapLst = new ArrayList();
    private AmaModelos selectedArma;

    private String fechaTurnoSeleccionada;
            
    List<Map> lstFechasCalendario = new ArrayList();

    List<Map> lstFechasNoHabilitadoCalendario = new ArrayList();
    private String fechasNoHabilitadas;
        
    //captcha
    Captcha captcha;
    boolean terminos = false;

    //TasasTupa
    private boolean solicitarRecibo;
    private boolean solicitarVoucherTemp;
    private SbProcesoTupa parametrizacionTupa;
    private Date fechaMinDeposito;
    private SbRecibos reciboBn;
    private String strNroSecuencia;
    private String strFechaMovimiento;
    private String strImporte;
    private boolean disableReciboBn;

    /// ELEGIR VOUCHER
    private boolean eligeReciboLista;

    private SbParametro paramOriDatArma;
    private String desdeMigra;

    private CitaTipoBase tipoCita;
    
    private int minutosAntesInicio = 0;
    private int minutosTolerancia = 0;
    private String mensajePaso3Nota1 = "";
    private String mensajePaso3Nota2 = "";

    private String nroDocComprador;

    @Inject
    private LoginController loginController;
    @Inject
    private CitaTipoGamacController tipoGamacController;
    @Inject
    private CitaWsPide wsPideController;
    @Inject
    private RenagiUploadFilesController upFilesCont;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurTurnoFacade ejbFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurLicenciaRegFacade ejbTurLicenciaRegFacade;
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
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaLicenciaDeUsoFacade ejbGamacAmaLicenciaDeUsoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaGuiaTransitoFacade ejbAmaGuiaTransitoFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.GestionCitasFacade gestionCitasFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade ejbSbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbPersonaFacade ejbSbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbProcesoTupaFacade ejbSbProcesoTupaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade ejbSbRecibosFacade;

    /**
     * Constructor
     *
     */
    public CitasVerificacionJurController() {
//        restaurarProgramacion();
//        registro = new TurTurno();
//        registro.setAudLogin(loginController.getUsuario().getLogin());
//        registro.setAudNumIp(JsfUtil.getNumIP());
//        registro.setActivo((short) 1);
//
//        turComprobante = new TurComprobante();
//        turComprobante.setBancoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_BCO_NAC"));
//        turComprobante.setAudLogin(loginController.getUsuario().getLogin());
//        turComprobante.setAudNumIp(JsfUtil.getNumIP());
//
//        perExamen = new TurPersona();
        estado = EstadoCrud.BUSCAR;
    }

    @PostConstruct
    public void inicializar() {
        //restaurarProgramacion();
        //restaurarReprogramacion();
        registro = new CitaTurTurno();
        registro.setAudLogin(loginController.getUsuario().getLogin());
        registro.setAudNumIp(JsfUtil.getNumIP());
        registro.setActivo((short) 1);

        turComprobante = new CitaTurComprobante();
        turComprobante.setBancoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_BCO_NAC"));
        turComprobante.setAudLogin(loginController.getUsuario().getLogin());
        turComprobante.setAudNumIp(JsfUtil.getNumIP());

        perExamen = new CitaTurPersona();

        setVisibleBtnCargarArma(false);
        setVisibleBtnBuscarArma(false);

        // Parámetro origen data
        setParamOriDatArma(ejbSbParametroFacade.obtenerParametroXSistemaCodProg("TP_ORIGDAT_ARMDIS", 5L));
        if (paramOriDatArma != null) {
            setDesdeMigra(paramOriDatArma.getValor());
        } else {
            setDesdeMigra("DISCA");
        }

    }

    //Getters & Setters
    public Date getFechaIni() {
        Date maxFechaAux = new GregorianCalendar(2019, Calendar.JANUARY, 15).getTime();

        fechaIni = hoy;
        if (maxFechaAux.compareTo(hoy) > 0) {
            fechaIni = maxFechaAux;
        }

        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public String getTipoProg() {
        return tipoProg;
    }

    public void setTipoProg(String tipoProg) {
        this.tipoProg = tipoProg;
    }

    public CitaTipoBase getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(CitaTipoBase tipoCita) {
        this.tipoCita = tipoCita;
    }
    
    public Date getHoy() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = "23-07-2018";
        Date dat = sdf.parse(dateString);
        Date today = new Date();
        if (dat.compareTo(today) > 0) {
            hoy = dat;
        } else {
            hoy = today;
        }

        return hoy;
    }

    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }

    public String getFechasNoHabilitadas() {
        return fechasNoHabilitadas;
    }

    public void setFechasNoHabilitadas(String fechasNoHabilitadas) {
        this.fechasNoHabilitadas = fechasNoHabilitadas;
    }

    public List<Map> getLstFechasCalendario() {
        return lstFechasCalendario;
    }

    public void setLstFechasCalendario(List<Map> lstFechasCalendario) {
        this.lstFechasCalendario = lstFechasCalendario;
    }

    public String getFechaTurnoSeleccionada() {
        return fechaTurnoSeleccionada;
    }

    public void setFechaTurnoSeleccionada(String fechaTurnoSeleccionada) {
        this.fechaTurnoSeleccionada = fechaTurnoSeleccionada;
    }
    
    public String getMensajePaso3Nota1() {
        return mensajePaso3Nota1;
    }

    public void setMensajePaso3Nota1(String mensajePaso3Nota1) {
        this.mensajePaso3Nota1 = mensajePaso3Nota1;
    }

    public String getMensajePaso3Nota2() {
        return mensajePaso3Nota2;
    }

    public void setMensajePaso3Nota2(String mensajePaso3Nota2) {
        this.mensajePaso3Nota2 = mensajePaso3Nota2;
    }

    public String getNroDocComprador() {
        return nroDocComprador;
    }

    public void setNroDocComprador(String nroDocComprador) {
        this.nroDocComprador = nroDocComprador;
    }
    
    public Date getMaxFecha() throws ParseException {
        List<CitaSbFeriado> feriadosList = ejbSbFeriadoFacade.listaFeriados();

//      maxFecha = new Date();
        Date fechaIni = hoy; //new Date();
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(fechaIni);
        //fecha.add(Calendar.DAY_OF_MONTH, 20);
        fecha = obtenerCantidadDiasHabiles2(fecha, 7);
        maxFecha = fecha.getTime();

        /*SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = "31-08-2017";
        Date dat = sdf.parse(dateString);
        maxFecha = dat;*/
 /*if (fechaIni.getDay() < 4) {
            fecha.add(Calendar.DAY_OF_MONTH, 9); //9
            maxFecha = fecha.getTime();
            while (!Objects.equals(fechaIni, maxFecha)) {
                for (SbFeriado feriado : feriadosList) {
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
            fecha.add(Calendar.DAY_OF_MONTH, 11);
            maxFecha = fecha.getTime();
            while (!Objects.equals(fechaIni, maxFecha)) {
                for (SbFeriado feriado : feriadosList) {
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
        }*/
//        maxFecha = fecha.getTime();
        //Date maxFechaAux = new GregorianCalendar(2016, Calendar.OCTOBER, 31).getTime();

        /*if (maxFechaAux.compareTo(maxFecha) > 0) {
            maxFecha = maxFechaAux;
        }*/
        return maxFecha;
    }

    public void setMaxFecha(Date maxFecha) {
        this.maxFecha = maxFecha;
    }
    
    public Date getMaxFechaGeneral() {
        if (registro.getTipoSedeId() != null) {
            //Inicializa las fechas del Calendario y La lista donde va estar las fechas habilitadas
            lstFechasNoHabilitadoCalendario = new ArrayList();
            lstFechasCalendario = new ArrayList();
            HashMap mapFechaCalendario = new HashMap();
            Date maxFechaCalcula;

            boolean varLunes = false;
            boolean varMartes = false;
            boolean varMiercoles = false;
            boolean varJueves = false;
            boolean varViernes = false;
            boolean varSabado = false;
            boolean varDomingo = false;
            int diasFinReservaCita = 0;
            String diasSemanaHablitados = "";
            
            boolean procesoSoloDiasHabiles = false;
            
            //Selecciona la Configuración de la Sede para Verificación de Armas, cuando es Presencial o No presencial (tipoCita)
            List<Map> lstconfiguracionSede = ejbFacade.listaConfiguraSede(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PROG_VER").getId(),registro.getTipoSedeId().getId(),tipoCita.getId()); 
            for (Map item : lstconfiguracionSede) {
                minutosAntesInicio = Integer.parseInt(item.get("MINUTOS_ANTES_INICIO").toString());
                minutosTolerancia = Integer.parseInt(item.get("MINUTOS_TOLERANCIA").toString());
                
                //Aqui se realiza la distincion si va a trabajar con dias Calendario o Dias Habiles
                if (item.get("NUM_DIAS_RESERVA_HABILES") != null) {
                    //Solo Dias Habiles (No cuenta Sabado / Domingo, no Feriados Generales, no feriados especificos por sede)
                    diasFinReservaCita = Integer.parseInt(item.get("NUM_DIAS_RESERVA_HABILES").toString()); 
                    //Solo de Lunes a Viernes pueden ser configurables, el Sabado y Domingo no es un dia habil configurable
                    varLunes = Integer.parseInt(item.get("LUNES").toString()) == 1 ? true : false;
                    varMartes = Integer.parseInt(item.get("MARTES").toString()) == 1 ? true : false;
                    varMiercoles = Integer.parseInt(item.get("MIERCOLES").toString()) == 1 ? true : false;
                    varJueves = Integer.parseInt(item.get("JUEVES").toString()) == 1 ? true : false;
                    varViernes = Integer.parseInt(item.get("VIERNES").toString()) == 1 ? true : false;
                    varSabado = false; //El Sabado no cuenta
                    varDomingo = false; //El Domingo no cuenta
                    //Habilita que se realiza el proceso por dias Habiles
                    procesoSoloDiasHabiles = true;
                }    
                else {    
                    //Solo Dias Calendario (configurable cualquier dia de la semana incluye Sabado y Domingo, no Feriados Generales, no feriados especificos por sede)
                    diasFinReservaCita = Integer.parseInt(item.get("NUM_DIAS_RESERVA").toString());      
                    //Todos los dias pueden ser configurables de Lunes a Domingo
                    varLunes = Integer.parseInt(item.get("LUNES").toString()) == 1 ? true : false;
                    varMartes = Integer.parseInt(item.get("MARTES").toString()) == 1 ? true : false;
                    varMiercoles = Integer.parseInt(item.get("MIERCOLES").toString()) == 1 ? true : false;
                    varJueves = Integer.parseInt(item.get("JUEVES").toString()) == 1 ? true : false;
                    varViernes = Integer.parseInt(item.get("VIERNES").toString()) == 1 ? true : false;
                    varSabado = Integer.parseInt(item.get("SABADO").toString()) == 1 ? true : false;
                    varDomingo = Integer.parseInt(item.get("DOMINGO").toString()) == 1 ? true : false;
                    //El proceso es por dias Calendario 
                    procesoSoloDiasHabiles = false;
                }

                //Configura los dias de Semana Habilitados
                if (varLunes) diasSemanaHablitados += "1";
                if (varMartes) diasSemanaHablitados += "2";
                if (varMiercoles) diasSemanaHablitados += "3";
                if (varJueves) diasSemanaHablitados += "4";
                if (varViernes) diasSemanaHablitados += "5";
                if (varSabado) diasSemanaHablitados += "6";
                if (varDomingo) diasSemanaHablitados += "0";
            }

            
            //Proceso por dias Calendario
            if (procesoSoloDiasHabiles = false) {
                //Fecha de Inicio
                Date fechaIni = new Date();
                Date fechaIniRango = new Date();
                Calendar fecha = Calendar.getInstance();
                fecha.setTime(fechaIni);
                maxFecha = fecha.getTime();

                //Fecha Fin
                fecha.add(Calendar.DAY_OF_MONTH, diasFinReservaCita);
                Date fechaFin = fecha.getTime();
                //Actualiza con la fecha fin
                maxFechaCalcula = fecha.getTime();

                //Busca las Fechas Habilitadas por Sede de manera extraordinaria
                List<Date> lstFechaHabilitadaSede = ejbFacade.listaFechaHabilitadaSede(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PROG_VER").getId(),registro.getTipoSedeId().getId(),tipoCita.getId()); 
                if (lstFechaHabilitadaSede != null) {
                    for (Date fechasHabilitadas : lstFechaHabilitadaSede) {
                        if (fechasHabilitadas.compareTo(maxFechaCalcula) > 0) {
                            maxFechaCalcula.setTime(fechasHabilitadas.getTime());
                        }
                    }
                }


                //Se agrupa los Feriados Generales mas los Feriados de la Sede mas las fechas habilitadas (porque la fecha habilitada no debe quitarse ni por feriado/dia semana diferente a su programacion SEDE
                List<Map> lstSedeFechaHabilitadaFeriado = ejbFacade.listaSedeFechaHabilitadaFeriado(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PROG_VER").getId(),registro.getTipoSedeId().getId(),tipoCita.getId(),fechaIni,fechaFin); 
                //Inserta por cada dia
                while (maxFechaCalcula.compareTo(JsfUtil.getFechaSinHora(fechaIniRango)) >= 0) {

                    //Si no hay feriados o fechas habilitadas
                    if (lstSedeFechaHabilitadaFeriado.size() == 0) {
                        //Si lo encuentra dentro del dia de semana lo agrega
                        if (diasSemanaHablitados.indexOf(String.valueOf(fechaIniRango.getDay())) != -1) {
                            mapFechaCalendario = new HashMap();
                            mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                            if(!lstFechasCalendario.contains(mapFechaCalendario)){
                                lstFechasCalendario.add(mapFechaCalendario);     
                                maxFecha = JsfUtil.getFechaSinHora(fechaIniRango);
                            }   
                        }  else {
                            //Las que no van al Calendario
                            mapFechaCalendario = new HashMap();
                            mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                            if(!lstFechasNoHabilitadoCalendario.contains(mapFechaCalendario)){
                                lstFechasNoHabilitadoCalendario.add(mapFechaCalendario);     
                            }   
                        }
                        //Aumenta un dia mas
                        fecha.setTime(fechaIniRango);
                        fecha.add(Calendar.DAY_OF_MONTH, 1);
                        fechaIniRango = fecha.getTime();
                    }
                    else
                    {
                        //Buscamos si es un Feriado o Fecha Habilitada
                        List<Map> lstSedeFechaHabilitadaFeriadoEspefifica = ejbFacade.listaSedeFechaHabilitadaFeriadoEspefifica(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PROG_VER").getId(),registro.getTipoSedeId().getId(),tipoCita.getId(),fechaIniRango); 
                        if (lstSedeFechaHabilitadaFeriadoEspefifica.size() > 0) {
                            for (Map feriadoHabilita : lstSedeFechaHabilitadaFeriadoEspefifica) {
                                //Si es fecha habilitada si la agrega
                                if (JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(feriadoHabilita.get("FECHA").toString().substring(0,10),"yyyy-MM-dd"), 2).equals(JsfUtil.obtenerFechaXCriterio(fechaIniRango, 2))
                                    && JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(feriadoHabilita.get("FECHA").toString().substring(0,10),"yyyy-MM-dd"), 3).equals(JsfUtil.obtenerFechaXCriterio(fechaIniRango, 3))
                                    && JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(feriadoHabilita.get("FECHA").toString().substring(0,10),"yyyy-MM-dd"), 1).equals(JsfUtil.obtenerFechaXCriterio(fechaIniRango, 1))
                                    &&  feriadoHabilita.get("TIPO").toString().equals("HABILITA")) {
                                        //Si es fecha habilitada si la agrega
                                        mapFechaCalendario = new HashMap();
                                        mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                                        if(!lstFechasCalendario.contains(mapFechaCalendario)){
                                            lstFechasCalendario.add(mapFechaCalendario);     
                                            maxFecha = JsfUtil.getFechaSinHora(fechaIniRango);
                                        }     
                                }

                                //Si es fecha Feriado por sede o General lo marca en la lista que no van al calendario
                                if (JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(feriadoHabilita.get("FECHA").toString().substring(0,10),"yyyy-MM-dd"), 2).equals(JsfUtil.obtenerFechaXCriterio(fechaIniRango, 2))
                                    && JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(feriadoHabilita.get("FECHA").toString().substring(0,10),"yyyy-MM-dd"), 3).equals(JsfUtil.obtenerFechaXCriterio(fechaIniRango, 3))
                                    && JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(feriadoHabilita.get("FECHA").toString().substring(0,10),"yyyy-MM-dd"), 1).equals(JsfUtil.obtenerFechaXCriterio(fechaIniRango, 1))
                                    &&  feriadoHabilita.get("TIPO").toString().equals("FERIADO")) {                        
                                        //Las que no van al Calendario
                                        mapFechaCalendario = new HashMap();
                                        mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                                        if(!lstFechasNoHabilitadoCalendario.contains(mapFechaCalendario)){
                                            lstFechasNoHabilitadoCalendario.add(mapFechaCalendario);     
                                        }   
                                }            

                            }    
                        } else 
                        {
                            //Verifica hasta el rando de la fecha de los dias de reserva
                            if (fechaFin.compareTo(JsfUtil.getFechaSinHora(fechaIniRango)) > 0) {
                                //Si lo encuentra dentro del dia de semana lo agrega
                                if (diasSemanaHablitados.indexOf(String.valueOf(fechaIniRango.getDay())) != -1) {
                                    mapFechaCalendario = new HashMap();
                                    mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                                    if(!lstFechasCalendario.contains(mapFechaCalendario)){
                                        lstFechasCalendario.add(mapFechaCalendario);
                                        maxFecha = JsfUtil.getFechaSinHora(fechaIniRango);
                                    }
                                } else 
                                {
                                    //Las que no van al Calendario
                                    mapFechaCalendario = new HashMap();
                                    mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                                    if(!lstFechasNoHabilitadoCalendario.contains(mapFechaCalendario)){
                                        lstFechasNoHabilitadoCalendario.add(mapFechaCalendario);     
                                    }     
                                }        
                            } 
                            else 
                            {
                                //Las que no van al Calendario
                                mapFechaCalendario = new HashMap();
                                mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                                if(!lstFechasNoHabilitadoCalendario.contains(mapFechaCalendario)){
                                    lstFechasNoHabilitadoCalendario.add(mapFechaCalendario);     
                                }     
                            }  
                        }

                        //Aumenta un dia mas
                        fecha.setTime(fechaIniRango);
                        fecha.add(Calendar.DAY_OF_MONTH, 1);
                        fechaIniRango = fecha.getTime();
                    }
                }
                //Fin de Inserta por cada dia
                //fechaFin
            } //Fin de Proceso por dias Calendario    
            
            //Proceo por dias Habiles
            if (procesoSoloDiasHabiles = true) {
                //Fecha de Inicio
                Date fechaIni = new Date();
                Date fechaIniRango = new Date();
                Calendar fecha = Calendar.getInstance();
                fecha.setTime(fechaIni);
                maxFecha = fecha.getTime();

                //Fecha Fin
                fecha.add(Calendar.DAY_OF_MONTH, diasFinReservaCita);
                Date fechaFin = fecha.getTime();
                //Actualiza con la fecha fin
                maxFechaCalcula = fecha.getTime();

                
                //Verifica los dias habiles
                int totalDias = 1;
                int dias = diasFinReservaCita;
                fecha.setTime(fechaIniRango);
                while (totalDias < dias) {
                    //Aumenta un dia mas
                    fecha.add(Calendar.DAY_OF_MONTH, 1);
                    if (fecha.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fecha.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                        Date Ini = fecha.getTime();
                        List<Date> listaFeriados = ejbSbFeriadoFacade.listFeriados(Ini, Ini);
                        if (listaFeriados.size() == 0) {
                            totalDias++;
                        }
                    }
                }
                
                fechaFin = fecha.getTime(); //Setea la Fecha Fin
                maxFechaCalcula = fecha.getTime();
                
                //Busca las Fechas Habilitadas por Sede de manera extraordinaria
                List<Date> lstFechaHabilitadaSede = ejbFacade.listaFechaHabilitadaSede(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PROG_VER").getId(),registro.getTipoSedeId().getId(),tipoCita.getId()); 
                if (lstFechaHabilitadaSede != null) {
                    for (Date fechasHabilitadas : lstFechaHabilitadaSede) {
                        if (fechasHabilitadas.compareTo(maxFechaCalcula) > 0) {
                            maxFechaCalcula.setTime(fechasHabilitadas.getTime());
                        }
                    }
                }

                //Verifica la fecha fin
/*                if (maxFechaCalcula.compareTo(fechaFin) > 0) {
                    fechaFin.setTime(maxFechaCalcula.getTime());
                }*/
                                        

                //Se agrupa los Feriados Generales mas los Feriados de la Sede mas las fechas habilitadas (porque la fecha habilitada no debe quitarse ni por feriado/dia semana diferente a su programacion SEDE
                //List<Map> lstSedeFechaHabilitadaFeriado = ejbFacade.listaSedeFechaHabilitadaFeriado(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PROG_VER").getId(),registro.getTipoSedeId().getId(),tipoCita.getId(),fechaIni,fechaFin); 
                List<Map> lstSedeFechaHabilitadaFeriado = ejbFacade.listaSedeFechaHabilitadaFeriado(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PROG_VER").getId(),registro.getTipoSedeId().getId(),tipoCita.getId(),fechaIni,maxFechaCalcula); 
                //Inserta por cada dia
                while (maxFechaCalcula.compareTo(JsfUtil.getFechaSinHora(fechaIniRango)) >= 0) {

                    //Si no hay feriados o fechas habilitadas
                    if (lstSedeFechaHabilitadaFeriado.size() == 0) {
                        //Si lo encuentra dentro del dia de semana lo agrega
                        if (diasSemanaHablitados.indexOf(String.valueOf(fechaIniRango.getDay())) != -1) {
                            mapFechaCalendario = new HashMap();
                            mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                            if(!lstFechasCalendario.contains(mapFechaCalendario)){
                                lstFechasCalendario.add(mapFechaCalendario);     
                                maxFecha = JsfUtil.getFechaSinHora(fechaIniRango);
                            }   
                        }  else {
                            //Las que no van al Calendario
                            mapFechaCalendario = new HashMap();
                            mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                            if(!lstFechasNoHabilitadoCalendario.contains(mapFechaCalendario)){
                                lstFechasNoHabilitadoCalendario.add(mapFechaCalendario);     
                            }   
                        }
                        //Aumenta un dia mas
                        fecha.setTime(fechaIniRango);
                        fecha.add(Calendar.DAY_OF_MONTH, 1);
                        fechaIniRango = fecha.getTime();
                    }
                    else
                    {
                        //Buscamos si es un Feriado o Fecha Habilitada
                        List<Map> lstSedeFechaHabilitadaFeriadoEspefifica = ejbFacade.listaSedeFechaHabilitadaFeriadoEspefifica(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PROG_VER").getId(),registro.getTipoSedeId().getId(),tipoCita.getId(),fechaIniRango); 
                        if (lstSedeFechaHabilitadaFeriadoEspefifica.size() > 0) {
                            for (Map feriadoHabilita : lstSedeFechaHabilitadaFeriadoEspefifica) {
                                //Si es fecha habilitada si la agrega
                                if (JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(feriadoHabilita.get("FECHA").toString().substring(0,10),"yyyy-MM-dd"), 2).equals(JsfUtil.obtenerFechaXCriterio(fechaIniRango, 2))
                                    && JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(feriadoHabilita.get("FECHA").toString().substring(0,10),"yyyy-MM-dd"), 3).equals(JsfUtil.obtenerFechaXCriterio(fechaIniRango, 3))
                                    && JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(feriadoHabilita.get("FECHA").toString().substring(0,10),"yyyy-MM-dd"), 1).equals(JsfUtil.obtenerFechaXCriterio(fechaIniRango, 1))
                                    &&  feriadoHabilita.get("TIPO").toString().equals("HABILITA")) {
                                        //Si es fecha habilitada si la agrega
                                        mapFechaCalendario = new HashMap();
                                        mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                                        if(!lstFechasCalendario.contains(mapFechaCalendario)){
                                            lstFechasCalendario.add(mapFechaCalendario);     
                                            maxFecha = JsfUtil.getFechaSinHora(fechaIniRango);
                                        }     
                                }

                                //Si es fecha Feriado por sede o General lo marca en la lista que no van al calendario
                                if (JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(feriadoHabilita.get("FECHA").toString().substring(0,10),"yyyy-MM-dd"), 2).equals(JsfUtil.obtenerFechaXCriterio(fechaIniRango, 2))
                                    && JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(feriadoHabilita.get("FECHA").toString().substring(0,10),"yyyy-MM-dd"), 3).equals(JsfUtil.obtenerFechaXCriterio(fechaIniRango, 3))
                                    && JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(feriadoHabilita.get("FECHA").toString().substring(0,10),"yyyy-MM-dd"), 1).equals(JsfUtil.obtenerFechaXCriterio(fechaIniRango, 1))
                                    &&  feriadoHabilita.get("TIPO").toString().equals("FERIADO")) {                        
                                        //Las que no van al Calendario
                                        mapFechaCalendario = new HashMap();
                                        mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                                        if(!lstFechasNoHabilitadoCalendario.contains(mapFechaCalendario)){
                                            lstFechasNoHabilitadoCalendario.add(mapFechaCalendario);     
                                        }   
                                }            

                            }    
                        } else 
                        {
                            //Verifica hasta el rando de la fecha de los dias de reserva
                            if (fechaFin.compareTo(JsfUtil.getFechaSinHora(fechaIniRango)) > 0) {
                                //Si lo encuentra dentro del dia de semana lo agrega
                                if (diasSemanaHablitados.indexOf(String.valueOf(fechaIniRango.getDay())) != -1) {
                                    mapFechaCalendario = new HashMap();
                                    mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                                    if(!lstFechasCalendario.contains(mapFechaCalendario)){
                                        lstFechasCalendario.add(mapFechaCalendario);
                                        maxFecha = JsfUtil.getFechaSinHora(fechaIniRango);
                                    }
                                } else 
                                {
                                    //Las que no van al Calendario
                                    mapFechaCalendario = new HashMap();
                                    mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                                    if(!lstFechasNoHabilitadoCalendario.contains(mapFechaCalendario)){
                                        lstFechasNoHabilitadoCalendario.add(mapFechaCalendario);     
                                    }     
                                }        
                            } 
                            else 
                            {
                                //Las que no van al Calendario
                                mapFechaCalendario = new HashMap();
                                mapFechaCalendario.put("fecha", JsfUtil.dateToString(fechaIniRango,"dd/MM/yyyy"));
                                if(!lstFechasNoHabilitadoCalendario.contains(mapFechaCalendario)){
                                    lstFechasNoHabilitadoCalendario.add(mapFechaCalendario);     
                                }     
                            }  
                        }

                        //Aumenta un dia mas
                        fecha.setTime(fechaIniRango);
                        fecha.add(Calendar.DAY_OF_MONTH, 1);
                        fechaIniRango = fecha.getTime();
                    }
                }
                //Fin de Inserta por cada dia
                //fechaFin
            } //Fin de Proceso por dias Habiles    
            
        } else {
            //Fecha de Inicio
            Date fechaIni = new Date();
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(fechaIni);
            maxFecha = fecha.getTime();
        }    
        return maxFecha;
    }
    
    
    public Date getFechasStringDate(String pFecha) {
        return JsfUtil.stringToDate(pFecha,"dd/MM/yyyy");
    }
    
    public String getFechasNoHabilitadoCalendario() {
        fechasNoHabilitadas = "";
        if (lstFechasNoHabilitadoCalendario.size() > 0) {
            fechasNoHabilitadas = "[";
            for (Map fechasNoHabilitadoCalendario : lstFechasNoHabilitadoCalendario) {
                fechasNoHabilitadas = fechasNoHabilitadas
                    + "\"" + JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(fechasNoHabilitadoCalendario.get("fecha").toString().substring(0,10),"dd/MM/yyyy"), 2)
                    + "-" + JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(fechasNoHabilitadoCalendario.get("fecha").toString().substring(0,10),"dd/MM/yyyy"), 3)
                    + "-" + JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(fechasNoHabilitadoCalendario.get("fecha").toString().substring(0,10),"dd/MM/yyyy"), 1) + "\",";
            }
            fechasNoHabilitadas = fechasNoHabilitadas.substring(0, fechasNoHabilitadas.length() - 1);
            fechasNoHabilitadas += "]";
        } else {
            Date fechaIni = new Date();
            //fechasNoHabilitadas = "[\"1-1-2000\"]";
            fechasNoHabilitadas = "\"" + JsfUtil.obtenerFechaXCriterio(fechaIni, 2)
                    + "-" + JsfUtil.obtenerFechaXCriterio(fechaIni, 3)
                    + "-" + JsfUtil.obtenerFechaXCriterio(fechaIni, 1) + "\"";
        }   
        return fechasNoHabilitadas;
    }
        
    public boolean fechaBuscaHabilitada(Date fechaBusca) {
        boolean encuentraFecha = false;
        if (lstFechasCalendario.size() > 0) {
            for (Map fechasCalendario : lstFechasCalendario) {
                if (JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(fechasCalendario.get("fecha").toString().substring(0,10),"dd/MM/yyyy"), 2).equals(JsfUtil.obtenerFechaXCriterio(fechaBusca, 2))
                            && JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(fechasCalendario.get("fecha").toString().substring(0,10),"dd/MM/yyyy"), 3).equals(JsfUtil.obtenerFechaXCriterio(fechaBusca, 3))
                            && JsfUtil.obtenerFechaXCriterio(JsfUtil.stringToDate(fechasCalendario.get("fecha").toString().substring(0,10),"dd/MM/yyyy"), 1).equals(JsfUtil.obtenerFechaXCriterio(fechaBusca, 1))) 
                {
                    encuentraFecha = true;
                }
            }
        }            
        return encuentraFecha;
    }    

    public List<CitaTurProgramacion> getListaProgramaciones() {
        return listaProgramaciones;
    }

    public void setListaProgramaciones(List<CitaTurProgramacion> listaProgramaciones) {
        this.listaProgramaciones = listaProgramaciones;
    }

    public List<CitaHora> getListaHoras() {
        return listaHoras;
    }

    public void setListaHoras(List<CitaHora> listaHoras) {
        this.listaHoras = listaHoras;
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

    public SbPersona getCitaRepresentante() {
        return citaRepresentante;
    }

    public void setCitaRepresentante(SbPersona citaRepresentante) {
        this.citaRepresentante = citaRepresentante;
    }

    public List<SbPersona> getCitaLstRepresentantes() {
        return citaLstRepresentantes;
    }

    public void setCitaLstRepresentantes(List<SbPersona> citaLstRepresentantes) {
        this.citaLstRepresentantes = citaLstRepresentantes;
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

    public List<CitaTipoGamac> getListaTramites() {
        listaTramites = ejbTipoGamacFacade.lstTipoTramite();
        return listaTramites;
    }

    public CitaTipoGamac getTipoSubTramite() {
        return tipoSubTramite;
    }

    public void setTipoSubTramite(CitaTipoGamac tipoSubTramite) {
        this.tipoSubTramite = tipoSubTramite;
    }

    public void setListaTramites(List<CitaTipoGamac> listaTramites) {
        this.listaTramites = listaTramites;
    }

    public List<CitaTipoGamac> getListaSubTramites() {
        listaSubTramites = ejbTipoGamacFacade.lstTipoSubTramiteVerJur();
        return listaSubTramites;
    }

    public void setListaSubTramites(List<CitaTipoGamac> listaSubTramites) {
        this.listaSubTramites = listaSubTramites;
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

    public boolean isVisibleCalendarCentral() {
        return visibleCalendarCentral;
    }

    public void setVisibleCalendarCentral(boolean visibleCalendarCentral) {
        this.visibleCalendarCentral = visibleCalendarCentral;
    }

    public boolean isVisibleCalendarNoPresencial() {
        return visibleCalendarNoPresencial;
    }

    public void setVisibleCalendarNoPresencial(boolean visibleCalendarNoPresencial) {
        this.visibleCalendarNoPresencial = visibleCalendarNoPresencial;
    }
    
    public boolean isVisibleTipoSede() {
        return visibleTipoSede;
    }

    public void setVisibleTipoSede(boolean visibleTipoSede) {
        this.visibleTipoSede = visibleTipoSede;
    }
    
    public Date getFechaProcess() {
        return fechaProcess;
    }

    public void setFechaProcess(Date fechaProcess) {
        this.fechaProcess = fechaProcess;
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

    public List<CitaDetalleArma> getLstDetalleArma() {
        return lstDetalleArma;
    }

    public void setLstDetalleArma(List<CitaDetalleArma> lstDetalleArma) {
        this.lstDetalleArma = lstDetalleArma;
    }

    public List<CitaDetalleArma> getLstDetalleArmaSelected() {
        return lstDetalleArmaSelected;
    }

    public void setLstDetalleArmaSelected(List<CitaDetalleArma> lstDetalleArmaSelected) {
        this.lstDetalleArmaSelected = lstDetalleArmaSelected;
    }

    public CitaDetalleArma getDetalleArma() {
        return detalleArma;
    }

    public void setDetalleArma(CitaDetalleArma detalleArma) {
        this.detalleArma = detalleArma;
    }

    public List<Map> getListArmas() {
        return listArmas;
    }

    public void setListArmas(List<Map> listArmas) {
        this.listArmas = listArmas;
    }

    public boolean isVisibleBtnBuscarArma() {
        return visibleBtnBuscarArma;
    }

    public void setVisibleBtnBuscarArma(boolean visibleBtnBuscarArma) {
        this.visibleBtnBuscarArma = visibleBtnBuscarArma;
    }

    public boolean isVisibleBtnCargarArma() {
        return visibleBtnCargarArma;
    }

    public void setVisibleBtnCargarArma(boolean visibleBtnCargarArma) {
        this.visibleBtnCargarArma = visibleBtnCargarArma;
    }

    public boolean isDisabledTramite() {
        return disabledTramite;
    }

    public void setDisabledTramite(boolean disabledTramite) {
        this.disabledTramite = disabledTramite;
    }

    public String getFiltroArmas() {
        return filtroArmas;
    }

    public void setFiltroArmas(String filtroArmas) {
        this.filtroArmas = filtroArmas;
    }

    public List<AmaTarjetaPropiedad> getDatosTarjetasPropiedad() {
        return datosTarjetasPropiedad;
    }

    public void setDatosTarjetasPropiedad(List<AmaTarjetaPropiedad> datosTarjetasPropiedad) {
        this.datosTarjetasPropiedad = datosTarjetasPropiedad;
    }

    public AmaTarjetaPropiedad getTarjetaSelected() {
        return tarjetaSelected;
    }

    public void setTarjetaSelected(AmaTarjetaPropiedad tarjetaSelected) {
        this.tarjetaSelected = tarjetaSelected;
    }

    public List<Map> getArmasMap() {
        return armasMap;
    }

    public void setArmasMap(List<Map> armasMap) {
        this.armasMap = armasMap;
    }

    public List<Map> getDiscaMapLst() {
        return discaMapLst;
    }

    public void setDiscaMapLst(List<Map> discaMapLst) {
        this.discaMapLst = discaMapLst;
    }

    public AmaModelos getSelectedArma() {
        return selectedArma;
    }

    public void setSelectedArma(AmaModelos selectedArma) {
        this.selectedArma = selectedArma;
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

    public boolean isEligeReciboLista() {
        return eligeReciboLista;
    }

    public void setEligeReciboLista(boolean eligeReciboLista) {
        this.eligeReciboLista = eligeReciboLista;
    }

    public SbParametro getParamOriDatArma() {
        return paramOriDatArma;
    }

    public void setParamOriDatArma(SbParametro paramOriDatArma) {
        this.paramOriDatArma = paramOriDatArma;
    }

    public String getDesdeMigra() {
        return desdeMigra;
    }

    public void setDesdeMigra(String desdeMigra) {
        this.desdeMigra = desdeMigra;
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
                    if (ejbPersonaFacade.buscarPersonaXNroDoc(perExamen.getNumDoc()).isEmpty()) {
                        if (perExamen.getTipoDoc().getCodProg().equals("TP_DOCID_DNI")) {
                            //WEB SERVICE PARA CREAR PERSONA DESDE RENIEC
                            perExamen = wsPideController.buscarReniec(perExamen.getNumDoc());
                            perExamen = (CitaTurPersona) JsfUtil.entidadMayusculas(perExamen, "");
                            ejbPersonaFacade.create(perExamen);
                        } else {
                            perExamen.setActivo((short) 1);
                            perExamen.setAudLogin(loginController.getUsuario().getLogin());
                            perExamen.setAudNumIp(JsfUtil.getNumIP());
                            perExamen.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
                            perExamen.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_DNI"));
                            perExamen = (CitaTurPersona) JsfUtil.entidadMayusculas(perExamen, "");
                            ejbPersonaFacade.create(perExamen);
                        }
                    } else {
                        perExamen = ejbPersonaFacade.buscarPersonaXNroDoc(perExamen.getNumDoc()).get(0);
                    }

                    if (ejbPersonaFacade.buscarPersonaXNroRuc(perPago.getRuc()).isEmpty()) {
                        SbPersona empresa = ejbSbPersonaFacade.findByRuc(perPago.getRuc());
                        perPago.setActivo((short) 1);
                        perPago.setAudLogin(loginController.getUsuario().getLogin());
                        perPago.setAudNumIp(JsfUtil.getNumIP());
                        perPago.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_JUR"));
                        perPago.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_RUC"));
                        perPago.setRznSocial(empresa.getRznSocial());
                        perPago = (CitaTurPersona) JsfUtil.entidadMayusculas(perPago, "");
                        ejbPersonaFacade.create(perPago);
                    } else {
                        perPago = ejbPersonaFacade.buscarPersonaXNroRuc(perPago.getRuc()).get(0);
                    }

                    registro.setTipoTramiteId(tipoTramite);
                    registro.setPerPagoId(perPago);
                    registro.setPerExamenId(perExamen);
                    registro.setActivo((short) 1);
                    registro.setAudLoginTurno(loginController.getUsuario().getLogin());
                    registro.setAudNumIpTurno(JsfUtil.getNumIP());
                    registro.setEstado(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_EST_PEN"));
                    registro.setFechaPrograma(new Date());

                    List<CitaTurTurno> lstTurno = ejbFacade.listarTurnosXFechaTurno(registro.getFechaTurno(), registro.getProgramacionId().getId(), registro.getTipoSedeId().getId());
                    if (lstTurno.isEmpty()) {
                        registro.setNroTurno(1);
                    } else {
                        registro.setNroTurno(lstTurno.size() + 1);
                    }

                    //registro.setNroTurno(ejbTurProgramacionFacade.lstTurProgramacion(tipoProg).get(0).getCantCupos() + 1 - registro.getProgramacionId().getCantCupos());
                    //registro.setSedeId(ejbTurSedeFacade.buscarSedeXCodProg("TP_SEDE_LIMA"));
//                    registro.setTipoSedeId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_AREA_TRAM"));
                    registro.setVezTurno(1);

                    //List<CitaAmaTurnoActa> actaLst = new ArrayList();                    
                    List<CitaTurLicenciaReg> licLst = new ArrayList();
                    for (CitaDetalleArma det : lstDetalleArma) {
                        if (det.getParaLista() == 1) {
                            CitaTurLicenciaReg licReg = new CitaTurLicenciaReg();
                            licReg.setNumLic(det.getNroLicencia());
                            licReg.setSerie(det.getNroSerie());
                            licReg.setTurnoId(registro);
                            licReg.setActivo((short) 1);
                            licReg.setAudLogin(loginController.getUsuario().getLogin());
                            licReg.setAudNumIp(JsfUtil.getNumIP());
                            licReg.setSituacionId(det.getSituacionId());
                            licReg.setTipoEstado(det.getEstadoId());
                            if (det.getArmaId() != null) {
                                licReg.setArmaId(det.getArmaId());
                                licReg.setTipoEstado(ejbTipoGamacFacade.find(det.getArmaId().getEstadoId().getId()));
                                licReg.setSituacionId(ejbTipoGamacFacade.find(det.getArmaId().getSituacionId().getId()));
                                licReg.setModelo(det.getArmaId().getModeloId());
                            }

                            CitaTurComprobante newComprobante = null;
                            if(tipoSubTramite.getCodProg().equals("TP_PROG_VER_ANR"))
                            {
                                solicitarRecibo=false;
                                solicitarVoucherTemp=false;
                            }
                           /* GUARDAR COMPROBRANTE */
                            if (solicitarRecibo || solicitarVoucherTemp) {

                                newComprobante = new CitaTurComprobante();
                                newComprobante.setBancoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_BCO_NAC"));
                                newComprobante.setVoucherAutentica("TUPA-2018");
                                /*if (solicitarVoucherTemp) {
                                    newComprobante.setVoucherTasa(registro.getMontoEmpoce().toString());
                                    newComprobante.setVoucherSeq(registro.getSecuenciaEmpoce().toString());
                                    newComprobante.setFechaPago(registro.getFechaEmpoce());

                                    Date fecha = new Date();     
                                    long num = ejbSbNumeracionFacade.selectNumero("TP_NUM_VOUC_BN");
                                    String path = pe.gob.sucamec.renagi.jsf.util.ReportUtil.mostrarAnio(fecha) + File.separator +  pe.gob.sucamec.renagi.jsf.util.ReportUtil.mostrarMes(fecha) + File.separator;
                                    int index = archivo.lastIndexOf('.');
                                    String ext = archivo.substring(index); //archivo.substring(index + 1);
                                    String reciboFileName = path + num + ext;

                                    try {
                                        FileUtils.writeByteArrayToFile(new File((ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_depositoPol").getValor())+ reciboFileName), fotoByte);                                
                                    } catch (IOException ex) {
                                        JsfUtil.mensajeError(ex, JsfUtil.bundle("ErrorDePersistencia"));
                                        //Logger.getLogger(TurTurnoController.class.getName()).log(Level.SEVERE, null, ex);
                                    }                            
                                    newComprobante.setVoucherAgencia(reciboFileName);
                                }*/

                                if (solicitarRecibo) {
                                    newComprobante.setVoucherTasa(det.getReciboArma().getImporte().toString());
                                    newComprobante.setVoucherSeq(det.getReciboArma().getNroSecuencia().toString());
                                    newComprobante.setFechaPago(det.getReciboArma().getFechaMovimiento());
                                    newComprobante.setVoucherCta(det.getReciboArma().getId().toString());
                                    newComprobante.setNroDocComprador(det.getReciboArma().getNroDocumento().toString());
                                    //registro.setSecuenciaEmpoce(reciboBn.getNroSecuencia());
                                    //registro.setFechaEmpoce(reciboBn.getFechaMovimiento());
                                    //registro.setMontoEmpoce(BigDecimal.valueOf(reciboBn.getImporte()));
                                }

                                newComprobante.setAudLoginTurno(JsfUtil.getLoggedUser().getLogin());
                                newComprobante.setAudNumIpTurno(JsfUtil.getNumIP());
                                newComprobante.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                                newComprobante.setAudNumIp(JsfUtil.getNumIP());
                                newComprobante.setActivo((short) 1);

                                newComprobante = (CitaTurComprobante) JsfUtil.entidadMayusculas(newComprobante, "");
                                ejbTurComprobanteFacade.create(newComprobante);
                            }
                            /* */
                            licReg.setComprobanteId(newComprobante);

                            licLst.add(licReg);
                            /*CitaAmaTurnoActa acta = new CitaAmaTurnoActa();
                            acta.setTurnoId(registro);
                            acta.setNroLic(det.getNroLicencia());
                            acta.setSerie(det.getNroSerie());
                            acta.setAudLogin(loginController.getUsuario().getLogin());
                            acta.setAudNumIp(JsfUtil.getNumIP());
                            acta.setActivo((short) 1);
                            acta = (CitaAmaTurnoActa) JsfUtil.entidadMayusculas(acta, "");
                            actaLst.add(acta);*/
                        }
                    }
                    //registro.setTurL (actaLst);                    
                    registro.setTurLicenciaRegList(licLst);
                    registro.setSubTramiteId(tipoSubTramite);
                    //COMENTAR
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

                    registro = (CitaTurTurno) JsfUtil.entidadMayusculas(registro, "");
                    ejbFacade.create(registro);

                    if (registro.getId() != null) {
                        JsfUtil.addSuccessMessage("Cita reservada correctamente");
                        siguiente("paso4");
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
                turnoRep.setNroTurno(ejbTurProgramacionFacade.lstTurProgramacion(tipoProg).get(0).getCantCupos() + 1 - registro.getProgramacionId().getCantCupos());
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

        //VALIDACION WSPIDE
//        if (perExamen.getTipoDoc().getCodProg().equals("TP_DOCID_DNI")) {
//            if (wsPideController.buscarReniec(perExamen.getNumDoc(), perExamen.getNumDocVal()) == null) {
//                flagValidar = false;
//            }
//        }
        if (visibleNroDocVal && !JsfUtil.ValidarDniRuc(perExamen.getNumDoc() + perExamen.getNumDocVal(), "DNI")) {
            flagValidar = false;
            JsfUtil.addErrorMessage("El DNI no es válido.");
        }

//        if (perPago.getRuc() != null) {
//            if (wsPideController.buscarSunat(perPago.getRuc()) == null) {
//                flagValidar = false;
//            }
//        }

        /* VALIDAR QUE LOS COMPROBANTES NO SE REPITAN MAS DE 3 VECES */
//        List<TurComprobante> tcList = ejbTurComprobanteFacade.listarComprobantesActivos();
//        
//        for (TurComprobante tc : listaComprobantes) {
//            for (TurComprobante tcBD : tcList) {
//                if (tc.getComprobanteCompleto().equals(tcBD.getComprobanteCompleto())) {
//                    boolean compVenc = false;
//                    if (tcBD.getTurConstanciaList().size() == 3) {
//                        JsfUtil.addWarningMessage("El comprobante " + tc.getComprobanteCompleto() + " ya ha sido usado 3 veces");
//                        flagValidar = false;
//                    }
//                    for (TurTurno tt : tcBD.getTurTurnoList()) {
//                        if (JsfUtil.getFechaSinHora(hoy).compareTo(JsfUtil.getFechaSinHora(tt.getFechaTurno())) > 30 && tt.getActivo() == 1 && tt.getFechaConfirma() != null) {
//                            compVenc = true;
//                        }
//                    }
//                    if (compVenc) {
//                        JsfUtil.addWarningMessage("El comprobante " + tc.getComprobanteCompleto() + " ya caducó (30 días desde la primera vez que se usó)");
//                        flagValidar = false;
//                    }
//                }
//            }
//        }
        if (flagValidar == true) {
//            if (!ejbPersonaFacade.buscarPersonaXNroDoc(perExamen.getNumDoc()).isEmpty()) {
//                TurPersona p = ejbPersonaFacade.buscarPersonaXNroDoc(perExamen.getNumDoc()).get(0);
//                for (TurTurno tt : ejbFacade.listarTurnosXPersonaYDia(p.getId(), JsfUtil.getFechaSinHora(registro.getFechaTurno()))) {
////                    if (Objects.equals(tt.getProgramacionId(), selectedProgra) && Objects.equals(JsfUtil.getFechaSinHora(fechaTurno), JsfUtil.getFechaSinHora(tt.getFechaTurno()))) {
////                        JsfUtil.addWarningMessage("Ya tiene un cita programado a la misma hora del mismo día");
////                        flagValidar = false;
////                    }
//                    if (JsfUtil.getFechaSinHora(registro.getFechaTurno()).compareTo(JsfUtil.getFechaSinHora(tt.getFechaTurno())) == 0
//                            || JsfUtil.getFechaSinHora(registro.getFechaTurno()).toString().equals(JsfUtil.getFechaSinHora(tt.getFechaTurno()))) {
//                        JsfUtil.addWarningMessage("Ya tiene una cita programada en el mismo día");
//                        flagValidar = false;
//                    }
//                }
//            }

//            tipoTramite = ejbTipoGamacFacade.buscarTipoGamacXId(tipoTramite.getId());
            /*if (!registro.getTipoTramiteId().getCodProg().equals("TP_TRA_DEF")
                    && !registro.getTipoTramiteId().getCodProg().equals("TP_TRA_DEP")
                    && !registro.getTipoTramiteId().getCodProg().equals("TP_TRA_CAZ")
                    && !registro.getTipoTramiteId().getCodProg().equals("TP_TRA_COL")
                    && !registro.getTipoTramiteId().getCodProg().equals("TP_TRA_MUL")) {
                if (ejbRma1369Facade.buscarVigilanteEmpresa(perPago.getRuc(), perExamen.getNumDoc()).isEmpty()) {
                    JsfUtil.addWarningMessage("Usted no cuenta con carnet de identidad de seguridad privada vigente");
                    flagValidar = false;
                }
            }*/
            
            
            //Valida que la Fecha del Turno sea una de la lista de Fechas Habilitadas para esta Sede
            flagValidar = fechaBuscaHabilitada(registro.getFechaTurno());
            if (flagValidar == false) {
                JsfUtil.addWarningMessage("La fecha no esta Habilitada");
                getMaxFechaGeneral(); //Carga las Fechas de la Sede seleccionada
            }
            
            //Valida si es una Fecha Habilita que el turno este registrado como turnos especifico
            List<Map> lstSedeTurnosCuposFechaHabilitada = ejbFacade.listaSedeTurnosCuposFechaHabilitada(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PROG_VER").getId(),registro.getTipoSedeId().getId(),tipoCita.getId(),registro.getFechaTurno()); 
            if (lstSedeTurnosCuposFechaHabilitada.size() > 0){
                    boolean encuentraTurno = false;
                    String lsturnoNoEncontrado = "";
                    for (Map tp : lstSedeTurnosCuposFechaHabilitada) {
                        if (registro.getProgramacionId().getId() == Long.parseLong(tp.get("ID_PROGRAMACION").toString()))  {
                            encuentraTurno = true;  
                            lsturnoNoEncontrado = tp.get("HORA").toString();
                        }       
                    }
                    if (encuentraTurno == false) {
                        JsfUtil.addWarningMessage("El turno " + lsturnoNoEncontrado + " no esta habilitado");
                        flagValidar = false;
                        cargarTurnosDisponibles();
                    }    
            }
            
            List<CitaTurTurno> lstTurno = ejbFacade.listarTurnosXFechaTurno(registro.getFechaTurno(), registro.getProgramacionId().getId(), registro.getTipoSedeId().getId());
            CitaTurProgramacion progra = ejbTurProgramacionFacade.buscarTurProgramacionXId(registro.getProgramacionId().getId());
            if (lstTurno.size() >= progra.getCantCupos()) {
                JsfUtil.addWarningMessage("Los cupos para el turno seleccionado ya han sido ocupados, seleccione otro horario");
                flagValidar = false;
                cargarTurnosDisponibles();

            }
        }

        if (validarVoucherTupa2018()) {
            JsfUtil.addErrorMessage("El recibo ingresado ya esta siendo usado en otra cita");
            flagValidar = false;
        }

        return flagValidar;
    }

    public void restaurarProgramacion() {
        registro = new CitaTurTurno();
        listaProgramaciones = new ArrayList();
//        codVerificacion = null;

        //cargarTasasTupa();
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
        tipoTramite = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_TRAM_VER");
        tipoSubTramite = null;
        lstDetalleArma = new ArrayList<CitaDetalleArma>();
        setVisibleBtnBuscarArma(false);
        setDisabledTramite(false);

        perExamen = new CitaTurPersona();
        perPago = new CitaTurPersona();
        perPago.setRuc(loginController.getUsuario().getNumDoc());
        perPago.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_RUC"));

        citaLstRepresentantes = ejbSbPersonaFacade.listxRepresentante(loginController.getUsuario().getPersona().getId());
        citaRepresentante = null;

        tipoProg = "TP_PROG_VER";

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
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = CitaTurTurno.class)
    public static class CItasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CitasController c = (CitasController) JsfUtil.obtenerBean("citasController", CitasController.class);
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

    public void eligeRepresentante(SelectEvent event) {
        if (event.getObject() != null) {
            //Syso("Representante: " + event.getObject().toString());
            String dniPerNat = event.getObject().toString();
            citaRepresentante = ejbSbPersonaFacade.find(Long.valueOf(dniPerNat));
            perExamen.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_DNI"));
            perExamen.setNumDoc(citaRepresentante.getNumDoc());
            perExamen.setNumDocVal(citaRepresentante.getNumDocVal());
            perExamen.setApePat(citaRepresentante.getApePat());
            perExamen.setApeMat(citaRepresentante.getApeMat());
            perExamen.setNombres(citaRepresentante.getNombres());

        } else {
            citaRepresentante = null;
            perExamen = null;
        }
    }

    public void eligeSubTramite(SelectEvent event) {
        if (event.getObject() != null) {
            String idSub = event.getObject().toString();
            CitaTipoGamac subTram = ejbTipoGamacFacade.find(Long.valueOf(idSub));
            if (subTram != null) {
                tipoSubTramite = subTram;
                switch (subTram.getCodProg()) {
                    case "TP_PROG_VER_TRA":
                    case "TP_PROG_VER_TPN":
                    case "TP_PROG_VER_TPJ":
                    case "TP_PROG_VER_TFA":
                    case "TP_PROG_VER_RDA":
                    case "TP_PROG_VER_CM":
                    case "TP_PROG_VER_ANR":
                    case "TP_PROG_VER_TNJ": // PARA PERSONAS NAT/JR
                    case "TP_PROG_VER_FAP": // PARA MIEMBRO FFAA/PNP
                        setVisibleBtnCargarArma(false);
                        setVisibleBtnBuscarArma(true);
                        break;
                    case "TP_PROG_VER_RLU":
                        setVisibleBtnCargarArma(true);
                        setVisibleBtnBuscarArma(false);
                        break;
                }
                cargarTasasTupa(tipoSubTramite.getCodProg());
                parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaIdByTipoProceso("TP_TUP_SUC1", tipoSubTramite.getCodProg());  // TUPA 2018, Proceso                
                RequestContext.getCurrentInstance().update("tabGestion:crearCitaVerJurForm:panelPaso1");
            }
        } else {
            setVisibleBtnCargarArma(false);
            setVisibleBtnBuscarArma(false);
            tipoSubTramite = null;
        }
    }

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
                //Integer horaInt;
                //horaInt = Integer.parseInt(hora.substring(0, 2));
//                String car1 = hora.substring(0, 1);
                //String min;
//                if (car1.equals("1")) {
                //horaProgra = hora.substring(0, 5);
                //horaTolera = hora.substring(0, 4) + "5";
                //min = hora.substring(3, 5);
//                } else {
//                    horaProgra = hora.substring(0, 4);
//                    horaTolera = hora.substring(0, 3) + "5";
//                    min = hora.substring(2, 4);
//                }
                /*switch (min) {
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
                }*/
            }
        }
        return proceed ? next : current;
    }

    public void siguiente(String p) {
        switch (p) {
            case "paso12":
                validaPaso1();
                RequestContext.getCurrentInstance().update("tabGestion:crearCitaVerJurForm");
                break;
            case "paso23":
                validaPaso2();
                break;
            case "paso34":
                break;
            case "rpaso12":
                if (listaComprobantes.isEmpty()) {
                    JsfUtil.addErrorMessage("Busque la cita que desea reprogramar");
                } else {
                    paso = "paso2";
                }
                break;
            case "rpaso23":
                if ((registro.getProgramacionId() == null)) {
                    //proceed only when data was selected and user is moving to the next step
                    JsfUtil.addErrorMessage("Debe seleccionar la hora de la cita");
                } else {
                    //String horaProg = registro.getProgramacionId().getHora();
                    //Integer horaInt;

//                    String car1 = horaProg.substring(0, 1);
                    //String min;
//                    if (car1.equals("1")) {
                    //horaProgra = horaProg.substring(0, 5);
                    //horaTolera = horaProg.substring(0, 4) + "5";
                    //min = horaProg.substring(3, 5);
                    //horaInt = Integer.parseInt(horaProg.substring(0, 2));
//                    } else {
//                        horaProgra = horaProg.substring(0, 4);
//                        horaTolera = horaProg.substring(0, 3) + "5";
//                        min = horaProg.substring(2, 4);
//                        horaInt = Integer.parseInt(horaProg.substring(0, 1));
//                    }
                    /*switch (min) {
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
                    }*/
                    captcha = new Captcha();
                    terminos = false;
                    paso = "paso3";
                }
                break;
            default:
                paso = p;
                break;
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

    public void cargarTurnosDisponibles() {
        if (fechaTurnoSeleccionada != null) {
            try {
                listaHoras = new ArrayList();
                registro.setFechaTurno(JsfUtil.stringToDate(fechaTurnoSeleccionada.substring(7,17),"dd/MM/yyyy")); 
                fechaProcess = registro.getFechaTurno();
                //listaProgramaciones = ejbTurProgramacionFacade.lstTurProgramacionXTipoCita(registro.getTipoSedeId().getId(), tipoProg, getTipoCita().getCodProg());

                /*for (CitaTurProgramacion tp : listaProgramaciones) {
                    List<CitaTurTurno> lstTurno = new ArrayList();
                    lstTurno = ejbFacade.listarTurnosXFechaTurno(registro.getFechaTurno(), tp.getId(), registro.getTipoSedeId().getId());
                    tp.setCantCupos(tp.getCantCupos() - lstTurno.size());
                }*/
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateInString = "26/07/2019";
                Date fechaFormateada = formatter.parse(dateInString);

                //Verifica si la fecha esta habilitada con programacion de turnos especificos
                List<Map> lstSedeTurnosCuposFechaHabilitada = ejbFacade.listaSedeTurnosCuposFechaHabilitada(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PROG_VER").getId(),registro.getTipoSedeId().getId(),tipoCita.getId(),registro.getFechaTurno()); 
                if (lstSedeTurnosCuposFechaHabilitada.size() > 0){
                    for (Map tp : lstSedeTurnosCuposFechaHabilitada) {
                        CitaHora cHora = new CitaHora();
                        cHora.setId(Long.parseLong(tp.get("ID_PROGRAMACION").toString()));
                        cHora.setHora(tp.get("HORA").toString());
                        cHora.setCantCupos(Long.parseLong(tp.get("CANT_CUPOS").toString()));
                        cHora.setCantCuposAct(Long.parseLong(tp.get("CANT_LIBRES").toString()));
                        cHora.setSedeId(registro.getTipoSedeId()); //ejbTipoBaseFacade.buscarTipoBaseXCodProg(tp.get("COD_PROG").toString())
                        if (cHora.getCantCuposAct() <= 0) {
                            cHora.setSeleccionable(false);
                        } else {
                            cHora.setSeleccionable(true);
                        }
                        listaHoras.add(cHora);
                    }
                } else
                {    
                    List<Map> lstSedeTurnosCupos = ejbFacade.listaSedeTurnosCupos(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PROG_VER").getId(),registro.getTipoSedeId().getId(),tipoCita.getId(),registro.getFechaTurno()); 
                    if (lstSedeTurnosCupos.size() > 0){
                        for (Map tp : lstSedeTurnosCupos) {
                            CitaHora cHora = new CitaHora();
                            cHora.setId(Long.parseLong(tp.get("ID_PROGRAMACION").toString()));
                            cHora.setHora(tp.get("HORA").toString());
                            cHora.setCantCupos(Long.parseLong(tp.get("CANT_CUPOS").toString()));
                            cHora.setCantCuposAct(Long.parseLong(tp.get("CANT_LIBRES").toString()));
                            cHora.setSedeId(registro.getTipoSedeId()); //ejbTipoBaseFacade.buscarTipoBaseXCodProg(tp.get("COD_PROG").toString())
                            if (cHora.getCantCuposAct() <= 0) {
                                cHora.setSeleccionable(false);
                            } else {
                                cHora.setSeleccionable(true);
                            }
                            listaHoras.add(cHora);
                        }
                    }
                }                
                
                /*listaHoras = new ArrayList();
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
                */
                        
                //        visibleRegDatos = true;            
            } catch (ParseException ex) {
                Logger.getLogger(CitasPoligonoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }

    public boolean mostrarSelectorHora(CitaHora item) {
        boolean flagValido = true;
        List<CitaTurTurno> lstTurno = new ArrayList();
        if (item != null) {
            if (item.getId() != null) {
                //item = ejbTurProgramacionFacade.buscarTurProgramacionXId(item.getId());
                /*lstTurno = ejbFacade.listarTurnosXFechaTurno(registro.getFechaTurno(), item.getId(), registro.getTipoSedeId().getId());
                long canCupos = item.getCantCupos() - lstTurno.size();
                item.setCantCuposAct(item.getCantCupos() - lstTurno.size());
                if (canCupos <= 0) {
                    flagValido = false;
                }*/
                if (item.getCantCuposAct() <= 0) {
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
                item = ejbTurProgramacionFacade.buscarTurProgramacionXId(item.getId());
                lstTurno = ejbFacade.listarTurnosXFechaTurno(registro.getFechaTurno(), item.getId(), registro.getTipoSedeId().getId());
                //item.setCantCupos(item.getCantCupos() - lstTurno.size());
                long canCupos = item.getCantCupos() - lstTurno.size();
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

    public String prepareModulo() {
        restaurarProgramacion();
        registro = new CitaTurTurno();
        registro.setAudLogin(loginController.getUsuario().getLogin());
        registro.setAudNumIp(JsfUtil.getNumIP());
        registro.setActivo((short) 1);

        //listaArmas();
        return "/aplicacion/citas/verificacionJur/ReservarCitaJur";
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

    public void validaPaso1() {
        listaComprobantes = new ArrayList();
        visibleNroRuc = false;
        visibleAgrComprobante = true;
        boolean faltaDato = false;
//      codVerificacion = null;

        if (tipoTramite != null) {
            //registro.setTipoTramiteId(tipoTramite);   

            //Validar que el administrado tenga licencia nueva
            /*if(ejbGamacAmaLicenciaDeUsoFacade.selectPersonaLicencia(perExamen.getNumDoc()).size()<=0){
                JsfUtil.addErrorMessage("El adminsitrado debe contar con una Licencia de uso Vigente de acuerdo al nuevo reglamento!");
                faltaDato=true;
            }*/
            if (tipoSubTramite == null) {
                JsfUtil.addErrorMessage("Debe elegir el tipo de trámite para la verificación!");
                faltaDato = true;
            }

            if (citaRepresentante == null) {
                JsfUtil.addErrorMessage("Debe elegir el representante!");
                faltaDato = true;
            }

            if (lstDetalleArma.size() <= 0) {
                JsfUtil.addErrorMessage("Debe agregar por lo menos un arma!");
                faltaDato = true;
            }

            if (!faltaDato) {
                paso = "paso2";
                visibleSiguiente3 = true;
            }
        } else {
            JsfUtil.addErrorMessage(JsfUtil.bundle("CitaArmas_paso1TipTram_Msg"));
        }
    }

    public void validaPaso2() {

        if (validarVoucherTupa2018()) {
            JsfUtil.addErrorMessage("Un recibo ingresado ya está siendo usado en otra cita");
        } else if (validarCitasPendientes()) {
            String horaProg = registro.getProgramacionId().getHora();
            calculaHoraPresentaTolerancia(horaProg);
                
            /*String horaProg = registro.getProgramacionId().getHora();
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
            }*/
            captcha = new Captcha();
            terminos = false;

            paso = "paso3";
            RequestContext.getCurrentInstance().execute("$(\"div[id*='panelPaso3_content']\").attr(\"style\", \"padding:2px!important\")");
        }
    }
    
    public void calculaHoraPresentaTolerancia(String hora) {
        String horaTurno = hora;
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

        horaProgra = horaT + ":" + minutoT;
        
        //calculando tolerancia
        String horaI = horaT;
        int minutoI = Integer.parseInt(minutoT) + minutosTolerancia; //5

        if (minutoI >= 60) {
            if ((Integer.parseInt(horaT) + (minutoI / 60)) <= 9) { 
                horaI = "0" + (Integer.parseInt(horaT) + (minutoI / 60));
            }else {     
                horaI = String.valueOf((Integer.parseInt(horaT) + (minutoI / 60)));
            }   
            minutoI = (minutoI % 60);
        }
        if (minutoI >= 10) { 
            horaTolera = horaI + ":" + minutoI;
        } else {
            horaTolera = horaI + ":0" + minutoI;
        }
        //Fin calculando Tolerancia

        //calculando Minutos Antes
        String horaA = horaT;
        //int minutoA = Integer.parseInt(minutoT) - minutosAntesInicio; //15
        int minutoA = Integer.parseInt(minutoT); //15

        if (Integer.parseInt(minutoT) < minutosAntesInicio) {
            if (minutosAntesInicio >= 60) {
                if ((Integer.parseInt(horaT) - (minutosAntesInicio / 60)) <= 9) { 
                    horaA = "0" + (Integer.parseInt(horaT) - (minutosAntesInicio / 60));
                }else {     
                    horaA = String.valueOf((Integer.parseInt(horaT) - (minutosAntesInicio / 60)));
                }   
                minutoA = minutoA - (minutosAntesInicio % 60);
            } else {
                if ((Integer.parseInt(minutoT) - minutosAntesInicio) < 0) {
                    if ((Integer.parseInt(horaT) - 1) <= 9) { 
                        horaA = "0" + (Integer.parseInt(horaT) - 1);
                    }else {     
                        horaA = String.valueOf((Integer.parseInt(horaT) - 1));
                    }   
                    minutoA = 60 - (minutosAntesInicio - Integer.parseInt(minutoT));
                }    
            }     
        } else {
            minutoA = Integer.parseInt(minutoT) - minutosAntesInicio; 
        }
        if (minutoA >= 10) { 
            horaPresen = horaA + ":" + minutoA;
        } else {
            horaPresen = horaA + ":0" + minutoA;
        }
        //Fin calculando Minutos Antes
        
        if (minutosAntesInicio == 0) {
            mensajePaso3Nota1 = "Debe presentarse a la hora de inicio";
        } else {
            mensajePaso3Nota1 = "Debe presentarse " + minutosAntesInicio + " minutos antes de la hora de inicio";
        }    
        
        if (minutosTolerancia == 0) {
            mensajePaso3Nota2 = "No se consideran minutos de tolerancia, caso contrario perderá su cita.";
        } else {
            mensajePaso3Nota2 = "Se considera " + minutosTolerancia + " minutos de tolerancia después de la hora de inicio, caso contrario perderá su cita.";
        }       
    }


    /**
     * Funcion para validar si administrado tiene citas pendientes o examen
     * rendido
     *
     * @return boolean
     */
    public boolean validarCitasPendientes() {
        boolean sinPendiente = false;
        Date fecha1 = JsfUtil.getFechaSinHora(fechaProcess);
        Date fecha2 = JsfUtil.getFechaSinHora(JsfUtil.getFechaSinHora(registro.getFechaTurno()));

        Date fecha3 = JsfUtil.getFechaSinHora(maxFecha);

        if (fecha3.compareTo(fecha2) < 0) {
            JsfUtil.addErrorMessage("El turno seleccionado no es válido");
        } else if ((registro.getProgramacionId() == null)) {
            JsfUtil.addErrorMessage("Debe seleccionar la hora de la cita");
        } else if (!Objects.equals(fecha1, fecha2)) {
            JsfUtil.addErrorMessage("El turno seleccionado no es válido");
        } else {

            List<CitaTurTurno> ttList = ejbFacade.listarTurnosXNroDocXTipo(perExamen.getNumDoc(), "VER");
            //Syso("ttList: " + ttList.size());
            boolean existeProgramado = false;
            Date hoyD = new Date();

            for (CitaTurTurno tt : ttList) {
                //VALIDAR QUE LA PERSONA NO TENGA OTRAS CITAS PENDIENTES
                Date fechaTur = JsfUtil.getFechaSinHora(tt.getFechaTurno());
                Date fechaHoy = JsfUtil.getFechaSinHora(hoyD);
                if (!existeProgramado) {
                    if (!tt.getEstado().getCodProg().equals("TP_EST_CON")) {
                        if (fechaTur.compareTo(fechaHoy) > 0) {
                            existeProgramado = true;
                        } else if (fechaTur.compareTo(fechaHoy) == 0) {
                            existeProgramado = validarHorario(tt);
                        }
                    }
                }
            }
            if (existeProgramado) {
                JsfUtil.addErrorMessage("Ya existe un turno registrado para el mismo administrado");
                paso = "paso2";
                sinPendiente = false;
            } else {
                sinPendiente = true;
            }
        }

        return sinPendiente;
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

    public boolean validarTurno(CitaTurTurno tur) {
        List<CitaTurTurno> ttList = ejbFacade.listarTurnosXNroDoc(perExamen.getNumDoc());

        boolean existeProgramado = false;
        Date hoyD = new Date();

        for (CitaTurTurno tt : ttList) {
            //VALIDAR QUE LA PERSONA NO TENGA OTRAS CITAS PENDIENTES
            Date fechaTur = JsfUtil.getFechaSinHora(tt.getFechaTurno());
            Date fechaHoy = JsfUtil.getFechaSinHora(hoyD);
            if (!existeProgramado) {
                if (!tt.getEstado().getCodProg().equals("TP_EST_CON")) {
                    if (fechaTur.compareTo(fechaHoy) > 0) {
                        existeProgramado = true;
                    } else if (fechaTur.compareTo(fechaHoy) == 0) {
                        existeProgramado = validarHorario(tt);
                    }
                }
            }
        }
        if (existeProgramado) {
            JsfUtil.addErrorMessage("Ya existe un turno registrado para la misma persona");
            paso = "paso3";
        }

        return existeProgramado;
    }

    public boolean validarHorario(CitaTurTurno tur) {
//        Date hoy = new Date();
//        hoy = JsfUtil.getFechaSinHora(fecha);
//        if (ejbFacade.listarTurnosXVoucher(secuencia, importe, fecha).isEmpty()) {
//            return false;
//        }
//        TurTurno tur = ejbFacade.listarTurnosXVoucher(secuencia, importe, fecha).get(0);
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
//                            List<TurTurno> ttList = constancia.getComprobanteId().getTurTurnoList();
//
//                            Collections.sort(ttList, new Comparator<TurTurno>() {
//                                @Override
//                                public int compare(TurTurno p1, TurTurno p2) {
//                                    return (int) (p1.getId() - p2.getId());
//                                }
//
//                            });
//
//                            TurTurno tt = ttList.get(ttList.size() - 1);
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
        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("closeOnEscape", false);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentWidth", 480);
        options.put("contentHeight", 430);
        // Modificar directorio y agregar archivo ListDialog //
        context.openDialog("dlgVoucher.xhtml", options, null);
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
        context.openDialog("dlgDni.xhtml", options, null);
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

    public void listaTramite() {
        listaTramites = ejbTipoGamacFacade.lstTipoTramite();
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
        //List<CitaTurProgramacion> resultado = ejbTurProgramacionFacade.lstTurProgramacion(tipoProg);
        return ejbTurProgramacionFacade.lstTurProgramacion(tipoProg);
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

    public String mostrarConstancia(CitaTurTurno reg) throws IOException, Exception {
        //TurConstancia tc = (TurConstancia) item.get("constancia");

        //BAJAR PDF DEL SERVIDOR
        if (!upFilesCont.findFiles(reg.getId() + ".pdf")) {
            //Syso("GENERANDO ARCHIVO POR QUE NO EXISTIA");
            //verRg(p_rau.getRegistro(), true,true);
            mostrarReporte(reg);
            upFilesCont.PdfDownload(reg.getId() + ".pdf");
        } else {
            //BAJAR PDF DEL SERVIDOR
            //Syso("SI EXISTE ARCHIVO, BAJANDO DEL SERVIDOR");
            //upFilesCont.PdfDownload(p_rau.getRegistro().getId() + ".pdf");
            upFilesCont.PdfDownload(reg.getId() + ".pdf");
        }
        return null;
    }

    /**
     * MOSTRAR REPORTE
     *
     * @param reg
     * @throws java.lang.Exception
     */
    public void mostrarReporte(CitaTurTurno reg) throws Exception {
        RequestContext.getCurrentInstance().execute("$(\".verConst\").click();");
    }

    /* MOSTRAR REPORTE */
    public StreamedContent verPdf(CitaTurTurno reg) {

        List<ArrayRecord> ls2 = ejbRma1369Facade.listarArrayRecord();
        List<ArrayRecord> ls = new ArrayList();
        ArrayRecord regis = (ArrayRecord) ls2.get(0);
        ls.add(regis);

        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap mapa = new HashMap();

        String jasperConst = "constCitasVal.jasper";

        mapa.put("P_logoSucamec", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));

        Date fecha = reg.getFechaPrograma(); //new Date();
        String cFecha = ReportUtil.mostrarFechaDdMmYyyy(fecha);
        String cHora = ReportUtil.mostrarFechaHhMmSs(fecha);
        String cFechaNom = ReportUtil.formatoFechaYyyyMmDdHhMmSs(fecha);
        mapa.put("p_fechaString", cFecha);
        mapa.put("p_fechaHora", cHora);
        String tipoNumDoc = reg.getPerExamenId().getTipoDoc().getAbreviatura() + " - " + reg.getPerExamenId().getNumDoc();
        String nombres = reg.getPerExamenId().getApePat() + " " + reg.getPerExamenId().getApeMat() + " " + reg.getPerExamenId().getNombres();
        String lugar = reg.getProgramacionId().getSedeId().getNombre();

        String termyCond = ResourceBundle.getBundle("/BundleCitas").getString("CitaArmas_paso3Nota1_Title");
        termyCond += "\n" + ResourceBundle.getBundle("/BundleCitas").getString("CitaArmas_paso3Nota2_Title");

        mapa.put("p_tipoDoc", tipoNumDoc);
        mapa.put("p_nombres", nombres);
        mapa.put("p_lugar", lugar);
        mapa.put("p_turno", ReportUtil.mostrarFechaDdMmYyyy((Date) reg.getFechaTurno()) + " - " + horaProgra);
        mapa.put("p_fechaAnio", ReportUtil.mostrarAnio(fecha));
        mapa.put("p_termycond", termyCond);
        mapa.put("p_fechaTurno", ReportUtil.mostrarFechaDdMmYyyy((Date) reg.getFechaTurno()));
        mapa.put("p_horasTurno", horaPresen + " y " + horaProgra);

        mapa.put("p_clave", reg.getClaveConsulta());

        listArmas.clear();
        for (int i = 0; i < lstDetalleArma.size(); i++) {
            Map arm = new HashMap();
            arm.put("nroLicencia", lstDetalleArma.get(i).getNroLicencia().toString());
            arm.put("fecVen", ReportUtil.mostrarFechaDdMmYyyy(lstDetalleArma.get(i).getFecVencimiento()));
            arm.put("modalidad", lstDetalleArma.get(i).getTipoLicencia());
            arm.put("nroSerie", lstDetalleArma.get(i).getNroSerie());
            arm.put("arma", lstDetalleArma.get(i).getTipoArma() + " " + lstDetalleArma.get(i).getMarca() + " " + lstDetalleArma.get(i).getModelo() + " " + lstDetalleArma.get(i).getCalibre());
            arm.put("estado", lstDetalleArma.get(i).getEstado());
            arm.put("situacion", lstDetalleArma.get(i).getSituacion());
            int x = 0;
            listArmas.add(arm);
        }

        mapa.put("p_armas", listArmas);

        String nomFile = "citasVal_" + cFechaNom + "_" + reg.getId();

        try {
            if (JsfUtil.buscarPdfRepositorio(nomFile, ResourceBundle.getBundle("/BundleCitas").getString("Documentos_pathUpload_constanciaCita")) == false) {
                JsfUtil.subirPdf(nomFile, mapa, ls, "/aplicacion/citas/rep/" + jasperConst, ResourceBundle.getBundle("/BundleCitas").getString("Documentos_pathUpload_constanciaCita"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: Reporte", ex);
        }

        try {
            upFilesCont.PdfDownload(nomFile + ".pdf");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return null;

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
            JsfUtil.invalidar("tabGestion:crearCitaValForm:terminos");
            error = true;
        }
        // Validar Captcha //
        if (!captcha.ok()) {
            JsfUtil.addErrorMessage("Error el captcha es incorrecto");
            JsfUtil.invalidar("tabGestion:crearCitaValForm:textoCaptcha");
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
        visibleCalendarNoPresencial = false;
        visibleCalendarZonal = false;
        visibleCalendarCentral = false;
        if (getTipoCita().getCodProg().equals("TP_CIT_NPR")) {
            visibleCalendarNoPresencial = true;
        } else {
            if (registro.getTipoSedeId() != null) {
                visibleCalendarZonal = registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_ANCASH")
                        || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_AQP")
                        || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_CHICLA")
                        || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_CUSCO")
                        || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_LIBERTA")
                        || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_PIURA")
                        || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_PUNO")
                        || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_TACNA")
                        || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_ICA")
                        || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_CAJAMARCA")
                        || registro.getTipoSedeId().getCodProg().equals("TP_AREA_OD_LOR");
            } else {
                visibleCalendarZonal = false;
            }
            visibleCalendarCentral = !visibleCalendarZonal;
        }
        
        fechaTurnoSeleccionada = "";
        getMaxFechaGeneral();
    }

    public void seleccionarTipoCita() {
        listaProgramaciones = new ArrayList();
        registro.setFechaTurno(null);
        registro.setProgramacionId(null);
        registro.setTipoSedeId(null);
        setVisibleTipoSede(true);
        setVisibleCalendarCentral(false);
        setVisibleCalendarZonal(false);
        setVisibleCalendarNoPresencial(false);

        /*if (getTipoCita() != null) {
            if (getTipoCita().getCodProg().equals("TP_CIT_NPR")) {
                tipoProg = "TP_PROG_VER_NPR";
            } else {
                tipoProg = "TP_PROG_VER";
            }
        }*/
    }
     
    public void seleccionarTramite() {
        if (tipoTramite != null) {
            if (tipoTramite.getCodProg().equals("TP_TRAM_VER")) {
                listaArmas();
            }
        } else {

        }
    }

    public List<CitaTipoBase> listarSedes() {
        List<CitaTipoBase> sedes = new ArrayList();
        if (getTipoCita().getCodProg().equals("TP_CIT_NPR")) {
            sedes = ejbTipoBaseFacade.listarSedesNoPresencial();
        } else {
            sedes = ejbTipoBaseFacade.listarSedes();
        }

        for (CitaTipoBase sede : sedes) {
            if (sede.getCodProg().equals("TP_AREA_TRAM")) {
                sede.setNombre("LIMA");
            }
        }
        return sedes;
    }

    public List<CitaTipoBase> listarTipoCitas() {
        List<CitaTipoBase> tipoCitaLst = ejbTipoBaseFacade.lstTipoBase("TP_CIT");
        return tipoCitaLst;
    }

    /**
     * FUNCION PARA OBTENER LISTA DE ARMAS A VALIDAR
     *
     * @author rarevalo
     */
    public void listaArmas() {
        if (tipoSubTramite != null) {
            switch (tipoSubTramite.getCodProg()) {
                case "TP_PROG_VER_RLU":
                    listaArmasVerRen();
                    break;
            }
        }
    }

    public void listaArmasVerRen() {
        String situacionArma = "TP_SITU_POS','TP_SITU_POS_EP','TP_SITU_POS_LC";
        List<AmaTarjetaPropiedad> lstTarjetas = gestionCitasFacade.listTarjetaArmasPropietario(loginController.getUsuario().getPersona().getId(),situacionArma);
        if (!lstTarjetas.isEmpty()) {
            if (lstTarjetas.size() > 0) {
                for (AmaTarjetaPropiedad tar : lstTarjetas) {
                    /*boolean agregar = false;
                    if (tar.getArmaId().getSituacionId().getCodProg().equals("TP_SITU_POS")) {
                        agregar = true;
                    }*/

                    //if (agregar) {
                        detalleArma = new CitaDetalleArma();
                        detalleArma.setId(String.valueOf(lstDetalleArma.size() + 1));
                        //detalleArma.setNroLicencia(Long.valueOf(row.get("NRO_LIC").toString()));
                        //detalleArma.setFecVencimiento((row.get("FEC_VENCIMIENTO")!=null)? (Date) row.get("FEC_VENCIMIENTO"):null);
                        //detalleArma.setTipoLicencia(row.get("TIPO_LICENCIA").toString());
                        detalleArma.setNroRua(tar.getArmaId().getNroRua());
                        detalleArma.setNroSerie(tar.getArmaId().getSerie());
                        detalleArma.setTipoArma(tar.getArmaId().getModeloId().getTipoArmaId().getNombre());
                        detalleArma.setMarca(tar.getArmaId().getModeloId().getMarcaId().getNombre());
                        detalleArma.setModelo(tar.getArmaId().getModeloId().getModelo());
                        detalleArma.setSituacion(tar.getArmaId().getSituacionId().getNombre());
                        //detalleArma.setCalibre((row.get("CALIBRE").toString()!=null)? row.get("CALIBRE").toString():"-");
                        detalleArma.setArmaId(tar.getArmaId());

                        lstDetalleArma.add(detalleArma);
                    //}
                }
            }
        }

        /*
        //Syso("docPropietario: " + loginController.getUsuario().getNumDoc());
        String docPropietario = loginController.getUsuario().getNumDoc();
        CitaTipoGamac tipoTram = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_TRAM_VER");
        //CitaTipoGamac tipoEst1 = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_EST_CON");
        //CitaTipoGamac tipoEst2 = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_ESTUR_CON");
        //String estados = "(" + tipoEst1.getId() + ", " + tipoEst2.getId() + ")";
        CitaTipoGamac tipoEnRepa = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_ESTA_ENT_REP");
        CitaTipoGamac situEnRepa = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_SITU_ENT_REP");
        CitaTipoGamac situInternado = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_SITU_INT");
        
        List<CitaTurTurno> lstCitas = ejbFacade.listarTurnosConfirmadosXPersonaXTipo(docPropietario, "TP_TRAM_VER");
        List<Map> listLicencias = null;
        if(!lstCitas.isEmpty()){
            CitaTurTurno ultimaCita = lstCitas.get(0);
            if(ultimaCita!=null){
                listLicencias = ejbRma1369Facade.listarArmasxUsuarioNoVal(docPropietario, ultimaCita.getId(), null, null);
            }else{
                listLicencias = ejbRma1369Facade.listarArmasxUsuarioNoVal(docPropietario, null, tipoTram.getId(), null);            
            }            
        }else{
            listLicencias = ejbRma1369Facade.listarArmasxUsuarioNoVal(docPropietario, null, tipoTram.getId(), null);
        }
        
        lstDetalleArma = new ArrayList<CitaDetalleArma>();
        if(!listLicencias.isEmpty()){
            if(listLicencias.size()>0){
                ListDataModel<ArrayRecord> rs = new ListDataModel(listLicencias);
                Iterator<ArrayRecord> iteRs = rs.iterator();
                while (iteRs.hasNext()) {
                    ArrayRecord row = iteRs.next();
                    if(row.get("TIPO_ESTADO")==null || row.get("TIPO_ESTADO").toString().equals(tipoEnRepa.getId().toString())) {                        
                        
                        boolean agregar = true;
                        if( row.get("SITUACION_ID")!=null ){
                            if( row.get("SITUACION_ID").toString().equals(situInternado.getId().toString()) ){
                                agregar = false; //No agregar si ya tiene registro en tur_licencia_reg con situacion internado 'TP_SITU_INT'
                            }
                        }
                        
                        if(agregar){
                            detalleArma = new CitaDetalleArma();
                            detalleArma.setId(String.valueOf(lstDetalleArma.size()+1));
                            detalleArma.setNroLicencia(Long.valueOf(row.get("NRO_LIC").toString()));
                            detalleArma.setFecVencimiento((row.get("FEC_VENCIMIENTO")!=null)? (Date) row.get("FEC_VENCIMIENTO"):null);
                            detalleArma.setTipoLicencia(row.get("TIPO_LICENCIA").toString());
                            detalleArma.setNroSerie(row.get("NRO_SERIE").toString());
                            detalleArma.setTipoArma(row.get("TIPO_ARMA").toString());
                            detalleArma.setMarca(row.get("MARCA").toString());
                            detalleArma.setModelo(row.get("MODELO").toString());
                            detalleArma.setCalibre((row.get("CALIBRE").toString()!=null)? row.get("CALIBRE").toString():"-");

                            //Obtener nro Guia
                            List<AmaGuiaTransito> listGuia = ejbAmaGuiaTransitoFacade.selectxLicDisca(Long.valueOf(row.get("NRO_LIC").toString()));
                            String nroGuia= "";
                            if(listGuia.size()>0){
                                nroGuia = " (" + listGuia.get(0).getNroGuia() + ")";
                            }

                            if(row.get("TIPO_ESTADO")!=null && row.get("TIPO_ESTADO").toString().equals(tipoEnRepa.getId().toString())){
                                detalleArma.setEstado(tipoEnRepa.getNombre());
                                detalleArma.setSituacion(tipoEnRepa.getNombre());
                                detalleArma.setSituacionId(situEnRepa);
                                detalleArma.setEstadoId(tipoEnRepa);
                            }else{
                                detalleArma.setEstado(row.get("ESTADO").toString());
                                detalleArma.setSituacion(row.get("SITUACION").toString() + nroGuia);
                                if(nroGuia!=""){
                                    detalleArma.setSituacionId(situInternado);
                                }
                            }
                            
                            lstDetalleArma.add(detalleArma);
                        }
                    }
                    
                }
            }                
        }*/
    }

    public boolean validarVerificacionArma(AmaTarjetaPropiedad tarjeta) {
        boolean resul = true;
        if (tarjeta != null) {
            AmaArma mapArma = tarjeta.getArmaId();

            List<CitaTurLicenciaReg> listAprobadas = new ArrayList<>();
            if (mapArma != null) {
                listAprobadas = gestionCitasFacade.listarArmasVerificacion(mapArma.getId());

                if (listAprobadas.isEmpty()) {
                    resul = false;
                } else {
                    CitaTurLicenciaReg veri = listAprobadas.get(0);
                    if (!JsfUtil.validaFechaMayorMenor(new Date(), veri.getFecVen())) {
                        resul = false;
                    }
                }
            }

        }

        return resul;
    }

    public void addArmaGrilla() {

        boolean validaOk = false;
        boolean existeId = false;
        Date fecPrc = new Date();

        String source = JsfUtil.getRequestParameter("javax.faces.source");
        String nroSerie = JsfUtil.getRequestParameter("crearCitaValForm:nroSerie");
        String nroLic = JsfUtil.getRequestParameter("crearCitaValForm:nroLicencia");
        nroSerie = nroSerie.trim();
        nroLic = nroLic.replace(" ", "").trim();

        if (source.equals("crearCitaValForm:addArma")) {
            if (lstDetalleArma.size() < 10) {
                if (!nroSerie.equals("")) {
                    validaOk = true;
                } else {
                    validaOk = false;
                    JsfUtil.addWarningMessage("Por favor ingrese nro de Serie.");
                }

                if (!nroLic.equals(0)) {
                    validaOk = true;
                } else {
                    validaOk = false;
                    JsfUtil.addWarningMessage("Por favor ingrese nro de Licencia.");
                }

                if (lstDetalleArma.isEmpty()) {
                    validaOk = true;
                } else {
                    for (int i = 0; i < lstDetalleArma.size(); i++) {
                        ////Syso(lstDetalleArma.get(i).getId() + "==" + idDetalle);
                        if (lstDetalleArma.get(i).getNroSerie().equals(nroSerie.toString().toUpperCase()) || lstDetalleArma.get(i).getNroSerie().equals(nroLic)) {
                            existeId = true;
                            break;
                        }
                    }
                    if (existeId) {
                        validaOk = false;
                        JsfUtil.addWarningMessage("Uno de los datos ya ha sido ingresado.");
                        return;
                    } else {
                        validaOk = true;
                    }
                }

                //List<Map> listLicencias = new ArrayList();            
                List<Map> listLicencias = ejbRma1369Facade.listarArmasCanceladasxFiltro(nroSerie, nroLic);
                if (listLicencias != null) {
                    if (listLicencias.size() > 0) {
                        ListDataModel<ArrayRecord> rs = new ListDataModel(listLicencias);
                        Iterator<ArrayRecord> iteRs = rs.iterator();
                        while (iteRs.hasNext()) {
                            ArrayRecord row = iteRs.next();
                            if (row.get("ESTADO").toString().equals("CANCELADO")) {
                                validaOk = true;
                                fecPrc = (Date) row.get("FEC_PRC");
                            } else {
                                JsfUtil.addWarningMessage("La licencia NO está cancelada por pérdida de vigencia, si usted desea internarla, favor de acercarse a las oficinas de SUCAMEC.");
                                validaOk = false;
                            }
                        }
                    } else {
                        validaOk = false;
                        JsfUtil.addWarningMessage("La licencia NO está cancelada por pérdida de vigencia, si usted desea internarla, favor de acercarse a las oficinas de SUCAMEC.");
                    }
                } else {
                    validaOk = false;
                    JsfUtil.addWarningMessage("La licencia NO está cancelada por pérdida de vigencia, si usted desea internarla, favor de acercarse a las oficinas de SUCAMEC.");
                }
            } else {
                validaOk = false;
                JsfUtil.addWarningMessage("No es posible agregar más de 10 armas");
            }

        } else {
            JsfUtil.mensajeError("Función no adecuada!");
        }

        if (validaOk) {
            detalleArma = new CitaDetalleArma();
            detalleArma.setId(String.valueOf(lstDetalleArma.size() + 1));
            detalleArma.setNroSerie(nroSerie.toString().toUpperCase());
            detalleArma.setNroLicencia(Long.valueOf(nroLic));
            detalleArma.setFecPrc(fecPrc);

            lstDetalleArma.add(detalleArma);
            detalleArma = new CitaDetalleArma();

            RequestContext.getCurrentInstance().execute("limpiarInputDet();");

        }

    }

    public void eliminarDetalleSeleccionados() {
        if (!lstDetalleArmaSelected.isEmpty()) {
            //RequestContext.getCurrentInstance().execute("PF('confirmDlgGlobal').show();");
            for (CitaDetalleArma ls : lstDetalleArmaSelected) {
                for (CitaDetalleArma l : lstDetalleArma) {
                    if (Objects.equals(ls.getId(), l.getId())) {
                        lstDetalleArma.remove(l);
                        break;
                    }
                }
            }
            JsfUtil.addSuccessMessage("Arma(s) Eliminada(s)");
        } else {
            JsfUtil.addWarningMessage("Debe seleccionar un registro para eliminar");
        }

    }

    /**
     * FUNCIÓN PARA REINICIAR VALORES DE DLG DE BUSQUEDA DE ARMAS
     *
     * @author arevalo
     */
    public void reiniciarFormBuscaArma() {
        filtroArmas = null;
        listaTarjetasPropiedad = null;
        datosTarjetasPropiedad = null;
        tarjetaSelected = null;
        armasMap = null;
        discaMapLst = null;
        selectedArma = null;
        nroDocComprador = null;
        
        borrarReciboSeleccionado();
    }

    /**
     * FUNCIÓN PARA MOSTRAR DLG DE BUSQUEDA DE ARMAS
     *
     * @author rarevalo
     *
     */
    public void loadBuscaArma() {
        reiniciarFormBuscaArma();
        if(tipoSubTramite.getCodProg().equals("TP_PROG_VER_TNJ"))
        {
            solicitarRecibo=true;
        }
        if(tipoSubTramite.getCodProg().equals("TP_PROG_VER_FAP"))
        {
            solicitarRecibo=false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("tabGestion:buscarArmaJurForm");
        context.execute("PF('wvDlgBuscarArmaJur').show();");
    }

    public void buscarArmas() {
        armasMap = new ArrayList();
        datosTarjetasPropiedad = gestionCitasFacade.listarTarjetasXRuc(filtroArmas, perPago.getRuc());
        for (AmaTarjetaPropiedad tarjeta : datosTarjetasPropiedad) {
            Map tarMap = new HashMap();
            tarMap.put("tarjeta", tarjeta);
            //tarMap.put("tipoComprador", tarjeta.getPersonaCompradorId().getTipoDoc().getAbreviatura());
            tarMap.put("docComprador", tarjeta.getPersonaCompradorId().getRuc() == null ? tarjeta.getPersonaCompradorId().getNumDoc() : tarjeta.getPersonaCompradorId().getRuc());
            tarMap.put("personaComprador", tarjeta.getPersonaCompradorId().getRuc() == null ? tarjeta.getPersonaCompradorId().getDatosPerNat() : tarjeta.getPersonaCompradorId().getDatosPerJur());
            tarMap.put("nroRua", tarjeta.getArmaId().getNroRua());
            tarMap.put("serie", tarjeta.getArmaId().getSerie());
            tarMap.put("nroLic", tarjeta.getLicenciaId() != null ? tarjeta.getLicenciaId().getNroLicencia() : "");

            tarMap.put("tipoArma", tarjeta.getArmaId().getModeloId().getTipoArmaId().getNombre());
            String calibres = "";
            for (AmaCatalogo calibre : tarjeta.getArmaId().getModeloId().getAmaCatalogoList()) {
                if (calibre.getActivo() == 1) {
                    if (calibres.equals("")) {
                        calibres = calibre.getNombre();
                    } else {
                        calibres = calibres + " / " + calibre.getNombre();
                    }
                }
            }
            tarMap.put("calibre", calibres);
            tarMap.put("marca", tarjeta.getArmaId().getModeloId().getMarcaId().getNombre());
            tarMap.put("modelo", tarjeta.getArmaId().getModeloId().getModelo());

            //Obtener nro Guia
            List<AmaGuiaTransito> listGuia = ejbAmaGuiaTransitoFacade.selectxInternadoxSerieRua(tarjeta.getArmaId().getSerie(), tarjeta.getArmaId().getNroRua());
            String nroGuia = "";
            if (listGuia.size() > 0) {
                nroGuia = " (" + listGuia.get(0).getNroGuia() + ")";
            }

            tarMap.put("situacion", tarjeta.getArmaId().getSituacionId().getNombre() + nroGuia);
            tarMap.put("situacionCod", tarjeta.getArmaId().getSituacionId().getCodProg());
            tarMap.put("estado", tarjeta.getArmaId().getEstadoId().getNombre());
            tarMap.put("estadoCod", tarjeta.getArmaId().getEstadoId().getCodProg());
            tarMap.put("arma", tarjeta.getArmaId());
            tarMap.put("tipo", "SistemaGAMAC");
            armasMap.add(tarMap);
        }

        switch (desdeMigra) {
            case "DISCA":
                discaMapLst = gestionCitasFacade.listarTarjetasDiscaXRuc(filtroArmas, perPago.getRuc());
                break;

            case "MIGRA":
                discaMapLst = gestionCitasFacade.listarTarjetasDiscaXRucMigra(filtroArmas, perPago.getRuc());
                break;
        }

        for (Map disMap : discaMapLst) {
            boolean agregar = true;
            for (Map m : armasMap) {
                if (disMap.get("serie").equals(m.get("serie"))) {
                    agregar = false;
                }
            }

            if (agregar) {
                Map tarMap = new HashMap();
                tarMap.put("tarjeta", null);
                tarMap.put("personaComprador", disMap.get("personaComprador"));
                tarMap.put("tipoComprador", disMap.get("tipoComprador"));
                tarMap.put("docComprador", disMap.get("docComprador").toString().trim());
                tarMap.put("nroRua", disMap.get("nroRua"));
                tarMap.put("serie", disMap.get("serie"));
                tarMap.put("tipoArma", disMap.get("tipoArma"));
                tarMap.put("calibre", disMap.get("calibre"));
                tarMap.put("marca", disMap.get("marca"));
                tarMap.put("modelo", disMap.get("modelo"));
                tarMap.put("situacion", disMap.get("situacion"));
                tarMap.put("estado", disMap.get("estado"));
                tarMap.put("nroLic", disMap.get("nroLic"));

                //Obtener nro Guia
                List<AmaGuiaTransito> listGuia = ejbAmaGuiaTransitoFacade.selectxInternadoxLicDisca(Long.valueOf(disMap.get("nroLic").toString()));
                String nroGuia = "";
                if (listGuia.size() > 0) {
                    nroGuia = " (" + listGuia.get(0).getNroGuia() + ")";
                }

                tarMap.put("situacion", disMap.get("situacion") + nroGuia);
                tarMap.put("tipo", "DISCA");
                armasMap.add(tarMap);
            }
        }

    }

    public void seleccionarTarjeta(Map mapa) {
        boolean agregar;
        boolean existeArma = false;
        boolean existeReciboLista = false;
        /*for (CitaDetalleArma resDet : lstDetalleArma) {
            if (resDet.getNroSerie().equals(mapa.get("serie").toString())) {
                existeArma = true;
                break;
            }
        }*/

        if (!existeArma) {

            //# VALIDAR COMPROBRANTE #//
            if (!validarVoucherTupa2018Paso1()) {
                if (solicitarRecibo) {
                    if (reciboBn != null) {
                        for (CitaDetalleArma resDet : lstDetalleArma) {
                            if (resDet.getReciboArma().getId().equals(reciboBn.getId())) {
                                existeReciboLista = true;
                                break;
                            }
                        }
                    }
                }
                if (!existeReciboLista) {
                    agregar = true;
                } else {
                    agregar = false;
                    JsfUtil.mensajeAdvertencia("El recibo con número " + reciboBn.getNroSecuencia() + " ya se agregó a la lista!");
                }
            } else {
                agregar = false;
            }
            //# #//

            if (agregar) {
                detalleArma = new CitaDetalleArma();
                detalleArma.setId(String.valueOf(lstDetalleArma.size() + 1));
                //detalleArma.setFecVencimiento((row.get("FEC_VENCIMIENTO")!=null)? (Date) row.get("FEC_VENCIMIENTO"):null);
                //detalleArma.setTipoLicencia(row.get("TIPO_LICENCIA").toString());
                detalleArma.setNroSerie(mapa.get("serie").toString());
                detalleArma.setTipoArma(mapa.get("tipoArma").toString());
                detalleArma.setMarca(mapa.get("tipoArma").toString());
                detalleArma.setModelo(mapa.get("modelo").toString());
                detalleArma.setCalibre(mapa.get("calibre").toString());
                try {
                    detalleArma.setNroLicencia(Long.valueOf(mapa.get("nroLic").toString()));
                } catch (Exception e) {
                }

                if (mapa.get("tipo").equals("SistemaGAMAC")) {
                    detalleArma.setNroRua(mapa.get("nroRua").toString());
                    detalleArma.setEstado(mapa.get("estado").toString());
                    detalleArma.setSituacion(mapa.get("situacion").toString());
                    detalleArma.setSituacionId(ejbTipoGamacFacade.buscarTipoGamacXCodProg(mapa.get("situacionCod").toString()));
                    detalleArma.setEstadoId(ejbTipoGamacFacade.buscarTipoGamacXCodProg(mapa.get("estadoCod").toString()));
                    detalleArma.setArmaId((AmaArma) mapa.get("arma"));
                } else {
                    detalleArma.setEstado(mapa.get("estado").toString());
                    detalleArma.setSituacion(mapa.get("situacion").toString());
                }

                detalleArma.setParaLista(1);
                /*
                // PARA VALIDAR VERIFICAION ANTERIOR DE ARMA
                CitaTurLicenciaReg verifacion = validarVerificacionArma(detalleArma);
                if (verifacion!=null) {
                    detalleArma.setParaLista(0);
                    detalleArma.setFecVencimiento(verifacion.getFecVen());
                } else {
                    detalleArma.setParaLista(1);
                }
                 */

                if (reciboBn != null) {
                    SbRecibos rec = SerializationUtils.clone(reciboBn);
                    detalleArma.setReciboArma(rec);
                }

                lstDetalleArma.add(detalleArma);
                setDisabledTramite(true);

                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('wvDlgBuscarArmaJur').hide();");
                context.update("tabGestion:crearCitaVerJurForm:panelPaso1");
                context.update("tabGestion:crearCitaVerJurForm:armasDT");
                context.execute("selectPasos();");
                //context.execute("selectArmaNoTi();");                            
            }
        } else {
            JsfUtil.mensajeAdvertencia("El arma con nro de Serie " + mapa.get("serie").toString() + " ya se agregó a la lista!");
        }

    }

    public boolean validaArmaxAdmExp(Map mapa) {
        boolean res = false;
        if (mapa != null) {
            //RUC EMPRESA
            //SbUsuario usu = ejbSbUsuarioFacade.selectUsuarioXId(JsfUtil.getLoggedUser().getId());        
            //String dni = usu.getNumDoc();
            String ruc = loginController.getUsuario().getNumDoc();

            if (mapa.get("docComprador").equals(ruc)) {
                if (tipoSubTramite != null) {
                    switch (tipoSubTramite.getCodProg()) {
                        case "TP_PROG_VER_TRA":
                        case "TP_PROG_VER_TPN":
                        case "TP_PROG_VER_TPJ":
                        case "TP_PROG_VER_TFA":
                        case "TP_PROG_VER_TNJ": // PARA PERSONAS NAT/JR
                        case "TP_PROG_VER_FAP": // PARA MIEMBRO FFAA/PNP
                            if (mapa.get("tipo").equals("SistemaGAMAC")) {
                                if (mapa.get("situacion") != null) {
                                    if (mapa.get("situacion").toString().contains("EN POSESION")) {
                                        res = true;
                                    }
                                }
                            }
                            if (mapa.get("tipo").equals("DISCA")) {
                                if (mapa.get("situacion").equals("OPERATIVO") || mapa.get("situacion").equals("RECUPERADO")) {
                                    res = true;
                                }
                            }
                            break;
                        case "TP_PROG_VER_RDA":
                            if (mapa.get("tipo").equals("SistemaGAMAC")) {
                                if (mapa.get("situacion").equals("EN POSESION")) {
                                    res = true;
                                }
                            }
                            break;
                        case "TP_PROG_VER_CM":
                            if (mapa.get("tipo").equals("SistemaGAMAC")) {
                                if (mapa.get("situacion").equals("EN POSESION")) {
                                    res = true;
                                }
                            }
                            break;
                        case "TP_PROG_VER_ANR":
                            //# ARMAS DISCA NO REGULARIZADAS #//
                            if (mapa.get("tipo").equals("DISCA")) {
                                if (mapa.get("situacion").equals("OPERATIVO") || mapa.get("situacion").equals("RECUPERADO")) {
                                    res = true;
                                }
                            }
                            break;
                        case "TP_PROG_VER_RLU":
                            break;
                    }
                }
            }

            /*if (mapa.get("docComprador").equals(ruc)) {
                if (mapa.get("tipo").equals("SistemaGAMAC")) {
                    if (mapa.get("situacion").equals("EN POSESION")) {
                        res = true;
                    }
                }

                if (mapa.get("tipo").equals("DISCA")) {
                    if (mapa.get("situacion").equals("OPERATIVO") || mapa.get("situacion").equals("RECUPERADO")) {
                        res = true;
                    }
                    
                }
            }*/
        }

        return res;
    }

    public int obtenerCantidadDiasHabiles(Calendar fechaInicial, Calendar fechaFinal) {
        int totalDias = -1;
        Calendar fechaTemporal = new GregorianCalendar();
        fechaTemporal.setTime(fechaInicial.getTime());
        while (fechaTemporal.before(fechaFinal) || fechaTemporal.equals(fechaFinal)) {
            if (fechaTemporal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaTemporal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                totalDias++;
            }
            fechaTemporal.add(Calendar.DATE, 1);
        }
        return totalDias;
    }

    public int obtenerCantidadDiasCalendario(Calendar fechaInicial, Calendar fechaFinal) {
        int totalDias = -1;
        Calendar fechaTemporal = new GregorianCalendar();
        fechaTemporal.setTime(fechaInicial.getTime());
        while (fechaTemporal.before(fechaFinal) || fechaTemporal.equals(fechaFinal)) {
            totalDias++;
            fechaTemporal.add(Calendar.DATE, 1);
        }
        return totalDias;
    }

    /**
     * SU USARÁ ESTA FUNCION EN 137 DÍAS ADICONALES AL 17/05/2017
     *
     * @return
     */
    private boolean validaTurnoxFechaCancelacion() {
        Date fechaValidar = null;

        for (CitaDetalleArma det : lstDetalleArma) {
            if (fechaValidar != null) {
                if (det.getFecPrc().before(fechaValidar)) {
                    fechaValidar = det.getFecPrc();
                }
            }
        }

        Calendar fecha1 = Calendar.getInstance();
        fecha1.setTime(fechaValidar);
        Calendar fecha1Rst = obtenerCantidadDiasHabiles2(fecha1, 15);
        Calendar fechaFinal = obtenerCantidadDiasCalendario2(fecha1Rst, 120);

        Calendar fechaTur = Calendar.getInstance();
        fechaTur.setTime(registro.getFechaTurno());

        if (fechaTur.before(fechaFinal)) {
            return true;
        } else {
            JsfUtil.addErrorMessage("La fecha de turno es mayor al plazo de ");
            return false;
        }

    }

    public Calendar obtenerCantidadDiasHabiles2(Calendar fechaInicial, int dias) {
        int totalDias = 1;
        Calendar fechaTemporal = new GregorianCalendar();
        fechaTemporal.setTime(fechaInicial.getTime());
        while (totalDias < dias) {
            fechaTemporal.add(Calendar.DATE, 1);
            if (fechaTemporal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaTemporal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                Date Ini = fechaTemporal.getTime();
                List<Date> listaFeriados = ejbSbFeriadoFacade.listFeriados(Ini, Ini);
                if (listaFeriados.size() == 0) {
                    totalDias++;
                }
            }
        }

        return fechaTemporal;
    }

    public Calendar obtenerCantidadDiasCalendario2(Calendar fechaInicial, int dias) {
        int totalDias = 1;
        Calendar fechaTemporal = new GregorianCalendar();
        fechaTemporal.setTime(fechaInicial.getTime());
        while (totalDias < dias) {
            fechaTemporal.add(Calendar.DATE, 1);
            totalDias++;
        }
        return fechaTemporal;
    }

    public CitaTurLicenciaReg validarVerificacionArma(CitaDetalleArma detArma) {
        CitaTurLicenciaReg veri = new CitaTurLicenciaReg();

        List<CitaTurLicenciaReg> listAprobadas = new ArrayList<>();
        if (detArma.getArmaId() != null) {
            listAprobadas = ejbTurLicenciaRegFacade.listarArmasVerificaAprob(detArma.getArmaId().getId());
        } else {
            listAprobadas = ejbTurLicenciaRegFacade.listarArmasVerificaAprobXSerie(detArma.getNroSerie());
        }

        if (listAprobadas.isEmpty()) {
            veri = null;
            //resul = false;
            //msg = "El arma de fuego debe pasar por verificación física";
        } else {
            veri = listAprobadas.get(0);

            if (!JsfUtil.validaFechaMayorMenor(hoy, veri.getFecVen())) {
                veri = null;
                //resul = false;
                //msg = "El arma de fuego debe pasar por verificación física";
            }
        }

        return veri;
    }

    public void cargarTasasTupa(String codSubTramie) {
        try {
            //solicitarRecibo = Boolean.parseBoolean(ejbSbParametroFacade.obtenerParametroXCodProg("TP_BOOL_TUPA_VER").getValor());
            //solicitarVoucherTemp = Boolean.parseBoolean(ejbSbParametroFacade.obtenerParametroXCodProg("TP_BOOL_VCTMP_VER").getValor());
            solicitarRecibo = Boolean.parseBoolean(ejbSbParametroFacade.obtenerParametroXCodProg(codSubTramie + "_BOOL").getValor());
            solicitarVoucherTemp = Boolean.parseBoolean(ejbSbParametroFacade.obtenerParametroXCodProg(codSubTramie + "_VCTM").getValor());
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
        boolean isActivo = false;
        try {
            //TP_PROG_VER_TNJ  TRANSFERENCIA A FAVOR DE PERSONA NATURAL,PERSONA JURÍDICA
            if (tipoSubTramite.getCodProg().equals("TP_PROG_VER_TNJ")) {
                if (nroDocComprador == null) {
                    JsfUtil.mensajeAdvertencia("Debe de Ingresar el Nro. de Documento del Comprador");
                    return null;
                }    
                if (nroDocComprador.equals(perExamen.getNumDoc())) {
                    JsfUtil.mensajeAdvertencia("El Nro. de Documento debe ser del Comprador");
                    return null;
                }   
            }
           
            borrarValoresRecibo();
            List<SbRecibos> filteredList = new ArrayList();

            //if (registro.getTipoTramiteId()!=null) {
            HashMap mMap = new HashMap();
            mMap.put("tipo", null);
            mMap.put("importe", parametrizacionTupa.getImporte());
            mMap.put("codigo", parametrizacionTupa.getCodTributo());
            //mMap.put("ruc", perExamen.getRuc() );

            if (tipoSubTramite.getCodProg().equals("TP_PROG_VER_ANR")) {
                mMap.put("numDoc", perExamen.getNumDoc());
            /*} else if (tipoSubTramite.getCodProg().equals("TP_PROG_VER_TRA") || tipoSubTramite.getCodProg().equals("TP_PROG_VER_TNJ") || tipoSubTramite.getCodProg().equals("TP_PROG_VER_FAP") ) {
                mMap.put("importes", "valor");*/
            }
            //TP_PROG_VER_TNJ  TRANSFERENCIA A FAVOR DE PERSONA NATURAL,PERSONA JURÍDICA
            if (tipoSubTramite.getCodProg().equals("TP_PROG_VER_TNJ")) {
                mMap.put("numDoc", nroDocComprador);
            }
            Long nroSec = Long.valueOf(query.trim());
            if (nroSec.toString().trim().length() >= 2) {
                mMap.put("secuencia", nroSec);
                List<SbRecibos> lstUniv = ejbSbRecibosFacade.listarRecibosVeriByFiltro(mMap);
                if (lstUniv != null) {
                    for (SbRecibos x : lstUniv) {
                        
                        List<CitaTurComprobante> tcomp = ejbTurComprobanteFacade.listarComprobantePorSecuencia(x.getImporte().toString(),x.getNroSecuencia().toString(), x.getId().toString());
                        for (CitaTurComprobante tc : tcomp) {
                            if (JsfUtil.getFechaSinHora(tc.getFechaPago()).compareTo(JsfUtil.getFechaSinHora(x.getFechaMovimiento())) == 0) {
                                isActivo = true;
                                JsfUtil.addWarningMessage("El comprobante que esta tratando de ingresar ya ha sido usado anteriormente");
                            }
                        }
                        
                        for (SbReciboRegistro rg : x.getSbReciboRegistroList()) {
                            if (rg.getActivo() == 1) {
                                isActivo = true;
                            }
                        }
                        if (!isActivo) {
                            if (x.getNroSecuencia().toString().contains(nroSec.toString().trim())) {
                                filteredList.add(x);
                            }
                        }
                        /*if (x.getSbReciboRegistroList().isEmpty()) {
                            switch (tipoSubTramite.getCodProg()) {
                                case "TP_PROG_VER_TRA":
                                case "TPG_PRO_VER_TNJ": // PARA PERSONAS NAT/JR
                                case "TP_PROG_VER_FAP": // PARA MIEMBRO FFAA/PNP
                                    if (x.getNroSecuencia().toString().equals(nroSec.toString().trim())) {
                                        filteredList.add(x);
                                    }
                                    break;
                                default:
                                    if (x.getNroSecuencia().toString().contains(nroSec.toString().trim())) {
                                        filteredList.add(x);
                                    }
                                    break;
                            }
                        }*/
                    }
                }
                return filteredList;
            } else {
                return null;
            }

            /*List<SbRecibos> lstUniv = ejbSbRecibosFacade.listarRecibosVeriByFiltro(mMap);
                if (lstUniv!=null) {
                    for (SbRecibos x : lstUniv) {
                        if (x.getSbReciboRegistroList().isEmpty()) {
                            if (x.getNroSecuencia().toString().contains(query.trim())) {
                                filteredList.add(x);
                            }                            
                        }
                    }                
                }
                return filteredList;*/
            //} else {
            //JsfUtil.mensajeAdvertencia("Seleccione tipo de Licencia!");
            //return null;
            //}
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

    public boolean validarVoucherTupa2018Paso1() {
        boolean res = false;

        //if (solicitarRecibo && eligeReciboLista) {
        if (solicitarRecibo || solicitarVoucherTemp) {
            if (solicitarRecibo && eligeReciboLista) {
                if (reciboBn != null) {
                    List<CitaTurTurno> lstTurnosVoucher = new ArrayList<>();
                    if (solicitarRecibo) {
                        lstTurnosVoucher = ejbFacade.listarTurnosVeriXVoucherCta(reciboBn.getNroSecuencia(), BigDecimal.valueOf(reciboBn.getImporte()), reciboBn.getFechaMovimiento());
                    } else if (solicitarVoucherTemp) {
                        lstTurnosVoucher = ejbFacade.listarTurnosVeriXVoucherCta(registro.getSecuenciaEmpoce(), registro.getMontoEmpoce(), registro.getFechaEmpoce());
                    }

                    CitaTurTurno tur = null;
                    if (lstTurnosVoucher.isEmpty()) {
                        res = false;
                    } else {
                        tur = lstTurnosVoucher.get(0);
                        for (CitaTurLicenciaReg lic : tur.getTurLicenciaRegList()) {
                            if (lic.getComprobanteId().getNroExpediente() != null) {
                                res = true;
                            }
                            break;
                        }
                    }

                    /*if (!res) {
                        if (solicitarRecibo && tur!=null) {
                            for (CitaTurLicenciaReg lic : tur.getTurLicenciaRegList()) {
                                //Syso("consta: " + lic.getComprobanteId().getId() + " / " + tur.getProgramacionId().getHora());
                                if (lic.getComprobanteId().getVoucherCta().equals(reciboBn.getId().toString())) {
                                    res = true;
                                    break;
                                }
                            }                                    
                        }
                    }*/
                    if (res) {
                        JsfUtil.addWarningMessage("El recibo ingresado ya está siendo usado en otra cita");
                    }
                } else {
                    res = true;
                    //JsfUtil.addErrorMessage("Debe ingresar el número del comprobante");
                }
            } else {
                res = true;
                JsfUtil.addWarningMessage("Seleccione el número del comprobante");
            }
        } else if (!solicitarRecibo && !solicitarVoucherTemp) {
           res = false;
        }
        return res;
    }

    public boolean validarVoucherTupa2018() {
        boolean res = false;
        boolean validaDia = true;
        boolean validaHora = false;
        if (solicitarRecibo || solicitarVoucherTemp) {
            for (CitaDetalleArma det : lstDetalleArma) {
                //Syso("recibo: " + det.getReciboArma().getNroSecuencia());
                List<CitaTurTurno> lstTurnosVoucher = new ArrayList<>();
                if (solicitarRecibo) {
                    lstTurnosVoucher = ejbFacade.listarTurnosVeriXVoucherCta(det.getReciboArma().getNroSecuencia(), BigDecimal.valueOf(det.getReciboArma().getImporte()), det.getReciboArma().getFechaMovimiento());
                } else if (solicitarVoucherTemp) {
                    lstTurnosVoucher = ejbFacade.listarTurnosVeriXVoucherCta(det.getReciboArma().getNroSecuencia(), BigDecimal.valueOf(det.getReciboArma().getImporte()), det.getReciboArma().getFechaMovimiento());
                }

                CitaTurTurno tur = null;
                if (lstTurnosVoucher.isEmpty()) {
                    res = false;
                    validaDia = false;
                } else {
                    tur = lstTurnosVoucher.get(0);
                    for (CitaTurLicenciaReg lic : tur.getTurLicenciaRegList()) {
                        if (lic.getComprobanteId().getNroExpediente() != null) {
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
                        } else if (hoy.compareTo(fechaTur) > 0) {
                            res = false;
                        } else if (hoy.compareTo(fechaTur) < 0) {
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

                            /*if (!res) {
                                if (solicitarRecibo) {
                                    for (CitaTurLicenciaReg lic : tur.getTurLicenciaRegList()) {
                                        //Syso("licReg: " + lic.getComprobanteId().getId() + " / " + tur.getProgramacionId().getHora());
                                        if (lic.getComprobanteId().getVoucherCta().equals(det.getReciboArma().getId().toString())) {
                                            res = true;
                                            break;
                                        }
                                    }                                    
                                }
                            }*/
                        }

                        if (res) {
                            break;
                        }

                    }
                }

            }

        } else if (!solicitarRecibo && !solicitarVoucherTemp) {
            res = false;             
        }
        return res;
    }

}
