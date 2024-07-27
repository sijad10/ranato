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
public class GamacDetalleVenta1 {
    public String id;
    private String wsLicenciaId;
    private String nroRUA;
    private Long armaId;
    private String datosArma;
    private Long tarjetaPropiedaId;
    private String modalidad;
    private Long municionId;
    private String municion;
    private int cantidadEmpaques;
    private int muniPorEmpaque;
    private int saldoMesAnt;
    private int saldoMesAct;
    private int cantidadVendida;
    private String codFabricante;
    private String loteFabricacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWsLicenciaId() {
        return wsLicenciaId;
    }

    public void setWsLicenciaId(String wsLicenciaId) {
        this.wsLicenciaId = wsLicenciaId;
    }

    public String getNroRUA() {
        return nroRUA;
    }

    public void setNroRUA(String nroRUA) {
        this.nroRUA = nroRUA;
    }

    public Long getArmaId() {
        return armaId;
    }

    public void setArmaId(Long armaId) {
        this.armaId = armaId;
    }

    public String getDatosArma() {
        return datosArma;
    }

    public void setDatosArma(String datosArma) {
        this.datosArma = datosArma;
    }

    public Long getTarjetaPropiedaId() {
        return tarjetaPropiedaId;
    }

    public void setTarjetaPropiedaId(Long tarjetaPropiedaId) {
        this.tarjetaPropiedaId = tarjetaPropiedaId;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
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

    public int getSaldoMesAnt() {
        return saldoMesAnt;
    }

    public void setSaldoMesAnt(int saldoMesAnt) {
        this.saldoMesAnt = saldoMesAnt;
    }

    public int getSaldoMesAct() {
        return saldoMesAct;
    }

    public void setSaldoMesAct(int saldoMesAct) {
        this.saldoMesAct = saldoMesAct;
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
