/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gssp.data;
import pe.gob.sucamec.bdintegrado.data.SspTipoUsoLocal;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
/**
 *
 * @author ocastillo
 */
public class LocalAutorizacionDetalle {
    private Long id;
    private String direccion;
    private String referencia;
    private String geoLatitud;
    private String geoLongitud;
    private String nroFisico;
    private TipoBaseGt tipoUbicacionId;
    private SbDistritoGt distritoId;
    private TipoSeguridad tipoLocalId;
    private TipoSeguridad tipoUsoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getGeoLatitud() {
        return geoLatitud;
    }

    public void setGeoLatitud(String geoLatitud) {
        this.geoLatitud = geoLatitud;
    }

    public String getGeoLongitud() {
        return geoLongitud;
    }

    public void setGeoLongitud(String geoLongitud) {
        this.geoLongitud = geoLongitud;
    }

    public String getNroFisico() {
        return nroFisico;
    }

    public void setNroFisico(String nroFisico) {
        this.nroFisico = nroFisico;
    }

    public TipoBaseGt getTipoUbicacionId() {
        return tipoUbicacionId;
    }

    public void setTipoUbicacionId(TipoBaseGt tipoUbicacionId) {
        this.tipoUbicacionId = tipoUbicacionId;
    }

    public SbDistritoGt getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(SbDistritoGt distritoId) {
        this.distritoId = distritoId;
    }

    public TipoSeguridad getTipoLocalId() {
        return tipoLocalId;
    }

    public void setTipoLocalId(TipoSeguridad tipoLocalId) {
        this.tipoLocalId = tipoLocalId;
    }
    
    public TipoSeguridad getTipoUsoId() {
        return tipoUsoId;
    }

    public void setTipoUsoId(TipoSeguridad tipoUsoId) {
        this.tipoUsoId = tipoUsoId;
    }
    
}
