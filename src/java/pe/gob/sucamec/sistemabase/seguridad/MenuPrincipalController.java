/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.seguridad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import pe.gob.sucamec.sistemabase.beans.SbPaginaFacade;
import pe.gob.sucamec.sistemabase.data.SbPagina;
import pe.gob.sucamec.sistemabase.data.SbPerfil;

/**
 *
 * @author Renato
 */
@Named(value = "menuPrincipalController")
@SessionScoped
public class MenuPrincipalController implements Serializable {

    /**
     * Creates a new instance of MenuNavegacionController
     */
    private DefaultMenuModel model = null;

    @EJB
    SbPaginaFacade paginaFacade;

    @Inject
    LoginController loginController;

    private String datos;

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public MenuPrincipalController() {
    }

    public ArrayList<String> perfilesToArray(List<SbPerfil> perfiles) {
        if (perfiles == null) {
            return null;
        }
        ArrayList<String> r = new ArrayList();
        for (SbPerfil p : perfiles) {
            r.add(p.getCodProg());
        }
        return r.isEmpty() ? null : r;
    }

    public DefaultMenuModel getModel() {
        // Generar el menu solo una vez x sesion para que no impacte en la performance//
        if (model == null) {
            model = new DefaultMenuModel(); // No debe devolver nulo //
            datos = "";
            List<SbPagina> categorias = paginaFacade.selectCategoriasMenu(loginController.getSistemaId());
            for (SbPagina c : categorias) {
                boolean accesoc = true;
                ArrayList<String> lpc = perfilesToArray(c.getSbPerfilList());
                if (lpc != null && !loginController.getUsuario().tienePerfiles(lpc)) {
                    accesoc = false;
                }
                if (accesoc) {
                    DefaultSubMenu sm = menu_recursivo(c);
                    if (sm.getElementsCount() > 0) {
                        model.addElement(sm);
                    }
                }
            }
            model.generateUniqueIds();
        }
        return model;
    }

    public DefaultSubMenu menu_recursivo(SbPagina c) {
        DefaultSubMenu sm = new DefaultSubMenu(c.getNombre());
        List<SbPagina> items = paginaFacade.selectItemsCategoriasMenu(c.getId());
        for (SbPagina i : items) {
            boolean accesoi = true;
            ArrayList<String> lp = perfilesToArray(i.getSbPerfilList());
            if (lp != null && !loginController.getUsuario().tienePerfiles(lp)) {
                accesoi = false;
            }
            if (accesoi) {
                if (i.getSbPaginaList() != null && !i.getSbPaginaList().isEmpty()) {
                    DefaultSubMenu sm2 = menu_recursivo(i);
                    sm2.setStyle("background-color: #CEECF5;margin-left:12px;");
                    sm.addElement(sm2);
                } else {
                    DefaultMenuItem m = new DefaultMenuItem(i.getNombre());
                    m.setStyle("background-color: #FFFFFF;margin-left:-20px;");
                    m.setAjax(false);
                    String url = i.getUrl().trim(), funcion = i.getFuncion();
                    funcion = funcion == null ? "" : funcion.trim();
                    if (funcion.isEmpty()) {
                        m.setHref(url);
                    } else {
                        m.setCommand(funcion);
                    }
                    sm.addElement(m);
                }
            }
        }
        return sm;
    }

    public void setModel(DefaultMenuModel model) {
        this.model = model;
    }

}
