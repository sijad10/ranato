/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.jsf;

/**
 *
 * @author rarevalo
 */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.renagi.data.ConsultaAmaCatalogo;
import pe.gob.sucamec.renagi.data.ConsultaAmaLicenciaDeUso;
import pe.gob.sucamec.renagi.data.ConsultaAmaTarjetaPropiedad;
import pe.gob.sucamec.renagi.data.ConsultaAmaTipoLicencia;
import pe.gob.sucamec.renagi.jsf.util.EstadoCrud;
import pe.gob.sucamec.renagi.jsf.util.JsfUtil;
import pe.gob.sucamec.renagi.jsf.util.ReportUtil;
import pe.gob.sucamec.renagi.data.ConsultaSbPersona;
import pe.gob.sucamec.renagi.data.ConsultaSbConstancias;
import pe.gob.sucamec.renagi.data.ConsultaSbNumeracion;
import pe.gob.sucamec.renagi.data.ConsultaSbUsuario;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.seguridad.LogController;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;
import pe.gob.sucamec.sel.citas.jsf.util.RenagiUploadFilesController;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaFoto;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.ws.WsPide;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.renagi.beans.InfoArmasFacade;

// Nombre de la instancia en la aplicacion //
@Named("gamacConstanciaLicTarController")
@SessionScoped

/**
 * Clase con GamacConstanciaLicTarController instanciada como
 * gamacConstanciaLicTarController. Contiene funciones utiles para la entidad
 * GamacConstanciaLicTar/list.xhtml Nota: Las tablas deben tener la estructura
 * de Sucamec para que funcione adecuadamente, revisar si tiene el campo activo
 * y modificar las búsquedas.
 */
public class GamacConstanciaLicTarController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro, tipo;

    private byte[] foto;

    /**
     * Registro actual para editar o ver.
     */
    ArrayRecord registro;
    ConsultaSbPersona persona;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<ArrayRecord> resultados = null;

    List<Map> listNoReg = new ArrayList();
    List<Map> listLicencias = new ArrayList();
    List<Map> listTarjetas = new ArrayList();

    /**
     * Control de carga de Jasper
     */
    private boolean loadReport = false;

    /**
     * Variables para vista
     */
    AmaTarjetaPropiedad tarjeta;
    String armaCalibre;
    AmaInventarioArma inventarioArma;
    Expediente expTar;
    AmaGuiaTransito guiaT;
    String direInternamiento;
    String interDisca;
    
    /* */
    @Inject
    LoginController loginController;
    @Inject
    LogController logController;
    @Inject
    private RenagiUploadFilesController upFilesCont;
    @Inject
    WsTramDoc wsTramDocController;
    @Inject
    private WsPide wsPideController;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    //@EJB
    //private pe.gob.sucamec.renagi.beans.ConsultasDiscaFacade consultasDiscaFacade;
    @EJB
    private pe.gob.sucamec.sel.disca.beans.ConsultasDiscaFacade consultasDiscaFacade;
    @EJB
    private pe.gob.sucamec.renagi.beans.ConsultaSbPersonaFacade consultaSbPersonaFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbPersonaFacade SbPersonaFacade;
    @EJB
    private pe.gob.sucamec.renagi.beans.ConsultaAmaLicenciaDeUsoFacade consultaAmaLicenciaDeUsoFacade;
    @EJB
    private pe.gob.sucamec.renagi.beans.ConsultaAmaTarjetaPropiedadFacade consultaAmaTarjetaPropiedadFacade;
    @EJB
    private pe.gob.sucamec.renagi.beans.ConsultaTipoBaseFacade consultaTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.renagi.beans.ConsultaSbUsuarioFacade consultaSbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.renagi.beans.ConsultaSbConstanciasFacade consultaSbConstanciasFacade;
    @EJB
    private pe.gob.sucamec.renagi.beans.ConsultaSbNumeracionFacade consultaSbNumeracionFacade;
    @EJB
    private SbParametroFacade ejbSbParametroFacade;
    @EJB
    private InfoArmasFacade infoArmasFacade;

    /**
     * Tipo de busqueda
     *
     * @return
     */
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void crearFoto() {
        if (registro != null) {
            BigDecimal bd = (BigDecimal) registro.get("TIP_LIC");
            long tl = 0;
            if (bd != null) {
                tl = bd.longValue();
            }
            boolean vigilante = false;
            if (bd != null && (tl == 5 || tl == 7 || tl == 10)) {
                vigilante = true;
            }
            foto = JsfUtil.oracleTiffenJpg(consultasDiscaFacade.selectFoto(registro.get("DOC_PROPIETARIO").toString(), vigilante), 240, 320);
        }
    }

    public DefaultStreamedContent getFoto() {
        if (foto == null) {
            try {
                return new DefaultStreamedContent(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/ninguno_320.jpg"), "image/jpg");
            } catch (Exception ex) {
                //Syso("WsLicenciasController:getFoto:Error:" + ex.getMessage());
            }
        }
        return new DefaultStreamedContent(new ByteArrayInputStream(foto), "image/jpg");
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public ArrayRecord getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(ArrayRecord registro) {
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

    public boolean isLoadReport() {
        return loadReport;
    }

    public void setLoadReport(boolean loadReport) {
        this.loadReport = loadReport;
    }

    public String getArmaCalibre() {
        return armaCalibre;
    }

    public void setArmaCalibre(String armaCalibre) {
        this.armaCalibre = armaCalibre;
    }

    public Expediente getExpTar() {
        return expTar;
    }

    public void setExpTar(Expediente expTar) {
        this.expTar = expTar;
    }

    public AmaGuiaTransito getGuiaT() {
        return guiaT;
    }

    public void setGuiaT(AmaGuiaTransito guiaT) {
        this.guiaT = guiaT;
    }

    public String getDireInternamiento() {
        return direInternamiento;
    }

    public void setDireInternamiento(String direInternamiento) {
        this.direInternamiento = direInternamiento;
    }

    public AmaTarjetaPropiedad getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(AmaTarjetaPropiedad tarjeta) {
        this.tarjeta = tarjeta;
    }

    public AmaInventarioArma getInventarioArma() {
        return inventarioArma;
    }

    public void setInventarioArma(AmaInventarioArma inventarioArma) {
        this.inventarioArma = inventarioArma;
    }

    public String getInterDisca() {
        return interDisca;
    }

    public void setInterDisca(String interDisca) {
        this.interDisca = interDisca;
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
    public ListDataModel<ArrayRecord> getResultados() {
        return resultados;
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        resultados = null;
        filtro = "";
        mostrarBuscar();
        return "/aplicacion/consultasRenagi/GamacConstanciaLicTar/List";
    }

    /**
     * Cambia el estado del controller a buscar
     */
    public void mostrarBuscar() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
        /*String licencia = null, serie = null,
                doc_prop = null, nom_prop = null, doc_portador = null, nom_portador = null;*/

        if (filtro != null && !filtro.equals("")) {
            boolean buscar = false;
            filtro = filtro.trim();
            String licTipo = "";
            switch (tipo) {
                case "dni":
                    if (filtro.trim().length() != 8) {
                        JsfUtil.mensajeAdvertencia("Ingresar 8 dígitos para búsqueda!");
                    } else {
                        buscar = true;
                    }
                    break;
                case "ruc":
                    if (filtro.trim().length() != 11) {
                        JsfUtil.mensajeAdvertencia("Ingresar 11 dígitos para búsqueda!");
                    } else {
                        buscar = true;
                    }
                    break;
                case "cip":
                    if (filtro.trim().length() <= 3) {
                        JsfUtil.mensajeAdvertencia("Digitar por lo menos 4 carácteres para búsqueda!");
                    } else {
                        buscar = true;
                    }
                    break;
                case "ape":
                    if (filtro.trim().length() <= 2) {
                        JsfUtil.mensajeAdvertencia("Digitar por lo menos 3 carácteres para búsqueda!");
                    } else {
                        buscar = true;
                    }
                    break;
                case "lic":
                    if (filtro.trim().length() <= 2) {
                        JsfUtil.mensajeAdvertencia("Digitar por lo menos 3 carácteres para búsqueda!");
                    } else {
                        List<ConsultaAmaLicenciaDeUso> rs = new ArrayList<ConsultaAmaLicenciaDeUso>();
                        rs = consultaAmaLicenciaDeUsoFacade.listarAmaLicenciaXNroLic(Long.valueOf(filtro.trim()));
                        if (!rs.isEmpty()) {
                            filtro = rs.get(0).getPersonaLicenciaId().getNumDoc();
                            licTipo = rs.get(0).getPersonaLicenciaId().getTipoDoc().getCodProg();
                            buscar = true;
                        }
                    }
                    break;
                default:
                    buscar = true;
            }

            /*if (tipo.equals("dni")) {
            } else if (tipo.equals("cip")) {
                if(filtro.trim().length() <= 3){
                    JsfUtil.mensajeAdvertencia("Digitar por lo menos 4 carácteres para búsqueda!");
                } else{
                    buscar = true;
                }
            } else if (tipo.equals("ape")) {
                if(filtro.trim().length() <= 2){
                    JsfUtil.mensajeAdvertencia("Digitar por lo menos 3 carácteres para búsqueda!");                    
                } else {
                    buscar = true;                    
                }
            } else {
                buscar = true;                
            }*/
            resultados = null;
            if (buscar) {
                logController.escribirLogBuscar("" + tipo + "\t" + filtro, "INTEROP_WS_LICENCIAS");
                List<ArrayRecord> list = consultasDiscaFacade.listarPropietariosxFiltro(tipo, filtro);
                //Syso("resul: " + list.size());
                if (list != null) {
                    if (!list.isEmpty()) {
                        resultados = new ListDataModel(list);
                    } else {
                        //persona = obtenerPersona(tipo, filtro);
                        //if(persona==null){
                        switch (tipo) {
                            case "dni":
                            case "ruc":
                                list = consultaSbPersonaFacade.listarNoPropietariosxFiltro(tipo, filtro);
                                if (list.isEmpty()) {
                                    if (crearPersonaPide(tipo, filtro)) {
                                        list = consultaSbPersonaFacade.listarNoPropietariosxFiltro(tipo, filtro);
                                        resultados = new ListDataModel(list);
                                    }
                                } else {
                                    resultados = new ListDataModel(list);
                                }
                                break;
                            case "ce":
                            case "cip":
                                list = consultaSbPersonaFacade.listarNoPropietariosxFiltro(tipo, filtro);
                                resultados = new ListDataModel(list);
                                break;
                            case "lic":
                                switch (licTipo) {
                                    case "TP_DOCID_DNI":
                                        licTipo = "dni";
                                        break;
                                    case "TP_DOCID_CE":
                                    case "TP_DOCID_EXT":
                                        licTipo = "ce";
                                        break;
                                }

                                list = consultaSbPersonaFacade.listarNoPropietariosxFiltro(licTipo, filtro);
                                resultados = new ListDataModel(list);
                                break;
                        }
                        //}
                    }
                }
            }
        } else {
            resultados = null;
        }

    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (ArrayRecord) resultados.getRowData();
        crearFoto();
        logController.escribirLogVer("" + registro.get("NRO_LIC"), "WS_LICENCIAS");
        estado = EstadoCrud.VER;
    }

    /**
     * Obtiene el valor de un campo del hashmap, por algun motivo (bug) el xhtml
     * no permite esto directamente.
     *
     * @param r El resultado del native query como Map.
     * @param c El campo que se desea obtener.
     * @return El valor del campo como Object
     */
    public Object valorCampo(ArrayRecord r, String c) {
        return r.get(c);
    }

    /**
     * Constructor
     *
     */
    public GamacConstanciaLicTarController() {
        estado = EstadoCrud.BUSCAR;
    }

    ////////////////// REPORTE //////////////////
    /**
     * PARAMETROS PARA GENERAR PDF
     *
     * @author rarevalo
     * @version 1.0
     * @param reg Registro a crear el reporte
     * @return Listado de parámetros para reporte
     */
    private HashMap parametrosPdf(ArrayRecord reg) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();

        String numDoc = reg.get("DOC_PROPIETARIO").toString().trim();
        String tipoDoc = reg.get("TIPO_DOC").toString().trim();
        ConsultaSbPersona persona = null;

        //Syso("Buscando numDoc: " + numDoc);
        switch (tipoDoc) {
            case "DNI":
                persona = consultaSbPersonaFacade.findByNumDoc(numDoc, tipoDoc);
                break;
            case "CE":
                persona = consultaSbPersonaFacade.findByNumDoc(numDoc, tipoDoc);
                break;
            case "RUC":
                persona = consultaSbPersonaFacade.findByRuc(numDoc);
                break;
            case "CIP":
            case "FAP":
            case "POLICIA":
            case "MARINA":
            case "MILITAR":
            case "EJERCITO":
                persona = consultaSbPersonaFacade.findByNroCip(numDoc);
                break;
        }
        ph.put("P_logoSucamec", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
        ph.put("p_tipoDoc", tipoDoc);

        //Syso("Persona: " + persona);
        if (persona != null) {
            String propietario = " ";
            switch (tipoDoc) {
                case "DNI":
                case "CE":
                case "CIP":
                    propietario = (persona.getApePat() != null) ? persona.getApePat().toString() + " " : " ";
                    propietario += (persona.getApeMat() != null) ? persona.getApeMat().toString() + " " : " ";
                    propietario += (persona.getNombres() != null) ? persona.getNombres().toString() : " ";
                    ph.put("p_numDoc", persona.getNumDoc());
                    if (tipoDoc.equals("CIP")) {
                        ph.put("p_tipoDoc", tipoDoc + "/DNI");
                        ph.put("p_numDoc", persona.getNroCip() + "/" + persona.getNumDoc());
                    }
                    ph.put("p_propietario", propietario);
                    List<ConsultaAmaLicenciaDeUso> listLic = consultaAmaLicenciaDeUsoFacade.listarAmaLicenciaXPerLicencia(persona.getId());
                    if (!listLic.isEmpty()) {
                        try {
                            ConsultaAmaLicenciaDeUso lic = listLic.get(0);
                            //syso("fotoGamac: " + lic.getFotoId().getId());
                            InputStream is;
                            foto = FileUtils.readFileToByteArray(new File((ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_foto_ama").getValor()) + lic.getFotoId().getId() + ".jpg"));
                            ph.put("p_foto", new ByteArrayInputStream(foto));
                        } catch (Exception e) {
                            ph.put("p_foto", null);
                            //e.printStackTrace();
                        }

                    } else {
                        foto = null;
                    }
                    break;
                case "RUC":
                    propietario = (persona.getRznSocial() != null) ? persona.getRznSocial().toString() : " ";
                    ph.put("p_numDoc", persona.getRuc());
                    ph.put("p_propietario", propietario);
                    break;
            }
            String[] dire = ReportUtil.mostrarDireccionPersonaAr(SbPersonaFacade.find(persona.getId()));
            ph.put("p_dire", dire[0]);
            ph.put("p_direDep", dire[1]);
            ph.put("p_direProv", dire[2]);
            ph.put("p_direDist", dire[3]);
            //syso("Dirección: " + dire[0]);
            /*
            ph.put("P_TIPONRODOC", ReportUtil.mostrarTipoYNroDocumemtoString(ppdf.getPersonaId()));
            ph.put("P_EMPRESASTRING", ReportUtil.mostrarClienteString(ppdf.getPersonaId()));
            ph.put("P_CODTRIBUTO", String.valueOf(ppdf.getReciboId().getCodTributo()) );
            ph.put("P_MONTO", String.valueOf(new DecimalFormat("###,###,###,##0.00").getInstance(Locale.ENGLISH).format( ppdf.getReciboId().getImporte()  )));
            ph.put("P_NROSECUENCIA", String.valueOf(ppdf.getReciboId().getNroSecuencia()));
            ph.put("P_FECHAPAGO", ReportUtil.formatoFechaDdMmYyyy(ppdf.getReciboId().getFechaMovimiento()));
            ph.put("P_CODOFICINA", String.valueOf(ppdf.getReciboId().getCodOficina()) );
             */

        } else {
            switch (tipoDoc) {
                case "DNI":
                case "CE":
                    boolean encontrado = false;
                    ListDataModel<ArrayRecord> rsPersona;
                    rsPersona = new ListDataModel(consultasDiscaFacade.listarPersonaNatural(numDoc));
                    if (rsPersona.getRowCount() > 0) {
                        encontrado = true;
                    } else {
                        rsPersona = new ListDataModel(consultasDiscaFacade.listarExtranjero(numDoc));
                        if (rsPersona.getRowCount() > 0) {
                            encontrado = true;
                        }
                    }
                    if (encontrado) {
                        Iterator<ArrayRecord> iteRs = rsPersona.iterator();
                        while (iteRs.hasNext()) {
                            ArrayRecord row = iteRs.next();
                            ph.put("p_numDoc", row.get("COD_USR").toString());
                            ph.put("p_propietario", row.get("APE_PAT").toString() + " " + row.get("APE_MAT").toString() + " " + row.get("NOMBRE").toString());
                            ph.put("p_dire", row.get("DOMICILIO").toString());
                            ph.put("p_direDep", row.get("NOM_DPTO").toString());
                            ph.put("p_direProv", row.get("NOM_PROV").toString());
                            ph.put("p_direDist", row.get("NOM_DIST").toString());
                        }
                    } else {
                        ph.put("p_numDoc", "");
                        ph.put("p_propietario", "");
                        ph.put("p_dire", "");
                        ph.put("p_direDep", "");
                        ph.put("p_direProv", "");
                        ph.put("p_direDist", "");
                    }
                    break;
                case "RUC":
                    ListDataModel<ArrayRecord> rsEmpresa = new ListDataModel(consultasDiscaFacade.listarEmpresa(numDoc));
                    if (rsEmpresa.getRowCount() > 0) {
                        Iterator<ArrayRecord> iteRs = rsEmpresa.iterator();
                        while (iteRs.hasNext()) {
                            ArrayRecord row = iteRs.next();
                            ph.put("p_numDoc", row.get("RUC").toString());
                            ph.put("p_propietario", row.get("RZN_SOC").toString());
                            ph.put("p_dire", row.get("DOMICILIO").toString());
                            ph.put("p_direDep", row.get("NOM_DPTO").toString());
                            ph.put("p_direProv", row.get("NOM_PROV").toString());
                            ph.put("p_direDist", row.get("NOM_DIST").toString());
                        }
                    }
                    break;
                case "FAP":
                case "POLICIA":
                case "MARINA":
                case "MILITAR":
                case "EJERCITO":
                    ListDataModel<ArrayRecord> rsExt;
                    rsExt = new ListDataModel(consultasDiscaFacade.listarEfectivo(numDoc));
                    if (rsExt.getRowCount() > 0) {
                        Iterator<ArrayRecord> iteRs = rsExt.iterator();
                        while (iteRs.hasNext()) {
                            ArrayRecord row = iteRs.next();
                            ph.put("p_numDoc", row.get("COD_USR").toString());
                            ph.put("p_propietario", row.get("APE_PAT").toString() + " " + row.get("APE_MAT").toString() + " " + row.get("NOMBRE").toString());
                            ph.put("p_dire", row.get("DOMICILIO").toString());
                            ph.put("p_direDep", row.get("NOM_DPTO").toString());
                            ph.put("p_direProv", row.get("NOM_PROV").toString());
                            ph.put("p_direDist", row.get("NOM_DIST").toString());
                        }
                    }
                    break;
            }

        }

        //carga foto
        if (tipoDoc.equals("DNI") || tipoDoc.equals("CE")) {
            if (foto == null) {
                try {
                    foto = JsfUtil.oracleTiffenJpg(consultasDiscaFacade.selectFoto(numDoc.trim(), false), 240, 320);
                    ph.put("p_foto", new ByteArrayInputStream(foto));
                } catch (Exception e) {
                    ph.put("p_foto", null);
                    //syso("No existe foto!!");
                    //e.printStackTrace();
                }
            }
        } else {
            ph.put("p_foto", null);
        }

        obtenerListadoLicencia(persona, numDoc, tipoDoc);
        ph.put("p_licencias", listLicencias);
        obtenerListadoTarjetas(persona, numDoc);
        ph.put("p_tarjetas", listTarjetas);
        obtenerListadoLicenciaArmaNoReg(numDoc, tipoDoc);

        if (tipoDoc.equals("DNI")) {
            // BUSQUEDA DE POSIBLE RUC DESDE DNI
            String ruc = obtenerListadoLicenciaArmaNoRegRucCipxDni(numDoc, "RUC");
            if (ruc != "") {
                ph.put("p_tipoDoc", tipoDoc + "/RUC");
                ph.put("p_numDoc", ph.get("p_numDoc") + "/" + ruc);
            }

            // BUSQUEDA DE POSIBLE CIP DESDE DNI
            if (persona != null) {
                if (!persona.getNroCip().isEmpty()) {
                    String nroCip = obtenerListadoLicenciaArmaNoRegRucCipxDni(persona.getNroCip(), "CIP");
                    if (nroCip != "") {
                        ph.put("p_tipoDoc", tipoDoc + "/CIP");
                        ph.put("p_numDoc", ph.get("p_numDoc") + "/" + nroCip);
                    }
                }
            }

        }

        ph.put("p_armasNoReg", listNoReg);
        ph.put("firmaGerente", ec.getRealPath("/resources/imagenes/firma_nombre_GAMAC.png"));

        return ph;
    }

    /**
     * PARAMETROS PARA GENERAR PDF CON SB_PERSONA
     *
     * @author rarevalo
     * @version 1.0
     * @param reg Registro a crear el reporte
     * @return Listado de parámetros para reporte
     */
    private HashMap parametrosPdfPersona(ArrayRecord reg, ConsultaSbPersona persona, String tipoDoc, String numDoc) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();

        //String numDoc = reg.get("DOC_PROPIETARIO").toString().trim();
        //String tipoDoc = reg.get("TIPO_DOC").toString().trim();
        ph.put("P_logoSucamec", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
        ph.put("p_tipoDoc", tipoDoc);
        ph.put("p_instituLab", (persona.getInstituLabId()!=null)? persona.getInstituLabId().getNombre():"");

        //Syso("Persona: " + persona);
        persona = obtenerPersona(tipoDoc, numDoc);
        if (persona != null) {
            String propietario = " ";
            switch (tipoDoc) {
                case "DNI":
                case "CE":
                case "CIP":
                    propietario = (persona.getApePat() != null) ? persona.getApePat().toString() + " " : " ";
                    propietario += (persona.getApeMat() != null) ? persona.getApeMat().toString() + " " : " ";
                    propietario += (persona.getNombres() != null) ? persona.getNombres().toString() : " ";
                    ph.put("p_numDoc", persona.getNumDoc());
                    if (tipoDoc.equals("CIP")) {
                        ph.put("p_tipoDoc", tipoDoc + " / DNI");
                        ph.put("p_numDoc", persona.getNroCip() + " / " + persona.getNumDoc());
                    }
                    ph.put("p_propietario", propietario);
                    List<ConsultaAmaLicenciaDeUso> listLic = consultaAmaLicenciaDeUsoFacade.listarAmaLicenciaXPerLicencia(persona.getId());
                    if (!listLic.isEmpty()) {
                        try {
                            ConsultaAmaLicenciaDeUso lic = listLic.get(0);
                            //syso("fotoGamac: " + lic.getFotoId().getId());
                            InputStream is;
                            foto = FileUtils.readFileToByteArray(new File((ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_foto_ama").getValor()) + lic.getFotoId().getId() + ".jpg"));
                            ph.put("p_foto", new ByteArrayInputStream(foto));
                        } catch (Exception e) {
                            ph.put("p_foto", null);
                            //e.printStackTrace();
                        }

                    } else {
                        foto = null;
                    }
                    break;
                case "RUC":
                    propietario = (persona.getRznSocial() != null) ? persona.getRznSocial().toString() : " ";
                    ph.put("p_numDoc", persona.getRuc());
                    ph.put("p_propietario", propietario);
                    break;
            }

            String[] dire = ReportUtil.mostrarDireccionPersonaAr(SbPersonaFacade.find(persona.getId()));
            if (dire[0] == null) {
                switch (tipoDoc) {
                    case "DNI":
                        ListDataModel<ArrayRecord> listPersona = new ListDataModel(consultasDiscaFacade.listarPersonaNatural(numDoc));
                        Iterator<ArrayRecord> iteRs = listPersona.iterator();
                        while (iteRs.hasNext()) {
                            ArrayRecord row = iteRs.next();
                            ph.put("p_dire", (row.get("DOMICILIO") != null) ? row.get("DOMICILIO").toString() : " ");
                            ph.put("p_direDep", (row.get("NOM_DPTO") != null) ? row.get("NOM_DPTO").toString() : " ");
                            ph.put("p_direProv", (row.get("NOM_PROV") != null) ? row.get("NOM_PROV").toString() : " ");
                            ph.put("p_direDist", (row.get("NOM_DIST") != null) ? row.get("NOM_DIST").toString() : " ");
                            break;
                        }
                    case "CE":
                        ListDataModel<ArrayRecord> listExt = new ListDataModel(consultasDiscaFacade.listarExtranjero(numDoc));
                        Iterator<ArrayRecord> iteExt = listExt.iterator();
                        while (iteExt.hasNext()) {
                            ArrayRecord row = iteExt.next();
                            ph.put("p_dire", (row.get("DOMICILIO") != null) ? row.get("DOMICILIO").toString() : " ");
                            ph.put("p_direDep", (row.get("NOM_DPTO") != null) ? row.get("NOM_DPTO").toString() : " ");
                            ph.put("p_direProv", (row.get("NOM_PROV") != null) ? row.get("NOM_PROV").toString() : " ");
                            ph.put("p_direDist", (row.get("NOM_DIST") != null) ? row.get("NOM_DIST").toString() : " ");
                            break;
                        }
                    case "CIP":
                }
            } else {
                ph.put("p_dire", dire[0]);
                ph.put("p_direDep", dire[1]);
                ph.put("p_direProv", dire[2]);
                ph.put("p_direDist", dire[3]);
            }
        }

        //carga foto
        if (tipoDoc.equals("DNI") || tipoDoc.equals("CE")) {
            if (foto == null) {
                try {
                    foto = JsfUtil.oracleTiffenJpg(consultasDiscaFacade.selectFoto(numDoc.trim(), false), 240, 320);
                    ph.put("p_foto", new ByteArrayInputStream(foto));
                } catch (Exception e) {
                    ph.put("p_foto", null);
                    //e.printStackTrace();
                }
            }
        } else {
            ph.put("p_foto", null);
        }

        obtenerListadoLicencia(persona, numDoc, tipoDoc);
        ph.put("p_licencias", listLicencias);
        obtenerListadoTarjetas(persona, numDoc);
        ph.put("p_tarjetas", listTarjetas);
        obtenerListadoLicenciaArmaNoReg(numDoc, tipoDoc);

        if (tipoDoc.equals("DNI")) {
            // BUSQUEDA DE POSIBLE RUC DESDE DNI
            String ruc = obtenerListadoLicenciaArmaNoRegRucCipxDni(numDoc, "RUC");
            if (ruc != "") {
                ph.put("p_tipoDoc", tipoDoc + " / RUC");
                ph.put("p_numDoc", ph.get("p_numDoc") + " / " + ruc);
            }

            // BUSQUEDA DE POSIBLE CIP DESDE DNI
            if (persona != null) {
                if (persona.getNroCip() != null) {
                    String nroCip = obtenerListadoLicenciaArmaNoRegRucCipxDni(persona.getNroCip(), "CIP");
                    if (nroCip != "") {
                        ph.put("p_tipoDoc", tipoDoc + " / CIP");
                        ph.put("p_numDoc", ph.get("p_numDoc") + " / " + nroCip);
                    }
                }
            }

        }

        ph.put("p_armasNoReg", listNoReg);
        ph.put("firmaGerente", ec.getRealPath("/resources/imagenes/firma_nombre_GAMAC.png"));

        ArrayRecord denegado = obtenerDenegado(numDoc, tipoDoc);
        if (denegado != null) {
            ph.put("d_condicion", "INHABILITADO");
            ph.put("d_autorizadoPor", denegado.get("JUZGADO"));
            switch (Integer.valueOf(denegado.get("TIPO_SUSPENCION").toString())) {
                case 1:
                    ph.put("d_tipoSusp", "TEMPORAL");
                    ph.put("d_fecDesde", (denegado.get("FEC_INI") != null) ? denegado.get("FEC_INI").toString() : " ");
                    ph.put("d_fecHasta", (denegado.get("FEC_FIN") != null) ? denegado.get("FEC_FIN").toString() : " ");
                    if (denegado.get("FEC_FIN") != null) {
                        try {
                            Date hoy = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                            Date hasta;
                            hasta = sdf.parse(denegado.get("FEC_FIN").toString());
                            if (hoy.compareTo(hasta) > 0) {
                                ph.put("d_condicion", "HABILITADO");
                                ph.put("d_autorizadoPor", " ");
                                ph.put("d_tipoSusp", " ");
                                ph.put("d_fecDesde", " ");
                                ph.put("d_fecHasta", " ");
                            }
                        } catch (ParseException ex) {
                            Logger.getLogger(GamacConstanciaLicTarController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                case 2:
                    ph.put("d_tipoSusp", "INDEFINIDA");
                    ph.put("d_fecDesde", (denegado.get("FEC_INI") != null) ? denegado.get("FEC_INI").toString() : " ");
                    ph.put("d_fecHasta", "-");
                    break;
                case 3:
                    ph.put("d_tipoSusp", "DEFINITIVA");
                    ph.put("d_fecDesde", (denegado.get("FEC_INI") != null) ? denegado.get("FEC_INI").toString() : " ");
                    ph.put("d_fecHasta", "-");
                    break;
                default:
                    break;
            }
        } else {
            ph.put("d_condicion", "HABILITADO");
            ph.put("d_autorizadoPor", "-");
            ph.put("d_tipoSusp", "-");
            ph.put("d_fecDesde", "-");
            ph.put("d_fecHasta", "-");
        }

        return ph;
    }

    /**
     * OBTENER LISTADO DE LICENCIAS (INTEGRADO)
     *
     * @param per
     * @param numDoc
     * @param tipoDoc
     * @author rarevalo
     * @version 1.0
     */
    public void obtenerListadoLicencia(ConsultaSbPersona per, String numDoc, String tipoDoc) {
        listLicencias.clear();
        List<ConsultaAmaLicenciaDeUso> rs = new ArrayList<ConsultaAmaLicenciaDeUso>();
        if (per != null) {
            switch (tipoDoc) {
                case "DNI":
                case "CE":
                case "CIP":
                    rs = consultaAmaLicenciaDeUsoFacade.listarAmaLicenciaXPerLicencia(per.getId());
                    break;
                case "RUC":
                    rs = consultaAmaLicenciaDeUsoFacade.listarAmaLicenciaXPerPadre(per.getId());
                    break;
            }
            for (int i = 0; i < rs.size(); i++) {
                Map licencia = new HashMap();
                licencia.put("nroLicencia", rs.get(i).getNroLicencia().toString());
                licencia.put("fecEmision", ReportUtil.mostrarFechaDdMmYyyy((Date) rs.get(i).getFechaEmision()));
                licencia.put("fecVenci", ReportUtil.mostrarFechaDdMmYyyy((Date) rs.get(i).getFechaVencimietnto()));
                licencia.put("fecInscripcion", ReportUtil.mostrarFechaDdMmYyyy((Date) rs.get(i).getFechaInscripcion()));
                licencia.put("estado", rs.get(i).getEstadoId().getNombre());
                String modalidades = "";
                int x = 0;
                for (ConsultaAmaTipoLicencia row : rs.get(i).getAmaTipoLicenciaList()) {
                    if (row.getActivo() == 1) {
                        boolean mostrar = false;
                        if (row.getEstadoId()==null) {
                            mostrar = true;
                        } else {
                            if (row.getEstadoId().getCodProg().equals("TP_EST_NULIDAD")) {
                                mostrar = false;
                            } else {
                                mostrar = true;
                            }
                        }
                        
                        if (mostrar) {
                            if (x > 0) {
                                modalidades += ", " + row.getModalidadId().getNombre();
                            } else {
                                modalidades += row.getModalidadId().getNombre();
                            }                            
                        }

                        x++;
                    }
                }
                licencia.put("modalidades", modalidades);

                listLicencias.add(licencia);
            }
        }

    }

    /**
     * OBTENER LISTADO DE TARJETAS DE PROPIEDAD (INTEGRADO)
     *
     * @param per
     * @param numDoc
     * @author rarevalo
     * @version 1.0
     */
    public void obtenerListadoTarjetas(ConsultaSbPersona per, String numDoc) {
        listTarjetas = new ArrayList();

        if (per != null) {
            List<ConsultaAmaTarjetaPropiedad> rs = consultaAmaTarjetaPropiedadFacade.listarTarjetasXPerComp(per.getId());
            if (rs != null) {
                for (int i = 0; i < rs.size(); i++) {
                    Map tarjetaProp = new HashMap();
                    if (rs.get(i).getLicenciaId() == null) {
                        tarjetaProp.put("licVinculada", " ");
                    } else {
                        tarjetaProp.put("licVinculada", rs.get(i).getLicenciaId().getNroLicencia().toString());
                    }
                    tarjetaProp.put("nroRua", rs.get(i).getArmaId().getNroRua());
                    tarjetaProp.put("fecEmision", ReportUtil.mostrarFechaDdMmYyyy((Date) rs.get(i).getFechaEmision()));
                    tarjetaProp.put("arma", rs.get(i).getArmaId().getModeloId().getTipoArmaId().getNombre());
                    tarjetaProp.put("serie", rs.get(i).getArmaId().getSerie());
                    tarjetaProp.put("marca", rs.get(i).getArmaId().getModeloId().getMarcaId().getNombre());
                    tarjetaProp.put("modelo", rs.get(i).getArmaId().getModeloId().getModelo());
                    tarjetaProp.put("modalidad", rs.get(i).getModalidadId().getNombre());
                    //tarjetaProp.put("estado", rs.get(i).getEstadoId().getNombre() );
                    tarjetaProp.put("estado", rs.get(i).getArmaId().getEstadoId().getNombre());
                    tarjetaProp.put("situacion", (rs.get(i).getArmaId().getSituacionId() != null) ? rs.get(i).getArmaId().getSituacionId().getNombre() : "");
                    String calibres = "";
                    int x = 0;

                    for (ConsultaAmaCatalogo row : rs.get(i).getArmaId().getModeloId().getGamacAmaCatalogoCollection()) {
                        if (x > 0) {
                            calibres += ", " + row.getNombre();
                        } else {
                            calibres += row.getNombre();
                        }

                        x++;
                    }
                    tarjetaProp.put("calibres", calibres);

                    listTarjetas.add(tarjetaProp);
                }
            }
        }

    }

    /**
     * OBTENER LISTADO DE ARMAS NO REGULARIZADAS (DISCA)
     *
     * @param numDoc
     * @param tipoDoc
     * @author rarevalo
     * @version 1.0
     */
    public void obtenerListadoLicenciaArmaNoReg(String numDoc, String tipoDoc) {
        //Syso(tipoDoc + "-" + numDoc);
        listNoReg = new ArrayList();
        ListDataModel<ArrayRecord> rs = new ListDataModel(consultasDiscaFacade.listarxNumDoc(tipoDoc, numDoc));
        Iterator<ArrayRecord> iteRs = rs.iterator();
        while (iteRs.hasNext()) {
            ArrayRecord row = iteRs.next();
            if (row.get("SISTEMA").toString().equals("DISCA")) {
                Map licencia = new HashMap();
                licencia.put("nroLicencia", row.get("NRO_LIC").toString());
                licencia.put("licAnterior", row.get("NRO_LIC").toString()); //(row.get("NRO_LIC_ORG")==null)? " ":row.get("NRO_LIC_ORG").toString() );            
                licencia.put("fecEmision", ReportUtil.mostrarFechaDdMmYyyy((Date) row.get("FEC_EMISION")));
                licencia.put("tipoArma", row.get("TIPO_ARMA").toString());
                licencia.put("nroSerie", (row.get("NRO_SERIE") == null) ? " " : row.get("NRO_SERIE").toString());
                licencia.put("marca", (row.get("MARCA") == null) ? " " : row.get("MARCA").toString());
                licencia.put("modelo", (row.get("MODELO") == null) ? " " : row.get("MODELO").toString());
                licencia.put("calibre", (row.get("CALIBRE") == null) ? " " : row.get("CALIBRE").toString());
                licencia.put("fecVenci", ReportUtil.mostrarFechaDdMmYyyy((Date) row.get("FEC_VENCIMIENTO")));
                licencia.put("situacionArma", row.get("SITUACION").toString());
                licencia.put("situacionLicencia", row.get("ESTADO").toString());
                licencia.put("tipoLicencia", (row.get("TIPO_LICENCIA") == null) ? " " : row.get("TIPO_LICENCIA").toString());
                listNoReg.add(licencia);
                /*if(row.get("TIPO_PROPIETARIO").toString().equals("PERS. NATURAL") || row.get("TIPO_PROPIETARIO").toString().equals("EXTRANJERO")){
                    if(row.get("DOC_PROPIETARIO").toString()==row.get("DOC_PORTADOR").toString()){
                        listNoReg.add(licencia);                        
                    }
                }else{
                    listNoReg.add(licencia);
                }*/
            }
        }
        //Syso("numDoc: " + numDoc + " " + listNoReg.size());
    }

    /**
     * OBTENER LISTADO DE ARMAS NO REGULARIZADAS (DISCA) CON RUC, DESDE DNI
     *
     * @param numDoc
     * @param tipoDoc
     * @author rarevalo
     * @version 1.0
     */
    private String obtenerListadoLicenciaArmaNoRegRucCipxDni(String numDoc, String tipoDoc) {
        //listNoReg.clear();
        String doc = "";
        ListDataModel<ArrayRecord> rs = new ListDataModel(consultasDiscaFacade.listarArmasxNroReferencia("doc_prop", numDoc, tipoDoc));
        Iterator<ArrayRecord> iteRs = rs.iterator();
        while (iteRs.hasNext()) {
            ArrayRecord row = iteRs.next();
            if (row.get("SISTEMA").toString().equals("DISCA")) {
                Map licencia = new HashMap();
                doc = row.get("DOC_PROPIETARIO").toString();
                licencia.put("nroLicencia", row.get("NRO_LIC").toString());
                licencia.put("licAnterior", row.get("NRO_LIC").toString()); //(row.get("NRO_LIC_ORG")==null)? " ":row.get("NRO_LIC_ORG").toString() );            
                licencia.put("fecEmision", ReportUtil.mostrarFechaDdMmYyyy((Date) row.get("FEC_EMISION")));
                licencia.put("tipoArma", row.get("TIPO_ARMA").toString());
                licencia.put("nroSerie", (row.get("NRO_SERIE") == null) ? " " : row.get("NRO_SERIE").toString());
                licencia.put("marca", (row.get("MARCA") == null) ? " " : row.get("MARCA").toString());
                licencia.put("modelo", (row.get("MODELO") == null) ? " " : row.get("MODELO").toString());
                licencia.put("calibre", (row.get("CALIBRE") == null) ? " " : row.get("CALIBRE").toString());
                licencia.put("fecVenci", ReportUtil.mostrarFechaDdMmYyyy((Date) row.get("FEC_VENCIMIENTO")));
                licencia.put("situacionArma", row.get("SITUACION").toString());
                licencia.put("situacionLicencia", row.get("ESTADO").toString());
                licencia.put("tipoLicencia", (row.get("TIPO_LICENCIA") == null) ? " " : row.get("TIPO_LICENCIA").toString());
                listNoReg.add(licencia);
                /*if(row.get("TIPO_PROPIETARIO").toString().equals("PERS. NATURAL") || row.get("TIPO_PROPIETARIO").toString().equals("EXTRANJERO")){
                    if(row.get("DOC_PROPIETARIO").toString()==row.get("DOC_PORTADOR").toString()){
                        listNoReg.add(licencia);                        
                    }
                }else{
                    listNoReg.add(licencia);
                }*/
            }
        }
        return doc;
    }

    /**
     * OBTENER DATOS DE TABLA DENEGADO DEL RMA
     *
     * @param numDoc
     * @param tipoDoc
     * @author rarevalo
     * @version 1.0
     */
    private ArrayRecord obtenerDenegado(String numDoc, String tipoDoc) {
        List<ArrayRecord> listRs = consultasDiscaFacade.listarDenegadosxFiltro(numDoc, tipoDoc);
        ArrayRecord rs = null;
        if (listRs != null) {
            rs = listRs.get(0);
        }
        return rs;
    }

    /**
     * FUNCION PARA GENERACION DE REPORTE DE LICENCIA
     *
     * @author rarevalo
     * @version 1.0
     * @param per
     * @param tipoDoc
     * @param numDoc
     * @return Reporte
     */
    public StreamedContent reporte(ArrayRecord per, String tipoDoc, String numDoc) {
        try {
            List<ArrayRecord> ls = new ArrayList();
            registro = (ArrayRecord) per;
            ls.add(registro);

            //String numDoc = registro.get("DOC_PROPIETARIO").toString().trim();
            //String tipoDoc = registro.get("TIPO_DOC").toString().trim();
            if (persona != null) {
                String jasperConst = "";
                switch (tipoDoc) {
                    case "DNI":
                    case "CE":
                        //persona = consultaSbPersonaFacade.findByNumDoc( numDoc );
                        jasperConst = "constanciaLicTarArma.jasper";
                        break;
                    case "RUC":
                        //persona = consultaSbPersonaFacade.findByRuc( numDoc );                
                        jasperConst = "constanciaLicTarPerJur.jasper";
                        break;
                    case "CIP":
                    case "FAP":
                    case "POLICIA":
                    case "MARINA":
                    case "MILITAR":
                    case "EJERCITO":
                        //persona = consultaSbPersonaFacade.findByNroCip( numDoc ); 
                        //jasperConst = "constanciaLicTarPerJur.jasper";
                        jasperConst = "constanciaLicTarArma.jasper";
                        break;
                }

                HashMap ph = new HashMap();
                ph = parametrosPdfPersona(registro, persona, tipoDoc, numDoc); //parametrosPdf(registro);
                Date fecha = new Date();
                String cFecha = ReportUtil.mostrarFechaDdMmYyyy(fecha);
                String cHora = ReportUtil.mostrarFechaHhMmSs(fecha);
                String cFechaNom = ReportUtil.formatoFechaYyyyMmDdHhMmSs(fecha);
                ph.put("p_fechaString", cFecha);
                ph.put("p_fechaHora", cHora);
                ph.put("p_fechaAnio", ReportUtil.mostrarAnio(fecha));

                ConsultaSbConstancias newConsLic = new ConsultaSbConstancias();
                newConsLic.setPersonaId(persona);
                newConsLic.setTipoConstanciaId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_CONST_LIC"));
                newConsLic.setAreaId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_AREA_OTR"));
                newConsLic.setUsuarioId(consultaSbUsuarioFacade.find(loginController.getUsuario().getId()));
                newConsLic.setFechaEmision(fecha);
                newConsLic.setAudLogin(loginController.getUsuario().getLogin());
                newConsLic.setAudNumIp(loginController.getNumIP());
                newConsLic.setHashQr(JsfUtil.crearHash(numDoc + "-" + cFechaNom));
                newConsLic.setActivo((short) 1);
                //if(loginController.getUsuario())
                ConsultaSbUsuario gerente = consultaSbUsuarioFacade.obtenerJefeId(loginController.getUsuario().getId());
                if (gerente != null) {
                    newConsLic.setGerenteId(gerente);
                } else {
                    gerente = consultaSbUsuarioFacade.selectUsuarioXLogin(loginController.getUsuario().getLogin()).get(0);
                    newConsLic.setGerenteId(gerente);
                }

                /*try {
                    //SE BUSCAR EL USUARIO DE TRAMDOC PARA ASIGNARLO COMO PARAMETRO
                    DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                    Integer usuaTramDoc = consultaSbUsuarioFacade.obtenerIdUsuarioTramDoc(p_user.getLogin());
                    //SE CREA POR WS EL DOCUMENTO DE RESOLUCION PARA OBTENER EL NUMERO
                    Expediente ex = null;//expedienteFacade.obtenerExpedienteXNumeroList(expdienteOficio).get(0);
                    String numResWs = wsTramDocController.crearResolucion("", 78, usuaTramDoc, cFecha, "CONSTANCIA");
                    //Syso("Nro Tramdoc: " + numResWs);
                } catch (Exception e) {
                    //Syso("No se pudo generar el numero de oficio");
                    //JsfUtil.mensajeError("No se pudo generar el numero de oficio");
                }*/
                ConsultaSbNumeracion num = consultaSbNumeracionFacade.selectNumeroLicencia("TP_NUM_CONST_LIC");
                num.setValor(num.getValor() + 1);
                newConsLic.setNroConstancia(num.getValor());

                newConsLic = (ConsultaSbConstancias) JsfUtil.entidadMayusculas(newConsLic, "");
                consultaSbConstanciasFacade.create(newConsLic);

                ph.put("p_hashqr", "https://www.sucamec.gob.pe/sel/faces/qr/constanciaLic.xhtml?h=" + newConsLic.getHashQr() + "-" + JsfUtil.crearHash(newConsLic.getId().toString()));
                ph.put("p_nroDoc", StringUtils.leftPad(String.valueOf(newConsLic.getNroConstancia()), 5, "0") + "-" + cFechaNom.substring(0, 4) + "-SUCAMEC-GAMAC");
                ph.put("P_WEBVALIDATOR", newConsLic.getHashQr().substring(0, 4) + "-N°" + ph.get("p_nroDoc"));

                String nomFile = cFechaNom + "_" + newConsLic.getId().toString();
                String ruta = ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_constanciaLic").getValor();
                try {
                    if (JsfUtil.buscarPdfRepositorio(nomFile, ruta) == false) {
                        JsfUtil.subirPdf(nomFile, ph, ls, "/aplicacion/consultasRenagi/GamacConstanciaLicTar/reportes/" + jasperConst, ruta);
                        //newConsLic.setHashQr(JsfUtil.crearHash("LICENCIA_CONSTANCIA_" + newConsLic.getId()));
                        /*newConsLic.setHashQr(JsfUtil.crearHash(ph.get("p_hashqr").toString()));
                        newConsLic.setActivo((short) 1);
                        newConsLic = (ConsultaSbConstancias) JsfUtil.entidadMayusculas(newConsLic, "hashQr");
                        consultaSbConstanciasFacade.edit(newConsLic);*/
                        consultaSbNumeracionFacade.edit(num);

                    }
                } catch (Exception ex) {
                    //ex.printStackTrace();
                    loadReport = false;
                    return JsfUtil.errorDescarga("Error: gamacConstanciaLicTarController.reporte :aa", ex);
                }

                try {
                    upFilesCont.PdfDownload(ruta, nomFile + ".pdf");
                } catch (IOException ex) {
                    //ex.printStackTrace();
                    Logger.getLogger(GamacConstanciaLicTarController.class.getName()).log(Level.SEVERE, null, ex);
                }
                loadReport = false;
            } else {
                JsfUtil.mensajeAdvertencia(ResourceBundle.getBundle("/BundleRenagi").getString("GamacConstanciaErrorPersona"));
                loadReport = false;
                return null;
                //return JsfUtil.errorPersona("Error: " + ResourceBundle.getBundle("/BundleRenagi").getString("GamacConstanciaErrorPersona"));
                /*try {
                    ph.put("p_hashqr", registro.get("DOC_PROPIETARIO").toString().trim() + ":" + cFecha + ":" + cHora);
                    return JsfUtil.generarReportePdf("/aplicacion/consultasRenagi/GamacConstanciaLicTar/reportes/constanciaLicTarArma.jasper", ls, ph, cFechaNom+".pdf", null, null);                    
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }

        } catch (Exception ex) {
            loadReport = false;
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: gamacConstanciaLicTarController.reporte: xx", ex);
            //return null;        
        }
        loadReport = false;
        return null;

    }

    /**
     * FUNCION PARA VALIDACION DE PERSONA
     *
     * @author rarevalo
     * @version 1.0
     * @param per
     */
    public void validaPersona(ArrayRecord per) {

        if (loadReport) {
            JsfUtil.mensajeAdvertencia("No se puede ejecutar 2 reportes a la vez!");
            return;
        }
        registro = (ArrayRecord) per;

        String value = JsfUtil.getRequestParameter("javax.faces.source");
        String[] parts = value.split(":");
        String indiceTable = parts[2];

        //String numDoc = registro.get("DOC_PROPIETARIO").toString().trim();
        //String tipoDoc = registro.get("TIPO_DOC").toString().trim();
        String tipoDoc = JsfUtil.getRequestParameter("buscarForm:buscarDatatable:" + indiceTable + ":rowDni");
        String numDoc = JsfUtil.getRequestParameter("buscarForm:buscarDatatable:" + indiceTable + ":rowNumDoc");
        //Syso("selectIdDoc: " + tipoDoc + numDoc);
        persona = null;
        switch (tipoDoc) {
            case "DNI":
                persona = consultaSbPersonaFacade.findByNumDoc(numDoc, tipoDoc);
                break;
            case "CE":
                persona = consultaSbPersonaFacade.findByNumDoc(numDoc, tipoDoc);
                break;
            case "RUC":
                persona = consultaSbPersonaFacade.findByRuc(numDoc);
                break;
            case "CIP":
            case "FAP":
            case "POLICIA":
            case "MARINA":
            case "MILITAR":
            case "EJERCITO":
                persona = consultaSbPersonaFacade.findByNroCip(numDoc);
                break;
        }

        if (persona != null) {
            loadReport = true;
            RequestContext.getCurrentInstance().execute("$(\".verificaDoc" + numDoc + "\").click();");
        } else {
            persona = new ConsultaSbPersona();
            switch (tipoDoc) {
                case "DNI":
                    wspide.Persona resReniec = wsPideController.ReniecDni(numDoc);
                    if (resReniec != null) {
                        persona.setApePat(resReniec.getAPPAT().trim());
                        persona.setApeMat(resReniec.getAPMAT().trim());
                        persona.setNombres(resReniec.getNOMBRES().trim());
                        persona.setTipoDoc(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_DNI"));
                        persona.setNumDoc(numDoc);
                        persona.setTipoId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
                        persona.setActivo((short) 1);
                        persona.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        persona.setAudNumIp(loginController.getNumIP());

                        if (resReniec.getSEXO()!=null) {
                            switch (resReniec.getSEXO()) {
                                case "F":
                                    persona.setGeneroId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_GEN_FEM"));
                                    break;
                                case "M":
                                    persona.setGeneroId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_GEN_MAS"));
                                    break;
                                default:
                                    break;
                            }                            
                        }

                        try {
                            persona = (ConsultaSbPersona) JsfUtil.entidadMayusculas(persona, "");
                            consultaSbPersonaFacade.create(persona);
                            loadReport = true;
                            RequestContext.getCurrentInstance().execute("$(\".verificaDoc" + numDoc + "\").click();");
                        } catch (Exception e) {
                            JsfUtil.mensajeAdvertencia("No se pudo guardar nuevo registro de Persona!");
                        }
                    }
                    break;
                case "CE":
                    ListDataModel<ArrayRecord> listExt = new ListDataModel(consultasDiscaFacade.listarExtranjero(numDoc));
                    if (listExt.getRowCount() > 0) {
                        Iterator<ArrayRecord> iteExt = listExt.iterator();
                        while (iteExt.hasNext()) {
                            ArrayRecord row = iteExt.next();
                            persona.setApePat(row.get("APE_PAT").toString().trim());
                            persona.setApeMat(row.get("APE_MAT").toString().trim());
                            persona.setNombres(row.get("NOMBRE").toString().trim());
                            persona.setFechaNac((Date) row.get("FEC_NAC"));
                            
                            if (row.get("SEXO")!=null) {
                                switch (row.get("SEXO").toString()) {
                                    case "F":
                                        persona.setGeneroId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_GEN_FEM"));
                                        break;
                                    case "M":
                                        persona.setGeneroId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_GEN_MAS"));
                                        break;
                                    default:
                                        break;
                                }
                            }

                        }
                        persona.setFechaReg(new Date());
                        persona.setNumDoc(numDoc);
                        persona.setTipoDoc(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_CE"));
                        persona.setTipoId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
                        persona.setActivo((short) 1);
                        persona.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        persona.setAudNumIp(loginController.getNumIP());

                        try {
                            persona = (ConsultaSbPersona) JsfUtil.entidadMayusculas(persona, "");
                            consultaSbPersonaFacade.create(persona);
                            loadReport = true;
                            RequestContext.getCurrentInstance().execute("$(\".verificaDoc" + numDoc + "\").click();");
                        } catch (Exception e) {
                            JsfUtil.mensajeAdvertencia("No se pudo guardar nuevo registro de Persona!");
                        }

                    } else {
                        JsfUtil.mensajeAdvertencia(ResourceBundle.getBundle("/BundleRenagi").getString("GamacConstanciaErrorPersona"));
                        //throw new Error("Para imprimir la constancia favor de registrar a la persona!");                        
                    }
                    break;
                case "RUC":
                    SbPersona perJur = wsPideController.buscarPerSunat(numDoc);
                    if (perJur != null) {
                        persona.setRznSocial(perJur.getRznSocial());
                        persona.setTipoDoc(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_RUC"));
                        persona.setTipoId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_JUR"));
                        persona.setRuc(perJur.getRuc());
                        persona.setActivo((short) 1);
                        persona.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        persona.setAudNumIp(loginController.getNumIP());

                        try {
                            persona = (ConsultaSbPersona) JsfUtil.entidadMayusculas(persona, "");
                            consultaSbPersonaFacade.create(persona);
                            loadReport = true;
                            RequestContext.getCurrentInstance().execute("$(\".verificaDoc" + numDoc + "\").click();");
                        } catch (Exception e) {
                            JsfUtil.mensajeAdvertencia("No se pudo guardar nuevo registro de Empresa!");
                        }
                    } else {
                        JsfUtil.mensajeAdvertencia(ResourceBundle.getBundle("/BundleRenagi").getString("GamacConstanciaErrorPersona"));
                        //throw new Error("Para imprimir la constancia favor de registrar a la persona!");                        
                    }
                    break;
                case "CIP":
                    JsfUtil.mensajeAdvertencia(ResourceBundle.getBundle("/BundleRenagi").getString("GamacConstanciaErrorPersona"));
                    //throw new Error("Para imprimir la constancia favor de registrar a la persona!");
                    break;
                default:
                    break;
            }
        }

    }

    private ConsultaSbPersona obtenerPersona(String tipoDoc, String numDoc) {
        ConsultaSbPersona per = null;
        switch (tipoDoc) {
            case "dni":
            case "DNI":
                per = consultaSbPersonaFacade.findByNumDoc(numDoc, tipoDoc);
                break;
            case "CE":
            case "ce":
                per = consultaSbPersonaFacade.findByNumDoc(numDoc, tipoDoc);
                break;
            case "RUC":
            case "ruc":
                per = consultaSbPersonaFacade.findByRuc(numDoc);
                break;
            case "CIP":
            case "FAP":
            case "POLICIA":
            case "MARINA":
            case "MILITAR":
            case "EJERCITO":
            case "cip":
                per = consultaSbPersonaFacade.findByNroCip(numDoc);
                break;
        }
        return per;
    }

    private boolean crearPersonaPide(String tipoDoc, String numDoc) {
        boolean status = false;
        persona = new ConsultaSbPersona();
        switch (tipoDoc) {
            case "dni":
            case "DNI":
                wspide.Persona resReniec = wsPideController.ReniecDni(numDoc);
                if (resReniec != null) {
                    persona.setApePat(resReniec.getAPPAT().trim());
                    persona.setApeMat(resReniec.getAPMAT().trim());
                    persona.setNombres(resReniec.getNOMBRES().trim());
                    persona.setTipoDoc(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_DNI"));
                    persona.setNumDoc(numDoc);
                    persona.setTipoId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
                    persona.setActivo((short) 1);
                    persona.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    persona.setAudNumIp(loginController.getNumIP());

                    if (resReniec.getSEXO() != null) {
                        switch (resReniec.getSEXO()) {
                            case "F":
                                persona.setGeneroId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_GEN_FEM"));
                                break;
                            case "M":
                                persona.setGeneroId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_GEN_MAS"));
                                break;
                            default:
                                break;
                        }
                    }

                    try {
                        persona = (ConsultaSbPersona) JsfUtil.entidadMayusculas(persona, "");
                        consultaSbPersonaFacade.create(persona);
                        status = true;

                    } catch (Exception e) {
                        JsfUtil.mensajeAdvertencia("No se pudo guardar nuevo registro de Persona!");
                        status = false;
                    }
                }
                break;
            case "ruc":
            case "RUC":
                SbPersona perJur = wsPideController.buscarPerSunat(numDoc);
                if (perJur != null) {
                    persona.setRznSocial(perJur.getRznSocial());
                    persona.setTipoDoc(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_RUC"));
                    persona.setTipoId(consultaTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_JUR"));
                    persona.setRuc(perJur.getRuc());
                    persona.setActivo((short) 1);
                    persona.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    persona.setAudNumIp(loginController.getNumIP());

                    try {
                        persona = (ConsultaSbPersona) JsfUtil.entidadMayusculas(persona, "");
                        consultaSbPersonaFacade.create(persona);
                        status = true;

                    } catch (Exception e) {
                        JsfUtil.mensajeAdvertencia("No se pudo guardar nuevo registro de Persona!");
                        status = false;
                    }
                }
                break;
            default:
                status = false;
                break;
        }
        return status;
    }
    
    public void limpiarVista(){
        tarjeta = new AmaTarjetaPropiedad();
        expTar = new Expediente();
        armaCalibre = "";
        inventarioArma = new AmaInventarioArma();
        guiaT = new AmaGuiaTransito();
        direInternamiento = "";
        interDisca = "";
        foto = null;
    }

    /**
     * Funcion tipo 'actionListener' que muestra el modal para ver un registro.
     *
     * @param item
     */
    public void mostrarVerDlg(ArrayRecord item) {
        limpiarVista();
        RequestContext context = RequestContext.getCurrentInstance();
        
        if(item != null){
            registro = item;
            String codUsr = "";

            List<String> sList = Arrays.asList("PERS. NATURAL", "PERS.NAT.C/RUC");
            
            //logController.escribirLogVer("" + registro.get("NRO_LIC"), "INTEROP_WS_LICENCIAS");
            //estado = EstadoCrud.VER;
            
            if(registro.get("SISTEMA").equals("GAMAC")){
                tarjeta = infoArmasFacade.obtenerArmaPorSerieRua(registro.get("NRO_RUA").toString(), registro.get("NRO_SERIE").toString());
                if (tarjeta != null) {
                    expTar = infoArmasFacade.selectExpedienteXNro(tarjeta.getNroExpediente()).get(0);
                    armaCalibre = ordenarCalibres(tarjeta.getArmaId().getModeloId().getAmaCatalogoList());
                    
                    if (tarjeta.getArmaId().getSituacionId().getCodProg().equals("TP_SITU_INT")) {
                        List<AmaInventarioArma> lstInventario = infoArmasFacade.selectInventarioArmaDisca(registro.get("NRO_LIC").toString(), registro.get("NRO_SERIE").toString());
                        if (!lstInventario.isEmpty()) {
                            inventarioArma = infoArmasFacade.selectInventarioArma(registro.get("NRO_RUA").toString(), registro.get("NRO_SERIE").toString()).get(0);
                            if(inventarioArma != null){
                                guiaT = inventarioArma.getAmaGuiaTransitoList().get(0);
                                direInternamiento = JsfUtil.mostrarDireccion(inventarioArma.getAlmacenSucamecId());
                            }

                        }
                    }
                    
                    //# OBTENER LA FOTO #//
                    //Syso("Tarjeta: [" + tarjeta.getId() + "]");
                    if (sList.contains(registro.get("TIPO_PROPIETARIO").toString())) {
                        if(tarjeta.getLicenciaId() != null){
                            if(tarjeta.getLicenciaId().getPersonaPadreId().equals(tarjeta.getLicenciaId().getPersonaLicenciaId())){
                                //Syso("Licencia: [" + tarjeta.getLicenciaId().getId() + "]");
                                obtenerFotoGamacxLic(tarjeta.getLicenciaId().getId());
                            } else {
                                obtenerFotoGamacxPer(tarjeta.getPersonaCompradorId().getId());
                            }
                        } else {
                            obtenerFotoGamacxPer(tarjeta.getPersonaCompradorId().getId());
                        }
                    }
                    //# #//
                }
                
                context.update("dlgViewArmGamacForm");
                context.execute("PF('wvDlgViewArmGamac').show();");
            }
            if(registro.get("SISTEMA").equals("DISCA")){
                //# OBTENER LA FOTO #//
                if (sList.contains(registro.get("TIPO_PROPIETARIO").toString())) {
                    crearFoto();
                }                    
                //# #//

                if (registro.get("SITUACION").toString().contains("INTERNA")) {
                    List<AmaInventarioArma> lstInventario = infoArmasFacade.selectInventarioArmaDisca(registro.get("NRO_LIC").toString(), registro.get("NRO_SERIE").toString());
                    if (lstInventario != null ) {
                        if (!lstInventario.isEmpty()) {
                            inventarioArma = lstInventario.get(0);
                            guiaT = inventarioArma.getAmaGuiaTransitoList().get(0);
                            direInternamiento = JsfUtil.mostrarDireccion(inventarioArma.getAlmacenSucamecId());
                        } else {
                            interDisca = " - (DISCA)";
                        }
                    } else {
                        interDisca = " - (DISCA)";
                    }                    
                }
                
                context.update("dlgViewArmDiscaForm");
                context.execute("PF('wvDlgViewArmDisca').show();");
            }

        }

    }
    
    public void obtenerFotoGamacxLic(Long licenciaId) {
        if (licenciaId != null) {
            String fileName = null;

            AmaFoto amaFoto = infoArmasFacade.listarAmaFotoXLicencia(licenciaId).get(0);

            fileName = amaFoto.getId() + ".jpg";
            try {
                foto = FileUtils.readFileToByteArray(new File(JsfUtil.bundle("Documentos_pathUpload_foto_ama") + fileName));
            } catch (IOException ex) {
                //JsfUtil.mensajeError(ex, JsfUtil.bundle("ErrorDePersistencia"));
                JsfUtil.mensajeError("No existe archivo imagen");
            }            
        }

    }

    public void obtenerFotoGamacxPer(Long personaId) {
        if (personaId != null) {
            String fileName = null;

            List<AmaFoto> lstAmaFoto = infoArmasFacade.listarAmaFotoXPersona(personaId);
            if (lstAmaFoto.size()>0) {
                fileName = lstAmaFoto.get(0).getId() + ".jpg";
                try {
                    foto = FileUtils.readFileToByteArray(new File(JsfUtil.bundle("Documentos_pathUpload_foto_ama") + fileName));
                } catch (IOException ex) {
                    //JsfUtil.mensajeError(ex, JsfUtil.bundle("ErrorDePersistencia"));
                    JsfUtil.mensajeError("No existe archivo imagen");
                }                
            }

        }
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
    
}
