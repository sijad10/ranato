/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gssp.data;

import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;

/**
 *
 * @author ocastillo
 */
public class PersonaDetalle {
    private Long id;
    private String numDoc;
    private String apePat;
    private String apeMat;
    private String nombres;
    private String partidaRegistral;
    private String asientoRegistral;
    private TipoBaseGt zonaRegistralId;
    private TipoBaseGt zonaOficinaRegistralId;
    private TipoBaseGt tipoViaId;
    private String domicilio;
    private SbDistritoGt distritoId;  

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
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

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getPartidaRegistral() {
        return partidaRegistral;
    }

    public void setPartidaRegistral(String partidaRegistral) {
        this.partidaRegistral = partidaRegistral;
    }

    public String getAsientoRegistral() {
        return asientoRegistral;
    }

    public void setAsientoRegistral(String asientoRegistral) {
        this.asientoRegistral = asientoRegistral;
    }

    public TipoBaseGt getZonaRegistralId() {
        return zonaRegistralId;
    }

    public void setZonaRegistralId(TipoBaseGt zonaRegistralId) {
        this.zonaRegistralId = zonaRegistralId;
    }

    public TipoBaseGt getZonaOficinaRegistralId() {
        return zonaOficinaRegistralId;
    }

    public void setZonaOficinaRegistralId(TipoBaseGt zonaOficinaRegistralId) {
        this.zonaOficinaRegistralId = zonaOficinaRegistralId;
    }

    public TipoBaseGt getTipoViaId() {
        return tipoViaId;
    }

    public void setTipoViaId(TipoBaseGt tipoViaId) {
        this.tipoViaId = tipoViaId;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public SbDistritoGt getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(SbDistritoGt distritoId) {
        this.distritoId = distritoId;
    }
    
}
