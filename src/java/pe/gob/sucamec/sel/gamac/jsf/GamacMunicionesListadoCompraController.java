/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.jsf;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import pe.gob.sucamec.sel.gamac.data.GamacAmaAdmunDetalleTrans;
import pe.gob.sucamec.sel.gamac.data.GamacAmaAdmunDevolucion;
import pe.gob.sucamec.sel.gamac.data.GamacAmaAdmunTransaccion;
import pe.gob.sucamec.sel.gamac.data.GamacAmaAdqmunPresenta;
import pe.gob.sucamec.sel.gamac.data.GamacAmaArma;
import pe.gob.sucamec.sel.gamac.data.GamacAmaCatalogo;
import pe.gob.sucamec.sel.gamac.data.GamacAmaLicenciaDeUso;
import pe.gob.sucamec.sel.gamac.data.GamacAmaMunicion;
import pe.gob.sucamec.sel.gamac.data.GamacAmaTarjetaPropiedad;
import pe.gob.sucamec.sel.gamac.data.GamacDetalleDev;
import pe.gob.sucamec.sel.gamac.data.GamacDetalleVenta2;
import pe.gob.sucamec.sel.gamac.data.GamacListaCompra;
import pe.gob.sucamec.sel.gamac.data.GamacResolucion;
import pe.gob.sucamec.sel.gamac.data.GamacSbDireccion;
import pe.gob.sucamec.sel.gamac.data.GamacSbPersona;
import pe.gob.sucamec.sel.gamac.data.GamacTipoGamac;
import pe.gob.sucamec.sel.gamac.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;
import pe.gob.sucamec.bdintegrado.bean.AmaResolucionFacade;
import pe.gob.sucamec.bdintegrado.data.AmaResolucion;

/**
 *
 * @author rarevalo
 */
@Named("gamacMunicionesListadoCompraController")
@SessionScoped
public class GamacMunicionesListadoCompraController implements Serializable {
    
    /**
     * Variables de búsqueda
     */
    //private String findMes;
    //private String findAnio;
    private Date findFechaVenta = new Date();
    private String tipoFiltro;
    private String filtro;
    private GamacSbDireccion findLocal;    
    private List<GamacListaCompra> lstCompras;
    
    /**
     * Variables de Registro de Compra
     */
    private List<GamacSbDireccion> lstLocales;
    private GamacSbDireccion direccion; 
    private int tipoComprador;
    private List<GamacTipoGamac> lstTipoDoc;
    private GamacTipoGamac tipoComprobante;
    private Date fechaCompra = new Date();
    private GamacSbPersona vendedor;
    private String vendedorNroRD;
    private String vendedorFecVen;
    //private GamacSbPersona representante;
    private String dniPerNat;
    private String dniVendedor;
    private String rucVendedor;
    //private List<GamacSbPersona> lstRepresentantes;
    //private String dniRepresentante; 
    private String bioDni;
    private String bioNombre;
    private List<GamacAmaTarjetaPropiedad> lstRuaSerie;
    private List<GamacAmaCatalogo> lstCalibres; 
    private List<GamacAmaMunicion> lstMuniciones; 
    private GamacAmaArma arma;
    private String armaTipo;
    private String armaMarca;
    private String armaModelo;    
    private String numDocVenta;    
    private GamacAmaMunicion municion;
    private GamacAmaCatalogo calibre;
    //private List<GamacAmaAdmunDetalleTrans> lstDetalle2; 
    private List<GamacDetalleVenta2> lstDetalleVenta2; 
    private List<GamacDetalleVenta2> lstDetalleVenta2Selected;    
    //private GamacAmaAdmunDetalleTrans detalleTrans; 
    private GamacDetalleVenta2 detalleVenta2;    
    private String saveOk; 
    
    /**
     * Variables de vista
     */
    private String selectIdCompra;
    private GamacAmaAdmunTransaccion selectedCompra;
    
    /**
     * Variables para devolucion
     */
    private Date devFecha = new Date();
    private GamacSbPersona devRepresentante;
    private List<GamacSbPersona> devLstRepresentantes;
    private List<GamacDetalleDev> lstDetalleDev1; 
    private List<GamacAmaAdmunDetalleTrans> lstDetalleDev2; 
    private boolean todoEmpaques;
    //private boolean devValidBio;
    private String motivoDevolucion;
    
    private List<GamacResolucion> resoList = new ArrayList(); 
    private GamacResolucion selectedRD;
    
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacTipoGamacFacade gamacTipoGamacFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacSbDireccionFacade gamacSbDireccionFacade;    
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaAdmunTransaccionFacade gamacAmaAdmunTransaccionFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaAdmunDevolucionFacade gamacAmaAdmunDevolucionFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaTarjetaPropiedadFacade gamacAmaTarjetaPropiedadFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacSbPersonaFacade gamacSbPersonaFacade;    
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaMunicionFacade gamacAmaMunicionFacade;    
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaAdqmunPresentaFacade gamacAmaAdqmunPresentaFacade;    
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacRma1369Facade gamacRma1369Facade;   
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaLicenciaDeUsoFacade gamacAmaLicenciaDeUsoFacade;
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

    public GamacTipoGamac getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(GamacTipoGamac tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }
    
    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }    

    /*public String getFindMes() {
        return findMes;
    }

    public void setFindMes(String findMes) {
        this.findMes = findMes;
    }

    public String getFindAnio() {
        return findAnio;
    }

    public void setFindAnio(String findAnio) {
        this.findAnio = findAnio;
    }*/

    public Date getFindFechaVenta() {
        return findFechaVenta;
    }

    public void setFindFechaVenta(Date findFechaVenta) {
        Logger.getLogger(GamacMunicionesListadoCompraController.class.getName()).log(Level.INFO, "setFindFechaVenta: ", findFechaVenta);
        this.findFechaVenta = findFechaVenta;
    }

    public String getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(String tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public GamacSbDireccion getFindLocal() {
        return findLocal;
    }

    public void setFindLocal(GamacSbDireccion findLocal) {
        this.findLocal = findLocal;
    }

    public List<GamacListaCompra> getLstCompras() {
        return lstCompras;
    }

    public void setLstCompras(List<GamacListaCompra> lstCompras) {
        this.lstCompras = lstCompras;
    }

    public GamacSbPersona getVendedor() {
        return vendedor;
    }

    public void setVendedor(GamacSbPersona vendedor) {
        this.vendedor = vendedor;
    }

    public String getVendedorNroRD() {
        return vendedorNroRD;
    }

    public void setVendedorNroRD(String vendedorNroRD) {
        this.vendedorNroRD = vendedorNroRD;
    }

    public String getVendedorFecVen() {
        return vendedorFecVen;
    }

    public void setVendedorFecVen(String vendedorFecVen) {
        this.vendedorFecVen = vendedorFecVen;
    }

    /*public GamacSbPersona getRepresentante() {
        return representante;
    }

    public void setRepresentante(GamacSbPersona representante) {
        this.representante = representante;
    }*/

    public String getDniPerNat() {
        return dniPerNat;
    }

    public void setDniPerNat(String dniPerNat) {
        this.dniPerNat = dniPerNat;
    }

    public String getDniVendedor() {
        return dniVendedor;
    }

    public void setDniVendedor(String dniVendedor) {
        this.dniVendedor = dniVendedor;
    }

    public String getRucVendedor() {
        return rucVendedor;
    }

    public void setRucVendedor(String rucVendedor) {
        this.rucVendedor = rucVendedor;
    }

    /*public List<GamacSbPersona> getLstRepresentantes() {
        return lstRepresentantes;
    }

    public void setLstRepresentantes(List<GamacSbPersona> lstRepresentantes) {
        this.lstRepresentantes = lstRepresentantes;
    }*/

    /*public String getDniRepresentante() {
        return dniRepresentante;
    }

    public void setDniRepresentante(String dniRepresentante) {
        this.dniRepresentante = dniRepresentante;
    }*/

    public String getBioDni() {
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

    public List<GamacAmaTarjetaPropiedad> getLstRuaSerie() {
        return lstRuaSerie;
    }

    public void setLstRuaSerie(List<GamacAmaTarjetaPropiedad> lstRuaSerie) {
        this.lstRuaSerie = lstRuaSerie;
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

    public GamacAmaArma getArma() {
        return arma;
    }

    public void setArma(GamacAmaArma arma) {
        this.arma = arma;
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

    public String getNumDocVenta() {
        return numDocVenta;
    }

    public void setNumDocVenta(String numDocVenta) {
        this.numDocVenta = numDocVenta;
    }

    public String getArmaTipo() {
        return armaTipo;
    }

    public void setArmaTipo(String armaTipo) {
        this.armaTipo = armaTipo;
    }

    /*public List<GamacAmaAdmunDetalleTrans> getLstDetalle2() {
        return lstDetalle2;
    }

    public void setLstDetalle2(List<GamacAmaAdmunDetalleTrans> lstDetalle2) {
        this.lstDetalle2 = lstDetalle2;
    }*/

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

    /*public GamacAmaAdmunDetalleTrans getDetalleTrans() {
        return detalleTrans;
    }

    public void setDetalleTrans(GamacAmaAdmunDetalleTrans detalleTrans) {
        this.detalleTrans = detalleTrans;
    }*/

    public GamacDetalleVenta2 getDetalleVenta2() {
        return detalleVenta2;
    }

    public void setDetalleVenta2(GamacDetalleVenta2 detalleVenta2) {
        this.detalleVenta2 = detalleVenta2;
    }

    public String getSelectIdCompra() {
        return selectIdCompra;
    }

    public void setSelectIdCompra(String selectIdCompra) {
        this.selectIdCompra = selectIdCompra;
    }

    public GamacAmaAdmunTransaccion getSelectedCompra() {
        return selectedCompra;
    }

    public void setSelectedCompra(GamacAmaAdmunTransaccion selectedCompra) {
        this.selectedCompra = selectedCompra;
    }

    public String getSaveOk() {
        return saveOk;
    }

    public void setSaveOk(String saveOk) {
        this.saveOk = saveOk;
    }

    public Date getDevFecha() {
        return devFecha;
    }

    public void setDevFecha(Date devFecha) {
        this.devFecha = devFecha;
    }

    public GamacSbPersona getDevRepresentante() {
        return devRepresentante;
    }

    public void setDevRepresentante(GamacSbPersona devRepresentante) {
        this.devRepresentante = devRepresentante;
    }

    public List<GamacSbPersona> getDevLstRepresentantes() {
        return devLstRepresentantes;
    }

    public void setDevLstRepresentantes(List<GamacSbPersona> devLstRepresentantes) {
        this.devLstRepresentantes = devLstRepresentantes;
    }

    public List<GamacDetalleDev> getLstDetalleDev1() {
        return lstDetalleDev1;
    }

    public void setLstDetalleDev1(List<GamacDetalleDev> lstDetalleDev1) {
        this.lstDetalleDev1 = lstDetalleDev1;
    }

    public List<GamacAmaAdmunDetalleTrans> getLstDetalleDev2() {
        return lstDetalleDev2;
    }

    public void setLstDetalleDev2(List<GamacAmaAdmunDetalleTrans> lstDetalleDev2) {
        this.lstDetalleDev2 = lstDetalleDev2;
    }

    public boolean isTodoEmpaques() {
        return todoEmpaques;
    }

    public void setTodoEmpaques(boolean todoEmpaques) {
        this.todoEmpaques = todoEmpaques;
    }

//    public boolean isDevValidBio() {
//        return devValidBio;
//    }
//
//    public void setDevValidBio(boolean devValidBio) {
//        this.devValidBio = devValidBio;
//    }

    public String getMotivoDevolucion() {
        return motivoDevolucion;
    }

    public void setMotivoDevolucion(String motivoDevolucion) {
        this.motivoDevolucion = motivoDevolucion;
    }

    public List<GamacResolucion> getResoList() {
        return resoList;
    }

    public void setResoList(List<GamacResolucion> resoList) {
        this.resoList = resoList;
    }

    public GamacResolucion getSelectedRD() {
        return selectedRD;
    }

    public void setSelectedRD(GamacResolucion selectedRD) {
        this.selectedRD = selectedRD;
    }
    
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
    public String prepareListado() {
        String res = "";
        if (verificarAutorizacionUsuario()) {
            reiniciarValoresBusqueda();
            res = "/aplicacion/gamac/municiones/listadoCompras/listCompras";
        } else {
            JsfUtil.mensajeAdvertencia(mensajeValidacion());
        }
        return res;
        
    }
    
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
    
    private void reiniciarValoresBusqueda() {
        direccion = null;
        lstCompras = null;
        tipoFiltro = "";
        filtro = "";
        /*findMes = "";
        findAnio = "";*/
        findFechaVenta = new Date();
        findLocal = null;
    }
    
    public void buscar() {
        List<ArrayRecord> listado;
        lstCompras = new ArrayList<GamacListaCompra>();
        boolean goBuscar = false;

        HashMap mMap = new HashMap();
        mMap.put("agenteComerId", loginController.getUsuario().getPersonaId());
        //mMap.put("periodo", findAnio + findMes);
        mMap.put("tipoFiltro", tipoFiltro);
        
        if (tipoFiltro != null) {
            
            switch (tipoFiltro) {
                case "1":
                case "2":
                    if (!((filtro == null || filtro.isEmpty()))) {
                        if (filtro != null) {
                            filtro = ((filtro.length() == 0) ? null : filtro.trim());
                        }
                        mMap.put("filtro", (filtro != null) ? filtro.trim().toUpperCase() : filtro);
                        goBuscar = true;                        
                    } else {
                        JsfUtil.mensajeAdvertencia("Por favor escriba el criterio de búsqueda");
                    }
                    break;
                case "3":
                    if (findFechaVenta != null) {
                        mMap.put("findFechaVenta", findFechaVenta);
                        goBuscar = true;
                    } else {
                        JsfUtil.mensajeAdvertencia("Por favor ingrese la fecha de compra");                        
                    }
                    break;
                case "4":
                    if (findLocal != null) {
                        mMap.put("findLocal", findLocal.getId());
                        goBuscar = true;
                    } else {
                        JsfUtil.mensajeAdvertencia("Por favor eliga el local");                        
                    }
                    break;
            }

        } else {
            //goBuscar = true;
            JsfUtil.mensajeAdvertencia("Por favor seleccione un criterio de búsqueda");
        }

        if (goBuscar) {
            listado = gamacAmaAdmunTransaccionFacade.buscarBandejaListaCompras(mMap);
            if (listado != null) {
                for (int i = 0; i < listado.size(); i++) {
                    GamacListaCompra row = new GamacListaCompra();

                    row.setId(listado.get(i).get("ID").toString() + "," + i);
                    row.setDireccion(listado.get(i).get("DIRECCION").toString());
                    row.setTipoDoc(listado.get(i).get("TIPO_DOC").toString());
                    row.setNroDoc(listado.get(i).get("NRO_DOC").toString());
                    row.setVendedorNombre(listado.get(i).get("VENDEDOR_NOMBRE").toString());
                    row.setTipoVendedor(listado.get(i).get("TIPO_VENDEDOR").toString());
                    row.setTipoAutorizacion(listado.get(i).get("TIPO_AUTORIZACION").toString());
                    row.setNroAutorizacion(listado.get(i).get("NRO_AUTORIZACION").toString());
                    row.setFechaCompra((Date) listado.get(i).get("FECHA_COMPRA"));
                    if (listado.get(i).get("TOTAL_MUNICIONES") != null) {
                        row.setTotalMuniciones(Integer.valueOf(listado.get(i).get("TOTAL_MUNICIONES").toString()));
                    } else {
                        row.setTotalMuniciones(0);
                    }
                    row.setIsCancelable(validarIsCancelable(row.getFechaCompra()));
                    row.setResponsable(listado.get(i).get("NOMBRE_RESPONSABLE").toString());
                    row.setTipoTransaccion(listado.get(i).get("TIPO_TRANSACCION").toString());
                    row.setCierreInventario(Integer.valueOf(listado.get(i).get("CIERRE_INVENTARIO").toString()));
                    
                    
                    /*row.setTipoVendedorCod(listado.get(i).get("TIPO_COMPRADOR_COD").toString());
                    row.setDireccionId(Long.valueOf(listado.get(i).get("LOCALCOMERCIAL_ID").toString()));
                    row.setPersonaId(Long.valueOf(listado.get(i).get("PERSONA_ID").toString()));
                    row.setTipoVendedorId(Long.valueOf(listado.get(i).get("TIPO_COMPRADOR_ID").toString()));
                    row.setTipoVendedor(listado.get(i).get("TIPO_COMPRADOR").toString());
                    row.setTipoAutorizacion(listado.get(i).get("TIPO_AUTORIZACION").toString());
                    if (listado.get(i).get("CANTIDAD_EMPAQUES") != null) {
                        row.setTotalEmpaques(Integer.valueOf(listado.get(i).get("CANTIDAD_EMPAQUES").toString()));
                        //row.setMunixEmpaques(Integer.valueOf(listado.get(i).get("MUNI_POR_EMPAQUE").toString()));
                        row.setTotalMuniciones(Integer.valueOf(listado.get(i).get("TOTAL_MUNICIONES").toString()));
                    } else {
                        row.setTotalEmpaques(0);
                        //row.setMunixEmpaques(Integer.valueOf(listado.get(i).get("MUNI_POR_EMPAQUE").toString()));
                        row.setTotalMuniciones(0);
                    }
                    row.setResponsable(listado.get(i).get("AUD_LOGIN").toString());             
                    row.setActivo(Integer.valueOf(listado.get(i).get("ACTIVO").toString()));
                    row.setEstado(listado.get(i).get("ESTADO").toString());*/

                    lstCompras.add(i, row);
                }
            }
        } else {
            RequestContext req = RequestContext.getCurrentInstance();
            //req.update("buscarForm:tipoFiltro");                
        }
        
    }
    
    public boolean validarIsCancelable(Date fechaCompra) {
        Date hoy = new Date();
        Calendar calCompra = Calendar.getInstance();
        Calendar calHoy = Calendar.getInstance();
        calCompra.setTime(fechaCompra);
        calHoy.setTime(hoy);

        int numberOfDays = 0;
        while (calCompra.before(calHoy)) {
            if ((Calendar.SATURDAY != calCompra.get(Calendar.DAY_OF_WEEK))
               &&(Calendar.SUNDAY != calHoy.get(Calendar.DAY_OF_WEEK))) {
                numberOfDays++;
            }
            calCompra.add(Calendar.DATE,1);
        }
        //System.out.println(numberOfDays);
        if (numberOfDays > 4) {
            return false;
        }
        return true;
    }
    
    public List<GamacSbDireccion> listDire() {
        String id = String.valueOf(loginController.getUsuario().getPersonaId());
        return gamacSbDireccionFacade.listarDirecionPorPersonaS(id);
    }
    
    private void resetRegistroCompra() {
        direccion = null;
        lstTipoDoc = null;
        tipoComprobante = null;
        tipoComprador = 0;
        //lstRepresentantes = null;
        rucVendedor = "";
        //dniRepresentante = "";
        vendedor = null;
        vendedorNroRD = "";
        vendedorFecVen = "";        
        numDocVenta = "";
        bioDni = "";
        bioNombre = "";
        lstRuaSerie = null;        
        armaMarca = "";
        armaModelo = "";
        armaTipo = "";
        lstCalibres = null;
        lstMuniciones = null;
        municion = null;
        //lstDetalle2 = null;
        lstDetalleVenta2 = null;
    }
    
    public void newRegistroCompra(){
        //Syso("PF('newCompraDialog').show()");
        resetRegistroCompra();
        
        //detalleTrans = new GamacAmaAdmunDetalleTrans(); 
        //lstDetalle2 = new ArrayList<GamacAmaAdmunDetalleTrans>(); 
        lstDetalleVenta2 = new ArrayList<GamacDetalleVenta2>();
        
        RequestContext.getCurrentInstance().execute("PF('newCompraDialog').show()");
        RequestContext.getCurrentInstance().reset("newCompraForm"); 
        RequestContext.getCurrentInstance().update("newCompraForm");
    }
    
    public void cancelCreaCompra(){
        RequestContext.getCurrentInstance().execute("PF('newCompraDialog').hide()");
    }
    
    public List<ArrayRecord> autcompletePerJurVen(String query) {
        try {
            List<ArrayRecord> list = gamacAmaTarjetaPropiedadFacade.listPerJurComer(loginController.getUsuario().getPersonaId(), query);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<GamacAmaMunicion> autcompleteMunicionxPerJurCom(String query) {
        try {
            List<GamacAmaMunicion> list = gamacAmaMunicionFacade.obtenerPorMarcaCalibre(query.toUpperCase());
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
 
    public void eligePerJurVen(SelectEvent event) {
        String value = (String) event.getObject().toString();
        //Syso("elegido:" + value);
        String[] ids = value.split(",");
        rucVendedor = ids[1];
        //vendedorNroRD = ids[2];
        //vendedorFecVen = ids[3];
        List<ArrayRecord> list = gamacAmaTarjetaPropiedadFacade.listResoComer(rucVendedor);
        Long id = 0L;
        for (ArrayRecord record : list) {
            GamacResolucion reso = new GamacResolucion();
            id++;
            reso.setId(id);
            reso.setNroRd("" + record.get("NRO_RD"));
            reso.setFechaVencRd((Date) record.get("FEC_VEN"));
            reso.setTipoRd("" + record.get("TIPO_AUTORIZACION"));
            resoList.add(reso);
        }
        vendedor = gamacSbPersonaFacade.find(Long.valueOf(ids[0]));
        //lstRepresentantes = gamacSbPersonaFacade.listxRepresentante(ids[0]);
        //representante = null;
        
        RequestContext.getCurrentInstance().update("newCompraForm:razonSocial");        
        RequestContext.getCurrentInstance().update("newCompraForm:vendedorNroRD");
        RequestContext.getCurrentInstance().update("newCompraForm:vendedorFecVen");        
        //RequestContext.getCurrentInstance().update("newCompraForm:dniRepresentante");
    }
    
    public void eligeResolucion() {
        vendedorNroRD = selectedRD.getNroRd();
        vendedorFecVen = JsfUtil.dateToString(selectedRD.getFechaVencRd(), "dd/MM/yyyy");
        
        RequestContext.getCurrentInstance().update("newCompraForm:vendedorNroRD");
        RequestContext.getCurrentInstance().update("newCompraForm:vendedorFecVen");        
        //RequestContext.getCurrentInstance().update("newCompraForm:dniRepresentante");
    }
    
    //public GamacAmaLicenciaDeUso getGamacAmaLicenciaDeUso(Long id) {
    //    return gamacAmaLicenciaDeUsoFacade.find(id);
    //}
    public GamacResolucion getGamacResolucion(Long id) {
        for(GamacResolucion reso : resoList) {
            if(reso.getId() == id) {
                return reso;
            }
        }        
        return null;
    }
    
    @FacesConverter(forClass = GamacResolucion.class)
    public static class GamacMunicionesListadoCompraControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GamacMunicionesListadoCompraController controller = (GamacMunicionesListadoCompraController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "gamacMunicionesListadoCompraController");
            return controller.getGamacResolucion(getKey(value));
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
            if (object instanceof GamacResolucion) {
                GamacResolucion o = (GamacResolucion) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + GamacResolucion.class.getName());
            }
        }

    }
    
    /*public void eligeRepresentante(SelectEvent event) {
        if (event.getObject() != null) {
            //Syso("Representante: " + event.getObject().toString());
            dniPerNat = event.getObject().toString();
            representante = gamacSbPersonaFacade.find(Long.valueOf(dniPerNat));
            bioDni = representante.getNumDoc();
            bioNombre = representante.getNombres() + " " + representante.getApePat() + " " + representante.getApeMat();

            RequestContext.getCurrentInstance().execute("asignarDniBio('" + bioDni + "','" + bioNombre + "');");
            RequestContext.getCurrentInstance().update("controlBiometricoForm:bioDNI");
            RequestContext.getCurrentInstance().update("controlBiometricoForm:bioNombre");        
            
        }else{
            dniPerNat = "";
            representante = null;
            bioDni = "";
            bioNombre = "";
        }

    }*/
   
    public void eligeMunicionxPerJurCom(SelectEvent event){
        if (event.getObject() != null) {
            municion = (GamacAmaMunicion) event.getObject();
        } else {
        }         
    }
    
    public void addMunicionGrilla(){
        boolean validaOk = false;
        boolean existeId = false;        
        String source = JsfUtil.getRequestParameter("javax.faces.source");
        String codFab = JsfUtil.getRequestParameter("newCompraForm:codFab");        
        String loteFab = JsfUtil.getRequestParameter("newCompraForm:loteFab");        
        String munXEmp = JsfUtil.getRequestParameter("newCompraForm:munXEmp");
        String cantEmp = JsfUtil.getRequestParameter("newCompraForm:cantEmp");
        //String cantMuniciones = JsfUtil.getRequestParameter("newCompraForm:cantMunicion");
        
        String idDetalle = "";
        if(municion!=null){
            idDetalle = municion.getId() + "";            
        }
        
        //Syso("source: " + source);         
        if(source.equals("newCompraForm:addArticulo")){
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

//                if(lstDetalleVenta2.isEmpty()){
//                    validaOk = true; 
//                }
//                else {
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
//                        return;
//                    }else{
//                        validaOk = true;
//                    }
//                }
                
            }else{
                validaOk = false;
                JsfUtil.mensajeAdvertencia("Por favor seleccione un artículo de munición y una cantidad para agregar a la tabla de Detalle de Municiones Compradas.");                
                //JsfUtil.mensajeAdvertencia("Elige munición!");
            }
            
        } else {
            JsfUtil.mensajeError("Función no adecuada!");
        }

        if (validaOk){
            
            detalleVenta2 = new GamacDetalleVenta2();
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

            /*int cantVendida = Integer.valueOf(cantEmpaques) * Integer.valueOf(cantxEmpaques);
            detalleVenta2.setCantidadVendida(cantVendida);*/
            
            lstDetalleVenta2.add(detalleVenta2); 
            detalleVenta2 = new GamacDetalleVenta2();
            
            /* Reseteo de datos de la municion */
            RequestContext.getCurrentInstance().reset("newCompraForm:codFab");
            RequestContext.getCurrentInstance().reset("newCompraForm:loteFab");
            RequestContext.getCurrentInstance().reset("newCompraForm:munXEmp");
            RequestContext.getCurrentInstance().reset("newCompraForm:cantEmp");
            municion = null;
        }
        
    }
        
    public void eliminarDetalleSeleccionados(){
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
    
    public void guardarRC(){
        String source = JsfUtil.getRequestParameter("javax.faces.source");
        
        if(source.equals("newCompraForm:btnGuardarRC")){
                    
            if(validarFormRC()){
                GamacAmaAdmunTransaccion transaccion = new GamacAmaAdmunTransaccion();
                List<GamacAmaAdmunDetalleTrans> lstDetalle2 = new ArrayList<GamacAmaAdmunDetalleTrans>();
                /*ID
                TIPO_TRANSACCION_ID --
                FECHATRANSACCION --
                PERIODO
                AGENTECOMER_ID --
                TIPOCLIENTEPROVEEDOR_ID --
                CLIENTEPROVEEDOR_ID --
                REPRESENTANTE_ID --  revisar que dato se esta guardando
                LOCALCOMERCIAL_ID --
                LICENCIA_ID
                RG_AUTO_CLIPRO --
                RG_AUTOTRANSF
                TIPODOC_TRANSAC_ID --
                NUMDOC_TRANSAC --
                DIR_DESTINO_ID
                ESTADOPRESEN_ID
                CIERRE_INVENTARIO --
                ACTIVO --
                AUD_LOGIN --
                AUD_NUM_IP --  */
                
                transaccion.setTipoTransaccionId(gamacTipoGamacFacade.tipoGamacXCodProg("TP_TRANS_COM"));
                transaccion.setFechatransaccion(fechaCompra);
                transaccion.setAgentecomerId(gamacSbPersonaFacade.find(loginController.getUsuario().getPersonaId()));
                transaccion.setTipoclienteproveedorId(gamacTipoGamacFacade.tipoGamacXCodProg("TP_PROV_AGTECOM"));
                transaccion.setClienteproveedorId(vendedor);
                transaccion.setLocalcomercialId(direccion);
                transaccion.setRgAutoClipro(vendedorNroRD);
                transaccion.setTipodocTransacId(tipoComprobante);
                transaccion.setNumdocTransac(numDocVenta);
                transaccion.setCierreInventario((short) 1);
                transaccion.setActivo((short) 1);
                transaccion.setAudLogin(loginController.getUsuario().getLogin());
                transaccion.setAudNumIp(JsfUtil.getIpAddress());
                
                for (GamacDetalleVenta2 row : lstDetalleVenta2) {
                    
                    /*
                    ID
                    TRANSACCION_ID --
                    ARTICULO_MUNICION_ID --
                    TARJETA_PROPIEDAD_ID
                    CANTIDAD_EMPAQUES --
                    MUNI_POR_EMPAQUE --
                    ACTIVO --
                    AUD_LOGIN --
                    AUD_NUM_IP --
                    */
                    GamacAmaAdmunDetalleTrans detalleTrans = new GamacAmaAdmunDetalleTrans();
                    detalleTrans.setTransaccionId(transaccion);
                    detalleTrans.setArticuloMunicionId(gamacAmaMunicionFacade.find(row.getMunicionId()));
                    detalleTrans.setCantidadEmpaques(row.getCantidadEmpaques());
                    detalleTrans.setMuniPorEmpaque(row.getMuniPorEmpaque());
                    detalleTrans.setCantidadMuniciones(row.getCantidadVendida());
                    detalleTrans.setActivo((short) 1);
                    detalleTrans.setAudLogin(loginController.getUsuario().getLogin());
                    detalleTrans.setAudNumIp(JsfUtil.getIpAddress());                    
                    lstDetalle2.add(detalleTrans);
                }
                
                transaccion.setGamacAmaAdmunDetalleTransCollection(lstDetalle2);
                
                try {
                    transaccion = (GamacAmaAdmunTransaccion) JsfUtil.entidadMayusculas(transaccion, "");
                    gamacAmaAdmunTransaccionFacade.create(transaccion);
                    JsfUtil.mensaje("Se registró la compra de municiones correctamente, Id Nro. " + transaccion.getId() + "!");
                    
                    resetRegistroCompra();
                    reiniciarValoresBusqueda();

                    //detalleTrans = new GamacAmaAdmunDetalleTrans(); 
                    //lstDetalle2 = new ArrayList<GamacAmaAdmunDetalleTrans>(); 
                    lstDetalleVenta2 = new ArrayList<GamacDetalleVenta2>();

                    RequestContext.getCurrentInstance().execute("PF('newCompraDialog').hide()");
                    RequestContext.getCurrentInstance().reset("newCompraForm"); 
                    RequestContext.getCurrentInstance().update("newCompraForm");
                    RequestContext.getCurrentInstance().update("buscarForm");
                    
                } catch (Exception e) {
                    JsfUtil.mensajeError("NO se pudo guardar registro de compra!!"); 
                }
                
            }
            
        } else {
            JsfUtil.mensajeError("Función no adecuada!");
        }
        
    }
    
    private boolean validarFormRC() {
        boolean valida = true;

        if(direccion == null){
            valida = false;
            JsfUtil.mensajeError("Seleccione el local.");                
        }

        if(vendedor == null){
            valida = false;
            JsfUtil.mensajeError("El ruc del vendedor es obligatorio.");
        }
        
        if(lstDetalleVenta2.isEmpty()){
            valida = false;
            JsfUtil.mensajeError("Ingrese al menos 01 ítem en el detalle.");
        }
        
        return valida;
    }    
    
    public void loadVerCompra(){
        selectIdCompra = "";
        resetRegistroCompra();

        try {
            String value = JsfUtil.getRequestParameter("javax.faces.source");                        
            //Syso("value: " + value);
            String[] parts = value.split(":");
            String indiceTable = parts[2];
            selectIdCompra = JsfUtil.getRequestParameter("buscarForm:buscarDatatable:" + indiceTable + ":idCompra");
            String[] ids = selectIdCompra.split(",");
            
            selectedCompra = gamacAmaAdmunTransaccionFacade.find(Long.valueOf(ids[0]));
            //Syso("venta selected: " + selectedCompra.getId());
            
            String tipoVendedor = "0";
            switch (selectedCompra.getTipoclienteproveedorId().getCodProg()){
                case "TP_PROV_AGTECOM":
                    tipoVendedor = "1";
                    break;
            }
            
            List<ArrayRecord> list;
            vendedorFecVen = "";
            if(selectedCompra.getRgAutoClipro()!= null){
                switch (selectedCompra.getTipoclienteproveedorId().getCodProg()){
                    case "TP_PROV_AGTECOM":
                        list = gamacAmaTarjetaPropiedadFacade.listPerJurComerxNroRD(selectedCompra.getRgAutoClipro());
                        if(list != null){
                            if(list.size() > 0){
                                vendedorFecVen = list.get(0).get("FEC_VEN").toString();
                            }
                        }
                        break;
                }
            }                                    

            RequestContext.getCurrentInstance().execute("PF('viewCompraDialog').show()");
            RequestContext.getCurrentInstance().reset("viewCompraForm"); 
            RequestContext.getCurrentInstance().update("viewCompraForm");
            //RequestContext.getCurrentInstance().execute("showViewOptions("+ tipoVendedor +");");            
            
            //RequestContext.getCurrentInstance().execute("lectorBiometrico()");
            
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeAdvertencia("Vuelva a seleccionar Compra!");
        }
    }

    public void closeViewCompra(){
        RequestContext.getCurrentInstance().execute("PF('viewCompraDialog').hide()");        
    }
    
    public void loadAnularCompra(){
        selectIdCompra = "";

        try {
            String value = JsfUtil.getRequestParameter("javax.faces.source");
            //Syso("value: " + value);
            String[] parts = value.split(":");
            String indiceTable = parts[2];
            selectIdCompra = JsfUtil.getRequestParameter("buscarForm:buscarDatatable:" + indiceTable + ":idCompra");
            String[] ids = selectIdCompra.split(",");
            
            if (!lstCompras.isEmpty()) {
                for (GamacListaCompra l : lstCompras) {
                    String[] idl = l.getId().split(",");
                    //Syso(ids[0] + " = " + idl[0]);
                    if (Objects.equals(ids[0], idl[0])) {
                        lstCompras.remove(l);
                        GamacAmaAdmunTransaccion regCompra = gamacAmaAdmunTransaccionFacade.find(Long.valueOf(ids[0]));
                        regCompra.setTipoTransaccionId(gamacTipoGamacFacade.tipoGamacXCodProg("TP_TRANS_ANUCOM"));
                        regCompra.setFechaAnulacion(new Date());
                        regCompra.setCierreInventario((short) 0);
                        gamacAmaAdmunTransaccionFacade.edit(regCompra);
                        break;
                    }
                }
                RequestContext.getCurrentInstance().update("buscarForm:buscarDatatable");
                JsfUtil.mensaje("Registro de Compra Anulado!");
            }       

        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Error de persistencia!");
        }
    }
    
    /*public void presentarRC(){
        String source = JsfUtil.getRequestParameter("javax.faces.source");
        //Syso("source: " + source);
        
        if(source.equals("buscarForm:btnPresentarCom")){
            HashMap mMap = new HashMap();
            mMap.put("agenteComerId", loginController.getUsuario().getPersonaId());
            
            List<GamacAmaAdmunTransaccion> listCompras = gamacAmaAdmunTransaccionFacade.listComprasNuevas(mMap);
            if(listCompras!=null){
                if(listCompras.size()>0){
                    GamacTipoGamac tipo = new GamacTipoGamac();
                    tipo = gamacTipoGamacFacade.tipoGamacXCodProg("TP_ESTPRE_PRE");

                    GamacAmaAdqmunPresenta newPresenta = new GamacAmaAdqmunPresenta();
                    newPresenta.setFechaPresentacion(new Date());
                    newPresenta.setTipoPresentacionId(gamacTipoGamacFacade.tipoGamacXCodProg("TP_PTRANS_COM"));
                    newPresenta.setActivo((short) 1);
                    newPresenta.setAudLogin(loginController.getUsuario().getLogin());
                    newPresenta.setAudNumIp(loginController.getNumIP());
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
                    String periodo = df.format(new Date());
                    newPresenta.setPeriodo(Integer.valueOf(periodo));
                    
                    Iterator<GamacAmaAdmunTransaccion> iteCompras = listCompras.iterator();
                    for (Iterator<GamacAmaAdmunTransaccion> iterator = iteCompras; iteCompras.hasNext();) {
                        GamacAmaAdmunTransaccion row = iterator.next();
                        row.setEstadopresenId(tipo);
                        gamacAmaAdmunTransaccionFacade.edit(row);
                    }
                    newPresenta.setGamacAmaAdmunTransaccionCollection(listCompras);

                    newPresenta = (GamacAmaAdqmunPresenta) JsfUtil.entidadMayusculas(newPresenta, "");
                    gamacAmaAdqmunPresentaFacade.create(newPresenta);

                    JsfUtil.mensaje("Se presentaron todas la compras creadas!");
                    
                    if(!lstCompras.isEmpty()){
                        for(GamacListaCompra row: lstCompras){
                            row.setEstado("TP_ESTPRE_PRE");
                        }
                        RequestContext.getCurrentInstance().update("buscarForm:buscarDatatable");                        
                    }
                }else{
                    JsfUtil.mensajeAdvertencia("No hay compras para presentar a SUCAMEC");
                }
            }else{
                JsfUtil.mensajeAdvertencia("No hay compras para presentar a SUCAMEC");                
            }
        } else {
            JsfUtil.mensajeError("Función no adecuada!");
        }
        
    }*/

//    public void confirmarHuellaDev() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        Map map = context.getExternalContext().getRequestParameterMap();
//        String id = (String) map.get("id");
//        String mensaje = (String) map.get("mensaje");
//        String txtMsg = "El DNI ingresado corresponde con los registros de la huella dactilar ingresada!";
//        JsfUtil.addSuccessMessage(txtMsg);
////        devValidBio = true;        
//        
//        try{    
//            Thread.sleep(2000);
//            RequestContext.getCurrentInstance().execute("PF('controlBiometricoDialog').hide()");
//            Thread.sleep(1000);
//            RequestContext.getCurrentInstance().execute("confirmaDevBio('La HUELLA CORRESPONDE AL DNI!')");
//        }catch(InterruptedException e){
//            e.printStackTrace();
//        }
//    }
//    
//    public void fallaHuella() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        Map map = context.getExternalContext().getRequestParameterMap();
//        //String id = (String) map.get("id");
//        String mensaje = (String) map.get("mensaje");
//        JsfUtil.mensajeAdvertencia("Error en consulta Biométrica: " + mensaje);
//    }
    
    private void resetDevolCompra() {
        devLstRepresentantes = null;
        devRepresentante = null;
        //cliente = null;
        //clienteNroRD = "";
        //clienteFecVen = "";        
        bioDni = "";
        bioNombre = "";
        municion = null;
        lstDetalleVenta2 = null;
        lstDetalleDev1 = null;
        lstDetalleDev2 = null;
        todoEmpaques = false;
//        devValidBio = false;
        motivoDevolucion = null;
    }
    
    public void eligeRepresentanteDev(SelectEvent event) {
        if (event.getObject() != null) {
            //Syso("Representante: " + event.getObject().toString());
            dniPerNat = event.getObject().toString();
            devRepresentante = gamacSbPersonaFacade.find(Long.valueOf(dniPerNat));
            bioDni = devRepresentante.getNumDoc();
            bioNombre = devRepresentante.getNombres() + " " + devRepresentante.getApePat() + " " + devRepresentante.getApeMat();

            /*RequestContext.getCurrentInstance().execute("asignarDniBio('" + bioDni + "','" + bioNombre + "');");
            RequestContext.getCurrentInstance().update("controlBiometricoForm:bioDNI");
            RequestContext.getCurrentInstance().update("controlBiometricoForm:bioNombre");*/        
            
        }else{
            dniPerNat = "";
            devRepresentante = null;
            bioDni = "";
            bioNombre = "";
        }
//        devValidBio = false;            
        RequestContext.getCurrentInstance().execute("$(\"#divMsgBioDev\").hide();");
    }
    
    public void loadDevolCompra(){
        selectIdCompra = "";
        resetDevolCompra();
        
        try {
            String value = JsfUtil.getRequestParameter("javax.faces.source");                        
            //Syso("value: " + value);
            String[] parts = value.split(":");
            String indiceTable = parts[2];
            selectIdCompra = JsfUtil.getRequestParameter("buscarForm:buscarDatatable:" + indiceTable + ":idCompra");
            String[] ids = selectIdCompra.split(",");
            
            selectedCompra = gamacAmaAdmunTransaccionFacade.find(Long.valueOf(ids[0]));
            //Syso("venta selected: " + selectedVenta.getId());

            RequestContext.getCurrentInstance().execute("PF('devMunicionCDialog').show()");
            RequestContext.getCurrentInstance().reset("devMunicionCForm"); 
            
            devLstRepresentantes = gamacSbPersonaFacade.listxRepresentante(selectedCompra.getClienteproveedorId().getId().toString());
            devRepresentante = null;

            int i = 0;
            lstDetalleDev1 = new ArrayList<GamacDetalleDev>();
            for (GamacAmaAdmunDetalleTrans row : selectedCompra.getGamacAmaAdmunDetalleTransCollection()) {
                int cantTotEmpDev = 0;
                for (GamacAmaAdmunTransaccion transDev: selectedCompra.getAmaAdmunTransaccionList()) {
                    if (transDev.getTipoTransaccionId().getCodProg().equals("TP_TRANS_DEVC")) {
                        for (GamacAmaAdmunDetalleTrans detalleDev: transDev.getAmaAdmunDetalleTransList()) {
                            if (row.getArticuloMunicionId().getId() == detalleDev.getArticuloMunicionId().getId()) {
                                cantTotEmpDev += detalleDev.getCantidadEmpaques();
                            }
                        }
                    }
                }
                GamacDetalleDev det = new GamacDetalleDev();
                det.setId( row.getId().toString() + i );
                det.setDetId( row.getId() );
                det.setMunicionId( row.getArticuloMunicionId().getId() );
                det.setCalibre( row.getArticuloMunicionId().getCalibrearmaId().getNombre() );
                det.setMunicion( row.getArticuloMunicionId().getMarcaId().getNombre() + " " + row.getArticuloMunicionId().getCalibrearmaId().getNombre() );
                det.setMuniPorEmpaque( row.getMuniPorEmpaque() );
                det.setCantidadEmpaques( row.getCantidadEmpaques() - cantTotEmpDev );
                det.setCantidadVendida( det.getCantidadEmpaques() * det.getMuniPorEmpaque() );
                det.setCantidadEmpDev(0);
                
                lstDetalleDev1.add(det);
                i++;
            }
            
            RequestContext.getCurrentInstance().update("devMunicionCForm");
            //RequestContext.getCurrentInstance().update("devMunicionCForm:dniRepresentante");            
            
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeAdvertencia("Vuelva a seleccionar Compra!");
        }
    }

    public void closeDevolVenta(){
        RequestContext.getCurrentInstance().execute("PF('devMunicionCDialog').hide()");
    }
    
    public void chkTodoEmpaques(){
        if(todoEmpaques){
            for (int i = 0; i < lstDetalleDev1.size(); i++) {
                lstDetalleDev1.get(i).setCantidadEmpDev(lstDetalleDev1.get(i).getCantidadEmpaques());
            }            
        }else{
            for (int i = 0; i < lstDetalleDev1.size(); i++) {
                lstDetalleDev1.get(i).setCantidadEmpDev(0);
            }            
        }
        RequestContext.getCurrentInstance().update("devMunicionCForm:detDevDatatable");
    }
    
    public void onCellEditGridDev(CellEditEvent event) {
        try {
            String oldValue = (String) event.getOldValue().toString();
            String newValue = (String) event.getNewValue().toString();

            if (newValue != null && !newValue.equals(oldValue)) {
                /*long codDetDev = Long.valueOf(JsfUtil.getRequestParameter("newInicioCursoForm:detDevDatatable:" + event.getRowIndex() + ":idDetDev"));
                String nomInst = JsfUtil.getRequestParameter("newInicioCursoForm:detDevDatatable:" + event.getRowIndex() + ":empInput_input");
                String[] ids = newValue.split(",");
                long codInst = Long.valueOf(ids[1].toString());

                for (GamacAmaAdmunDetalleTrans row : selectedVenta.getGamacAmaAdmunDetalleTransCollection()) {
                    if(row.getId()==codDetDev){
                        
                    }
                }*/

                //JsfUtil.mensaje("Se cambió cantidad");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("No se pudo cambiar cantindad!");
        }
    }
    
    public void guardarDevRV(){
        String source = JsfUtil.getRequestParameter("javax.faces.source");
        //Syso("source: " + source);
        
        if(source.equals("devMunicionCForm:btnGuardarDev")){
                    
            if(validarFormDevRV()){
                GamacAmaAdmunTransaccion regDevol = new GamacAmaAdmunTransaccion();
                GamacTipoGamac tipoTrans = gamacTipoGamacFacade.tipoGamacXCodProg("TP_TRANS_DEVC");
                GamacSbPersona agenteCom = gamacSbPersonaFacade.find(loginController.getUsuario().getPersonaId());
                regDevol.setTipoTransaccionId(tipoTrans);
                regDevol.setAgentecomerId(agenteCom);
                regDevol.setClienteproveedorId(selectedCompra.getClienteproveedorId());
                
                regDevol.setLocalcomercialId(selectedCompra.getLocalcomercialId());
                regDevol.setTipodocTransacId(selectedCompra.getTipoTransaccionId());
                regDevol.setEstadopresenId(gamacTipoGamacFacade.tipoGamacXCodProg("TP_ESTPRE_CRE"));
                
                regDevol.setFechatransaccion(devFecha);
                //SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
                //String periodo = df.format(devFecha);
                //regDevol.setPeriodo(Integer.valueOf(periodo));
                
                regDevol.setNumdocTransac(selectedCompra.getNumdocTransac());
                regDevol.setCierreInventario((short) 0);
                regDevol.setActivo((short) 1);
                regDevol.setAudLogin(loginController.getUsuario().getLogin());
                regDevol.setAudNumIp(JsfUtil.getIpAddress());
                
                //GamacTipoGamac tipComprador = new GamacTipoGamac();
                //tipComprador = gamacTipoGamacFacade.tipoGamacXCodProg("TP_CLI_AGEC");                
                regDevol.setTipoclienteproveedorId(selectedCompra.getTipoclienteproveedorId());
                regDevol.setRepresentanteId(devRepresentante);
                regDevol.setRgAutoClipro(selectedCompra.getRgAutoClipro());
                regDevol.setRgAutotransf(selectedCompra.getRgAutotransf());
                regDevol.setTransacionDevId(selectedCompra);
                
                lstDetalleDev2 = new ArrayList<GamacAmaAdmunDetalleTrans>();
                for (GamacDetalleDev row : lstDetalleDev1) {
                    if(row.getCantidadEmpDev()>0){
                        GamacAmaAdmunDetalleTrans detalleTrans = new GamacAmaAdmunDetalleTrans();
                        GamacAmaMunicion muni = gamacAmaMunicionFacade.find(row.getMunicionId());
                        detalleTrans.setTransaccionId(regDevol);
                        detalleTrans.setArticuloMunicionId(muni);
                        detalleTrans.setActivo((short) 1);
                        detalleTrans.setCantidadEmpaques(row.getCantidadEmpDev());
                        detalleTrans.setMuniPorEmpaque(row.getMuniPorEmpaque());
                        detalleTrans.setCantidadMuniciones(row.getCantidadEmpDev()*row.getMuniPorEmpaque());
                        detalleTrans.setAudLogin(loginController.getUsuario().getLogin());
                        detalleTrans.setAudNumIp(loginController.getNumIP());
                        lstDetalleDev2.add(detalleTrans);                        
                    }
                }

                regDevol.setGamacAmaAdmunDetalleTransCollection(lstDetalleDev2);
                
                try {
                    regDevol = (GamacAmaAdmunTransaccion) JsfUtil.entidadMayusculas(regDevol, "");
                    gamacAmaAdmunTransaccionFacade.create(regDevol);
                    
                    //GamacAmaAdmunDevolucion
                    for (GamacAmaAdmunDetalleTrans detTrans : lstDetalleDev2) {
                        GamacAmaAdmunDevolucion admunDev = new GamacAmaAdmunDevolucion();
                        admunDev.setActivo((short) 1);
                        admunDev.setAudLogin(loginController.getUsuario().getLogin());
                        admunDev.setAudNumIp(loginController.getNumIP());
                        admunDev.setSolicitanteId(agenteCom);
                        admunDev.setFechaDevolucion(devFecha);
                        admunDev.setMotivoDevolucion(motivoDevolucion);
                        admunDev.setAmaAdmunDetalleId(detTrans);
                        gamacAmaAdmunDevolucionFacade.create(admunDev);
                    }
                    
                    JsfUtil.mensaje("Se registró la devolución correctamente, Id Nro. " + regDevol.getId() + "!!");
                    
                    resetDevolCompra();
                    reiniciarValoresBusqueda();

                    //detalleTrans = new GamacAmaAdmunDetalleTrans(); 
                    lstDetalleDev1 = new ArrayList<GamacDetalleDev>(); 
                    lstDetalleDev2 = new ArrayList<GamacAmaAdmunDetalleTrans>();

                    RequestContext.getCurrentInstance().execute("PF('devMunicionCDialog').hide()");
                    RequestContext.getCurrentInstance().reset("devMunicionCForm"); 
                    RequestContext.getCurrentInstance().update("devMunicionCForm");
                    
                } catch (Exception e) {
                    JsfUtil.mensajeError("NO se pudo guardar registro de devolución!!"); 
                }
                
            }
            
        } else {
            JsfUtil.mensajeError("Función no adecuada!");
        }
        
    }
    
    private boolean validarFormDevRV(){
        boolean valida = true;
        
//        if(devRepresentante == null){
//            valida = false;
//            JsfUtil.mensajeError("Elige el representante!");
//        }
        
        int cantSinDevolver = 0;
        for (GamacDetalleDev row : lstDetalleDev1) {
            if(row.getCantidadEmpDev()==0){
                cantSinDevolver++;
            }
            if(row.getCantidadEmpDev() > row.getCantidadEmpaques()){
                valida = false;
                JsfUtil.mensajeError("La cantidad de empaques a devolver de algún artículo de la lista es mayor a la permitida!");
            }
        }
        
        if(cantSinDevolver==lstDetalleDev1.size()){
            valida = false;
            JsfUtil.mensajeError("Ingrese la cantidad de empaques a devolver de algún artículo de la lista!");
        }
        
        /*if(!isDevValidBio()){
            valida = false;
            JsfUtil.mensajeError("Falta realizar la verificación biométrica!.");            
        }*/
        
        return valida;
    }
    
//    public void loadFormLectorDev() {
//        if(devRepresentante!= null){
//            RequestContext.getCurrentInstance().execute("PF('controlBiometricoDialog').show()");
//            RequestContext.getCurrentInstance().execute("lectorBiometrico()");            
//        } else {
//            JsfUtil.mensajeAdvertencia("Eliga un representante para validación biométrica!");
//        }
//
//    }
    
}
