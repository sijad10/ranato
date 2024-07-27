/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.jsf;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.SbExpVirtualSolicitud;
import pe.gob.sucamec.bdintegrado.data.SbParametro;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.renagi.data.ConsultaAmaCatalogo;
import pe.gob.sucamec.sel.citas.data.CitaAmaTurnoActa;
import pe.gob.sucamec.sel.citas.data.CitaDetalleArma;
import pe.gob.sucamec.sel.citas.data.CitaTipoBase;
import pe.gob.sucamec.sel.citas.data.CitaTipoGamac;
import pe.gob.sucamec.sel.citas.data.CitaTurConstancia;
import pe.gob.sucamec.sel.citas.data.CitaTurLicenciaReg;
import pe.gob.sucamec.sel.citas.data.TurMunicion;
import pe.gob.sucamec.sel.citas.data.CitaTurTurno;
import pe.gob.sucamec.sel.citas.jsf.util.EstadoCrud;
import pe.gob.sucamec.sel.citas.jsf.util.JsfUtil;
import pe.gob.sucamec.sel.citas.jsf.util.RenagiUploadFilesController;
import pe.gob.sucamec.sel.citas.jsf.util.ReportUtil;
import pe.gob.sucamec.sel.citas.ws.CitaWsPide;
import pe.gob.sucamec.sistemabase.data.SbConstancias;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbTipo;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;
import pe.gob.sucamec.sel.citas.data.CitaTurComprobante;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.bdintegrado.bean.AmaGuiaTransitoFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaCatalogoFacade;
import pe.gob.sucamec.bdintegrado.jsf.util.AmaGuiaTransitoGenericoController;
import pe.gob.sucamec.bdintegrado.jsf.util.ArmaRepClass;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaMuniciones;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioAccesorios;
import pe.gob.sucamec.renagi.jsf.util.StringUtil;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;

/**
 *
 * @author rarevalo
 */
@Named("citasReservadasController")
@SessionScoped
public class CitasReservadasController implements Serializable{

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;

    private static String DEPOSITO = "DEP", DEVOLUCION = "DEV";

    /**
     * Filtro basico para las búsquedas
     */
    private List<CitaTipoGamac> listaTramites;
    private CitaTipoGamac filtroTramite;    
    private List<CitaTurTurno> lstCitas;
    private Date filtroBandejaFecIni;
    private Date filtroBandejaFecFin;
    
    private List<CitaDetalleArma> lstDetalleArma; 
    List<Map> listArmas = new ArrayList();        
    private String horaProgra;
    private String horaPresen;
    private String horaTolera;
    private CitaDetalleArma detalleArma;        
    private String paso;
    private String tipoDoc;
    
    /**
     * Variables para vista
     */
    private CitaTurTurno turnoSeleccionado;
    
    private List<CitaTurConstancia> listaTipoLicencia = new ArrayList();    
    private boolean entroTipoarma = false;
    
    private List<SbExpVirtualSolicitud> listaSolExpedientes = new ArrayList();
    
    private SbParametro paramOriDatArma;
    private String desdeMigra;

    private List<TurMunicion> lstDetalleMunicion;
    private List<AmaGuiaMuniciones> listGuiaMunicion;
    private List<AmaInventarioAccesorios> lstAccesoriosActa;
    private AmaInventarioArma invArmaDeposito;
    private List<AmaInventarioAccesorios> listAccesArma;
    private Long actaInternamientoMunicionId;

    @Inject
    private LoginController loginController;
    @Inject
    private RenagiUploadFilesController upFilesCont;
    @Inject
    private AmaGuiaTransitoGenericoController amaGuiaTransitoGenericoController;

    @EJB
    private AmaGuiaTransitoFacade ejbAmaGuiaTransitoFacade;   
    @EJB
    private AmaCatalogoFacade ejbAmaCatalogoFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurTurnoFacade ejbFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurLicenciaRegFacade ejbLicenciaRegFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitasTurMunicionFacade ejbTurMunicionFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTipoGamacFacade ejbTipoGamacFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaRma1369Facade ejbRma1369Facade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade ejbSbUsuarioFacade;    
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbPersonaFacade ejbSbPersonaFacade;
    @EJB
    private SbParametroFacade ejbSbParametroFacade;    
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbConstanciasFacade ejbSbConstanciasFacade;    
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbTipoFacade ejbSbTipoFacade;    
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaSbNumeracionFacade ejbSbNumeracionFacade;    
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbDireccionFacade ejbSbDireccionFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbExpVirtualSolicitudFacade ejbSbExpVirtualSolicitudFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade tipoBaseGtFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTurComprobanteFacade ejbTurComprobanteFacade;
    
    public CitasReservadasController() {
    }

    @PostConstruct
    public void inicializar() {
        // Parámetro origen data ARMAS
        setParamOriDatArma(ejbSbParametroFacade.obtenerParametroXSistemaCodProg("TP_ORIGDAT_ARMDIS", 5L));
        if (paramOriDatArma != null) {
            setDesdeMigra(paramOriDatArma.getValor());
        } else {
            setDesdeMigra("DISCA");
        }
    }
    
    public EstadoCrud getEstado() {
        return estado;
    }

    public void setEstado(EstadoCrud estado) {
        this.estado = estado;
    }

    public CitaTipoGamac getTipoTramite() {
        return filtroTramite;
    }

    public void setTipoTramite(CitaTipoGamac filtroTramite) {
        this.filtroTramite = filtroTramite;
    }

    public List<CitaTipoGamac> getListaTramites() {
        listaTramites = ejbTipoGamacFacade.lstTipoTramite();        
        return listaTramites;
    }

    public void setListaTramites(List<CitaTipoGamac> listaTramites) {
        this.listaTramites = listaTramites;
    }

    public List<CitaTurTurno> getLstCitas() {
        return lstCitas;
    }

    public void setLstCitas(List<CitaTurTurno> lstCitas) {
        this.lstCitas = lstCitas;
    }

    public Date getFiltroBandejaFecIni() {
        return filtroBandejaFecIni;
    }

    public void setFiltroBandejaFecIni(Date filtroBandejaFecIni) {
        this.filtroBandejaFecIni = filtroBandejaFecIni;
    }

    public Date getFiltroBandejaFecFin() {
        return filtroBandejaFecFin;
    }

    public void setFiltroBandejaFecFin(Date filtroBandejaFecFin) {
        this.filtroBandejaFecFin = filtroBandejaFecFin;
    }

    public CitaTurTurno getTurnoSeleccionado() {
        return turnoSeleccionado;
    }

    public void setTurnoSeleccionado(CitaTurTurno turnoSeleccionado) {
        this.turnoSeleccionado = turnoSeleccionado;
    }

    public List<CitaTurConstancia> getListaTipoLicencia() {
        return listaTipoLicencia;
    }

    public void setListaTipoLicencia(List<CitaTurConstancia> listaTipoLicencia) {
        this.listaTipoLicencia = listaTipoLicencia;
    }

    public boolean isEntroTipoarma() {
        return entroTipoarma;
    }

    public void setEntroTipoarma(boolean entroTipoarma) {
        this.entroTipoarma = entroTipoarma;
    }

    public String getPaso() {
        return paso;
    }

    public void setPaso(String paso) {
        this.paso = paso;
    }

    public CitaTipoGamac getFiltroTramite() {
        return filtroTramite;
    }

    public void setFiltroTramite(CitaTipoGamac filtroTramite) {
        this.filtroTramite = filtroTramite;
    }

    public List<CitaDetalleArma> getLstDetalleArma() {
        return lstDetalleArma;
    }

    public void setLstDetalleArma(List<CitaDetalleArma> lstDetalleArma) {
        this.lstDetalleArma = lstDetalleArma;
    }

    public List<Map> getListArmas() {
        return listArmas;
    }

    public void setListArmas(List<Map> listArmas) {
        this.listArmas = listArmas;
    }

    public String getHoraProgra() {
        return horaProgra;
    }

    public void setHoraProgra(String horaProgra) {
        this.horaProgra = horaProgra;
    }

    public String getHoraPresen() {
        return horaPresen;
    }

    public void setHoraPresen(String horaPresen) {
        this.horaPresen = horaPresen;
    }

    public CitaDetalleArma getDetalleArma() {
        return detalleArma;
    }

    public void setDetalleArma(CitaDetalleArma detalleArma) {
        this.detalleArma = detalleArma;
    }

    public String getHoraTolera() {
        return horaTolera;
    }

    public void setHoraTolera(String horaTolera) {
        this.horaTolera = horaTolera;
    }

    public SbParametro getParamOriDatArma() {
        return paramOriDatArma;
    }

    public void setParamOriDatArma(SbParametro paramOriDatArma) {
        this.paramOriDatArma = paramOriDatArma;
    }

    public String getDesdeMigra() {
        return desdeMigra;
    }

    public void setDesdeMigra(String desdeMigra) {
        this.desdeMigra = desdeMigra;
    }

    public List<TurMunicion> getLstDetalleMunicion() {
        return lstDetalleMunicion;
    }

    public void setLstDetalleMunicion(List<TurMunicion> lstDetalleMunicion) {
        this.lstDetalleMunicion = lstDetalleMunicion;
    }

    public List<AmaGuiaMuniciones> getListGuiaMunicion() {
        return listGuiaMunicion;
    }

    public void setListGuiaMunicion(List<AmaGuiaMuniciones> listGuiaMunicion) {
        this.listGuiaMunicion = listGuiaMunicion;
    }

    public List<AmaInventarioAccesorios> getLstAccesoriosActa() {
        return lstAccesoriosActa;
    }

    public void setLstAccesoriosActa(List<AmaInventarioAccesorios> lstAccesoriosActa) {
        this.lstAccesoriosActa = lstAccesoriosActa;
    }

    public List<AmaInventarioAccesorios> getListAccesArma() {
        return listAccesArma;
    }

    public void setListAccesArma(List<AmaInventarioAccesorios> listAccesArma) {
        this.listAccesArma = listAccesArma;
    }

    public Long getActaInternamientoMunicionId() {
        return actaInternamientoMunicionId;
    }

    public void setActaInternamientoMunicionId(Long actaInternamientoMunicionId) {
        this.actaInternamientoMunicionId = actaInternamientoMunicionId;
    }
    
    ////////////////// CITAS RESERVADAS //////////////////
    /**
     * LIMPIAR VALORES DE VARIABLES
     *
     * @author rarevalo
     * @version 1.0
     */
    public void reiniciarValores() {
        lstCitas = new ArrayList();
        filtroTramite = null;
        filtroBandejaFecFin = null;
        filtroBandejaFecIni = null;
        turnoSeleccionado = null;
        actaInternamientoMunicionId = null;
    }
    
    public void reiniciarVista() {
        turnoSeleccionado = null;
        entroTipoarma = false;
        lstDetalleArma = new ArrayList<CitaDetalleArma>();
        listaTipoLicencia = new ArrayList<CitaTurConstancia>();
    }
    
    /**
     * FUNCION PARA PREPARAR LISTADO DE CITAS
     *
     * @author rarevalo
     * @version 1.0
     * @return Página para redireccionar
     */
    public String prepareModulo() {
        reiniciarValores();
        lstCitas = null;
        estado = EstadoCrud.BUSCAR;
        return "/aplicacion/citas/ListCitasReservadas.xhtml";
    }

    /**
     * FUNCION PARA BUSCAR CITAS RESERVADAS
     *
     * @author rarevalo
     * @version 1.0
     */
    public void buscarBandeja() {
        if (filtroTramite == null ) {
            JsfUtil.mensajeError("Debe seleccionar un tipo de trámite");
        } else {
            HashMap mMap = new HashMap();
            mMap.put("tipoTramite", filtroTramite.getCodProg());
            mMap.put("fechaIni", filtroBandejaFecIni);
            mMap.put("fechaFin", filtroBandejaFecFin);
            mMap.put("docUser", loginController.getUsuario().getNumDoc());
            //SbUsuario usu = ejbSbUsuarioFacade.selectUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()).get(0);
            mMap.put("tipUser", loginController.getUsuario().getPersona().getTipoId().getCodProg());
            
            lstCitas = ejbFacade.listarCitasReservadas(mMap);
            for (CitaTurTurno tur : lstCitas) {
                if (tur.getTipoSedeId().getCodProg().equals("TP_AREA_TRAM")) {
                    tur.getTipoSedeId().setNombre("LIMA");
                }
            }
        }
    }
    
    public void listaArmas(CitaTurTurno reg){
        String docPropietario = loginController.getUsuario().getNumDoc();
        
        lstDetalleArma = new ArrayList<CitaDetalleArma>();
        for (CitaTurLicenciaReg det : reg.getTurLicenciaRegList()) {
            Long nroLic = det.getNumLic();
            String nroSerie = det.getSerie();
            List<Map> list = new ArrayList<Map>();
            
            switch (desdeMigra){
                case "DISCA":
                    if (det.getArmaId()!=null) {
                        list = ejbRma1369Facade.listarArmasxUsrRuaSerie(docPropietario, det.getArmaId().getNroRua(), nroSerie);
                    } else {
                        list = ejbRma1369Facade.listarArmasxUsrLicSerie(docPropietario, nroLic, nroSerie);
                    }
                    break;

                case "MIGRA":
                    if (det.getArmaId()!=null) {
                        list = ejbRma1369Facade.listarArmasxUsrRuaSerieMigra(docPropietario, det.getArmaId().getNroRua(), nroSerie, loginController.getUsuario().getPersonaId());
                    } else {
                        list = ejbRma1369Facade.listarArmasxUsrLicSerieMigra(docPropietario, nroLic, nroSerie, loginController.getUsuario().getPersonaId());
                    }
                    break;
            }

            
            detalleArma = new CitaDetalleArma();
            detalleArma.setId(String.valueOf(lstDetalleArma.size()+1));
            detalleArma.setNroLicencia(nroLic);
            if (list.size()>0) {
                detalleArma.setFecVencimiento((list.get(0).get("FEC_VENCIMIENTO")!=null)? (Date) list.get(0).get("FEC_VENCIMIENTO"):null);
                detalleArma.setTipoLicencia(list.get(0).get("TIPO_LICENCIA").toString());
                detalleArma.setNroSerie(nroSerie);
                detalleArma.setTipoArma(list.get(0).get("TIPO_ARMA").toString());
                detalleArma.setMarca(list.get(0).get("MARCA").toString());
                detalleArma.setModelo(list.get(0).get("MODELO").toString());
                detalleArma.setCalibre((list.get(0).get("CALIBRE").toString()!=null)? list.get(0).get("CALIBRE").toString():"-");
                detalleArma.setEstado(list.get(0).get("ESTADO").toString());
                detalleArma.setSituacion(list.get(0).get("SITUACION").toString());                
            } else {
                detalleArma.setFecVencimiento(det.getFecVen());
                //detalleArma.setTipoLicencia(det.get);
                detalleArma.setNroSerie(nroSerie);
                //detalleArma.setTipoArma(det.getT);
                //detalleArma.setMarca(list.get(0).get("MARCA").toString());
                detalleArma.setModelo((det.getModelo()!=null)? det.getModelo().getModelo():"");
                //detalleArma.setCalibre((list.get(0).get("CALIBRE").toString()!=null)? list.get(0).get("CALIBRE").toString():"-");
                //detalleArma.setEstado(det.get);
                detalleArma.setSituacion(det.getSituacionId().getNombre());
            }

            lstDetalleArma.add(detalleArma); 
            
        }
        
    }
    
    public void listaArmasMunicionesEmpadronamiento(CitaTurTurno reg){
        actaInternamientoMunicionId = null;
        //Armas
        lstDetalleArma = new ArrayList<CitaDetalleArma>();
        for (CitaTurLicenciaReg det : reg.getTurLicenciaRegList()) {
            detalleArma = new CitaDetalleArma();
            detalleArma.setTurLicenciaRegId(det.getId());
            detalleArma.setTipoArma(det.getTipoArmaId().getNombre());
            detalleArma.setTipoInternamiento(det.getTipoInternamientoId().getNombre());
            detalleArma.setNroSerie(det.getSerie());
            detalleArma.setMarca(det.getMarcaNombre());
            detalleArma.setModelo(det.getModeloNombre());
            detalleArma.setCalibre(det.getCalibreNombre());
            if (det.getComprobanteId() != null) {
                detalleArma.setComprobantePago(det.getComprobanteId());
            }
            detalleArma.setActaInternamientoId(det.getActaInternamientoId());
            lstDetalleArma.add(detalleArma); 
        }
        
        //Municiones
        TurMunicion detalleMunicionTemp;
        lstDetalleMunicion = new ArrayList<TurMunicion>();
        for (TurMunicion det : reg.getTurMunicionList()) {
            detalleMunicionTemp = new TurMunicion();
            detalleMunicionTemp.setId(det.getId());
            detalleMunicionTemp.setMarca(det.getMarca());
            detalleMunicionTemp.setCalibre(det.getCalibre());
            detalleMunicionTemp.setCantidad(det.getCantidad());
            if (det.getActaInternamientoId() != null) {
                actaInternamientoMunicionId = det.getActaInternamientoId();
            }
            lstDetalleMunicion.add(detalleMunicionTemp); 
        }
    }
    
    public void listaArmasVer(CitaTurTurno reg){
        String docPropietario = loginController.getUsuario().getNumDoc();
        
        lstDetalleArma = new ArrayList<CitaDetalleArma>();
        for (CitaTurLicenciaReg det : reg.getTurLicenciaRegList()) {
            if (det.getArmaId()!=null) {
                detalleArma = new CitaDetalleArma();
                detalleArma.setId(String.valueOf(lstDetalleArma.size()+1));
                detalleArma.setNroLicencia(det.getNumLic());
                detalleArma.setNroRua(det.getArmaId().getNroRua());
                detalleArma.setNroSerie(det.getSerie());
                detalleArma.setTipoArma(det.getArmaId().getModeloId().getTipoArmaId().getNombre());
                detalleArma.setMarca(det.getArmaId().getModeloId().getMarcaId().getNombre());
                detalleArma.setModelo(det.getArmaId().getModeloId().getModelo());
                String calibres = "";
                for (AmaCatalogo calibre : det.getArmaId().getModeloId().getAmaCatalogoList()) {
                    if (calibre.getActivo() == 1) {
                        if (calibres.equals("")) {
                            calibres = calibre.getNombre();
                        } else {
                            calibres = calibres + " / " + calibre.getNombre();
                        }
                    }
                }
                
                detalleArma.setCalibre(calibres);
                detalleArma.setEstado((det.getTipoEstado()!=null)? det.getTipoEstado().getNombre():"");
                detalleArma.setSituacion((det.getSituacionId()!=null)? det.getSituacionId().getNombre():"");
                detalleArma.setTipoEvaluado(det.getTipoEvaluado());
                detalleArma.setTurLicenciaRegId(det.getId());
            } else {
                Long nroLic = det.getNumLic();
                String nroSerie = det.getSerie();
                List<Map> list = new ArrayList<>();
                switch (desdeMigra){
                    case "DISCA":
                        list = ejbRma1369Facade.listarArmasxUsrLicSerie(docPropietario, nroLic, nroSerie);
                        break;

                    case "MIGRA":
                        list = ejbRma1369Facade.listarArmasxUsrLicSerieMigra(docPropietario, nroLic, nroSerie, loginController.getUsuario().getPersona().getId());
                        break;
                }
                detalleArma = new CitaDetalleArma();
                detalleArma.setId(String.valueOf(lstDetalleArma.size()+1));
                detalleArma.setNroSerie(nroSerie);
                if (list.size()>0) {
                    detalleArma.setNroLicencia(nroLic);
                    detalleArma.setFecVencimiento((list.get(0).get("FEC_VENCIMIENTO")!=null)? (Date) list.get(0).get("FEC_VENCIMIENTO"):null);
                    detalleArma.setTipoLicencia(list.get(0).get("TIPO_LICENCIA").toString());
                    detalleArma.setTipoArma(list.get(0).get("TIPO_ARMA").toString());
                    detalleArma.setMarca(list.get(0).get("MARCA").toString());
                    detalleArma.setModelo(list.get(0).get("MODELO").toString());
                    detalleArma.setCalibre((list.get(0).get("CALIBRE").toString()!=null)? list.get(0).get("CALIBRE").toString():"-");
                    detalleArma.setEstado(list.get(0).get("ESTADO").toString());
                    detalleArma.setSituacion(list.get(0).get("SITUACION").toString());                
                    detalleArma.setTipoEvaluado(det.getTipoEvaluado());                    
                } else {
                    detalleArma.setTurLicenciaRegId(det.getId());
                    detalleArma.setTipoArma(det.getModelo().getTipoArmaId().getNombre());
                    detalleArma.setMarca(det.getModelo().getMarcaId().getNombre());
                    detalleArma.setModelo(det.getModelo().getModelo());
                    detalleArma.setModeloId(det.getModelo());
                    detalleArma.setCalibre(obtenerCalibreArma(det.getModelo().getAmaCatalogoList()));
                    detalleArma.setEstado(det.getTipoEstado().getNombre());
                    detalleArma.setTipoEvaluado(det.getTipoEvaluado());                    
                }
            }
            
            if (det.getModelo()!=null) {
                detalleArma.setModeloId(det.getModelo());
            }
            lstDetalleArma.add(detalleArma); 
            
        }
        
    }

    /**
     * FUNCION PARA VISUALIZAR CITA SELECCIONADA
     *
     * @author rarevalo
     * @version 1.0
     * @param turno Registro selecconado
     */
    public void mostrarVerCita(CitaTurTurno turno) {
        
        reiniciarVista();
        turnoSeleccionado = turno;
        
        String horaProg = turnoSeleccionado.getProgramacionId().getHora();
        Integer horaInt;

        String min;
        horaProgra = horaProg.substring(0, 5);
        horaTolera = horaProg.substring(0, 4) + "5";
        min = horaProg.substring(3, 5);
        horaInt = Integer.parseInt(horaProg.substring(0, 2));

        switch (min) {
            case "00":
                horaInt = horaInt - 1;
                horaPresen = horaInt + ":45";
                break;
            case "15":
                horaPresen = horaInt + ":00";
                break;
            default:
                horaPresen = horaInt + ":15";
                break;
        }

        switch(turnoSeleccionado.getTipoTramiteId().getCodProg()){
            case "TP_TRAM_VAL":
                mostrarVistaVal();
                break;
                
            case "TP_TRAM_POL":
            case "TP_TRA_DEF":
            case "TP_TRA_DEP":
            case "TP_TRA_CAZ":
            case "TP_TRA_COL":
            case "TP_TRA_MUL":
            case "TP_TRA_VP":
            case "TP_TRA_SIS":                
                mostrarVistaPol();
                break;
                
            case "TP_TRAM_VER":
                mostrarVistaVer();
                break;
             
            case "TP_TRAM_TD":
                mostrarVistaTram();
                break;    

            case "TP_TRAM_EMP":
                mostrarVistaEmpadronamiento(); 
                break;                
                
        }
        
    }
    public void mostrarVistaTram(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("dlgVerCitaTramForm");
        context.execute("PF('wvDlgVerCitaTram').show();");
    }
    
    public void mostrarVistaVal(){
        
        listaArmas(turnoSeleccionado);
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("dlgVerCitaValForm");
        context.execute("PF('wvDlgVerCitaVal').show();");

        /*if (turno.getTipoTramiteId().getCodProg().equals("TP_TRAM_REG")) {
            blnAntecedentes = true;
        }*/
        /*for (TurLicenciaReg lic : turno.getTurLicenciaRegList()) {
            if (lic.getActivo() == 1) {
                lstArmas.add(lic);
            }
        }
        for (TurDireccion dir : turno.getPerExamenId().getTurDireccionList()) {
            if (dir.getActivo() == 1 && dir.getTipoId().getCodProg().equals("TP_DIRECB_FIS")) {
                direccionCita = dir;
                departamento = dir.getDistritoId().getProvinciaId().getDepartamentoId();
                provincia = dir.getDistritoId().getProvinciaId();
                distrito = dir.getDistritoId();
            }
        }*/        
    }   
    
    public void mostrarVistaPol(){
        
        listaTipoLicencia.clear();
        for (CitaTurConstancia it : turnoSeleccionado.getTurConstanciaList()) {
            if (it.getActivo() == 1) {
                listaTipoLicencia.add(it);
            }
        }               
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("dlgVerCitaPolForm");
        context.execute("PF('wvDlgVerCitaPol').show();");

    }   

    public void mostrarVistaVer(){
        
        listaArmasVer(turnoSeleccionado);
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("dlgVerCitaVerForm");
        context.execute("PF('wvDlgVerCitaVer').show();");

    }   
    
    
    public void mostrarVistaEmpadronamiento(){
        
        listaArmasMunicionesEmpadronamiento(turnoSeleccionado);
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("dlgVerCitaEmpadronamientoForm");
        context.execute("PF('wvDlgVerCitaEmpadronamiento').show();");

    }   
        
    
    public void cerrarVerCita(String tipo){
        switch(tipo){
            case "VAL":
                RequestContext.getCurrentInstance().execute("PF('wvDlgVerCitaVal').hide()");        
                break;
            case "POL":
                RequestContext.getCurrentInstance().execute("PF('wvDlgVerCitaPol').hide()");
                break;
            case "VER":
                RequestContext.getCurrentInstance().execute("PF('wvDlgVerCitaVer').hide()");
                break;
            case "TRAM":
                RequestContext.getCurrentInstance().execute("PF('wvDlgVerCitaTram').hide()");
                break;    
            case "EMPADRONAMIENTO":
                RequestContext.getCurrentInstance().execute("PF('wvDlgVerCitaEmpadronamiento').hide()");
                break;        
        }
    }
    
    public String descripcionTipoArma(CitaTurConstancia item) {
        String tipArma = "";
        if (item.getTipoArmaId()!=null) {
            return item.getTipoArmaId().getNombre();
        }
        if (item.getArmaTeoId() != null) {
            switch (item.getArmaTeoId().getCodProg()) {
                case "TP_ARMPIS":
                case "TP_ARMREV":
                    entroTipoarma = true;
                    tipArma = "CORTA";
                    break;
                case "TP_ARMCAR":
                case "TP_ARMESC":
                    entroTipoarma = false;
                    tipArma = "LARGA";
                    break;
            }
            /*if (tipArma.equals("")) {
                if (!entroTipoarma) {
                    entroTipoarma = true;
                    tipArma = "CORTA";
                } else {
                    entroTipoarma = false;
                    tipArma = "LARGA";
                }                
            }*/
        } else {
            if (!entroTipoarma) {
                entroTipoarma = true;
                tipArma = "CORTA";
            } else {
                entroTipoarma = false;
                tipArma = "LARGA";
            }            
        }
        
        return tipArma;
    }
    
    public boolean verImprimirConstancia(CitaTurTurno reg){
        switch(reg.getTipoTramiteId().getCodProg()){
            case "TP_TRAM_VER":
                return false;
            case "TP_TRAM_VAL":
                if (reg.getHashQr() != null) {
                    return true;
                }
                
            case "TP_TRAM_POL":
            case "TP_TRA_DEF":
            case "TP_TRA_DEP":
            case "TP_TRA_CAZ":
            case "TP_TRA_COL":
            case "TP_TRA_MUL":
            case "TP_TRA_VP":
            case "TP_TRA_SIS":
                boolean mostrar = false;
                for (CitaTurConstancia tc : reg.getTurConstanciaList()) {
                    if (tc.getActivo() == 1) {
                        mostrar = false;
                        if (tc.getResulGral() != null) {
                            mostrar = true;
                        }
                    }
                }
                
                return mostrar;
        }
        
        return false;
    }
    
    /**
     * MOSTRAR REPORTE DE CITA
     * @author rarevalo
     * @param reg
     * @return 
     */
    public StreamedContent verPdf(CitaTurTurno reg) {
        
        Date fecha = reg.getFechaPrograma(); //new Date();
        String cFecha = ReportUtil.mostrarFechaDdMmYyyy(fecha);
        String cHora = ReportUtil.mostrarFechaHhMmSs(fecha);
        String cFechaNom = ReportUtil.formatoFechaYyyyMmDdHhMmSs(fecha);
        String nomFile = "citasVal_" + cFechaNom + "_" + reg.getId();
                
        try {
            if (JsfUtil.buscarPdfRepositorio(nomFile, ResourceBundle.getBundle("/BundleCitas").getString("Documentos_pathUpload_constanciaCita")) == false) {
                List<ArrayRecord> ls2 = ejbRma1369Facade.listarArrayRecord();
                List<ArrayRecord> ls =new ArrayList();
                ArrayRecord regis = (ArrayRecord) ls2.get(0);
                ls.add(regis);

                ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();       
                HashMap mapa = new HashMap();

                String jasperConst = "constCitasVal.jasper";

                mapa.put("P_logoSucamec", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));         

                mapa.put("p_fechaString", cFecha);
                mapa.put("p_fechaHora", cHora);
                String tipoNumDoc = reg.getPerExamenId().getTipoDoc().getAbreviatura() + " - " + reg.getPerExamenId().getNumDoc();
                String nombres = reg.getPerExamenId().getApePat() + " " + reg.getPerExamenId().getApeMat() + " " + reg.getPerExamenId().getNombres();
                String lugar = reg.getProgramacionId().getSedeId().getNombre();

                String termyCond = ResourceBundle.getBundle("/BundleCitas").getString("CitaArmas_paso3Nota1_Title");
                termyCond += "\n" +  ResourceBundle.getBundle("/BundleCitas").getString("CitaArmas_paso3Nota2_Title");

                String horaProg = reg.getProgramacionId().getHora();
                Integer horaInt;

                String min;
                horaProgra = horaProg.substring(0, 5);
                horaTolera = horaProg.substring(0, 4) + "5";
                min = horaProg.substring(3, 5);
                horaInt = Integer.parseInt(horaProg.substring(0, 2));

                switch (min) {
                    case "00":
                        horaInt = horaInt - 1;
                        horaPresen = horaInt + ":45";
                        break;
                    case "15":
                        horaPresen = horaInt + ":00";
                        break;
                    default:
                        horaPresen = horaInt + ":15";
                        break;
                }

                mapa.put("p_tipoDoc", tipoNumDoc);
                mapa.put("p_nombres", nombres);
                mapa.put("p_lugar", lugar);
                mapa.put("p_turno", ReportUtil.mostrarFechaDdMmYyyy((Date) reg.getFechaTurno()) + " - " + horaProgra);
                mapa.put("p_fechaAnio", ReportUtil.mostrarAnio(fecha));
                mapa.put("p_termycond", termyCond);
                mapa.put("p_fechaTurno", ReportUtil.mostrarFechaDdMmYyyy((Date) reg.getFechaTurno()));
                mapa.put("p_horasTurno", horaPresen + " y " + horaProgra);

                mapa.put("p_clave", reg.getClaveConsulta());        

                listArmas.clear();
                listaArmas(reg);
                for (int i = 0; i < lstDetalleArma.size(); i++) {
                    Map arm = new HashMap();
                    arm.put("nroLicencia", lstDetalleArma.get(i).getNroLicencia().toString() );
                    arm.put("fecVen", ReportUtil.mostrarFechaDdMmYyyy(lstDetalleArma.get(i).getFecVencimiento()) );
                    arm.put("modalidad", lstDetalleArma.get(i).getTipoLicencia() );
                    arm.put("nroSerie", lstDetalleArma.get(i).getNroSerie() );
                    arm.put("arma", lstDetalleArma.get(i).getTipoArma() + " " + lstDetalleArma.get(i).getMarca() + " " + lstDetalleArma.get(i).getModelo() + " " + lstDetalleArma.get(i).getCalibre() );
                    arm.put("estado", lstDetalleArma.get(i).getEstado() );
                    arm.put("situacion", lstDetalleArma.get(i).getSituacion() );
                    int x = 0;
                    listArmas.add(arm);
                }

                mapa.put("p_armas", listArmas);
                
                JsfUtil.subirPdf(nomFile, mapa, ls, "/aplicacion/citas/rep/" + jasperConst, ResourceBundle.getBundle("/BundleCitas").getString("Documentos_pathUpload_constanciaCita"));

            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: Reporte", ex);
        }

        try {
            upFilesCont.PdfDownload(nomFile + ".pdf");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        
        return null;
        
    }

    /**
     * MOSTRAR CONSTANCIA DE VALIDACION DE CITA
     * @author rarevalo
     * @param reg
     * @return 
     */
    public StreamedContent verConstanciaValPdf(CitaTurTurno reg) {
        System.out.println("reg->"+reg.getId());
        switch(reg.getTipoTramiteId().getCodProg()){
            case "TP_TRAM_VAL":
                mostrarConstanciaVal(reg);
                break;
                
            case "TP_TRAM_VER":
                mostrarConstanciaVerRen(reg);
                break;

            case "TP_TRAM_POL":
            case "TP_TRA_DEF":
            case "TP_TRA_DEP":
            case "TP_TRA_CAZ":
            case "TP_TRA_COL":
            case "TP_TRA_MUL":
            case "TP_TRA_VP":
            case "TP_TRA_SIS":                
                mostrarConstanciaPol(reg);
                break;       
                
        }
        return null;
    }

    public StreamedContent mostrarConstanciaVal(CitaTurTurno reg) {
        
        //String nomFile = reg.getId() + "_" + reg.getClaveConsulta();    
        //String pathFecha = ReportUtil.mostrarAnio(reg.getFechaTurno()) + File.separator +  ReportUtil.mostrarMes(reg.getFechaTurno()) + File.separator;
        
        SbPersona persona = ejbSbPersonaFacade.buscarEmpresaXDoc(reg.getPerExamenId().getNumDoc()).get(0);
        String cFechaNom = ReportUtil.formatoFechaYyyyMmDdHhMmSs(reg.getFechaTurno());
        cFechaNom = cFechaNom.substring(0, 8);
        String cHora = reg.getProgramacionId().getHora().substring(0, 5);
        String nomFile = cFechaNom + cHora.replace(":", "") + "_" + persona.getId();
        String pathFecha = ReportUtil.mostrarAnio(reg.getFechaTurno()) + File.separator + ReportUtil.mostrarMes(reg.getFechaTurno()) + File.separator;        
        String pathPdf = ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_constanciaVal").getValor() + pathFecha; //JsfUtil.bundle("Documentos_pathUpload_constanciaVal") + pathFecha;

        try {
            JsfUtil.PdfDownload(pathPdf, nomFile + ".pdf");
        } catch (IOException ex) {
            //ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: citasReservadasController.Constancia :", ex);
        }            
        return null;
        
    }
    
    public StreamedContent mostrarConstanciaPol(CitaTurTurno reg) {
        //String nomFile = reg.getId() + "_" + reg.getClaveConsulta();
        //String pathFecha = ReportUtil.mostrarAnio(reg.getFechaTurno()) + File.separator +  ReportUtil.mostrarMes(reg.getFechaTurno()) + File.separator;
        String pathPdf = ResourceBundle.getBundle("/BundleCitas").getString("Documentos_pathUpload_constanciaPol"); //JsfUtil.bundle("Documentos_pathUpload_constanciaVal") + pathFecha;

        //List<CitaTurTurno> lp = new ArrayList();
        //lp.add(reg);
        Long tcIdMayor = null;
        for (CitaTurConstancia turCons : reg.getTurConstanciaList()) {
            if (turCons.getActivo() == 1) {
                if (tcIdMayor == null) {
                    tcIdMayor = turCons.getId();
                } else if (turCons.getId() > tcIdMayor) {
                    tcIdMayor = turCons.getId();
                }
            }
        }
                
        try {
            JsfUtil.PdfDownload(pathPdf, tcIdMayor + ".pdf");
        } catch (IOException ex) {
            //ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: citasReservadasController.Constancia :", ex);
        }            
        return null;
        
    }

    public StreamedContent mostrarConstanciaVerRen(CitaTurTurno reg) {
        
        //String nomFile = reg.getId() + "_" + reg.getClaveConsulta();    
        //String pathFecha = ReportUtil.mostrarAnio(reg.getFechaTurno()) + File.separator +  ReportUtil.mostrarMes(reg.getFechaTurno()) + File.separator;
        
        SbPersona persona = ejbSbPersonaFacade.buscarEmpresaXDoc(reg.getPerExamenId().getNumDoc()).get(0);
        String cFechaNom = ReportUtil.formatoFechaYyyyMmDdHhMmSs(reg.getFechaTurno());
        cFechaNom = cFechaNom.substring(0, 8);
        String cHora = reg.getProgramacionId().getHora().substring(0, 5);
        String nomFile = cFechaNom + cHora.replace(":", "") + "" + persona.getId() + "" + reg.getId();
        String pathFecha = ReportUtil.mostrarAnio(reg.getFechaTurno()) + File.separator + ReportUtil.mostrarMes(reg.getFechaTurno()) + File.separator;        
        String pathPdf = ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_constanciaVeri").getValor() + pathFecha; //JsfUtil.bundle("Documentos_pathUpload_constanciaVal") + pathFecha;

        try {
            JsfUtil.PdfDownload(pathPdf, nomFile + ".pdf");
        } catch (IOException ex) {
            //ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: citasReservadasController.Constancia :", ex);
        }            
        return null;
        
    }
    
    public StreamedContent mostrarConstanciaVer(CitaDetalleArma item) {
        
        CitaTurLicenciaReg turLicReg = ejbLicenciaRegFacade.find(item.getTurLicenciaRegId());
        String numDoc;
        if (turLicReg.getTurnoId().getPerPagoId().getTipoId().getCodProg().equals("TP_PER_JUR")){
            numDoc = turLicReg.getTurnoId().getPerPagoId().getRuc();
        } else {
            numDoc = turLicReg.getTurnoId().getPerPagoId().getNumDoc();
        }        
        
        SbPersona persona = ejbSbPersonaFacade.buscarEmpresaXDoc(numDoc).get(0);
        String cFechaNom = ReportUtil.formatoFechaYyyyMmDdHhMmSs(turLicReg.getTurnoId().getFechaTurno());
        cFechaNom = cFechaNom.substring(0, 8);
        String cHora = turLicReg.getTurnoId().getProgramacionId().getHora().substring(0, 5);
        String nomFile = cFechaNom + cHora.replace(":", "") + "_" + turLicReg.getId() + "_" + persona.getId();
        String pathFecha = ReportUtil.mostrarAnio(turLicReg.getTurnoId().getFechaTurno()) + File.separator + ReportUtil.mostrarMes(turLicReg.getTurnoId().getFechaTurno()) + File.separator;
        String pathPdf = (ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_constanciaVeri").getValor()) + pathFecha;
                
        /*SbPersona persona = ejbSbPersonaFacade.buscarEmpresaXDoc(reg.getPerExamenId().getNumDoc()).get(0);
        String cFechaNom = ReportUtil.formatoFechaYyyyMmDdHhMmSs(reg.getFechaTurno());
        cFechaNom = cFechaNom.substring(0, 8);
        String cHora = reg.getProgramacionId().getHora().substring(0, 5);
        String nomFile = cFechaNom + cHora.replace(":", "") + "_" + persona.getId();
        String pathFecha = ReportUtil.mostrarAnio(reg.getFechaTurno()) + File.separator + ReportUtil.mostrarMes(reg.getFechaTurno()) + File.separator;        
        String pathPdf = ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_constanciaVeri").getValor() + pathFecha; //JsfUtil.bundle("Documentos_pathUpload_constanciaVal") + pathFecha;
        */
        try {
            JsfUtil.PdfDownload(pathPdf, nomFile + ".pdf");
        } catch (IOException ex) {
            //ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: citasReservadasController.Constancia :", ex);
        }            
        return null;        
    }
    
    /**
     * MOSTRAR HORA DE TURNO FORMATEADA: HH:MM
     *
     * @author rarevalo
     * @version 1.0
     * @param turno Registro selecconado
     * @return Hora formateada
     */
    public String mostrarHora(CitaTurTurno turno) {
        if(turno!=null){
            return turno.getProgramacionId().getHora().substring(0, 5);            
        }else{
            return "";
        }
    }
    
    /**
     * FUNCIÓN PARA OBTENER CALIBRE DE ARMA SELECCIONADA
     *
     * @author Richar Fernández
     * @version 1.0
     * @param lstCatalogo Listado de calibres de arma
     * @return Cadena de calibres de un arma
     */
    public String obtenerCalibreArma(List<AmaCatalogo> lstCatalogo) {
        String cadena = "";

        if (lstCatalogo.size() == 1) {
            cadena = lstCatalogo.get(0).getNombre();
        } else {
            for (AmaCatalogo cat : lstCatalogo) {
                if (cat.getActivo() == 1) {
                    if (cadena.isEmpty()) {
                        cadena = cat.getNombre();
                    } else {
                        cadena = cadena + "/" + cat.getNombre();
                    }
                }
            }
        }

        return cadena;
    }
 
    public StreamedContent generarTrasladoPdf(CitaTurTurno reg) {
        if (validarImprimirPdf()) {
            try {

                List<CitaTipoBase> lp = new ArrayList();
                lp.add(new CitaTipoBase());
                
                HashMap ph = new HashMap();
                ph = parametrosTrasladoPdf(ph, reg); //parametrosPdf(registro);
                
                Date fecha = new Date();
                String cFecha = ReportUtil.mostrarFechaDdMmYyyy(fecha);
                String cHora = ReportUtil.mostrarFechaHhMmSs(fecha);
                String cFechaNom = ReportUtil.formatoFechaYyyyMmDdHhMmSs(fecha);
                ph.put("p_fechaString", cFecha);

                return JsfUtil.generarReportePdf("/aplicacion/citas/rep/constAutTrasladoVeri.jasper", lp, ph, cFechaNom+".pdf", null, null);                                    
                //return generarReportePdf("/aplicacion/consMultiple/reportes/consultaMultiplePdf.jasper", lp, parametrosPdf(), "consultaMultiple.pdf", null, null);

            } catch (Exception ex) {
                ex.printStackTrace();
                return JsfUtil.errorDescarga("Error: ConsMultipleController.reporte :", ex);
            }
        }
        return null;
    }

    private boolean validarImprimirPdf(){
        return true;
    }
    
    private HashMap parametrosTrasladoPdf(HashMap ph, CitaTurTurno ppdf){
        
            //# CREANDO REGISTRO DE CONSTANCIA #//
            CitaTurTurno reg = ejbFacade.find(ppdf.getId());
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();       
            
            SbPersona persona = ejbSbPersonaFacade.buscarEmpresaXDoc(reg.getPerExamenId().getNumDoc()).get(0);
            SbTipo tipoCons = ejbSbTipoFacade.buscarTipoBaseXCodProg("TP_CONST_ATTR");
            HashMap mMapCon = new HashMap();
            mMapCon.put("referenciaId", reg.getId());
            mMapCon.put("personaId", persona.getId());
            mMapCon.put("tipo", tipoCons.getCodProg());

            SbConstancias newConsExa = null;
            List<SbConstancias> lstConsExa = ejbSbConstanciasFacade.listaConstanciaTraslado(mMapCon);
            if (lstConsExa!=null) {
                if (!lstConsExa.isEmpty()) {
                    newConsExa = lstConsExa.get(0);
                }
            }
            
            ph.put("administrado", persona.getApellidosyNombres());
            ph.put("tipoNumDoc", persona.getTipoDoc().getNombre() + " - " + persona.getNumDoc());
            if (reg.getTipoSedeId().getCodProg().equals("TP_AREA_TRAM")) {
                ph.put("sedeSucamec", "LIMA");
            } else {
                ph.put("sedeSucamec", reg.getTipoSedeId().getNombre());                
            }

            SbDireccion direc = ejbSbDireccionFacade.listarDireccionesSucamec(reg.getTipoSedeId().getId()).get(0);
            ph.put("sedeDireccion", JsfUtil.mostrarDireccion(direc));
                        
            String cFecha =  JsfUtil.dateToString(reg.getFechaTurno(), "dd/MM/yyyy");
            String cHora = reg.getProgramacionId().getHora().substring(0, 5) + ":00";

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date rfecha = null;
            try {
                rfecha = sdf.parse(cFecha + " " + cHora);
                //String date = sdf.format(fecha );
            } catch (ParseException ex) {
                //Logger.getLogger(TurValidacionController.class.getName()).log(Level.SEVERE, null, ex);
                rfecha = reg.getFechaTurno();
            }

            String cFechaNom = ReportUtil.formatoFechaYyyyMmDdHhMmSs(rfecha);
            //ph.put("p_fechaString", cFecha);
            ph.put("p_fechaHora", cHora);
            ph.put("p_fechaAnio", ReportUtil.mostrarAnio(rfecha));
            ph.put("logoSucamec", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
            ph.put("firmaGerente", ec.getRealPath("/resources/imagenes/firma_nombre_GAMAC.png"));            
            ph.put("p_fechaPie", ReportUtil.mostrarFechaString(reg.getFechaTurno()));

            if (newConsExa == null) {
                newConsExa = new SbConstancias();
                newConsExa.setPersonaId(persona);
                newConsExa.setTipoConstanciaId(tipoCons);
                newConsExa.setAreaId(ejbSbTipoFacade.find(reg.getTipoSedeId().getId()));
                newConsExa.setUsuarioId(ejbSbUsuarioFacade.find(loginController.getUsuario().getId()));
                newConsExa.setFechaEmision(rfecha);
                newConsExa.setAudLogin(loginController.getUsuario().getLogin());
                newConsExa.setAudNumIp(JsfUtil.getNumIP());
                newConsExa.setHashQr(JsfUtil.crearHash(reg.getPerExamenId().getNumDoc() + "-" + reg.getId() + "-" + cFechaNom));
                newConsExa.setActivo((short) 1);

                SbUsuario gerente = ejbSbUsuarioFacade.obtenerJefeId(loginController.getUsuario().getLogin());
                if (gerente != null) {
                    newConsExa.setGerenteId(gerente);
                } else {
                    gerente = ejbSbUsuarioFacade.selectUsuarioXLogin(loginController.getUsuario().getLogin()).get(0);
                    newConsExa.setGerenteId(gerente);   
                }

                newConsExa.setNroConstancia(ejbSbNumeracionFacade.selectNumero("TP_NUM_CONST_ATTR"));
                newConsExa.setReferenciaId(reg.getId());

                newConsExa = (SbConstancias) JsfUtil.entidadMayusculas(newConsExa, "");
                ejbSbConstanciasFacade.create(newConsExa);                
            }

            ph.put("p_hashqr", "https://www.sucamec.gob.pe/sel/faces/qr/constAutTraslado.xhtml?h=" + newConsExa.getHashQr() + "-" + JsfUtil.crearHash(newConsExa.getId().toString()));
            ph.put("p_nroDoc", StringUtils.leftPad(String.valueOf(newConsExa.getNroConstancia()), 5, "0") + "-" + cFechaNom.substring(0, 4) + "-SUCAMEC-GAMAC");
            ph.put("P_WEBVALIDATOR", newConsExa.getHashQr().substring(0, 4) + "-N°" + ph.get("p_nroDoc"));
            ph.put("P_numIdExa", newConsExa.getHashQr().substring(0, 4) + "-N°" + StringUtils.leftPad(String.valueOf(newConsExa.getNroConstancia()), 5, "0"));
            //urlsel = StringUtils.substring(tc.getHashQr(), 1, 4) + "-" + ppdf.getId();
            //# #// 
        
            List<Map> listArmas = new ArrayList();
            for (CitaTurLicenciaReg arm : reg.getTurLicenciaRegList()){
                    Map tarjetaProp = new HashMap();
                    tarjetaProp.put("nroRua", arm.getArmaId().getNroRua());
                    tarjetaProp.put("serie", arm.getArmaId().getSerie());
                    tarjetaProp.put("tipoArma", arm.getModelo().getTipoArmaId().getNombre());
                    tarjetaProp.put("marca", arm.getModelo().getMarcaId().getNombre());
                    tarjetaProp.put("modelo", arm.getModelo().getModelo());
                    //tarjetaProp.put("modalidad", rs.get(i).getModalidadId().getNombre());
                    tarjetaProp.put("estado", arm.getArmaId().getEstadoId().getNombre());
                    //tarjetaProp.put("situacion", (arm.getArmaId().getSituacionId() != null) ? arm.getArmaId().getSituacionId().getNombre() : "");
                    String calibres = "";
                    int x = 0;

                    for (AmaCatalogo row : arm.getArmaId().getModeloId().getAmaCatalogoList()) {
                        if (x > 0) {
                            calibres += ", " + row.getNombre();
                        } else {
                            calibres += row.getNombre();
                        }

                        x++;
                    }
                    tarjetaProp.put("calibres", calibres);

                    listArmas.add(tarjetaProp);
                
            }
            ph.put("p_armas", listArmas);
            
        return ph;        
    }
    
    public boolean rendererVerPdfVeriRen(CitaTurTurno item) {
        if (item != null) {
            if (item.getSubTramiteId()!=null && item.getSubTramiteId().getCodProg().equals("TP_PROG_VER_RLU")) {
                if (item.getHashQr() != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }                    
        } else {
            return false;
        }
    }    
    
    public boolean rendererVerPdfVeri(CitaTurTurno item) {
        if (item != null) {
            if (item.getSubTramiteId()!=null && !item.getSubTramiteId().getCodProg().equals("TP_PROG_VER_RLU")) {
                if (item.getHashQr() != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }                    
        } else {
            return false;
        }
    }    

    public List<SbExpVirtualSolicitud> getListaSolExpedientes() {
        return listaSolExpedientes;
    }

    public void setListaSolExpedientes(List<SbExpVirtualSolicitud> listaSolExpedientes) {
        this.listaSolExpedientes = listaSolExpedientes;
    }
    
    public boolean renderBtnCancelarCitaReservada(CitaTurTurno item) {
        if (item == null) {
            return false;
        } else {
            //Si esta Pendiente la cita
            if (item.getEstado().getCodProg().equals("TP_EST_PEN")) {
                return true;
            }
        }
        return false;
    }
    
    public void cancelarCitaReservada(CitaTurTurno item) {
        try {
            CitaTurTurno cita = item;
            cita.setActivo((short) 0);
            ejbFacade.edit(cita);
            
            //Actualiza como inactivos en tur_licencia_reg
            List<CitaTurLicenciaReg> lstLicencias = ejbLicenciaRegFacade.listarLicenciasPorTurno(item.getId());
            if (lstLicencias.size() > 0) { 
                for (CitaTurLicenciaReg licReg : lstLicencias) {
                    if (licReg.getComprobanteId() != null) {
                        List<CitaTurComprobante> lstComprobante = ejbTurComprobanteFacade.listarComprobantePorTurno(licReg.getComprobanteId().getId());
                        for (CitaTurComprobante listCompro : lstComprobante) {
                            listCompro.setActivo((short) 0);
                            ejbTurComprobanteFacade.edit(listCompro);
                        }
                    }    
                    licReg.setActivo((short) 0);
                    ejbLicenciaRegFacade.edit(licReg);
                }
            }    

            //Inactiva las Municiones de las Citas por Empadronamiento y Amnistia
            List<TurMunicion> lstcitasTurMunicion = ejbTurMunicionFacade.listarCitasEmpadronamientoPorTurno(item.getId());
            if (lstcitasTurMunicion.size() > 0) { 
                for (TurMunicion citaTurMunicion : lstcitasTurMunicion) {
                    citaTurMunicion.setActivo((short) 0);
                    ejbTurMunicionFacade.edit(citaTurMunicion);
                }
            }    
        
            //Actualiza en caso de Poligono que este Verificado por Tramite
            if (item.getExpVirtualSolicitudId() != null) {
                List<SbExpVirtualSolicitud> lstSolicitud = ejbSbExpVirtualSolicitudFacade.listarSolicitudesXIdSolicitud(item.getExpVirtualSolicitudId().getId());
                if (lstSolicitud.size() > 0) { 
                     for (SbExpVirtualSolicitud solic : lstSolicitud) {
                        TipoBaseGt tipoRevisado = tipoBaseGtFacade.buscarTipoBasePorCodProg("TP_EXVIEST_REVTRA");
                        solic.setEstadoId(tipoRevisado);
                        ejbSbExpVirtualSolicitudFacade.edit(solic);
                     }
                }
            }    
            lstCitas.remove(item);

            JsfUtil.mensaje("Se ha cancelado correctamente la cita reservada");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeError("Ocurrió un error al cancelar la cita reservada.");
        }
    }

    public StreamedContent verActaArmaEmpadronamientoPdf(CitaDetalleArma objSelect) {
        try {
            //String jasper = "/aplicacion/amaDepositoArmas/reportes/DepositoDevolucion.jasper";
            String jasper = "/aplicacion/citas/rep/DepositoDevolucion.jasper";
            
            List<AmaGuiaTransito> agt = new ArrayList();
            //AmaGuiaTransito amaGuia = ejbAmaGuiaTransitoFacade.find(objSelect.getActaInternamientoId());
            AmaGuiaTransito amaGuia = ejbAmaGuiaTransitoFacade.buscarGuiaTransitoById(objSelect.getActaInternamientoId());
            agt.add(amaGuia);
            if ("TP_GTGAMAC_FIN".equalsIgnoreCase(amaGuia.getEstadoId().getCodProg())) {
                StreamedContent pdf = null;
                try {
                    pdf = JsfUtil.obtenerArchivo((ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_actasDepDev").getValor()), amaGuia.getId() + ".pdf", amaGuia.getId() + ".pdf", "application/pdf");
                } catch (Exception e) {
                }
                if (pdf == null) {
                    if (subirActaPdf(amaGuia, jasper, agt)) {
                        return JsfUtil.obtenerArchivo((ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_actasDepDev").getValor()), amaGuia.getId() + ".pdf", amaGuia.getId() + ".pdf", "application/pdf");
                    }
                } else {
                    return pdf;
                }
            } else {
                return JsfUtil.generarReportePdf(jasper, agt, parametrosAlmacenPdf(amaGuia), amaGuia.getId() + "_borrador.pdf");
                //return JsfUtil.generarReportePdf("/aplicacion/citas/rep/constAutTrasladoVeri.jasper", lp, ph, cFechaNom+".pdf", null, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: AmaGuiaTransito.reporte :", ex);
        }
        return null;
    }

    public HashMap parametrosAlmacenPdf(AmaGuiaTransito amaGuia) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap p = new HashMap();
        SbPersona solicitante = amaGuia.getSolicitanteId();
        SbDireccion dirSucamec = obtenerDireccionSedeSegunActa(amaGuia);
        p.put("TP_DIR_SEDE", dirSucamec == null ? null : StringUtils.capitalize(dirSucamec.getDistritoId().getNombre().toLowerCase()));
        if ("TP_PER_JUR".equalsIgnoreCase(solicitante.getTipoId().getCodProg())) {
            solicitante = amaGuia.getRepresentanteId();
        }
        p.put("P_TIPO_DEP", obtenerTipoDeposito(amaGuia));
        p.put("P_TIPO_ART_ARMA", obtenerTipoArticulArma(amaGuia));
        List<ArmaRepClass> listRp = amaGuiaTransitoGenericoController.tablaArmas(amaGuia);
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        p.put("P_FECHA_EMI", JsfUtil.mostrarFechaDateFormat(amaGuia.getFechaEmision()));
        p.put("P_HORA_EMI", amaGuia.getFechaEmision() == null ? StringUtil.VACIO : formatoHora.format(amaGuia.getFechaEmision()));
        //p.put("P_EXTERIOR", StringUtil.VACIO);//falta--Es un campo de AmaGuiaTRansito
        for (ArmaRepClass arma : listRp) {
            p.put("P_SERIE_ARMA", arma.getSerie());
            p.put("P_MODELO_ARMA", arma.getModelo());
            p.put("P_CALIBRE_ARMA", arma.getCalibre());
            p.put("P_MARCA_ARMA", arma.getMarca() == null ? StringUtil.VACIO : arma.getMarca().getNombre());
            p.put("P_ESTADO_SERIE", arma.getEstadoserieId() == null ? StringUtil.VACIO : arma.getEstadoserieId().getNombre());
            p.put("P_TIPO_ARMA", arma.getTipoArma() == null ? StringUtil.VACIO : arma.getTipoArma().getNombre());
            p.put("P_MOD_TIRO", arma.getModalidadTiroId() == null ? StringUtil.VACIO : arma.getModalidadTiroId().getNombre());
            p.put("P_PROPIETARIO", amaGuiaTransitoGenericoController.obtenerDescPersona(arma.getPropietarioId()));
            p.put("P_NOVEDAD_CANON", arma.getNovedadCanonId() == null ? StringUtil.VACIO : arma.getNovedadCanonId().getNombre());
            p.put("P_MECANISMOS", arma.getMecanismoId() == null ? StringUtil.VACIO : arma.getMecanismoId().getNombre());
            p.put("P_EXTERIOR", arma.getExteriorId() == null ? StringUtil.VACIO : arma.getExteriorId().getNombre());
            p.put("P_CACHAS", (arma.getMaterialCachaId() == null ? StringUtil.VACIO : (arma.getMaterialCachaId().getNombre())) + (arma.getEstadoCachaId() == null ? StringUtil.VACIO : (" - " + arma.getEstadoCachaId().getNombre())));
            p.put("P_GUARDAMANO", (arma.getMaterialGuardamanoId() == null ? StringUtil.VACIO : arma.getMaterialGuardamanoId().getNombre()) + (arma.getEstadoGuardamanoId() == null ? StringUtil.VACIO : (" - " + arma.getEstadoGuardamanoId().getNombre())));
            p.put("P_CULATA", (arma.getMaterialCulataId() == null ? StringUtil.VACIO : arma.getMaterialCulataId().getNombre()) + (arma.getEstadoCulataId() == null ? StringUtil.VACIO : (" - " + arma.getEstadoCulataId().getNombre())));
            p.put("P_CANTONERA", (arma.getMaterialCantoneraId() == null ? StringUtil.VACIO : arma.getMaterialCantoneraId().getNombre()) + (arma.getEstadoCantoneraId() == null ? StringUtil.VACIO : (" - " + arma.getEstadoCantoneraId().getNombre())));
            p.put("P_EMPUNADURA", (arma.getMaterialEmpunaduraId() == null ? StringUtil.VACIO : arma.getMaterialEmpunaduraId().getNombre()) + (arma.getEstadoEmpunaduraId() == null ? StringUtil.VACIO : (" - " + arma.getEstadoEmpunaduraId().getNombre())));
            p.put("P_ESTADO_CONSER", arma.getEstadoconservacionId() == null ? StringUtil.VACIO : arma.getEstadoconservacionId().getNombre());
            p.put("P_ESTADO_ARMA", arma.getEstadofuncionalId() == null ? StringUtil.VACIO : arma.getEstadofuncionalId().getNombre());
            p.put("P_SITUACION", arma.getSituacionId() == null ? StringUtil.VACIO : arma.getSituacionId().getNombre());
            break;
        }
        if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
            p.put("P_SOLICITANTE_DEP", amaGuiaTransitoGenericoController.obtenerNombrePersona(solicitante));
            p.put("P_SOLICITANTE_DEV", StringUtil.VACIO);
            p.put("P_IS_DEPOSITO", true);
            p.put("P_TIPO", DEPOSITO);
        } else if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEVOLUCION).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
            AmaGuiaTransito amaGuiaDep = amaGuia.getGuiaReferenciadaId();
            SbPersona solDeposito = amaGuiaDep.getSolicitanteId();
            if ("TP_PER_JUR".equalsIgnoreCase(solDeposito.getTipoId().getCodProg())) {
                solDeposito = amaGuiaDep.getRepresentanteId();
            }
            p.put("P_SOLICITANTE_DEP", amaGuiaTransitoGenericoController.obtenerNombrePersona(solDeposito));
            p.put("P_SOLICITANTE_DEV", amaGuiaTransitoGenericoController.obtenerNombrePersona(solicitante));
            p.put("P_IS_DEPOSITO", false);
            p.put("P_FECHA_DEV", amaGuia.getFechaEmision());
            p.put("P_TIPO", DEVOLUCION);
        }
        p.put("P_DOC_SOLICITANTE", amaGuiaTransitoGenericoController.obtenerDocumentoPersona(solicitante));
        p.put("P_LISTA_INFRACTORES", (amaGuia.getSbPersonaList() != null && amaGuia.getSbPersonaList().size() > 0) ? amaGuia.getSbPersonaList() : null);
        p.put("P_LISTA_ACCESORIOS", obtenerAccesMap(obtenerAccesoriosVer(amaGuia)));
        p.put("P_LISTA_ACCESORIOS_ACTA", lstAccesoriosActa);
        p.put("P_LISTA_MUNICIONES", obtenerListMuniActivos(amaGuia));
        p.put("P_LISTA_DOCUMENTOS", amaGuia.getAmaDocumentoList());
        p.put("P_LOGO_SUCAMEC", ec.getRealPath("/resources/imagenes/logo_sucamec_of.png"));
        p.put("P_HASHQR", "https://www.sucamec.gob.pe/sel/faces/qr/gamacGT.xhtml?h=" + amaGuia.getHashQr());
        return p;
    }

    private Boolean subirActaPdf(AmaGuiaTransito amaGuia, String nombreJasper, List<AmaGuiaTransito> lp) {
        boolean valida = true;
        try {
            HashMap parametros = parametrosAlmacenPdf(amaGuia);
            if (parametros != null) {
                JsfUtil.subirPdf(amaGuia.getId().toString(), parametros, lp, nombreJasper, (ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_actasDepDev").getValor()));
            } else {
                valida = false;
            }
        } catch (Exception ex) {
            JsfUtil.mensajeError(JsfUtil.bundle("MensajeNoSubioArchivo") + StringUtil.ESPACIO + ex.getMessage());
            valida = false;
        }
        return valida;
    }

    public AmaCatalogo obtenerTipoDeposito(AmaGuiaTransito amaGuia) {
        AmaCatalogo tipoArt = null;
        if (amaGuia.getAmaInventarioArmaList() != null) {
            for (AmaInventarioArma invArma : amaGuia.getAmaInventarioArmaList()) {
                //Obtiene el tipo de articulo del arma seleccionada y prepara el formulario para mostrarlo.
                tipoArt = invArma.getModeloId().getTipoArticuloId();
                break;
            }
        }
        if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaGuiaMunicionesList())) {
            //Obtiene el tipo de articulo del arma seleccionada y prepara el formulario para mostrarlo.
            tipoArt = ejbAmaCatalogoFacade.obtenerPorCodProg("TP_ART_MUNI");
            //
        }
        if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioAccesoriosList())) {
            tipoArt = ejbAmaCatalogoFacade.obtenerPorCodProg("TP_ART_ACCE");
            lstAccesoriosActa = new ArrayList<>();
            for (AmaInventarioAccesorios acces : amaGuia.getAmaInventarioAccesoriosList()) {
                if (acces.getActivo() == JsfUtil.TRUE) {
                    lstAccesoriosActa.add(acces);
                }
            }
        }
        return tipoArt;
    }

    public String obtenerTipoArticulArma(AmaGuiaTransito amaGuia) {
        AmaCatalogo tipoArt = null;
        if (amaGuia.getAmaInventarioArmaList() != null) {
            for (AmaInventarioArma invArma : amaGuia.getAmaInventarioArmaList()) {
                //Obtiene el tipo de articulo del arma seleccionada y prepara el formulario para mostrarlo.
                tipoArt = invArma.getModeloId().getTipoArticuloId();
                break;
            }
        }
        return tipoArt == null ? null : JsfUtil.convertirNombrePropio(tipoArt.getNombre());
    }

    /**
     * FUNCIÓN QUE OBTIENE LA DIRECCIÓN DEL ALMACÉN SUCAMEC SEGÚN EL TIPO DE
     * GUIA (DEPÓSITO/DEVOLUCIÓN)
     *
     * @param amaGuia
     * @return
     */
    public SbDireccion obtenerDireccionSedeSegunActa(AmaGuiaTransito amaGuia) {
        SbDireccion res = null;
        if (amaGuia != null) {
            String tipoActa = StringUtil.VACIO;
            if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEPOSITO).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
                res = amaGuia.getDireccionDestinoId();
            } else if (amaGuiaTransitoGenericoController.listaIdTipoGuiaBus(DEVOLUCION).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
                res = amaGuia.getDireccionOrigenId();
            }
        }
        return res;
    }

    public List<AmaInventarioAccesorios> obtenerAccesoriosVer(AmaGuiaTransito amaGuia) {
        List<AmaInventarioAccesorios> listRes = null;
        obtenerInvArma(amaGuia);
        if (invArmaDeposito != null && (!JsfUtil.isNullOrEmpty(invArmaDeposito.getAmaInventarioAccesoriosList()))) {
            listRes = new ArrayList<>();
            for (AmaInventarioAccesorios acces : invArmaDeposito.getAmaInventarioAccesoriosList()) {
                if (acces.getActivo() == JsfUtil.TRUE) {
                    listRes.add(acces);
                }
            }
        }
        invArmaDeposito = null;
        return listRes;
    }

    public List<AmaGuiaMuniciones> obtenerListMuniActivos(AmaGuiaTransito amaGuia) {
        if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaGuiaMunicionesList())) {
            listGuiaMunicion = new ArrayList<>();
            for (AmaGuiaMuniciones muniTemp : amaGuia.getAmaGuiaMunicionesList()) {
                if (muniTemp.getActivo() == JsfUtil.TRUE) {
                    getListGuiaMunicion().add(muniTemp);
                }
            }
        } else {
            setListGuiaMunicion(null);
        }
        //Return lista se usa solamente para el reporte en pdf
        return getListGuiaMunicion();
    }

    public void obtenerInvArma(AmaGuiaTransito amaGuia) {
        for (AmaInventarioArma invArma : amaGuia.getAmaInventarioArmaList()) {
            if (invArma.getActivo() == JsfUtil.TRUE) {
                invArmaDeposito = invArma;
                if (!JsfUtil.isNullOrEmpty(invArma.getAmaInventarioAccesoriosList())) {
                    listAccesArma = new ArrayList<>();
                    for (AmaInventarioAccesorios acc : invArmaDeposito.getAmaInventarioAccesoriosList()) {
                        if (acc.getActivo() == JsfUtil.TRUE) {
                            listAccesArma.add(acc);
                        }
                    }
                }

            } else {
                JsfUtil.mensajeError("El arma ha sido eliminado.");
            }

            break;
        }
    }

    public AmaInventarioArma getInvArmaDeposito() {
        return invArmaDeposito;
    }

    public void setInvArmaDeposito(AmaInventarioArma invArmaDeposito) {
        this.invArmaDeposito = invArmaDeposito;
    }

    /**
     * OBTENER LISTADO DE ACCESORIOS EN 4 COLUMNAS PARA TABLA
     *
     * @author Gino Chávez
     * @version 2.0
     * @param listAcces Listado de Accesorios para preparar Tabla de reporte
     * @return Listado de Accesorios
     */
    public List<Map> obtenerAccesMap(List<AmaInventarioAccesorios> listAcces) {
        short cont = 0;
        List<Map> lista = new ArrayList();
        Map mMap = new HashMap();
        if (listAcces != null) {
            for (AmaInventarioAccesorios acces : listAcces) {
                cont++;
                switch (cont) {
                    case 1:
                        mMap.put("cant1", String.valueOf(acces.getCantidad()));
                        mMap.put("nombre1", acces.getArticuloConexoId().getNombre());
                        break;
                    case 2:
                        mMap.put("cant2", String.valueOf(acces.getCantidad()));
                        mMap.put("nombre2", acces.getArticuloConexoId().getNombre());
                        break;
                    case 3:
                        mMap.put("cant3", String.valueOf(acces.getCantidad()));
                        mMap.put("nombre3", acces.getArticuloConexoId().getNombre());
                        lista.add(mMap);
                        mMap = new HashMap();
                        cont = 0;
                        break;
                }
            }
            if (cont < 3 && cont > 0) {
                lista.add(mMap);
            }
        }

        return lista;
    }

    public StreamedContent verActaMunicionEmpadronamientoPdf(Long idActa) {
        try {
            //actaInternamientoId
            //String jasper = "/aplicacion/amaDepositoArmas/reportes/DepositoDevolucion.jasper";
            String jasper = "/aplicacion/citas/rep/DepositoDevolucion.jasper";
            List<AmaGuiaTransito> agt = new ArrayList();
            AmaGuiaTransito amaGuia = ejbAmaGuiaTransitoFacade.find(idActa);
            agt.add(amaGuia);
            if ("TP_GTGAMAC_FIN".equalsIgnoreCase(amaGuia.getEstadoId().getCodProg())) {
                StreamedContent pdf = null;
                try {
                    pdf = JsfUtil.obtenerArchivo((ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_actasDepDev").getValor()), amaGuia.getId() + ".pdf", amaGuia.getId() + ".pdf", "application/pdf");
                } catch (Exception e) {
                }
                if (pdf == null) {
                    if (subirActaPdf(amaGuia, jasper, agt)) {
                        return JsfUtil.obtenerArchivo((ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_actasDepDev").getValor()), amaGuia.getId() + ".pdf", amaGuia.getId() + ".pdf", "application/pdf");
                    }
                } else {
                    return pdf;
                }
            } else {
                return JsfUtil.generarReportePdf(jasper, agt, parametrosAlmacenPdf(amaGuia), amaGuia.getId() + "_borrador.pdf");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: AmaGuiaTransito.reporte :", ex);
        }
        return null;
    }
    
}
