package pe.gob.sucamec.bdintegrado.jsf;

import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import pe.gob.sucamec.bdintegrado.data.EppDetalleAutUso;
import pe.gob.sucamec.bdintegrado.data.EppExplosivo;
import pe.gob.sucamec.bdintegrado.data.EppExplosivoSolicitado;
import pe.gob.sucamec.bdintegrado.data.EppLibroMes;
import pe.gob.sucamec.bdintegrado.data.EppLibroUsoDiario;
import pe.gob.sucamec.bdintegrado.data.EppLibro;
import pe.gob.sucamec.bdintegrado.data.EppLibroDetalle;
import pe.gob.sucamec.bdintegrado.data.EppLocal;
import pe.gob.sucamec.bdintegrado.data.EppLugarUso;
import pe.gob.sucamec.bdintegrado.data.EppPlantaExplosivo;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;
import pe.gob.sucamec.bdintegrado.data.EppRegistroGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbParametro;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
import pe.gob.sucamec.bdintegrado.data.Usuario;
import pe.gob.sucamec.bdintegrado.jsf.util.SaldoExplosivo;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;

// Nombre de la instancia en la aplicacion //
@Named("eppLibroController")
@SessionScoped

/**
 * Clase con EppLibroController instanciada como eppLibroController. Contiene
 * funciones utiles para la entidad EppLibro. Esta vinculada a las páginas
 * EppLibro/create.xhtml, EppLibro/update.xhtml, EppLibro/list.xhtml,
 * EppLibro/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec
 * para que funcione adecuadamente, revisar si tiene el campo activo y modificar
 * las búsquedas.
 */
public class EppLibroController implements Serializable {

    @Inject
    WsTramDoc wsTramDocController;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppLibroFacade eppLibroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppRegistroFacade eppRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoExplosivoFacade tipoExplosivoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade tipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppExplosivoFacade eppExplosivoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppPlantaExplosivoFacade eppPlantaExplosivoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppLibroMesFacade eppLibroMesFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt sbPersonaFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppLibroUsoDiarioFacade eppLibroUsoDiarioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt sbUsuarioFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppExplosivoFacade explosivoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppRegistroGuiaTransitoFacade eppRegistroGuiaTransitoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbFeriadoFacade sbFeriadoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppLibroDetalleFacade eppLibroDetalleFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade expedienteFacade;

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade sbParametroFacade;

    private String tipoMenu;
    private EppRegistro registroPlantaUsoComercio;
    private List<EppRegistro> listRegistrosPlantaUsoComercio;
    private String nroResolucion;
    private TipoExplosivoGt tipoLibroFabrica;
    private TipoExplosivoGt tipoFabricacion;
    private boolean disabledFinalidad;
    private boolean disabledButtonRegFabri;
    private EppLibroMes libroMes;
    private List<EppExplosivo> listExplosivos;
    private List<EppExplosivo> listExplosivosInsumos;
    private List<EppPlantaExplosivo> listPlantaExplosivos;
    private EppExplosivo explosivo;
    private EppPlantaExplosivo eppPlantaExplosivo;
    private Double cantidad;
    private Double cantidadEdit;
    private Double cantidadMater;
    private String unidadMedidaExplo;
    private String unidadMedidaMateRela;
    private Long cantMaxProcesar;
    private List<EppLibroUsoDiario> selectExplosivos;
    private List<EppLibroUsoDiario> selectExplosivosDes;
    private List<EppLibroUsoDiario> selectExplosivosReproc;
    private List<EppLibroUsoDiario> selectMaterial;
    private List<EppLibroUsoDiario> selectMaterialRepro;
    private List<EppLibroUsoDiario> listEppLibroMaterialUtilizado;
    private List<EppLibroUsoDiario> listEppLibroExploFabricados;
    private String nroLote;
    private List<EppLibroMes> selectLibroMes;
    private List<TipoExplosivoGt> listTipoExplosivoGtEstados;
    private TipoExplosivoGt tipoEstado;
    private TipoExplosivoGt tipoRegistro;
    private List<TipoExplosivoGt> listTipoExplosivoGtRegistro;
    private List<EppLibro> listResolucionFabrica;
    private EppLibro eppLibro;
    private Date fechaIni;
    private Date fechaFin;
    private TipoExplosivoGt tiposFabricacion;
    private boolean renderTiposFabricacion;
    private TipoBaseGt mes;
    private String procesoPlanta = "'TP_PRTUP_PLA','TP_PRTUP_AFAB'";
    private String procesoAutUso = "'TP_PRTUP_GLO','TP_PRTUP_EVE','TP_PRTUP_EXC','TP_PRTUP_AAFOR','TP_PRTUP_AAHID','TP_PRTUP_AAPOR'";
    private String procesoComercio = "'TP_PRTUP_ACOM','TP_PRTUP_COM'";
    private String procesoGeneral = "'TP_PRTUP_GLO','TP_PRTUP_EVE','TP_PRTUP_EXC','TP_PRTUP_AAFOR','TP_PRTUP_AAHID','TP_PRTUP_AAPOR','TP_PRTUP_PLA','TP_PRTUP_AFAB','TP_PRTUP_ACOM','TP_PRTUP_COM'";
    private List<EppLibroUsoDiario> listaUpdateActivos = new ArrayList<>();
    private List<EppLibroDetalle> listaUpdateActivosEppdetaDetalle = new ArrayList<>();
    private List<EppLibroDetalle> listaUpdateActivosEppdetaDetalleIdMenorcero = new ArrayList<>();
    private String estados = "'TP_REGEV_CRE','TP_REGEV_ENV','TP_REGEV_OBS','TP_REGEV_APRO','TP_REGEV_PEN'";
    private Calendar fechaInicialCalendar;
    private Calendar fechaFinalCalendar;
    private Calendar fechaRealCalendar = Calendar.getInstance();

    private String headerDlgConfirmacion = "Registros de Fabricación";
    private String valorTexto;
    private String headerCabecera;
    private String headerDetalle;
    private boolean renderTableDetalle;
    private boolean renderTableInsuRepro;
    private boolean renderPanGridMaterial;
    private String[] aniosBusqueda = {"2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
    private List<String> listAnios;
    private String anio;
    Double cantidadTotal = 0.0;
    private String dirLocal;
    private long diaUsoFabExpl;
    private long diaUsoFabMat;
    private String dirFabrica;

    // USO
    private String tipoLugarUso;
    private TipoExplosivoGt tipoUso;
    private List<EppRegistroGuiaTransito> listGuiaTransito;
    private EppRegistroGuiaTransito eppGuiaTransito;
    private boolean disabledGuiaTransito = true;
    private List<EppExplosivoSolicitado> listExplosivoSolicitados;
    private EppExplosivoSolicitado explosivoSolicitado;
    private Double saldo;
    private double saldoIngreso = 0.0;
    private double saldoBaseDatos = 0.0;
    private double saldoUso = 0.0;
    private double saldoReingreso = 0.0;
    private double saldoDevolucion = 0.0;
    private List<EppLibro> listResolucionUso;
    private List<SaldoExplosivo> listSaldoExplosivos;
    private EppDetalleAutUso eppDetalleAutUso;
    private List<EppDetalleAutUso> listEppDetalleAutUso;
    private List<Long> listaDiasMes = new ArrayList<>();
    private List<Long> listaDiasMesEd = new ArrayList<>();
    private long diaUsoDiario;
    private List<EppExplosivo> listExploTemporal = new ArrayList<>();
    private boolean disabledAgregarUso = true;
    private String data = null;
    private boolean disabledPanExplRela; // disabled componentes despues de elegir uso
    private String valorTextoBorrarUso;
    private List<EppExplosivoSolicitado> listExploFabricadosBorrarUsoDialog = new ArrayList<>(); //  los explosivos a borrar de la guia en el Dialog
    private EppLibroUsoDiario usoDiarioDialog;
    private boolean disabledCabeceraEdit;
    private boolean disabledCantida; // para cantidad al editar un explo tipo ingreso
    private boolean renderPanelUso;
    private String labelLugaUso;
    private boolean renderColunna;
    private List<Map> listObservaciones;
    private boolean UniMedidaBol;
    private boolean saldoBol;
    private double saldoEdit;
    private boolean verMensDlg;
    private boolean verMensDlgFab;
    private String textoListVacia;
    private Long idGuardado;
    private boolean corregido;

    // FABRICA
    private List<EppLibroUsoDiario> listEppLibroMaterialUtilizadoExploRepro;
    private List<EppLibroUsoDiario> listEppLibroMaterialUtilizadoFa;
    private List<EppLibroUsoDiario> listEppLibroExploFabricadosFa;
    private List<EppLibroUsoDiario> listEppLibroExploFabricadosFaDestruccion;
    private List<EppLibroUsoDiario> listEppLibroExploFabricadosFaReproce;
    private EppLibroMes libroMesFabrica;
    private List<EppLibroUsoDiario> listaUpdateActivosFabrica = new ArrayList<>();
    private long diaFaDiarioEx;
    private long diaFaDiarioIns;
    private List<Long> listaFasExMesList = new ArrayList<>();
    private List<Long> listaFasInsMesList = new ArrayList<>();
    private boolean renderFinaDetalle;
    private TipoExplosivoGt tipoFinalidad;
    private TipoExplosivoGt tipoDetalle;
    private boolean rennderTableDestr;
    private boolean rennderTableReproc;
    private int contador;
    private String datas;
    private boolean renderPanelExplFabr;
    private boolean requiAnio;
    private boolean visiPaneMat = true;

    // COMERCIO
    private String numeroResFabri;
    private String numeroResComercio;
    private String direccionFabrica;
    private String comprador;
    private String filtroComprador;
    private EppRegistro registroComprador;
    private EppPlantaExplosivo epplantaExplosivo;
    private TipoExplosivoGt tipoDestinatario;
    // private List<EppLibroUsoDiario> listUsoDiario;
    private List<EppExplosivo> cboListExplosivo;
    private EppExplosivo cboExplosivo;
    private EppLibroUsoDiario eppLibroUsoDiario;
    private List<EppLibroDetalle> listEppLibroDetalle;//selectLibrodetalle
    private List<EppLibroDetalle> selectLibrodetalle;
    private String destino;
    private List<EppLibroUsoDiario> listEppLibroExploFabricadosCo;
    private EppLibroUsoDiario libroUsoDiario;
    private EppLibroMes libroMesComercio;
    private List<EppLibroUsoDiario> listaUpdateActivosCom = new ArrayList<>();
    private TipoBaseGt mesIdComercio;
    private long diaVenDiarioEx;
    private List<Long> listaVenExMesList = new ArrayList<>();
    private int contadorVe;

    public boolean isCorregido() {
        return corregido;
    }

    public void setCorregido(boolean corregido) {
        this.corregido = corregido;
    }

    public List<EppExplosivo> getListExplosivosInsumos() {
        return listExplosivosInsumos;
    }

    public void setListExplosivosInsumos(List<EppExplosivo> listExplosivosInsumos) {
        this.listExplosivosInsumos = listExplosivosInsumos;
    }

    public EppLibroUsoDiario getLibroUsoDiario() {
        return libroUsoDiario;
    }

    public void setLibroUsoDiario(EppLibroUsoDiario libroUsoDiario) {
        this.libroUsoDiario = libroUsoDiario;
    }

    public boolean isVisiPaneMat() {
        return visiPaneMat;
    }

    public void setVisiPaneMat(boolean visiPaneMat) {
        this.visiPaneMat = visiPaneMat;
    }

    public boolean isRequiAnio() {
        return requiAnio;
    }

    public void setRequiAnio(boolean requiAnio) {
        this.requiAnio = requiAnio;
    }

    public int getContadorVe() {
        return contadorVe;
    }

    public void setContadorVe(int contadorVe) {
        this.contadorVe = contadorVe;
    }

    public List<Long> getListaVenExMesList() {
        return listaVenExMesList;
    }

    public void setListaVenExMesList(List<Long> listaVenExMesList) {
        this.listaVenExMesList = listaVenExMesList;
    }

    public long getDiaVenDiarioEx() {
        return diaVenDiarioEx;
    }

    public void setDiaVenDiarioEx(long diaVenDiarioEx) {
        this.diaVenDiarioEx = diaVenDiarioEx;
    }

    public boolean isRenderPanelExplFabr() {
        return renderPanelExplFabr;
    }

    public void setRenderPanelExplFabr(boolean renderPanelExplFabr) {
        this.renderPanelExplFabr = renderPanelExplFabr;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public boolean isVerMensDlgFab() {
        return verMensDlgFab;
    }

    public void setVerMensDlgFab(boolean verMensDlgFab) {
        this.verMensDlgFab = verMensDlgFab;
    }

    public List<EppLibroUsoDiario> getSelectMaterialRepro() {
        return selectMaterialRepro;
    }

    public void setSelectMaterialRepro(List<EppLibroUsoDiario> selectMaterialRepro) {
        this.selectMaterialRepro = selectMaterialRepro;
    }

    public boolean isRenderTableInsuRepro() {
        return renderTableInsuRepro;
    }

    public void setRenderTableInsuRepro(boolean renderTableInsuRepro) {
        this.renderTableInsuRepro = renderTableInsuRepro;
    }

    public List<EppLibroUsoDiario> getListEppLibroMaterialUtilizadoExploRepro() {
        return listEppLibroMaterialUtilizadoExploRepro;
    }

    public void setListEppLibroMaterialUtilizadoExploRepro(List<EppLibroUsoDiario> listEppLibroMaterialUtilizadoExploRepro) {
        this.listEppLibroMaterialUtilizadoExploRepro = listEppLibroMaterialUtilizadoExploRepro;
    }

    public List<EppLibroUsoDiario> getSelectExplosivosReproc() {
        return selectExplosivosReproc;
    }

    public void setSelectExplosivosReproc(List<EppLibroUsoDiario> selectExplosivosReproc) {
        this.selectExplosivosReproc = selectExplosivosReproc;
    }

    public List<EppLibroUsoDiario> getListEppLibroExploFabricadosFaReproce() {
        return listEppLibroExploFabricadosFaReproce;
    }

    public boolean isRennderTableReproc() {
        return rennderTableReproc;
    }

    public void setRennderTableReproc(boolean rennderTableReproc) {
        this.rennderTableReproc = rennderTableReproc;
    }

    public void setListEppLibroExploFabricadosFaReproce(List<EppLibroUsoDiario> listEppLibroExploFabricadosFaReproce) {
        this.listEppLibroExploFabricadosFaReproce = listEppLibroExploFabricadosFaReproce;
    }

    public boolean isRenderPanGridMaterial() {
        return renderPanGridMaterial;
    }

    public void setRenderPanGridMaterial(boolean renderPanGridMaterial) {
        this.renderPanGridMaterial = renderPanGridMaterial;
    }

    public boolean isRennderTableDestr() {
        return rennderTableDestr;
    }

    public void setRennderTableDestr(boolean rennderTableDestr) {
        this.rennderTableDestr = rennderTableDestr;
    }

    public List<EppLibroUsoDiario> getSelectExplosivosDes() {
        return selectExplosivosDes;
    }

    public void setSelectExplosivosDes(List<EppLibroUsoDiario> selectExplosivosDes) {
        this.selectExplosivosDes = selectExplosivosDes;
    }

    public List<EppLibroUsoDiario> getListEppLibroExploFabricadosFaDestruccion() {
        return listEppLibroExploFabricadosFaDestruccion;
    }

    public void setListEppLibroExploFabricadosFaDestruccion(List<EppLibroUsoDiario> listEppLibroExploFabricadosFaDestruccion) {
        this.listEppLibroExploFabricadosFaDestruccion = listEppLibroExploFabricadosFaDestruccion;
    }

    public TipoExplosivoGt getTipoFinalidad() {
        return tipoFinalidad;
    }

    public void setTipoFinalidad(TipoExplosivoGt tipoFinalidad) {
        this.tipoFinalidad = tipoFinalidad;
    }

    public TipoExplosivoGt getTipoDetalle() {
        return tipoDetalle;
    }

    public void setTipoDetalle(TipoExplosivoGt tipoDetalle) {
        this.tipoDetalle = tipoDetalle;
    }

    public boolean isRenderFinaDetalle() {
        return renderFinaDetalle;
    }

    public void setRenderFinaDetalle(boolean renderFinaDetalle) {
        this.renderFinaDetalle = renderFinaDetalle;
    }

    public List<EppLibroUsoDiario> getListaUpdateActivosCom() {
        return listaUpdateActivosCom;
    }

    public void setListaUpdateActivosCom(List<EppLibroUsoDiario> listaUpdateActivosCom) {
        this.listaUpdateActivosCom = listaUpdateActivosCom;
    }

    public EppLibroMes getLibroMesComercio() {
        return libroMesComercio;
    }

    public void setLibroMesComercio(EppLibroMes libroMesComercio) {
        this.libroMesComercio = libroMesComercio;
    }

    public List<EppLibroUsoDiario> getListEppLibroExploFabricadosCo() {
        return listEppLibroExploFabricadosCo;
    }

    public void setListEppLibroExploFabricadosCo(List<EppLibroUsoDiario> listEppLibroExploFabricadosCo) {
        this.listEppLibroExploFabricadosCo = listEppLibroExploFabricadosCo;
    }

    public List<Long> getListaFasExMesList() {
        return listaFasExMesList;
    }

    public void setListaFasExMesList(List<Long> listaFasExMesList) {
        this.listaFasExMesList = listaFasExMesList;
    }

    public List<Long> getListaFasInsMesList() {
        return listaFasInsMesList;
    }

    public void setListaFasInsMesList(List<Long> listaFasInsMesList) {
        this.listaFasInsMesList = listaFasInsMesList;
    }

    public long getDiaFaDiarioEx() {
        return diaFaDiarioEx;
    }

    public void setDiaFaDiarioEx(long diaFaDiarioEx) {
        this.diaFaDiarioEx = diaFaDiarioEx;
    }

    public long getDiaFaDiarioIns() {
        return diaFaDiarioIns;
    }

    public void setDiaFaDiarioIns(long diaFaDiarioIns) {
        this.diaFaDiarioIns = diaFaDiarioIns;
    }

    public List<EppLibroUsoDiario> getListaUpdateActivosFabrica() {
        return listaUpdateActivosFabrica;
    }

    public void setListaUpdateActivosFabrica(List<EppLibroUsoDiario> listaUpdateActivosFabrica) {
        this.listaUpdateActivosFabrica = listaUpdateActivosFabrica;
    }

    public EppLibroMes getLibroMesFabrica() {
        return libroMesFabrica;
    }

    public void setLibroMesFabrica(EppLibroMes libroMesFabrica) {
        this.libroMesFabrica = libroMesFabrica;
    }

    public Long getIdGuardado() {
        return idGuardado;
    }

    public void setIdGuardado(Long idGuardado) {
        this.idGuardado = idGuardado;
    }

    public List<EppLibroUsoDiario> getListEppLibroMaterialUtilizadoFa() {
        return listEppLibroMaterialUtilizadoFa;
    }

    public void setListEppLibroMaterialUtilizadoFa(List<EppLibroUsoDiario> listEppLibroMaterialUtilizadoFa) {
        this.listEppLibroMaterialUtilizadoFa = listEppLibroMaterialUtilizadoFa;
    }

    public List<EppLibroUsoDiario> getListEppLibroExploFabricadosFa() {
        return listEppLibroExploFabricadosFa;
    }

    public void setListEppLibroExploFabricadosFa(List<EppLibroUsoDiario> listEppLibroExploFabricadosFa) {
        this.listEppLibroExploFabricadosFa = listEppLibroExploFabricadosFa;
    }

    public String getTextoListVacia() {
        return textoListVacia;
    }

    public void setTextoListVacia(String textoListVacia) {
        this.textoListVacia = textoListVacia;
    }

    public boolean isVerMensDlg() {
        return verMensDlg;
    }

    public void setVerMensDlg(boolean verMensDlg) {
        this.verMensDlg = verMensDlg;
    }

    public List<Long> getListaDiasMesEd() {
        return listaDiasMesEd;
    }

    public void setListaDiasMesEd(List<Long> listaDiasMesEd) {
        this.listaDiasMesEd = listaDiasMesEd;
    }

    public double getSaldoEdit() {
        return saldoEdit;
    }

    public void setSaldoEdit(double saldoEdit) {
        this.saldoEdit = saldoEdit;
    }

    public double getSaldoBaseDatos() {
        return saldoBaseDatos;
    }

    public void setSaldoBaseDatos(double saldoBaseDatos) {
        this.saldoBaseDatos = saldoBaseDatos;
    }

    public boolean isUniMedidaBol() {
        return UniMedidaBol;
    }

    public void setUniMedidaBol(boolean UniMedidaBol) {
        this.UniMedidaBol = UniMedidaBol;
    }

    public boolean isSaldoBol() {
        return saldoBol;
    }

    public void setSaldoBol(boolean saldoBol) {
        this.saldoBol = saldoBol;
    }

    public List<Map> getListObservaciones() {
        return listObservaciones;
    }

    public void setListObservaciones(List<Map> listObservaciones) {
        this.listObservaciones = listObservaciones;
    }

    public String getDirFabrica() {
        return dirFabrica;
    }

    public void setDirFabrica(String dirFabrica) {
        this.dirFabrica = dirFabrica;
    }

    public long getDiaUsoFabExpl() {
        return diaUsoFabExpl;
    }

    public void setDiaUsoFabExpl(long diaUsoFabExpl) {
        this.diaUsoFabExpl = diaUsoFabExpl;
    }

    public long getDiaUsoFabMat() {
        return diaUsoFabMat;
    }

    public void setDiaUsoFabMat(long diaUsoFabMat) {
        this.diaUsoFabMat = diaUsoFabMat;
    }

    public boolean isRenderColunna() {
        return renderColunna;
    }

    public void setRenderColunna(boolean renderColunna) {
        this.renderColunna = renderColunna;
    }

    public String getDirLocal() {
        return dirLocal;
    }

    public void setDirLocal(String dirLocal) {
        this.dirLocal = dirLocal;
    }

    public String getLabelLugaUso() {
        return labelLugaUso;
    }

    public void setLabelLugaUso(String labelLugaUso) {
        this.labelLugaUso = labelLugaUso;
    }

    public boolean isRenderPanelUso() {
        return renderPanelUso;
    }

    public void setRenderPanelUso(boolean renderPanelUso) {
        this.renderPanelUso = renderPanelUso;
    }

    public Double getCantidadEdit() {
        return cantidadEdit;
    }

    public void setCantidadEdit(Double cantidadEdit) {
        this.cantidadEdit = cantidadEdit;
    }

    public boolean isDisabledCantida() {
        return disabledCantida;
    }

    public void setDisabledCantida(boolean disabledCantida) {
        this.disabledCantida = disabledCantida;
    }

    public boolean isDisabledCabeceraEdit() {
        return disabledCabeceraEdit;
    }

    public void setDisabledCabeceraEdit(boolean disabledCabeceraEdit) {
        this.disabledCabeceraEdit = disabledCabeceraEdit;
    }

    public EppLibroUsoDiario getUsoDiarioDialog() {
        return usoDiarioDialog;
    }

    public void setUsoDiarioDialog(EppLibroUsoDiario usoDiarioDialog) {
        this.usoDiarioDialog = usoDiarioDialog;
    }

    public List<EppExplosivoSolicitado> getListExploFabricadosBorrarUsoDialog() {
        return listExploFabricadosBorrarUsoDialog;
    }

    public void setListExploFabricadosBorrarUsoDialog(List<EppExplosivoSolicitado> listExploFabricadosBorrarUsoDialog) {
        this.listExploFabricadosBorrarUsoDialog = listExploFabricadosBorrarUsoDialog;
    }

    public String getValorTextoBorrarUso() {
        return valorTextoBorrarUso;
    }

    public void setValorTextoBorrarUso(String valorTextoBorrarUso) {
        this.valorTextoBorrarUso = valorTextoBorrarUso;
    }

    public boolean isDisabledPanExplRela() {
        return disabledPanExplRela;
    }

    public void setDisabledPanExplRela(boolean disabledPanExplRela) {
        this.disabledPanExplRela = disabledPanExplRela;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isDisabledAgregarUso() {
        return disabledAgregarUso;
    }

    public void setDisabledAgregarUso(boolean disabledAgregarUso) {
        this.disabledAgregarUso = disabledAgregarUso;
    }

    public List<EppExplosivo> getListExploTemporal() {
        return listExploTemporal;
    }

    public void setListExploTemporal(List<EppExplosivo> listExploTemporal) {
        this.listExploTemporal = listExploTemporal;
    }

    public long getDiaUsoDiario() {
        return diaUsoDiario;
    }

    public void setDiaUsoDiario(long diaUsoDiario) {
        this.diaUsoDiario = diaUsoDiario;
    }

    public List<Long> getListaDiasMes() {
        return listaDiasMes;
    }

    public void setListaDiasMes(List<Long> listaDiasMes) {
        this.listaDiasMes = listaDiasMes;
    }

    public Calendar getFechaRealCalendar() {
        return fechaRealCalendar;
    }

    public void setFechaRealCalendar(Calendar fechaRealCalendar) {
        this.fechaRealCalendar = fechaRealCalendar;
    }

    public Calendar getFechaInicialCalendar() {
        return fechaInicialCalendar;
    }

    public void setFechaInicialCalendar(Calendar fechaInicialCalendar) {
        this.fechaInicialCalendar = fechaInicialCalendar;
    }

    public Calendar getFechaFinalCalendar() {
        return fechaFinalCalendar;
    }

    public void setFechaFinalCalendar(Calendar fechaFinalCalendar) {
        this.fechaFinalCalendar = fechaFinalCalendar;
    }

    public EppDetalleAutUso getEppDetalleAutUso() {
        return eppDetalleAutUso;
    }

    public void setEppDetalleAutUso(EppDetalleAutUso eppDetalleAutUso) {
        this.eppDetalleAutUso = eppDetalleAutUso;
    }

    public List<EppDetalleAutUso> getListEppDetalleAutUso() {
        return listEppDetalleAutUso;
    }

    public void setListEppDetalleAutUso(List<EppDetalleAutUso> listEppDetalleAutUso) {
        this.listEppDetalleAutUso = listEppDetalleAutUso;
    }

    public String getEstados() {
        return estados;
    }

    public void setEstados(String estados) {
        this.estados = estados;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public List<EppLibroDetalle> getListaUpdateActivosEppdetaDetalleIdMenorcero() {
        return listaUpdateActivosEppdetaDetalleIdMenorcero;
    }

    public void setListaUpdateActivosEppdetaDetalleIdMenorcero(List<EppLibroDetalle> listaUpdateActivosEppdetaDetalleIdMenorcero) {
        this.listaUpdateActivosEppdetaDetalleIdMenorcero = listaUpdateActivosEppdetaDetalleIdMenorcero;
    }

    public List<EppLibroDetalle> getListaUpdateActivosEppdetaDetalle() {
        return listaUpdateActivosEppdetaDetalle;
    }

    public void setListaUpdateActivosEppdetaDetalle(List<EppLibroDetalle> listaUpdateActivosEppdetaDetalle) {
        this.listaUpdateActivosEppdetaDetalle = listaUpdateActivosEppdetaDetalle;
    }

    public Double getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(Double cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public EppExplosivo getCboExplosivo() {
        return cboExplosivo;
    }

    public void setCboExplosivo(EppExplosivo cboExplosivo) {
        this.cboExplosivo = cboExplosivo;
    }

    public List<EppExplosivo> getCboListExplosivo() {
        return cboListExplosivo;
    }

    public void setCboListExplosivo(List<EppExplosivo> cboListExplosivo) {
        this.cboListExplosivo = cboListExplosivo;
    }

    public List<EppLibroDetalle> getSelectLibrodetalle() {
        return selectLibrodetalle;
    }

    public void setSelectLibrodetalle(List<EppLibroDetalle> selectLibrodetalle) {
        this.selectLibrodetalle = selectLibrodetalle;
    }

    public List<EppLibroDetalle> getListEppLibroDetalle() {
        return listEppLibroDetalle;
    }

    public void setListEppLibroDetalle(List<EppLibroDetalle> listEppLibroDetalle) {
        this.listEppLibroDetalle = listEppLibroDetalle;
    }

    public EppLibroUsoDiario getEppLibroUsoDiario() {
        return eppLibroUsoDiario;
    }

    public void setEppLibroUsoDiario(EppLibroUsoDiario eppLibroUsoDiario) {
        this.eppLibroUsoDiario = eppLibroUsoDiario;
    }

//    public List<EppLibroUsoDiario> getListUsoDiario() {
//        return listUsoDiario;
//    }
//
//    public void setListUsoDiario(List<EppLibroUsoDiario> listUsoDiario) {
//        this.listUsoDiario = listUsoDiario;
//    }
    public TipoExplosivoGt getTipoDestinatario() {
        return tipoDestinatario;
    }

    public void setTipoDestinatario(TipoExplosivoGt tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    public EppPlantaExplosivo getEpplantaExplosivo() {
        return epplantaExplosivo;
    }

    public void setEpplantaExplosivo(EppPlantaExplosivo epplantaExplosivo) {
        this.epplantaExplosivo = epplantaExplosivo;
    }

    public EppRegistro getRegistroComprador() {
        return registroComprador;
    }

    public void setRegistroComprador(EppRegistro registroComprador) {
        this.registroComprador = registroComprador;
    }

    public String getFiltroComprador() {
        return filtroComprador;
    }

    public void setFiltroComprador(String filtroComprador) {
        this.filtroComprador = filtroComprador;
    }

    public String getProcesoGeneral() {
        return procesoGeneral;
    }

    public void setProcesoGeneral(String procesoGeneral) {
        this.procesoGeneral = procesoGeneral;
    }

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public String getNumeroResFabri() {
        return numeroResFabri;
    }

    public void setNumeroResFabri(String numeroResFabri) {
        this.numeroResFabri = numeroResFabri;
    }

    public String getNumeroResComercio() {
        return numeroResComercio;
    }

    public void setNumeroResComercio(String numeroResComercio) {
        this.numeroResComercio = numeroResComercio;
    }

    public String getDireccionFabrica() {
        return direccionFabrica;
    }

    public void setDireccionFabrica(String direccionFabrica) {
        this.direccionFabrica = direccionFabrica;
    }

    public String getProcesoComercio() {
        return procesoComercio;
    }

    public void setProcesoComercio(String procesoComercio) {
        this.procesoComercio = procesoComercio;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public List<String> getListAnios() {
        return listAnios;
    }

    public void setListAnios(List<String> listAnios) {
        this.listAnios = listAnios;
    }

    public String[] getAniosBusqueda() {
        return aniosBusqueda;
    }

    public void setAniosBusqueda(String[] aniosBusqueda) {
        this.aniosBusqueda = aniosBusqueda;
    }

    public List<SaldoExplosivo> getListSaldoExplosivos() {
        return listSaldoExplosivos;
    }

    public void setListSaldoExplosivos(List<SaldoExplosivo> listSaldoExplosivos) {
        this.listSaldoExplosivos = listSaldoExplosivos;
    }

    public double getSaldoIngreso() {
        return saldoIngreso;
    }

    public void setSaldoIngreso(double saldoIngreso) {
        this.saldoIngreso = saldoIngreso;
    }

    public double getSaldoUso() {
        return saldoUso;
    }

    public void setSaldoUso(double saldoUso) {
        this.saldoUso = saldoUso;
    }

    public double getSaldoReingreso() {
        return saldoReingreso;
    }

    public void setSaldoReingreso(double saldoReingreso) {
        this.saldoReingreso = saldoReingreso;
    }

    public double getSaldoDevolucion() {
        return saldoDevolucion;
    }

    public void setSaldoDevolucion(double saldoDevolucion) {
        this.saldoDevolucion = saldoDevolucion;
    }

    public void setSaldoDevolucion(Double saldoDevolucion) {
        this.saldoDevolucion = saldoDevolucion;
    }

    public String getTipoMenu() {
        return tipoMenu;
    }

    public void setTipoMenu(String tipoMenu) {
        this.tipoMenu = tipoMenu;
    }

    public List<EppLibro> getListResolucionUso() {
        return listResolucionUso;
    }

    public void setListResolucionUso(List<EppLibro> listResolucionUso) {
        this.listResolucionUso = listResolucionUso;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public EppExplosivoSolicitado getExplosivoSolicitado() {
        return explosivoSolicitado;
    }

    public void setExplosivoSolicitado(EppExplosivoSolicitado explosivoSolicitado) {
        this.explosivoSolicitado = explosivoSolicitado;
    }

    public List<EppExplosivoSolicitado> getListExplosivoSolicitados() {
        return listExplosivoSolicitados;
    }

    public void setListExplosivoSolicitados(List<EppExplosivoSolicitado> listExplosivoSolicitados) {
        this.listExplosivoSolicitados = listExplosivoSolicitados;
    }

    public boolean isDisabledGuiaTransito() {
        return disabledGuiaTransito;
    }

    public void setDisabledGuiaTransito(boolean disabledGuiaTransito) {
        this.disabledGuiaTransito = disabledGuiaTransito;
    }

    public List<EppRegistroGuiaTransito> getListGuiaTransito() {
        return listGuiaTransito;
    }

    public void setListGuiaTransito(List<EppRegistroGuiaTransito> listGuiaTransito) {
        this.listGuiaTransito = listGuiaTransito;
    }

    public EppRegistroGuiaTransito getEppGuiaTransito() {
        return eppGuiaTransito;
    }

    public void setEppGuiaTransito(EppRegistroGuiaTransito eppGuiaTransito) {
        this.eppGuiaTransito = eppGuiaTransito;
    }

    public TipoExplosivoGt getTipoUso() {
        return tipoUso;
    }

    public void setTipoUso(TipoExplosivoGt tipoUso) {
        this.tipoUso = tipoUso;
    }

    public String getTipoLugarUso() {
        return tipoLugarUso;
    }

    public void setTipoLugarUso(String tipoLugarUso) {
        this.tipoLugarUso = tipoLugarUso;
    }

    public String getProcesoAutUso() {
        return procesoAutUso;
    }

    public void setProcesoAutUso(String procesoAutUso) {
        this.procesoAutUso = procesoAutUso;
    }

    public boolean isRenderTableDetalle() {
        return renderTableDetalle;
    }

    public void setRenderTableDetalle(boolean renderTableDetalle) {
        this.renderTableDetalle = renderTableDetalle;
    }

    public String getHeaderCabecera() {
        return headerCabecera;
    }

    public void setHeaderCabecera(String headerCabecera) {
        this.headerCabecera = headerCabecera;
    }

    public String getHeaderDetalle() {
        return headerDetalle;
    }

    public void setHeaderDetalle(String headerDetalle) {
        this.headerDetalle = headerDetalle;
    }

    public String getValorTexto() {
        return valorTexto;
    }

    public void setValorTexto(String valorTexto) {
        this.valorTexto = valorTexto;
    }

    public String getHeaderDlgConfirmacion() {
        return headerDlgConfirmacion;
    }

    public void setHeaderDlgConfirmacion(String headerDlgConfirmacion) {
        this.headerDlgConfirmacion = headerDlgConfirmacion;
    }

    public List<EppLibroUsoDiario> getListaUpdateActivos() {
        return listaUpdateActivos;
    }

    public void setListaUpdateActivos(List<EppLibroUsoDiario> listaUpdateActivos) {
        this.listaUpdateActivos = listaUpdateActivos;
    }

    public String getProcesoPlanta() {
        return procesoPlanta;
    }

    public void setProcesoPlanta(String procesoPlanta) {
        this.procesoPlanta = procesoPlanta;
    }

    public TipoBaseGt getMes() {
        return mes;
    }

    public void setMes(TipoBaseGt mes) {
        this.mes = mes;
    }

    public boolean isRenderTiposFabricacion() {
        return renderTiposFabricacion;
    }

    public void setRenderTiposFabricacion(boolean renderTiposFabricacion) {
        this.renderTiposFabricacion = renderTiposFabricacion;
    }

    public TipoExplosivoGt getTiposFabricacion() {
        return tiposFabricacion;
    }

    public void setTiposFabricacion(TipoExplosivoGt tiposFabricacion) {
        this.tiposFabricacion = tiposFabricacion;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EppLibro getEppLibro() {
        return eppLibro;
    }

    public void setEppLibro(EppLibro eppLibro) {
        this.eppLibro = eppLibro;
    }

    public List<EppLibro> getListResolucionFabrica() {
        return listResolucionFabrica;
    }

    public void setListResolucionFabrica(List<EppLibro> listResolucionFabrica) {
        this.listResolucionFabrica = listResolucionFabrica;
    }

    public List<TipoExplosivoGt> getListTipoExplosivoGtRegistro() {
        return listTipoExplosivoGtRegistro;
    }

    public void setListTipoExplosivoGtRegistro(List<TipoExplosivoGt> listTipoExplosivoGtRegistro) {
        this.listTipoExplosivoGtRegistro = listTipoExplosivoGtRegistro;
    }

    public TipoExplosivoGt getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(TipoExplosivoGt tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public TipoExplosivoGt getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(TipoExplosivoGt tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public List<TipoExplosivoGt> getListTipoExplosivoGtEstados() {
        return listTipoExplosivoGtEstados;
    }

    public void setListTipoExplosivoGtEstados(List<TipoExplosivoGt> listTipoExplosivoGtEstados) {
        this.listTipoExplosivoGtEstados = listTipoExplosivoGtEstados;
    }

    public List<EppLibroMes> getSelectLibroMes() {
        return selectLibroMes;
    }

    public void setSelectLibroMes(List<EppLibroMes> selectLibroMes) {
        this.selectLibroMes = selectLibroMes;
    }

    public Double getCantidadMater() {
        return cantidadMater;
    }

    public void setCantidadMater(Double cantidadMater) {
        this.cantidadMater = cantidadMater;
    }

    public String getNroLote() {
        return nroLote;
    }

    public void setNroLote(String nroLote) {
        this.nroLote = nroLote;
    }

    public List<EppLibroUsoDiario> getListEppLibroMaterialUtilizado() {
        return listEppLibroMaterialUtilizado;
    }

    public void setListEppLibroMaterialUtilizado(List<EppLibroUsoDiario> listEppLibroMaterialUtilizado) {
        this.listEppLibroMaterialUtilizado = listEppLibroMaterialUtilizado;
    }

    public List<EppLibroUsoDiario> getListEppLibroExploFabricados() {
        return listEppLibroExploFabricados;
    }

    public void setListEppLibroExploFabricados(List<EppLibroUsoDiario> listEppLibroExploFabricados) {
        this.listEppLibroExploFabricados = listEppLibroExploFabricados;
    }

    public String getUnidadMedidaMateRela() {
        return unidadMedidaMateRela;
    }

    public void setUnidadMedidaMateRela(String unidadMedidaMateRela) {
        this.unidadMedidaMateRela = unidadMedidaMateRela;
    }

    public EppPlantaExplosivo getEppPlantaExplosivo() {
        return eppPlantaExplosivo;
    }

    public void setEppPlantaExplosivo(EppPlantaExplosivo eppPlantaExplosivo) {
        this.eppPlantaExplosivo = eppPlantaExplosivo;
    }

    public List<EppPlantaExplosivo> getListPlantaExplosivos() {
        return listPlantaExplosivos;
    }

    public void setListPlantaExplosivos(List<EppPlantaExplosivo> listPlantaExplosivos) {
        this.listPlantaExplosivos = listPlantaExplosivos;
    }

    public List<EppLibroUsoDiario> getSelectExplosivos() {
        return selectExplosivos;
    }

    public void setSelectExplosivos(List<EppLibroUsoDiario> selectExplosivos) {
        this.selectExplosivos = selectExplosivos;
    }

    public List<EppLibroUsoDiario> getSelectMaterial() {
        return selectMaterial;
    }

    public void setSelectMaterial(List<EppLibroUsoDiario> selectMaterial) {
        this.selectMaterial = selectMaterial;
    }

    public Long getCantMaxProcesar() {
        return cantMaxProcesar;
    }

    public void setCantMaxProcesar(Long cantMaxProcesar) {
        this.cantMaxProcesar = cantMaxProcesar;
    }

    public String getUnidadMedidaExplo() {
        return unidadMedidaExplo;
    }

    public void setUnidadMedidaExplo(String unidadMedidaExplo) {
        this.unidadMedidaExplo = unidadMedidaExplo;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public EppExplosivo getExplosivo() {
        return explosivo;
    }

    public void setExplosivo(EppExplosivo explosivo) {
        this.explosivo = explosivo;
    }

    public List<EppExplosivo> getListExplosivos() {
        return listExplosivos;
    }

    public void setListExplosivos(List<EppExplosivo> listExplosivos) {
        this.listExplosivos = listExplosivos;
    }

    public EppLibroMes getLibroMes() {
        return libroMes;
    }

    public void setLibroMes(EppLibroMes libroMes) {
        this.libroMes = libroMes;
    }

    public boolean isDisabledButtonRegFabri() {
        return disabledButtonRegFabri;
    }

    public void setDisabledButtonRegFabri(boolean disabledButtonRegFabri) {
        this.disabledButtonRegFabri = disabledButtonRegFabri;
    }

    public boolean isDisabledFinalidad() {
        return disabledFinalidad;
    }

    public void setDisabledFinalidad(boolean disabledFinalidad) {
        this.disabledFinalidad = disabledFinalidad;
    }

    public TipoExplosivoGt getTipoFabricacion() {
        return tipoFabricacion;
    }

    public void setTipoFabricacion(TipoExplosivoGt tipoFabricacion) {
        this.tipoFabricacion = tipoFabricacion;
    }

    public TipoExplosivoGt getTipoLibroFabrica() {
        return tipoLibroFabrica;
    }

    public void setTipoLibroFabrica(TipoExplosivoGt tipoLibroFabrica) {
        this.tipoLibroFabrica = tipoLibroFabrica;
    }

    public String getNroResolucion() {
        return nroResolucion;
    }

    public void setNroResolucion(String nroResolucion) {
        this.nroResolucion = nroResolucion;
    }

    public List<EppRegistro> getListRegistrosPlantaUsoComercio() {
        return listRegistrosPlantaUsoComercio;
    }

    public void setListRegistrosPlantaUsoComercio(List<EppRegistro> listRegistrosPlantaUsoComercio) {
        this.listRegistrosPlantaUsoComercio = listRegistrosPlantaUsoComercio;
    }

    public EppRegistro getRegistroPlantaUsoComercio() {
        return registroPlantaUsoComercio;
    }

    public void setRegistroPlantaUsoComercio(EppRegistro registroPlantaUsoComercio) {
        this.registroPlantaUsoComercio = registroPlantaUsoComercio;
    }

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;
    EstadoCrud estadoLibros;

    public EstadoCrud getEstadoLibros() {
        return estadoLibros;
    }

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    //EppLibroMes libroMes;
    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<EppLibroMes> resultados = null;
    ListDataModel<EppLibroMes> resultadosUso = null;
    ListDataModel<EppLibroMes> resultadosFabrica = null;
    ListDataModel<EppRegistro> resultadosComprador = null;
    ListDataModel<EppLibroMes> resultadosComercio = null;

    public ListDataModel<EppLibroMes> getResultadosComercio() {
        return resultadosComercio;
    }

    public void setResultadosComercio(ListDataModel<EppLibroMes> resultadosComercio) {
        this.resultadosComercio = resultadosComercio;
    }

    public ListDataModel<EppLibroMes> getResultadosFabrica() {
        return resultadosFabrica;
    }

    public void setResultadosFabrica(ListDataModel<EppLibroMes> resultadosFabrica) {
        this.resultadosFabrica = resultadosFabrica;
    }

    public ListDataModel<EppRegistro> getResultadosComprador() {
        return resultadosComprador;
    }

    public void setResultadosComprador(ListDataModel<EppRegistro> resultadosComprador) {
        this.resultadosComprador = resultadosComprador;
    }

    public ListDataModel<EppLibroMes> getResultadosUso() {
        return resultadosUso;
    }

    public void setResultadosUso(ListDataModel<EppLibroMes> resultadosUso) {
        this.resultadosUso = resultadosUso;
    }

    /**
     * Lista de areas para seleccion multiple
     */
    List<EppLibro> libroMessSeleccionados;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    // Si la tabla no tiene el campo activo borrar DESDE ACA //
    /**
     * Devuelve si el libroMes actual se encuentra activo, se usa para borrado
     * lógico.
     *
     * @return true si se encuenta activo, false si no.
     */
    public boolean isActivo() {
        return (libroMes.getActivo() == 1);
    }

    /**
     * Cambia el estado del libroMes a activo o inactivo, se usa para borrado
     * lógico.
     *
     * @param activo true o false para cambiar el estado.
     */
    public void setActivo(boolean activo) {
        if (activo) {
            libroMes.setActivo((short) 1);
        } else {
            libroMes.setActivo((short) 0);
        }
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @return
     */
    public List<EppLibro> getRegistrosSeleccionados() {
        return libroMessSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<EppLibro> a) {
        this.libroMessSeleccionados = a;
    }

    /**
     * Anula multiples libroMess, usar como base para acciones de multiples
     * libroMess.
     */
    public void anularMultiple() {
        if ((libroMessSeleccionados == null) || libroMessSeleccionados.isEmpty()) {
            JsfUtil.mensajeAdvertencia("Seleccione un Registro");
            return;
        }
        try {
            for (EppLibro a : libroMessSeleccionados) {
                a.setActivo((short) 0);
                eppLibroFacade.edit(a);
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Activa multiples libroMess, usar como base para acciones de multiples
     * libroMess.
     */
    public void activarMultiple() {
        if ((libroMessSeleccionados == null) || libroMessSeleccionados.isEmpty()) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorSeleccion"));
            return;
        }
        try {
            for (EppLibro a : libroMessSeleccionados) {
                a.setActivo((short) 1);
                eppLibroFacade.edit(a);
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a activo en un
     * libroMes de una lista.
     *
     */
    public void activar() {
        try {
            //libroMes = (EppLibroMes) resultados.getRowData();
            libroMes.setActivo((short) 1);
            // eppLibroFacade.edit(libroMes);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a anulado en un
     * libroMes de una lista.
     *
     */
    public void eliminarResultadosMultiple() {
        try {

            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Propiedad para el libroMes actual.
     *
     * @return El libroMes actual.
     */
    public EppLibroMes getRegistro() {
        return libroMes;
    }

    /**
     * Propiedad para el libroMes actual
     *
     * @param libroMes El nuevo libroMes
     */
    public void setRegistro(EppLibroMes libroMes) {
        this.libroMes = libroMes;
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
    public ListDataModel<EppLibroMes> getResultados() {
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
    public List<EppLibro> getSelectItems() {
        return eppLibroFacade.findAll();
    }

    /**
     * Cambia el estado del controller a buscar
     */
    public void mostrarBuscar() {

        resultados = null;
        menuLibroFabrica();
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
        //resultados = new ListDataModel(eppLibroFacade.selectLike(filtro));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * libroMes.
     *
     */
//    public void mostrarVer() {
//        libroMes = (EppLibro) resultados.getRowData();
//        estado = EstadoCrud.VER;
//    }
//
    /**
     * MUESTRA EL DLG DE CONFIRMACION PARA MOSTRAR EL SGTE FRM SEGUN EL
     * SELECTOMENU
     */
    public void mostrarDlgdlgConfirmacionCamFabrica() {
        if (tipoFinalidad == null) {
            settValoresPaneles();
            setVisiPaneMat(true);
            listEppLibroExploFabricadosFa = new ArrayList<>();
            listEppLibroMaterialUtilizadoFa = new ArrayList<>();
            listEppLibroExploFabricadosFaReproce = new ArrayList<>();
            listEppLibroExploFabricadosFaReproce = new ArrayList<>();
            listEppLibroExploFabricadosFaDestruccion = new ArrayList<>();
            RequestContext.getCurrentInstance().execute("PF('FafDialog').hide()");
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('FafDialog').show()");
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * libroMes AJAX
     */
    public void mostrarEditarRegistroFabrica() {
        listaFasExMesList = new ArrayList<>();
        listaFasInsMesList = new ArrayList<>();

        if (tipoFinalidad == null) {
            settValoresPaneles();
            setVisiPaneMat(true);
            listEppLibroExploFabricadosFa = new ArrayList<>();
            listEppLibroMaterialUtilizadoFa = new ArrayList<>();
            listEppLibroExploFabricadosFaReproce = new ArrayList<>();
            listEppLibroExploFabricadosFaReproce = new ArrayList<>();
            listEppLibroExploFabricadosFaDestruccion = new ArrayList<>();
            return;
        }

        //libroMesFabrica = (EppLibroMes) resultadosFabrica.getRowData();
        registroPlantaUsoComercio = libroMesFabrica.getLibroId().getRegistroId();
        nroResolucion = libroMesFabrica.getLibroId().getRegistroId().getEppResolucion().getNumero();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        listRegistrosPlantaUsoComercio = eppRegistroFacade.buscarResolucion(null, "(" + procesoPlanta + ")", p_user.getPersona().getId().toString());
        listExplosivosInsumos = eppExplosivoFacade.buscarExplosivos();
        listPlantaExplosivos = eppPlantaExplosivoFacade.listPlantaExplosivo(registroPlantaUsoComercio);

        switch (tipoFinalidad.getCodProg()) {
            case "TP_LIBFAB_FAB":

                setTipoLibroFabrica(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBFAB_FAB"));
                setDisabledFinalidad(false);
                setHeaderCabecera(ResourceBundle.getBundle("/BundleBDIntegrado").getString("Fabricacion_header_explosivos"));
                setHeaderDetalle(ResourceBundle.getBundle("/BundleBDIntegrado").getString("Fabricacion_header_material"));
                setRenderTableDetalle(true);
                setRenderFinaDetalle(true);
                String material = "'TP_EXPP_INS'";
                String explo = "'TP_EXPP_EXP'";
                listEppLibroExploFabricadosFa = eppLibroMesFacade.selectLibroUsoDiario(libroMesFabrica, "(" + explo + ")", "TP_LIBFAB_FAB");
                orderListaExplo(listEppLibroExploFabricadosFa);
                listEppLibroMaterialUtilizadoFa = eppLibroMesFacade.selectLibroUsoDiario(libroMesFabrica, "(" + material + ")", "TP_LIBFAB_FAB");
                orderListaExplo(listEppLibroMaterialUtilizadoFa);
                setRennderTableDestr(false);
                setRenderPanGridMaterial(true);
                setRennderTableReproc(false);
                setRenderTableInsuRepro(false);
                setRenderPanelExplFabr(true);

                listEppLibroExploFabricadosFaDestruccion = new ArrayList<>();
                listEppLibroExploFabricadosFaReproce = new ArrayList<>();
                listEppLibroExploFabricadosFaReproce = new ArrayList<>();
                eppPlantaExplosivo = null;
                unidadMedidaMateRela = null;
                setVisiPaneMat(true);
                break;
            case "TP_LIBFAB_DES":
                switch (datas) {
                    case "Edit":
                        if (!validarAperturaFabrica(tipoFinalidad)) {
                            settValoresPaneles();
                            tipoFinalidad = null;
                            return;
                        }
                        break;
                    case "View":

                        break;

                }

                setTipoLibroFabrica(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBFAB_DES"));
                setDisabledFinalidad(true);
                setTipoFabricacion(null);
                setHeaderCabecera(ResourceBundle.getBundle("/BundleBDIntegrado").getString("Fabricacion_header_destruccion"));
                setRenderTableDetalle(false);
                setRenderFinaDetalle(true);
                String exploD = "'TP_EXPP_EXP'";
                listEppLibroExploFabricadosFaDestruccion = eppLibroMesFacade.selectLibroUsoDiario(libroMesFabrica, "(" + exploD + ")", "TP_LIBFAB_DES");
                orderListaExplo(listEppLibroExploFabricadosFaDestruccion);
                setRennderTableDestr(true);
                setRenderTableDetalle(false);
                setRenderFinaDetalle(false);
                setRenderPanGridMaterial(false);
                setRennderTableReproc(false);
                setRenderTableInsuRepro(false);
                setRenderPanelExplFabr(true);
                listEppLibroExploFabricadosFa = new ArrayList<>();
                listEppLibroMaterialUtilizadoFa = new ArrayList<>();
                listEppLibroExploFabricadosFaReproce = new ArrayList<>();
                listEppLibroExploFabricadosFaReproce = new ArrayList<>();
                eppPlantaExplosivo = null;
                unidadMedidaMateRela = null;
                setVisiPaneMat(false);

                break;
            case "TP_LIBFAB_REP":
                switch (datas) {
                    case "Edit":
                        if (!validarAperturaFabrica(tipoFinalidad)) {
                            settValoresPaneles();
                            tipoFinalidad = null;
                            return;
                        }
                        break;
                    case "View":

                        break;

                }

                setTipoLibroFabrica(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBFAB_REP"));
                setDisabledFinalidad(true);
                setTipoFabricacion(null);
                setHeaderCabecera(ResourceBundle.getBundle("/BundleBDIntegrado").getString("Fabricacion_header_reprocesados_expl_fabricados"));
                setHeaderDetalle(ResourceBundle.getBundle("/BundleBDIntegrado").getString("Fabricacion_header_reprocesados_expl_vencidos"));

                setRenderFinaDetalle(false);
                String nuevos = "'TP_LIBFAB_NUV'";
                String deteriorados = "'TP_LIBFAB_DET'";
                listEppLibroExploFabricadosFaReproce = eppLibroMesFacade.selectLibroUsoDiario(libroMesFabrica, "(" + nuevos + ")", "TP_LIBFAB_REP");
                listEppLibroMaterialUtilizadoExploRepro = eppLibroMesFacade.selectLibroUsoDiario(libroMesFabrica, "(" + deteriorados + ")", "TP_LIBFAB_REP");
                orderListaExplo(listEppLibroExploFabricadosFaReproce);
                orderListaExplo(listEppLibroMaterialUtilizadoExploRepro);
                setRennderTableDestr(false);
                setRenderPanGridMaterial(true);
                setRennderTableReproc(true);
                listEppLibroExploFabricadosFa = new ArrayList<>();
                listEppLibroMaterialUtilizadoFa = new ArrayList<>();
                listEppLibroExploFabricadosFaDestruccion = new ArrayList<>();
                setRenderTableInsuRepro(true);
                setRenderTableDetalle(false);
                setRenderPanelExplFabr(true);
                eppPlantaExplosivo = null;
                unidadMedidaMateRela = null;
                setVisiPaneMat(true);
                break;
        }

        String date = "1";
        String mesL = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 2);
        String mesActualLibroMes = "" + libroMesFabrica.getMesId().getOrden();
        if (mesActualLibroMes.equals(mesL)) {
            date = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 3);
        }
        int diasMaxSegunMes = JsfUtil.diasSegunMes(libroMesFabrica.getMesId().getOrden(), new Integer("" + libroMesFabrica.getLibromAnio()));
        diasMaxSegunMes += 1;
        String diasMaxSegunMesString = "" + diasMaxSegunMes;
        listaFasExMesList = listaDiasParam(date, diasMaxSegunMesString);
        resultadosFabrica = null;
        listaFasInsMesList.addAll(listaFasExMesList);
    }

    /**
     * PROCESO LIBRO (DESTRUCCION , REPROCESAMIENTO) CADA 3 MESES
     *
     * @param tipo
     * @return
     */
    public boolean validarAperturaFabrica(TipoExplosivoGt tipo) {

//       if (libroMesFabrica.getEppLibroUsoDiarioList().isEmpty()) {
//
//           return true;
//        }
        List<EppLibroUsoDiario> listLocal = new ArrayList<>();
        EppLibroUsoDiario libroUsoDiarioLocal = null;
        switch (tipo.getCodProg()) {
            case "TP_LIBFAB_DES":// tiene que ser del mismo libro
                listLocal = eppLibroMesFacade.listEppUsoDiario("TP_LIBRO_FAB", "TP_REGEV_ENV", "TP_LIBFAB_DES", libroMesFabrica.getLibroId());
                if (listLocal.isEmpty()) {
                    listLocal = eppLibroMesFacade.listEppUsoDiario("TP_LIBRO_FAB", "TP_REGEV_APRO", "TP_LIBFAB_DES", libroMesFabrica.getLibroId());
                }
                break;
            case "TP_LIBFAB_REP":
                listLocal = eppLibroMesFacade.listEppUsoDiario("TP_LIBRO_FAB", "TP_REGEV_ENV", "TP_LIBFAB_REP", libroMesFabrica.getLibroId());
                if (listLocal.isEmpty()) {
                    listLocal = eppLibroMesFacade.listEppUsoDiario("TP_LIBRO_FAB", "TP_REGEV_APRO", "TP_LIBFAB_DES", libroMesFabrica.getLibroId());
                }
                break;

        }

//        if (listLocal.isEmpty()) {
//            listLocal = eppLibroMesFacade.listEppUsoDiario("TP_LIBRO_FAB", "TP_REGEV_ENV", "TP_LIBFAB_FAB");
//        }
        if (listLocal.isEmpty()) {
            return true;
        }

        if (!listLocal.isEmpty()) {
            libroUsoDiarioLocal = listLocal.get(0);
            long mesAnterior = libroUsoDiarioLocal.getLibroMesId().getLibromOrden();
            long mesAperturar = mesAnterior + 3;
            String mesActual = JsfUtil.obtenerFechaXCriterio(new Date(), 2);
            long mesActualLong = new Long(mesActual);
            List<TipoBaseGt> listaMes = tipoBaseFacade.lstTipoBase("TP_MES");
            String nombreMes = null;
            for (TipoBaseGt it : listaMes) {
                if (it.getOrden().equals(new Integer("" + mesAperturar))) {
                    nombreMes = it.getNombre();
                    break;
                }

            }
            if (mesAperturar != libroMesFabrica.getMesId().getOrden()) {
                JsfUtil.mostrarMensajeDialog("El mes siguiente para hacer un movimiento de tipo " + tipoFinalidad.getNombre() + " es en " + nombreMes, 2);
                return false;
            }
        }
        return true;
    }

    /**
     * AJAX
     */
    public void preMostrarEditar() {

        tipoFinalidad = null;
        libroMesFabrica = (EppLibroMes) resultadosFabrica.getRowData();

        if (!validarFecActualMayorFechaRegistro(libroMesFabrica.getMesId().getOrden(), libroMesFabrica)) {
            return;
        }

        datas = JsfUtil.parametroHtml("action");
        registroPlantaUsoComercio = libroMesFabrica.getLibroId().getRegistroId();
        nroResolucion = libroMesFabrica.getLibroId().getRegistroId().getEppResolucion().getNumero();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        listRegistrosPlantaUsoComercio = eppRegistroFacade.buscarResolucion(null, "(" + procesoPlanta + ")", p_user.getPersona().getId().toString());

        listEppLibroExploFabricadosFa = new ArrayList<>();
        listEppLibroMaterialUtilizadoFa = new ArrayList<>();
        listEppLibroExploFabricadosFaDestruccion = new ArrayList<>();
        listEppLibroMaterialUtilizadoExploRepro = new ArrayList<>();
        listEppLibroExploFabricadosFaReproce = new ArrayList<>();

        switch (datas) {
            case "Edit":
                setVisiPaneMat(true);
                settValoresPaneles();
                estado = EstadoCrud.EDITAR;
                break;
            case "View":
                settValoresPaneles();
                estado = EstadoCrud.VER;
                break;

        }
    }

    /**
     * USO
     */
    public void mostrarEditarUso() {
        libroMes = (EppLibroMes) resultados.getRowData();
        listaUpdateActivos = new ArrayList<>();
        if (!validarFecActualMayorFechaRegistro(libroMes.getMesId().getOrden(), libroMes)) {
            return;
        }
        setDiaUsoDiario(new Long(JsfUtil.obtenerFechaXCriterio(new Date(), 3)));
//        listEppLibroDetalle = new ArrayList<>();
//        cboListExplosivo = new ArrayList<>();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        listRegistrosPlantaUsoComercio = eppRegistroFacade.listRegistrosLibros("(" + procesoAutUso + ")", p_user.getPersona().getId().toString());

//        for (EppRegistro next : lst) {
//            if (next.getEppRegistroGuiaTransito() != null) {
//                if ((next.getEppRegistroGuiaTransito().getTipoTramite().getCodProg().equals("TP_RGUIA_ADQUI1")
//                        || next.getEppRegistroGuiaTransito().getTipoTramite().getCodProg().equals("TP_RGUIA_ADQUI2"))) {
//                    listRegistrosPlantaUsoComercio.add(next);
//                }
//            }
//        }
        registroPlantaUsoComercio = libroMes.getLibroId().getRegistroId();
        tipoLugarUso = registroPlantaUsoComercio.getComId().getEppLugarUsoList().get(0).getTipoLugarId().getNombre();
        for (EppLugarUso it : registroPlantaUsoComercio.getComId().getEppLugarUsoList()) {
            if (it.getActivo() == 1) {
                labelLugaUso = it.getNombre();
                break;
            }
        }
        //nroResolucion = libroMes.getLibroId().getRegistroId().getEppResolucion().getNumero();
        // antes
//        String explo = "'TP_EXPP_EXP'";
        //listEppLibroExploFabricados = eppLibroMesFacade.selectLibroUsoDiario(libroMes, "(" + explo + ")");

        // AHORA
        listEppLibroExploFabricados = eppLibroMesFacade.selectLibroUsoDiarioNuevo(libroMes);

        // YA NO SE VA USAR ESTE PASO TODO ESTA EN listEppLibroExploFabricados(EppLibroUsoDiario)
//        for (EppLibroUsoDiario it : listEppLibroExploFabricados) {
//            for (EppLibroDetalle itDe : eppLibroDetalleFacade.listEppdetalle(it)) {
//                if (it.equals(itDe.getLibroUsoDiarioId())) {
//                    listEppLibroDetalle.add(itDe);
//                }
//
//            }
//        }
        ajaxMostrarResolucion();
        // ANTES
//        for (EppLibroUsoDiario re : listEppLibroExploFabricados) {
//            cboListExplosivo.add(re.getExplosivoId());
//        }
        data = JsfUtil.parametroHtml("action");
        switch (data) {
            case "Edit":
                setDisabledCabeceraEdit(true);
                setRenderPanelUso(true);
                setVerMensDlg(true);
                estado = EstadoCrud.EDITAR;
                break;
            case "View":
                setDisabledCabeceraEdit(true);
                setRenderPanelUso(true);
                setVerMensDlg(true);
                estado = EstadoCrud.VER;
                break;

        }
        listaDiasMes = new ArrayList<>();
        String date = "1";
        String dateFin = "32";
        String mesL = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 2);
        String mesFinResolucion = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaFin(), 2);
        String mesActualLibroMes = "" + libroMes.getMesId().getOrden();
        if (mesActualLibroMes.equals(mesL)) {
            date = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 3);
        }
//        if (mesFinResolucion.equals(mesActualLibroMes)) {
//            dateFin = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaFin(), 3);
//        }

        int diasMaxSegunMes = JsfUtil.diasSegunMes(libroMes.getMesId().getOrden(), new Integer("" + libroMes.getLibromAnio()));
        diasMaxSegunMes += 1;
        String diasMaxSegunMesString = "" + diasMaxSegunMes;
        listaDiasMes = listaDiasParam(date, diasMaxSegunMesString);
        limpiarValoresDetalleUso();
        resultados = null;
    }

    /**
     * VENTA/COMERCIO
     */
    public void mostrarEditarVenta() {
        libroMesComercio = (EppLibroMes) resultadosComercio.getRowData();
        if (!validarFecActualMayorFechaRegistro(libroMesComercio.getMesId().getOrden(), libroMesComercio)) {
            return;
        }
        listRegistrosPlantaUsoComercio = new ArrayList<>();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        listRegistrosPlantaUsoComercio = eppRegistroFacade.listRegistrosLibros("(" + procesoComercio + ")", p_user.getPersona().getId().toString());
        registroPlantaUsoComercio = libroMesComercio.getLibroId().getRegistroId();

        listEppLibroDetalle = eppLibroDetalleFacade.listEppdetalle(libroMesComercio);
        numeroResFabri = libroMesComercio.getLibroId().getRegistroId().getRegistroOpeId().getEppResolucion().getNumero();
        direccionFabrica = libroMesComercio.getLibroId().getRegistroId().getRegistroOpeId().getEppLocalList().get(0).getDireccionCompleta();
        cboListExplosivo = new ArrayList<>();
        try {
            destino = registroComprador.getComId().getEppLugarUsoList().get(0).getNombre();
        } catch (NullPointerException e) {
        }
        //ajaxMostrarRgfabricaComercio();
        //tipoDestinatario = registroPlantaUsoComercio.getComId().getEppLugarUsoList().get(0).getTipoLugarId().getNombre();
        //String explo = "'TP_EXPP_EXP'";
        listEppLibroExploFabricadosCo = eppLibroMesFacade.selectLibroUsoDiarioNuevo(libroMesComercio);
        for (EppLibroUsoDiario it : listEppLibroExploFabricadosCo) {
            cboListExplosivo.add(it.getExplosivoId());

        }

        String date = "1";
        String mesL = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 2);
        String mesActualLibroMes = "" + libroMesComercio.getMesId().getOrden();
        if (mesActualLibroMes.equals(mesL)) {
            // date = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 3);
            date = "1";
        }
        int diasMaxSegunMes = JsfUtil.diasSegunMes(libroMesComercio.getMesId().getOrden(), new Integer("" + libroMesComercio.getLibromAnio()));
        diasMaxSegunMes += 1;
        String diasMaxSegunMesString = "" + diasMaxSegunMes;
        listaVenExMesList = listaDiasParam(date, diasMaxSegunMesString);

        String dataC = JsfUtil.parametroHtml("action");
        switch (dataC) {
            case "Edit":
                estado = EstadoCrud.EDITAR;
                comprador = null;
                destino = null;
                tipoDestinatario = null;
                break;
            case "View":
                estado = EstadoCrud.VER;
                break;

        }
        resultados = null;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * libroMes
     */
    public void mostrarCrear() {
        estado = EstadoCrud.CREAR;
        libroMes = new EppLibroMes();
        libroMes.setActivo((short) 1);
    }

    /**
     * mostrar crear libroMess de fabricacion
     */
    public void mostrarCrearMaster() {
        libroMesFabrica = new EppLibroMes();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        listRegistrosPlantaUsoComercio = eppRegistroFacade.buscarResolucion(null, "(" + procesoPlanta + ")", p_user.getPersona().getId().toString());
//        if (listRegistrosPlantaUsoComercio.isEmpty()) {
//            estado = EstadoCrud.CREAR;
//        }

        for (Iterator<EppRegistro> iterator = listRegistrosPlantaUsoComercio.iterator(); iterator.hasNext();) {
            EppRegistro next = iterator.next();
            if (next.getEppLibro() == null) {
                iterator.remove();
            }

            if (next.getEppLibro() != null) {
                if (next.getEppLibro().getEppLibroMesList() != null) {
                    if (!next.getEppLibro().getEppLibroMesList().isEmpty()) {
                        iterator.remove();
                    }
                }
            }

            libroMesFabrica.setEppLibroUsoDiarioList(new ArrayList<EppLibroUsoDiario>());
            ajaxChangeTipolibroMes();
            mostrarPeriodoFabrica();

            //List<EppLibroMes> lst = eppLibroMesFacade.listEpplibroMes(listRegistrosPlantaUsoComercio);
            //validarRgFabrica(lst);
            limpiarRegFabrica();

        }
        limpiarRegFabrica();
        estado = EstadoCrud.CREAR;
    }

    /**
     * LIMPIA VALORES DE FABRICA
     */
    public void limpiarRegFabrica() {
        registroPlantaUsoComercio = null;
        dirLocal = null;
    }

    /**
     * MOSTRAR RG FABRICA LOCAL
     */
    public void ajaxMostrarResolucionFabrica() {
        if (registroPlantaUsoComercio != null) {
            for (EppLocal it : registroPlantaUsoComercio.getEppLocalList()) {
                if (it.getActivo() == 1) {
                    dirLocal = it.getDireccionCompleta();
                    break;
                }
            }

        }

    }

    /**
     * APERTUTA EL SGTE MES AUTOMATICO PROC LIBRO (FABRICA ,
     * COMERCIALIZACION=VENTA) METODO PARA LOS DOS PROCESOS DE LIBROS
     */
    public void aperturarTiposDeFabrica() {
        EppLibroMes libroMesss = new EppLibroMes();
        libroMesss = (EppLibroMes) JsfUtil.entidadMayusculas(libroMesss, "");
        libroMesss.setActivo(JsfUtil.TRUE);
        libroMesss.setUsoExplosivo(JsfUtil.TRUE);
        libroMesss.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        libroMesss.setAudNumIp(JsfUtil.getIpAddress());
        libroMesss.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));

        EppLibro libro = registroPlantaUsoComercio.getEppLibro();
        libro.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_LIBEVT_APE").get(0));
        libro.setEppLibroMesList(new ArrayList<EppLibroMes>());
        String anios = JsfUtil.obtenerFechaXCriterio(new Date(), 1);
        String messActual = JsfUtil.obtenerFechaXCriterio(new Date(), 2);

        for (TipoBaseGt it : tipoBaseFacade.lstTipoBase("TP_MES")) {
            if (Integer.valueOf(messActual).equals(it.getOrden())) {
                libroMesss.setMesId(it);
                libroMesss.setLibromOrden(new Long("" + it.getOrden()));
                break;
            }
        }
        libroMesss.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBRO_FAB"));
        switch (registroPlantaUsoComercio.getTipoProId().getCodProg()) {
            case "TP_PRTUP_ACOM":
            case "TP_PRTUP_COM":
                libroMesss.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBRO_COM"));
                libroMesss.setEppLibroUsoDiarioList(creaLibroUsoDiarioVenta(explosivosRgComercializacion(registroPlantaUsoComercio), libroMesss));
                break;
        }
        libroMesss.setLibroId(libro);
        libroMesss.setLibromAnio(Long.parseLong(anios));

        libro.getEppLibroMesList().add(libroMesss);
        eppLibroFacade.edit(libro);
        libroMesss = new EppLibroMes();
        JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado"));
        switch (registroPlantaUsoComercio.getTipoProId().getCodProg()) {
            case "TP_PRTUP_ACOM":
            case "TP_PRTUP_COM":
                menuLibroComercio();
                break;
            case "TP_PRTUP_PLA":
            case "TP_PRTUP_AFAB":
                menuLibroFabrica();
                break;
        }

        estado = EstadoCrud.BUSCAR;

    }

    /**
     * DEL FRM CREAR
     */
    public void limpiarValoresVenta() {
        registroPlantaUsoComercio = null;
        numeroResFabri = null;
        direccionFabrica = null;
    }

    /**
     * SOLO PARA USO
     */
    public void ajaxMostrarResolucion() {
//        if (registroPlantaUsoComercio == null) {
//            labelLugaUso = null;
//            tipoLugarUso = null;
//            return;
//        }

        if (registroPlantaUsoComercio != null) {
            for (EppLugarUso it : registroPlantaUsoComercio.getComId().getEppLugarUsoList()) {
                if (it.getActivo() == 1) {
                    labelLugaUso = it.getNombre();
                    break;
                }
            }

            mostrarPeriodoUso();
            listExplosivos = registroPlantaUsoComercio.getEppExplosivoList();
            listPlantaExplosivos = eppPlantaExplosivoFacade.listPlantaExplosivo(registroPlantaUsoComercio);
            switch (registroPlantaUsoComercio.getTipoProId().getCodProg()) {
                case "TP_PRTUP_GLO":
                case "TP_PRTUP_EVE":
                case "TP_PRTUP_EXC":
                case "TP_PRTUP_AAFOR":
                case "TP_PRTUP_AAHID":
                case "TP_PRTUP_AAPOR":
                    tipoLugarUso = registroPlantaUsoComercio.getComId().getEppLugarUsoList().get(0).getTipoLugarId().getNombre();
                    listEppDetalleAutUso = registroPlantaUsoComercio.getEppDetalleAutUsoList();

                    try {
                        listGuiaTransito = new ArrayList<>();
                        EppLugarUso lugarUso = registroPlantaUsoComercio.getComId().getEppLugarUsoList().get(0);
                        List<EppRegistroGuiaTransito> lst = eppRegistroGuiaTransitoFacade.listRegistroGuiaTransitoxlugarUsoDestinino(lugarUso);
                        if (lst.isEmpty()) {
                            JsfUtil.mensajeAdvertencia("El lugar de uso no cuenta con Registros de Guias de Transito");
                            return;
                        }
                        for (EppRegistroGuiaTransito next : lst) {
                            if ((next.getTipoTramite().getCodProg().equals("TP_RGUIA_ADQUI1")
                                    || next.getTipoTramite().getCodProg().equals("TP_RGUIA_ADQUI2"))) {
                                listGuiaTransito.add(next);
                            }
                        }
                        // ¿ ?
                        listGuiaTransito.removeAll(eppLibroUsoDiarioFacade.listLibroUsoDiarioXguia(listGuiaTransito));

//                        if (!listEppLibroExploFabricados.isEmpty()) {
//                            if (!listGuiaTransito.isEmpty()) {
//                                for (EppLibroUsoDiario it : listEppLibroExploFabricados) {
//                                    //OBS
////                                    if (listGuiaTransito.contains(it.getGuiaTransitoId())) {
////                                        listGuiaTransito.remove(it.getGuiaTransitoId());
////                                    }
//                                }
//                            }
//                        }
//                        if (!eppLibroUsoDiarioFacade.listLibroUsoDiarioAll().isEmpty()) {
//                            if (listEppLibroExploFabricados.isEmpty()) {
//                                if (!listGuiaTransito.isEmpty()) {
//                                    for (EppLibroUsoDiario it : eppLibroUsoDiarioFacade.listLibroUsoDiarioAll()) {
//                                        if (listGuiaTransito.contains(it.getGuiaTransitoId())) {
//                                            listGuiaTransito.remove(it.getGuiaTransitoId());
//                                        }
//                                    }
//                                }
//
//                            }
//
//                        }
                    } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                        JsfUtil.mensajeAdvertencia("El lugar de uso no cuenta con Registros de Guias de Transito");
                    }
                    break;
            }

        }

    }

    public void ajaxEncontrarUnMedidaExplo() {
        if (explosivo == null) {
            unidadMedidaExplo = null;

            return;
        }

        if (explosivo != null) {
            unidadMedidaExplo = explosivo.getUnidadMedidaList().get(0).getNombre();
        }
    }

    public void ajaxEncontrarUnMedidaMateRela() {
        if (eppPlantaExplosivo == null) {
            unidadMedidaMateRela = null;

            return;
        }
        if (eppPlantaExplosivo != null) {
            unidadMedidaMateRela = eppPlantaExplosivo.getExplosivoId().getUnidadMedidaList().get(0).getNombre();
        }
    }

    /**
     * proc libro fabrica
     */
    public void agregarMaterUtilizado() {
        if (explosivo == null) {
            JsfUtil.mensajeAdvertencia("Seleccione un Explosivo");
            return;
        }

        if (cantidadMater == null) {
            JsfUtil.mensajeAdvertencia("Ingrese una Cantidad");
            return;
        }

        EppLibroUsoDiario ep = new EppLibroUsoDiario();
        ep.setId(JsfUtil.tempId());
        ep.setActivo(JsfUtil.TRUE);
        ep.setAudNumIp(JsfUtil.getIpAddress());
        ep.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        ep.setExplosivoId(explosivo);
        ep.setDia(diaUsoFabMat);
        ep.setTipoFinalidad(tipoFinalidad);

//        switch (tipoLibroFabrica.getCodProg()) {
//            case "TP_LIBFAB_FAB":
//                ep.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_EXPP_INS"));
//            case "TP_LIBFAB_DES":
//                break;
//            case "TP_LIBFAB_REP":
//                ep.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBFAB_DET"));
//                break;
//        }
        ep.setDia(diaFaDiarioIns);
        ep.setCantidad(cantidadMater);
        switch (registroPlantaUsoComercio.getTipoProId().getCodProg()) {
            case "TP_PRTUP_PLA":
            case "TP_PRTUP_AFAB":
                switch (tipoFinalidad.getCodProg()) {
                    case "TP_LIBFAB_FAB":
//                        if (!validarExploDuplicadomMaterial(explosivo)) {
//                            return;
//                        }
                        ep.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_EXPP_INS"));
                        break;
                    case "TP_LIBFAB_DES":
                    case "TP_LIBFAB_REP":
                        if (!validarExploDuplicadomMaterialRepro(explosivo)) {
                            return;

                        }
                        ep.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBFAB_DET"));
                        break;

                }
                ep.setLibroMesId(libroMesFabrica);

                break;
            case "TP_PRTUP_ACOM":
            case "TP_PRTUP_COM":

                break;

        }
        ep.setUnidadMedidaId(explosivo.getUnidadMedidaList().get(0).getId());
        ep.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
        switch (tipoFinalidad.getCodProg()) {
            case "TP_LIBFAB_FAB":
                listEppLibroMaterialUtilizadoFa.add(ep);
                orderListaExplo(listEppLibroMaterialUtilizadoFa);
                break;
            case "TP_LIBFAB_REP":
                listEppLibroMaterialUtilizadoExploRepro.add(ep);
                orderListaExplo(listEppLibroMaterialUtilizadoExploRepro);
                break;
        }

        ep = new EppLibroUsoDiario();
        explosivo = null;
        cantidadMater = null;
        setUnidadMedidaExplo(null);
    }

    /**
     * FABRICA , COMERCIO
     */
    public void agregarExplosivos() {

//        if (eppPlantaExplosivo == null) {
//            JsfUtil.mensajeAdvertencia("Seleccione un explosivo");
//            return;
//        }
        if ((registroPlantaUsoComercio.getTipoProId().getCodProg().equals("TP_PRTUP_PLA")
                || registroPlantaUsoComercio.getTipoProId().getCodProg().equals("TP_PRTUP_AFAB"))) {
            if (eppPlantaExplosivo == null) {
                JsfUtil.mensajeAdvertencia("Seleccione un Explosivo");
                return;
            }

            if (cantidad == null) {
                JsfUtil.mensajeAdvertencia("Ingrese una Cantidad");
                return;
            }

//            if (validarExploDuplicado(eppPlantaExplosivo.getExplosivoId())) {
//                return;
//            }
        }

        EppLibroUsoDiario ep = new EppLibroUsoDiario();
        ep.setId(JsfUtil.tempId());
        ep.setActivo(JsfUtil.TRUE);
        ep.setAudNumIp(JsfUtil.getIpAddress());
        ep.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        ep.setNroLote(nroLote);
        ep.setCantidad(cantidad == null ? 0.0 : cantidad);

        // validar si es fabrica o comercio
        ep.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
        switch (registroPlantaUsoComercio.getTipoProId().getCodProg()) {
            case "TP_PRTUP_PLA":
            case "TP_PRTUP_AFAB":
                ep.setDia(diaFaDiarioEx);
                ep.setLibroMesId(libroMesFabrica);
                ep.setTipoFinalidad(tipoFinalidad);
                switch (tipoFinalidad.getCodProg()) {
                    case "TP_LIBFAB_FAB":
                        if (tipoDetalle == null) {
                            JsfUtil.mensajeAdvertencia("El campo tipo nesecita un valor");
                            return;
                        }
                        ep.setTipoDetalle(tipoDetalle);
                        break;
                }

                break;
            case "TP_PRTUP_ACOM":
            case "TP_PRTUP_COM":
                ep.setLibroMesId(libroMesComercio);
                if (explosivo == null) {
                    JsfUtil.mensajeAdvertencia("Seleccione un Explosivo");
                    return;
                }

                ep.setExplosivoId(explosivo);
                ep.setUnidadMedidaId(explosivo.getUnidadMedidaList().get(0).getId());
                ep.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_EXPP_EXP"));
                ep.setCantidad(0.0);

                break;

        }

        if (tipoLibroFabrica != null) {
            switch (tipoLibroFabrica.getCodProg()) {
                case "TP_LIBFAB_FAB":
                case "TP_LIBFAB_DES":
                    ep.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_EXPP_EXP"));
                    ep.setExplosivoId(eppPlantaExplosivo.getExplosivoId());
                    ep.setUnidadMedidaId(eppPlantaExplosivo.getExplosivoId().getUnidadMedidaList().get(0).getId());
                    break;
                case "TP_LIBFAB_REP":
                    ep.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBFAB_NUV"));
                    ep.setExplosivoId(eppPlantaExplosivo.getExplosivoId());
                    ep.setUnidadMedidaId(eppPlantaExplosivo.getExplosivoId().getUnidadMedidaList().get(0).getId());
                    break;
            }
        }

        //listEppLibroExploFabricados.add(ep);
        switch (registroPlantaUsoComercio.getTipoProId().getCodProg()) {
            case "TP_PRTUP_PLA":
            case "TP_PRTUP_AFAB":
                switch (tipoFinalidad.getCodProg()) {
                    case "TP_LIBFAB_FAB":
//                        if (validarExploDuplicadoDes(ep.getExplosivoId())) {
//                            return;
//                        }
                        listEppLibroExploFabricadosFa.add(ep);
                        orderListaExplo(listEppLibroExploFabricadosFa);
                        break;
                    case "TP_LIBFAB_DES":
//                        if (validarExploDuplicadoDes(ep.getExplosivoId())) {
//                            return;
//                        }
                        listEppLibroExploFabricadosFaDestruccion.add(ep);
                        orderListaExplo(listEppLibroExploFabricadosFaDestruccion);
                        break;
                    case "TP_LIBFAB_REP":
//                        if (validarExploDuplicadoRep(ep.getExplosivoId())) {
//                            return;
//                        }
                        listEppLibroExploFabricadosFaReproce.add(ep);
                        orderListaExplo(listEppLibroExploFabricadosFaReproce);
                        //listEppLibroExploFabricadosFaDestruccion.add(ep);
                        break;
                }

                break;
            case "TP_PRTUP_ACOM":
            case "TP_PRTUP_COM":
                cboListExplosivo.add(ep.getExplosivoId());
                listEppLibroExploFabricadosCo.add(ep);

        }

        ep = new EppLibroUsoDiario();
        limpiarValoExplosivos();
    }

    /**
     * PROC LIBRO VENTA/COMERCIALIZACION BUSCA LOS EXPLOSIVOS
     *
     * @param re
     * @return
     */
    public List<EppExplosivo> explosivosRgComercializacion(EppRegistro re) {
        HashSet<EppExplosivo> lst = new HashSet<>();
        List<EppExplosivo> listExTemporal = new ArrayList<>();
        for (EppPlantaExplosivo ex1 : re.getEppPlantaExplosivoList()) {
            lst.add(ex1.getExplosivoId());
        }

//        for (EppPlantaExplosivo ex2 : re.getRegistroOpeId().getEppPlantaExplosivoList()) {
//            lst.add(ex2.getExplosivoId());
//        }
//
//        for (EppExplosivo ex3 : explosivoFacade.listExploInterSalidaXempresa(re.getEmpresaId())) {
//            lst.add(ex3);
//        }
        listExTemporal.addAll(lst);
        return listExTemporal;
    }

    /**
     * PROC LIBRO VENTA/COMERCIO settea campos de EppLibroUsoDiario
     *
     * @param ex
     * @param mesLibro
     * @return
     */
    public List<EppLibroUsoDiario> creaLibroUsoDiarioVenta(List<EppExplosivo> ex, EppLibroMes mesLibro) {
        List<EppLibroUsoDiario> listUsoDiario = new ArrayList<>();
        String dia = JsfUtil.obtenerFechaXCriterio(new Date(), 3);
        for (EppExplosivo it : ex) {
            EppLibroUsoDiario lib = new EppLibroUsoDiario();
            lib.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
            lib.setActivo(JsfUtil.TRUE);
            lib.setAudNumIp(JsfUtil.getIpAddress());
            lib.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            lib.setCantidad(0.0);
            lib.setDia(new Long(dia));
            lib.setLibroMesId(libroMesComercio.getId() == null ? mesLibro : libroMesComercio);
            lib.setExplosivoId(it);
            lib.setUnidadMedidaId(it.getUnidadMedidaList().get(0).getId());
            lib.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_EXPP_EXP"));// comprobar
            listUsoDiario.add(lib);
        }
        return listUsoDiario;
    }

    /**
     * VALIDAR EXPLOSIVO DUPLICADO
     *
     * @param ep
     * @return
     */
    public boolean validarExploDuplicadoRep(EppExplosivo ep) {
        if (listEppLibroExploFabricadosFaReproce == null) {
            listEppLibroExploFabricadosFaReproce = new ArrayList<>();
        }
        for (EppLibroUsoDiario de : listEppLibroExploFabricadosFaReproce) {
            if (de.getExplosivoId().equals(ep)) {
                JsfUtil.mensajeAdvertencia("El Explosivo ya fue Ingressado a la lista");
                return true;
            }
        }
        return false;
    }

    public boolean validarExploDuplicadoDes(EppExplosivo ep) {
        if (listEppLibroExploFabricadosFaDestruccion == null) {
            listEppLibroExploFabricadosFaDestruccion = new ArrayList<>();
        }
        for (EppLibroUsoDiario de : listEppLibroExploFabricadosFaDestruccion) {
            if (de.getExplosivoId().equals(ep)) {
                JsfUtil.mensajeAdvertencia("El Explosivo ya fue Ingressado a la lista");
                return true;
            }
        }
        return false;
    }

    public boolean validarExploDuplicadoDesFa(EppExplosivo ep) {
        if (listEppLibroExploFabricadosFa == null) {
            listEppLibroExploFabricadosFa = new ArrayList<>();
        }
        for (EppLibroUsoDiario de : listEppLibroExploFabricadosFa) {
            if (de.getExplosivoId().equals(ep)) {
                JsfUtil.mensajeAdvertencia("El Explosivo ya fue Ingressado a la lista");
                return true;
            }
        }
        return false;
    }

    /**
     * VALIDAR EXPLOSIVO DUPLICADO MATERIAL UTILIZADO A FABRICAR
     *
     * @param ep
     * @return
     */
    public boolean validarExploDuplicadomMaterial(EppExplosivo ep) {
        if (listEppLibroMaterialUtilizadoFa == null) {
            listEppLibroMaterialUtilizadoFa = new ArrayList<>();
        }
        if (listEppLibroMaterialUtilizadoFa.isEmpty()) {
            return true;
        }
        for (EppLibroUsoDiario de : listEppLibroMaterialUtilizadoFa) {
            if (de.getExplosivoId().equals(ep) && de.getTipoRegistroId().getCodProg().equals("TP_EXPP_INS")) {
                JsfUtil.mensajeAdvertencia("El Explosivo ya fue Ingressado a la lista");
                return false;
            }
        }
        return true;
    }

    public boolean validarExploDuplicadomMaterialRepro(EppExplosivo ep) {
        if (listEppLibroMaterialUtilizadoExploRepro == null) {
            listEppLibroMaterialUtilizadoExploRepro = new ArrayList<>();
        }
        if (listEppLibroMaterialUtilizadoExploRepro.isEmpty()) {
            return true;
        }
        for (EppLibroUsoDiario de : listEppLibroMaterialUtilizadoExploRepro) {
            if (de.getExplosivoId().equals(ep) && de.getTipoRegistroId().getCodProg().equals("TP_EXPP_INS")) {
                JsfUtil.mensajeAdvertencia("El Explosivo ya fue Ingressado a la lista");
                return false;
            }
        }
        return true;
    }

    public void limpiarValoExplosivos() {
        explosivoSolicitado = null;
        eppPlantaExplosivo = null;
        cantidad = null;
        nroLote = null;
        setUnidadMedidaMateRela(null);
        setUnidadMedidaExplo(null);
        saldo = null;
        tipoDetalle = null;
        explosivo = null;
        cantidadMater = null;
        unidadMedidaExplo = null;

    }

    public void elimimarMultipleMaterial() {
        for (EppLibroUsoDiario it : selectMaterial) {
            if (it.getId() > 0) {
                listaUpdateActivosFabrica.add(it);
            }
            listEppLibroMaterialUtilizadoFa.remove(it);
        }
    }

    public void elimimarMultipleMaterialRepro() {
        for (EppLibroUsoDiario it : selectMaterialRepro) {
            if (it.getId() > 0) {
                listaUpdateActivosFabrica.add(it);
            }
            listEppLibroMaterialUtilizadoExploRepro.remove(it);
        }
    }

    public void elimimarMultipleExplosivo() {
        for (EppLibroUsoDiario it : selectExplosivos) {
            if (it.getId() > 0) {
                listaUpdateActivosFabrica.add(it);
            }
            listEppLibroExploFabricadosFa.remove(it);
            selectExplosivos = null;

        }
    }

    public void elimimarMultipleExplosivoDes() {
        for (EppLibroUsoDiario it : selectExplosivosDes) {
            if (it.getId() > 0) {
                listaUpdateActivosFabrica.add(it);
            }
            listEppLibroExploFabricadosFaDestruccion.remove(it);
            selectExplosivosDes = null;

        }
    }

    public void elimimarMultipleExplosivoRepro() {
        for (EppLibroUsoDiario it : selectExplosivosReproc) {
            if (it.getId() > 0) {
                listaUpdateActivosFabrica.add(it);
            }
            listEppLibroExploFabricadosFaReproce.remove(it);
            selectExplosivosDes = null;

        }
    }

    public void elimimarMultipleExplosivoComercio() {
        for (EppLibroUsoDiario it : selectExplosivos) {
            if (it.getId() > 0) {
                listaUpdateActivos.add(it);
            }

            if (it.getEppLibroDetalleList() != null) {
                for (EppLibroDetalle itDe : it.getEppLibroDetalleList()) {
                    if (itDe.getId() > 0) {
                        listaUpdateActivosEppdetaDetalle.add(itDe);
                    }
                    listEppLibroDetalle.remove(itDe);
                }
            }

            listEppLibroExploFabricados.remove(it);
            selectExplosivos = null;

        }
    }

    /**
     * muestra dialog de borrarUso
     */
    public void mostrarDialogBorrarUso() {
        if (selectExplosivos.isEmpty() || selectExplosivos == null) {
            JsfUtil.mensajeAdvertencia("Seleccione un registro para eliminar");
            return;
        }
//        if (selectExplosivos.size() > 1) {
//            JsfUtil.mensajeAdvertencia("Seleccione solo uno por favor");
//            return;
//        }

        for (EppLibroUsoDiario it : selectExplosivos) {
            if (it.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_ING")) {
//                if (libroMes.getId() != null) {
//                    JsfUtil.mensajeAdvertencia("No puede eliminar un Explosivo de Tipo Ingreso que esta Registrado en la Base de datos");
//                    return;
//                }
                /* CUANDO SE PODIA BORRAR ALL
                valorTextoBorrarUso = "Se borrara todos los explosivos de tipo Ingreso relacionados con la Guia " + it.getGuiaTransitoId().getRegistroId().getEppResolucion().getNumero();
                setListExploFabricadosBorrarUsoDialog(it.getGuiaTransitoId().getEppExplosivoSolicitadoList());
               eppGuiaTransito = it.getGuiaTransitoId();
                 */
//                cargarValores(it);
//                RequestContext.getCurrentInstance().execute("PF('ConfDialogExpl').show()");
                JsfUtil.mensajeAdvertencia("Los Registro de tipo Ingreso no pueden ser eliminados solo puede ser editados ");
                selectExplosivos = null;
                return;
            } else {
                eliminarExploLibroUsoDiario();
                limpiarValoresPanelExplMatRela();
            }

        }
        selectExplosivos = null;
    }

    /**
     * BORRA LOS EXPLOSIVOS DESDE EL DIALOG USO (LIBRO)
     */
    public void borrarExploLibroUsoDiarioDialog() {
        for (EppLibroUsoDiario it : selectExplosivos) {
            if (it.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_ING")) {
                EppRegistroGuiaTransito re = it.getGuiaTransitoId();
                if (disabledFinalidad) {

                }
            }

//            if (it.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_ING")) {
//                valorTextoBorrarUso = "Se borrara todos los explosivos de tipo Ingreso relacionados con la Guia " + it.getGuiaTransitoId().getRegistroId().getEppResolucion().getNumero();
//                RequestContext.getCurrentInstance().execute("PF('ConfDialog').show()");
//                return;
//            }
            if (it.getId() > 0) {
                listaUpdateActivos.add(it);
            }
            listEppLibroExploFabricados.remove(it);
            selectExplosivos = null;

        }
    }

    /**
     * elimina registro de eppLibroUsoDiario (proceso uso Libro) solo datos de
     * esa tabla
     */
    public void eliminarExploLibroUsoDiario() {

        for (EppLibroUsoDiario it : selectExplosivos) {
//            if (it.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_ING")) {
//                valorTextoBorrarUso = "Se borrara todos los explosivos de tipo Ingreso relacionados con la Guia " + it.getGuiaTransitoId().getRegistroId().getEppResolucion().getNumero();
//                RequestContext.getCurrentInstance().execute("PF('ConfDialog').show()");
//                return;
//            }
            if (it.getId() > 0) {
                listaUpdateActivos.add(it);

            }
            listEppLibroExploFabricados.remove(it);

        }

    }

    /**
     * PROC LIBRO VENTA
     */
    public void elimimarMultipleEpplibroDetalle() {
        for (EppLibroDetalle it : selectLibrodetalle) {
            if (it.getId() > 0 || it.getId() < 0) {
                for (EppLibroUsoDiario itLiDia : listEppLibroExploFabricadosCo) {
                    if (itLiDia.equals(it.getLibroUsoDiarioId())) {
                        itLiDia.getEppLibroDetalleList().remove(it);
                        double cantDetalle = it.getCantidad();
                        double cantUso = itLiDia.getCantidad();
                        cantUso -= cantDetalle;
                        itLiDia.setCantidad(cantUso);

                    }
                }
                if (it.getId() > 0) {
                    listaUpdateActivosEppdetaDetalle.add(it);
                }

            }

            listEppLibroDetalle.remove(it);
            selectLibrodetalle = null;

        }
    }

    /**
     * USO
     */
    public void limpiarValoresDetalleUso() {
        tipoUso = null;
        eppGuiaTransito = null;
        cboExplosivo = null;
        nroLote = null;
        cantidad = null;
        saldo = null;
        unidadMedidaExplo = null;
        eppDetalleAutUso = null;
        libroUsoDiario = null;
    }

    /**
     * AJAX VISIBLE PANEL PROC FABRICA / DESTRUCCION / REPROCESAMIENTO
     */
    public void ajaxChangeTipolibroMes() {
        if (tipoLibroFabrica != null) {
            switch (tipoLibroFabrica.getCodProg()) {
                case "TP_LIBFAB_FAB":
                    setTipoLibroFabrica(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBFAB_FAB"));
                    setDisabledFinalidad(false);
                    setHeaderCabecera(ResourceBundle.getBundle("/BundleBDIntegrado").getString("Fabricacion_header_explosivos"));
                    setHeaderDetalle(ResourceBundle.getBundle("/BundleBDIntegrado").getString("Fabricacion_header_material"));
                    setRenderTableDetalle(true);
                    setRenderFinaDetalle(true);
                    break;
                case "TP_LIBFAB_DES":
                    setTipoLibroFabrica(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBFAB_DES"));
                    setDisabledFinalidad(true);
                    setTipoFabricacion(null);
                    setHeaderCabecera(ResourceBundle.getBundle("/BundleBDIntegrado").getString("Fabricacion_header_destruccion"));
                    setRenderTableDetalle(false);
                    setRenderFinaDetalle(false);
                    break;
                case "TP_LIBFAB_REP":
                    setTipoLibroFabrica(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBFAB_REP"));
                    setDisabledFinalidad(true);
                    setTipoFabricacion(null);
                    setHeaderCabecera(ResourceBundle.getBundle("/BundleBDIntegrado").getString("Fabricacion_header_reprocesados_expl_fabricados"));
                    setHeaderDetalle(ResourceBundle.getBundle("/BundleBDIntegrado").getString("Fabricacion_header_reprocesados_expl_vencidos"));
                    setRenderTableDetalle(true);
                    setRenderFinaDetalle(false);
                    break;

            }
        } else {
            settValoresPaneles();
        }

    }

    /**
     * TODOS LOS PANELES NO VISIBLES
     */
    public void settValoresPaneles() {
        setRenderTableDetalle(false);
        setRenderFinaDetalle(false);
        setRennderTableDestr(false);
        setRenderTableInsuRepro(false);
        setRenderPanGridMaterial(false);
        setRennderTableReproc(false);
        setRenderPanelExplFabr(false);
        cantidad = null;
        eppPlantaExplosivo = null;
        unidadMedidaMateRela = null;
        nroLote = null;

    }

    public void limpiarValores() {
        libroMes = new EppLibroMes();
        registroPlantaUsoComercio = new EppRegistro();
        nroResolucion = null;
        tipoFabricacion = null;
        tipoLibroFabrica = null;
        listEppLibroMaterialUtilizado = null;
        listEppLibroExploFabricados = null;
    }

    public void limpiarValoresUso() {
        registroPlantaUsoComercio = null;
        tipoLugarUso = null;
        nroResolucion = null;
        tipoUso = null;
        eppGuiaTransito = null;
        explosivoSolicitado = null;
        saldo = null;
        unidadMedidaExplo = null;
        eppDetalleAutUso = null;
        nroLote = null;
        cantidad = null;

    }

    public void disabledButtonxPeriodos() {
        String dia = JsfUtil.obtenerFechaXCriterio(new Date(), 3);
        int diaInt = Integer.parseInt(dia);
        if (diaInt > 10) {
            setDisabledButtonRegFabri(true);
        } else {
            setDisabledButtonRegFabri(false);
        }

    }

    /**
     * PERIODO USO MES DE INICIO DE LA RG TAMBIEN PARA FABRICA
     */
    public void mostrarPeriodoUso() {
        if (libroMes != null) {
            if (libroMes.getId() != null) {
                return;
            }
        }

        String anios = JsfUtil.obtenerFechaXCriterio(new Date(), 1);
        String mesL = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 2);
        for (TipoBaseGt it : tipoBaseFacade.lstTipoBase("TP_MES")) {
            if (Integer.valueOf(mesL).equals(it.getOrden())) {
                libroMes.setMesId(it);
                libroMes.setLibromAnio(Long.parseLong(anios));
                break;
            }

        }

    }

    /**
     * comercio
     */
    public void mostrarPeriodoComer() {
        if (libroMesComercio != null) {
            if (libroMesComercio.getId() != null) {
                return;
            }
        }

        String anios = JsfUtil.obtenerFechaXCriterio(new Date(), 1);
        String mesL = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 2);
        for (TipoBaseGt it : tipoBaseFacade.lstTipoBase("TP_MES")) {
            if (Integer.valueOf(mesL).equals(it.getOrden())) {
                libroMesComercio.setMesId(it);
                libroMesComercio.setLibromAnio(Long.parseLong(anios));
                break;
            }

        }

    }

    /**
     * FABRICA
     */
    public void mostrarPeriodoFabrica() {
        String anios = JsfUtil.obtenerFechaXCriterio(new Date(), 1);
        String mesL = JsfUtil.obtenerFechaXCriterio(new Date(), 2);
        for (TipoBaseGt it : tipoBaseFacade.lstTipoBase("TP_MES")) {
            if (Integer.valueOf(mesL).equals(it.getOrden())) {
                libroMesFabrica.setMesId(it);
                libroMesFabrica.setLibromAnio(Long.parseLong(anios));
                break;
            }

        }

    }

    /**
     * Evento para guardar la información al editar un libroMes.
     *
     */
    public void editarLibroMesFabrica() {
        List<EppLibroUsoDiario> listEppLibroUsoDiarioTemporal = new ArrayList<>();

        if (tipoFinalidad == null) {
            JsfUtil.mensajeAdvertencia("Seleccione una finalidad");
            return;
        }
        switch (tipoFinalidad.getCodProg()) {
            case "TP_LIBFAB_FAB":

                if (listEppLibroMaterialUtilizadoFa.isEmpty()) {
                    JsfUtil.mensajeAdvertencia("Ingresar material utilizado");
                    return;
                }

                if (listEppLibroExploFabricadosFa.isEmpty()) {
                    JsfUtil.mensajeAdvertencia("Ingresar explosivos fabricados");
                    return;
                }
                break;
            case "TP_LIBFAB_DES":
                if (listEppLibroExploFabricadosFaDestruccion.isEmpty()) {
                    JsfUtil.mensajeAdvertencia("Ingresar explosivos fabricados");
                    return;
                }

                break;
            case "TP_LIBFAB_REP":
                if (listEppLibroExploFabricadosFaReproce.isEmpty()) {
                    JsfUtil.mensajeAdvertencia("Ingresar explosivos fabricados");
                    return;
                }

                break;

        }

        try {
            libroMesFabrica = (EppLibroMes) JsfUtil.entidadMayusculas(libroMesFabrica, "");
            for (EppLibroUsoDiario it : listEppLibroExploFabricadosFa) {
                if (it.getId() < 0) {
                    it.setId(null);
                    listEppLibroUsoDiarioTemporal.add(it);
                    //libroMesFabrica.getEppLibroUsoDiarioList().add(it);

                }
            }

            for (EppLibroUsoDiario it : listEppLibroMaterialUtilizadoFa) {
                if (it.getId() < 0) {
                    it.setId(null);
                    listEppLibroUsoDiarioTemporal.add(it);
                    //libroMesFabrica.getEppLibroUsoDiarioList().add(it);

                }
            }

            for (EppLibroUsoDiario it : listEppLibroExploFabricadosFaReproce) {
                if (it.getId() < 0) {
                    it.setId(null);
                    listEppLibroUsoDiarioTemporal.add(it);
                    // libroMesFabrica.getEppLibroUsoDiarioList().add(it);
                }
            }

            for (EppLibroUsoDiario it : listEppLibroMaterialUtilizadoExploRepro) {
                if (it.getId() < 0) {
                    it.setId(null);
                    listEppLibroUsoDiarioTemporal.add(it);
                    //libroMesFabrica.getEppLibroUsoDiarioList().add(it);
                }
            }

            for (EppLibroUsoDiario it : listEppLibroExploFabricadosFaDestruccion) {
                if (it.getId() < 0) {
                    it.setId(null);
                    listEppLibroUsoDiarioTemporal.add(it);
                    //libroMesFabrica.getEppLibroUsoDiarioList().add(it);
                }
            }

            switch (libroMesFabrica.getTipoEstado().getCodProg()) {
                case "TP_REGEV_OBS":
                    libroMesFabrica.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
                    break;
            }
            libroMesFabrica.setEppLibroUsoDiarioList(new ArrayList<EppLibroUsoDiario>());
            libroMesFabrica.setEppLibroUsoDiarioList(listEppLibroUsoDiarioTemporal);
            eppLibroMesFacade.edit(libroMesFabrica);
            limpiarValoExplosivos();

            cantidad = null;
            eppPlantaExplosivo = null;
            unidadMedidaMateRela = null;
            nroLote = null;

            estado = EstadoCrud.BUSCAR;
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        if (listaUpdateActivosFabrica.size() > 0) {
            for (EppLibroUsoDiario it : listaUpdateActivosFabrica) {
                it.setActivo(JsfUtil.FALSE);
                eppLibroUsoDiarioFacade.edit(it);
            }
        }

    }

    /**
     * FABRICA APERTURAR EL SGTE MES
     *
     * @param lmes
     */
    public void aperturarSgteMesFabrica(EppLibroMes lmes) {
        EppLibroMes libroMesLocal = new EppLibroMes();
        libroMesLocal = (EppLibroMes) JsfUtil.entidadMayusculas(libroMesLocal, "");

        libroMesLocal = settMesFabrica(lmes);

        libroMesLocal.setActivo(JsfUtil.TRUE);
        libroMesLocal.setUsoExplosivo(JsfUtil.TRUE);
        libroMesLocal.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        libroMesLocal.setAudNumIp(JsfUtil.getIpAddress());
        EppLibro libro = lmes.getLibroId();
        libro.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_LIBEVT_APE").get(0));
        libro.setEppLibroMesList(new ArrayList<EppLibroMes>());

        String anios = JsfUtil.obtenerFechaXCriterio(new Date(), 1);
        String messActual = JsfUtil.obtenerFechaXCriterio(new Date(), 2);
        //libroMesLocal.setLibromOrden(new Long(messActual));

        libroMesLocal.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBRO_FAB"));
        if (lmes.getTipoRegistroId().getCodProg().equals("TP_LIBRO_COM")) {
            libroMesLocal.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBRO_COM"));
            libroMesLocal.setEppLibroUsoDiarioList(new ArrayList<EppLibroUsoDiario>());
            try {
                for (EppLibroUsoDiario it : lmes.getEppLibroUsoDiarioList()) {
                    it.setId(null);
                    it.setLibroMesId(libroMesLocal);
                    it.setCantidad(0.0);
                }
                libroMesLocal.setEppLibroUsoDiarioList(lmes.getEppLibroUsoDiarioList());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        libroMesLocal.setLibroId(libro);
        //libroMesLocal.setLibromAnio(Long.parseLong(anios));

        libro.getEppLibroMesList().add(libroMesLocal);
        eppLibroFacade.edit(libro);
        libroMesLocal = new EppLibroMes();
        switch (lmes.getTipoRegistroId().getCodProg()) {
            case "TP_LIBRO_FAB":
                menuLibroFabrica();
                break;
            case "TP_LIBRO_COM":
                menuLibroComercio();
                break;

        }

        estado = EstadoCrud.BUSCAR;

    }

    /**
     * Evento para guardar la información al crear un nuevo libroMes.
     *
     */
    public void crearLibroMesFabrica() {
        try {
            libroMes = (EppLibroMes) JsfUtil.entidadMayusculas(libroMes, "");
            settLibroMes();
            switch (registroPlantaUsoComercio.getTipoProId().getCodProg()) {
                case "TP_PRTUP_GLO":
                case "TP_PRTUP_EVE":
                case "TP_PRTUP_EXC":
                case "TP_PRTUP_AAFOR":
                case "TP_PRTUP_AAHID":
                case "TP_PRTUP_AAPOR":
                    if (!settValoresComercio()) {
                        return;
                    }
                    libroMes.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBRO_USO"));
                    break;
                case "TP_PRTUP_ACOM":
                case "TP_PRTUP_COM":
                    if (!settValoresComercio()) {
                        return;
                    }

                    break;
                case "TP_PRTUP_PLA":
                case "TP_PRTUP_AFAB":
                    settValoresFabrica();
            }

            if (listEppLibroMaterialUtilizado != null) {
                for (EppLibroUsoDiario it : listEppLibroMaterialUtilizado) {
                    if (it.getId() < 0) {
                        it.setId(null);
                        libroMes.getEppLibroUsoDiarioList().add(it);
                    }
                }
            }

            eppLibroMesFacade.create(libroMes);
            switch (libroMes.getLibroId().getRegistroId().getTipoProId().getCodProg()) {
                case "TP_PRTUP_PLA":
                case "TP_PRTUP_AFAB":
                    menuLibroFabrica();
                    break;

            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado"));
            estado = EstadoCrud.BUSCAR;

        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * proceso aut uso (libro) metodo solo una tabla en el frm
     */
    public void crearAutoUsoLibro() {
        try {
//            libroMes = (EppLibroMes) JsfUtil.entidadMayusculas(libroMes, "");
//            settLibroMes();
//            libroMes.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBRO_USO")); // este tipo se guardaria en eppLibroUsoDiario
//            if (listEppLibroExploFabricados != null) {
//                for (EppLibroUsoDiario it : listEppLibroExploFabricados) {
//                    if (it.getId() < 0) {
//                        it.setId(null);
//                        libroMes.setEppLibroUsoDiarioList(listEppLibroExploFabricados);
//                    }
//                }
//            }
//
//            eppLibroMesFacade.create(libroMes);
            if (registroPlantaUsoComercio.getEppResolucion().getFechaIni().after(registroPlantaUsoComercio.getEppResolucion().getFechaFin())) {
                JsfUtil.mensajeAdvertencia("La resolucion tiene una Fecha de Inicio " + JsfUtil.formatoFechaDdMmYyyy(registroPlantaUsoComercio.getEppResolucion().getFechaIni())
                        + " mayor a la Fecha de Fin " + JsfUtil.formatoFechaDdMmYyyy(registroPlantaUsoComercio.getEppResolucion().getFechaFin()));
                return;
            }
            crearEpplibroMes2();
            listLibrosUso();
            labelLugaUso = null;
            estado = EstadoCrud.BUSCAR;

            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }

    }

    /**
     * SE CREA LIBRO MES SEGUN LOS MESES DE LA RESOLUCION REGISTROPLANTACOMERCIO
     */
    public void crearEpplibroMes() {
        try {
            EppLibroMes libroMess = new EppLibroMes();
            EppLibroMes libroMesTemporal = null;

            libroMess = (EppLibroMes) JsfUtil.entidadMayusculas(libroMess, "");
            String messIni = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 2);
            String messFin = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaFin(), 2);
            String anioResFin = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaFin(), 1);
            String mesNombre = null;
            String anioResIni = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 1);

            long tiempo = JsfUtil.getDiffDates(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), registroPlantaUsoComercio.getEppResolucion().getFechaFin(), 1);

            String anios = JsfUtil.obtenerFechaXCriterio(new Date(), 1);
            int nuevoMes = new Integer(messIni);
            int cont = 0;
            int contMes = 0;

            EppLibro libro = registroPlantaUsoComercio.getEppLibro();
            libro.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_LIBEVT_APE").get(0));
            libro.setEppLibroMesList(new ArrayList<EppLibroMes>());
            if (anioResIni.equals(anioResFin)) {
                for (int i = nuevoMes++; i <= new Integer(messFin); i++) {
                    libroMess.setActivo(JsfUtil.TRUE);
                    libroMess.setUsoExplosivo(JsfUtil.TRUE);
                    libroMess.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    libroMess.setAudNumIp(JsfUtil.getIpAddress());
                    if (cont == 0) {
                        libroMess.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
                    } else {
                        libroMess.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_PEN"));
                    }

                    if (registroPlantaUsoComercio.getEppLibro() == null) {
                        JsfUtil.mensajeAdvertencia("El lugar de Uso no tiene Asociado un Libro");
                        return;
                    }
                    libroMess.setEppLibroUsoDiarioList(null);
//                if (nuevoMes <= 12) {
//                    libroMess = settMesFabrica(messIni, new Integer(anios));
//                }
//
//                if (nuevoMes > 12) {
//                    libroMess = settMesFabrica(messIni, new Integer(anios) + 1);
//                }

                    //libroMess = settMesFabrica(messIni,nuevoMes);
                    int mesL = i;

                    for (TipoBaseGt it : tipoBaseFacade.lstTipoBase("TP_MES")) {
                        if (it.getOrden() == mesL) {
                            libroMess.setMesId(it);
                            libroMess.setLibromAnio(Long.parseLong(anios));
                            libroMess.setLibromOrden(it.getOrden().longValue());

                        }
                    }

                    libroMess.setLibroId(libro);
                    libroMess.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBRO_USO")); // este tipo se guardaria en eppLibroUsoDiario

                    cont++;
                    libro.getEppLibroMesList().add(libroMess);
                    libroMess = new EppLibroMes();
                }
            } else {

                for (int i = nuevoMes++; i <= tiempo; i++) {
                    libroMess.setActivo(JsfUtil.TRUE);
                    libroMess.setUsoExplosivo(JsfUtil.TRUE);
                    libroMess.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    libroMess.setAudNumIp(JsfUtil.getIpAddress());
                    if (cont == 0) {
                        libroMess.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
                    } else {
                        libroMess.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_PEN"));
                    }

                    if (registroPlantaUsoComercio.getEppLibro() == null) {
                        JsfUtil.mensajeAdvertencia("El lugar de Uso no tiene Asociado un Libro");
                        return;
                    }
                    libroMess.setEppLibroUsoDiarioList(null);
//                if (nuevoMes <= 12) {
//                    libroMess = settMesFabrica(messIni, new Integer(anios));
//                }
//
//                if (nuevoMes > 12) {
//                    libroMess = settMesFabrica(messIni, new Integer(anios) + 1);
//                }

                    //libroMess = settMesFabrica(messIni,nuevoMes);
                    int mesL = i;
                    int mesL2 = 1;

                    for (TipoBaseGt it : tipoBaseFacade.lstTipoBase("TP_MES")) {
                        if (it.getOrden() == mesL) {
                            libroMess.setMesId(it);
                            libroMess.setLibromAnio(Long.parseLong(anios));
                            libroMess.setLibromOrden(it.getOrden().longValue());

                        }
                    }

                    libroMess.setLibroId(libro);
                    libroMess.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBRO_USO")); // este tipo se guardaria en eppLibroUsoDiario

                    cont++;
                    libro.getEppLibroMesList().add(libroMess);
                    libroMess = new EppLibroMes();
                }

            }

            eppLibroFacade.edit(libro);
        } catch (Exception e) {
        }

    }

//    public void recorrerFechas(Date ini, Date fin) {
//        Calendar calendarInicio = Calendar.getInstance();
//        calendarInicio.setTime(ini);
//        int Calendar calendarFin = Calendar.getInstance();
//        calendarInicio.setTime(fin);
//
//        List<Calendar> listCa = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//
//        }
//
//    }
    public void crearEpplibroMes2() {
        if (registroPlantaUsoComercio.getEppLibro() == null) {
            JsfUtil.mensajeAdvertencia("El lugar de Uso no tiene Asociado un Libro");
            return;
        }
        try {
            EppLibroMes libroMess = new EppLibroMes();
            libroMess = (EppLibroMes) JsfUtil.entidadMayusculas(libroMess, "");
            String messIni = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 2);
            String messFin = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaFin(), 2);
            String anioResIni = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 1);
            String anioResFin = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaFin(), 1);
            long numerMesesPrimerAnio = 0;
            long numerMesesSegundoAnio = 0;

            if (anioResIni.equals(anioResFin)) {
                numerMesesPrimerAnio = JsfUtil.getDiffDates(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), registroPlantaUsoComercio.getEppResolucion().getFechaFin(), 1);
                numerMesesPrimerAnio += 2;
            }

            if (!anioResIni.equals(anioResFin)) {
                GregorianCalendar calendar = new GregorianCalendar(new Integer(anioResFin), 1, 1);
                Date date = calendar.getTime();
                numerMesesSegundoAnio = JsfUtil.getDiffDates(date, registroPlantaUsoComercio.getEppResolucion().getFechaFin(), 1);
                numerMesesSegundoAnio += 2;
            }

            String anios = JsfUtil.obtenerFechaXCriterio(new Date(), 1);
            int nuevoMes = new Integer(messIni);
            int nuevoMesNuevoAnio = 1;
            int cont = 0;

            EppLibro libro = registroPlantaUsoComercio.getEppLibro();
            libro.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_LIBEVT_APE").get(0));
            libro.setEppLibroMesList(new ArrayList<EppLibroMes>());
            if (anioResIni.equals(anioResFin)) {
                for (int i = nuevoMes++; i <= new Integer(messFin); i++) {
                    int mesL = i;
                    libroMess = settCamposLibroMes(libro, cont);
                    for (TipoBaseGt it : tipoBaseFacade.lstTipoBase("TP_MES")) {
                        if (it.getOrden() == mesL) {
                            libroMess.setMesId(it);
                            libroMess.setLibromAnio(Long.parseLong(anioResIni));
                            libroMess.setLibromOrden(it.getOrden().longValue());
                            break;
                        }
                    }

                    cont++;
                    libro.getEppLibroMesList().add(libroMess);
                    libroMess = new EppLibroMes();
                }

            }

            if (!anioResIni.equals(anioResFin)) {
                for (int i = nuevoMes++; i <= 12; i++) {
                    libroMess = settCamposLibroMes(libro, cont);
                    int mesL = i;
                    for (TipoBaseGt it : tipoBaseFacade.lstTipoBase("TP_MES")) {
                        if (it.getOrden() == mesL) {
                            libroMess.setMesId(it);
                            libroMess.setLibromAnio(Long.parseLong(anioResIni));
                            libroMess.setLibromOrden(it.getOrden().longValue());
                            break;
                        }
                    }
                    cont++;
                    libro.getEppLibroMesList().add(libroMess);
                    libroMess = new EppLibroMes();
                }

                for (int i = nuevoMesNuevoAnio++; i <= numerMesesSegundoAnio; i++) {
                    libroMess = settCamposLibroMes(libro, cont);
                    int mesL = i;
                    for (TipoBaseGt it : tipoBaseFacade.lstTipoBase("TP_MES")) {
                        if (it.getOrden() == mesL) {
                            libroMess.setMesId(it);
                            libroMess.setLibromAnio(Long.parseLong(anioResIni) + 1);
                            libroMess.setLibromOrden(it.getOrden().longValue());
                            break;
                        }
                    }
                    cont++;
                    libro.getEppLibroMesList().add(libroMess);
                    libroMess = new EppLibroMes();
                }

            }
            eppLibroFacade.edit(libro);
        } catch (Exception e) {
        }

    }

    /**
     * sett campos libro mes para uso (libro)
     *
     * @param epplibro
     * @param cont
     * @return
     */
    public EppLibroMes settCamposLibroMes(EppLibro epplibro, int cont) {
        EppLibroMes libroMess = new EppLibroMes();
        libroMess.setActivo(JsfUtil.TRUE);
        libroMess.setUsoExplosivo(JsfUtil.TRUE);
        libroMess.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        libroMess.setAudNumIp(JsfUtil.getIpAddress());
        libroMess.setEppLibroUsoDiarioList(null);
        libroMess.setLibroId(epplibro);
        libroMess.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBRO_USO")); // este tipo se guardaria en eppLibroUsoDiario
        if (cont == 0) {
            libroMess.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
        } else {
            libroMess.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_PEN"));
        }

        return libroMess;
    }

//    public void prueba1() {
//        List<Long> arrayAnios = new ArrayList<>();
//        String anioResIni = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaIni(), 1);
//        int uno = new Integer("" + anioResIni);
//        String anioResFin = JsfUtil.obtenerFechaXCriterio(registroPlantaUsoComercio.getEppResolucion().getFechaFin(), 1);
//        int uno = new Integer("" + anioResIni);
//        for (int i = new Integer("" + anioResIni); i < = new Integer(anioResFin + ""); i++) {
//
//        }
//
//    }
    /**
     * CALCULA LOS MESES DE UNA RESOLUCION USO (LIBRO)
     *
     * @return
     */
    public Integer calcularMesesUso() {
        Date ini = registroPlantaUsoComercio.getEppResolucion().getFechaIni();
        Date fin = registroPlantaUsoComercio.getEppResolucion().getFechaFin();
        String mesIni = JsfUtil.obtenerFechaXCriterio(ini, 2);
        String mesFin = JsfUtil.obtenerFechaXCriterio(fin, 2);
        int cont = 0;
        for (int i = new Integer(mesIni); i <= new Integer(mesFin); i++) {
            cont++;
        }
        return cont;
    }

//    public boolean settValoresComercio() {
//        try {
//            libroMes.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBRO_COM"));
//            if (listEppLibroExploFabricados != null) {
//                for (EppLibroUsoDiario it : listEppLibroExploFabricados) {
//                    if (it.getEppLibroDetalleList() == null || it.getEppLibroDetalleList().isEmpty()) {
//                        JsfUtil.mensajeAdvertencia("Falta ingresar el detalle de Explosivo de Datos de Venta de " + it.getExplosivoId().getNombre());
//                        return false;
//                    }
//                }
//
//                for (EppLibroUsoDiario it : listEppLibroExploFabricados) {
//                    if (it.getId() < 0) {
//                        it.setId(null);
//                        it.setLibroMesId(libroMes);
//                        JsfUtil.borrarIds(it.getEppLibroDetalleList());
//                        libroMes.getEppLibroUsoDiarioList().add(it);
//                    }
//                }
//            }
//        } catch (Exception e) {
//        }
//        return true;
//    }
    public boolean settValoresComercio() {
        try {
            libroMesComercio.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBRO_COM"));
            if (listEppLibroExploFabricadosCo != null) {
                for (EppLibroUsoDiario it : listEppLibroExploFabricadosCo) {
                    if (it.getEppLibroDetalleList() == null || it.getEppLibroDetalleList().isEmpty()) {
                        JsfUtil.mensajeAdvertencia("Falta ingresar el detalle de Explosivo de Datos de Venta de " + it.getExplosivoId().getNombre());
                        return false;
                    }
                }

                for (EppLibroUsoDiario it : listEppLibroExploFabricadosCo) {
                    if (it.getId() < 0) {
                        it.setId(null);
                        it.setLibroMesId(libroMesComercio);
                        JsfUtil.borrarIds(it.getEppLibroDetalleList());
                        libroMesComercio.getEppLibroUsoDiarioList().add(it);
                    }
                    if (it.getId() > 0) {
                        for (EppLibroDetalle de : it.getEppLibroDetalleList()) {
                            if (de.getId() < 0) {
                                de.setId(null);
                            }
                        }
                        libroMesComercio.setEppLibroUsoDiarioList(listEppLibroExploFabricadosCo);
                    }
                }
            }
        } catch (Exception e) {
        }
        return true;
    }

    public void settValoresFabrica() {
        try {
            if (libroMes.getTipoRegistroId() == null) {
                libroMes.setTipoRegistroId(tipoLibroFabrica);
                if (listEppLibroExploFabricados != null) {
                    for (EppLibroUsoDiario it : listEppLibroExploFabricados) {
                        if (it.getId() < 0) {
                            it.setId(null);
                            it.setLibroMesId(libroMes);
                            libroMes.getEppLibroUsoDiarioList().add(it);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public EppLibro getEppLibro(Long id) {
        return eppLibroFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public EppLibroController() {
        estado = EstadoCrud.BUSCAR;
    }

    public String menuLibroFabrica() {

        String tipoRegistros = "TP_LIBFAB";
        listResolucionFabrica = new ArrayList();
        List<EppLibro> listLibros = new ArrayList<>();
        listTipoExplosivoGtEstados = tipoExplosivoFacade.lstTipoDocumentoAutorizacionUso(estados);
        listTipoExplosivoGtRegistro = tipoExplosivoFacade.lstTipoExplosivo(tipoRegistros);
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        SbPersonaGt sbpersonaEmpresa = sbPersonaFacadeGt.buscar(p_user.getNumDoc().trim()).get(0);
        for (EppLibro itLibro : eppRegistroFacade.listLibros(sbpersonaEmpresa.getId())) {
            switch (itLibro.getRegistroId().getTipoProId().getCodProg()) {
                case "TP_PRTUP_PLA":
                case "TP_PRTUP_AFAB":
                    listLibros.add(itLibro);
            }
        }

        for (EppLibro itLibro2 : listLibros) {
            try {
                EppLibroMes lm = eppRegistroFacade.listLibrosMes(itLibro2).get(0);
                if (itLibro2.equals(lm.getLibroId())) {
                    // validar que una rg solo se use una vez
                    listResolucionFabrica.add(itLibro2);
                }
            } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            }

        }
        listAnios = Arrays.asList(aniosBusqueda);
        resultados = null;
        resultadosFabrica = null;
        nroResolucion = null;
        tipoMenu = "fabrica";

        if (contador == 0) {
            validarFechaEnvioFabrica();
        }
        contador++;

        estado = EstadoCrud.BUSCAR;
        return "/aplicacion/libros/rgFabrica/List.xhtml";
    }

    /**
     * SOLO PARA FABRICACION , REPROCESAMIENTO , DESTRUCCION SOLO UNA VEZ PARA
     * VALIDA QUE LA RESOLUCION SEA USADA UNA VEZ
     *
     * @param lst
     */
    public void validarRgFabrica(List<EppLibroMes> lst) {
        for (EppLibroMes it : lst) {
            switch (tipoLibroFabrica.getCodProg()) {
                case "TP_LIBFAB_FAB":
                    switch (it.getTipoRegistroId().getCodProg()) {
                        case "TP_LIBFAB_COM":
                        case "TP_LIBFAB_AUT":
                        case "TP_LIBFAB_EXP":
                        case "TP_LIBFAB_PRO":
                            listRegistrosPlantaUsoComercio.remove(it.getLibroId().getRegistroId());

                    }
                    break;
                case "TP_LIBFAB_REP":
                    switch (it.getTipoRegistroId().getCodProg()) {
                        case "TP_LIBFAB_REP":
                            listRegistrosPlantaUsoComercio.remove(it.getLibroId().getRegistroId());
                    }

                    break;
                case "TP_LIBFAB_DES":
                    switch (it.getTipoRegistroId().getCodProg()) {
                        case "TP_LIBFAB_DES":
                            listRegistrosPlantaUsoComercio.remove(it.getLibroId().getRegistroId());
                    }

                    break;
            }

        }

    }

    /**
     * ajax mostrar direccion de planta
     */
    public void mostrarDirLocal() {
        if (eppLibro == null) {
            dirFabrica = null;
            return;
        }
        dirFabrica = eppLibro.getRegistroId().getEppLocalList().get(0).getDireccionCompleta();
    }

    /**
     * USO
     *
     * @return
     */
    public String menuLibroUso() {
        // String tipoRegistros = "TP_LIBFAB";
        listTipoExplosivoGtEstados = tipoExplosivoFacade.lstTipoDocumentoAutorizacionUso(estados);
        //listTipoExplosivoGtRegistro = tipoExplosivoFacade.lstTipoExplosivo(tipoRegistros);
        listLibrosUso();
        tipoMenu = "uso";
        listAnios = Arrays.asList(aniosBusqueda);
        resultados = null;
        estado = EstadoCrud.BUSCAR;
        nroResolucion = null;
        data = null;
        setDisabledPanExplRela(false);
        labelLugaUso = null;
        if (!verMensDlg) {

            validarFechaEnvioFabrica();

        }

        //setDisabledCabeceraEdit(false);
        return "/aplicacion/libros/rgAutorUso/List.xhtml";
    }

    /**
     * COMERCIO
     *
     * @return
     */
    public String menuLibroComercio() {
        listTipoExplosivoGtEstados = tipoExplosivoFacade.lstTipoDocumentoAutorizacionUso(estados);
        listResolucionUso = new ArrayList();
        List<EppLibro> listLibros = new ArrayList<>();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        SbPersonaGt sbpersonaEmpresa = sbPersonaFacadeGt.buscar(p_user.getNumDoc().trim()).get(0);
        for (EppLibro itLibro : eppRegistroFacade.listLibros(sbpersonaEmpresa.getId())) {
            switch (itLibro.getRegistroId().getTipoProId().getCodProg()) {
                case "TP_PRTUP_ACOM":
                case "TP_PRTUP_COM":
                    listLibros.add(itLibro);
            }
        }

        for (EppLibro itLibro2 : listLibros) {
            try {
                EppLibroMes lm = eppRegistroFacade.listLibrosMes(itLibro2).get(0);
                if (itLibro2.equals(lm.getLibroId())) {
                    listResolucionUso.add(itLibro2);
                }
            } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            }
        }
        if (contadorVe == 0) {
            validarFechaEnvioFabrica();
        }
        contadorVe++;
        listAnios = Arrays.asList(aniosBusqueda);
        tipoMenu = "venta";
        nroResolucion = null;
        anio = null;
        registroPlantaUsoComercio = null;
        resultadosComercio = null;
        estado = EstadoCrud.BUSCAR;
        return "/aplicacion/libros/rgComercio/List.xhtml";

    }

    /**
     * buscar libros de tipo uso menu
     */
    public void listLibrosUso() {
        listResolucionUso = new ArrayList();
        List<EppLibro> listLibros = new ArrayList<>();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        SbPersonaGt sbpersonaEmpresa = sbPersonaFacadeGt.buscar(p_user.getNumDoc().trim()).get(0);

        for (EppLibro itLibro : eppRegistroFacade.listLibros(sbpersonaEmpresa.getId())) {
            switch (itLibro.getRegistroId().getTipoProId().getCodProg()) {
                case "TP_PRTUP_GLO":
                case "TP_PRTUP_EVE":
                case "TP_PRTUP_EXC":
                case "TP_PRTUP_AAFOR":
                case "TP_PRTUP_AAHID":
                case "TP_PRTUP_AAPOR":
                    //case "TP_PRTUP_GUI":
                    listLibros.add(itLibro);
            }
        }

        for (EppLibro itLibro2 : listLibros) {
            try {
                EppLibroMes lm = eppRegistroFacade.listLibrosMes(itLibro2).get(0);
                if (itLibro2.equals(lm.getLibroId())) {
                    listResolucionUso.add(itLibro2);
                }
            } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            }

        }
    }

    public String textoConfirmacion() {

        if (selectLibroMes == null || selectLibroMes.isEmpty()) {
            JsfUtil.mensajeAdvertencia("Seleccione un Registro por favor");
            return null;
        }

        EppLibroMes me = selectLibroMes.get(0);
        if (!validarFecActualMayorFechaRegistro(me.getMesId().getOrden(), me)) {
            //  JsfUtil.mostrarMensajeDialog("No puede presentar(enviar) el registro de " + me.getMesId().getNombre() + " porque el mes actual es " + formatMes(new Date()), 1);
            return null;
        }

        if (me.getEppLibroUsoDiarioList() == null || me.getEppLibroUsoDiarioList().isEmpty()) {
            textoListVacia = "¿ El registro del mes de " + me.getMesId().getNombre() + " que intenta enviar no tiene ningun movimiento esta usted seguro ? ";
        } else {
            textoListVacia = "";
        }

        if (selectLibroMes.size() > 1) {
            JsfUtil.mensajeAdvertencia("Solo puede seleccionar un Registro");
            limpiarValorLista();
            return null;
        }
        // validar si se paso la fecha de emision
        libroMes = selectLibroMes.get(0);

        switch (libroMes.getTipoEstado().getCodProg()) {
            case "TP_REGEV_ENV":
            case "TP_REGEV_APRO":
            case "TP_REGEV_OBS":
            case "TP_REGEV_PEN":
                JsfUtil.mensajeAdvertencia("El registro no se puede Presentar por estar en estado " + libroMes.getTipoEstado().getNombre());
                libroMes = null;
                return null;
        }
        switch (libroMes.getTipoEstado().getCodProg()) {
            case "TP_REGEV_CRE":
            case "TP_REGEV_SUB":
                //validar¿?

                String resolucion = libroMes.getLibroId().getRegistroId().getEppResolucion().getNumero();
                String mesLibro = libroMes.getMesId().getNombre();
                String anios = "" + libroMes.getLibromAnio();

                //String numero = "" + resultados.getRowCount();
                String TipoRegistroEnvio = "";
                //String direccionFabrica = "";
                switch (libroMes.getTipoRegistroId().getCodProg()) {
                    case "TP_LIBFAB_DES":
                        TipoRegistroEnvio = "Destrucción";
                        // direccionFabrica = JsfUtil.ubicacionLocalPlanta(libroMes.getLibroId().getRegistroId());
                        break;
                    case "TP_LIBFAB_REP":
                        // direccionFabrica = JsfUtil.ubicacionLocalPlanta(libroMes.getLibroId().getRegistroId());
                        TipoRegistroEnvio = "Procesamiento";
                        break;
                    case "TP_LIBFAB_PRO":
                    case "TP_LIBFAB_EXP":
                    case "TP_LIBFAB_AUT":
                    case "TP_LIBFAB_COM":
                        //direccionFabrica = JsfUtil.ubicacionLocalPlanta(libroMes.getLibroId().getRegistroId());
                        TipoRegistroEnvio = "Fabricación";
                        break;
                    case "TP_LIBRO_USO":
                        TipoRegistroEnvio = "Lugar de Uso";
                        break;
                }

                valorTexto = "Usted va Transmitir el Registro del libro de " + TipoRegistroEnvio + " <br/>"
                        + " con el numero de resolución " + resolucion + " <br/>"
                        + "para el periodo de " + mesLibro + "_" + anios + "  <br/>"
                        + "con la sgte información <br/><br/>"
                        + "¿ confirma el envio los datos ?";

        }
        RequestContext.getCurrentInstance().execute("PF('ConfDialog').show()");
        libroMes = null;
        return valorTexto;

    }

    /**
     * EVALUA SI EL MESS DE REGISTRO ES MAYOR A LA DE FECHA ACTUAL TRUE CASO
     * CONTRARIO FALSE
     *
     * @param mesLibro
     * @param me
     * @return
     */
    public boolean validarFecActualMayorFechaRegistro(int mesLibro, EppLibroMes me) {
        Calendar feActual = Calendar.getInstance();
        int mesActual = feActual.get(Calendar.MONTH) + 1;
        int anio = feActual.get(Calendar.YEAR);

        if (me.getLibromAnio() == anio) {
            if (mesLibro > mesActual) {
                JsfUtil.mostrarMensajeDialog("No puede declarar / presentar / ver el registro de " + me.getMesId().getNombre() + " porque el mes actual es " + formatMes(new Date()), 1);
                //JsfUtil.mensajeAdvertencia("pruebaaaaaaaaaaaaaaaaaaaaaaaaa");
//            RequestContext.getCurrentInstance().update("buscarForm:miGrowl");
                return false;

            } else {

                return true;
            }

        }

        return true;

    }

    /**
     * MAPA DE OBSERVACIONES DE TRAZA
     *
     * @param nurExped
     */
    public void verObservaciones(String nurExped) {
        Expediente ex = expedienteFacade.buscarExpedienteXNumero(nurExped).get(0);
        listObservaciones = eppLibroMesFacade.listObsrTraza("" + ex.getIdExpediente());

        RequestContext.getCurrentInstance().execute("PF('obsDialog').show()");

    }

    /**
     * CAMBIA DE ESTADO DEL SGTE MES A CREADO CUANDO EL MES ANTERIOR PASA A
     * ENVIADO USO (LIBRO)
     *
     * @param lmes
     */
    public void aperturararSgteMes(EppLibroMes lmes) {
        long id = lmes.getId() + 1;
        switch (lmes.getMesId().getNombre()) {
            case "ENERO":
                for (EppLibroMes it : lmes.getLibroId().getEppLibroMesList()) {
                    if (it.getMesId().getNombre().equals("FEBRERO") && it.getId() == id) {
                        it.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                        eppLibroMesFacade.edit(it);
                        break;
                    }
                }
                break;
            case "FEBRERO":
                for (EppLibroMes it : lmes.getLibroId().getEppLibroMesList()) {
                    if (it.getMesId().getNombre().equals("MARZO") && it.getId() == id) {
                        it.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                        eppLibroMesFacade.edit(it);
                        break;
                    }
                }

                break;
            case "MARZO":
                for (EppLibroMes it : lmes.getLibroId().getEppLibroMesList()) {
                    if (it.getMesId().getNombre().equals("ABRIL") && it.getId() == id) {
                        it.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                        eppLibroMesFacade.edit(it);
                        break;
                    }
                }

                break;
            case "ABRIL":
                for (EppLibroMes it : lmes.getLibroId().getEppLibroMesList()) {
                    if (it.getMesId().getNombre().equals("MAYO") && it.getId() == id) {
                        it.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                        eppLibroMesFacade.edit(it);
                        break;
                    }
                }

                break;
            case "MAYO":
                for (EppLibroMes it : lmes.getLibroId().getEppLibroMesList()) {
                    if (it.getMesId().getNombre().equals("JUNIO") && it.getId() == id) {
                        it.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                        eppLibroMesFacade.edit(it);
                        break;
                    }
                }

                break;
            case "JUNIO":
                for (EppLibroMes it : lmes.getLibroId().getEppLibroMesList()) {
                    if (it.getMesId().getNombre().equals("JULIO") && it.getId() == id) {
                        it.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                        eppLibroMesFacade.edit(it);
                        break;
                    }
                }

                break;
            case "JULIO":
                for (EppLibroMes it : lmes.getLibroId().getEppLibroMesList()) {
                    if (it.getMesId().getNombre().equals("AGOSTO") && it.getId() == id) {
                        it.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                        eppLibroMesFacade.edit(it);
                        break;
                    }
                }

                break;
            case "AGOSTO":
                for (EppLibroMes it : lmes.getLibroId().getEppLibroMesList()) {
                    if (it.getMesId().getNombre().equals("SEPTIEMBRE") && it.getId() == id) {
                        it.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                        eppLibroMesFacade.edit(it);
                        break;
                    }
                }

                break;
            case "SEPTIEMBRE":
                for (EppLibroMes it : lmes.getLibroId().getEppLibroMesList()) {
                    if (it.getMesId().getNombre().equals("OCTUBRE") && it.getId() == id) {
                        it.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                        eppLibroMesFacade.edit(it);
                        break;
                    }
                }

                break;
            case "OCTUBRE":

                for (EppLibroMes it : lmes.getLibroId().getEppLibroMesList()) {
                    if (it.getMesId().getNombre().equals("NOVIEMBRE") && it.getId() == id) {
                        it.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                        eppLibroMesFacade.edit(it);
                        break;
                    }
                }

                break;
            case "NOVIEMBRE":

                for (EppLibroMes it : lmes.getLibroId().getEppLibroMesList()) {
                    if (it.getMesId().getNombre().equals("DICIEMBRE") && it.getId() == id) {
                        it.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                        eppLibroMesFacade.edit(it);
                        break;
                    }
                }

                break;
            case "DICIEMBRE":
                for (EppLibroMes it : lmes.getLibroId().getEppLibroMesList()) {
                    if (it.getMesId().getNombre().equals("ENERO") && it.getId() == id) {
                        it.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                        it.setLibromAnio(lmes.getLibromAnio() + 1);
                        eppLibroMesFacade.edit(it);
                        break;
                    }
                }

                break;

        }

    }

    /**
     * APERTURA EL SGTE MES DE PROC FABRICA
     *
     * @param lmes
     * @return
     */
    public EppLibroMes settMesFabrica(EppLibroMes lmes) {
        EppLibroMes libroMesLocal = new EppLibroMes();
        TipoBaseGt tpMes = null;
        switch (lmes.getMesId().getNombre()) {
            case "ENERO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_FEB").get(0);
                libroMesLocal.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(lmes.getLibromAnio());
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());

                break;

            case "FEBRERO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_MAR").get(0);
                libroMesLocal.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(lmes.getLibromAnio());
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;

            case "MARZO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_ABR").get(0);
                libroMesLocal.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(lmes.getLibromAnio());
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;

            case "ABRIL":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_MAY").get(0);
                libroMesLocal.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(lmes.getLibromAnio());
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;

            case "MAYO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_JUN").get(0);
                libroMesLocal.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(lmes.getLibromAnio());
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;
            case "JUNIO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_JUL").get(0);
                libroMesLocal.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(lmes.getLibromAnio());
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;
            case "JULIO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_AGO").get(0);
                libroMesLocal.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(lmes.getLibromAnio());
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;
            case "AGOSTO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_SEP").get(0);
                libroMesLocal.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(lmes.getLibromAnio());
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;

            case "SEPTIEMBRE":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_OCT").get(0);
                libroMesLocal.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(lmes.getLibromAnio());
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;
            case "OCTUBRE":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_NOV").get(0);
                libroMesLocal.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(lmes.getLibromAnio());
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;
            case "NOVIEMBRE":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_DIC").get(0);
                libroMesLocal.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(lmes.getLibromAnio());
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;
            case "DICIEMBRE":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_ENE").get(0);
                libroMesLocal.setTipoEstado(tipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
                libroMesLocal.setMesId(tpMes);
                long anioss = lmes.getLibromAnio() + 1;
                libroMesLocal.setLibromAnio(anioss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;

        }
        return libroMesLocal;
    }

    /**
     * APERTURA EL SGTE MES DE PROC FABRICA
     *
     * @param lmes
     * @param aniossss
     * @return
     */
    public EppLibroMes settMesFabrica(String lmes, long aniossss) {
        EppLibroMes libroMesLocal = new EppLibroMes();
        TipoBaseGt tpMes = null;
        switch (lmes) {

            case "ENERO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_ENE").get(0);

                libroMesLocal.setMesId(tpMes);

                libroMesLocal.setLibromAnio(aniossss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());

                break;

            case "FEBRERO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_FEB").get(0);

                libroMesLocal.setMesId(tpMes);

                libroMesLocal.setLibromAnio(aniossss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());

                break;

            case "MARZO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_MAR").get(0);
                libroMesLocal.setMesId(tpMes);

                libroMesLocal.setLibromAnio(aniossss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;

            case "ABRIL":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_ABR").get(0);

                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(aniossss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;

            case "MAYO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_MAY").get(0);

                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(aniossss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;

            case "JUNIO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_JUN").get(0);

                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(aniossss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;
            case "JULIO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_JUL").get(0);

                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(aniossss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;
            case "AGOSTO":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_AGO").get(0);

                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(aniossss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;
            case "SEPTIEMBRE":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_SEP").get(0);

                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(aniossss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;

            case "OCTUBRE":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_OCT").get(0);

                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(aniossss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;
            case "NOVIEMBRE":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_NOV").get(0);

                libroMesLocal.setMesId(tpMes);
                libroMesLocal.setLibromAnio(aniossss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;
            case "DICIEMBRE":
                tpMes = tipoBaseFacade.findByCodProg("TP_MES_ENE").get(0);
                libroMesLocal.setMesId(tpMes);
                long anioss = aniossss + 1;
                libroMesLocal.setLibromAnio(anioss);
                libroMesLocal.setLibromOrden(tpMes.getOrden().longValue());
                break;

        }
        return libroMesLocal;
    }

    /**
     * BUSCA LIBROS PROCESO FABRICA,USO,VENTA/COMERCIALIZACION
     */
    public void buscarLibros() {
        if (mensajeAdvertenciaGeneral()) {
            return;
        }
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        SbPersonaGt personaAdministrado = sbPersonaFacadeGt.obtenerEmpresaXRuc(p_user.getNumDoc());
        List<EppLibroMes> list = new ArrayList<>();
        String procesoComercios = "'TP_PRTUP_ACOM','TP_PRTUP_COM'";
        String procComer = "(" + procesoComercios + ")";
        String proceso = "'TP_PRTUP_PLA','TP_PRTUP_AFAB'";
        String procesoExplosivos = "(" + proceso + ")";
        String procesoUso = "'TP_PRTUP_GLO','TP_PRTUP_EVE','TP_PRTUP_EXC','TP_PRTUP_AAFOR','TP_PRTUP_AAHID','TP_PRTUP_AAPOR'";
        String procesoExplosivosUso = "(" + procesoUso + ")";
        switch (eppLibro.getRegistroId().getTipoProId().getCodProg()) {
            case "TP_PRTUP_GLO":
            case "TP_PRTUP_EVE":
            case "TP_PRTUP_EXC":
            case "TP_PRTUP_AAFOR":
            case "TP_PRTUP_AAHID":
            case "TP_PRTUP_AAPOR":
                if (anio == null) {
                    JsfUtil.mensajeAdvertencia("Seleccione un año");
                    limpiarVariablesBuscarLibros();
                    resultados = null;
                    return;
                }
                list = (eppLibroMesFacade.selectDataBandejaLibros(eppLibro, tiposFabricacion, tipoEstado, procesoExplosivosUso, personaAdministrado, mes, anio == null ? null : anio));
                validarAperturaLibro(list);
                resultados = new ListDataModel<>(list);
                break;

            case "TP_PRTUP_PLA":
            case "TP_PRTUP_AFAB":
                if (anio == null) {
                    JsfUtil.mensajeAdvertencia("Seleccione un año");
                    limpiarVariablesBuscarLibros();
                    resultadosFabrica = null;
                    return;
                }
//                if (eppLibro == null) {
//                    JsfUtil.mensajeAdvertencia("Seleccione una Resolución");
//                    limpiarVariablesBuscarLibros();
//                    resultadosFabrica = null;
//                    return;
//                }
//                if (tipoRegistro.getCodProg().equals("TP_LIBFAB_FAB")) {
//                    if (tiposFabricacion == null) {
//                        JsfUtil.mensajeAdvertencia("Seleccione un Tipo de Fabricación");
//                        return;
//                    }
//                    if (tiposFabricacion != null) {
//                        list = (eppLibroMesFacade.selectDataBandejaLibros(eppLibro, tiposFabricacion, tipoEstado, procesoExplosivos, personaAdministrado, mes, anio == null ? null : anio));
//                        resultadosFabrica = new ListDataModel<>(list);
//                    }
//                } else {
                list = (eppLibroMesFacade.selectDataBandejaLibros(eppLibro, tipoExplosivoFacade.findByCodProg("TP_LIBRO_FAB").get(0), tipoEstado, procesoExplosivos, personaAdministrado, mes, anio == null ? null : anio));
                resultadosFabrica = new ListDataModel<>(list);
                // }
                break;
            case "TP_PRTUP_ACOM":
            case "TP_PRTUP_COM":
                if (anio == null) {
                    JsfUtil.mensajeAdvertencia("Seleccione un año");
                    limpiarVariablesBuscarLibros();
                    resultadosComercio = null;
                    return;
                }
                list = (eppLibroMesFacade.selectDataBandejaLibros(eppLibro, tipoExplosivoFacade.findByCodProg("TP_LIBRO_COM").get(0), tipoEstado, procComer, personaAdministrado, mes, anio == null ? null : anio));
                resultadosComercio = new ListDataModel<>(list);
                break;
        }

        limpiarVariablesBuscarLibros();

    }

    /**
     * AJAX RENDER PANELES
     */
    public void renderPanel() {

    }

    /**
     * APERTURA UN LIBRO DESPUES QUE SE CIERRE EL MES ANTERIOR
     *
     * @param lst
     */
    public void validarAperturaLibro(List<EppLibroMes> lst) {
//        List lt = new ArrayList<>();
//        for (EppLibroMes it : lst) {
//            if (it.getTipoEstado().getCodProg().equals("TP_REGEV_FIN")) {
//                lt.add(it);
//            }
//        }
//        Collections.sort(lt);
//       // EppLibroMes mes = lt.get(lt.size() - 1);

    }

    public void renderSubTipoFabricacion() {
        if (tipoRegistro == null) {
            setRenderTiposFabricacion(false);
            return;
        }
        if (tipoRegistro.getCodProg().equals("TP_LIBFAB_FAB")) {
            setRenderTiposFabricacion(true);
            setTiposFabricacion(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_LIBFAB_COM"));
        } else {
            setRenderTiposFabricacion(false);
            setTipoFabricacion(null);
        }
    }

    public void limpiarVariablesBuscarLibros() {
        eppLibro = null;
        tipoRegistro = null;
        tiposFabricacion = null;
        mes = null;
        tipoEstado = null;
        anio = null;
        labelLugaUso = null;
        dirFabrica = null;
        setRenderTiposFabricacion(false);
    }

    /**
     * PROCESO LIBRO FABRICA
     */
    public void limpiarFabrica() {

    }

    /**
     * VALIDAR USER REMITENETE WEB SERVICE
     */
    public int validarUserRemitente(SbParametro sb) {
        Usuario userTramdoc = sbUsuarioFacadeGt.obtenerUsuarioTramDoc(sb.getValor()).get(0);
        if (userTramdoc == null) {
            return -1;
        }

        if (userTramdoc.getEstado().equals('I')) {
            JsfUtil.mensajeAdvertencia("El usuario respomsable esta inactivo comuniquese con la SUCAMEC");
            return -1;
        }

        if (userTramdoc.getIdUsuario() != null) {
            return userTramdoc.getIdUsuario().intValue();
        }

        return userTramdoc.getIdUsuario().intValue();
    }

    public void presentarLibro() {

        EppLibroMes libroMesTemporal = null;
        SbParametro sbparametro = null;
        String expedienteId = null;
        Long idLIbroMes = null;
        String usuarioObser = null;
        boolean pasoTraza = false;
        short subsAntesDeEditar = 0;

        try {
            for (EppLibroMes itLibro : selectLibroMes) {
                String asunto = "";
                Integer usuaTramDoc = sbUsuarioFacadeGt.obtenerIdUsuarioTramDoc("USRWEB");//
                asunto = "Libro - " + itLibro.getMesId().getNombre() + " proceso " + itLibro.getLibroId().getRegistroId().getTipoProId().getNombre() + " para " + itLibro.getLibroId().getEmpresaId().getRznSocial()
                        + " con ruc " + itLibro.getLibroId().getEmpresaId().getRuc();

                if (itLibro.getNroExpediente() == null) {
                    if (itLibro.getTipoEstado().getCodProg().equals("TP_REGEV_CRE")) {
                        // VWERIFICAR WEB SERVICE
                        // USUR REMITENTE SEGUN PROCESO
                        switch (itLibro.getTipoRegistroId().getCodProg()) {
                            case "TP_LIBRO_FAB":
                            case "TP_LIBRO_COM":
                                sbparametro = sbParametroFacade.obtenerParametroXNombre(JsfUtil.bundle("userDestinoLibrosCoordinadorFabrica"));
                                if (sbparametro == null) {
                                    JsfUtil.mensajeAdvertencia("El valor de sbParametro no se existe"); // cuando el usario no existe
                                    return;
                                }
                                int idUserRemitente = validarUserRemitente(sbparametro);
                                if (idUserRemitente == -1) {
                                    return;
                                }
                                expedienteId = wsTramDocController.crearExpediente_libro(null, itLibro.getLibroId().getEmpresaId(),
                                        asunto, 757, usuaTramDoc, idUserRemitente);// proc libro ivan 757 / prueba 751
                                break;
                            case "TP_LIBRO_USO":
                                sbparametro = sbParametroFacade.obtenerParametroXNombre(JsfUtil.bundle("userDestinoLibrosCoordinadorUso"));
                                if (sbparametro == null) {
                                    JsfUtil.mensajeAdvertencia("El valor de sbParametro no se existe"); // cuando el usario no existe
                                    return;
                                }
                                int idUserRemitente2 = validarUserRemitente(sbparametro);
                                if (idUserRemitente2 == -1) {
                                    return;
                                }
                                expedienteId = wsTramDocController.crearExpediente_libro(null, itLibro.getLibroId().getEmpresaId(),
                                        asunto, 757, usuaTramDoc, idUserRemitente2);// proc libro ivan 757 / prueba 751
                                break;
                        }
                        if (expedienteId == null) {
                            JsfUtil.mensajeAdvertencia("Error de Web Service");
                            return;
                        }
                        itLibro.setNroExpediente(expedienteId);

                    }
                }
                itLibro.setFechaCierre(new Date());
                idLIbroMes = itLibro.getId();

                /* SOLO CUANDO ES OBSERVADO BUSCAR EL USUARIO QUE OBSERVO EL REGISTRO Y DERIVARLO A EL MISMO
                 BUSCO EL USAURIO subsAntesDeEditar VARIABLE PRA USAR MAS ABAJO
                 */
                if (itLibro.getSubsanado() == 1) {
                    subsAntesDeEditar = 1;
                    if (itLibro.getNroExpediente() != null) {
                        usuarioObser = loginUsuarioRemiteTraza(itLibro.getNroExpediente());
                    }
                }

                /*  SI ES == 1 (OBSERVADO) PASA POR LA TRAZA  */
                if (itLibro.getSubsanado() == 1) {
                    pasoTraza = registraTrazaExpediente(itLibro, usuarioObser, subsAntesDeEditar);
                    if (!pasoTraza) {
                        return;
                    }
                }
                itLibro.setSubsanado(JsfUtil.FALSE);// 0
                itLibro.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_ENV"));

                eppLibroMesFacade.edit(itLibro);
                try {
                    switch (itLibro.getTipoEstado().getCodProg()) {
                        case "TP_REGEV_ENV":
                            switch (itLibro.getTipoRegistroId().getCodProg()) {
                                case "TP_LIBRO_FAB":
                                    if (subsAntesDeEditar == 0) {
                                        aperturarSgteMesFabrica(itLibro);
                                    }
                                    break;
                                case "TP_LIBRO_USO":
                                    if (subsAntesDeEditar == 0) {
                                        aperturararSgteMes(itLibro);
                                    }
                                    break;
                                case "TP_LIBRO_COM":
                                    if (subsAntesDeEditar == 0) {
                                        aperturarSgteMesFabrica(itLibro);
                                    }

                                    break;

                            }

                    }

                } catch (Exception e) {
                    /* VALIDAR SI SE CAE APERTURAR EL SGTE MES
                    if (pasoTraza) {
                        EppLibroMes lmes = eppLibroMesFacade.find(idLIbroMes);
                        lmes.setSubsanado(JsfUtil.TIENETRAZA); // 2
                        itLibro.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
                        eppLibroMesFacade.edit(lmes);
                    }
                     */
                    JsfUtil.mensajeAdvertencia("No se pudo aperturar el siguiente mes");
                    return;

                }

            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            if (expedienteId != null) {
                EppLibroMes libMes = eppLibroMesFacade.find(idLIbroMes);
                libMes.setNroExpediente(expedienteId);
                eppLibroMesFacade.edit(libMes);
            }

            if (pasoTraza) {
                EppLibroMes lmes = eppLibroMesFacade.find(idLIbroMes);
                lmes.setSubsanado(JsfUtil.TIENETRAZA); // 2
                eppLibroMesFacade.edit(lmes);
            }

            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
            return;
        }

        // EppLibroMes libMes = selectLibroMes.get(0);
        selectLibroMes = null;
        resultados = null;
        resultadosFabrica = null;
    }

    /**
     * VALIDAR QUE ENVIO SEA EN EL MES CORRECTO
     *
     * @param lit
     * @return
     */
    public boolean validarFeEnvioUso(EppLibroMes lit) {
        Calendar fecha = Calendar.getInstance();
        int mesL = fecha.get(Calendar.MONTH) + 1;
        return lit.getMesId().getOrden() == mesL;
    }

    /**
     * BUSCA LA TRAZA ACTUAL Y EL ID REMITENTE PARA BUSCAR EL LOGIN DEL USUARIO
     *
     * @param numExpedie
     * @return
     */
    public String loginUsuarioRemiteTraza(String numExpedie) {
        Expediente idExpe = expedienteFacade.buscarExpedienteXNumero(numExpedie).get(0);
        List<Map> traza = eppLibroMesFacade.listObsrTraza("" + idExpe.getIdExpediente());
        Object ob = (traza.get(0).get("REM"));
        String id = "" + ob;
        return eppLibroMesFacade.loginUsuarioTramdoc(id).get(0);
    }
//    public String menuLibroAutoUso() {
//
//        //"'TP_PRTUP_GLO','TP_PRTUP_EVE','TP_PRTUP_EXC'"
//        String tipoRegistros = "TP_LIBFAB";
//        String estados = "'TP_REGEV_CRE','TP_REGEV_ENV','TP_REGEV_OBS','TP_REGEV_APRO'";
//        listResolucionFabrica = new ArrayList();
//        List<EppLibro> listLibros = new ArrayList<>();
//        listTipoExplosivoGtEstados = tipoExplosivoFacade.lstTipoDocumentoAutorizacionUso(estados);
//        listTipoExplosivoGtRegistro = tipoExplosivoFacade.lstTipoExplosivo(tipoRegistros);
//        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
//        SbPersonaGt sbpersonaEmpresa = sbPersonaFacadeGt.buscar(p_user.getNumDoc().trim()).get(0);
//        for (EppLibro itLibro : eppRegistroFacade.listLibros(sbpersonaEmpresa.getId())) {
//            switch (itLibro.getRegistroId().getTipoProId().getCodProg()) {
//                case "TP_PRTUP_GLO":
//                case "TP_PRTUP_EVE":
//                case "TP_PRTUP_EXC":
//                    listLibros.add(itLibro);
//            }
//        }
//
//        for (EppLibro itLibro2 : listLibros) {
//            try {
//                EppLibroMes lm = eppRegistroFacade.listLibrosMes(itLibro2).get(0);
//                if (itLibro2.equals(lm.getLibroId())) {
//                    listResolucionFabrica.add(itLibro2);
//                }
//            } catch (NullPointerException e) {
//            }
//
//        }
//
//        estado = EstadoCrud.BUSCAR;
//        resultados = null;
//        return "/aplicacion/libros/rgFabrica/List.xhtml";
//    }

    /**
     * USO
     *
     * @return
     */
    public String menuUso() {
        estado = EstadoCrud.BUSCAR;
        return "/aplicacion/libros/rgAutorUso/List.xhtml";
    }

    /**
     * USO
     */
    public void mostrarCrearUso() {

        libroMes = new EppLibroMes();
        listEppLibroExploFabricados = new ArrayList<>();
        listEppLibroDetalle = new ArrayList<>();
        cboListExplosivo = new ArrayList<>();
        listaUpdateActivos = new ArrayList<>();
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        listRegistrosPlantaUsoComercio = eppRegistroFacade.buscarResolucion(null, "(" + procesoAutUso + ")", p_user.getPersona().getId().toString());
        for (Iterator<EppRegistro> iterator = listRegistrosPlantaUsoComercio.iterator(); iterator.hasNext();) {
            EppRegistro next = iterator.next();
            if (next.getEppLibro() == null) {// traer todos y asignar un libro a registro si no tubiera
                iterator.remove();
            }

            if (next.getEppLibro() != null) {
                if (next.getEppLibro().getEppLibroMesList() != null) {
                    if (!next.getEppLibro().getEppLibroMesList().isEmpty()) {
                        iterator.remove();
                    }
                }
            }

        }
//        for (EppRegistro next : lst) {
//            if (next.getEppRegistroGuiaTransito() != null) {
//                if ((next.getEppRegistroGuiaTransito().getTipoTramite().getCodProg().equals("TP_RGUIA_ADQUI1")
//                        || next.getEppRegistroGuiaTransito().getTipoTramite().getCodProg().equals("TP_RGUIA_ADQUI2"))) {
//                    listRegistrosPlantaUsoComercio.add(next);
//                }
//            }
//        }
        libroMes.setEppLibroUsoDiarioList(new ArrayList<EppLibroUsoDiario>());
        for (EppLibroUsoDiario it : libroMes.getEppLibroUsoDiarioList()) {
            it.setEppLibroDetalleList(new ArrayList<EppLibroDetalle>());
        }
        //mostrarPeriodo();
        limpiarValoresUso();
        listaDiasMes = new ArrayList<>();
        //listaDias();
        setDiaUsoDiario(new Long(JsfUtil.obtenerFechaXCriterio(new Date(), 3)));
        setDisabledPanExplRela(false);
        setDisabledCabeceraEdit(false);
        labelLugaUso = null;
        setVerMensDlg(true);
        estado = EstadoCrud.CREAR;

    }

    /**
     * USO
     */
    public void ajaxDisabledGuiaTransito() {
        if (tipoUso == null) {
            setDisabledGuiaTransito(true);
            setDisabledAgregarUso(true);
            setDisabledPanExplRela(false);
            selectExplosivos = new ArrayList<>();
            return;
        }
        switch (tipoUso.getCodProg()) {
            case "TP_LIBUSO_DEV":
            case "TP_LIBUSO_USO":
            case "TP_LIBUSO_REI":
                setDisabledGuiaTransito(true);
                eppGuiaTransito = null;
                eppDetalleAutUso = null;
                setDisabledAgregarUso(false);
                setDisabledPanExplRela(false);
                setSaldoBol(true);
                setUniMedidaBol(true);
                break;
            case "TP_LIBUSO_ING":
                eppDetalleAutUso = null;
                cantidad = null;
                setDisabledGuiaTransito(false);
                setDisabledPanExplRela(true);
                setDisabledAgregarUso(true);
                setSaldoBol(true);
                setUniMedidaBol(true);
                break;

        }
        selectExplosivos = new ArrayList<>();
        saldo = null;
        cantidad = null;
        unidadMedidaExplo = null;
        setDiaUsoDiario(new Long(JsfUtil.obtenerFechaXCriterio(new Date(), 3)));
    }

    /**
     * USO
     */
    public void ajaxBuscarSaldo() {
        if (tipoUso == null) {
            JsfUtil.mensajeAdvertencia("Seleccione un Tipo de Movimiento");
            return;
        }
        switch (tipoUso.getCodProg()) {
            case "TP_LIBUSO_DEV":
            case "TP_LIBUSO_USO":
            case "TP_LIBUSO_REI":
                for (EppLibro it : listResolucionUso) {
                    for (EppLibroMes libMes : it.getEppLibroMesList()) {
                        for (EppLibroUsoDiario usoDiar : eppLibroUsoDiarioFacade.listLibroUsoDiario(libMes)) {
                            if (usoDiar.getExplosivoId().equals(cboExplosivo)) {
                                for (EppLibroDetalle deta : usoDiar.getEppLibroDetalleList()) {
                                    switch (deta.getTipoRegistroId().getCodProg()) {
                                        case "TP_LIBUSO_ING":
                                            saldoIngreso += deta.getCantidad() == 0.0 ? saldoIngreso : deta.getCantidad();
                                            break;
                                        case "TP_LIBUSO_USO":
                                            saldoUso += deta.getCantidad() == 0.0 ? saldoUso : deta.getCantidad();
                                            break;
                                        case "TP_LIBUSO_REI":
                                            saldoReingreso += deta.getCantidad() == 0.0 ? saldoReingreso : deta.getCantidad();
                                            break;
                                        case "TP_LIBUSO_DEV":
                                            saldoDevolucion += deta.getCantidad() == 0.0 ? saldoDevolucion : deta.getCantidad();
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
        }
        saldo = (saldoIngreso - saldoUso) + (saldoReingreso + saldoDevolucion);
        try {
            unidadMedidaExplo = cboExplosivo.getUnidadMedidaList().get(0).getNombre();
        } catch (NullPointerException e) {
        }

    }

    /**
     * validar ajaxBuscarSaldoProcAuUso
     *
     * @return
     */
    public boolean validarSaldoUso() {
        if (eppDetalleAutUso == null) {
            JsfUtil.mensajeAdvertencia("Seleccione un explosivo");
            return false;
        }
        if (tipoUso == null) {
            JsfUtil.mensajeAdvertencia("Seleccione un Tipo de Movimiento");
            eppDetalleAutUso = null;
            return false;
        }

        if (tipoUso.getCodProg().equals("TP_LIBUSO_ING")) {
            if (eppGuiaTransito == null) {
                JsfUtil.mensajeAdvertencia("ha seleccionado Tipo de movimiento ingreso usted nesecita una guia de transito para tener el saldo del explosivo");
                eppDetalleAutUso = null;
                return false;
            }
        }
        return true;
    }

    /**
     * BUSCAMOS PRIMERO EL SALDO EN BASE DE DATOS PROC LIBRO USO
     *
     * @param detaUso
     */
    public void saldoBaseDatosUso(EppDetalleAutUso detaUso) {
        List<EppLibroUsoDiario> listLibroUsoDiarioXexplosivoLibroMes = new ArrayList<>();
        listLibroUsoDiarioXexplosivoLibroMes = eppLibroUsoDiarioFacade.listLibroUsoDiarioXexplosivoLibroMes(detaUso.getExplosivoId(), null);
        listLibroUsoDiarioXexplosivoLibroMes.removeAll(listaUpdateActivos);

        List<EppLibroUsoDiario> listLibroUsoDiarioXexplosivo = new ArrayList<>();
        listLibroUsoDiarioXexplosivo = eppLibroUsoDiarioFacade.listLibroUsoDiarioXexplosivo(detaUso.getExplosivoId());
        listLibroUsoDiarioXexplosivo.removeAll(listaUpdateActivos);

        if (!listLibroUsoDiarioXexplosivoLibroMes.isEmpty()) {
            for (EppLibroUsoDiario it : listLibroUsoDiarioXexplosivo) {
                if (detaUso.getExplosivoId().equals(it.getExplosivoId())) {
                    switch (it.getTipoRegistroId().getCodProg()) {
                        case "TP_LIBUSO_ING":
                            saldoIngreso += it.getCantidad() == 0.0 ? saldoIngreso : it.getCantidad();
                            break;

                        case "TP_LIBUSO_USO":
                            saldoUso += it.getCantidad() == 0.0 ? saldoUso : it.getCantidad();
                            break;
                        case "TP_LIBUSO_REI":
                            saldoReingreso += it.getCantidad() == 0.0 ? saldoReingreso : it.getCantidad();
                            break;
                        case "TP_LIBUSO_DEV":
                            saldoDevolucion += it.getCantidad() == 0.0 ? saldoDevolucion : it.getCantidad();
                            break;

                    }

                }

            }

            if (tipoUso.getCodProg().equals("TP_LIBUSO_USO")) {
                // esta bien
                //saldoBaseDatos = (saldoIngreso - saldoUso) + (saldoReingreso);
                // probando formula
                saldoBaseDatos = ((saldoIngreso - saldoUso) + (saldoReingreso)) - (saldoDevolucion);
            }

            if (tipoUso.getCodProg().equals("TP_LIBUSO_REI")) {
                saldoBaseDatos = (saldoUso - saldoReingreso);
            }

            if (tipoUso.getCodProg().equals("TP_LIBUSO_DEV")) {
                // probando formula
                // saldoBaseDatos = ((saldoIngreso - saldoUso) + (saldoReingreso));
                // otra formula
                saldoBaseDatos = (saldoIngreso - saldoUso) + (saldoReingreso);
                saldoBaseDatos = saldoBaseDatos - saldoDevolucion;
            }
            saldoIngreso = 0.0;
            saldoUso = 0.0;
            saldoReingreso = 0.0;
            saldoDevolucion = 0.0;
        }

    }

    /**
     * SALDO EN MEMORIA + SALDO EN BASE DE DATOS NOS DA SALDO REAL (CALIENTE)
     * PROC LIBRO USO
     *
     * @param lisUsoDiarioTem
     * @return *
     */
    public List<EppLibroUsoDiario> removeSiyaEstaEnBaseDatos(List<EppLibroUsoDiario> lisUsoDiarioTem) {

        List<EppLibroUsoDiario> listLibroUsoDiarioXexplosivo = new ArrayList<>();
        listLibroUsoDiarioXexplosivo = eppLibroUsoDiarioFacade.listLibroUsoDiarioXexplosivo(eppDetalleAutUso.getExplosivoId());

        if (!listLibroUsoDiarioXexplosivo.isEmpty()) {
            for (Iterator<EppLibroUsoDiario> iterator = lisUsoDiarioTem.iterator(); iterator.hasNext();) {
                EppLibroUsoDiario next = iterator.next();
                if (next.getExplosivoId().equals(eppDetalleAutUso.getExplosivoId())) {
                    if (next.getId() > 0) {
                        if (next.getTipoRegistroId().getCodProg().endsWith("TP_LIBUSO_ING")) {
                            iterator.remove();
                        }
                        if (next.getTipoRegistroId().getCodProg().endsWith("TP_LIBUSO_USO")) {

                            iterator.remove();
                        }
                        if (next.getTipoRegistroId().getCodProg().endsWith("TP_LIBUSO_DEV")) {
                            iterator.remove();
                        }
                        //if (tipoUso.getCodProg().equals("TP_LIBUSO_USO")) {
                        if (next.getTipoRegistroId().getCodProg().endsWith("TP_LIBUSO_REI")) {
                            iterator.remove();
                        }
                        // }

                    }

                }
            }
        }
        return lisUsoDiarioTem;
    }

    /**
     * SALDO MEMORIA (CALIENTE) PROC LIBRO USO
     *
     * @param listTempo
     */
    public void saldoMemoria(List<EppLibroUsoDiario> listTempo) {
        for (EppLibroUsoDiario usoDiar : listTempo) {
            if (usoDiar.getExplosivoId().equals(eppDetalleAutUso.getExplosivoId())) {
                switch (usoDiar.getTipoRegistroId().getCodProg()) {
                    case "TP_LIBUSO_ING":
                        saldoIngreso += usoDiar.getCantidad() == 0.0 ? saldoIngreso : usoDiar.getCantidad();
                        break;
                    case "TP_LIBUSO_USO":
                        saldoUso += usoDiar.getCantidad() == 0.0 ? saldoUso : usoDiar.getCantidad();
                        break;
                    case "TP_LIBUSO_REI":
                        saldoReingreso += usoDiar.getCantidad() == 0.0 ? saldoReingreso : usoDiar.getCantidad();
                        break;
                    case "TP_LIBUSO_DEV":
                        saldoDevolucion += usoDiar.getCantidad() == 0.0 ? saldoDevolucion : usoDiar.getCantidad();
                        break;
                }

            }
        }
    }

    /**
     * saldo en caliente de explosivos proc AuUso no incluye ingreso / el saldo
     * de ingreso se trae de la guia transito
     */
    public void ajaxBuscarSaldoProcAuUso() {
        saldo = 0.0;
        saldoBaseDatos = 0.0;
        List<EppLibroUsoDiario> listUsoDiarioTemporal = new ArrayList<>();
        if (!validarSaldoUso()) {
            return;
        }

        switch (tipoUso.getCodProg()) {
            case "TP_LIBUSO_DEV":
            case "TP_LIBUSO_USO":
            case "TP_LIBUSO_REI":

                saldoBaseDatosUso(eppDetalleAutUso);

                listUsoDiarioTemporal.addAll(listEppLibroExploFabricados);

                listUsoDiarioTemporal = removeSiyaEstaEnBaseDatos(listUsoDiarioTemporal);

                saldoMemoria(listUsoDiarioTemporal);

                cantidad = 0.0;

                // saldo base datos + saldo memoria = saldo real (caliente)
                if (tipoUso.getCodProg().equals("TP_LIBUSO_REI")) {
                    saldo = (saldoBaseDatos - saldoReingreso);
                    saldo = saldo + saldoUso;
                }

                if (tipoUso.getCodProg().equals("TP_LIBUSO_USO")) {
                    //funciona
                    // saldo = (saldoIngreso - saldoUso) + (saldoReingreso) + saldoBaseDatos;
                    // probando formula
                    saldo = (((saldoIngreso - saldoUso) + (saldoReingreso)) - (saldoDevolucion)) + saldoBaseDatos;
                }

                if (tipoUso.getCodProg().equals("TP_LIBUSO_DEV")) {
                    // saldo = (((saldoIngreso - saldoUso) + (saldoReingreso)) + (saldoDevolucion)) + saldoBaseDatos;
                    saldo = (saldoIngreso - saldoUso) + (saldoReingreso);
                    saldo = saldo - saldoDevolucion;
                    saldo = saldo + saldoBaseDatos;
                }
                try {
                    unidadMedidaExplo = eppDetalleAutUso.getExplosivoId().getUnidadMedidaList().get(0).getNombre();
                } catch (NullPointerException e) {
                }

//            case "TP_LIBUSO_ING":
//
                saldoIngreso = 0.0;
                saldoUso = 0.0;
                saldoReingreso = 0.0;
                saldoDevolucion = 0.0;

        }
    }

    /**
     * BUSCA SALDO SEGUN LOS PARAMETROS USO (LIBRO)
     *
     * @param lst
     * @param ex
     * @return
     */
    public Double buscarSaldo2(List<EppLibroUsoDiario> lst, EppExplosivo ex) {
        saldoEdit = 0.0;
        double saldoReingresoTemporal = 0.0;
        List<EppLibroUsoDiario> listLibroUsoDiarioXexplosivo = eppLibroUsoDiarioFacade.listLibroUsoDiarioXexplosivo(ex);
        listLibroUsoDiarioXexplosivo.remove(usoDiarioDialog);
//        if (usoDiarioDialog.getId() > 0) {
//            saldoUsoTemporal = usoDiarioDialog.getCantidad();
//        }

        if (!eppLibroUsoDiarioFacade.listLibroUsoDiarioXexplosivoLibroMes(ex, null).isEmpty()) {
            for (EppLibroUsoDiario it : listLibroUsoDiarioXexplosivo) {
                if (ex.equals(it.getExplosivoId())) {
                    switch (it.getTipoRegistroId().getCodProg()) {
                        case "TP_LIBUSO_ING":
                            saldoIngreso += it.getCantidad() == 0.0 ? saldoIngreso : it.getCantidad();
                            break;

                        case "TP_LIBUSO_USO":
                            saldoUso += it.getCantidad() == 0.0 ? saldoUso : it.getCantidad();
                            break;
                        case "TP_LIBUSO_REI":
                            saldoReingreso += it.getCantidad() == 0.0 ? saldoReingreso : it.getCantidad();
                            break;
                        case "TP_LIBUSO_DEV":
                            saldoDevolucion += it.getCantidad() == 0.0 ? saldoDevolucion : it.getCantidad();
                            break;

                    }
                }

            }
            // saldoBaseDatos = (saldoIngreso - saldoUso) + (saldoReingreso - saldoDevolucion);
            if (usoDiarioDialog.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_USO")) {
                // funciona
                // saldoBaseDatos = (saldoIngreso - saldoUso) + (saldoReingreso);
                // probando formula
                saldoBaseDatos = ((saldoIngreso - saldoUso) + (saldoReingreso)) - (saldoDevolucion);

            }

            if (usoDiarioDialog.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_REI")) {
                saldoBaseDatos = (saldoUso - saldoReingreso);
                saldoReingresoTemporal = saldoReingreso;
            }
            if (usoDiarioDialog.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_DEV")) {
                // probando formula
                saldoBaseDatos = ((saldoIngreso - saldoUso) + (saldoReingreso));
            }
            saldoIngreso = 0.0;
            saldoUso = 0.0;
            saldoReingreso = 0.0;
            saldoDevolucion = 0.0;
        }

        lst.remove(usoDiarioDialog);

        if (!eppLibroUsoDiarioFacade.listLibroUsoDiarioXexplosivo(ex).isEmpty()) {
            for (Iterator<EppLibroUsoDiario> iterator = lst.iterator(); iterator.hasNext();) {
                EppLibroUsoDiario next = iterator.next();
                if (next.getExplosivoId().equals(ex)) {
                    if (next.getId() > 0) {
                        if (next.getTipoRegistroId().getCodProg().endsWith("TP_LIBUSO_ING")) {
                            iterator.remove();
                        }
                        if (next.getTipoRegistroId().getCodProg().endsWith("TP_LIBUSO_USO")) {
                            iterator.remove();
                        }
                        if (next.getTipoRegistroId().getCodProg().endsWith("TP_LIBUSO_DEV")) {
                            iterator.remove();
                        }
                        //if (tipoUso.getCodProg().equals("TP_LIBUSO_USO")) {
                        if (next.getTipoRegistroId().getCodProg().endsWith("TP_LIBUSO_REI")) {
                            iterator.remove();
                        }
                    }

                }
            }
        }

        for (EppLibroUsoDiario usoDiar : lst) {
            if (usoDiar.getExplosivoId().equals(ex)) {
                switch (usoDiar.getTipoRegistroId().getCodProg()) {
                    case "TP_LIBUSO_ING":
                        saldoIngreso += usoDiar.getCantidad() == 0.0 ? saldoIngreso : usoDiar.getCantidad();
                        break;
                    case "TP_LIBUSO_USO":
                        saldoUso += usoDiar.getCantidad() == 0.0 ? saldoUso : usoDiar.getCantidad();
                        break;
                    case "TP_LIBUSO_REI":
                        saldoReingreso += usoDiar.getCantidad() == 0.0 ? saldoReingreso : usoDiar.getCantidad();
                        break;
                    case "TP_LIBUSO_DEV":
                        saldoDevolucion += usoDiar.getCantidad() == 0.0 ? saldoDevolucion : usoDiar.getCantidad();
                        break;
                }

            }
        }

        //saldoEdit = (saldoIngreso - saldoUso) + (saldoReingreso - saldoDevolucion) + saldoBaseDatos;
        cantidad = 0.0;
        if (usoDiarioDialog.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_USO")) {
            saldoEdit = (saldoBaseDatos - saldoReingreso) + (saldoIngreso);
            //saldoEdit = saldoEdit + saldoUsoTemporal;
        }

        if (usoDiarioDialog.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_REI")) {
            saldoEdit = (saldoIngreso - saldoUso) + (saldoReingreso + saldoReingresoTemporal) + saldoBaseDatos;
        }
        if (usoDiarioDialog.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_DEV")) {
            saldoEdit = (((saldoIngreso - saldoUso) + (saldoReingreso)) + (saldoDevolucion)) + saldoBaseDatos;
        }

        saldoIngreso = 0.0;
        saldoUso = 0.0;
        saldoReingreso = 0.0;
        saldoDevolucion = 0.0;
        saldoBaseDatos = 0.0;
        return saldoEdit;
    }

    public void listadoSaldosUsoExplosivos() {
        if (eppLibro == null) {
            JsfUtil.mensajeAdvertencia("Seleccione un Lugar de Uso (LIBRO)");
            return;
        }
        listSaldoExplosivos = new ArrayList<>();
        SaldoExplosivo saldoExplosivo = new SaldoExplosivo();
        for (EppLibroMes libMes : eppLibro.getEppLibroMesList()) {
            for (EppLibroUsoDiario usoDiar : eppLibroUsoDiarioFacade.listLibroUsoDiario(libMes)) {
                switch (usoDiar.getTipoRegistroId().getCodProg()) {
                    case "TP_LIBUSO_ING":
                        saldoExplosivo.setExplosivo(usoDiar.getExplosivoId().getNombre());
                        saldoExplosivo.setSaldo("" + usoDiar.getCantidad());
                        saldoExplosivo.setPeriodo("" + usoDiar.getDia() + "-" + libMes.getLibromOrden() + "-" + libMes.getLibromAnio());
                        saldoExplosivo.setTipoRegistro(usoDiar.getTipoRegistroId().getNombre());
                        listSaldoExplosivos.add(saldoExplosivo);
                        saldoExplosivo = new SaldoExplosivo();
                        break;
                    case "TP_LIBUSO_USO":
                        saldoExplosivo.setExplosivo(usoDiar.getExplosivoId().getNombre());
                        saldoExplosivo.setSaldo("" + usoDiar.getCantidad());
                        saldoExplosivo.setPeriodo("" + usoDiar.getDia() + "-" + libMes.getLibromOrden() + "-" + libMes.getLibromAnio());
                        saldoExplosivo.setTipoRegistro(usoDiar.getTipoRegistroId().getNombre());
                        listSaldoExplosivos.add(saldoExplosivo);
                        saldoExplosivo = new SaldoExplosivo();
                        break;
                    case "TP_LIBUSO_REI":
                        saldoExplosivo.setExplosivo(usoDiar.getExplosivoId().getNombre());
                        saldoExplosivo.setSaldo("" + usoDiar.getCantidad());
                        saldoExplosivo.setPeriodo("" + usoDiar.getDia() + "-" + libMes.getLibromOrden() + "-" + libMes.getLibromAnio());
                        saldoExplosivo.setTipoRegistro(usoDiar.getTipoRegistroId().getNombre());
                        listSaldoExplosivos.add(saldoExplosivo);
                        saldoExplosivo = new SaldoExplosivo();
                        break;
                    case "TP_LIBUSO_DEV":
                        saldoExplosivo.setExplosivo(usoDiar.getExplosivoId().getNombre());
                        saldoExplosivo.setSaldo("" + usoDiar.getCantidad());
                        saldoExplosivo.setPeriodo("" + usoDiar.getDia() + "-" + libMes.getLibromOrden() + "-" + libMes.getLibromAnio());
                        saldoExplosivo.setTipoRegistro(usoDiar.getTipoRegistroId().getNombre());
                        listSaldoExplosivos.add(saldoExplosivo);
                        saldoExplosivo = new SaldoExplosivo();
                        break;
                }

            }
        }

    }

    /**
     * USO
     */
    public void settEppLibroDetalle() {
        for (EppLibroUsoDiario usoDiasrio : listEppLibroExploFabricados) {
            EppLibroDetalle eppLibroDetalle = new EppLibroDetalle();
            eppLibroDetalle.setActivo(JsfUtil.TRUE);
            eppLibroDetalle.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            eppLibroDetalle.setAudNumIp(JsfUtil.getIpAddress());
            if (tipoUso != null) {
                eppLibroDetalle.setTipoRegistroId(tipoUso);
                switch (tipoUso.getCodProg()) {
                    case "TP_LIBUSO_ING":
                        // eppLibroDetalle.setGuiaTransitoId(eppGuiaTransito);
                        break;

                }
            }

//            if (tipoDestinatario != null) {
//                eppLibroDetalle.setTipoRegistroId(tipoDestinatario);
//                eppLibroDetalle.setRegcompradorId(registroComprador);
//            }
            eppLibroDetalle.setLibroUsoDiarioId(usoDiasrio);
            eppLibroDetalle.setNroLote(usoDiasrio.getNroLote());
            eppLibroDetalle.setCantidad(usoDiasrio.getCantidad());
            try {
                eppLibroDetalle.setLugarUsoId(registroPlantaUsoComercio.getComId().getEppLugarUsoList().get(0));
            } catch (NullPointerException e) {
            }
            usoDiasrio.setEppLibroDetalleList(new ArrayList<EppLibroDetalle>());
            usoDiasrio.getEppLibroDetalleList().add(eppLibroDetalle);
            eppLibroDetalle = new EppLibroDetalle();
        }
    }

    /**
     * COMERCIO
     */
    public void agregarEppLibroDetalle() {
        if (tipoUso != null) {
            if (tipoUso.getCodProg().equals("TP_LIBUSO_ING") && eppGuiaTransito == null) {
                JsfUtil.mensajeAdvertencia("Para hacer un movimiento tipo Ingreso es Nesecario una Guia transito");
                return;
            }
        }

        if (cantidad == null) {
            JsfUtil.mensajeAdvertencia("Ingrese una Cantidad");
            return;
        }
        if (libroUsoDiario == null) {
            JsfUtil.mensajeAdvertencia("Seleccione un Explosivo");
            return;
        }

        for (EppLibroUsoDiario it : listEppLibroExploFabricadosCo) {

            if (libroUsoDiario.getExplosivoId().equals(it.getExplosivoId())) {
                EppLibroDetalle eppLibroDetalle = new EppLibroDetalle();
                eppLibroDetalle.setActivo(JsfUtil.TRUE);
                eppLibroDetalle.setId(JsfUtil.tempId());
                eppLibroDetalle.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                eppLibroDetalle.setAudNumIp(JsfUtil.getIpAddress());

                eppLibroDetalle.setRegcompradorId(registroComprador);
                eppLibroDetalle.setTipoRegistroId(tipoDestinatario);

                eppLibroDetalle.setNroLote(nroLote);
                eppLibroDetalle.setCantidad(cantidad);
                eppLibroDetalle.setFecha(fechaEpplibroDetalle(it.getLibroMesId()));
                eppLibroDetalle.setLibroUsoDiarioId(it);
                listEppLibroDetalle.add(eppLibroDetalle);
                if (it.getEppLibroDetalleList() == null) {
                    it.setEppLibroDetalleList(new ArrayList<EppLibroDetalle>());
////                    cantidadTotal = 0.0;
                }
                it.getEppLibroDetalleList().add(eppLibroDetalle);
//
//                cantidadTotal += cantidad;
                it.setCantidad(it.getCantidad() + cantidad);
                break;
            }

        }
        limpiarValoresDetalleComercio();
        limpiarValoresDetalleUso();
        // reset los filterBy de data table
        DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("editarForm:deExp");
        table.reset();
    }

    /**
     * SETT FECHA epplibroDetalle
     *
     * @param lmes
     * @return
     */
    public void agregarEppLibroDetalle2() {
        if (tipoUso != null) {
            if (tipoUso.getCodProg().equals("TP_LIBUSO_ING") && eppGuiaTransito == null) {
                JsfUtil.mensajeAdvertencia("Para hacer un movimiento tipo Ingreso es Nesecario una Guia transito");
                return;
            }
        }

        if (cantidad == null) {
            JsfUtil.mensajeAdvertencia("Ingrese una Cantidad");
            return;
        }

        if (libroUsoDiario != null) {
            EppLibroDetalle eppLibroDetalle = new EppLibroDetalle();
            eppLibroDetalle.setActivo(JsfUtil.TRUE);
            eppLibroDetalle.setId(JsfUtil.tempId());
            eppLibroDetalle.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            eppLibroDetalle.setAudNumIp(JsfUtil.getIpAddress());

            eppLibroDetalle.setRegcompradorId(registroComprador);
            eppLibroDetalle.setTipoRegistroId(tipoDestinatario);

            eppLibroDetalle.setNroLote(nroLote);
            eppLibroDetalle.setCantidad(cantidad);
            eppLibroDetalle.setFecha(fechaEpplibroDetalle(libroUsoDiario.getLibroMesId()));
            eppLibroDetalle.setLibroUsoDiarioId(libroUsoDiario);
            listEppLibroDetalle.add(eppLibroDetalle);
            if (libroUsoDiario.getEppLibroDetalleList() == null) {
                libroUsoDiario.setEppLibroDetalleList(new ArrayList<EppLibroDetalle>());
////                    cantidadTotal = 0.0;
            }
            libroUsoDiario.getEppLibroDetalleList().add(eppLibroDetalle);

            //libroMesComercio.getEppLibroUsoDiarioList().add(libroUsoDiario);
        }

//        limpiarValoresDetalleComercio();
//        limpiarValoresDetalleUso();
//        for (EppLibroUsoDiario it : listEppLibroExploFabricadosCo) {
//
//            if (cboExplosivo.equals(it.getExplosivoId())) {
//                EppLibroDetalle eppLibroDetalle = new EppLibroDetalle();
//                eppLibroDetalle.setActivo(JsfUtil.TRUE);
//                eppLibroDetalle.setId(JsfUtil.tempId());
//                eppLibroDetalle.setAudLogin(JsfUtil.getLoggedUser().getLogin());
//                eppLibroDetalle.setAudNumIp(JsfUtil.getIpAddress());
//
//                eppLibroDetalle.setRegcompradorId(registroComprador);
//                eppLibroDetalle.setTipoRegistroId(tipoDestinatario);
//
//                eppLibroDetalle.setNroLote(nroLote);
//                eppLibroDetalle.setCantidad(cantidad);
//                eppLibroDetalle.setFecha(fechaEpplibroDetalle(it.getLibroMesId()));
//                eppLibroDetalle.setLibroUsoDiarioId(it);
//                listEppLibroDetalle.add(eppLibroDetalle);
//                if (it.getEppLibroDetalleList() == null) {
//                    it.setEppLibroDetalleList(new ArrayList<EppLibroDetalle>());
//////                    cantidadTotal = 0.0;
//                }
//                it.getEppLibroDetalleList().add(eppLibroDetalle);
////
////                cantidadTotal += cantidad;
//                it.setCantidad(it.getCantidad() + cantidad);
//                break;
//            }
    }

    public Date fechaEpplibroDetalle(EppLibroMes lmes) {
        int anios = new Integer("" + lmes.getLibromAnio());
        int mesL = new Integer("" + lmes.getLibromOrden());
        int diaL = new Integer("" + diaVenDiarioEx);

        GregorianCalendar fec = new GregorianCalendar(anios, mesL - 1, diaL);
        Date newDate = fec.getTime();
        return newDate;

    }

    /**
     * ACTAULIZA LA CANTIDAD FABRICADA LIST listEppLibroExploFabricadosCo
     */
    public void actualizarCantidadFabricada() {
        for (EppLibroUsoDiario it : listEppLibroExploFabricadosCo) {
            if (it.getExplosivoId().equals(cboExplosivo)) {
                double cantiAntes = it.getCantidad();
                double cantidaActualizada = cantiAntes + cantidad;
                it.setCantidad(cantidaActualizada);
            }
        }

    }

    /**
     * USO
     */
    public void anularMultipleUso() {
        if ((selectLibroMes == null) || selectLibroMes.isEmpty()) {
            JsfUtil.mensajeAdvertencia("Seleccione un Registro");
            return;
        }

        try {
            for (EppLibroMes a : selectLibroMes) {
                if (!a.getTipoEstado().getCodProg().equals("TP_REGEV_CRE")) {
                    JsfUtil.mensajeAdvertencia("Solo puede Borrar los Registros en estado creado");
                    return;
                }
                a.setActivo((short) 0);
                eppLibroMesFacade.edit(a);

            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }

    }

    public void anularMultipleFabrica() {
        if ((selectLibroMes == null) || selectLibroMes.isEmpty()) {
            JsfUtil.mensajeAdvertencia("Seleccione un Registro");
            return;
        }

        try {
            for (EppLibroMes a : selectLibroMes) {
                if (!a.getTipoEstado().getCodProg().equals("TP_REGEV_CRE")) {
                    JsfUtil.mensajeAdvertencia("Solo puede Borrar los Registros en estado creado");
                    return;
                }
                a.setActivo((short) 0);
                eppLibroMesFacade.edit(a);
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }

    }

    /**
     * USO
     */
    public void settLibroMes() {
        libroMes.setActivo(JsfUtil.TRUE);
        libroMes.setUsoExplosivo(JsfUtil.TRUE);
        libroMes.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        libroMes.setAudNumIp(JsfUtil.getIpAddress());
        libroMes.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
        libroMes.setLibromOrden(new Long(libroMes.getMesId().getOrden()));
        if (registroPlantaUsoComercio.getEppLibro() == null) {
            JsfUtil.mensajeAdvertencia("El lugar de Uso no tiene Asociado un Libro");
            return;
        }
        libroMes.setLibroId(registroPlantaUsoComercio.getEppLibro());

    }

    public boolean mensajeAdvertenciaGeneral() {

        switch (tipoMenu) {
            case "fabrica":
                if (eppLibro == null) {
                    JsfUtil.mensajeAdvertencia("Seleccione una RG de Fabrica");
                    limpiarVariablesBuscarLibros();
                    resultadosFabrica = null;
                    return true;
                }
            case "uso":
                if (eppLibro == null) {
                    JsfUtil.mensajeAdvertencia("Seleccione un RG Adquisicion de Uso");
                    tipoEstado = null;
                    resultados = null;
                    return true;
                }
            case "venta":
                if (eppLibro == null) {
                    JsfUtil.mensajeAdvertencia("Seleccione una RG comercialización");
                    resultadosComercio = null;
                    return true;
                }
        }
        return false;
    }

    /**
     * USO
     */
    public void editarLibroMesUso() {

        try {
            libroMes = (EppLibroMes) JsfUtil.entidadMayusculas(libroMes, "");
            // ANTES
//            if (listEppLibroExploFabricados != null) {
//                if (!settValoresComercio()) {
//                    return;
//                }
//
//            }
            switch (libroMes.getTipoEstado().getCodProg()) {
                case "TP_REGEV_OBS":
                    libroMes.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
                    //setCorregido(true);
                    break;
                case "TP_REGEV_PEN":
                case "TP_REGEV_CRE":
                //setCorregido(false);
                //libroMes.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
            }
            for (EppLibroUsoDiario it : listEppLibroExploFabricados) {
                if (it.getId() < 0) {
                    it.setId(null);
                }
            }
            libroMes.setEppLibroUsoDiarioList(listEppLibroExploFabricados);
            eppLibroMesFacade.edit(libroMes);
            estado = EstadoCrud.BUSCAR;
            labelLugaUso = null;

            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        if (listaUpdateActivos.size() > 0) {
            for (EppLibroUsoDiario it : listaUpdateActivos) {
                it.setActivo(JsfUtil.FALSE);
                eppLibroUsoDiarioFacade.edit(it);
            }
        }
        // ANTES

//        if (listaUpdateActivosEppdetaDetalle.size() > 0) {
//            for (EppLibroDetalle de : listaUpdateActivosEppdetaDetalle) {
//                de.setActivo(JsfUtil.FALSE);
//                eppLibroDetalleFacade.edit(de);
//
//            }
//
//        }
    }

    /**
     *
     */
    public void limpiarValorLista() {
        selectLibroMes = null;

    }

    /**
     * COMERCIO
     */
    public void limpiarValoresDetalleComercio() {
        tipoDestinatario = null;
        comprador = null;
        destino = null;
        cantidad = null;
        unidadMedidaExplo = null;
        cboExplosivo = null;
        nroLote = null;
        libroUsoDiario = null;
    }

    /**
     * USO ver los saldos de libros de tipo adquisicion y uso
     *
     */
    public void verSaldos() {
        listLibrosUso();
        listSaldoExplosivos = null;
        estado = EstadoCrud.SALDOS;
    }

    public void regresarNavegacionSaldo() {
        estado = EstadoCrud.BUSCAR;

    }

    /**
     * COMERCIO
     */
    public void mostrarCrearVenta() {
        listEppLibroExploFabricadosCo = new ArrayList<>();
        libroMesComercio = new EppLibroMes();
        cantidadTotal = 0.0;
        libroMesComercio.setEppLibroUsoDiarioList(new ArrayList<EppLibroUsoDiario>());
        listRegistrosPlantaUsoComercio = new ArrayList<>();
        listEppLibroDetalle = new ArrayList<>();
        //listUsoDiario = new ArrayList<>();
        cboListExplosivo = new ArrayList<>();

        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        List<EppRegistro> lst = eppRegistroFacade.buscarResolucion(null, "(" + procesoComercio + ")", p_user.getPersona().getId().toString());
        for (Iterator<EppRegistro> iterator = lst.iterator(); iterator.hasNext();) {
            EppRegistro next = iterator.next();
            if (next.getEppLibro() == null) {
                iterator.remove();
            }

            if (next.getEppLibro() != null) {
                if (next.getEppLibro().getEppLibroMesList() != null) {
                    if (!next.getEppLibro().getEppLibroMesList().isEmpty()) {
                        iterator.remove();
                    }
                }
            }
        }
        for (EppRegistro it : lst) {
            if (it.getRegistroOpeId() != null) {
                listRegistrosPlantaUsoComercio.add(it);
            }
        }
        limpiarValoExplosivos();
        limpiarValoresVenta();
        //mostrarPeriodo();
        //mostrarPeriodoUso();
        //registroPlantaUsoComercio = null;
        estado = EstadoCrud.CREAR;
    }

    /**
     * COMERCIO
     */
    public void ajaxMostrarRgfabricaComercio() {
        if (registroPlantaUsoComercio == null) {
            listExplosivos = null;
            numeroResComercio = null;
            numeroResFabri = null;
            direccionFabrica = null;
            return;
        }
        listExplosivos = new ArrayList<>();
        numeroResComercio = registroPlantaUsoComercio.getEppResolucion().getNumero();
        numeroResFabri = registroPlantaUsoComercio.getRegistroOpeId().getEppResolucion().getNumero();
        direccionFabrica = registroPlantaUsoComercio.getRegistroOpeId().getEppLocalList().get(0).getDireccionCompleta();
        HashSet<EppExplosivo> lst = new HashSet<>();
        for (EppPlantaExplosivo ex1 : registroPlantaUsoComercio.getEppPlantaExplosivoList()) {
            lst.add(ex1.getExplosivoId());
        }

//        for (EppPlantaExplosivo ex2 : registroPlantaUsoComercio.getRegistroOpeId().getEppPlantaExplosivoList()) {
//            lst.add(ex2.getExplosivoId());
//        }
//        for (EppExplosivo ex3 : explosivoFacade.listExploInterSalidaXempresa(registroPlantaUsoComercio.getEmpresaId())) {
//            lst.add(ex3);
//        }
        //mostrarPeriodoComer();
        listExplosivos.addAll(lst);
        //listPlantaExplosivos = registroPlantaUsoComercio.getEppPlantaExplosivoList();
    }

    /**
     * LIMPIA LISTADO DE COMPRADORES PROC VENTA
     */
    public void limpiarTabla() {
        resultadosComprador = null;
        filtroComprador = null;

    }

    /**
     * LIMPIA LABEL DE COMPRADOR
     */
    public void limpiarComprador() {
        comprador = null;
        destino = null;
    }

    /**
     * COMERCIO
     */
    public void buscarComprador() {
        List<EppRegistro> lst;
        if (tipoDestinatario == null) {
            JsfUtil.mensajeAdvertencia("Seleccione un Tipo de Destinatorio");
            return;
        }
        switch (tipoDestinatario.getCodProg()) {
            case "TP_LIBCOM_USU":
                lst = (eppRegistroFacade.buscarResolucion(filtroComprador, "(" + procesoAutUso + ")", null));
                resultadosComprador = new ListDataModel<>(lst);
                break;
            case "TP_LIBCOM_FAB":
                lst = (eppRegistroFacade.buscarResolucion(filtroComprador, "(" + procesoPlanta + ")", null));
                resultadosComprador = new ListDataModel<>(lst);
                break;
            case "TP_LIBCOM_COM":
                lst = (eppRegistroFacade.buscarResolucion(filtroComprador, "(" + procesoComercio + ")", null));
                resultadosComprador = new ListDataModel<>(lst);
                break;
        }

        filtroComprador = null;

    }

    /**
     * COMERCIO
     */
    public void seleccionarComprador() {
        registroComprador = (EppRegistro) resultadosComprador.getRowData();
        try {
            switch (registroComprador.getTipoProId().getCodProg()) {

                case "TP_PRTUP_ACOM":
                case "TP_PRTUP_COM":
                case "TP_PRTUP_PLA":
                case "TP_PRTUP_AFAB":
                    destino = registroComprador.getEppLocalList().get(0).getDireccionCompleta();
                    break;
                case "TP_PRTUP_AAPOR":
                case "TP_PRTUP_EVE":
                case "TP_PRTUP_GLO":
                case "TP_PRTUP_EXC":
                case "TP_PRTUP_AAFOR":
                case "TP_PRTUP_AAHID":
                    destino = registroComprador.getComId().getEppLugarUsoList().get(0).getNombre();
            }

        } catch (NullPointerException e) {
        }
        comprador = registroComprador.getEmpresaId().getRznSocial() == null ? registroComprador.getEmpresaId().getNombreCompleto() : registroComprador.getEmpresaId().getRznSocial();

    }

    /**
     * COMERCIO
     */
    public void limpiarResultadosComprador() {
        resultadosComprador = null;
    }

    /**
     * VALIDAR FECHA DE ENVIO INFORMACION 10 DIAS HABILES COMTADOS A PARTIR DE
     * 01 DIA DEL MES
     *
     * @return
     */
    public void validarFechaEnvioFabrica() {
        fechaInicialCalendar = Calendar.getInstance();
        // obtengo el primer dia del mes
        int primerDiaMes = fechaInicialCalendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // fecha real
        int anioL = fechaRealCalendar.get(Calendar.YEAR);
        int mesL = fechaRealCalendar.get(Calendar.MONTH) + 1;
        int diaL = fechaRealCalendar.get(Calendar.DAY_OF_MONTH);

        // armo la fecha a evaluar
        Calendar fechaParametro = new GregorianCalendar(anioL, mesL, primerDiaMes);
        // averiguo que dia de la semana es
        int diaSemanaParametro = fechaParametro.get(Calendar.DAY_OF_WEEK);

        fechaFinalCalendar = Calendar.getInstance();
        fechaFinalCalendar.set(Calendar.DATE, 1);
        fechaInicialCalendar.set(Calendar.DATE, 1);
        Calendar fechaMesTexo = new GregorianCalendar(anioL, mesL - 2, diaL);
        SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
        String mesTexto = formatMes.format(fechaMesTexo.getTime());
        String mesIn = formatMes(fechaMesTexo);
        String mesFin = formatMes(fechaRealCalendar);

        int maxDias = 0;

        agregarDias(diaSemanaParametro);

        List<Calendar> listCalendar = validandoFeriados(fechaInicialCalendar, fechaFinalCalendar);
        fechaFinalCalendar.add(Calendar.DATE, numeroDeFeriadosHabiles(listCalendar));

        maxDias = fechaFinalCalendar.get(Calendar.DAY_OF_MONTH);

        if (diaL >= 1 && diaL <= maxDias) {
            if (diaL == maxDias) {
                JsfUtil.mostrarMensajeDialog("Hoy es el ultimo dia para enviar los libros del mes de " + mesTexto.toUpperCase(), 3);
                return;
            }
            if (diaL == maxDias) {
                return;
            }
            JsfUtil.mostrarMensajeDialog("La fecha maxima de envio del mes " + mesIn + " es el " + fechaFinalCalendar.get(Calendar.DATE) + " / " + mesFin + " / " + fechaFinalCalendar.get(Calendar.YEAR), 3);
        }

    }

//       public void validarFechaEnvioFabrica() {
//        fechaInicialCalendar = Calendar.getInstance();
//        // obtengo el primer dia del mes
//        int primerDiaMes = fechaInicialCalendar.getActualMinimum(Calendar.DAY_OF_MONTH);
//
//
//        int diaSemana = fechaRealCalendar.get(Calendar.DAY_OF_WEEK);
//        int anioL = fechaRealCalendar.get(Calendar.YEAR);
//        int mesL = fechaRealCalendar.get(Calendar.MONTH) + 1;
//        int diaL = fechaRealCalendar.get(Calendar.DAY_OF_MONTH);
//
//        // armo la fecha a evaluar
//        Calendar fechaParametro = new GregorianCalendar(anioL,mesL,primerDiaMes);
//        // averiguo que dia de la semana es
//        int diaSemanaParametro = fechaParametro.get(Calendar.DAY_OF_WEEK);
//
//
//
//        fechaFinalCalendar = Calendar.getInstance();
//        fechaFinalCalendar.set(Calendar.DATE, 1);
//        fechaInicialCalendar.set(Calendar.DATE, 1);
//        Calendar fechaMesTexo = new GregorianCalendar(anioL, mesL - 2, diaL);
//        SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
//        String mesTexto = formatMes.format(fechaMesTexo.getTime());
//        String mesIn = formatMes(fechaMesTexo);
//        String mesFin = formatMes(fechaRealCalendar);
//
//        int maxDias = 0;
//
//        agregarDias(diaSemanaParametro);
//
//        int diffDays = 0;
//
//        List<Calendar> listCalendar = validandoFeriados(fechaInicialCalendar, fechaFinalCalendar);
//        fechaFinalCalendar.add(Calendar.DATE, numeroDeFeriadosHabiles(listCalendar));
//
//        maxDias = fechaFinalCalendar.get(Calendar.DAY_OF_MONTH);
//        /*
//        while (fechaRealCalendar.before(fechaFinalCalendar) || fechaRealCalendar.equals(fechaFinalCalendar)) {
//            if (fechaRealCalendar.get(Calendar.DAY_OF_MONTH) == maxDias) {
//                break;
//            }
//            if (fechaRealCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
//                if (fechaRealCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
//                    if (!listCalendar.isEmpty()) {
//                        for (Calendar it : listCalendar) {
//                            if (fechaRealCalendar.get(Calendar.DAY_OF_MONTH) != it.get(Calendar.DAY_OF_MONTH)) {
//                                diffDays++;
//                            }
//                        }
//                    } else {
//                        diffDays++;
//                    }
//
//                }
//            }
//            //se suma 1 dia para hacer la validacion del siguiente dia.
//            fechaRealCalendar.add(Calendar.DATE, 1);
//
//        }
//         */
//
//        if (diaL >= 1 && diaL <= maxDias) {
//            if (diaL == maxDias - 1) {
//                JsfUtil.mostrarMensajeDialog("Hoy es el ultimo dia para enviar los libros del mes de " + mesTexto, 3);
//                return;
//            }
//            if (diaL == maxDias) {
//                return;
//            }
//            //JsfUtil.mensajeAdvertencia("Le Quedan " + diffDays + " dias habiles para enviar los libros del mes de " + mesTexto);
//            JsfUtil.mostrarMensajeDialog("La fecha maxima de envio del mes " + mesIn + " es el " + fechaFinalCalendar.get(Calendar.DATE) + " / " + mesFin + " / " + fechaFinalCalendar.get(Calendar.YEAR), 3);
//        }
//
//    }
    /**
     * FORMATO MES
     *
     * @param cal
     * @return
     */
    public String formatMes(Calendar cal) {
        SimpleDateFormat formatMesSS = new SimpleDateFormat("MMMM", new Locale("ES"));
        String messs = formatMesSS.format(cal.getTime());
        return messs = messs.substring(0, 1).toUpperCase() + messs.substring(1, messs.length());
    }

    /**
     * AGREGAR DIAS SEGUN CALENDARIO
     *
     * @param diaSema
     */
    public void agregarDias(int diaSema) {
        if (diaSema == Calendar.SUNDAY) {
            fechaFinalCalendar.add(Calendar.DATE, 12);
        }
        if (diaSema == Calendar.SATURDAY) {
            fechaFinalCalendar.add(Calendar.DATE, 13);
        }

        if (diaSema == Calendar.FRIDAY) {
            fechaFinalCalendar.add(Calendar.DATE, 13);
        }

        if (diaSema == Calendar.THURSDAY) {
            fechaFinalCalendar.add(Calendar.DATE, 14);
        }

        if (diaSema == Calendar.WEDNESDAY) {
            fechaFinalCalendar.add(Calendar.DATE, 13);
        }

        if (diaSema == Calendar.TUESDAY) {
            fechaFinalCalendar.add(Calendar.DATE, 13);
        }
        if (diaSema == Calendar.MONDAY) {
            fechaFinalCalendar.add(Calendar.DATE, 11);
        }

    }

    public int numeroDiasHabiles() {
        List<Calendar> listCalendar = validandoFeriados(fechaInicialCalendar, fechaFinalCalendar);
        fechaFinalCalendar.add(Calendar.DATE, numeroDeFeriadosHabiles(listCalendar));
        int maxDias = fechaFinalCalendar.get(Calendar.DAY_OF_MONTH);
        int diffDays = 0;
        while (fechaRealCalendar.before(fechaFinalCalendar) || fechaRealCalendar.equals(fechaFinalCalendar)) {
            if (fechaRealCalendar.get(Calendar.DAY_OF_MONTH) == maxDias) {
                break;
            }
            if (fechaRealCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                if (fechaRealCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    if (!listCalendar.isEmpty()) {
                        for (Calendar it : listCalendar) {
                            if (fechaRealCalendar.get(Calendar.DAY_OF_MONTH) != it.get(Calendar.DAY_OF_MONTH)) {
                                diffDays++;
                            }
                        }
                    } else {
                        diffDays++;
                    }

                }
            }
            //se suma 1 dia para hacer la validacion del siguiente dia.
            fechaRealCalendar.add(Calendar.DATE, 1);

        }
        return diffDays;
    }

    /**
     * ENCONTRANDO FERIADOS DE LA BASE DE DATOS
     *
     * @param fechaIni
     * @param fechaFin
     * @return
     */
    public List<Calendar> validandoFeriados(Calendar fechaIni, Calendar fechaFin) {
        Date fechaInicio = fechaIni.getTime();
        Date fechaFinal = fechaFin.getTime();
        List<Date> listaFeriadosXrangoFecha = sbFeriadoFacade.listFeriados(fechaInicio, fechaFinal);
        List<Calendar> listaFeriadosXrangoFechaCalendar = new ArrayList<>();
        for (Date date : listaFeriadosXrangoFecha) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            listaFeriadosXrangoFechaCalendar.add(calendar);
        }
        return listaFeriadosXrangoFechaCalendar;
    }

    /**
     * CONTAR FERIADOS HABILES
     *
     * @param listaFeriados
     * @return
     */
    public int numeroDeFeriadosHabiles(List<Calendar> listaFeriados) {
        int cont = 0;
        for (Calendar ca : listaFeriados) {
            if (ca.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    || ca.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                cont++;

            }
        }

        return cont;
    }

    /**
     * VENTA / COMERCIO
     */
    public void editarComercio() {
        try {

            for (EppLibroUsoDiario it : listEppLibroExploFabricadosCo) {
                for (EppLibroDetalle itDe : it.getEppLibroDetalleList()) {
                    if (itDe.getId() < 0) {
                        itDe.setId(null);
                    }
                }
            }

            switch (libroMesComercio.getTipoEstado().getCodProg()) {
                case "TP_REGEV_OBS":
                    libroMesComercio.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
                    break;
            }

            libroMesComercio.setEppLibroUsoDiarioList(listEppLibroExploFabricadosCo);

            eppLibroMesFacade.edit(libroMesComercio);
            resultadosComercio = null;
            estado = EstadoCrud.BUSCAR;
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }

//        if (listaUpdateActivosCom.size() > 0) {
//            for (EppLibroUsoDiario it : listaUpdateActivosCom) {
//                it.setActivo(JsfUtil.FALSE);
//                eppLibroUsoDiarioFacade.edit(it);
//            }
//        }
        if (listaUpdateActivosEppdetaDetalle.size() > 0) {
            for (EppLibroDetalle de : listaUpdateActivosEppdetaDetalle) {
                de.setActivo(JsfUtil.FALSE);
                eppLibroDetalleFacade.edit(de);

            }

        }
    }

    /**
     * llena automaticamente la tabla de explosivos cuando selecciona un lugar
     * de uso para el proceso Auto Uso (libros)
     *
     * @param re
     */
    public void llenarListExploAutUso(EppRegistro re) {
        listEppDetalleAutUso = re.getEppDetalleAutUsoList();

    }

    /**
     * carga los explosivos de la resolucion en el panel Explosivos o Material
     * Relacionado Vendidos proceso Aut Uso (Libros)
     */
    public void agregarExplosivosAutUso() {

        if (!validarImputUsoLibro()) {
            return;
        }

        switch (tipoUso.getCodProg()) {
            case "TP_LIBUSO_ING":
                agregarExplosGuiasAll();
                return;
            case "TP_LIBUSO_REI":
                if (cantidad == 0.0) {
                    JsfUtil.mensajeAdvertencia("Ingrese una cantidad");
                    return;
                }
                if (cantidad > saldo) {
                    JsfUtil.mensajeAdvertencia("la cantidad a reingresar es mayor al saldo");
                    return;
                }
            case "TP_LIBUSO_DEV":
            case "TP_LIBUSO_USO":
                if (cantidad == 0.0) {
                    JsfUtil.mensajeAdvertencia("Ingrese una cantidad");
                    return;
                }
                if (cantidad > saldo) {
                    JsfUtil.mensajeAdvertencia("No cuenta con saldo suficiente para el tipo de movimiento");
                    return;
                }

                EppLibroUsoDiario libroUsoDiario = new EppLibroUsoDiario();
                libroUsoDiario.setId(JsfUtil.tempId());
                libroUsoDiario.setActivo(JsfUtil.TRUE);
                libroUsoDiario.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                libroUsoDiario.setAudNumIp(JsfUtil.getIpAddress());
                libroUsoDiario.setDia(diaUsoDiario);
                libroUsoDiario.setNroLote(nroLote);
                libroUsoDiario.setCantidad(cantidad == null ? 0.0 : cantidad);
                libroUsoDiario.setExplosivoId(eppDetalleAutUso.getExplosivoId());
                libroUsoDiario.setUnidadMedidaId(eppDetalleAutUso.getExplosivoId().getUnidadMedidaList().get(0).getId());
                //libroUsoDiario.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_EXPP_EXP"));// antes se guardaba asi
                libroUsoDiario.setTipoRegistroId(tipoUso);
                libroUsoDiario.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
                libroUsoDiario.setLibroMesId(libroMes);
                if (tipoUso.getCodProg().equals("TP_LIBUSO_ING")) {
                    libroUsoDiario.setGuiaTransitoId(eppGuiaTransito);
                }
                listEppLibroExploFabricados.add(libroUsoDiario);
                libroUsoDiario = new EppLibroUsoDiario();
                limpiarValoresPanelExplMatRela();

        }
    }

    /**
     * BORRAR TODOS LOS EXPLOSIVOS LA GUIA DE TRANSITO EN REFERENCIA DE LA TABLA
     * EPP_LIBRO_USO _DIARIO (FRM CREATE TODO ESTA EN MEMORIA TODAVIA )
     */
    public void borrarExploDeGuiaAll() {
        if (eppGuiaTransito == null) {
            JsfUtil.mensajeAdvertencia("Seleccione una Guia de Transito");
            return;
        }
        if (eppGuiaTransito != null) {
            if (!eppLibroUsoDiarioFacade.listLibroUsoDiarioXguia(eppGuiaTransito).isEmpty()) {
                JsfUtil.mensajeAdvertencia("No puede Borrar los Explosivos de la guia " + eppGuiaTransito.getRegistroId().getEppResolucion().getNumero() + " porque ya fueron Ingresados a la Base de Datos");
                return;
            }
        }

        if (!listEppLibroExploFabricados.isEmpty()) {
            for (Iterator<EppLibroUsoDiario> iterator = listEppLibroExploFabricados.iterator(); iterator.hasNext();) {
                EppLibroUsoDiario next = iterator.next();
                if (next.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_ING")) {
                    if (next.getId() > 0) {
                        listaUpdateActivos.add(next);
                    }

                    if (next.getGuiaTransitoId().equals(eppGuiaTransito)) {
                        iterator.remove();
                    }
                }

            }
        } else {
            JsfUtil.mensajeAdvertencia("No hay explosivos en la lista");
        }
        eppGuiaTransito = null;
    }

    /**
     * AGREGA TODOS LOS EXPLOSIVOS DE GUIA TRANSITO EN EPP_LIBRO_USO_DIARIO
     * listEppLibroExploFabricados
     */
    public void agregarExplosGuiasAll() {

        if (eppGuiaTransito != null) {
            boolean flat = false;
            if (!eppLibroUsoDiarioFacade.listLibroUsoDiarioXguia(eppGuiaTransito).isEmpty()) {
                JsfUtil.mensajeAdvertencia("los Explosivos de la guia " + eppGuiaTransito.getRegistroId().getEppResolucion().getNumero() + " ya fueron Ingresados a la Base de Datos");
                return;

            }

        }

        for (EppLibroUsoDiario it : listEppLibroExploFabricados) {
            if (it.getGuiaTransitoId() != null) {
                if (it.getGuiaTransitoId().equals(eppGuiaTransito)) {
                    JsfUtil.mensajeAdvertencia("Los Explosivos de la Guia de Transito " + eppGuiaTransito.getRegistroId().getEppResolucion().getNumero() + " ya fueron Agregados ");
                    return;
                }
            }

        }

        for (EppExplosivoSolicitado it : eppGuiaTransito.getEppExplosivoSolicitadoList()) {
            EppLibroUsoDiario libroUsoDiario = new EppLibroUsoDiario();
            libroUsoDiario.setId(JsfUtil.tempId());
            libroUsoDiario.setActivo(JsfUtil.TRUE);
            libroUsoDiario.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            libroUsoDiario.setAudNumIp(JsfUtil.getIpAddress());
            libroUsoDiario.setDia(diaUsoDiario);
            libroUsoDiario.setNroLote(nroLote);
            libroUsoDiario.setCantidad(it.getCantidad());
            libroUsoDiario.setExplosivoId(it.getExplosivoId());
            libroUsoDiario.setUnidadMedidaId(it.getExplosivoId().getUnidadMedidaList().get(0).getId());
            //libroUsoDiario.setTipoRegistroId(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_EXPP_EXP"));// antes se guardaba asi
            libroUsoDiario.setTipoRegistroId(tipoUso);
            libroUsoDiario.setTipoEstado(tipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));
            libroUsoDiario.setLibroMesId(libroMes);
            if (tipoUso.getCodProg().equals("TP_LIBUSO_ING")) {
                libroUsoDiario.setGuiaTransitoId(eppGuiaTransito);
            }
            listEppLibroExploFabricados.add(libroUsoDiario);
            libroUsoDiario = new EppLibroUsoDiario();

        }
        tipoUso = null;
        eppGuiaTransito = null;

        //limpiarValoresPanelExplMatRela();
    }

    /**
     * valores de uso diario
     *
     * @param uso
     */
    public void cargarValores(EppLibroUsoDiario uso) {
        listaDiasMesEd = new ArrayList<>();

        usoDiarioDialog = uso;

//        idGuardado = null;
//        idGuardado = usoDiarioDialog.getId();
//        usoDiarioDialog.setId(null);
//        usoDiarioDialog.setId(JsfUtil.tempId());
//        List<EppLibroUsoDiario> listCopia = new ArrayList<>();
//        listCopia.addAll(listEppLibroExploFabricados);
//        buscarSaldo2(listCopia, usoDiarioDialog.getExplosivoId());
        diaUsoDiario = usoDiarioDialog.getDia();
        // cantidadEdit = usoDiarioDialog.getCantidad();
//        if (usoDiarioDialog.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_ING")) {
//            setDisabledCantida(true);
//        } else {
//            setDisabledCantida(false);
//        }
        listaDiasMesEd.addAll(listaDiasMes);
        limpiarValoresPanelExplMatRela();
    }

    /**
     * EDITA EXPLO PROC USO (LIBRO)
     */
    public void editarExploUso() {
        usoDiarioDialog.setDia(diaUsoDiario);

//        if (!usoDiarioDialog.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_ING")) {
//            if (cantidadEdit > saldoEdit) {
//                JsfUtil.mensajeAdvertencia("Usted no cuenta saldo suficiente para hacer este tipo de movimiento");
//                return;
//            }
//        }
        RequestContext.getCurrentInstance().execute("PF('ConfDialogExpl').hide()");
        //usoDiarioDialog.setCantidad(cantidadEdit);

//        //
//        cantidad = null;
//     diaUsoDiario
    }

    /**
     * CANCELA EDIT DE DLG DE EDIT DE EXPLO PORC USO (LIBRO)
     */
    public void cancelarEdit() {
        cantidad = null;

    }

    /**
     * VALIDANDO CANPOS REQUERIDOS
     *
     * @return
     */
    public boolean validarImputUsoLibro() {
        if (registroPlantaUsoComercio == null) {
            JsfUtil.mensajeAdvertencia("Seleccione un Lugar de Uso");
            return false;
        }
        if (tipoUso == null) {
            JsfUtil.mensajeAdvertencia("Seleccione Tipo de Movimiento");
            return false;
        }

        switch (tipoUso.getCodProg()) {
            case "TP_LIBUSO_ING":
                if (eppGuiaTransito == null) {
                    JsfUtil.mensajeAdvertencia("Seleccione una Guia de transito");
                    return false;
                }
                break;
            case "TP_LIBUSO_USO":
            case "TP_LIBUSO_REI":
            case "TP_LIBUSO_DEV":
                if (eppDetalleAutUso == null) {
                    JsfUtil.mensajeAdvertencia("Seleccione un Explosivo");
                    return false;
                }
                break;
        }
        return true;
    }

    public EppRegistroGuiaTransito objeto(EppRegistroGuiaTransito re) {
        return re;
    }

    public boolean verButtonEditar(EppLibroMes ep) {
        return ep.getTipoEstado().getCodProg().equals("TP_REGEV_SUB") || ep.getTipoEstado().getCodProg().equals("TP_REGEV_CRE")
                || ep.getTipoEstado().getCodProg().equals("TP_REGEV_OBS");
    }

    /**
     * lista de dias del mes 31 dias para proc uso (libro)
     *
     * @param date
     * @param dateFin
     * @return
     *
     */
//    public List<Long> listaDias() {
//        for (long i = 1; i < 32; i++) {
//            listaDiasMesEd.add(i);
//        }
//        return listaDiasMesEd;
//    }
    public List<Long> listaDiasParam(String date, String dateFin) {
        List<Long> dias = new ArrayList<>();
        for (long i = new Integer(date); i < new Integer(dateFin); i++) {
            dias.add(i);
        }
        return dias;
    }

    /**
     * limpia valores del panel Explosivos o Material Relacionado Vendidos
     * proceso USO (librp)
     */
    public void limpiarValoresPanelExplMatRela() {
        tipoUso = null;
        eppGuiaTransito = null;
        saldo = null;
        unidadMedidaExplo = null;
        eppDetalleAutUso = null;
        nroLote = null;
        cantidad = null;

    }

    /**
     * SUMA LA CANTIDAD DE EXPLOSIVO SELECCIONADO EN LA TABLA
     * EPP_LIBRO_USODIARIO TIENE QUE SER MENOR A LA CANTIDAD DE SALDO QUE VIENE
     * CON LA GUIA TRANSITO PROC USO (LIBRO) SOLO TIPO INGRESO
     *
     * @return
     */
    public Double calcularSaldoLibroYguiaTransito() {
        double cantiReal = 0.0;
        for (EppLibroUsoDiario it : listEppLibroExploFabricados) {
            if (it.getTipoRegistroId().getCodProg().equals("TP_LIBUSO_ING")) {
                if (eppDetalleAutUso.getExplosivoId().equals(it.getExplosivoId())) {
                    cantiReal = cantiReal + it.getCantidad();

                }
            }
        }
        return cantiReal;
    }

    /**
     * VALIDAR CANTIDAD USADA SOLO PUEDE REINGRESAR EL MAXIMO DEL EXPLOSIVO
     * REGISTRADO COMO USO VALIDANDO TIPO MOVIMIENTO REINGRESO PROC ADQUI Y USO
     * LIBRO
     *
     * @return
     */
    public double validarCantMaxUsoExpl() {
        double saldUso = 0.0;
        switch (tipoUso.getCodProg()) {
            case "TP_LIBUSO_REI":
                for (EppLibroUsoDiario usoDiar : listEppLibroExploFabricados) {
                    if (usoDiar.getExplosivoId().equals(eppDetalleAutUso.getExplosivoId())) {
                        switch (usoDiar.getTipoRegistroId().getCodProg()) {
                            case "TP_LIBUSO_USO":
                                saldUso += usoDiar.getCantidad() == 0.0 ? saldUso : usoDiar.getCantidad();
                                break;
                        }
                    }
                }
        }

        return saldUso;
    }

    /**
     * NAXIMA DEVOLUCION TIENE QUE SER MENOR AL SALDO ACTUAL SALDO EN CALIENTE
     * INGRESO - USO + REINGRESO - DEVOLUCION PROC ADQUI USO LIBRO (NO USAR)
     *
     * @return
     */
    public double maxDevolucion() {
        double salDevolu = 0.0;
        switch (tipoUso.getCodProg()) {
            case "TP_LIBUSO_DEV":
                for (EppLibroUsoDiario usoDiar : listEppLibroExploFabricados) {
                    if (usoDiar.getExplosivoId().equals(eppDetalleAutUso.getExplosivoId())) {
                        switch (usoDiar.getTipoRegistroId().getCodProg()) {
                            case "TP_LIBUSO_USO":
                                salDevolu += usoDiar.getCantidad() == 0.0 ? salDevolu : usoDiar.getCantidad();
                                break;
                        }
                    }
                }
        }

        return salDevolu;

    }

    /**
     * VALIDA QUE SE INGRESE TODO LOS EXPLOSIVOS DE LA GUIA TRANSITO
     * SELECCIONADA PRC USO (LIBRO)
     *
     * @return
     */
    public boolean validarIngreExploDeGuiaTransitoAll() {
        boolean flat = false;
        int cont = 0;
        for (EppExplosivo it : listExploTemporal) {
            for (EppLibroUsoDiario usoDia : listEppLibroExploFabricados) {
                if (it.equals(usoDia.getExplosivoId())) {
                    cont++;
                    flat = true;
                }

            }

        }
        if (cont < listExploTemporal.size()) {
            JsfUtil.mensajeAdvertencia("Falta completar los explosivos de la Guia de Transito ");
            flat = false;
        }
        return flat;
    }

    /**
     * EXPLOSIVOS QUE ESTAN EN LA GUIA TRANSITO PROC USO (LIBRO)
     */
    public void exploEnGuiaTRansito() {
        for (EppDetalleAutUso itUso : listEppDetalleAutUso) {
            for (EppExplosivoSolicitado itExplo : eppGuiaTransito.getEppExplosivoSolicitadoList()) {
                if (itUso.getExplosivoId().equals(itExplo.getExplosivoId())) {
                    listExploTemporal.add(itUso.getExplosivoId());
                }
            }
        }
    }

    /**
     * VALIDA QUE NO PUEDA AGREGAR OTRO TIPO DE MOVIMIENTO HASTA TERMINAR DE
     * INGRESAR TODO LOS EXPLOSIVOS EN TIPO MOVI (INGRESO ) PROC USO (LIBRO)
     *
     * @return
     */
    public boolean validarTodoIngreso() {
        if (listEppLibroExploFabricados.size() < listExploTemporal.size()) {
            JsfUtil.mensajeAdvertencia("Ingrese todo los Explosivos de Guia de Transito antes de hacer otro Movimiento");
            return false;
        }
        return true;

    }

    /**
     * AJAX EN LISTADO BANDEJA USO (LIBRO)
     */
    public void mostrarLugarUso() {
        if (eppLibro == null) {
            labelLugaUso = null;
            return;
        }
        for (EppLugarUso it : eppLibro.getRegistroId().getComId().getEppLugarUsoList()) {
            if (it.getActivo() == 1) {
                labelLugaUso = it.getNombre();
                break;
            }
        }
    }

    /**
     * REGISTRAR TRAZA DE EXPEDIENTE
     *
     * @author Richar Fernández
     * @version 2.0
     * @param reg Registro a validar
     * @param userObs
     * @param subsana
     * @return Flag de proceso de registro de traza
     */
    public boolean registraTrazaExpediente(EppLibroMes reg, String userObs, short subsana) {
        List<Integer> acciones = new ArrayList();
        String userRemitente = "", userDestino = "", observacion = "";
        SbUsuarioGt usuario = new SbUsuarioGt();
        boolean estadoTraza = true, adjuntar = false;

        if (reg.getTipoEstado().getCodProg().equals("TP_REGEV_CRE")) {
            userRemitente = JsfUtil.getLoggedUser().getLogin();//USRWEB
            usuario = sbUsuarioFacadeGt.selectUsuario(userRemitente).get(0);
            if (usuario.getUsuarioId() != null) {
                userDestino = usuario.getUsuarioId().getLogin();
            } else {
                userDestino = userRemitente;
            }
            acciones.add(20);   // A procesar
            observacion = "Observaciones Subsanadas";

            if (reg.getTipoEstado().getCodProg().equals("TP_REGEV_CRE")) {
                if (subsana == 1) {
                    userDestino = userObs;
                } else if (subsana == 0) {
                    userDestino = "MSEGURA";
                }
                estadoTraza = wsTramDocController.asignarExpediente(reg.getNroExpediente(), "USRWEB", userDestino, observacion, "", acciones, adjuntar, reg.getLibroId().getRegistroId().getEppResolucion().getNumero());
            }

            if (!estadoTraza) {
                JsfUtil.mensajeError("No se pudo generar la traza del expediente. Nro exp. : " + reg.getNroExpediente());

            }

        }
        return estadoTraza;
    }

    /**
     * MES ACTUAL
     *
     * @param de
     * @return
     */
    public String formatMes(Date de) {
        SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
        String mesTexto = formatMes.format(de);
        return mesTexto.toUpperCase();
    }

    /**
     *
     */
    public void resetearFitrosTabla(String id) {
        DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
        table.reset();
    }

    /**
     * ORDENAR X DIA PROC LIBRO FABRICA
     *
     * @param po
     */
    public void orderListaExplo(List<EppLibroUsoDiario> po) {
        Collections.sort(po, Collections.reverseOrder(new Comparator<EppLibroUsoDiario>() {

            @Override
            public int compare(EppLibroUsoDiario o1, EppLibroUsoDiario o2) {
                return o1.getDia().compareTo(o2.getDia());
            }

        }));
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "eppLibroConverter")
    public static class EppLibroControllerConverterN extends EppLibroControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = EppLibro.class)
    public static class EppLibroControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EppLibroController c = (EppLibroController) JsfUtil.obtenerBean("eppLibroController", EppLibroController.class);
            return c.getEppLibro(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof EppLibro) {
                EppLibro o = (EppLibro) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + EppLibro.class.getName());
            }
        }
    }
}
