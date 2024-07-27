/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.seguridad;

import java.util.Date;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.sistemabase.data.SbDistrito;

/**
 *
 * @author Renato
 */
public class DatosInscripcion {

private TipoBaseGt tipoDoc;
private String numDoc;
private String numCod;
private String nombres;
private String apePat;
private String apeMat;
private TipoBaseGt genero;
private Date fechaNac;
private TipoBaseGt estCivil;
private TipoBaseGt ocupacion;
private TipoBaseGt tipoVia;
private String direccion;
private String numero;
private String referencia;
private SbDistritoGt ubigeo;
private String correoElec;
private String numCelular;
private String numTelf;
private String codigoCaptcha;

    /**
     * @return the numDoc
     */
    public String getNumDoc() {
        return numDoc;
    }

    /**
     * @param numDoc the numDoc to set
     */
    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    /**
     * @return the nombres
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * @param nombres the nombres to set
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * @return the apePat
     */
    public String getApePat() {
        return apePat;
    }

    /**
     * @param apePat the apePat to set
     */
    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    /**
     * @return the apeMat
     */
    public String getApeMat() {
        return apeMat;
    }

    /**
     * @param apeMat the apeMat to set
     */
    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }
    /**
     * @return the fechaNac
     */
    public Date getFechaNac() {
        return fechaNac;
    }

    /**
     * @param fechaNac the fechaNac to set
     */
    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the referencia
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * @param referencia the referencia to set
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * @return the correoElec
     */
    public String getCorreoElec() {
        return correoElec;
    }

    /**
     * @param correoElec the correoElec to set
     */
    public void setCorreoElec(String correoElec) {
        this.correoElec = correoElec;
    }

    /**
     * @return the numCelular
     */
    public String getNumCelular() {
        return numCelular;
    }

    /**
     * @param numCelular the numCelular to set
     */
    public void setNumCelular(String numCelular) {
        this.numCelular = numCelular;
    }

    /**
     * @return the numTelf
     */
    public String getNumTelf() {
        return numTelf;
    }

    /**
     * @param numTelf the numTelf to set
     */
    public void setNumTelf(String numTelf) {
        this.numTelf = numTelf;
    }

    /**
     * @return the codigoCaptcha
     */
    public String getCodigoCaptcha() {
        return codigoCaptcha;
    }

    /**
     * @param codigoCaptcha the codigoCaptcha to set
     */
    public void setCodigoCaptcha(String codigoCaptcha) {
        this.codigoCaptcha = codigoCaptcha;
    }

    /**
     * @return the tipoDoc
     */
    public TipoBaseGt getTipoDoc() {
        return tipoDoc;
    }

    /**
     * @param tipoDoc the tipoDoc to set
     */
    public void setTipoDoc(TipoBaseGt tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    /**
     * @return the genero
     */
    public TipoBaseGt getGenero() {
        return genero;
    }

    /**
     * @param genero the genero to set
     */
    public void setGenero(TipoBaseGt genero) {
        this.genero = genero;
    }

    /**
     * @return the estCivil
     */
    public TipoBaseGt getEstCivil() {
        return estCivil;
    }

    /**
     * @param estCivil the estCivil to set
     */
    public void setEstCivil(TipoBaseGt estCivil) {
        this.estCivil = estCivil;
    }

    /**
     * @return the ocupacion
     */
    public TipoBaseGt getOcupacion() {
        return ocupacion;
    }

    /**
     * @param ocupacion the ocupacion to set
     */
    public void setOcupacion(TipoBaseGt ocupacion) {
        this.ocupacion = ocupacion;
    }

    /**
     * @return the tipoVia
     */
    public TipoBaseGt getTipoVia() {
        return tipoVia;
    }

    /**
     * @param tipoVia the tipoVia to set
     */
    public void setTipoVia(TipoBaseGt tipoVia) {
        this.tipoVia = tipoVia;
    }

    /**
     * @return the ubigeo
     */
    public SbDistritoGt getUbigeo() {
        return ubigeo;
    }

    /**
     * @param ubigeo the ubigeo to set
     */
    public void setUbigeo(SbDistritoGt ubigeo) {
        this.ubigeo = ubigeo;
    }

    /**
     * @return the numCod
     */
    public String getNumCod() {
        return numCod;
    }

    /**
     * @param numCod the numCod to set
     */
    public void setNumCod(String numCod) {
        this.numCod = numCod;
    }
}
