package pe.gob.sucamec.sel.gssp.jsf;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.sel.citas.jsf.util.JsfUtil;
import pe.gob.sucamec.sel.gssp.data.SspRegistroPosArma;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.jsf.SbUsuarioController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jrosales
 */

@Named("armasPosesionController")
@SessionScoped
public class ArmasPosesionController implements Serializable{
     /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;
    /**
     * Filtro basico para las búsquedas
     */
    String filtro,filtroTexto;
    
    ListDataModel<SspRegistroPosArma>resultados=null;
    private AmaModelos selectedArma;
    private String selectedArmaString;
    private UploadedFile file;
    private StreamedContent foto;
    private byte[] fotoByte;
    private byte[] fileByte;
    private List<Map> lstFotos;
    private boolean disabledBtnFoto;
    private boolean ocultarDatos;
    private boolean activarCheck;
    private SspRegistroPosArma registro;
    List<Map> resultadosMap = null;

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoGamacFacade ejbTipoGamacFacade;
    
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoSeguridadFacade ejbTipoSeguridadFacade;
    
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaModelosFacade ejbAmaModelosFacade;
    
     @EJB
    private pe.gob.sucamec.bdintegrado.bean.AmaCatalogoFacade ejbAmaCatalogoFacade;
    
    @EJB
    private pe.gob.sucamec.sel.gssp.beans.ArmasPosesionFacade ejbArmasPosesionFacade;
    
    
    public EstadoCrud getEstado() {
        return estado;
    }

    public void setEstado(EstadoCrud estado) {
        this.estado = estado;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String getFiltroTexto() {
        return filtroTexto;
    }

    public void setFiltroTexto(String filtroTexto) {
        this.filtroTexto = filtroTexto;
    }

    public List<Map> getResultadosMap() {
        return resultadosMap;
    }

    public void setResultadosMap(List<Map> resultadosMap) {
        this.resultadosMap = resultadosMap;
    }
    
    
    public String prepareListMios() {
        estado = EstadoCrud.BANDEJA;
        resultadosMap = new ArrayList<>();
        filtro=null;
        filtroTexto=null;
        return "/aplicacion/gssp/sspPosesionArma/List";
    }
    
    public void mostrarCrear() {
        registro=new SspRegistroPosArma();
        estado = EstadoCrud.CREAR;
        ocultarDatos=false;
        limpiarDatos();
    }
    public void mostrarEditar(Long ID,String tipoArma,String marca,String modelo,String calibre) {
        limpiarDatos();
        registro = ejbArmasPosesionFacade.find(ID);
        estado = EstadoCrud.EDITAR;
        if (registro.getFlagSinCatalogo() == 1) {
            ocultarDatos = true;
            activarCheck = true;

        } else {
            ocultarDatos = false;
            activarCheck = false;
            
            selectedArmaString=1L+"@/@"+modelo+"@/@"+1L+"@/@"+1L+"@/@"+calibre+"@/@"+tipoArma+"@/@"+marca;
            selectedArma=ejbAmaModelosFacade.obtenerModelo(registro.getModeloCatalogadoId().getId());
        }
        if (lstFotos == null) {
            lstFotos = new ArrayList();
        }
        Map mapa1 = new HashMap();
        mapa1.put("nro", 1);
        mapa1.put("nombre",registro.getNombreFoto1());
        lstFotos.add(mapa1);
        Map mapa2 = new HashMap();
        mapa2.put("nro", 2);
        mapa2.put("nombre",registro.getNombreFoto2());
        lstFotos.add(mapa2);
        if (lstFotos.size() == 2) {
            setDisabledBtnFoto(true);
        } else {
            setDisabledBtnFoto(false);
        }
    }
    
    public void mostrarVer(Long ID,String tipoArma,String marca,String modelo,String calibre) {
        limpiarDatos();
        
        registro = ejbArmasPosesionFacade.find(ID);
        estado = EstadoCrud.VER;
        if (registro.getFlagSinCatalogo() == 1) {
            ocultarDatos = true;
            activarCheck = true;

        } else {
            ocultarDatos = false;
            activarCheck = false;
            selectedArmaString=tipoArma+"/"+marca+"/"+modelo+"/"+calibre;
        }
        if (lstFotos == null) {
            lstFotos = new ArrayList();
        }
        Map mapa1 = new HashMap();
        mapa1.put("nro", 1);
        mapa1.put("nombre",registro.getNombreFoto1());
        lstFotos.add(mapa1);
        Map mapa2 = new HashMap();
        mapa2.put("nro", 2);
        mapa2.put("nombre",registro.getNombreFoto2());
        lstFotos.add(mapa2);
        if (lstFotos.size() == 2) {
            setDisabledBtnFoto(true);
        } else {
            setDisabledBtnFoto(false);
        }
    }
    public AmaModelos getSelectedArma() {
        return selectedArma;
    }

    public void setSelectedArma(AmaModelos selectedArma) {
        this.selectedArma = selectedArma;
    }

    public String getSelectedArmaString() {
        return selectedArmaString;
    }

    public void setSelectedArmaString(String selectedArmaString) {
        this.selectedArmaString = selectedArmaString;
    }
    
    
   public void buscarBandeja() {
        if (filtro != null) {
            if (!filtroTexto.trim().isEmpty()) { 
                
                resultadosMap=ejbArmasPosesionFacade.listarArmasXCriterios(filtroTexto,filtro,JsfUtil.getLoggedUser().getPersonaId());
                
            } else {
                JsfUtil.mensajeAdvertencia("Ingrese el criterio de busqueda");
            }
        } else {
            JsfUtil.mensajeAdvertencia("Seleccione Criterio de Busqueda");
        }

    }
    public ListDataModel<SspRegistroPosArma> getResultados() {
        return resultados;
    }

    public void setResultados(ListDataModel<SspRegistroPosArma> resultados) {
        this.resultados = resultados;
    }
    public String registrar(){
        if(validacionCreate()){
            // se guardara en DB
            try {
                registro.setEmpresaId(JsfUtil.getLoggedUser().getPersona());
                registro.setFechaRegistro(new Date());
                registro.setFlagSinCatalogo(activarCheck?pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil.TRUE:pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil.FALSE);
                registro.setNombreFoto1(lstFotos.get(0).get("nombre").toString());
                registro.setNombreFoto2(lstFotos.get(1).get("nombre").toString());
                registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registro.setAudNumIp(JsfUtil.getIpAddress());
                registro.setActivo(pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil.TRUE);
                registro.setUsuarioRegistroId(new SbUsuario(JsfUtil.getLoggedUser().getId()));
                registro.setModeloCatalogadoId(selectedArma);
               registro=(SspRegistroPosArma)JsfUtil.entidadMayusculas(registro,"");
                ejbArmasPosesionFacade.create(registro);
                JsfUtil.mensaje("Datos registrados correctamente");
                return prepareListMios();
            } 
            
            catch (Exception e) {
            JsfUtil.mensajeError("Error al Registrar los datos");
            }
        }
        return null;
    }
     public String modificar(){
        if(validacionCreate()){
            // se guardara en DB
            try {
                registro.setEmpresaId(JsfUtil.getLoggedUser().getPersona());
                registro.setFechaRegistro(new Date());
                registro.setFlagSinCatalogo(activarCheck?pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil.TRUE:pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil.FALSE);
                registro.setNombreFoto1(lstFotos.get(0).get("nombre").toString());
                registro.setNombreFoto2(lstFotos.get(1).get("nombre").toString());
                registro.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                registro.setAudNumIp(JsfUtil.getIpAddress());
                registro.setActivo(pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil.TRUE);
                registro.setUsuarioRegistroId(new SbUsuario(JsfUtil.getLoggedUser().getId()));
                registro.setModeloCatalogadoId(selectedArma);
                registro=(SspRegistroPosArma)JsfUtil.entidadMayusculas(registro,"");
                ejbArmasPosesionFacade.edit(registro);
                JsfUtil.mensaje("Datos modificados correctamente");
                return prepareListMios();
            } 
            
            catch (Exception e) {
            JsfUtil.mensajeError("Error al Registrar los datos");
            }
        }
        return null;
    }
    public void limpiarDatos(){
        lstFotos=new ArrayList<>();
        activarCheck=false;
        selectedArma=null;
        selectedArmaString=null;
        disabledBtnFoto=false;
    }
    
    public boolean validacionCreate(){
        boolean valida = true;
        if (activarCheck) {
            if (registro.getNuevoTipoArmaId() == null) {
                JsfUtil.mensajeError("Por favor seleccione el tipo de Arma");
                valida = false;
            }
            if (registro.getNuevoMarca() == null) {
                JsfUtil.mensajeError("Por favor ingrese la marca del Arma");
                valida = false;
            }
            if (registro.getNuevoModelo() == null) {
                JsfUtil.mensajeError("Por favor ingrese el modelo del Arma");
                valida = false;
            }
            if (registro.getNuevoCalibre() == null) {
                JsfUtil.mensajeError("Por favor ingrese el calibre del Arma");
                valida = false;
            }
        }else{
            if(selectedArma==null){
                JsfUtil.mensajeError("Por favor busque un Arma del catálogo");
                valida=false;
            }
        }
        if (registro.getNumeroSerie() != null) {
          if(registro.getNumeroSerie().isEmpty()){
           JsfUtil.mensajeError("Por favor ingrese el número de serie del Arma");
            valida = false;   
          }
        }else{
            JsfUtil.mensajeError("Por favor ingrese el número de serie del Arma");
            valida = false;
        }
        if (registro.getEstadoFisicoId() == null) {
            JsfUtil.mensajeError("Por favor seleccione el estado físico del Arma");
            valida = false;
        }
        if (registro.getSituacionId() == null) {
            JsfUtil.mensajeError("Por favor seleccione la situación del Arma");
            valida = false;
        }
        if (lstFotos != null) {
            if (lstFotos.size() != 2) {
                JsfUtil.mensajeError("Por favor adjunte 2 fotografias del Arma");
                valida = false;
            }
        } else {
            JsfUtil.mensajeError("Por favor adjunte 2 fotografias del Arma");
            valida = false;
        }
        return valida;
    }
    
    public List<String> completeArma(String query) {       
        List<String> filteredArma = new ArrayList<>();
        String[] lstVariosBusqueda = null;
        List<Map> lstArma;
        int cont = 0;
        query = query.toUpperCase();
        
         if(query.contains("S/M")){
            query = query.replaceAll("S/M", "");            
            lstVariosBusqueda = query.split("/");
            if(lstVariosBusqueda.length == 1 && lstVariosBusqueda[0].isEmpty()){
                lstVariosBusqueda[0] = "S/M";
            }else{
                lstVariosBusqueda = append(lstVariosBusqueda,"S/M");
            }
        }else{
            lstVariosBusqueda = query.split("/");
        }
        
        lstVariosBusqueda = limpiarArrayArma(lstVariosBusqueda);
        lstArma = ejbAmaModelosFacade.obtenerListadoArmasMap( null );
//        if(lstVariosBusqueda.length > 1 || query.contains("/") || query.contains("S/M") ){
//            lstArma = ejbAmaModelosFacade.obtenerListadoArmasMap( null );
//        }else{
//            lstArma = ejbAmaModelosFacade.obtenerListadoArmasMap( query );
//        }

        for (Map p : lstArma) {
            cont = 0;
            for(String cadena : lstVariosBusqueda){
                if(!cadena.isEmpty()){
                    if (p.get("TIPO_ARMA").toString().contains(cadena.toUpperCase().trim()) ||
                        p.get("MARCA").toString().contains(cadena.toUpperCase().trim()) ||
                        p.get("MODELO").toString().contains(cadena.toUpperCase().trim()) ||
                        p.get("CALIBRE").toString().contains(cadena.toUpperCase().trim()) ) {

                        cont++;
                    }
                }
            }
            
            if(cont == lstVariosBusqueda.length){
                filteredArma.add(p.get("ID")+ "@/@" + 
                                 p.get("MODELO")+ "@/@" + 
                                 p.get("MARCA_ID")+ "@/@" + 
                                 p.get("TIPO_ARMA_ID")+ "@/@" + 
                                 p.get("CALIBRE")+ "@/@" + 
                                 p.get("TIPO_ARMA")+ "@/@" + 
                                 p.get("MARCA")
                );
            }
        }
        return filteredArma;
    }
    
    /**
     * QUITAR FILA VACIAS DE ARRAY
     * @author Richar Fernández
     * @version 1.0
     * @param array de cadena
     * @return Array de cadena
     */
    public String[] limpiarArrayArma(String[] array){
        String[] nuevoArray = new String[0];
        for(String cadena : array){
            if(!cadena.isEmpty()){
                nuevoArray = append(nuevoArray,cadena);
            }
        }
        
        return nuevoArray;
    }
    
    /**
     * FUNCIÓN PARA OBTENER DESCRIPCIÓN DE ARMA PARA CAMPO AUTOCOMPLETABLE
     * @author Richar Fernández
     * @version 1.0
     * @param mod Modelo de arma
     * @return Descripción de arma (Nombre de Arma / Marca / Modelo / Calibre(s))
     */
    public String obtenerDescripArmaListadoString(String mod){
        String cadena = "";        
        if(mod != null && !mod.isEmpty()){            
            String[] lstVariosBusqueda = mod.toUpperCase().split("@/@");
            
            cadena = lstVariosBusqueda[5] + " / " + lstVariosBusqueda[6] + " / " + lstVariosBusqueda[1] + " / " + lstVariosBusqueda[4];
        }
        return cadena;
    }
    
    /**
     * FUNCIÓN AL SELECCIONAR ARMA DE AUTOCOMPLETABLE DE BÚSQUEDA DE ARMA
     * @author Richar Fernández
     * @version 1.0
     */
    public void seleccionaArma(){
        if(selectedArmaString != null){
            if(!selectedArmaString.isEmpty()){
                String[] lstVariosBusqueda = selectedArmaString.toUpperCase().split("@/@");
                selectedArma = ejbAmaModelosFacade.obtenerModelo(Long.parseLong(""+lstVariosBusqueda[0]));
            }
        }else{
            selectedArma = null;
        }
    }
    
    public void handleFileUploadFoto(FileUploadEvent event) {
        try {
            if (event != null) {
                file = event.getFile();
                if (pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil.verificarJPG(file)) {
                    fotoByte = IOUtils.toByteArray(file.getInputstream());
                    fotoByte = pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil.escalarRotarImagen(fotoByte, 1024, 768, 0);
                    foto = new DefaultStreamedContent(file.getInputstream(), "image/jpeg");
                    
                    if(lstFotos == null){
                        lstFotos = new ArrayList();
                    }
                    Map mapa = new HashMap();
                    mapa.put("nro", lstFotos.size()+1);
                    mapa.put("nombre", file.getFileName());
                    mapa.put("byte", fotoByte);
                    lstFotos.add(mapa);
                    
                    if(lstFotos.size() == 2){
                        setDisabledBtnFoto(true);
                    }else{
                        setDisabledBtnFoto(false);
                    }
                } else {
                    file = null;
                    pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil.mensajeAdvertencia("El archivo que trató de ingresar no es un archivo JPG/JPEG original, comuníquese con el administrador del sistema");
                }
            }
        } catch (Exception e) {
        //    e.printStackTrace();
        }
    }

    public List<Map> getLstFotos() {
        return lstFotos;
    }

    public void setLstFotos(List<Map> lstFotos) {
        this.lstFotos = lstFotos;
    }

    public boolean isDisabledBtnFoto() {
        return disabledBtnFoto;
    }

    public void setDisabledBtnFoto(boolean disabledBtnFoto) {
        this.disabledBtnFoto = disabledBtnFoto;
    }

    public StreamedContent getFoto() {
        return foto;
    }

    public void setFoto(StreamedContent foto) {
        this.foto = foto;
    }
    
    public void quitarFoto(int indice) {
         lstFotos.remove(indice - 1);
    }  

    public boolean isOcultarDatos() {
        return ocultarDatos;
    }

    public void setOcultarDatos(boolean ocultarDatos) {
        this.ocultarDatos = ocultarDatos;
    }

    public boolean isActivarCheck() {
        return activarCheck;
    }

    public void setActivarCheck(boolean activarCheck) {
        this.activarCheck = activarCheck;
    }

    public SspRegistroPosArma getRegistro() {
        return registro;
    }

    public void setRegistro(SspRegistroPosArma registro) {
        this.registro = registro;
    }
    
        public void cambiarCheck() {
        // JsfUtil.mensaje(String.valueOf(activarCheck));
        if (activarCheck) {
            ocultarDatos = true;
            selectedArma = null;
            selectedArmaString = null;
        } else {
            ocultarDatos = false;
        }
        
    }
    
   public List<TipoSeguridad> obtenerListadoSituacionArma(){
       
        return ejbTipoSeguridadFacade.lstSituacionArmas();
    }
   public List<TipoSeguridad> obtenerEstadoArma(){
        
        return ejbTipoSeguridadFacade.lstEstadoArmas();
    }
    public List<AmaCatalogo> obtenerTipoArmas(){
        
        return ejbAmaCatalogoFacade.buscarTipoArma();
    }
    public boolean esPerfilInpe(){
        
        return pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil.buscarPerfilUsuario("AMA_INPE");
    }
    
    /**
     * ADICIONAR NUEVA FILA A ARRAY
     * @author Richar Fernández
     * @version 1.0
     */
    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }
    public boolean verificarFlagConCat(Long valor){
        if(valor.equals(0L))
            return true;
        else
            return false;
    }
    public boolean verificarFlagSinCat(Long valor){
        if(valor.equals(1L))
            return true;
        else
            return false;
    }
}
