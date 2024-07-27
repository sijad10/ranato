/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.jsf.util;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;
import pe.gob.sucamec.notificacion.beans.NeDocumentoFacade;
import pe.gob.sucamec.notificacion.data.NeArchivo;
import pe.gob.sucamec.notificacion.data.NeDocumento;
import pe.gob.sucamec.notificacion.data.NeEvento;
import pe.gob.sucamec.bdintegrado.bean.AmaMaestroArmasFacade;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbDistrito;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.bdintegrado.data.SbReciboRegistro;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;
import pe.gob.sucamec.bdintegrado.bean.AmaArmaFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaArmaInventarioDifFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaCatalogoFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaDocumentoFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaGuiaTransitoFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaInventarioAccesoriosFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaInventarioArmaFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaTarjetaPropiedadFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaResolucionFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaTarjetaPropiedadFacade;
//import pe.gob.sucamec.bdintegrado.beans.Rma1369Facade;
import pe.gob.sucamec.rma1369.bean.Rma1369Facade;
import pe.gob.sucamec.bdintegrado.bean.SbNumeracionFacade;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;
import pe.gob.sucamec.bdintegrado.bean.SbReciboRegistroFacade;
import pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoGamacFacade;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
import pe.gob.sucamec.bdintegrado.data.AmaArmaInventarioDif;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaDocumento;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaMuniciones;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioAccesorios;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArtconexo;
import pe.gob.sucamec.bdintegrado.data.AmaLicenciaDeUso;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.AmaResolucion;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.sistemabase.data.TipoBase;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.jsf.AmaDepositoArmasController;
import pe.gob.sucamec.bdintegrado.bean.AmaDevolucionArmasController;
import pe.gob.sucamec.bdintegrado.jsf.AmaGuiaTransitoController;
import static pe.gob.sucamec.bdintegrado.jsf.AmaGuiaTransitoController.toDate;
import pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade;
import pe.gob.sucamec.bdintegrado.bean.ProcesoFacade;
import pe.gob.sucamec.bdintegrado.data.Documento;
import pe.gob.sucamec.bdintegrado.data.Proceso;
import pe.gob.sucamec.bdintegrado.bean.AmaTurnoActaFacade;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.renagi.jsf.util.StringUtil;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.sistemabase.data.SbTipo;
import pe.gob.sucamec.sistemabase.beans.SbTipoFacade;
        
/**
 *
 * @author gchavez
 */
@Named("amaGuiaTransitoGenericoController")
@SessionScoped
public class AmaGuiaTransitoGenericoController implements Serializable {

//    private String origen;
    private static String deposito = "DEP";
    private static String devolucion = "DEV";//DEVOL
    private static String difDeDepDev = "DIF_DEP_DEV";
    private static final Long L_UNO_NEG = -1L, L_DOS_NEG = -2L, L_TRES_NEG = -3L, L_CUATRO_NEG = -4L, L_CERO = 0L, L_UNO = 1L, L_DOS = 2L, L_TRES = 3L, L_CUATRO = 4L, L_CINCO = 5L, L_SEIS = 6L, L_SIETE = 7L, L_OCHO = 8L;

    @EJB
    private TipoGamacFacade ejbTipoGamacFacade;
    @EJB
    private SbUsuarioFacade ejbSbUsuarioFacade;
    @EJB
    private TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private AmaGuiaTransitoFacade ejbAmaGuiaTransitoFacade;
    @EJB
    private AmaInventarioArmaFacade ejbAmaInventarioArmaFacade;
    @EJB
    private SbReciboRegistroFacade ejbSbReciboRegistroFacade;
    @EJB
    private AmaInventarioAccesoriosFacade ejbAmaInventarioAccesoriosFacade;
    @EJB
    private AmaArmaInventarioDifFacade ejbAmaArmaInventarioDifFacade;
    @EJB
    private AmaArmaFacade ejbAmaArmaFacade;
    @EJB
    private AmaTarjetaPropiedadFacade ejbAmaTarjetaPropiedadFacade;
    @EJB
    private ProcesoFacade ejbProcesoFacade;
    @EJB
    private Rma1369Facade ejbRma1369Facade;
    @EJB
    private AmaTurnoActaFacade ejbAmaTurnoActaFacade;
    @EJB
    private SbNumeracionFacade ejbSbNumeracionFacade;
    @EJB
    private NeDocumentoFacade ejbNeDocumentoFacade;
    @EJB
    private AmaDocumentoFacade ejbAmaDocumentoFacade;
    @EJB
    private ExpedienteFacade ejbExpedienteFacade;
    @EJB
    private AmaCatalogoFacade ejbAmaCatalogoFacade;
    @EJB
    private SbParametroFacade ejbSbParametroFacade;
    @EJB
    private AmaResolucionFacade ejbAmaResolucionFacade;
    @EJB
    private AmaMaestroArmasFacade ejbAmaMaestroArmasFacade;
    @EJB
    private AmaTarjetaPropiedadFacade ejbAmaTarjetaProdiedadFacade;
    @EJB
    private SbTipoFacade ejbSbTipoFacade;
    
    @Inject
    private AmaDepositoArmasController amaDepositoArmasController;
    @Inject
    private AmaDevolucionArmasController amaDevolucionArmasController;
    @Inject
    private AmaGuiaTransitoController amaGuiaTransitoController;

    /**
     * FUNCIÓN QUE OBTIENE LA CADENA CON LOS IDS DE LOS TIPOS DE GUÍAS A LISTAR.
     *
     * @author Gino Chávez
     * @param origenBusq, String. Pantalla origen:GUÍAS DE TRÁNSITO, DEPÓSITO O
     * DEVOLUCIÓN.
     * @return Cadena con ids.
     */
    public String listaIdTipoGuiaBus(String origenBusq) {
        String cadena = StringUtil.VACIO;
        List<Long> listRes = new ArrayList<>();
        List<TipoGamac> listaTemp = null;
        if (deposito.equalsIgnoreCase(origenBusq)) {
            String codProg = StringUtil.VACIO;
            for (int i = 0; i < 2; i++) {
                switch (i) {
                    case 0:
                        codProg = "TP_GTGAMAC_ALM";
                        break;
                    case 1:
                        codProg = "TP_GTALM_DISADU";
                        break;
                    default:
                        break;
                }
                listaTemp = ejbTipoGamacFacade.selectTipoGamac(codProg);
                for (TipoGamac tipoGamac : listaTemp) {
                    listRes.add(tipoGamac.getId());
                }
                listaTemp.clear();
            }
        } else if (devolucion.equalsIgnoreCase(origenBusq)) {
            listaTemp = ejbTipoGamacFacade.selectTipoGamac("TP_GTGAMAC_DEV");
            for (TipoGamac tipoGamac : listaTemp) {
                listRes.add(tipoGamac.getId());
            }
            listaTemp.clear();
        } else if (difDeDepDev.equalsIgnoreCase(origenBusq)) {
            String codProg = StringUtil.VACIO;
            for (int i = 0; i < 3; i++) {
                switch (i) {
                    case 0:
                        codProg = "TP_GTGAMAC";
                        break;
                    case 1:
                        codProg = "TP_GTGAMAC_DON";
                        break;
                    case 2:
                        codProg = "TP_GTGAMAC_SUC";
                        break;
                    default:
                        break;
                }
                listaTemp = ejbTipoGamacFacade.selectTipoGamac(codProg);
                for (TipoGamac tipoGamac : listaTemp) {
                    if (!"TP_GTGAMAC".equals(codProg)) {
                        listRes.add(tipoGamac.getId());
                    } else if (!codProg.equals(tipoGamac.getCodProg())
                            && !"TP_GTGAMAC_ALM".equals(tipoGamac.getCodProg())
                            && !"TP_GTGAMAC_DEV".equals(tipoGamac.getCodProg())) {
                        listRes.add(tipoGamac.getId());
                    }
                }
                listaTemp.clear();
            }
        }
        int tam = listRes.size();
        int j = 1;
        for (Long id : listRes) {
            cadena += id;
            if (j < tam) {
                cadena += ",";
            }
            j++;
        }
        return cadena;
    }

    /**
     * Función que obtiene la situación del arma según el tipo de guía.
     *
     * @author Gino Chávez
     * @param tipoGuia
     * @return Código Programador de la situación del arma.
     */
    public String obtenerSituacionSegunTG(TipoGamac tipoGuia) {
        String codProg = StringUtil.VACIO;
        switch (tipoGuia.getCodProg()) {
            case "TP_GTGAMAC_CFA":
                codProg = "TP_SITU_EN_CFA";
                break;
            case "TP_GTGAMAC_REC":
                codProg = "TP_SITU_POS_LC";
                break;
            case "TP_GTGAMAC_EXH":
                codProg = "TP_SITU_EXH";
                break;
            case "TP_GTGAMAC_POL":
                codProg = "TP_SITU_EGT";
                break;
            case "TP_GTGAMAC_IMP":
                codProg = "TP_SITU_INT";
                break;
            case "TP_GTGAMAC_ALM":
                codProg = "TP_SITU_INT";
                break;
            case "TP_GTGAMAC_DEV":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_DON":
            case "TP_GTDON_ASIGN":
                codProg = "TP_SITU_DON";
                break;
            case "TP_GTGAMAC_AGE":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_SUC":
                codProg = "TP_SITU_INT";
                break;
            case "TP_GTGAMAC_COL":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_MUN":
                codProg = StringUtil.VACIO;//No existe arma
                break;
            case "TP_GTGAMAC_REI":
                codProg = "TP_SITU_INT";
                break;
            case "TP_GTGAMAC_SAL":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_DDV":
                codProg = "TP_SITU_POS";
                break;
            default:
                switch (tipoGuia.getTipoId().getCodProg()) {
                    case "TP_GTGAMAC_ALM":
                        switch (tipoGuia.getCodProg()) {
                            case "TP_GTALM_DEC":
                                codProg = "TP_SITU_INT";
                                break;
                            case "TP_GTALM_INC":
                                codProg = "TP_SITU_INT";
                                break;
                            default:
                                codProg = "TP_SITU_INT";
                                break;
                        }
                        break;
                    case "TP_GTGAMAC_DEV":
                        codProg = "TP_SITU_POS";
                        break;
                    case "TP_GTGAMAC_SUC":
                        codProg = "TP_SITU_INT";
                        break;
                    case "TP_GTGAMAC_DON":
                        switch (tipoGuia.getCodProg()) {
                            case "TP_GTDON_DONA":
                                codProg = "TP_SITU_DON";
                                break;
                            case "TP_GTDON_DEST":
                                codProg = "TP_SITU_DES";
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        switch (tipoGuia.getTipoId().getTipoId().getCodProg()) {
                            case "TP_GTGAMAC_ALM":
                                codProg = "TP_SITU_INT";
                                break;
                            default:
                                break;
                        }
                        break;
                }
        }
        return codProg;
    }

    /**
     * FUNCION QUE OBTIENE EL VALOR DE LA CONDICION DE ALMACEN SEGUN SITUACION
     * DEL ARMA
     *
     * @param situacionId
     * @return
     */
    public short obtenerCondicionSegunSituacionId(TipoGamac situacionId) {
        short resp = JsfUtil.FALSE;
        if (situacionId != null) {
            switch (situacionId.getCodProg()) {
                case "TP_SITU_DEC":
                //DECOMISADO
                case "TP_SITU_INC":
                //INCAUTADO
                case "TP_SITU_INT":
                    //INTERNADO
                    resp = JsfUtil.TRUE;
                    break;
            }
        }
        return resp;

    }

    /**
     * Función que obtiene la situación anterior según tipo de guía
     *
     * @author Gino Chávez
     * @param tipoGuia
     * @return Código Programador de la situación del arma.
     */
    public String obtenerSituacionAnteriorSegunTG(TipoGamac tipoGuia) {
        String codProg = StringUtil.VACIO;
        switch (tipoGuia.getCodProg()) {
            case "TP_GTGAMAC_REC":
                codProg = "TP_SITU_INT";
                break;
            case "TP_GTGAMAC_EXH":
                codProg = "TP_SITU_INT";
                break;
            case "TP_GTGAMAC_POL":
                codProg = StringUtil.VACIO;
                break;
            case "TP_GTGAMAC_IMP":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_DON":
                codProg = "TP_SITU_INT";
                break;
            case "TP_GTGAMAC_AGE":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_SUC":
                codProg = "TP_SITU_INT";
                break;
            case "TP_GTGAMAC_COL":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_MUN":
                codProg = StringUtil.VACIO;//No existe arma
                break;
            case "TP_GTGAMAC_REI":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_SAL":
                codProg = "TP_SITU_POS";
                break;
            case "TP_GTGAMAC_DDV":
//                codProg = "TP_SITU_POS";
                codProg = "TP_SITU_INT";
                break;
            default:
                switch (tipoGuia.getTipoId().getCodProg()) {
                    case "TP_GTGAMAC_SUC":
                        codProg = "TP_SITU_INT";
                        break;
                    case "TP_GTGAMAC_DON":
                        codProg = "TP_SITU_INT";
                        break;
                    default:
                        switch (tipoGuia.getTipoId().getTipoId().getCodProg()) {
                            case "TP_GTGAMAC_ALM":
                                codProg = "TP_SITU_INT";
                                break;
                            default:
                                break;
                        }
                        break;
                }
        }
        return codProg;
    }

    /**
     * FUNCIÓN QUE OBTIENE EL OBJETO USUARIO
     *
     * @author Gino Chávez
     * @param usuIn
     * @return Usuario
     */
    public SbUsuario objetoUsuario(DatosUsuario usuIn) {
        List<SbUsuario> listRes = ejbSbUsuarioFacade.selectUsuarioXLogin(usuIn.getLogin());
        List<SbUsuario> listTemp = new ArrayList<>();
        SbUsuario usu = new SbUsuario();
        listTemp.addAll(listRes);
        for (SbUsuario u : listTemp) {
            if (!u.getPersonaId().getId().equals(usuIn.getPersona().getId())) {
                listRes.remove(u);
            }
        }
        if (!listRes.isEmpty()) {
            usu = listRes.get(0);
        }
        return usu;
    }

    /**
     * Función que obtiene un valor que identifica la tabla en el cual se
     * buscará el arma, ésto según el tipo y subtipo de guía. 1=Buscar en tabla
     * AmaArma, 2=Buscar en tabla AmaInventarioArma, 3=Buscar en ambas tablas.
     *
     * @author Gino Chávez
     * @param origen, Variable de entrada que identifica de que controlador se
     * invoca a la función.
     * @param idTipoGuia
     * @param idSubTipoGuia
     * @return
     */
    public Long validarOrigenArma(String origen, TipoGamac idTipoGuia, TipoGamac idSubTipoGuia) {
        Long res = L_UNO_NEG;
        Boolean activarFlag = Boolean.FALSE;
        amaDepositoArmasController.setFlagRegInvArma(Boolean.FALSE); //AQUI
        amaGuiaTransitoController.setFlagRegInvArma(Boolean.FALSE);
        if (idTipoGuia != null) {
            switch (idTipoGuia.getCodProg()) {
                case "TP_GTGAMAC_REC":
                    res = L_TRES;
                    break;
                case "TP_GTGAMAC_EXH":
                    res = L_DOS;
                    break;
                case "TP_GTGAMAC_POL":
                    res = L_TRES;
                    break;
                case "TP_GTGAMAC_IMP":
                    res = L_DOS;
                    activarFlag = Boolean.TRUE;
                    break;
                case "TP_GTGAMAC_ALM":
                    if (idSubTipoGuia != null) {
                        switch (idSubTipoGuia.getCodProg()) {
                            case "TP_GTALM_MANVOL":
                                //res = L_UNO;
                                res = L_TRES;
                                activarFlag = Boolean.TRUE;
                                break;
                            case "TP_GTALM_DIPJMP":
                                res = L_UNO;
                                activarFlag = Boolean.TRUE;
                                break;
                            case "TP_GTALM_FALTIT":
                                activarFlag = Boolean.TRUE;
                                res = L_UNO;
                                break;
                            case "TP_GTALM_DISADU":
                                res = L_TRES;
                                activarFlag = Boolean.TRUE;
                                break;
                            case "TP_GTALM_DEC":
                                res = L_TRES;
                                activarFlag = Boolean.TRUE;
                                break;
                            case "TP_GTALM_INC":
                                res = L_TRES;
                                activarFlag = Boolean.TRUE;
                                break;
                            case "TP_GTALM_VENCAN":
                                res = L_UNO;
                                break;
                            case "TP_GTALM_VENPLA":
                                res = L_UNO;
                                break;
                            case "TP_GTALM_INDEF":
                                activarFlag = Boolean.TRUE;
                                break;
                            default:
                                break;
                        }
                    } else {
                        res = L_CERO;
                    }
                    break;
                case "TP_GTGAMAC_DEV":
                    if (idSubTipoGuia != null) {
                        switch (idSubTipoGuia.getCodProg()) {
                            case "TP_GTDEV_MANJUD":
                                //res = L_UNO;
                                break;
                            case "TP_GTDEV_DEPTEM":
                                //res = L_UNO;
                                break;
                            case "TP_GTDEV_VENREC":
                                //res = L_UNO;
                                break;
                            case "TP_GTDEV_RESINC":
                                //res = L_TRES;
                                break;
                            default:
                                break;
                        }
                    } else {
                        res = L_CERO;
                    }
                    break;
                case "TP_GTGAMAC_DON":
                    res = L_DOS;
                    break;
                case "TP_GTGAMAC_AGE":
                    res = L_UNO;
                    break;
                case "TP_GTGAMAC_SUC":
                    res = L_DOS;
                    break;
                case "TP_GTGAMAC_COL":
                    res = L_UNO;
                    break;
                case "TP_GTGAMAC_MUN":
                    //ORIGEN TABLA AMA_MUNICION
                    res = L_CUATRO;
                    break;
                case "TP_GTGAMAC_REI":
                    activarFlag = Boolean.TRUE;
                    res = L_DOS;
                    break;
                case "TP_GTGAMAC_SAL":
                    res = L_DOS;
                    break;
                case "TP_GTGAMAC_DDV":
                    res = L_TRES;
                    break;
                case "TP_GTGAMAC_CFA":
                    res = L_DOS;
                    break;
                default:
                    break;
            }
            if (activarFlag) {
                if (Objects.equals(origen, deposito)) {
                    amaDepositoArmasController.setFlagRegInvArma(Boolean.TRUE); //AQUI
//                    amaGuiaTransitoController.setFlagRegInvArma(Boolean.TRUE);
                } else {
                    amaGuiaTransitoController.setFlagRegInvArma(Boolean.TRUE);
                }
            }
        }
        return res;
    }

    /**
     *
     **FUNCION QUE CREA UN NUEVO REGISTRO DE GUIA CON LOS DATOS DE LA GUIA
     * ORIGINAL. A LA VEZ DE INACTIVAR LA GUIA ORIGINAL.
     *
     * @author Gino Chávez
     * @param origen
     * @param tipo
     * @param licArma
     * @param usuIn
     * @param amaGuiaOrg
     * @return
     */
    public AmaGuiaTransito copiarAnularGuia(Boolean origen, String tipo, Map licArma, DatosUsuario usuIn, AmaGuiaTransito amaGuiaOrg) {
        AmaGuiaTransito newGuia = new AmaGuiaTransito();
        try {
            if (amaGuiaOrg != null) {
                newGuia.setActivo(JsfUtil.TRUE);
                newGuia.setDependenciaInst(amaGuiaOrg.getDependenciaInst());
                newGuia.setDependenciaProc(amaGuiaOrg.getDependenciaProc());
                newGuia.setDestinoAlmacenAduana(amaGuiaOrg.getDestinoAlmacenAduana());
                newGuia.setDestinoPuerto(amaGuiaOrg.getDestinoPuerto());
                newGuia.setDireccionDestinoId(amaGuiaOrg.getDireccionDestinoId());
                newGuia.setDireccionOrigenId(amaGuiaOrg.getDireccionOrigenId());
                newGuia.setInstitucionOrdenaId(amaGuiaOrg.getInstitucionOrdenaId());
                newGuia.setMotivoId(amaGuiaOrg.getMotivoId());
                if (!origen) {
                    newGuia.setFechaLlegada(amaGuiaOrg.getFechaLlegada());
                    newGuia.setNroExpediente(amaGuiaOrg.getNroExpediente());
                }
                newGuia.setOrigenAlmacenAduana(amaGuiaOrg.getOrigenAlmacenAduana());
                newGuia.setOrigenPuerto(amaGuiaOrg.getOrigenPuerto());
                newGuia.setProcedenciaId(amaGuiaOrg.getProcedenciaId());
                newGuia.setRepresentanteId(amaGuiaOrg.getRepresentanteId());
                newGuia.setResponsableIngsalId(amaGuiaOrg.getResponsableIngsalId());
                newGuia.setRgReferenciada(amaGuiaOrg.getRgReferenciada());
                newGuia.setSolicitanteId(amaGuiaOrg.getSolicitanteId());
                newGuia.setSolicitudRecojoId(amaGuiaOrg.getSolicitudRecojoId());
                newGuia.setTipoDestino(amaGuiaOrg.getTipoDestino());
                newGuia.setTipoGuiaId(amaGuiaOrg.getTipoGuiaId());
                newGuia.setTipoIngreso(amaGuiaOrg.getTipoIngreso());
                newGuia.setTipoOrigen(amaGuiaOrg.getTipoOrigen());
                newGuia.setEstadoId(ejbTipoBaseFacade.tipoPorCodProg("TP_GTGAMAC_CRE"));
                newGuia.setUsuarioId(ejbSbUsuarioFacade.obtenerUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()));
                if (!JsfUtil.isNullOrEmpty(amaGuiaOrg.getAmaInventarioAccesoriosList())) {
                    newGuia.setAmaInventarioAccesoriosList(copiarDatosAccesorios(amaGuiaOrg.getAmaInventarioAccesoriosList(), null, newGuia));
                }

                if (!JsfUtil.isNullOrEmpty(amaGuiaOrg.getAmaInventarioArmaList())) {
                    if (origen) {
                        newGuia.setAmaInventarioArmaList(new ArrayList<AmaInventarioArma>());
                        AmaInventarioArma newArma = null;
                        for (AmaInventarioArma invArma : amaGuiaOrg.getAmaInventarioArmaList()) {
                            if (invArma.getActivo() == JsfUtil.TRUE && invArma.getActual() == JsfUtil.TRUE) {
                                newArma = new AmaInventarioArma();
                                newArma = copiaDatosArma(origen, invArma);
                                newArma.setId(JsfUtil.tempIdN());
                                newArma.setCodigo(null);
                                newArma.setAmaInventarioAccesoriosList(amaDepositoArmasController.getLstAccesorios()); //AQUI
//                                newArma.setAmaInventarioAccesoriosList(amaGuiaTransitoController.getLstAccesorios());
                                newGuia.getAmaInventarioArmaList().add(newArma);
                                if (Objects.equals(tipo, deposito)) {
                                    llenarListArmasTemp(origen, licArma, newArma, Boolean.FALSE);
                                }
                            }
                        }
                        amaDepositoArmasController.setLstAccesorios(null); //AQUI
//                        amaGuiaTransitoController.setLstAccesorios(null);
                    } else {
                        newGuia.setAmaInventarioArmaList(amaGuiaOrg.getAmaInventarioArmaList());
                    }
                }
                if (!JsfUtil.isNullOrEmpty(amaGuiaOrg.getAmaArmaList())) {
                    newGuia.setAmaArmaList(amaGuiaOrg.getAmaArmaList());
                }
                if (!JsfUtil.isNullOrEmpty(amaGuiaOrg.getAmaGuiaMunicionesList())) {
                    AmaGuiaMuniciones muniNew = null;
                    newGuia.setAmaGuiaMunicionesList(new ArrayList<AmaGuiaMuniciones>());
                    for (AmaGuiaMuniciones muni : amaGuiaOrg.getAmaGuiaMunicionesList()) {
                        muniNew = new AmaGuiaMuniciones();
                        if (muni.getActivo() == JsfUtil.TRUE) {
                            muniNew.setId(JsfUtil.tempIdN());
                            muniNew.setActivo(JsfUtil.TRUE);
                            muniNew.setCantidad(muni.getCantidad());
                            muniNew.setGuiaTransitoId(newGuia);
                            muniNew.setMunicionId(muni.getMunicionId());
                            muniNew.setPeso(muni.getPeso());
                            muniNew.setUmedidaId(muni.getUmedidaId());
                            newGuia.getAmaGuiaMunicionesList().add(muniNew);
                        }
                    }
                }
                if (!JsfUtil.isNullOrEmpty(amaGuiaOrg.getAmaDocumentoList())) {
                    newGuia.setAmaDocumentoList(amaGuiaOrg.getAmaDocumentoList());
                }
                if (!JsfUtil.isNullOrEmpty(amaGuiaOrg.getSbPersonaList())) {
                    newGuia.setSbPersonaList(amaGuiaOrg.getSbPersonaList());
                }
                if (Objects.equals(tipo, deposito)) {
                    cambiarEstadoCascadeActa(amaGuiaOrg, JsfUtil.FALSE);
                } else {
                    amaGuiaOrg.setActivo(JsfUtil.FALSE);
                }
                return newGuia;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     **FUNCION QUE CREA UN NUEVO REGISTRO DE GUIA CON LOS DATOS DE LA GUIA
     * ORIGINAL. A LA VEZ DE INACTIVAR LA GUIA ORIGINAL.
     *
     * @author Gino Chávez
     * @param amaGuiaOrg
     * @return
     */
    public AmaGuiaTransito copiarGuia(AmaGuiaTransito amaGuiaOrg) {
        AmaGuiaTransito newGuia = null;
        try {
            if (amaGuiaOrg != null) {
                newGuia = new AmaGuiaTransito();
                newGuia.setActivo(JsfUtil.TRUE);
                newGuia.setDependenciaInst(amaGuiaOrg.getDependenciaInst());
                newGuia.setDependenciaProc(amaGuiaOrg.getDependenciaProc());
                newGuia.setDestinoAlmacenAduana(amaGuiaOrg.getDestinoAlmacenAduana());
                newGuia.setDestinoPuerto(amaGuiaOrg.getDestinoPuerto());
                newGuia.setDireccionDestinoId(amaGuiaOrg.getDireccionDestinoId());
                newGuia.setDireccionOrigenId(amaGuiaOrg.getDireccionOrigenId());
                newGuia.setInstitucionOrdenaId(amaGuiaOrg.getInstitucionOrdenaId());
                newGuia.setMotivoId(amaGuiaOrg.getMotivoId());
                newGuia.setFechaLlegada(amaGuiaOrg.getFechaLlegada());
                newGuia.setNroExpediente(amaGuiaOrg.getNroExpediente());
                newGuia.setNroGuia(amaGuiaOrg.getNroGuia());
                newGuia.setOrigenAlmacenAduana(amaGuiaOrg.getOrigenAlmacenAduana());
                newGuia.setOrigenPuerto(amaGuiaOrg.getOrigenPuerto());
                newGuia.setProcedenciaId(amaGuiaOrg.getProcedenciaId());
                newGuia.setRepresentanteId(amaGuiaOrg.getRepresentanteId());
                newGuia.setResponsableIngsalId(amaGuiaOrg.getResponsableIngsalId());
                newGuia.setRgReferenciada(amaGuiaOrg.getRgReferenciada());
                newGuia.setSolicitanteId(amaGuiaOrg.getSolicitanteId());
                newGuia.setSolicitudRecojoId(amaGuiaOrg.getSolicitudRecojoId());
                newGuia.setTipoDestino(amaGuiaOrg.getTipoDestino());
                newGuia.setTipoGuiaId(amaGuiaOrg.getTipoGuiaId());
                newGuia.setTipoIngreso(amaGuiaOrg.getTipoIngreso());
                newGuia.setTipoOrigen(amaGuiaOrg.getTipoOrigen());
                newGuia.setTipoOperacionId(amaGuiaOrg.getTipoOperacionId());
                newGuia.setTipoRegistroId(amaGuiaOrg.getTipoRegistroId());
                newGuia.setEstadoId(amaGuiaOrg.getEstadoId());
                newGuia.setUsuarioId(amaGuiaOrg.getUsuarioId());
                copiarGuiaMuniciones(amaGuiaOrg, newGuia);
                JsfUtil.borrarIdsN(newGuia.getAmaGuiaMunicionesList());
                if (!JsfUtil.isNullOrEmpty(amaGuiaOrg.getAmaInventarioAccesoriosList())) {
                    newGuia.setAmaInventarioAccesoriosList(amaGuiaOrg.getAmaInventarioAccesoriosList());
                    for (AmaInventarioAccesorios acces : newGuia.getAmaInventarioAccesoriosList()) {
                        acces.setGuiaTransitoId(newGuia);
                    }
                }
                newGuia.setAmaInventarioArmaList(amaGuiaOrg.getAmaInventarioArmaList());//Tabla intermedia
                newGuia.setAmaArmaList(amaGuiaOrg.getAmaArmaList());//Tabla intermedia
//                if (!JsfUtil.isNullOrEmpty(amaGuiaOrg.getAmaGuiaMunicionesList())) {
//                    newGuia.setAmaGuiaMunicionesList(amaGuiaOrg.getAmaGuiaMunicionesList());
//                    for (AmaGuiaMuniciones muni : newGuia.getAmaGuiaMunicionesList()) {
//                        muni.setGuiaTransitoId(newGuia);
//                    }
//                }
                newGuia.setAmaDocumentoList(amaGuiaOrg.getAmaDocumentoList());//Tabla intermedia
                newGuia.setSbPersonaList(amaGuiaOrg.getSbPersonaList());//Tabla intermedia
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newGuia;
    }

    public void copiarGuiaMuniciones(AmaGuiaTransito amaGuiaOrg, AmaGuiaTransito newGuia) {
        if (!JsfUtil.isNullOrEmpty(amaGuiaOrg.getAmaGuiaMunicionesList())) {
            AmaGuiaMuniciones muniNew = null;
            newGuia.setAmaGuiaMunicionesList(new ArrayList<AmaGuiaMuniciones>());
            for (AmaGuiaMuniciones muni : amaGuiaOrg.getAmaGuiaMunicionesList()) {
                muniNew = new AmaGuiaMuniciones();
                if (muni.getActivo() == JsfUtil.TRUE) {
                    muniNew.setId(JsfUtil.tempIdN());
                    muniNew.setActivo(JsfUtil.TRUE);
                    muniNew.setCantidad(muni.getCantidad());
                    muniNew.setGuiaTransitoId(newGuia);
                    muniNew.setMunicionId(muni.getMunicionId());
                    muniNew.setPeso(muni.getPeso());
                    muniNew.setUmedidaId(muni.getUmedidaId());
                    newGuia.getAmaGuiaMunicionesList().add(muniNew);
                }
            }
        }
    }

    /**
     * Función que copia los datos del arma para poder usarla como un registro
     * nuevo posteriormente.
     *
     * @author Gino Chavez
     * @param origen
     * @param userSession
     * @param invArma
     * @return
     */
    public AmaInventarioArma copiaDatosArma(Boolean origen, AmaInventarioArma invArma) {
        AmaInventarioArma res = new AmaInventarioArma();

        res.setAmaInventarioAccesoriosList(new ArrayList());
        res.setCierreInventario(JsfUtil.TRUE);
        res.setActivo(JsfUtil.TRUE);
        res.setActual(JsfUtil.FALSE);
        res.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        res.setArmaId(invArma.getArmaId());
        res.setAudNumIp(JsfUtil.getIpAddress());
        res.setCodigo(invArma.getCodigo());
        res.setNroRua(invArma.getNroRua());
        res.setAmbienteId(invArma.getAmbienteId());
        res.setAnaquel(invArma.getAnaquel());
        res.setColumna(invArma.getColumna());
        res.setFila(invArma.getFila());
        res.setAlmacenSucamecId(invArma.getAlmacenSucamecId());
        res.setEstadoCachaId(invArma.getEstadoCachaId());
        res.setEstadoconservacionId(invArma.getEstadoconservacionId());
        res.setEstadofuncionalId(invArma.getEstadofuncionalId());
        res.setEstadoserieId(invArma.getEstadoserieId());
        res.setExteriorId(invArma.getExteriorId());
        res.setMaterialCachaId(invArma.getMaterialCachaId());
        res.setMecanismoId(invArma.getMecanismoId());
        res.setModalidadTiroId(invArma.getModalidadTiroId());
        res.setSerie(invArma.getSerie());
        res.setModeloId(invArma.getModeloId());
        res.setNovedadCanonId(invArma.getNovedadCanonId());
        res.setObservacion(invArma.getObservacion());
        res.setPeso(invArma.getPeso());
        res.setPropietarioId(invArma.getPropietarioId());
        res.setPosibleTitularId(invArma.getPosibleTitularId());
        res.setSituacionId(invArma.getSituacionId());
        res.setUmedidaId(invArma.getUmedidaId());
        res.setCondicionAlmacen(invArma.getCondicionAlmacen());
        res.setTipoInternamientoId(invArma.getTipoInternamientoId());
        res.setMaterialCantoneraId(invArma.getMaterialCantoneraId());
        res.setMaterialCulataId(invArma.getMaterialCulataId());
        res.setMaterialEmpunaduraId(invArma.getMaterialEmpunaduraId());
        res.setMaterialGuardamanoId(invArma.getMaterialGuardamanoId());
        res.setEstadoCantoneraId(invArma.getEstadoCantoneraId());
        res.setEstadoCulataId(invArma.getEstadoCulataId());
        res.setEstadoEmpunaduraId(invArma.getEstadoEmpunaduraId());
        res.setEstadoGuardamanoId(invArma.getEstadoGuardamanoId());
        res.setFoto1(invArma.getFoto1());
        res.setFoto2(invArma.getFoto2());
        res.setFoto3(invArma.getFoto3());
        res.setLicenciaDiscaId(invArma.getLicenciaDiscaId());
        res.setTarjetaPropiedadId(invArma.getTarjetaPropiedadId());
        res.setDiscaFoxId(invArma.getDiscaFoxId());
        if (!JsfUtil.isNullOrEmpty(invArma.getAmaInventarioAccesoriosList())) {
            if (origen != null) {
                if (origen) {
                    amaDepositoArmasController.setLstAccesorios(copiarDatosAccesorios(invArma.getAmaInventarioAccesoriosList(), res, null)); //AQUI
//                amaGuiaTransitoController.setLstAccesorios(copiarDatosAccesorios(userSession, invArma.getAmaInventarioAccesoriosList(), res, null));
                } else {
                    amaGuiaTransitoController.setLstAccesorios(copiarDatosAccesorios(invArma.getAmaInventarioAccesoriosList(), res, null));
                }
            } else {
                res.setAmaInventarioAccesoriosList(copiarDatosAccesorios(invArma.getAmaInventarioAccesoriosList(), res, null));
            }
        }
        invArma = null;
        return res;
    }

    /**
     * Función que obtiene una lista de accesorios los cuales son copias de
     * registros de base de datos.
     *
     * @author Gino Chávez
     * @param userSession
     * @param listAcces
     * @param invArma
     * @param amaGuia
     * @return
     */
    public List<AmaInventarioAccesorios> copiarDatosAccesorios(List<AmaInventarioAccesorios> listAcces, AmaInventarioArma invArma, AmaGuiaTransito amaGuia) {
        List<AmaInventarioAccesorios> listRes = new ArrayList<>();
        AmaInventarioAccesorios accesAdd = null;
        if (listAcces != null) {
            for (AmaInventarioAccesorios acces : listAcces) {
                accesAdd = new AmaInventarioAccesorios();
                accesAdd.setId(JsfUtil.tempIdN());
                accesAdd.setActivo(acces.getActivo());
                accesAdd.setEstadoDeposito(acces.getEstadoDeposito());
                accesAdd.setArticuloConexoId(acces.getArticuloConexoId());
                accesAdd.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                accesAdd.setAudNumIp(JsfUtil.getIpAddress());
                accesAdd.setCantidad(acces.getCantidad());
                accesAdd.setInventarioArmaId(invArma);
                accesAdd.setGuiaTransitoId(amaGuia);
                listRes.add(accesAdd);
            }
        }
        return listRes;
    }

    /**
     * Función que añade un arma a la lista temporal de armas. La lista temporal
     * de armas no persiste en Base de Datos si no se hace click en el botón
     * Guardar del formulario Registrar/Editar Guía de Tránsito o Depósito.
     *
     * @author Gino Chávez
     * @param origen
     * @param licArma
     * @param invArmaOri
     * @param mostrarMsj
     * @return
     */
    public AmaInventarioArma llenarListArmasTemp(Boolean origen, Map licArma, AmaInventarioArma invArmaOri, Boolean mostrarMsj) {
        AmaInventarioArma resCopia = null;
        try {
            List<AmaInventarioArma> listEvaluada = null;
            if (origen) {
                listEvaluada = amaDepositoArmasController.getListCopiaInvArmaTemp(); //AQUI
//                listEvaluada = amaGuiaTransitoController.getListCopiaInvArmaTemp();
            } else {
                listEvaluada = amaGuiaTransitoController.getListCopiaInvArmaTemp();
            }
            if (listEvaluada == null) {
                listEvaluada = new ArrayList<>();
            }
            resCopia = new AmaInventarioArma();
            resCopia.setId(invArmaOri.getId());
            resCopia.setAlmacenSucamecId(invArmaOri.getAlmacenSucamecId());
            resCopia.setActivo(JsfUtil.TRUE);
            resCopia.setActual(invArmaOri.getActual());
            resCopia.setArmaId(invArmaOri.getArmaId());
            resCopia.setAmbienteId(invArmaOri.getAmbienteId());
            resCopia.setAnaquel(invArmaOri.getAnaquel());
            resCopia.setCierreInventario(invArmaOri.getCierreInventario());
            resCopia.setCodigo(invArmaOri.getCodigo());
            resCopia.setColumna(invArmaOri.getColumna());
            resCopia.setEstadoCachaId(invArmaOri.getEstadoCachaId());
            resCopia.setEstadoconservacionId(invArmaOri.getEstadoconservacionId());
            resCopia.setEstadofuncionalId(invArmaOri.getEstadofuncionalId());
            resCopia.setEstadoserieId(invArmaOri.getEstadoserieId());
            resCopia.setExteriorId(invArmaOri.getExteriorId());
            resCopia.setFechaCierre(invArmaOri.getFechaCierre());
            resCopia.setFechaRegistro(invArmaOri.getFechaRegistro());
            resCopia.setFila(invArmaOri.getFila());
            resCopia.setMaterialCachaId(invArmaOri.getMaterialCachaId());
            resCopia.setMecanismoId(invArmaOri.getMecanismoId());
            resCopia.setModeloId(invArmaOri.getModeloId());
            resCopia.setModalidadTiroId(invArmaOri.getModalidadTiroId());
            resCopia.setNovedadCanonId(invArmaOri.getNovedadCanonId());
            resCopia.setNroRua(invArmaOri.getNroRua());
            String obs = StringUtil.VACIO;
            if (origen) {
                if (licArma != null) {
                    if (licArma.get("SISTEMA").toString().equals("GAMAC")) {
                        obs = "RUA Ref.: " + licArma.get("NRO_RUA") + ((licArma.get("ESTADO_TARJETA") != null) ? ", Estado Tarjeta: " + licArma.get("ESTADO_TARJETA").toString(): "");
                    } else {
                        obs = "Licencia Ref.: " + licArma.get("NRO_LIC") + ((licArma.get("ESTADO_TARJETA") != null) ? ", Estado Lic. Disca: " + licArma.get("ESTADO_LICENCIA_DISCA").toString(): "");
                    } 
                } else {
                    obs = invArmaOri.getObservacion();
                }
                resCopia.setObservacion(obs);
            } else {
                resCopia.setObservacion(invArmaOri.getObservacion());
            }
            resCopia.setPeso(invArmaOri.getPeso());
            resCopia.setPropietarioId(invArmaOri.getPropietarioId());
            resCopia.setSerie(invArmaOri.getSerie());
            resCopia.setSituacionId(invArmaOri.getSituacionId());
            resCopia.setUmedidaId(invArmaOri.getUmedidaId());
            resCopia.setCondicionAlmacen(JsfUtil.FALSE);
            resCopia.setTipoInternamientoId(invArmaOri.getTipoInternamientoId());
            resCopia.setMaterialCantoneraId(invArmaOri.getMaterialCantoneraId());
            resCopia.setMaterialCulataId(invArmaOri.getMaterialCulataId());
            resCopia.setMaterialEmpunaduraId(invArmaOri.getMaterialEmpunaduraId());
            resCopia.setMaterialGuardamanoId(invArmaOri.getMaterialGuardamanoId());
            resCopia.setEstadoCantoneraId(invArmaOri.getEstadoCantoneraId());
            resCopia.setEstadoCulataId(invArmaOri.getEstadoCulataId());
            resCopia.setEstadoEmpunaduraId(invArmaOri.getEstadoEmpunaduraId());
            resCopia.setEstadoGuardamanoId(invArmaOri.getEstadoGuardamanoId());
            resCopia.setFoto1(invArmaOri.getFoto1());
            resCopia.setFoto2(invArmaOri.getFoto2());
            resCopia.setFoto3(invArmaOri.getFoto3());
            if (origen) {
                if (licArma != null) {
                    if (licArma.get("SISTEMA").toString().equals("GAMAC")) {
                        resCopia.setTarjetaPropiedadId(ejbAmaTarjetaProdiedadFacade.find(Long.valueOf(licArma.get("ID_TARJETA").toString())));
                    }else{
                        resCopia.setLicenciaDiscaId(Long.valueOf(licArma.get("NRO_LIC").toString()));
                        resCopia.setDiscaFoxId(ejbAmaMaestroArmasFacade.find(Long.valueOf(licArma.get("ID_DISCA").toString())));
                    }                    
                } else {
                    resCopia.setLicenciaDiscaId(invArmaOri.getLicenciaDiscaId());
                    resCopia.setTarjetaPropiedadId(invArmaOri.getTarjetaPropiedadId());
                    resCopia.setDiscaFoxId(invArmaOri.getDiscaFoxId());
                }                
            } else {
                resCopia.setLicenciaDiscaId(invArmaOri.getLicenciaDiscaId());
                resCopia.setTarjetaPropiedadId(invArmaOri.getTarjetaPropiedadId());
                resCopia.setDiscaFoxId(invArmaOri.getDiscaFoxId());
            }
//            resCopia.setAmaGuiaTransitoList(invArmaOri.getAmaGuiaTransitoList());
            if (!JsfUtil.isNullOrEmpty(invArmaOri.getAmaInventarioAccesoriosList())) {
                AmaInventarioAccesorios copiaAccesTemp = null;
                List<AmaInventarioAccesorios> listAccesTemp = new ArrayList<>();
                for (AmaInventarioAccesorios acces : invArmaOri.getAmaInventarioAccesoriosList()) {
                    copiaAccesTemp = new AmaInventarioAccesorios();
                    copiaAccesTemp.setId(acces.getId());
                    copiaAccesTemp.setActivo(acces.getActivo());
                    copiaAccesTemp.setArticuloConexoId(acces.getArticuloConexoId());
                    copiaAccesTemp.setCantidad(acces.getCantidad());
                    copiaAccesTemp.setEstadoDeposito(JsfUtil.FALSE);
                    copiaAccesTemp.setInventarioArmaId(resCopia);
                    listAccesTemp.add(copiaAccesTemp);
                }
                resCopia.setAmaInventarioAccesoriosList(listAccesTemp);
            }
            List<AmaInventarioArma> listInvArmaTemp = new ArrayList<>();
            listInvArmaTemp.addAll(listEvaluada);
            Boolean isActualizado = Boolean.FALSE;
            for (AmaInventarioArma invArma : listInvArmaTemp) {
                if (Objects.equals(invArma.getId(), resCopia.getId())
                        && (Objects.equals(invArma.getModeloId(), resCopia.getModeloId()) && Objects.equals(invArma.getSerie(), resCopia.getSerie()))) {
                    listEvaluada.remove(invArma);
                    isActualizado = Boolean.TRUE;
                }
            }

            listEvaluada.add(resCopia);
            if (origen) {
                amaDepositoArmasController.setListCopiaInvArmaTemp(listEvaluada); //AQUI
                amaDepositoArmasController.setCopiaInvArmaTemp(resCopia);
//                amaGuiaTransitoController.setCopiaInvArmaTemp(resCopia);
//                amaGuiaTransitoController.setListCopiaInvArmaTemp(listEvaluada);
            } else {
                amaGuiaTransitoController.setCopiaInvArmaTemp(resCopia);
                amaGuiaTransitoController.setListCopiaInvArmaTemp(listEvaluada);
            }
            if (mostrarMsj) {
                if (isActualizado) {
                    JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizadoTemp"));
                    return resCopia;
                }
                JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreadoTemp"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resCopia;
    }

    /**
     * Función que actualiza el campo activo del arma, también actualiza los
     * accesorios del arma en cascada.
     *
     * @author Gino Chávez
     * @param amaGuia
     * @param activo
     * @return
     */
    public Boolean cambiarEstadoCascadeActa(AmaGuiaTransito amaGuia, short activo) {
        try {
            String obs = null;
            if (amaGuia != null) {
                if (amaGuia.getAmaInventarioArmaList() != null) {
                    for (AmaInventarioArma invArma : amaGuia.getAmaInventarioArmaList()) {
                        obs = invArma.getObservacion();
                        if (obs != null) {
                            char[] arrayChar = obs.toCharArray();
                            if (arrayChar.length > 420) {
                                obs = String.valueOf(arrayChar);
                                obs = obs.substring(0, 420);
                            }
                        }
                        if (Objects.equals(activo, JsfUtil.FALSE)) {
                            if (obs == null) {
                                obs = "[Anulado por rectificatoria de acta de internamiento con fecha " + JsfUtil.dateToString(new Date(), "dd/MM/yyyy") + ".]";
                            } else if (!obs.contains("[Anulado por rectificatoria")) {
                                obs = obs + StringUtil.ESPACIO + "[Anulado por rectificatoria de acta de internamiento con fecha " + JsfUtil.dateToString(new Date(), "dd/MM/yyyy") + ".]";
                            }
                        } else if (obs != null && !StringUtil.VACIO.equals(obs)) {
                            if (obs.contains("[Anulado por rectificatoria")) {
                                int ini = obs.indexOf("[");
                                if (ini > 0) {
                                    obs = obs.substring(0, ini - 1);
                                } else {
                                    obs = null;
                                }
                            }
                        }
                        invArma.setActivo(activo);
                        invArma.setObservacion(obs);
                        ejbAmaInventarioArmaFacade.edit(invArma);
                        if (invArma.getAmaInventarioAccesoriosList() != null) {
                            for (AmaInventarioAccesorios acces : invArma.getAmaInventarioAccesoriosList()) {
                                acces.setActivo(activo);
//                            acces.setEstadoDeposito(JsfUtil.TRUE);
                                ejbAmaInventarioAccesoriosFacade.edit(acces);
                            }
                        }
                    }
                }
                if (amaGuia.getAmaInventarioAccesoriosList() != null) {
                    for (AmaInventarioAccesorios acces : amaGuia.getAmaInventarioAccesoriosList()) {
                        acces.setActivo(activo);
                        ejbAmaInventarioAccesoriosFacade.edit(acces);
                    }
                }
                if (amaGuia.getAmaGuiaMunicionesList() != null) {
                    for (AmaGuiaMuniciones muni : amaGuia.getAmaGuiaMunicionesList()) {
                        muni.setActivo(activo);
                    }
                }
                //Inactivando comprobantes.
                String nroExp = StringUtil.VACIO;
                if (amaGuia.getNroExpediente() != null && !StringUtil.VACIO.equals(amaGuia.getNroExpediente())) {
                    nroExp = amaGuia.getNroExpediente();
                } else {
                    nroExp = "PENDIENTE_" + amaGuia.getId();
                }
                List<SbReciboRegistro> listReg = ejbSbReciboRegistroFacade.obtenerRecibosPorExpediente(nroExp);
                if (listReg != null) {
                    for (SbReciboRegistro reg : listReg) {
                        reg.setActivo(activo);
                        ejbSbReciboRegistroFacade.edit(reg);
                    }
                }
                //
                if (Objects.equals(activo, JsfUtil.FALSE)) {
                    amaGuia.setObservacion("[Anulado por rectificatoria de acta de internamiento con fecha " + JsfUtil.dateToString(new Date(), "dd/MM/yyyy") + ".]");
                }
                amaGuia.setActivo(activo);
                ejbAmaGuiaTransitoFacade.edit(amaGuia);
                RequestContext.getCurrentInstance().update("listaActasArmaForm");
            } else {
                JsfUtil.mensajeError("No es posible eliminar la Guía de Tránsito.");
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            JsfUtil.mensajeError("Ha ocurrido un error inesperado al rectificar el acta.");
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }

    /**
     * Función que obtiene el listado de armas a partir de un listado de String,
     * los cuales contienen datos del arma separados por ',' o '@/@'
     *
     * @author Gino Chávez
     * @param listString
     * @return
     */
    public List<AmaArma> listArmas(List<String> listString) {
        AmaArma amaArma = null;
        List<AmaArma> listArmas = new ArrayList<>();
        if (!JsfUtil.isNullOrEmpty(listString)) {
            for (String str : listString) {
                if (!existeEnInventarioArma(str)) {
                    amaArma = ejbAmaArmaFacade.find(Long.valueOf(obtenerDatosArma(str, "0", L_UNO)));
                    if (amaArma != null && amaArma.getId() != null) {
                        if (!listArmas.contains(amaArma)) {
                            listArmas.add(amaArma);
                        }
                    }
                }
            }
        }
        return listArmas;
    }

    /**
     * Función que obtiene el flag que determina si un arma se encuentra
     * registrado en AmaInventarioArma.
     *
     * @author Gino Chávez
     * @param cadena
     * @return
     */
    public Boolean existeEnInventarioArma(String cadena) {
        String res = StringUtil.VACIO;
        if (cadena != null && !cadena.isEmpty()) {
            String[] label = cadena.split(StringUtil.PUNTOCOMA);
            if (label.length > 20) {
                res = label[20];
            }
        }
        if (!Objects.equals(res, StringUtil.VACIO)) {
            if (Objects.equals(res, "0")) {
                return Boolean.FALSE;
            }
        } else {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * Función que obtiene el criterio de búsqueda de armas en InventarioArma:
     * 1=Buscar por serie, 2=Buscar por RUA, 3= Buscar por Serie y RUA
     *
     * @author Gino Chávez
     * @param idTipoGuia
     * @return
     */
    public Long obtenerIdTipoBus(TipoGamac idTipoGuia) {
        Long idTG = L_CERO;
        if (idTipoGuia != null) {
            if (idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_EXH")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_IMP")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_DON")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_REI")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_SAL")) {
                idTG = L_UNO;
            } else if (idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_ALM")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_AGE")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_DEV")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_SUC")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_COL")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_POL")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_DDV")
                    || idTipoGuia.getCodProg().equalsIgnoreCase("TP_GTGAMAC_REC")) {
                idTG = L_TRES;
            } else {
                switch (idTipoGuia.getTipoId().getCodProg()) {
                    case "TP_GTGAMAC_ALM":
                        idTG = L_TRES;
                        break;
                    case "TP_GTGAMAC_DEV":
                        idTG = L_TRES;
                        break;
                    case "TP_GTGAMAC_SUC":
                        idTG = L_TRES;
                        break;
                    case "TP_GTGAMAC_DON":
                        idTG = L_UNO;
                        break;
                    default:
                        break;
                }
            }
        }
        return idTG;
    }

    /**
     * Función que obtiene el valor a mostrar en el label del autocompletable de
     * armas (Guias de Tránsito, Depósito y Devolción)
     *
     * @author Gino Chávez
     * @param cadena
     * @param idTipoGuia
     * @return
     */
    public String obtenerLabelArma(String cadena, TipoGamac idTipoGuia) {
        String res = StringUtil.VACIO;
        if (cadena != null && !cadena.isEmpty()) {
            String[] label = cadena.split(StringUtil.PUNTOCOMA);
            if (L_UNO.equals(obtenerIdTipoBus(idTipoGuia))) {
                if (label.length > 1) {
                    res = label[6];
                }
            } else if (label.length > 1) {
                res = label[1];
                if (StringUtil.NULO.equalsIgnoreCase(res) || StringUtil.VACIO.equals(res.trim())) {
                    res = label[6];
                    //Validación en caso se haya registrado un arma sin nro RUA ni serie
                    if (StringUtil.NULO.equalsIgnoreCase(res) || StringUtil.VACIO.equals(res.trim())) {
                        res = "Registro Temporal";
                    }
                }
            }
        }
        return res;
    }

    /**
     * Función que obtiene los datos del arma que se encuentran en una variable
     * de tipo String separado por punto y coma ';' o '@/@'.
     *
     * @author Gino Chavez
     * @param cadena
     * @param posiciones
     * @param tipo
     * @return
     */
    public String obtenerDatosArma(String cadena, String posiciones, Long tipo) {
        String res = StringUtil.VACIO;
        //Se lista las posiciones de la cadena posiciones los cuales seran para conseguir los valores en la cadena 'cadena'
        String[] list = posiciones.split(",");
        int tam = list.length;
        String[] label = null;
        if (cadena != null && !cadena.isEmpty()) {
            //Se lista los valores de la cadena 'cadena'
            if (L_UNO.equals(tipo)) {
                label = cadena.split(StringUtil.PUNTOCOMA);
            } else {
                label = cadena.split("@/@");
            }
            int indice = 0;
            //Se recorre las posiciones deseadas
            for (int i = 0; i < tam; i++) {
                if (!StringUtil.VACIO.equals(list[i])) {
                    //Se obtiene el indice con el cual se conseguira un determinado valor de la cadena 'cadena' inicial
                    indice = Integer.valueOf(list[i]);
                    if (indice < label.length) {
                        //Se concatena la cadena de respuesta
                        if (i == 0) {
                            res += label[indice];
                        } else {
                            res += StringUtil.ESPACIO + label[indice];
                        }
                    }
                }
            }
        }
        if (StringUtil.NULO.equalsIgnoreCase(res)) {
            res = StringUtil.VACIO;
        }
        return res;
    }

    /**
     * Función que obtiene una lista de Strings a partir de una lista de Map.
     * Estos datos son del arma.
     *
     * @author Gino Chávez
     * @param listaMap
     * @return
     */
    public List<String> listArmasString(List<Map> listaMap) {
        String item = null;
        List<String> listRes = new ArrayList<>();
        if (listaMap != null) {
            for (Map map : listaMap) {
                item = StringUtil.VACIO;
                item = map.get("id") + StringUtil.PUNTOCOMA //indice 0
                        + map.get("nroRua") + StringUtil.PUNTOCOMA //indice 1
                        + map.get("tipoArma") + StringUtil.PUNTOCOMA //indice 2
                        + map.get("marca") + StringUtil.PUNTOCOMA //indice 3
                        + map.get("modelo") + StringUtil.PUNTOCOMA //indice 4
                        + map.get("calibre") + StringUtil.PUNTOCOMA //indice 5
                        + map.get("serie") + StringUtil.PUNTOCOMA //indice 6
                        + map.get("codigo") + StringUtil.PUNTOCOMA //indice 7
                        + map.get("propietario") + StringUtil.PUNTOCOMA //indice 8
                        + map.get("idModelo") + StringUtil.PUNTOCOMA//indice 9
                        + map.get("estadoSerie") + StringUtil.PUNTOCOMA//indice 10
                        + map.get("estadoFun") + StringUtil.PUNTOCOMA//indice 11
                        + map.get("estadoConsv") + StringUtil.PUNTOCOMA//indice 12
                        + map.get("matCacha") + StringUtil.PUNTOCOMA//indice 13
                        + map.get("estadoCacha") + StringUtil.PUNTOCOMA//indice 14
                        + map.get("mecanismo") + StringUtil.PUNTOCOMA//indice 15
                        + map.get("novCanon") + StringUtil.PUNTOCOMA//indice 16
                        + map.get("modTiro") + StringUtil.PUNTOCOMA//indice 17
                        + map.get("exterior") + StringUtil.PUNTOCOMA//indice 18
                        + map.get("peso") + StringUtil.PUNTOCOMA//indice 19
                        + map.get("existeIArma") + StringUtil.PUNTOCOMA//indice 20 --> Indicador de que es un arma registrado en la tabla Inventario arma. 1:Existe en inventario arma, 0:No existe en Inventario arma
                        + map.get("almSucamecId") + StringUtil.PUNTOCOMA//indice 21
                        + map.get("conTarjeta") + StringUtil.PUNTOCOMA//indice 22
                        ;
                listRes.add(item);
            }
        }
        return listRes;
    }

    /**
     * Función que retorna los datos del arma en una cadena separados por ';'
     *
     * @author Gino Chávez
     * @param idArma
     * @param idInvArma
     * @return
     */
    public String armaString(String idArma, String idInvArma, String nroActa) {
        String item = null;
        //String res = new ArrayList<>();
        if (idArma != null) {
            AmaArma arma = ejbAmaArmaFacade.find(Long.parseLong(idArma));
            item = StringUtil.VACIO;
            item = arma.getId() + StringUtil.PUNTOCOMA //indice 0
                    + arma.getNroRua() + StringUtil.PUNTOCOMA //indice 1
                    + arma.getModeloId().getTipoArmaId().getNombre() + StringUtil.PUNTOCOMA //indice 2
                    + arma.getModeloId().getMarcaId().getNombre() + StringUtil.PUNTOCOMA //indice 3
                    + arma.getModeloId().getModelo() + StringUtil.PUNTOCOMA //indice 4
                    + obtenerCalibres(arma.getModeloId()) + StringUtil.PUNTOCOMA //indice 5
                    + arma.getSerie() + StringUtil.PUNTOCOMA //indice 6
                    + null + StringUtil.PUNTOCOMA //indice 7
                    + null + StringUtil.PUNTOCOMA //indice 8
                    + arma.getModeloId().getId() + StringUtil.PUNTOCOMA//indice 9
                    + null + StringUtil.PUNTOCOMA //indice 10
                    + (arma.getEstadoId() == null ? null : arma.getEstadoId().getNombre()) + StringUtil.PUNTOCOMA //indice 11
                    + null + StringUtil.PUNTOCOMA //indice 12
                    + null + StringUtil.PUNTOCOMA //indice 13
                    + null + StringUtil.PUNTOCOMA //indice 14
                    + null + StringUtil.PUNTOCOMA //indice 15
                    + null + StringUtil.PUNTOCOMA //indice 16
                    + null + StringUtil.PUNTOCOMA //indice 17
                    + null + StringUtil.PUNTOCOMA //indice 18
                    + null + StringUtil.PUNTOCOMA //indice 19
                    + 0 + StringUtil.PUNTOCOMA //indice 20 --> Indicador de que es un arma registrado en la tabla Inventario arma. 1:Existe en inventario arma, 0:No existe en Inventario arma
                    + nroActa + StringUtil.PUNTOCOMA//indice 21
                    ;
        } else if (idInvArma != null) {
            AmaInventarioArma invArma = ejbAmaInventarioArmaFacade.find(Long.parseLong(idInvArma));
            item = StringUtil.VACIO;
            item = invArma.getId() + StringUtil.PUNTOCOMA //indice 0
                    + invArma.getNroRua() + StringUtil.PUNTOCOMA //indice 1
                    + invArma.getModeloId().getTipoArmaId().getNombre() + StringUtil.PUNTOCOMA //indice 2
                    + invArma.getModeloId().getMarcaId().getNombre() + StringUtil.PUNTOCOMA //indice 3
                    + invArma.getModeloId().getModelo() + StringUtil.PUNTOCOMA //indice 4
                    + obtenerCalibres(invArma.getModeloId()) + StringUtil.PUNTOCOMA //indice 5
                    + invArma.getSerie() + StringUtil.PUNTOCOMA //indice 6
                    + invArma.getCodigo() + StringUtil.PUNTOCOMA //indice 7
                    + (invArma.getPropietarioId() == null ? StringUtil.VACIO : invArma.getPropietarioId().getNombreCompleto()) + StringUtil.PUNTOCOMA //indice 8
                    + invArma.getModeloId().getId() + StringUtil.PUNTOCOMA //indice 9
                    + (invArma.getEstadoserieId() == null ? StringUtil.VACIO : invArma.getEstadoserieId().getNombre()) + StringUtil.PUNTOCOMA //indice 10
                    + (invArma.getEstadofuncionalId() == null ? StringUtil.VACIO : invArma.getEstadofuncionalId().getNombre()) + StringUtil.PUNTOCOMA //indice 11
                    + invArma.getEstadoconservacionId().getNombre() + StringUtil.PUNTOCOMA //indice 12
                    + (invArma.getMaterialCachaId() == null ? StringUtil.VACIO : invArma.getMaterialCachaId().getNombre()) + StringUtil.PUNTOCOMA //indice 13
                    + (invArma.getEstadoCachaId() == null ? StringUtil.VACIO : invArma.getEstadoCachaId().getNombre()) + StringUtil.PUNTOCOMA //indice 14
                    + (invArma.getMecanismoId() == null ? StringUtil.VACIO : invArma.getMecanismoId().getNombre()) + StringUtil.PUNTOCOMA //indice 15
                    + (invArma.getNovedadCanonId() == null ? StringUtil.VACIO : invArma.getNovedadCanonId().getNombre()) + StringUtil.PUNTOCOMA //indice 16
                    + (invArma.getModalidadTiroId() == null ? StringUtil.VACIO : invArma.getModalidadTiroId().getNombre()) + StringUtil.PUNTOCOMA //indice 17
                    + (invArma.getExteriorId() == null ? StringUtil.VACIO : invArma.getExteriorId().getNombre()) + StringUtil.PUNTOCOMA //indice 18
                    + (invArma.getPeso() == null ? StringUtil.VACIO : invArma.getPeso()) + StringUtil.PUNTOCOMA //indice 19
                    + 1 + StringUtil.PUNTOCOMA//indice 20 --> Indicador de que es un arma registrado en la tabla Inventario arma. 1:Existe en inventario arma, 0:No existe en Inventario arma
                    + nroActa + StringUtil.PUNTOCOMA//indice 21
                    ;
            ;
        }
        return item;
    }

    /**
     * Función que retorna los datos del arma (de manera temporal) en una cadena
     * separados por ';'
     *
     * @author Gino Chávez
     * @param invArma
     * @return
     */
    public String armaStringTemp(AmaInventarioArma invArma) {
        String item = null;
        //String res = new ArrayList<>();
        item = StringUtil.VACIO;
        item = invArma.getId() + StringUtil.PUNTOCOMA //indice 0
                + invArma.getNroRua() + StringUtil.PUNTOCOMA //indice 1
                + (invArma.getModeloId() == null ? StringUtil.VACIO : invArma.getModeloId().getTipoArmaId().getNombre()) + StringUtil.PUNTOCOMA //indice 2
                + (invArma.getModeloId() == null ? StringUtil.VACIO : invArma.getModeloId().getMarcaId().getNombre()) + StringUtil.PUNTOCOMA //indice 3
                + (invArma.getModeloId() == null ? StringUtil.VACIO : invArma.getModeloId().getModelo()) + StringUtil.PUNTOCOMA //indice 4
                + obtenerCalibres(invArma.getModeloId()) + StringUtil.PUNTOCOMA //indice 5
                + invArma.getSerie() + StringUtil.PUNTOCOMA //indice 6
                + invArma.getCodigo() + StringUtil.PUNTOCOMA //indice 7
                + (invArma.getPropietarioId() == null ? StringUtil.VACIO : invArma.getPropietarioId().getNombreCompleto()) + StringUtil.PUNTOCOMA //indice 8
                + (invArma.getModeloId() == null ? StringUtil.VACIO : invArma.getModeloId().getId()) + StringUtil.PUNTOCOMA //indice 9
                + (invArma.getEstadoserieId() == null ? StringUtil.VACIO : invArma.getEstadoserieId().getNombre()) + StringUtil.PUNTOCOMA //indice 10
                + (invArma.getEstadofuncionalId() == null ? StringUtil.VACIO : invArma.getEstadofuncionalId().getNombre()) + StringUtil.PUNTOCOMA //indice 11
                + (invArma.getEstadoconservacionId() == null ? StringUtil.VACIO : invArma.getEstadoconservacionId().getNombre()) + StringUtil.PUNTOCOMA //indice 12
                + (invArma.getMaterialCachaId() == null ? StringUtil.VACIO : invArma.getMaterialCachaId().getNombre()) + StringUtil.PUNTOCOMA //indice 13
                + (invArma.getEstadoCachaId() == null ? StringUtil.VACIO : invArma.getEstadoCachaId().getNombre()) + StringUtil.PUNTOCOMA //indice 14
                + (invArma.getMecanismoId() == null ? StringUtil.VACIO : invArma.getMecanismoId().getNombre()) + StringUtil.PUNTOCOMA //indice 15
                + (invArma.getNovedadCanonId() == null ? StringUtil.VACIO : invArma.getNovedadCanonId().getNombre()) + StringUtil.PUNTOCOMA //indice 16
                + (invArma.getModalidadTiroId() == null ? StringUtil.VACIO : invArma.getModalidadTiroId().getNombre()) + StringUtil.PUNTOCOMA //indice 17
                + (invArma.getExteriorId() == null ? StringUtil.VACIO : invArma.getExteriorId().getNombre()) + StringUtil.PUNTOCOMA //indice 18
                + (invArma.getPeso() == null ? StringUtil.VACIO : invArma.getPeso()) + StringUtil.PUNTOCOMA //indice 19
                + 1 + StringUtil.PUNTOCOMA; //indice 20 --> Indicador de que es un arma registrado en la tabla Inventario arma. 1:Existe en inventario arma, 0:No existe en Inventario arma
        ;
        return item;
    }

    /**
     * Función que retorna los datos del arma en una cadena separados por ';'
     *
     * @author Gino Chávez
     * @param invArma
     * @return
     */
    public String obtenerArmaString(AmaInventarioArma invArma) {
        String item = null;
        item = StringUtil.VACIO;
        item = invArma.getId() + StringUtil.PUNTOCOMA //indice 0
                + invArma.getNroRua() + StringUtil.PUNTOCOMA //indice 1
                + (invArma.getModeloId() == null ? StringUtil.VACIO : invArma.getModeloId().getTipoArmaId().getNombre()) + StringUtil.PUNTOCOMA //indice 2
                + (invArma.getModeloId() == null ? StringUtil.VACIO : invArma.getModeloId().getMarcaId().getNombre()) + StringUtil.PUNTOCOMA //indice 3
                + (invArma.getModeloId() == null ? StringUtil.VACIO : invArma.getModeloId().getModelo()) + StringUtil.PUNTOCOMA //indice 4
                + obtenerCalibres(invArma.getModeloId()) + StringUtil.PUNTOCOMA //indice 5
                + invArma.getSerie() + StringUtil.PUNTOCOMA //indice 6
                + invArma.getCodigo() + StringUtil.PUNTOCOMA //indice 7
                + (invArma.getPropietarioId() == null ? StringUtil.VACIO : invArma.getPropietarioId().getNombreCompleto()) + StringUtil.PUNTOCOMA //indice 8
                + (invArma.getModeloId() == null ? StringUtil.VACIO : invArma.getModeloId().getId()) + StringUtil.PUNTOCOMA //indice 9
                + (invArma.getEstadoserieId() == null ? StringUtil.VACIO : invArma.getEstadoserieId().getNombre()) + StringUtil.PUNTOCOMA //indice 10
                + (invArma.getEstadofuncionalId() == null ? StringUtil.VACIO : invArma.getEstadofuncionalId().getNombre()) + StringUtil.PUNTOCOMA //indice 11
                + (invArma.getEstadoconservacionId() == null ? StringUtil.VACIO : invArma.getEstadoconservacionId().getNombre()) + StringUtil.PUNTOCOMA //indice 12
                + (invArma.getMaterialCachaId() == null ? StringUtil.VACIO : invArma.getMaterialCachaId().getNombre()) + StringUtil.PUNTOCOMA //indice 13
                + (invArma.getEstadoCachaId() == null ? StringUtil.VACIO : invArma.getEstadoCachaId().getNombre()) + StringUtil.PUNTOCOMA //indice 14
                + (invArma.getMecanismoId() == null ? StringUtil.VACIO : invArma.getMecanismoId().getNombre()) + StringUtil.PUNTOCOMA //indice 15
                + (invArma.getNovedadCanonId() == null ? StringUtil.VACIO : invArma.getNovedadCanonId().getNombre()) + StringUtil.PUNTOCOMA //indice 16
                + (invArma.getModalidadTiroId() == null ? StringUtil.VACIO : invArma.getModalidadTiroId().getNombre()) + StringUtil.PUNTOCOMA //indice 17
                + (invArma.getExteriorId() == null ? StringUtil.VACIO : invArma.getExteriorId().getNombre()) + StringUtil.PUNTOCOMA //indice 18
                + (invArma.getPeso() == null ? StringUtil.VACIO : invArma.getPeso()) + StringUtil.PUNTOCOMA //indice 19
                + 1 + StringUtil.PUNTOCOMA; //indice 20 --> Indicador de que es un arma registrado en la tabla Inventario arma. 1:Existe en inventario arma, 0:No existe en Inventario arma
        ;
        return item;
    }

    /**
     * Función que retorna los datos del arma en una cadena separados por '@/@'
     *
     * @author Gino Chávez
     * @param invArma
     * @return
     */
    public String invArmaString(Map invArma) {
        String item = null;
        if (invArma != null) {
            item = StringUtil.VACIO;
            item = invArma.get("ID") + "@/@" //indice 0
                    + invArma.get("MODELO") + "@/@" //indice 1
                    + invArma.get("MARCA_ID") + "@/@" //indice 2
                    + invArma.get("TIPO_ARMA_ID") + "@/@" //indice 3
                    + invArma.get("CALIBRE") + "@/@" //indice 4
                    + invArma.get("TIPO_ARMA") + "@/@" //indice 5
                    + invArma.get("MARCA") + "@/@"; //indice 06
        }
        return item;
    }

    /**
     * Función que obtiene la lista de armas para reporte.
     *
     * @author Gino Chávez
     * @param amaGuia
     * @return
     */
    public List<ArmaRepClass> tablaArmas(AmaGuiaTransito amaGuia) {
        List<ArmaRepClass> armasRepLst = null;
        if (amaGuia != null) {
            armasRepLst = new ArrayList();
            Long id = 1L;
            if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaArmaList())) {
                for (AmaArma amaArma : amaGuia.getAmaArmaList()) {
                    ArmaRepClass armaRep = new ArmaRepClass();
                    armaRep.setId(id++);
                    armaRep.setTipoArma(amaArma.getModeloId().getTipoArmaId());
                    armaRep.setMarca(amaArma.getModeloId().getMarcaId());
                    armaRep.setModelo(amaArma.getModeloId().getModelo());
                    armaRep.setCalibre(ordenarCalibres(amaArma.getModeloId().getAmaCatalogoList()));
                    armaRep.setSerie(amaArma.getSerie());
                    armaRep.setNroRua(amaArma.getNroRua());
                    armasRepLst.add(armaRep);
                }
            }
            if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioArmaList())) {
                id = 1L;
                for (AmaInventarioArma amaInvArma : amaGuia.getAmaInventarioArmaList()) {
                    ArmaRepClass armaRep = new ArmaRepClass();
                    armaRep.setId(id++);
                    armaRep.setTipoArma(amaInvArma.getModeloId().getTipoArmaId());
                    armaRep.setMarca(amaInvArma.getModeloId().getMarcaId());
                    armaRep.setModelo(amaInvArma.getModeloId().getModelo());
                    armaRep.setCalibre(ordenarCalibres(amaInvArma.getModeloId().getAmaCatalogoList()));
                    armaRep.setSerie(amaInvArma.getSerie());
                    armaRep.setNroRua(amaInvArma.getNroRua());
                    armaRep.setEstadoserieId(amaInvArma.getEstadoserieId());
                    armaRep.setModalidadTiroId(amaInvArma.getModalidadTiroId());
                    armaRep.setPropietarioId(amaInvArma.getPropietarioId());
                    armaRep.setNovedadCanonId(amaInvArma.getNovedadCanonId());
                    armaRep.setMecanismoId(amaInvArma.getMecanismoId());
                    armaRep.setExteriorId(amaInvArma.getExteriorId());
                    armaRep.setMaterialCachaId(amaInvArma.getMaterialCachaId());
                    armaRep.setEstadoCachaId(amaInvArma.getEstadoCachaId());
                    armaRep.setMaterialCantoneraId(amaInvArma.getMaterialCantoneraId());
                    armaRep.setEstadoCantoneraId(amaInvArma.getEstadoCantoneraId());
                    armaRep.setMaterialCulataId(amaInvArma.getMaterialCulataId());
                    armaRep.setEstadoCulataId(amaInvArma.getEstadoCulataId());
                    armaRep.setMaterialEmpunaduraId(amaInvArma.getMaterialEmpunaduraId());
                    armaRep.setEstadoEmpunaduraId(amaInvArma.getEstadoEmpunaduraId());
                    armaRep.setMaterialGuardamanoId(amaInvArma.getMaterialGuardamanoId());
                    armaRep.setEstadoGuardamanoId(amaInvArma.getEstadoGuardamanoId());
                    armaRep.setEstadoconservacionId(amaInvArma.getEstadoconservacionId());
                    armaRep.setEstadofuncionalId(amaInvArma.getEstadofuncionalId());
                    armaRep.setSituacionId(amaInvArma.getSituacionId());
                    armasRepLst.add(armaRep);
                }

            }
        }
        return armasRepLst;
    }

    /**
     * Función que obtiene la lista de armas para reporte.
     *
     * @author Gino Chávez
     * @param listArmas
     * @param listInvArmas
     * @return
     */
    public List<ArmaRepClass> tablaArmas(List<AmaArma> listArmas, List<AmaInventarioArma> listInvArmas) {
        List<ArmaRepClass> armasRepLst = new ArrayList();
        Long id = L_CERO;
        //Ordenar por TIPO DE ARMA, MARCA, MODELO, CALIBRE y SERIE.

        if (!JsfUtil.isNullOrEmpty(listArmas)) {
            ordernarArmas(listArmas);
            for (AmaArma arma : listArmas) {
                if (arma.getActivo() == JsfUtil.TRUE) {
                    id += L_UNO;
                    ArmaRepClass armaRep = new ArmaRepClass();
                    armaRep.setId(id);
                    armaRep.setTipoArma(arma.getModeloId().getTipoArmaId());
                    armaRep.setMarca(arma.getModeloId().getMarcaId());
                    armaRep.setModelo(arma.getModeloId().getModelo());
                    armaRep.setCalibre(ordenarCalibres(arma.getModeloId().getAmaCatalogoList()));
                    armaRep.setSerie(arma.getSerie() == null ? StringUtil.VACIO : arma.getSerie().replaceAll("\t", StringUtil.VACIO));
                    armaRep.setNroRua(arma.getNroRua() == null ? StringUtil.VACIO : arma.getNroRua().replaceAll("\t", StringUtil.VACIO));

                    armasRepLst.add(armaRep);
                }
            }
        }
        if (!JsfUtil.isNullOrEmpty(listInvArmas)) {
            ordernarArmasInventario(listInvArmas);
            for (AmaInventarioArma invArma : listInvArmas) {
                if (invArma.getActivo() == JsfUtil.TRUE) {
                    id += L_UNO;
                    ArmaRepClass armaRep = new ArmaRepClass();
                    armaRep.setId(id);
                    armaRep.setTipoArma(invArma.getModeloId().getTipoArmaId());
                    armaRep.setMarca(invArma.getModeloId().getMarcaId());
                    armaRep.setModelo(invArma.getModeloId().getModelo());
                    armaRep.setCalibre(ordenarCalibres(invArma.getModeloId().getAmaCatalogoList()));
                    armaRep.setSerie(invArma.getSerie() == null ? StringUtil.GUION : invArma.getSerie().replaceAll("\t", StringUtil.VACIO));
                    armaRep.setNroRua(invArma.getNroRua() == null ? StringUtil.VACIO : invArma.getNroRua().replaceAll("\t", StringUtil.VACIO));
                    //Datos para la exportacion en PDF de las armas almacenadas
                    armaRep.setModeloId(invArma.getModeloId());
                    armaRep.setPropietarioId(invArma.getPropietarioId());
                    armaRep.setAlmacenSucamecId(invArma.getAlmacenSucamecId());
                    armaRep.setSituacionId(invArma.getSituacionId());
                    armaRep.setEstadofuncionalId(invArma.getEstadofuncionalId());
                    armaRep.setAnaquel(invArma.getAnaquel());
                    armaRep.setCodActaDeposito(obtenerCodActaInternamiento(invArma));
                    armaRep.setCodigo(invArma.getCodigo());
                    armasRepLst.add(armaRep);
                }
            }
        }
        return armasRepLst;
    }

    public void ordernarArmas(List<AmaArma> armasRepLst) {
        Collections.sort(armasRepLst, new Comparator<AmaArma>() {
            @Override
            public int compare(AmaArma one, AmaArma other) {
                return (one.getModeloId().getTipoArmaId().getNombre()
                        + one.getModeloId().getMarcaId().getNombre()
                        + one.getModeloId().getModelo()
                        + one.getSerie()).
                        compareTo(other.getModeloId().getTipoArmaId().getNombre()
                                + other.getModeloId().getMarcaId().getNombre()
                                + other.getModeloId().getModelo()
                                + other.getSerie());
            }
        });
    }

    public void ordernarArmasInventario(List<AmaInventarioArma> armasRepLst) {
        Collections.sort(armasRepLst, new Comparator<AmaInventarioArma>() {
            @Override
            public int compare(AmaInventarioArma one, AmaInventarioArma other) {
                return (one.getModeloId().getTipoArmaId().getNombre()
                        + one.getModeloId().getMarcaId().getNombre()
                        + one.getModeloId().getModelo()
                        + one.getSerie()).
                        compareTo(other.getModeloId().getTipoArmaId().getNombre()
                                + other.getModeloId().getMarcaId().getNombre()
                                + other.getModeloId().getModelo()
                                + other.getSerie());
            }
        });
    }

    public void ordernarArmaRepClass(List<ArmaRepClass> armasRepLst) {
        Collections.sort(armasRepLst, new Comparator<ArmaRepClass>() {
            @Override
            public int compare(ArmaRepClass one, ArmaRepClass other) {
                return one.getCodActaDeposito().compareTo(other.getCodActaDeposito());
            }
        });
    }

    /**
     *
     * @param calibresLst
     * @return
     */
    public String ordenarCalibres(List<AmaCatalogo> calibresLst) {
        //ordernar calibres de un arma
        Collections.sort(calibresLst, new Comparator<AmaCatalogo>() {
            @Override
            public int compare(AmaCatalogo one, AmaCatalogo other) {
                return one.getNombre().compareTo(other.getNombre());
            }
        });

        //formar cadena de calibres para un arma
        String calibresStr = StringUtil.VACIO;
        for (AmaCatalogo cal : calibresLst) {
            if (cal.getActivo() == 1) {
                if (StringUtil.VACIO.equals(calibresStr)) {
                    calibresStr = cal.getNombre();
                } else {
                    calibresStr = calibresStr + " / " + cal.getNombre();
                }
            }
        }
        return calibresStr;
    }

    /**
     * Función que obtiene los calibres del arma por modelo id
     *
     * @author Gino Chávez
     * @param amaModelos
     * @return
     */
    public String obtenerCalibres(AmaModelos amaModelos) {
        String r = StringUtil.VACIO;
        String separador = "/";
        if (amaModelos != null) {
            int tam = amaModelos.getAmaCatalogoList().size();
            for (AmaCatalogo amaCat : amaModelos.getAmaCatalogoList()) {
                if (tam > 1) {
                    r += amaCat.getNombre() + separador;
                } else {
                    r += amaCat.getNombre();
                }
                tam--;
            }
        }
        return r;
    }

    /**
     *
     * @param persona
     * @return
     */
    public String obtenerNombrePersona(SbPersona persona) {
        String res = StringUtil.VACIO;
        if (persona != null) {
            res = persona.getRznSocial() == null ? persona.getNombres() + StringUtil.ESPACIO + (persona.getApePat() == null ? StringUtil.VACIO : persona.getApePat()) + StringUtil.ESPACIO + (persona.getApeMat() == null ? StringUtil.VACIO : persona.getApeMat()) : persona.getRznSocial();
        }
        return res;
    }

    /**
     * Función para obtener el tipo y número de documento y el nombre o razon
     * social de una persona
     *
     * @param per
     * @return Descripción de propietario (Tipo Doc : Nro. Documento)
     * @author Gino Chávez
     * @version 1.0
     */
    public String obtenerDescPersona(SbPersona per) {
        String nombre = StringUtil.VACIO;

        if (per != null) {
            if (per.getTipoDoc() == null) {
                if (per.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                    nombre = "RUC: " + per.getRuc() + " - " + per.getRznSocial();
                } else {
                    nombre = "DNI: " + per.getNumDoc() + " - " + per.getApePat() + StringUtil.ESPACIO + per.getApeMat() + StringUtil.ESPACIO + per.getNombres();
                }
            } else {
                nombre = per.getTipoDoc().getNombre() + ": ";
                if (per.getTipoDoc().getCodProg().equals("TP_DOCID_RUC")
                        || per.getTipoDoc().getCodProg().equals("TP_DOCID_EXT")) {
                    nombre += per.getRuc() + " - " + (per.getRznSocial() == null ? (per.getApePat() + StringUtil.ESPACIO + per.getApeMat() + StringUtil.ESPACIO + per.getNombres()) : per.getRznSocial());
                } else {
                    nombre += per.getNumDoc() + " - "
                            + (per.getApePat() == null ? StringUtil.VACIO : per.getApePat()) + StringUtil.ESPACIO
                            + (per.getApeMat() == null ? StringUtil.VACIO : per.getApeMat()) + StringUtil.ESPACIO + per.getNombres();
                }
            }
        }

        return nombre;
    }

    /**
     *
     * @param rua
     * @return
     */
    public String obtenerPropietarioAnt(String rua) {
        if (rua != null && !StringUtil.VACIO.equals(rua.trim())) {
            List<AmaTarjetaPropiedad> listTrj = ejbAmaTarjetaPropiedadFacade.buscarTarXNroRua(rua);
            if (!JsfUtil.isNullOrEmpty(listTrj)) {
                for (AmaTarjetaPropiedad tarj : listTrj) {
                    if (tarj.getActivo() == JsfUtil.TRUE) {
                        return obtenerDescPersona(tarj.getPersonaVendedorId());
                    }
                }
            }
        }
        return StringUtil.VACIO;
    }

    /**
     *
     * @param rua
     * @return
     */
    public String obtenerPropietarioActual(String rua) {
        if (rua != null && !StringUtil.VACIO.equals(rua.trim())) {
            List<AmaTarjetaPropiedad> listTrj = ejbAmaTarjetaPropiedadFacade.buscarTarXNroRua(rua);
            if (!JsfUtil.isNullOrEmpty(listTrj)) {
                for (AmaTarjetaPropiedad tarj : listTrj) {
                    if (tarj.getActivo() == JsfUtil.TRUE) {
                        return obtenerDescPersona(tarj.getPersonaCompradorId());
                    }
                }
            }
        }
        return StringUtil.VACIO;
    }

    /**
     *
     * @param persona
     * @return
     */
    public String obtenerDocumentoPersona(SbPersona persona) {
        String res = StringUtil.VACIO;
        if (persona != null) {
            res = ("TP_PER_NAT".equals(persona.getTipoId().getCodProg()) && persona.getTipoDoc() == null) ? "DNI - " + persona.getNumDoc()
                    : (("TP_PER_NAT".equals(persona.getTipoId().getCodProg()) && persona.getTipoDoc() != null) ? ("TP_DOCID_DNI".equals(persona.getTipoDoc().getCodProg()) ? "DNI - " + persona.getNumDoc()
                    : ("TP_DOCID_CE".equals(persona.getTipoDoc().getCodProg())) ? "C. E. - " + persona.getNumDoc() : StringUtil.VACIO) : "RUC - " + persona.getRuc());
        }
        return res;
    }

    /**
     *
     * @param amaGuia
     * @return
     */
    public List<Map> tablaAccesorios(AmaGuiaTransito amaGuia) {
        List<Map> listRes = null;
        if (amaGuia != null) {
            short condicion = obtenerEstadoDeposito(amaGuia);
            if (!Objects.equals(condicion, -1)) {
                listRes = ejbAmaInventarioAccesoriosFacade.listAccesAllArmas(amaGuia, condicion);
            }
        }
        return listRes;
    }

    /**
     * FUNCION QUE OBTIENE EL ESTADO DE DEPOSITO DE ACCESORIO Y/0 CONDICION
     * ALMACEN DE ARMA, SEGUN TIPO DE GUIA (Y EL TIPO DE ORIGEN)
     *
     * @param amaGuia
     * @return
     */
    public short obtenerEstadoDeposito(AmaGuiaTransito amaGuia) {
        short condicion = -1;
        if (amaGuia != null) {
            switch (amaGuia.getTipoGuiaId().getCodProg()) {
                case "TP_GTGAMAC_CFA":
                case "TP_GTGAMAC_DDV":
                case "TP_GTGAMAC_REC":
                    condicion = JsfUtil.TRUE;//1
                    if (amaGuia.getFechaSalida() != null) {
                        condicion = JsfUtil.FALSE;//0
                    }
                    break;
                case "TP_GTGAMAC_EXH":
                    condicion = JsfUtil.TRUE;//1
                    if (amaGuia.getFechaSalida() != null
                            || (Objects.equals(amaGuia.getTipoOperacionId() == null ? StringUtil.VACIO : amaGuia.getTipoOperacionId().getCodProg(), "TP_OPE_REN")
                            && amaGuia.getGuiaReferenciadaId() != null)) {
                        condicion = JsfUtil.FALSE;//0
                    }
                    break;
                case "TP_GTGAMAC_SUC":
                    condicion = JsfUtil.TRUE;//1
                    if (amaGuia.getFechaSalida() != null && amaGuia.getFechaLlegada() == null) {
                        condicion = JsfUtil.FALSE;//0
                    }
                    break;
                case "TP_GTGAMAC_IMP":
                case "TP_GTGAMAC_REI":
                    condicion = JsfUtil.FALSE;//0
                    if (amaGuia.getFechaLlegada() != null) {
                        condicion = JsfUtil.TRUE;//1
                    }
                    break;
                case "TP_GTGAMAC_AGE":
                case "TP_GTGAMAC_COL":
                case "TP_GTGAMAC_SAL":
                    condicion = JsfUtil.FALSE;//0
                    break;
                case "TP_GTGAMAC_POL":
                    if (amaGuia.getTipoOrigen() != null) {
                        switch (amaGuia.getTipoOrigen().getCodProg()) {
                            case "TP_GTORDES_ALMS":
                                condicion = JsfUtil.TRUE;
                                break;
                            case "TP_GTORDES_LOC":
                                condicion = JsfUtil.FALSE;
                                break;
                            default:
                                break;
                        }
                        if (amaGuia.getFechaSalida() != null) {
                            condicion = JsfUtil.FALSE;//0
                        }
                    }
                    break;
                default:
                    if (amaGuia.getTipoGuiaId().getTipoId() != null) {
                        switch (amaGuia.getTipoGuiaId().getTipoId().getCodProg()) {
                            case "TP_GTGAMAC_DON":
                                condicion = JsfUtil.TRUE;//1
                                if (amaGuia.getFechaSalida() != null) {
                                    condicion = JsfUtil.FALSE;//0
                                }
                                break;
                            case "TP_GTGAMAC_SUC":
                                condicion = JsfUtil.TRUE;//1
                                if (amaGuia.getFechaSalida() != null && amaGuia.getFechaLlegada() == null) {
                                    condicion = JsfUtil.FALSE;//0
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    break;
            }
        }
        return condicion;
    }

    /**
     * Función que obtiene la cantidad de días hábiles entre dos fechas.
     *
     * @author Gino Chávez
     * @param fechaInicial
     * @param fechaFinal
     * @return
     */
    public int obtenerCantidadDiasHabiles(Calendar fechaInicial, Calendar fechaFinal) {
        int totalDias = -1;
        Calendar fechaTemporal = new GregorianCalendar();
        fechaTemporal.setTime(fechaInicial.getTime());
        while (fechaTemporal.before(fechaFinal) || fechaTemporal.equals(fechaFinal)) {
            if (fechaTemporal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaTemporal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                totalDias++;
            }
            fechaTemporal.add(Calendar.DATE, 1);
        }
        return totalDias;
    }

    /**
     * Función que obtiene la cantidad de días calendarios entre dos fechas.
     *
     * @param fechaInicial
     * @param fechaFinal
     * @return
     */
    public int obtenerCantidadDiasCalendario(Calendar fechaInicial, Calendar fechaFinal) {
        int totalDias = -1;
        Calendar fechaTemporal = new GregorianCalendar();
        fechaTemporal.setTime(fechaInicial.getTime());
        while (fechaTemporal.before(fechaFinal) || fechaTemporal.equals(fechaFinal)) {
            totalDias++;
            fechaTemporal.add(Calendar.DATE, 1);
        }
        return totalDias;
    }

    /**
     * Función que retorna un flag que indica si se encuentra fuera de horario
     * para realizar el depósito de armas.
     *
     * @return
     */
    public Boolean fueraHorario() {
        Calendar fecha = Calendar.getInstance();
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        Calendar fechaTemporal = new GregorianCalendar();
        if (fechaTemporal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || fechaTemporal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || hora >= 17) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * FUNCIÓN QUE OBTIENE EL PROCESO
     *
     * @param numExp
     * @author Gino Chávez
     * @return Proceso
     */
    public Proceso obtenerProceso(String numExp) {
        Proceso res = null;
        if (numExp != null && !StringUtil.VACIO.equals(numExp.trim())) {
            res = ejbProcesoFacade.obtenerProcesoPorExpediente(numExp);
        }
        return res;
    }

    /**
     * FUNCIÓN QUE VALIDA SI LA GUÍA DE TRÁNSITO, PARA EL EXPEDIENTE
     * SELECCIONADO, FUÉ OBSERVADA.
     *
     * @param numExp, String. Número de Expediente.
     * @return TRUE: Guía observada, FALSE: Guía no observada.
     * @author Gino Chávez
     */
    public Boolean esObservado(String numExp) {
        Boolean res = Boolean.FALSE;
        AmaGuiaTransito amaGuia = null;
        try {
            if (numExp != null) {
                amaGuia = ejbAmaGuiaTransitoFacade.obtenerGuiaXNroExp(numExp);
                if (amaGuia != null && "TP_GTGAMAC_OBS".equalsIgnoreCase(amaGuia.getEstadoId().getCodProg())) {
                    res = Boolean.TRUE;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * FUNCIÓN QUE VALIDA SI LA GUÍA DE TRÁNSITO, PARA EL EXPEDIENTE
     * SELECCIONADO, FUÉ DENEGADA.
     *
     * @param numExp, String. Número de Expediente.
     * @return TRUE: Guía denegada, FALSE: Guía no denegada.
     * @author Gino Chávez
     */
    public Boolean esDenegado(String numExp) {
        Boolean res = Boolean.FALSE;
        AmaGuiaTransito amaGuia = null;
        try {
            if (numExp != null) {
                amaGuia = ejbAmaGuiaTransitoFacade.obtenerGuiaXNroExp(numExp);
                if (amaGuia != null) {
                    if ("TP_GTGAMAC_DEN".equalsIgnoreCase(amaGuia.getEstadoId().getCodProg())) {
                        res = Boolean.TRUE;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * FUNCIÓN QUE VALIDA SI LA GUÍA DE TRÁNSITO, PARA EL EXPEDIENTE
     * SELECCIONADO, FUÉ ABANDONADA.
     *
     * @param numExp, String. Número de Expediente.
     * @return TRUE: Guía abandonada, FALSE: Guía no abandonada.
     * @author Gino Chávez
     */
    public Boolean esAbandonado(String numExp) {
        Boolean res = Boolean.FALSE;
        AmaGuiaTransito amaGuia = null;
        try {
            if (numExp != null) {
                amaGuia = ejbAmaGuiaTransitoFacade.obtenerGuiaXNroExp(numExp);
                if (amaGuia != null) {
                    if ("TP_GTGAMAC_IMP".equalsIgnoreCase(amaGuia.getEstadoId().getCodProg())) {
                        res = Boolean.TRUE;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * FUNCIÓN QUE VALIDA SI LA GUÍA DE TRÁNSITO, PARA EL EXPEDIENTE
     * SELECCIONADO, FUÉ FINALIZADA.
     *
     * @param numExp, String. Número de Expediente.
     * @return TRUE: Guía finalizada, FALSE: Guía no finalizada.
     * @author Gino Chávez
     */
    public Boolean esFinalizado(String numExp) {
        Boolean res = Boolean.FALSE;
        AmaGuiaTransito amaGuia = null;
        try {
            if (numExp != null) {
                amaGuia = ejbAmaGuiaTransitoFacade.obtenerGuiaXNroExp(numExp);
                if (amaGuia != null) {
                    if ("TP_GTGAMAC_FIN".equalsIgnoreCase(amaGuia.getEstadoId().getCodProg())) {
                        res = Boolean.TRUE;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * FUNCIÓN QUE VALIDA SI LA GUÍA DE TRÁNSITO, PARA EL EXPEDIENTE
     * SELECCIONADO, FUÉ VISADA.
     *
     * @param numExp, String. Número de Expediente.
     * @return TRUE: Guía visada, FALSE: Guía no visada.
     * @author Gino Chávez
     */
    public Boolean esVisado(String numExp) {
        Boolean res = Boolean.FALSE;
        AmaGuiaTransito amaGuia = null;
        try {
            if (numExp != null) {
                amaGuia = ejbAmaGuiaTransitoFacade.obtenerGuiaXNroExp(numExp);
                if (amaGuia != null) {
                    if ("TP_GTGAMAC_VIS".equalsIgnoreCase(amaGuia.getEstadoId().getCodProg())) {
                        res = Boolean.TRUE;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Boolean esActaDevolucion(Long idDevolucion) {
        return ejbAmaGuiaTransitoFacade.existeActaDevolucion(idDevolucion);
    }

    /**
     * Función que retorna un valor entero, el cual indica si es una guía
     * pendiente, vencida o procesada, de acuerdo a la fecha de vencimiento y
     * fechas de llegada a almacen SUCAMEC y salida de almacén SUCAMEC
     *
     * @param amaGuia
     * @return 0: vencido, 1:pendiente, 2:procesado
     */
    public int estadoGuia(AmaGuiaTransito amaGuia) {
        if (amaGuia != null) {
            if (Objects.equals(amaGuia.getTipoGuiaId().getTipoId().getCodProg(), "TP_GTGAMAC_SUC")) {
                if (amaGuia.getFechaSalida() != null && amaGuia.getFechaLlegada() != null) {
                    return 2;
                } else if (amaGuia.getFechaVencimiento() != null
                        && JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(amaGuia.getFechaVencimiento())) <= 0) {
                    return 1;
                } else {
                    return 0;
                }
            } else if (amaGuia.getFechaSalida() != null || amaGuia.getFechaLlegada() != null) {
                return 2;
            } else if (amaGuia.getFechaVencimiento() != null
                    && JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(amaGuia.getFechaVencimiento())) <= 0) {
                return 1;
            } else {
                return 0;
            }

        }
        return -1;
    }

    /**
     *
     * @param item
     * @return
     */
    public Boolean flagActaSeleccionable(Map item) {
        if (item != null) {
            if (Objects.equals((String) item.get("ESTADOGUIA"), "CREADO")
                    || (Objects.equals((String) item.get("ESTADOGUIA"), "FINALIZADO") && item.get("DEVOLUCION_ID") != null)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     *
     * @param item
     * @return
     */
    public Boolean flagVer(Map item) {
        Boolean res = Boolean.FALSE;
        if (item.get("IDGUIA") != null) {
            res = Boolean.TRUE;
        }
        return res;
    }

    /**
     *
     * @param item
     * @param isCoordinador
     * @param isEvaluador
     * @return
     */
    public Boolean flagAnularEditar(Map item, Boolean isCoordinador, Boolean isEvaluador) {
        Boolean res = Boolean.FALSE;
        if (isEvaluador
                && item.get("IDGUIA") != null
                && validarEstadoPorFlag(item, "ANULAR", isCoordinador, isEvaluador)) {
            res = Boolean.TRUE;
        }
        return res;
    }

    /**
     *
     * @param item
     * @param isCoordinador
     * @param isEvaluador
     * @return
     */
    public Boolean flagCrear(Map item, Boolean isCoordinador, Boolean isEvaluador) {
        Boolean res = Boolean.FALSE;
        if ((isEvaluador
                && item.get("IDGUIA") == null
                && validarEstadoPorFlag(item, "CREAR", isCoordinador, isEvaluador))) {//&& (StringUtil.VACIO.equalsIgnoreCase(obtenerEstadoExp(item)))
            res = Boolean.TRUE;
        }
        return res;
    }

    /**
     *
     * @param item
     * @param isCoordinador
     * @param isEvaluador
     * @return
     */
    public Boolean flagVerPdf(Map item, Boolean isCoordinador, Boolean isEvaluador) {
        Boolean res = Boolean.FALSE;
        if (validarEstadoPorFlag(item, "VERPDF", isCoordinador, isEvaluador)) {
            res = Boolean.TRUE;
        }
        return res;
    }

    /**
     *
     * @param item
     * @param isCoordinador
     * @param isEvaluador
     * @return
     */
    public Boolean flagRectificar(Map item, Boolean isCoordinador, Boolean isEvaluador) {
        Boolean res = Boolean.FALSE;
        if (validarEstadoPorFlag(item, "RECT", isCoordinador, isEvaluador)) {
            res = Boolean.TRUE;
        }
        return res;
    }

    /**
     *
     * @param item
     * @param isCoordinador
     * @param isEvaluador
     * @return
     */
    public Boolean flagMultiple(Map item, Boolean isCoordinador, Boolean isEvaluador) {
        Boolean res = Boolean.FALSE;
        if (item != null) {
            if (validarEstadoPorFlag(item, "MULTIPLE", isCoordinador, isEvaluador)) {
                res = Boolean.TRUE;
            }
        }
        return res;
    }

    /**
     *
     * @param item
     * @param isCoordinador
     * @param isEvaluador
     * @return
     */
    public Boolean flagNoMultiple(Map item, Boolean isCoordinador, Boolean isEvaluador) {
        Boolean res = Boolean.FALSE;
        if (item != null) {
            if (validarEstadoPorFlag(item, "NOMULTIPLE", isCoordinador, isEvaluador)) {
                res = Boolean.TRUE;
            }
        }
        return res;
    }

    /**
     *
     * @param tipoGamac
     * @return
     */
    public Boolean flagMostrarListMuniciones(TipoGamac tipoGamac) {
        if (tipoGamac != null) {
            if ("TP_GTGAMAC_MUN".equalsIgnoreCase(tipoGamac.getCodProg())
                    || "TP_GTGAMAC_SAL".equalsIgnoreCase(tipoGamac.getCodProg())
                    || "TP_GTGAMAC_AGE".equalsIgnoreCase(tipoGamac.getCodProg())
                    || "TP_GTGAMAC_SUC".equalsIgnoreCase(tipoGamac.getCodProg())
                    || "TP_GTGAMAC_DDV".equalsIgnoreCase(tipoGamac.getCodProg())
                    || "TP_GTGAMAC_DON".equalsIgnoreCase(tipoGamac.getCodProg())                    
                    || "TP_GTGAMAC_ING".equalsIgnoreCase(tipoGamac.getCodProg())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * FUNCIÓN QUE VALIDA EL ESTADO DE LA GUÍA SEGÚN ACCIÓN Y TIPO DE GUÍA
     *
     * @param item, Map. Datos de Guía.
     * @param flag, String. Acción sobre la Guía.
     * @param isCoordinador
     * @param isEvaluador
     * @return TRUE: Guía corregida, FALSE: Guía no corregida.
     * @author Gino Chávez
     */
    public Boolean validarEstadoPorFlag(Map item, String flag, Boolean isCoordinador, Boolean isEvaluador) {
        Boolean res = Boolean.FALSE;
        if (item != null) {
            String estadoGuia = StringUtil.VACIO;
            if (item.get("IDGUIA") != null) {
                estadoGuia = (String) item.get("COD_EG");
                switch (flag) {
                    case "ANULAR":
                        if (!"TP_GTGAMAC_FIN".equalsIgnoreCase(estadoGuia)
                                && !"TP_GTGAMAC_DEN".equalsIgnoreCase(estadoGuia)
                                && !"TP_GTGAMAC_IMP".equalsIgnoreCase(estadoGuia)
                                && !"TP_GTGAMAC_FIR".equalsIgnoreCase(estadoGuia)) {
                            res = Boolean.TRUE;
                        }
                        break;
                    case "CREAR":
                        if (!"TP_GTGAMAC_DEN".equalsIgnoreCase(estadoGuia)
                                && !"TP_GTGAMAC_IMP".equalsIgnoreCase(estadoGuia)) {
                            res = Boolean.TRUE;
                        }
                        break;
                    case "VERPDF":
                        if (("TP_GTGAMAC_CRE".equalsIgnoreCase(estadoGuia)
                                || "TP_GTGAMAC_OBS".equalsIgnoreCase(estadoGuia)
                                || "TP_GTGAMAC_APR".equalsIgnoreCase(estadoGuia)
                                || "TP_GTGAMAC_VIS".equalsIgnoreCase(estadoGuia)
                                || "TP_GTGAMAC_FIR".equalsIgnoreCase(estadoGuia))
                                && (!"TP_GTGAMAC_DEN".equalsIgnoreCase(estadoGuia)
                                && !"TP_GTGAMAC_IMP".equalsIgnoreCase(estadoGuia))) {
                            res = Boolean.TRUE;
                        }
                        break;
                    case "MULTIPLE":
                        if (((!"TP_GTGAMAC_FIN".equalsIgnoreCase(estadoGuia)
                                && (!"TP_GTGAMAC_DEN".equalsIgnoreCase(estadoGuia)
                                && !"TP_GTGAMAC_IMP".equalsIgnoreCase(estadoGuia))))
                                && !("TP_GTGAMAC_OBS".equalsIgnoreCase(estadoGuia) && isCoordinador)) {
                            res = Boolean.TRUE;
                        }
                        break;
                    case "NOMULTIPLE":
                        if ((("TP_GTGAMAC_FIN".equalsIgnoreCase(estadoGuia)
                                || ("TP_GTGAMAC_DEN".equalsIgnoreCase(estadoGuia)
                                || "TP_GTGAMAC_IMP".equalsIgnoreCase(estadoGuia))))
                                || (isCoordinador && "TP_GTGAMAC_OBS".equalsIgnoreCase(estadoGuia))) {
                            res = Boolean.TRUE;
                        }
                        break;
                    case "RECT":
                        if (isEvaluador && "TP_GTGAMAC_FIN".equalsIgnoreCase(estadoGuia)) {
                            Date fechaVenc = item.get("FEC_VENC") == null ? null : toDate((Timestamp) item.get("FEC_VENC"));
                            if ((Objects.equals((Date) item.get("FEC_LLEGADA"), null) && Objects.equals((Date) item.get("FEC_SALIDA"), null)) && (fechaVenc != null && JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(fechaVenc)) <= 0)) {
                                res = Boolean.TRUE;
                            }
                        }
                        break;
                    default:
                        break;
                }
            } else if ("CREAR".equals(flag) || "MULTIPLE".equals(flag)) {
                Boolean resAux = Boolean.TRUE;
                res = resAux;
            }
        }
        return res;
    }

    /**
     *
     * @param nroSerie
     * @return
     */
    public Boolean verificarSerieEnDisca(String nroSerie) {
        try {
            if (nroSerie != null && !StringUtil.VACIO.equals(nroSerie)) {
                //List<Map> listArmasDisca = ejbRma1369Facade.buscarArmaRMA(nroSerie, null, null);
                List<Map> listArmasDisca = ejbAmaGuiaTransitoFacade.buscarArmaRenagiMigra(nroSerie, null, null);
                if (listArmasDisca != null) {
                    Map itemArmaDisca = null;
                    for (Map arma : listArmasDisca) {
                        if (Objects.equals((String) arma.get("NRO_SERIE"), nroSerie.toUpperCase())) {
                            itemArmaDisca = arma;
                            break;
                        }
                    }
//                    if (itemArmaDisca != null) {
//                        if (Objects.equals(itemArmaDisca.get("ESTADO"), "CANCELADO")
//                                && (Objects.equals((String) itemArmaDisca.get("TIPO_PROPIETARIO"), "PERS. NATURAL")
//                                || Objects.equals((String) itemArmaDisca.get("TIPO_PROPIETARIO"), "EXTRANJERO"))) {
//
//                            String fechaLimite = ejbSbParametroFacade.obtenerParametroXNombre("amaDeposito_cita_fecha_limite").getValor();//JsfUtil.bundle("fecha_parametrizacion_guias");
//                            Date c_hoy = new Date();
//
//                            if (JsfUtil.getFechaSinHora(c_hoy).compareTo(JsfUtil.stringToDate(fechaLimite, "dd/MM/yyyy")) > 0) {
//                                Map fechas = ejbRma1369Facade.obtenerFechasArmaAnulada(String.valueOf(itemArmaDisca.get("NRO_LIC")), String.valueOf(itemArmaDisca.get("NRO_SERIE")));
//                                if (fechas != null && !fueraHorario()) {
//                                    //Validación para que no sea posible registrar un acta sin cita previa
//                                    List<AmaTurnoActa> listTurActa = ejbAmaTurnoActaFacade.listarPorLicSerie(Long.valueOf(itemArmaDisca.get("NRO_LIC") == null ? "0" : itemArmaDisca.get("NRO_LIC").toString()), String.valueOf(itemArmaDisca.get("NRO_SERIE")), new Date());
//                                    if (listTurActa == null) {
////                                        newInvArma.setSerie(null);
//                                        return Boolean.FALSE;
//                                    }
//                                }
//                            }
//                        }
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }

    /**
     *
     * @param nroSerie
     * @param nroLic
     * @return
     */
    public Boolean setearNroLicDisca(String nroSerie, String nroLic) {
        if (nroSerie != null && !StringUtil.VACIO.equals(nroSerie) && nroLic != null && !StringUtil.VACIO.equals(nroLic)) {
            //List<Map> listArmasDisca = ejbRma1369Facade.buscarArmaRMA(nroSerie, null, nroLic);
            List<Map> listArmasDisca = ejbAmaGuiaTransitoFacade.buscarArmaRenagiMigra(nroSerie, null, nroLic);
            if (listArmasDisca != null && listArmasDisca.size() > 0) {
                Map itemArmaDisca = listArmasDisca.get(0);
                if (itemArmaDisca != null) {
//                    if (Objects.equals(itemArmaDisca.get("ESTADO"), "CANCELADO")
//                            && (Objects.equals((String) itemArmaDisca.get("TIPO_PROPIETARIO"), "PERS. NATURAL")
//                            || Objects.equals((String) itemArmaDisca.get("TIPO_PROPIETARIO"), "EXTRANJERO"))) {
//                        Map fechas = ejbRma1369Facade.obtenerFechasArmaAnulada(String.valueOf(itemArmaDisca.get("NRO_LIC")), String.valueOf(itemArmaDisca.get("NRO_SERIE")));
//                        if (fechas != null) {
                    return Boolean.TRUE;
//                        }
//                    }
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * METODO PARA OBTENER EL NUMERO CORRELATIVO PARA REGISTRAR UNA NUEVA
     * SOLICITUD DE RECOJO
     *
     * @param origen
     * @param codNum
     * @param amaGuia
     * @return El número correlativo que identificará a la Solicitud de Recojo
     */
    public String numeroActa(String codNum, AmaGuiaTransito amaGuia) {
        String correlativo = StringUtil.VACIO;
        String ceros = StringUtil.VACIO;
        String tipo = StringUtil.VACIO;
        String codTipoArticulo = StringUtil.VACIO;
        String codTipoArma = StringUtil.VACIO;
        String iniTipoArma = StringUtil.VACIO;
        String tipoIngreso = StringUtil.VACIO;
        if (amaGuia != null) {
            if (amaGuia.getTipoIngreso() != null) {
                if ("TP_ING_DEF".equalsIgnoreCase(amaGuia.getTipoIngreso().getCodProg())) {
                    tipoIngreso = "D";
                } else if ("TP_ING_TEM".equalsIgnoreCase(amaGuia.getTipoIngreso().getCodProg())) {
                    tipoIngreso = "T";
                }
            }
            if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioArmaList())) {
                for (AmaInventarioArma invArma : amaGuia.getAmaInventarioArmaList()) {
                    codTipoArma = invArma.getModeloId().getTipoArmaId().getCodProg();
                    codTipoArticulo = invArma.getModeloId().getTipoArticuloId().getCodProg();
                    break;
                }
            } else if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaGuiaMunicionesList())) {
                codTipoArticulo = "TP_ART_MUNI";
            } else if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioAccesoriosList())) {
                codTipoArticulo = "TP_ART_ACCE";
            }
        }
        //String codNumeracion = StringUtil.VACIO;
        switch (codTipoArticulo) {
            //Valor a concatenar para el código del Acta (Letras iniciales y Numeración)
            case "TP_ART_ARMA":
                switch (codTipoArma) {
                    case "TP_ARMPIS":
                        iniTipoArma = "PI";
//                        codNumeracion = "TP_NUM_DEP_PIS";
                        break;
                    case "TP_ARMREV":
                        iniTipoArma = "RE";
//                        codNumeracion = "TP_NUM_DEP_REV";
                        break;
                    case "TP_ARMCAR":
                        iniTipoArma = "CA";
//                        codNumeracion = "TP_NUM_DEP_CAR";
                        break;
                    case "TP_ARMESC":
                        iniTipoArma = "ES";
//                        codNumeracion = "TP_NUM_DEP_ESC";
                        break;
                    case "TP_ARMCAE":
                        iniTipoArma = "CE";
//                        codNumeracion = "TP_NUM_DEP_CES";
                        break;
                    default:
                        iniTipoArma = "UC";
                        break;
                }
                break;
            case "TP_ART_ARGE":
                switch (codTipoArma) {
                    case "TP_ARM_FUS":
                        iniTipoArma = "FU";
//                        codNumeracion = "TP_NUM_DEP_FUS";
                        break;
                    case "TP_ARM_PAM":
                        iniTipoArma = "PA";
//                        codNumeracion = "TP_NUM_DEP_PAM";
                        break;
                    case "TP_ARM_PCAR":
                        iniTipoArma = "PC";
                        break;
                    case "TP_ARM_SFU":
                        iniTipoArma = "SF";
                        break;
                    default:
                        iniTipoArma = "AG";
                        break;
                }
                break;
            case "TP_ART_MUNI":
                iniTipoArma = "MU";
//                codNumeracion = "TP_NUM_DEP_MUN";
                break;
            case "TP_ART_REPU":
                iniTipoArma = StringUtil.VACIO;
//                codNumeracion = StringUtil.VACIO;
                break;
            case "TP_ART_ARNEU":
                iniTipoArma = "NE";
//                codNumeracion = "TP_NUM_DEP_NEU";
                break;
            case "TP_ART_INSU":
                iniTipoArma = StringUtil.VACIO;
//                codNumeracion = StringUtil.VACIO;
                break;
            case "TP_ART_CONE":
                iniTipoArma = StringUtil.VACIO;
//                codNumeracion = StringUtil.VACIO;
                break;
            case "TP_ART_ARFO":
                iniTipoArma = "FO";
//                codNumeracion = "TP_NUM_DEP_FOG";
                break;
            case "TP_ART_ARFA":
                iniTipoArma = "FC";
//                codNumeracion = "TP_NUM_DEP_FAC";
                break;
            case "TP_ART_ARJU":
                iniTipoArma = "JU";
//                codNumeracion = "TP_NUM_DEP_JUG";
                break;
            case "TP_ART_ARES":
                iniTipoArma = "SP";
//                codNumeracion = "TP_NUM_DEP_AES";
                break;
            case "TP_ART_ACCE":
                iniTipoArma = "AC";
//                codNumeracion = "TP_NUM_DEP_ACC";
                break;
            case "TP_ART_COL":
                iniTipoArma = "CO";
                break;
            default:
                iniTipoArma = "OT";
                break;
        }

        Long numeracion = ejbSbNumeracionFacade.selectNumeroCodProg(codNum);
        SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
        String anio = formatAnio.format(new Date());
        switch (String.valueOf(numeracion).length()) {
            case 1:
                ceros = "00000";
                break;
            case 2:
                ceros = "0000";
                break;
            case 3:
                ceros = "000";
                break;
            case 4:
                ceros = "00";
                break;
            case 5:
                ceros = "0";
                break;
            case 6:
                ceros = StringUtil.VACIO;
                break;
            default:
                ceros = StringUtil.VACIO;
                break;
        }
        if (listaIdTipoGuiaBus(deposito).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
            tipo = "I";
        } else if (listaIdTipoGuiaBus(devolucion).contains(String.valueOf(amaGuia.getTipoGuiaId().getId()))) {
            tipo = "D";
        }
        correlativo = iniTipoArma + "-" + tipo + tipoIngreso + anio + ceros + numeracion;
        return correlativo;
    }

    /**
     *
     * @param usuLogueado
     * @param amaGuia
     * @return
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Boolean enviarNotificacion(SbUsuario usuLogueado, AmaGuiaTransito amaGuia) {
        try {
            NeDocumento neDocumento = new NeDocumento();
            neDocumento.setActivo(JsfUtil.TRUE);
            neDocumento.setAnoExp(0);
            neDocumento.setNroExp(0);
            neDocumento.setSecExp(0);
            neDocumento.setNroExpediente(amaGuia.getNroExpediente());
            neDocumento.setAsunto("Se procede a la notificación de la Guía de Tránsito " + amaGuia.getNroGuia() + " con fecha " + ReportUtilTurno.formatoFechaDdMmYyyy(amaGuia.getFechaEmision()));
            neDocumento.setAreaId(usuLogueado.getAreaId());
            neDocumento.setFechaDoc(new Date());
            neDocumento.setEstadoId(ejbSbTipoFacade.tipoPorCodProg("TP_EV_ENV"));
            //
            String nomArch = UUID.randomUUID().toString().substring(0, 7) + "_" + neDocumento.getNroExpediente() + "_0.pdf";
            SbTipo estCre = ejbSbTipoFacade.tipoPorCodProg("TP_EST_CRE"),
                    adjFir = ejbSbTipoFacade.tipoPorCodProg("TP_ADJ_FIR");
            SbTipo evCre = null;
            neDocumento.setNeEventoList(new ArrayList());
            for (int i = 0; i < 3; i++) {
                switch (i) {
                    case 0:
                        evCre = ejbSbTipoFacade.tipoPorCodProg("TP_EV_CRE");
                        break;
                    case 1:
                        evCre = ejbSbTipoFacade.tipoPorCodProg("TP_EV_FIR");
                        break;
                    case 2:
                        evCre = ejbSbTipoFacade.tipoPorCodProg("TP_EV_ENV");
                        break;
                }
                // Grabar Traza //
                NeEvento e = new NeEvento(new Date(), JsfUtil.TRUE, neDocumento,
                        estCre, evCre, new SbUsuario(usuLogueado.getId()));
                neDocumento.getNeEventoList().add(e);
                //
            }
            NeArchivo a = new NeArchivo(new Date(),
                    "pdf", nomArch, JsfUtil.TRUE, neDocumento, adjFir,
                    estCre);
            neDocumento.setNeArchivoList(new ArrayList());
            neDocumento.getNeArchivoList().add(a);

            neDocumento.setNroDoc(amaGuia.getNroGuia());
            neDocumento.setPersonaId(amaGuia.getSolicitanteId());
            neDocumento.setReferencia("Guía de Tránsito " + amaGuia.getNroGuia() + " con Expediente N° " + amaGuia.getNroExpediente());
            neDocumento.setTipoId(ejbSbTipoFacade.tipoPorCodProg("TP_DOC_GT"));
            neDocumento.setUsuarioId(usuLogueado);
            ejbNeDocumentoFacade.create(neDocumento);
            String path = (ejbSbParametroFacade.obtenerParametroXNombre("PathUpload").getValor());
            FileUtils.copyInputStreamToFile(JsfUtil.obtenerArchivo((ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_guiasAma").getValor()), amaGuia.getId() + ".pdf", amaGuia.getId() + ".pdf", "application/pdf").getStream(), new File(path + nomArch));
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    /**
     * FUNCION QUE PERMITE GUARDAR LA LISTA DE DOCUMENTOS TRAMDOC A AMADOCUMENTO
     *
     * @param listDocTD
     * @param registro
     * @param usuLogueado
     * @param solicitante
     * @return
     */
    public List<AmaDocumento> listAmaDocumento(List<Documento> listDocTD, AmaGuiaTransito registro, SbUsuario usuLogueado, SbPersona solicitante) {
        List<AmaDocumento> listRes = new ArrayList<>();
        AmaDocumento docTemp = null;
        if (!JsfUtil.isNullOrEmpty(listDocTD)) {
            List<Documento> listDocTDTemp = new ArrayList<>();
            listDocTDTemp.addAll(listDocTD);
            //Bloque para quitar los registros que se eliminaron de la lista original
            if (!JsfUtil.isNullOrEmpty(registro.getAmaDocumentoList())) {
                listRes.addAll(registro.getAmaDocumentoList());
                for (AmaDocumento amaDoc : registro.getAmaDocumentoList()) {
                    Boolean remover = Boolean.TRUE;
                    for (Documento muniTemp : listDocTDTemp) {
                        if (amaDoc.getAsunto().equalsIgnoreCase(muniTemp.getIdTipoDocumento().getNombre())
                                && amaDoc.getNumero().equals(muniTemp.getNumero())
                                && (JsfUtil.getFechaSinHora(amaDoc.getFechaFirma()).compareTo(JsfUtil.getFechaSinHora(muniTemp.getFechaCreacion())) == 0)) {
                            remover = Boolean.FALSE;
                            break;
                        }
                    }
                    if (remover) {
                        listRes.remove(amaDoc);
                    }
                }
                //Se recorre la lista a guardar y se compara cada item para removerlo en caso ya exista en BD.
                for (Documento muniTemp : listDocTDTemp) {
                    for (AmaDocumento amaDoc : registro.getAmaDocumentoList()) {
                        if (amaDoc.getActivo() == JsfUtil.TRUE) {
                            if (amaDoc.getAsunto().equalsIgnoreCase(muniTemp.getIdTipoDocumento().getNombre())
                                    && amaDoc.getNumero().equals(muniTemp.getNumero())
                                    && (JsfUtil.getFechaSinHora(amaDoc.getFechaFirma()).compareTo(JsfUtil.getFechaSinHora(muniTemp.getFechaCreacion())) == 0)) {
                                listDocTD.remove(muniTemp);
                                break;
                            }
                        }
                    }
                }
            }
            //Bloque para añadir registros nuevos a la lista
            for (Documento doc : listDocTD) {
                docTemp = new AmaDocumento();
                docTemp.setActivo(JsfUtil.TRUE);
                //Encontrando equivalencia del tipo de documento de la tabla documento de cydoc
                TipoGamac tipoDoc = null;
                switch (doc.getIdTipoDocumento().getIdTipoDocumento().toString()) {
                    case "1":
                        tipoDoc = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_DOCUM_MEM");
                        break;
                    case "2":
                        tipoDoc = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_DOCUM_OFI");
                        break;
                    case "4":
                        tipoDoc = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_DOCUM_RES");
                        break;
                    case "6":
                        tipoDoc = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_DOCUM_ILE");
                        break;
                    case "12":
                        tipoDoc = ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_DOCUM_ITE");
                        break;
                }

                docTemp.setTipoDocumentoId(tipoDoc);
                docTemp.setUsuarioCreacionId(usuLogueado);
                docTemp.setNumero(doc.getNumero());
                //Se guarda en asunto el texto de tipo documento de TRAMDOC
                docTemp.setAsunto(doc.getIdTipoDocumento().getNombre());
                docTemp.setFechaFirma(doc.getFechaCreacion());
                docTemp.setFechaCreacion(new Date());
                docTemp.setNroExpediente(doc.getExpediente().getNumero());
                //
                docTemp.setAreaId(usuLogueado.getAreaId());
                docTemp.setPersonaId(solicitante);
                docTemp.setEventoTrazaDocId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_EVETR_CRE"));
                docTemp = (AmaDocumento) JsfUtil.entidadMayusculas(docTemp, StringUtil.VACIO);
                ejbAmaDocumentoFacade.create(docTemp);
                listRes.add(docTemp);
            }
            //
        }
        return listRes;
    }

    /**
     * Método que retorna la fecha sumado a la fecha ingresada el número de
     * meses que se desea
     *
     * @param fecha
     * @param meses
     * @return
     */
    public Date calculoFechaVencMeses(Date fecha, int meses) {
        String hoy = JsfUtil.dateToString(fecha, "dd/MM/yyyy");
        String[] dataTemp = hoy.split("/");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(dataTemp[2]), Integer.parseInt(dataTemp[1]) - 1, Integer.parseInt(dataTemp[0]));
        c.add(Calendar.MONTH, meses);
        return c.getTime();
    }

    /**
     *
     * @param amaGuiaSelect
     * @param itemSelectExp
     * @param itemGuia
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void accionAnularGuia(AmaGuiaTransito amaGuiaSelect, Map itemSelectExp, Map itemGuia) {
        try {
            if (itemSelectExp != null) {
                amaGuiaSelect = ejbAmaGuiaTransitoFacade.obtenerGuiaXNroExp((String) itemSelectExp.get("NROEXP"));
            } else if (itemGuia != null) {
                if (itemGuia.get("ID") != null) {
                    amaGuiaSelect = ejbAmaGuiaTransitoFacade.find(Long.valueOf(itemGuia.get("ID").toString()));
                }
            }
            if (amaGuiaSelect != null) {
                //Validación que determina se se debe anular o no el arma, accesorios y/o municiones.
                Boolean anular = Boolean.FALSE;
                Boolean isGuiaTransito = Boolean.FALSE;
                Boolean isActaDeposito = Boolean.FALSE;
                Boolean isActaDevolucion = Boolean.FALSE;
                //Si es un acta de depósito.
                if (listaIdTipoGuiaBus(deposito).contains(String.valueOf(amaGuiaSelect.getTipoGuiaId().getId()))) {
                    isActaDeposito = Boolean.TRUE;
                    anular = Boolean.TRUE;
                } else //Si se trata de una guía de tránsito (no es un acta de depósito ni de devolución)
                 if (!listaIdTipoGuiaBus(devolucion).contains(String.valueOf(amaGuiaSelect.getTipoGuiaId().getId()))) {
                        //se valida si se trata de una Guía de internamiento de importación de armas o reingreso a almacenes SUCAMEC
                        if (Objects.equals(amaGuiaSelect.getTipoGuiaId().getCodProg(), "TP_GTGAMAC_IMP")
                                || (Objects.equals(amaGuiaSelect.getTipoGuiaId().getCodProg(), "TP_GTGAMAC_REI"))) {
                            isGuiaTransito = Boolean.TRUE;
                        }
                    } else {
                        isActaDevolucion = Boolean.TRUE;
                    }
                if (isActaDevolucion) {
                    amaGuiaSelect.getGuiaReferenciadaId().setDevolucionId(null);
                    revertirCondicionSituacionArma(amaGuiaSelect, ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_SITU_INT"));
                }
                if (anular) {
                    if (amaGuiaSelect.getAmaInventarioArmaList() != null) {
                        for (AmaInventarioArma invArma : amaGuiaSelect.getAmaInventarioArmaList()) {
                            if (invArma.getActivo() == JsfUtil.TRUE) {
                                invArma.setActual(JsfUtil.FALSE);
                                invArma.setActivo(JsfUtil.FALSE);
                                if (invArma.getAmaInventarioAccesoriosList() != null) {
                                    for (AmaInventarioAccesorios acces : invArma.getAmaInventarioAccesoriosList()) {
                                        if (acces.getActivo() == JsfUtil.TRUE) {
                                            acces.setActivo(JsfUtil.FALSE);
                                        }
                                    }
                                }
                                setearArmasActualTrue(invArma);
                                ejbAmaInventarioArmaFacade.edit(invArma);
                            }
                        }
                    }
                    if (amaGuiaSelect.getAmaGuiaMunicionesList() != null) {
                        for (AmaGuiaMuniciones muni : amaGuiaSelect.getAmaGuiaMunicionesList()) {
                            muni.setActivo(JsfUtil.FALSE);
                        }
                    }
                } else if (isGuiaTransito) {
                    if (amaGuiaSelect.getAmaInventarioArmaList() != null) {
                        for (AmaInventarioArma invArma : amaGuiaSelect.getAmaInventarioArmaList()) {
                            if (invArma.getActivo() == JsfUtil.TRUE) {
                                anular = Boolean.FALSE;
                                //Eliminado si de trata de un arma creada al registrar la Guía de tipo importación o reingreso
                                if (isGuiaTransito && Objects.equals(invArma.getCodigo(), L_CERO)) {
                                    anular = Boolean.TRUE;
                                }
                                //Fin
                                if (anular) {
                                    invArma.setActivo(JsfUtil.FALSE);
                                    invArma.setActual(JsfUtil.FALSE);
                                    if (invArma.getAmaInventarioAccesoriosList() != null) {
                                        for (AmaInventarioAccesorios acces : invArma.getAmaInventarioAccesoriosList()) {
                                            if (acces.getActivo() == JsfUtil.TRUE) {
                                                acces.setActivo(JsfUtil.FALSE);
                                            }
                                        }
                                    }
                                    ejbAmaInventarioArmaFacade.edit(invArma);
                                }
                            }
                        }
                    }
                }
                //Inactivando comprobantes.
                String nroExp = StringUtil.VACIO;
                if (amaGuiaSelect.getNroExpediente() != null && !StringUtil.VACIO.equals(amaGuiaSelect.getNroExpediente())) {
                    nroExp = amaGuiaSelect.getNroExpediente();
                } else {
                    nroExp = "PENDIENTE_" + amaGuiaSelect.getId();
                }
                List<SbReciboRegistro> listReg = ejbSbReciboRegistroFacade.obtenerRecibosPorExpediente(nroExp);
                if (listReg != null) {
                    for (SbReciboRegistro reg : listReg) {
                        reg.setActivo(JsfUtil.FALSE);
                        ejbSbReciboRegistroFacade.edit(reg);
                    }
                }
                amaGuiaSelect.setActivo(JsfUtil.FALSE);
                ejbAmaGuiaTransitoFacade.edit(amaGuiaSelect);
                //Si es un acta de depósito
                if (isActaDeposito) {
                    //Si es rectificatoria de acta (deposito o devolucion)
                    if ((amaGuiaSelect.getTipoRegistroId() == null ? StringUtil.VACIO : amaGuiaSelect.getTipoRegistroId().getCodProg()).contains("TP_REGIST_REC")) {
                        if (amaGuiaSelect.getGuiaReferenciadaId() != null) {
                            cambiarEstadoCascadeActa(amaGuiaSelect.getGuiaReferenciadaId(), JsfUtil.TRUE);
                        }
                    }
                } else if (isGuiaTransito) {
                    if ((amaGuiaSelect.getTipoRegistroId() == null ? StringUtil.VACIO : amaGuiaSelect.getTipoRegistroId().getCodProg()).contains("TP_REGIST_REC")) {
                        if (amaGuiaSelect.getGuiaReferenciadaId() != null) {
                            amaGuiaSelect.getGuiaReferenciadaId().setActivo(JsfUtil.TRUE);
                            ejbAmaGuiaTransitoFacade.edit(amaGuiaSelect.getGuiaReferenciadaId());
                        }
                    }
                }
                if (itemSelectExp != null) {
                    amaGuiaTransitoController.buscarExpedientes(Boolean.TRUE);
                    RequestContext.getCurrentInstance().update("listarForm");
                } else {
                    amaDepositoArmasController.buscarActas(); //AQUI
                    RequestContext.getCurrentInstance().update("listaActasArmaForm");
                }
                JsfUtil.mensaje("El registro seleccionado ha sido eliminado con éxito.");
            } else {
                JsfUtil.mensajeError("No es posible eliminar el registro.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param amaGuia
     * @return
     */
    public SbPersona obtenerTitularArma(AmaGuiaTransito amaGuia) {
        SbPersona titular = null;
        if (amaGuia != null) {
            if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioArmaList())) {
                for (AmaInventarioArma amaInv : amaGuia.getAmaInventarioArmaList()) {
                    titular = amaInv.getPropietarioId();
                    break;
                }
            }
            if (titular == null) {
                if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaArmaList())) {
                    for (AmaArma amaArma : amaGuia.getAmaArmaList()) {
                        AmaTarjetaPropiedad amaTarj = (AmaTarjetaPropiedad) ejbAmaTarjetaPropiedadFacade.buscarTarXNroRua(amaArma.getNroRua()).get(0);
                        titular = amaTarj.getPersonaCompradorId();
                        break;
                    }
                }
            }
        }
        return titular;
    }

    /**
     *
     * @param idExp
     * @return
     */
    public Boolean estExpedienteApto(BigDecimal idExp) {
        if (idExp != null) {
            String accionExp = ejbExpedienteFacade.obtenerAccionXidExpediente(idExp);
            if ("Improcedente".equals(accionExp) || "Denegado".equals(accionExp)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     *
     * @param mod
     * @return
     */
    public String obtenerDescripcionArma(AmaModelos mod) {
        return (mod.getTipoArmaId() == null ? StringUtil.VACIO : mod.getTipoArmaId().getNombre())
                + StringUtil.ESPACIO + (mod.getMarcaId() == null ? StringUtil.VACIO : mod.getMarcaId().getNombre())
                + StringUtil.ESPACIO + mod.getModelo();
        //+ StringUtil.ESPACIO + obtenerCalibres(mod);
    }

    /**
     *
     * @param listInvArmas
     * @param condicion
     * @return
     */
    public List<Map> obtenerAccesoriosPorModelo(List<AmaInventarioArma> listInvArmas, short condicion) {
        List<Map> listModeloAcces = null;
        Map modeloAcces = null;
        if (!JsfUtil.isNullOrEmpty(listInvArmas)) {
            //Bloque para obtener el listado total de accesorios de las armas.
            List<AmaInventarioAccesorios> listAccesoriosIni = new ArrayList<>();
            List<AmaModelos> listModelos = new ArrayList<>();
            amaGuiaTransitoController.setListArticulos(new ArrayList<AmaInventarioArtconexo>());
            for (AmaInventarioArma invArma : listInvArmas) {
                if (invArma.getActivo() == JsfUtil.TRUE
                        //&& invArma.getActual() == JsfUtil.TRUE
                        && Objects.equals(invArma.getCondicionAlmacen(), condicion)) {
                    if (!JsfUtil.isNullOrEmpty(invArma.getAmaInventarioAccesoriosList())) {
                        for (AmaInventarioAccesorios acces : invArma.getAmaInventarioAccesoriosList()) {
                            if (acces.getActivo() == JsfUtil.TRUE
                                    && Objects.equals(acces.getEstadoDeposito(), condicion)) {
                                //Bloque para obtener los modelos de los accesorios
                                if (listModelos.isEmpty()) {
                                    listModelos.add(acces.getInventarioArmaId().getModeloId());
                                } else if (!listModelos.contains(acces.getInventarioArmaId().getModeloId())) {
                                    listModelos.add(acces.getInventarioArmaId().getModeloId());
                                }
                                //Fin del bloque.
                                //Bloque para obtener todos los artículos existentes
                                if (amaGuiaTransitoController.getListArticulos().isEmpty()) {
                                    amaGuiaTransitoController.getListArticulos().add(acces.getArticuloConexoId());
                                } else if (!amaGuiaTransitoController.getListArticulos().contains(acces.getArticuloConexoId())) {
                                    amaGuiaTransitoController.getListArticulos().add(acces.getArticuloConexoId());
                                }
                                //Fin del bloque
                                listAccesoriosIni.add(acces);//Lista total de accesorios
                            }
                        }
                    }
                }
            }
            //Fin del bloque
            //Bloque que obtiene el listado de modelos con sus respectivos accesorios.
            if (!listAccesoriosIni.isEmpty()) {
                if (!listModelos.isEmpty()) {
                    listModeloAcces = new ArrayList<>();
                    for (AmaModelos mod : listModelos) {
                        modeloAcces = new HashMap();
                        modeloAcces.put("modelo", mod);
                        modeloAcces.put("listaAcces", listAccesoriosArma(listAccesoriosIni, mod));
                        listModeloAcces.add(modeloAcces);
                    }
                }
            }
            //Fin del bloque.
        }
        return listModeloAcces;
    }

    /**
     *
     * @param listAccesorios
     * @param modelo
     * @return
     */
    public List<Map> listAccesoriosArma(List<AmaInventarioAccesorios> listAccesorios, AmaModelos modelo) {
        List<Map> listModeloAcces = null;
        Map modeloAcces = null;
        List<AmaInventarioAccesorios> listTempGlobal = new ArrayList(listAccesorios);
        if (!listTempGlobal.isEmpty()) {
            listModeloAcces = new ArrayList<>();
            List<AmaInventarioAccesorios> listTemp = null;
            int cant = 0;
            if (!JsfUtil.isNullOrEmpty(amaGuiaTransitoController.getListArticulos())) {
                for (AmaInventarioArtconexo art : amaGuiaTransitoController.getListArticulos()) {
                    cant = 0;
                    listTemp = new ArrayList(listTempGlobal);
                    if (!listTemp.isEmpty()) {
                        for (AmaInventarioAccesorios acces : listTemp) {
                            if (Objects.equals(acces.getInventarioArmaId().getModeloId(), modelo)
                                    && Objects.equals(art, acces.getArticuloConexoId())) {
                                listTempGlobal.remove(acces);
                                cant = cant + Integer.valueOf(acces.getCantidad().toString());
                            }
                        }
                    }
                    modeloAcces = new HashMap();
                    modeloAcces.put("articulo", art);
                    modeloAcces.put("cantidad", cant == 0 ? "-" : cant);
                    listModeloAcces.add(modeloAcces);
                }
            }
        }
        return listModeloAcces;
    }

    public boolean esInpe(String ruc) {
        if (ruc == null || ruc.isEmpty()) {
            return false;
        }
        return ruc.trim().equals(JsfUtil.bundle("amaRegEmpArma_rucInpe"));
    }
        
    /**
     * Función que valida si el arma internada en Almacenes SUCAMEC puede ser
     * devuelta, para ser devuelta debe contar con tarjeta de Propiedad emitida.
     * A
     *
     * @author Gino Chávez
     * @param actaDeposito
     * @return
     */
    public Boolean isDevolucionValida(AmaGuiaTransito actaDeposito) {
        Boolean res = Boolean.FALSE;
        try {
            if (actaDeposito != null) {
                if (!JsfUtil.isNullOrEmpty(actaDeposito.getAmaInventarioArmaList())) {
                    // Se obtiene el arma internada
                    AmaInventarioArma invArmaTemp = null;
                    for (AmaInventarioArma invArma : actaDeposito.getAmaInventarioArmaList()) {
                        if (invArma.getActivo() == JsfUtil.TRUE) {
                            invArmaTemp = invArma;
                            break;
                        }
                    }
                    //Fin
                    //Se evalúa el arma internada, debe cumplir algunos requisitos para poder ser devuelta.
                    if (invArmaTemp != null) {
                        //Si el arma es tipo de artículo 'ARMA NEUMÁTICA', entonces no requiere ninguna validacón adicional.
                        if (Objects.equals(invArmaTemp.getModeloId().getTipoArticuloId().getCodProg(), "TP_ART_ARNEU")
                                || Objects.equals(invArmaTemp.getModeloId().getTipoArticuloId().getCodProg(), "TP_ART_ARFO")
                                || Objects.equals(invArmaTemp.getModeloId().getTipoArticuloId().getCodProg(), "TP_ART_ARJU")) {
                            return Boolean.TRUE;
                        }
                        //Fin validación arma neumática
//                        AmaArma arma = ejbAmaArmaFacade.obtenerArmaPorModeloSerie(invArmaTemp.getModeloId().getId(), invArmaTemp.getSerie(), null);
                        if (invArmaTemp.getArmaId() != null) {
                            if (invArmaTemp.getArmaId().getNroRua() != null && (!JsfUtil.isNullOrEmpty(invArmaTemp.getArmaId().getAmaTarjetaPropiedadList()))) {
                                AmaTarjetaPropiedad tarjeta = null;
                                for (AmaTarjetaPropiedad amaTarjeta : invArmaTemp.getArmaId().getAmaTarjetaPropiedadList()) {
                                    if (amaTarjeta.getActivo() == JsfUtil.TRUE && amaTarjeta.getEmitido() == JsfUtil.TRUE) {
                                        tarjeta = amaTarjeta;
                                        break;
                                    }
                                }
                                //Debe existir tarjeta de propiedad del arma.
                                if (tarjeta != null) {
                                    //la tarjeta debe estar en estado vigente
                                    if (Objects.equals(tarjeta.getEstadoId().getCodProg(), "TP_EST_VIG")) {
                                        //Validación para verificar que el propietario del arma cuenta con licencia de uso de armas.
                                        Boolean conLicencia = Boolean.TRUE;
                                        if (Objects.equals(tarjeta.getPersonaCompradorId().getTipoId().getCodProg(), "TP_PER_NAT")) {
                                            if (tarjeta.getPersonaCompradorId().getNroCip() == null) {
                                                conLicencia = Boolean.FALSE;
                                                if (!JsfUtil.isNullOrEmpty(tarjeta.getPersonaCompradorId().getAmaLicenciaDeUsoList2())) {
                                                    for (AmaLicenciaDeUso lic : tarjeta.getPersonaCompradorId().getAmaLicenciaDeUsoList2()) {
                                                        if (lic.getActivo() == JsfUtil.TRUE && JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(lic.getFechaVencimietnto())) <= 0) {
                                                            conLicencia = Boolean.TRUE;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (Objects.equals(tarjeta.getPersonaCompradorId().getTipoId().getCodProg(), "TP_PER_JUR")) {
                                            conLicencia = Boolean.FALSE;
                                            //Si es RUC INPE no valida la licencia, solo la tarjeta
                                            if (esInpe(tarjeta.getPersonaCompradorId().getRuc())) {
                                                conLicencia = Boolean.TRUE;
                                            } else if(!Objects.equals(tarjeta.getModalidadId().getCodProg(), "TP_MOD_ESP")) {
                                                List<Map> listResGssp = ejbRma1369Facade.listResolucionesGsspPortaArmas(tarjeta.getPersonaCompradorId().getRuc() == null ? "0" : tarjeta.getPersonaCompradorId().getRuc());
                                                if (!JsfUtil.isNullOrEmpty(listResGssp)) {
                                                    for (Map gssp : listResGssp) {
                                                        if (gssp.get("FECVEN") != null && (((Date) gssp.get("FECVEN")).compareTo(JsfUtil.getFechaSinHora(new Date())) > 0)) {
                                                            conLicencia = Boolean.TRUE;
                                                            break;
                                                        }
                                                    }
                                                    if (!conLicencia) {
                                                        JsfUtil.mensajeError("El propietario cuenta con Autorización VENCIDA para brindar servicios de Seguridad.");
                                                    }
                                                } else {
                                                    List<AmaResolucion> listResolucionesIntg = ejbAmaResolucionFacade.buscarResolucionesVigentes(tarjeta.getPersonaCompradorId(), "'TP_PRTUP_AMA_GALT'", new Date());
                                                    if (!JsfUtil.isNullOrEmpty(listResolucionesIntg)) {
                                                        if (listResolucionesIntg.size() > 0) {
                                                            conLicencia = Boolean.TRUE;
                                                        }
                                                    } else {
                                                        JsfUtil.mensajeError("El propietario del arma NO cuenta con Autorización para brindar servicios de Seguridad o Polígono de Tiro.");
                                                    }
                                                    //JsfUtil.mensajeError("El propietario del arma NO cuenta con Autorización para brindar servicios de Seguridad.");
                                                }    
                                            } else {
                                                conLicencia = Boolean.TRUE;
                                            }
                                        }
                                        //Fin
                                        if (conLicencia) {
                                            //Seteo de solicitante de la devolución
                                            amaDevolucionArmasController.setPersonaReg(tarjeta.getPersonaCompradorId());
                                            //Fin
                                            invArmaTemp.setPropietarioId(tarjeta.getPersonaCompradorId());
                                            invArmaTemp.setNroRua(invArmaTemp.getArmaId().getNroRua());
                                            res = Boolean.TRUE;
                                            ejbAmaInventarioArmaFacade.edit(invArmaTemp);
                                        } else {
                                            JsfUtil.mensajeError("El propietario del arma no cuenta con Licencia de Uso vigente.");
                                        }
                                    } else if (Objects.equals(tarjeta.getEstadoId().getCodProg(), "TP_EST_NULIDAD")) {
                                        JsfUtil.mensajeError("No es posible realizar la devolución del arma debido a que la Tarjeta de Propiedad se encuentra en estado 'NULIDAD'");
                                    } else {
                                        JsfUtil.mensajeError("La Tarjeta de Propiedad del arma internada no se encuentra vigente.");
                                    }
                                } else {
                                    JsfUtil.mensajeError("NO se encontró una tarjeta de propiedad vigente para la serie "
                                            + (invArmaTemp.getSerie() == null ? StringUtil.VACIO : invArmaTemp.getSerie()) + " con modelo "
                                            + (invArmaTemp.getModeloId().getTipoArmaId().getNombre() + " " + invArmaTemp.getModeloId().getMarcaId().getNombre() + " " + invArmaTemp.getModeloId().getModelo()) + ".");
                                }
                            }
                        } else {
                            JsfUtil.mensajeError("NO se encontró una tarjeta de propiedad vigente para la serie "
                                    + (invArmaTemp.getSerie() == null ? StringUtil.VACIO : invArmaTemp.getSerie()) + " con modelo "
                                    + (invArmaTemp.getModeloId().getTipoArmaId().getNombre() + " " + invArmaTemp.getModeloId().getMarcaId().getNombre() + " " + invArmaTemp.getModeloId().getModelo()) + ".");
                        }
                    }
                } else if (!JsfUtil.isNullOrEmpty(actaDeposito.getAmaInventarioAccesoriosList())
                        || (!JsfUtil.isNullOrEmpty(actaDeposito.getAmaGuiaMunicionesList()))) {
                    res = Boolean.TRUE;
                }
//                } else {
//                    //Mostrar mensaje de confirmación
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     *
     * @param tipoArtLibre
     * @return
     */
    public List<AmaCatalogo> listTipoArticulos(String tipoArtLibre) {
        List<AmaCatalogo> listResp = ejbAmaCatalogoFacade.listarTiposArticulo();
        if (listResp != null && tipoArtLibre != null && !StringUtil.VACIO.equals(tipoArtLibre)) {
            List<AmaCatalogo> listTemp = new ArrayList(listResp);
            for (AmaCatalogo tipo : listTemp) {
                if ("TP_ART_REPU".equals(tipo.getCodProg())
                        || "TP_ART_INSU".equals(tipo.getCodProg())
                        || "TP_ART_CONE".equals(tipo.getCodProg())) {
                    listResp.remove(tipo);
                }
            }
            switch (tipoArtLibre) {
                case "arma":
                    for (AmaCatalogo tipo : listTemp) {
                        if (!Objects.equals(tipo.getCodProg(), "TP_ART_ARMA")) {
                            listResp.remove(tipo);
                        }
                    }
                    break;
                case "accesMuni":
                    for (AmaCatalogo tipo : listTemp) {
                        if (!Objects.equals(tipo.getCodProg(), "TP_ART_MUNI") && !Objects.equals(tipo.getCodProg(), "TP_ART_ACCE")) {
                            listResp.remove(tipo);
                        }
                    }
                    break;
                case "otros":
                    for (AmaCatalogo tipo : listTemp) {
                        if (Objects.equals(tipo.getCodProg(), "TP_ART_ARMA")) {
                            listResp.remove(tipo);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return listResp;
    }

    /**
     * FUNCION QUE OBTIENE LAS GUIAS EN LAS QUE ESTA INCLUIDA UN ARMA POR SU ID.
     *
     * @param invArma
     * @return lista de guias
     */
    public List<AmaGuiaTransito> obtenerGuiasDelArma(AmaInventarioArma invArma) {
        if (invArma != null) {
            return ejbAmaGuiaTransitoFacade.obtenerGuiasPorArma(invArma.getModeloId().getId(), invArma.getSerie());
        }
        return null;
    }

    /**
     * FUNCION QUE OBTIENE EL ALMACEN DE SUCAMEC REGISTRADA EN GUIA O ACTA DE
     * DEPOSITO O DEVOLUCION. SEGUN TIPOS DE ORIGEN Y DESTINO
     *
     * @author Gino Chávez
     * @param amaGuia
     * @param tipo
     * @return
     */
    public String obtenerAlmacenMovimiento(AmaGuiaTransito amaGuia, String tipo) {
        String res = StringUtil.VACIO;
        if ("orig".equals(tipo)) {
            if (amaGuia.getDireccionOrigenId() != null) {
                if (Objects.equals(amaGuia.getDireccionOrigenId().getPersonaId().getId(), L_UNO)) {
                    res = amaGuia.getDireccionOrigenId().getReferencia() + " - SUCAMEC";
                } else {
                    res = descripcionDireccion(amaGuia.getDireccionOrigenId(), "/");
                }
            } else if (amaGuia.getOrigenAlmacenAduana() != null) {
                res = amaGuia.getOrigenAlmacenAduana().getDireccion() + " " + amaGuia.getOrigenAlmacenAduana().getRazonSocial();
            } else if (amaGuia.getOrigenPuerto() != null) {
                res = amaGuia.getOrigenPuerto().getDireccion() + " " + amaGuia.getOrigenPuerto().getNombre();
            }
        } else if ("dest".equals(tipo)) {
            if (amaGuia.getDireccionDestinoId() != null) {
                if (Objects.equals(amaGuia.getDireccionDestinoId().getPersonaId().getId(), L_UNO)) {
                    res = amaGuia.getDireccionDestinoId().getReferencia() + " - SUCAMEC";
                } else {
                    res = descripcionDireccion(amaGuia.getDireccionDestinoId(), "/");
                }
            } else if (amaGuia.getDestinoAlmacenAduana() != null) {
                res = amaGuia.getDestinoAlmacenAduana().getDireccion() + " " + amaGuia.getDestinoAlmacenAduana().getRazonSocial();
            } else if (amaGuia.getDestinoPuerto() != null) {
                res = amaGuia.getDestinoPuerto().getDireccion() + " " + amaGuia.getDestinoPuerto().getNombre();
            }
        }
        return res;
    }

    /**
     * Función para obtener la descripción completa de dirección.
     *
     * @author Gino Chávez
     * @param dir
     * @param separador
     * @return
     */
    public String descripcionDireccion(SbDireccion dir, String separador) {
        String res = StringUtil.VACIO;
        if (dir != null) {
            res = (dir.getViaId().getCodProg().equals("TP_VIA_OTRO")
                    ? StringUtil.VACIO : dir.getViaId().getAbreviatura())
                    + StringUtil.ESPACIO + dir.getDireccion()
                    + StringUtil.ESPACIO + (dir.getNumero() == null ? StringUtil.VACIO : dir.getNumero())
                    + StringUtil.ESPACIO + "- " + (dir.getDistritoId() != null ? (dir.getDistritoId().getNombre() + separador + dir.getDistritoId().getProvinciaId().getNombre() + separador + dir.getDistritoId().getProvinciaId().getDepartamentoId().getNombre()) : "DISTRITO NO REGISTRADO");
        }
        return res;
    }

    /**
     *
     * @param dis
     * @param separador
     * @return
     */
    public String descripcionUbigeo(SbDistrito dis, String separador) {
        String res = StringUtil.VACIO;
        if (dis != null) {
            res = dis.getNombre() + separador + dis.getProvinciaId().getNombre() + separador + dis.getProvinciaId().getDepartamentoId().getNombre();
        }
        return res;
    }

    /**
     *
     * @param amaGuia
     * @return
     */
    public List<AmaInventarioAccesorios> obtenerListAccesActaActivos(AmaGuiaTransito amaGuia) {
        List<AmaInventarioAccesorios> listAccesActa = null;
        if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioAccesoriosList())) {
            listAccesActa = new ArrayList<>();
            for (AmaInventarioAccesorios accesTemp : amaGuia.getAmaInventarioAccesoriosList()) {
                if (accesTemp.getActivo() == JsfUtil.TRUE) {
                    listAccesActa.add(accesTemp);
                }
            }
        }
        return listAccesActa;
    }

    public TipoGamac tipoGamac(String codProg) {
        return ejbTipoGamacFacade.buscarTipoGamacXCodProg(codProg);
    }

    /**
     * FUNCION QUE REVIERTE LA CONDICION DE ALMACEN Y LA SITUACION DEL ARMA,
     * ADEMAS DE LOS ACCESORIOS VINCULADOS A DICHAS ARMAS O A LA GUIA (ACTA DE
     * DEPOSITO O DEVOLUCION).
     *
     * @param amaGuia
     * @param situacionId
     */
    public void revertirCondicionSituacionArma(AmaGuiaTransito amaGuia, TipoGamac situacionId) {
        short condicion = obtenerCondicionSegunSituacionId(situacionId);
        AmaArmaInventarioDif amaInvDif = null;
        if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaArmaList())) {
            for (AmaArma amaArma : amaGuia.getAmaArmaList()) {
                amaArma.setSituacionId(situacionId);
                ejbAmaArmaFacade.edit(amaArma);
                amaInvDif = ejbAmaArmaInventarioDifFacade.obtenerPorIdArma(amaArma.getId());
                if (amaInvDif != null && amaInvDif.getId() != null) {
                    amaInvDif.setSituacionId(situacionId);
                    amaInvDif.setActivo(JsfUtil.FALSE);
                    ejbAmaArmaInventarioDifFacade.edit(amaInvDif);
                }
                amaInvDif = new AmaArmaInventarioDif();
                amaInvDif.setActivo(JsfUtil.TRUE);
                amaInvDif.setFecha(new Date());
                amaInvDif.setArmaId(amaArma);
                amaInvDif.setInventarioArmaId(null);
                amaInvDif.setSituacionId(situacionId);
                ejbAmaArmaInventarioDifFacade.create(amaInvDif);
            }
        }
        if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioArmaList())) {
            for (AmaInventarioArma amaInvArma : amaGuia.getAmaInventarioArmaList()) {
                if (amaInvArma.getActivo() == JsfUtil.TRUE && amaInvArma.getActual() == JsfUtil.TRUE) {
                    //Actualización de la situación del arma en la tabla ama_arma
//                    AmaArma arma = ejbAmaArmaFacade.obtenerArmaPorModeloSerie(amaInvArma.getModeloId().getId(), amaInvArma.getSerie(), amaInvArma.getNroRua());
                    if (amaInvArma.getArmaId() != null) {
                        amaInvArma.getArmaId().setSituacionId(situacionId);
                        ejbAmaArmaFacade.edit(amaInvArma.getArmaId());
                    }
                    //Fin
                    amaInvArma.setSituacionId(situacionId);
                    amaInvArma.setCondicionAlmacen(condicion);
                    if (amaInvArma.getAmaInventarioAccesoriosList() != null) {
                        for (AmaInventarioAccesorios acces : amaInvArma.getAmaInventarioAccesoriosList()) {
                            if (acces.getActivo() == JsfUtil.TRUE) {
                                acces.setEstadoDeposito(condicion);
                                ejbAmaInventarioAccesoriosFacade.edit(acces);
                            }
                        }
                    }
                    ejbAmaInventarioArmaFacade.edit(amaInvArma);
                    amaInvDif = ejbAmaArmaInventarioDifFacade.obtenerPorIdInventarioArma(amaInvArma.getId());
                    if (amaInvDif != null && amaInvDif.getId() != null) {
                        amaInvDif.setSituacionId(situacionId);
                        amaInvDif.setActivo(JsfUtil.FALSE);
                        ejbAmaArmaInventarioDifFacade.edit(amaInvDif);
                    }
                    amaInvDif = new AmaArmaInventarioDif();
                    amaInvDif.setActivo(JsfUtil.TRUE);
                    amaInvDif.setFecha(new Date());
                    amaInvDif.setArmaId(amaInvArma.getArmaId());
                    amaInvDif.setInventarioArmaId(amaInvArma);
                    amaInvDif.setSituacionId(situacionId);
                    ejbAmaArmaInventarioDifFacade.create(amaInvDif);
                }
            }
        }

        if (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioAccesoriosList())) {
            for (AmaInventarioAccesorios acces : amaGuia.getAmaInventarioAccesoriosList()) {
                if (acces.getActivo() == JsfUtil.TRUE) {
                    acces.setEstadoDeposito(condicion);
                    ejbAmaInventarioAccesoriosFacade.edit(acces);
                }
            }
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void guardarInvArma(AmaInventarioArma copiaArma, Boolean guardar, TipoGamac tipoGuia) {
        try {
            String fileName;
            Boolean crearNuevo = Boolean.FALSE;
            if (copiaArma != null) {
                if (guardar) {
                    crearNuevo = Boolean.TRUE;
                } else if (copiaArma.getId() < 0L) {
                    crearNuevo = Boolean.TRUE;
                }
                ///// Guardado de fotos /////
                if (!JsfUtil.isNullOrEmpty(amaDepositoArmasController.getLstFotosTemp())) {
                    String path = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_foto_inventario").getValor();
                    for (Map fotoTemp : amaDepositoArmasController.getLstFotosTemp()) {
                        fileName = StringUtil.VACIO + fotoTemp.get("nombre");
                        fileName = fileName.toUpperCase();
                        if (Objects.equals((String) fotoTemp.get("nombre"), copiaArma.getFoto1())) {
                            if (JsfUtil.obtenerBytesFromFile((path), copiaArma.getFoto1()) == null) {
                                copiaArma.setFoto1((Objects.equals((short) fotoTemp.get("isAdjunto"), JsfUtil.TRUE) ? "ADJ_" : "GAMAC_")
                                        + ejbSbNumeracionFacade.selectNumeroCodProg("TP_NUM_FINV") + fileName.substring(fileName.lastIndexOf('.'), fileName.length()).toUpperCase());
                                FileUtils.writeByteArrayToFile(new File((path) + copiaArma.getFoto1()), (byte[]) fotoTemp.get("byte"));
                            }
                        } else if (Objects.equals((String) fotoTemp.get("nombre"), copiaArma.getFoto2())) {
                            if (JsfUtil.obtenerBytesFromFile((path), copiaArma.getFoto2()) == null) {
                                copiaArma.setFoto2((Objects.equals((short) fotoTemp.get("isAdjunto"), JsfUtil.TRUE) ? "ADJ_" : "GAMAC_")
                                        + ejbSbNumeracionFacade.selectNumeroCodProg("TP_NUM_FINV") + fileName.substring(fileName.lastIndexOf('.'), fileName.length()).toUpperCase());
                                FileUtils.writeByteArrayToFile(new File((path) + copiaArma.getFoto2()), (byte[]) fotoTemp.get("byte"));
                            }
                        } else if (Objects.equals((String) fotoTemp.get("nombre"), copiaArma.getFoto3())) {
                            if (JsfUtil.obtenerBytesFromFile((path), copiaArma.getFoto3()) == null) {
                                copiaArma.setFoto3((Objects.equals((short) fotoTemp.get("isAdjunto"), JsfUtil.TRUE) ? "ADJ_" : "GAMAC_")
                                        + ejbSbNumeracionFacade.selectNumeroCodProg("TP_NUM_FINV") + fileName.substring(fileName.lastIndexOf('.'), fileName.length()).toUpperCase());
                                FileUtils.writeByteArrayToFile(new File((path) + copiaArma.getFoto3()), (byte[]) fotoTemp.get("byte"));
                            }
                        }
                    }
                }
                //////////////////////////////
                if (crearNuevo) {
                    List<AmaInventarioAccesorios> lisAccesTemp = new ArrayList<>();
                    //Se setea el id a nulo porque acá llega con el id del registro original. Además se setea el id en null de cada accesorio de la lista.
                    copiaArma.setId(null);
                    copiaArma.setFechaRegistro(new Date());
                    copiaArma.setCodigo(copiaArma.getCodigo() == null ? L_CERO : copiaArma.getCodigo());
                    if (copiaArma.getAmaInventarioAccesoriosList() != null) {
                        lisAccesTemp.addAll(copiaArma.getAmaInventarioAccesoriosList());
                    }
                    if (!lisAccesTemp.isEmpty()) {
                        //Se añade solamente los accesorios que se encuentran en activo = 1
                        for (AmaInventarioAccesorios acces : copiaArma.getAmaInventarioAccesoriosList()) {
                            if (acces.getActivo() == JsfUtil.FALSE) {
                                lisAccesTemp.remove(acces);
                            }
                        }
                        JsfUtil.borrarIds(lisAccesTemp);
                    }
                    copiaArma.setAmaInventarioAccesoriosList(new ArrayList(lisAccesTemp));

                    //Ultimos cambios 16-03-2018
                    if (copiaArma.getTipoInternamientoId() == null) {
                        if (amaDepositoArmasController.getTipoInterSelect() != null) {
                            copiaArma.setTipoInternamientoId(amaDepositoArmasController.getTipoInterSelect());
                        } else if (tipoGuia != null) {
                            switch (tipoGuia.getCodProg()) {
                                case "TP_GTGAMAC_IMP":
                                    copiaArma.setTipoInternamientoId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_INTER_TEM"));
                                    break;
                                default:
                                    copiaArma.setTipoInternamientoId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_INTER_TEM"));
                                    break;

                            }
                        } else {
                            copiaArma.setTipoInternamientoId(ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_INTER_TEM"));
                        }
                    }
                    setearArmasActualFalse(copiaArma);
                    copiaArma.setActual(JsfUtil.TRUE);
                    ejbAmaInventarioArmaFacade.create(copiaArma);
                } else {
                    if (copiaArma.getAmaInventarioAccesoriosList() != null) {
                        JsfUtil.borrarIdsN(copiaArma.getAmaInventarioAccesoriosList());
                    }
                    setearArmasActualFalse(copiaArma);
                    ejbAmaInventarioArmaFacade.edit(copiaArma);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Función que actualiza el campo actual a 0, ésta función es invocada al
     * editar o crear un registro de arma en la tabla amaInventarioArma.
     *
     * @param amaInvArma
     */
    public void setearArmasActualFalse(AmaInventarioArma amaInvArma) {
        if (amaInvArma != null) {
            if (!StringUtil.isNullOrEmpty(amaInvArma.getSerie())) {
                List<AmaInventarioArma> listInvArmas = ejbAmaInventarioArmaFacade.obtenerArmasSinActual(amaInvArma.getModeloId().getId(), amaInvArma.getSerie());
                if (!JsfUtil.isNullOrEmpty(listInvArmas)) {
                    for (AmaInventarioArma ia : listInvArmas) {
                        if (!Objects.equals(ia.getId(), amaInvArma.getId())) {
                            if (ia.getActual() == JsfUtil.TRUE) {
                                ia.setActual(JsfUtil.FALSE);
                                ejbAmaInventarioArmaFacade.edit(ia);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Función que actualiza el campo actual a 1 de la tabla amaInventarioArma.
     * Es invocado cuando se anula un arma con el campo actual en 1, entonces se
     * debe establecer al registro anterior como un registro actual del arma.
     *
     * @param amaInvArma
     */
    public void setearArmasActualTrue(AmaInventarioArma amaInvArma) {
        if (amaInvArma != null) {
            if (!StringUtil.isNullOrEmpty(amaInvArma.getSerie())) {
                List<AmaInventarioArma> listInvArmas = ejbAmaInventarioArmaFacade.obtenerArmasSinActual(amaInvArma.getModeloId().getId(), amaInvArma.getSerie());
                removeArmasEntityDuplicadas(listInvArmas);
                if (!JsfUtil.isNullOrEmpty(listInvArmas)) {
                    for (AmaInventarioArma ia : listInvArmas) {
                        if (!Objects.equals(ia.getId(), amaInvArma.getId())) {
                            ia.setActual(JsfUtil.TRUE);
                            ejbAmaInventarioArmaFacade.edit(ia);
                            break;
                        }
                    }
                }
            }
        }
    }

    public Boolean requiereCustodiaMuniciones(List<AmaGuiaMuniciones> listMuniciones) {
        if (!JsfUtil.isNullOrEmpty(listMuniciones)) {
            int cantidad = 0;
            for (AmaGuiaMuniciones muni : listMuniciones) {
                cantidad += muni.getCantidad();
            }
            if (cantidad > 5000) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Boolean requiereCustodiaArmasInventario(AmaGuiaTransito amaGuia) {
        Boolean res = Boolean.FALSE;
        int contTipoCaso1 = 0;
        int contTipoCaso2 = 0;
        //&& "TP_GTGAMAC_AGE".equals(amaGuia.getTipoGuiaId().getCodProg())
        if (amaGuia.getTipoGuiaId() != null) {
            if ((!JsfUtil.isNullOrEmpty(amaGuia.getAmaArmaList()))
                    || (!JsfUtil.isNullOrEmpty(amaGuia.getAmaInventarioArmaList()))) {
                List<AmaArma> listaArmas = amaGuia.getAmaArmaList();
                List<AmaInventarioArma> listaInvArmas = amaGuia.getAmaInventarioArmaList();
                if (!JsfUtil.isNullOrEmpty(listaArmas)) {
                    for (AmaArma arma : listaArmas) {
                        if ("TP_ARMCAE".equals(arma.getModeloId().getTipoArmaId().getCodProg())
                                || "TP_ARMCAR".equals(arma.getModeloId().getTipoArmaId().getCodProg())
                                || "TP_ARMESC".equals(arma.getModeloId().getTipoArmaId().getCodProg())) {
                            contTipoCaso1 += 1;
                        } else if ("TP_ARMPIS".equals(arma.getModeloId().getTipoArmaId().getCodProg())
                                || "TP_ARMREV".equals(arma.getModeloId().getTipoArmaId().getCodProg())) {
                            contTipoCaso2 += 1;
                        }
                    }

                }
                if (!JsfUtil.isNullOrEmpty(listaInvArmas)) {
                    for (AmaInventarioArma invArma : listaInvArmas) {
                        if ("TP_ARMCAE".equals(invArma.getModeloId().getTipoArmaId().getCodProg())
                                || "TP_ARMCAR".equals(invArma.getModeloId().getTipoArmaId().getCodProg())
                                || "TP_ARMESC".equals(invArma.getModeloId().getTipoArmaId().getCodProg())) {
                            contTipoCaso1 += 1;
                        } else if ("TP_ARMPIS".equals(invArma.getModeloId().getTipoArmaId().getCodProg())
                                || "TP_ARMREV".equals(invArma.getModeloId().getTipoArmaId().getCodProg())) {
                            contTipoCaso2 += 1;
                        }
                    }
                }
            }
        }
        if (contTipoCaso1 > 5 || contTipoCaso2 > 10) {
            res = Boolean.TRUE;
        }
        return res;
    }

    /**
     *
     * FUNCIÓN QUE VERIFICA QUE LAS ARMAS ENCONTRADAS SE ENCUENTREN EN LA TABLA
     * AMA_ARMA Y AMA_INVENTARIO_ARMA
     *
     * @param lstInvArmaInOut
     * @param lstArma
     */
    public void verificarArmasNoRegistradas(List<Map> lstInvArmaInOut, List<Map> lstArma) {
        if ((!JsfUtil.isNullOrEmpty(lstArma))
                && (!JsfUtil.isNullOrEmpty(lstInvArmaInOut))) {
//            removeArmasMapDuplicadas(lstInvArmaInOut);
            List<Map> listTemp = new ArrayList(lstInvArmaInOut);
            // Bloque que recorre lista de armas y de inventario armas para verificar la existencia de armas en ambas tablas.
            for (Map amaArma : lstArma) {
                for (Map amaInvArma : listTemp) {
                    if (!(Objects.equals(amaArma.get("modeloId"), amaInvArma.get("modeloId"))
                            && Objects.equals(amaArma.get("serie"), amaInvArma.get("serie")))) {
                        lstInvArmaInOut.remove(amaInvArma);
                        break;
                    }
                }
            }
            // Fin del bloque
        } else {
            if (lstInvArmaInOut == null) {
                lstInvArmaInOut = new ArrayList<>();
            }
            lstInvArmaInOut.clear();
        }
    }

    /**
     * FUNCIÓN QUE REMUEVE ARMAS DUPLICADAS CONSERVANDO ÚNICAMENTE EL ÚLTIMO
     * ARMA REGISTRADA O ACTUALIZADA.
     *
     * @param listInOut
     * @author Gino Chávez
     */
//    public void removeArmasMapDuplicadas(List<Map> listInOut) {
//        if (!JsfUtil.isNullOrEmpty(listInOut)) {
//            List<Map> listArmaFinal = null;
//            List<Map> listArmaDup = null;
//            List<Map> listArmaDupTemp = null;
//            List<Map> lstInvArmaTemp1 = new ArrayList<>();
//            lstInvArmaTemp1.addAll(listInOut);
//            listArmaFinal = new ArrayList<>();
//            for (Map amaInv : lstInvArmaTemp1) {
//                listArmaDup = new ArrayList<>();
//                List<Map> lstInvArmaTemp2 = new ArrayList<>();
//                lstInvArmaTemp2.addAll(listInOut);
//                for (Map amaInv2 : lstInvArmaTemp2) {
//                    if (Objects.equals(amaInv.get("modeloId"), amaInv2.get("modeloId"))
//                            && Objects.equals(amaInv.get("serie"), amaInv2.get("serie"))) {
//                        listArmaDup.add(amaInv2);
//                        listInOut.remove(amaInv2);
//                    }
//                }
//                if (!listArmaDup.isEmpty()) {
//                    if (listArmaDup.size() > 1) {
//                        listArmaDupTemp = new ArrayList<>();
//                        listArmaDupTemp.addAll(listArmaDup);
//                        Long idTemp = L_UNO_NEG;
//                        //Buscar el id Mayor en la lista de duplicados.
//                        for (Map map : listArmaDupTemp) {
//                            if ((Long) map.get("id") > idTemp) {
//                                idTemp = (Long) map.get("id");
//                            }
//                        }
//                        //Remover los items con id menores
//                        for (Map itemMap : listArmaDupTemp) {
//                            if (!Objects.equals((Long) itemMap.get("id"), idTemp)) {
//                                listArmaDup.remove(itemMap);
//                            } else {
//                                //Añadir, a la lista final, el registro con el id mayor.
//                                listArmaFinal.add(itemMap);
//                            }
//                        }
//                    } else {
//                        //Añadir a la lista final el registro.
//                        for (Map itemMap : listArmaDup) {
//                            listArmaFinal.add(itemMap);
//                        }
//                    }
//                }
//            }
//            //Añadir la lista Final a la lista original. En este punto la lista original llega vacía.
//            if (!listArmaFinal.isEmpty()) {
//                listInOut.addAll(listArmaFinal);
//            }
//        }
//    }
//
//    /**
//     * FUNCIÓN QUE REMUEVE ARMAS DUPLICADAS CONSERVANDO ÚNICAMENTE EL ÚLTIMO
//     * ARMA REGISTRADA O ACTUALIZADA.
//     *
//     * @param listInOut
//     * @author Gino Chávez
//     */
//    public void removeArmaStringDuplicadas(List<String> listInOut) {
//        if (!JsfUtil.isNullOrEmpty(listInOut)) {
//            List<String> listArmaFinal = null;
//            List<String> listArmaDup = null;
//            List<String> listArmaDupTemp = null;
//            List<String> lstInvArmaTemp1 = new ArrayList<>();
//            lstInvArmaTemp1.addAll(listInOut);
//            listArmaFinal = new ArrayList<>();
//            for (String amaInv : lstInvArmaTemp1) {
//                String[] arreglo1 = amaInv.split(StringUtil.PUNTOCOMA);
//                listArmaDup = new ArrayList<>();
//                List<String> lstInvArmaTemp2 = new ArrayList<>();
//                lstInvArmaTemp2.addAll(listInOut);
//                for (String amaInv2 : lstInvArmaTemp2) {
//                    String[] arreglo2 = amaInv2.split(StringUtil.PUNTOCOMA);
//                    if (Objects.equals(arreglo1[4], arreglo2[4])
//                            && Objects.equals(arreglo1[6], arreglo2[6])) {
//                        listArmaDup.add(amaInv2);
//                        listInOut.remove(amaInv2);
//                    }
//                }
//                if (!listArmaDup.isEmpty()) {
//                    if (listArmaDup.size() > 1) {
//                        listArmaDupTemp = new ArrayList<>();
//                        listArmaDupTemp.addAll(listArmaDup);
//                        //Remover las armas que se encuentren en AmaArma
//                        for (String itemString : listArmaDupTemp) {
//                            String[] arreglo4 = itemString.split(StringUtil.PUNTOCOMA);
//                            if (arreglo4[7] == null || Objects.equals(arreglo4[7], StringUtil.VACIO) || Objects.equals(arreglo4[7], "null")) {
//                                listArmaDup.remove(itemString);
//                            } else {
//                                //Añadir, a la lista final, el registro con el id mayor.
//                                listArmaFinal.add(itemString);
//                            }
//                        }
//                    } else {
//                        //Añadir a la lista final el registro.
//                        for (String itemString : listArmaDup) {
//                            listArmaFinal.add(itemString);
//                        }
//                    }
//                }
//            }
//            //Añadir la lista Final a la lista original. En este punto la lista original llega vacía.
//            if (!listArmaFinal.isEmpty()) {
//                listInOut.addAll(listArmaFinal);
//            }
//        }
//    }
//
//    /**
//     * FUNCIÓN QUE REMUEVE ARMAS DUPLICADAS CONSERVANDO ÚNICAMENTE EL ÚLTIMO
//     * ARMA REGISTRADA O ACTUALIZADA.
//     *
//     * @param listInOut
//     * @author Gino Chávez
//     */
    public void removeArmasEntityDuplicadas(List<AmaInventarioArma> listInOut) {
        if (!JsfUtil.isNullOrEmpty(listInOut)) {
            List<AmaInventarioArma> listArmaFinal = null;
            List<AmaInventarioArma> listArmaDup = null;
            List<AmaInventarioArma> listArmaDupTemp = null;
            List<AmaInventarioArma> lstInvArmaTemp1 = new ArrayList<>();
            lstInvArmaTemp1.addAll(listInOut);
            listArmaFinal = new ArrayList<>();
            for (AmaInventarioArma amaInv : lstInvArmaTemp1) {
//                if (amaInv.getActual() == JsfUtil.TRUE) {
                listArmaDup = new ArrayList<>();
                List<AmaInventarioArma> lstInvArmaTemp2 = new ArrayList<>();
                lstInvArmaTemp2.addAll(listInOut);
                for (AmaInventarioArma amaInv2 : lstInvArmaTemp2) {
                    if (Objects.equals(amaInv.getModeloId(), amaInv2.getModeloId())
                            && Objects.equals(amaInv.getSerie(), amaInv2.getSerie())) {
                        listArmaDup.add(amaInv2);
                        listInOut.remove(amaInv2);
                    }
                }
                if (!listArmaDup.isEmpty()) {
                    if (listArmaDup.size() > 1) {
                        listArmaDupTemp = new ArrayList<>();
                        listArmaDupTemp.addAll(listArmaDup);
                        Long idTemp = L_UNO_NEG;
                        //Buscar el id Mayor en la lista de duplicados.
                        for (AmaInventarioArma invArmaDup : listArmaDupTemp) {
                            if (invArmaDup.getId() > idTemp) {
                                idTemp = invArmaDup.getId();
                            }
                        }
                        //Remover los items con id menores
                        for (AmaInventarioArma amaInvDup : listArmaDupTemp) {
                            if (!Objects.equals(amaInvDup.getId(), idTemp)) {
                                listArmaDup.remove(amaInvDup);
                            } else {
                                //Añadir, a la lista final, el registro con el id mayor.
                                listArmaFinal.add(amaInvDup);
                            }
                        }
                    } else {
                        //Añadir a la lista final el registro.
                        for (AmaInventarioArma amaInvDup : listArmaDup) {
                            listArmaFinal.add(amaInvDup);
                        }
                    }
                }
//                }
            }
            //Añadir la lista Final a la lista original. En este punto la lista original llega vacía.
            if (!listArmaFinal.isEmpty()) {
                listInOut.addAll(listArmaFinal);
            }
        }
    }
//
//    /**
//     * FUNCIÓN QUE REMUEVE ARMAS DUPLICADAS CONSERVANDO UNO POR CADA MODELO Y
//     * SERIE
//     *
//     * @param listInOut
//     * @author Gino Chávez
//     */
//    public void removeArmasEntityDuplicadasSave(List<AmaInventarioArma> listInOut) {
//        if (!JsfUtil.isNullOrEmpty(listInOut)) {
//            List<AmaInventarioArma> lstInvArmaTemp1 = new ArrayList<>();
//            lstInvArmaTemp1.addAll(listInOut);
//            Boolean remover = null;
//            int i = 0;
//            for (AmaInventarioArma amaInv : lstInvArmaTemp1) {
//                List<AmaInventarioArma> lstInvArmaTemp2 = new ArrayList<>();
//                lstInvArmaTemp2.addAll(listInOut);
//                remover = Boolean.FALSE;
//                i = 0;
//                for (AmaInventarioArma amaInv2 : lstInvArmaTemp2) {
//                    if (Objects.equals(amaInv.getModeloId(), amaInv2.getModeloId())
//                            && Objects.equals(amaInv.getSerie(), amaInv2.getSerie())) {
//                        i++;
//                        if (i == 2) {//Remueve si se encuentran dos armas con el mismo modelo y serie.
//                            remover = Boolean.TRUE;
//                            break;
//                        }
//                    }
//                }
//                if (remover) {
//                    listInOut.remove(amaInv);
//                }
//            }
//        }
//    }
//
//    /**
//     * FUNCIÓN QUE REMUEVE ARMAS DUPLICADAS CONSERVANDO ÚNICAMENTE EL ÚLTIMO
//     * ARMA REGISTRADA O ACTUALIZADA.
//     *
//     * @param listInOut
//     * @author Gino Chávez
//     */
//    public void removeLicMapDuplicadas(List<Map> listInOut) {
//        if (!JsfUtil.isNullOrEmpty(listInOut)) {
//            List<Map> listArmaFinal = null;
//            List<Map> listArmaDup = null;
//            List<Map> listArmaDupTemp = null;
//            List<Map> lstInvArmaTemp1 = new ArrayList<>();
//            lstInvArmaTemp1.addAll(listInOut);
//            listArmaFinal = new ArrayList<>();
//            for (Map amaInv : lstInvArmaTemp1) {
//                listArmaDup = new ArrayList<>();
//                List<Map> lstInvArmaTemp2 = new ArrayList<>();
//                lstInvArmaTemp2.addAll(listInOut);
//                for (Map amaInv2 : lstInvArmaTemp2) {
//                    if (Objects.equals(amaInv.get("NRO_LIC"), amaInv2.get("NRO_LIC"))
//                            && Objects.equals(amaInv.get("NRO_SERIE"), amaInv2.get("NRO_SERIE"))
//                            && Objects.equals(amaInv.get("TIPO_ARMA"), amaInv2.get("TIPO_ARMA"))
//                            && Objects.equals(amaInv.get("MARCA"), amaInv2.get("MARCA"))
//                            && Objects.equals(amaInv.get("MODELO"), amaInv2.get("MODELO"))
//                            && Objects.equals(amaInv.get("CALIBRE"), amaInv2.get("CALIBRE"))
//                            && Objects.equals(amaInv.get("DOC_PROPIETARIO"), amaInv2.get("DOC_PROPIETARIO"))) {
//                        listArmaDup.add(amaInv2);
//                        listInOut.remove(amaInv2);
//                    }
//                }
//                if (!listArmaDup.isEmpty()) {
//                    if (listArmaDup.size() > 1) {
//                        listArmaDupTemp = new ArrayList<>();
//                        listArmaDupTemp.addAll(listArmaDup);
//                        Long idTemp = L_UNO_NEG;
//                        //Buscar el id Mayor en la lista de duplicados.
//                        for (Map map : listArmaDupTemp) {
//                            if (Long.valueOf((String) map.get("DOC_PORTADOR")) > idTemp) {
//                                idTemp = Long.valueOf((String) map.get("DOC_PORTADOR"));
//                            }
//                        }
//                        //Remover los items con id menores
//                        for (Map itemMap : listArmaDupTemp) {
//                            if (!Objects.equals(Long.valueOf((String) itemMap.get("DOC_PORTADOR")), idTemp)) {
//                                listArmaDup.remove(itemMap);
//                            } else {
//                                //Añadir, a la lista final, el registro con el id mayor.
//                                listArmaFinal.add(itemMap);
//                            }
//                        }
//                    } else {
//                        //Añadir a la lista final el registro.
//                        for (Map itemMap : listArmaDup) {
//                            listArmaFinal.add(itemMap);
//                        }
//                    }
//                }
//            }
//            //Añadir la lista Final a la lista original. En este punto la lista original llega vacía.
//            if (!listArmaFinal.isEmpty()) {
//                listInOut.addAll(listArmaFinal);
//            }
//        }
//    }

    /**
     * Función que obtiene el valor del campo condición almacén, de la tabla
     * AmaInventarioArma, que debe tener al momento de la búsqueda, ésto según
     * tipo de Guía o tipo de Depósito. 1=Arma en Almacén SUCAMEC, 0=Arma fuera
     * de Almacén SUCAMEC.
     *
     * @author Gino Chávez
     * @param tipoGuia
     * @param tipoOrigen
     * @return
     */
    public short condicionAlmacen(TipoGamac tipoGuia, TipoGamac tipoOrigen) {
        short res = -1;
        if (tipoGuia != null) {
            switch (tipoGuia.getCodProg()) {
                //Bloque para Guías de Tránsito
                case "TP_GTGAMAC_REC":
                case "TP_GTGAMAC_EXH":
                case "TP_GTGAMAC_DON":
                case "TP_GTGAMAC_SUC":
                case "TP_GTGAMAC_DDV":
                case "TP_GTGAMAC_CFA":
                    res = JsfUtil.TRUE;
                    break;
                case "TP_GTGAMAC_IMP":
                case "TP_GTGAMAC_REI":
                case "TP_GTGAMAC_SAL":
                    res = JsfUtil.FALSE;
                    break;
                case "TP_GTGAMAC_COL":
                case "TP_GTGAMAC_MUN":
                case "TP_GTGAMAC_AGE":
                    break;
                case "TP_GTGAMAC_POL":
                    if (tipoOrigen != null) {
                        switch (tipoOrigen.getCodProg()) {
                            case "TP_GTORDES_ALMS":
                                res = JsfUtil.TRUE;
                                break;
                            case "TP_GTORDES_LOC":
                                res = JsfUtil.FALSE;
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                //Fin del bloque para Guías de tránsito.

                //Bloque para actas de depósito
                case "TP_GTGAMAC_ALM":
                    res = JsfUtil.FALSE;
                    break;
                //Fin de bloque para depósito.
                default:
                    break;
            }
        }
        return res;
    }

    /**
     * Función que obtiene el número de acta de depósito o internamiento de
     * aquellas armas que no cuentan con número de serie.
     *
     * @param amaInvArma
     * @return
     */
    public String obtenerCodActaInternamiento(AmaInventarioArma amaInvArma) {
        String codigoActaDep = StringUtil.GUION;
        if (!JsfUtil.isNullOrEmpty(amaInvArma.getAmaGuiaTransitoList())) {
            orderDescListaGuiasTransito(amaInvArma.getAmaGuiaTransitoList());
            for (AmaGuiaTransito gt : amaInvArma.getAmaGuiaTransitoList()) {
                if (gt.getActivo() == JsfUtil.TRUE
                        && listaIdTipoGuiaBus("DEP").contains(String.valueOf(gt.getTipoGuiaId().getId()))
                        && Objects.equals(gt.getEstadoId().getCodProg(), "TP_GTGAMAC_FIN")) {
                    codigoActaDep = gt.getNroGuia();
                    break;
                }
            }
        }
        return codigoActaDep;
    }

    /**
     * Ordenaiento descendente de guias de transito.
     *
     * @param po
     */
    public void orderDescListaGuiasTransito(List<AmaGuiaTransito> po) {
        Collections.sort(po, Collections.reverseOrder(new Comparator<AmaGuiaTransito>() {
            @Override
            public int compare(AmaGuiaTransito o1, AmaGuiaTransito o2) {
                return o1.getId().compareTo(o2.getId());
            }
        }));
    }
    
    /**
     *  Verifica si un arma en inventario cuenta con tarjeta de propiedad emitida
     * @param arma
     * @return 
     */   
    public boolean tieneTarjetaVigente(AmaInventarioArma arma){
        boolean res = false;
        if ( arma.getArmaId() != null ) {
            for( AmaTarjetaPropiedad tp : arma.getArmaId().getAmaTarjetaPropiedadList() ) {
                if ( tp.getEstadoId().getCodProg().equals("TP_EST_NULIDAD") == false )  {
                    res = true;
                    break; 
                }
            }            
        }
        return res;
    }    
}
