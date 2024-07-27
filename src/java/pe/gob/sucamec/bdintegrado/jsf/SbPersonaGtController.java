package pe.gob.sucamec.bdintegrado.jsf;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Named;
import javax.transaction.Transactional;
import org.primefaces.context.RequestContext;
import pe.gob.sucamec.bdintegrado.bean.AmaResolucionFacade;
import pe.gob.sucamec.bdintegrado.bean.SbPersonaFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.SbRelacionPersonaFacadeGt;
import pe.gob.sucamec.bdintegrado.bean.TipoBaseFacadeGt;
import pe.gob.sucamec.bdintegrado.data.AmaResolucion;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbRelacionPersonaGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.rma1369.bean.Rma1369Facade;
import pe.gob.sucamec.sel.gamac.jsf.util.JsfUtil;
import wspide.Consulta;
import wspide.Consulta_Service;

// Nombre de la instancia en la aplicacion //
@Named("sbPersonaGtController")
@SessionScoped

/**
 * Clase con SbPersonaController instanciada como sbPersonaController. Contiene
 * funciones utiles para la entidad SbPersonaGt. Esta vinculada a las páginas
 * SbPersonaGt/create.xhtml, SbPersonaGt/update.xhtml, SbPersonaGt/list.xhtml,
 * SbPersonaGt/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec
 * para que funcione adecuadamente, revisar si tiene el campo activo y modificar
 * las búsquedas.
 */
public class SbPersonaGtController implements Serializable {

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    SbPersonaGt registro;

    //VARIABLES PARA LA AGREGAR PERSONAS AUTORIZADAS
    private SbPersonaGt casaComercial;
    private SbPersonaGt personaVinc = null;
    private List<SbRelacionPersonaGt> perVincLst = new ArrayList();
    private String filtroNroDoc = null;
    private List<SbPersonaGt> personaList = new ArrayList();
    private String msjConfirmacionRegistroPersona;
    private SbPersonaGt personaNew = new SbPersonaGt();
    private String cantNumeros;
    private Boolean disabledBtVal;

    private Date fechaHoy = new Date();
    //

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private SbPersonaFacadeGt sbPersonaFacade;
    @EJB
    private TipoBaseFacadeGt ejbTipoBaseGtFacade;
    @EJB
    private SbRelacionPersonaFacadeGt ejbSbRelacionPersonaGtFacade;
    @EJB
    private Rma1369Facade ejbRma1369Facade;
    @EJB
    private AmaResolucionFacade ejbAmaResolucionFacade;

    public void mostrarBuscarPersonas() {
        filtroNroDoc = null;
        personaList = new ArrayList();
    }

    public String preparePersonaAutorizada() {
        casaComercial = sbPersonaFacade.obtenerEmpresaXRuc(JsfUtil.getLoggedUser().getPersona().getRuc());
        if (validaSeleccionCC(casaComercial)) {
            seleccionarCC(casaComercial);
            personaVinc = null;
            return "/aplicacion/gamac/autorizacionPersona/BandejaVinculacion.xhtml";
        }
        return null;
    }

    public void seleccionarCC(SbPersonaGt casaCom) {
        perVincLst = new ArrayList();
        if (casaCom.getSbRelacionPersonaList() != null && !casaCom.getSbRelacionPersonaList().isEmpty()) {
            for (SbRelacionPersonaGt relacion : casaCom.getSbRelacionPersonaList()) {
                if (relacion.getActivo() == 1 && Objects.equals(relacion.getTipoId().getCodProg(), "TP_REL_AUT")) {
                    perVincLst.add(relacion);
                }
            }
        }
    }

    public boolean validaSeleccionCC(SbPersonaGt casaCom) {
        boolean flagValida = true;
        List<Map> lstResolucionRma = ejbRma1369Facade.buscarRDCasaCom(casaCom.getRuc() == null ? casaCom.getNumDoc() : casaCom.getRuc());
        List<AmaResolucion> listResoluciones = ejbAmaResolucionFacade.obtenerResolucionesVigentes(casaCom.getRuc() == null ? casaCom.getNumDoc() : casaCom.getRuc());
        if ((lstResolucionRma == null || lstResolucionRma.isEmpty())
                && (listResoluciones == null || listResoluciones.isEmpty())) {
            JsfUtil.mensajeError("La casa comercial seleccionada no cuenta con autorización vigente");
            flagValida = false;
        }
        return flagValida;
    }

    public void vincular() {
        TipoBaseGt tipoRelacion = ejbTipoBaseGtFacade.tipoPorCodProg("TP_REL_AUT");
        if (validaVinculacion(tipoRelacion)) {
            SbRelacionPersonaGt relacion = new SbRelacionPersonaGt();
            relacion.setActivo((short) 1);
            relacion.setAudLogin(JsfUtil.getLoggedUser().getLogin());
            relacion.setAudNumIp(JsfUtil.getNumIP());
            relacion.setFecha(new Date());
            relacion.setPersonaOriId(casaComercial);
            relacion.setPersonaDestId(personaVinc);
            relacion.setTipoId(tipoRelacion);
            ejbSbRelacionPersonaGtFacade.create(relacion);
            casaComercial = sbPersonaFacade.obtenerEmpresaXRuc(JsfUtil.getLoggedUser().getPersona().getRuc());
            if (perVincLst == null) {
                perVincLst = new ArrayList<>();
            }
            if (!perVincLst.contains(relacion)) {
                perVincLst.add(relacion);
            }
            personaVinc = null;
            RequestContext.getCurrentInstance().update("vincForm");
            JsfUtil.mensaje("La persona ha sido vinculada exitosamente.");
        }
    }

//    public SbPersonaGt convertirEntityPersona(SbPersona perIn) {
//        SbPersonaGt perOut = null;
//        if (perIn != null) {
//            perOut = new SbPersonaGt();
//            perOut.setActivo(perIn.getActivo());
//            perOut.setApePat(perIn.getApePat());
//            perOut.setApeMat(perIn.getApeMat());
//            perOut.setNombres(perIn.getNomCom());
//            perOut.setEnActividad(perIn.getEnActividad());
//            perOut.setEstCivilId(convertirTipoBaseEntity(perIn.getEstCivilId()));
//            perOut.setFechaNac(perIn.getFechaNac());
//            perOut.setFechaReg(perIn.getFechaReg());
//            perOut.setGeneroId(convertirTipoBaseEntity(perIn.getGeneroId()));
//            perOut.setId(perIn.getId());
//            perOut.setInstituLabId(convertirTipoBaseEntity(perIn.getInstituLabId()));
//            perOut.setNroCip(perIn.getNroCip());
//            perOut.setNumDoc(perIn.getNumDoc());
//            perOut.setNumDocVal(perIn.getNumDocVal());
//            perOut.setOcupacionId(convertirTipoBaseEntity(perIn.getOcupacionId()));
//            perOut.setRuc(perIn.getRuc());
//            perOut.setRznSocial(perIn.getRznSocial());
//            perOut.setTipoDoc(convertirTipoBaseEntity(perIn.getTipoDoc()));
//            perOut.setTipoId(convertirTipoBaseEntity(perIn.getTipoId()));
//        }
//        return perOut;
//    }
//    public TipoBaseGt convertirTipoBaseEntity(SbTipo tipoIn) {
//        TipoBaseGt tipoOut = null;
//        if (tipoIn != null) {
//            tipoOut = new TipoBaseGt();
//            tipoOut.setAbreviatura(tipoIn.getAbreviatura());
//            tipoOut.setActivo(tipoIn.getActivo());
//            tipoOut.setCodProg(tipoIn.getCodProg());
//            tipoOut.setDescripcion(tipoIn.getDescripcion());
//            tipoOut.setNombre(tipoIn.getNombre());
//            tipoOut.setOrden(tipoIn.getOrden());
//            tipoOut.setId(tipoIn.getId());
//            tipoOut.setTipoId(convertirTipoBaseEntity(tipoIn.getTipoId()));
//        }
//        return tipoOut;
//    }
    public void mostrarCrearPersonas() {
        personaNew = new SbPersonaGt();
        personaNew.setActivo((short) 1);
        personaNew.setAudLogin(JsfUtil.getLoggedUser().getLogin());
        personaNew.setAudNumIp(JsfUtil.getNumIP());
        personaNew.setTipoId(ejbTipoBaseGtFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
        disabledBtVal = Boolean.TRUE;
    }

    public void desvincular(SbRelacionPersonaGt relacion) {
        relacion.setActivo((short) 0);
        ejbSbRelacionPersonaGtFacade.edit(relacion);
        JsfUtil.mensaje("La persona ha sido desvinculada exitosamente");
        seleccionarCC(casaComercial);
    }

    public void limpiarBandejaVinculacion() {
        personaVinc = null;
        filtroNroDoc = null;
        personaList = new ArrayList();
        personaNew = new SbPersonaGt();
    }

//    /**
//     * buscador de personas naturales
//     */
//    public void buscarPersonasNaturales() {
//        listBusPersona = sbPersonaFacade.selectLikePN(filtro);
//    }
    public void buscarPersonas() {
        if (validaBusquedaPer()) {
            personaList = sbPersonaFacade.selectPersonaActivaxDoc(filtroNroDoc);
        }
    }

    public boolean validaBusquedaPer() {
        boolean flagValida = true;
        if (sbPersonaFacade.selectPersonaActivaxDoc(filtroNroDoc).isEmpty()) {
            JsfUtil.mensajeError("El número de documento ingresado no existe en el sistema");
            flagValida = false;
        }
        return flagValida;
    }

    public void seleccionarPersona(SbPersonaGt per) {
        if (validaSeleccionPersona(per)) {
            personaVinc = per;
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void registrarVincular() {
        try {
            if (validarDatosPersona(personaNew)) {
                if (documentoOk(personaNew.getNumDoc().length())) {
                    Boolean existePersona = Boolean.FALSE;
                    SbPersonaGt personaTemp = sbPersonaFacade.buscarPersonaXNumDoc(personaNew.getNumDoc());
                    if (personaTemp == null) {
                        if ("TP_DOCID_DNI".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())
                                || "TP_DOCID_CE".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())) {
                            personaNew.setTipoId(ejbTipoBaseGtFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
                            personaNew.setRznSocial(null);
                            //Validación 
                        } else if ("TP_DOCID_RUC".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())
                                || "TP_DOCID_EXT".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())) {
                            if (personaNew.getNumDoc().startsWith("20")) {
                                personaNew.setTipoId(ejbTipoBaseGtFacade.buscarTipoBaseXCodProg("TP_PER_JUR"));
                                personaNew.setRuc(personaNew.getNumDoc());
                                personaNew.setNombres(null);
                                personaNew.setApePat(null);
                                personaNew.setApeMat(null);
                                personaNew.setNumDoc(null);
                            } else {
                                JsfUtil.mensajeError("El RUC ingresado no corresponde a una persona jurídica.");
                                return;
                            }
                        }
                        personaNew.setAudLogin(JsfUtil.getLoggedUser().getLogin());
                        personaNew.setAudNumIp(JsfUtil.getIpAddress());
                        personaNew.setFechaReg(new Date());
                        personaNew.setActivo((short) 1);
                        personaNew = (SbPersonaGt) JsfUtil.entidadMayusculas(personaNew, "");
                        sbPersonaFacade.create(personaNew);
                        if (personaNew.getId() != null) {
                            personaVinc = personaNew;
                            existePersona = Boolean.TRUE;
                        } else {
                            JsfUtil.mensajeError(JsfUtil.bundleGamac("MensajeErrorRegistroPersona"));
                        }
                    } else {
                        personaVinc = personaTemp;
                        existePersona = Boolean.TRUE;
                        JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("MensajeNumeroDocRegistrado"));
                    }
                    if (existePersona) {
                        vincular();
                        RequestContext.getCurrentInstance().execute("PF('ConfRegPersonaViewDialog').hide()");
                        RequestContext.getCurrentInstance().update("formConfRegPersona");
                        RequestContext.getCurrentInstance().update("buscarPersonaForm");

                        RequestContext.getCurrentInstance().execute("PF('dlgCrearPersona').hide()");
                        RequestContext.getCurrentInstance().update("formConfEntrega");
                        RequestContext.getCurrentInstance().update("vincForm");
                    }

                } else {
                    JsfUtil.mensajeError(JsfUtil.bundleGamac("MensajeValidacionNumeroTipoDocumento"));
                }
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelar() {
        RequestContext.getCurrentInstance().execute("PF('ConfRegPersonaViewDialog').hide()");
        RequestContext.getCurrentInstance().update("formConfRegPersona");
        RequestContext.getCurrentInstance().update("buscarPersonaForm");
    }

    /**
     * @return the listTipoDoc
     */
    public List<TipoBaseGt> getListTipoDoc() {
        List<TipoBaseGt> listRes = ejbTipoBaseGtFacade.listarTipoBaseXCodProg("TP_DOCID");
        if (listRes != null && !listRes.isEmpty()) {
            List<TipoBaseGt> listTemp = new ArrayList(listRes);
            for (TipoBaseGt tipoDoc : listTemp) {
                if (!"TP_DOCID_DNI".equals(tipoDoc.getCodProg())
                        && !"TP_DOCID_CE".equals(tipoDoc.getCodProg())) {
                    listRes.remove(tipoDoc);
                }
            }
        }
        return listRes;
    }

    public void obtenerNumCaract() {
        TipoBaseGt tipoDoc = personaNew.getTipoDoc();
        personaNew = new SbPersonaGt();
        personaNew.setTipoDoc(tipoDoc);
        //disabledDat = Boolean.TRUE;
        if (personaNew.getTipoDoc() != null) {
            if ("TP_DOCID_DNI".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())) {
                setCantNumeros("8");
                disabledBtVal = Boolean.FALSE;
            } else if ("TP_DOCID_CE".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())) {
                setCantNumeros("9");
                disabledBtVal = Boolean.TRUE;
                //disabledDat = Boolean.FALSE;
            } else if ("TP_DOCID_RUC".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())) {
                setCantNumeros("11");
                disabledBtVal = Boolean.FALSE;
            } else {
                setCantNumeros("15");
                disabledBtVal = Boolean.TRUE;
                //disabledDat = Boolean.FALSE;
            }
        }
    }

    public void validarDocumento() {
        String msjErrorWsPide = "";
        //Boolean existePerReniec = Boolean.FALSE;
        try {
            Consulta_Service serR = new Consulta_Service();
            Consulta port = serR.getConsultaPort();
            //disabledDat = Boolean.FALSE;
            wspide.BeanDdp pj = null;
            wspide.Persona pn = null;
            if (personaNew != null && personaNew.getTipoDoc() != null) {
                if (personaNew.getNumDoc() != null && !"".equals(personaNew.getNumDoc().trim())) {
                    if ("TP_DOCID_DNI".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())) {
                        pn = port.consultaDNI(personaNew.getNumDoc());
                        personaNew.setApePat(null);
                        personaNew.setApeMat(null);
                        personaNew.setNombres(null);
                        if (pn != null) {
                            personaNew.setApePat(pn.getAPPAT());
                            personaNew.setApeMat(pn.getAPMAT());
                            personaNew.setNombres(pn.getNOMBRES());
                            if (pn.getFENAC() != null && !"".equals(pn.getFENAC())) {
                                Date date = null;
                                try {
                                    date = date = JsfUtil.obtenerFechaPorDatos(pn.getFENAC().substring(6, 8), pn.getFENAC().substring(4, 6), pn.getFENAC().substring(0, 4));
                                } catch (Exception ex) {
                                    date = null;
                                }
                                if (date != null) {
                                    personaNew.setFechaNac(date);
                                }
                            }
                            if (pn.getSEXO() != null && !"".equals(pn.getSEXO())) {
                                personaNew.setGeneroId(null);
                                if ("F".equals(pn.getSEXO())) {
                                    personaNew.setGeneroId(ejbTipoBaseGtFacade.tipoPorCodProg("TP_GEN_FEM"));
                                } else {
                                    personaNew.setGeneroId(ejbTipoBaseGtFacade.tipoPorCodProg("TP_GEN_MAS"));
                                }
                            }
                        } else {
                            personaNew = new SbPersonaGt();
                            JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("MensajeServicioConsultaReniecNulo"));
                        }
                    } else if ("TP_DOCID_RUC".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())) {
                        if (personaNew.getNumDoc().startsWith("20")) {
                            pj = port.consultaRuc(personaNew.getNumDoc());
                            personaNew.setRznSocial(null);
                            if (pj != null) {
                                if (pj.isEsHabido()) {
                                    personaNew.setRznSocial(pj.getDdpNombre());
                                } else {
                                    JsfUtil.mensajeAdvertencia("No se encontró resultado con el RUC ingresado.");
                                }
                            } else {
                                personaNew.setNumDoc(null);
                                personaNew.setApePat(null);
                                personaNew.setApeMat(null);
                                personaNew.setNombres(null);
                                personaNew.setFechaNac(null);
                                personaNew.setGeneroId(null);
                                JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("MensajeServicioConsultaSunatNulo"));
                            }
                        } else {
                            JsfUtil.mensajeAdvertencia("El RUC ingresado no corresponde a una persona jurídica.");
                        }
                    }
                } else {
                    JsfUtil.mensajeAdvertencia("Ingrese el número de documento a validar.");
                }
            } else {
                JsfUtil.mensajeAdvertencia("No se encontraron resultados. Por favor, inténtelo nuevamente.");
            }
            RequestContext.getCurrentInstance().update("crearPersonaForm");
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.mensajeError(JsfUtil.bundleGamac("Error al consultar el documento ingresado."));
        }
    }

    public void validarDatoIngresado(String in) {
        String tipo = "";
        int tam = 0;
        String valor = "";
        String idCampo = "";
        switch (in) {
            case "NUMDOC":
                disabledBtVal = Boolean.TRUE;
                if (personaNew.getNumDoc() != null && !"".equals(personaNew.getNumDoc().trim())) {
                    tam = personaNew.getNumDoc().length();
                    valor = personaNew.getNumDoc();
//                    if ("TP_DOCID_DNI".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())
//                            || "TP_DOCID_RUC".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())) {
//                        disabledBtVal = Boolean.FALSE;
//                    }
                    tipo = "N";
                }
                RequestContext.getCurrentInstance().update("crearPersonaForm");
                idCampo = "numDoc";
                break;
            case "NOM":
                if (personaNew.getNombres() != null && !"".equals(personaNew.getNombres().trim())) {
                    tam = personaNew.getNombres().length();
                    valor = personaNew.getNombres();
                    tipo = "L";
                }
                idCampo = "nomb";
                break;
            case "APPAT":
                if (personaNew.getApePat() != null && !"".equals(personaNew.getApePat().trim())) {
                    tam = personaNew.getApePat().length();
                    valor = personaNew.getApePat();
                    tipo = "L";
                }
                idCampo = "appat";
                break;
            case "APMAT":
                if (personaNew.getApeMat() != null && !"".equals(personaNew.getApeMat().trim())) {
                    tam = personaNew.getApeMat().length();
                    valor = personaNew.getApeMat();
                    tipo = "L";
                }
                idCampo = "apmat";
                break;
            default:
                break;
        }
        String nomCampo = "";
        Boolean isLetra = Boolean.TRUE;
        Boolean isNumero = Boolean.TRUE;
        if ("N".equalsIgnoreCase(tipo)) {
            for (int i = 0; i < tam; i++) {
                if (!Character.isDigit(valor.charAt(i))
                        || ' ' == valor.charAt(i)) {
                    isNumero = Boolean.FALSE;
                    break;
                }
            }
            if (!isNumero) {
                nomCampo = limpiarObtenerCampo(in);
                JsfUtil.mensajeAdvertencia("El campo " + nomCampo + " debe contener solamente números");
            }
        } else {
            for (int i = 0; i < tam; i++) {
                if (!Character.isLetter(valor.charAt(i))
                        && !Character.isSpaceChar(valor.charAt(i))) {
                    isLetra = Boolean.FALSE;
                    break;
                }
            }
            if (!isLetra) {
                nomCampo = limpiarObtenerCampo(in);
                JsfUtil.mensajeAdvertencia("El campo " + nomCampo + " debe contener solamente letras");
            }
        }
        if (!isLetra || !isNumero) {
            RequestContext.getCurrentInstance().update("crearPersonaForm:" + idCampo);
            return;
        } else if ("NUMDOC".equalsIgnoreCase(in)) {
            if (!documentoOk(tam)) {
                JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("MensajeValidacionNumeroTipoDocumento"));
                return;
            }
        }
    }

    public String limpiarObtenerCampo(String campo) {
        String nomCampo = "";
        switch (campo) {
            case "NUMDOC":
                personaNew.setNumDoc(null);
                nomCampo = "Número de documento";
                break;
            case "NOM":
                personaNew.setNombres(null);
                nomCampo = "Nombres";
                break;
            case "APPAT":
                personaNew.setApePat(null);
                nomCampo = "Apellido paterno";
                break;
            case "APMAT":
                personaNew.setApeMat(null);
                nomCampo = "Apellido materno";
                break;
            default:
                break;
        }
        return nomCampo;
    }

    public Boolean validarDatosPersona(SbPersonaGt persona) {
        Boolean res = Boolean.TRUE;
        if (persona.getTipoDoc() != null) {
            if ("TP_DOCID_DNI".equals(persona.getTipoDoc().getCodProg())
                    || "TP_DOCID_CE".equals(persona.getTipoDoc().getCodProg())) {
                if (persona.getNumDoc() == null || "".equals(persona.getNumDoc().trim())) {
                    JsfUtil.mensajeError(JsfUtil.bundleGamac("CrearPersonaRequiredMessage_NumDoc"));
                    res = Boolean.FALSE;
                }
                if (persona.getNombres() == null || "".equals(persona.getNombres().trim())) {
                    JsfUtil.mensajeError(JsfUtil.bundleGamac("CrearPersonaRequiredMessage_Nombres"));
                    res = Boolean.FALSE;
                }
                if (persona.getApePat() == null || "".equals(persona.getApePat().trim())) {
                    JsfUtil.mensajeError(JsfUtil.bundleGamac("CrearPersonaRequiredMessage_ApePat"));
                    res = Boolean.FALSE;
                }
                if ((persona.getApeMat() == null || "".equals(persona.getApeMat().trim()))
                        && "TP_DOCID_DNI".equals(persona.getTipoDoc().getCodProg())) {
                    JsfUtil.mensajeError(JsfUtil.bundleGamac("CrearPersonaRequiredMessage_ApeMat"));
                    res = Boolean.FALSE;
                }
                if (persona.getGeneroId() == null) {
                    JsfUtil.mensajeError(JsfUtil.bundleGamac("CrearPersonaRequiredMessage_Genero"));
                    res = Boolean.FALSE;
                }
            } else if ("TP_DOCID_RUC".equals(persona.getTipoDoc().getCodProg())
                    || "TP_DOCID_EXT".equals(persona.getTipoDoc().getCodProg())) {
                if (persona.getNumDoc() == null || "".equals(persona.getNumDoc().trim())) {
                    JsfUtil.mensajeError(JsfUtil.bundleGamac("CrearPersonaRequiredMessage_NumDoc"));
                    res = Boolean.FALSE;
                }
                if (persona.getRznSocial() == null || "".equals(persona.getRznSocial().trim())) {
                    JsfUtil.mensajeError("El campo Razón Social es obligatorio");
                    res = Boolean.FALSE;
                }
            }
        } else {
            JsfUtil.mensajeError(JsfUtil.bundleGamac("CrearPersonaRequiredMessage_TipoDoc"));
            res = Boolean.FALSE;
        }

        return res;
    }

    public Boolean documentoOk(int tam) {
        Boolean res = Boolean.TRUE;
        if ("TP_DOCID_DNI".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())
                && tam < 8) {
            res = Boolean.FALSE;
        }
        if ("TP_DOCID_CE".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())
                && tam < 9) {
            res = Boolean.FALSE;
        }
        if ("TP_DOCID_RUC".equalsIgnoreCase(personaNew.getTipoDoc().getCodProg())
                && tam < 11) {
            res = Boolean.FALSE;
        }
        return res;
    }

    /**
     * EVENTO PARA ABRIR FORMULARIO DE CONFIRMACION AL REGISTRAR PERSONA
     *
     * @author Gino Chávez
     * @version 2.0
     */
    public void openDlgConfirmarRegistro() {
        if (personaNew.getNombres() != null && personaNew.getApePat() != null && personaNew.getApeMat() != null && personaNew.getTipoDoc() != null && personaNew.getNumDoc() != null) {
            setMsjConfirmacionRegistroPersona("¿Confirma que " + personaNew.getNombres() + " " + personaNew.getApePat() + " " + personaNew.getApeMat()
                    + " con " + personaNew.getTipoDoc().getNombre() + " " + personaNew.getNumDoc() + " se registre como persona autorizada para el recojo de armas por parte de la casa comercializadora "
                    + casaComercial.getRznSocial() + "?");
            RequestContext.getCurrentInstance().execute("PF('ConfRegPersonaViewDialog').show()");
            RequestContext.getCurrentInstance().update("formConfRegPersona");
        }
    }

    public boolean validaVinculacion(TipoBaseGt tipoId) {
        boolean flagValida = true;
        casaComercial = sbPersonaFacade.obtenerEmpresaXRuc(JsfUtil.getLoggedUser().getPersona().getRuc());
        if (casaComercial.getSbRelacionPersonaList() != null && !casaComercial.getSbRelacionPersonaList().isEmpty()) {
            for (SbRelacionPersonaGt relacion : casaComercial.getSbRelacionPersonaList()) {
                if (Objects.equals(relacion.getPersonaDestId(), personaVinc)
                        && Objects.equals(relacion.getTipoId(), tipoId)
                        && relacion.getActivo() == 1) {
                    flagValida = false;
                }
            }
        }
        if (!flagValida) {
            JsfUtil.mensajeError("La persona ya se encuentra registrada como Persona Autorizada.");
        }
        return flagValida;
    }

    /**
     * @return the fechaMaxima
     */
    public Date getFechaMinMayorEdad() {
        return calculoFechaVencMeses(fechaHoy, -12 * 18);
    }

    /*
    Método que retorna la fecha sumado a la fecha ingresada el número de meses que se desea
     */
    public Date calculoFechaVencMeses(Date fecha, int meses) {
        String hoy = fechaSimple(fecha);
        String[] dataTemp = hoy.split("/");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(dataTemp[2]), Integer.parseInt(dataTemp[1]) - 1, Integer.parseInt(dataTemp[0]));
        c.add(Calendar.MONTH, meses);
        return c.getTime();
    }

    public String fechaSimple(Date fecha) {
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
    }

    public boolean validaSeleccionPersona(SbPersonaGt per) {
        boolean flagValida = true;
        return flagValida;
    }

    // Si la tabla no tiene el campo activo borrar DESDE ACA //
    /**
     * Devuelve si el registro actual se encuentra activo, se usa para borrado
     * lógico.
     *
     * @return true si se encuenta activo, false si no.
     */
    public boolean isActivo() {
        return (registro.getActivo() == 1);
    }

    /**
     * Cambia el estado del registro a activo o inactivo, se usa para borrado
     * lógico.
     *
     * @param activo true o false para cambiar el estado.
     */
    public void setActivo(boolean activo) {
        if (activo) {
            registro.setActivo((short) 1);
        } else {
            registro.setActivo((short) 0);
        }
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public SbPersonaGt getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SbPersonaGt registro) {
        this.registro = registro;
    }

    /**
     * Propiedad para el filtro de busquedas
     *
     * @return el filtro de busquedas
     */
    public String getFiltro() {
        return filtro;
    }

    /**
     * Propiedad para el filtro de busquedas
     *
     * @param filtro el nuevo filtro de busquedas
     */
    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<SbPersonaGt> getSelectItems() {
        return sbPersonaFacade.findAll();
    }

    /**
     * Constructor
     *
     */
    public SbPersonaGtController() {

    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public SbPersonaGt getSbPersona(Long id) {
        return sbPersonaFacade.find(id);
    }

    public SbPersonaGt getCasaComercial() {
        return casaComercial;
    }

    public void setCasaComercial(SbPersonaGt casaComercial) {
        this.casaComercial = casaComercial;
    }

    public SbPersonaGt getPersonaVinc() {
        return personaVinc;
    }

    public List<SbRelacionPersonaGt> getPerVincLst() {
        return perVincLst;
    }

    public String getFiltroNroDoc() {
        return filtroNroDoc;
    }

    public void setFiltroNroDoc(String filtroNroDoc) {
        this.filtroNroDoc = filtroNroDoc;
    }

    public List<SbPersonaGt> getPersonaList() {
        return personaList;
    }

    public void setPersonaList(List<SbPersonaGt> personaList) {
        this.personaList = personaList;
    }

    public String getMsjConfirmacionRegistroPersona() {
        return msjConfirmacionRegistroPersona;
    }

    public void setMsjConfirmacionRegistroPersona(String msjConfirmacionRegistroPersona) {
        this.msjConfirmacionRegistroPersona = msjConfirmacionRegistroPersona;
    }

    public SbPersonaGt getPersonaNew() {
        return personaNew;
    }

    public void setPersonaNew(SbPersonaGt personaNew) {
        this.personaNew = personaNew;
    }

    public String getCantNumeros() {
        return cantNumeros;
    }

    public void setCantNumeros(String cantNumeros) {
        this.cantNumeros = cantNumeros;
    }

    public Boolean getDisabledBtVal() {
        return disabledBtVal;
    }

    public void setDisabledBtVal(Boolean disabledBtVal) {
        this.disabledBtVal = disabledBtVal;
    }

    public Date getFechaHoy() {
        return fechaHoy;
    }

    public void setFechaHoy(Date fechaHoy) {
        this.fechaHoy = fechaHoy;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sbPersonaConverter")
    public static class SbPersonaControllerConverterN extends SbPersonaControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SbPersonaGt.class)
    public static class SbPersonaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SbPersonaGtController c = (SbPersonaGtController) JsfUtil.obtenerBean("sbPersonaGtController", SbPersonaGtController.class);
            Long x = 0L;
            try {
                x = Long.valueOf(value);
            } catch (Exception e) {
                x = -1L;
            }
            if (x != -1L) {
                return c.getSbPersona(Long.valueOf(value));
            } else {
                return null;
            }
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbPersonaGt) {
                SbPersonaGt o = (SbPersonaGt) object;
                String r = "";
                if (o.getId() != null) {
                    r = o.getId().toString();
                }
                return r;
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SbPersonaGt.class.getName());
            }
        }
    }

}
