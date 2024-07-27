/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.turreg.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;

/**
 *
 * @author msalinas
 */
@Entity
@Table(name = "TUR_CONSTANCIA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TurConstancia.findAll", query = "SELECT t FROM TurConstancia t"),
    @NamedQuery(name = "TurConstancia.findById", query = "SELECT t FROM TurConstancia t WHERE t.id = :id"),
    @NamedQuery(name = "TurConstancia.findByRutaFoto", query = "SELECT t FROM TurConstancia t WHERE t.rutaFoto = :rutaFoto"),
    @NamedQuery(name = "TurConstancia.findByNota", query = "SELECT t FROM TurConstancia t WHERE t.nota = :nota"),
    @NamedQuery(name = "TurConstancia.findByCantMunicion", query = "SELECT t FROM TurConstancia t WHERE t.cantMunicion = :cantMunicion"),
    @NamedQuery(name = "TurConstancia.findByCantTiro", query = "SELECT t FROM TurConstancia t WHERE t.cantTiro = :cantTiro"),
    @NamedQuery(name = "TurConstancia.findByCantImpacto", query = "SELECT t FROM TurConstancia t WHERE t.cantImpacto = :cantImpacto"),
    @NamedQuery(name = "TurConstancia.findByFechaEmision", query = "SELECT t FROM TurConstancia t WHERE t.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "TurConstancia.findByFechaVencimiento", query = "SELECT t FROM TurConstancia t WHERE t.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "TurConstancia.findByCodVerifica", query = "SELECT t FROM TurConstancia t WHERE t.codVerifica = :codVerifica"),
    @NamedQuery(name = "TurConstancia.findByAudUsuTeoPrac", query = "SELECT t FROM TurConstancia t WHERE t.audUsuTeoPrac = :audUsuTeoPrac"),
    @NamedQuery(name = "TurConstancia.findByAudIpTeoPrac", query = "SELECT t FROM TurConstancia t WHERE t.audIpTeoPrac = :audIpTeoPrac"),
    @NamedQuery(name = "TurConstancia.findByAudUsuTiro", query = "SELECT t FROM TurConstancia t WHERE t.audUsuTiro = :audUsuTiro"),
    @NamedQuery(name = "TurConstancia.findByAudIpTiro", query = "SELECT t FROM TurConstancia t WHERE t.audIpTiro = :audIpTiro"),
    @NamedQuery(name = "TurConstancia.findByNroConstancia", query = "SELECT t FROM TurConstancia t WHERE t.nroConstancia = :nroConstancia"),
    @NamedQuery(name = "TurConstancia.findByHashQr", query = "SELECT t FROM TurConstancia t WHERE t.hashQr = :hashQr"),
    @NamedQuery(name = "TurConstancia.findByActivo", query = "SELECT t FROM TurConstancia t WHERE t.activo = :activo"),
    @NamedQuery(name = "TurConstancia.findByAudLogin", query = "SELECT t FROM TurConstancia t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TurConstancia.findByAudNumIp", query = "SELECT t FROM TurConstancia t WHERE t.audNumIp = :audNumIp")})
public class TurConstancia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 500)
    @Column(name = "RUTA_FOTO")
    private String rutaFoto;
    @Column(name = "NOTA")
    private Long nota;
    @Column(name = "CANT_MUNICION")
    private Short cantMunicion;
    @Column(name = "CANT_TIRO")
    private Short cantTiro;
    @Column(name = "CANT_IMPACTO")
    private Short cantImpacto;
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
    @Size(max = 40)
    @Column(name = "COD_VERIFICA")
    private String codVerifica;
    @Size(max = 20)
    @Column(name = "AUD_USU_TEO_PRAC")
    private String audUsuTeoPrac;
    @Size(max = 40)
    @Column(name = "AUD_IP_TEO_PRAC")
    private String audIpTeoPrac;
    @Size(max = 20)
    @Column(name = "AUD_USU_TIRO")
    private String audUsuTiro;
    @Size(max = 40)
    @Column(name = "AUD_IP_TIRO")
    private String audIpTiro;
    @Size(max = 100)
    @Column(name = "NRO_CONSTANCIA")
    private String nroConstancia;
    @Size(max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
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
    @Size(max = 500)
    @Column(name = "FOTO_SILUETA")
    private String fotoSilueta;
    @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TurTurno turnoId;
    @JoinColumn(name = "COMPROBANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private TurComprobante comprobanteId;
    @JoinColumn(name = "EXA_TEO_PRAC", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac exaTeoPrac;
    @JoinColumn(name = "EXA_TIRO", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac exaTiro;
    @JoinColumn(name = "RESUL_GRAL", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac resulGral;
    @JoinColumn(name = "TIPO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac tipoArmaId;
    @JoinColumn(name = "MODALIDAD_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac modalidadId;
    @JoinColumn(name = "GERENTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuario gerenteId;
    @JoinColumn(name = "ARMA_TIRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaCatalogo armaTiroId;
    @JoinColumn(name = "ARMA_TEO_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaCatalogo armaTeoId;

    public TurConstancia() {
    }

    public TurConstancia(Long id) {
        this.id = id;
    }

    public TurConstancia(Long id, short activo, String audLogin, String audNumIp) {
        this.id = id;
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

    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }

    public Long getNota() {
        return nota;
    }

    public void setNota(Long nota) {
        this.nota = nota;
    }

    public Short getCantMunicion() {
        return cantMunicion;
    }

    public void setCantMunicion(Short cantMunicion) {
        this.cantMunicion = cantMunicion;
    }

    public Short getCantTiro() {
        return cantTiro;
    }

    public void setCantTiro(Short cantTiro) {
        this.cantTiro = cantTiro;
    }

    public Short getCantImpacto() {
        return cantImpacto;
    }

    public void setCantImpacto(Short cantImpacto) {
        this.cantImpacto = cantImpacto;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getCodVerifica() {
        return codVerifica;
    }

    public void setCodVerifica(String codVerifica) {
        this.codVerifica = codVerifica;
    }

    public String getAudUsuTeoPrac() {
        return audUsuTeoPrac;
    }

    public void setAudUsuTeoPrac(String audUsuTeoPrac) {
        this.audUsuTeoPrac = audUsuTeoPrac;
    }

    public String getFotoSilueta() {
        return fotoSilueta;
    }

    public void setFotoSilueta(String fotoSilueta) {
        this.fotoSilueta = fotoSilueta;
    }

    public String getAudIpTeoPrac() {
        return audIpTeoPrac;
    }

    public void setAudIpTeoPrac(String audIpTeoPrac) {
        this.audIpTeoPrac = audIpTeoPrac;
    }

    public String getAudUsuTiro() {
        return audUsuTiro;
    }

    public void setAudUsuTiro(String audUsuTiro) {
        this.audUsuTiro = audUsuTiro;
    }

    public String getAudIpTiro() {
        return audIpTiro;
    }

    public void setAudIpTiro(String audIpTiro) {
        this.audIpTiro = audIpTiro;
    }

    public String getNroConstancia() {
        return nroConstancia;
    }

    public void setNroConstancia(String nroConstancia) {
        this.nroConstancia = nroConstancia;
    }

    public String getHashQr() {
        return hashQr;
    }

    public void setHashQr(String hashQr) {
        this.hashQr = hashQr;
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

    public TurTurno getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(TurTurno turnoId) {
        this.turnoId = turnoId;
    }

    public TurComprobante getComprobanteId() {
        return comprobanteId;
    }

    public void setComprobanteId(TurComprobante comprobanteId) {
        this.comprobanteId = comprobanteId;
    }

    public TipoGamac getExaTeoPrac() {
        return exaTeoPrac;
    }

    public void setExaTeoPrac(TipoGamac exaTeoPrac) {
        this.exaTeoPrac = exaTeoPrac;
    }

    public TipoGamac getExaTiro() {
        return exaTiro;
    }

    public void setExaTiro(TipoGamac exaTiro) {
        this.exaTiro = exaTiro;
    }

    public TipoGamac getResulGral() {
        return resulGral;
    }

    public void setResulGral(TipoGamac resulGral) {
        this.resulGral = resulGral;
    }

    public TipoGamac getTipoArmaId() {
        return tipoArmaId;
    }

    public void setTipoArmaId(TipoGamac tipoArmaId) {
        this.tipoArmaId = tipoArmaId;
    }

    public TipoGamac getModalidadId() {
        return modalidadId;
    }

    public void setModalidadId(TipoGamac modalidadId) {
        this.modalidadId = modalidadId;
    }

    public SbUsuario getGerenteId() {
        return gerenteId;
    }

    public void setGerenteId(SbUsuario gerenteId) {
        this.gerenteId = gerenteId;
    }

    public AmaCatalogo getArmaTiroId() {
        return armaTiroId;
    }

    public void setArmaTiroId(AmaCatalogo armaTiroId) {
        this.armaTiroId = armaTiroId;
    }

    public AmaCatalogo getArmaTeoId() {
        return armaTeoId;
    }

    public void setArmaTeoId(AmaCatalogo armaTeoId) {
        this.armaTeoId = armaTeoId;
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
        if (!(object instanceof TurConstancia)) {
            return false;
        }
        TurConstancia other = (TurConstancia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.turreg.data.TurConstancia[ id=" + id + " ]";
    }

}
