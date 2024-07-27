/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author msalinas
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "TUR_COMPROBANTE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaTurComprobante.findAll", query = "SELECT t FROM CitaTurComprobante t"),
    @NamedQuery(name = "CitaTurComprobante.findById", query = "SELECT t FROM CitaTurComprobante t WHERE t.id = :id"),
    @NamedQuery(name = "CitaTurComprobante.findByVoucherTasa", query = "SELECT t FROM CitaTurComprobante t WHERE t.voucherTasa = :voucherTasa"),
    @NamedQuery(name = "CitaTurComprobante.findByVoucherSeq", query = "SELECT t FROM CitaTurComprobante t WHERE t.voucherSeq = :voucherSeq"),
    @NamedQuery(name = "CitaTurComprobante.findByFechaPago", query = "SELECT t FROM CitaTurComprobante t WHERE t.fechaPago = :fechaPago"),
    @NamedQuery(name = "CitaTurComprobante.findByVoucherCta", query = "SELECT t FROM CitaTurComprobante t WHERE t.voucherCta = :voucherCta"),
    @NamedQuery(name = "CitaTurComprobante.findByVoucherAgencia", query = "SELECT t FROM CitaTurComprobante t WHERE t.voucherAgencia = :voucherAgencia"),
    @NamedQuery(name = "CitaTurComprobante.findByVoucherAutentica", query = "SELECT t FROM CitaTurComprobante t WHERE t.voucherAutentica = :voucherAutentica"),
    @NamedQuery(name = "CitaTurComprobante.findByAudLoginTurno", query = "SELECT t FROM CitaTurComprobante t WHERE t.audLoginTurno = :audLoginTurno"),
    @NamedQuery(name = "CitaTurComprobante.findByAudNumIpTurno", query = "SELECT t FROM CitaTurComprobante t WHERE t.audNumIpTurno = :audNumIpTurno"),
    @NamedQuery(name = "CitaTurComprobante.findByNroExpediente", query = "SELECT t FROM CitaTurComprobante t WHERE t.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "CitaTurComprobante.findByActivo", query = "SELECT t FROM CitaTurComprobante t WHERE t.activo = :activo"),
    @NamedQuery(name = "CitaTurComprobante.findByAudLogin", query = "SELECT t FROM CitaTurComprobante t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "CitaTurComprobante.findByAudNumIp", query = "SELECT t FROM CitaTurComprobante t WHERE t.audNumIp = :audNumIp")})
public class CitaTurComprobante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TUR_COMPROBANTE")
    @SequenceGenerator(name = "SEQ_TUR_COMPROBANTE", schema = "BDINTEGRADO", sequenceName = "SEQ_TUR_COMPROBANTE", allocationSize = 1)
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
    @Size(max = 20)
    @Column(name = "AUD_LOGIN_TURNO")
    private String audLoginTurno;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP_TURNO")
    private String audNumIpTurno;
    @Size(max = 40)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @Basic
    @Size(min = 1, max = 20)
    @Column(name = "NRO_SERIE")
    private String nroSerie;
    @ManyToMany(mappedBy = "turComprobanteList")
    private List<CitaTurTurno> turTurnoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprobanteId")
    private List<CitaTurConstancia> turConstanciaList;
    @JoinColumn(name = "TIPO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoGamac tipoArmaId;
    @JoinColumn(name = "CALIBRE_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoGamac calibreId;
    @JoinColumn(name = "BANCO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoBase bancoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprobanteId")
    private List<CitaTurLicenciaReg> turLicenciaRegList;
    @Size(max = 20)
    @Column(name = "NUM_DOC_COMPRADOR")
    private String nroDocComprador;
    
    public CitaTurComprobante() {
    }

    public CitaTurComprobante(Long id) {
        this.id = id;
    }

    public CitaTurComprobante(Long id, String voucherTasa, String voucherSeq, Date fechaPago, String voucherCta, String voucherAgencia, String voucherAutentica, String audNumIpTurno, short activo, String audLogin, String audNumIp, String nroSerie) {
        this.id = id;
        this.voucherTasa = voucherTasa;
        this.voucherSeq = voucherSeq;
        this.fechaPago = fechaPago;
        this.voucherCta = voucherCta;
        this.voucherAgencia = voucherAgencia;
        this.voucherAutentica = voucherAutentica;
        this.audNumIpTurno = audNumIpTurno;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
        this.nroSerie = nroSerie;
    }

    public String getComprobanteCompleto() {
        String result = "" + voucherTasa + voucherSeq + voucherCta + voucherAgencia + voucherAutentica;
        return result;
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

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
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

    public String getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    @XmlTransient
    public List<CitaTurTurno> getTurTurnoList() {
        return turTurnoList;
    }

    public void setTurTurnoList(List<CitaTurTurno> turTurnoList) {
        this.turTurnoList = turTurnoList;
    }

    @XmlTransient
    public List<CitaTurConstancia> getTurConstanciaList() {
        return turConstanciaList;
    }

    public void setTurConstanciaList(List<CitaTurConstancia> turConstanciaList) {
        this.turConstanciaList = turConstanciaList;
    }

    public CitaTipoGamac getTipoArmaId() {
        return tipoArmaId;
    }

    public void setTipoArmaId(CitaTipoGamac tipoArmaId) {
        this.tipoArmaId = tipoArmaId;
    }

    public CitaTipoGamac getCalibreId() {
        return calibreId;
    }

    public void setCalibreId(CitaTipoGamac calibreId) {
        this.calibreId = calibreId;
    }

    public CitaTipoBase getBancoId() {
        return bancoId;
    }

    public void setBancoId(CitaTipoBase bancoId) {
        this.bancoId = bancoId;
    }

    public List<CitaTurLicenciaReg> getTurLicenciaRegList() {
        return turLicenciaRegList;
    }
    
    public void setTurLicenciaRegList(List<CitaTurLicenciaReg> turLicenciaRegList) {
        this.turLicenciaRegList = turLicenciaRegList;
    }

    public String getNroDocComprador() {
        return nroDocComprador;
    }

    public void setNroDocComprador(String nroDocComprador) {
        this.nroDocComprador = nroDocComprador;
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
        if (!(object instanceof CitaTurComprobante)) {
            return false;
        }
        CitaTurComprobante other = (CitaTurComprobante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.citas.data.CitaTurComprobante[ id=" + id + " ]";
    }

}
