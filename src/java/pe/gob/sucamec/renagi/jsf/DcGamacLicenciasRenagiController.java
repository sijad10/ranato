/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.jsf;

/**
 *
 * @author rmoscoso
 */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaFoto;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.bdintegrado.data.AmaLicenciaDeUso;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbParametro;
import pe.gob.sucamec.renagi.jsf.util.EstadoCrud;
import pe.gob.sucamec.renagi.jsf.util.JsfUtil;
import pe.gob.sucamec.renagi.jsf.util.ReportUtil;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.seguridad.LogController;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

// Nombre de la instancia en la aplicacion //
@Named("dcGamacLicenciasRenagiController")
@SessionScoped

/**
 * Clase con DcGamacLicenciasController instanciada como
 * dcGamacLicenciasController. Contiene funciones utiles para la entidad
 * DcGamacLicencias. Esta vinculada a las páginas DcGamacLicencias/create.xhtml,
 * DcGamacLicencias/update.xhtml, DcGamacLicencias/list.xhtml,
 * DcGamacLicencias/view.xhtml Nota: Las tablas deben tener la estructura de
 * Sucamec para que funcione adecuadamente, revisar si tiene el campo activo y
 * modificar las búsquedas.
 */
public class DcGamacLicenciasRenagiController implements Serializable {

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

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<ArrayRecord> resultados = null;

    List<Map> listPropias = new ArrayList();
    List<Map> listPerJur = new ArrayList();
    
    AmaTarjetaPropiedad tarjeta;
    Expediente expTar;
    String armaCalibre;
    AmaInventarioArma inventarioArma;
    AmaGuiaTransito guiaT;
    String direInternamiento;
    String interDisca;    
    
    private SbParametro paramOriDatArma;
    private String desdeMigra;
    
    @Inject
    LoginController loginController;

    @Inject
    LogController logController;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.renagi.beans.ConsultasDiscaRenagiFacade consultasDiscaFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbPersonaFacade sbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaLicenciaDeUsoFacade ejbAmaLicenciaUso;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private pe.gob.sucamec.renagi.beans.InfoArmasFacade infoArmasFacade;    

    public DcGamacLicenciasRenagiController(String desdeMigra) {
        estado = EstadoCrud.BUSCAR;
    }

    @PostConstruct
    public void inicializar() {
        setDesdeMigra("MIGRA");
        // Parámetro origen data ARMAS
        setParamOriDatArma(ejbSbParametroFacade.obtenerParametroXSistemaCodProg("TP_ORIGDAT_ARMDIS", 5L));
        if (paramOriDatArma != null) {
            setDesdeMigra(paramOriDatArma.getValor());
        }
    }
    
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
            try {
                foto = JsfUtil.oracleTiffenJpg(consultasDiscaFacade.selectFoto(registro.get("DOC_PORTADOR").toString(), vigilante), 240, 320);                
            } catch (Exception e) {
                foto = null;
            }
        }
    }

    public DefaultStreamedContent getFoto() {
        if (foto == null) {
            try {
                return new DefaultStreamedContent(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/ninguno_320.jpg"), "image/jpg");
            } catch (Exception ex) {
                ex.printStackTrace();
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

    /**
     * Propiedad para el estado del CRUD.
     *
     * @return el estado actual del CRUD.
     */
    public EstadoCrud getEstado() {
        return estado;
    }

    public AmaTarjetaPropiedad getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(AmaTarjetaPropiedad tarjeta) {
        this.tarjeta = tarjeta;
    }

    public Expediente getExpTar() {
        return expTar;
    }

    public void setExpTar(Expediente expTar) {
        this.expTar = expTar;
    }

    public String getArmaCalibre() {
        return armaCalibre;
    }

    public void setArmaCalibre(String armaCalibre) {
        this.armaCalibre = armaCalibre;
    }

    public AmaInventarioArma getInventarioArma() {
        return inventarioArma;
    }

    public void setInventarioArma(AmaInventarioArma inventarioArma) {
        this.inventarioArma = inventarioArma;
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

    public String getInterDisca() {
        return interDisca;
    }

    public void setInterDisca(String interDisca) {
        this.interDisca = interDisca;
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
        return "/aplicacion/consultasRenagi/DcGamacLicencias/List";
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
            if (tipo.equals("nom_prop") || tipo.equals("nom_portador")) {
                if(filtro.trim().length() <= 3){
                    JsfUtil.mensajeAdvertencia("Digitar por lo menos 4 carácteres para búsqueda!");
                } else{
                    buscar = true;  
                }
            } else {
                buscar = true;
            }
            
            if (buscar) {

                switch (desdeMigra) {
                    case "DISCA":
                        logController.escribirLogBuscar("" + tipo + "\t" + filtro, "INTEROP_WS_LICENCIAS");
                        resultados = new ListDataModel(consultasDiscaFacade.listarArmasxFiltro(tipo, filtro));
                        break;

                    case "MIGRA":
                        Long personaId = null;
                        if (tipo.equals("doc_prop")) {
                            SbPersona per = null;
                            if (filtro.length()<=10) {
                                per = sbPersonaFacade.findByNumDoc(filtro);
                            }
                            if (filtro.length()>=11) {
                                per = sbPersonaFacade.findByRuc(filtro);
                            }
                            if (per==null) {
                                per = sbPersonaFacade.findByNroCip(filtro);
                            }
                            if (per!=null) {
                                personaId = per.getId();
                            }                            
                        }
                        
                        logController.escribirLogBuscar("" + tipo + "\t" + filtro, "ARMA_GENERAL_MIGRA");
                        resultados = new ListDataModel(consultasDiscaFacade.listarArmasxFiltroMigra(tipo, filtro, personaId));
                        break;
                }
                
            }
            
        } else {
            resultados = null;
        }
        
        /*if (tipo.equals("licencia")) {
            licencia = filtro;
        }
        if (tipo.equals("serie")) {
            serie = filtro;
        }
        if (tipo.equals("doc_prop")) {
            doc_prop = filtro;
        }
        if (tipo.equals("nom_prop")) {
            nom_prop = filtro;
        }
        if (tipo.equals("doc_portador")) {
            doc_portador = filtro;
        }
        if (tipo.equals("nom_portador")) {
            nom_portador = filtro;
        }*/
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (ArrayRecord) resultados.getRowData();
        crearFoto();
        logController.escribirLogVer("" + registro.get("NRO_LIC"), "INTEROP_WS_LICENCIAS");
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
    public DcGamacLicenciasRenagiController() {
        estado = EstadoCrud.BUSCAR;
    }

    ////////////////// REPORTE //////////////////
    
    /**
     * PARAMETROS PARA GENERAR PDF
     * @author rarevalo
     * @version 1.0
     * @param ppdf Registro a crear el reporte
     * @return Listado de parámetros para reporte
     */
    private HashMap parametrosPdf(ArrayRecord reg) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();        
        HashMap ph = new HashMap();
        
        //String numDoc = reg.get("DOC_PORTADOR").toString().trim();
        String numDoc = (reg.get("DOC_PORTADOR")!=null)? reg.get("DOC_PORTADOR").toString().trim():reg.get("DOC_PROPIETARIO").toString();
        String tipoDoc = reg.get("TIPO_DOC").toString().trim();
        SbPersona persona = obtenerPersona( tipoDoc, numDoc );
        ph.put("P_logoSucamec", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png")); 
        
        if(persona!=null){
            ph.put("p_apePat", persona.getApePat());
            ph.put("p_apeMat", persona.getApeMat());
            ph.put("p_nombres", persona.getNombres()); 
            ph.put("p_dni", persona.getNumDoc());
            ph.put("p_sexo", "");            
            String fecNac = ReportUtil.mostrarFechaDdMmYyyy(persona.getFechaNac());
            ph.put("p_fecNac", (fecNac.equals("BORRADOR"))? "-":fecNac); 
            String[] dire = ReportUtil.mostrarDireccionPersonaAr(persona);
            ph.put("p_dire", (dire[0]!=null)? dire[0]:"-");
            ph.put("p_direDep", (dire[1]!=null)? dire[1]:"-");
            ph.put("p_direProv", (dire[2]!=null)? dire[2]:"-");
            ph.put("p_direDist", (dire[3]!=null)? dire[3]:"-");
            /*
            ph.put("P_TIPONRODOC", ReportUtil.mostrarTipoYNroDocumemtoString(ppdf.getPersonaId()));
            ph.put("P_EMPRESASTRING", ReportUtil.mostrarClienteString(ppdf.getPersonaId()));
            ph.put("P_CODTRIBUTO", String.valueOf(ppdf.getReciboId().getCodTributo()) );
            ph.put("P_MONTO", String.valueOf(new DecimalFormat("###,###,###,##0.00").getInstance(Locale.ENGLISH).format( ppdf.getReciboId().getImporte()  )));
            ph.put("P_NROSECUENCIA", String.valueOf(ppdf.getReciboId().getNroSecuencia()));
            ph.put("P_FECHAPAGO", ReportUtil.formatoFechaDdMmYyyy(ppdf.getReciboId().getFechaMovimiento()));
            ph.put("P_CODOFICINA", String.valueOf(ppdf.getReciboId().getCodOficina()) );
            */
            
            switch (tipoDoc) {
                case "DNI":
                case "CE":
                case "CIP":
                    ph.put("p_numDoc", persona.getNumDoc());
                    if (tipoDoc.equals("CIP")) {
                        ph.put("p_tipoDoc", tipoDoc + " / DNI");
                        ph.put("p_numDoc", persona.getNroCip() + " / " + persona.getNumDoc());
                    }
                    List<AmaLicenciaDeUso> listLic = ejbAmaLicenciaUso.listarAmaLicenciaXPerLicencia(persona.getId());
                    if (!listLic.isEmpty()) {
                        try {
                            AmaLicenciaDeUso lic = listLic.get(0);
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
            }
        }else {
            ListDataModel<ArrayRecord> rsPersona = new ListDataModel(consultasDiscaFacade.listarPersonaNatural(numDoc));
            if(rsPersona.getRowCount()>0){
                Iterator<ArrayRecord> iteRs = rsPersona.iterator();
                while (iteRs.hasNext()) {
                    ArrayRecord row = iteRs.next();
                    ph.put("p_apePat", row.get("APE_PAT").toString());
                    ph.put("p_apeMat", row.get("APE_MAT").toString());
                    ph.put("p_nombres", row.get("NOMBRE").toString()); 
                    ph.put("p_dni", row.get("COD_USR").toString());
                    String fecNac = ReportUtil.mostrarFechaDdMmYyyy((Date) row.get("FEC_NAC"));                    
                    ph.put("p_fecNac", (fecNac.equals("BORRADOR"))? "-":fecNac); 
                    ph.put("p_sexo", row.get("SEXO").toString());               
                    ph.put("p_dire", row.get("DOMICILIO").toString());
                    ph.put("p_direDep", row.get("NOM_DPTO").toString());
                    ph.put("p_direProv", row.get("NOM_PROV").toString());
                    ph.put("p_direDist", row.get("NOM_DIST").toString());
                }
            }else{
                ph.put("p_apePat", "");
                ph.put("p_apeMat", "");
                ph.put("p_nombres", ""); 
                ph.put("p_dni", "");
                ph.put("p_fecNac", ""); 
                ph.put("p_sexo", "");                
                ph.put("p_dire", "");
                ph.put("p_direDep", "");
                ph.put("p_direProv", "");
                ph.put("p_direDist", "");
            }
            
        }
        ph.put("p_fechaString", ReportUtil.mostrarFechaDdMmYyyy(new Date()));
        ph.put("p_fechaAnio", ReportUtil.mostrarAnio(new Date()));
        
        ArrayRecord denegado = obtenerDenegado(numDoc, registro.get("TIPO_DOC").toString().trim());
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
                            Logger.getLogger(DcGamacLicenciasRenagiController.class.getName()).log(Level.SEVERE, null, ex);
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
        
        /*try {
            foto = JsfUtil.oracleTiffenJpg(consultasDiscaFacade.selectFoto(numDoc.trim(), false), 240, 320);
            ph.put("p_foto", new ByteArrayInputStream(foto));
        } catch (Exception e) {
            ph.put("p_foto", null); 
            e.printStackTrace();
        }*/
        
        obtenerListadoLicenciaArmaPropias(numDoc);
        ph.put("p_armasPropias", listPropias);
        ph.put("p_armasPerJur", listPerJur);
        
        return ph;
    }
    
    /**
     * OBTENER LISTADO DE LICENCIAS DE ARMAS PROPIAS
     * @param numDoc
     * @author rarevalo
     * @version 1.0
     */
    public void obtenerListadoLicenciaArmaPropias(String numDoc){
        listPropias.clear();
        listPerJur.clear();

        ListDataModel<ArrayRecord> rs = new ListDataModel<>();
        switch (desdeMigra){
            case "DISCA":
                rs = new ListDataModel(consultasDiscaFacade.listarArmasxFiltro("doc_portador", numDoc));
                break;

            case "MIGRA":
                Long personaId = null;
                SbPersona per = null;
                if (numDoc.length()<=10) {
                    per = sbPersonaFacade.findByNumDoc(numDoc);
                }
                if (numDoc.length()>=11) {
                    per = sbPersonaFacade.findByRuc(numDoc);
                }
                if (per==null) {
                    per = sbPersonaFacade.findByNroCip(numDoc);
                }
                if (per!=null) {
                    personaId = per.getId();
                }                            
                
                rs = new ListDataModel(consultasDiscaFacade.listarArmasxFiltroMigra("doc_prop", numDoc, personaId));
                break;
        }
        Iterator<ArrayRecord> iteRs = rs.iterator();
        while (iteRs.hasNext()) {
            ArrayRecord row = iteRs.next();
            Map licencia = new HashMap();
            licencia.put("tipoArma", (row.get("TIPO_ARMA")!=null)? row.get("TIPO_ARMA").toString():"-" );
            licencia.put("marca", (row.get("MARCA")!=null)? row.get("MARCA").toString():"-" );
            licencia.put("calibre", (row.get("CALIBRE")!=null)? row.get("CALIBRE").toString():"-" );
            licencia.put("nroSerie", (row.get("NRO_SERIE")!=null)? row.get("NRO_SERIE").toString():"-" );
            licencia.put("nroLicencia", (row.get("NRO_LIC")!=null)? row.get("NRO_LIC").toString():"-" );
            String fecEmision = ReportUtil.mostrarFechaDdMmYyyy((Date) row.get("FEC_EMISION"));
            String fecVenci = ReportUtil.mostrarFechaDdMmYyyy((Date) row.get("FEC_VENCIMIENTO"));
            licencia.put("fecEmision", (fecEmision.equals("BORRADOR"))? "-":fecEmision);
            licencia.put("fecVenci",  (fecVenci.equals("BORRADOR"))? "-":fecVenci);
            licencia.put("tipoLicencia", (row.get("TIPO_LICENCIA")!=null)? row.get("TIPO_LICENCIA").toString():"-" );
            licencia.put("situacionArma", (row.get("SITUACION")!=null)? row.get("SITUACION").toString():"-" );
            licencia.put("situacionLicencia", (row.get("ESTADO")!=null)? row.get("ESTADO").toString():"-" );
            if(row.get("DOC_PROPIETARIO").toString().equals(numDoc)){
                listPropias.add(licencia);
            }else{
                listPerJur.add(licencia);
            }

        }
    }
    
     /**
     * OBTENER LISTADO DE LICENCIAS DE ARMAS DE PROPIEDAD DE PERSONAS JUR
     * @param persona
     * @param numDoc
     * @return Listado Map con campos a mostrar en el reporte
     * @author rarevalo
     * @version 1.0
     */
    public List<Map> obtenerListadoLicenciaArmaPerJur(SbPersona persona, String numDoc){
        List<Map> listado = new ArrayList();

        return listado;
    }
    
    /**
     * FUNCION PARA GENERACION DE REPORTE DE LICENCIA
     * @author rarevalo
     * @version 1.0
     * @param per
     * @return Reporte 
     */
    public StreamedContent reporte(ArrayRecord per) {
        try {            
            foto = null;
            //List<ArrayRecord> lp = new ArrayList();
            List<SbPersona> ls = new ArrayList<>();
            SbPersona r = loginController.getUsuario().getPersona();
            ls.add(r);
            registro = (ArrayRecord) resultados.getRowData();
            
            return JsfUtil.generarReportePdf("/aplicacion/consultasRenagi/DcGamacLicencias/reportes/licenciaArma.jasper", ls, parametrosPdf(registro), "licenciaArma.pdf", null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: dcGamacLicenciasRenagiController.reporte :", ex);
        }
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

    public void limpiarVista(){
        tarjeta = new AmaTarjetaPropiedad();
        expTar = new Expediente();
        armaCalibre = "";
        inventarioArma = null;
        guiaT = null;
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

            List<String> sList = new ArrayList<>();            

            switch (desdeMigra){
                case "DISCA":
                    sList = Arrays.asList("PERS. NATURAL", "PERS.NAT.C/RUC");            
                    logController.escribirLogVer("" + registro.get("NRO_LIC"), "INTEROP_WS_LICENCIAS");
                    break;

                case "MIGRA":
                    sList = Arrays.asList("PERSONA NATURAL", "PERS.NAT.C/RUC");            
                    logController.escribirLogVer("" + registro.get("NRO_LIC"), "ARMA_GENERAL_MIGRA");
                    break;
            }
            
            
            if (registro.get("SISTEMA").equals("GAMAC")) {
                tarjeta = infoArmasFacade.obtenerArmaPorSerieRua(registro.get("NRO_RUA").toString(), registro.get("NRO_SERIE").toString());
                if (tarjeta != null) {
                    expTar = infoArmasFacade.selectExpedienteXNro(tarjeta.getNroExpediente()).get(0);
                    armaCalibre = ordenarCalibres(tarjeta.getArmaId().getModeloId().getAmaCatalogoList());
                    
                    if (tarjeta.getArmaId().getSituacionId().getCodProg().equals("TP_SITU_INT")) {
                        List<AmaInventarioArma> lstInventario = infoArmasFacade.selectInventarioArma(registro.get("NRO_RUA").toString(), registro.get("NRO_SERIE").toString());
                        if (!lstInventario.isEmpty()) {
                            inventarioArma = lstInventario.get(0); //infoArmasFacade.selectInventarioArma(registro.get("NRO_RUA").toString(), registro.get("NRO_SERIE").toString()).get(0);
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
            if (registro.get("SISTEMA").equals("DISCA")) {
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
                foto = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_foto_ama").getValor() + fileName));
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
                    foto = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_foto_ama").getValor() + fileName));
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
    
    /**
     * Función que obtiene los calibres del arma por modelo id
     *
     * @author Gino Chávez
     * @param amaModelos
     * @return
     */
    public String obtenerCalibres(AmaModelos amaModelos) {
        String r = "";
        String separador = "/";
        if (amaModelos != null) {
            int tam = amaModelos.getAmaCatalogoList().size();
            for (AmaCatalogo amaCat : amaModelos.getAmaCatalogoList()) {
                if (tam > 1) {
                    r += amaCat.getNombre() + separador;
                } else {
                    r += amaCat.getNombre();
                }
                tam--;
            }
        }
        return r;
    }
    
    private SbPersona obtenerPersona(String tipoDoc, String numDoc) {
        SbPersona per = null;
        switch (tipoDoc) {
            case "dni":
            case "DNI":
                per = sbPersonaFacade.findByNumDoc(numDoc);
                break;
            case "CE":
            case "ce":
                per = sbPersonaFacade.findByNroCip(numDoc);
                break;
            case "RUC":
            case "ruc":
                per = sbPersonaFacade.findByRuc(numDoc);
                break;
            case "CIP":
            case "FAP":
            case "POLICIA":
            case "MARINA":
            case "MILITAR":
            case "EJERCITO":
            case "cip":
                per = sbPersonaFacade.findByNroCip(numDoc);
                break;
        }
        return per;
    }
    
}
