/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.data;

import java.util.Date;

/**
 *
 * @author msalinas
 */
public class GamacResolucion {
    private Long id;
    private String nroRd;
    private Date fechaVencRd;
    private String tipoRd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNroRd() {
        return nroRd;
    }

    public void setNroRd(String nroRd) {
        this.nroRd = nroRd;
    }

    public Date getFechaVencRd() {
        return fechaVencRd;
    }

    public void setFechaVencRd(Date fechaVencRd) {
        this.fechaVencRd = fechaVencRd;
    }    

    public String getTipoRd() {
        return tipoRd;
    }

    public void setTipoRd(String tipoRd) {
        this.tipoRd = tipoRd;
    }
    
    
    
}
