package pe.gob.sucamec.sistemabase.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import pe.gob.sucamec.sistemabase.beans.SbPerfilFacade;
import pe.gob.sucamec.sistemabase.beans.SbTipoFacade;
import pe.gob.sucamec.sistemabase.data.SbPerfil;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.sistemabase.jsf.util.EstadoCrud;
import pe.gob.sucamec.sistemabase.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

// Nombre de la instancia en la aplicacion //
@Named("sbUsuarioController")
@SessionScoped

/**
 * Clase con SbUsuarioController instanciada como sbUsuarioController. Contiene
 * funciones utiles para la entidad SbUsuario. Esta vinculada a las páginas
 * SbUsuario/create.xhtml, SbUsuario/update.xhtml, SbUsuario/list.xhtml,
 * SbUsuario/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec
 * para que funcione adecuadamente, revisar si tiene el campo activo y modificar
 * las búsquedas.
 */
public class SbUsuarioController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro, clave2, cambiarClave, cambiarClave2, originalLogin;

    /**
     * Registro actual para editar o ver.
     */
    SbUsuario registro;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<SbUsuario> resultados = null;

    /**
     * Lista de areas para seleccion multiple
     */
    List<SbUsuario> registrosSeleccionados;
    
    private List<SbPerfil> perfilesPadre;
    private List<SbPerfil> listPerfilesSelect;
    private Boolean disablePerfil;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade sbUsuarioFacade;
    @EJB
    SbTipoFacade sbTipoFacade;
    @EJB
    SbPerfilFacade sbPerfilFacade;
    
    @Inject
    private LoginController loginController;
    
    public String getOriginalLogin() {
        return originalLogin;
    }
    
    public void setOriginalLogin(String originalLogin) {
        this.originalLogin = originalLogin;
    }
    
    public String getCambiarClave() {
        return cambiarClave;
    }
    
    public void setCambiarClave(String cambiarClave) {
        this.cambiarClave = cambiarClave;
    }
    
    public String getCambiarClave2() {
        return cambiarClave2;
    }
    
    public void setCambiarClave2(String cambiarClave2) {
        this.cambiarClave2 = cambiarClave2;
    }
    
    public String getClave2() {
        return clave2;
    }
    
    public void setClave2(String clave2) {
        this.clave2 = clave2;
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
     * Propiedad de seleccion multiple
     *
     * @return
     */
    public List<SbUsuario> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    /**
     * Propiedad de seleccion multiple
     *
     * @param a
     */
    public void setRegistrosSeleccionados(List<SbUsuario> a) {
        this.registrosSeleccionados = a;
    }

    /**
     * Anula multiples registros, usar como base para acciones de multiples
     * registros.
     */
    public void anularMultiple() {
        if ((registrosSeleccionados == null) || registrosSeleccionados.isEmpty()) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorSeleccion"));
            return;
        }
        try {
            for (SbUsuario a : registrosSeleccionados) {
                if (a.getTipoId().getCodProg().equals("TP_USR_EXT")) {
                    a.setActivo((short) 0);
                    sbUsuarioFacade.edit(a);
                } else {
                    JsfUtil.mensaje(JsfUtil.bundle("MensajeNoAnularUsuarioMa"));
                }
            }
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Activa multiples registros, usar como base para acciones de multiples
     * registros.
     */
    public void activarMultiple() {
        if ((registrosSeleccionados == null) || registrosSeleccionados.isEmpty()) {
            JsfUtil.mensajeError(JsfUtil.bundle("ErrorSeleccion"));
            return;
        }
        try {
            long maxUsr = loginController.getUsuario().getCantidadUsuario();
            if ((sbUsuarioFacade.cantidadUsuarios(loginController.getUsuario().getId()) + registrosSeleccionados.size()) >= (maxUsr + 1)) {
                JsfUtil.mensajeError("Solo puede activar hasta " + maxUsr + " usuarios.");
            } else {
                for (SbUsuario a : registrosSeleccionados) {
                    if (a.getTipoId().getCodProg().equals("TP_USR_EXT")) {
                        a.setActivo((short) 1);
                        sbUsuarioFacade.edit(a);
                    } else {
                        JsfUtil.mensaje(JsfUtil.bundle("MensajeNoActivarUsuarioMa"));
                    }
                }
                JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistrosActualizados"));
            }
            
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a activo en un
     * registro de una lista.
     *
     */
    public void activar() {
        try {
            registro = (SbUsuario) resultados.getRowData();
            registro.setActivo((short) 1);
            sbUsuarioFacade.edit(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento tipo 'actionListener' para cambiar el estado a anulado en un
     * registro de una lista.
     *
     */
    public void anular() {
        try {
            registro = (SbUsuario) resultados.getRowData();
            registro.setActivo((short) 0);
            sbUsuarioFacade.edit(registro);
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public SbUsuario getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(SbUsuario registro) {
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
    public ListDataModel<SbUsuario> getResultados() {
        return resultados;
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        mostrarBuscar();
        obtenerPerfiles();
        return "/aplicacion/configuracion/usuarios/List";
    }
    
    public void obtenerPerfiles() {
        perfilesPadre = objetoUsuario().getSbPerfilList();
    }
    
    public void validarUsuario() {
        if (registro != null && registro.getLogin() != null) {
            for (int i = 0; i < registro.getLogin().length(); i++) {
                if (Character.isSpaceChar(registro.getLogin().charAt(i))) {
                    registro.setLogin(null);
                    JsfUtil.mensajeAdvertencia("El campo usuario no debe contener espacios.");
                    break;
                }
            }
        }
    }
    
    public SbUsuario objetoUsuario() {
        List<SbUsuario> listRes = sbUsuarioFacade.selectUsuarioXLogin(loginController.getUsuario().getLogin());
        List<SbUsuario> listTemp = new ArrayList<>();
        SbUsuario usu = new SbUsuario();
        listTemp.addAll(listRes);
        for (SbUsuario u : listTemp) {
            if (!u.getPersonaId().getId().equals(loginController.getUsuario().getPersona().getId())) {
                listRes.remove(u);
            }
        }
        if (!listRes.isEmpty()) {
            usu = listRes.get(0);
        }
        return usu;
    }

    /**
     * Devuelve una lista básica de items para un selectOneMenu, debe
     * implementarse en cada controller de acuerdo a los casos de uso.
     *
     * @return Lista de items para un selectOneMenu
     */
    public List<SbUsuario> getSelectItems() {
        return sbUsuarioFacade.findAll();
    }
    
    public void mostrarCambiarClave() {
        registro = (SbUsuario) resultados.getRowData();
        cambiarClave = "";
        cambiarClave2 = "";
    }
    
    public void guardarClave() {
        try {
            boolean error = false;
            if (!cambiarClave.equals(cambiarClave2)) {
                JsfUtil.mensajeError("Debe ingresar la misma clave dos veces.");
                JsfUtil.invalidar("crearForm:clave_nuevo");
                error = true;
            }
            if (cambiarClave.length() < 8 || cambiarClave.contains(" ")) {
                JsfUtil.mensajeError("Error la clave debe tener al menos 8 caracteres y no tener espacios.");
                JsfUtil.invalidar("crearForm:clave_nuevo");
                error = true;
            }
            if (error) {
                cambiarClave = "";
                cambiarClave2 = "";
                return;
            }
            registro.setClave(JsfUtil.crearHash(cambiarClave));
            sbUsuarioFacade.edit(registro);
            JsfUtil.mensaje(JsfUtil.bundle("CambioClaveExitosa"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
        
    }

    /**
     * Cambia el estado del controller a buscar
     */
    public void mostrarBuscar() {
        if (loginController.getUsuario().getTipo().equals("TP_USR_MA")) {
            resultados = null;
            registrosSeleccionados = null;
            listPerfilesSelect = null;
            disablePerfil = Boolean.FALSE;
            estado = EstadoCrud.BUSCAR;
        } else {
            estado = null;
            JsfUtil.mensajeAdvertencia("Usted no tiene permisos para visualizar esta opción.");
        }
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
        resultados = new ListDataModel(sbUsuarioFacade.selectLike(filtro, loginController.getUsuario().getId()));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (SbUsuario) resultados.getRowData();
        estado = EstadoCrud.VER;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para editar un
     * registro
     */
    public void mostrarEditar() {
        registro = (SbUsuario) resultados.getRowData();
        //listPerfilesSelect=new ArrayList<>();
        if ("TP_USR_MA".equals(registro.getTipoId().getCodProg())) {
            disablePerfil = Boolean.TRUE;
        }
        listPerfilesSelect = registro.getSbPerfilList();
        originalLogin = registro.getLogin();
        estado = EstadoCrud.EDITAR;
    }
    
    public String mostrarMensajePerfil() {
        String res = "";
        if (listPerfilesSelect != null && !listPerfilesSelect.isEmpty()) {
            for (SbPerfil per : listPerfilesSelect) {
                switch (per.getCodProg()) {
                    case "AMA_CSALUD":
                        res += "<b><big>" + obtenerNombrePer(per.getNombre()) + ": </big></b>" + JsfUtil.bundle("MensajeCentroSalud") + "<br>";
                        break;
                    case "SEL_NOTIF":
                        res += "<b><big>" + obtenerNombrePer(per.getNombre()) + ": </big></b>" + JsfUtil.bundle("MensajeNotificaciones") + "<br>";
                        break;
                    case "SEL_PJ":
                        res += "<b><big>" + obtenerNombrePer(per.getNombre()) + ": </big></b>" + JsfUtil.bundle("MensajePj") + "<br>";
                        break;
                    case "SEL_PNPCON":
                        res += "<b><big>" + obtenerNombrePer(per.getNombre()) + ": </big></b>" + JsfUtil.bundle("MensajePnp") + "<br>";
                        break;
                    default:
                        break;
                }
            }
        }
        return res;
    }

    /**
     * Función tipo 'actionListener' que muestra el formulario para crear un
     * registro
     */
    public void mostrarCrear() {
        long maxUsr = loginController.getUsuario().getCantidadUsuario();
        if (sbUsuarioFacade.cantidadUsuarios(loginController.getUsuario().getId()) >= maxUsr) {
            mostrarBuscar();
            JsfUtil.mensajeError("Solo puede ingresar hasta " + maxUsr + " usuarios.");
            return;
        }
        disablePerfil = Boolean.FALSE;
        estado = EstadoCrud.CREAR;
        registro = new SbUsuario();
        registro.setActivo((short) 1);
    }

    /**
     * Evento para guardar la información al editar un registro.
     *
     */
    public void editar() {
        try {
            String pass = registro.getClave();
            registro = (SbUsuario) JsfUtil.entidadMayusculas(registro, "");
            registro.setClave(pass);
            registro.setSbPerfilList(new ArrayList(listPerfilesSelect));
            sbUsuarioFacade.edit(registro);
            estado = EstadoCrud.BUSCAR;
            JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroActualizado"));
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }

    /**
     * Evento para guardar la información al crear un nuevo registro.
     *
     */
    public void crear() {
        try {
            if (listPerfilesSelect != null && !listPerfilesSelect.isEmpty()) {
                boolean error = false;
                if (!clave2.equals(registro.getClave())) {
                    JsfUtil.mensajeError("Debe ingresar la misma clave dos veces.");
                    JsfUtil.invalidar("crearForm:clave_nuevo");
                    error = true;
                }
                if (clave2.length() < 8 || clave2.contains(" ")) {
                    JsfUtil.mensajeError("Error la clave debe tener al menos 8 caracteres y no tener espacios.");
                    JsfUtil.invalidar("crearForm:clave_nuevo");
                    error = true;
                }
                if (sbUsuarioFacade.validaLogin(registro.getLogin())) {
                    JsfUtil.mensajeError("Error el Login ingresado ya existe.");
                    JsfUtil.invalidar("crearForm:login");
                    error = true;
                }
                if (error) {
                    clave2 = "";
                    registro.setClave("");
                } else {
                    registro = (SbUsuario) JsfUtil.entidadMayusculas(registro, "clave");
                    registro.setId(null);
                    registro.setClave(JsfUtil.crearHash(registro.getClave()));
                    registro.setFechaIni(new Date());
                    registro.setPersonaId(loginController.getUsuario().getPersona());
                    registro.setUsuarioId(new SbUsuario(loginController.getUsuario().getId()));
                    registro.setTipoAutenId(sbTipoFacade.tipoPorCodProg("TP_AUT_BD"));
                    registro.setTipoId(sbTipoFacade.tipoPorCodProg("TP_USR_EXT"));
                    registro.setSbPerfilList(listPerfilesSelect);
                    registro.setCantidadUsuario(0L);
                    registro.setActivo((short) 1);
                    sbUsuarioFacade.create(registro);
                    JsfUtil.mensaje(JsfUtil.bundle("MensajeRegistroCreado"));
                    mostrarBuscar();
                }
            } else {
                JsfUtil.mensajeError("Debe seleccionar al menos un perfil.");
            }
            
        } catch (Exception e) {
            JsfUtil.mensajeError(e, JsfUtil.bundle("ErrorDePersistencia"));
        }
    }
    
    public String obtenerNombrePer(String cadena) {
        String res = "";
        if (cadena != null && !"".equals(cadena.trim())) {
            res = cadena.substring(4, cadena.length());
        }
        return res;
    }

    /**
     * Función para que el converter obtenga a la entidad.
     *
     * @param id ID de la entidad
     * @return Devuelve la entidad con los datos de la base de datos.
     */
    public SbUsuario getSbUsuario(Long id) {
        return sbUsuarioFacade.find(id);
    }

    /**
     * Constructor
     *
     */
    public SbUsuarioController() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * @return the perfilesPadre
     */
    public List<SbPerfil> getPerfilesPadre() {
        return perfilesPadre;
    }

    /**
     * Obtiene una lista de Perfil para un selectOneMenu con los datos del
     * controller, sin opción de nulos.
     *
     * @return Lista de SelectItems para un selectOneMenu o SelectList.
     */
    public SelectItem[] getItemsPerfilesPadre() {
        return JsfUtil.getSelectItems(perfilesPadre, false);
    }

    /**
     * @param perfilesPadre the perfilesPadre to set
     */
    public void setPerfilesPadre(List<SbPerfil> perfilesPadre) {
        this.perfilesPadre = perfilesPadre;
    }

    /**
     * @return the listPerfilesSelect
     */
    public List<SbPerfil> getListPerfilesSelect() {
        return listPerfilesSelect;
    }

    /**
     * @param listPerfilesSelect the listPerfilesSelect to set
     */
    public void setListPerfilesSelect(List<SbPerfil> listPerfilesSelect) {
        this.listPerfilesSelect = listPerfilesSelect;
    }

    /**
     * @return the disablePerfil
     */
    public Boolean getDisablePerfil() {
        return disablePerfil;
    }

    /**
     * @param disablePerfil the disablePerfil to set
     */
    public void setDisablePerfil(Boolean disablePerfil) {
        this.disablePerfil = disablePerfil;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
    @FacesConverter(value = "sbUsuarioConverter")
    public static class SbUsuarioControllerConverterN extends SbUsuarioControllerConverter {
    }

    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     */
    @FacesConverter(forClass = SbUsuario.class)
    public static class SbUsuarioControllerConverter implements Converter {
        
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SbUsuarioController c = (SbUsuarioController) JsfUtil.obtenerBean("sbUsuarioController", SbUsuarioController.class);
            return c.getSbUsuario(Long.valueOf(value));
        }
        
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof SbUsuario) {
                SbUsuario o = (SbUsuario) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + SbUsuario.class.getName());
            }
        }
    }
    
    public boolean renderDescripcion() {
        if (registro.getTipoId().getCodProg().equals("TP_USR_MA")) {
            return false;
        }
        return true;
    }
    
    public SbUsuario getUsuario(String u) {
        List<SbUsuario> r = sbUsuarioFacade.selectUsuario(u);
        if (r.isEmpty()) {
            return null;
        } else {
            return r.get(0);
        }
    }
}
