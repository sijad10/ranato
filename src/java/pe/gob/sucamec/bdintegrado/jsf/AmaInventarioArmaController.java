package pe.gob.sucamec.bdintegrado.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import pe.gob.sucamec.bdintegrado.bean.AmaArmaFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaArmaInventarioDifFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaInventarioArmaFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaModelosFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaResolucionFacade;
import pe.gob.sucamec.bdintegrado.bean.AmaTarjetaPropiedadFacade;
import pe.gob.sucamec.bdintegrado.bean.TipoGamacFacade;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
import pe.gob.sucamec.bdintegrado.data.AmaArmaInventarioDif;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.AmaResolucion;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.jsf.util.EstadoCrud;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.bdintegrado.jsf.util.ReportUtil;
import pe.gob.sucamec.rma1369.bean.Rma1369Facade;
import pe.gob.sucamec.sistemabase.beans.SbPersonaFacade;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

// Nombre de la instancia en la aplicacion //
@Named("amaInventarioArmaController")
@SessionScoped

/**
 * Clase con AmaInventarioArmaController instanciada como
 * amaInventarioArmaController. Contiene funciones utiles para la entidad
 * AmaInventarioArma. Esta vinculada a las páginas
 * AmaInventarioArma/create.xhtml, AmaInventarioArma/update.xhtml,
 * AmaInventarioArma/list.xhtml, AmaInventarioArma/view.xhtml Nota: Las tablas
 * deben tener la estructura de Sucamec para que funcione adecuadamente, revisar
 * si tiene el campo activo y modificar las búsquedas.
 */
public class AmaInventarioArmaController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    private EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    /**
     * Registro actual para editar o ver.
     */
    AmaInventarioArma registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<AmaInventarioArma> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
//    List<AmaInventarioArma> registrosSeleccionados;
//    private List<AmaInvArmaClass> lstArmasEntrega;
//
//    private List<AmaInvArmaClass> lstArmasEntregaSelect;
    private List<Map> lstArmasEntregaMap;

    private List<Map> lstArmasEntregaMapSelect;

    private Long idTipoBusquedaEntrega1;

//    private Long idEstadoBusqueda;
    private String estadoBusqueda;

    private String filtroEntrega = null;

    private String msjConfirmacionRegistroEntrega1;

    private static Long L_UNO = 1L, L_DOS = 2L, L_TRES = 3L, L_CUATRO = 4L;

    @EJB
    private Rma1369Facade ejbRma1369Facade;
    @EJB
    private AmaInventarioArmaFacade ejbAmaInventarioArmaFacade;
    @EJB
    private SbPersonaFacade ejbSbPersonaFacade;
    @EJB
    private AmaTarjetaPropiedadFacade ejbAmaTarjetaPropiedadFacade;
    @EJB
    private AmaModelosFacade ejbAmaModelosFacade;
    @EJB
    private AmaArmaInventarioDifFacade ejbAmaArmaInventarioDifFacade;
    @EJB
    private AmaArmaFacade ejbAmaArmaFacade;
    @EJB
    private TipoGamacFacade ejbTipoGamacFacade;
    @EJB
    private AmaResolucionFacade ejbAmaResolucionFacade;
    @Inject
    private LoginController loginController;

    public String prepareListArmas() {
        String res = "";
        if (verificarAutorizacionUsuario()) {
            setLstArmasEntregaMap(null);
            RequestContext.getCurrentInstance().update("entregaArmasForm:buscarDatatable");
            registro = null;
            estado = EstadoCrud.VERENTARM;
            res = "/aplicacion/gamac/amaInventarioArma/ListArmas.xhtml";
        } else {
            JsfUtil.mensajeAdvertencia(mensajeValidacion());
        }
        return res;
    }

    public Boolean verificarAutorizacionUsuario() {
        Boolean autorizado = Boolean.FALSE;
        List<Map> lstResolucionRma = ejbRma1369Facade.buscarRDCasaCom(personaUsuario().getRuc() == null ? personaUsuario().getNumDoc() : personaUsuario().getRuc());
        List<AmaResolucion> listResoluciones = ejbAmaResolucionFacade.obtenerResolucionesVigentes(personaUsuario().getRuc());
        if ((lstResolucionRma != null && !lstResolucionRma.isEmpty())
                || (listResoluciones != null && !listResoluciones.isEmpty())) {
            estado = EstadoCrud.USUAUTORIZADO;
            autorizado = Boolean.TRUE;
        } else {
            estado = EstadoCrud.USUNOAUTORIZADO;
        }
        return autorizado;
    }

    public String mensajeValidacion() {
        String msj = personaUsuario().getRznSocial() == null ? JsfUtil.bundleGamac("MensajeUsuarioNatNoAutorizado")
                : JsfUtil.bundleGamac("MensajeUsuarioNoAutorizado1") + " "
                + (personaUsuario().getRznSocial()) + " " + JsfUtil.bundleGamac("MensajeUsuarioNoAutorizado2");
        return msj;
    }

    /**
     * METODO PARA OBTENER LOS DATOS PERSONALES DEL USUARIO LOGUEADO
     *
     * @return Datos personales completos del usuario logueado
     */
    public SbPersona personaUsuario() {
        SbPersona personaUsuario = new SbPersona();
        personaUsuario = ejbSbPersonaFacade.find(loginController.getUsuario().getPersona().getId());
        return personaUsuario;
    }

    /**
     * METODO PARA BUSCAR TARJETAS DE PROPIEDAD DE ARMAS CON SUS RESPECTIVOS
     * ESTADOS
     *
     */
    public void buscarParaEntrega() {
        String tipoBus = "";
        String msjValidacion = "";
        if ((getIdTipoBusquedaEntrega1() != null && (filtroEntrega == null || "".equals(filtroEntrega.trim())))) {
            msjValidacion = JsfUtil.bundleGamac("RequeridoValoresBusqueda3");
        } else if ((getIdTipoBusquedaEntrega1() == null && (filtroEntrega != null && !"".equals(filtroEntrega.trim())))) {
            msjValidacion = JsfUtil.bundleGamac("RequeridoValoresBusqueda4");
        }
        if ("".equals(msjValidacion)) {
            if (getIdTipoBusquedaEntrega1() != null) {
                if (Objects.equals(getIdTipoBusquedaEntrega1(), L_UNO)) {
                    tipoBus = "A";
                } else if (Objects.equals(getIdTipoBusquedaEntrega1(), L_DOS)) {
                    tipoBus = "B";
                } else if (Objects.equals(getIdTipoBusquedaEntrega1(), L_TRES)) {
                    tipoBus = "C";
                }
            }
            if (getIdTipoBusquedaEntrega1() != null && (filtroEntrega == null || "".equals(filtroEntrega))) {
                JsfUtil.mensajeAdvertencia(JsfUtil.bundleGamac("RequeridoTextoFiltro"));
                return;
            }

            List<Map> listInvArmas = ejbAmaInventarioArmaFacade.listArmasRecojoMap(estadoBusqueda, tipoBus, filtroEntrega, personaUsuario().getId());
//            List<Map> listInvArmas = ejbAmaInventarioArmaFacade.listArmasRecojoMap( tipoBus, filtroEntrega, personaUsuario().getId());
            lstArmasEntregaMap = new ArrayList(listInvArmas);
//            if (getIdEstadoBusqueda() != null) {
//                List<Map> listTemp = new ArrayList(lstArmasEntregaMap);
//                switch (getIdEstadoBusqueda().toString()) {
//                    case "1":
//                        for (Map invArma : listTemp) {
//                            if (!Objects.equals((String) invArma.get("ESTADO"), "POR ENTREGAR")) {
//                                lstArmasEntregaMap.remove(invArma);
//                            }
//                        }
//                        break;
//                    case "2":
//                        for (Map invArma : listTemp) {
//                            if (!Objects.equals((String) invArma.get("ESTADO"), "ENTREGADO")) {
//                                lstArmasEntregaMap.remove(invArma);
//                            }
//                        }
//                        break;
//                    case "3":
//                        for (Map invArma : listTemp) {
//                            if (!Objects.equals((String) invArma.get("ESTADO"), "VENCIDO")) {
//                                lstArmasEntregaMap.remove(invArma);
//                            }
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
            RequestContext.getCurrentInstance().update("entregaArmasForm:buscarDatatable");
        } else {
            lstArmasEntregaMap = null;
            JsfUtil.mensajeAdvertencia(msjValidacion);
        }
    }

    /**
     * EVENTO PARA ABRIR FORMULARIO DE CONFIRMACION AL REGISTRAR ENTREGA DE
     * ARMAS
     *
     * @author Gino Chávez
     * @version 2.0
     */
    public void openDlgConfRegistrarEntrega() {
//        Boolean indNoSeleccion = Boolean.FALSE;
        if (!lstArmasEntregaMapSelect.isEmpty()) {
            //Validacion de las armas seleccionadas
            List<Map> listaTemporal = new ArrayList<>();
            listaTemporal.addAll(lstArmasEntregaMapSelect);
            for (Map invArma : listaTemporal) {
                if (!Objects.equals(invArma.get("ESTADO"), "POR ENTREGAR")) {
                    lstArmasEntregaMapSelect.remove(invArma);
                }
            }
            if (lstArmasEntregaMapSelect.isEmpty()) {
                JsfUtil.mensajeError("Realice una selección válida para realizar la entrega de armas.");
                return;
            }
            //Fin validacion
            //Validación, las armas seleccionadas deben pertenecer a un mismo propietario.
            Boolean diferente = Boolean.FALSE;
            String idPropietarioIni = lstArmasEntregaMapSelect.get(0).get("IDPROP").toString();
            String nombrePropietario = lstArmasEntregaMapSelect.get(0).get("PROPIETARIO").toString();
            for (Map ia : lstArmasEntregaMapSelect) {
                if (!Objects.equals(idPropietarioIni, ia.get("IDPROP").toString())) {
                    diferente = Boolean.TRUE;
                    break;
                }
            }
            if (diferente) {
                JsfUtil.mensajeError("Debe seleccionar armas que pertenecen a un mismo propietario.");
                return;
            }
            //Fin validación
            setMsjConfirmacionRegistroEntrega1("¿Confirma que el día de hoy " + ReportUtil.mostrarFechaString(new Date()) + " hace entrega" + (lstArmasEntregaMapSelect.size() > 1 ? " de las " + lstArmasEntregaMapSelect.size() + " armas, cuyo titular es "
                    + nombrePropietario + ", y cuyas características de las armas se detallan " : " del arma, cuyo titular es "
                    + nombrePropietario + ", y cuyas características del arma se detalla ") + "en el siguiente cuadro?");
            RequestContext.getCurrentInstance().execute("PF('ConfRegEntregaViewDialog').show()");
            RequestContext.getCurrentInstance().update("formConfRegEntrega");
        } else {
            JsfUtil.mensajeAdvertencia("Para registrar Entrega de Armas debe seleccionar al menos un registro");
        }
    }

    /**
     * METODO PARA REGISTRAR LA ENTREGA DEL ARMA
     *
     * @author Gino Chávez
     * @version 2.0
     */
    public void registrarEntrega() {
        if (lstArmasEntregaMapSelect != null && !lstArmasEntregaMapSelect.isEmpty()) {
            for (Map invArma : lstArmasEntregaMapSelect) {
                AmaTarjetaPropiedad amaTarjeta = ejbAmaTarjetaPropiedadFacade.obtenerTarjetaPorRuaSerie((String) invArma.get("NRO_RUA"), (String) invArma.get("SERIE"));
                if (amaTarjeta != null) {
                    amaTarjeta.setFechaEntregaPropietario(new Date());
                    actualizarDatosArma(amaTarjeta.getArmaId(), ejbTipoGamacFacade.buscarTipoGamacXCodProg("TP_SITU_POS"));
                    ejbAmaTarjetaPropiedadFacade.edit(amaTarjeta);
                }
            }
            buscarParaEntrega();
            RequestContext.getCurrentInstance().execute("PF('ConfRegEntregaViewDialog').hide()");
            RequestContext.getCurrentInstance().update("formConfRegEntrega");
            RequestContext.getCurrentInstance().update("entregaArmasForm:buscarDatatable");
            JsfUtil.mensaje("Se ha registrado correctamente la Entrega de Armas");
        } else {
            JsfUtil.mensajeError("No es posible registrar Entrega de Armas");
        }
    }

    /**
     * Función que actualiza la condición almacén y la situación del arma en
     * Inventario y en la tabla Ama arma.
     *
     * @author Gino Chávez
     * @param amaArma
     * @param condicion
     * @param situacion
     */
    public void actualizarDatosArma(AmaArma amaArma, TipoGamac situacion) {
        AmaArmaInventarioDif amaInvDif = null;
        if (amaArma != null && situacion != null) {
            amaArma.setSituacionId(situacion);
            ejbAmaArmaFacade.edit(amaArma);
            //Actualización de la situación del arma en la tabla ama_arma
            AmaInventarioArma invArma = ejbAmaInventarioArmaFacade.obtenerArmasPorSerieRua(amaArma.getNroRua(), amaArma.getSerie());
            if (invArma != null) {
                //Fin
                invArma.setObservacion("Arma entregada al propietario en Local Comercial, con fecha " + JsfUtil.dateToString(new Date(), "dd/MM/yyyy") + ".");
                invArma.setSituacionId(situacion);
                invArma = (AmaInventarioArma) JsfUtil.entidadMayusculas(invArma, "");
                ejbAmaInventarioArmaFacade.edit(invArma);
                amaInvDif = ejbAmaArmaInventarioDifFacade.obtenerPorIdInventarioArma(invArma.getId());
                if (amaInvDif != null && amaInvDif.getId() != null) {
                    amaInvDif.setActivo(JsfUtil.FALSE);
                    ejbAmaArmaInventarioDifFacade.edit(amaInvDif);
                }
                amaInvDif = new AmaArmaInventarioDif();
                amaInvDif.setActivo(JsfUtil.TRUE);
                amaInvDif.setFecha(new Date());
                amaInvDif.setInventarioArmaId(invArma);
                amaInvDif.setArmaId(amaArma);
                amaInvDif.setSituacionId(situacion);
                ejbAmaArmaInventarioDifFacade.create(amaInvDif);
            }
        }
    }

    public void limpiarFormEntregaArmas() {
        idTipoBusquedaEntrega1 = null;
        estadoBusqueda = null;
        setFiltroEntrega("");
    }

    /**
     *
     * @param arma
     * @return
     */
    public String obtenerPropietarioActual(AmaInventarioArma arma) {
        if (arma != null) {
            AmaTarjetaPropiedad tarjeta = ejbAmaTarjetaPropiedadFacade.obtenerTarjetaPorRuaSerie(arma.getNroRua(), arma.getSerie());
            if (tarjeta != null && tarjeta.getActivo() == JsfUtil.TRUE) {
                return obtenerDescPersonaGt(tarjeta.getPersonaCompradorId());
            }
        }
        return "";
    }

    /**
     * Función para campo almacén autocompletable
     *
     * @param per Propietario a buscar
     * @return Descripción de propietario (Tipo Doc : Nro. Documento)
     * @author Gino Chávez
     * @version 1.0
     */
    public String obtenerDescPersonaGt(SbPersona per) {
        String nombre = "";

        if (per != null) {
            if (per.getTipoDoc() == null) {
                if (per.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                    nombre = "RUC: " + per.getRuc() + " - " + per.getRznSocial();
                } else {
                    nombre = "DNI: " + per.getNumDoc() + " - " + per.getApePat() + " " + per.getApeMat() + " " + per.getNombres();
                }
            } else {
                nombre = per.getTipoDoc().getNombre() + ": ";
                if (per.getTipoDoc().getCodProg().equals("TP_DOCID_RUC")
                        || per.getTipoDoc().getCodProg().equals("TP_DOCID_EXT")) {
                    nombre += per.getRuc() + " - " + per.getRznSocial();
                } else {
                    nombre += per.getNumDoc() + " - " + per.getApePat() + " " + (per.getApeMat() == null ? "" : per.getApeMat()) + " " + per.getNombres();
                }
            }
        }

        return nombre;
    }

    /**
     * Función para campo almacén autocompletable
     *
     * @param per Propietario a buscar
     * @return Descripción de propietario (Tipo Doc : Nro. Documento)
     * @author Gino Chávez
     * @version 1.0
     */
    public String obtenerDescPersona(SbPersona per) {
        String nombre = "";

        if (per != null) {
            if (per.getTipoDoc() == null) {
                if (per.getTipoId().getCodProg().equals("TP_PER_JUR")) {
                    nombre = "RUC: " + per.getRuc() + " - " + per.getRznSocial();
                } else {
                    nombre = "DNI: " + per.getNumDoc() + " - " + per.getApePat() + " " + per.getApeMat() + " " + per.getNombres();
                }
            } else {
                nombre = per.getTipoDoc().getNombre() + ": ";
                if (per.getTipoDoc().getCodProg().equals("TP_DOCID_RUC")
                        || per.getTipoDoc().getCodProg().equals("TP_DOCID_EXT")) {
                    nombre += per.getRuc() + " - " + per.getRznSocial();
                } else {
                    nombre += per.getNumDoc() + " - " + per.getApePat() + " " + (per.getApeMat() == null ? "" : per.getApeMat()) + " " + per.getNombres();
                }
            }
        }

        return nombre;
    }

    public String obtenerCalibres(AmaModelos mod) {
        if (mod != null) {
            String r = "";
            String separador = "/";
            int tam = mod.getAmaCatalogoList().size();
            for (AmaCatalogo amaCat : mod.getAmaCatalogoList()) {
                if (tam > 1) {
                    r += amaCat.getNombre() + separador;
                } else {
                    r += amaCat.getNombre();
                }
                tam--;
            }
            return r;
        }
        return null;
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
    public AmaInventarioArma getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(AmaInventarioArma registro) {
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
     * Propiedad para el estado del CRUD.
     *
     * @return el estado actual del CRUD.
     */
    public EstadoCrud getEstado() {
        return estado;
    }

    /**
     * Propiedad para los resultados de la búsqueda del CRUD.
     *
     * @return Los resultados de la búsqueda.
     */
    public ListDataModel<AmaInventarioArma> getResultados() {
        return resultados;
    }

    /**
     * Constructor
     *
     */
    public AmaInventarioArmaController() {
//        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public AmaInventarioArma getAmaInventarioArma(Long id) {
        return ejbAmaInventarioArmaFacade.find(id);
    }

    public Long getIdTipoBusquedaEntrega1() {
        return idTipoBusquedaEntrega1;
    }

    public void setIdTipoBusquedaEntrega1(Long idTipoBusquedaEntrega1) {
        this.idTipoBusquedaEntrega1 = idTipoBusquedaEntrega1;
    }

    public String getFiltroEntrega() {
        return filtroEntrega;
    }

    public void setFiltroEntrega(String filtroEntrega) {
        this.filtroEntrega = filtroEntrega;
    }

    public String getMsjConfirmacionRegistroEntrega1() {
        return msjConfirmacionRegistroEntrega1;
    }

    public void setMsjConfirmacionRegistroEntrega1(String msjConfirmacionRegistroEntrega1) {
        this.msjConfirmacionRegistroEntrega1 = msjConfirmacionRegistroEntrega1;
    }

    public List<Map> getLstArmasEntregaMap() {
        return lstArmasEntregaMap;
    }

    public void setLstArmasEntregaMap(List<Map> lstArmasEntregaMap) {
        this.lstArmasEntregaMap = lstArmasEntregaMap;
    }

    public List<Map> getLstArmasEntregaMapSelect() {
        return lstArmasEntregaMapSelect;
    }

    public void setLstArmasEntregaMapSelect(List<Map> lstArmasEntregaMapSelect) {
        this.lstArmasEntregaMapSelect = lstArmasEntregaMapSelect;
    }

    public void setEstado(EstadoCrud estado) {
        this.estado = estado;
    }

    public String getEstadoBusqueda() {
        return estadoBusqueda;
    }

    public void setEstadoBusqueda(String estadoBusqueda) {
        this.estadoBusqueda = estadoBusqueda;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "amaInventarioArmaConverter")
    public static class AmaInventarioArmaControllerConverterN extends AmaInventarioArmaControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = AmaInventarioArma.class)
    public static class AmaInventarioArmaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AmaInventarioArmaController c = (AmaInventarioArmaController) JsfUtil.obtenerBean("amaInventarioArmaController", AmaInventarioArmaController.class);
            return c.getAmaInventarioArma(Long.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AmaInventarioArma) {
                AmaInventarioArma o = (AmaInventarioArma) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + AmaInventarioArma.class.getName());
            }
        }
    }

}
