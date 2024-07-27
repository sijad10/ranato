package pe.gob.sucamec.pub.jsf;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.faces.context.ExternalContext;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;
import pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SiTipoFacade;
import pe.gob.sucamec.bdintegrado.bean.TicketsFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.SiTipo;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.Tickets;
import pe.gob.sucamec.sel.epp.beans.GeppConsultaFeriasFacade;

// Nombre de la instancia en la aplicacion //
@Named("oficinaPubController")
@SessionScoped

/**
 * Clase con SbDepartamentoController instanciada como sbDepartamentoController.
 * Contiene funciones utiles para la entidad SbDepartamento. Esta vinculada a
 * las páginas SbDepartamento/create.xhtml, SbDepartamento/update.xhtml,
 * SbDepartamento/list.xhtml, SbDepartamento/view.xhtml Nota: Las tablas deben
 * tener la estructura de Sucamec para que funcione adecuadamente, revisar si
 * tiene el campo activo y modificar las búsquedas.
 */
public class OficinaPubController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    private String filtro;
    private TipoBaseGt oficina = null;
    private MapModel modelMap;
    private Marker markerSelected;
    private String google_srcapi_key = "";
    private String google_jsapi = "";

    private String direccion;
    private String distrito;
    private String referencia;
    private double lat;
    private double lng;
    private String descripcion;
    private String latitudParameter;
    private String longitudParameter;
    private String hidden;
    private String lastMap = "";
    private int medidaPopUp = 100;
    private boolean blnFin;
    private String hiddenParameter;
    private String token = "";

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private SbParametroFacade ejbSbParametroFacade;
    @EJB
    private TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private SbNumeracionFacade ejbSbNumeracionFacade;
    @EJB
    private SiTipoFacade ejbSiTipoFacade;
    @EJB
    private SbUsuarioFacadeGt ejbSbUsuarioFacadeGt;
    @EJB
    private TicketsFacade ejbTicketsFacade;
    @EJB
    private GeppConsultaFeriasFacade ejbGeppConsultaFeriasFacade;

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

    public TipoBaseGt getOficina() {
        return oficina;
    }

    public void setOficina(TipoBaseGt oficina) {
        this.oficina = oficina;
    }

    public MapModel getModelMap() {
        return modelMap;
    }

    public void setModelMap(MapModel modelMap) {
        this.modelMap = modelMap;
    }

    public Marker getMarkerSelected() {
        return markerSelected;
    }

    public void setMarkerSelected(Marker markerSelected) {
        this.markerSelected = markerSelected;
    }

    public String getGoogle_srcapi_key() {
        return google_srcapi_key;
    }

    public void setGoogle_srcapi_key(String google_srcapi_key) {
        this.google_srcapi_key = google_srcapi_key;
    }

    public String getGoogle_jsapi() {
        return google_jsapi;
    }

    public void setGoogle_jsapi(String google_jsapi) {
        this.google_jsapi = google_jsapi;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getLastMap() {
        return lastMap;
    }

    public void setLastMap(String lastMap) {
        this.lastMap = lastMap;
    }

    public String getLatitudParameter() {
        return latitudParameter;
    }

    public void setLatitudParameter(String latitudParameter) {
        this.latitudParameter = latitudParameter;
    }

    public String getLongitudParameter() {
        return longitudParameter;
    }

    public void setLongitudParameter(String longitudParameter) {
        this.longitudParameter = longitudParameter;
    }

    public int getMedidaPopUp() {
        return medidaPopUp;
    }

    public void setMedidaPopUp(int medidaPopUp) {
        this.medidaPopUp = medidaPopUp;
    }

    public boolean isBlnFin() {
        return blnFin;
    }

    public void setBlnFin(boolean blnFin) {
        this.blnFin = blnFin;
    }

    public String getHiddenParameter() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        String latitude = (String) ec.getRequestParameterMap().get("latitude");
        String longitude = (String) ec.getRequestParameterMap().get("longitude");
        String tokenTemp = (String) ec.getRequestParameterMap().get("token");
        hiddenParameter = latitude + "," + longitude;

        if (tokenTemp == null) {
            tokenTemp = "";
        }
        if (!tokenTemp.isEmpty() && !token.equals(tokenTemp)) {
            direccion = null;
            referencia = null;
            descripcion = null;
            token = tokenTemp;
        }

        if (latitude != null && longitude != null && !latitude.isEmpty() && !longitude.isEmpty()) {
            lastMap = latitude + "," + longitude;
            if (latitude != null) {
                lat = Double.parseDouble(latitude);
            }
            if (longitude != null) {
                lng = Double.parseDouble(longitude);
            }
        }
        return lastMap;
    }

    public void setHiddenParameter(String hiddenParameter) {
        this.hiddenParameter = hiddenParameter;
    }

    /**
     * Constructor
     *
     */
    public OficinaPubController() {
        estado = EstadoCrud.BUSCAR;
    }

    ////////////////////////////////////////////////////////
    public Date getFechaDia() {
        return new Date();
    }

    public List<TipoBaseGt> getListadoAreas() {
        //area = ejbTipoBaseFacade.tipoBaseXCodProg("TP_AREA_SN");     
        String areasCodProgs = ejbSbParametroFacade.obtenerParametroXNombre("sbPublicacionDoc_areas").getValor();
        return ejbTipoBaseFacade.lstTiposXCodigosProg(areasCodProgs);
    }

    public String obtenerMapCenterBuscarMap() {
        LatLng coord1 = null;
        modelMap = new DefaultMapModel();

        //Marker SUCAMEC
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_lima")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_lima")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_TRAM"));

        //Marker Ancash
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_ancash")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_ancash")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_ANCASH"));

        //Marker Arequipa
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_arequipa")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_arequipa")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_AQP"));

        //Marker Cajamarca
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_cajamarca")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_cajamarca")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_CAJAMARCA"));

        //Marker Chiclayo
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_chiclayo")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_chiclayo")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_CHICLA"));

        //Marker Cusco
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_cusco")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_cusco")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_CUSCO"));

        //Marker Ica
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_ica")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_ica")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_ICA"));

        //Marker Junin
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_junin")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_junin")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_JUNIN"));

        //Marker La Libertad
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_laLibertad")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_laLibertad")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_LIBERTA"));

        //Marker Piura
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_piura")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_piura")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_PIURA"));

        //Marker Puno
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_puno")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_puno")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_PUNO"));

        //Marker Tacna
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_tacna")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_tacna")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_TACNA"));

        //Marker Iquitos
        coord1 = new LatLng(Double.parseDouble(JsfUtil.bundleBDIntegrado("google_latitud_iquitos")), Double.parseDouble(JsfUtil.bundleBDIntegrado("google_longitud_iquitos")));
        modelMap.addOverlay(new Marker(coord1, "TP_AREA_OD_LOR"));

        return JsfUtil.bundleBDIntegrado("google_coordenadas_pointCenter");
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        if (event == null) {
            return;
        }
        markerSelected = (Marker) event.getOverlay();
        if (markerSelected == null) {
            return;
        }
        oficina = ejbTipoBaseFacade.tipoBaseXCodProg(markerSelected.getTitle());
    }

    public String obtenerKeyGoogle() {
        if (google_srcapi_key == null || google_srcapi_key.isEmpty()) {
            google_srcapi_key = ejbSbParametroFacade.obtenerParametroXNombre("google_srcapi_key").getValor();
        }
        return google_srcapi_key;
    }

    public String obtenerApiGoogle() {
        if (google_jsapi == null || google_jsapi.isEmpty()) {
            google_jsapi = ejbSbParametroFacade.obtenerParametroXNombre("google_jsapi").getValor();
        }
        return google_jsapi;
    }

    public String obtenerTituloOficina() {
        if (oficina == null) {
            oficina = ejbTipoBaseFacade.tipoBaseXCodProg("TP_AREA_TRAM");
        }
        if (oficina.getCodProg().equals("TP_AREA_TRAM")) {
            return JsfUtil.bundleBDIntegrado("oficinaPub_lima_title");
        }
        return oficina.getNombre();
    }

    public String obtenerImagenOficina() {
        if (oficina == null) {
            oficina = ejbTipoBaseFacade.tipoBaseXCodProg("TP_AREA_TRAM");
        }
        switch (oficina.getCodProg()) {
            case "TP_AREA_TRAM":
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_lima_imagen").getValor();
            //return JsfUtil.bundleBDIntegrado("oficinaPub_lima_imagen");
            case "TP_AREA_OD_PIURA":
                //return JsfUtil.bundleBDIntegrado("oficinaPub_piura_imagen");
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_piura_imagen").getValor();
            case "TP_AREA_OD_CHICLA":
                //return JsfUtil.bundleBDIntegrado("oficinaPub_chiclayo_imagen");
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_chiclayo_imagen").getValor();
            case "TP_AREA_OD_CAJAMARCA":
                //return JsfUtil.bundleBDIntegrado("oficinaPub_cajamarca_imagen");
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_cajamarca_imagen").getValor();
            case "TP_AREA_OD_LOR":
                //return JsfUtil.bundleBDIntegrado("oficinaPub_iquitos_imagen");
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_iquitos_imagen").getValor();
            case "TP_AREA_OD_LIBERTA":
                //return JsfUtil.bundleBDIntegrado("oficinaPub_trujillo_imagen");
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_trujillo_imagen").getValor();
            case "TP_AREA_OD_ANCASH":
                //return JsfUtil.bundleBDIntegrado("oficinaPub_ancash_imagen");
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_ancash_imagen").getValor();
            case "TP_AREA_OD_JUNIN":
                //return JsfUtil.bundleBDIntegrado("oficinaPub_junin_imagen");
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_junin_imagen").getValor();
            case "TP_AREA_OD_ICA":
                //return JsfUtil.bundleBDIntegrado("oficinaPub_ica_imagen");
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_ica_imagen").getValor();
            case "TP_AREA_OD_CUSCO":
                //return JsfUtil.bundleBDIntegrado("oficinaPub_cusco_imagen");
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_cusco_imagen").getValor();
            case "TP_AREA_OD_PUNO":
                //return JsfUtil.bundleBDIntegrado("oficinaPub_puno_imagen");
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_puno_imagen").getValor();
            case "TP_AREA_OD_AQP":
                //return JsfUtil.bundleBDIntegrado("oficinaPub_arequipa_imagen");
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_arequipa_imagen").getValor();
            case "TP_AREA_OD_TACNA":
                //return JsfUtil.bundleBDIntegrado("oficinaPub_tacna_imagen");            
                return ejbSbParametroFacade.obtenerParametroXNombre("oficinaPub_tacna_imagen").getValor();
        }

        return null;
    }

    public String obtenerDireccionOficina() {
        if (oficina == null) {
            oficina = ejbTipoBaseFacade.tipoBaseXCodProg("TP_AREA_TRAM");
        }
        switch (oficina.getCodProg()) {
            case "TP_AREA_TRAM":
                return JsfUtil.bundleBDIntegrado("oficinaPub_lima_direccion");
            case "TP_AREA_OD_PIURA":
                return JsfUtil.bundleBDIntegrado("oficinaPub_piura_direccion");
            case "TP_AREA_OD_CHICLA":
                return JsfUtil.bundleBDIntegrado("oficinaPub_chiclayo_direccion");
            case "TP_AREA_OD_CAJAMARCA":
                return JsfUtil.bundleBDIntegrado("oficinaPub_cajamarca_direccion");
            case "TP_AREA_OD_LOR":
                return JsfUtil.bundleBDIntegrado("oficinaPub_iquitos_direccion");
            case "TP_AREA_OD_LIBERTA":
                return JsfUtil.bundleBDIntegrado("oficinaPub_trujillo_direccion");
            case "TP_AREA_OD_ANCASH":
                return JsfUtil.bundleBDIntegrado("oficinaPub_ancash_direccion");
            case "TP_AREA_OD_JUNIN":
                return JsfUtil.bundleBDIntegrado("oficinaPub_junin_direccion");
            case "TP_AREA_OD_ICA":
                return JsfUtil.bundleBDIntegrado("oficinaPub_ica_direccion");
            case "TP_AREA_OD_CUSCO":
                return JsfUtil.bundleBDIntegrado("oficinaPub_cusco_direccion");
            case "TP_AREA_OD_PUNO":
                return JsfUtil.bundleBDIntegrado("oficinaPub_puno_direccion");
            case "TP_AREA_OD_AQP":
                return JsfUtil.bundleBDIntegrado("oficinaPub_arequipa_direccion");
            case "TP_AREA_OD_TACNA":
                return JsfUtil.bundleBDIntegrado("oficinaPub_tacna_direccion");

        }

        return null;
    }

    public void addMarker() {
        if (direccion == null) {
            direccion = "";
        }
        if (descripcion == null) {
            descripcion = "";
        }

        try {
            if (direccion.trim().isEmpty()) {
                JsfUtil.mensajeAdvertencia("Por favor completar la dirección");
            } else if (descripcion.trim().isEmpty()) {
                JsfUtil.mensajeAdvertencia("Por favor completar la descripción");
            } else {
                Marker marker = new Marker(new LatLng(lat, lng), direccion);
                modelMap.addOverlay(marker);
                // registrar

//                Tickets registro = new Tickets();
//                SiTipo canal = ejbSiTipoFacade.tipoBaseXCodProg("TKCAN_06");
//                SbUsuarioGt uc = ejbSbUsuarioFacadeGt.buscarUsuarioByLogin("APPMOVIL");
//                registro.setNumero(ejbSbNumeracionFacade.buscarNumeracionActual("TP_NUM_TK"));
//                registro.setNombre("APP SUCAMEC MÓVIL");
//                registro.setDni("98765432");
//                registro.setAsunto("REPORTE DE FERIA NO AUTORIZADA");
//                registro.setTelefono("99999999");
//                registro.setFechaIni(new Date());
//                registro.setFechaAct(new Date());
//                registro.setActivo((short) 1);
//                registro.setImportado(0);
//                registro.setTipo(ejbSiTipoFacade.tipoBaseXCodProg("TKTIP_05"));
//                registro.setUsuario(uc);
//                registro.setUsuarioActual(canal.getUsuarioCanal());
//                registro.setTramite(ejbSiTipoFacade.tipoBaseXCodProg("TKTRM_12"));
//                registro.setEstado(ejbSiTipoFacade.tipoBaseXCodProg("TKEST_01"));
//                registro.setCanal(canal);
//                registro.setArea(ejbTipoBaseFacade.tipoBaseXCodProg("TP_AREA_GCF"));
//                registro.setCerrado((short) 0);                
//                registro.setDescripcion("UBICACION FERIA NO AUTORIZADA\n\nDirección: "+direccion+"\n\nReferencia: "+referencia+"\n\nDescripción: "+descripcion+"\n\nPor favor ingresar al siguiente enlace:\n http://maps.google.com/maps?z=18&q=loc:"+latitudParameter);
//                ejbTicketsFacade.create(registro);
                // enviar correo
                //JsfUtil.mensajeAdvertencia("Mensaje enviado");
                String asunto = "[DENUNCIA PIROTECNIASEGURA2019]";
                String mensaje = "REPORTE DE FERIA NO AUTORIZADA MEDIANTE EL APP SUCAMEC MÓVIL\n\nDirección del lugar: " + direccion + "\n\nReferencia: " + referencia + "\n\nDescripción del reporte: " + descripcion
                        + "\n\nPor favor ingresar al siguiente enlace para ver el lugar reportado:\n http://maps.google.com/maps?z=18&q=loc:" + latitudParameter;
                String correos = ejbSbParametroFacade.obtenerParametroXNombre("ferias_reporte_feria_noaut_correos_destino").getValor();
                String[] correosArray = correos.split(",");
                for (String correo : correosArray) {
                    JsfUtil.enviarCorreo(correo, asunto, mensaje);
                }

                descripcion = "";
                direccion = "";
                referencia = "";
                blnFin = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String mostrarVer() {
        System.err.println("entro");
        return "pm:verPage";
        //RequestContext.getCurrentInstance().execute("PF('wvConfirmacionDlg').show()");
        //RequestContext.getCurrentInstance().update("confirmForm");

    }

    public String obtenerMapCenterMovilMap() {
        modelMap = new DefaultMapModel();

        if (lastMap.isEmpty()) {
            lastMap = JsfUtil.bundleBDIntegrado("google_coordenadas_sucamec");
        }
        return lastMap;
    }

    public String getHidden() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        hidden = (String) ec.getRequestParameterMap().get("latlng");
        if (hidden != null) {
            latitudParameter = hidden;
            blnFin = false;
        }
        return latitudParameter;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    public void onMarkerSelectWeb(OverlaySelectEvent event) {
        if (event == null) {
            return;
        }
        markerSelected = (Marker) event.getOverlay();
        if (markerSelected == null) {
            return;
        }
    }

    public void onPointSelect(PointSelectEvent event) {
        LatLng latlng = event.getLatLng();
        lastMap = latlng.getLat() + "," + latlng.getLng();
        modelMap = new DefaultMapModel();
    }

    public String SeparararDMS(String coordenada, int type) {
        String grados = null;
        String minutos = null;
        String segundos = null;
        String direccion = null;

        switch (type) {
            case 1: // latitude
                grados = coordenada.substring(0, 2);
                minutos = coordenada.substring(2, 4);
                segundos = coordenada.substring(5, coordenada.length() - 1);
                break;
            case 2: // longitude
                grados = coordenada.substring(0, 3);
                minutos = coordenada.substring(3, 5);
                segundos = coordenada.substring(6, coordenada.length() - 1);
                break;
            default:

        }

        double sec = 0;
        try {
            sec = Double.parseDouble("0." + segundos);
        } catch (Exception e) {

        }

        sec = (sec * 60);
        direccion = coordenada.substring(coordenada.length() - 1);

        String data = grados + "°" + minutos + "'" + sec + "''" + direccion;
        return data;
    }

    public String obtenerMapCenterFeriasAutorizadas() {
        LatLng coord1 = null;
        modelMap = new DefaultMapModel();

        List<Map> listFerias = ejbGeppConsultaFeriasFacade.listarFerias();

        if (listFerias != null && !listFerias.isEmpty()) {
            for (Map item : listFerias) {
                //Marker Ferias
                coord1 = new LatLng(Double.parseDouble(item.get("LATITUD").toString()), Double.parseDouble(item.get("LONGITUD").toString()));
                modelMap.addOverlay(new Marker(coord1, item.get("PROPIETARIO").toString(), "<b><u>DIRECCIÓN:</u></b> " + item.get("DIRECCION").toString() + "<div class='datosMarker'><b><u>RESOLUCIÓN:</u></b> " + item.get("NUMERO").toString() + "</div>"));
            }
        }
        return JsfUtil.bundleBDIntegrado("google_coordenadas_pointCenter");
    }

    public void onMarkerSelectFeria(OverlaySelectEvent event) {
        if (event == null) {
            return;
        }
        markerSelected = (Marker) event.getOverlay();
        if (markerSelected == null) {
            return;
        }
    }
}
