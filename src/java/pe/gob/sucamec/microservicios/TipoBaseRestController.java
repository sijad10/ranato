/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.microservicios;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONObject;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;

/**
 *
 * @author lbartolo
 */
@Named(value = "tipoBaseRestController")
@SessionScoped
public class TipoBaseRestController implements Serializable{
        
    private EstadoCrud estado;    
    private SbPersonaGt regUserAdminist;  
    
    //filtro
    private ArrayList<JSONObject> filtroTipoBaseListaObject;    
    private String filtroTipoBaseSelectedString;    
    private JSONObject filtroTipoBaseSelectedObject;    
    
    //Tabla
    private ArrayList<JSONObject> tablaTipoBaseLista;  
    private String tablaTipoBaseListaSelectedString;    
    private JSONObject tablaTipoBaseSelectedObject;
    
    
    //Formulario
    private Long regTB_ID;
    private ArrayList<JSONObject> regTB_TipoIdListaObject;
    private String regTB_TipoIdSelectedString;
    private String regTB_Nombre;
    private String regTB_Abreviatura;
    private String regTB_Descripcion;
    private String regTB_CodProg;
    private Integer regTB_Orden;
     
    @EJB
    private pe.gob.sucamec.microservicios.endpoint.TipoBaseEndpoint ejbTipoBaseEndpoint;
        
    
    public String cargarDatos() {          
           
        filtroTipoBaseListaObject = new ArrayList<JSONObject>();
        filtroTipoBaseListaObject = ejbTipoBaseEndpoint.listarTipoBaseByTipoId_JsonObject(0L);
        filtroTipoBaseSelectedString = "";          
        
        return "/aplicacion/microservicios/tipoBase/Principal";
    }
    
    public String redireccionarCRUDLaravel(){
        Object objToken = (Object) JsfUtil.getSessionAttribute("seguridad_usuario_token");
        System.out.println("objToken--->"+objToken);
        return "http://192.168.14.77:8022/api/"+objToken;
    }
    
    
    public void buscarBandeja() {        
        boolean validacion = true;        
        
        if(filtroTipoBaseSelectedString == null){
            validacion = false;
            JsfUtil.mensajeAdvertencia("Seleccione un Tipo");
        }
        
        if(validacion){
            Long xTipoIdPadre = null;
            if(filtroTipoBaseSelectedString.equals("0L")){
                xTipoIdPadre = 0L;
            }else{
                filtroTipoBaseSelectedObject = new JSONObject(filtroTipoBaseSelectedString);
                xTipoIdPadre = filtroTipoBaseSelectedObject.getLong("id");
            }
            tablaTipoBaseLista = ejbTipoBaseEndpoint.listarTipoBaseByTipoId_JsonObject(xTipoIdPadre);
        }
    }
    
    
    public void FormularioTipoBase(String stringID){
        
        JSONObject JSONObjectRpta = new JSONObject();
        estado = (stringID == null || stringID.equals("") ) ? EstadoCrud.CREARTIPOBASE : EstadoCrud.EDITARTIPOBASE;
           
        regTB_TipoIdListaObject = ejbTipoBaseEndpoint.listarTipoBaseByTipoId_JsonObject(0L);
        regTB_TipoIdSelectedString = "";
        
        if(estado.equals(EstadoCrud.EDITARTIPOBASE) && !stringID.equals("")){
            
            JSONObjectRpta = ejbTipoBaseEndpoint.verTipoBaseById_JsonObject(Long.parseLong(stringID));
            
            regTB_ID = JSONObjectRpta.getLong("id");
            regTB_TipoIdSelectedString = JSONObjectRpta.get("tipoId").toString();
            regTB_Nombre = JSONObjectRpta.get("nombre").toString();
            regTB_Descripcion= JSONObjectRpta.get("descripcion").toString();
            regTB_Abreviatura = JSONObjectRpta.get("abreviatura").toString();
            regTB_CodProg = JSONObjectRpta.get("codProg").toString();
            regTB_Orden = JSONObjectRpta.getInt("orden");
        }        
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('wvDlgFormulario').show()");
        context.update("frmCuerpoFormulario");
        
    }
    
    public void cancelarFormTipoBase(){
        
        regTB_ID = null;
        regTB_TipoIdSelectedString = "";
        regTB_Nombre = "";
        regTB_Descripcion= "";
        regTB_Abreviatura = "";
        regTB_CodProg = "";
        regTB_Orden = 0;
        
        RequestContext.getCurrentInstance().update("frmCuerpoFormulario");
        RequestContext.getCurrentInstance().execute("PF('wvDlgFormulario').hide()");
    }
    
    public void btnRegistrarTipoBase(){
        JSONObject JSONObjectRpta = new JSONObject();
        boolean validarRegistro = true;
        
        if (regTB_TipoIdSelectedString == null) {
            validarRegistro = false;
            JsfUtil.invalidar("frmCuerpoFormulario:cbo_Tipo");
            JsfUtil.mensajeAdvertencia("Seleccione un tipo");
            RequestContext.getCurrentInstance().update("frmCuerpoFormulario");
            
        }else if (regTB_Nombre == null) {
            validarRegistro = false;
            JsfUtil.invalidar("frmCuerpoFormulario:txt_Nombre");
            JsfUtil.mensajeAdvertencia("Ingrese nombre");
            RequestContext.getCurrentInstance().update("frmCuerpoFormulario");
            
        }else if (regTB_Descripcion == null) {
            validarRegistro = false;
            JsfUtil.invalidar("frmCuerpoFormulario:txt_Descripcion");
            JsfUtil.mensajeAdvertencia("Ingrese descripción");
            RequestContext.getCurrentInstance().update("frmCuerpoFormulario");
            
        }else if (regTB_Abreviatura == null) {
            validarRegistro = false;
            JsfUtil.invalidar("frmCuerpoFormulario:txt_Abreviatura");
            JsfUtil.mensajeAdvertencia("Ingrese abreviatura");
            RequestContext.getCurrentInstance().update("frmCuerpoFormulario");
            
        }else if (regTB_CodProg == null) {
            validarRegistro = false;
            JsfUtil.invalidar("frmCuerpoFormulario:txt_CodProg");
            JsfUtil.mensajeAdvertencia("Ingrese código de programación");
            RequestContext.getCurrentInstance().update("frmCuerpoFormulario");
            
        }else if (regTB_Orden == null) {
            validarRegistro = false;
            JsfUtil.invalidar("frmCuerpoFormulario:txt_Orden");
            JsfUtil.mensajeAdvertencia("Ingrese número de orden");
            RequestContext.getCurrentInstance().update("frmCuerpoFormulario");
            
        }
        
        if(validarRegistro){    
            
            
            if(estado.equals(EstadoCrud.CREARTIPOBASE)){ //REGISTRAR
                
                JSONObject jsonDataTipoId = new JSONObject(regTB_TipoIdSelectedString);
                Long xTipoId = jsonDataTipoId.getLong("id");
                
                JSONObject jsonObjectTipoID = new JSONObject();                
                jsonObjectTipoID.put("id", xTipoId);
                
                JSONObject mMapTipoBase = new JSONObject();
                mMapTipoBase.put("id", 0L);
                mMapTipoBase.put("nombre", regTB_Nombre);
                mMapTipoBase.put("abreviatura", regTB_Abreviatura);
                mMapTipoBase.put("descripcion", regTB_Descripcion);
                mMapTipoBase.put("codProg", regTB_CodProg);
                mMapTipoBase.put("orden", regTB_Orden);
                mMapTipoBase.put("audLogin", JsfUtil.getLoggedUser().getLogin());
                mMapTipoBase.put("audNumIp", JsfUtil.getIpAddress());                
                mMapTipoBase.put("tipoId", jsonObjectTipoID);
                JSONObjectRpta = ejbTipoBaseEndpoint.registrarTipoBase(mMapTipoBase);                
                
                if(JSONObjectRpta.get("estado").equals("OK")){     
                    
                    filtroTipoBaseSelectedString = regTB_TipoIdSelectedString;                    
                    RequestContext.getCurrentInstance().update("listForm");
                    buscarBandeja();

                    cancelarFormTipoBase();                    
                    JsfUtil.mensaje(JSONObjectRpta.get("mensaje").toString());
                    
                }else if(JSONObjectRpta.get("estado").equals("ERROR")){
                    JsfUtil.mensajeError(JSONObjectRpta.get("mensaje").toString());
                }else{
                    JsfUtil.mensajeError(JSONObjectRpta.get("mensaje").toString());
                }
                
            }else if(estado.equals(EstadoCrud.EDITARTIPOBASE) && regTB_ID != null){//ACTUALIZAR
                
                JSONObject jsonDataTipoId = new JSONObject(regTB_TipoIdSelectedString);
                Long xTipoId = jsonDataTipoId.getLong("id");
                
                JSONObject jsonObjectTipoID = new JSONObject();
                jsonObjectTipoID.put("id", xTipoId);
                
                JSONObject mMapTipoBase = new JSONObject();
                mMapTipoBase.put("id", regTB_ID);
                mMapTipoBase.put("nombre", regTB_Nombre);
                mMapTipoBase.put("abreviatura", regTB_Abreviatura);
                mMapTipoBase.put("descripcion", regTB_Descripcion);
                mMapTipoBase.put("codProg", regTB_CodProg);
                mMapTipoBase.put("orden", regTB_Orden);
                mMapTipoBase.put("audLogin", JsfUtil.getLoggedUser().getLogin());
                mMapTipoBase.put("audNumIp", JsfUtil.getIpAddress());                
                mMapTipoBase.put("tipoId", jsonObjectTipoID);
                JSONObjectRpta = ejbTipoBaseEndpoint.actualizarTipoBase(mMapTipoBase);                
                
                if(JSONObjectRpta.get("estado").equals("OK")){     
                    
                    filtroTipoBaseSelectedString = regTB_TipoIdSelectedString;                    
                    RequestContext.getCurrentInstance().update("listForm");
                    buscarBandeja();

                    cancelarFormTipoBase();                    
                    JsfUtil.mensaje(JSONObjectRpta.get("mensaje").toString());
                    
                }else if(JSONObjectRpta.get("estado").equals("ERROR")){
                    JsfUtil.mensajeError(JSONObjectRpta.get("mensaje").toString());
                }else{
                    JsfUtil.mensajeError(JSONObjectRpta.get("mensaje").toString());
                }
            }
            
            
            
        }
        
    }
    
    
    public void borrarRegistro(String stringID){
        
        JSONObject JSONObjectRpta = new JSONObject();
        JSONObjectRpta = ejbTipoBaseEndpoint.anularTipoBase(Long.parseLong(stringID));         
                
        if(JSONObjectRpta.get("estado").equals("OK")){
            buscarBandeja();
            JsfUtil.mensaje(JSONObjectRpta.get("mensaje").toString());

        }else if(JSONObjectRpta.get("estado").equals("ERROR")){
            JsfUtil.mensajeError(JSONObjectRpta.get("mensaje").toString());
        }else{
            JsfUtil.mensajeError(JSONObjectRpta.get("mensaje").toString());
        }
        
    }
    
 

    

    public EstadoCrud getEstado() {
        return estado;
    }

    public void setEstado(EstadoCrud estado) {
        this.estado = estado;
    }


    public SbPersonaGt getRegUserAdminist() {
        return regUserAdminist;
    }

    public void setRegUserAdminist(SbPersonaGt regUserAdminist) {
        this.regUserAdminist = regUserAdminist;
    }    

    public ArrayList<JSONObject> getFiltroTipoBaseListaObject() {
        return filtroTipoBaseListaObject;
    }

    public void setFiltroTipoBaseListaObject(ArrayList<JSONObject> filtroTipoBaseListaObject) {
        this.filtroTipoBaseListaObject = filtroTipoBaseListaObject;
    }   

    public String getFiltroTipoBaseSelectedString() {
        return filtroTipoBaseSelectedString;
    }

    public void setFiltroTipoBaseSelectedString(String filtroTipoBaseSelectedString) {
        this.filtroTipoBaseSelectedString = filtroTipoBaseSelectedString;
    }

    public JSONObject getFiltroTipoBaseSelectedObject() {
        return filtroTipoBaseSelectedObject;
    }

    public void setFiltroTipoBaseSelectedObject(JSONObject filtroTipoBaseSelectedObject) {
        this.filtroTipoBaseSelectedObject = filtroTipoBaseSelectedObject;
    } 

    public ArrayList<JSONObject> getTablaTipoBaseLista() {
        return tablaTipoBaseLista;
    }

    public void setTablaTipoBaseLista(ArrayList<JSONObject> tablaTipoBaseLista) {
        this.tablaTipoBaseLista = tablaTipoBaseLista;
    }   

    public String getTablaTipoBaseListaSelectedString() {
        return tablaTipoBaseListaSelectedString;
    }

    public void setTablaTipoBaseListaSelectedString(String tablaTipoBaseListaSelectedString) {
        this.tablaTipoBaseListaSelectedString = tablaTipoBaseListaSelectedString;
    }

    public JSONObject getTablaTipoBaseSelectedObject() {
        return tablaTipoBaseSelectedObject;
    }

    public void setTablaTipoBaseSelectedObject(JSONObject tablaTipoBaseSelectedObject) {
        this.tablaTipoBaseSelectedObject = tablaTipoBaseSelectedObject;
    }

    public Long getRegTB_ID() {
        return regTB_ID;
    }

    public void setRegTB_ID(Long regTB_ID) {
        this.regTB_ID = regTB_ID;
    }   

    public ArrayList<JSONObject> getRegTB_TipoIdListaObject() {
        return regTB_TipoIdListaObject;
    }

    public void setRegTB_TipoIdListaObject(ArrayList<JSONObject> regTB_TipoIdListaObject) {
        this.regTB_TipoIdListaObject = regTB_TipoIdListaObject;
    }

    public String getRegTB_TipoIdSelectedString() {
        return regTB_TipoIdSelectedString;
    }

    public void setRegTB_TipoIdSelectedString(String regTB_TipoIdSelectedString) {
        this.regTB_TipoIdSelectedString = regTB_TipoIdSelectedString;
    }

    public String getRegTB_Nombre() {
        return regTB_Nombre;
    }

    public void setRegTB_Nombre(String regTB_Nombre) {
        this.regTB_Nombre = regTB_Nombre;
    }

    public String getRegTB_Abreviatura() {
        return regTB_Abreviatura;
    }

    public void setRegTB_Abreviatura(String regTB_Abreviatura) {
        this.regTB_Abreviatura = regTB_Abreviatura;
    }

    public String getRegTB_Descripcion() {
        return regTB_Descripcion;
    }

    public void setRegTB_Descripcion(String regTB_Descripcion) {
        this.regTB_Descripcion = regTB_Descripcion;
    }

    public Integer getRegTB_Orden() {
        return regTB_Orden;
    }

    public void setRegTB_Orden(Integer regTB_Orden) {
        this.regTB_Orden = regTB_Orden;
    }

    public String getRegTB_CodProg() {
        return regTB_CodProg;
    }

    public void setRegTB_CodProg(String regTB_CodProg) {
        this.regTB_CodProg = regTB_CodProg;
    }
    
    
    
}
