/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.sel.gamac.data.GamacAmaAdmunDetalleTrans;
import pe.gob.sucamec.sel.gamac.data.GamacAmaAdmunTransaccion;
import pe.gob.sucamec.sel.gamac.data.GamacAmaCatalogo;
import pe.gob.sucamec.sel.gamac.data.GamacAmaMunicion;
import pe.gob.sucamec.sel.gamac.data.GamacDetalleVenta2;
import pe.gob.sucamec.sel.gamac.data.GamacListaInventario;
import pe.gob.sucamec.sel.gamac.data.GamacSbDireccion;
import pe.gob.sucamec.sel.gamac.data.GamacTipoGamac;
import pe.gob.sucamec.sel.gamac.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

/**
 *
 * @author rarevalo
 */
@Named("gamacMunicionesMiInventarioController")
@SessionScoped
public class GamacMunicionesMiInventarioController implements Serializable {

    /**
     * Variables de búsqueda
     */
    private String tipoFiltro;
    //private String filtro;
    private GamacSbDireccion findLocal;
    private GamacAmaCatalogo findMarca;
    private GamacAmaCatalogo findCalibreArma;
    private GamacTipoGamac findDenominacion;

    private List<GamacListaInventario> lstInventario;

    /**
     * Variables de Registro de Stock Inicial
     */
    private GamacSbDireccion direccion;
    private GamacSbDireccion direccionOrigen;
    private GamacTipoGamac tipoTransaccion;
    private String archivo;
    private byte[] fotoByte;
    private UploadedFile file;
    private StreamedContent foto;
    private Date fechaRegistro = new Date();
    private GamacAmaMunicion municion;
    private int cantMunicion;
    private int cantEmpaques;
    private int munXEmpaque;
    private String codFabricante;
    private String loteFabricacion;
    private boolean perdidaORobo;
    private boolean internamiento;
    private boolean sucursales;
    private String nroAutoInt;
    private String nroGuiaTrans;

    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacTipoGamacFacade gamacTipoGamacFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaCatalogoFacade gamacAmaCatalogoFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaAdmunDetalleTransFacade gamacAmaAdmunDetalleTransFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacSbDireccionFacade gamacSbDireccionFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaMunicionFacade gamacAmaMunicionFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacSbPersonaFacade gamacSbPersonaFacade;
    @EJB
    private pe.gob.sucamec.sel.gamac.beans.GamacAmaAdmunTransaccionFacade gamacAmaAdmunTransaccionFacade;

    @Inject
    private LoginController loginController;

    public String getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(String tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public GamacSbDireccion getFindLocal() {
        return findLocal;
    }

    public void setFindLocal(GamacSbDireccion findLocal) {
        this.findLocal = findLocal;
    }

    public GamacSbDireccion getDireccionOrigen() {
        return direccionOrigen;
    }

    public void setDireccionOrigen(GamacSbDireccion direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
    }

    public GamacAmaCatalogo getFindMarca() {
        return findMarca;
    }

    public void setFindMarca(GamacAmaCatalogo findMarca) {
        this.findMarca = findMarca;
    }

    public GamacAmaCatalogo getFindCalibreArma() {
        return findCalibreArma;
    }

    public void setFindCalibreArma(GamacAmaCatalogo findCalibreArma) {
        this.findCalibreArma = findCalibreArma;
    }

    public GamacTipoGamac getFindDenominacion() {
        return findDenominacion;
    }

    public void setFindDenominacion(GamacTipoGamac findDenominacion) {
        this.findDenominacion = findDenominacion;
    }

    public List<GamacListaInventario> getLstInventario() {
        return lstInventario;
    }

    public void setLstInventario(List<GamacListaInventario> lstInventario) {
        this.lstInventario = lstInventario;
    }

    public GamacSbDireccion getDireccion() {
        return direccion;
    }

    public void setDireccion(GamacSbDireccion direccion) {
        this.direccion = direccion;
    }

    public GamacTipoGamac getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(GamacTipoGamac tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public void setFotoByte(byte[] fotoByte) {
        this.fotoByte = fotoByte;
    }

    public byte[] getFotoByte() {
        return fotoByte;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFoto(StreamedContent foto) {
        this.foto = foto;
    }

    public StreamedContent getFoto() {
        return foto;
    }

    public List<GamacTipoGamac> getLstTipoDoc() {
        return gamacTipoGamacFacade.lstTipoComprobante();
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public GamacAmaMunicion getMunicion() {
        return municion;
    }

    public void setMunicion(GamacAmaMunicion municion) {
        this.municion = municion;
    }

    public int getCantMunicion() {
        cantMunicion = munXEmpaque * cantEmpaques;
        return cantMunicion;
    }

    public void setCantMunicion(int cantMunicion) {
        this.cantMunicion = cantMunicion;
    }

    public int getCantEmpaques() {
        return cantEmpaques;
    }

    public void setCantEmpaques(int cantEmpaques) {
        this.cantEmpaques = cantEmpaques;
    }

    public int getMunXEmpaque() {
        return munXEmpaque;
    }

    public void setMunXEmpaque(int munXEmpaque) {
        this.munXEmpaque = munXEmpaque;
    }

    public String getCodFabricante() {
        return codFabricante;
    }

    public void setCodFabricante(String codFabricante) {
        this.codFabricante = codFabricante;
    }

    public String getLoteFabricacion() {
        return loteFabricacion;
    }

    public void setLoteFabricacion(String loteFabricacion) {
        this.loteFabricacion = loteFabricacion;
    }

    public boolean isPerdidaORobo() {
        return perdidaORobo;
    }

    public void setPerdidaORobo(boolean perdidaORobo) {
        this.perdidaORobo = perdidaORobo;
    }

    public boolean isInternamiento() {
        return internamiento;
    }

    public void setInternamiento(boolean internamiento) {
        this.internamiento = internamiento;
    }

    public boolean isSucursales() {
        return sucursales;
    }

    public void setSucursales(boolean sucursales) {
        this.sucursales = sucursales;
    }

    public String getNroAutoInt() {
        return nroAutoInt;
    }

    public void setNroAutoInt(String nroAutoInt) {
        this.nroAutoInt = nroAutoInt;
    }

    public String getNroGuiaTrans() {
        return nroGuiaTrans;
    }

    public void setNroGuiaTrans(String nroGuiaTrans) {
        this.nroGuiaTrans = nroGuiaTrans;
    }

    /**
     * Funcion tipo 'action' para iniciar registro
     *
     * @return URL de registro
     */
    public String prepareListado() {
        reiniciarValoresBusqueda();
        return "/aplicacion/gamac/municiones/miInventario/listInventario";
    }

    private void reiniciarValoresBusqueda() {
        direccion = null;
        direccionOrigen = null;
        lstInventario = null;
        tipoFiltro = "";
        findLocal = null;
        findMarca = null;
        findCalibreArma = null;
        findDenominacion = null;
    }

    public void reiniciarCreate() {
        tipoTransaccion = null;
        archivo = null;
        fotoByte = null;
        file = null;
        foto = null;
        direccion = null;
        direccionOrigen = null;
        fechaRegistro = new Date();
        municion = null;
        cantMunicion = 0;
        munXEmpaque = 0;
        cantEmpaques = 0;
        loteFabricacion = null;
        codFabricante = null;
    }

    public void buscar() {
        List<ArrayRecord> listado;
        lstInventario = new ArrayList<GamacListaInventario>();
        boolean goBuscar = false;

        HashMap mMap = new HashMap();
        mMap.put("agenteComerId", loginController.getUsuario().getPersonaId());
        mMap.put("tipoFiltro", tipoFiltro);

        if (tipoFiltro != null) {

            switch (tipoFiltro) {
                case "1":
                    if (findLocal != null) {
                        mMap.put("findLocal", findLocal.getId());
                        goBuscar = true;
                    } else {
                        JsfUtil.mensajeAdvertencia("Por favor eliga el local!");
                    }
                    break;
                case "2":
                    if (findMarca != null) {
                        //mMap.put("findMarca", findMarca.getId());
                        mMap.put("findMarca", findMarca.getNombre());
                        goBuscar = true;
                    } else {
                        JsfUtil.mensajeAdvertencia("Digite el texto para elegir la marca que desee buscar!");
                    }
                    break;
                case "3":
                    if (findCalibreArma != null) {
                        //mMap.put("findCalibreArma", findCalibreArma.getId());
                        mMap.put("findCalibreArma", findCalibreArma.getNombre());
                        goBuscar = true;
                    } else {
                        JsfUtil.mensajeAdvertencia("Digite el texto para elegir el calibre que desee buscar!");
                    }
                    break;
                case "4":
                    if (findDenominacion != null) {
                        mMap.put("findDenominacion", findDenominacion.getId());
                        goBuscar = true;
                    } else {
                        JsfUtil.mensajeAdvertencia("Digite el texto para elegir la denominación que desee buscar!");
                    }
                    break;
            }

        } else {
            //goBuscar = true;
            JsfUtil.mensajeAdvertencia("Por favor seleccione un criterio de búsqueda");
        }

        if (goBuscar) {
            listado = gamacAmaAdmunDetalleTransFacade.listInventarioxLogin(mMap);

            if (listado != null) {
                for (int i = 0; i < listado.size(); i++) {
                    GamacListaInventario row = new GamacListaInventario();

                    //row.setId(listado.get(i).get("ID").toString() + "," + i);
                    row.setLocalComercial(listado.get(i).get("LOCAL_COMERCIAL").toString());
                    row.setMarca(listado.get(i).get("MARCA_NOMBRE").toString());
                    row.setCalibreaArma(listado.get(i).get("CALIBRE_MUNICION").toString());
                    row.setDenominacion(listado.get(i).get("DENOMINACION").toString());
                    row.setTipoMunicion(listado.get(i).get("TIPO_MUNICION").toString());
                    row.setTipoProyectil(listado.get(i).get("TIPO_PROYECTIL").toString());
                    //row.setCantCompraEmpaques(Integer.valueOf( listado.get(i).get("SUM_EMPAQUES_COMPRAS").toString()));
                    //row.setCantVentaEmpaques(Integer.valueOf( listado.get(i).get("SUM_EMPAQUES_VENTAS").toString()));
                    row.setCantCompraMuniciones(Integer.valueOf(listado.get(i).get("SUM_MUNI_COMPRAS").toString()));
                    row.setCantVentaMuniciones(Integer.valueOf(listado.get(i).get("SUM_MUNI_VENTAS").toString()));
                    //row.setStockEmpaques( row.getCantCompraEmpaques() - row.getCantVentaEmpaques() );
                    row.setStockMuniciones(row.getCantCompraMuniciones() - row.getCantVentaMuniciones());

                    lstInventario.add(i, row);
                }
            }

        }

    }

    public List<GamacSbDireccion> listDire() {
        String id = String.valueOf(loginController.getUsuario().getPersonaId());
        return gamacSbDireccionFacade.listarDirecionPorPersonaS(id);
    }

    public List<GamacTipoGamac> listTipoTransaccion() {
        List<GamacTipoGamac> tipoTransaccion = new ArrayList<GamacTipoGamac>();
        tipoTransaccion.add(gamacTipoGamacFacade.tipoGamacXCodProg("TP_TRANS_STCINI"));
        tipoTransaccion.add(gamacTipoGamacFacade.tipoGamacXCodProg("TP_TRANS_PERDID"));
        tipoTransaccion.add(gamacTipoGamacFacade.tipoGamacXCodProg("TP_TRANS_INTERN"));
        tipoTransaccion.add(gamacTipoGamacFacade.tipoGamacXCodProg("TP_TRANS_TRSLDO"));
        return tipoTransaccion;
    }

    public void selectTransaccion() {
        perdidaORobo = false;
        internamiento = false;
        sucursales = false;
        if (tipoTransaccion != null) {
            switch (tipoTransaccion.getCodProg()) {
                case "TP_TRANS_STCINI":
                    break;
                case "TP_TRANS_PERDID":
                    perdidaORobo = true;
                    break;
                case "TP_TRANS_INTERN":
                    internamiento = true;
                    break;
                case "TP_TRANS_TRSLDO":
                    sucursales = true;
                    break;
            }
        }
        //RequestContext.getCurrentInstance().update("buscarForm");
    }

    public List<GamacAmaMunicion> autcompleteMunicion(String query) {
        try {
            List<GamacAmaMunicion> list = gamacAmaMunicionFacade.obtenerPorMarcaCalibre(query.toUpperCase());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void eligeMunicion(SelectEvent event) {
        if (event.getObject() != null) {
            municion = (GamacAmaMunicion) event.getObject();
        } else {
        }
    }

    public void handleFileUploadAdjunto(FileUploadEvent event) {
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

    public boolean validarTraslado() {
        boolean flagValido = false;
        if (direccion != null) {
            HashMap mMap = new HashMap();
            mMap.put("agenteComerId", loginController.getUsuario().getPersonaId());
            mMap.put("localId", direccionOrigen.getId());
            mMap.put("municionId", municion.getId());
            int cantVendida = munXEmpaque * cantEmpaques;
            List<ArrayRecord> listStock = gamacAmaMunicionFacade.stockMunicion(mMap);
            if (listStock != null) {
                if (listStock.size() > 0) {
                    int sumMuniIngresos = Integer.valueOf(listStock.get(0).get("SUM_MUNI_COMPRAS").toString());
                    int sumMuniSalidas = Integer.valueOf(listStock.get(0).get("SUM_MUNI_VENTAS").toString());
                    int stockMuni = sumMuniIngresos - sumMuniSalidas;
                    if (cantVendida > stockMuni) {
                        flagValido = true;
                        JsfUtil.mensajeAdvertencia("El Artículo que intenta trasladar, no cuenta con stock suficiente!");
                    }
                } else {
                    flagValido = true;
                    JsfUtil.mensajeAdvertencia("El Artículo que intenta trasladar, no cuenta con stock!");

                }
            } else {
                flagValido = true;
                JsfUtil.mensajeAdvertencia("El Artículo que intenta trasladar, no cuenta con stock!");
            }
        } else {
            flagValido = true;
            JsfUtil.mensajeError("Seleccione el local.");
        }
        return flagValido;
    }

    public void guardarStockInicial() {
        String source = JsfUtil.getRequestParameter("javax.faces.source");

        if (source.equals("newMunicionForm:btnGuardarIni")) {
            if (validarForm()) {
                if (sucursales && validarTraslado()) {
                    return;
                }
                GamacAmaAdmunTransaccion transaccion = new GamacAmaAdmunTransaccion();
                List<GamacAmaAdmunDetalleTrans> lstDetalle = new ArrayList<GamacAmaAdmunDetalleTrans>();

                if (sucursales) {
                    transaccion.setDirOrigenId(direccionOrigen);
                    transaccion.setTipoTransaccionId(gamacTipoGamacFacade.tipoGamacXCodProg("TP_TRANS_TLDIN"));
                } else {
                    transaccion.setTipoTransaccionId(tipoTransaccion);
                }
                transaccion.setLocalcomercialId(direccion);
                transaccion.setFechatransaccion(fechaRegistro);
                transaccion.setAgentecomerId(gamacSbPersonaFacade.find(loginController.getUsuario().getPersonaId()));
                transaccion.setCierreInventario((short) 1);
                transaccion.setActivo((short) 1);
                transaccion.setAudLogin(loginController.getUsuario().getLogin());
                transaccion.setAudNumIp(JsfUtil.getIpAddress());

                GamacAmaAdmunDetalleTrans detalleTrans = new GamacAmaAdmunDetalleTrans();
                detalleTrans.setTransaccionId(transaccion);
                detalleTrans.setArticuloMunicionId(municion);
                detalleTrans.setCodigoFabricante(codFabricante);
                detalleTrans.setLoteFabricacion(loteFabricacion);
                detalleTrans.setMuniPorEmpaque(munXEmpaque);
                detalleTrans.setCantidadEmpaques(cantEmpaques);
                cantMunicion = munXEmpaque * cantEmpaques;
                detalleTrans.setCantidadMuniciones(cantMunicion);
                detalleTrans.setActivo((short) 1);
                detalleTrans.setAudLogin(loginController.getUsuario().getLogin());
                detalleTrans.setAudNumIp(JsfUtil.getIpAddress());
                lstDetalle.add(detalleTrans);

                transaccion.setGamacAmaAdmunDetalleTransCollection(lstDetalle);

                try {
                    transaccion = (GamacAmaAdmunTransaccion) JsfUtil.entidadMayusculas(transaccion, "");
                    gamacAmaAdmunTransaccionFacade.create(transaccion);
                    
                    if (sucursales) {
                        GamacAmaAdmunTransaccion transaccionOut = new GamacAmaAdmunTransaccion();
                        List<GamacAmaAdmunDetalleTrans> lstDetalleOut = new ArrayList<GamacAmaAdmunDetalleTrans>();

                        transaccionOut.setDirDestinoId(direccion);
                        transaccionOut.setTipoTransaccionId(gamacTipoGamacFacade.tipoGamacXCodProg("TP_TRANS_TLDOUT"));
                        transaccionOut.setLocalcomercialId(direccionOrigen);
                        transaccionOut.setFechatransaccion(fechaRegistro);
                        transaccionOut.setAgentecomerId(gamacSbPersonaFacade.find(loginController.getUsuario().getPersonaId()));
                        transaccionOut.setCierreInventario((short) 1);
                        transaccionOut.setActivo((short) 1);
                        transaccionOut.setAudLogin(loginController.getUsuario().getLogin());
                        transaccionOut.setAudNumIp(JsfUtil.getIpAddress());

                        GamacAmaAdmunDetalleTrans detalleTransOut = new GamacAmaAdmunDetalleTrans();
                        detalleTransOut.setTransaccionId(transaccionOut);
                        detalleTransOut.setArticuloMunicionId(municion);
                        detalleTransOut.setCodigoFabricante(codFabricante);
                        detalleTransOut.setLoteFabricacion(loteFabricacion);
                        detalleTransOut.setMuniPorEmpaque(munXEmpaque);
                        detalleTransOut.setCantidadEmpaques(cantEmpaques);
                        cantMunicion = munXEmpaque * cantEmpaques;
                        detalleTransOut.setCantidadMuniciones(cantMunicion);
                        detalleTransOut.setActivo((short) 1);
                        detalleTransOut.setAudLogin(loginController.getUsuario().getLogin());
                        detalleTransOut.setAudNumIp(JsfUtil.getIpAddress());
                        lstDetalleOut.add(detalleTransOut);

                        transaccionOut.setGamacAmaAdmunDetalleTransCollection(lstDetalleOut);
                        
                        transaccionOut = (GamacAmaAdmunTransaccion) JsfUtil.entidadMayusculas(transaccionOut, "");
                        gamacAmaAdmunTransaccionFacade.create(transaccionOut);
                    }
                    
                    JsfUtil.mensaje("Se registró la transacción de municiones correctamente, Id Nro. " + transaccion.getId() + "!");

                    reiniciarCreate();
                    reiniciarValoresBusqueda();
                    RequestContext.getCurrentInstance().execute("PF('newMunicionDialog').hide()");
                    RequestContext.getCurrentInstance().update("buscarForm");

                } catch (Exception e) {
                    JsfUtil.mensajeError("NO se pudo guardar el registro de la transacción!!");
                }
            }

        } else {
            JsfUtil.mensajeError("Función no adecuada!");
        }
    }

    private boolean validarForm() {
        boolean valida = true;

        if (direccion == null) {
            valida = false;
            JsfUtil.mensajeError("Seleccionar el local");
        }

        if (fechaRegistro == null) {
            valida = false;
            JsfUtil.mensajeError("Ingresar la fecha de registro");
        }

        if (municion == null) {
            valida = false;
            JsfUtil.mensajeError("Ingresar la munición");
        }

        if (cantEmpaques == 0) {
            valida = false;
            JsfUtil.mensajeError("La cantidad de empaques debe ser mayor a 0");
        }

        if (munXEmpaque == 0) {
            valida = false;
            JsfUtil.mensajeError("Las municiones por empaque debe ser mayor a 0");
        }

        return valida;
    }

    public List<GamacAmaCatalogo> autcompleteMarcaMunicion(String query) {
        try {
            List<GamacAmaCatalogo> list = gamacAmaCatalogoFacade.listxMarcaMunicion(query);
            return list;

        } catch (Exception e) {
            Logger.getLogger(GamacMunicionesMiInventarioController.class.getName()).log(Level.SEVERE, "autcompleteMarcaMunicion", e);
            return null;
        }

    }

    public List<GamacAmaCatalogo> autcompleteCalibreArma(String query) {
        try {
            List<GamacAmaCatalogo> list = gamacAmaCatalogoFacade.listxCalibreArma(query);
            return list;

        } catch (Exception e) {
            Logger.getLogger(GamacMunicionesMiInventarioController.class.getName()).log(Level.SEVERE, "autcompleteCalibreArma", e);
            return null;
        }

    }

    public List<GamacTipoGamac> autcompleteDenominacion(String query) {
        try {
            List<GamacTipoGamac> list = gamacTipoGamacFacade.listxDenominacion(query);
            return list;

        } catch (Exception e) {
            Logger.getLogger(GamacMunicionesMiInventarioController.class.getName()).log(Level.SEVERE, "autcompleteDenominacion", e);
            return null;
        }

    }

    public void eligeMarcaMunicion(SelectEvent event) {
        if (event.getObject() != null) {
            findMarca = (GamacAmaCatalogo) event.getObject();
            //Syso("Marca elegida: " + event.getObject().toString());

        } else {
            //
        }
    }

    public void eligeCalibreArma(SelectEvent event) {
        if (event.getObject() != null) {
            findCalibreArma = (GamacAmaCatalogo) event.getObject();
            //Syso("Marca elegida: " + event.getObject().toString());

        } else {
            //
        }
    }

    public void eligeDenominacion(SelectEvent event) {
        if (event.getObject() != null) {
            findDenominacion = (GamacTipoGamac) event.getObject();
            //Syso("Denominacion elegido: " + event.getObject().toString());

        } else {
            //
        }
    }
}
