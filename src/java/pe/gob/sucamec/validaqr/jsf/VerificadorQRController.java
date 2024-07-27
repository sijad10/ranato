/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.validaqr.jsf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;
import pe.gob.sucamec.bdintegrado.bean.SbUsuarioFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.TipoGamacFacade;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;


/**
 * Instancia para validar datos de GEPP, se tiene un controller por tipo de
 * validación para evitar problemas de manejo de sesion. Se debe ampliar para
 * todos los tipos.
 */
// Nombre de la instancia en la aplicacion //
@Named("verificadorQRController")
// La instancia se crea al momento de iniciar una session http //
@RequestScoped
public class VerificadorQRController implements Serializable {

    public String getTipoMostrar() {
        return tipoMostrar;
    }

    public void setTipoMostrar(String tipoMostrar) {
        this.tipoMostrar = tipoMostrar;
    }

    enum tipoDoc {
        DATOS, PDF, COLS, IMG, PDFFIRMADO, DATOSSEGURIDAD
    };

    VerificadorVisualizador visualizador = null;

    @EJB
    private pe.gob.sucamec.validaqr.beans.VerificadorFacade validaQrFacade;
    @EJB
    private TipoGamacFacade ejbTipoGamacFacade;
    @EJB
    private SbParametroFacade ejbSbParametroFacade;    
    @EJB
    private SbUsuarioFacadeGt ejbSbUsuarioFacadeGt ;
    

    private ArrayRecord registro;
    private Map registroMap;
    boolean error;
    private Boolean isHis = Boolean.FALSE;
    private String tipoMostrar;
    private String nombreArchivo;
    private String nombreArchivoEnc;

    public ArrayRecord getRegistro() {
        return registro;
    }

    public void setRegistro(ArrayRecord registro) {
        this.registro = registro;
    }

    public Map getRegistroMap() {
        return registroMap;
    }

    public void setRegistroMap(Map registroMap) {
        this.registroMap = registroMap;
    }

    public VerificadorVisualizador getVisualizador() {
        return visualizador;
    }

    public void setVisualizador(VerificadorVisualizador visualizador) {
        this.visualizador = visualizador;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Boolean getIsHis() {
        return isHis;
    }

    public void setIsHis(Boolean isHis) {
        this.isHis = isHis;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreArchivoEnc() {
        return nombreArchivoEnc;
    }

    public void setNombreArchivoEnc(String nombreArchivoEnc) {
        this.nombreArchivoEnc = nombreArchivoEnc;
    }
    
    /**
     * Todas las preticiones se empiezan por acá.
     */
    @PostConstruct
    public void inicio() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            //TODO: modificar esto para jalar el hash de un facade y convertirlo en la imagen, tal como esta en la version original
            ExternalContext ec = context.getExternalContext();
            String hash = (String) ec.getRequestParameterMap().get("h"),
                    path = ec.getRequestPathInfo(), pathPdf = null, pathImg = null, id = null;
            
            //System.out.println("hash->"+hash);
            //System.out.println("path->"+path);
            
            VerificadorVisualizador.Orientacion o = VerificadorVisualizador.Orientacion.VERTICAL;
            tipoDoc tipo = tipoDoc.PDF;
            List<Map> lMap = null;
            error = false;
            String idDinamico = "", textoOpcional = "";
            boolean pathDinamico = false, blnSello = true;
            String idDinamico2 = "";
            boolean pathDinamico2 = false;
            String id2 = null;
            
            Integer tipoReporte = 0;
            if (hash != null && path != null) {
                // Validación de PDFs //
                if (path.endsWith("/resgepp.xhtml")) {
                    pathPdf = JsfUtil.bundleBase("PathGeppResPdf");
                    pathImg = JsfUtil.bundleBase("PathGeppResImg");
                    id = validaQrFacade.buscarHashGepp(hash);
                }
                if (path.endsWith("/resgamac.xhtml")) {
                    pathPdf = JsfUtil.bundleBase("PathGamacResPdf");
                    pathImg = JsfUtil.bundleBase("PathGamacResImg");
                    id = validaQrFacade.buscarHashGamac(hash);
                }
                if (path.endsWith("/resTurPoli.xhtml")) {
                    pathPdf = JsfUtil.bundleBase("PathGamacPolPdf");
                    pathImg = JsfUtil.bundleBase("PathGamacPolImg");
                    id = validaQrFacade.buscarHashPoligono(hash);
                }                
                if (path.endsWith("/raess.xhtml")) {
                    pathPdf = JsfUtil.bundleBase("PathGamacResolucionArmaPdf");
                    pathImg = JsfUtil.bundleBase("PathGamacResolucionArmaImg");
                    id = validaQrFacade.buscarHashRAESS(hash);
                }
                if (path.endsWith("/constanciaLic.xhtml")) {
                    pathPdf = JsfUtil.bundleBase("PathConstanciaLicTarPdf");
                    pathImg = JsfUtil.bundleBase("PathConstanciaLicTarImg");
                    id = validaQrFacade.buscarHashConstLicTar(hash);
                }
                if (path.endsWith("/constanciaVal.xhtml")) {
                    pathPdf = JsfUtil.bundleBase("PathConstanciaValPdf");
                    pathImg = JsfUtil.bundleBase("PathConstanciaValImg");
                    Map constanciaMap = validaQrFacade.buscarConstanciaConstanciaValWeb(hash);
                    String pathF = constanciaMap.get("PATH").toString();
                    pathF = pathF.replace("/", File.separator);                    
                    id = pathF + JsfUtil.bundleBase("PathSeparator") + constanciaMap.get("NOMBRE").toString();
                    pathDinamico = true;
                    idDinamico = constanciaMap.get("NOMBRE").toString();
                }
                if (path.endsWith("/constanciaVer.xhtml") || path.endsWith("/constanciaVeri.xhtml")) {
                    pathPdf = ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_constanciaVeri").getValor();
                    pathImg = JsfUtil.bundleBase("PathConstanciaVerImg");
                    Map constanciaMap = validaQrFacade.buscarConstanciaConstanciaVerWeb(hash);
                    String nom = constanciaMap.get("NOMBRE").toString();
                    nom = nom.replace("__", "_");
                    String pathF = constanciaMap.get("PATH").toString();
                    pathF = pathF.replace("/", JsfUtil.bundleBase("PathSeparator"));
                    id = pathF + JsfUtil.bundleBase("PathSeparator") + nom;
                    pathDinamico = true;
                    idDinamico = nom;
                    
                    String nom2 = constanciaMap.get("NOMBRE_OPCION2").toString();
                    nom2 = nom2.replace("__", "_");
                    id2 = path + JsfUtil.bundleBase("PathSeparator") + nom2;
                    pathDinamico2 = true;
                    idDinamico2 = nom2;
                }
                if (path.endsWith("/examenPoli.xhtml")) {
                    pathPdf = ejbSbParametroFacade.obtenerParametroXNombreIntegrado("Documentos_pathUpload_examenPol").getValor();
                    pathImg = JsfUtil.bundleBase("PathConstanciaExaPoliImg");
                    Map constanciaMap = validaQrFacade.buscarConstanciaVerExamenPoligono(hash);
                    String nom = constanciaMap.get("NOMBRE").toString();
                    nom = nom.replace("__", "_");
                    String pathF = constanciaMap.get("PATH").toString();
                    pathF = pathF.replace("/", JsfUtil.bundleBase("PathSeparator"));
                    id = pathF + JsfUtil.bundleBase("PathSeparator") + nom;
                    pathDinamico = true;
                    idDinamico = nom;
                }
                if (path.endsWith("/gamacGT.xhtml")) {
                    String tipoGuia = validaQrFacade.obtenerTipoGuia(hash);
                    if (listaIdTipoGuiaBus("guia").contains(tipoGuia)) {
                        tipoMostrar = "guia";
                        pathPdf = JsfUtil.bundleBase("PathGamacAmaPdf");
                        pathImg = JsfUtil.bundleBase("PathGamacAmaImg");
                        id = validaQrFacade.buscarHashGamacGuia(hash);
                    } else if (listaIdTipoGuiaBus("acta").contains(tipoGuia)) {
                        tipoMostrar = "acta";
                        tipo = tipoDoc.COLS;
                        lMap = validaQrFacade.buscarHashActaDepDev(hash);
                    }
                }
                if (path.endsWith("/gamacPevam.xhtml")) {
                    tipoMostrar = "actaPevam";
                    tipo = tipoDoc.COLS;
                    lMap = validaQrFacade.buscarHashActaPevam(hash);                    
                }
                if (path.endsWith("/gamacPevamMuni.xhtml")) {
                    tipoMostrar = "actaPevamMuni";
                    tipo = tipoDoc.COLS;
                    lMap = validaQrFacade.buscarHashActaPevamMuni(hash);                    
                }                
                if (path.endsWith("/gamacActaMov.xhtml")) {
                    pathPdf = JsfUtil.bundleBase("PathGamacActaPdf");
                    pathImg = JsfUtil.bundleBase("PathGamacActaImg");
                    id = validaQrFacade.buscarHashGamacGuia(hash);
                    pathDinamico = true;
                    idDinamico = id + "_acta";
                }
                
                //////////////////// GSSP - AUTORIZACION DE SERVICIO DE SEGURIDAD ////////////////////
                if (path.endsWith("/resGsspAuto.xhtml")) {
                    tipo = tipoDoc.DATOSSEGURIDAD;
                    tipoMostrar = "ResolAutoSeguridad";
                    pathPdf = JsfUtil.bundleBase("PathGsspInsPdf");
                    pathImg = JsfUtil.bundleBase("PathGsspInsImg");
                    id = validaQrFacade.buscarHashGsspResolAutoSeg(hash);
                    
                    //System.out.println("pathPdf->"+pathPdf);
                    //System.out.println("id->"+id);
                    
                }
                
                //////////////////// GSSP ////////////////////
                if (path.endsWith("/gssp.xhtml")) {
                    pathPdf = JsfUtil.bundleBase("PathGsspInsPdf");
                    pathImg = JsfUtil.bundleBase("PathGsspInsImg");
                    id = validaQrFacade.buscarHashGssp(hash);
                    o = VerificadorVisualizador.Orientacion.HORIZONTAL;
                    
                }
                if (path.endsWith("/constanciaCCli.xhtml")) {
                    pathPdf = JsfUtil.bundleBase("PathGsspConstanciaCarteraCPdf");
                    pathImg = JsfUtil.bundleBase("PathGsspConstanciaCarteraCImg");
                    blnSello = false;
                    Map nmapCC = validaQrFacade.buscarHashGsspCarteraClientes(hash);
                    id = nmapCC.get("ID").toString();
                    textoOpcional = JsfUtil.bundleBase("SspCarteraClientes_msjeQR") + nmapCC.get("ESTADO").toString();
                }
                if (path.endsWith("/constanciaCursos.xhtml")) {
                    pathPdf = JsfUtil.bundleBase("PathGsspConstanciaCursosPdf");
                    pathImg = JsfUtil.bundleBase("PathGsspConstanciaCursosImg");
                    Map nmapCC = validaQrFacade.buscarHashGsspProgramaciónCursos(hash);
                    if(nmapCC.get("ESTADO").toString().equals("TP_ECC_EVA") || nmapCC.get("ESTADO").toString().equals("TP_ECC_ACT")){
                        textoOpcional = JsfUtil.bundleBase("SspRegistroCursos_pendiente");
                    }else{
                        textoOpcional = JsfUtil.bundleBase("SspRegistroCursos_finalizado");
                    }
                    if(nmapCC.get("TIPO_HASH").toString().equals("1")){ // HASH_QR REGISTRO
                        id = nmapCC.get("ID").toString();
                        blnSello = false;
                    }else{ // HASH_QR CONSTANCIA
                        id = "CONSTANCIA_"+nmapCC.get("ID").toString();
                        blnSello = true;
                    }
                }
                if (path.endsWith("/constanciaAnt.xhtml") || 
                    path.endsWith("/constanciaSanc.xhtml") || 
                    path.endsWith("/reporteCarnesVig.xhtml") || 
                    path.endsWith("/constanciaPerSeg.xhtml") || 
                    path.endsWith("/reporteCarteraCliente.xhtml") || 
                    path.endsWith("/reporteCursosPJ.xhtml") || 
                    path.endsWith("/reporteCursosInst.xhtml") || 
                    path.endsWith("/constanciaAntPNat.xhtml") ||                   
                    path.endsWith("/reporteCrnCesados.xhtml") ||
                    path.endsWith("/reporteCrnVencido.xhtml") 
                    ) {
                    //pathPdf = ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_reportesGssp").getValor().trim();
                    pathPdf = JsfUtil.bundleBase("PathGsspReportesConstanciasPdf");
                    pathImg = JsfUtil.bundleBase("PathGsspReportesConstanciasImg");
                    id = validaQrFacade.buscarHashReportesyConstancias(hash);
                    pathDinamico = true;
                    id = id.substring(0, id.lastIndexOf('.'));
                    tipoReporte = 1;
                }
                /////////////////// NOT QR ///////////////////
                if (path.endsWith("/notqr.xhtml")) {
                    //pathPdf = ejbSbParametroFacade.obtenerParametroXSistemaCodProg("TP_PATH_1NOTIFQR",1L).getValor();
                    pathPdf = ejbSbParametroFacade.obtenerParametroXNombre("PathUpload").getValor();
                    pathImg = "";
                    tipo = tipoDoc.PDFFIRMADO;
                    nombreArchivo = validaQrFacade.buscarHashNotQR(hash);
                    nombreArchivoEnc = hash;//pe.gob.sucamec.sel.jsf.util.JsfUtil.encriptar(nombreArchivo);
                    //descargaDeArchivo(nombreArchivo);
                }
                if (path.endsWith("/notqrpreview.xhtml")) {
                    //pathPdf = ejbSbParametroFacade.obtenerParametroXSistemaCodProg("TP_PATH_1NOTIFQR",1L).getValor();
                    pathPdf = ejbSbParametroFacade.obtenerParametroXNombre("PathUpload").getValor();
                    pathImg = "";
                    //tipo = tipoDoc.PDFFIRMADO;
                    //nombreArchivo = validaQrFacade.buscarHashNotQR(hash);
                    String na = (String) ec.getRequestParameterMap().get("h");
                    nombreArchivoEnc = validaQrFacade.buscarHashNotQR(na);
                    //na = pe.gob.sucamec.sel.jsf.util.JsfUtil.desencriptar(na);
                    //System.out.println(nombreArchivoEnc);
                    descargaDeArchivo(nombreArchivoEnc);
                }
                //////////////////////////////////////////////
                // Validación de documentos en base de datos //
                List<ArrayRecord> l = null;
                registro = null;
                if (path.endsWith("/gamaclic.xhtml")) {
                    tipo = tipoDoc.DATOS;
                    l = validaQrFacade.buscarHashGamacLic(hash);
                }
                if (path.endsWith("/gamactar.xhtml")) {
                    tipo = tipoDoc.DATOS;
                    l = validaQrFacade.buscarHashGamacTar(hash);
                }
                if (path.endsWith("/lic.xhtml")) {
                    tipo = tipoDoc.DATOS;
                    l = validaQrFacade.buscarHashLic(hash);
                }
                if (path.endsWith("/car.xhtml")) {
                    tipo = tipoDoc.DATOS;
                    l = validaQrFacade.buscarHashCarGsspMigrado(hash);
                    if (!(l != null && !l.isEmpty())) {
                        l = validaQrFacade.buscarHashCarGsspHisMigrado(hash);
                        setIsHis(Boolean.TRUE);
                    }
                }
                if (path.endsWith("/carneElectronico.xhtml")) {
                    tipo = tipoDoc.IMG;
                    l = validaQrFacade.buscarHashCarGsspMigrado(hash);
                    pathPdf = JsfUtil.bundleNotificacion("PathUpload");
                    pathImg = JsfUtil.bundleBase("PathGsspReportesCarnesGsspDigitalImg");
                    if (!(l != null && !l.isEmpty())) {
                        l = validaQrFacade.buscarHashCarGsspHisMigrado(hash);
                        setIsHis(Boolean.TRUE);
                    }                    
                }
                if (path.endsWith("/luaspe.xhtml")) {
                    tipo = tipoDoc.DATOS;
                    l = validaQrFacade.buscarHashLuaspeGamac(hash);
                }
                if (path.endsWith("/cmp.xhtml")) {
                    tipo = tipoDoc.DATOS;
                    l = validaQrFacade.buscarHashCMP(hash);
                }
                if (path.endsWith("/lme.xhtml")) {
                    tipo = tipoDoc.DATOS;
                    l = validaQrFacade.buscarHashLME(hash);                    
                }
                if (path.endsWith("/lmed.xhtml")) {
                    tipo = tipoDoc.IMG;
                    l = validaQrFacade.buscarHashLMED(hash);
                    pathPdf = JsfUtil.bundleBase("PathGeppPdfCMED");
                    pathImg = JsfUtil.bundleBase("PathGeppCmedImg");
                    setIsHis(Boolean.FALSE);
                }
                if (path.endsWith("/lmppd.xhtml")) {
                    tipo = tipoDoc.IMG;
                    l = validaQrFacade.buscarHashLMPP(hash);
                    pathPdf = JsfUtil.bundleBase("PathGeppPdfCMPPD");
                    pathImg = JsfUtil.bundleBase("PathGeppCmppdImg");
                    setIsHis(Boolean.FALSE);
                }
                // Tipo PDF
                if (!getIsHis()) {
                    if (tipo == tipoDoc.PDF) {
                        visualizador = new VerificadorVisualizador(pathPdf, pathImg, o);
                        if (id != null) {
                            switch(tipoReporte){
                                case 1: //Reportes GSSP
                                    visualizador.generarArchivosReportesGssp(id);
                                    break;
                                default:
                                    if(blnSello){                                
                                        visualizador.generarArchivos(id, idDinamico, pathDinamico,id2,idDinamico2,pathDinamico2);
                                    }else{
                                        visualizador.generarArchivos_sinSelloAgua(id, idDinamico, pathDinamico);
                                    }
                                    break;
                            }
                            visualizador.setTextOpcional(textoOpcional);
                        }
                        // Tipo Datos
                    } else if (tipo == tipoDoc.DATOSSEGURIDAD && lMap == null) {
                        
                        if(tipoMostrar.equals("ResolAutoSeguridad")){
                            registroMap = validaQrFacade.mostrarDatosResolucionGSSPAutorizaciones(id);
                            
                        }else{
                            registroMap = null;
                        }
                        
                        //System.out.println("registroMap->"+registroMap);

                    }  else if (tipo == tipoDoc.COLS && lMap != null && lMap.size() > 0) {
                        
                        if(tipoMostrar.equals("actaPevam")){
                            registroMap=lMap.get(0);
                            
                        }else if(tipoMostrar.equals("actaPevamMuni")){
                            registroMap =obtenerDatosActaPevamMuni(lMap);
                            
                        }else{
                            registroMap = obtenerDatosActa(lMap);
                        }
                        
                        //System.out.println("registroMap->"+registroMap);

                    } else if (tipo == tipoDoc.IMG && l != null && l.size() == 1) {
                        registro = l.get(0);                        
                        visualizador = new VerificadorVisualizador(pathPdf, pathImg, o);
                        id = l.get(0).get("ID").toString()+"[R]";
                        //id = id+"_front";
                        visualizador.generarArchivosCarneDigital(id);
                        
                    } else if (l != null && l.size() == 1) {
                        registro = l.get(0);
                        
                    } else if (tipo == tipoDoc.PDFFIRMADO) {
                        visualizador = new VerificadorVisualizador(pathPdf, pathImg, o);
                        if (nombreArchivo != null) {
                            visualizador.setVerDocumento(true);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            error = true;
        }
    }

    public Map obtenerDatosActa(List<Map> listIn) {
        Map res = new HashMap();
        for (Map ar : listIn) {
            res.put("TIPO", ar.get("NRO_GUIA").toString().contains("-I") ? "ACTA DE INTERNAMIENTO" : "ACTA DE DEVOLUCIÓN");
            res.put("FLAG", String.valueOf(ar.get("FLAG")));
            res.put("NRO_GUIA", ar.get("NRO_GUIA"));
            res.put("TP_GUIA", ar.get("TP_GUIA"));
            res.put("CTP_GUIA", ar.get("CTP_GUIA"));
            switch (String.valueOf(ar.get("FLAG"))) {
                case "1":
                    ar.put("TIPO", res.get("TIPO"));
                    return ar;
                case "2":
                    return listaDetalle(listIn, res);
                case "3":
                    return listaDetalle(listIn, res);
                default:
                    break;
            }
        }
        return null;
    }
    public Map obtenerDatosActaPevamMuni(List<Map> listIn) {
        Map res = new HashMap();
        for (Map ar : listIn) {            
            res.put("NRO_ACTA", ar.get("NRO_ACTA"));
            res.put("TIPO_ACTA", ar.get("TIPO_ACTA"));
            res.put("TIPO", ar.get("TIPO"));
            res.put("FECHA_INTERNAMIENTO", ar.get("FECHA_INTERNAMIENTO"));
            res.put("TP_DOC_ENT", ar.get("TP_DOC_ENT"));
            res.put("PER_ENT_NUM_DOC", ar.get("PER_ENT_NUM_DOC"));
            res.put("PERSONA_ENTREGA", ar.get("PERSONA_ENTREGA"));
            res.put("ESTADO", ar.get("ESTADO"));
            return listaDetalle(listIn, res);
        }
        return null;
    }

    public Map listaDetalle(List<Map> listIn, Map resInOut) {
        List<Map> listDet = new ArrayList<>();
        for (Map map : listIn) {
            listDet.add(map);
        }
        resInOut.put("LISTA", listDet);
        return resInOut;
    }

    /**
     * FUNCIÓN QUE OBTIENE LA CADENA CON LOS IDS DE LOS TIPOS DE GUÍAS A LISTAR.
     *
     * @param origenBusq, String. Pantalla origen:GUÍAS DE TRÁNSITO, DEPÓSITO O
     * DEVOLUCIÓN.
     * @return Cadena con ids.
     * @author Gino Chávez
     */
    public String listaIdTipoGuiaBus(String origenBusq) {
        String cadena = "";
        List<Long> listRes = new ArrayList<>();
        List<TipoGamac> listaTemp = null;
        if ("acta".equalsIgnoreCase(origenBusq)) {
            String codProg = "";
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
//        } else if (devolucion.equalsIgnoreCase(origenBusq)) {
            listaTemp = ejbTipoGamacFacade.selectTipoGamac("TP_GTGAMAC_DEV");
            for (TipoGamac tipoGamac : listaTemp) {
                listRes.add(tipoGamac.getId());
            }
            listaTemp.clear();
        } else if ("guia".equalsIgnoreCase(origenBusq)) {
            String codProg = "";
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

    public void mostrarMensaje() {        
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        String a = (String) map.get("mensaje");
        String msn;
        if (a.equals("TP_SITU_INT_NUL")) {
            msn = "EL ARMA DE FUEGO DEBE SER INTERNADA POR EXISTIR LA NULIDAD DEL ACTO ADMINISTRATIVO SEGÚN LAS CAUSALES DESCRITAS EN EL CAPÍTULO II DEL TUO DE LA LEY N° 27444.";
        } else {
            msn = "SI EL ARMA DE FUEGO SE ENCUENTRA EN SITUACIÓN DIFERENTE A LO DETALLADO, APERSONARSE A LA SUCAMEC A REALIZAR EL TRÁMITE CORRESPONDIENTE.";
        }
        
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, msn, ""));
    }

    public void mostrarMensajeTransferencia() {        
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        String a = (String) map.get("mensaje");
        String msn;
        if (a.equals("0")) {
            msn = "LA TARJETA DE PROPIEDAD PERDIÓ SUS EFECTOS SEGÚN LO ESTABLECIDO EN EL ARTÍCULO 53.2 DEL REGLAMENTO DE LA LEY N° 30299, LEY DE ARMAS DE FUEGO, MUNICIONES, EXPLOSIVOS,"
                    + " PRODUCTOS PIROTÉCNICOS Y MATERIALES RELACIONADOS DE USO CIVIL.";
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, msn, ""));
        }        
    }
 
    public Map preparaMapCarneGssp(Map carne){
        Map item = new HashMap();
        item.put("ID", carne.get("ID"));
        item.put("CARNE_FOTO", carne.get("NOMBRE_FOTO"));
        item.put("FECHA_INI", carne.get("FEC_EMI"));
        item.put("FECHA_FIN", carne.get("FEC_VENC"));
        item.put("HASH_QR", carne.get("HASH_QR"));
        item.put("NRO_EXPEDIENTE", carne.get("EXPEDIENTE"));
        item.put("NUM_DOC", carne.get("COD_USR"));
        item.put("APE_PAT", carne.get("APE_PAT"));
        item.put("APE_MAT", carne.get("APE_MAT"));
        item.put("NOMBRES", carne.get("NOMBRE_VIG"));
        item.put("RUC", carne.get("RUC"));
        item.put("RZN_SOCIAL", carne.get("EMPRESA"));
        item.put("EMPRESA_NOMBRES", carne.get("EMPRESA_NOMBRE"));
        item.put("MODALIDAD_DESCRIPCION", carne.get("DES_MOD"));
        item.put("NRO_CARNE", carne.get("NRO_CRN_VIG"));        
        
        return item;
    }    
    
    public String getColorCarneGssp(String nombreEstado){
        if(nombreEstado != null && (nombreEstado.equals("VENCIDO") || nombreEstado.equals("CESADO") || nombreEstado.equals("INACTIVO")) ){
            return "color:red;";
        }
        return "";
    }
    
    public void descargaDeArchivo(String n) throws IOException {
        //System.out.println("Descarga "+n);
        String fileName = n;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        try {
            //String rutaPath = ejbSbParametroFacade.obtenerParametroXSistemaCodProg("TP_PATH_1NOTIFQR",1L).getValor();
            String rutaPath = ejbSbParametroFacade.obtenerParametroXNombre("PathUpload").getValor();
            //System.out.println("Descarga:" + rutaPath + fileName);
            InputStream in = new FileInputStream(rutaPath + fileName);
            //InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(pathAppDownload + fileName);
            //file = new DefaultStreamedContent(stream,"application/pdf", fileName);

            response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
            response.setContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "inline; filename=\"" + fileName + "\""); // The Save As popup magic is done here. You can give it any filename you want, this only won't work in MSIE, it will use current request URL as filename instead.

            BufferedInputStream input = null;
            BufferedOutputStream output = null;

            try {
                input = new BufferedInputStream(in);
                output = new BufferedOutputStream(response.getOutputStream());

                byte[] buffer = new byte[10240];
                for (int length; (length = input.read(buffer)) > 0;) {
                    output.write(buffer, 0, length);
                }
            } catch (Exception e) {
            } finally {
                output.close();
                input.close();
            }
            facesContext.responseComplete(); // Important! Else JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.

        } catch (Exception e) {
            JsfUtil.showRedirectHtmlError("No existe el archivo: <font color=\"red\">" + fileName + "</font> en la ruta especificada, debe revisar.");
        }

    }
}