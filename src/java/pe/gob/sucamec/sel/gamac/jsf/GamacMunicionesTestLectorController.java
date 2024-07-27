/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.jsf;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.xml.ws.WebServiceException;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.sel.gamac.data.GamacTipoGamac;
import pe.gob.sucamec.sel.jsf.util.JsfUtil;
//import wsb3reniec.ws.*;
//import pe.gob.sucamec.sel.gamac.ws.wsb3Reniec;

/**
 *
 * @author rarevalo
 */
@Named("gamacMunicionesTestLectorController")
@SessionScoped
public class GamacMunicionesTestLectorController implements Serializable {
    
    /**
     * Variables de Registro de Venta
     */
    private List<SbDireccionGt> lstLocales;
    private SbDireccionGt direccion; 
    private int tipoComprador;
    private List<GamacTipoGamac> lstTipoDoc;
    private GamacTipoGamac tipoComprobante;
    private Date fechaInicio = new Date();
    private String saveOk; 
    
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacTipoGamacFacade gamacTipoGamacFacade;

    //@Inject
    //wsb3Reniec ws3ReniecController;    
    
    public List<SbDireccionGt> getLstLocales() {
        return lstLocales;
    }

    public void setLstLocales(List<SbDireccionGt> lstLocales) {
        this.lstLocales = lstLocales;
    }

    public SbDireccionGt getDireccion() {
        return direccion;
    }

    public void setDireccion(SbDireccionGt direccion) {
        this.direccion = direccion;
    }

    public int getTipoComprador() {
        return tipoComprador;
    }

    public void setTipoComprador(int tipoComprador) {
        this.tipoComprador = tipoComprador;
    }

    public List<GamacTipoGamac> getLstTipoDoc() {
        return gamacTipoGamacFacade.lstTipoComprobante();
    }

    public void setLstTipoDoc(List<GamacTipoGamac> lstTipoDoc) {
        this.lstTipoDoc = lstTipoDoc;
    }

    public GamacTipoGamac getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(GamacTipoGamac tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }
    
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }    
    
    /**
     * Funcion tipo 'action' para iniciar registro
     *
     * @return URL de registro
     */
    public String prepareTest() {
        //mostrarBuscar();
        return "/aplicacion/gamac/municiones/testLectorBiometrico/testLector";
    }
    
    public void verificaHuella(){
        JsfUtil.addSuccessMessage("La HUELLA CORRESPONDE AL DNI");

        /*try {
            WsReniec_ServiceLocator w1 = new WsReniec_ServiceLocator();
            WsReniecSOAPStub ws2 = new WsReniecSOAPStub(new URL(w1.getwsReniecSOAPAddress()) , w1);

            //Syso("Reniec: " + ws2.newOperation("40378979"));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
        
        /*URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("C:\\_RA\\download\\_Reniec\\WSQ\\Desarrollo\\BiometricVerificationImageService_IP.wsdl"); //
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        
        try {
            WsReniec_Service ws = new WsReniec_Service(url);
            WsReniec wsR = ws.getWsReniecSOAP();

            wsR.newOperation("suc");
        } catch (Exception ec) {
            ec.printStackTrace();
        }
        */
        //boolean rsptaReniec = ws3ReniecController.consulta(saveOk);
    }
}
