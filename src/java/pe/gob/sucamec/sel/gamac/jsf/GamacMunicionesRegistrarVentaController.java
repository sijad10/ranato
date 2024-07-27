/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.jsf;

import com.sun.xml.ws.client.BindingProviderProperties;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.ws.BindingProvider;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import pe.gob.sucamec.sel.gamac.data.GamacAmaAdmunDetalleTrans;
import pe.gob.sucamec.sel.gamac.data.GamacAmaAdmunTransaccion;
import pe.gob.sucamec.sel.gamac.data.GamacAmaArma;
import pe.gob.sucamec.sel.gamac.data.GamacAmaCatalogo;
import pe.gob.sucamec.sel.gamac.data.GamacSbDireccion;
import pe.gob.sucamec.sel.gamac.data.GamacTipoBase;
import pe.gob.sucamec.sel.gamac.data.GamacAmaLicenciaDeUso;
import pe.gob.sucamec.sel.gamac.data.GamacAmaModelos;
import pe.gob.sucamec.sel.gamac.data.GamacAmaMunicion;
import pe.gob.sucamec.sel.gamac.data.GamacAmaTarjetaPropiedad;
import pe.gob.sucamec.sel.gamac.data.GamacDetalleVenta1;
import pe.gob.sucamec.sel.gamac.data.GamacDetalleVenta2;
import pe.gob.sucamec.sel.gamac.data.GamacSbDistrito;
import pe.gob.sucamec.sel.gamac.data.GamacSbPersona;
import pe.gob.sucamec.sel.gamac.data.GamacTipoGamac;
import pe.gob.sucamec.sel.gamac.data.GamacWsLicencias;
import pe.gob.sucamec.sel.gamac.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;
import pe.gob.sucamec.bdintegrado.bean.AmaResolucionFacade;
import pe.gob.sucamec.bdintegrado.data.AmaResolucion;

/**
 *
 * @author rarevalo
 */
@Named("gamacMunicionesRegistrarVentaController")
@SessionScoped
public class GamacMunicionesRegistrarVentaController implements Serializable {

    /**
     * Variables de Registro de Venta
     */
    private List<GamacSbDireccion> lstLocales;
    private GamacSbDireccion direccion;
    private int tipoComprador;
    private List<GamacTipoGamac> lstTipoDoc;
    private List<GamacTipoBase> lstZonas;
    private List<GamacTipoBase> lstVias;
    private GamacTipoBase direZona;
    private GamacTipoBase direVia;
    private GamacTipoGamac tipoComprobante;
    private GamacAmaLicenciaDeUso licenciaDeUso;
    private GamacAmaTarjetaPropiedad tarjetaPropiedad;
    private GamacWsLicencias wsLicencia;
    private GamacWsLicencias personaPnpFa;
    private GamacSbPersona cliente;
    private GamacSbPersona representante;
    private String nroLicencia;
    private String dniPerNat;
    private String dniComprador;
    private String rucComprador;
    private List<GamacSbPersona> lstRepresentantes;
    private String dniRepresentante; 
    private String licenciaEstado;
    //private String bioDni;
    //private String bioNombre;
    private List<GamacAmaTarjetaPropiedad> lstRuaSerie;
    private List<GamacWsLicencias> lstRuaSeries; 
    private List<GamacAmaCatalogo> lstCalibres; 
    private List<GamacAmaMunicion> lstMuniciones; 
    private GamacWsLicencias nroRuaSerieArma; 
    private GamacAmaArma arma;
    private String armaTipo;
    private String armaMarca;
    private String armaModelo;
    private String tipoLicencia;
    private boolean validarSaldo;
    private String saldoMesAnt;
    private String saldoMesAct;
    private String numDocVenta;
    private Date fechaVenta = new Date();
    private String nroConsultaRENIEC;
    private List<GamacAmaAdmunDetalleTrans> lstDetalle1;  
    private List<GamacDetalleVenta1> lstDetalleVenta1; 
    private List<GamacDetalleVenta1> lstDetalleVenta1Selected; 
    private List<GamacAmaAdmunDetalleTrans> lstDetalle2; 
    private List<GamacDetalleVenta2> lstDetalleVenta2; 
    private List<GamacDetalleVenta2> lstDetalleVenta2Selected;     
    private GamacAmaAdmunDetalleTrans detalleTrans; 
    private GamacDetalleVenta1 detalleVenta1;
    private GamacDetalleVenta2 detalleVenta2;
    private GamacAmaMunicion municion;
    private GamacAmaCatalogo calibre;
    private List<GamacSbDireccion> lstDestinos;
    private GamacSbDireccion destino;
    private String idsUbigeo; 
    //private String saveOk; 
    //private boolean venValidBio;
    
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacTipoGamacFacade gamacTipoGamacFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaLicenciaDeUsoFacade gamacAmaLicenciaDeUsoFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacWsLicenciasFacade gamacWsLicenciasFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacTipoBaseFacade gamacTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacSbDireccionFacade gamacSbDireccionFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacSbDistritoFacade gamacSbDistritoFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaArmaFacade gamacAmaArmaFacade;    
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaCatalogoFacade gamacAmaCatalogoFacade;    
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaModelosFacade gamacAmaModelosFacade;    
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaMunicionFacade gamacAmaMunicionFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaTarjetaPropiedadFacade gamacAmaTarjetaPropiedadFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaAdmunTransaccionFacade gamacAmaAdmunTransaccionFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaAdmunDetalleTransFacade gamacAmaAdmunDetalleTransFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacSbPersonaFacade gamacSbPersonaFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacRma1369Facade gamacRma1369Facade;
    @EJB
    private AmaResolucionFacade ejbAmaResolucionFacade;
	
    @Inject
    private LoginController loginController; 

    public List<GamacSbDireccion> getLstLocales() {
        return lstLocales;
    }

    public void setLstLocales(List<GamacSbDireccion> lstLocales) {
        this.lstLocales = lstLocales;
    }

    public GamacSbDireccion getDireccion() {
        return direccion;
    }

    public void setDireccion(GamacSbDireccion direccion) {
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

    public List<GamacTipoBase> getLstZonas() {
        return gamacTipoBaseFacade.lstTipoBase("TP_ZON");
    }

    public void setLstZonas(List<GamacTipoBase> lstZonas) {
        this.lstZonas = lstZonas;
    }

    public List<GamacTipoBase> getLstVias() {
        return gamacTipoBaseFacade.lstTipoBase("TP_VIA");
    }

    public void setLstVias(List<GamacTipoBase> lstVias) {
        this.lstVias = lstVias;
    }

    public GamacTipoBase getDireZona() {
        return direZona;
    }

    public void setDireZona(GamacTipoBase direZona) {
        this.direZona = direZona;
    }

    public GamacTipoBase getDireVia() {
        return direVia;
    }

    public void setDireVia(GamacTipoBase direVia) {
        this.direVia = direVia;
    }

    public GamacTipoGamac getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(GamacTipoGamac tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public GamacAmaLicenciaDeUso getLicenciaDeUso() {
        return licenciaDeUso;
    }

    public void setLicenciaDeUso(GamacAmaLicenciaDeUso licenciaDeUso) {
        this.licenciaDeUso = licenciaDeUso;
    }

    public GamacAmaTarjetaPropiedad getTarjetaPropiedad() {
        return tarjetaPropiedad;
    }

    public void setTarjetaPropiedad(GamacAmaTarjetaPropiedad tarjetaPropiedad) {
        this.tarjetaPropiedad = tarjetaPropiedad;
    }

    public GamacWsLicencias getWsLicencia() {
        return wsLicencia;
    }

    public void setWsLicencia(GamacWsLicencias wsLicencia) {
        this.wsLicencia = wsLicencia;
    }

    public GamacWsLicencias getPersonaPnpFa() {
        return personaPnpFa;
    }

    public GamacSbPersona getCliente() {
        return cliente;
    }

    public void setCliente(GamacSbPersona cliente) {
        this.cliente = cliente;
    }

    public GamacSbPersona getRepresentante() {
        return representante;
    }

    public void setRepresentante(GamacSbPersona representante) {
        this.representante = representante;
    }

    public void setPersonaPnpFa(GamacWsLicencias personaPnpFa) {
        this.personaPnpFa = personaPnpFa;
    }

    public String getNroLicencia() {
        return nroLicencia;
    }

    public void setNroLicencia(String nroLicencia) {
        this.nroLicencia = nroLicencia;
    }

    public String getDniPerNat() {
        return dniPerNat;
    }

    public void setDniPerNat(String dniPerNat) {
        this.dniPerNat = dniPerNat;
    }

    public String getDniComprador() {
        return dniComprador;
    }

    public void setDniComprador(String dniComprador) {
        this.dniComprador = dniComprador;
    }

    public String getRucComprador() {
        return rucComprador;
    }

    public void setRucComprador(String rucComprador) {
        this.rucComprador = rucComprador;
    }

    public List<GamacSbPersona> getLstRepresentantes() {
        return lstRepresentantes;
    }

    public void setLstRepresentantes(List<GamacSbPersona> lstRepresentantes) {
        this.lstRepresentantes = lstRepresentantes;
    }

    public String getDniRepresentante() {
        return dniRepresentante;
    }

    public void setDniRepresentante(String dniRepresentante) {
        this.dniRepresentante = dniRepresentante;
    }

    public String getLicenciaEstado() {
        return licenciaEstado;
    }

    public void setLicenciaEstado(String licenciaEstado) {
        this.licenciaEstado = licenciaEstado;
    }

    /*public String getBioDni() {
        return bioDni;
    }

    public void setBioDni(String bioDni) {
        this.bioDni = bioDni;
    }

    public String getBioNombre() {
        return bioNombre;
    }

    public void setBioNombre(String bioNombre) {
        this.bioNombre = bioNombre;
    }*/

    public List<GamacAmaTarjetaPropiedad> getLstRuaSerie() {
        return lstRuaSerie;
    }

    public void setLstRuaSerie(List<GamacAmaTarjetaPropiedad> lstRuaSerie) {
        this.lstRuaSerie = lstRuaSerie;
    }

    public List<GamacWsLicencias> getLstRuaSeries() {
        return lstRuaSeries;
    }

    public void setLstRuaSeries(List<GamacWsLicencias> lstRuaSeries) {
        this.lstRuaSeries = lstRuaSeries;
    }

    public List<GamacAmaCatalogo> getLstCalibres() {
        return lstCalibres;
    }

    public void setLstCalibres(List<GamacAmaCatalogo> lstCalibres) {
        this.lstCalibres = lstCalibres;
    }

    public List<GamacAmaMunicion> getLstMuniciones() {
        return lstMuniciones;
    }

    public void setLstMuniciones(List<GamacAmaMunicion> lstMuniciones) {
        this.lstMuniciones = lstMuniciones;
    }
    
    public GamacWsLicencias getNroRuaSerieArma() {
        return nroRuaSerieArma;
    }

    public void setNroRuaSerieArma(GamacWsLicencias nroRuaSerieArma) {
        this.nroRuaSerieArma = nroRuaSerieArma;
    }

    public GamacAmaArma getArma() {
        return arma;
    }

    public void setArma(GamacAmaArma arma) {
        this.arma = arma;
    }

    public String getArmaTipo() {
        return armaTipo;
    }

    public void setArmaTipo(String armaTipo) {
        this.armaTipo = armaTipo;
    }

    public String getArmaMarca() {
        return armaMarca;
    }

    public void setArmaMarca(String armaMarca) {
        this.armaMarca = armaMarca;
    }

    public String getArmaModelo() {
        return armaModelo;
    }

    public void setArmaModelo(String armaModelo) {
        this.armaModelo = armaModelo;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public boolean isValidarSaldo() {
        return validarSaldo;
    }

    public void setValidarSaldo(boolean validarSaldo) {
        this.validarSaldo = validarSaldo;
    }

    public String getSaldoMesAnt() {
        return saldoMesAnt;
    }

    public void setSaldoMesAnt(String saldoMesAnt) {
        this.saldoMesAnt = saldoMesAnt;
    }

    public String getSaldoMesAct() {
        return saldoMesAct;
    }

    public void setSaldoMesAct(String saldoMesAct) {
        this.saldoMesAct = saldoMesAct;
    }

    public String getNumDocVenta() {
        return numDocVenta;
    }

    public void setNumDocVenta(String numDocVenta) {
        this.numDocVenta = numDocVenta;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getNroConsultaRENIEC() {
        return nroConsultaRENIEC;
    }

    public void setNroConsultaRENIEC(String nroConsultaRENIEC) {
        this.nroConsultaRENIEC = nroConsultaRENIEC;
    }

    public List<GamacAmaAdmunDetalleTrans> getLstDetalle1() {
        return lstDetalle1;
    }

    public void setLstDetalle1(List<GamacAmaAdmunDetalleTrans> lstDetalle1) {
        this.lstDetalle1 = lstDetalle1;
    }

    public List<GamacDetalleVenta1> getLstDetalleVenta1() {
        return lstDetalleVenta1;
    }

    public void setLstDetalleVenta1(List<GamacDetalleVenta1> lstDetalleVenta1) {
        this.lstDetalleVenta1 = lstDetalleVenta1;
    }

    public List<GamacDetalleVenta1> getLstDetalleVenta1Selected() {
        return lstDetalleVenta1Selected;
    }

    public void setLstDetalleVenta1Selected(List<GamacDetalleVenta1> lstDetalleVenta1Selected) {
        this.lstDetalleVenta1Selected = lstDetalleVenta1Selected;
    }

    public List<GamacAmaAdmunDetalleTrans> getLstDetalle2() {
        return lstDetalle2;
    }

    public void setLstDetalle2(List<GamacAmaAdmunDetalleTrans> lstDetalle2) {
        this.lstDetalle2 = lstDetalle2;
    }

    public List<GamacDetalleVenta2> getLstDetalleVenta2() {
        return lstDetalleVenta2;
    }

    public void setLstDetalleVenta2(List<GamacDetalleVenta2> lstDetalleVenta2) {
        this.lstDetalleVenta2 = lstDetalleVenta2;
    }

    public List<GamacDetalleVenta2> getLstDetalleVenta2Selected() {
        return lstDetalleVenta2Selected;
    }

    public void setLstDetalleVenta2Selected(List<GamacDetalleVenta2> lstDetalleVenta2Selected) {
        this.lstDetalleVenta2Selected = lstDetalleVenta2Selected;
    }

    public GamacAmaAdmunDetalleTrans getDetalleTrans() {
        return detalleTrans;
    }

    public void setDetalleTrans(GamacAmaAdmunDetalleTrans detalleTrans) {
        this.detalleTrans = detalleTrans;
    }

    public GamacDetalleVenta1 getDetalleVenta1() {
        return detalleVenta1;
    }

    public void setDetalleVenta1(GamacDetalleVenta1 detalleVenta1) {
        this.detalleVenta1 = detalleVenta1;
    }

    public GamacDetalleVenta2 getDetalleVenta2() {
        return detalleVenta2;
    }

    public void setDetalleVenta2(GamacDetalleVenta2 detalleVenta2) {
        this.detalleVenta2 = detalleVenta2;
    }

    public GamacAmaMunicion getMunicion() {
        return municion;
    }

    public void setMunicion(GamacAmaMunicion municion) {
        this.municion = municion;
    }

    public GamacAmaCatalogo getCalibre() {
        return calibre;
    }

    public void setCalibre(GamacAmaCatalogo calibre) {
        this.calibre = calibre;
    }

    public List<GamacSbDireccion> getLstDestinos() {
        return lstDestinos;
    }

    public void setLstDestinos(List<GamacSbDireccion> lstDestinos) {
        this.lstDestinos = lstDestinos;
    }

    public GamacSbDireccion getDestino() {
        return destino;
    }

    public void setDestino(GamacSbDireccion destino) {
        this.destino = destino;
    }

    public String getIdsUbigeo() {
        return idsUbigeo;
    }

    public void setIdsUbigeo(String idsUbigeo) {
        this.idsUbigeo = idsUbigeo;
    }

    /*public boolean isVenValidBio() {
        return venValidBio;
    }

    public void setVenValidBio(boolean venValidBio) {
        this.venValidBio = venValidBio;
    }*/
   
    /**
     * METODO PARA OBTENER LOS DATOS PERSONALES DEL USUARIO LOGUEADO
     *
     * @return Datos personales completos del usuario logueado
     */
    public GamacSbPersona personaUsuario() {
        GamacSbPersona personaUsuario = new GamacSbPersona();
        personaUsuario = gamacSbPersonaFacade.find(loginController.getUsuario().getPersona().getId());
        return personaUsuario;
    }
    
    /**
     * Funcion tipo 'action' para iniciar registro
     *
     * @return URL de registro
     */
    public String prepareRegistro() {
        String res = "";
        if (verificarAutorizacionUsuario()) {
            reiniciarValoresRegistro();
            res = "/aplicacion/gamac/municiones/registrarVenta/RegistrarVenta.xhtml";
        } else {
            JsfUtil.mensajeAdvertencia(mensajeValidacion());
        }
        return res;
    }

    private void reiniciarValoresRegistro() {
        direccion = null;
        lstTipoDoc = null;
        tipoComprobante = null;
        tipoComprador = 0;
        lstRepresentantes = null;
        cliente = null;
        representante = null;
        licenciaDeUso = null;
        wsLicencia = null;
        personaPnpFa = null;
        nroLicencia = "";
        dniComprador = "";
        rucComprador = "";
        dniRepresentante = "";
        numDocVenta = "";
        nroConsultaRENIEC = "";
        //bioDni = "";
        //bioNombre = "";
        tipoLicencia = "";
        licenciaEstado = "";
        lstRuaSeries = null;
        lstRuaSerie = null;        
        nroRuaSerieArma = null;
        armaMarca = "";
        armaModelo = "";
        armaTipo = "";
        lstCalibres = null;
        lstMuniciones = null;
        municion = null;
        lstDetalle1 = null;
        lstDetalleVenta1 = null;
        direZona = null;
        direVia = null;
        saldoMesAct = "";
        saldoMesAnt = "";
        //venValidBio = false;
    }

    /*public void loadFormLector() {
        if (tipoComprador != 0) {
            RequestContext.getCurrentInstance().execute("$(\"input[id*='bioLogin']\").val(\""+ loginController.getUsuario().getLogin().trim() +"\")");
            switch (tipoComprador) {
                case 1:
                    if(cliente!= null){
                        RequestContext.getCurrentInstance().execute("PF('controlBiometricoDialog').show()");
                        RequestContext.getCurrentInstance().execute("lectorBiometrico()");            
                    } else {
                        JsfUtil.mensajeAdvertencia("Eliga un DNI validación biométrica!");
                    }
                    break;
                case 3:
                case 4:     
                    if(representante!= null){
                        RequestContext.getCurrentInstance().execute("PF('controlBiometricoDialog').show()");
                        RequestContext.getCurrentInstance().execute("lectorBiometrico()");            
                    } else {
                        JsfUtil.mensajeAdvertencia("Eliga un Representante para validación biométrica!");
                    }
                    break;                    
            }
        } else {
            JsfUtil.mensajeAdvertencia("Eliga un DNI o Representante para validación biométrica!");
        }
    }

    public void reiniciaFormLector() {
        RequestContext.getCurrentInstance().execute("beforeVerificar()");                
        RequestContext.getCurrentInstance().execute("ReiniciarBio()");
    }

    public void cancelFormLector() {
        RequestContext.getCurrentInstance().execute("cancelBio(0)");
    }
    
    public void verificaHuella() {
        JsfUtil.addSuccessMessage("La HUELLA CORRESPONDE AL DNI");
    }

    public void confirmarHuella() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        String id = (String) map.get("id");
        String mensaje = (String) map.get("mensaje");
        String txtMsg = "El DNI ingresado corresponde con los registros de la huella dactilar ingresada!";
        JsfUtil.addSuccessMessage(txtMsg);
        venValidBio = true;
        
        try{    
            Thread.sleep(2000);
            RequestContext.getCurrentInstance().execute("PF('controlBiometricoDialog').hide()");
            RequestContext.getCurrentInstance().execute("confirmaVentaBio('La HUELLA CORRESPONDE AL DNI!')");
            Thread.sleep(1000);
            validaComprador();
            RequestContext.getCurrentInstance().execute("continuarCompra()");            
        }catch(InterruptedException e){
            e.printStackTrace();
        }        
    }

    public void fallaHuella() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        //String id = (String) map.get("id");
        String mensaje = (String) map.get("mensaje");
        JsfUtil.mensajeAdvertencia("Error en consulta Biométrica: " + mensaje);
    }*/
    
    public Boolean verificarAutorizacionUsuario() {
        List<Map> lstResolucionRma = null;
        Boolean autorizado = Boolean.FALSE;
        //Busca Autorizaciones en el Disca
        lstResolucionRma = gamacRma1369Facade.buscarRDCasaCom(personaUsuario().getRuc() == null ? personaUsuario().getNumDoc() : personaUsuario().getRuc());
        //Busca Autorizaciones en el RENAGI
        List<AmaResolucion> listResoluciones = ejbAmaResolucionFacade.obtenerResolucionesVigentes(personaUsuario().getRuc() == null ? personaUsuario().getNumDoc() : personaUsuario().getRuc());
        if ((lstResolucionRma != null && !lstResolucionRma.isEmpty())
                || (listResoluciones != null && !listResoluciones.isEmpty())) {         
            //estado = EstadoCrud.USUAUTORIZADO;
            autorizado = Boolean.TRUE;
        } else {
            //estado = EstadoCrud.USUNOAUTORIZADO;
        }
        return autorizado;
    }
    
    public String mensajeValidacion() {
        String msj = personaUsuario().getRznSocial() == null ? JsfUtil.bundleGamac("MensajeUsuarioNatNoAutorizado")
                : JsfUtil.bundleGamac("MensajeUsuarioNoAutorizado1") + " "
                + (personaUsuario().getRznSocial()) + " " + JsfUtil.bundleGamac("MensajeUsuarioNoAutorizado2");
        return msj;
    }
    
    public void elijeTipoComprador(SelectEvent event) {
        tipoComprador = Integer.valueOf(event.getObject().toString());
        //Syso("tipoComprador:" + tipoComprador);

        nroLicencia = "";
        dniComprador = "";
        rucComprador = "";
        dniRepresentante = "";
        licenciaEstado = "";
        RequestContext req = RequestContext.getCurrentInstance();
        req.update("registroForm:estadoLicencia");

    }

    public void validaComprador() {
        if (tipoComprador != 0) {
            saldoMesAct = "";
            saldoMesAnt = "";
            
            switch (tipoComprador) {
                case 1:
                    if (dniPerNat == "") {
                        JsfUtil.addErrorMessage("DNI es obligatorio");
                        return;
                    }
                    if (!licenciaEstado.trim().equals("VIGENTE")) {
                        JsfUtil.addErrorMessage("la Licencia elegida debe estar VIGENTE!");
                        return;
                    }

                    lstRuaSerie = autcompleteRuaSerie(dniPerNat);
                    //Syso("lstRuaSeries.size(): " + lstRuaSerie.size());
                    RequestContext.getCurrentInstance().update("registroForm:detalleDatatable1"); 
                    RequestContext.getCurrentInstance().update("registroForm:nroRuaSerie"); 
                    RequestContext.getCurrentInstance().execute("continuarCompra();");
                    detalleTrans = new GamacAmaAdmunDetalleTrans(); 
                    detalleVenta1 = new GamacDetalleVenta1();
                    lstDetalle1 = new ArrayList<GamacAmaAdmunDetalleTrans>();
                    lstDetalleVenta1 = new ArrayList<GamacDetalleVenta1>();
                    break;
                case 2:
                    if (dniComprador == "") {
                        JsfUtil.addErrorMessage("DNI de comprador es obligatorio");
                        return;
                    }
                    if (!licenciaEstado.trim().equals("VIGENTE")) {
                        JsfUtil.addErrorMessage("la Licencia elegida debe estar VIGENTE!");
                        return;
                    }
                    
                    lstRuaSerie = autcompleteRuaSerie(dniPerNat);
                    //Syso("lstRuaSeries.size(): " + lstRuaSerie.size());
                    RequestContext.getCurrentInstance().execute("continuarCompra();");
                    detalleTrans = new GamacAmaAdmunDetalleTrans(); 
                    detalleVenta1 = new GamacDetalleVenta1();
                    lstDetalle1 = new ArrayList<GamacAmaAdmunDetalleTrans>();
                    lstDetalleVenta1 = new ArrayList<GamacDetalleVenta1>();
                    
                    RequestContext.getCurrentInstance().execute("continuarCompra();");
                    break;

                case 3:
                    if (rucComprador == "") {
                        JsfUtil.addErrorMessage("RUC de comprador es obligatorio");
                        return;
                    }
                    if (representante == null) {
                        JsfUtil.addErrorMessage("El representante es obligatorio");
                        return;
                    }
                    
                    lstRuaSerie = autcompleteRuaSerie(rucComprador);
                    //Syso("lstRuaSeries.size(): " + lstRuaSerie.size());
                    RequestContext.getCurrentInstance().update("registroForm:detalleDatatable1"); 
                    RequestContext.getCurrentInstance().execute("continuarCompra();");
                    detalleTrans = new GamacAmaAdmunDetalleTrans(); 
                    detalleVenta1 = new GamacDetalleVenta1();
                    lstDetalle1 = new ArrayList<GamacAmaAdmunDetalleTrans>();
                    lstDetalleVenta1 = new ArrayList<GamacDetalleVenta1>();
                    break;
                    
                case 4:
                    if (rucComprador == "") {
                        JsfUtil.addErrorMessage("RUC de comprador es obligatorio");
                        return;
                    }
                    if (representante == null) {
                        JsfUtil.addErrorMessage("El representante es obligatorio");
                        return;
                    }
                    
                    destino = null;
                    municion = null;
                    detalleTrans = new GamacAmaAdmunDetalleTrans(); 
                    detalleVenta2 = new GamacDetalleVenta2();
                    lstDetalle2 = new ArrayList<GamacAmaAdmunDetalleTrans>(); 
                    lstDetalleVenta2 = new ArrayList<GamacDetalleVenta2>();
                    RequestContext.getCurrentInstance().update("registroForm:detalleDatatable2");
                    RequestContext.getCurrentInstance().execute("continuarCompra();");
                    
                    lstDestinos = gamacSbDireccionFacade.listarDirecionPorPersonaS(rucComprador);
                    RequestContext.getCurrentInstance().update("registroForm:destino");
                    break;
            }
        }
    }

    public List<ArrayRecord> autcompleteLicencia(String query) {
        try {
            List<ArrayRecord> listLicencia = gamacWsLicenciasFacade.listPersonaNatLic(query);
            return listLicencia;

            //List<GamacAmaLicenciaDeUso> listLicencia = gamacAmaLicenciaDeUsoFacade.listLicencia(query);
            //return listLicencia;
            /*List<GamacAmaLicenciaDeUso> filteredList = new ArrayList();
            List<GamacAmaLicenciaDeUso> lstUniv = gamacAmaLicenciaDeUsoFacade.listLicencia(query);
            //Syso("cant.: " + lstUniv.size());
            for (GamacAmaLicenciaDeUso x : lstUniv) {
                //if (x.getRuc().contains(query.trim()) || x.getRznSocial().contains(query.trim().toUpperCase())) {
                    filteredList.add(x);
                //}
            }
            return filteredList;*/
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<GamacWsLicencias> autcompleteWsLicencia(String query) {
        try {
            List<GamacWsLicencias> listLicencia = gamacWsLicenciasFacade.listPersonaNatLicencia(query);
            return listLicencia;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<GamacWsLicencias> autcompleteNroSerie(String query, String nroLicencia) {
        try {
            List<GamacWsLicencias> listNroSerie = gamacWsLicenciasFacade.listNroSerie(query, nroLicencia);
            return listNroSerie;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<GamacAmaTarjetaPropiedad> autcompleteRuaSerie(String dniPerNat) {
        try {
            List<GamacAmaTarjetaPropiedad> list = gamacAmaTarjetaPropiedadFacade.findByComprador(dniPerNat);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    public List<ArrayRecord> autcompletePerJur(String query) {
        try {
            List<ArrayRecord> list = gamacAmaTarjetaPropiedadFacade.listPerJur(query);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<ArrayRecord> autocompletePerNatPnpFfaa(String query) {
        try {
            List<ArrayRecord> list = gamacAmaTarjetaPropiedadFacade.listPerNatPnpFa(query);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<GamacAmaMunicion> autcompleteMunicionxPerJur(String query) {
        try {
            List<GamacAmaMunicion> list = gamacAmaMunicionFacade.listxPerJur(rucComprador, query);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<GamacAmaMunicion> autcompleteMunicionxCalibre(String query) {
        try {
            if(calibre!=null){
                List<GamacAmaMunicion> list = gamacAmaMunicionFacade.listxCalibre(calibre.getId().toString(), query);
                return list; 
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    public void eligePerNatPnpFa(SelectEvent event) {
        if (event.getObject() != null) {
            String value = (String) event.getObject().toString();
            //Syso("elegido:" + value);
            String[] ids = value.split(",");
            dniPerNat = ids[0];
            licenciaEstado = ids[1];
            cliente = gamacSbPersonaFacade.find(Long.valueOf(dniPerNat));
            //bioDni = cliente.getNumDoc();
            //bioNombre = cliente.getNombres() + " " + cliente.getApePat() + " " + cliente.getApeMat();

            RequestContext.getCurrentInstance().update("registroForm:estadoLicencia");
            //RequestContext.getCurrentInstance().execute("asignarDniBio('" + bioDni + "','" + bioNombre + "');");            
            //RequestContext.getCurrentInstance().update("controlBiometricoForm:bioDNI");
            //RequestContext.getCurrentInstance().update("controlBiometricoForm:bioNombre");
            //RequestContext.getCurrentInstance().execute("asignarDniBio();");
            
        } else {
            dniPerNat = "";
            cliente = null;
            //bioDni = "";
            //bioNombre = "";            
        }
        //venValidBio = false;
        //RequestContext.getCurrentInstance().execute("$(\"#divMsgBio\").hide();");
        
    }
    
    public void eligePerJur(SelectEvent event) {
        String value = (String) event.getObject().toString();
        //Syso("elegido:" + value);
        String[] ids = value.split(",");
        rucComprador = ids[0];
        cliente = gamacSbPersonaFacade.find(Long.valueOf(rucComprador));
        lstRepresentantes = gamacSbPersonaFacade.listxRepresentante(rucComprador);
        representante = null;
        
        RequestContext.getCurrentInstance().update("registroForm:dniRepresentante");
    }
    
//    public void eligeLicencia(SelectEvent event) {
//        String value = (String) event.getObject().toString();
//
//        //Syso("elegido:" + value);
//        String[] ids = value.split(",");
//        nroLicencia = ids[0].trim();
//        licenciaEstado = ids[1].trim();
//
//        RequestContext.getCurrentInstance().update("registroForm:estadoLicencia");
//    }

//    public void eligeWsLicencia(SelectEvent event) {
//        //licenciaDeUso = (GamacAmaLicenciaDeUso) event.getObject();
//        //licenciaEstado = licenciaDeUso.getEstadoId().getNombre().toUpperCase();
//        wsLicencia = (GamacWsLicencias) event.getObject();
//        licenciaEstado = wsLicencia.getEstado();
//
//        RequestContext.getCurrentInstance().update("registroForm:estadoLicencia");
//    }

    public void eligeRuaSerieXX(SelectEvent event) {
        if (event.getObject() != null) {
            //Syso("nroRuaSerieArma: " + event.getObject().toString());
            String value = event.getObject().toString();
            nroRuaSerieArma = gamacWsLicenciasFacade.find(event.getObject());  //getWsLicencias(value);
            armaTipo = nroRuaSerieArma.getTipoArma();
            armaMarca = nroRuaSerieArma.getMarca();
            armaModelo = nroRuaSerieArma.getModelo();
            tipoLicencia = nroRuaSerieArma.getTipoLicencia();

            arma = gamacAmaArmaFacade.findBySerie(nroRuaSerieArma.getNroSerie());
            tarjetaPropiedad = gamacAmaTarjetaPropiedadFacade.findByArma(arma);
            //Syso("Tarjeta Propiedad: " + tarjetaPropiedad.getId());
            
            //Syso("busca calibre de modelo: " + arma.getModeloId().getId() );
            
            GamacAmaModelos modelo = gamacAmaModelosFacade.find(arma.getModeloId().getId());
            //Syso("cant Calibre: " + modelo.getGamacAmaCatalogoCollection().size() );
            
            lstCalibres = (List<GamacAmaCatalogo>) modelo.getGamacAmaCatalogoCollection();
            if(lstCalibres != null){
                if(lstCalibres.size()>0){
                    for (GamacAmaCatalogo row : lstCalibres) {
                        //Syso("Calibre: " + row.getNombre());
                    }
                    RequestContext.getCurrentInstance().update("registroForm:calibre"); 
                    
                    lstMuniciones = null;
                    RequestContext.getCurrentInstance().update("registroForm:articuloPN"); 
                }
                
            }else{
                JsfUtil.addErrorMessage("El modelo de arma no registra Calibre!"); 
            }
        }
        else{
            nroRuaSerieArma = null;
            armaTipo = "";
            armaMarca = "";
            armaModelo = "";
            tipoLicencia = "";
        }

        //RequestContext.getCurrentInstance().update("registroForm:estadoLicencia");
    }

    public void eligeRepresentante(SelectEvent event) {
        if (event.getObject() != null) {
            //Syso("Representante: " + event.getObject().toString());
            //RequestContext.getCurrentInstance().update("registroForm:estadoLicencia");
            dniPerNat = event.getObject().toString();
            representante = gamacSbPersonaFacade.find(Long.valueOf(dniPerNat));
            //bioDni = representante.getNumDoc();
            //bioNombre = representante.getNombres() + " " + representante.getApePat() + " " + representante.getApeMat();

            //RequestContext.getCurrentInstance().execute("asignarDniBio('" + bioDni + "','" + bioNombre + "');");
            //RequestContext.getCurrentInstance().update("controlBiometricoForm:bioDNI");
            //RequestContext.getCurrentInstance().update("controlBiometricoForm:bioNombre");        
            
        }
        else{
            dniPerNat = "";
            representante = null;
            //bioDni = "";
            //bioNombre = "";
        }
        //venValidBio = false;
        //RequestContext.getCurrentInstance().execute("$(\"#divMsgBio\").hide();");
    }

    public void eligeRuaSerie(SelectEvent event) {
        if (event.getObject() != null) {
            //Syso("nroRuaSerieArma: " + event.getObject().toString());
            String value = event.getObject().toString();
            tarjetaPropiedad = gamacAmaTarjetaPropiedadFacade.findById(value);
            //tarjetaPropiedad = (GamacAmaTarjetaPropiedad) event.getObject();
            armaTipo = tarjetaPropiedad.getArmaId().getModeloId().getTipoArmaId().getNombre();
            armaMarca = tarjetaPropiedad.getArmaId().getModeloId().getMarcaId().getNombre();
            armaModelo = tarjetaPropiedad.getArmaId().getModeloId().getModelo();
            tipoLicencia = tarjetaPropiedad.getModalidadId().getNombre();
            arma = tarjetaPropiedad.getArmaId();
            validarSaldo = false;
            saldoMesAct = "";
            
            List<String> list = new ArrayList<String>();
            list.add("TP_MOD_DEF");
            list.add("TP_MOD_SIS");
            list.add("TP_MOD_SEG");
            if(list.contains(tarjetaPropiedad.getModalidadId().getCodProg())){
                validarSaldo = true;
            }
  
            GamacAmaModelos modelo = tarjetaPropiedad.getArmaId().getModeloId(); //gamacAmaModelosFacade.find(arma.getModeloId().getId());
            
            lstCalibres = (List<GamacAmaCatalogo>) modelo.getGamacAmaCatalogoCollection();
            if(lstCalibres != null){
                if(lstCalibres.size()>0){
                    for (GamacAmaCatalogo row : lstCalibres) {
                        //Syso("Calibre: " + row.getNombre());
                    }
                    RequestContext.getCurrentInstance().update("registroForm:calibre"); 
                    
                    lstMuniciones = null;
                    RequestContext.getCurrentInstance().update("registroForm:articuloPN"); 
                }
            }else{
                JsfUtil.addErrorMessage("El modelo de arma no registra Calibre!"); 
            }
            calibre = null;
            saldoMesAct = "";            
        }
        else{
            nroRuaSerieArma = null;
            armaTipo = "";
            armaMarca = "";
            armaModelo = "";
            tipoLicencia = "";
            calibre = null;
        }

        //RequestContext.getCurrentInstance().update("registroForm:estadoLicencia");
    }
    
    public void eligeCalibre(SelectEvent event) {
        if (event.getObject() != null) {
            //Syso("Calibre elegido: " + event.getObject().toString());
            calibre = (GamacAmaCatalogo) event.getObject();

            if(lstCalibres != null){
                if(lstCalibres.size()>0){
                    // Filtro para saldos
                    HashMap mMap = new HashMap();
                    mMap.put("tarjetaPropiedadId", tarjetaPropiedad.getId());
                    mMap.put("calibreId", calibre.getId());
                    //Syso("tarjetaPropiedadId:" + tarjetaPropiedad.getId() + ", municionId: " + calibre.getId());
                    
                    // Obtenemos saldo Anterior
                    /*List<ArrayRecord> cantMesAnt = gamacAmaAdmunDetalleTransFacade.saldoMesAnterior(mMap);
                    int salMesAnt = 600;
                    if(cantMesAnt!=null){
                        if(cantMesAnt.size()>0){
                            int rowCanMesAnt = Integer.valueOf(cantMesAnt.get(0).get("SALDO_MES_ANT").toString());
                            salMesAnt = salMesAnt - rowCanMesAnt;
                        }
                        
                    }
                    saldoMesAnt = String.valueOf(salMesAnt);*/

                    // SI tarjetaPropiedad es de Modalidad L1, L4, L5
                    if(validarSaldo){
                        //Syso("validarSaldo:" + validarSaldo);
                        // Obtenemos saldo Mes Actual
                        List<ArrayRecord> cantMesAct = gamacAmaAdmunDetalleTransFacade.saldoMesActual(mMap);
                        //Syso("cantMesAct:" + cantMesAct);   
                        
                        int salMesAct = 600;
                        if(cantMesAct!=null){
                            if(cantMesAct.size()>0){
                                int rowCanMesAnt = Integer.valueOf(cantMesAct.get(0).get("SALDO_MES_ACT").toString());
                                salMesAct = salMesAct - rowCanMesAnt;
                            }
                        }
                        int sumCtaVen = 0;
                        for (GamacDetalleVenta1 detVta: lstDetalleVenta1) {
                            sumCtaVen += detVta.getCantidadVendida();
                        }
                        salMesAct -= sumCtaVen;
                        saldoMesAct = String.valueOf(salMesAct);
                    } else {
                        saldoMesAct = "-";  
                    }
                    RequestContext.getCurrentInstance().update("registroForm:saldoMesAct"); 
                    
                    // Filtro de Municiones para Calibre
                    lstMuniciones = gamacAmaMunicionFacade.findAll();
                    RequestContext.getCurrentInstance().update("registroForm:articuloPN"); 
                    municion = null;
                    //
                } 
            }else{
                JsfUtil.addErrorMessage("El modelo de arma no registra Calibre!"); 
            }
        }
        else{
            lstMuniciones = null; 
            municion = null; 
        }
        //RequestContext.getCurrentInstance().update("registroForm:estadoLicencia");
    }

    public void eligeMunicion(SelectEvent event) {
        if (event.getObject() != null) {
            municion = (GamacAmaMunicion) event.getObject();
            //Syso("Municion elegida: " + event.getObject().toString());
            
        } else {
            //
        } 
    }   
    
    public void eligeMunicionxPerJur(SelectEvent event){
        if (event.getObject() != null) {
            municion = (GamacAmaMunicion) event.getObject();
            //Syso("Municion elegida: " + event.getObject().toString());
            
        } else {
            //
        }         
    }
    
    public void addMunicionGrilla1() {
        boolean validaOk = false;
        String source = JsfUtil.getRequestParameter("javax.faces.source"); 
        String codFab = JsfUtil.getRequestParameter("registroForm:codFabPN");        
        String loteFab = JsfUtil.getRequestParameter("registroForm:loteFabPN");        
        String munXEmp = JsfUtil.getRequestParameter("registroForm:munXEmpPN");
        String cantEmp = JsfUtil.getRequestParameter("registroForm:cantEmpPN");
        //String cantMunicion = JsfUtil.getRequestParameter("registroForm:cantMunicionPN");
        String idDetalle = arma.getNroRua().toString() + ":" + arma.getId() + ":" + municion.getId();
        if(source.equals("registroForm:addArticulo1")){
            //Syso("source: " + source); 
            
            if(direccion != null){
                validaOk = true;
            }else{
                validaOk = false;
                JsfUtil.mensajeError("Seleccione el local.");    
                return;
            }
            
            if(codFab != null){
                validaOk = true;
            }else{
                validaOk = false;
                JsfUtil.mensajeError("Ingrese el código de fabricante.");  
                return;
            }
            
            if(loteFab != null){
                validaOk = true;
            }else{
                validaOk = false;
                JsfUtil.mensajeError("Ingrese el lote de fabricación.");   
                return;
            }
            
            if (municion != null) {
                //validaOk = true; 
                if (!munXEmp.equals("0")) {
                    validaOk = true;
                } else {
                    validaOk = false;
                    JsfUtil.mensajeAdvertencia("Ingresa cantidad de municiones por empaque!");
                    return;
                }
                
                if (!cantEmp.equals("0")) {
                    validaOk = true;
                } else {
                    validaOk = false;
                    JsfUtil.mensajeAdvertencia("Ingresa cantidad de empaque!");
                    return;
                }

//                if(lstDetalleVenta1.isEmpty()){
//                    validaOk = true; 
//                }
//                else
//                {
//                    boolean existeId = false;
//                    for (int i = 0; i < lstDetalleVenta1.size(); i++) {
//                        //Syso(lstDetalleVenta1.get(i).getId() + "==" + idDetalle);
//                        if(lstDetalleVenta1.get(i).getId().equals(idDetalle)){
//                            existeId = true;
//                            break;
//                        }
//                    }
//                    if(existeId){
//                        validaOk = false;
//                        JsfUtil.mensajeAdvertencia("El Artículo de Munición que intenta agregar, ya se encuentra en la tabla.");
//                        return;
//                    }else{
//                        validaOk = true;
//                    }
//                }

                /* validar stock */
                if(direccion != null){ 
                    HashMap mMap = new HashMap();
                    mMap.put("agenteComerId", loginController.getUsuario().getPersonaId());
                    mMap.put("localId", direccion.getId());
                    mMap.put("municionId", municion.getId()); 
                    int numXEmpInt = Integer.valueOf(munXEmp);
                    int cantEmpInt = Integer.valueOf(cantEmp);
                    int cantVendida = numXEmpInt * cantEmpInt;
                    List<ArrayRecord> listStock = gamacAmaMunicionFacade.stockMunicion(mMap);
                    validaOk=true;
                    if(listStock!=null){
                        if(listStock.size()>0){
                            //Syso("listStock: "+ listStock.size());
                            int sumMuniIngresos = Integer.valueOf( listStock.get(0).get("SUM_MUNI_COMPRAS").toString());
                            int sumMuniSalidas = Integer.valueOf( listStock.get(0).get("SUM_MUNI_VENTAS").toString());
                            int stockMuni = sumMuniIngresos - sumMuniSalidas;                    
                            if(cantVendida>stockMuni){
                                validaOk = false;
                                JsfUtil.mensajeAdvertencia("El Artículo que intenta agregar, no cuenta con stock suficiente!");
                            }
                        }else{
                            validaOk = false;
                            JsfUtil.mensajeAdvertencia("El Artículo que intenta agregar, no cuenta con stock suficiente!");

                        }
                    }else{
                        validaOk = false;
                        JsfUtil.mensajeAdvertencia("El Artículo que intenta agregar, no cuenta con stock suficiente!");                    
                    }
                }else{
                    validaOk = false;
                }
                /* */
                
            } else {
                JsfUtil.mensajeAdvertencia("Elige munición!"); 
            }

        }else{
            JsfUtil.mensajeError("Función no adecuada!");
        }
        
        if (validaOk){
            
            detalleVenta1.setId(idDetalle);
            detalleVenta1.setNroRUA(arma.getNroRua());
            detalleVenta1.setArmaId(arma.getId());
            detalleVenta1.setTarjetaPropiedaId(tarjetaPropiedad.getId());
            detalleVenta1.setDatosArma(armaTipo + " " + armaMarca + " " + armaModelo);
            detalleVenta1.setModalidad(tarjetaPropiedad.getModalidadId().getNombre());            
            detalleVenta1.setMunicionId(municion.getId());
            detalleVenta1.setMunicion(municion.getMarcaId().getNombre() + " " + municion.getCalibrearmaId().getNombre());
            int numXEmpInt = Integer.valueOf(munXEmp);
            int cantEmpInt = Integer.valueOf(cantEmp);
            int cantVendida = numXEmpInt * cantEmpInt ;
            detalleVenta1.setCantidadVendida(cantVendida);
            detalleVenta1.setMuniPorEmpaque(numXEmpInt);
            detalleVenta1.setCantidadEmpaques(cantEmpInt);
            detalleVenta1.setLoteFabricacion(loteFab);
            detalleVenta1.setCodFabricante(codFab);          
            
            boolean addMuni = false;
            if(validarSaldo){
                int nuevoSaldo = Integer.valueOf(saldoMesAct) - cantVendida;
                //detalleVenta1.setSaldoMesAnt(Integer.valueOf(saldoMesAnt));
                if(nuevoSaldo < 0){
                    JsfUtil.mensajeAdvertencia("No es posible agregar el artículo. La cantidad a trasladar debe ser como máximo la cantidad autorizada.");
                } else {
                    detalleVenta1.setSaldoMesAct(nuevoSaldo);
                    saldoMesAct = String.valueOf(nuevoSaldo);
                    addMuni = true;
                } 
            } else {
                saldoMesAct = "-";
                addMuni = true;
            }
            
            if(addMuni){
                lstDetalleVenta1.add(detalleVenta1); 
                detalleVenta1 = new GamacDetalleVenta1(); 
                
                //LIMPIAR VARIABLES
                armaTipo = null;
                armaMarca = null;
                armaModelo = null;
                calibre = null;
                saldoMesAct = null;
                tipoLicencia = null;
                municion = null;
                RequestContext.getCurrentInstance().reset("registroForm:codFabPN");
                RequestContext.getCurrentInstance().reset("registroForm:loteFabPN");
                RequestContext.getCurrentInstance().reset("registroForm:munXEmpPN");
                RequestContext.getCurrentInstance().reset("registroForm:cantEmpPN");
                RequestContext.getCurrentInstance().reset("registroForm:nroRuaSerie");
                
                RequestContext.getCurrentInstance().update("registroForm:saldoMesAct");
                RequestContext.getCurrentInstance().update("registroForm:detallePanel1");
            } else {
                //detalleVenta1 = null;
            }
            
            /*detalleTrans.setArticuloMunicionId(municion); 
            detalleTrans.setId(municion.getId()); 
            detalleTrans.setCantidadEmpaques(Integer.valueOf(cantEmpaques)); 
            detalleTrans.setMuniPorEmpaque(Integer.valueOf(cantxEmpaques)); 
            
            lstDetalle1.add(detalleTrans); 
            detalleTrans = new GamacAmaAdmunDetalleTrans(); */
        }
        
    } 
    
    public void eliminarDetalle1Seleccionados(){
        if (!lstDetalleVenta1Selected.isEmpty()) {
            for (GamacDetalleVenta1 ls : lstDetalleVenta1Selected) {
                for (GamacDetalleVenta1 l : lstDetalleVenta1) {
                    if (Objects.equals(ls.getId(), l.getId())) {
                        lstDetalleVenta1.remove(l);
                        break;
                    }
                }
            }
            JsfUtil.mensaje("Artículo(s) Eliminado(s)");
        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar un registro para eliminar");
        }       
    }
    
    public void addMunicionGrilla2(){
        boolean validaOk = false;     
        String source = JsfUtil.getRequestParameter("javax.faces.source");
        String codFab = JsfUtil.getRequestParameter("registroForm:codFabES");        
        String loteFab = JsfUtil.getRequestParameter("registroForm:loteFabES");        
        String munXEmp = JsfUtil.getRequestParameter("registroForm:munXEmpES");
        String cantEmp = JsfUtil.getRequestParameter("registroForm:cantEmpES");
        //String cantMunicion = JsfUtil.getRequestParameter("registroForm:cantMunicionES"); 
        
        String idDetalle = "";
        if(municion!=null){
            idDetalle = municion.getId() + "";            
        }
        
        if(source.equals("registroForm:addArticulo2")){
            //Syso("source: " + source); 
            if(codFab != null){
                validaOk = true;
            }else{
                validaOk = false;
                JsfUtil.mensajeError("Ingrese el código de fabricante.");                
            }
            
            if(loteFab != null){
                validaOk = true;
            }else{
                validaOk = false;
                JsfUtil.mensajeError("Ingrese el lote de fabricación.");                
            }
            
            if (municion != null) {
                if (!munXEmp.equals(0)) {
                    validaOk = true;
                } else {
                    validaOk = false;
                    JsfUtil.mensajeAdvertencia("Ingresa cantidad de municiones por empaque!");
                }
                
                if (!cantEmp.equals(0)) {
                    validaOk = true;
                } else {
                    validaOk = false;
                    JsfUtil.mensajeAdvertencia("Ingresa cantidad de empaque!");
                }

//                if(lstDetalleVenta2.isEmpty()){
//                    validaOk = true; 
//                }
//                else
//                {
//                    boolean existeId = false;
//                    for (int i = 0; i < lstDetalleVenta2.size(); i++) {
//                        //Syso(lstDetalleVenta2.get(i).getId() + "==" + idDetalle);
//                        if(lstDetalleVenta2.get(i).getId().equals(idDetalle)){
//                            existeId = true;
//                            break;
//                        }
//                    }
//                    if(existeId){
//                        validaOk = false;
//                        JsfUtil.mensajeAdvertencia("El Artículo de Munición que intenta agregar, ya se encuentra en la tabla.");
//                    }else{
//                        validaOk = true;
//                    }
//                }
                
            }else{
                validaOk = false;
                JsfUtil.mensajeAdvertencia("Por favor seleccione un artículo de munición y una cantidad para agregar a la tabla de Detalle de Municiones Vendidas.");                
                //JsfUtil.mensajeAdvertencia("Elige munición!");
            }
            
            /* validar stock */
            if(direccion != null){ 
                HashMap mMap = new HashMap();
                mMap.put("agenteComerId", loginController.getUsuario().getPersonaId());
                mMap.put("localId", direccion.getId());
                mMap.put("municionId", municion.getId());
                int numXEmpInt = Integer.valueOf(munXEmp);
                int cantEmpInt = Integer.valueOf(cantEmp);
                int cantVendida = numXEmpInt * cantEmpInt;
                List<ArrayRecord> listStock = gamacAmaMunicionFacade.stockMunicion(mMap);
                validaOk=true;
                if(listStock!=null){
                    if(listStock.size()>0){
                        //Syso("listStock: "+ listStock.size());
                        int sumMuniIngresos = Integer.valueOf( listStock.get(0).get("SUM_MUNI_COMPRAS").toString());
                        int sumMuniSalidas = Integer.valueOf( listStock.get(0).get("SUM_MUNI_VENTAS").toString());
                        int stockMuni = sumMuniIngresos - sumMuniSalidas;                    
                        if(cantVendida>stockMuni){
                            validaOk = false;
                            JsfUtil.mensajeAdvertencia("El Artículo que intenta agregar, no cuenta con stock suficiente!");
                        }
                    }else{
                        validaOk = false;
                        JsfUtil.mensajeAdvertencia("El Artículo que intenta agregar, no cuenta con stock suficiente!");
                        
                    }
                }else{
                    validaOk = false;
                    JsfUtil.mensajeAdvertencia("El Artículo que intenta agregar, no cuenta con stock suficiente!");                    
                }
            }else{
                validaOk = false;
                JsfUtil.mensajeError("Seleccione el local."); 
            }
            /* */
            
        } else {
            JsfUtil.mensajeError("Función no adecuada!");
        }

        if (validaOk){
            
            detalleVenta2.setId(idDetalle);
            detalleVenta2.setCalibreArmaId(municion.getCalibrearmaId().getId());
            detalleVenta2.setCalibreArma(municion.getCalibrearmaId().getNombre());
            detalleVenta2.setMunicionId(municion.getId());
            detalleVenta2.setMunicion(municion.getMarcaId().getNombre() + " " + municion.getCalibrearmaId().getNombre());
            int numXEmpInt = Integer.valueOf(munXEmp);
            int cantEmpInt = Integer.valueOf(cantEmp);
            int cantVendida = numXEmpInt * cantEmpInt ;
            detalleVenta2.setCantidadVendida(cantVendida);
            detalleVenta2.setMuniPorEmpaque(numXEmpInt);
            detalleVenta2.setCantidadEmpaques(cantEmpInt);
            detalleVenta2.setLoteFabricacion(loteFab);
            detalleVenta2.setCodFabricante(codFab);
            
            lstDetalleVenta2.add(detalleVenta2); 
            detalleVenta2 = new GamacDetalleVenta2();
        }
        
    }
        
    public void eliminarDetalle2Seleccionados(){
        if (!lstDetalleVenta2Selected.isEmpty()) {
            //RequestContext.getCurrentInstance().execute("PF('confirmDlgGlobal').show();");
            for (GamacDetalleVenta2 ls : lstDetalleVenta2Selected) {
                for (GamacDetalleVenta2 l : lstDetalleVenta2) {
                    if (Objects.equals(ls.getId(), l.getId())) {
                        lstDetalleVenta2.remove(l);
                        break;
                    }
                }
            }
            JsfUtil.mensaje("Artículo(s) Eliminado(s)");
        } else {
            JsfUtil.mensajeAdvertencia("Debe seleccionar un registro para eliminar");
        }       
        
    }
    
    public void guardarOpcion1(){
        String source = JsfUtil.getRequestParameter("javax.faces.source");
        //Syso("source: " + source);
        
        if(source.equals("registroForm:btnGuardar")){
            if(validarFormRV()){
                GamacAmaAdmunTransaccion regVenta = new GamacAmaAdmunTransaccion();
                regVenta.setTipoTransaccionId(gamacTipoGamacFacade.tipoGamacXCodProg("TP_TRANS_VTA"));
                regVenta.setFechatransaccion(fechaVenta);
                regVenta.setAgentecomerId(gamacSbPersonaFacade.find(loginController.getUsuario().getPersonaId()));
                GamacTipoGamac tipComprador = new GamacTipoGamac();
                switch (tipoComprador){
                    case 1:
                        tipComprador = gamacTipoGamacFacade.tipoGamacXCodProg("TP_CLTE_PNPNPFA");
                        break;
                    case 3:
                        tipComprador = gamacTipoGamacFacade.tipoGamacXCodProg("TP_CLTE_EMPSEG");
                        regVenta.setRepresentanteId(representante);
                        break;
                    case 4:
                        tipComprador = gamacTipoGamacFacade.tipoGamacXCodProg("TP_CLTE_EMPENTR");
                        regVenta.setRepresentanteId(representante);
                        regVenta.setDirDestinoId(destino);
                        break;
                }
                regVenta.setTipoclienteproveedorId(tipComprador);
                regVenta.setClienteproveedorId(cliente);
                regVenta.setLocalcomercialId(direccion);
                if(tarjetaPropiedad != null){
                    if(tarjetaPropiedad.getLicenciaId() != null){
                        regVenta.setLicenciaId(tarjetaPropiedad.getLicenciaId());
                    }                    
                }
                regVenta.setTipodocTransacId(tipoComprobante);
                regVenta.setNumdocTransac(numDocVenta);
                regVenta.setCierreInventario((short) 1);
                regVenta.setActivo((short) 1);
                regVenta.setAudLogin(loginController.getUsuario().getLogin());
                regVenta.setAudNumIp(JsfUtil.getIpAddress());
                regVenta.setNroConsultaRENIEC(nroConsultaRENIEC);

                switch (tipoComprador){
                    case 1:
                    case 3:
                        for (GamacDetalleVenta1 row : lstDetalleVenta1) {
                            detalleTrans = new GamacAmaAdmunDetalleTrans();
                            detalleTrans.setTransaccionId(regVenta);
                            detalleTrans.setArticuloMunicionId(gamacAmaMunicionFacade.find(row.getMunicionId()));
                            detalleTrans.setTarjetaPropiedadId(gamacAmaTarjetaPropiedadFacade.find(row.getTarjetaPropiedaId()));                            
                            detalleTrans.setCantidadMuniciones(row.getCantidadVendida());
                            detalleTrans.setActivo((short) 1);
                            detalleTrans.setAudLogin(loginController.getUsuario().getLogin());
                            detalleTrans.setAudNumIp(JsfUtil.getIpAddress());
                            
                            detalleTrans.setCantidadEmpaques(row.getCantidadEmpaques());
                            detalleTrans.setMuniPorEmpaque(row.getMuniPorEmpaque());
                            detalleTrans.setLoteFabricacion(row.getLoteFabricacion());
                            detalleTrans.setCodigoFabricante(row.getCodFabricante());
                            detalleTrans.setNroRua(row.getNroRUA());
                            
                            lstDetalle1.add(detalleTrans);
                        }
                        regVenta.setGamacAmaAdmunDetalleTransCollection(lstDetalle1);
                        break;
                    case 4:
                        for (GamacDetalleVenta2 row : lstDetalleVenta2) {
                            detalleTrans = new GamacAmaAdmunDetalleTrans();
                            detalleTrans.setTransaccionId(regVenta);
                            detalleTrans.setArticuloMunicionId(gamacAmaMunicionFacade.find(row.getMunicionId()));
                            detalleTrans.setCantidadMuniciones(row.getCantidadVendida());
                            detalleTrans.setActivo((short) 1);
                            detalleTrans.setAudLogin(loginController.getUsuario().getLogin());
                            detalleTrans.setAudNumIp(JsfUtil.getIpAddress());
                            
                            detalleTrans.setCantidadEmpaques(row.getCantidadEmpaques());
                            detalleTrans.setMuniPorEmpaque(row.getMuniPorEmpaque());
                            detalleTrans.setLoteFabricacion(row.getLoteFabricacion());
                            detalleTrans.setCodigoFabricante(row.getCodFabricante());
                            
                            lstDetalle2.add(detalleTrans);
                        }
                        regVenta.setGamacAmaAdmunDetalleTransCollection(lstDetalle2);
                        break;
                }
                
                try {
                    regVenta = (GamacAmaAdmunTransaccion) JsfUtil.entidadMayusculas(regVenta, "");
                    gamacAmaAdmunTransaccionFacade.create(regVenta);
                    JsfUtil.mensaje("Se registró la venta de municiones correctamente, Id Nro. " + regVenta.getId() + "!!");

                    RequestContext.getCurrentInstance().execute("$(\"div[id*='botonesPanel']\").hide(\"fast\")"); 
                    RequestContext.getCurrentInstance().execute("$(\"div[id*='botonesSec']\").show(\"fast\")"); 
                    RequestContext.getCurrentInstance().execute("PF('wvAddArticulo1').disable();"); 
                    RequestContext.getCurrentInstance().execute("PF('wvDelArticulo1').disable();"); 
                    
                    //Thread.sleep(5000);
                    /*ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();                    
                    reiniciarValoresRegistro();
                    ec.redirect(ec.getRequestContextPath() + "/faces/aplicacion/gamac/municiones/registrarVenta/RegistrarVenta.xhtml");*/
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(GamacMunicionesRegistrarVentaController.class.getName()).log(Level.SEVERE, null, ex);
                    JsfUtil.mensajeError("NO se pudo guardar registro de venta!!"); 
                }
                
            }
            
        } else {
            JsfUtil.mensajeError("Función no adecuada!");
        }
        
    }
    
    private boolean validarFormRV() {
        boolean valida = true;
        
        if(direccion == null){
            valida = false;
            JsfUtil.mensajeError("Seleccione el local.");                
        }
        
        switch (tipoComprador) {
            case 1:
                if(lstDetalleVenta1.size()<=0){
                    valida = false;
                    JsfUtil.mensajeError("Por favor, ingrese al menos 01 ítem en el detalle.");
                }
                break;
            case 3:
                if(lstDetalleVenta1.size()<=0){
                    valida = false;
                    JsfUtil.mensajeError("Por favor, ingrese al menos 01 ítem en el detalle.");
                }
                break;                    
            case 4:
                if(lstDetalleVenta2.size()<=0){
                    valida = false;
                    JsfUtil.mensajeError("Por favor, ingrese al menos 01 ítem en el detalle.");
                }

                if(destino==null){
                    valida = false;
                    JsfUtil.mensajeError("Por favor, elige una dirección de destino."); 
                }
                break;
        }
        
        //# Habilitar para uso de Lector Biométrico#//
        /*if(!isVenValidBio()){
            valida = false;
            JsfUtil.mensajeError("Falta realizar la verificación biométrica!.");            
        }*/
        
        return valida;
    }
    
    public void cancelarRV(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            reiniciarValoresRegistro();
            ec.redirect(ec.getRequestContextPath() + "/faces/aplicacion/gamac/municiones/registrarVenta/RegistrarVenta.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(GamacMunicionesRegistrarVentaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public List<GamacWsLicencias> autcompletePersonalPnpFa(String query) {
        try {
            List<GamacWsLicencias> listLicencia = gamacWsLicenciasFacade.listPersonaPnpFa(query);
            return listLicencia;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void eligePersonaPnpFa(SelectEvent event) {
        //personaPnpFa = (GamacWsLicencias) event.getObject();
        //licenciaEstado = personaPnpFa.getEstado();
        
        wsLicencia = (GamacWsLicencias) event.getObject();
        licenciaEstado = wsLicencia.getEstado();

        RequestContext.getCurrentInstance().update("registroForm:estadoLicencia");
    }

    public GamacAmaLicenciaDeUso getGamacAmaLicenciaDeUso(java.lang.Long id) {
        return gamacAmaLicenciaDeUsoFacade.find(id);
    }

    public List<ArrayRecord> autoCompleteUbigeo(String filtroUbigeo) {
        List<ArrayRecord> distritos = gamacSbDireccionFacade.findUbigeo(filtroUbigeo);
        //("distritos encontrados: " + distritos.size());
        return distritos;
    }

    public List<GamacSbDireccion> listDire() {
        String id = String.valueOf(loginController.getUsuario().getPersonaId());
        //Syso("ID persona: " + id);
        return gamacSbDireccionFacade.listarDirecionPorPersonaS(id);
    }

    public void eligeUbigeo(SelectEvent event) {
        //("element: " + event.getComponent().getClientId );
        idsUbigeo = (String) event.getObject().toString();
        Map<String, String> parameterMap = (Map<String, String>) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String txtUbi = parameterMap.get("newInicioCursoForm:ubigeo_input");
        //JsfUtil.mensaje("Seleccionaste ubigeo: " + txtUbi);
    }
    
    public void showCreaDireccion(){
        direZona = null;
        direVia = null;
        RequestContext.getCurrentInstance().reset("newDireccionForm");
        
    }
    
    public void cancelCreaDirecion(){
        RequestContext.getCurrentInstance().execute("PF('newDireccionDialog').hide()");
    }
    
    /*public void creaDireccion(){
        String source = JsfUtil.getRequestParameter("javax.faces.source");
        //Syso("source: " + source);
        
        if(source.equals("newDireccionForm:btnSaveDire")){
            
            String dire = JsfUtil.getRequestParameter("newDireccionForm:direccion"); 
            String numero = JsfUtil.getRequestParameter("newDireccionForm:numero"); 
            String geoLat = JsfUtil.getRequestParameter("newDireccionForm:latitud"); 
            String geoLong = JsfUtil.getRequestParameter("newDireccionForm:longitud"); 
            String[] idUbi = idsUbigeo.split(","); 
            GamacSbDistrito distrito = gamacSbDistritoFacade.find(Long.valueOf(idUbi[2]));
            GamacTipoBase tipoDire = gamacTipoBaseFacade.tipoBaseXCodProg("TP_DIRECB_FIS");
            
            GamacSbDireccion newDire = new GamacSbDireccion();
            newDire.setTipoId(tipoDire);
            newDire.setDistritoId(distrito);
            newDire.setPersonaId(cliente);
            newDire.setZonaId(direZona);
            newDire.setViaId(direVia);
            newDire.setAudLogin(loginController.getUsuario().getLogin());
            newDire.setAudNumIp(JsfUtil.getIpAddress());
            newDire.setDireccion(dire);
            newDire.setNumero(numero);
            newDire.setActivo((short) 1);
            if(!geoLat.equals("")){
                newDire.setGeoLat(Double.valueOf(geoLat));                
            }
            if(!geoLong.equals("")){
                newDire.setGeoLong(Double.valueOf(geoLong));
            }

            try {
                newDire = (GamacSbDireccion) JsfUtil.entidadMayusculas(newDire, "");
                gamacSbDireccionFacade.create(newDire);

                lstDestinos = gamacSbDireccionFacade.listarDirecionPorPersonaS(rucComprador);
                destino = newDire;
                RequestContext.getCurrentInstance().update("registroForm:destino");
                RequestContext.getCurrentInstance().reset("newDireccionForm");
                
                JsfUtil.mensaje("Se registró la Dirección correctamente!. Id Nro. " + newDire.getId());
            } catch (Exception e) {
                JsfUtil.mensajeError("NO se pudo guardar dirección!"); 
            }
            
        }
        
    }*/
    
    @FacesConverter(forClass = GamacAmaLicenciaDeUso.class)
    public static class GamacAmaLicenciaDeUsoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GamacMunicionesRegistrarVentaController controller = (GamacMunicionesRegistrarVentaController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "gamacMunicionesRegistrarVentaController");
            return controller.getGamacAmaLicenciaDeUso(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GamacAmaLicenciaDeUso) {
                GamacAmaLicenciaDeUso o = (GamacAmaLicenciaDeUso) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + GamacAmaLicenciaDeUso.class.getName());
            }
        }

    }

    public GamacWsLicencias getWsLicencias(java.lang.String id) {
        return gamacWsLicenciasFacade.find(id);
    }

    @FacesConverter(forClass = GamacWsLicencias.class)
    public static class GamacWsLicenciasControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GamacMunicionesRegistrarVentaController controller = (GamacMunicionesRegistrarVentaController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "gamacMunicionesRegistrarVentaController");
            return controller.getWsLicencias(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GamacWsLicencias) {
                GamacWsLicencias o = (GamacWsLicencias) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + GamacWsLicencias.class.getName());
            }
        }

    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public GamacAmaMunicion getGamacAmaMunicion(Long id) {
        return gamacAmaMunicionFacade.find(id);
    }
    
    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = GamacAmaMunicion.class)
    public static class gamacAmaMunicionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            
            GamacAmaMunicionController c = (GamacAmaMunicionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gamacAmaMunicionController");
            return c.getGamacAmaMunicion(getKey(value));
            
        }
        
        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GamacAmaMunicion) {
                GamacAmaMunicion o = (GamacAmaMunicion) object;
                String r="";
                if(o.getId()!=null){
                    r=o.getId().toString();
                }
                return r;
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + GamacAmaMunicion.class.getName());
            }
        }
    }

    public GamacAmaCatalogo getGamacAmaCatalogo(Long id) {
        return gamacAmaCatalogoFacade.find(id);
    }

    @FacesConverter(forClass = GamacAmaCatalogo.class)
    public static class gamacAmaCatalogoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GamacAmaCatalogoController c = (GamacAmaCatalogoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gamacAmaCatalogoController");
            return c.getGamacAmaCatalogo(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof GamacAmaCatalogo) {
                GamacAmaCatalogo o = (GamacAmaCatalogo) object;
                String r="";
                if(o.getId()!=null){
                    r=o.getId().toString();
                }
                return r;
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + GamacAmaCatalogo.class.getName());
            }
        }

    }
    
}
