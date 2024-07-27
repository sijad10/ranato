package pe.gob.sucamec.sel.citas.jsf;

import pe.gob.sucamec.sel.citas.jsf.util.JsfUtil;
import pe.gob.sucamec.sel.citas.jsf.util.EstadoCrud;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import pe.gob.sucamec.sel.citas.data.CitaTipoGamac;
//import pe.gob.sucamec.sel.ctias.jsf.util.UploadFilesController;
import pe.gob.sucamec.sistemabase.data.SbPerfil;
import pe.gob.sucamec.sistemabase.data.SbUsuario;

// Nombre de la instancia en la aplicacion //
@Named("citasGestionController")
@SessionScoped

/**
 * Clase con TurTurnoController instanciada como turTurnoController. Contiene
 * funciones utiles para la entidad TurTurno. Esta vinculada a las páginas
 * TurTurno/create.xhtml, TurTurno/update.xhtml, TurTurno/list.xhtml,
 * TurTurno/view.xhtml Nota: Las tablas deben tener la estructura de Sucamec
 * para que funcione adecuadamente, revisar si tiene el campo activo y modificar
 * las búsquedas.
 */
public class CitasGestionController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud opcion;
    boolean permiso;
    //TabView tabviewGest =null;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro;

    private List<CitaTipoGamac> listaTramites;
    private CitaTipoGamac tipoTramite;

    @Inject
    CitasValidacionController citasValidacionController;
    @Inject
    CitasPoligonoController citasPoligonoController;
    @Inject
    CitasPoligonoJurController citasPoligonoJurController;
    @Inject
    CitasVerificacionController citasVerificacionController;
    @Inject
    CitasVerificacionJurController citasVerificacionJurController;
    @Inject
    CitasTramiteController citasTramiteController;
    @Inject
    CitasTramiteController citasTramiteJurController;
    @Inject
    CitasEmpadronamientoController citasEmpadronamientoController;

    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbUsuarioFacade ejbSbUsuarioFacade;
    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTipoGamacFacade ejbTipoGamacFacade;

    public List<CitaTipoGamac> getListaTramites() {
        listaTramites = ejbTipoGamacFacade.lstTipoTramite();
        return listaTramites;
    }

    public void setListaTramites(List<CitaTipoGamac> listaTramites) {
        this.listaTramites = listaTramites;
    }

    public CitaTipoGamac getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(CitaTipoGamac tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    /*public TabView getTabviewGest() {
    return tabviewGest;
    }
    public void setTabviewGest(TabView tabviewGest) {
    this.tabviewGest = tabviewGest;
    }*/
    public boolean isPermiso() {
        return permiso;
    }

    public void setPermiso(boolean permiso) {
        this.permiso = permiso;
    }

    /**
     * Propiedad para el estado del CRUD.
     *
     * @return el estado actual del CRUD.
     */
    public EstadoCrud getOpcion() {
        return opcion;
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String prepareModulo() {
        restaurarBandeja();
        return "/aplicacion/citas/GestionCitas";
    }

    public void restaurarBandeja() {
        tipoTramite = null;
    }

    public void onTabChange(TabChangeEvent event) {
        String tab = event.getTab().getId().toString();
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("$(\"a[href*='tab0'], a[href*='tab1'], a[href*='tab2'], a[href*='tab3'], a[href*='tab4'], a[href*='tab5'], a[href*='tab6'], a[href*='tab7']\").parent().hide(\"fast\");");
        //Syso("tab: " + tab);        

        switch (tab) {
            case "tab0":
                if (!permiso) {
                    JsfUtil.mensajeError("No tiene permisos para esta opción!");
                }
                opcion = null;
                context.update("tabGestion");
                context.execute("selectTab0()");
                permiso = true;
                break;
            case "tab1":
                if (validarPerNat()) {
                    opcion = EstadoCrud.CITAVAL;
                    context.execute("selectTab1();");
                    context.update("tabGestion:crearCitaValForm");
                    citasValidacionController.prepareModulo();
                    permiso = true;
                } else {
                    permiso = false;
                }
                break;
            case "tab2":
                if (validarPerNat()) {
                    opcion = EstadoCrud.CITAPOL;
                    citasPoligonoController.mostrarProgramarTurno();
                    context.execute("selectTab2();");
                    context.update("tabGestion:creaCitaPolForm");
                    permiso = true;
                } else {
                    permiso = false;
                }
                break;
            case "tab3":
                if (validarPerJur()) {
                    opcion = EstadoCrud.CITAPOL;
                    citasPoligonoJurController.mostrarProgramarTurno();
                    context.execute("selectTab3();");
                    context.update("tabGestion:creaCitaPolJurForm");
                    permiso = true;
                } else {
                    permiso = false;
                }
                break;
            case "tab4":
                if (validarPerNat()) {
                    opcion = EstadoCrud.CITAVER;
                    citasVerificacionController.prepareModulo();
                    context.execute("selectTab4();");
                    context.update("tabGestion:creaCitaVerForm");
                    permiso = true;
                } else {
                    permiso = false;
                }
                break;
            case "tab5":
                if (validarPerJur()) {
                    opcion = EstadoCrud.CITAVER;
                    citasVerificacionJurController.prepareModulo();
                    context.execute("selectTab5();");
                    context.update("tabGestion:creaCitaVerJurForm");
                    permiso = true;
                } else {
                    permiso = false;
                }
                break;
            case "tab6":
                if (validarPerNat() || validarPerJur()) {
                    opcion = EstadoCrud.CITATRAM;
                    citasTramiteController.mostrarProgramarTurnoTramite();
                    context.execute("selectTab6();");
                    context.update("tabGestion:creaCitaTramForm");
                    
                    permiso = true;
                } else {
                    permiso = false;
                }
                break;
//            case "tab7":
//                if (validarPerJur()) {
//                    opcion = EstadoCrud.CITATRAM;
//                    citasTramiteJurController.mostrarProgramarTurnoTramite();
//                    context.execute("selectTab7();");
//                    context.update("tabGestion:creaCitaTramJurForm");
//                    permiso = true;
//                } else {
//                    permiso = false;
//                }
//                break;
            case "tab7":
                /*if (validarPerNat() || validarPerJur()) {
                    opcion = EstadoCrud.CITAEMPADRONAMIENTO;
                    citasEmpadronamientoController.prepareModulo();
                    context.execute("selectTab7();");
                    context.update("tabGestion:crearCitaEmpadronaVerForm");
                    permiso = true;
                } else {
                    permiso = false;
                }*/
                //Por defecto no tiene permiso
                //SOlo puede utilizar como persona natural DNI/CE
                //Solo RUC pero como persona Juridica de 20 no de 10
                permiso = false;    
                //Cuando el usuario se ha logurado con RUC
                if (JsfUtil.getLoggedUser().getTipoDoc().equals("RUC")) {
                    //Ventana como Persona Juridica o tenga RUC similar a la Verificación de Armas
                    //context.execute("PF('wvTabGestion').select(5);");
                    if (validarPerJur()) {
                        permiso = true;
                    } else {
                        permiso = false;
                    }
                } else {
                    //Ventana como Persona Natural 
                    //context.execute("PF('wvTabGestion').select(4);");
                    if (validarPerNat()) {
                        permiso = true;
                    } else {
                        permiso = false;
                    }
                }
                
                if (permiso) {
                    opcion = EstadoCrud.CITAEMPADRONAMIENTO;
                    citasEmpadronamientoController.prepareModulo();
                    context.execute("selectTab7();");
                    context.update("tabGestion:crearCitaEmpadronaVerForm");
                }
                                    
                break;
        }

        context.execute("$(\".panelTab\").children(\".ui-panel-content\").attr(\"style\", \"padding:2px!important\")");
        //JsfUtil.mensaje("Active Tab: " + tab);
        validaPermiso();
    }
    public void seleccionarSubTramite() {
        
    }
    public void seleccionarTramite() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (tipoTramite != null) {
            switch (tipoTramite.getCodProg()) {
                case "TP_TRAM_VAL":
                    context.execute("PF('wvTabGestion').select(1);");
                    /*if (validarPerNat()) {
                        context.execute("PF('wvTabGestion').select(1);");
                        permiso = true;
                    } else {
                        permiso = false;
                    }*/
                    break;

                case "TP_TRAM_POL":
                    if (JsfUtil.getLoggedUser().getTipoDoc().equals("RUC")) {
                        //if (validarPerJur()) {
                        context.execute("PF('wvTabGestion').select(3);");
                        //break;
                        //}
                        /*if (validarPerNat()) {
                            opcion = EstadoCrud.CITAPOL;
                            context.execute("PF('wvTabGestion').select(2);");
                            permiso = true;
                        } else {
                            permiso = false;
                        }*/
                    } else {
                        //if (validarPerNat()) {
                        context.execute("PF('wvTabGestion').select(2);");
                        //break;
                        //}
                    }
                    break;
                case "TP_TRAM_VER":
                    if (JsfUtil.getLoggedUser().getTipoDoc().equals("RUC")) {
                        context.execute("PF('wvTabGestion').select(5);");
                    } else {
                        context.execute("PF('wvTabGestion').select(4);");
                    }
                    break;
                // SE REMPLAZO TP_TRAM_RECPOR TP_TRAM_TD   
                case "TP_TRAM_TD":                                         
                        context.execute("PF('wvTabGestion').select(6);");
                        //tipoTramite=null;                    
                    break;
                case "TP_TRAM_EMP":
                    context.execute("PF('wvTabGestion').select(7);");
                    /*if (JsfUtil.getLoggedUser().getTipoDoc().equals("RUC")) {
                        context.execute("PF('wvTabGestion').select(7);");
                    } else {
                        context.execute("PF('wvTabGestion').select(7);");
                    }*/
                    break;                         
            }
        } else {
            //Aqui procesa 
            
        }

        context.update("gestionCitasHelp");
    }

    public void restaurarCitasVal() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('wvTabGestion').select(1);");
    }

    public void restaurarCitasPol() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('wvTabGestion').select(2);");
    }

    public void restaurarCitasPolJur() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('wvTabGestion').select(3);");
    }

    public void restaurarCitasVer() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('wvTabGestion').select(4);");
    }

    public void restaurarCitasVerJur() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('wvTabGestion').select(5);");
    }
    public void restaurarCitasTram() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('wvTabGestion').select(6);");
    }

    public void restaurarCitasEmpadronamiento() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('wvTabGestion').select(7);");
    }
    
    public void validaPermiso() {
        if (!permiso) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('wvTabGestion').select(0);");
            //tabviewGest.setActiveIndex(0);
            //tabviewGest.getOnTabChange();                    
        }
    }

    public boolean validarEvaluador() {
        boolean flagValidar = false;
        SbUsuario usu = ejbSbUsuarioFacade.selectUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()).get(0);
        for (SbPerfil perfil : usu.getSbPerfilList()) {
            if (perfil.getCodProg().equals("AMA_EVAL")) {
                flagValidar = true;
            }
        }
        return flagValidar;
    }

    public boolean validarCoordinador() {
        boolean flagValidar = false;
        SbUsuario usu = ejbSbUsuarioFacade.selectUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()).get(0);
        for (SbPerfil perfil : usu.getSbPerfilList()) {
            if (perfil.getCodProg().equals("AMA_COOR")) {
                flagValidar = true;
            }
        }
        return flagValidar;
    }

    public boolean validarCoordinadorLic() {
        boolean flagValidar = false;
        SbUsuario usu = ejbSbUsuarioFacade.selectUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()).get(0);
        for (SbPerfil perfil : usu.getSbPerfilList()) {
            if (perfil.getCodProg().equals("AMA_CANC")) {
                flagValidar = true;
            }
        }
        return flagValidar;
    }

    public boolean validarPerNat() {
        boolean flagValidar = false;
        //SbUsuario usu = ejbSbUsuarioFacade.selectUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()).get(0);
        SbUsuario usu = ejbSbUsuarioFacade.selectUsuarioXId(JsfUtil.getLoggedUser().getId());
        if (usu != null) {
            if (usu.getPersonaId().getTipoId().getCodProg().equals("TP_PER_NAT")) {
                flagValidar = true;
            }
        }
        return flagValidar;
    }

    public boolean validarPerJur() {
        boolean flagValidar = false;
        //SbUsuario usu = ejbSbUsuarioFacade.selectUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()).get(0);
        SbUsuario usu = ejbSbUsuarioFacade.selectUsuarioXId(JsfUtil.getLoggedUser().getId());
        if (usu != null) {
            if (usu.getPersonaId().getTipoId().getCodProg().equals("TP_PER_JUR")) {
                flagValidar = true;
            }
        }
        return flagValidar;
    }

    public boolean validarAccesoCitPol() {
        boolean flagValidar = false;
        SbUsuario usu = ejbSbUsuarioFacade.selectUsuarioXLogin(JsfUtil.getLoggedUser().getLogin()).get(0);
        List<String> sList = Arrays.asList("TP_PER_NAT", "TP_PER_JUR");
        if (sList.contains(usu.getPersonaId().getTipoId().getCodProg())) {
            flagValidar = true;
        }
        return flagValidar;
    }

    public CitasGestionController() {
    }
}
