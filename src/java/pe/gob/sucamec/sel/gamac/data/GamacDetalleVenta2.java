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
public class GamacDetalleVenta2 {
    public String id;
    private Long calibreArmaId;
    private String calibreArma;
    private Long municionId;
    private String municion;
    private int cantidadEmpaques;
    private int muniPorEmpaque;
    private int cantidadVendida;
    private String codFabricante;
    private String loteFabricacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCalibreArmaId() {
        return calibreArmaId;
    }

    public void setCalibreArmaId(Long calibreArmaId) {
        this.calibreArmaId = calibreArmaId;
    }

    public String getCalibreArma() {
        return calibreArma;
    }

    public void setCalibreArma(String calibreArma) {
        this.calibreArma = calibreArma;
    }

    public Long getMunicionId() {
        return municionId;
    }

    public void setMunicionId(Long municionId) {
        this.municionId = municionId;
    }

    public String getMunicion() {
        return municion;
    }

    public void setMunicion(String municion) {
        this.municion = municion;
    }

    public int getCantidadEmpaques() {
        return cantidadEmpaques;
    }

    public void setCantidadEmpaques(int cantidadEmpaques) {
        this.cantidadEmpaques = cantidadEmpaques;
    }

    public int getMuniPorEmpaque() {
        return muniPorEmpaque;
    }

    public void setMuniPorEmpaque(int muniPorEmpaque) {
        this.muniPorEmpaque = muniPorEmpaque;
    }

    public int getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public String getCodFabricante() {
        return codFabricante;
    }

    public void setCodFabricante(String codFabricante) {
        this.codFabricante = codFabricante;
    }

    public String getLoteFabricacion() {
        return loteFabricacion;
    }

    public void setLoteFabricacion(String loteFabricacion) {
        this.loteFabricacion = loteFabricacion;
    }
    
}
