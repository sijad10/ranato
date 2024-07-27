/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.seguridad;

/**
 *
 * @author Renato
 */
public class DatosPagina {

    private Long id;
    private String url, funcion;
    private boolean acceso;

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isAcceso() {
        return acceso;
    }

    public void setAcceso(boolean acceso) {
        this.acceso = acceso;
    }

    public DatosPagina(Long id, String url, String funcion, boolean acceso) {
        this.id = id;
        this.url = url;
        this.funcion = funcion;
        this.acceso = acceso;
    }

}
