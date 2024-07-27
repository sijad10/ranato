/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

/**
 *
 * @author rarevalo
 */
public class CitaHora {
    private Long id;
    private String hora;
    private long cantCupos;
    private long cantCuposAct;    
    private CitaTipoBase sedeId;    
    private boolean Seleccionable;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }

    public long getCantCupos() {
        return cantCupos;
    }
    public void setCantCupos(long cantCupos) {
        this.cantCupos = cantCupos;
    }

    public long getCantCuposAct() {
        return cantCuposAct;
    }

    public void setCantCuposAct(long cantCuposAct) {
        this.cantCuposAct = cantCuposAct;
    }

    public CitaTipoBase getSedeId() {
        return sedeId;
    }
    public void setSedeId(CitaTipoBase sedeId) {
        this.sedeId = sedeId;
    }

    public boolean isSeleccionable() {
        return Seleccionable;
    }

    public void setSeleccionable(boolean Seleccionable) {
        this.Seleccionable = Seleccionable;
    }    
}
