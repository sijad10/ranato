package pe.gob.sucamec.bdintegrado.jsf;

import pe.gob.sucamec.bdintegrado.data.AmaSolicitudRecojo;
import pe.gob.sucamec.bdintegrado.bean.AmaSolicitudRecojoFacade;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.bdintegrado.bean.AmaArmaFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaGuiaTransitoFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaInventarioAccesoriosFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaInventarioArmaFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaResolucionFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaTarjetaPropiedadFacade;
import pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoGamacFacade;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.AmaResolucion;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbProcesoTupa;
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.jsf.util.ArmaRepClass;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.ReportUtil;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.rma1369.bean.Rma1369Facade;
import pe.gob.sucamec.sistemabase.beans.SbDepartamentoFacade;
import pe.gob.sucamec.sistemabase.beans.SbDireccionFacade;
import pe.gob.sucamec.sistemabase.beans.SbDistritoFacade;
import pe.gob.sucamec.sistemabase.beans.SbMedioContactoFacade;
import pe.gob.sucamec.sistemabase.beans.SbPersonaFacade;
import pe.gob.sucamec.sistemabase.beans.SbProvinciaFacade;
import pe.gob.sucamec.sistemabase.beans.SbRelacionPersonaFacade;
import pe.gob.sucamec.sistemabase.data.SbDepartamento;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbDistrito;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbProvincia;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

@Named("amaSolicitudRecojoController")
@SessionScoped
public class AmaSolicitudRecojoController implements Serializable {

    private DataModel items = null;

    @EJB
    private Rma1369Facade ejbRma1369Facade;

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaSolicitudRecojoFacade ejbFacade;

    @EJB
    private AmaArmaFacade ejbAmaArmaFacade;

    @EJB
    private TipoGamacFacade ejbTipoGamacFacade;

    @EJB
    private SbPersonaFacade ejbSbPersonaFacade;

    @EJB
    private SbDepartamentoFacade ejbSbDepartamentoFacade;

    @EJB
    private SbProvinciaFacade ejbSbProvinciaFacade;

    @EJB
    private SbDistritoFacade ejbSbDistritoFacade;

    @EJB
    private AmaSolicitudRecojoFacade ejbAmaSolicitudRecojoFacade;

    @EJB
    private AmaGuiaTransitoFacade ejbAmaGuiaTransitoFacade;

    @EJB
    private SbNumeracionFacade ejbSbNumeracionFacade;

    @EJB
    private SbDireccionFacade ejbSbDireccionFacade;

    @EJB
    private AmaInventarioArmaFacade ejbAmaInventarioArmaFacade;

    @EJB
    private SbMedioContactoFacade ejbSbMedioContactoFacade;

    @EJB
    private AmaInventarioAccesoriosFacade ejbAmaInventarioAccesoriosFacade;

    @EJB
    private SbRelacionPersonaFacade ejbSbRelacionPersonaFacade;

    @EJB
    private AmaTarjetaPropiedadFacade ejbAmaTarjetaPropiedadFacade;

    @EJB
    private AmaResolucionFacade ejbAmaResolucionFacade;

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade ejbSbRecibosFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbPersonaFacade; 
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbProcesoTupaFacade ejbSbProcesoTupaFacade;    
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacade;    
    
    @Inject
    private LoginController loginController;
    @Inject
    private AmaInventarioArmaController amaInventarioArmaController;
    @Inject
    WsTramDoc wsTramDocController;

    private EstadoCrud estado;

    private Date fprueba;

    private AmaSolicitudRecojo registro;

    private SbDireccion sedeSelect;

    private Date fechaini;

    private Date fechafin;

    private Date fechaHoy = new Date();

    private Long idTipoBusqueda;

    private String filtro = null;

    private ListDataModel<AmaArma> resultados;

    private ListDataModel<AmaArma> registrosSeleccionados;

    private ListDataModel<Map> lstArmasNuevas;

    private List<Map> lstArmasNuevasSelect;

    private List<Map> lstArmasRecojoFinal;

    private List<Map> lstArmasReingresoFinal;

    private List<SbDireccion> listaDestino;

    private SbDireccion objDestino;

    private SbDireccion objOrigen;

    private List<SbPersona> listaPersonaRecojo;

    private SbPersona objPersonaRecojo;

    private SbDireccion direccion;

    private List<SbDireccion> listaDireccionPorPersona;

    private ListDataModel<AmaSolicitudRecojo> listaSolicitudRecojo;

    private List<Map> listTarjConSolRecojo = new ArrayList<>();

    private List<Map> listTarjConGuiaTran = new ArrayList<>();

    private SbPersona personaTitular;

    private static Long L_UNO = 1L;

    private static Long L_DOS = 2L;

    private static Long L_TRES = 3L;

    private static Long L_CUATRO = 4L;

    private static Long L_CINCO = 5L;

    private Boolean flagNumDir;

    private SbDireccion sedeSucamecSelect;

    private TipoGamac tipoSolicitud;

    private List<TipoGamac> listTipoSolicitud;

    private String valorBusqueda;
    private static final String LISTA_REG_SOL = "listaRegSol";
    private static final String LISTA_SOL_GEN = "listaSolGen";
    private static final String LISTA_RECOJO = "listaRecojo";

    private List<Map> listArmasVencidas;
    private List<Map> listArmasPorVencer;
    private String cantidadArmasPorVencer;
    private String cantidadArmasVencidas;
    private String opcionListado;
    private int opcion;
    
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

    public int getOpcion() {
        return opcion;
    }

    public void setOpcion(int opcion) {
        this.opcion = opcion;
    }
    
    public AmaSolicitudRecojoController() {
        direccion = new SbDireccion();
    }

    public String prepareList(Boolean ignorar) {
        String res = "/aplicacion/gamac/amaSolicitudRecojo/ListArmas";
        try {
            setOpcionListado(LISTA_REG_SOL);
            if (verificarAutorizacionUsuario()) {
                if (!ignorar && validarArmasPorVencer()) {
                    estado = EstadoCrud.REDIRECT;
                    openDlgNotificacion();
                } else {
                    estado = EstadoCrud.BUSCAR;
                    registro = null;
                    sedeSelect = null;
                    fechaini = null;
                    fechafin = null;
                    idTipoBusqueda = null;
                    filtro = null;
                    lstArmasRecojoFinal = null;
                    lstArmasReingresoFinal = null;
                    cargarTiposSolicitud();
                }
            } else {
                JsfUtil.mensajeAdvertencia(mensajeValidacion());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String prepareListSolGen(Boolean ignorar) {
        String res = "/aplicacion/gamac/amaSolicitudRecojo/ListSolicitudesGeneradas";
        setOpcionListado(LISTA_SOL_GEN);
        if (verificarAutorizacionUsuario()) {
            if (!ignorar && validarArmasPorVencer()) {
                estado = EstadoCrud.REDIRECT;
                openDlgNotificacion();
            } else {
                estado = EstadoCrud.VERSOLREC;
                registro = null;
                cargarTiposSolicitud();
            }
        } else {
            JsfUtil.mensajeAdvertencia(mensajeValidacion());
        }
        return res;
    }

    public void ignorarNotificacion() {
        if (Objects.equals(opcionListado, LISTA_REG_SOL)) {
            prepareList(Boolean.TRUE);
        } else if (Objects.equals(opcionListado, LISTA_SOL_GEN)) {
            prepareListSolGen(Boolean.TRUE);
        }
        RequestContext.getCurrentInstance().execute("PF('NotificacionViewDialog').hide()");
    }

    public void openDlgNotificacion() {
        RequestContext.getCurrentInstance().execute("PF('NotificacionViewDialog').show()");
        RequestContext.getCurrentInstance().update("formNotificacion");
    }

    public String redireccionarFormEntrega(int opc) {
        RequestContext.getCurrentInstance().execute("PF('NotificacionViewDialog').hide()");
        amaInventarioArmaController.setEstadoBusqueda(opc == 1 ? "POR ENTREGAR" : (opc == 2 ? "VENCIDO" : null));
        amaInventarioArmaController.setEstado(EstadoCrud.VERENTARM);
        amaInventarioArmaController.setIdTipoBusquedaEntrega1(null);
        amaInventarioArmaController.setFiltroEntrega(null);
        amaInventarioArmaController.buscarParaEntrega();
//        RequestContext.getCurrentInstance().update("entregaArmasForm:buscarDatatable");
        registro = null;
        return "/aplicacion/gamac/amaInventarioArma/ListArmas.xhtml";
    }

    public Boolean validarArmasPorVencer() {
        Boolean res = Boolean.FALSE;
        try {
            listArmasPorVencer = new ArrayList<>();
            listArmasVencidas = new ArrayList<>();
            setCantidadArmasPorVencer("0");
            setCantidadArmasVencidas("0");
            List<Map> listInvArmas = ejbAmaInventarioArmaFacade.listCantidadArmasRecojoMap(personaUsuario().getId());
            if (listInvArmas != null) {
                for (Map item : listInvArmas) {
                    if (item.get("ESTADO") != null) {
                        switch ((String) item.get("ESTADO")) {
                            case "POR ENTREGAR":
                                setCantidadArmasPorVencer(String.valueOf(item.get("CANTIDAD")));
                                res = Boolean.TRUE;
                                break;
                            case "VENCIDO":
                                setCantidadArmasVencidas(String.valueOf(item.get("CANTIDAD")));
                                res = Boolean.TRUE;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void cargarTiposSolicitud() {
        listTipoSolicitud = ejbTipoGamacFacade.selectTipoGamac("TP_SOLIC");
        tipoSolicitud = null;
    }

    public void reiniciarVlores() {
        sedeSelect = null;
        lstArmasNuevas = null;
        lstArmasRecojoFinal = null;
        lstArmasReingresoFinal = null;
    }

    public void reiniciarListaFinal() {
        lstArmasRecojoFinal = null;
        lstArmasReingresoFinal = null;
    }

    public void mostrarListaSolicitudes() {
        if (tipoSolicitud != null) {
            listaSolicitudRecojo = (new ListDataModel(ejbAmaSolicitudRecojoFacade.listaSolicitudesPorIdPersona(personaUsuario().getId(), tipoSolicitud, valorBusqueda)));
        }
    }

    public Boolean verificarSolicitudesPendientes() {
        Boolean mostrar = Boolean.FALSE;
        List<AmaSolicitudRecojo> lstSolicitudes = ejbAmaSolicitudRecojoFacade.listaSolicitudesPorIdPersona(personaUsuario().getId(), tipoSolicitud, null);
        for (AmaSolicitudRecojo sol : lstSolicitudes) {
            if ( (sol.getFechaRetiroProgramada() == null || sol.getAmaGuiaTransitoList().isEmpty()) && sol.getEstado()!=-1 ){
                mostrar = Boolean.TRUE;
                break;
            }
        }
        return mostrar;
    }

    public Boolean verificarAutorizacionUsuario() {
        Boolean autorizado = Boolean.FALSE;
        try {
            List<Map> lstResolucionRma = ejbRma1369Facade.buscarRDCasaCom(personaUsuario().getRuc() == null ? personaUsuario().getNumDoc() : personaUsuario().getRuc());
            List<AmaResolucion> listResoluciones = ejbAmaResolucionFacade.obtenerResolucionesVigentes(personaUsuario().getRuc() == null ? personaUsuario().getNumDoc() : personaUsuario().getRuc());
            if ((lstResolucionRma != null && !lstResolucionRma.isEmpty())
                || (listResoluciones != null && !listResoluciones.isEmpty())) {  
                estado = EstadoCrud.USUAUTORIZADO;
                autorizado = Boolean.TRUE;
            } else {
                estado = EstadoCrud.USUNOAUTORIZADO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return autorizado;
    }

    public String mensajeValidacion() {
        String msj = personaUsuario().getRznSocial() == null ? JsfUtil.bundleGamac("MensajeUsuarioNatNoAutorizado")
                : JsfUtil.bundleGamac("MensajeUsuarioNoAutorizado1") + " "
                + (personaUsuario().getRznSocial()) + " " + JsfUtil.bundleGamac("MensajeUsuarioNoAutorizado2");
        return msj;
    }

    public String obtenerCorreoSol() {
        if (loginController.getUsuario().getCorreo() != null) {
            return loginController.getUsuario().getCorreo();
        }
        return "NO REGISTRADO";
    }

    /**
     * METODO PAR ANULAR UN REGISTRO DE SOLICITUD DE RECOJO
     *
     * @param objElim, tipo de dato AmaSolicitudRecojo
     */
    public void anular(AmaSolicitudRecojo objElim) {
        if (objElim != null) {
            objElim.setActivo(JsfUtil.FALSE);
            objElim.setAmaInventarioArmaList(new ArrayList<AmaInventarioArma>());
//            objElim.setAmaTarjetaPropiedadList(new ArrayList<AmaTarjetaPropiedad>());
            ejbAmaSolicitudRecojoFacade.anularPorId(objElim);
            mostrarListaSolicitudes();
            RequestContext.getCurrentInstance().update("listarSolForm");
        }
    }

    /**
     * METODO PARA GENERAR EL PDF DE LA SOLICITUD DE RECOJO
     *
     * @param objSolRec, tipo de dato AmaSolicitudRecojo
     * @return Un archivo Pdf
     */
    public StreamedContent verPdf(AmaSolicitudRecojo objSolRec) {
        try {
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
            HashMap p = new HashMap();
            List<AmaSolicitudRecojo> sr = new ArrayList();
            sr.add(objSolRec);

            List<AmaInventarioArma> lstTarjArmas = obtenerArmasVendidas(objSolRec);
            p.put("P_FECHA_IMP", ReportUtil.mostrarFechaString(new Date()));
            p.put("P_FECHA_VENC", ReportUtil.mostrarFechaString(objSolRec.getFechaVencimiento()));
            p.put("P_NUMERO_ARMAS", lstTarjArmas.size());
            p.put("P_FLAG_CUSTODIA", validarCustodiaParaReporte(lstTarjArmas));
            p.put("P_LOGO_SUCAMEC", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
            String jasper = "";
            if (Objects.equals(objSolRec.getTipoSolicitudId().getCodProg(), "TP_SOLIC_RECARM")) {
                p.put("P_LISTA_ARMAS", tablaArmas(lstTarjArmas, 1));
                p.put("P_LISTA_ACCESORIOS", tablaAccesorios(objSolRec.getAmaInventarioArmaList(), 1));
                jasper = "/aplicacion/gamac/amaSolicitudRecojo/reportes/SolicitudRecojo.jasper";
            } else if (Objects.equals(objSolRec.getTipoSolicitudId().getCodProg(), "TP_SOLIC_REIARM")) {
                p.put("P_LISTA_ACCESORIOS", tablaAccesorios(objSolRec.getAmaInventarioArmaList(), 2));
                p.put("P_LISTA_ARMAS", tablaArmas(lstTarjArmas, 2));
                jasper = "/aplicacion/gamac/amaSolicitudRecojo/reportes/SolicitudReingreso.jasper";
            }
            return JsfUtil.generarReportePdf(jasper, sr, p, "solicitud.pdf", null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: EppCertificadoController.reporte :", ex);
        }
    }

    public List<ArmaRepClass> tablaArmas(List<AmaInventarioArma> lstInvArma, int opcion) {
        List<ArmaRepClass> armasRepLst = new ArrayList();
        int i = 0;
        for (AmaInventarioArma invArma : lstInvArma) {
            i += 1;
            ArmaRepClass armaRep = new ArmaRepClass();
            armaRep.setOrden(i);
            armaRep.setTipoArma(invArma.getModeloId().getTipoArmaId());
            armaRep.setMarca(invArma.getModeloId().getMarcaId());
            armaRep.setModelo(invArma.getModeloId().getModelo());
            armaRep.setCalibre(ordenarCalibres(invArma.getModeloId().getAmaCatalogoList()));
            armaRep.setSerie(invArma.getSerie());
            armaRep.setNroRua(invArma.getNroRua());
            // Seteo de número de guía si es una solicitud de ingreso.
            if (opcion == 2) {
                armaRep.setNroGuia(obtenerNroGuiaExhibicion(invArma));
            }
            armaRep.setNombrePersona(obtenerPropietarioActual(invArma));
            armasRepLst.add(armaRep);
        }
        return armasRepLst;
    }

    /**
     *
     * @param arma
     * @return
     */
    public String obtenerPropietarioActual(AmaInventarioArma arma) {
        if (arma != null) {
            AmaTarjetaPropiedad tarjeta = ejbAmaTarjetaPropiedadFacade.obtenerTarjetaPorRuaSerie(arma.getNroRua(), arma.getSerie());
            if (tarjeta != null && tarjeta.getActivo() == JsfUtil.TRUE) {
                return obtenerDescPersonaGt(tarjeta.getPersonaCompradorId());
            }
        }
        return "";
    }

    public List<Map> tablaAccesorios(List<AmaInventarioArma> listArmas, int tipo) {
        List<Map> listRes = null;
        String cadenaIds = "";
        short estado = JsfUtil.FALSE;
        if (listArmas != null && !listArmas.isEmpty()) {
            int tam = listArmas.size();
            int j = 1;
            for (AmaInventarioArma ia : listArmas) {
                if (ia.getActivo() == JsfUtil.TRUE) {
                    estado = ia.getCondicionAlmacen();
                    cadenaIds += ia.getId();
                    if (j < tam) {
                        cadenaIds += ",";
                    }
                    j++;
                }
            }
            if (!cadenaIds.equals("")) {
                listRes = ejbAmaInventarioAccesoriosFacade.listAccesAllArmas(cadenaIds, estado);
            }
        }
        return listRes;
    }

    public String obtenerNroGuiaExhibicion(AmaInventarioArma invArma) {
        if (invArma != null) {
            if (invArma.getAmaGuiaTransitoList() != null && !invArma.getAmaGuiaTransitoList().isEmpty()) {
                if (Objects.equals(invArma.getSituacionId().getCodProg(), "TP_SITU_INT")
                        || Objects.equals(invArma.getSituacionId().getCodProg(), "TP_SITU_EXH")) {
                    for (AmaGuiaTransito amaGuia : invArma.getAmaGuiaTransitoList()) {
                        if (amaGuia.getActivo() == JsfUtil.TRUE) {
                            if (Objects.equals(amaGuia.getTipoGuiaId().getCodProg(), "TP_GTGAMAC_EXH")) {
                                return amaGuia.getNroGuia();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public String ordenarCalibres(List<AmaCatalogo> calibresLst) {
        //ordernar calibres de un arma
        Collections.sort(calibresLst, new Comparator<AmaCatalogo>() {
            @Override
            public int compare(AmaCatalogo one, AmaCatalogo other) {
                return one.getNombre().compareTo(other.getNombre());
            }
        });

        //formar cadena de calibres para un arma
        String calibresStr = "";
        for (AmaCatalogo cal : calibresLst) {
            if (cal.getActivo() == 1) {
                if ("".equals(calibresStr)) {
                    calibresStr = cal.getNombre();
                } else {
                    calibresStr = calibresStr + " / " + cal.getNombre();
                }
            }
        }
        return calibresStr;
    }

    public List<SbDireccion> listaDireccionSucamec() {
        List<SbDireccion> listRes = ejbSbDireccionFacade.listarDireccionesSucamec();
        List<SbDireccion> listTemp = new ArrayList(listRes);
        for (SbDireccion dir : listTemp) {
            if (Objects.equals("POR REGULARIZAR", dir.getReferencia())) {//temporalmente
                listRes.remove(dir);
            }
        }
        return listRes;
    }

    public void removerArmaRecojo(Map itemMap) {
        if (itemMap != null) {
            if (lstArmasRecojoFinal != null && lstArmasRecojoFinal.contains(itemMap)) {
                lstArmasRecojoFinal.remove(itemMap);
            }
            RequestContext.getCurrentInstance().update("formSolRecojo");
            RequestContext.getCurrentInstance().update("listarForm:fsOpciones");
            RequestContext.getCurrentInstance().update("listarForm:dtArmas1");
        }
    }

    public void removerArmaReingreso(Map itemMap) {
        if (itemMap != null) {
            if (lstArmasReingresoFinal != null && lstArmasReingresoFinal.contains(itemMap)) {
                lstArmasReingresoFinal.remove(itemMap);
            }
            RequestContext.getCurrentInstance().update("formSolReingreso");
            RequestContext.getCurrentInstance().update("listarForm:fsOpciones");
            RequestContext.getCurrentInstance().update("listarForm:dtArmas2");
        }
    }

    /**
     * METODO PARA OBTENER LAS ARMAS VENDIDAS POR CADA SOLICITUD DE RECOJO
     * GENERADA
     *
     * @param objSolicitud, tipo de dato AmaSolicitudRecojo
     * @return Un listado de Tarjetas de Propiedad
     */
    public List<AmaInventarioArma> obtenerArmasVendidas(AmaSolicitudRecojo objSolicitud) {
        return ejbAmaInventarioArmaFacade.listaArmasPorSolicitudRecojo(objSolicitud.getId());
    }

    public String descripcionDireccion(SbDireccion dir, String separador) {
        String res = "";
        if (dir != null) {
            res = (dir.getViaId().getCodProg().equals("TP_VIA_OTRO") ? "" : dir.getViaId().getAbreviatura()) + " " + dir.getDireccion() + " " + (dir.getNumero() == null ? "" : dir.getNumero()) + " " + (dir.getDistritoId() == null ? "- " + dir.getPaisId().getNombre() : dir.getDistritoId().getNombre() + separador + dir.getDistritoId().getProvinciaId().getNombre() + separador + dir.getDistritoId().getProvinciaId().getDepartamentoId().getNombre());
        }
        return res;
    }

    public Date fechaMovimientoArma(AmaSolicitudRecojo sol) {
        if (sol != null) {
//            AmaGuiaTransito amaGuia = ejbAmaGuiaTransitoFacade.guiaPorIdSolRecojo(sol.getId());
            AmaGuiaTransito amaGuia = null;
            if (sol.getAmaGuiaTransitoList() != null && !sol.getAmaGuiaTransitoList().isEmpty()) {
                for (AmaGuiaTransito gt : sol.getAmaGuiaTransitoList()) {
                    if (gt.getActivo() == JsfUtil.TRUE) {
                        amaGuia = gt;
                        break;
                    }
                }
            }
            if (Objects.equals(sol.getTipoSolicitudId().getCodProg(), "TP_SOLIC_RECARM")) {
                return amaGuia == null ? null : amaGuia.getFechaSalida();
            } else if (Objects.equals(sol.getTipoSolicitudId().getCodProg(), "TP_SOLIC_REIARM")) {
                return amaGuia == null ? null : amaGuia.getFechaLlegada();
            }
        }
        return null;
    }

    public boolean tieneGuiaEmitida(AmaSolicitudRecojo sol){
        if (sol != null) {
            if (sol.getAmaGuiaTransitoList() != null && !sol.getAmaGuiaTransitoList().isEmpty()) {
                for (AmaGuiaTransito gt : sol.getAmaGuiaTransitoList()) {
                    if (gt.getActivo() == JsfUtil.TRUE && Objects.equals(gt.getEstadoId().getCodProg(), "TP_GTGAMAC_FIN")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public AmaSolicitudRecojo getAmaSolicitudRecojo(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    /**
     * @return the estado
     */
    public EstadoCrud getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(EstadoCrud estado) {
        this.estado = estado;
    }

    /**
     * @return the listaDireccion
     */
    public List<SbDireccion> getListaDireccion() {
        return ejbSbDireccionFacade.listarDireccionesSucamec();
    }

    /**
     * @return the fechaini
     */
    public Date getFechaini() {
        return fechaini;
    }

    /**
     * @param fechaini the fechaini to set
     */
    public void setFechaini(Date fechaini) {
        this.fechaini = fechaini;
    }

    /**
     * @return the fechafin
     */
    public Date getFechafin() {
        return fechafin;
    }

    /**
     * @param fechafin the fechafin to set
     */
    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    /**
     * @return the idTipoBusqueda
     */
    public Long getIdTipoBusqueda() {
        return idTipoBusqueda;
    }

    /**
     * @param idTipoBusqueda the idTipoBusqueda to set
     */
    public void setIdTipoBusqueda(Long idTipoBusqueda) {
        this.idTipoBusqueda = idTipoBusqueda;
    }

    /**
     * @return the filtro
     */
    public String getFiltro() {
        return filtro;
    }

    /**
     * @param filtro the filtro to set
     */
    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    /**
     * @return the registro
     */
    public AmaSolicitudRecojo getRegistro() {
        return registro;
    }

    /**
     * @param registro the registro to set
     */
    public void setRegistro(AmaSolicitudRecojo registro) {
        this.registro = registro;
    }

    /**
     * @return the resultados
     */
    public ListDataModel<AmaArma> getResultados() {
        return resultados;
    }

    /**
     * METODO PARA BUSCAR TARJETAS DE PROPIEDAD QUE NO ESTEEN INCLUIDAS EN UNA
     * SOLICITUD DE RECOJO, Y TAMPOCO EN LA EMISION DE UNA GUIA DE TRANSITO
     *
     * @param msjExito
     */
    public void buscar(String msjExito) {
        try {
            String tipoBus = "";
            Boolean indCamposVacios = Boolean.FALSE;
            lstArmasNuevasSelect = null;
            if (sedeSelect == null && Objects.equals(tipoSolicitud.getCodProg(), "TP_SOLIC_RECARM")) {
                JsfUtil.mensajeAdvertencia("Es necesario que seleccione la sede para listar las armas.");
                return;
            }
            if (fechaini != null || fechafin != null || (idTipoBusqueda != null && (filtro != null && !"".equals(filtro)))) {
                if (idTipoBusqueda != null) {
                    if (idTipoBusqueda == L_UNO) {
                        tipoBus = "A";
                    } else if (idTipoBusqueda == L_DOS) {
                        tipoBus = "B";
                    } else if (idTipoBusqueda == L_TRES) {
                        tipoBus = "C";
                    } else if (idTipoBusqueda == L_CUATRO) {
                        tipoBus = "D";
                    } else if (idTipoBusqueda == L_CINCO) {
                        tipoBus = "E";
                    }
                }
                if (fechaini != null && fechafin == null ) {
                    JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("RequeridoFechaIniNoFechaFin"));
                    indCamposVacios = Boolean.TRUE;
                }
                if (fechafin != null && fechaini == null) {
                    JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("RequeridoFechaFinNoFechaIni"));
                    indCamposVacios = Boolean.TRUE;
                }
                if (idTipoBusqueda != null && (filtro == null || "".equals(filtro))) {
                    JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("RequeridoTextoFiltro"));
                    indCamposVacios = Boolean.TRUE;
                }
                if (indCamposVacios) {
                    return;
                }
                List<Map> listaArmasNuevas = null;
                switch (tipoSolicitud.getCodProg()) {
                    case "TP_SOLIC_RECARM":
//                        listaArmasNuevas = null;
                        listaArmasNuevas = ejbAmaInventarioArmaFacade.listArmasNuevasMap((Objects.equals(tipoBus, "D") ? null : (sedeSelect == null ? null : sedeSelect.getId())), fechaini, fechafin, tipoBus, filtro, personaUsuario().getId());
                        if (listaArmasNuevas != null) {
                            //Validación de la búsqueda por serie de armas que no se encuentran en la sede seleccionada.
                            List<Map> listAux = new ArrayList(listaArmasNuevas);
                            for (Map arma : listAux) {
                                if (!Objects.equals(Long.valueOf(arma.get("IDALMACEN").toString()), sedeSelect == null ? null : sedeSelect.getId())) {
                                    SbDireccion alm = ejbSbDireccionFacade.find(Long.valueOf(arma.get("IDALMACEN").toString()));
                                    JsfUtil.mensajeAdvertencia("El arma con serie: " + ((String) arma.get("SERIE")) + " se encuentra en el almacén de la " + alm.getReferencia()
                                            + ". Por favor, seleccione la sede correcta para realizar la búsqueda.");
                                    listaArmasNuevas.remove(arma);
                                }
                            }
                            //Validación para el caso en que el arma cuente con Solicitud de Reingreso a almacenes SUCAMEC.
                            //En este caso se debe validar que no tenga Solicitud de Recojo de armas para poder listarlo.
                            listAux = new ArrayList(listaArmasNuevas);
                            for (Map arma : listAux) {
                                if (Objects.equals(arma.get("CODTIPOSOL"), "TP_SOLIC_REIARM")) {
                                    AmaInventarioArma ia = ejbAmaInventarioArmaFacade.find(Long.valueOf(arma.get("IDINVARMA").toString()));
                                    if (ia.getAmaSolicitudRecojoList() != null && !ia.getAmaSolicitudRecojoList().isEmpty()) {
                                        for (AmaSolicitudRecojo sr : ia.getAmaSolicitudRecojoList()) {
                                            if (sr.getActivo() == JsfUtil.TRUE && Objects.equals(sr.getTipoSolicitudId().getCodProg(), "TP_SOLIC_RECARM")) {
                                                listaArmasNuevas.remove(arma);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "TP_SOLIC_REIARM":
                        listaArmasNuevas = ejbAmaInventarioArmaFacade.listArmasNuevasMap(fechaini, fechafin, tipoBus, filtro, personaUsuario().getId());
                        if (listaArmasNuevas != null && tipoBus == "D") {
                            List<Map> listAux = new ArrayList(listaArmasNuevas);
                            Date femax = null;
                            long guiaId = 0;
                            for (Map arma : listAux) {
                                AmaGuiaTransito gt = ejbAmaGuiaTransitoFacade.find(Long.valueOf(arma.get("gt_id").toString()));
                                Date feactual = gt.getFechaEmision();
                                if (femax == null) {
                                    femax = gt.getFechaEmision();
                                    guiaId = gt.getId();
                                }
                                if (JsfUtil.getFechaSinHora(feactual).compareTo(JsfUtil.getFechaSinHora(femax)) > 0) {
                                    guiaId = gt.getId();
                                    femax = feactual;
                                }
                            }

                            for (Map arma : listAux) {
                                if (!arma.get("gt_id").equals(guiaId)) {
                                    listaArmasNuevas.remove(arma);
                                }
                            }
                        }
                        //Si existe un arma con mas de una guia de exhibicion, se mostrará unicamente la una fila con datos del arma y la ultima guia
//                        removeArmasMapDuplicadas(listaArmasNuevas);
                        //Fin
                        break;
                    default:
                        break;
                }
                //Fin validación.
                setLstArmasNuevas((ListDataModel<Map>) new ListDataModel(listaArmasNuevas));
//                RequestContext.getCurrentInstance().update("listarForm:buscarDatatable");
                if (!"".equalsIgnoreCase(msjExito)) {
                    JsfUtil.mensaje(msjExito);
                }
            } else {
                setLstArmasNuevas(new ListDataModel<>(new ArrayList<Map>()));
                JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("RequeridoValoresBusqueda1"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * FUNCIÓN QUE REMUEVE ARMAS DUPLICADAS CONSERVANDO ÚNICAMENTE EL ÚLTIMO
     * ARMA REGISTRADA O ACTUALIZADA.
     *
     * @param listInOut
     * @author Gino Chávez
     */
    public void removeArmasMapDuplicadas(List<Map> listInOut) {
        if (listInOut != null && !listInOut.isEmpty()) {
            List<Map> listArmaFinal = null;
            List<Map> listArmaDup = null;
            List<Map> listArmaDupTemp = null;
            List<Map> lstInvArmaTemp1 = new ArrayList<>();
            lstInvArmaTemp1.addAll(listInOut);
            listArmaFinal = new ArrayList<>();
            for (Map amaInv : lstInvArmaTemp1) {
                listArmaDup = new ArrayList<>();
                List<Map> lstInvArmaTemp2 = new ArrayList<>();
                lstInvArmaTemp2.addAll(listInOut);
                for (Map amaInv2 : lstInvArmaTemp2) {
                    if (Objects.equals(amaInv.get("modeloId"), amaInv2.get("modeloId"))
                            && Objects.equals(amaInv.get("serie"), amaInv2.get("serie"))) {
                        listArmaDup.add(amaInv2);
                        listInOut.remove(amaInv2);
                    }
                }
                if (!listArmaDup.isEmpty()) {
                    if (listArmaDup.size() > 1) {
                        listArmaDupTemp = new ArrayList<>();
                        listArmaDupTemp.addAll(listArmaDup);
//                        Long idTemp = -1L;
                        //Buscar el id Mayor en la lista de duplicados.
//                        for (Map map : listArmaDupTemp) {
//                            if ((Long) map.get("id") > idTemp) {
//                                idTemp = (Long) map.get("id");
//                            }
//                        }
                        //Remover los items con id menores
                        for (Map itemMap : listArmaDupTemp) {
//                            if (!Objects.equals((Long) itemMap.get("id"), idTemp)) {
                            listArmaDup.remove(itemMap);
//                            } else {
                            //Añadir, a la lista final, el registro con el id mayor.
                            listArmaFinal.add(itemMap);
//                            }
                            break;
                        }
                    } else {
                        //Añadir a la lista final el registro.
                        for (Map itemMap : listArmaDup) {
                            listArmaFinal.add(itemMap);
                        }
                    }
                }
            }
            //Añadir la lista Final a la lista original. En este punto la lista original llega vacía.
            if (!listArmaFinal.isEmpty()) {
                listInOut.addAll(listArmaFinal);
            }
        }
    }

    public String obtenerDescDireccion(Long idDireccion, String valorRetorno) {
        String res = "";
        if (idDireccion != null) {
            SbDireccion dir = ejbSbDireccionFacade.find(idDireccion);
            if (dir != null) {
                if (valorRetorno != null) {
                    switch (valorRetorno) {
                        case "sede":
                            res = dir.getReferencia() + " - " + (dir.getViaId().getCodProg().equals("TP_VIA_OTRO") ? "" : dir.getViaId().getAbreviatura()) + " " + dir.getDireccion() + " " + (dir.getNumero() == null ? "" : dir.getNumero());
                            break;
                        case "dir":
                            res = (dir.getViaId().getCodProg().equals("TP_VIA_OTRO") ? "" : dir.getViaId().getAbreviatura()) + " " + dir.getDireccion() + " " + (dir.getNumero() == null ? "" : dir.getNumero()) + " " + "- " + dir.getDistritoId().getNombre() + " / " + dir.getDistritoId().getProvinciaId().getNombre() + " / " + dir.getDistritoId().getProvinciaId().getDepartamentoId().getNombre();
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return res;
    }

    /**
     * Función para campo almacén autocompletable
     *
     * @param per Propietario a buscar
     * @return Descripción de propietario (Tipo Doc : Nro. Documento)
     * @author Gino Chávez
     * @version 1.0
     */
    public String obtenerDescPersona(SbPersona per) {
        String nombre = "";

        if (per != null) {
            if (per.getTipoDoc() == null) {
                if (per.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                    nombre = "RUC: " + per.getRuc() + " - " + per.getRznSocial();
                } else {
                    nombre = "DNI: " + per.getNumDoc() + " - " + per.getApePat() + " " + per.getApeMat() + " " + per.getNombres();
                }
            } else {
                nombre = per.getTipoDoc().getNombre() + ": ";
                if (per.getTipoDoc().getCodProg().equals("TP_DOCID_RUC")
                        || per.getTipoDoc().getCodProg().equals("TP_DOCID_EXT")) {
                    nombre += per.getRuc() + " - " + per.getRznSocial();
                } else {
                    nombre += per.getNumDoc() + " - " + per.getApePat() + " " + (per.getApeMat() == null ? "" : per.getApeMat()) + " " + per.getNombres();
                }
            }
        }

        return nombre;
    }

    /**
     * Función para campo almacén autocompletable
     *
     * @param per Propietario a buscar
     * @return Descripción de propietario (Tipo Doc : Nro. Documento)
     * @author Gino Chávez
     * @version 1.0
     */
    public String obtenerDescPersonaGt(SbPersona per) {
        String nombre = "";

        if (per != null) {
            if (per.getTipoDoc() == null) {
                if (per.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                    nombre = "RUC: " + per.getRuc() + " - " + per.getRznSocial();
                } else {
                    nombre = "DNI: " + per.getNumDoc() + " - " + per.getApePat() + " " + per.getApeMat() + " " + per.getNombres();
                }
            } else {
                nombre = per.getTipoDoc().getNombre() + ": ";
                if (per.getTipoDoc().getCodProg().equals("TP_DOCID_RUC")
                        || per.getTipoDoc().getCodProg().equals("TP_DOCID_EXT")) {
                    nombre += per.getRuc() + " - " + per.getRznSocial();
                } else {
                    nombre += per.getNumDoc() + " - " + per.getApePat() + " " + (per.getApeMat() == null ? "" : per.getApeMat()) + " " + per.getNombres();
                }
            }
        }

        return nombre;
    }

    public void limpiarFormPrincipal() {
        fechaini = null;
        fechafin = null;
        idTipoBusqueda = null;
        filtro = "";
    }

    /**
     * METODO PARA OBTENER LOS DATOS PERSONALES DEL USUARIO LOGUEADO
     *
     * @return Datos personales completos del usuario logueado
     */
    public SbPersona personaUsuario() {
        SbPersona personaUsuario = new SbPersona();
        personaUsuario = ejbSbPersonaFacade.find(loginController.getUsuario().getPersona().getId());
        return personaUsuario;
    }

    public List<SbDistrito> completeUbigeo(String query) {
        List<SbDistrito> filteredUbigeo = new ArrayList<>();
        List<SbDistrito> lstUbigeo = ejbSbDistritoFacade.obtenerUbigeo(query);
        for (SbDistrito d : lstUbigeo) {
            if (d.getNombre().contains(query.toUpperCase())
                    || d.getProvinciaId().getNombre().contains(query.toUpperCase())
                    || d.getProvinciaId().getDepartamentoId().getNombre().contains(query.toUpperCase())) {
                filteredUbigeo.add(d);
            }
        }
        return filteredUbigeo;
    }

    /**
     * METODO PAR REGISTRAR DIRECCION
     *
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void agregarDireccion() {
        try {
            SbPersona personaUsuario = personaUsuario();
            if (direccion.getId() == null) {
                direccion = (SbDireccion) JsfUtil.entidadMayusculas(direccion, "");
                //objeto nuevo --> inserta
                direccion.setId(null);
                direccion.setPersonaId(personaUsuario);
                direccion.setActivo(JsfUtil.TRUE);
                direccion.setAudLogin(loginController.getUsuario().getLogin());
                direccion.setAudNumIp(loginController.getNumIP());
                ejbSbDireccionFacade.create(direccion);
                setObjDestino(direccion);
                mostrarDireccionPorPersona(personaUsuario);
                limpiarCamposDireccion();
                getListaDestino();
                RequestContext.getCurrentInstance().execute("PF('CrearDirViewDialog').hide()");
                RequestContext.getCurrentInstance().update("frmDireccion");
                RequestContext.getCurrentInstance().update("formSolRecojo:cboDestino");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void limpiarCamposDireccion() {
        direccion = null;
        direccion = new SbDireccion();
    }

    /**
     * METODO PARA REGISTRAR/GENERAR LA SOLICITUD DE RECOJO
     *
     * @param opcion
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void crear(int opcion) {
        Boolean indicadorDatos = Boolean.TRUE;
        List<Map> listArmasTemp = null;
        validarDireccion();
        if (opcion == 1) {            
            if (lstArmasRecojoFinal != null && !lstArmasRecojoFinal.isEmpty()) {
                listArmasTemp = new ArrayList(lstArmasRecojoFinal);
                if (getObjDestino() == null) {
                    indicadorDatos = Boolean.FALSE;
                    JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("RequeridoSeleccionCboDestino"));
                }
            } else {
                JsfUtil.mensajeError("La lista de armas se encuentra vacía. Por favor, seleccione las armas que desea retirar del almacén de SUCAMEC.");
                return;
            }
        } else if (opcion == 2) {
            if (lstArmasReingresoFinal != null && !lstArmasReingresoFinal.isEmpty()) {
                //if (lstArmasReingresoFinal.size() <= 5) {
                    listArmasTemp = new ArrayList(lstArmasReingresoFinal);
                    if (getObjOrigen() == null) {
                        indicadorDatos = Boolean.FALSE;
                        JsfUtil.mensajeAdvertencia("No es posible guardar los datos.");
                    }
//                } else {
//                    JsfUtil.mensajeAdvertencia("La cantidad máxima de armas a reingresar, mediante una solicitud de reingreso, son 5.");
//                    return;
//                }
            } else {
                JsfUtil.mensajeError("La lista de armas se encuentra vacía. Por favor, seleccione las armas que desea reingresar al almacén de SUCAMEC.");
                return;
            }
        }
        if (objPersonaRecojo == null) {
            indicadorDatos = Boolean.FALSE;
            JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("RequeridoSeleccionCboPersonaRecojo"));
        }
        if (indicadorDatos) {
            SbPersona solicitante = personaUsuario();
            if (verificarSolicitudesPendientes()) {
                JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("MensajeSolicitudesPorProcesar1") + cantidadMaximaArmasSolicitar() + JsfUtil.bundleGamac("MensajeSolicitudesPorProcesar2"));
            }
            
            registro = new AmaSolicitudRecojo();
            registro = (AmaSolicitudRecojo) JsfUtil.entidadMayusculas(registro, "");
            registro.setId(null);
            registro.setTipoSolicitudId(tipoSolicitud);
            registro.setCorrelativo(Long.parseLong(numeroCorrelativo("TP_NUM_REC")));
            registro.setAudLogin(loginController.getUsuario().getLogin());
            registro.setAudNumIp(loginController.getNumIP());
            registro.setActivo(JsfUtil.TRUE);
            registro.setEstado(JsfUtil.TRUE);
            registro.setPersonaArmeriaId(solicitante);
            registro.setPersonaAutRecojoId(objPersonaRecojo);
            registro.setDireccionAlmacenSucamecId(sedeSucamecSelect);
            registro.setDireccionLocalArmeriaId(opcion == 1 ? objDestino : objOrigen);
            registro.setFechaEmision(new Date());
            registro.setAmaInventarioArmaList(obtenerListaInvArmas(listArmasTemp, opcion));
            registro.setFechaVencimiento(calculoFechaVenc(new Date(), 30));
            ejbAmaSolicitudRecojoFacade.create(registro);
            
            if (solicitarRecibo) {
                //System.out.println("Guardando recibo!!");
                Integer usuaTramDoc = ejbSbUsuarioFacade.obtenerIdUsuarioTramDoc("USRWEB");                
                if(!generaExpediente(registro, usuaTramDoc)){
                    JsfUtil.mensajeAdvertencia("No se pudo crear el expediente para el registro ID: " + registro.getId());
                    //continue;
                } else {
                    borrarValoresRecibo();
                }
            }
            
            limpiarFrmPuntoEntrega();
            lstArmasRecojoFinal = null;
            lstArmasReingresoFinal = null;
            String msjExito = JsfUtil.bundleGamac("MensajeRegistroCorrecto");
            buscar(msjExito);
            if (loginController.getUsuario().getCorreo() != null) {
                JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("MensajeEnviaNotificacion") + " " + loginController.getUsuario().getCorreo() + ".");
            } else {
                JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("MensajeRevisarFechaProgramacion"));
            }
            RequestContext.getCurrentInstance().update("listarForm:fsOpciones");
            RequestContext.getCurrentInstance().update("listarForm:listasArmas");
            RequestContext.getCurrentInstance().execute("PF('ConfSolReingresoViewDialog').hide()");
            RequestContext.getCurrentInstance().execute("PF('ConfSolRecojoViewDialog').hide()");
        }
    }

    public Boolean isArmaRecojoSelect(Map item) {
        if (lstArmasRecojoFinal != null) {
            if (lstArmasRecojoFinal.contains(item)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Boolean isArmaReingresoSelect(Map item) {
        if (lstArmasReingresoFinal != null) {
            if (lstArmasReingresoFinal.contains(item)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public List<AmaInventarioArma> obtenerListaInvArmas(List<Map> listIn, int opcion) {
        List<AmaInventarioArma> listRes = null;
        AmaInventarioArma invArma = null;
        if (listIn != null && !listIn.isEmpty()) {
            listRes = new ArrayList<>();
            String campo = "";
            switch (opcion) {
                case 1:
                    campo = "IDINVARMA";
                    break;
                case 2:
                    campo = "id";
                    break;
                default:
                    break;
            }
            for (Map dato : listIn) {
                invArma = ejbAmaInventarioArmaFacade.find(Long.valueOf(dato.get(campo).toString()));
                listRes.add(invArma);
            }
        }
        return listRes;
    }

    public Date calculoFechaVenc(Date fechIni, int nroDias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fechIni.getTime());
        cal.add(Calendar.DATE, nroDias);
        return new Date(cal.getTimeInMillis());
    }

    /**
     * METODO PARA OBTENER EL NUMERO CORRELATIVO PARA REGISTRAR UNA NUEVA
     * SOLICITUD DE RECOJO
     *
     * @param codProg, Tipo de dato String
     * @return El número correlativo que identificará a la Solicitud de Recojo
     */
    public String numeroCorrelativo(String codProg) {
        String correlativo = "";
        String ceros = "";
        String numeracion = ejbSbNumeracionFacade.buscarNumeracionActual(codProg).toString();
        SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
        String anio = formatAnio.format(new Date());
        switch (numeracion.length()) {
            case 1:
                ceros = "000000";
                break;
            case 2:
                ceros = "00000";
                break;
            case 3:
                ceros = "0000";
                break;
            case 4:
                ceros = "000";
                break;
            case 5:
                ceros = "00";
                break;
            case 6:
                ceros = "0";
                break;
            case 7:
                ceros = "";
                break;
            default:
                ceros = "";
                break;
        }
        correlativo = anio + ceros + numeracion;
        return correlativo;
    }

    /**
     * METODO PARA MOSTRAR DIRECCION POR PERSONA
     *
     * @param persona, Tipo de dato SbPersona
     */
    public void mostrarDireccionPorPersona(SbPersona persona) {
        listaDireccionPorPersona = ejbSbDireccionFacade.listarDirecionPorPersona(persona);
    }

    /**
     * EVENTO PARA ABRIR FORMULARIO DE CONFIRMACION
     *
     * @author Gino Chávez
     * @version 2.0
     * @param indicadorAcept, Tipo de dato Boolean
     */
    public void openDlgConfRecojo(Boolean indicadorAcept) {
        opcion = 1;
        if (lstArmasRecojoFinal != null && !lstArmasRecojoFinal.isEmpty()) {
            for (Map arma : lstArmasRecojoFinal) {
                sedeSucamecSelect = ejbSbDireccionFacade.find(Long.valueOf(arma.get("IDALMACEN").toString()));
                break;
            }
            if (validarCustodia() && !indicadorAcept) {
                RequestContext.getCurrentInstance().execute("PF('AvisoEntregaViewDialog').show()");
                RequestContext.getCurrentInstance().update("formMensajeAviso");
            } else {
                if (indicadorAcept) {
                    RequestContext.getCurrentInstance().execute("PF('AvisoEntregaViewDialog').hide()");
                }
                limpiarFrmPuntoEntrega();
                
                parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaIdByTipoProceso("TP_TUP_SUC1", "TP_GTGAMAC_REC");
                borrarValoresRecibo();
                setDisableReciboBn(false);
                setSolicitarRecibo(true);
                
                RequestContext.getCurrentInstance().execute("PF('ConfSolRecojoViewDialog').show()");
                RequestContext.getCurrentInstance().update("formSolRecojo");
            }
        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar al menos un Arma de la lista");
        }
    }

    /**
     * EVENTO PARA ABRIR FORMULARIO DE CONFIRMACION
     *
     * @author Gino Chávez
     * @version 2.0
     * @param indicadorAcept, Tipo de dato Boolean
     */
    public void openDlgConfReingreso(Boolean indicadorAcept) {
        opcion = 2;
        if (lstArmasReingresoFinal != null && !lstArmasReingresoFinal.isEmpty()) {
            for (Map arma : lstArmasReingresoFinal) {
                objOrigen = (SbDireccion) arma.get("direccionDestinoId");
                break;
            }
            if (validarCustodia() && !indicadorAcept) {
                RequestContext.getCurrentInstance().execute("PF('AvisoEntregaViewDialog').show()");
                RequestContext.getCurrentInstance().update("formMensajeAviso");
            } else {
                if (indicadorAcept) {
                    RequestContext.getCurrentInstance().execute("PF('AvisoEntregaViewDialog').hide()");
                }
                limpiarFrmPuntoEntrega();
                
                parametrizacionTupa = ejbSbProcesoTupaFacade.obtenerTupaByTupaIdByTipoProceso("TP_TUP_SUC1", "TP_GTGAMAC_REC");
                borrarValoresRecibo();
                setDisableReciboBn(false);
                setSolicitarRecibo(true);
              
                RequestContext.getCurrentInstance().execute("PF('ConfSolReingresoViewDialog').show()");
                RequestContext.getCurrentInstance().update("formSolReingreso");
            }
        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar al menos un Arma de la lista");
        }
    }
        
//    public Boolean validarGuiasDentroPlazo() {
////        dentroPlazo = Boolean.TRUE;
//        try {
//            List<AmaInvArmaClass> listaTemporal = new ArrayList<>();
//            listaTemporal.addAll(lstArmasEntregaSelect);
////            lstTPEntregaVencida = new ArrayList<>();
//
//            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
//            Date fechahoy = formateador.parse(formateador.format(fechaHoy));
////            for (AmaInventarioArma itemMapVal : listaTemporal) {
////                Date fEmisionGuia = formateador.parse(formateador.format(obtenerFechaEstado(itemMapVal)));
////                Calendar fecha1 = Calendar.getInstance();
////                Calendar fecha2 = Calendar.getInstance();
////                fecha1.setTime(fEmisionGuia);
////                fecha2.setTime(fechahoy);
////                List<Date> listaFeriados = ejbSbFeriadoFacade.listFeriados(fEmisionGuia, fechahoy);
////                if (listaFeriados == null) {
////                    listaFeriados = new ArrayList<>();
////                }
////                int dias = (obtenerCantidadDiasHabiles(fecha1, fecha2) - (listaFeriados.size()));
////                if (dias > 3) {
////                    lstTPEntregaVencida.add(itemMapVal);
////                    lstArmasEntregaSelect.remove(itemMapVal);
////                }
////            }
////            if (!lstTPEntregaVencida.isEmpty()) {
////                dentroPlazo = Boolean.FALSE;
////            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
////        return dentroPlazo;
//    }
//    public String validarFechaVencida(Map item) {
//        String res = "";
//        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
//        Date fechahoy;
//        try {
//            if (JsfUtil.bundleGamac("EstadoTarjetaParaEntregar").equalsIgnoreCase("")) {//obtenerEstado(item)
//                fechahoy = (Date) formateador.parse(formateador.format(fechaHoy));
//                Date fEmisionGuia = (Date) formateador.parse(formateador.format(obtenerFechaEstado(item)));
//                Calendar fecha1 = Calendar.getInstance();
//                Calendar fecha2 = Calendar.getInstance();
//                fecha1.setTime(fEmisionGuia);
//                fecha2.setTime(fechahoy);
//                List<Date> listaFeriados = ejbSbFeriadoFacade.listFeriados(fEmisionGuia, fechahoy);
//                if (listaFeriados == null) {
//                    listaFeriados = new ArrayList<>();
//                } else {
//                    List<Date> lstFeriadoTemp = new ArrayList<>();
//                    List<Date> lstFinSemana = obtenerFechaFinSemana(fecha1, fecha2);
//                    lstFeriadoTemp.addAll(listaFeriados);
//                    for (Date f_feriado : lstFeriadoTemp) {
//                        if (!lstFinSemana.isEmpty()) {
//                            for (Date f_fin_semana : lstFinSemana) {
//                                if (f_fin_semana.equals(f_feriado)) {
//                                    listaFeriados.remove(f_feriado);
//                                }
//                            }
//                        }
//                    }
//                }
//                int dias = (obtenerCantidadDiasHabiles(fecha1, fecha2) - (listaFeriados.size()));
//                if (dias > 3) {
//                    res = "datatable-row-amarillo";
//                } else {
//                    res = "datatable-row-verde";
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return res;
//    }
//    public String validarFechaVencida(AmaInventarioArma item) {
//        String res = "";
//        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
//        Date fechahoy;
//        try {
//            if (JsfUtil.bundleGamac("EstadoTarjetaParaEntregar").equalsIgnoreCase("")) {//obtenerEstado(item)
//                fechahoy = (Date) formateador.parse(formateador.format(fechaHoy));
//                Date fEmisionGuia = new Date();//(Date) formateador.parse(formateador.format(obtenerFechaEstado(item)));
//                Calendar fecha1 = Calendar.getInstance();
//                Calendar fecha2 = Calendar.getInstance();
//                fecha1.setTime(fEmisionGuia);
//                fecha2.setTime(fechahoy);
//                List<Date> listaFeriados = ejbSbFeriadoFacade.listFeriados(fEmisionGuia, fechahoy);
//                if (listaFeriados == null) {
//                    listaFeriados = new ArrayList<>();
//                } else {
//                    List<Date> lstFeriadoTemp = new ArrayList<>();
//                    List<Date> lstFinSemana = obtenerFechaFinSemana(fecha1, fecha2);
//                    lstFeriadoTemp.addAll(listaFeriados);
//                    for (Date f_feriado : lstFeriadoTemp) {
//                        if (!lstFinSemana.isEmpty()) {
//                            for (Date f_fin_semana : lstFinSemana) {
//                                if (f_fin_semana.equals(f_feriado)) {
//                                    listaFeriados.remove(f_feriado);
//                                }
//                            }
//                        }
//                    }
//                }
//                int dias = (obtenerCantidadDiasHabiles(fecha1, fecha2) - (listaFeriados.size()));
//                if (dias > 3) {
//                    res = "datatable-row-amarillo";
//                } else {
//                    res = "datatable-row-verde";
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return res;
//    }
//    public int obtenerCantidadDiasHabiles(Calendar fechaInicial, Calendar fechaFinal) {
//        int totalDias = -1;
//        Calendar fechaTemporal = new GregorianCalendar();
//        fechaTemporal.setTime(fechaInicial.getTime());
//        while (fechaTemporal.before(fechaFinal) || fechaTemporal.equals(fechaFinal)) {
//            if (fechaTemporal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaTemporal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
//                totalDias++;
//            }
//            fechaTemporal.add(Calendar.DATE, 1);
//        }
//        return totalDias;
//    }
//
//    public List<Date> obtenerFechaFinSemana(Calendar fechaInicial, Calendar fechaFinal) {
//        List<Date> listaFinSemana = new ArrayList<>();
//        Calendar fechaTemporal = new GregorianCalendar();
//        fechaTemporal.setTime(fechaInicial.getTime());
//        while (fechaTemporal.before(fechaFinal) || fechaTemporal.equals(fechaFinal)) {
//            if (fechaTemporal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || fechaTemporal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
//                listaFinSemana.add(fechaTemporal.getTime());
//            }
//            fechaTemporal.add(Calendar.DATE, 1);
//        }
//        return listaFinSemana;
//    }
//
    public Boolean validarCustodia() {
        Boolean res = Boolean.FALSE;
        int contTipoCaso1 = 0;
        int contTipoCaso2 = 0;
        if (lstArmasRecojoFinal != null && !lstArmasRecojoFinal.isEmpty()) {
            for (Map arma : lstArmasRecojoFinal) {
                AmaArma amaArma = ejbAmaArmaFacade.find(Long.valueOf(arma.get("IDARMA").toString()));
                if ("TP_ARMCAE".equals(amaArma.getModeloId().getTipoArmaId().getCodProg())
                        || "TP_ARMCAR".equals(amaArma.getModeloId().getTipoArmaId().getCodProg())
                        || "TP_ARMESC".equals(amaArma.getModeloId().getTipoArmaId().getCodProg())) {
                    contTipoCaso1 += 1;
                } else if ("TP_ARMPIS".equals(amaArma.getModeloId().getTipoArmaId().getCodProg())
                        || "TP_ARMREV".equals(amaArma.getModeloId().getTipoArmaId().getCodProg())) {
                    contTipoCaso2 += 1;
                }
            }
            if (contTipoCaso1 > 5 || contTipoCaso2 > 10) {
                res = Boolean.TRUE;
            }
        }
        if (lstArmasReingresoFinal != null && !lstArmasReingresoFinal.isEmpty()) {
            for (Map arma : lstArmasReingresoFinal) {
                //System.out.println("ITEM ID: " + (Long) arma.get("id"));
                AmaInventarioArma amaArma = ejbAmaInventarioArmaFacade.find((Long) arma.get("id")) ;
                if ("TP_ARMCAE".equals(amaArma.getModeloId().getTipoArmaId().getCodProg())
                        || "TP_ARMCAR".equals(amaArma.getModeloId().getTipoArmaId().getCodProg())
                        || "TP_ARMESC".equals(amaArma.getModeloId().getTipoArmaId().getCodProg())) {
                    contTipoCaso1 += 1;
                } else if ("TP_ARMPIS".equals(amaArma.getModeloId().getTipoArmaId().getCodProg())
                        || "TP_ARMREV".equals(amaArma.getModeloId().getTipoArmaId().getCodProg())) {
                    contTipoCaso2 += 1;
                }
            }
            if (contTipoCaso1 > 5 || contTipoCaso2 > 10) {
                res = Boolean.TRUE;
            }
        }
        return res;
    }

    public Boolean validarCustodiaParaReporte(List<AmaInventarioArma> lista) {
        Boolean res = Boolean.FALSE;
        int contTipoCaso1 = 0;
        int contTipoCaso2 = 0;
        if (lista != null && !lista.isEmpty()) {
            for (AmaInventarioArma arma : lista) {
                if ("TP_ARMCAE".equals(arma.getModeloId().getTipoArmaId().getCodProg())
                        || "TP_ARMCAR".equals(arma.getModeloId().getTipoArmaId().getCodProg())
                        || "TP_ARMESC".equals(arma.getModeloId().getTipoArmaId().getCodProg())) {
                    contTipoCaso1 += 1;
                } else if ("TP_ARMPIS".equals(arma.getModeloId().getTipoArmaId().getCodProg())
                        || "TP_ARMREV".equals(arma.getModeloId().getTipoArmaId().getCodProg())) {
                    contTipoCaso2 += 1;
                }
            }
            if (contTipoCaso1 > 5 || contTipoCaso2 > 10) {
                res = Boolean.TRUE;
            }
        }
        return res;
    }

    public void habilitarNumDir() {
        flagNumDir = Boolean.FALSE;
        if (direccion != null && direccion.getViaId() != null) {
            if ("TP_VIA_OTRO".equals(direccion.getViaId().getCodProg())) {
                flagNumDir = Boolean.TRUE;
            }
        }
        RequestContext.getCurrentInstance().update("frmDireccion:numero2");
    }

    public String obtenerCalibresMap(Map tarjetaArma) {
        String r = "";
        String separador = "/";
        AmaArma amaArma = ejbAmaArmaFacade.find(Long.valueOf(tarjetaArma.get("IDARMA").toString()));
        int tam = amaArma.getModeloId().getAmaCatalogoList().size();
        for (AmaCatalogo amaCat : amaArma.getModeloId().getAmaCatalogoList()) {
            if (tam > 1) {
                r += amaCat.getNombre() + separador;
            } else {
                r += amaCat.getNombre();
            }
            tam--;
        }
        return r;
    }

    public String obtenerCalibres(AmaModelos mod) {
        if (mod != null) {
            String r = "";
            String separador = "/";
            int tam = mod.getAmaCatalogoList().size();
            for (AmaCatalogo amaCat : mod.getAmaCatalogoList()) {
                if (tam > 1) {
                    r += amaCat.getNombre() + separador;
                } else {
                    r += amaCat.getNombre();
                }
                tam--;
            }
            return r;
        }
        return null;
    }

    public void limpiarFrmPuntoEntrega() {
        setObjDestino(null);
        objPersonaRecojo = null;
    }

    public void validarDireccion() {
        if (objDestino != null && objDestino.getDistritoId() != null) {
            if (objDestino.getDistritoId().getId() == 0L) {
                objDestino = null;
                JsfUtil.mensajeAdvertencia("La dirección seleccionada no cuenta con Ubigeo válido. Por favor comuníquese con SUCAMEC para que realice la actualizacion correspondiente.");
            }
        }
    }

    public int cantidadMaximaArmasSolicitar(){
        return Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("AmaSolicitudRecRei_NroMaxArmasSolicitar").getValor());
    }

    public void anadirArmaRecojo(Map itemMap) {
        if (!Objects.equals((String) itemMap.get("COD_ESTTP"), "TP_EST_NULIDAD")) {
            if (lstArmasRecojoFinal == null) {
                lstArmasRecojoFinal = new ArrayList<>();
            }
            if (!lstArmasRecojoFinal.contains(itemMap)) {
                int cantidadMax = cantidadMaximaArmasSolicitar();
                if (lstArmasRecojoFinal.size() < cantidadMax) {
                    lstArmasRecojoFinal.add(itemMap);
                }else{
                    JsfUtil.mensajeAdvertencia("Ya tiene agregados " + StringUtils.leftPad(Integer.toString(lstArmasRecojoFinal.size()), 2, "0") + " armas de fuego, para traslados de más de " + StringUtils.leftPad(Integer.toString(cantidadMax), 2, "0") + " armas de fuego se debe solicitar y obtener una guía de tránsito mediante VUCE.");
                }                
            } else {
                lstArmasRecojoFinal.remove(itemMap);
                JsfUtil.mensajeAdvertencia("El arma seleccionada se quitó de la lista temporal para la solicitud. La serie del arma es: " + itemMap.get("SERIE"));
            }
            RequestContext.getCurrentInstance().update("listarForm:fsOpciones");
        } else {
            JsfUtil.mensajeError("No es posible realizar la solicitud de recojo de un arma cuya Tarjeta de Propiedad se encuentra en estado 'NULIDAD'.");
        }
    }

    public void anadirArmaReingreso(Map itemMap) {
        if (lstArmasReingresoFinal == null) {
            lstArmasReingresoFinal = new ArrayList<>();
        }
        if (!lstArmasReingresoFinal.contains(itemMap)) {
            int cantidadMax = cantidadMaximaArmasSolicitar();
            if (lstArmasReingresoFinal.size() < cantidadMax) {
                lstArmasReingresoFinal.add(itemMap);
            }else{
                JsfUtil.mensajeAdvertencia("Ya tiene agregados " + StringUtils.leftPad(Integer.toString(lstArmasReingresoFinal.size()), 2, "0") + " armas de fuego, para traslados de más de " + StringUtils.leftPad(Integer.toString(cantidadMax), 2, "0") + " armas de fuego se debe solicitar y obtener una guía de tránsito mediante VUCE.");
            }
        } else {
            lstArmasReingresoFinal.remove(itemMap);
            JsfUtil.mensajeAdvertencia("El arma seleccionada se quitó de la lista temporal para la solicitud. La serie del arma es: " + itemMap.get("serie"));
        }
        RequestContext.getCurrentInstance().update("listarForm:fsOpciones");
    }

    public void openArmasFinal() {

    }

    /**
     * EVENTO PARA ABRIR FORMULARIO DE REGISTRO DE DIRECCION
     *
     * @author Gino Chávez
     * @version 2.0
     */
    public void openDlgCrearDireccion() {
        limpiarCamposDireccion();
        listaDestino = new ArrayList<>();
        listaDestino.clear();
        RequestContext.getCurrentInstance().execute("PF('CrearDirViewDialog').show()");
        RequestContext.getCurrentInstance().update("frmDireccion");
    }

    /**
     * @return the registrosSeleccionados
     */
    public ListDataModel<AmaArma> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * @param registrosSeleccionados the registrosSeleccionados to set
     */
    public void setRegistrosSeleccionados(ListDataModel<AmaArma> registrosSeleccionados) {
        this.registrosSeleccionados = registrosSeleccionados;
    }

    /**
     * @return the lstArmasNuevasSelect
     */
    public List<Map> getLstArmasNuevasSelect() {
        return lstArmasNuevasSelect;
    }

    /**
     * @param lstArmasNuevasSelect the lstArmasNuevasSelect to set
     */
    public void setLstArmasNuevasSelect(List<Map> lstArmasNuevasSelect) {
        this.lstArmasNuevasSelect = lstArmasNuevasSelect;
    }

    /**
     * @return the listaDestino
     */
    public List<SbDireccion> getListaDestino() {
        SbPersona usuarioLog = personaUsuario();
        listaDestino = ejbSbDireccionFacade.listarDirecionPorPersona(usuarioLog);
        return listaDestino;
    }

    public List<SbDepartamento> listarDepartamentos() {
        List<SbDepartamento> listDepa = new ArrayList<SbDepartamento>();
        listDepa = ejbSbDepartamentoFacade.lstDepartamentos();
        return listDepa;
    }

    public List<SbProvincia> listarProvincias() {
        List<SbProvincia> listaProv = new ArrayList<SbProvincia>();
        if (direccion.getDistritoId().getProvinciaId().getDepartamentoId() != null) {
            listaProv = ejbSbProvinciaFacade.lstProvincias(direccion.getDistritoId().getProvinciaId().getDepartamentoId());
        }
        return listaProv;
    }

    public List<SbDistrito> listarDistritos() {
        List<SbDistrito> listaDist = new ArrayList<SbDistrito>();
        if (direccion.getDistritoId().getProvinciaId() != null) {
            listaDist = ejbSbDistritoFacade.lstDistritos(direccion.getDistritoId().getProvinciaId());
        }
        return listaDist;
    }

    /**
     * @param listaDestino the listaDestino to set
     */
    public void setListaDestino(List<SbDireccion> listaDestino) {
        this.listaDestino = listaDestino;
    }

    /**
     * @return the listaPersonaRecojo
     */
    public List<SbPersona> getListaPersonaRecojo() {
        listaPersonaRecojo = ejbSbRelacionPersonaFacade.listarPersonasAutorizadas(personaUsuario().getId());
        return listaPersonaRecojo;
    }

    /**
     * @param listaPersonaRecojo the listaPersonaRecojo to set
     */
    public void setListaPersonaRecojo(List<SbPersona> listaPersonaRecojo) {
        this.listaPersonaRecojo = listaPersonaRecojo;
    }

    /**
     * @return the objPersonaRecojo
     */
    public SbPersona getObjPersonaRecojo() {
        return objPersonaRecojo;
    }

    /**
     * @param objPersonaRecojo the objPersonaRecojo to set
     */
    public void setObjPersonaRecojo(SbPersona objPersonaRecojo) {
        this.objPersonaRecojo = objPersonaRecojo;
    }

    /**
     * @return the direccion
     */
    public SbDireccion getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(SbDireccion direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the listaDireccionPorPersona
     */
    public List<SbDireccion> getListaDireccionPorPersona() {
        return listaDireccionPorPersona;
    }

    /**
     * @param listaDireccionPorPersona the listaDireccionPorPersona to set
     */
    public void setListaDireccionPorPersona(List<SbDireccion> listaDireccionPorPersona) {
        this.listaDireccionPorPersona = listaDireccionPorPersona;
    }

    /**
     * @return the listaSolicitudRecojo
     */
    public ListDataModel<AmaSolicitudRecojo> getListaSolicitudRecojo() {
        return listaSolicitudRecojo;
    }

    /**
     * @param listaSolicitudRecojo the listaSolicitudRecojo to set
     */
    public void setListaSolicitudRecojo(ListDataModel<AmaSolicitudRecojo> listaSolicitudRecojo) {
        this.listaSolicitudRecojo = listaSolicitudRecojo;
    }

    /**
     * @return the listTarjConSolRecojo
     */
    public List<Map> getListTarjConSolRecojo() {
        return listTarjConSolRecojo;
    }

    /**
     * @param listTarjConSolRecojo the listTarjConSolRecojo to set
     */
    public void setListTarjConSolRecojo(List<Map> listTarjConSolRecojo) {
        this.listTarjConSolRecojo = listTarjConSolRecojo;
    }

    /**
     * @return the listTarjConGuiaTran
     */
    public List<Map> getListTarjConGuiaTran() {
        return listTarjConGuiaTran;
    }

    /**
     * @param listTarjConGuiaTran the listTarjConGuiaTran to set
     */
    public void setListTarjConGuiaTran(List<Map> listTarjConGuiaTran) {
        this.listTarjConGuiaTran = listTarjConGuiaTran;
    }

    /**
     * @return the fechaHoy
     */
    public Date getFechaHoy() {
        return fechaHoy;
    }

    /**
     * @param fechaHoy the fechaHoy to set
     */
    public void setFechaHoy(Date fechaHoy) {
        this.fechaHoy = fechaHoy;
    }

    /**
     * @return the personaTitular
     */
    public SbPersona getPersonaTitular() {
        return personaTitular;
    }

    /**
     * @param personaTitular the personaTitular to set
     */
    public void setPersonaTitular(SbPersona personaTitular) {
        this.personaTitular = personaTitular;
    }

    /**
     * @return the flagNumDir
     */
    public Boolean getFlagNumDir() {
        return flagNumDir;
    }

    /**
     * @param flagNumDir the flagNumDir to set
     */
    public void setFlagNumDir(Boolean flagNumDir) {
        this.flagNumDir = flagNumDir;
    }

    public Date getFprueba() {
        return fprueba;
    }

    public void setFprueba(Date fprueba) {
        this.fprueba = fprueba;
    }

    public SbDireccion getSedeSelect() {
        return sedeSelect;
    }

    public void setSedeSelect(SbDireccion sedeSelect) {
        this.sedeSelect = sedeSelect;
    }

    public ListDataModel<Map> getLstArmasNuevas() {
        return lstArmasNuevas;
    }

    public void setLstArmasNuevas(ListDataModel<Map> lstArmasNuevas) {
        this.lstArmasNuevas = lstArmasNuevas;
    }

    public SbDireccion getSedeSucamecSelect() {
        return sedeSucamecSelect;
    }

    public void setSedeSucamecSelect(SbDireccion sedeSucamecSelect) {
        this.sedeSucamecSelect = sedeSucamecSelect;
    }

    public SbDireccion getObjDestino() {
        return objDestino;
    }

    public void setObjDestino(SbDireccion objDestino) {
        this.objDestino = objDestino;
    }

    public TipoGamac getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(TipoGamac tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public List<TipoGamac> getListTipoSolicitud() {
        return listTipoSolicitud;
    }

    public void setListTipoSolicitud(List<TipoGamac> listTipoSolicitud) {
        this.listTipoSolicitud = listTipoSolicitud;
    }

    public SbDireccion getObjOrigen() {
        return objOrigen;
    }

    public void setObjOrigen(SbDireccion objOrigen) {
        this.objOrigen = objOrigen;
    }

    public List<Map> getLstArmasRecojoFinal() {
        return lstArmasRecojoFinal;
    }

    public void setLstArmasRecojoFinal(List<Map> lstArmasRecojoFinal) {
        this.lstArmasRecojoFinal = lstArmasRecojoFinal;
    }

    public List<Map> getLstArmasReingresoFinal() {
        return lstArmasReingresoFinal;
    }

    public void setLstArmasReingresoFinal(List<Map> lstArmasReingresoFinal) {
        this.lstArmasReingresoFinal = lstArmasReingresoFinal;
    }

    public String getValorBusqueda() {
        return valorBusqueda;
    }

    public void setValorBusqueda(String valorBusqueda) {
        this.valorBusqueda = valorBusqueda;
    }

    public List<Map> getListArmasVencidas() {
        return listArmasVencidas;
    }

    public void setListArmasVencidas(List<Map> listArmasVencidas) {
        this.listArmasVencidas = listArmasVencidas;
    }

    public List<Map> getListArmasPorVencer() {
        return listArmasPorVencer;
    }

    public void setListArmasPorVencer(List<Map> listArmasPorVencer) {
        this.listArmasPorVencer = listArmasPorVencer;
    }

    public String getOpcionListado() {
        return opcionListado;
    }

    public void setOpcionListado(String opcionListado) {
        this.opcionListado = opcionListado;
    }

    /**
     * @return the cantidadArmasPorVencer
     */
    public String getCantidadArmasPorVencer() {
        return cantidadArmasPorVencer;
    }

    /**
     * @param cantidadArmasPorVencer the cantidadArmasPorVencer to set
     */
    public void setCantidadArmasPorVencer(String cantidadArmasPorVencer) {
        this.cantidadArmasPorVencer = cantidadArmasPorVencer;
    }

    /**
     * @return the cantidadArmasVencidas
     */
    public String getCantidadArmasVencidas() {
        return cantidadArmasVencidas;
    }

    /**
     * @param cantidadArmasVencidas the cantidadArmasVencidas to set
     */
    public void setCantidadArmasVencidas(String cantidadArmasVencidas) {
        this.cantidadArmasVencidas = cantidadArmasVencidas;
    }

    /**
     * Autocompletar para la busqueda de recibos
     *
     * @param query
     * @return
     */
public List<SbRecibos> autoCompleteRecibos(String query) {
        try {
            Long recFilter = null;
            try {
                recFilter = Long.parseLong(query.trim());
            } catch (Exception e) {
                JsfUtil.mensajeError("El voucher debe contener sólo numeros");
                return null;
            }
            borrarValoresRecibo();
            List<SbRecibos> filteredList = new ArrayList();

            HashMap mMap = new HashMap();
            mMap.put("tipo", null);
            mMap.put("importe", parametrizacionTupa.getImporte() );
            mMap.put("codigo", parametrizacionTupa.getCodTributo() );

            //System.out.println("JsfUtil.getLoggedUser().getNumDoc(): " + JsfUtil.getLoggedUser().getNumDoc());
            SbPersonaGt per = ejbPersonaFacade.selectPersonaActivaxRuc(JsfUtil.getLoggedUser().getNumDoc());
            List<SbRecibos> lstUniv = ejbSbRecibosFacade.listarRecibosByImporteByCodAtributo(per, mMap);
            //List<SbRecibos> lstUniv = sbRecibosFacade.listarRecibosByFiltro(mMap);
            if (lstUniv!=null) {
                for (SbRecibos x : lstUniv) {
                    if (x.getSbReciboRegistroList().isEmpty()) {
                        //if (x.getNroSecuencia().toString().contains(query.trim())) {
                        if (x.getNroSecuencia().equals(Long.parseLong(query.trim()))) {
                            filteredList.add(x);
                        }                            
                    }
                }                
            }                
            return filteredList;
        } catch (Exception e) {
            borrarValoresRecibo();
            e.printStackTrace();
            return null;
        }
    }

    public void lstnrSelectRecibo(SelectEvent event) {
        try {
            if (event!=null) {
                borrarValoresRecibo();            
                reciboBn = (SbRecibos) event.getObject();
                strNroSecuencia = "<b>" + reciboBn.getNroSecuencia() + "</b>";
                strFechaMovimiento = "<b>" + JsfUtil.formatoFechaDdMmYyyy(reciboBn.getFechaMovimiento()) + "</b>";
                strImporte = "<b>" + reciboBn.getImporte() + "</b>";
                setDisableReciboBn(true);
                //setEligeReciboLista(true);            
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
        //setEligeReciboLista(false);
    }
    
    public void borrarReciboSeleccionado() {
        borrarValoresRecibo();
        setDisableReciboBn(false);
    }
    
    public boolean generaExpediente(AmaSolicitudRecojo reg, Integer usuaTramDoc){
        boolean validacion = true;
        
        if(reciboBn != null){
            SbPersonaGt per = ejbPersonaFacade.selectPersonaActivaxRuc(JsfUtil.getLoggedUser().getNumDoc());        

            String asunto = ((reg.getPersonaArmeriaId().getRuc() != null)?
                    (reg.getPersonaArmeriaId().getRznSocial()+" - RUC: "+reg.getPersonaArmeriaId().getRuc() ):
                    (reg.getPersonaArmeriaId().getApePat() + " " + reg.getPersonaArmeriaId().getApeMat() + " " + reg.getPersonaArmeriaId().getNombres() +" - "+ 
                        (reg.getPersonaArmeriaId().getTipoDoc() != null ?" - "+reg.getPersonaArmeriaId().getTipoDoc().getNombre():" - DNI" )+": "+reg.getPersonaArmeriaId().getNumDoc() ));
            String titulo = "SOLICITUD WEB DE EMISIÓN DE GUIA DE TRANSITO";

            String expediente = wsTramDocController.crearExpediente_guiaExp(reciboBn, per , asunto , parametrizacionTupa.getCydocIdProceso() , usuaTramDoc, usuaTramDoc, titulo);

            if(expediente != null){
                // Recibo utilizado
                SbReciboRegistro rec = new SbReciboRegistro();
                rec.setId(null);
                rec.setRegistroId(null);
                rec.setNroExpediente(expediente);
                rec.setReciboId(reciboBn);
                rec.setActivo((short) 1);
                rec.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                rec.setAudNumIp(JsfUtil.getIpAddress());
                if(reciboBn.getSbReciboRegistroList() == null){
                    reciboBn.setSbReciboRegistroList(new ArrayList());
                }
                reciboBn.getSbReciboRegistroList().add(rec);
                ejbSbRecibosFacade.edit(reciboBn);
                reg.setNroExpediente(expediente);
                ejbAmaSolicitudRecojoFacade.edit(reg);
                //
            } else {
                validacion = false;            
            }            
        } else {
            return true;
        }
        
        return validacion;
    }

    
    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "amaSolicitudRecojoConverter")
    public static class AmaSolicitudRecojoControllerConverterN extends AmaSolicitudRecojoControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = AmaSolicitudRecojo.class)
    public static class AmaSolicitudRecojoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AmaSolicitudRecojoController c = (AmaSolicitudRecojoController) JsfUtil.obtenerBean("amaSolicitudRecojoController", AmaSolicitudRecojoController.class);
            return c.getAmaSolicitudRecojo(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AmaSolicitudRecojo) {
                AmaSolicitudRecojo o = (AmaSolicitudRecojo) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + TipoGamac.class.getName());
            }
        }
    }

}
