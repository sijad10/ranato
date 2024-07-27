package pe.gob.sucamec.bdintegrado.jsf;

import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import pe.gob.sucamec.bdintegrado.jsf.util.PaginationHelper;
import pe.gob.sucamec.bdintegrado.bean.AmaGuiaTransitoFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import pe.gob.sucamec.bdintegrado.bean.AmaArmaFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaTarjetaPropiedadFacade;
import pe.gob.sucamec.bdintegrado.bean.EppAlmacenAduaneroFacade;
import pe.gob.sucamec.bdintegrado.bean.EppPuertoAduaneroFacade;
import pe.gob.sucamec.bdintegrado.bean.SbDireccionFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbPersonaGtFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoGamacFacade;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaSolicitudRecojo;
import pe.gob.sucamec.bdintegrado.data.EppAlmacenAduanero;
import pe.gob.sucamec.bdintegrado.data.EppPuertoAduanero;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.jsf.util.EstadoCrud;
import pe.gob.sucamec.sistemabase.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.renagi.jsf.util.StringUtil;
import pe.gob.sucamec.sistemabase.data.TipoBase;
import pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade;
import pe.gob.sucamec.bdintegrado.bean.TrazaFacade;
import pe.gob.sucamec.bdintegrado.jsf.util.AmaGuiaTransitoGenericoController;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioAccesorios;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArtconexo;

@Named("amaGuiaTransitoController")
@SessionScoped
public class AmaGuiaTransitoController implements Serializable {

    private AmaGuiaTransito current;
    private DataModel items = null;

    private PaginationHelper pagination;
    private int selectedItemIndex;

    private EstadoCrud estado;

    private Date fechaini;

    private Date fechafin;

    private Date fechaHoy = new Date();

    private Long idTipoBusqueda;

    private String filtro;

    private SbPersonaGt personaUsuario;

    private AmaSolicitudRecojo registroSol;

    private AmaGuiaTransito registro;

    private ListDataModel<AmaSolicitudRecojo> listaSolGT;

    private List<AmaSolicitudRecojo> listaSolGTSelected;
    
    private TipoGamac idTipoGuia;

    private List<TipoGamac> listTipoGuia;
    
    private String rgReferido;
    
    private List<TipoGamac> listOrigen;

    private TipoGamac tipoOrigenSelect;

    private List<TipoGamac> listDestino;

    private TipoGamac tipoDestinoSelect;
    
    private List<AmaGuiaTransito> listAmaGuiaDerEncar;

    private ListDataModel<AmaArma> listaArmasGuia;

    private List<AmaArma> listaArmasTemp;

    private List<AmaArma> listaArmasGuiaSelect;
    
    private List<EppAlmacenAduanero> listaAlmacen;

    private SbDireccion selectedDestOtro;

    private SbDireccion selectedDestSucamec;

    private SbDireccion selectedDestLocal;

    private EppAlmacenAduanero selectedDestAlm;

    private EppPuertoAduanero selectedDestPuer;

    private SbDireccion selectedOriOtro;

    private SbDireccion selectedOriSucamec;

    private SbDireccion selectedOriLocal;

    private EppAlmacenAduanero selectedOriAlm;

    private EppPuertoAduanero selectedOriPuer;

    private AmaArma selectedAmaArma;
    
    private static Long L_UNO = 1L;

    private static Long L_DOS = 2L;

    private static Long L_TRES = 3L;

    private static Long L_CUATRO = 4L;

    private static Long L_CINCO = 5L;
    
    private static String vacio = "";

    private static String espacio = " ";
    
    private Boolean flagRegInvArma = Boolean.FALSE;//DEVOL
    
    private List<AmaInventarioArma> listCopiaInvArmaTemp;
    private AmaInventarioArma copiaInvArmaTemp;
    private Date fechaReg;
    private Boolean isEvaluador = Boolean.FALSE;
    private Boolean isCoordinador = Boolean.FALSE;
    private Boolean isGerente = Boolean.FALSE;
    private TipoGamac idTipoGuiaBus;
    private Date fechaIni;
    private Date fechaFin;
    private TipoBase estadoGuiaBusSelect;
    private Long idEstadoExp;
    private Boolean isBandejaRev;
    private String listProcesos;
    private List<String> listExpSinGuia = new ArrayList<>();
    private String listIdsUsuTD = StringUtil.VACIO;
    private List<Map> listaTipo1 = new ArrayList<>();
    private List<Map> listaTipo2 = new ArrayList<>();
    private List<Map> listaTipo3 = new ArrayList<>();
    private List<Map> listaTipo4 = new ArrayList<>();
    private List<Map> listaTipo5 = new ArrayList<>();
    private List<Map> listaTipo6 = new ArrayList<>();
    private List<Map> listaTipo7 = new ArrayList<>();
    private List<Map> listaTipo8 = new ArrayList<>();
    private List<Map> listaTipo9 = new ArrayList<>();
    private List<Map> listaTipo10 = new ArrayList<>();
    private List<Map> listaTipo11 = new ArrayList<>();
    private List<Map> listaTipo12 = new ArrayList<>();
    private List<Map> listaTipo13 = new ArrayList<>();
    private List<Map> listaTipo14 = new ArrayList<>();
    private List<Map> listaTipo15 = new ArrayList<>();
    private List<Map> listaTipo16 = new ArrayList<>();
    private static String COOR_REVIERTE = "Coordinador - GAMAC (Comercio)";
    private static String GER_REVIERTE = "Gerente - GAMAC (Comercio)";
    private ListDataModel<Map> listExpGuias;
    private static String DIF_DEP_DEV = "DIF_DEP_DEV";
    private List<AmaInventarioAccesorios> lstAccesorios;
    private List<AmaInventarioArtconexo> listArticulos;

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaGuiaTransitoFacade ejbFacade;

    @EJB
    private SbPersonaGtFacade ejbSbPersonaFacade;
    
    @EJB
    private SbDireccionFacadeGt ejbSbDireccionFacade;
    @EJB
    private AmaArmaFacade ejbAmaArmaFacade;
    @EJB
    private AmaTarjetaPropiedadFacade ejbAmaTarjetaPropiedadFacade;
    @EJB
    private EppAlmacenAduaneroFacade ejbEppAlmacenAduaneroFacade;
    @EJB
    private EppPuertoAduaneroFacade ejbEppPuertoAduaneroFacade;
    @EJB
    private TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private TipoGamacFacade ejbTipoGamacFacade;
    @EJB
    private ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private TrazaFacade ejbTrazaFacade;

    @Inject
    private LoginController loginController;
    @Inject
    private AmaGuiaTransitoGenericoController amaGuiaTransitoGenericoController;

    public AmaGuiaTransitoController() {
    }

    public AmaGuiaTransito getSelected() {
        if (current == null) {
            current = new AmaGuiaTransito();
            selectedItemIndex = -1;
        }
        return current;
    }

    private AmaGuiaTransitoFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareSolicitudesGuia() {
        String res = "";
        obtenerPersonaUsuario();
        registroSol = null;
        estado = EstadoCrud.BUSCAR;
        res = "/aplicacion/gamac/amaGuiaTransito/ListSolicitudesGT";
        return res;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (AmaGuiaTransito) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new AmaGuiaTransito();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AmaGuiaTransitoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            // JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public void accionCrearSolicitudGT() {
        estado = EstadoCrud.CREAR;
        if (listaArmasTemp != null) {
            listaArmasTemp.clear();
        } else {
            listaArmasTemp = new ArrayList<>();
        }
        listaArmasGuia = new ListDataModel(new ArrayList<AmaArma>());
        setSelectedAmaArma(null);
        if (listaArmasGuiaSelect != null) {
            listaArmasGuiaSelect.clear();
        } else {
            listaArmasGuiaSelect = new ArrayList<>();
        }
        listTipoGuia = null;
        idTipoGuia = null;
        rgReferido = null;
        limpiarDatosTraslado();
    }
    
    public void limpiarDatosTraslado() {
        listOrigen = null;
        tipoOrigenSelect = null;

        listDestino = null;
        tipoDestinoSelect = null;

        setSelectedOriAlm(null);
        setSelectedOriPuer(null);
        setSelectedOriOtro(null);
        setSelectedOriSucamec(null);
        setSelectedOriLocal(null);
        setSelectedDestAlm(null);
        setSelectedDestPuer(null);
        setSelectedDestOtro(null);
        setSelectedDestSucamec(null);
        setSelectedDestLocal(null);
    }

    public void accionVer(AmaSolicitudRecojo amaSol) {

    }

    public void accionEditar(AmaSolicitudRecojo amaSol) {

    }

    public void accionAnular(AmaSolicitudRecojo amaSol) {

    }
    
    public List<EppAlmacenAduanero> completeAlmacenAduanero(String query) {
        List<EppAlmacenAduanero> filteredAlmacen = new ArrayList<>();
        List<EppAlmacenAduanero> lstAlmacen = ejbEppAlmacenAduaneroFacade.selectLike(null);
        if (lstAlmacen != null && !lstAlmacen.isEmpty()) {
            for (EppAlmacenAduanero dir : lstAlmacen) {
                if (dir.getDireccion().contains(query.toUpperCase())) {
                    filteredAlmacen.add(dir);
                }
            }
        }
        return filteredAlmacen;
    }

    public List<EppPuertoAduanero> completePuertoAduanero(String query) {
        List<EppPuertoAduanero> filteredPuerto = new ArrayList<>();
        List<EppPuertoAduanero> lstPuerto = ejbEppPuertoAduaneroFacade.selectLike(null);
        if (lstPuerto != null && !lstPuerto.isEmpty()) {
            for (EppPuertoAduanero dir : lstPuerto) {
                if (dir.getNombre().contains(query.toUpperCase())) {
                    filteredPuerto.add(dir);
                }
            }
        }
        return filteredPuerto;
    }

    public List<SbDireccionGt> completeSbDireccion(String query) {
        List<SbDireccionGt> filteredSbDireccion = new ArrayList<>();
        List<SbDireccionGt> lstDireccion = ejbSbDireccionFacade.selectLike(null);
        if (lstDireccion != null && !lstDireccion.isEmpty()) {
            for (SbDireccionGt dir : lstDireccion) {
                if (dir.getDireccion().contains(query.toUpperCase())) {
                    filteredSbDireccion.add(dir);
                }
            }
        }
        return filteredSbDireccion;
    }

    public List<SbDireccionGt> completeSbDireccionSucamec(String query) {
        List<SbDireccionGt> filteredSbDireccion = new ArrayList<>();
        List<SbDireccionGt> lstDireccion = ejbSbDireccionFacade.listarDireccionesSucamec();
        if (lstDireccion != null && !lstDireccion.isEmpty()) {
            for (SbDireccionGt dir : lstDireccion) {
                if (dir.getDireccion().contains(query.toUpperCase())
                        || dir.getReferencia().contains(query.toUpperCase())) {
                    filteredSbDireccion.add(dir);
                }
            }
        }
        return filteredSbDireccion;
    }

    public List<SbDireccionGt> completeSbDireccionLocal(String query) {
        List<SbDireccionGt> filteredSbDireccion = new ArrayList<>();
        List<SbDireccionGt> lstDireccion = ejbSbDireccionFacade.listarDireccionesXPersona(personaUsuario.getId());
        if (lstDireccion != null && !lstDireccion.isEmpty()) {
            for (SbDireccionGt dir : lstDireccion) {
                if (dir.getDireccion().contains(query.toUpperCase())) {
                    filteredSbDireccion.add(dir);
                }
            }
        }
        return filteredSbDireccion;
    }
    
//    public List<Map> completeSbOrigenDestino(String query) {
//        List<Map> filteredSbDireccion = new ArrayList<>();
//        List<Map> lstDireccion = ejbSbDireccionFacade.listarDireccionesXPersonaMap(personaUsuario.getId());
//        if (lstDireccion != null && !lstDireccion.isEmpty()) {
//            for (Map dir : lstDireccion) {
//                if (((String)dir.get("direccion")).contains(query.toUpperCase())) {
//                    filteredSbDireccion.add(dir);
//                }
//            }
//        }
//        return filteredSbDireccion;
//    }

    /**
     * FUNCION PARA AUTOCOMPLETAR DATOS DE Usuario
     *
     * @author Gino Chávez Pajuelo
     * @version 2.0
     * @param query Nombre de usuario a buscar
     * @return Listado de Usuarios
     */
    public List<AmaArma> completeDatosArmaAnterior(String query) {
        Long idTG = obtenerIdTipoBus();
        List<AmaArma> filteredArma = new ArrayList<>();
        List<AmaArma> lstArma = ejbAmaArmaFacade.listarArmasXCriteriosAnterior(query, idTG, personaUsuario);
        if (lstArma != null && !lstArma.isEmpty()) {
            for (AmaArma amaArma : lstArma) {
                if (amaArma.getNroRua().contains(query.toUpperCase())
                        || amaArma.getSerie().contains(query.toUpperCase())) {
                    filteredArma.add(amaArma);
                }
            }
        }
        return filteredArma;
    }

    public Long obtenerIdTipoBus() {
        Long idTG = 0L;
        if (idTipoGuia != null) {
            if (idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_EXH")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_IMP")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_DON")) {
                idTG = L_UNO;
            } else if (idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_POL")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_AGE")) {
                idTG = L_DOS;
            } else if (idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_ALM")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_DEV")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_SUC")) {
                idTG = L_TRES;
            }
        }
        return idTG;
    }

    public void agregarArma() {
        if (selectedAmaArma != null) {
            if (!listaArmasTemp.contains(selectedAmaArma)) {
                listaArmasTemp.add(selectedAmaArma);
            }
            listaArmasGuia = new ListDataModel(listaArmasTemp);
            selectedAmaArma = null;
        } else {
            //JsfUtil.mensajeAdvertencia(JsfUtil.bundle("IngreseArmaAgregar"));
        }
    }

    public String obtenerCalibres(AmaArma amaArma) {
        String r = vacio;
        String separador = "/";
        //AmaArma amaArma = ejbAmaArmaFacade.find((Long) tarjetaArma.get("idArma"));
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
    
    public void regresar() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * METODO PARA OBTENER LOS DATOS PERSONALES DEL USUARIO LOGUEADO
     *
     * @return Datos personales completos del usuario logueado
     */
    public SbPersonaGt obtenerPersonaUsuario() {
        personaUsuario = new SbPersonaGt();
        personaUsuario = ejbSbPersonaFacade.find(loginController.getUsuario().getPersona().getId());
        return personaUsuario;
    }

    public String prepareEdit() {
        current = (AmaGuiaTransito) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public void buscar() {
        listaSolGT = new ListDataModel<>();
    }
    
    public void guardarSolicitud(Boolean indicador) {
        try {
            String msj = vacio;
            List<AmaArma> listArmas = new ArrayList<>();
            if (listaArmasGuia != null && listaArmasGuia.getRowCount() > 0) {
                if (!"TP_GTGAMAC_AGE".equalsIgnoreCase(idTipoGuia.getCodProg()) || ("TP_GTGAMAC_AGE".equalsIgnoreCase(idTipoGuia.getCodProg()) && listaArmasGuia.getRowCount() >= 5)) {
                    if (indicador) {
                        RequestContext.getCurrentInstance().execute("PF('AvisoEntregaViewDialog').show()");
                    } else {
                        for (AmaArma amaArma : listaArmasGuia) {
                            listArmas.add(amaArma);
                        }
                        if (estado == EstadoCrud.EDITAR) {
                            //registro = amaGuiaSelect;
                        } else {
                            registro = new AmaGuiaTransito();
                            registro.setActivo((short) 1);
                            //registro.setNroExpediente(itemSelect.getNumero());
                            registro.setNroGuia("0");
                            //registro.setEstadoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GTGAMAC_CRE"));
                            //registro.setUsuarioId(ejbSbUsuarioFacade.obtenerUsuarioXLogin(loginController.getUsuario().getLogin()));
                        }
                        registro.setAmaArmaList(new ArrayList<AmaArma>());
                        registro.setAmaArmaList(listArmas);
//                        registro.setRgReferenciada(RgReferido);
//                        registro.setTipoOrigen(tipoOrigenSelect);
//                        registro.setTipoDestino(tipoDestinoSelect);
//                        registro.setOrigenAlmacenAduana(selectedOriAlm);
//                        registro.setOrigenPuerto(selectedOriPuer);
                        //
                        registro.setTipoGuiaId(idTipoGuia);
                        if (selectedDestOtro != null) {
                            registro.setDireccionDestinoId(selectedDestOtro);
                        } else if (selectedDestSucamec != null) {
                            registro.setDireccionDestinoId(selectedDestSucamec);
                        } else if (selectedDestLocal != null) {
                            registro.setDireccionDestinoId(selectedDestLocal);
                        }
                        if (selectedOriOtro != null) {
                            registro.setDireccionOrigenId(selectedOriOtro);
                        } else if (selectedOriSucamec != null) {
                            registro.setDireccionOrigenId(selectedOriSucamec);
                        } else if (selectedOriLocal != null) {
                            registro.setDireccionOrigenId(selectedOriLocal);
                        }
                        if (estado == EstadoCrud.EDITAR) {
                           // ejbAmaGuiaTransitoFacade.edit(registro);
                            msj = JsfUtil.bundle("MensajeConfirmacionActualizacionRegistro");
                        } else {
                           // ejbAmaGuiaTransitoFacade.create(registro);
                            msj = JsfUtil.bundle("MensajeConfirmacionCreacionRegistro");
                        }
                        regresar();
                        //RequestContext.getCurrentInstance().execute("PF('AvisoEntregaViewDialog').hide()");
                        JsfUtil.mensaje(msj);
                    }
                } else {
                    JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeListaCantidadMinima"));
                }
            } else {
                JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeListaArmasVacia"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Boolean validarCustodia() {
        Boolean res = Boolean.FALSE;
        int contTipoCaso1 = 0;
        int contTipoCaso2 = 0;
        if (listaArmasGuia != null && listaArmasGuia.getRowCount() > 0) {
            for (AmaArma arma : listaArmasGuia) {
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
    
    public void eliminarArmas() {
        List<AmaArma> listaTemp = new ArrayList<>();
        listaTemp.addAll(getListaArmasTemp());
        for (AmaArma tarjArma : listaTemp) {
            if (listaArmasGuiaSelect.contains(tarjArma)) {
                listaArmasTemp.remove(tarjArma);
            }
        }
        setListaArmasGuia((ListDataModel<AmaArma>) new ListDataModel(listaArmasTemp));
        listaArmasGuiaSelect.clear();
    }
    
    public void actualizarGridTraslado() {
        RequestContext.getCurrentInstance().update("registroSolForm:datosTraslado");
    }
    
    public void inicializarOrigenDestino() {
        limpiarDatosTraslado();
        if (idTipoGuia != null) {
            //EnumSet<OrigenDestino> todoOrigen = EnumSet.allOf(OrigenDestino.class);
            List<TipoGamac> listO = new ArrayList<>();

            //EnumSet<OrigenDestino> todoDestino = EnumSet.allOf(OrigenDestino.class);
            List<TipoGamac> listD = new ArrayList<>();
            listOrigen = new ArrayList<>();
            listDestino = new ArrayList<>();

            switch (idTipoGuia.getCodProg()) {
                case "TP_GTGAMAC_EXH":
                    listO.add(tipoGamac("TP_GTORDES_ALMS"));
                    listD.add(tipoGamac("TP_GTORDES_LOC"));
                    break;
                case "TP_GTGAMAC_POL":
                    listO.add(tipoGamac("TP_GTORDES_ALMS"));
                    listO.add(tipoGamac("TP_GTORDES_LOC"));

                    listD.add(tipoGamac("TP_GTORDES_POL"));
                    break;
                case "TP_GTGAMAC_IMP":
                    listO.add(tipoGamac("TP_GTORDES_ALMA"));
                    listO.add(tipoGamac("TP_GTORDES_PUER"));

                    listD.add(tipoGamac("TP_GTORDES_ALMS"));
                    break;
                case "TP_GTGAMAC_ALM":
                    listO.add(tipoGamac("TP_GTORDES_ALMS"));
                    listO.add(tipoGamac("TP_GTORDES_LOC"));
                    listO.add(tipoGamac("TP_GTORDES_POL"));

                    listD.add(tipoGamac("TP_GTORDES_ALMS"));
                    break;
                case "TP_GTGAMAC_DEV":
                    listO.add(tipoGamac("TP_GTORDES_ALMS"));

                    listD.add(tipoGamac("TP_GTORDES_ALMS"));
                    listD.add(tipoGamac("TP_GTORDES_LOC"));
                    listD.add(tipoGamac("TP_GTORDES_POL"));
                    break;
                case "TP_GTGAMAC_DON":
                    listO.add(tipoGamac("TP_GTORDES_ALMS"));
                    listD.add(tipoGamac("TP_GTORDES_DON"));
                    break;
                case "TP_GTGAMAC_AGE":
                    listO.add(tipoGamac("TP_GTORDES_LOC"));
                    listD.add(tipoGamac("TP_GTORDES_LOC"));
                    break;
                case "TP_GTGAMAC_SUC":
                    listO.add(tipoGamac("TP_GTORDES_ALMS"));

                    listD.add(tipoGamac("TP_GTORDES_ALMS"));
                    break;
                default:
                    break;
            }
            setListOrigen(listO);
            setListDestino(listD);
        }
        RequestContext.getCurrentInstance().update("registroSolForm:datosTraslado");
        //RequestContext.getCurrentInstance().update("editarForm:datosTraslado");
    }
    
    public TipoGamac tipoGamac(String codProg) {
        return ejbTipoGamacFacade.buscarTipoGamacXCodProg(codProg);
    }

    public String update() {
        try {
            getFacade().edit(current);
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AmaGuiaTransitoUpdated"));
            return "View";
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (AmaGuiaTransito) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AmaGuiaTransitoDeleted"));
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }
//
//    public SelectItem[] getItemsAvailableSelectMany() {
//        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
//    }
//
//    public SelectItem[] getItemsAvailableSelectOne() {
//        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
//    }

    public AmaGuiaTransito getAmaGuiaTransito(java.lang.Long id) {
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
     * @return the personaUsuario
     */
    public SbPersonaGt getPersonaUsuario() {
        return personaUsuario;
    }

    /**
     * @param personaUsuario the personaUsuario to set
     */
    public void setPersonaUsuario(SbPersonaGt personaUsuario) {
        this.personaUsuario = personaUsuario;
    }

    /**
     * @return the registroSol
     */
    public AmaSolicitudRecojo getRegistroSol() {
        return registroSol;
    }

    /**
     * @param registroSol the registroSol to set
     */
    public void setRegistroSol(AmaSolicitudRecojo registroSol) {
        this.registroSol = registroSol;
    }

    /**
     * @return the registro
     */
    public AmaGuiaTransito getRegistro() {
        return registro;
    }

    /**
     * @param registro the registro to set
     */
    public void setRegistro(AmaGuiaTransito registro) {
        this.registro = registro;
    }

    /**
     * @return the listaSolGT
     */
    public ListDataModel<AmaSolicitudRecojo> getListaSolGT() {
        return listaSolGT;
    }

    /**
     * @param listaSolGT the listaSolGT to set
     */
    public void setListaSolGT(ListDataModel<AmaSolicitudRecojo> listaSolGT) {
        this.listaSolGT = listaSolGT;
    }

    /**
     * @return the listaSolGTSelected
     */
    public List<AmaSolicitudRecojo> getListaSolGTSelected() {
        return listaSolGTSelected;
    }

    /**
     * @param listaSolGTSelected the listaSolGTSelected to set
     */
    public void setListaSolGTSelected(List<AmaSolicitudRecojo> listaSolGTSelected) {
        this.listaSolGTSelected = listaSolGTSelected;
    }

    /**
     * @return the idTipoGuia
     */
    public TipoGamac getIdTipoGuia() {
        return idTipoGuia;
    }

    /**
     * @param idTipoGuia the idTipoGuia to set
     */
    public void setIdTipoGuia(TipoGamac idTipoGuia) {
        this.idTipoGuia = idTipoGuia;
    }

    /**
     * @return the listTipoGuia
     */
    public List<TipoGamac> getListTipoGuia() {
        listTipoGuia = ejbTipoGamacFacade.selectTipoGamac("TP_GTGAMAC");
        if (listTipoGuia != null && !listTipoGuia.isEmpty()) {
            List<TipoGamac> listTemporal = new ArrayList<>();
            listTemporal.addAll(listTipoGuia);
            for (TipoGamac tipoGamac : listTemporal) {
                if ("TP_GTGAMAC_REC".equalsIgnoreCase(tipoGamac.getCodProg())) {
                    listTipoGuia.remove(tipoGamac);
                }
            }
        }

        return listTipoGuia;
    }

    /**
     * @param listTipoGuia the listTipoGuia to set
     */
    public void setListTipoGuia(List<TipoGamac> listTipoGuia) {
        this.listTipoGuia = listTipoGuia;
    }

    /**
     * @return the rgReferido
     */
    public String getRgReferido() {
        return rgReferido;
    }

    /**
     * @param rgReferido the rgReferido to set
     */
    public void setRgReferido(String rgReferido) {
        this.rgReferido = rgReferido;
    }

    /**
     * @return the listOrigen
     */
    public List<TipoGamac> getListOrigen() {
        return listOrigen;
    }

    /**
     * @param listOrigen the listOrigen to set
     */
    public void setListOrigen(List<TipoGamac> listOrigen) {
        this.listOrigen = listOrigen;
    }

    /**
     * @return the tipoOrigenSelect
     */
    public TipoGamac getTipoOrigenSelect() {
        return tipoOrigenSelect;
    }

    /**
     * @param tipoOrigenSelect the tipoOrigenSelect to set
     */
    public void setTipoOrigenSelect(TipoGamac tipoOrigenSelect) {
        this.tipoOrigenSelect = tipoOrigenSelect;
    }

    /**
     * @return the listDestino
     */
    public List<TipoGamac> getListDestino() {
        return listDestino;
    }

    /**
     * @param listDestino the listDestino to set
     */
    public void setListDestino(List<TipoGamac> listDestino) {
        this.listDestino = listDestino;
    }

    /**
     * @return the tipoDestinoSelect
     */
    public TipoGamac getTipoDestinoSelect() {
        return tipoDestinoSelect;
    }

    /**
     * @param tipoDestinoSelect the tipoDestinoSelect to set
     */
    public void setTipoDestinoSelect(TipoGamac tipoDestinoSelect) {
        this.tipoDestinoSelect = tipoDestinoSelect;
    }

    /**
     * @return the listAmaGuiaDerEncar
     */
    public List<AmaGuiaTransito> getListAmaGuiaDerEncar() {
        return listAmaGuiaDerEncar;
    }

    /**
     * @param listAmaGuiaDerEncar the listAmaGuiaDerEncar to set
     */
    public void setListAmaGuiaDerEncar(List<AmaGuiaTransito> listAmaGuiaDerEncar) {
        this.listAmaGuiaDerEncar = listAmaGuiaDerEncar;
    }

    /**
     * @return the listaArmasGuia
     */
    public ListDataModel<AmaArma> getListaArmasGuia() {
        return listaArmasGuia;
    }

    /**
     * @param listaArmasGuia the listaArmasGuia to set
     */
    public void setListaArmasGuia(ListDataModel<AmaArma> listaArmasGuia) {
        this.listaArmasGuia = listaArmasGuia;
    }

    /**
     * @return the listaArmasTemp
     */
    public List<AmaArma> getListaArmasTemp() {
        return listaArmasTemp;
    }

    /**
     * @param listaArmasTemp the listaArmasTemp to set
     */
    public void setListaArmasTemp(List<AmaArma> listaArmasTemp) {
        this.listaArmasTemp = listaArmasTemp;
    }

    /**
     * @return the listaArmasGuiaSelect
     */
    public List<AmaArma> getListaArmasGuiaSelect() {
        return listaArmasGuiaSelect;
    }

    /**
     * @param listaArmasGuiaSelect the listaArmasGuiaSelect to set
     */
    public void setListaArmasGuiaSelect(List<AmaArma> listaArmasGuiaSelect) {
        this.listaArmasGuiaSelect = listaArmasGuiaSelect;
    }

    /**
     * @return the listaAlmacen
     */
    public List<EppAlmacenAduanero> getListaAlmacen() {
        return listaAlmacen;
    }

    /**
     * @param listaAlmacen the listaAlmacen to set
     */
    public void setListaAlmacen(List<EppAlmacenAduanero> listaAlmacen) {
        this.listaAlmacen = listaAlmacen;
    }

    /**
     * @return the selectedAmaArma
     */
    public AmaArma getSelectedAmaArma() {
        return selectedAmaArma;
    }

    /**
     * @param selectedAmaArma the selectedAmaArma to set
     */
    public void setSelectedAmaArma(AmaArma selectedAmaArma) {
        this.selectedAmaArma = selectedAmaArma;
    }

    /**
     * @return the selectedDestOtro
     */
    public SbDireccion getSelectedDestOtro() {
        return selectedDestOtro;
    }

    /**
     * @param selectedDestOtro the selectedDestOtro to set
     */
    public void setSelectedDestOtro(SbDireccion selectedDestOtro) {
        this.selectedDestOtro = selectedDestOtro;
    }

    /**
     * @return the selectedDestSucamec
     */
    public SbDireccion getSelectedDestSucamec() {
        return selectedDestSucamec;
    }

    /**
     * @param selectedDestSucamec the selectedDestSucamec to set
     */
    public void setSelectedDestSucamec(SbDireccion selectedDestSucamec) {
        this.selectedDestSucamec = selectedDestSucamec;
    }

    /**
     * @return the selectedDestLocal
     */
    public SbDireccion getSelectedDestLocal() {
        return selectedDestLocal;
    }

    /**
     * @param selectedDestLocal the selectedDestLocal to set
     */
    public void setSelectedDestLocal(SbDireccion selectedDestLocal) {
        this.selectedDestLocal = selectedDestLocal;
    }

    /**
     * @return the selectedDestAlm
     */
    public EppAlmacenAduanero getSelectedDestAlm() {
        return selectedDestAlm;
    }

    /**
     * @param selectedDestAlm the selectedDestAlm to set
     */
    public void setSelectedDestAlm(EppAlmacenAduanero selectedDestAlm) {
        this.selectedDestAlm = selectedDestAlm;
    }

    /**
     * @return the selectedDestPuer
     */
    public EppPuertoAduanero getSelectedDestPuer() {
        return selectedDestPuer;
    }

    /**
     * @param selectedDestPuer the selectedDestPuer to set
     */
    public void setSelectedDestPuer(EppPuertoAduanero selectedDestPuer) {
        this.selectedDestPuer = selectedDestPuer;
    }

    /**
     * @return the selectedOriOtro
     */
    public SbDireccion getSelectedOriOtro() {
        return selectedOriOtro;
    }

    /**
     * @param selectedOriOtro the selectedOriOtro to set
     */
    public void setSelectedOriOtro(SbDireccion selectedOriOtro) {
        this.selectedOriOtro = selectedOriOtro;
    }

    /**
     * @return the selectedOriSucamec
     */
    public SbDireccion getSelectedOriSucamec() {
        return selectedOriSucamec;
    }

    /**
     * @param selectedOriSucamec the selectedOriSucamec to set
     */
    public void setSelectedOriSucamec(SbDireccion selectedOriSucamec) {
        this.selectedOriSucamec = selectedOriSucamec;
    }

    /**
     * @return the selectedOriLocal
     */
    public SbDireccion getSelectedOriLocal() {
        return selectedOriLocal;
    }

    /**
     * @param selectedOriLocal the selectedOriLocal to set
     */
    public void setSelectedOriLocal(SbDireccion selectedOriLocal) {
        this.selectedOriLocal = selectedOriLocal;
    }

    /**
     * @return the selectedOriAlm
     */
    public EppAlmacenAduanero getSelectedOriAlm() {
        return selectedOriAlm;
    }

    /**
     * @param selectedOriAlm the selectedOriAlm to set
     */
    public void setSelectedOriAlm(EppAlmacenAduanero selectedOriAlm) {
        this.selectedOriAlm = selectedOriAlm;
    }

    /**
     * @return the selectedOriPuer
     */
    public EppPuertoAduanero getSelectedOriPuer() {
        return selectedOriPuer;
    }

    /**
     * @param selectedOriPuer the selectedOriPuer to set
     */
    public void setSelectedOriPuer(EppPuertoAduanero selectedOriPuer) {
        this.selectedOriPuer = selectedOriPuer;
    }

    @FacesConverter(forClass = AmaGuiaTransito.class)
    public static class AmaGuiaTransitoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AmaGuiaTransitoController controller = (AmaGuiaTransitoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "amaGuiaTransitoController");
            return controller.getAmaGuiaTransito(getKey(value));
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
            if (object instanceof AmaGuiaTransito) {
                AmaGuiaTransito o = (AmaGuiaTransito) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + AmaGuiaTransito.class.getName());
            }
        }

    }

    public static java.util.Date toDate(java.sql.Timestamp timestamp) {
        if (timestamp != null) {
            long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
            return new java.util.Date(milliseconds);
        }
        return new Date();
    }

    /**
     * @return the flagRegInvArma
     */
    public Boolean getFlagRegInvArma() {
        return flagRegInvArma;
    }

    /**
     * @param flagRegInvArma the flagRegInvArma to set
     */
    public void setFlagRegInvArma(Boolean flagRegInvArma) {
        this.flagRegInvArma = flagRegInvArma;
    }

    /**
     * @return the listCopiaInvArmaTemp
     */
    public List<AmaInventarioArma> getListCopiaInvArmaTemp() {
        return listCopiaInvArmaTemp;
    }

    /**
     * @param listCopiaInvArmaTemp the listCopiaInvArmaTemp to set
     */
    public void setListCopiaInvArmaTemp(List<AmaInventarioArma> listCopiaInvArmaTemp) {
        this.listCopiaInvArmaTemp = listCopiaInvArmaTemp;
    }

    public AmaInventarioArma getCopiaInvArmaTemp() {
        return copiaInvArmaTemp;
    }

    public void setCopiaInvArmaTemp(AmaInventarioArma copiaInvArmaTemp) {
        this.copiaInvArmaTemp = copiaInvArmaTemp;
    }    

    /**
     * FUNCIÓN QUE REALIZA LA BÚSQUEDA DE EXPEDIENTES, SEGÚN LOS VALORES
     * INGRESADOS, PARA EL MÓDULO COMERCIO.
     *
     * @param org, Boolean. TRUE: Invocación desde otra función,FALSE:
     * Invocación desde la vista.
     * @author Gino Chávez
     */
    public void buscarExpedientes(Boolean org) {
        String tipoBusqueda = StringUtil.VACIO;
        Date fecha = new Date();
        String msjValidacion = StringUtil.VACIO;
        if (L_CUATRO.equals(idTipoBusqueda)) {
            filtro = null;
        } else {
            fechaReg = null;
        }
        if (isEvaluador && (idTipoBusqueda == null && ((filtro == null || StringUtil.VACIO.equals(filtro.trim())) && fechaReg == null)) && (idTipoGuiaBus == null) && (fechaIni == null && fechaFin == null) && estadoGuiaBusSelect == null && idEstadoExp == null) {
            msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda1");
        } else if (isEvaluador && (idTipoBusqueda == null && ((filtro == null || StringUtil.VACIO.equals(filtro.trim())) && fechaReg == null)) && (fechaIni == null && fechaFin == null) && (idTipoGuiaBus != null)) {
            msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda2");
        } else if (isEvaluador && (idTipoBusqueda == null && ((filtro == null || StringUtil.VACIO.equals(filtro.trim())) && fechaReg == null)) && (fechaIni == null && fechaFin == null) && (estadoGuiaBusSelect != null)) {
            msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda7");
        } else if (isEvaluador && (idTipoBusqueda == null && ((filtro == null || StringUtil.VACIO.equals(filtro.trim())) && fechaReg == null)) && (fechaIni == null && fechaFin == null) && (idEstadoExp != null)) {
            msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda8");
        } else if (isEvaluador && idTipoBusqueda != null && ((filtro == null || StringUtil.VACIO.equals(filtro.trim())) && fechaReg == null)) {
            msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda3");
        } else if (isEvaluador && (fechaIni != null && fechaFin == null) || (fechaIni == null && fechaFin != null)) {
            msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda6");
        } else if (isEvaluador && idTipoBusqueda == null && ((filtro != null && !StringUtil.VACIO.equals(filtro.trim())) || fechaReg != null)) {
            msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda4");
        } else if (isEvaluador && fechaIni != null && fechaFin != null && fechaReg != null) {
            msjValidacion = JsfUtil.bundle("RequeridoValoresBusqueda5");
        }
        if (fechaFin != null && fechaFin.compareTo(fechaHoy) > 0) {
            msjValidacion = "La fecha Máxima para la búsqueda por rango de fecha es hasta el día de hoy";
        }
        if (StringUtil.VACIO.equalsIgnoreCase(msjValidacion)) {
            if (Objects.equals(idTipoBusqueda, L_UNO)) {
                tipoBusqueda = "A";
            } else if (Objects.equals(idTipoBusqueda, L_DOS)) {
                tipoBusqueda = "B";
            } else if (Objects.equals(idTipoBusqueda, L_TRES)) {
                tipoBusqueda = "C";
            } else if (Objects.equals(idTipoBusqueda, L_CUATRO)) {
                try {
                    tipoBusqueda = "D";
                    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
                    fecha = formateador.parse(fechaSimple(fechaReg));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                filtro = null;
            } else if (Objects.equals(idTipoBusqueda, L_CINCO)) {
                tipoBusqueda = "E";
            }
            List<Map> listaExp = null;
            //Diferentes búsquedas dependiendo de la bandeja del que se invoca.
            if (isBandejaRev) {
                listaExp = ejbExpedienteFacade.obtenerExpedientes2(fechaIni, fechaFin, tipoBusqueda, (filtro != null ? filtro.trim().toUpperCase() : null), fecha, listProcesos, listIdsUsuTD);
            } else {
                listaExp = ejbExpedienteFacade.obtenerExpedientes1(fechaIni, fechaFin, tipoBusqueda, (filtro != null ? filtro.trim().toUpperCase() : null), fecha, listProcesos, listIdsUsuTD);
            }
            removerExpSegunTipoGuia(listaExp);
            if (idTipoGuiaBus != null && (!JsfUtil.isNullOrEmpty(listaExp))) {
                vaciarListasTipos();
                obtenerExpedientesXTipoGuia(listaExp);
                listaExp.clear();
                switch (idTipoGuiaBus.getCodProg()) {
                    case "TP_GTGAMAC_EXH":
                        listaExp.addAll(listaTipo1);
                        break;
                    case "TP_GTGAMAC_POL":
                        listaExp.addAll(listaTipo2);
                        break;
                    case "TP_GTGAMAC_IMP":
                        listaExp.addAll(listaTipo3);
                        break;
                    case "TP_GTGAMAC_ALM":
                        listaExp.addAll(listaTipo4);
                        break;
                    case "TP_GTGAMAC_DEV":
                        listaExp.addAll(listaTipo5);
                        break;
                    case "TP_GTGAMAC_DON":
                        listaExp.addAll(listaTipo6);
                        break;
                    case "TP_GTGAMAC_AGE":
                        listaExp.addAll(listaTipo7);
                        break;
                    case "TP_GTGAMAC_SUC":
                        listaExp.addAll(listaTipo8);
                        break;
                    case "TP_GTGAMAC_COL":
                        listaExp.addAll(listaTipo9);
                        break;
                    case "TP_GTGAMAC_MUN":
                        listaExp.addAll(listaTipo10);
                        break;
                    case "TP_GTGAMAC_REI":
                        listaExp.addAll(listaTipo11);
                        break;
                    case "TP_GTGAMAC_SAL":
                        listaExp.addAll(listaTipo12);
                        break;
                    case "TP_GTGAMAC_DDV":
                        listaExp.addAll(listaTipo13);
                        break;
                    case "TP_GTGAMAC_REC":
                        listaExp.addAll(listaTipo14);
                        break;
                    case "TP_GTGAMAC_ING":
                        listaExp.addAll(listaTipo15);
                        break;
                    case "TP_GTGAMAC_CFA":
                        listaExp.addAll(listaTipo16);
                        break;
                    default:
                        break;
                }
            }
            if (estadoGuiaBusSelect != null) {
                List<Map> listExpTemp = new ArrayList<>();
                Boolean remover;
                listExpTemp.addAll(listaExp);
                for (Map item : listExpTemp) {
                    remover = Boolean.FALSE;
                    if (item.get("COD_EG") != null) {
                        if (!((String) item.get("COD_EG")).equals(estadoGuiaBusSelect == null ? StringUtil.VACIO : estadoGuiaBusSelect.getCodProg())) {
                            remover = Boolean.TRUE;
                        }
                    } else {
                        remover = Boolean.TRUE;
                    }
                    if (remover) {
                        listaExp.remove(item);
                    }
                }
            }
            if (isCoordinador || (isBandejaRev && isGerente)) {
                if (!JsfUtil.isNullOrEmpty(listExpSinGuia)) {
                    List<Map> listIterable = new ArrayList<>();
                    listIterable.addAll(listaExp);
                    for (String ex : listExpSinGuia) {
                        for (Map item : listIterable) {
                            if (ex.equalsIgnoreCase((String) item.get("NROEXP"))) {
                                listaExp.remove(item);
                                //break;
                            }
                        }
                    }
                }
            }
            if (isEvaluador && idEstadoExp != null) {
                List<Map> listIterable = new ArrayList<>();
                listIterable.addAll(listaExp);
                if (L_UNO.equals(idEstadoExp)) {
                    listaExp.clear();
                    for (String ex : listExpSinGuia) {
                        for (Map item : listIterable) {
                            if (ex.equalsIgnoreCase((String) item.get("NROEXP"))) {
                                listaExp.add(item);
                                break;
                            }
                        }
                    }
                } else if (L_DOS.equals(idEstadoExp)) {
                    for (String ex : listExpSinGuia) {
                        for (Map item : listIterable) {
                            if (ex.equalsIgnoreCase((String) item.get("NROEXP"))) {
                                listaExp.remove(item);
                                break;
                            }
                        }
                    }
                }
            }
            //Bloque para mostrar expedientes a revertir. Dependerá del perfil de usuario logueado.
            if (isBandejaRev) {
                List<Map> listTemp = new ArrayList<>();
                listTemp.addAll(listaExp);
                for (Map item : listTemp) {
                    List<String> listObsTraza = ejbTrazaFacade.obtenerObsTrazasPorExp((BigDecimal) item.get("IDEXP"));
                    Boolean remover = Boolean.FALSE;
                    if (listObsTraza != null) {
                        if (isCoordinador) {
                            remover = Boolean.FALSE;
                            for (String obsTraza : listObsTraza) {
                                String obsTemp = obsTraza == null ? StringUtil.VACIO : obsTraza;
                                if (obsTemp.contains(COOR_REVIERTE) || obsTemp.contains(GER_REVIERTE)) {
                                    remover = Boolean.TRUE;
                                    break;
                                }
                            }
                        }
                        if (isGerente) {
                            remover = Boolean.TRUE;
                            for (String obsTraza : listObsTraza) {
                                String obsTemp = obsTraza == null ? StringUtil.VACIO : obsTraza;
                                if (obsTemp.contains(COOR_REVIERTE) || obsTemp.contains(GER_REVIERTE)) {
                                    remover = Boolean.FALSE;
                                    break;
                                }
                            }
                        }
                    }
                    if (remover) {
                        listaExp.remove(item);
                    }
                }
            }
            //
            setListExpGuias(new ListDataModel(listaExp));
            if (isEvaluador && (estadoGuiaBusSelect != null || idTipoGuiaBus != null) && !listaExp.isEmpty()) {
                JsfUtil.mensajeAdvertencia(JsfUtil.bundle("MensajeSoloExpConGuiaCeada"));
                return;
            }
        } else {
            listExpGuias = new ListDataModel<>(new ArrayList<Map>());
            if (!org) {
                JsfUtil.mensajeAdvertencia(msjValidacion);
            }
            return;
        }
    }

    /**
     * @return the fechaReg
     */
    public Date getFechaReg() {
        return fechaReg;
    }

    /**
     * @param fechaReg the fechaReg to set
     */
    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }

    /**
     * @return the isEvaluador
     */
    public Boolean getIsEvaluador() {
        return isEvaluador;
    }

    /**
     * @param isEvaluador the isEvaluador to set
     */
    public void setIsEvaluador(Boolean isEvaluador) {
        this.isEvaluador = isEvaluador;
    }

    /**
     * @param listTipoGuia the listTipoGuia to set
     */
//    public void setListTipoGuia(List<TipoGamac> listTipoGuia) {
//        this.listTipoGuia = listTipoGuia;
//    }
    /**
     * @return the idTipoGuiaBus
     */
    public TipoGamac getIdTipoGuiaBus() {
        return idTipoGuiaBus;
    }

    /**
     * @param idTipoGuiaBus the idTipoGuiaBus to set
     */
    public void setIdTipoGuiaBus(TipoGamac idTipoGuiaBus) {
        this.idTipoGuiaBus = idTipoGuiaBus;
    }

    /**
     * @return the fechaIni
     */
    public Date getFechaIni() {
        return fechaIni;
    }

    /**
     * @param fechaIni the fechaIni to set
     */
    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    /**
     * @return the fechaFin
     */
    public Date getFechaFin() {
        return fechaFin;
    }

    /**
     * @param fechaFin the fechaFin to set
     */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * @return the estadoGuiaBusSelect
     */
    public TipoBase getEstadoGuiaBusSelect() {
        return estadoGuiaBusSelect;
    }

    /**
     * @param estadoGuiaBusSelect the estadoGuiaBusSelect to set
     */
    public void setEstadoGuiaBusSelect(TipoBase estadoGuiaBusSelect) {
        this.estadoGuiaBusSelect = estadoGuiaBusSelect;
    }

    /**
     * @return the idEstadoExp
     */
    public Long getIdEstadoExp() {
        return idEstadoExp;
    }

    /**
     * @param idEstadoExp the idEstadoExp to set
     */
    public void setIdEstadoExp(Long idEstadoExp) {
        this.idEstadoExp = idEstadoExp;
    }

    public String fechaSimple(Date fecha) {
        return fecha == null ? null : new SimpleDateFormat("dd/MM/yyyy").format(fecha);
    }

    /**
     * @return the isBandejaRev
     */
    public Boolean getIsBandejaRev() {
        return isBandejaRev;
    }

    /**
     * @param isBandejaRev the isBandejaRev to set
     */
    public void setIsBandejaRev(Boolean isBandejaRev) {
        this.isBandejaRev = isBandejaRev;
    }

    /**
     * FUNCIÓN QUE REMUEVE ÍTEMS DE LA LISTA ORIGINAL DE EXPEDIENTES SEGÚN EL
     * PERFIL DEL USUARIO LOGUEADO.
     *
     * @param listExp, List<>. Lista de Expedientes.
     * @author Gino Chávez
     */
    public void removerExpSegunTipoGuia(List<Map> listExp) {
        List<Map> listExpTemp = new ArrayList<>();
        listExpTemp.addAll(listExp);
        listExpSinGuia.clear();
        for (Map item : listExpTemp) {
            //Bloque para llenar la lista de expedientes sin guía
            verificarExisteGuia(item);
            //Fín
            if (item.get("IDGUIA") != null) {
                //Se aprovecha la iteracion de la lista de expedientes para remover los expedientes del cual la guía se encuentre en estados válidos
                //a ser visualizados según perfil del usuario. Esta validación se realizará según el perfil del usuario logueado.
                Boolean remover = Boolean.FALSE;
                if (isCoordinador) {
                    if (getIsBandejaRev()) {
                        if (!"TP_GTGAMAC_FIN".equals((String) item.get("COD_EG"))) {
                            remover = Boolean.TRUE;
                        }
                    } else if ((!"TP_GTGAMAC_APR".equals((String) item.get("COD_EG"))
                            && !"TP_GTGAMAC_OBS".equals((String) item.get("COD_EG")))) {
                        remover = Boolean.TRUE;
                    }
                }
                if (isEvaluador) {
                    if (!"TP_GTGAMAC_CRE".equals((String) item.get("COD_EG"))
                            && !"TP_GTGAMAC_OBS".equals((String) item.get("COD_EG"))
                            && !"TP_GTGAMAC_FIN".equals((String) item.get("COD_EG"))) {
                        remover = Boolean.TRUE;
                    }
                }
                String cadena = amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DIF_DEP_DEV);
                if (remover || !cadena.contains(String.valueOf((BigDecimal) item.get("ID_TG")))) {
                    listExp.remove(item);
                }
            }
        }
    }

    /**
     * @return the listExpSinGuia
     */
    public List<String> getListExpSinGuia() {
        return listExpSinGuia;
    }

    /**
     * @param listExpSinGuia the listExpSinGuia to set
     */
    public void setListExpSinGuia(List<String> listExpSinGuia) {
        this.listExpSinGuia = listExpSinGuia;
    }

    /**
     * FUNCIÓN QUE VACÍA LAS LISTAS DE EXPEDIENTES (LLENADOS SEGÚN EL TIPO DE
     * GUÍA).
     *
     * @author Gino Chávez
     */
    public void vaciarListasTipos() {
        listaTipo1.clear();
        listaTipo2.clear();
        listaTipo3.clear();
        listaTipo4.clear();
        listaTipo5.clear();
        listaTipo6.clear();
        listaTipo7.clear();
        listaTipo8.clear();
        listaTipo9.clear();
        listaTipo10.clear();
        listaTipo11.clear();
        listaTipo12.clear();
        listaTipo13.clear();
        listaTipo16.clear();
    }

    /**
     * FUNCIÓN QUE LLENA LAS LISTAS DE EXPEDIENTES SEGÚN EL TIPO DE GUÍA.
     *
     * @param listaExp,List<>. Lista original.
     * @author Gino Chávez
     */
    public void obtenerExpedientesXTipoGuia(List<Map> listaExp) {
        for (Map itemExp : listaExp) {
            if (itemExp.get("COD_TG") != null) {
                switch ((String) itemExp.get("COD_TG")) {
                    case "TP_GTGAMAC_EXH":
                        listaTipo1.add(itemExp);
                        break;
                    case "TP_GTGAMAC_POL":
                        listaTipo2.add(itemExp);
                        break;
                    case "TP_GTGAMAC_IMP":
                        listaTipo3.add(itemExp);
                        break;
                    case "TP_GTGAMAC_ALM":
                        listaTipo4.add(itemExp);
                        break;
                    case "TP_GTGAMAC_DEV":
                        listaTipo5.add(itemExp);
                        break;
                    case "TP_GTGAMAC_DON":
                        listaTipo6.add(itemExp);
                        break;
                    case "TP_GTGAMAC_AGE":
                        listaTipo7.add(itemExp);
                        break;
                    case "TP_GTGAMAC_SUC":
                        listaTipo8.add(itemExp);
                        break;
                    case "TP_GTGAMAC_COL":
                        listaTipo9.add(itemExp);
                        break;
                    case "TP_GTGAMAC_MUN":
                        listaTipo10.add(itemExp);
                        break;
                    case "TP_GTGAMAC_REI":
                        listaTipo11.add(itemExp);
                        break;
                    case "TP_GTGAMAC_SAL":
                        listaTipo12.add(itemExp);
                        break;
                    case "TP_GTGAMAC_DDV":
                        listaTipo13.add(itemExp);
                        break;
                    case "TP_GTGAMAC_REC":
                        listaTipo14.add(itemExp);
                        break;
                    case "TP_GTGAMAC_ING":
                        listaTipo15.add(itemExp);
                        break;
                    case "TP_GTGAMAC_CFA":
                        listaTipo16.add(itemExp);
                        break;
                    default:
                        TipoGamac tg = ejbTipoGamacFacade.buscarTipoGamacXCodProg((String) itemExp.get("COD_TG"));
                        switch (tg.getTipoId().getCodProg()) {
                            case "TP_GTGAMAC_ALM":
                                listaTipo4.add(itemExp);
                                break;
                            case "TP_GTGAMAC_DEV":
                                listaTipo5.add(itemExp);
                                break;
                            case "TP_GTGAMAC_SUC":
                                listaTipo8.add(itemExp);
                                break;
                            case "TP_GTGAMAC_DON":
                                listaTipo6.add(itemExp);
                                break;
                            default:
                                TipoGamac tg2 = ejbTipoGamacFacade.buscarTipoGamacXCodProg(tg.getCodProg());
                                switch (tg2.getTipoId().getCodProg()) {
                                    case "TP_GTGAMAC_ALM":
                                        listaTipo4.add(itemExp);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                        }
                        break;
                }
            }
        }
    }

    /**
     * @return the isCoordinador
     */
    public Boolean getIsCoordinador() {
        return isCoordinador;
    }

    /**
     * @param isCoordinador the isCoordinador to set
     */
    public void setIsCoordinador(Boolean isCoordinador) {
        this.isCoordinador = isCoordinador;
    }

    /**
     * @return the isGerente
     */
    public Boolean getIsGerente() {
        return isGerente;
    }

    /**
     * @param isGerente the isGerente to set
     */
    public void setIsGerente(Boolean isGerente) {
        this.isGerente = isGerente;
    }

    /**
     * @return the listExpGuias
     */
    public ListDataModel<Map> getListExpGuias() {
        return listExpGuias;
    }

    /**
     * @param listExpGuias the listExpGuias to set
     */
    public void setListExpGuias(ListDataModel<Map> listExpGuias) {
        this.listExpGuias = listExpGuias;
    }

    public void verificarExisteGuia(Map item) {
        if (item.get("IDGUIA") == null) {
            if (!listExpSinGuia.contains((String) item.get("NROEXP"))) {
                listExpSinGuia.add((String) item.get("NROEXP"));
            }
        }
    }

    /**
     * @return the lstAccesorios
     */
    public List<AmaInventarioAccesorios> getLstAccesorios() {
        return lstAccesorios;
    }

    /**
     * @param lstAccesorios the lstAccesorios to set
     */
    public void setLstAccesorios(List<AmaInventarioAccesorios> lstAccesorios) {
        this.lstAccesorios = lstAccesorios;
    }

    public List<AmaInventarioArtconexo> getListArticulos() {
        return listArticulos;
    }

    public void setListArticulos(List<AmaInventarioArtconexo> listArticulos) {
        this.listArticulos = listArticulos;
    }    
}
