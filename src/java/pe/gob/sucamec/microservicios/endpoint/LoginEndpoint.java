/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.microservicios.endpoint;

import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.primefaces.json.JSONObject;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;

/**
 *
 * @author lbartolo
 */
@Stateless
public class LoginEndpoint {
    
    private static String servidorIP = JsfUtil.bundleMicroservicios("ServidorIP_API");
    private static String servidorNOM = JsfUtil.bundleMicroservicios("ServidorIP_NOM");
    
    
    public JSONObject generarTOKEN_SEL_Login_JsonObject(JSONObject dataJSONObjectBody) {
        
        System.out.println("servidorIP: "+servidorIP + " ("+ servidorNOM+")");
              
        JSONObject rptaObject = new JSONObject();
        
        String apiUrl = (servidorIP+""+JsfUtil.bundleMicroservicios("EndPoint_post_GenerarToken_SEL_Logueado"));
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
            rptaObject.put("LoginSEL", "null");
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
}
