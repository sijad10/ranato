package pe.gob.sucamec.tickets.jsf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.bean.TicketsFacade;
import pe.gob.sucamec.bdintegrado.data.SiTipo;
import pe.gob.sucamec.bdintegrado.data.Tickets;
import pe.gob.sucamec.bdintegrado.jsf.NumeracionController;
import pe.gob.sucamec.bdintegrado.jsf.TipoIntegradoController;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.TicketArchivo;
import pe.gob.sucamec.bdintegrado.data.TicketObs;
import pe.gob.sucamec.bdintegrado.jsf.TipoBaseController;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.jsf.SbUsuarioController;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;
import pe.gob.sucamec.bdintegrado.data.TicketRutas;


@Named("ticketsController")
@SessionScoped
public class TicketsController implements Serializable {

    private Tickets current;
    private DataModel items = null, itemsRel = null, itemsSim = null, itemsRelBusqueda = null;
    private boolean crearTicketCerrado = false;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TicketsFacade ejbFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TicketArchivoFacade ticketArchivoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt tipoBaseFacadeGt;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TicketObsFacade ejbFacadeTicketObs;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TicketRutasFacade ejbTicketRutasFacade;
    @Inject
    private TicketObsController ticketObsController;
    @Inject
    private TicketRutasController ticketRutasController;
    @Inject
    private LoginController loginController;
    private UploadedFile file;
    private StreamedContent foto;
    private byte[] fotoByte;
    List <byte[]> lsFotoByte;
    private Integer maxLength;
    private String nombreFoto;
    private String nombreArchivos;
    List <String> lsNombreTemporal;
    List <TicketArchivo> lsTicketArchivo = null;
    private List<Map> lstGaleriaFotos;    
    private String respuesta="";

    public boolean isCrearTicketCerrado() {
        return crearTicketCerrado;
    }

    public void setCrearTicketCerrado(boolean crearTicketCerrado) {
        this.crearTicketCerrado = crearTicketCerrado;
    }
    
    public List<Map> getLstGaleriaFotos() {
        return lstGaleriaFotos;
    }

    public void setLstGaleriaFotos(List<Map> lstGaleriaFotos) {
        this.lstGaleriaFotos = lstGaleriaFotos;
    }

    public List<String> getLsNombreTemporal() {
        return lsNombreTemporal;
    }

    public UploadedFile getFile() {
        return file;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
    
    public String getNombreArchivos() {
        nombreArchivos = null;
        if (lsTicketArchivo != null) {
            for (int i = 0; i < lsTicketArchivo.size(); ++i) {
                if (i==0){
                    nombreArchivos = lsNombreTemporal.get(i);
                }
                else {
                    nombreArchivos = nombreArchivos + ", " + lsNombreTemporal.get(i);
                }
            }
        }
        return nombreArchivos;
    }

    public DataModel getItemsRelBusqueda() {
        return itemsRelBusqueda;
    }

    public void setItemsRelBusqueda(DataModel itemsRelBuscar) {
        this.itemsRelBusqueda = itemsRelBuscar;
    }

    public DataModel getItemsSim() {
        return itemsSim;
    }

    public void setItemsSim(DataModel itemsSim) {
        this.itemsSim = itemsSim;
    }

    public DataModel getItemsRel() {
        return itemsRel;
    }

    public void setItemsRel(DataModel itemsRel) {
        this.itemsRel = itemsRel;
    }

    public String getFiltroRel() {
        return filtroRel;
    }

    public void setFiltroRel(String filtroRel) {
        this.filtroRel = filtroRel;
    }
    
    public StreamedContent getFoto() {
        try {
            if (fotoByte != null) {
                InputStream is = new ByteArrayInputStream(fotoByte);
                foto = new DefaultStreamedContent(is, "image/jpeg", "foto");
            } else if (file != null) {
                fotoByte = IOUtils.toByteArray(file.getInputstream());
                foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
            } else {
                return new DefaultStreamedContent(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/avatar.jpeg"), "image/jpeg");
            }
        } catch (Exception ex) {
            fotoByte = null;
            foto = null;
        }

        return foto;
    }
    
    /**
     * EVENTO PARA CARGAR FOTO
     *
     * @author Richar Fernández
     * @version 2.0
     * @param event Evento al cargar archivo
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (event != null) {
                file = event.getFile();
                //System.out.println("file: "+file.getFileName());                
                if (lsNombreTemporal== null) {
                    lsNombreTemporal = new ArrayList();
                }
                if (lsTicketArchivo== null) {
                    lsTicketArchivo = new ArrayList();
                }
                if (lsFotoByte == null) {
                    lsFotoByte  = new ArrayList();
                }
                
                if (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selTicket_countArchivo").getValor()) <= lsTicketArchivo.size()){
                    JsfUtil.mensajeAdvertencia("Solo se permiten " + Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selTicket_countArchivo").getValor()) + " archivos.");
                }
                else{
                    if (JsfUtil.verificarJPG(file)) {
                        fotoByte = IOUtils.toByteArray(file.getInputstream());
                        lsFotoByte.add(fotoByte);
                        foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
                        nombreFoto = file.getFileName();
                        lsNombreTemporal.add(nombreFoto);
                        String fileName = nombreFoto;
                        fileName = "FOTO_T" +(new Date()).getTime()+ fileName.substring(fileName.lastIndexOf('.'), fileName.length());

                        TicketArchivo archivo = new TicketArchivo();
                        archivo.setNombre(fileName);
                        lsTicketArchivo.add(archivo);

                    } else {
                        file = null;
                        JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo JPG/JPEG original");
                    } 
                }
                    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String obtenerMsjeInvalidSizeFoto() {
        return JsfUtil.bundleBDIntegrado("fileUpload_InvalidSizeMessage") + " "
                + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selTicket_sizeArchivo").getValor()) / 1024) + " Kb. ";
    }
    
    public String obtenerMsjeFoto() {
        return JsfUtil.bundleBDIntegrado("sspCarne_crear_msjeFoto") + (Integer.parseInt(ejbSbParametroFacade.obtenerParametroXNombre("selTicket_sizeArchivo").getValor()) / 1024) + " Kb. ";
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

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    private String filtro, filtroEstado, filtroTipo, filtroTramite, filtroArea,
            filtroUsuario, filtroDNI, filtroRel, filtroActivo, filtroCerrado, 
            tipoBus = "ASUNTO", regreso = "List";
    private int selectedItemIndex, tabIndex;
    private boolean filtroUsuarioDisabled;
    private String nuevoNumero = "";

    public String getRegreso() {
        return regreso;
    }

    public void setRegreso(String regreso) {
        this.regreso = regreso;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public String getTipoBus() {
        return tipoBus;
    }

    public void setTipoBus(String tipoBus) {
        this.tipoBus = tipoBus;
    }

    public boolean isFiltroUsuarioDisabled() {
        return filtroUsuarioDisabled;
    }

    public void setFiltroUsuarioDisabled(boolean filtroUsuarioDisabled) {
        this.filtroUsuarioDisabled = filtroUsuarioDisabled;
    }

    public String getFiltroCerrado() {
        return filtroCerrado;
    }

    public void setFiltroCerrado(String filtroCerrado) {
        this.filtroCerrado = filtroCerrado;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getNuevoNumero() {
        return nuevoNumero;
    }

    public void setNuevoNumero(String nuevoNumero) {
        this.nuevoNumero = nuevoNumero;
    }

    public boolean isActivo() {
        return (this.getSelected().getActivo() == 1);
    }

    public String limpiarFiltro(String s) {
        if (s == null || s.equals("")) {
            return "%";
        }
        return s;
    }

    public boolean mostrarTransferir() {
        LoginController lc = (LoginController) JsfUtil.obtenerBean("loginController", LoginController.class);
        return (current.getUsuarioActual().getId() == (long) lc.getUsuario().getId());
    }

    public boolean mostrarCerrar() {
        return (current.getFechaFin() == null);
    }

    public String getFiltroTipo() {
        return filtroTipo;
    }

    public void setFiltroTipo(String filtroTipo) {
        this.filtroTipo = limpiarFiltro(filtroTipo);
    }

    public String getFiltroEstado() {
        return filtroEstado;
    }

    public void setFiltroEstado(String filtroEstado) {
        this.filtroEstado = limpiarFiltro(filtroEstado);
    }

    public String getFiltroTramite() {
        return filtroTramite;
    }

    public void setFiltroTramite(String filtroTramite) {
        this.filtroTramite = limpiarFiltro(filtroTramite);
    }

    public String getFiltroArea() {
        return filtroArea;
    }

    public void setFiltroArea(String filtroArea) {
        this.filtroArea = limpiarFiltro(filtroArea);
    }

    public String getFiltroUsuario() {
        return filtroUsuario;
    }

    public void setFiltroUsuario(String filtroUsuario) {
        this.filtroUsuario = limpiarFiltro(filtroUsuario);
    }

    public String getFiltroCanal() {
        return filtroDNI;
    }

    public void setFiltroCanal(String filtroDNI) {
        this.filtroDNI = limpiarFiltro(filtroDNI);
    }

    public void setActivo(boolean activo) {
        if (activo) {
            this.getSelected().setActivo((short) 1);
        } else {
            this.getSelected().setActivo((short) 0);
        }
    }

    public void updateActivar() {
        try {
            current = (Tickets) getItems().getRowData();
            selectedItemIndex = getItems().getRowIndex();
            current.setActivo((short) 1);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeRegistroActualizado"));
            buscar();
            recreateModel();
            //return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("PersistenceErrorOccured"));
            //return null;
        }
    }

    public void transferirTicket(SbUsuarioGt usr) {
        try {
            current.setUsuarioActual(usr);
            current.setFechaAct(new Date());
            getFacade().edit(current);
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("PersistenceErrorOccured"));
        }
    }

    public void cerrarTicket() {
        if ((current == null) || (current.getCerrado() == 0)) {
            current = (Tickets) getItems().getRowData();
        }
        try {
            TipoIntegradoController tc = (TipoIntegradoController) JsfUtil.obtenerBean("tipoIntegradoController", TipoIntegradoController.class);
            current.setFechaFin(GregorianCalendar.getInstance().getTime());
            current.setCerrado((short) 1);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("PersistenceErrorOccured"));
        }
    }

    public void abrirTicket() {
        try {
            TipoIntegradoController tc = (TipoIntegradoController) JsfUtil.obtenerBean("tipoIntegradoController", TipoIntegradoController.class);
            current.setFechaFin(null);
            current.setCerrado((short) 0);
            current.setEstado(tc.tipoPorCodProg("TKEST_01"));
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("PersistenceErrorOccured"));
        }
    }

    public boolean verAnular(short estado) {
        return estado == (short) 1;
    }

    public void updateAnular() {
        try {
            current = (Tickets) getItems().getRowData();
            if (getFacade().selectCountRel(current.getId()) > 0) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("ErrorTicketConRelaciones"));
                return;
            }
            selectedItemIndex = getItems().getRowIndex();
            current.setActivo((short) 0);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeRegistroActualizado"));
            buscar();
            recreateModel();
            //return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("PersistenceErrorOccured"));
            //return null;
        }
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public void buscarRelBusqueda() {
        itemsRelBusqueda = new ListDataModel(getFacade().selectRelBusqueda(filtroRel));
    }

    public void buscarSim(String dni) {
        itemsSim = new ListDataModel(getFacade().selectSim(dni));
    }

    public void buscarRel(Long id) {
        itemsRel = new ListDataModel(getFacade().selectRel(id));
    }
    public void buscarRespuesta(Long id,String codProg) {
       List<TicketObs> listTicketObs = ejbFacadeTicketObs.getTicketObsSel(id, codProg);
       if(listTicketObs!=null){
           if(listTicketObs.size()>0)
                respuesta=listTicketObs.get(0).getObservacion();
           else
            respuesta="";
       }else
           respuesta="";
    }

    public String buscar() {
        items = createDataModel(filtro);
        return "List";
    }

    public DataModel createDataModel(String filtro) {
        //Se cambia filtroUsuario por ruc
        LoginController lc = (LoginController) JsfUtil.obtenerBean("loginController", LoginController.class);
        String ruc = lc.getUsuario().getPersona().getRuc();         
        return new ListDataModel(getFacade().selectLike(tipoBus, filtro, filtroEstado, filtroTipo, filtroTramite, filtroArea, ruc, filtroUsuario, filtroActivo, filtroCerrado));
    }

    public TicketsController() {
    }

    public String estiloPorAtencion(Tickets t) {
        short cerrado = t.getCerrado();
        if (cerrado == 1) {
            return "datatable-row-cerrado";
        }
        return "datatable-row-emitido";
    }

    public String getCerrado() {
        if ((current == null) || (current.getCerrado() == 0)) {
            return "ABIERTO";
        }
        return "CERRADO";
    }
    
    public short permisoPorAtencion(Tickets t) {
        short cerrado = t.getCerrado();
        return cerrado; 
        /*
        if (t.getCerrado() ) {
            return false;
        }
        return true;
        */
    }

    public Tickets getSelected() {
        if (current == null) {
            current = new Tickets();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TicketsFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        recreateModel();
        filtro = "";
        filtroEstado = "%";
        filtroTipo = "%";
        filtroTramite = "%";
        filtroArea = "%";
        filtroUsuario = "%";
        filtroDNI = "%";
        filtroActivo = "%";
        filtroCerrado = "%";
        filtroUsuarioDisabled = false;
        return "/aplicacion/tickets/List";
    }

    public String prepareListMios() {
        LoginController lc = (LoginController) JsfUtil.obtenerBean("loginController", LoginController.class);
        recreateModel();
        filtro = "";
        filtroEstado = "%";
        filtroTipo = "%";
        filtroTramite = "%";
        filtroArea = "%";
        filtroUsuario = lc.getUsuario().getLogin();
        filtroDNI = "%";
        filtroActivo = "1";
        filtroCerrado = "%";
        filtroUsuarioDisabled = true;
        return "/aplicacion/tickets/List";
    }

    public String prepareViewRelBusqueda() {
        current = getFacade().find(current.getTicketRel().getId());
        tabIndex = 0;
        ticketObsController.prepareListFromTickets(current.getId());
        ticketRutasController.prepareListFromTickets(current.getId());
        buscarRel(current.getId());
        return "View";
    }

    public String prepareViewRel() {
        current = (Tickets) getItemsRel().getRowData();
        selectedItemIndex = getItemsRel().getRowIndex();
        tabIndex = 0;
        ticketObsController.prepareListFromTickets(current.getId());
        ticketRutasController.prepareListFromTickets(current.getId());
        buscarRel(current.getId());
        return "View";
    }

    public String prepareViewSim() {
        current = (Tickets) getItemsSim().getRowData();
        selectedItemIndex = getItemsSim().getRowIndex();
        tabIndex = 0;
        ticketObsController.prepareListFromTickets(current.getId());
        ticketRutasController.prepareListFromTickets(current.getId());
        buscarSim(current.getDni());
        return "View";
    }

    public String prepareViewFromRutas(Tickets c) {
        regreso = "ListDerivados";
        itemsRelBusqueda = null;
        filtroRel = "";
        current = c;
        selectedItemIndex = 0;
        tabIndex = 0;
        ticketObsController.prepareListFromTickets(current.getId());
        ticketRutasController.prepareListFromTickets(current.getId());
        buscarRel(current.getId());
        buscarSim(current.getDni());
        return "View";
    }

    public String prepareView() {
        regreso = "List";
        itemsRelBusqueda = null;
        filtroRel = "";
        current = (Tickets) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        tabIndex = 0;
        ticketObsController.prepareListFromTickets(current.getId());
        ticketRutasController.prepareListFromTickets(current.getId());
        buscarRel(current.getId());
        buscarSim(current.getDni());
        buscarRespuesta(current.getId(), "TKRSEL_01");
        return "View";
    }

    public void buscarSimilares() {
        buscarSim(current.getDni());
    }

    public String prepareViewDeEdit() {
        itemsRelBusqueda = null;
        filtroRel = "";
        tabIndex = 0;
        ticketObsController.prepareListFromTickets(current.getId());
        ticketRutasController.prepareListFromTickets(current.getId());
        buscarRel(current.getId());
        buscarSim(current.getDni());
        return "View";
    }

    public String prepareCreate() {
        return prepareCreate(true);
    }

    public String prepareCreate(boolean borrarNumero) {
        if (borrarNumero) {
            nuevoNumero = "";
        }

        current = new Tickets();
        lsFotoByte = null;
        lsTicketArchivo = null;
        lsNombreTemporal = null;
        crearTicketCerrado = false;
        selectedItemIndex = -1;
        return "/aplicacion/tickets/Create"; 
    }

    public String prepareCreateConsultaWeb() {
        current = new Tickets();
        crearTicketCerrado = false;
        nuevoNumero = "";
        selectedItemIndex = -1;
        return "/aplicacion/tickets/ConsultaWeb";
    }

    public String prepareCreateSugerenciaWeb() {
        current = new Tickets();
        crearTicketCerrado = false;
        nuevoNumero = "";
        selectedItemIndex = -1;
        return "/aplicacion/tickets/SugerenciaWeb";
    }

    public String create() {
        try {
            nuevoNumero = "";
            TipoIntegradoController tc = (TipoIntegradoController) JsfUtil.obtenerBean("tipoIntegradoController", TipoIntegradoController.class);
            TipoBaseController tbc = (TipoBaseController) JsfUtil.obtenerBean("tipoBaseController", TipoBaseController.class);
            LoginController lc = (LoginController) JsfUtil.obtenerBean("loginController", LoginController.class);
            
            SiTipo canal = tc.tipoPorCodProg("TKCAN_AS");
            current.setCanal(canal);
            current.setDni(lc.getUsuario().getLogin()) ;
            current.setNombre(lc.getUsuario().getApellidosyNombres());
            current.setRuc(lc.getUsuario().getPersona().getRuc()) ;
            current.setArea(tipoBaseFacadeGt.buscarTipoBaseXCodProg("TP_AREA_OTR"));
            if (current.getOficioRef().trim().length() != 0) { 
                current.setOficioRef("Solicitud-"+current.getOficioRef().trim());
            }
            Date fecha = GregorianCalendar.getInstance().getTime();
            current.setFechaIni(fecha);
            current.setFechaAct(fecha);
            current.setActivo((short) 1);
            current.setImportado(0);
            current.setUsuario(new SbUsuario((long) lc.getUsuario().getId()));
            current.setUsuarioActual(canal.getUsuarioCanal());
            current.setEstado(tc.tipoPorCodProg("TKEST_01"));
            if (crearTicketCerrado) {
                current.setCerrado((short) 1);
                current.setFechaFin(current.getFechaIni());
            } else {
                current.setCerrado((short) 0); 
            }
            current.setNumero(obtenerNumero(tbc.tipoPorCodProg("TP_NUM_TK").getId()));            
            getFacade().create(current);
            
            //Para guardar la foto adjunta: 
            prepararRegistro();
            if (lsTicketArchivo != null) {
                for (int i = 0; i < lsFotoByte.size(); ++i) {
                        FileUtils.writeByteArrayToFile(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ticket").getValor() + lsTicketArchivo.get(i).getNombre()), lsFotoByte.get(i));
                }
            }
            
            //CREANDO RUTA DE TICKET
            TicketRutas ruta = new TicketRutas();
            ruta.setActivo((short) 1);
            ruta.setAudLogin(loginController.getUsuario().getLogin());
            ruta.setAudNumIp(loginController.getNumIP());
            ruta.setFechaHora(fecha);
            ruta.setTicket(current);
            ruta.setTipo(tc.tipoPorCodProg("TKRUT_01"));

            ruta.setUsuarioDestino(canal.getUsuarioCanal());
            ruta.setUsuario(new SbUsuarioGt((long) lc.getUsuario().getId()));

            ejbTicketRutasFacade.create(ruta);
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeRegistroCreado"));
            return prepareCreate(false);
        } catch (Exception e) {
            nuevoNumero = "";
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("ErrorDePersistencia"));
            return null;
        }
    }
    
    public void prepararRegistro() {
        if (lsTicketArchivo != null) {
            if (current.getTicketArchivoList() == null || current.getTicketArchivoList().isEmpty()) {
                current.setTicketArchivoList(new ArrayList());
            }
            for (TicketArchivo archivo : lsTicketArchivo) {
                archivo.setActivo(JsfUtil.TRUE);
                archivo.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                archivo.setAudNumIp(JsfUtil.getIpAddress());
                archivo.setId(null);
                archivo.setTicketId(current);
                archivo.setNombre(archivo.getNombre());
                archivo.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_TARCH_JPG"));
                archivo = (TicketArchivo) JsfUtil.entidadMayusculas(archivo, "");
                ticketArchivoFacade.create(archivo);
                current.getTicketArchivoList().add(archivo);
            }
        }
               
    }
    
    
    public long obtenerNumero(long id) throws Exception {
        NumeracionController nc = (NumeracionController) JsfUtil.obtenerBean("numeracionController", NumeracionController.class);
        long num = nc.nuevoNumero(id);
        if (num == 0) {
            throw new Exception("Error de numeracion.");
        }
        nuevoNumero = ResourceBundle.getBundle("/BundleBDIntegrado").getString("TicketNumero")+ String.valueOf(num);
        return num;
    }

    public String createSugerenciaWeb() {
        try {
            TipoIntegradoController tc = (TipoIntegradoController) JsfUtil.obtenerBean("tipoIntegradoController", TipoIntegradoController.class);
            TipoBaseController tbc = (TipoBaseController) JsfUtil.obtenerBean("tipoBaseController", TipoBaseController.class);

            SbUsuarioController uc = (SbUsuarioController) JsfUtil.obtenerBean("usuarioController", SbUsuarioController.class);
            SiTipo canal = tc.tipoPorCodProg("TKCAN_01");
            nuevoNumero = "";
            Date fecha = GregorianCalendar.getInstance().getTime();
            current.setFechaIni(fecha);
            current.setFechaAct(fecha);
            current.setActivo((short) 1);
            current.setImportado(0);
            current.setTipo(tc.tipoPorCodProg("TKTIP_03"));
            current.setUsuario(uc.getUsuario("WEB"));
            current.setUsuarioActual(canal.getUsuarioCanal());
            current.setTramite(tc.tipoPorCodProg("TKTRM_01"));
            current.setEstado(tc.tipoPorCodProg("TKEST_01"));
            current.setCanal(canal);
            current.setNumero(obtenerNumero(tbc.tipoPorCodProg("TP_NUM_TK").getId()));
            current.setArea(tipoBaseFacadeGt.selectAreaPorSiglas("GG"));
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeSugerenciaCreada"));
            return prepareCreateSugerenciaWeb();
        } catch (Exception e) {
            nuevoNumero = "";
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("ErrorDePersistencia"));
            return null;
        }
    }

    public String createConsultaWeb() {
        try {
            TipoIntegradoController tc = (TipoIntegradoController) JsfUtil.obtenerBean("tipoIntegradoController", TipoIntegradoController.class);
            TipoBaseController tbc = (TipoBaseController) JsfUtil.obtenerBean("tipoBaseController", TipoBaseController.class);

            SbUsuarioController uc = (SbUsuarioController) JsfUtil.obtenerBean("usuarioController", SbUsuarioController.class);
            SiTipo canal = tc.tipoPorCodProg("TKCAN_01");
            nuevoNumero = "";
            Date fecha = GregorianCalendar.getInstance().getTime();
            current.setFechaIni(fecha);
            current.setFechaAct(fecha);
            current.setActivo((short) 1);
            current.setImportado(0);
            current.setTipo(tc.tipoPorCodProg("TKTIP_02"));
            current.setUsuario(uc.getUsuario("WEB"));
            current.setUsuarioActual(canal.getUsuarioCanal());
            current.setTramite(tc.tipoPorCodProg("TKTRM_01"));
            current.setEstado(tc.tipoPorCodProg("TKEST_01"));
            current.setCanal(canal);
            current.setNumero(obtenerNumero(tbc.tipoPorCodProg("TP_NUM_TK").getId()));
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeConsultaCreada"));
            return prepareCreateConsultaWeb();
        } catch (Exception e) {
            nuevoNumero = "";
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("ErrorDePersistencia"));
            return null;
        }
    }

    public void eliminarRel() {
        try {
            current.setTicketRel(null);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("ErrorDePersistencia"));
            System.err.print("TicketsObsController.createFromTickets():" + e.getMessage());
        }
    }

    public void relacionarTicket() {
        try {
            Tickets t = (Tickets) getItemsRelBusqueda().getRowData();
            if (Objects.equals(t.getId(), current.getId())) {
                JsfUtil.addErrorMessage(new Exception(), ResourceBundle.getBundle("/BundleBDIntegrado").getString("ErrorRelacionTicket"));
            } else {
                current.setTicketRel(t);
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeRegistroActualizado"));
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("ErrorDePersistencia"));
            System.err.print("TicketsObsController.createFromTickets():" + e.getMessage());
        }
    }

    public String prepareEdit() {
        regreso = "List";
        current = (Tickets) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        return "Edit";
    }

    public void updateEstado() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("ErrorDePersistencia"));
        }
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeRegistroActualizado"));
            return "Edit";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("ErrorDePersistencia"));
            return null;
        }
    }

    public String destroy() {
        current = (Tickets) getItems().getRowData();
        selectedItemIndex = getItems().getRowIndex();
        performDestroy();
        buscar();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/BundleBDIntegrado").getString("MensajeRegistroBorrado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/BundleBDIntegrado").getString("ErrorDePersistencia"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = createDataModel(filtro);
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Tickets getTickets(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Tickets.class)
    public static class TicketsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TicketsController controller = (TicketsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ticketsController");
            return controller.getTickets(getKey(value));
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
            if (object instanceof Tickets) {
                Tickets o = (Tickets) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Tickets.class.getName());
            }
        }

    }
    
    public void openDlgGaleriaFotos(List <TicketArchivo> lsTicketArchivo, boolean cargarFoto){
        lstGaleriaFotos = new ArrayList();
        byte[] bytesFoto = null; 
        Map nuevaFoto = null;

        try {
            String pathFoto = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_ticket").getValor();

            for (TicketArchivo archivo : lsTicketArchivo) {  
                bytesFoto = FileUtils.readFileToByteArray(new File(pathFoto + archivo.getNombre()));
                nuevaFoto = new HashMap();
                nuevaFoto.put("nombre", archivo.getNombre());
                nuevaFoto.put("byte", bytesFoto);
                lstGaleriaFotos.add(nuevaFoto);
            }  
        } catch (Exception e) {
            e.printStackTrace();
        }                

        if(lstGaleriaFotos.isEmpty()){
            JsfUtil.mensajeError("No se pudo cargar las fotos");
            return;
        }        
        RequestContext.getCurrentInstance().execute("PF('wvGaleriaFoto').show()");
        RequestContext.getCurrentInstance().update("frmGaleriaFoto");
    }
    
    public StreamedContent streamedContentGaleriaFoto(Map fotoSelected) {
        try {
            if(fotoSelected == null){
                return null;
            }
            byte[] fotoByte = (byte[]) fotoSelected.get("byte");
            return new DefaultStreamedContent(new ByteArrayInputStream(fotoByte), "image/jpeg");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar la foto");
        }
        return null;
    }
    
    public boolean tieneFotos(List <TicketArchivo> lsTicketArchivo) {
        return lsTicketArchivo.size() > 0;
    }
    
    public void borrarFotoTemporal(String fotoSeleccionada) {
        try{
            if (lsNombreTemporal != null) {
                for (int i = 0; i < lsNombreTemporal.size(); ++i) {
                    if (lsNombreTemporal.get(i).equals(fotoSeleccionada)){
                        lsNombreTemporal.remove(i);
                        lsTicketArchivo.remove(i);
                        lsFotoByte.remove(i);
                    }
                }
                if (lsNombreTemporal.isEmpty()){
                    lsNombreTemporal= null;
                    lsTicketArchivo= null;
                    lsFotoByte= null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al borrar la foto");
        }
    }
}
