/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.turreg.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.TipoBase;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;

/**
 *
 * @author msalinas
 */
@Entity
@Table(name = "TUR_COMPROBANTE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TurComprobante.findAll", query = "SELECT t FROM TurComprobante t"),
    @NamedQuery(name = "TurComprobante.findById", query = "SELECT t FROM TurComprobante t WHERE t.id = :id"),
    @NamedQuery(name = "TurComprobante.findByVoucherTasa", query = "SELECT t FROM TurComprobante t WHERE t.voucherTasa = :voucherTasa"),
    @NamedQuery(name = "TurComprobante.findByVoucherSeq", query = "SELECT t FROM TurComprobante t WHERE t.voucherSeq = :voucherSeq"),
    @NamedQuery(name = "TurComprobante.findByFechaPago", query = "SELECT t FROM TurComprobante t WHERE t.fechaPago = :fechaPago"),
    @NamedQuery(name = "TurComprobante.findByVoucherCta", query = "SELECT t FROM TurComprobante t WHERE t.voucherCta = :voucherCta"),
    @NamedQuery(name = "TurComprobante.findByVoucherAgencia", query = "SELECT t FROM TurComprobante t WHERE t.voucherAgencia = :voucherAgencia"),
    @NamedQuery(name = "TurComprobante.findByVoucherAutentica", query = "SELECT t FROM TurComprobante t WHERE t.voucherAutentica = :voucherAutentica"),
    @NamedQuery(name = "TurComprobante.findByNroExpediente", query = "SELECT t FROM TurComprobante t WHERE t.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "TurComprobante.findByAudLoginTurno", query = "SELECT t FROM TurComprobante t WHERE t.audLoginTurno = :audLoginTurno"),
    @NamedQuery(name = "TurComprobante.findByAudNumIpTurno", query = "SELECT t FROM TurComprobante t WHERE t.audNumIpTurno = :audNumIpTurno"),
    @NamedQuery(name = "TurComprobante.findByNroSerie", query = "SELECT t FROM TurComprobante t WHERE t.nroSerie = :nroSerie"),
    @NamedQuery(name = "TurComprobante.findByActivo", query = "SELECT t FROM TurComprobante t WHERE t.activo = :activo"),
    @NamedQuery(name = "TurComprobante.findByAudLogin", query = "SELECT t FROM TurComprobante t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TurComprobante.findByAudNumIp", query = "SELECT t FROM TurComprobante t WHERE t.audNumIp = :audNumIp")})
public class TurComprobante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "VOUCHER_TASA")
    private String voucherTasa;
    @Size(max = 20)
    @Column(name = "VOUCHER_SEQ")
    private String voucherSeq;
    @Column(name = "FECHA_PAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    @Size(max = 20)
    @Column(name = "VOUCHER_CTA")
    private String voucherCta;
    @Size(max = 20)
    @Column(name = "VOUCHER_AGENCIA")
    private String voucherAgencia;
    @Size(max = 20)
    @Column(name = "VOUCHER_AUTENTICA")
    private String voucherAutentica;
    @Size(max = 40)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Size(max = 20)
    @Column(name = "AUD_LOGIN_TURNO")
    private String audLoginTurno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP_TURNO")
    private String audNumIpTurno;
    @Size(max = 20)
    @Column(name = "NRO_SERIE")
    private String nroSerie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @ManyToMany(mappedBy = "turComprobanteList")
    private List<TurTurno> turTurnoList;
    @OneToMany(mappedBy = "comprobanteId")
    private List<TurConstancia> turConstanciaList;
    @JoinColumn(name = "TIPO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac tipoArmaId;
    @JoinColumn(name = "CALIBRE_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac calibreId;
    @JoinColumn(name = "BANCO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBase bancoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprobanteId")
    private List<TurLicenciaReg> turLicenciaRegList;

    public TurComprobante() {
    }

    public TurComprobante(Long id) {
        this.id = id;
    }

    public TurComprobante(Long id, String audNumIpTurno, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.audNumIpTurno = audNumIpTurno;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVoucherTasa() {
        return voucherTasa;
    }

    public void setVoucherTasa(String voucherTasa) {
        this.voucherTasa = voucherTasa;
    }

    public String getVoucherSeq() {
        return voucherSeq;
    }

    public void setVoucherSeq(String voucherSeq) {
        this.voucherSeq = voucherSeq;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getVoucherCta() {
        return voucherCta;
    }

    public void setVoucherCta(String voucherCta) {
        this.voucherCta = voucherCta;
    }

    public String getVoucherAgencia() {
        return voucherAgencia;
    }

    public void setVoucherAgencia(String voucherAgencia) {
        this.voucherAgencia = voucherAgencia;
    }

    public String getVoucherAutentica() {
        return voucherAutentica;
    }

    public void setVoucherAutentica(String voucherAutentica) {
        this.voucherAutentica = voucherAutentica;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public String getAudLoginTurno() {
        return audLoginTurno;
    }

    public void setAudLoginTurno(String audLoginTurno) {
        this.audLoginTurno = audLoginTurno;
    }

    public String getAudNumIpTurno() {
        return audNumIpTurno;
    }

    public void setAudNumIpTurno(String audNumIpTurno) {
        this.audNumIpTurno = audNumIpTurno;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public String getAudLogin() {
        return audLogin;
    }

    public void setAudLogin(String audLogin) {
        this.audLogin = audLogin;
    }

    public String getAudNumIp() {
        return audNumIp;
    }

    public void setAudNumIp(String audNumIp) {
        this.audNumIp = audNumIp;
    }

    @XmlTransient
    public List<TurTurno> getTurTurnoList() {
        return turTurnoList;
    }

    public void setTurTurnoList(List<TurTurno> turTurnoList) {
        this.turTurnoList = turTurnoList;
    }

    @XmlTransient
    public List<TurConstancia> getTurConstanciaList() {
        return turConstanciaList;
    }

    public void setTurConstanciaList(List<TurConstancia> turConstanciaList) {
        this.turConstanciaList = turConstanciaList;
    }

    public TipoGamac getTipoArmaId() {
        return tipoArmaId;
    }

    public void setTipoArmaId(TipoGamac tipoArmaId) {
        this.tipoArmaId = tipoArmaId;
    }

    public TipoGamac getCalibreId() {
        return calibreId;
    }

    public void setCalibreId(TipoGamac calibreId) {
        this.calibreId = calibreId;
    }

    public TipoBase getBancoId() {
        return bancoId;
    }

    public void setBancoId(TipoBase bancoId) {
        this.bancoId = bancoId;
    }

    public List<TurLicenciaReg> getTurLicenciaRegList() {
        return turLicenciaRegList;
    }

    public void setTurLicenciaRegList(List<TurLicenciaReg> turLicenciaRegList) {
        this.turLicenciaRegList = turLicenciaRegList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TurComprobante)) {
            return false;
        }
        TurComprobante other = (TurComprobante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.turreg.data.TurComprobante[ id=" + id + " ]";
    }
    
}
