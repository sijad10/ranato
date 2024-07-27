package pe.gob.sucamec.bdintegrado.jsf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import pe.gob.sucamec.bdintegrado.data.SbCsCertifsalud;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import java.util.Objects;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.data.SbCsCertifMedico;
import pe.gob.sucamec.bdintegrado.data.SbCsEstablecimiento;
import pe.gob.sucamec.bdintegrado.data.SbCsHorario;
import pe.gob.sucamec.bdintegrado.data.SbCsMedico;
import pe.gob.sucamec.bdintegrado.data.SbCsMedicoEstabsal;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.ws.WsPide;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;
import wspide.ResPideMigra;

// Nombre de la instancia en la aplicacion //
@Named("sbCsCertifsaludController")
@SessionScoped

/**
 * Clase con SbCsCertifsaludController instanciada como
 * sbCsCertifsaludController. Contiene funciones utiles para la entidad
 * SbCsCertifsalud. Esta vinculada a las páginas SbCsCertifsalud/create.xhtml,
 * SbCsCertifsalud/update.xhtml, SbCsCertifsalud/list.xhtml,
 * SbCsCertifsalud/view.xhtml Nota: Las tablas deben tener la estructura de
 * Sucamec para que funcione adecuadamente, revisar si tiene el campo activo y
 * modificar las búsquedas.
 */
public class SbCsCertifsaludController implements Serializable {

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
    SbCsCertifsalud registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<SbCsCertifsalud> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<SbCsCertifsalud> registrosSeleccionados;
    // LISTADO  //
    private Integer tipoBusqueda;
    private Date fechaSolicitante;
    private boolean renderSolicitante;
    private boolean renderFechaSolicitante;
    private List<SbCsMedicoEstabsal> lstMedicos;
    private List<SbCsCertifMedico> lstMedicosFirman;
    private List<SbCsCertifMedico> lstMedicosSeleccionados;
    private SbCsMedico medico;
    private SbCsMedicoEstabsal medicoEstab;
    private List<SbCsMedicoEstabsal> medicoEstabSeleccionados;
    private SbPersonaGt solicitanteRegistro = new SbPersonaGt();
    private boolean disabledApellidos;
    private int maxLengthDoc;
    private String nroCertificado;
    private SbCsEstablecimiento establecimiento;
    private TipoBaseGt estadoCertif;
    private int centrosHabilitados;
    private String imagenAyuda;
    private boolean busquedaCertifExternos;
    private boolean mostrarAdicionalNroCertif;
    private String complementoCertificado;
    private List<SbCsHorario> listaHorarioCs;
    private List<SbCsEstablecimiento> listaEstablecimiento;
    /////
    private byte[] fotoByte;
    private UploadedFile file;
    private StreamedContent foto;
    private String archivo;
    private String archivoTemporal;
    ///// Reporte
    private Integer anio;
    private List<Map> listadoRep;
    ///////////

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbCsCertifsaludFacade ejbSbCsCertifsaludFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbCsEstablecimientoFacade ejbSbCsEstablecimientoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt ejbSbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt ejbSbPersonaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade ejbNumeracionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    
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
    public List<SbCsCertifsalud> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<SbCsCertifsalud> a) {
        this.registrosSeleccionados = a;
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
            for (SbCsCertifsalud a : registrosSeleccionados) {
                a.setActivo((short) 0);
                ejbSbCsCertifsaludFacade.edit(a);
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
            for (SbCsCertifsalud a : registrosSeleccionados) {
                a.setActivo((short) 1);
                ejbSbCsCertifsaludFacade.edit(a);
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
            registro = (SbCsCertifsalud) resultados.getRowData();
            registro.setActivo((short) 1);
            ejbSbCsCertifsaludFacade.edit(registro);
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
            registro = (SbCsCertifsalud) resultados.getRowData();
            registro.setActivo((short) 0);
            ejbSbCsCertifsaludFacade.edit(registro);
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
    public SbCsCertifsalud getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SbCsCertifsalud registro) {
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

    public Integer getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(Integer tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public boolean isRenderSolicitante() {
        return renderSolicitante;
    }

    public void setRenderSolicitante(boolean renderSolicitante) {
        this.renderSolicitante = renderSolicitante;
    }

    public boolean isRenderFechaSolicitante() {
        return renderFechaSolicitante;
    }

    public void setRenderFechaSolicitante(boolean renderFechaSolicitante) {
        this.renderFechaSolicitante = renderFechaSolicitante;
    }

    public Date getFechaSolicitante() {
        return fechaSolicitante;
    }

    public void setFechaSolicitante(Date fechaSolicitante) {
        this.fechaSolicitante = fechaSolicitante;
    }

    public List<SbCsMedicoEstabsal> getLstMedicos() {
        return lstMedicos;
    }

    public void setLstMedicos(List<SbCsMedicoEstabsal> lstMedicos) {
        this.lstMedicos = lstMedicos;
    }

    public SbCsMedico getMedico() {
        return medico;
    }

    public void setMedico(SbCsMedico medico) {
        this.medico = medico;
    }

    public List<SbCsCertifMedico> getLstMedicosFirman() {
        return lstMedicosFirman;
    }

    public void setLstMedicosFirman(List<SbCsCertifMedico> lstMedicosFirman) {
        this.lstMedicosFirman = lstMedicosFirman;
    }

    public List<SbCsCertifMedico> getLstMedicosSeleccionados() {
        return lstMedicosSeleccionados;
    }

    public void setLstMedicosSeleccionados(List<SbCsCertifMedico> lstMedicosSeleccionados) {
        this.lstMedicosSeleccionados = lstMedicosSeleccionados;
    }

    public SbCsMedicoEstabsal getMedicoEstab() {
        return medicoEstab;
    }

    public void setMedicoEstab(SbCsMedicoEstabsal medicoEstab) {
        this.medicoEstab = medicoEstab;
    }

    public SbPersonaGt getSolicitanteRegistro() {
        return solicitanteRegistro;
    }

    public void setSolicitanteRegistro(SbPersonaGt solicitanteRegistro) {
        this.solicitanteRegistro = solicitanteRegistro;
    }

    public boolean isDisabledApellidos() {
        return disabledApellidos;
    }

    public void setDisabledApellidos(boolean disabledApellidos) {
        this.disabledApellidos = disabledApellidos;
    }

    public int getMaxLengthDoc() {
        return maxLengthDoc;
    }

    public void setMaxLengthDoc(int maxLengthDoc) {
        this.maxLengthDoc = maxLengthDoc;
    }

    public String getNroCertificado() {
        return nroCertificado;
    }

    public void setNroCertificado(String nroCertificado) {
        this.nroCertificado = nroCertificado;
    }

    public byte[] getFotoByte() {
        return fotoByte;
    }

    public void setFotoByte(byte[] fotoByte) {
        this.fotoByte = fotoByte;
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

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getArchivoTemporal() {
        return archivoTemporal;
    }

    public void setArchivoTemporal(String archivoTemporal) {
        this.archivoTemporal = archivoTemporal;
    }

    public SbCsEstablecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(SbCsEstablecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public TipoBaseGt getEstadoCertif() {
        return estadoCertif;
    }

    public void setEstadoCertif(TipoBaseGt estadoCertif) {
        this.estadoCertif = estadoCertif;
    }

    public int getCentrosHabilitados() {
        return centrosHabilitados;
    }

    public void setCentrosHabilitados(int centrosHabilitados) {
        this.centrosHabilitados = centrosHabilitados;
    }

    public String getImagenAyuda() {
        return imagenAyuda;
    }

    public void setImagenAyuda(String imagenAyuda) {
        this.imagenAyuda = imagenAyuda;
    }

    public List<SbCsMedicoEstabsal> getMedicoEstabSeleccionados() {
        return medicoEstabSeleccionados;
    }

    public void setMedicoEstabSeleccionados(List<SbCsMedicoEstabsal> medicoEstabSeleccionados) {
        this.medicoEstabSeleccionados = medicoEstabSeleccionados;
    }

    public boolean isBusquedaCertifExternos() {
        return busquedaCertifExternos;
    }

    public void setBusquedaCertifExternos(boolean busquedaCertifExternos) {
        this.busquedaCertifExternos = busquedaCertifExternos;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public List<Map> getListadoRep() {
        return listadoRep;
    }

    public void setListadoRep(List<Map> listadoRep) {
        this.listadoRep = listadoRep;
    }

    public boolean isMostrarAdicionalNroCertif() {
        return mostrarAdicionalNroCertif;
    }

    public void setMostrarAdicionalNroCertif(boolean mostrarAdicionalNroCertif) {
        this.mostrarAdicionalNroCertif = mostrarAdicionalNroCertif;
    }

    public List<SbCsHorario> getListaHorarioCs() {
        return listaHorarioCs;
    }

    public void setListaHorarioCs(List<SbCsHorario> listaHorarioCs) {
        this.listaHorarioCs = listaHorarioCs;
    }

    public List<SbCsEstablecimiento> getListaEstablecimiento() {
        return listaEstablecimiento;
    }

    public void setListaEstablecimiento(List<SbCsEstablecimiento> listaEstablecimiento) {
        this.listaEstablecimiento = listaEstablecimiento;
    }

    public String getComplementoCertificado() {
        return complementoCertificado;
    }

    public void setComplementoCertificado(String complementoCertificado) {
        this.complementoCertificado = complementoCertificado;
    }

    /**
     * Propiedad para los resultados de la búsqueda del CRUD.
     *
     * @return Los resultados de la búsqueda.
     */
    public ListDataModel<SbCsCertifsalud> getResultados() {
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
    public List<SbCsCertifsalud> getSelectItems() {
        return ejbSbCsCertifsaludFacade.findAll();
    }

    /**
     * Cambia el estado del controller a buscar
     */
    public void mostrarBuscar() {
        if (resultados != null) {
            //buscar();
        }
        estado = EstadoCrud.BUSCAR;
    }
    
    public void verificaNombreNroCertificado(){
        String nroCertificadoTemp = registro.getNumero();
        try {
            mostrarAdicionalNroCertif = false;
            //nroCertificado
            if(nroCertificadoTemp.contains("-") && nroCertificadoTemp.contains("/") ){
                String[] partsNroCertif = nroCertificadoTemp.split("-");
                if(partsNroCertif.length == 2){
                    String[] partsAditonal = partsNroCertif[1].split("/");

                    if(partsAditonal.length == 2){

                        if(Integer.parseInt(partsAditonal[0]) >= 1 && Integer.parseInt(partsAditonal[0]) <= 12 && 
                           Integer.parseInt(partsAditonal[1]) >= 2015 ){
                            nroCertificado = partsNroCertif[0];
                            complementoCertificado = partsNroCertif[1];
                            mostrarAdicionalNroCertif = true;
                        }
                    }
                }        
            }
        } catch (Exception e) {
            mostrarAdicionalNroCertif = false;
        }
    }
    
    public void cargarVariables(){
        
        verificaNombreNroCertificado();
        if(!mostrarAdicionalNroCertif){
            nroCertificado = registro.getNumero();
            complementoCertificado = JsfUtil.formatearFecha(registro.getFechaEmision(), "MM") + "/" + JsfUtil.formatearFecha(registro.getFechaEmision(), "YYYY");
            if(estado == EstadoCrud.EDITAR || estado == EstadoCrud.RECTIFICAR){
                mostrarAdicionalNroCertif = true;
                JsfUtil.mensajeAdvertencia("Por favor ingresar correctamente el nro. de certificado con el formato correcto");
                JsfUtil.invalidar(obtenerForm()+ ":certificado");                
            }
        }
        
        archivoTemporal = registro.getRutaDocumento();
    }
    
    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        try {
            reiniciarValores();
            registro = (SbCsCertifsalud) resultados.getRowData();
            resultados = null;
            estado = EstadoCrud.VER;
            
            cargarVariables();            
            setRenderSolicitante(false);
            cargarFoto();
            
            for(SbCsCertifMedico med : registro.getSbCsCertifMedicoList() ){
                if(med.getActivo() == 1){
                    lstMedicosFirman.add(med);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar formulario");
        }
    }
    
    /**
     * Función tipo 'actionListener' que muestra el formulario para rectificar un
     * registro
     */
    public void mostrarRectificar() {
        try {
            reiniciarValores();
            SbCsCertifsalud registroRect = (SbCsCertifsalud) resultados.getRowData();
            registro = new SbCsCertifsalud();            
            registro.setActivo(JsfUtil.TRUE);
            registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            registro.setAudNumIp(JsfUtil.getIpAddress());
            registro.setCondicionObtenidaId(registroRect.getCondicionObtenidaId());
            registro.setEstablecimientoId(registroRect.getEstablecimientoId());
            registro.setEstadoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_ECS_CRE"));
            registro.setFechaEmision(registroRect.getFechaEmision());
            registro.setFechaPresentacion(null);
            registro.setFechaRegistro(new Date());
            registro.setFechaVencimiento(registroRect.getFechaVencimiento());
            registro.setId(registroRect.getId());   // temporalmente para validaciones
            registro.setNroHistClinica(registroRect.getNroHistClinica());
            registro.setNumero(registroRect.getNumero());
            registro.setRutaDocumento(registroRect.getRutaDocumento());
            registro.setSbCsCertifMedicoList(new ArrayList());
            registro.setSolicitanteId(registroRect.getSolicitanteId());
            registro.setTipoCertificadoId(registroRect.getTipoCertificadoId());
            registro.setUsuarioId(ejbSbUsuarioFacade.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
            registro.setCertificadoId(registroRect);
            registro.setEspecieValorada(registroRect.getEspecieValorada());

            resultados = null;
            estado = EstadoCrud.RECTIFICAR;
            
            cargarVariables();
            seleccionaSolicitante();
            cambiarEstablecimientoCS();
            cambiarEstablecimiento();
            cargarFoto();
            
            for(SbCsCertifMedico med : registroRect.getSbCsCertifMedicoList() ){
                if(med.getActivo() == 1){
                    SbCsCertifMedico medCertif = new SbCsCertifMedico();
                    medCertif.setActivo(JsfUtil.TRUE);
                    medCertif.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    medCertif.setAudNumIp(JsfUtil.getIpAddress());
                    medCertif.setCargoId(med.getCargoId());
                    medCertif.setCertifmedicoId(med.getCertifmedicoId());
                    medCertif.setId(JsfUtil.tempId());
                    medCertif.setMedicoId(med.getMedicoId());
                    registro.getSbCsCertifMedicoList().add(medCertif);
                    lstMedicosFirman.add(medCertif);
                }
            }            
            
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar formulario");
        }
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     */
    public void mostrarEditar() {
        try {
            reiniciarValores();
            registro = (SbCsCertifsalud) resultados.getRowData();
            resultados = null;
            estado = EstadoCrud.EDITAR;
            
            cargarVariables();
            seleccionaSolicitante();
            cambiarEstablecimientoCS();
            cambiarEstablecimiento();
            cargarFoto();
            
            for(SbCsCertifMedico med : registro.getSbCsCertifMedicoList() ){
                if(med.getActivo() == 1){
                    lstMedicosFirman.add(med);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar formulario");
        }
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     */
    public void mostrarCrear() {
        try {
            reiniciarValores();
            resultados = null;
            estado = EstadoCrud.CREAR;
            registro = new SbCsCertifsalud();
            registro.setSbCsCertifMedicoList(new ArrayList());
            registro.setActivo(JsfUtil.TRUE);
            registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            registro.setAudNumIp(JsfUtil.getIpAddress());
            registro.setFechaRegistro(new Date());
            // rfv - temporal
            //registro.setFechaEmision(JsfUtil.getFechaSinHora(new Date()));
            //calcularFechaFin();
            registro.setEstadoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_ECS_CRE"));
            registro.setTipoCertificadoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_TCERT_SML"));
            registro.setUsuarioId(ejbSbUsuarioFacade.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
            complementoCertificado = JsfUtil.formatearFecha(new Date(), "MM") + "/" + JsfUtil.formatearFecha(new Date(), "YYYY");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar formulario");
        }
    }

    /**
     * Evento para guardar la información al editar un registro.
     * @return 
     */
    public String editar() {
        try {
            int cont = 0;
            if(validacionCreateCertificado()) {
                registro = (SbCsCertifsalud) JsfUtil.entidadMayusculas(registro, "");
                registro.setNumero(nroCertificado+"-"+complementoCertificado);
                JsfUtil.borrarIds(lstMedicosFirman);
                
                if(registro.getSolicitanteId().getId() == null){
                    ejbSbPersonaFacade.create(registro.getSolicitanteId());    
                }
                
                if(estado == EstadoCrud.RECTIFICAR){
                    registro.setId(null);
                    registro.setSbCsCertifMedicoList(new ArrayList());
                    for(SbCsCertifMedico certif : lstMedicosFirman){
                        certif.setCertifmedicoId(registro);
                        registro.getSbCsCertifMedicoList().add(certif);
                    }
                }else{
                    for(SbCsCertifMedico certif: registro.getSbCsCertifMedicoList()){
                        cont = 0;
                        for(SbCsCertifMedico certif2: lstMedicosFirman){                        
                            if(Objects.equals(certif.getId(),certif2.getId()) ){
                                cont++;
                                break;
                            }                        
                        }
                        if(cont == 0){
                            certif.setActivo(JsfUtil.FALSE);
                            lstMedicosFirman.add(certif);
                        }
                    }
                    registro.setSbCsCertifMedicoList(lstMedicosFirman);
                }                    
                
                if(!Objects.equals(archivoTemporal, archivo)){
                    String fileName = file.getFileName();
                    String nombreFoto = "CERTIF_SALUD_"+ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_CERTSAL") + fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                    registro.setRutaDocumento(nombreFoto.toUpperCase());
                    FileUtils.writeByteArrayToFile(new File(JsfUtil.bundleBDIntegrado("Documentos_pathUpload_certificadoSalud") +  registro.getRutaDocumento() ), fotoByte );
                }

                if(estado == EstadoCrud.RECTIFICAR){
                    ejbSbCsCertifsaludFacade.create(registro);
                    registro.getCertificadoId().setActivo(JsfUtil.FALSE);
                    ejbSbCsCertifsaludFacade.edit(registro.getCertificadoId());
                    JsfUtil.mensaje("Registro rectificado correctamente");
                }else{
                    ejbSbCsCertifsaludFacade.edit(registro);
                    JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
                }
                
                return prepareCertificadoDeSalud();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     * @return Página a redireccionar
     */
    public String crear() {
        try {
            String nroCertificadoSalud = "";
            if(validacionCreateCertificado()){
                registro = (SbCsCertifsalud) JsfUtil.entidadMayusculas(registro, "");
                
                if(registro.getSolicitanteId().getId() == null){
                    ejbSbPersonaFacade.create(registro.getSolicitanteId());    
                }
                nroCertificadoSalud = nroCertificado + "-"+ complementoCertificado;
                registro.setNumero(nroCertificadoSalud);
                JsfUtil.borrarIds(lstMedicosFirman);
                
                for(SbCsCertifMedico certif : lstMedicosFirman){
                    certif.setCertifmedicoId(registro);
                    registro.getSbCsCertifMedicoList().add(certif);
                }
                
                String fileName = file.getFileName();
                String nombreFoto = "CERTIF_SALUD_"+ejbNumeracionFacade.buscarNumeracionActual("TP_NUM_CERTSAL") + fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                registro.setRutaDocumento(nombreFoto.toUpperCase());
                FileUtils.writeByteArrayToFile(new File(JsfUtil.bundleBDIntegrado("Documentos_pathUpload_certificadoSalud") +  registro.getRutaDocumento() ), fotoByte );
                
                ejbSbCsCertifsaludFacade.create(registro);
                JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado") + " " + registro.getId());                
                return prepareCertificadoDeSalud();
            }
        } catch (Exception e) {
            e.printStackTrace();            
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        return null;
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public SbCsCertifsalud getSbCsCertifsalud(Long id) {
        return ejbSbCsCertifsaludFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public SbCsCertifsaludController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sbCsCertifsaludConverter")
    public static class SbCsCertifsaludControllerConverterN extends SbCsCertifsaludControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SbCsCertifsalud.class)
    public static class SbCsCertifsaludControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SbCsCertifsaludController c = (SbCsCertifsaludController) JsfUtil.obtenerBean("sbCsCertifsaludController", SbCsCertifsaludController.class);
            return c.getSbCsCertifsalud(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbCsCertifsalud) {
                SbCsCertifsalud o = (SbCsCertifsalud) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SbCsCertifsalud.class.getName());
            }
        }
    }
    ///////////////////////////////////
    
    /**
     * FUNCION DE VALIDACIÓN AL CAMBIAR EL TIPO DE BÚSQUEDA
     * @author Richar Fernández
     * @version 1.0
     */
    public void cambiaTipoBusqueda(){
        filtro = null;
        fechaSolicitante = null;
        estadoCertif = null;
        if (tipoBusqueda != null && tipoBusqueda.equals(4)) {
            setRenderFechaSolicitante(true);
        } else {
            setRenderFechaSolicitante(false);
        }
        RequestContext.getCurrentInstance().update("listForm:pnlBusquedaCriterio");
    }
    
    /**
     * FUNCION PARA LIMPIAR VALORES DE LISTADO DE BÚSQUEDA
     * @author Richar Fernández
     * @version 1.0
     */
    public void reiniciarCamposBusqueda(){
        filtro = null;
        tipoBusqueda = null;
        setRenderFechaSolicitante(false);
        fechaSolicitante = null;
        establecimiento = null;
        estadoCertif = null;
        setBusquedaCertifExternos(false);
    }    
    
    /**
     * FUNCION PARA BUSCAR EN EL LISTADO DE CERTIFICADO DE SALUD
     * @author Richar Fernández
     * @version 1.0
     */
    public void buscarBandeja(){
        List<SbCsCertifsalud> listado = new ArrayList();

        if (tipoBusqueda == null && establecimiento == null) {
            JsfUtil.mensajeError("Debe seleccionar al menos un criterio de búsqueda para mostrar resultados");
            return;
        }
        if(tipoBusqueda != null && tipoBusqueda == 1){
            setBusquedaCertifExternos(true);    // Solo habilitado cuando busca por DNI
        }
        
        if (!((tipoBusqueda != null) && ((filtro == null || filtro.isEmpty()) && fechaSolicitante == null && estadoCertif == null)) || (establecimiento != null)  ) {
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            
            resultados = null;

            HashMap mMap = new HashMap();
            mMap.put("ruc", p_user.getNumDoc());
            mMap.put("buscarPor", tipoBusqueda);
            mMap.put("filtro", (filtro != null) ? filtro.trim().toUpperCase() : filtro);
            mMap.put("fechaSolicitante", fechaSolicitante );
            mMap.put("establecimiento", (establecimiento != null)?establecimiento.getId():null );
            mMap.put("estadoCertif", (estadoCertif != null)?estadoCertif.getId():null );
            mMap.put("busquedaCertifExternos", isBusquedaCertifExternos() );

            listado = ejbSbCsCertifsaludFacade.buscarCertificadosMedicos(mMap);
            if (listado != null) {
                resultados = new ListDataModel(listado);
            }
            reiniciarCamposBusqueda();
        } else {
            JsfUtil.mensajeError("Por favor, ingresar el campo a buscar");
        }
    }
    
    /**
     * FUNCION PARA RENDERIZAR EL BOTÓN VER DEL LISTADO DE CERTIF. DE SALUD
     * @author Richar Fernández
     * @version 1.0
     * @param reg Certificado de salud a validar
     * @return Booleano para mostrar o no el botón
     */
    public boolean renderBtnVer(SbCsCertifsalud reg){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        return (reg.getEstablecimientoId().getHabilitado() == 1) && (p_user.getNumDoc().equals(reg.getEstablecimientoId().getPropietarioId().getRuc()));
    }
    
    /**
     * FUNCION PARA RENDERIZAR EL BOTÓN EDITAR DEL LISTADO DE CERTIF. DE SALUD
     * @author Richar Fernández
     * @version 1.0
     * @param reg Certificado de salud a validar
     * @return Booleano para mostrar o no el botón
     */
    public boolean renderBtnEditar(SbCsCertifsalud reg){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        return (reg.getEstadoId().getCodProg().equals("TP_ECS_CRE") && (reg.getEstablecimientoId().getHabilitado() == 1)) && (p_user.getNumDoc().equals(reg.getEstablecimientoId().getPropietarioId().getRuc()));
    }
    
    /**
     * FUNCION PARA RENDERIZAR EL BOTÓN RECTIFICAR DEL LISTADO DE CERTIF. DE SALUD
     * @author Richar Fernández
     * @version 1.0
     * @param reg Certificado de salud a validar
     * @return Booleano para mostrar o no el botón
     */
    public boolean renderBtnRectificar(SbCsCertifsalud reg){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        boolean render = false;
        Calendar c_hoy = Calendar.getInstance();
        Calendar c_reg = Calendar.getInstance();
        Date fechaPresentacion = getFechaPresentacionInicial(reg);
        
        if(fechaPresentacion != null){
            c_reg.setTime(fechaPresentacion);
            c_reg.add(Calendar.HOUR_OF_DAY, 48);    //se le aumenta 48 horas
            if(c_reg.getTime().compareTo(c_hoy.getTime()) >= 0){
                render = true;
            }
        }

        return reg.getEstadoId().getCodProg().equals("TP_ECS_TRA") && render && (p_user.getNumDoc().equals(reg.getEstablecimientoId().getPropietarioId().getRuc())) ;
    }
    
    /**
     * FUNCION PARA RENDERIZAR EL BOTÓN VER DEL LISTADO DE CERTIF. DE SALUD
     * @author Richar Fernández
     * @version 1.0
     * @param reg Certificado de salud a validar
     * @return Booleano para mostrar o no el botón
     */
    public boolean renderBtnBorrar(SbCsCertifsalud reg){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        return (reg.getEstadoId().getCodProg().equals("TP_ECS_CRE") && (reg.getEstablecimientoId().getHabilitado() == 1)) && (p_user.getNumDoc().equals(reg.getEstablecimientoId().getPropietarioId().getRuc()));
    }
    
    /**
     * FUNCION RECURSIVA PARA OBTENER LA FECHA INICIAL DE PRESENTACION
     * @author Richar Fernández
     * @version 1.0
     * @param reg Certificado de salud a validar
     * @return Fecha de presentación inicial
     */
    public Date getFechaPresentacionInicial(SbCsCertifsalud reg){
        Date fecha = null;
        if(reg.getFechaPresentacion() != null){
            fecha = reg.getFechaPresentacion();
        }
        if(reg.getCertificadoId() != null){
            fecha = getFechaPresentacionInicial(reg.getCertificadoId());
        }
        
        return fecha;
    }
    
    /**
     * Función para borrar registro de certif. de salud
     * @author Richar Fernández
     * @version 1.0
     */
    public void borrarRegistro() {
        try {
            registro = (SbCsCertifsalud) resultados.getRowData();
            List<SbCsCertifsalud> lstTemporal = (List<SbCsCertifsalud>) resultados.getWrappedData();
            
            registro.setActivo(JsfUtil.FALSE);
            ejbSbCsCertifsaludFacade.edit(registro);
            
            if(registro.getCertificadoId() != null){
                registro.getCertificadoId().setActivo(JsfUtil.TRUE);
                ejbSbCsCertifsaludFacade.edit(registro.getCertificadoId());
                lstTemporal.add(registro.getCertificadoId());
            }
            
            lstTemporal.remove(registro);
            resultados = new ListDataModel(lstTemporal);

            JsfUtil.mensaje("Se ha borrado correctamente el registro.");
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }
    
    /**
     * FUNCION PARA VALIDAR SI EL USUARIO TIENE PERFIL DE CENTRO DE SALUD
     * @author Richar Fernández
     * @version 1.0
     * @return Booleano si tiene perfil Centro de salud
     */
    public boolean esRolCentroSalud(){
        return JsfUtil.buscarPerfilUsuario("AMA_CSALUD");
    }

    /**
     * FUNCION PARA REINICIAR VALORES AL CARGAR LISTADO
     * @author Richar Fernández
     * @version 1.0
     */
    public void reiniciarValores(){
        setRenderSolicitante(true);
        setDisabledApellidos(false);
        setMostrarAdicionalNroCertif(true);
        medico = null;
        medicoEstab = null;
        solicitanteRegistro = new SbPersonaGt();
        nroCertificado = null;
        fotoByte = null;
        file = null;
        foto = null;
        archivo = null;
        lstMedicos = new ArrayList();
        lstMedicosFirman = new ArrayList();
        lstMedicosSeleccionados = new ArrayList();
        medicoEstabSeleccionados = new ArrayList();        
    }
    
    /**
     * FUNCION PARA DIRECCIONAR AL LISTADO DE CERTIFICADO DE SALUD
     * @author Richar Fernández
     * @version 1.0
     * @return Página para direccionar
     */
    public String prepareCertificadoDeSalud(){
        if(esRolCentroSalud()){
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            reiniciarCamposBusqueda();
            centrosHabilitados = 0;
            listaHorarioCs = new ArrayList();
            
            listaEstablecimiento = ejbSbCsEstablecimientoFacade.listarEstablecimientosHabilitadosByRuc(p_user.getNumDoc());
            if(listaEstablecimiento != null){
                centrosHabilitados = listaEstablecimiento.size();
            }
            
            resultados = null;
            estado = EstadoCrud.BUSCAR;
            
            if(centrosHabilitados == 0){
                int cont = ejbSbCsEstablecimientoFacade.contarEstablecimientosHabilitadosSinMinsaByRuc(p_user.getNumDoc());
                if(cont > 0){
                    JsfUtil.mensajeAdvertencia("Ud. cuenta con algún centro de salud habilitado pero no tiene registrado la resolución del MINSA. Por favor solicite su actualización.");
                }else{
                    JsfUtil.mensajeAdvertencia("Ud. no cuenta con algún centro de salud habilitado o vigente. Por favor, solicite su habilitación como Centro de Salud autorizado en la Plataforma Virtual SEL");    
                }
            }else{
                int contInhabilitados = ejbSbCsEstablecimientoFacade.contarEstablecimientosInhabilitadosByRuc(p_user.getNumDoc());
                if(contInhabilitados > 0){
                    JsfUtil.mensajeAdvertencia("Ud. tiene "+ contInhabilitados + " centro(s) de salud inhabilitado(s)");
                }
                for(SbCsEstablecimiento estab : listaEstablecimiento){
                    if(estab.getActivo() == 0){
                        continue;
                    }
                    if(estab.getSbCsHorarioList() == null || (estab.getSbCsHorarioList() != null && estab.getSbCsHorarioList().isEmpty())){
                        centrosHabilitados = 0;
                        JsfUtil.mensajeAdvertencia("Algunos de sus centros autorizados no tiene registrado el horario de atención. Por favor registrarlo en la opción: Establecimientos");
                        break;
                    }                    
                }
            }
            
            return "/aplicacion/gamac/sbCsCertifsalud/List";
        }else{
            JsfUtil.mensajeAdvertencia("Esta opción es permitida sólo para perfiles de Centros de Salud"); 
        }
        return null;
    }
    
    /**
     * FUNCION PARA OBTENER LA DESCRIPCIÓN DEL SOLICITANTE EN EL FORMATO (DNI XXXX APELLIDOS NOMBRES)
     * @author Richar Fernández
     * @version 1.0
     * @param reg Certificado de salud a validar
     * @return Descripción de solicitante de certif.
     */
    public String obtenerDescripcionSolicitante(SbCsCertifsalud reg){
        String nombre;
        
        if(reg.getSolicitanteId().getTipoDoc() != null){
            nombre = reg.getSolicitanteId().getTipoDoc().getNombre();
        }else{
            nombre = "DNI";
        }
        nombre = nombre + " "+ reg.getSolicitanteId().getNumDoc() + " - ";
        if(reg.getSolicitanteId().getApePat() != null){
            nombre = nombre + reg.getSolicitanteId().getApePat();
        }
        if(reg.getSolicitanteId().getApeMat() != null){
            nombre = nombre + reg.getSolicitanteId().getApeMat();
        }
        if(reg.getSolicitanteId().getNombres() != null){
            nombre = nombre + reg.getSolicitanteId().getNombres();
        }
        return nombre;
    }
    
    public void openDlgConfirmarTransmitir(){
       try {
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            int cont = ejbSbCsCertifsaludFacade.contarCertificadosCreadorXEstablecimiento(p_user.getNumDoc(), p_user.getLogin());
            if(cont > 0){
                RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmTransmitir').show()");
            }else{
                JsfUtil.mensajeAdvertencia("No se encontraron registros para transmitir");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al transmitir los registros pendientes");
        } 
    }
    
    /**
     * FUNCION PARA TRANSMITIR LOS CERTIFICADOS DE SALUD A LA SUCAMEC
     * @author Richar Fernández
     * @version 1.0
     */
    public void transmitirCertificados(){
        try {
            int cont = 0, contNoTransm = 0;
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            List<SbCsCertifsalud> lstPendientes = ejbSbCsCertifsaludFacade.obtenerCertificadosCreadorXEstablecimiento(p_user.getNumDoc(), p_user.getLogin());
            if(!lstPendientes.isEmpty()){
                for(SbCsCertifsalud certif : lstPendientes ){
//                    valida = true;                    
//                    conSolicitante = ejbSbCsCertifsaludFacade.contarCertificadosCreadosXSolicitanteXEmpresa(certif.getId(), certif.getSolicitanteId().getId(), p_user.getNumDoc());
//                    if(conSolicitante > 0){
//                        valida = false;
//                    }else{
//                        conSolicitante = ejbSbCsCertifsaludFacade.contarCertificadosXSolicitanteXEstado(certif.getId(), certif.getSolicitanteId().getId(),null);
//                        if(conSolicitante > 0){
//                            valida = false;
//                        }
//                    }
//                    if(!valida){
//                        contNoTransm ++;
//                        continue;
//                    }
                    
                    cont++;
                    certif.setEstadoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_ECS_TRA"));
                    certif.setFechaPresentacion(new Date());
                    ejbSbCsCertifsaludFacade.edit(certif);
                }
                reiniciarCamposBusqueda();
                resultados = null;
                JsfUtil.mensaje("Se transfirieron "+ cont + " registro(s) ");
                if( contNoTransm > 0){
                    JsfUtil.mensajeAdvertencia("No se transfirieron "+ contNoTransm + " registro(s) porque el solicitante ya cuenta con certificado vigente o en trámite en este u otro establecimiento ");
                }
                
                RequestContext.getCurrentInstance().execute("PF('wvDlgConfirmTransmitir').hide()");
                RequestContext.getCurrentInstance().update("listForm");
            }else{
                JsfUtil.mensajeAdvertencia("No se encontraron registros para transmitir");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al transmitir los registros pendientes");
        }
    }
    
    /**
     * FUNCION PARA OBTENER EL LISTADO DE CONDICIÓN DE CERTIFICADO (APTO/NO APTO)
     * @author Richar Fernández
     * @version 1.0
     * @return Booleano para mostrar o no el botón
     */
    public List<TipoBaseGt> getObtenerTipoCondicion(){
        return ejbTipoBaseFacade.lstTipoBase("TP_COB");
    }
    
    /**
     * FUNCION PARA OBTENER EL LISTADO DE ESTABLECIMIENTOS DE CENTRO DE SALUD ACTIVOS
     * @author Richar Fernández
     * @version 1.0
     * @return Listado de Certificados de Centro de salud
     */
    public List<SbCsEstablecimiento> getObtenerEstablecimientosCSActivo(){
        DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
        if(p_user == null){
            return null;
        }
        return ejbSbCsEstablecimientoFacade.listarEstablecimientos(p_user.getNumDoc(), true);
    }
    
    /**
     * FUNCION PARA AUTOCOMPLETABLE DE BÚSQUEDA DE SOLICITANTE
     * @author Richar Fernández
     * @version 1.0
     * @param query
     * @return Listado de Personas
     */
    public List<SbPersonaGt> completeSolicitante(String query) {
        return ejbSbPersonaFacade.selectPersonaListado(query);
    }
    
    /**
     * FUNCION PARA OBTENER DESCRIPCIÓN DE SOLICITANTE EN EL FORMATO (DNI XXXX, APELLIDOS NOMBRES)
     * @author Richar Fernández
     * @version 1.0
     * @param persona Persona a validar
     * @return Descripción de solicitante
     */
    public String obtenerDescSolicitante(SbPersonaGt persona) {
        String nombre = "";

        if (persona != null) {
            if (persona.getTipoDoc() == null) {
                nombre = "DNI ";
            }else{
                nombre = persona.getTipoDoc().getNombre() + " ";
            }
            nombre = nombre + persona.getNumDoc() + ", " + 
                              persona.getApellidosyNombres();
        }

        return nombre;
    }
    
    /**
     * FUNCION AL SELECCIONAR EL SOLICITANTE EN AUTOCOMPLETABLE
     * @author Richar Fernández
     * @version 1.0
     */
    public void seleccionaSolicitante() {
        if(registro.getSolicitanteId() != null){
            int cont = ejbSbCsCertifsaludFacade.contarCertificadosXSolicitanteXEstado(registro.getId(), registro.getSolicitanteId().getId(), null);
            if(cont > 0){
                String msje = "";
                DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
                SbCsCertifsalud certifTemp = ejbSbCsCertifsaludFacade.obtenerEstadoCertificadosXSolicitanteXEstado(registro.getId(), registro.getSolicitanteId().getId());
                registro.setSolicitanteId(null);
                msje = "La persona seleccionada ya cuenta con un certificado "+ (certifTemp.getEstadoId().getCodProg().equals("TP_ECS_CRE")?"en trámite":"ya emitido");
                if(Objects.equals(p_user.getNumDoc(), certifTemp.getEstablecimientoId().getPropietarioId().getRuc() )){
                   msje += " en su centro de salud"; 
                }else{
                   msje += " en otro centro de salud";  
                }
                JsfUtil.mensajeAdvertencia(msje);                
                return;
            }
        }
            
        setRenderSolicitante(false);
    }
    
    /**
     * FUNCION PARA ELIMINAR SOLICITANTE SELECCIONADO
     * @author Richar Fernández
     * @version 1.0
     */
    public void eliminarSolicitanteRegistro() {
        registro.setSolicitanteId(null);
        setRenderSolicitante(true);
    }

    /**
     * FUNCION PARA ABRIR POPUP DE NUEVO SOLICITANTE
     * @author Richar Fernández
     * @version 1.0
     */
    public void openDlgNuevoSolicitante(){
        maxLengthDoc = 8;
        solicitanteRegistro = new SbPersonaGt();
        solicitanteRegistro.setId(null);
        solicitanteRegistro.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
        solicitanteRegistro.setActivo(JsfUtil.TRUE);
        solicitanteRegistro.setFechaReg(new Date());
        solicitanteRegistro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        solicitanteRegistro.setAudNumIp(JsfUtil.getIpAddress());
        RequestContext.getCurrentInstance().execute("PF('wvRegistrarSolicitante').show()");
        RequestContext.getCurrentInstance().update("solicitanteForm");
    }
    
    
    public void llenarMedicos(){
        int cont = 0;
        for(SbCsMedicoEstabsal establec : registro.getEstablecimientoId().getSbCsMedicoEstabsalList()){
                if(establec.getActivo() == 1 && establec.getMedicoId().getActivo() == 1 &&
                   !establec.getCargoId().getCodProg().equals("TP_CCS_DIR") && !establec.getCargoId().getCodProg().equals("TP_CCS_SUB") &&
                   establec.getHabilitado() == 1 && 
                   (JsfUtil.getFechaSinHora(registro.getFechaEmision()).compareTo(establec.getFechaIni()) >= 0 ) &&
                   (JsfUtil.getFechaSinHora(registro.getFechaEmision()).compareTo(establec.getFechaFin()) <= 0 ) 
                  ){
                    if(establec.getCargoId().getCodProg().equals("TP_CCS_TIT")){
                        if(establec.getFechaFinHab() != null){
                            if( (JsfUtil.getFechaSinHora(registro.getFechaEmision()).compareTo(establec.getFechaFinHab()) <= 0) &&
                                (JsfUtil.getFechaSinHora(registro.getFechaEmision()).compareTo(establec.getFechaIniHab()) >= 0)
                              ){
                                lstMedicos.add(establec);
                            }
                        }else{
                            lstMedicos.add(establec);
                        }
                    }else{
                        if(establec.getFechaFinHab() != null && 
                           (JsfUtil.getFechaSinHora(registro.getFechaEmision()).compareTo(establec.getFechaIniHab()) >= 0) &&
                           (JsfUtil.getFechaSinHora(registro.getFechaEmision()).compareTo(establec.getFechaFinHab()) <= 0)
                          ){
                            lstMedicos.add(establec);
                        }else{
                            if(establec.getFechaFinHab() == null){
                                cont++;
                            }
                        }
                    }
                }
            }
            if(lstMedicos.isEmpty()){
                JsfUtil.mensajeAdvertencia("El establecimiento seleccionado no tiene médicos registrados activos o no cuenta con las fechas contractuales o de habilitación vigentes. Por favor verifique en la opción: MÉDICOS.");
            }
            if(cont > 0){
                JsfUtil.mensajeAdvertencia("No se mostró "+cont+" psicólogo(s) y/o psiquiatra(s) porque no tienen fecha de habilitación");
            }
    }
    
    public void validaMedicoCalculoFechaCS(){
        int cont = 0;
        lstMedicos = new ArrayList();
       
        if(registro.getEstablecimientoId() != null){
            llenarMedicos();
            
            for(SbCsCertifMedico medTab : lstMedicosFirman){
                cont = 0;
                for(SbCsMedicoEstabsal medList : lstMedicos){                
                    if(Objects.equals(medList.getMedicoId().getId(), medTab.getMedicoId().getId())){
                        cont++;
                    }
                }
                if(cont == 0){
                    lstMedicosFirman.remove(medTab);
                    lstMedicosSeleccionados.remove(medTab);
                }
            }
            
            RequestContext.getCurrentInstance().update("pnlMedicosFirman");
        }else{
            JsfUtil.mensajeError("Por favor seleccione un establecimiento de Salud");
        }
    }
    
    /**
     * FUNCION AL SELECCIONAR UN ESTABLECIMIENTO DE CENTRO DE SALUD
     * @author Richar Fernández
     * @version 1.0
     */
    public void cambiarEstablecimientoCS(){
        lstMedicos = new ArrayList();
       
        if(registro.getEstablecimientoId() != null){
            lstMedicosFirman = new ArrayList();
            lstMedicosSeleccionados = new ArrayList(); 
            llenarMedicos();
            
            RequestContext.getCurrentInstance().update("pnlMedicosFirman");
        }else{
            JsfUtil.mensajeError("Por favor seleccione un establecimiento de Salud");
        }
    }
    
    /**
     * FUNCION PARA OBTENER DESCRIPCIÓN DE MÉDICO EN FORMATO (DNI XXX APELLIDOS NOMBRES - CARGO)
     * @author Richar Fernández
     * @version 1.0
     * @param estab Establecimiento a validar
     * @return Descripción de establecimiento
     */
    public String obtenerDescripcionMedico(SbCsMedicoEstabsal estab){
        String desc = "";
        
        if(estab.getMedicoId().getPersonaId().getTipoDoc() != null){
            desc = estab.getMedicoId().getPersonaId().getTipoDoc().getNombre();
        }else{
            desc = "DNI";
        }
        desc = desc + " " + estab.getMedicoId().getPersonaId().getNumDoc() + ", " + estab.getMedicoId().getPersonaId().getApellidosyNombres() + " - " + estab.getCargoId().getNombre();
        
        if(estab.getEsDelegado() != null && estab.getEsDelegadoBoolean() ){
            desc += " (DELEGADO TITULAR)";
        }
        
        return desc;
    }
    
    /**
     * FUNCION PARA ADICIONAR UN NUEVO MÉDICO AL LISTADO DE CERTIFICADO DE SALUD
     * @author Richar Fernández
     * @version 1.0
     */
    public void agregarMedico(){
        boolean valida = true;
        if(validacionMedicoCheck()){
            for(SbCsMedicoEstabsal estab : medicoEstabSeleccionados){
                for(SbCsCertifMedico med : lstMedicosFirman){
                    if(Objects.equals(med.getMedicoId().getId(), estab.getMedicoId().getId()) && Objects.equals(med.getCargoId().getId(), estab.getCargoId().getId()) ){
                         valida = false;
                         break;
                    }
                }
                if(valida){
                    SbCsCertifMedico certif = new SbCsCertifMedico();
                    certif.setId(JsfUtil.tempId());
                    certif.setActivo(JsfUtil.TRUE);
                    certif.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                    certif.setAudNumIp(JsfUtil.getIpAddress());
                    certif.setCargoId(estab.getCargoId());
                    certif.setCertifmedicoId(registro);
                    certif.setMedicoId(estab.getMedicoId());
                    lstMedicosFirman.add(certif);
                }else{
                    JsfUtil.mensajeError("El médico seleccionado ya ha sido adicionado");
                }   
            }
            medicoEstabSeleccionados = new ArrayList();
        }
    }
    
    /**
     * FUNCION PARA ELIMINAR UN MÉDICO AGREGADO AL LISTADO
     * @author Richar Fernández
     * @version 1.0
     */
    public void eliminarMedicosSeleccionados(){
        if(!lstMedicosSeleccionados.isEmpty()){
            for(SbCsCertifMedico medEstab : lstMedicosSeleccionados){
                for(SbCsCertifMedico medEstab2: lstMedicosFirman){
                    if(Objects.equals(medEstab.getId(), medEstab2.getId())){
                        lstMedicosFirman.remove(medEstab);
                        break;
                    }
                }
            }
        }else{
            JsfUtil.mensajeError("Por favor seleccione al menos un médico para eliminar el registro");
        }
    }
   
    /**
     * FUNCION PARA OBTENER EL LISTADO DE INSTITUCIONES MÉDICAS
     * @author Richar Fernández
     * @version 1.0
     * @return Listado de instituciones 
     */
    public List<TipoBaseGt> getTipoInstitucion(){
        return ejbTipoBaseFacade.lstTipoBase("TP_INS");
    }
    
    /**
     * FUNCION PARA BUSCAR AL SOLICITANTE
     * @author Richar Fernández
     * @version 1.0
     */
    public void buscarDlgPersonaReniec(){
        boolean validacion = true;
        SbPersonaGt persona = null;

        if(solicitanteRegistro.getTipoDoc() == null){
            validacion = false;
            JsfUtil.invalidar("solicitanteForm:tipoDocumento");
            JsfUtil.mensajeError("Por favor ingresar el tipo de documento");
        }
        if(solicitanteRegistro.getNumDoc() == null){
            solicitanteRegistro.setNumDoc("");
        }else{
            solicitanteRegistro.setNumDoc(solicitanteRegistro.getNumDoc().trim());
        }
        if(solicitanteRegistro.getNumDoc().isEmpty()){
            JsfUtil.invalidar("solicitanteForm:numDocNuevoSolicitante");
            JsfUtil.mensajeError("Por favor ingresar el nro. de DNI");
            return;
        }
        if(solicitanteRegistro.getNumDoc().length() != 8){
            JsfUtil.invalidar("solicitanteForm:numDocNuevoSolicitante");
            JsfUtil.mensajeError("El DNI no tiene la cantidad de dígitos correctos");
            return;
        }
        persona = ejbSbPersonaFacade.buscarPersonaSel("", solicitanteRegistro.getNumDoc());
        if(persona != null){
            JsfUtil.invalidar("solicitanteForm:numDocNuevoSolicitante");
            JsfUtil.mensajeError("El DNI ya se encuentra registrado, por favor verifique");
            return;
        }
        
        if(validacion){
            persona = wsPideController.buscarReniec(solicitanteRegistro);
            if(persona !=null){
                if(persona.getNombres() != null){
                    solicitanteRegistro.setNombres(persona.getNombres());
                    solicitanteRegistro.setApePat(persona.getApePat());
                    solicitanteRegistro.setApeMat(persona.getApeMat());
                    solicitanteRegistro.setGeneroId(persona.getGeneroId());
                    solicitanteRegistro.setFechaNac(persona.getFechaNac());
                    if(solicitanteRegistro.getApeMat() != null && solicitanteRegistro.getApeMat().equals("X")){
                        solicitanteRegistro.setApeMat("");
                    }
                    RequestContext.getCurrentInstance().update(obtenerForm());
                }else{
                    solicitanteRegistro.setNumDoc(null);
                    solicitanteRegistro.setNombres(null);
                    solicitanteRegistro.setApePat(null);
                    solicitanteRegistro.setApeMat(null);
                    solicitanteRegistro.setGeneroId(null);
                    solicitanteRegistro.setFechaNac(null);
                    JsfUtil.invalidar("solicitanteForm:numDocNuevoSolicitante");
                    JsfUtil.mensajeAdvertencia("No se encontró a la persona con el servicio del RENIEC.");
                }
            }else{
                solicitanteRegistro.setNumDoc(null);
                solicitanteRegistro.setNombres(null);
                solicitanteRegistro.setApePat(null);
                solicitanteRegistro.setApeMat(null);
                solicitanteRegistro.setGeneroId(null);
                solicitanteRegistro.setFechaNac(null);
                JsfUtil.mensajeAdvertencia("El servicio del RENIEC no se encuentra disponible, intente en otro momento.");
            }
        }
    }
    
    public void buscarDlgPersonaMigraciones() {
        try {
            SbPersonaGt persona = null;

            if(solicitanteRegistro.getTipoDoc() == null){
                JsfUtil.invalidar("solicitanteForm:tipoDocumento");
                JsfUtil.mensajeError("Por favor ingresar el tipo de documento");
                return;
            }
            if(solicitanteRegistro.getNumDoc() == null){
                solicitanteRegistro.setNumDoc("");
            }else{
                solicitanteRegistro.setNumDoc(solicitanteRegistro.getNumDoc().trim());
            }
            if(solicitanteRegistro.getNumDoc().isEmpty()){
                JsfUtil.invalidar("solicitanteForm:numDocNuevoSolicitante");
                JsfUtil.mensajeError("Por favor ingresar el nro. de carnet de extranjería");
                return;
            }
            if(solicitanteRegistro.getNumDoc().length() != 9){
                JsfUtil.invalidar("solicitanteForm:numDocNuevoSolicitante");
                JsfUtil.mensajeError("El CE no tiene la cantidad de dígitos correctos");
                return;
            }
            persona = ejbSbPersonaFacade.buscarPersonaSel("", solicitanteRegistro.getNumDoc());
            if(persona != null){
                JsfUtil.invalidar("solicitanteForm:numDocNuevoSolicitante");
                JsfUtil.mensajeError("El CE ya se encuentra registrado, por favor verifique");
                return;
            }
            
            ResPideMigra resMigra = wsPideController.buscarCarnetExtranjeria( ejbSbParametroFacade.obtenerParametroXNombre("sspCursos_usuario_Pide").getValor() , solicitanteRegistro.getNumDoc());
            if(resMigra != null){
                if(resMigra.isRspta()){
                    if(resMigra.getResultado().getStrNumRespuesta().equals("0000")){
                        solicitanteRegistro.setNombres(resMigra.getResultado().getStrNombres());
                        solicitanteRegistro.setApePat(resMigra.getResultado().getStrPrimerApellido());
                        solicitanteRegistro.setApeMat(resMigra.getResultado().getStrSegundoApellido());
                        RequestContext.getCurrentInstance().update(obtenerForm());

                        JsfUtil.mensaje("Se encontró datos de la persona en Migraciones");
                    }else{
                        solicitanteRegistro.setNumDoc(null);
                        solicitanteRegistro.setNombres(null);
                        solicitanteRegistro.setApePat(null);
                        solicitanteRegistro.setApeMat(null);
                        solicitanteRegistro.setFechaNac(null);
                        JsfUtil.invalidar("solicitanteForm:numDocNuevoSolicitante");
                        JsfUtil.mensajeAdvertencia("No se encontró datos de la persona en Migraciones");                        
                    }
                }else{
                    solicitanteRegistro.setNumDoc(null);
                    solicitanteRegistro.setNombres(null);
                    solicitanteRegistro.setApePat(null);
                    solicitanteRegistro.setApeMat(null);
                    solicitanteRegistro.setFechaNac(null);
                    JsfUtil.mensajeAdvertencia("Hubo un error consultando el servicio de Migraciones.");
                }
            }else{
                solicitanteRegistro.setNumDoc(null);
                solicitanteRegistro.setNombres(null);
                solicitanteRegistro.setApePat(null);
                solicitanteRegistro.setApeMat(null);
                solicitanteRegistro.setFechaNac(null);
                JsfUtil.mensajeAdvertencia("El servicio de Migraciones no está disponible.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.mensajeAdvertencia("Hubo un error al consultar los datos en Migraciones");
        }
    }
    
    /**
     * FUNCION PARA OBTENER EL NOMBRE DEL FORMULARIO
     * @author Richar Fernández
     * @version 1.0
     */
    public String obtenerForm(){
        String nombreForm = "";
        switch(estado){            
            case CREAR:
                    nombreForm = "createForm";
                    break;
            case EDITAR:
                    nombreForm = "editForm";
                    break;
            case VER:
                    nombreForm = "viewForm";
                    break;
        }
        return nombreForm;
    }
    
    /**
     * FUNCION DE VALIDACIÓN DEL SOLICITANTE
     * @author Richar Fernández
     * @version 1.0
     */
    public boolean validarSolicitante(){
        boolean validacion = true;

        if(solicitanteRegistro.getNumDoc() == null || solicitanteRegistro.getNumDoc().isEmpty()){
            validacion = false;
            JsfUtil.mensajeAdvertencia("Por favor ingresar el nro. de DNI");
        }
        if(solicitanteRegistro.getApePat() != null){
            solicitanteRegistro.setApePat(solicitanteRegistro.getApePat().trim());
        }
        if(solicitanteRegistro.getApeMat() != null){
            solicitanteRegistro.setApeMat(solicitanteRegistro.getApeMat().trim());
        }
        if(solicitanteRegistro.getNombres() != null){
            solicitanteRegistro.setNombres(solicitanteRegistro.getNombres().trim());
        }
        if(solicitanteRegistro.getTipoDoc() == null){
            validacion = false;
            JsfUtil.mensajeAdvertencia("Por favor ingresar el tipo de documento");
        }else{
            if(solicitanteRegistro.getNumDoc() != null && !solicitanteRegistro.getNumDoc().isEmpty()){
                switch(solicitanteRegistro.getTipoDoc().getCodProg()){
                    case "TP_DOCID_DNI":
                            if(solicitanteRegistro.getNumDoc().length() != 8){
                                validacion = false;
                                JsfUtil.mensajeAdvertencia("El DNI no tiene la cantidad de dígitos correctos");
                            }
                            if(solicitanteRegistro.getApePat() == null || solicitanteRegistro.getApePat().isEmpty() || 
                               solicitanteRegistro.getApeMat() == null || solicitanteRegistro.getApeMat().isEmpty() || 
                               solicitanteRegistro.getNombres() == null || solicitanteRegistro.getNombres().isEmpty() ){
                                validacion = false;
                                JsfUtil.mensajeAdvertencia("Por favor busque a una persona");
                            }
                            break;
                    case "TP_DOCID_CE":
                            if(solicitanteRegistro.getNumDoc().length() != 9){
                                validacion = false;
                                JsfUtil.mensajeAdvertencia("El carné de extranjería no tiene la cantidad de dígitos correctos");
                            }
                            if(solicitanteRegistro.getApePat() == null || solicitanteRegistro.getApePat().isEmpty()){
                                validacion = false;
                                JsfUtil.mensajeAdvertencia("Por favor ingresar el apellido paterno");
                            }
                            if(solicitanteRegistro.getNombres()== null || solicitanteRegistro.getNombres().isEmpty() ){
                                validacion = false;
                                JsfUtil.mensajeAdvertencia("Por favor ingresar el nombre");
                            }
                            break;        
                }
            }   
        }
        
        SbPersonaGt persona = ejbSbPersonaFacade.buscarPersonaSel("", solicitanteRegistro.getNumDoc());
        if(persona != null){
            validacion = false;
            JsfUtil.mensajeAdvertencia("Ya se encuentra registrado una persona con el mismo nro. de documento, por favor buscar al solicitante.");
        }
        
        return validacion;
    }
    
    /**
     * FUNCION PARA VALIDAR Y ABRIR POPUP DE NUEVO SOLICITANTE
     * @author Richar Fernández
     * @version 1.0
     */
    public void registrarNuevoSolicitante(){
        try {
            if(validarSolicitante()){
                solicitanteRegistro = (SbPersonaGt) JsfUtil.entidadMayusculas(solicitanteRegistro, "");
                registro.setSolicitanteId(solicitanteRegistro);
                seleccionaSolicitante();
                
                JsfUtil.mensaje("Solicitante registrado correctamente");
                RequestContext.getCurrentInstance().execute("PF('wvRegistrarSolicitante').hide()");
                RequestContext.getCurrentInstance().update(obtenerForm());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al registrar el solicitante");
        } 
    }
    
    public boolean validaNroCertificadoEdicion(){
        boolean validacion = false;
        
        String nroCertificadoTemp = nroCertificado;
        if(nroCertificadoTemp.contains("-") && nroCertificadoTemp.contains("/") ){
            String[] partsNroCertif = nroCertificadoTemp.split("-");
            if(partsNroCertif.length == 2){
                String[] partsAditonal = partsNroCertif[1].split("/");

                if(partsAditonal.length == 2){

                    if(Integer.parseInt(partsAditonal[0]) >= 1 && Integer.parseInt(partsAditonal[0]) <= 12 && 
                       Integer.parseInt(partsAditonal[1]) >= 2015 ){
                        validacion = true;
                    }
                 }
             }
        }
        
        return validacion;
    }
    
    public boolean validacionCreateCertificado(){
        boolean validacion = true;
        
        if(nroCertificado == null || nroCertificado.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar el nro. de certificado");
        }else{
            String nroCertificadoValidate = nroCertificado + "-"+ JsfUtil.formatearFecha(new Date(), "MM") + "/" + JsfUtil.formatearFecha(new Date(), "YYYY");
            if(registro.getEstablecimientoId() != null){
                int cont = ejbSbCsCertifsaludFacade.contarNroCertificado(nroCertificadoValidate, registro.getEstablecimientoId().getId(), registro.getId() );    
                if(cont > 0){
                    validacion = false;
                    JsfUtil.mensajeError("El nro. de certificado ya ha sido emitido en el establecimiento seleccionado");
                } 
            }
            if(estado == EstadoCrud.EDITAR || estado == EstadoCrud.RECTIFICAR){
                try {
                    nroCertificado = nroCertificado.trim();
                    int num = Integer.parseInt(nroCertificado);                    
                } catch (Exception e) {
                    validacion = false;
                    JsfUtil.mensajeError("En el nro. de certificado sólo debe ingresar números");
                }
            }
        }
        if(registro.getEstablecimientoId() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor seleccionar el establecimiento");
        }
        if(registro.getSolicitanteId() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar el solicitante");
        }
        if(registro.getFechaEmision() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar la fecha de emisión");
        }else{
            Calendar c_fec = Calendar.getInstance();
            c_fec.setTime(registro.getFechaEmision());
            c_fec.add(Calendar.MONDAY, 3);
            registro.setFechaVencimiento(c_fec.getTime());
        }
        if(registro.getFechaVencimiento() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar la fecha de vencimiento");
        }
        //-- ACTIVAR VALIDACIÓN CUANDO SE VUELVA A RESTRINGIR VENCIMIENTOS CON LA FECHA ACUTAL --//
        /*else{
            Calendar c_hoy = Calendar.getInstance();
            if(JsfUtil.getFechaSinHora(registro.getFechaVencimiento()).compareTo(JsfUtil.getFechaSinHora(c_hoy.getTime())) < 0 ){
                validacion = false;
                JsfUtil.mensajeError("No puede registrar un certificado con fecha vencida.");
            }
        }*/
        if(registro.getCondicionObtenidaId() == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar la condición obtenida");
        }
        if(lstMedicosFirman.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Por favor ingresar al menos un médico que firma el certificado");
        }else{
            int contPsicologo = 0, contPsiquiatra = 0, contTitular = 0, contEncargadoTitular = 0;
            for(SbCsCertifMedico estab : lstMedicosFirman){
                if(estab.getActivo() == 0){
                    continue;
                }
//                if(estab.getCargoId().getCodProg().equals("TP_CCS_DIR") || estab.getCargoId().getCodProg().equals("TP_CCS_SUB")){
//                    contDirector ++;
//                }
                if(registro.getEstablecimientoId() != null){
                    for(SbCsMedicoEstabsal estabMedico :  estab.getMedicoId().getSbCsMedicoEstabsalList()){
                        if(Objects.equals( estabMedico.getEstablecimientoId().getId(), registro.getEstablecimientoId().getId()) && 
                           Objects.equals(estab.getCargoId().getId(), estabMedico.getCargoId().getId()) &&
                           estabMedico.getEsDelegadoBoolean() && estabMedico.getActivo() == 1 ){
                            contEncargadoTitular++;
                        }
                    }
                }

                if(estab.getCargoId().getCodProg().equals("TP_CCS_TIT")){
                    contTitular ++;
                }
                if(estab.getCargoId().getCodProg().equals("TP_CCS_PCO")){
                    contPsicologo ++;
                }
                if(estab.getCargoId().getCodProg().equals("TP_CCS_PSA")){
                    contPsiquiatra ++;
                }
            }
//            if(contDirector == 0){
//                validacion = false;
//                JsfUtil.mensajeError("Es necesario que se adicione a un director o subdirector en la lista de médicos que firman el certificado");
//            }
            if(contTitular == 0 && contEncargadoTitular == 0){
                validacion = false;
                JsfUtil.mensajeError("Es necesario que se adicione a un titular o delegado en la lista que firman el certificado");
            }
            if(contTitular > 0 && contEncargadoTitular > 0){
                validacion = false;
                JsfUtil.mensajeError("No puede adicionar al titular del establecimiento y delegado a la vez. Seleccione solo a uno");
            }
            if(contPsicologo == 0){
                validacion = false;
                JsfUtil.mensajeError("Es necesario que se adicione a un psicólogo en la lista de médicos que firman el certificado");
            }
            if(contPsiquiatra == 0){
                validacion = false;
                JsfUtil.mensajeError("Es necesario que se adicione a un psiquiatra en la lista de médicos que firman el certificado");
            }
        }
        if(registro.getFechaEmision() != null && registro.getFechaVencimiento() != null){
            if(registro.getFechaEmision() != null && registro.getFechaVencimiento() != null){
                if(JsfUtil.getFechaSinHora(registro.getFechaEmision()).compareTo(JsfUtil.getFechaSinHora(registro.getFechaVencimiento())) >0 ){
                    validacion = false;
                    JsfUtil.mensajeError("La fecha de emisión no puede ser mayor que la fecha de vencimiento, por favor verifique");
                }
            }else{
                Calendar c_hoy = Calendar.getInstance();
                c_hoy.add(Calendar.MONTH, 3);

                if(JsfUtil.getFechaSinHora(registro.getFechaVencimiento()).compareTo(JsfUtil.getFechaSinHora(c_hoy.getTime())) > 0 ){
                    validacion = false;
                    JsfUtil.mensajeError("La fecha de vencimiento no puede exceder los 3 meses a partir de la fecha de emisión");
                }
            }   
        }
        if(archivo == null){
            validacion = false;
            JsfUtil.mensajeError("Por favor adjunte el certificado");
        }
        
        return validacion;
    }
    
    public Date getMinFechaEmision(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -6);
// rfv - temporalmente
//        switch(estado){
//            case EDITAR:
//                            calendar.setTime(registro.getFechaEmision());
//                            break;
//            case RECTIFICAR:
//                            calendar.setTime(registro.getCertificadoId().getFechaEmision());
//                            break;
//        }
        return calendar.getTime();
    }
    
    
    public Date getMaxFechaEmision(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
    
    public void handleFileUploadAdjunto(FileUploadEvent event){
        try {
            if (event != null) {
                file = event.getFile();
                if (JsfUtil.verificarJPG(file) || JsfUtil.verificarPDF(file)) {
                    fotoByte = IOUtils.toByteArray(file.getInputstream());
                    foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
                    archivo = file.getFileName();
                } else {
                    file = null;
                    JsfUtil.mensajeAdvertencia("El archivo que trató de cargar no tiene formato PDF ni JPG/JPEG");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cargarFoto(){
        try {
            String fotoName = registro.getRutaDocumento();
            fotoByte = FileUtils.readFileToByteArray(new File(JsfUtil.bundleBDIntegrado("Documentos_pathUpload_certificadoSalud") + fotoName ));
            archivo = fotoName;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void openDlgFoto(){
        try {
            String fotoName = registro.getRutaDocumento();
            InputStream is = new ByteArrayInputStream(fotoByte);
            foto =  new DefaultStreamedContent(is, "image/jpeg", fotoName );
            RequestContext.getCurrentInstance().execute("PF('wvDialogFoto').show()");
            RequestContext.getCurrentInstance().update("frmFoto");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Hubo un error al cargar la foto");
        }
    }
    
    public List<TipoBaseGt> getTipoDocumentosSolicitante(){
        return ejbTipoBaseFacade.lstTiposXCodigosProg("'TP_DOCID_DNI','TP_DOCID_CE'");
    }
    
    public String obtenerDescripcionEstablecimiento(SbCsEstablecimiento estab){
        String nombre = estab.getNombre() + " -";
        
        if(estab.getDireccionId().getViaId() != null){
            if(!estab.getDireccionId().getViaId().getCodProg().equals("TP_VIA_OTRO")){
                nombre = nombre + " "+ estab.getDireccionId().getViaId().getNombre();
            }
        }
        nombre = nombre + " " + estab.getDireccionId().getDireccion();
        if(estab.getDireccionId().getNumero() != null){
            nombre = nombre + " " + estab.getDireccionId().getNumero();
        }
        
        return nombre;
    }
    
    public String getTipoDocumento(SbPersonaGt per){
        String tipoDoc = "";
        
        if(per.getTipoDoc() != null){
            tipoDoc = per.getTipoDoc().getNombre();
        }else{
            tipoDoc = "DNI";
        }        
        return tipoDoc;
    }
    
    public void calcularFechaFin(){
        if(registro.getFechaEmision() != null){
            if(registro.getEstablecimientoId() == null){
                registro.setFechaEmision(null);
                JsfUtil.invalidar(obtenerForm()+":estabCentroSalud");
                JsfUtil.mensajeError("Por favor seleccione el establecimiento primero.");
                return;
            }
            registro.setFechaEmision(JsfUtil.getFechaSinHora(registro.getFechaEmision()));
            Calendar c_hoy = Calendar.getInstance();
            Calendar c_fec = Calendar.getInstance();            
            c_fec.setTime(registro.getFechaEmision());            
            c_fec.add(Calendar.MONDAY, 3);
            c_hoy.setTime(registro.getFechaEmision());
            registro.setFechaVencimiento(c_fec.getTime());
            
            int diaSemana = c_hoy.get(Calendar.DAY_OF_WEEK);
            boolean diaEncontrado = false;
            //Verificando horario
            for(SbCsHorario horario : listaHorarioCs ){
                if(horario.getActivo() == 0){
                    continue;
                }
                for(TipoBaseGt dia : horario.getTipoBaseList()){
                    if(dia.getActivo() == 0){
                        continue;
                    }
                    if(dia.getOrden() == diaSemana){
                        diaEncontrado = true;
                        break;
                    }
                }
            }
            if(!diaEncontrado){
                registro.setFechaEmision(null);
                registro.setFechaVencimiento(null);
                JsfUtil.invalidar(obtenerForm()+":fechaInicio");
                JsfUtil.mensajeError("Este establecimiento no tiene atención en el día seleccionado. Por favor verifique el horario de atención en la opción: ESTABLECIMIENTOS.");
                return;
            }
            complementoCertificado = JsfUtil.formatearFecha(registro.getFechaEmision(), "MM") + "/" + JsfUtil.formatearFecha(registro.getFechaEmision(), "YYYY");
            validaMedicoCalculoFechaCS();
        }else{
            registro.setFechaVencimiento(null);
        }
    }
    
    public List<TipoBaseGt> getEstadosCertificadoSaludM(){
        return ejbTipoBaseFacade.lstTiposXCodigosProg("'TP_ECS_CRE','TP_ECS_TRA'");
    }
    
    public StreamedContent cargarDocumento(){
        String nombre = registro.getRutaDocumento().substring(0, registro.getRutaDocumento().lastIndexOf(".") );
        String extension = registro.getRutaDocumento().substring(registro.getRutaDocumento().lastIndexOf("."), registro.getRutaDocumento().length());
        
        try {            
            String nombreArchivo = nombre + extension.toLowerCase();
                     
            if(file != null){
                if(file.getFileName().toUpperCase().contains("PDF")){
                    return new DefaultStreamedContent(file.getInputstream(), "application/pdf", file.getFileName() );
                }else{
                    return new DefaultStreamedContent(file.getInputstream(), "image/jpeg", file.getFileName() );
                }
            }else{
                if(registro.getRutaDocumento().toUpperCase().contains("PDF")){
                    return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_certificadoSalud").getValor(), nombreArchivo, nombreArchivo, "application/pdf");
                }else{
                    return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_certificadoSalud").getValor(), nombreArchivo, nombreArchivo, "image/jpeg");    
                }
            }
        } catch (Exception e) {
             
            try {                
                String nombreArchivo = nombre + extension.toUpperCase();
                if(file != null){
                    if(registro.getRutaDocumento().toUpperCase().contains("PDF")){
                        return new DefaultStreamedContent(file.getInputstream(), "application/pdf", nombreArchivo );
                    }else{
                        return new DefaultStreamedContent(file.getInputstream(), "image/jpeg", nombreArchivo );
                    }
                }else{
                    if(registro.getRutaDocumento().toUpperCase().contains("PDF")){
                        return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_certificadoSalud").getValor(), nombreArchivo, nombreArchivo, "application/pdf");
                    }else{
                        return JsfUtil.obtenerArchivoBDIntegrado(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_certificadoSalud").getValor(), nombreArchivo, nombreArchivo, "image/jpeg");    
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
             
         }     
        return null;
     }
    
    public void cambiaTipoDocumento(){
        maxLengthDoc = 0;
        
        if(solicitanteRegistro.getTipoDoc() != null){
            solicitanteRegistro.setNumDoc("");
            solicitanteRegistro.setApePat("");
            solicitanteRegistro.setApeMat("");
            solicitanteRegistro.setNombres("");
            switch(solicitanteRegistro.getTipoDoc().getCodProg() ){
                case "TP_DOCID_DNI":
                        maxLengthDoc = 8;
                        break;
                case "TP_DOCID_CE":
                        maxLengthDoc = 9;
                        break;
            }
        }
    }
    
    public void openDlgAyuda(int tipo){
        switch(tipo){
            case 1:
                    imagenAyuda = "img_ayuda_list_certifSalud.jpg";
                    break;
            case 2:
                    imagenAyuda = "img_ayuda_crear_certifSalud.jpg";
                    break;
            case 3: //Especie Valorada
                    imagenAyuda = "img_ayuda_especieValorada.jpg";
                    break;
            case 4: //Certificado
                    imagenAyuda = "img_ayuda_certificado.jpg";
                    break;
            case 5: //Historia Clínica
                    imagenAyuda = "img_ayuda_historiaClinica.jpg";
                    break;
        }        
        RequestContext.getCurrentInstance().execute("PF('dlgAyuda').show()");
        RequestContext.getCurrentInstance().update("frmAyuda");
    }
    
    
    public void seleccionaMedico(){
        validacionMedicoCheck();
    }
    
    public void seleccionaAllMedico(){
        validacionMedicoCheck();
    }
    
    public boolean validacionMedicoCheck(){
        boolean validacion = true;
        int cont = 0;
        
        if(medicoEstabSeleccionados.isEmpty() && lstMedicosFirman.isEmpty()){
            validacion = false;
            JsfUtil.mensajeError("Debe seleccionar los 3 médicos autorizados para el certificado");
            return validacion;
        }
        if(medicoEstabSeleccionados.size() > 3){
            validacion = false;
            JsfUtil.mensajeError("Sólo puede seleccionar 3 médicos como máximo");
            return validacion;
        }
        for(int i=0; i<medicoEstabSeleccionados.size(); i++){
            for(int j=i+1; j<medicoEstabSeleccionados.size(); j++){
                if(Objects.equals(medicoEstabSeleccionados.get(i).getCargoId().getId(), medicoEstabSeleccionados.get(j).getCargoId().getId())  &&
                   Objects.equals(medicoEstabSeleccionados.get(i).getEsDelegadoBoolean(), medicoEstabSeleccionados.get(j).getEsDelegadoBoolean())
                  ){
                    cont++;
                }
            }
            if(cont > 0){
                validacion = false;
                JsfUtil.mensajeError("Ha seleccionado más de un médico con el mismo cargo");
                return validacion;
            }
        }        
        
        for(SbCsMedicoEstabsal estab : medicoEstabSeleccionados){
            cont = 0;
            if(!estab.getEsDelegadoBoolean()){
                for(SbCsCertifMedico med : lstMedicosFirman){
                    if(Objects.equals(estab.getCargoId().getId(), med.getCargoId().getId())){
                         cont++; 
                    }
                }
            }
            if(cont > 0){
                validacion = false;
                JsfUtil.mensajeError("Ha seleccionado más de un médico con el mismo cargo");
                return validacion;
            }
        }
        
        for(SbCsMedicoEstabsal estab : medicoEstabSeleccionados){
            for(SbCsCertifMedico med : lstMedicosFirman){
                if(Objects.equals(med.getMedicoId().getId(), estab.getMedicoId().getId()) && Objects.equals(med.getCargoId().getId(), estab.getCargoId().getId()) ){
                     validacion = false;
                     JsfUtil.mensajeError("El médico "+ med.getMedicoId().getPersonaId().getApellidosyNombres() +" ya ha sido adicionado");
                     break;
                }
            }
        }
        
        return validacion;
    }
    
    /**
     * FUNCIÓN OBTENER ESTILO DE COLOR DE LISTADO DE CERTIF. DE SALUD
     * @author Richar Fernández
     * @version 1.0
     * @param reg Registro a validar
     * @return Cadena con estilo de celda
     */
    public String estiloPorTipo(SbCsCertifsalud reg){
        String estilo = "";
        if(reg.getCertificadoId() != null){
            estilo = "datatable-row-emitidos";
        }else{
            estilo = "";
        }
        
        return estilo;
    }
    
    ////////////////////// REPORTE //////////////////////
    
    public String prepareReporte(){
        listadoRep = new ArrayList();
        anio = null;
        estado = EstadoCrud.REPORTE;
        return "/aplicacion/gamac/sbCsCertifsalud/Reporte";
    }
    
    public List<Integer> obtenerListadoAnios(){
        List<Integer> lstAnios = new ArrayList();        
        for(int i=2016; i<=Integer.parseInt(JsfUtil.dateToString(new Date(),"yyyy")); i++){
            lstAnios.add(i);
        }
        return lstAnios;
    }
    
    public void buscarReporte(){
        if (anio != null) {
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            
            listadoRep = null;
            listadoRep = ejbSbCsCertifsaludFacade.buscarCertificadosReporte(p_user.getNumDoc(), anio );
            anio = null;
        } else {
            JsfUtil.mensajeError("Por favor, ingresar el año.");
        }
    }
    
    public String obtenerDescDelegadoListadoMedicos(SbCsCertifMedico certMedic){
        String desc = "";
        
        if(certMedic != null){
            for(SbCsMedicoEstabsal medEstab : certMedic.getMedicoId().getSbCsMedicoEstabsalList() ){
                if( registro.getEstablecimientoId() != null &&
                    Objects.equals(medEstab.getEstablecimientoId().getId(), registro.getEstablecimientoId().getId()) &&
                    Objects.equals(certMedic.getCargoId().getId(), medEstab.getCargoId().getId()) &&
                    medEstab.getActivo() == 1
                  ){
                    desc = certMedic.getCargoId().getNombre();
                    if(medEstab.getEsDelegadoBoolean()){
                        desc += " " + JsfUtil.bundleBDIntegrado("sbCsCertifsalud_crear_dtMedico_descDelegadoT");
                    }
                }
            }
        }
        
        return desc;
    }
    
    public String obtenerComplementoNuevoCertificado(){
        return "-"+JsfUtil.formatearFecha(new Date(), "MM") + "/" + JsfUtil.formatearFecha(new Date(), "YYYY");
    }
    
    public void cambiarEstablecimiento(){
        if(registro.getEstablecimientoId() == null){
            JsfUtil.mensajeError("Por favor seleccione un establecimiento");
            return;
        }
        for(SbCsEstablecimiento estab : listaEstablecimiento){
            if(estab.getActivo() == 0){
                continue;
            }
            if(Objects.equals(registro.getEstablecimientoId().getId(), estab.getId())){
                listaHorarioCs = estab.getSbCsHorarioList();
                break;
            }
        }
    }
    
    public String obtenerNroCertificado(){
        String nroCertif = registro.getNumero();
        
        if(mostrarAdicionalNroCertif){
            String nroCertificadoTemp = registro.getNumero();
            String[] partsNroCertif = nroCertificadoTemp.split("-");
            nroCertif = partsNroCertif[0];
        }
        
        return nroCertif;
    }
    
    public String obtenerComplementoNroCertificado(){
        String complemento = "";
        
        if(mostrarAdicionalNroCertif){
            String nroCertificadoTemp = registro.getNumero();
            String[] partsNroCertif = nroCertificadoTemp.split("-");
            complemento = partsNroCertif[1];
        }
        
        return complemento;
    }
    
}
