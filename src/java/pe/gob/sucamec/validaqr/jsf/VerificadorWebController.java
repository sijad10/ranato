/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.validaqr.jsf;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;
import pe.gob.sucamec.validaqr.jsf.util.Captcha;
import pe.gob.sucamec.validaqr.jsf.util.JsfUtil;

/**
 *
 * @author Renato
 */
// Nombre de la instancia en la aplicacion //
@Named("verificadorWebController")
// La instancia se crea al momento de iniciar una session http //
@SessionScoped
/**
 * Instancia para validar datos de GEPP, se tiene un controller por tipo de
 * validación para evitar problemas de manejo de sesion.
 */
public class VerificadorWebController implements Serializable {

    private String subQr, numero, anho, tipo;
    private boolean inicio = true;
    private Captcha captcha;
    private String nombreArchivo;
    private String nombreArchivoEnc;
    VerificadorVisualizador visualizador;
    @EJB
    private pe.gob.sucamec.validaqr.beans.VerificadorFacade validaQrFacade;
    @EJB
    private SbParametroFacade ejbSbParametroFacade;    

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isInicio() {
        return inicio;
    }

    public void setInicio(boolean inicio) {
        this.inicio = inicio;
    }

    public String getSubQr() {
        return subQr;
    }

    public void setSubQr(String subQr) {
        this.subQr = subQr;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAnho() {
        return anho;
    }

    public void setAnho(String anho) {
        this.anho = anho;
    }

    public Captcha getCaptcha() {
        return captcha;
    }

    public void setCaptcha(Captcha captcha) {
        this.captcha = captcha;
    }

    public VerificadorVisualizador getVisualizador() {
        return visualizador;
    }

    public void setVisualizador(VerificadorVisualizador visualizador) {
        this.visualizador = visualizador;
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

    public SelectItem[] getListaAnhos() {
        return JsfUtil.listaUltimosAnhos(5);
    }

    private boolean validaParametros() {
        boolean valida = true, validaQr = true;
        if (!captcha.ok()) {
            JsfUtil.mensajeError("Error el captcha es incorrecto");
            JsfUtil.invalidar("validaForm:textoCaptcha");
            valida = false;
        }
        if (subQr == null) {
            validaQr = false;
            valida = false;
        } else {
            switch (tipo) {
                case "GAMAC_POL":
                    if (subQr.length() < 3 || subQr.length() > 4) {
                        validaQr = false;
                        valida = false;
                    }
                    break;
                default:
                    if (subQr.length() != 4) {
                        validaQr = false;
                        valida = false;
                    }
                    break;
            }
        }
        if (!validaQr) {
            JsfUtil.mensajeError("Error el código es incorrecto");
            if (tipo.equals("GEPP_RES")) {
                JsfUtil.invalidar("validaForm:txtSubQr");
            } else {
                JsfUtil.invalidar("validaForm:txtSubQr2");
            }
        }
        return valida;
    }

    public void buscarDocumento() {
        try {
            String idDinamico = "";
            boolean pathDinamico = false;
            String idDinamico2 = "";
            boolean pathDinamico2 = false;
            if (validaParametros()) {
                visualizador.setVerDocumento(false);
                inicio = false;
                String p_id = null;
                String p_id2 = null;
                if (tipo.equals("GEPP_RES")) {
                    p_id = validaQrFacade.buscarHashGeppWeb(subQr, numero, anho);
                }
                if (tipo.equals("GAMAC_POL")) {
                    p_id = validaQrFacade.buscarHashPoligonoWeb(subQr, numero);
                }
                if (tipo.equals("GSSP_INS")) {
                    p_id = validaQrFacade.buscarHashGsspWeb(subQr, numero);
                }
                if (tipo.equals("GAMAC_GTRAN")) {
                    p_id = validaQrFacade.buscarHashGamacGTWeb(subQr, numero, anho);
                }
                if (tipo.equals("GAMAC_ACTA")) {
                    p_id = validaQrFacade.buscarHashGamacActaWeb(subQr, numero, anho);
                    pathDinamico = true;
                    idDinamico = p_id + "_acta";
                }
                if (tipo.equals("GAMAC_RAESS")) {
                    p_id = validaQrFacade.buscarHashGamacRaessWeb(subQr, numero, anho);
                }
                if (tipo.equals("GAMAC_CONSTLICTAR")) {
                    p_id = validaQrFacade.buscarHashGamacConstLicTarWeb(subQr, numero, anho);
                }

                if (tipo.equals("GAMAC_CONST_VAL")) {
                    Map constanciaMap = validaQrFacade.buscarHashGamacConstValWeb(subQr, numero, anho);
                    String path = constanciaMap.get("PATH").toString();
                    path = path.replace("/", File.separator);
                    p_id = path + JsfUtil.bundle("PathSeparator") + constanciaMap.get("NOMBRE").toString();
                    pathDinamico = true;
                    idDinamico = constanciaMap.get("NOMBRE").toString();
                }
                if (tipo.equals("GAMAC_CONST_VER")) {
                    Map constanciaMap = validaQrFacade.buscarHashGamacConstVerWeb(subQr, numero, anho);
                    String nom = constanciaMap.get("NOMBRE").toString();
                    nom = nom.replace("__", "_");
                    String path = constanciaMap.get("PATH").toString();
                    path = path.replace("/", JsfUtil.bundle("PathSeparator"));
                    p_id = path + JsfUtil.bundle("PathSeparator") + nom;
                    pathDinamico = true;
                    idDinamico = nom;
                    
                    String nom2 = constanciaMap.get("NOMBRE_OPCION2").toString();
                    nom2 = nom2.replace("__", "_");
                    p_id2 = path + JsfUtil.bundle("PathSeparator") + nom2;
                    pathDinamico2 = true;
                    idDinamico2 = nom2;
                }
                if (tipo.equals("GAMAC_EXAMEN_POLI")) {
                    Map constanciaMap = validaQrFacade.buscarHashGamacConstExamenPoligonoVerWeb(subQr, numero, anho);
                    String nom = constanciaMap.get("NOMBRE").toString();
                    nom = nom.replace("__", "_");
                    String path = constanciaMap.get("PATH").toString();
                    path = path.replace("/", JsfUtil.bundle("PathSeparator"));
                    p_id = path + JsfUtil.bundle("PathSeparator") + nom;
                    pathDinamico = true;
                    idDinamico = nom;
                }
                if (tipo.equals("GSSP_OFIC")) {
                    p_id = validaQrFacade.buscarHashOficio(subQr, numero, anho);   
                }
                if (tipo.equals("GAMAC_OFIC")) {
                    Map res = validaQrFacade.buscarHashOficioGamac(subQr, numero, anho);   
                    nombreArchivo = res.get("NOMBRE").toString();
                    //System.out.println("nomar:"+nombreArchivo);
                    nombreArchivoEnc = res.get("HASH_QR").toString();
                    if (nombreArchivo != null) {
                        visualizador.setVerDocumento(true);
                    }
                }
                if (tipo.equals("GSSP_CONSTEM_CAR")) {
                    Map res = validaQrFacade.buscarHashConstTempCarne(subQr, numero, anho);
                    nombreArchivo = res.get("NOMBRE").toString();
                    //System.out.println("nomar:"+nombreArchivo);
                    nombreArchivoEnc = res.get("HASH_QR").toString();
                    if (nombreArchivo != null) {
                        visualizador.setVerDocumento(true);
                    }
                }
                if (tipo.equals("GAMAC_CERT_IMP")) {
                    Map res = validaQrFacade.buscarHashCertImp(subQr, numero, anho);
                    nombreArchivo = res.get("NOMBRE").toString();
                    //System.out.println("nomar:"+nombreArchivo);
                    nombreArchivoEnc = res.get("HASH_QR").toString();
                    if (nombreArchivo != null) {
                        visualizador.setVerDocumento(true);
                    }
                }
                
                if (p_id != null) {
                    visualizador.generarArchivos(p_id, idDinamico, pathDinamico, p_id2, idDinamico2, pathDinamico2);
                }
            }
        } catch (Exception ex) {
        }

    }

    public void cambiarTipo() {
        inicializar();
    }

    public void inicializar() {
        subQr = "";
        numero = "";
        anho = "";
        String pathPdf = null, pathImg = null;
        VerificadorVisualizador.Orientacion o = VerificadorVisualizador.Orientacion.VERTICAL;
        nombreArchivo = null;
        nombreArchivoEnc = null;

        if (tipo.equals("GEPP_RES")) {
            pathPdf = JsfUtil.bundle("PathGeppResPdf");
            pathImg = JsfUtil.bundle("PathGeppResImg");
        }
        if (tipo.equals("GAMAC_POL")) {
            pathPdf = JsfUtil.bundle("PathGamacPolPdf");
            pathImg = JsfUtil.bundle("PathGamacPolImg");
        }
        if (tipo.equals("GSSP_INS")) {
            pathPdf = JsfUtil.bundle("PathGsspInsPdf");
            pathImg = JsfUtil.bundle("PathGsspInsImg");
            o = VerificadorVisualizador.Orientacion.HORIZONTAL;
        }
        if (tipo.equals("GAMAC_GTRAN")) {
            pathPdf = JsfUtil.bundle("PathGamacAmaPdf");
            pathImg = JsfUtil.bundle("PathGamacAmaImg");
        }
        if (tipo.equals("GAMAC_ACTA")) {
            pathPdf = JsfUtil.bundle("PathGamacActaPdf");
            pathImg = JsfUtil.bundle("PathGamacActaImg");
        }
        if (tipo.equals("GAMAC_RAESS")) {
            pathPdf = JsfUtil.bundle("PathGamacResolucionArmaPdf");
            pathImg = JsfUtil.bundle("PathGamacResolucionArmaImg");
        }
        if (tipo.equals("GAMAC_CONST_VAL")) {
            pathPdf = JsfUtil.bundle("PathConstanciaValPdf");
            pathImg = JsfUtil.bundle("PathConstanciaValImg");
        }
        if (tipo.equals("GAMAC_CONSTLICTAR")) {
            pathPdf = JsfUtil.bundle("PathConstanciaLicTarPdf");
            pathImg = JsfUtil.bundle("PathConstanciaLicTarImg");
        }
        if (tipo.equals("GAMAC_CONST_VER")) {
            pathPdf = ejbSbParametroFacade.obtenerParametroXNombreGamac("Documentos_pathUpload_constanciaVeri").getValor();
            pathImg = JsfUtil.bundle("PathConstanciaVerImg");
        }
        if (tipo.equals("GAMAC_EXAMEN_POLI")) {
            pathPdf = ejbSbParametroFacade.obtenerParametroXNombreIntegrado("Documentos_pathUpload_examenPol").getValor();
            pathImg = JsfUtil.bundle("PathConstanciaExaPoliImg");
        }
        if (tipo.equals("GSSP_OFIC")) {
            pathPdf = JsfUtil.bundle("PathGsspCarneCursoVencidoPdf");
            pathImg = JsfUtil.bundle("PathGsspCarneCursoVencidoImg");
        }
        visualizador = new VerificadorVisualizador(pathPdf, pathImg, o);
        inicio = true;
    }

    @PostConstruct
    public void construirController() {
        captcha = new Captcha();
        tipo = "GEPP_RES";
        inicializar();
    }
}
