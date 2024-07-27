/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.seguridad;

import java.util.ArrayList;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author Renato
 */
public class DatosUsuario {

    private ArrayList<String> perfiles;
    private String login, descripcion, nombres, apePat, apeMat, correo, hashClave, tipo, tipoAuten, numDoc, tipoDoc;
    private long id, areaId, personaId, cantidadUsuario;
    private boolean logeado;
    private Long sistema;
    private SbPersona persona;

    public SbPersona getPersona() {
        return persona;
    }

    public void setPersona(SbPersona persona) {
        this.persona = persona;
    }

    public long getCantidadUsuario() {
        return cantidadUsuario;
    }

    public void setCantidadUsuario(long cantidadUsuario) {
        this.cantidadUsuario = cantidadUsuario;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(long personaId) {
        this.personaId = personaId;
    }

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
            r += ", '" + p.replace("'", "''") + "'";
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
    
     public String getApellidosyNombres() {
        return (apePat!=null?apePat.trim():"") + " " + (apeMat != null?apeMat.trim():"") + " " + (nombres!=null?nombres.trim():"");
    }
     
    @Override
    public String toString() {
        return "DatosUsuario [login=" + login + ", descripcion=" + descripcion + ", nombres="+ nombres + ", apePat="+ apePat + ", apeMat="+ apeMat + ", correo="+ correo + ", numDoc="+ numDoc + ", tipoDoc="+ tipoDoc + ", personaId="+ personaId + ", id = " + id  + ", persona="+ persona.getId() + "]";
    }
     
     
}
