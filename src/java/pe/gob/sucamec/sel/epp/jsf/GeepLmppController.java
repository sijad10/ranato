/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.epp.jsf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;

import pe.gob.sucamec.sistemabase.seguridad.LoginController;

/**
 *
 * @author eceliz
 */
// Nombre de la instancia en la aplicacion //
@Named("geepLmppController")
@SessionScoped
public class GeepLmppController implements Serializable{
    private DataModel items = null, itemsRel = null, itemsSim = null, itemsRelBusqueda = null;
    private Date fechaIni,fechaFin;
    private String categoria;
    private char tipoBusqueda;
    private String textoBuscar;
    private Integer activeTabIndex = 0;
    private TipoExplosivoGt tipoCategoria;
    private List<TipoExplosivoGt> listaCategoria = new ArrayList();
    private List<Map> lstRegLicManip = new ArrayList<>();
    String nomManipulador;
    String docManipulador;
    String numExpediente;
    String archFirmado="[R]";
    Long nroLicencia;
    private String pathFoto;
    
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoExplosivoFacade ejbTipoExplosivoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.EppCarneFacade ejbEppCarneFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    
    
    
    public String prepareListMios() {
        LoginController lc = (LoginController) JsfUtil.obtenerBean("loginController", LoginController.class);
        recreateModel();
        
        return "/aplicacion/gepp/eppLMPP/List";
    }
    private void recreateModel() {
        items = null;
        listaCategoria=ejbTipoExplosivoFacade.lstCategoriaLMPP();
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public char getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(char tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public String getTextoBuscar() {
        return textoBuscar;
    }

    public void setTextoBuscar(String textoBuscar) {
        this.textoBuscar = textoBuscar;
    }   

    public TipoExplosivoGt getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(TipoExplosivoGt tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    public List<TipoExplosivoGt> getListaCategoria() {
        return listaCategoria;
    }

    public void setListaCategoria(List<TipoExplosivoGt> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    public List<Map> getLstRegLicManip() {
        return lstRegLicManip;
    }

    public void setLstRegLicManip(List<Map> lstRegLicManip) {
        this.lstRegLicManip = lstRegLicManip;
    }

    public Integer getActiveTabIndex() {
        return activeTabIndex;
    }

    public void setActiveTabIndex(Integer activeTabIndex) {
        this.activeTabIndex = activeTabIndex;
    }
    
    public void tabChanged(TabChangeEvent event){
           TabView tv = (TabView) event.getComponent(); 
           this.activeTabIndex = tv.getActiveIndex();
           limpiarBusqueda();          
       }
    
    public void buscarLME() {
        if (!validacionBuscarBandeja()) {
            return;
        }
        try {
            
            if(activeTabIndex==1){
                switch (tipoBusqueda)
                 {
                 case 'E':
                    numExpediente=textoBuscar.trim();
                    break;
                 case 'P':
                    nomManipulador=textoBuscar.trim();
                    break;
                 case 'D':
                    nomManipulador=textoBuscar.trim();
                    break;
                 case 'L':
                    nroLicencia=Long.valueOf(textoBuscar.trim());
                    break;
                 default:
                    break;                 
                }
            }
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            if(p_user.getPersona().getRuc()!=null)
                lstRegLicManip = ejbEppCarneFacade.listarCarneMPxFirmados(numExpediente,(tipoCategoria == null) ? null : tipoCategoria.getCodProg(), fechaIni, fechaFin, p_user.getPersona().getRuc().trim(), nomManipulador, nroLicencia);
            else
                JsfUtil.mensajeError("RUC no valido para consultar carnés digital ME, verifique su RUC.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void limpiarBusqueda(){
        textoBuscar=null;
        tipoBusqueda='N';
        fechaIni=null;
        fechaFin=null;
        tipoCategoria=null;
        lstRegLicManip= new ArrayList<>();
        
    }
    public boolean validacionBuscarBandeja() {
        //validación búsqueda múltiple
        if(activeTabIndex==0){
            if ((fechaIni == null && fechaFin == null)  && ((fechaIni != null || fechaFin == null) && (fechaIni == null || fechaFin != null) )) {
                JsfUtil.mensajeError("Por favor escriba un criterio de búsqueda");
                return false;
            } else {
                if(fechaIni != null && fechaFin != null){
                    fechaIni=JsfUtil.getFechaSinHora(fechaIni);
                    fechaFin=JsfUtil.getFechaSinHora(fechaFin);
                    if (JsfUtil.calculaDiasEntreFechas(fechaIni, fechaFin) > 365) {
                       JsfUtil.mensajeError("El rango de fechas no debe exceder el año de busqueda");
                       return false;
                    }
                }    
            }
        }
        //validación búsqueda Simple
        if(activeTabIndex==1){
            if (tipoBusqueda != 'N') {
                if ((textoBuscar == null || textoBuscar.isEmpty())) {
                    JsfUtil.mensajeError("Por favor escriba un criterio de búsqueda");
                    //JsfUtil.invalidar(":listForm:filtroBusqueda");
                    return false;
                }
            }else{
                JsfUtil.mensajeError("Por favor, seleccione un Filtro y escriba un criterio de búsqueda");
                return false;
            }
        }        
        return true;
    }
    
    public StreamedContent reportePDF(String ID) {
        try {            
           if (JsfUtil.buscarPdfRepositorio(ID+archFirmado, JsfUtil.bundleGeppLMPP("Documentos_pathUpload_CMPP") )) {
                return JsfUtil.obtenerArchivoCME(JsfUtil.bundleGeppLMPP("Documentos_pathUpload_CMPP"), ID +archFirmado+ ".pdf", ID +archFirmado+ ".pdf", "application/pdf");
            }else{
                JsfUtil.mensajeError("No se encontró el Carné Digital MPP");
            }
        } catch (Exception ex) {
            return JsfUtil.errorDescarga("Error: geepLmppController.reporte :", ex);
        }
        return null;
    }
    /**
     * GENERAR REPORTE .ZIP
     *
     * @author Elmer Celiz
     * @version 1.0
     * @return Página para redireccionar
     */
    public String descargarLicenciasCMEDigital() {       
        if(lstRegLicManip.size()>0){    
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream out = new ZipOutputStream(baos);
                for(Map mapa : lstRegLicManip){
                    if (mapa.get("ID") != null) {
                        StreamedContent pdfFile;
                        try { 
                            pdfFile=JsfUtil.obtenerArchivoCME(JsfUtil.bundleGeppLMPP("Documentos_pathUpload_CMPP"), mapa.get("id") +archFirmado+ ".pdf", mapa.get("id") +archFirmado+ ".pdf", "application/pdf");
                            if (pdfFile != null) {
                                byte[] bytesPdf = IOUtils.toByteArray(pdfFile.getStream());
                                // name the file inside the zip  file 
                                out.putNextEntry(new ZipEntry(mapa.get("id").toString()+".pdf"));
                                out.write(bytesPdf, 0, bytesPdf.length);
                            } else {
                                JsfUtil.mensajeError("El carnet N°" + mapa.get("CARNE") + " no existe, verificar");
                                return "/aplicacion/gepp/eppLMPP/List";
                            }
                        }catch(Exception ex){
                            JsfUtil.mensajeError("No se pudo obtener el carné firmado, intente nuevamente.");
                            return "/aplicacion/gepp/eppLMPP/List";
                        }                       
                    }
                }
                out.close();
                
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "LICENCIAS EMITIDAS", "Las licencias listadas fueron descargadas exitosamente "));
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("/aplicacion/gepp/eppLMPP/List");
                download(new ByteArrayInputStream(baos.toByteArray()));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
           JsfUtil.mensajeError("No existen registros para descargar, verifique"); 
        return "/aplicacion/gepp/eppLMPP/List";
    }
    /**
     * FUNCION PARA QUE DESCARGUE AUTOMATICAMENTE EXCEL GENERADO
     *
     * @param in Array de bytes
     */
    public void download(InputStream in) {
        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

            response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
            response.setContentType("application/zip"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "attachment; filename=\"LicenciasManipulacionPP.zip\""); // The Save As popup magic is done here. You can give it any filename you want, this only won't work in MSIE, it will use current request URL as filename instead.

            input = new BufferedInputStream(in);
            output = new BufferedOutputStream(response.getOutputStream());

            byte[] buffer = new byte[10240];
            for (int length; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
            facesContext.responseComplete(); // Important! Else JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
        } catch (Exception e) {

        } finally {
            try {
                output.close();
                input.close();
            } catch (IOException ex) {
                JsfUtil.mensajeError("Hubo un error al descargar los archivos");
            }

        }
    }
}
