/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.microservicios.endpoint;

import java.util.ArrayList;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author lbartolo
 */
@Stateless
public class TipoBaseEndpoint{
    
    private static String servidorIP = JsfUtil.bundleMicroservicios("ServidorIP_API");    
   
    
    public ArrayList<JSONObject> listarTipoBaseByTipoId_JsonObject(Long tipoId) {
              
        ArrayList<JSONObject> listaJson = new ArrayList<>();
      
        String apiUrl = (servidorIP+""+JsfUtil.bundleMicroservicios("EndPoint_get_TipoBase_ListarByTipoId")).replace("{tipoId}",tipoId.toString());        
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(apiUrl);
        httpGet.setHeader("Accept", "application/json");        

        try {            
            
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String jsonString = EntityUtils.toString(httpEntity);    
            
            if(httpResponse.getStatusLine().getStatusCode() == 200){//200 - Solicitud Satisfactoria                
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    listaJson.add(jsonObject);
                }
            }else{
                listaJson = new ArrayList<>();
            }

            
        } catch (Exception e) {
            e.printStackTrace();
            
        }  finally {// Cierra los recursos
            
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }   
        
        return listaJson;
      
    
    }
    

    public JSONObject registrarTipoBase(JSONObject dataJSONObjectBody) {
                
        JSONObject rptaObject = new JSONObject();
        
        String apiUrl = (servidorIP+""+JsfUtil.bundleMicroservicios("EndPoint_post_TipoBase_Register"));
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(apiUrl);
        httpPost.setHeader("Content-Type", "application/json");
                   
        try {            
            StringEntity requestEntity = new StringEntity(dataJSONObjectBody.toString());
            httpPost.setEntity(requestEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String responseJson = EntityUtils.toString(httpEntity);        
            
            if(httpResponse.getStatusLine().getStatusCode() == 201){//201 - Created
                rptaObject = new JSONObject(responseJson);                
            }else if(httpResponse.getStatusLine().getStatusCode() == 500){//500 - Internal Server Error
                rptaObject = new JSONObject(responseJson);                
            }
            
        } catch (Exception e) {
            
            rptaObject = new JSONObject();
            rptaObject.put("estado", "ERROR");
            rptaObject.put("mensaje", "ERROR EN EL MICROSERVICIO");
            rptaObject.put("mensaje_error", e.getMessage());
            rptaObject.put("TipoBase", "null");
            System.out.println("error:"+e.getMessage());
            
        } finally {// Cierra los recursos
            
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        
        return rptaObject;
    
    }
    
    
    
    public JSONObject verTipoBaseById_JsonObject(Long Id) {
              
        JSONObject dataJSONObject = new JSONObject();
        
        String apiUrl = (servidorIP+""+JsfUtil.bundleMicroservicios("EndPoint_get_TipoBase_VerById")).replace("{id}",Id.toString());        
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(apiUrl);
        httpGet.setHeader("Accept", MediaType.APPLICATION_JSON);
        
        try {
            
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String jsonString = EntityUtils.toString(httpEntity);                        
            dataJSONObject = new JSONObject(jsonString);
            
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {// Cierra los recursos
            
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }     
        
        return dataJSONObject;
    
    }
    
    
    public JSONObject actualizarTipoBase(JSONObject dataJSONObjectBody) {
                
        JSONObject rptaObject = new JSONObject();
        
        String apiUrl = (servidorIP+""+JsfUtil.bundleMicroservicios("EndPoint_put_TipoBase_Update")).replace("{id}",dataJSONObjectBody.get("id").toString());  
        HttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(apiUrl);
        httpPut.setHeader("Content-Type", "application/json");
        
                   
        try {            
            httpPut.setEntity(new StringEntity(dataJSONObjectBody.toString(), ContentType.APPLICATION_JSON));
            HttpResponse httpResponse = httpClient.execute(httpPut);
            HttpEntity httpEntity = httpResponse.getEntity();            
            String responseJson = EntityUtils.toString(httpEntity);        
            
            if(httpResponse.getStatusLine().getStatusCode() == 201){//201 - Created
                rptaObject = new JSONObject(responseJson);                
            }else if(httpResponse.getStatusLine().getStatusCode() == 500){//500 - Internal Server Error
                rptaObject = new JSONObject(responseJson);                
            }
            
        } catch (Exception e) {
            
            rptaObject = new JSONObject();
            rptaObject.put("estado", "ERROR");
            rptaObject.put("mensaje", "ERROR EN EL MICROSERVICIO");
            rptaObject.put("mensaje_error", e.getMessage());
            rptaObject.put("TipoBase", "null");
            System.out.println("error:"+e.getMessage());
            
        } finally {// Cierra los recursos
            
            if (httpPut != null) {
                httpPut.releaseConnection();
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        
        return rptaObject;
    
    }
    
    
    
    public JSONObject anularTipoBase(Long xId) {
                
        JSONObject rptaObject = new JSONObject();
        
        String apiUrl = (servidorIP+""+JsfUtil.bundleMicroservicios("EndPoint_put_TipoBase_Annul")).replace("{id}",xId.toString());  
        HttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(apiUrl);
        httpPut.setHeader("Content-Type", "application/json");
                           
        try {                        
            HttpResponse httpResponse = httpClient.execute(httpPut);
            HttpEntity httpEntity = httpResponse.getEntity();            
            String responseJson = EntityUtils.toString(httpEntity);        
            
            if(httpResponse.getStatusLine().getStatusCode() == 201){//201 - Created
                rptaObject = new JSONObject(responseJson);                
            }else if(httpResponse.getStatusLine().getStatusCode() == 500){//500 - Internal Server Error
                rptaObject = new JSONObject(responseJson);                
            }
            
        } catch (Exception e) {
            
            rptaObject = new JSONObject();
            rptaObject.put("estado", "ERROR");
            rptaObject.put("mensaje", "ERROR EN EL MICROSERVICIO");
            rptaObject.put("mensaje_error", e.getMessage());
            rptaObject.put("TipoBase", "null");
            System.out.println("error:"+e.getMessage());
            
        } finally {// Cierra los recursos
            
            if (httpPut != null) {
                httpPut.releaseConnection();
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        
        return rptaObject;
    
    }
    
    
}

