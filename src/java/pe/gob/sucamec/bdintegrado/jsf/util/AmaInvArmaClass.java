/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.jsf.util;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import pe.gob.sucamec.bdintegrado.bean.AmaInventarioArmaFacade;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioArma;
import pe.gob.sucamec.bdintegrado.data.AmaTarjetaPropiedad;

/**
 *
 * @author gchavez
 */
public class AmaInvArmaClass {

    private AmaInventarioArma inventarioArma;
    private String estado;
    private Date fechaEstado;
    private String colorFila;

    public AmaInvArmaClass() {

    }

    public AmaInvArmaClass(AmaInventarioArma inventarioArma, String estado, Date fechaEstado, String colorFila) {
        this.inventarioArma = inventarioArma;
        this.estado = estado;
        this.fechaEstado = fechaEstado;
        this.colorFila = colorFila;
    }

    public String setearDatosArma(AmaInventarioArma arma, AmaTarjetaPropiedad amaTarjeta) {
        String res = "";
        this.inventarioArma = arma;
        if (amaTarjeta != null && arma != null) {
            if (amaTarjeta.getFechaEntregaPropietario() != null) {
                this.estado = "ENTREGADO";
                this.fechaEstado = amaTarjeta.getFechaEntregaPropietario();
                this.colorFila = "datatable-row-azul";
            } else if (arma.getAmaGuiaTransitoList() != null && !arma.getAmaGuiaTransitoList().isEmpty()) {
                for (AmaGuiaTransito gt : arma.getAmaGuiaTransitoList()) {
                    if (gt.getActivo() == JsfUtil.TRUE && gt.getFechaVencimiento() != null && Objects.equals(gt.getTipoGuiaId().getCodProg(),"TP_GTGAMAC_REC")) {
                        if (JsfUtil.getFechaSinHora(new Date()).compareTo(JsfUtil.getFechaSinHora(gt.getFechaVencimiento())) > 0) {
                            this.estado = "VENCIDO";
                            this.fechaEstado = gt.getFechaVencimiento();
                            this.colorFila = "datatable-row-amarillo";
                        } else {
                            this.fechaEstado = gt.getFechaEmision();
                            this.estado = "POR ENTREGAR";
                            this.colorFila = "datatable-row-verde";
                        }
                    }
                }
            }
        }
        return res;
    }

    public AmaInventarioArma getInventarioArma() {
        return inventarioArma;
    }

    public void setInventarioArma(AmaInventarioArma inventarioArma) {
        this.inventarioArma = inventarioArma;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(Date fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public String getColorFila() {
        return colorFila;
    }

    public void setColorFila(String colorFila) {
        this.colorFila = colorFila;
    }
}
