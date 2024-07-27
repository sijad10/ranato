/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.jsf.util;

import pe.gob.sucamec.bdintegrado.data.SspModulo;

/**
 *
 * @author rarevalo
 */
public class CursosOption {
    private SspModulo modulo;
    private boolean disabled;

    public CursosOption(SspModulo modulo, boolean disabled) {
        this.modulo = modulo;
        this.disabled = disabled;
    }

    public SspModulo getModulo() {
        return modulo;
    }

    public void setModulo(SspModulo modulo) {
        this.modulo = modulo;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    
}
