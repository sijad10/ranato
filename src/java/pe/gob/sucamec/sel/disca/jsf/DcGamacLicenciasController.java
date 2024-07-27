/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.disca.jsf;

/**
 *
 * @author rmoscoso
 */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaFoto;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbParametro;
import pe.gob.sucamec.sel.disca.jsf.util.EstadoCrud;
import pe.gob.sucamec.sel.disca.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

// Nombre de la instancia en la aplicacion //
@Named("dcGamacLicenciasController")
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
public class DcGamacLicenciasController implements Serializable {

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

    /**
     * Variables para Vista
     */
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

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.disca.beans.ConsultasDiscaFacade consultasDiscaFacade;
    @EJB
    private SbUsuarioFacade ejbSbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;    
    @EJB
    private pe.gob.sucamec.renagi.beans.InfoArmasFacade infoArmasFacade;    

    /**
     * Constructor
     *
     */
    public DcGamacLicenciasController() {
        estado = EstadoCrud.BUSCAR;
    }

    @PostConstruct
    public void inicializar() {
        // Parámetro origen data ARMAS
        setParamOriDatArma(ejbSbParametroFacade.obtenerParametroXSistemaCodProg("TP_ORIGDAT_ARMDIS", 5L));
        if (paramOriDatArma != null) {
            setDesdeMigra(paramOriDatArma.getValor());
        } else {
            setDesdeMigra("DISCA");
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
            String tl = (String) registro.get("TIPO_LICENCIA");
            boolean vigilante = false;
            if (tl != null && tl.equals("SERVICIO VIGILANCIA PRIVADA")) {
                vigilante = true;
            }
            try {
                foto = JsfUtil.oracleTiffenJpg(consultasDiscaFacade.selectFoto(registro.get("DOC_PORTADOR").toString(), vigilante), 180, 240);
            } catch (Exception ex) {
                //JsfUtil.mensajeError(ex, JsfUtil.bundle("ErrorDePersistencia"));
                JsfUtil.mensajeError("No existe archivo imagen");
            }      
        }
    }

    public DefaultStreamedContent getFoto() {
        if (foto == null) {
            try {
                return new DefaultStreamedContent(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/ninguno_320.jpg"), "image/jpg");
            } catch (Exception ex) {
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

    /**
     * Propiedad para los resultados de la búsqueda del CRUD.
     *
     * @return Los resultados de la búsqueda.
     */
    public ListDataModel<ArrayRecord> getResultados() {
        return resultados;
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
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        resultados = null;
        mostrarBuscar();
        return "/aplicacion/consultas/DcGamacLicencias/List";
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscarPn() {
        resultados = new ListDataModel(consultasDiscaFacade.listarArmasDeFuego(null, null, null, loginController.getUsuario().getNumDoc(), objetoUsuario().getPersonaId().getNroCip()));;
        mostrarBuscar();
        return "/aplicacion/consultas/DcGamacLicenciasPn/List";
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
        /*String serie = null, licencia = null, portador = null;
        if (tipo != null) {
            if (tipo.equals("licencia")) {
                licencia = filtro;
            }
            if (tipo.equals("serie")) {
                serie = filtro;
            }
            if (tipo.equals("portador")) {
                portador = filtro;
            }
        }*/
        String docUsr = loginController.getUsuario().getNumDoc();
        String cipUsr = loginController.getUsuario().getPersona().getNroCip();
        String nombreAdministrado = null;
        if (loginController.getUsuario().getPersona().getTipoId().getCodProg().equals("TP_PER_JUR")){
            nombreAdministrado = null; //loginController.getUsuario().getPersona().getRznSocial();
        }
        else {
            nombreAdministrado = loginController.getUsuario().getPersona().getApellidosyNombres();
        }
        
        switch (desdeMigra){
            case "DISCA":
                resultados = new ListDataModel(consultasDiscaFacade.listarArmasDeFuegoTipo(filtro, tipo, docUsr, cipUsr));
                break;

            case "MIGRA":
                resultados = new ListDataModel(consultasDiscaFacade.listarArmasDeFuegoTipoMigra(filtro, tipo, docUsr, cipUsr, loginController.getUsuario().getPersona().getId(),nombreAdministrado));
                break;
        }
        
    }

     /**
     * FUNCIÓN QUE OBTIENE EL OBJETO USUARIO
     *
     * @author Gino Chávez
     * @return Usuario
     */
    public SbUsuario objetoUsuario() {
        List<SbUsuario> listRes = ejbSbUsuarioFacade.selectUsuarioXLogin(loginController.getUsuario().getLogin());
        List<SbUsuario> listTemp = new ArrayList<>();
        SbUsuario usu = new SbUsuario();
        listTemp.addAll(listRes);
        for (SbUsuario u : listTemp) {
            if (!u.getPersonaId().getId().equals(loginController.getUsuario().getPersona().getId())) {
                listRes.remove(u);
            }
        }
        if (!listRes.isEmpty()) {
            usu = listRes.get(0);
        }
        return usu;
    }
    
    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (ArrayRecord) resultados.getRowData();
        crearFoto();
        estado = EstadoCrud.VER;
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

            List<String> sList = Arrays.asList("PERS. NATURAL", "PERS.NAT.C/RUC");

            switch (desdeMigra){
                case "DISCA":
                    sList = Arrays.asList("PERS. NATURAL", "PERS.NAT.C/RUC");            
                    break;

                case "MIGRA":
                    sList = Arrays.asList("PERSONA NATURAL", "PERS.NAT.C/RUC");            
                    break;
            }
            
            //logController.escribirLogVer("" + registro.get("NRO_LIC"), "INTEROP_WS_LICENCIAS");
            //estado = EstadoCrud.VER;
            //syso(registro.get("SISTEMA").toString());
            if(registro.get("SISTEMA").equals("GAMAC")){
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
                                direInternamiento = pe.gob.sucamec.renagi.jsf.util.JsfUtil.mostrarDireccion(inventarioArma.getAlmacenSucamecId());
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
                            direInternamiento = pe.gob.sucamec.renagi.jsf.util.JsfUtil.mostrarDireccion(inventarioArma.getAlmacenSucamecId());
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
                pe.gob.sucamec.renagi.jsf.util.JsfUtil.mensajeError("No existe archivo imagen");
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
                    pe.gob.sucamec.renagi.jsf.util.JsfUtil.mensajeError("No existe archivo imagen");
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
        
}
