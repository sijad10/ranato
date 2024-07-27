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
public class GamacListaCompra {
    public String id;
    private String regCompraId;
    private Long direccionId;
    private String direccion;
    private Long personaId;
    private String tipoDoc;
    private String nroDoc;
    private String vendedorNombre;
    private Long tipoVendedorId;
    private String tipoVendedor;
    private String tipoVendedorCod;    
    private String tipoAutorizacion;
    private String NroAutorizacion;
    private Date fechaCompra;
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

    public String getRegCompraId() {
        return regCompraId;
    }

    public void setRegCompraId(String regCompraId) {
        this.regCompraId = regCompraId;
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

    public String getVendedorNombre() {
        return vendedorNombre;
    }

    public void setVendedorNombre(String vendedorNombre) {
        this.vendedorNombre = vendedorNombre;
    }

    public Long getTipoVendedorId() {
        return tipoVendedorId;
    }

    public void setTipoVendedorId(Long tipoVendedorId) {
        this.tipoVendedorId = tipoVendedorId;
    }

    public String getTipoVendedor() {
        return tipoVendedor;
    }

    public void setTipoVendedor(String tipoVendedor) {
        this.tipoVendedor = tipoVendedor;
    }

    public String getTipoVendedorCod() {
        return tipoVendedorCod;
    }

    public void setTipoVendedorCod(String tipoVendedorCod) {
        this.tipoVendedorCod = tipoVendedorCod;
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

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
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
