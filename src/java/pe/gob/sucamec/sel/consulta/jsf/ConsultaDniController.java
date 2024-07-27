/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.consulta.jsf;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import pe.gob.sucamec.bdintegrado.bean.SbPersonaGtFacade;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;

/**
 *
 * @author jrosales
 */
@Named("consultaDniController")
@SessionScoped
public class ConsultaDniController implements Serializable{
@EJB
private SbPersonaGtFacade ejbSbPersonaFacade;

    List<SbPersonaGt>resultado;
    String numDoc="";
    public String prepareList() {
        return "/aplicacion/gamac/consultaDNI/List"; 
    }
    public void buscarDocumento(){
        try {
           if(!numDoc.trim().isEmpty()){
               resultado=ejbSbPersonaFacade.buscarPersonaSel(numDoc);
               if(resultado==null){
                    JsfUtil.mensaje("No exite registros con el numero de documento: " +numDoc);
               }
           }
           else{
                  JsfUtil.mensaje("Ingrese un numero de documento a buscar");
           }
        } catch (Exception e) {
        }
    }

    public List<SbPersonaGt> getResultado() {
        return resultado;
    }

    public void setResultado(List<SbPersonaGt> resultado) {
        this.resultado = resultado;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }
    
}
