/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.data;

import java.util.Date;

/**
 *
 * @author rarevalo
 */
public class GamacListaVenta {
    public String id;
    private String regVentaId;
    private Long direccionId;
    private String direccion;
    private Long personaId;
    private String tipoDoc;
    private String nroDoc;
    private String compradorNombre;
    private Long tipoCompradorId;
    private String tipoComprador;
    private String tipoCompradorCod;
    private String tipoAutorizacion;
    private String NroAutorizacion;
    private Date fechaVenta;
    private int totalEmpaques;
    private int munixEmpaques;
    private int totalMuniciones;    
    private String responsable;
    private int cierreInventario;
    private int activo;
    private String estado;
    private String tipoTransaccion;
    private boolean isCancelable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegVentaId() {
        return regVentaId;
    }

    public void setRegVentaId(String regVentaId) {
        this.regVentaId = regVentaId;
    }

    public Long getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(Long direccionId) {
        this.direccionId = direccionId;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getCompradorNombre() {
        return compradorNombre;
    }

    public void setCompradorNombre(String compradorNombre) {
        this.compradorNombre = compradorNombre;
    }

    public Long getTipoCompradorId() {
        return tipoCompradorId;
    }

    public void setTipoCompradorId(Long tipoCompradorId) {
        this.tipoCompradorId = tipoCompradorId;
    }

    public String getTipoComprador() {
        return tipoComprador;
    }

    public void setTipoComprador(String tipoComprador) {
        this.tipoComprador = tipoComprador;
    }

    public String getTipoCompradorCod() {
        return tipoCompradorCod;
    }

    public void setTipoCompradorCod(String tipoCompradorCod) {
        this.tipoCompradorCod = tipoCompradorCod;
    }

    public String getTipoAutorizacion() {
        return tipoAutorizacion;
    }

    public void setTipoAutorizacion(String tipoAutorizacion) {
        this.tipoAutorizacion = tipoAutorizacion;
    }

    public String getNroAutorizacion() {
        return NroAutorizacion;
    }

    public void setNroAutorizacion(String NroAutorizacion) {
        this.NroAutorizacion = NroAutorizacion;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public int getTotalEmpaques() {
        return totalEmpaques;
    }

    public void setTotalEmpaques(int totalEmpaques) {
        this.totalEmpaques = totalEmpaques;
    }

    public int getMunixEmpaques() {
        return munixEmpaques;
    }

    public void setMunixEmpaques(int munixEmpaques) {
        this.munixEmpaques = munixEmpaques;
    }

    public int getTotalMuniciones() {
        return totalMuniciones;
    }

    public void setTotalMuniciones(int totalMuniciones) {
        this.totalMuniciones = totalMuniciones;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public int getCierreInventario() {
        return cierreInventario;
    }

    public void setCierreInventario(int cierreInventario) {
        this.cierreInventario = cierreInventario;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public boolean isIsCancelable() {
        return isCancelable;
    }

    public void setIsCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }
    
}
