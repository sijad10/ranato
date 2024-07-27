/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.data;

/**
 *
 * @author rarevalo
 */
public class GamacListaInventario {
    public String id;
    private Long transaccionId;
    private Long detTransaccionId;
    private Long municionId;
    private String localComercial;
    private String marca;
    private String calibreaArma;
    private String denominacion;
    private String tipoMunicion;
    private String tipoProyectil;
    private int cantCompraEmpaques;
    private int cantCompraMuniciones;
    private int cantVentaEmpaques;
    private int cantVentaMuniciones;
    private int stockEmpaques;
    private int stockMuniciones;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(Long transaccionId) {
        this.transaccionId = transaccionId;
    }

    public Long getDetTransaccionId() {
        return detTransaccionId;
    }

    public void setDetTransaccionId(Long detTransaccionId) {
        this.detTransaccionId = detTransaccionId;
    }

    public Long getMunicionId() {
        return municionId;
    }

    public void setMunicionId(Long municionId) {
        this.municionId = municionId;
    }

    public String getLocalComercial() {
        return localComercial;
    }

    public void setLocalComercial(String localComercial) {
        this.localComercial = localComercial;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCalibreaArma() {
        return calibreaArma;
    }

    public void setCalibreaArma(String calibreaArma) {
        this.calibreaArma = calibreaArma;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getTipoMunicion() {
        return tipoMunicion;
    }

    public void setTipoMunicion(String tipoMunicion) {
        this.tipoMunicion = tipoMunicion;
    }

    public String getTipoProyectil() {
        return tipoProyectil;
    }

    public void setTipoProyectil(String tipoProyectil) {
        this.tipoProyectil = tipoProyectil;
    }

    public int getCantCompraEmpaques() {
        return cantCompraEmpaques;
    }

    public void setCantCompraEmpaques(int cantCompraEmpaques) {
        this.cantCompraEmpaques = cantCompraEmpaques;
    }

    public int getCantCompraMuniciones() {
        return cantCompraMuniciones;
    }

    public void setCantCompraMuniciones(int cantCompraMuniciones) {
        this.cantCompraMuniciones = cantCompraMuniciones;
    }

    public int getCantVentaEmpaques() {
        return cantVentaEmpaques;
    }

    public void setCantVentaEmpaques(int cantVentaEmpaques) {
        this.cantVentaEmpaques = cantVentaEmpaques;
    }

    public int getCantVentaMuniciones() {
        return cantVentaMuniciones;
    }

    public void setCantVentaMuniciones(int cantVentaMuniciones) {
        this.cantVentaMuniciones = cantVentaMuniciones;
    }

    public int getStockEmpaques() {
        return stockEmpaques;
    }

    public void setStockEmpaques(int stockEmpaques) {
        this.stockEmpaques = stockEmpaques;
    }

    public int getStockMuniciones() {
        return stockMuniciones;
    }

    public void setStockMuniciones(int stockMuniciones) {
        this.stockMuniciones = stockMuniciones;
    }
    
    
}
