package pe.gob.sucamec.encuesta.jsf;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.primefaces.context.RequestContext;
import pe.gob.sucamec.encuesta.beans.SbEncuestaFacade;
import pe.gob.sucamec.encuesta.beans.SbRespuestaFacade;
import pe.gob.sucamec.encuesta.data.SbAlternativa;
import pe.gob.sucamec.encuesta.data.SbEncuesta;
import pe.gob.sucamec.encuesta.data.SbPregunta;
import pe.gob.sucamec.encuesta.data.SbRespuesta;
import pe.gob.sucamec.sel.gamac.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.jsf.util.SbRespuestaMostrarClass;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

@Named("sbEncuestaController")
@SessionScoped
public class SbEncuestaController implements Serializable {
    
    private SbEncuesta encuestaPend;
    private List<SbRespuestaMostrarClass> listRespuesta;
    private SbUsuario usuSesion;
    private Boolean finalizado;
    private Boolean pendiente;
    private List<SbEncuesta> listEncuestas;
    private String codTipo;
    private List<SbAlternativa> listAlternativas;
    private List<Map> listPreguntas;

    @EJB
    private SbEncuestaFacade ejbSbEncuestaFacade;
    @EJB
    private SbRespuestaFacade ejbSbRespuestaFacade;
    
    @Inject
    private LoginController loginController;

    public SbEncuesta getSbEncuesta(java.lang.Long id) {
        return ejbSbEncuestaFacade.find(id);
    }
    
    public void onTimeout() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El tiempo de la evaluación ha concluido. Gracias por su participación. Se guardaron con éxito las respuestas seleccionadas hasta este momento.", null));
        guardarRpta();
        //encuestaPend = null;
        loginController.direccionarLogin();
    }
    
    public void existeEncuesta(Long sistemaId) {
        pendiente = Boolean.FALSE;
        finalizado = Boolean.FALSE;
        listEncuestas = listEncuestaPendiente(sistemaId);
        if (listEncuestas != null && !listEncuestas.isEmpty()) {
            listRespuesta = new ArrayList<>();
            pendiente = Boolean.TRUE;
            openDlgSeleccionEncuesta();
        }
    }
    
    public List<SbEncuesta> listEncuestaPendiente(Long sistemaId) {
        List<SbEncuesta> listEncBD = null;
        if (usuSesion != null) {
            listEncBD = ejbSbEncuestaFacade.selectPendientes(sistemaId);
//            encuestasDentroFecha(listEncBD);
            if (listEncBD != null && !listEncBD.isEmpty()) {
                List<SbEncuesta> listAux = new ArrayList(listEncBD);
                for (SbEncuesta enc : listAux) {
                    List<SbRespuesta> listRespBD = ejbSbRespuestaFacade.selectRespuestas(enc, usuSesion);
                    if (listRespBD != null && !listRespBD.isEmpty()) {
                        listEncBD.remove(enc);
                    }
                }
            }
        }
        return listEncBD;
    }
    
    public void openDlgSeleccionEncuesta() {
        encuestaPend = listEncuestas.get(0);
        RequestContext.getCurrentInstance().execute("PF('seleccionViewDialog').show()");
        RequestContext.getCurrentInstance().update("formSeleccion");
    }
    
    public void openEncuesta() {
        if (encuestaPend == null) {
            JsfUtil.mensajeAdvertencia("Por favor seleccione la encuesta a realizar.");
            return;
        }
        //
       listPreguntas= obtenerTablaPreguntas(encuestaPend.getSbPreguntaList());
        //
        //respuesta = new SbRespuesta();
        if (encuestaPend != null && encuestaPend.getSbPreguntaList() != null && !encuestaPend.getSbPreguntaList().isEmpty()) {
            codTipo = encuestaPend.getTipoId().getTipoId().getCodProg();
            listRespuesta = new ArrayList<>();
            SbRespuesta resp = null;
            SbRespuestaMostrarClass r = null;
            for (SbPregunta pre : encuestaPend.getSbPreguntaList()) {

                resp = new SbRespuesta();
                resp.setPreguntaId(pre);
                resp.setFechaRegistro(new Date());
                resp.setUsuarioId(usuSesion);
                r = new SbRespuestaMostrarClass(resp, new ArrayList<SbAlternativa>());
                listRespuesta.add(r);
            }
        }
        RequestContext.getCurrentInstance().execute("PF('seleccionViewDialog').hide()");
        RequestContext.getCurrentInstance().update("formEncuesta");
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void guardarRpta() {
        if (validarRespuestas()) {
            if (listRespuesta != null && !listRespuesta.isEmpty()) {
//                List<SbAlternativa> alternativasSelect = null;
                SbRespuesta resp = null;
                for (SbRespuestaMostrarClass item : listRespuesta) {
                    resp = item.getRespuesta();
//                    alternativasSelect = (List) item.get("alternativas");
                    switch (resp.getPreguntaId().getTipoId().getCodProg()) {
                        case "TP_EUNI_DIC":
                        case "TP_EUNI_POLI":
                        case "TP_ESC_NUM":
                        case "TP_ESC_NOM":
                            if (resp.getAlternativaId() != null) {
                                resp = (SbRespuesta) JsfUtil.entidadMayusculas(resp, "");
                                resp.setActivo(JsfUtil.TRUE);
                                resp.setAudLogin(usuSesion.getLogin());
                                ejbSbRespuestaFacade.create(resp);
                            }
                            break;
                        case "TP_ESC_LIK":
                            break;
                        case "TP_CER_EMUL":
                            if (item.getAlternativas() != null && !item.getAlternativas().isEmpty()) {
                                SbRespuesta respuestaTemp = null;
                                for (SbAlternativa a : item.getAlternativas()) {
                                    respuestaTemp = new SbRespuesta();
                                    respuestaTemp.setFechaRegistro(resp.getFechaRegistro());
                                    respuestaTemp.setPreguntaId(resp.getPreguntaId());
                                    respuestaTemp.setUsuarioId(resp.getUsuarioId());
                                    respuestaTemp = (SbRespuesta) JsfUtil.entidadMayusculas(respuestaTemp, "");
                                    respuestaTemp.setAlternativaId(a);
                                    respuestaTemp.setActivo(JsfUtil.TRUE);
                                    respuestaTemp.setAudLogin(usuSesion.getLogin());
                                    ejbSbRespuestaFacade.create(respuestaTemp);
                                }
                            }
                            break;
                        case "TP_CER_RANK":
                            break;
                        default:
                            break;
                    }
                }
//                JsfUtil.mensaje("¡Muchas gracias por su colaboración al contestar la encuesta, su opinión es muy valiosa!");
                RequestContext.getCurrentInstance().execute("PF('FormEncuestaViewDialog').hide()");
                RequestContext.getCurrentInstance().update("formEncuesta");
                encuestaPend = new SbEncuesta();
                finalizado = Boolean.TRUE;
            }
        }
    }

    public Boolean validarRespuestas() {
        Boolean res = Boolean.FALSE;
        if (listRespuesta != null && !listRespuesta.isEmpty()) {
            Boolean ok = Boolean.TRUE;
            res = Boolean.TRUE;
            SbRespuesta r = null;
//            List<SbAlternativa> alternativasSelect = null;

            for (SbRespuestaMostrarClass item : listRespuesta) {
                r = item.getRespuesta();
                if (r.getPreguntaId().getCondicionRespuesta() == JsfUtil.TRUE) {
                    switch (r.getPreguntaId().getTipoId().getCodProg()) {
                        case "TP_EUNI_DIC":
                        case "TP_EUNI_POLI":
                        case "TP_ESC_NUM":
                        case "TP_ESC_NOM":
                            ok = r.getAlternativaId() != null;
                            break;
                        case "TP_ESC_LIK":
                            break;
                        case "TP_CER_EMUL":
                            ok = Boolean.FALSE;
                            if (item.getAlternativas() != null && !item.getAlternativas().isEmpty()) {
                                ok = Boolean.TRUE;
                            }
                            break;
                        case "TP_CER_RANK":
                            break;
                        default:
                            break;
                    }
                    if (!ok) {
                        res = Boolean.FALSE;
                        JsfUtil.mensajeError("Debe responder a la pregunta " + r.getPreguntaId().getDescripcion());
                    }
                }
            }
        }
        return res;
    }

    public String obtenerFechaClock() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        if (encuestaPend != null) {
            Calendar c_hoy = Calendar.getInstance();
            String fechaFormateada = "";

            if (c_hoy.getTime().compareTo(encuestaPend.getFechaIni()) < 0) {
                fechaFormateada = dateFormat.format(encuestaPend.getFechaIni());
            } else {
                fechaFormateada = dateFormat.format(encuestaPend.getFechaFin());
            }
            return fechaFormateada;
        }
        return "02/19/2018 10:10"; //dateFormat.format(new Date());
    }
    
    public List<Map> obtenerTablaPreguntas(List<SbPregunta> listPreg){
        List<Map> listResp=null;
        if(listPreg!=null&&!listPreg.isEmpty()){
            
            listAlternativas=new ArrayList<>();
            for(SbPregunta p:listPreg){
//                item.put("pregunta", p);
                listAlternativas=p.getSbAlternativaList();
                break;
            }
            
            for(SbPregunta p:listPreg){
                listResp = new ArrayList<>();
                Map item=new HashMap();
                    for (SbPregunta p1:listPreg) {
                        item = new HashMap();
                        item.put("pregunta", p1);
                        item.put("listAlternativas", p1.getSbAlternativaList());
                        listResp.add(item);
                    }
                
            }
        }
        return listResp;
    }
    
    public SbEncuesta getEncuestaPend() {
        return encuestaPend;
    }

    public void setEncuestaPend(SbEncuesta encuestaPend) {
        this.encuestaPend = encuestaPend;
    }

    public List<SbRespuestaMostrarClass> getListRespuesta() {
        return listRespuesta;
    }

    public void setListRespuesta(List<SbRespuestaMostrarClass> listRespuesta) {
        this.listRespuesta = listRespuesta;
    }

    public SbUsuario getUsuSesion() {
        return usuSesion;
    }

    public void setUsuSesion(SbUsuario usuSesion) {
        this.usuSesion = usuSesion;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public Boolean getPendiente() {
        return pendiente;
    }

    public void setPendiente(Boolean pendiente) {
        this.pendiente = pendiente;
    }

    public List<SbEncuesta> getListEncuestas() {
        return listEncuestas;
    }

    public void setListEncuestas(List<SbEncuesta> listEncuestas) {
        this.listEncuestas = listEncuestas;
    }

    public String getCodTipo() {
        return codTipo;
    }

    public void setCodTipo(String codTipo) {
        this.codTipo = codTipo;
    }

    public List<SbAlternativa> getListAlternativas() {
        return listAlternativas;
    }

    public void setListAlternativas(List<SbAlternativa> listAlternativas) {
        this.listAlternativas = listAlternativas;
    }

    public List<Map> getListPreguntas() {
        return listPreguntas;
    }

    public void setListPreguntas(List<Map> listPreguntas) {
        this.listPreguntas = listPreguntas;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sbEncuestaConverter")
    public static class SbEncuestaControllerConverterN extends SbEncuestaControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SbEncuesta.class)
    public static class SbEncuestaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SbEncuestaController c = (SbEncuestaController) JsfUtil.obtenerBean("sbEncuestaController", SbEncuestaController.class);
            return c.getSbEncuesta(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbEncuesta) {
                SbEncuesta o = (SbEncuesta) object;
                return o.getId() == null ? null : o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SbEncuesta.class.getName());
            }
        }
    }

}
