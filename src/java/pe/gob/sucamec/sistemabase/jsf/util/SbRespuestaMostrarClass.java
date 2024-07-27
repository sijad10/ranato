/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.jsf.util;

import java.util.List;
import pe.gob.sucamec.encuesta.data.SbAlternativa;
import pe.gob.sucamec.encuesta.data.SbRespuesta;

/**
 *
 * @author gchavez
 */
public class SbRespuestaMostrarClass {
    private SbRespuesta respuesta;
    private List<SbAlternativa> alternativas;

    
    public SbRespuestaMostrarClass() {
    }

    public SbRespuestaMostrarClass(SbRespuesta respuesta, List<SbAlternativa> alternativas) {
        this.respuesta = respuesta;
        this.alternativas = alternativas;
    }
    
    public SbRespuesta getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(SbRespuesta respuesta) {
        this.respuesta = respuesta;
    }

    public List<SbAlternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<SbAlternativa> alternativas) {
        this.alternativas = alternativas;
    }

    @Override
    public String toString() {
        return "SbRespuestaMostrarClass{" + "respuesta=" + respuesta + ", alternativas=" + alternativas + '}';
    }

}
