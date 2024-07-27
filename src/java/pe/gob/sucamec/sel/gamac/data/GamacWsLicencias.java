/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rarevalo
 */
@Entity
@Table(name = "WS_LICENCIAS", catalog = "", schema = "BDINTEGRADO")
//@Table(name = "INTEROP_WS_LICENCIAS", catalog = "", schema = "RMA1369")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacWsLicencias.findAll", query = "SELECT w FROM GamacWsLicencias w"),
    @NamedQuery(name = "GamacWsLicencias.findById", query = "SELECT w FROM GamacWsLicencias w WHERE w.id = :id"),
    @NamedQuery(name = "GamacWsLicencias.findByTipoLicencia", query = "SELECT w FROM GamacWsLicencias w WHERE w.tipoLicencia = :tipoLicencia"),
    @NamedQuery(name = "GamacWsLicencias.findByNroLic", query = "SELECT w FROM GamacWsLicencias w WHERE w.nroLic = :nroLic")
})
public class GamacWsLicencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 86)
    @Column(name = "ID")
    private String id;
    @Size(max = 200)
    @Column(name = "TIPO_LICENCIA")
    private String tipoLicencia;
    @Column(name = "NRO_LIC")
    private BigInteger nroLic;
    @Size(max = 25)
    @Column(name = "NRO_SERIE")
    private String nroSerie;
    @Size(max = 200)
    @Column(name = "TIPO_ARMA")
    private String tipoArma;
    @Size(max = 200)
    @Column(name = "MARCA")
    private String marca;
    @Size(max = 200)
    @Column(name = "MODELO")
    private String modelo;
    @Size(max = 200)
    @Column(name = "CALIBRE")
    private String calibre;
    @Size(max = 50)
    @Column(name = "TIPO_PROPIETARIO")
    private String tipoPropietario;
    @Column(name = "DOC_PROPIETARIO")
    private BigInteger docPropietario;
    @Size(max = 902)
    @Column(name = "PROPIETARIO")
    private String propietario;
    @Size(max = 50)
    @Column(name = "TIPO_PORTADOR")
    private String tipoPortador;
    @Column(name = "DOC_PORTADOR")
    private BigInteger docPortador;
    @Column(name = "FEC_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecEmision;
    @Column(name = "FEC_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecVencimiento;
    @Size(max = 902)
    @Column(name = "PORTADOR")
    private String portador;
    @Size(max = 200)
    @Column(name = "SITUACION")
    private String situacion;
    @Size(max = 7)
    @Column(name = "ESTADO")
    private String estado;
    @Size(max = 300)
    @Column(name = "RAZON_SOCIAL_PROPIETARIO")
    private String razonSocialPropietario;
    @Size(max = 200)
    @Column(name = "APE_PAT_PROPIETARIO")
    private String apePatPropietario;
    @Size(max = 200)
    @Column(name = "APE_MAT_PROPIETARIO")
    private String apeMatPropietario;
    @Size(max = 200)
    @Column(name = "NOMBRE_PROPIETARIO")
    private String nombrePropietario;
    @Size(max = 300)
    @Column(name = "RAZON_SOCIAL_PORTADOR")
    private String razonSocialPortador;
    @Size(max = 200)
    @Column(name = "APE_PAT_PORTADOR")
    private String apePatPortador;
    @Size(max = 200)
    @Column(name = "APE_MAT_PORTADOR")
    private String apeMatPortador;
    @Size(max = 200)
    @Column(name = "NOMBRE_PORTADOR")
    private String nombrePortador;
    @Column(name = "TIP_ART")
    private BigInteger tipArt;
    @Column(name = "TIP_ARM")
    private BigInteger tipArm;
    @Column(name = "COD_MARCA")
    private BigInteger codMarca;
    @Column(name = "TIP_LIC")
    private BigInteger tipLic;
    @Column(name = "TIP_USR_PROPIETARIO")
    private BigInteger tipUsrPropietario;
    @Column(name = "TIP_USR_PORTADOR")
    private BigInteger tipUsrPortador;
    @Column(name = "FEC_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecIni;
    @Size(max = 40)
    @Column(name = "VIGENCIA")
    private String vigencia;
    @Column(name = "NRO_EXP")
    private BigInteger nroExp;
    @Column(name = "ANO_EXP")
    private BigInteger anoExp;

    public GamacWsLicencias() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public BigInteger getNroLic() {
        return nroLic;
    }

    public void setNroLic(BigInteger nroLic) {
        this.nroLic = nroLic;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    public String getTipoArma() {
        return tipoArma;
    }

    public void setTipoArma(String tipoArma) {
        this.tipoArma = tipoArma;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCalibre() {
        return calibre;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }

    public String getTipoPropietario() {
        return tipoPropietario;
    }

    public void setTipoPropietario(String tipoPropietario) {
        this.tipoPropietario = tipoPropietario;
    }

    public BigInteger getDocPropietario() {
        return docPropietario;
    }

    public void setDocPropietario(BigInteger docPropietario) {
        this.docPropietario = docPropietario;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getTipoPortador() {
        return tipoPortador;
    }

    public void setTipoPortador(String tipoPortador) {
        this.tipoPortador = tipoPortador;
    }

    public BigInteger getDocPortador() {
        return docPortador;
    }

    public void setDocPortador(BigInteger docPortador) {
        this.docPortador = docPortador;
    }

    public Date getFecEmision() {
        return fecEmision;
    }

    public void setFecEmision(Date fecEmision) {
        this.fecEmision = fecEmision;
    }

    public Date getFecVencimiento() {
        return fecVencimiento;
    }

    public void setFecVencimiento(Date fecVencimiento) {
        this.fecVencimiento = fecVencimiento;
    }

    public String getPortador() {
        return portador;
    }

    public void setPortador(String portador) {
        this.portador = portador;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRazonSocialPropietario() {
        return razonSocialPropietario;
    }

    public void setRazonSocialPropietario(String razonSocialPropietario) {
        this.razonSocialPropietario = razonSocialPropietario;
    }

    public String getApePatPropietario() {
        return apePatPropietario;
    }

    public void setApePatPropietario(String apePatPropietario) {
        this.apePatPropietario = apePatPropietario;
    }

    public String getApeMatPropietario() {
        return apeMatPropietario;
    }

    public void setApeMatPropietario(String apeMatPropietario) {
        this.apeMatPropietario = apeMatPropietario;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public String getRazonSocialPortador() {
        return razonSocialPortador;
    }

    public void setRazonSocialPortador(String razonSocialPortador) {
        this.razonSocialPortador = razonSocialPortador;
    }

    public String getApePatPortador() {
        return apePatPortador;
    }

    public void setApePatPortador(String apePatPortador) {
        this.apePatPortador = apePatPortador;
    }

    public String getApeMatPortador() {
        return apeMatPortador;
    }

    public void setApeMatPortador(String apeMatPortador) {
        this.apeMatPortador = apeMatPortador;
    }

    public String getNombrePortador() {
        return nombrePortador;
    }

    public void setNombrePortador(String nombrePortador) {
        this.nombrePortador = nombrePortador;
    }

    public BigInteger getTipArt() {
        return tipArt;
    }

    public void setTipArt(BigInteger tipArt) {
        this.tipArt = tipArt;
    }

    public BigInteger getTipArm() {
        return tipArm;
    }

    public void setTipArm(BigInteger tipArm) {
        this.tipArm = tipArm;
    }

    public BigInteger getCodMarca() {
        return codMarca;
    }

    public void setCodMarca(BigInteger codMarca) {
        this.codMarca = codMarca;
    }

    public BigInteger getTipLic() {
        return tipLic;
    }

    public void setTipLic(BigInteger tipLic) {
        this.tipLic = tipLic;
    }

    public BigInteger getTipUsrPropietario() {
        return tipUsrPropietario;
    }

    public void setTipUsrPropietario(BigInteger tipUsrPropietario) {
        this.tipUsrPropietario = tipUsrPropietario;
    }

    public BigInteger getTipUsrPortador() {
        return tipUsrPortador;
    }

    public void setTipUsrPortador(BigInteger tipUsrPortador) {
        this.tipUsrPortador = tipUsrPortador;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public BigInteger getNroExp() {
        return nroExp;
    }

    public void setNroExp(BigInteger nroExp) {
        this.nroExp = nroExp;
    }

    public BigInteger getAnoExp() {
        return anoExp;
    }

    public void setAnoExp(BigInteger anoExp) {
        this.anoExp = anoExp;
    }
    
}
