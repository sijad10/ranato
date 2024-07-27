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
public class GamacDetalleDev {
    public String id;
    private Long detId;
    private Long municionId;
    private String calibre;    
    private String municion;
    private int cantidadEmpaques;
    private int muniPorEmpaque;
    private int cantidadVendida;
    private int cantidadEmpDev;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getDetId() {
        return detId;
    }

    public void setDetId(Long detId) {
        this.detId = detId;
    }

    public Long getMunicionId() {
        return municionId;
    }

    public void setMunicionId(Long municionId) {
        this.municionId = municionId;
    }

    public String getCalibre() {
        return calibre;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
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

    public int getCantidadEmpDev() {
        return cantidadEmpDev;
    }

    public void setCantidadEmpDev(int cantidadEmpDev) {
        this.cantidadEmpDev = cantidadEmpDev;
    }
    
    
}
