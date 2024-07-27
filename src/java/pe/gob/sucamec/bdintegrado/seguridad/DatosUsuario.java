/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.seguridad;

import java.util.ArrayList;

/**
 *
 * @author Renato
 */
public class DatosUsuario {

    private ArrayList<String> perfiles;
    private String login, descripcion, nombres, apePat, apeMat, correo, hashClave, tipo, tipoAuten;
    private long id, areaId;
    private boolean logeado;
    private Long sistema;

    public Long getSistema() {
        return sistema;
    }

    public void setSistema(Long sistema) {
        this.sistema = sistema;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoAuten() {
        return tipoAuten;
    }

    public void setTipoAuten(String tipoAuten) {
        this.tipoAuten = tipoAuten;
    }

    public String getHashClave() {
        return hashClave;
    }

    public void setHashClave(String hashClave) {
        this.hashClave = hashClave;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public boolean isLogeado() {
        return logeado;
    }

    public void setLogeado(boolean logeado) {
        this.logeado = logeado;
    }

    public String getPerfilesAsString() {
        if (perfiles == null || perfiles.isEmpty()) {
            return "";
        }
        String r = "";
        for (String p : perfiles) {
            r += ", '"+p.replace("'", "''")+"'";
        }
        r = r.substring(2);
        return r;
    }

    public ArrayList<String> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(ArrayList<String> perfiles) {
        this.perfiles = perfiles;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean tienePerfil(String perfil) {
        return perfiles.contains(perfil);
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean tienePerfiles(ArrayList<String> listaPerfiles) {
        if (listaPerfiles == null) {
            return false;
        }
        for (String lr : listaPerfiles) {
            if (perfiles.contains(lr)) {
                return true;
            }
        }
        return false;
    }

    public DatosUsuario(String usr, String desc) {
        inicializar(usr, desc, null);
    }

    public DatosUsuario(String usr, String desc, ArrayList<String> listaPerfiles) {
        inicializar(usr, desc, listaPerfiles);
    }

    private void inicializar(String usr, String desc, ArrayList<String> listaPerfiles) {
        if (listaPerfiles == null) {
            perfiles = new ArrayList<String>();
        } else {
            perfiles = new ArrayList<String>(listaPerfiles);
        }
        login = usr;
        descripcion = desc;
    }
}
