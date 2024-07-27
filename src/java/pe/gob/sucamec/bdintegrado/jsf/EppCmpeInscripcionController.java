package pe.gob.sucamec.bdintegrado.jsf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import pe.gob.sucamec.bdintegrado.data.EppCmpeInscripcion;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.ejb.EJB;import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.data.EppCapaHorariodir;
import pe.gob.sucamec.bdintegrado.data.EppCapacitacion;
import pe.gob.sucamec.bdintegrado.data.EppCertificado;
import pe.gob.sucamec.bdintegrado.data.EppFoto;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;
import pe.gob.sucamec.bdintegrado.data.EppRegistroEvento;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
import pe.gob.sucamec.bdintegrado.jsf.util.ReportUtil;
import pe.gob.sucamec.bdintegrado.ws.WsPide;
import pe.gob.sucamec.bdintegrado.ws.WsTramDoc;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;

// Nombre de la instancia en la aplicacion //
@Named("eppCmpeInscripcionController")
@SessionScoped

/**
 * Clase con EppCmpeInscripcionController instanciada como
 * eppCmpeInscripcionController. Contiene funciones utiles para la entidad
 * EppCmpeInscripcion. Esta vinculada a las páginas
 * EppCmpeInscripcion/create.xhtml, EppCmpeInscripcion/update.xhtml,
 * EppCmpeInscripcion/list.xhtml, EppCmpeInscripcion/view.xhtml Nota: Las tablas
 * deben tener la estructura de Sucamec para que funcione adecuadamente, revisar
 * si tiene el campo activo y modificar las búsquedas.
 */
public class EppCmpeInscripcionController implements Serializable {

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
    EppCmpeInscripcion registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<EppCmpeInscripcion> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<EppCmpeInscripcion> registrosSeleccionados;
    // REGISTRO
    private List<Map> resultadosBandeja = null;
    private List<Map> listEppRegistroSelec = null;
    private EppCapacitacion capacitacion;
    private boolean terminos;
    private List<TipoExplosivoGt> lstActividadesDisponibles;
    private String tipoResultado = null;
    private String tipoBusqueda = null;
    private Date fechaBusqueda = null;
    private Date fechaInicio = null;
    private Date fechaFin = null;
    private boolean muestraBusquedafecha = false;
    private List<SbPersonaGt> listaPersonasBusqueda;
    private SbPersonaGt personaEdicion = new SbPersonaGt();
    private UploadedFile file;
    private StreamedContent foto;
    private byte[] fotoByte;
    private int vacantes;
    private Expediente expediente;
    private SbDireccionGt direccionEdicion = new SbDireccionGt();
    private SbDireccionGt direccionPersona = new SbDireccionGt();
    private String suce;
    private boolean disabledReniec;
    private boolean disabledTipoDoc;
    private boolean disabledNombres;
    private List<SbDistritoGt> listDistritos = null;
    private boolean renderRecibo;
    private SbRecibos recibo;
    private String nombreImagen;
    private EppRegistro registroInscripcion;
    private Integer procesoCapacitacion = 704; // = Proceso de Certificado de Medidas de Seguridad
    private String fotoAnterior;
    
    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppCmpeInscripcionFacade ejbEppCmpeInscripcionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppCertificadoFacade ejbCertificadoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppCapacitacionFacade ejbCapacitacionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppRegistroFacade ejbRegistroFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbRecibosFacade ejbSbRecibosFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoExplosivoFacade ejbTipoExplosivoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade ejbTipoBaseFacade;    
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDireccionFacadeGt ejbDireccionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppFotoFacade ejbFotoFacade;    
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbDistritoFacadeGt ejbDistritoFacade;
    @Inject
    WsTramDoc wsTramDocController;
    @Inject
    WsPide wsPideController;

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
     * Propiedad de seleccion multiple
     *
     * @return
     */
    public List<EppCmpeInscripcion> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<EppCmpeInscripcion> a) {
        this.registrosSeleccionados = a;
    }

    public List<Map> getResultadosBandeja() {
        return resultadosBandeja;
    }

    public void setResultadosBandeja(List<Map> resultadosBandeja) {
        this.resultadosBandeja = resultadosBandeja;
    }

    public List<Map> getListEppRegistroSelec() {
        return listEppRegistroSelec;
    }

    public void setListEppRegistroSelec(List<Map> listEppRegistroSelec) {
        this.listEppRegistroSelec = listEppRegistroSelec;
    }

    public EppCapacitacion getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(EppCapacitacion capacitacion) {
        this.capacitacion = capacitacion;
    }

    public boolean isTerminos() {
        return terminos;
    }

    public void setTerminos(boolean terminos) {
        this.terminos = terminos;
    }

    public List<TipoExplosivoGt> getLstActividadesDisponibles() {
        return lstActividadesDisponibles;
    }

    public void setLstActividadesDisponibles(List<TipoExplosivoGt> lstActividadesDisponibles) {
        this.lstActividadesDisponibles = lstActividadesDisponibles;
    }

    public String getTipoResultado() {
        return tipoResultado;
    }

    public void setTipoResultado(String tipoResultado) {
        this.tipoResultado = tipoResultado;
    }

    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public Date getFechaBusqueda() {
        return fechaBusqueda;
    }

    public void setFechaBusqueda(Date fechaBusqueda) {
        this.fechaBusqueda = fechaBusqueda;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isMuestraBusquedafecha() {
        return muestraBusquedafecha;
    }

    public void setMuestraBusquedafecha(boolean muestraBusquedafecha) {
        this.muestraBusquedafecha = muestraBusquedafecha;
    }

    public List<SbPersonaGt> getListaPersonasBusqueda() {
        return listaPersonasBusqueda;
    }

    public void setListaPersonasBusqueda(List<SbPersonaGt> listaPersonasBusqueda) {
        this.listaPersonasBusqueda = listaPersonasBusqueda;
    }

    public SbPersonaGt getPersonaEdicion() {
        return personaEdicion;
    }

    public void setPersonaEdicion(SbPersonaGt personaEdicion) {
        this.personaEdicion = personaEdicion;
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

    public byte[] getFotoByte() {
        return fotoByte;
    }

    public void setFotoByte(byte[] fotoByte) {
        this.fotoByte = fotoByte;
    }

    public int getVacantes() {
        return vacantes;
    }

    public void setVacantes(int vacantes) {
        this.vacantes = vacantes;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public SbDireccionGt getDireccionEdicion() {
        return direccionEdicion;
    }

    public void setDireccionEdicion(SbDireccionGt direccionEdicion) {
        this.direccionEdicion = direccionEdicion;
    }

    public SbDireccionGt getDireccionPersona() {
        return direccionPersona;
    }

    public void setDireccionPersona(SbDireccionGt direccionPersona) {
        this.direccionPersona = direccionPersona;
    }

    public String getSuce() {
        return suce;
    }

    public void setSuce(String suce) {
        this.suce = suce;
    }

    public boolean isDisabledReniec() {
        return disabledReniec;
    }

    public void setDisabledReniec(boolean disabledReniec) {
        this.disabledReniec = disabledReniec;
    }

    public boolean isDisabledTipoDoc() {
        return disabledTipoDoc;
    }

    public void setDisabledTipoDoc(boolean disabledTipoDoc) {
        this.disabledTipoDoc = disabledTipoDoc;
    }

    public boolean isDisabledNombres() {
        return disabledNombres;
    }

    public void setDisabledNombres(boolean disabledNombres) {
        this.disabledNombres = disabledNombres;
    }

    public List<SbDistritoGt> getListDistritos() {
        return listDistritos;
    }

    public void setListDistritos(List<SbDistritoGt> listDistritos) {
        this.listDistritos = listDistritos;
    }

    public boolean isRenderRecibo() {
        return renderRecibo;
    }

    public void setRenderRecibo(boolean renderRecibo) {
        this.renderRecibo = renderRecibo;
    }

    public SbRecibos getRecibo() {
        return recibo;
    }

    public void setRecibo(SbRecibos recibo) {
        this.recibo = recibo;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    /**
    * OBTENER LISTADO DE CAPACITACIONES DISPONIBLES SEGUN FECHA DE INSCRIPCION
    * @author Richar Fernández
    * @version 2.0
    * @return Listado de Capacitaciones
    */
    public List<EppCapacitacion> getItemsCapacitacionCertificadoMS(){
        return ejbCapacitacionFacade.buscarCapacitacionConFechaLimiteInscripcion();
    }

    public EppRegistro getRegistroInscripcion() {
        return registroInscripcion;
    }

    public void setRegistroInscripcion(EppRegistro registroInscripcion) {
        this.registroInscripcion = registroInscripcion;
    }
    
     public List<TipoBaseGt> getListaDocumentosPersona(){
        return ejbTipoBaseFacade.lstTiposDocumentosPersona();
    }

    public Integer getProcesoCapacitacion() {
        return procesoCapacitacion;
    }

    public void setProcesoCapacitacion(Integer procesoCapacitacion) {
        this.procesoCapacitacion = procesoCapacitacion;
    }

    public String getFotoAnterior() {
        return fotoAnterior;
    }

    public void setFotoAnterior(String fotoAnterior) {
        this.fotoAnterior = fotoAnterior;
    }
    
    /**
     * Anula multiples registros, usar como base para acciones de multiples
     * registros.
     */
    public void anularMultiple() {
        if ((registrosSeleccionados == null) || registrosSeleccionados.isEmpty()) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorSeleccion"));
            return;
        }
        try {
            for (EppCmpeInscripcion a : registrosSeleccionados) {
                a.setActivo((short) 0);
                ejbEppCmpeInscripcionFacade.edit(a);
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Activa multiples registros, usar como base para acciones de multiples
     * registros.
     */
    public void activarMultiple() {
        if ((registrosSeleccionados == null) || registrosSeleccionados.isEmpty()) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorSeleccion"));
            return;
        }
        try {
            for (EppCmpeInscripcion a : registrosSeleccionados) {
                a.setActivo((short) 1);
                ejbEppCmpeInscripcionFacade.edit(a);
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a activo en un
     * registro de una lista.
     *
     */
    public void activar() {
        try {
            registro = (EppCmpeInscripcion) resultados.getRowData();
            registro.setActivo((short) 1);
            ejbEppCmpeInscripcionFacade.edit(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a anulado en un
     * registro de una lista.
     *
     */
    public void anular() {
        try {
            registro = (EppCmpeInscripcion) resultados.getRowData();
            registro.setActivo((short) 0);
            ejbEppCmpeInscripcionFacade.edit(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public EppCmpeInscripcion getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(EppCmpeInscripcion registro) {
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
    public ListDataModel<EppCmpeInscripcion> getResultados() {
        return resultados;
    }

    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<EppCmpeInscripcion> getSelectItems() {
        return ejbEppCmpeInscripcionFacade.findAll();
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     * @param item
     */
    public void mostrarVer(Map item) {
        reiniciarValores();
        registro = ejbEppCmpeInscripcionFacade.selectLike(Long.parseLong(""+item.get("ID")));
        cambiaCapacitacion();
        
        for (SbDireccionGt dir : registro.getParticipanteId().getSbDireccionList()) {
            if (dir.getActivo() == 1 && dir.getTipoId().getCodProg().equals("TP_DIRECB_FIS")) {

                if(dir.getDistritoId().getNombre().equals("MIGRACION")){
                    direccionPersona = null;
                    JsfUtil.mensajeError("La dirección de la persona seleccionada necesita actualizarse la ubicación.");
                    RequestContext.getCurrentInstance().update(obtenerForm()+":pnlParticipante");
                    RequestContext.getCurrentInstance().update(obtenerForm()+":pnlActividades");
                    return;
                }
                direccionPersona = dir;
            }
        }
        
        if (registro.getFotoRuta() != null) {
            try {
                fotoByte = FileUtils.readFileToByteArray(new File(JsfUtil.bundleBDIntegrado("Documentos_pathUpload_foto") + registro.getFotoRuta() ));
                InputStream is = new ByteArrayInputStream(fotoByte);
                foto = new DefaultStreamedContent(is, "image/jpeg", registro.getFotoRuta() );
            } catch (IOException ex) {
                foto = null;
                fotoByte = null;
                JsfUtil.mensajeAdvertencia("Error al cargar la foto.");
            }
        }
        
        estado = EstadoCrud.VER;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     * @param item
     */
    public void mostrarEditar(Map item) {
        reiniciarValores();
        registro = ejbEppCmpeInscripcionFacade.selectLike(Long.parseLong(""+item.get("ID")));
        cambiaCapacitacion();
        
        for (SbDireccionGt dir : registro.getParticipanteId().getSbDireccionList()) {
            if (dir.getActivo() == 1 && dir.getTipoId().getCodProg().equals("TP_DIRECB_FIS")) {

                if(dir.getDistritoId().getNombre().equals("MIGRACION")){
                    direccionPersona = null;
                    JsfUtil.mensajeError("La dirección de la persona seleccionada necesita actualizarse la ubicación.");
                    RequestContext.getCurrentInstance().update(obtenerForm()+":pnlParticipante");
                    RequestContext.getCurrentInstance().update(obtenerForm()+":pnlActividades");
                    return;
                }
                direccionPersona = dir;
            }
        }
        if (registro.getFotoRuta() != null) {
            try {
                fotoByte = FileUtils.readFileToByteArray(new File(JsfUtil.bundleBDIntegrado("Documentos_pathUpload_foto") + registro.getFotoRuta() ));
                InputStream is = new ByteArrayInputStream(fotoByte);
                foto = new DefaultStreamedContent(is, "image/jpeg", registro.getFotoRuta() );
            } catch (IOException ex){
                ex.printStackTrace();
                foto = null;
                fotoByte = null;
                JsfUtil.mensajeAdvertencia("Error al cargar la foto.");
            }
        }
        fotoAnterior = registro.getFotoRuta();
        setTerminos(true);
        estado = EstadoCrud.EDITAR;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     */
    public void mostrarCrear() {
        estado = EstadoCrud.CREAR;
        registro = new EppCmpeInscripcion();
        registro.setActivo((short) 1);
    }

    /**
     * Evento para guardar la información al editar un registro.
     * @return 
     */
    public String editar() {
        try {
            if(validaCreate()){
                registro = (EppCmpeInscripcion) JsfUtil.entidadMayusculas(registro, "");

                EppFoto fotoReg = new EppFoto();
                if(!Objects.equals(fotoAnterior, registro.getFotoRuta())){
                    if(file != null && foto != null){
                        String fileName = file.getFileName();
                        registro.setFotoRuta("ADJ_CERTIFCAPPIRO" + "_" + UUID.randomUUID().toString().substring(0, 7) + fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                        
                        //SUBIR FOTO A LA RUTA
                        FileUtils.writeByteArrayToFile(new File(JsfUtil.bundleBDIntegrado("Documentos_pathUpload_foto") + fotoReg.getRutaFile()), fotoByte);
                    }
                }
                ejbEppCmpeInscripcionFacade.edit(registro);

//                if(recibo != null){
//                    SbReciboRegistro rec = new SbReciboRegistro();
//                    rec.setId(null);
//                    rec.setNroExpediente(registro.getNroExpediente());
//                    rec.setReciboId(recibo);
//                    rec.setActivo(JsfUtil.TRUE);
//                    recibo.getSbReciboRegistroList().add(rec);
//
//                    ejbSbRecibosFacade.edit(recibo);
//                }

                JsfUtil.mensaje(JsfUtil.bundleBDIntegrado("MensajeRegistroActualizado") + " ID: " + registro.getId());

                foto = null;
                file = null;
                return prepareListInscritos();
            }
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundleBDIntegrado("ErrorDePersistencia"));
        }
        return null;
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     * @author Richar Fernández
     * @version 2.0
     * @return Página para direccionar
     */
    public String crear() {
        try {
            if(validaCreate()){
                registro = (EppCmpeInscripcion) JsfUtil.entidadMayusculas(registro, "");

                EppFoto fotoReg = new EppFoto();
                if(registro.getFotoRuta() == null){
                    if(file != null && foto != null){
                        String fileName = file.getFileName();
                        registro.setFotoRuta("ADJ_CERTIFCAPPIRO" + "_" + UUID.randomUUID().toString().substring(0, 7) + fileName.substring(fileName.lastIndexOf('.'), fileName.length()));
                        
                        //SUBIR FOTO A LA RUTA
                        FileUtils.writeByteArrayToFile(new File(JsfUtil.bundleBDIntegrado("Documentos_pathUpload_foto") + fotoReg.getRutaFile()), fotoByte);
                    }
                }
                registro.setFecha(new Date());
                ejbEppCmpeInscripcionFacade.create(registro);

//                if(recibo != null){
//                    SbReciboRegistro rec = new SbReciboRegistro();
//                    rec.setId(null);
//                    rec.setNroExpediente(registro.getNroExpediente());
//                    rec.setReciboId(recibo);
//                    rec.setActivo(JsfUtil.TRUE);
//                    recibo.getSbReciboRegistroList().add(rec);
//
//                    ejbSbRecibosFacade.edit(recibo);
//                }

                JsfUtil.mensaje(JsfUtil.bundleBDIntegrado("MensajeRegistroCreado") + " ID: " + registro.getId());

                foto = null;
                file = null;
                return inscripcion();
            }
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundleBDIntegrado("ErrorDePersistencia"));
        }
        return null;
    }

    /**
     * FUNCION PARA VALIDAR ANTES DE GUARDADO DE DATOS
     * @author Richar Fernández
     * @version 1.0
     * @return Flag de Validación
     */
    public boolean validaCreate() {
        boolean validacion = true;
  
        if(registro.getCapacitacionId() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, seleccione un lugar y fecha de capacitación");
        }else{
            if(vacantes == 0){
                validacion = false;
                JsfUtil.mensajeError("La Capacitación seleccionada no cuenta con vacantes disponibles. Por favor, seleccione otra");
            }
        }
        if(registro.getParticipanteId().getId() == null ){
            validacion = false;
            JsfUtil.mensajeError("Por favor, ingrese al Participante de la capacitación");
        }
        if(foto == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor, agregue una foto ");
        }
        if(!terminos){
            validacion = false;
            JsfUtil.mensajeError("Por favor, marque el check de la declaración jurada del participante.");
        }
        
        return validacion;
    }
    
    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public EppCmpeInscripcion getEppCmpeInscripcion(Long id) {
        return ejbEppCmpeInscripcionFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public EppCmpeInscripcionController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "eppCmpeInscripcionConverter")
    public static class EppCmpeInscripcionControllerConverterN extends EppCmpeInscripcionControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = EppCmpeInscripcion.class)
    public static class EppCmpeInscripcionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EppCmpeInscripcionController c = (EppCmpeInscripcionController) JsfUtil.obtenerBean("eppCmpeInscripcionController", EppCmpeInscripcionController.class);
            return c.getEppCmpeInscripcion(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof EppCmpeInscripcion) {
                EppCmpeInscripcion o = (EppCmpeInscripcion) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + EppCmpeInscripcion.class.getName());
            }
        }
    }
    
    /// REGISTRO ///
    
    /**
     * FOARMATEAR CADENA DE NOTA PARA MOSTRARLA
     * @author Richar Fernández
     * @version 2.0
     * @param cap Capacitación para obtener lugar y fecha
     * @return Cadena de fecha formateada: ubigeo [ fechaini - fechafin año] 
     */
    public String obtenerLugarFechaCertificacionesConstancia(EppCapacitacion cap){
        String capacitacionCadena = "";
        
        if(cap != null){
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));

            capacitacionCadena = cap.getUbigeoId().getProvinciaId().getDepartamentoId().getNombre() + " - " +
                                 cap.getUbigeoId().getProvinciaId().getNombre() + " - " +
                                 cap.getUbigeoId().getNombre() + 
                                 " [" + formatearFecha(cap.getFechaInicio()) + " - " +  formatearFecha(cap.getFechaFin()) + " " + formatAnio.format(cap.getFechaFin()) + "]";
        }
        return capacitacionCadena;
    }
    
    /**
     * FORMATEAR FECHA (DIA MES)
     * @author Richar Fernández
     * @version 2.0
     * @param fecha fecha a formatear
     * @return Cadena de fecha formateada: dd MMM
     */
    public String formatearFecha(Date fecha){
        String fech = "";
        SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
        SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
        fech = formatDia.format(fecha) + " " + formatMes.format(fecha).toUpperCase();
        
        return fech;
    }
    
    /**
     * EVENTO AL CAMBIAR CAPACITACION
     * @author Richar Fernández
     * @version 2.0
     */
    public void cambiaCapacitacion(){
        lstActividadesDisponibles = new ArrayList();
        vacantes = 0;
        if(registro.getCapacitacionId() != null){
            for(TipoExplosivoGt exp : registro.getCapacitacionId().getTipoExplosivoList()){
                lstActividadesDisponibles.add(exp);
            }
            
            vacantes = registro.getCapacitacionId().getNroVacantes() - ejbCertificadoFacade.contarInscritos(registro.getCapacitacionId().getId().toString());
            if(vacantes < 0) vacantes = 0;
            
            if(vacantes == 0){
                JsfUtil.mensajeAdvertencia("La Capacitación seleccionada no cuenta con vacantes disponibles. Por favor, seleccione otra");
            }
        }
    }
    
    /**
     * FORMATEA FECHA DE HORARIO PARA MOPSTRARSE EN TABLA DE PASO 1
     * @author Richar Fernández
     * @version 2.0
     * @param cap Horario de capacitación para formatear fecha
     * @return Cadena de fecha formateada: dd MMM [ HH:mm A HH:mm]
     */
    public String formatoFechaHorarioPaso1(EppCapaHorariodir cap){
        String fech = "";
        SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
        SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
        SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm", new Locale("ES"));       
        
        fech = formatDia.format(cap.getFecha()) + " " + formatMes.format(cap.getFecha()).toUpperCase() +
                " [" + formatHora.format(cap.getHoraInicio()) + " A " + formatHora.format(cap.getHoraFin()) + "]";
                
        return fech;       
    }
    
    /**
     * ABRIR FORMULARIO DE AYUDA
     * @author Richar Fernández
     * @version 1.0
     * @param tipo
     */
    public void openDlgAyuda(int tipo){
        if(tipo == 1){
            nombreImagen = "ayuda_capacitacion.png";
        }else{
            //rfv
            //falta imagen de ayuda inicial.
        }
        RequestContext.getCurrentInstance().execute("PF('wvDialogAyuda').show()");
        RequestContext.getCurrentInstance().update("frmAyuda");
    }
    
    /**
     * EVENTO PARA CARGAR FOTO
     * @author Richar Fernández
     * @version 2.0
     * @param event Evento al cargar archivo
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (event != null) {
                file = event.getFile();
                if (JsfUtil.verificarJPG(file)) {
                    fotoByte = IOUtils.toByteArray(file.getInputstream());
                    foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
                    registro.setFotoRuta(null);
                } else {
                    file = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo JPG/JPEG original, comuníquese con el administrador del sistema");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
        /**
     * OBTENER NOMBRE DE FORMULARIO SEGUN ESTADO ACTUAL
     * @author Richar Fernández
     * @version 2.0
     * @return Nombre de Formulario
     */
    public String obtenerForm(){        
        switch(estado){            
            case CREAR:
                    return "createForm";
            case EDITAR:
                    return "editForm";
            case VER:
                    return "viewForm";
            case BUSCAR:
                    return "listForm";
        }
        return null;
    }
    
    
    /**
     * FUNCION PARA PREPARAR E INICIAR INSCRIPCION DE REGISTRO
     * @author Richar Fernández
     * @version 2.0
     * @return Página inicial de inscripción para direccionar
     */
    public String inscripcion(){
        short cont = 0;
        reiniciarValores();
        resultadosBandeja = null;
        
        registro.setFecha(new Date());
        registro.setActivo(JsfUtil.TRUE);
        registro.setAudLogin("USRWEB");
        registro.setAudNumIp(JsfUtil.getIpAddress());
        registro.setParticipanteId(new SbPersonaGt());
        registro.setTipoExplosivoList(new ArrayList());
        lstActividadesDisponibles = ejbTipoExplosivoFacade.lstTipoExplosivo("TP_PIROACT");
        
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        SbPersonaGt per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());
        if(per != null){
            cont = 0;
            if(per.getSbDireccionList() != null && !per.getSbDireccionList().isEmpty()){
                for (SbDireccionGt itemDir : per.getSbDireccionList()) {
                    if (itemDir.getTipoId().getCodProg().equals("TP_DIRECB_FIS") && itemDir.getActivo() == 1) {
                        cont++;
                    }
                }
            }
            
            if(cont > 0){
                registro.setPersonaId(per);
                estado = EstadoCrud.CREAR;
            }else{
                JsfUtil.mensajeAdvertencia("El administrado no cuenta con una direción registrada");
            }
        }else{
            JsfUtil.mensajeAdvertencia("No se encontró a la persona para inscripción");
        }
        
        return "/aplicacion/gepp/eppCmpeInscripcion/Create";
    }
    
    /**
     * LIMPIAR VALORES PARA NUEVO/EDICION DE REGISTROS.
     * @author Richar Fernández
     * @version 2.0
     */
    public void reiniciarValores(){
        registro = new EppCmpeInscripcion();        
        lstActividadesDisponibles = new ArrayList();
        listaPersonasBusqueda = new ArrayList();
        recibo = null;
        terminos = false;
        expediente = null;
        foto = null;
        fotoByte = null;
        file = null;
        vacantes = 0;
        nombreImagen = null;
        setRenderRecibo(true);
        direccionEdicion = new SbDireccionGt();
        direccionPersona = new SbDireccionGt();
        listEppRegistroSelec = new ArrayList();
        listDistritos = new ArrayList();
        registroInscripcion = null;
        fechaBusqueda = null;
        fotoAnterior = null;
    }
    
    public String inscribirCertificado(){
        try {
            short cont = 0;
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            SbPersonaGt per = ejbPersonaFacade.buscarPersonaSel(p_user.getTipoDoc(), p_user.getNumDoc());

            if(per != null){
                cont = 0;
                if(per.getSbDireccionList() != null && !per.getSbDireccionList().isEmpty()){
                    for (SbDireccionGt itemDir : per.getSbDireccionList()) {
                        if (itemDir.getTipoId().getCodProg().equals("TP_DIRECB_FIS") && itemDir.getActivo() == 1) {
                            cont++;
                        }
                    }
                }

                if(cont > 0){
                    for(Map borrador : listEppRegistroSelec){
                        registro = ejbEppCmpeInscripcionFacade.selectLike(Long.parseLong(""+borrador.get("ID")));
                        
                        int contParticipante = ejbCertificadoFacade.buscarPersonaInscritaCapacitacion(registro.getCapacitacionId().getId().toString(), registro.getParticipanteId().getId().toString());
                        if(contParticipante > 0){
                            JsfUtil.mensajeError("El participante "+ registro.getParticipanteId().getNumDoc() + " ya se encuentro registrado en la capacitación.");
                            continue;
                        }
                        
                        inicializarRegistro(per);
                        
                        registroInscripcion.setNroExpediente(crearExpediente());
                        if(registroInscripcion.getNroExpediente() != null){
                            EppFoto fotoReg = new EppFoto();
                            fotoReg.setId(null);
                            fotoReg.setRutaFile(registro.getFotoRuta());
                            fotoReg.setActivo(JsfUtil.TRUE );
                            fotoReg.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                            fotoReg.setAudNumIp(JsfUtil.getIpAddress());
                            ejbFotoFacade.create(fotoReg);
                            
                            registroInscripcion.getEppCertificado().setCapacitacionId(registro.getCapacitacionId());
                            registroInscripcion.getEppCertificado().setFotoId(fotoReg);
                            registroInscripcion.getEppCertificado().setPersonaId(registro.getParticipanteId());
                            registroInscripcion.getEppCertificado().setRegistroId(registroInscripcion);
                            registroInscripcion.getEppCertificado().setTipoExplosivoList(registro.getTipoExplosivoList());
                            
                            creaRegistroEvento(registroInscripcion);
                            ejbRegistroFacade.create(registroInscripcion);

                            registro.setActivo(JsfUtil.FALSE);
                            registro.setRegistroId(registroInscripcion);
                            ejbEppCmpeInscripcionFacade.edit(registro);
//
//                            if(recibo != null){
//                                SbReciboRegistro rec = new SbReciboRegistro();
//                                rec.setId(null);
//                                rec.setNroExpediente(registroInscripcion.getNroExpediente());
//                                rec.setReciboId(recibo);
//                                rec.setActivo(JsfUtil.TRUE);
//                                recibo.getSbReciboRegistroList().add(rec);
//
//                                ejbSbRecibosFacade.edit(recibo);
//                            }
                            
                            JsfUtil.mensaje(JsfUtil.bundleBDIntegrado("MensajeRegistroCreado") + " " + registroInscripcion.getId());
                            RequestContext.getCurrentInstance().execute("PF('wvDialogInscripcion').hide()");
                            RequestContext.getCurrentInstance().update("listBand");
                            return prepareListInscritos();
                        }else{
                            JsfUtil.mensajeError("Hubo un error al crear el expediente.");
                            break;
                        }
                    }
                }else{
                    JsfUtil.mensajeError("El administrado no cuenta con una direción registrada");
                }
            }else{
                JsfUtil.mensajeError("No se encontró a la persona para inscripción");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al realizar el proceso de inscripción.");
        }
        return null;
    }
    
    public void inicializarRegistro(SbPersonaGt per){        
        registroInscripcion = new EppRegistro();        
        registroInscripcion.setId(null);
        registroInscripcion.setTipoProId(ejbTipoBaseFacade.findByCodProg("TP_PIRO_SEGU").get(0)); //CERTIFICACION DE CMS
        registroInscripcion.setTipoOpeId(ejbTipoBaseFacade.findByCodProg("TP_OPE_INI").get(0));  //INICIAL
        registroInscripcion.setTipoRegId( ejbTipoBaseFacade.findByCodProg("TP_REGIST_NOR").get(0) ); //INICIAL
        registroInscripcion.setEmpresaId(per);
        registroInscripcion.setFecha(new Date());
        registroInscripcion.setEstado(ejbTipoExplosivoFacade.tipoExplosivoXCodProg("TP_REGEV_CRE"));//CREADO
        registroInscripcion.setActivo(JsfUtil.TRUE);
        registroInscripcion.setAudLogin("USRWEB");
        registroInscripcion.setAudNumIp(JsfUtil.getIpAddress());
        
        registroInscripcion.setEppCertificado(new EppCertificado());
        registroInscripcion.getEppCertificado().setId(null);
        registroInscripcion.getEppCertificado().setEppCapaHorariodirList(new ArrayList());
        registroInscripcion.getEppCertificado().setActivo(JsfUtil.TRUE);
        registroInscripcion.getEppCertificado().setAudLogin(JsfUtil.getLoggedUser().getLogin());
        registroInscripcion.getEppCertificado().setAudNumIp(JsfUtil.getIpAddress());        
        registroInscripcion.getEppCertificado().setPersonaId(new SbPersonaGt());
        registroInscripcion.getEppCertificado().setTipoExplosivoList(new ArrayList());
        registroInscripcion.getEppCertificado().setNroCertificado(0);
        registroInscripcion.getEppCertificado().setFechaEmision(new Date());
        registroInscripcion.getEppCertificado().setCondDioexamen(JsfUtil.FALSE);
        registroInscripcion.getEppCertificado().setNotaEvaluacion(null);

        Calendar hoy = Calendar.getInstance();
        hoy.setTime(registroInscripcion.getEppCertificado().getFechaEmision());
        hoy.add(Calendar.MONTH, 12);
        registroInscripcion.getEppCertificado().setFechaVencimiento(hoy.getTime());
        registroInscripcion = (EppRegistro) JsfUtil.entidadMayusculas(registroInscripcion, "");
    }
    
    public String crearExpediente(){
        Integer usuaTramDoc = ejbUsuarioFacade.obtenerIdUsuarioTramDoc("USRWEB");
        String nroExpediente = null, asunto = null;
        
        if(usuaTramDoc != null){
            if(registroInscripcion.getEmpresaId().getRznSocial() == null ){
                asunto = registroInscripcion.getEmpresaId().getNumDoc() + " " + registroInscripcion.getEmpresaId().getNombreCompleto();
            }else{
                asunto = registroInscripcion.getEmpresaId().getRuc() + " " + registroInscripcion.getEmpresaId().getRznSocial();
            }
            
            nroExpediente = wsTramDocController.crearExpediente_capacitacion(recibo, registroInscripcion.getEmpresaId() , asunto , procesoCapacitacion, usuaTramDoc, usuaTramDoc); 
        }else{
            JsfUtil.mensajeAdvertencia("No se encontró al usuario USRWEB para creación de expediente");
        }
        
        return nroExpediente;
    }
    
    /**
     * REGISTRAR NUEVO EVENTO
     * @author Richar Fernández
     * @version 2.0
     * @param rrev Registro a crear evento
     */
    private void creaRegistroEvento(EppRegistro rrev) {
        //CREAMOS OBJ REGISTRO EVENTO
        EppRegistroEvento rev = new EppRegistroEvento();

        rev.setUserId(ejbUsuarioFacade.selectUsuario(JsfUtil.getLoggedUser().getLogin()).get(0));//USUARIO
        rev.setFecha(new Date());
        rev.setTipoEventoId(ejbTipoExplosivoFacade.findByCodProg("TP_REGEV_CRE").get(0));
        rev.setRegistroId(rrev);
        rev.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        rev.setAudNumIp(JsfUtil.getIpAddress());
        rev.setActivo(JsfUtil.TRUE);
        //SI LA LISTA DE EVENTOS ESTA NULA LA INICIALIZAMOS
        if (rrev.getEppRegistroEventoList() == null) {
            rrev.setEppRegistroEventoList(new ArrayList());
        }
        //AGREGAMOS EL OBJETO REGISTRO EVENTO A LA LISTA DEL REGISTRO
        rrev.getEppRegistroEventoList().add(rev);
    }
    
    public void openDlgDeclaracionInscripcion(){
        boolean validacion = true;
        setTerminos(false);
        
        if(listEppRegistroSelec.isEmpty()){
           validacion = false;
           JsfUtil.mensajeAdvertencia("Por favor seleccione por lo menos un registro borrador de inscripción.");
        }
        
        if(validacion){
            RequestContext.getCurrentInstance().execute("PF('wvDialogInscripcion').show()");
            RequestContext.getCurrentInstance().update("frmInscripcion");
        }
    }
    
    /**
    * MOSTRAR CAMPO FECHA PARA BUSQUEDA EN BANDEJA
    * @author Richar Fernández
    * @version 2.0
    */
    public void cambiaResultadoBusqueda(){        
        if (tipoBusqueda != null && tipoBusqueda.equals("3")){
            setMuestraBusquedafecha(true);
        }else
            setMuestraBusquedafecha(false);
    }
    
    /**
     * BORRAR REGISTRO SELECCIONADO
     *
     * @author Richar Fernández
     * @version 2.0
     * @param item
     */
    public void borrarRegistroListado(Map item) {
        try {
            registro = ejbEppCmpeInscripcionFacade.selectLike(Long.parseLong(""+item.get("ID")));
            registro.setActivo(JsfUtil.FALSE);
            ejbEppCmpeInscripcionFacade.edit(registro);
            
            resultadosBandeja.remove(item);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }
    
    ///////////// RECIBO //////////
    
    public List<SbRecibos> completeRecibo(String query){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            HashMap mMap = new HashMap();
            mMap.put("nroSecuencia", (query != null)?query.trim():null);
            mMap.put("importe", Double.parseDouble(JsfUtil.bundleBDIntegrado("EppCertificado_recibo_precio")) );
            mMap.put("codigo", Long.parseLong(JsfUtil.bundleBDIntegrado("EppCertificado_recibo_codigo")) );

            return ejbSbRecibosFacade.listarRecibosBusquedaSEL(registro.getPersonaId(), mMap);
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al buscar comprobante de pago.");
        }
        return null;
    }
    
    public String obtenerDescRecibo(SbRecibos reciboSelected){
        if(reciboSelected != null){
            return reciboSelected.getNroSecuencia() + " - " + ReportUtil.formatoFechaDdMmYyyy(reciboSelected.getFechaMovimiento());    
        }
        return null;
    }
    
    /**
     * Función al seleccionar el propietario del campo autocompletable
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void seleccionaRecibo() {
        setRenderRecibo(false);
    }

    /**
     * Función al eliminar propietario de campo autocompletable
     *
     * @author Richar Fernández
     * @version 1.0
     */
    public void eliminarReciboRegistro() {
        recibo = null;
        setRenderRecibo(true);
    }
    
    //////// PERSONA /////////
    
    /**
     * ABRIR FORMULARIO PARA REGISTRO DE NUEVA PERSONA
     * @author Richar Fernández
     * @version 2.0
     */
    public void openDlgNuevaPersona(){
        setDisabledReniec(true);
        setDisabledTipoDoc(true);
        setDisabledNombres(true);
        personaEdicion = new SbPersonaGt();
        direccionEdicion = new SbDireccionGt();
        listDistritos = new ArrayList();
        RequestContext.getCurrentInstance().execute("PF('PerViewDialog').show()");
        RequestContext.getCurrentInstance().update("persForm");
    }
    
    /**
     * GRABAR DATOS DE NUEVA PERSONA
     * @author Richar Fernández
     * @version 2.0
     */
    public void guardarPersona(){
        boolean validacion = true;
        
        if(personaEdicion.getTipoDoc() == null){
            validacion = false;
            JsfUtil.mensajeError("Debe seleccionar el tipo de documento");
        }
        if(personaEdicion.getNumDoc() == null || personaEdicion.getNumDoc().isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Debe ingresar el número de documento");
        }
        if(personaEdicion.getApePat() == null || personaEdicion.getApePat().isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Debe ingresar el apellido paterno");
        }
        if(personaEdicion.getApeMat() == null || personaEdicion.getApeMat().isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Debe ingresar el apellido materno");
        }
        if(personaEdicion.getNombres() == null || personaEdicion.getNombres().isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Debe ingresar los nombres");
        }
        if(personaEdicion.getGeneroId() == null){
            validacion = false;
            JsfUtil.mensajeError("Debe seleccionar el género");
        }
        if(personaEdicion.getEstCivilId() == null){
            validacion = false;
            JsfUtil.mensajeError("Debe seleccionar el estado civil");
        }
        if(direccionEdicion.getDireccion() == null || direccionEdicion.getDireccion().isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Debe ingresar la dirección");
        }
        if(direccionEdicion.getDistritoId() == null){
            validacion = false;
            JsfUtil.mensajeError("Debe seleccionar la ubicación");
        }
        
        if(validacion){
            if(validacionPersona()){
                personaEdicion = (SbPersonaGt) JsfUtil.entidadMayusculas(personaEdicion, "");
                direccionEdicion = (SbDireccionGt) JsfUtil.entidadMayusculas(direccionEdicion, "");
                
                direccionEdicion.setId(null);
                direccionEdicion.setActivo(JsfUtil.TRUE);
                direccionEdicion.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                direccionEdicion.setAudNumIp(JsfUtil.getIpAddress());
                        
                personaEdicion.setId(null);
                personaEdicion.setActivo(JsfUtil.TRUE);
                personaEdicion.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                personaEdicion.setAudNumIp(JsfUtil.getIpAddress());
                personaEdicion.setSbDireccionList(new ArrayList());
                personaEdicion.setTipoId(ejbTipoBaseFacade.findByCodProg("TP_PER_NAT").get(0));

                direccionEdicion.setPersonaId(personaEdicion);
                personaEdicion.getSbDireccionList().add(direccionEdicion);
                
                ejbPersonaFacade.create(personaEdicion);                
                seleccionarDlgPersona(personaEdicion);
                registro.setFotoRuta(null);
                RequestContext.getCurrentInstance().execute("PF('PerViewDialog').hide()");
            }
        }        
    }
    
    /**
     * VALIDACIONES AL CAMBIAR TIPO DE DOCUMENTO EN FORMULARIO
     * DE REGISTRO DE NUEVA PERSONA
     * @author Richar Fernández
     * @version 2.0
     */
    public void cambiaTipoDocumento(){
        personaEdicion.setNumDoc(null);
        personaEdicion.setNumDocVal(null);
        if (personaEdicion.getTipoDoc().getCodProg().equals("TP_DOCID_DNI")) {
            setDisabledTipoDoc(true);
            setDisabledReniec(true);
            setDisabledNombres(true);
        }else{
            setDisabledTipoDoc(false);
            setDisabledReniec(false);
            setDisabledNombres(false);
        }
    }
    
    /**
     * BUSCAR DOCUMENTO EN RENIEC
     * @author Richar Fernández
     * @version 2.0
     */
    public void buscarDocumento(){
        boolean flag = true;
        if(personaEdicion.getTipoDoc() == null){
            flag = false;
            JsfUtil.mensajeAdvertencia("Por favor, ingrese el tipo de documento");
        }
        if(personaEdicion.getNumDoc() == null || personaEdicion.getNumDoc().isEmpty()){
            flag = false;
            JsfUtil.mensajeAdvertencia("Por favor, ingrese el número de documento");
        }
        if (personaEdicion.getTipoDoc().getCodProg().equals("TP_DOCID_DNI")) {
            if(personaEdicion.getNumDocVal() == null || personaEdicion.getNumDocVal().isEmpty()){
                flag = false;
                JsfUtil.mensajeAdvertencia("Por favor, ingrese el número de documento completo");
            }
        }
        if(flag){
            if(validacionPersona()){
                SbPersonaGt personaTemp = wsPideController.buscarReniec_capacitacion(personaEdicion);
                if(personaTemp != null){
                    if(personaTemp.getApePat() != null){
                        personaEdicion.setApePat(personaTemp.getApePat());
                        personaEdicion.setApeMat(personaTemp.getApeMat());
                        personaEdicion.setNombres(personaTemp.getNombres());
                        personaEdicion.setActivo(JsfUtil.TRUE);
                        personaEdicion.setTipoDoc(personaTemp.getTipoDoc());
                        personaEdicion.setTipoId(personaTemp.getTipoId());
                        setDisabledReniec(false);
                        setDisabledNombres(true);
                    }else{
                        personaEdicion.setApeMat(null);
                        personaEdicion.setApePat(null);
                        personaEdicion.setNombres(null);
                        setDisabledReniec(true);
                        setDisabledNombres(true);                        
                    }
                }else{
                    personaEdicion.setApeMat(null);
                    personaEdicion.setApePat(null);
                    personaEdicion.setNombres(null);
                    setDisabledReniec(false);
                    setDisabledNombres(false);
                }
            }else{
                personaEdicion.setApeMat(null);
                personaEdicion.setApePat(null);
                personaEdicion.setNombres(null);
                setDisabledReniec(true);
            }
            RequestContext.getCurrentInstance().update("persForm");
        }
        
    }
    
    /**
     * VALIDACION DE DATOS DE FORM. PARA REGISTRO DE NUEVA PERSONA
     * @author Richar Fernández
     * @version 2.0
     * @return Flag de validación de persona
     */
    public boolean validacionPersona(){
        boolean valida = true;
        
        if (personaEdicion.getTipoDoc().getCodProg().equals("TP_DOCID_DNI")) {
            if (personaEdicion.getNumDoc().length() != 8) {
                JsfUtil.mensajeAdvertencia("El dni es de 8 digitos ");
                valida = false;                
            }
        }
        if (personaEdicion.getTipoDoc().getCodProg().equals("TP_DOCID_PAS")) {
            if (personaEdicion.getNumDoc().length() != 12) {
                JsfUtil.mensajeAdvertencia("El pasaporte es de 12 digitos ");
                valida = false;                
            }
        }

        if (personaEdicion.getTipoDoc().getCodProg().equals("TP_DOCID_CE")) {
            if (personaEdicion.getNumDoc().length() != 9) {
                JsfUtil.mensajeAdvertencia("El carnet de extranjería es de 9 digitos ");
                valida = false;                
            }
        }
        if (!ejbPersonaFacade.selectPersonaActivaxDoc(personaEdicion.getNumDoc()).isEmpty()) {
            JsfUtil.mensajeAdvertencia("! El numero de documento ya esta registrado ! Por favor intentar con la opción de búsqueda de persona. ");
            valida = false;
        }
                
        return valida;
    }
    

    /**
     * ABRIR FORMULARIO DE BUSQUEDA DE PERSONA
     * @author Richar Fernández
     * @version 2.0
     */
    public void openDlgBusqPersona(){
        filtro = null;
        tipoBusqueda = null;
        listaPersonasBusqueda = new ArrayList();
        RequestContext.getCurrentInstance().execute("PF('wvDialogBusquedaPersona').show()");
        RequestContext.getCurrentInstance().update("frmBuscPersona");
    }
    
    /**
     * FUNCION DE BUSQUEDA DE PERSONA 
     * @author Richar Fernández
     * @version 2.0
     */
    public void buscarDlgPersona(){
        boolean blnValidacion = true;

        if(tipoBusqueda != null){
            if(filtro == null || filtro.isEmpty()){
                blnValidacion = false;
                JsfUtil.mensajeAdvertencia("Debe ingresar el dato a buscar");
            }
        }else{
            if(tipoBusqueda == null && !filtro.isEmpty()){
                blnValidacion = false;
                JsfUtil.mensajeAdvertencia("Debe ingresar el tipo de búsqueda");
            }
        }
        
        if(blnValidacion)
            listaPersonasBusqueda = ejbPersonaFacade.buscarPersonaLME(tipoBusqueda,filtro);
    }

    
    /**
     * FUNCION DE SELECCION DE PERSONA EN FORMULARIO DE BUSQUEDA
     * @author Richar Fernández
     * @version 2.0
     * @param pers Persona seleccionada en formulario de búsqueda
     */
    public void seleccionarDlgPersona(SbPersonaGt pers){
        short contDireccion;
        int cont = ejbCertificadoFacade.buscarPersonaInscritaCapacitacion(registro.getCapacitacionId().getId().toString(), pers.getId().toString());
        if(cont == 0){
            direccionPersona = null;
            List<SbDireccionGt> lstDireccion = ejbDireccionFacade.listarDirecionPorPersona(pers);
            if(!lstDireccion.isEmpty()){
                
                contDireccion = 0;
                for (SbDireccionGt dir : lstDireccion) {
                    if (dir.getActivo() == 1 && dir.getTipoId().getCodProg().equals("TP_DIRECB_FIS")) {
                        contDireccion ++;
                        
                        if(dir.getDistritoId().getNombre().equals("MIGRACION")){
                            direccionPersona = null;
                            JsfUtil.mensajeError("La dirección de la persona seleccionada necesita actualizarse la ubicación.");
                            RequestContext.getCurrentInstance().update(obtenerForm()+":pnlParticipante");
                            RequestContext.getCurrentInstance().update(obtenerForm()+":pnlActividades");
                            return;
                        }
                        direccionPersona = dir;
                    }
                }
                
                if(contDireccion > 0){
                    registro.setParticipanteId(pers);
                    if(cont > 1){
                        JsfUtil.mensajeAdvertencia("La persona seleccionada cuenta con más de una dirección, por favor verificar antes de la emisión del carné");
                    }
                }else{
                    JsfUtil.mensajeError("La persona seleccionada no cuenta con una dirección activa, por favor verificar");    
                }
            }else{
                 JsfUtil.mensajeAdvertencia("La persona no tiene una dirección registrada");
            }

            RequestContext.getCurrentInstance().update(obtenerForm()+":pnlParticipante");
            RequestContext.getCurrentInstance().update(obtenerForm()+":pnlActividades");
        }else{
            JsfUtil.mensajeAdvertencia("La persona seleccionada ya se encuentra registrada en la capacitación.");
        }            
    }    
    
    //////////////////// COMPROBANTE ////////////////////
    
    public void cambiaTipoBusquedaComprobante(){
        //rfv        
    }
    
    public void buscarDlgComprobante(){
        //rfv
    }
    
    public void seleccionarDlgComprobante(){
        //rfv
    }
    
    
    ////////////////// LISTADO ///////////////////
    
     /**
     * FUINCION PARA ABRIR LISTADO DE CAPACITACIONES
     * @author Richar Fernández
     * @version 2.0
     * @return Página Listado para direccionar
     */
    public String prepareListInscritos(){
        reiniciarCamposBusqueda();
        resultadosBandeja = null;
        estado = EstadoCrud.BUSCAR;
        return "/aplicacion/gepp/eppCmpeInscripcion/List";
    }
    
    /**
     * BUSQUEDA DE BANDEJA
     * @author Richar Fernández
     * @version 2.0
     */
    public void buscarBandeja(){
        boolean validacion = true;
        
        if(tipoBusqueda != null || capacitacion != null || (fechaInicio !=null && fechaFin != null)){
            if (!(tipoBusqueda != null && ((filtro == null || filtro.isEmpty()) && tipoResultado == null))) {
                if(!((fechaInicio == null && fechaFin != null) || (fechaInicio != null && fechaFin == null))){
                    if((fechaInicio != null &&  fechaFin !=null) && ( fechaInicio.compareTo(fechaFin) > 0 )){
                        JsfUtil.mensajeAdvertencia("La fecha inicial no puede ser mayor que la fecha final, por favor ingresar correctamente las fechas");
                        validacion = false;
                    }
                }else{
                    JsfUtil.mensajeAdvertencia("Por favor ingrese ambas fechas");    
                    validacion = false;
                }
            }else{
                validacion = false;
                JsfUtil.mensajeAdvertencia("Por favor escriba el criterio de búsqueda");
            }
        }else{
            validacion = false;
            JsfUtil.mensajeAdvertencia("No ha ingresado un criterio de búsqueda adecuado, por favor ingrese un criterio de búsqueda adecuado según la opción seleccionado en Buscar por");
        }
        
        if(validacion){
            if(filtro != null)
                filtro = ((filtro.length()==0)?null:filtro.trim());
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            HashMap mMap = new HashMap();
            
            if(fechaInicio != null)
                fechaInicio = JsfUtil.getFechaSinHora(fechaInicio);
            if(fechaFin != null)
                fechaFin = JsfUtil.getFechaSinHora(fechaFin);
            
            mMap.put("fechaInicio", fechaInicio);
            mMap.put("fechaFin", fechaFin);
            mMap.put("tipoBusqueda", tipoBusqueda);
            mMap.put("filtro", filtro);
            mMap.put("tipoDoc", p_user.getTipoDoc());
            mMap.put("numDoc", p_user.getNumDoc());
            mMap.put("capacitacionId", (capacitacion == null)?null:capacitacion.getId() );
            mMap.put("tipoResultado", tipoResultado);

            resultadosBandeja = ejbRegistroFacade.buscarCapacitacionListadoSEL(mMap);
            reiniciarCamposBusqueda();
        }
    }
    
    /**
     * REINICAR VALORES DE CAMPOS DE BUSQUEDA DE BANDEJAS
     * @author Richar Fernández
     * @version 2.0
     */
    public void reiniciarCamposBusqueda(){
        tipoBusqueda = null;
        tipoResultado = null;
        filtro = null;
        fechaInicio = null;
        fechaFin = null;
        capacitacion = null;
        fechaBusqueda = null;
        listEppRegistroSelec = new ArrayList();
    }

    /**
     * FOARMATEAR CADENA DE NOTA PARA MOSTRARLA
     * @author Richar Fernández
     * @version 2.0
     * @param reg Capacitación para obtener lugar y fecha
     * @return Cadena de fecha formateada: ubigeo [ fechaini - fechafin año] 
     */
    public String obtenerLugarFechaCertificacionesMS(Map reg){
        String capacitacionCadena = "";
        
        if(reg != null){
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
             
            capacitacionCadena = reg.get("DEPARTAMENTO") + " - " +
                                 reg.get("PROVINCIA") + " - " +
                                 reg.get("DISTRITO") + 
                                 " [" + formatearFecha((Date) reg.get("FECHA_INICIO")) + " - " +  formatearFecha((Date) reg.get("FECHA_FIN")) + " " + formatAnio.format((Date) reg.get("FECHA_FIN")) + "]";
        }
        return capacitacionCadena;
    }
    
    /**
     * OBTENER CADENA DE ACTIVIDADES DE PARTICIPANTE
     * @author Richar Fernández
     * @version 2.0
     * @param lstActividades Listado de actividades disponibles
     * @return Cadena concatenando actividades
     */
    public String obtenerFormatoActividades(List<TipoExplosivoGt> lstActividades){
        String cadena = "";
        
        if( lstActividades != null && !lstActividades.isEmpty())
        {
            for(TipoExplosivoGt exp : lstActividades){
                cadena = cadena + ", " + exp.getNombre();
            }
            cadena= cadena.substring(2);
        }
        return cadena;
    }

    /**
     * FORMATO DE TEXTO DE RESULTADO DE CAPACITACION
     * @author Richar Fernández
     * @version 2.0
     * @param reg Certificado a validar nota de evaluación
     * @return Cadena de texto de aprobación de nota
     */
    public String formatoResultadoCap(Map reg){
        String cadena = "INSCRITO";
        
        if(reg.get("TIPOREG").toString().equals("2")){
            cadena = "BORRADOR";
        }else{
            Integer notaEvaluacion = (Integer) reg.get("NOTA_EVALUACION");

            if(notaEvaluacion != null){
                if(notaEvaluacion >= 12){
                    cadena = "APROBADO";
                }else{
                    cadena = "DESAPROBADO";
                }
            }
        }   
        
        return cadena;
    }    
    
    /////////////////////////// CONSTANCIA CERTIFICADO  ///////////////////////////
    
    /**
     * FUNCION PARA GENERACION DE CONSTANCIA DE CERTIFICADOS
     * @author Richar Fernández
     * @version 2.0
     * @param pReg Registro a crear constancia
     * @return Constancia
     */
    public StreamedContent constanciaListado(Map pReg) {
        try {
            List<EppRegistro> lp = ejbRegistroFacade.buscarRegistroXCertificado(Long.parseLong(pReg.get("ID").toString()));
            if(lp.isEmpty()){
                JsfUtil.mensajeAdvertencia("No se encontró el registro de certificado.");
                return null;
            }
            
            return JsfUtil.generarReportePdf("/aplicacion/gepp/capacitacion/reportes/ConstanciaCapacitacion.jasper", lp, parametrosPdf(lp.get(0)), "ConstanciaCapacitacion.pdf", null, null);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: EppCertificadoController.reporte :", ex);
        }
    }
    
    
    /**
     * PARAMETROS PARA GENERAR PDF
     * @author Richar Fernández
     * @version 2.0
     * @param ppdf Registro a crear constancia
     * @return Listado de parámetros para constancia
     */
    private HashMap parametrosPdf(EppRegistro ppdf) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();
        String tipoDoc = "";
        
        if(ppdf.getEmpresaId().getTipoDoc() != null){
            tipoDoc = ppdf.getEmpresaId().getTipoDoc().getNombre();
        }else{
            if(ppdf.getEmpresaId().getRuc() != null){
                tipoDoc = "RUC"; 
            }else{
                tipoDoc = "DNI"; 
            }
        }
        
        ph.put("P_LOGO_SUCAMEC", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
        ph.put("P_SOL_TIPODOC",  tipoDoc + " " + ((ppdf.getEmpresaId().getRuc() == null)?ppdf.getEmpresaId().getNumDoc():ppdf.getEmpresaId().getRuc()) );
        ph.put("P_SOL_RZNSOCIAL", (ppdf.getEmpresaId().getRznSocial() == null)?(ppdf.getEmpresaId().getApePat() + " " + ppdf.getEmpresaId().getApeMat() + " " + ppdf.getEmpresaId().getNombres()):(ppdf.getEmpresaId().getRznSocial()));
        ph.put("P_PAR_TIPODOC", ppdf.getEppCertificado().getPersonaId().getTipoDoc().getNombre() + " " + ppdf.getEppCertificado().getPersonaId().getNumDoc());
        ph.put("P_PAR_APELLIDOS", ppdf.getEppCertificado().getPersonaId().getApePat() + " " + ppdf.getEppCertificado().getPersonaId().getApeMat() + " " + ppdf.getEppCertificado().getPersonaId().getNombres() );
        ph.put("P_PAR_ACTIVIDADES", obtenerFormatoActividades(ppdf.getEppCertificado().getTipoExplosivoList() ) );
        ph.put("P_CAP_LUGAR", obtenerLugarFechaCertificacionesConstancia(ppdf.getEppCertificado().getCapacitacionId()) );
        ph.put("P_CAP_FECHAINS", mostrarFechaPaso4(ppdf.getFecha()) );
        ph.put("P_FECHA_REG", ppdf.getEppCertificado().getCapacitacionId().getSedeOrganizaId().getAbreviatura() + ", " + mostrarFechaString(ppdf.getFecha()) );

        return ph;
    }  
    
    /**
     * FUNCION PARA FORMATEAR FECHA AL FORMATO: dd/MM/yyyy
     * @author Richar Fernández
     * @version 2.0
     * @param param Fecha a formatear
     * @return Cadena con fecha formateada: dd/MM/yyyy
     */
    public static String mostrarFechaDdMmYyyy(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);

            return dia + "/" + mes + "/" + anio;
        } else {
            return "BORRADOR";
        }
    }

    /**
     * FUNCION PARA DESCRIBIR FECHA EN EL FORMATO: xx de xxxxx de xxxx
     * @author Richar Fernández
     * @version 2.0
     * @param param Fecha a formatear
     * @return Cadena con fecha formateada: dd de MMM de yyyy
     */
    public static String mostrarFechaString(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);

            mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());

            return dia + " de " + mes + " de " + anio;
        } else {
            return "BORRADOR";
        }
    }
    
    /**
     * FORMATEAR FECHA PARA MOSTRAR EN PASO 4
     * @author Richar Fernández
     * @version 2.0
     * @param fecha Fecha a formatear
     * @return Cadena de fecha formateada: dd MMMM yyyy
     */
    public String mostrarFechaPaso4(Date fecha){
        SimpleDateFormat formato = new SimpleDateFormat("dd MMMM yyyy", new Locale("ES"));
        return formato.format(fecha).toUpperCase();
    }    
    
    //////////////////////////////////////////////
    
    
    
}
