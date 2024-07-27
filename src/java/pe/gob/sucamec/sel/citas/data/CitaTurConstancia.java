/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.bdintegrado.data.AmaLicenciaDeUso;

/**
 *
 * @author msalinas
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "TUR_CONSTANCIA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaTurConstancia.findAll", query = "SELECT t FROM CitaTurConstancia t"),
    @NamedQuery(name = "CitaTurConstancia.findById", query = "SELECT t FROM CitaTurConstancia t WHERE t.id = :id"),
    @NamedQuery(name = "CitaTurConstancia.findByRutaFoto", query = "SELECT t FROM CitaTurConstancia t WHERE t.rutaFoto = :rutaFoto"),
    @NamedQuery(name = "CitaTurConstancia.findByNota", query = "SELECT t FROM CitaTurConstancia t WHERE t.nota = :nota"),
    @NamedQuery(name = "CitaTurConstancia.findByCantMunicion", query = "SELECT t FROM CitaTurConstancia t WHERE t.cantMunicion = :cantMunicion"),
    @NamedQuery(name = "CitaTurConstancia.findByCantTiro", query = "SELECT t FROM CitaTurConstancia t WHERE t.cantTiro = :cantTiro"),
    @NamedQuery(name = "CitaTurConstancia.findByCantImpacto", query = "SELECT t FROM CitaTurConstancia t WHERE t.cantImpacto = :cantImpacto"),
    @NamedQuery(name = "CitaTurConstancia.findByFechaEmision", query = "SELECT t FROM CitaTurConstancia t WHERE t.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "CitaTurConstancia.findByFechaVencimiento", query = "SELECT t FROM CitaTurConstancia t WHERE t.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "CitaTurConstancia.findByCodVerifica", query = "SELECT t FROM CitaTurConstancia t WHERE t.codVerifica = :codVerifica"),
    @NamedQuery(name = "CitaTurConstancia.findByAudUsuTeoPrac", query = "SELECT t FROM CitaTurConstancia t WHERE t.audUsuTeoPrac = :audUsuTeoPrac"),
    @NamedQuery(name = "CitaTurConstancia.findByAudIpTeoPrac", query = "SELECT t FROM CitaTurConstancia t WHERE t.audIpTeoPrac = :audIpTeoPrac"),
    @NamedQuery(name = "CitaTurConstancia.findByAudUsuTiro", query = "SELECT t FROM CitaTurConstancia t WHERE t.audUsuTiro = :audUsuTiro"),
    @NamedQuery(name = "CitaTurConstancia.findByAudIpTiro", query = "SELECT t FROM CitaTurConstancia t WHERE t.audIpTiro = :audIpTiro"),
    @NamedQuery(name = "CitaTurConstancia.findByNroConstancia", query = "SELECT t FROM CitaTurConstancia t WHERE t.nroConstancia = :nroConstancia"),
    @NamedQuery(name = "CitaTurConstancia.findByHashQr", query = "SELECT t FROM CitaTurConstancia t WHERE t.hashQr = :hashQr"),
    @NamedQuery(name = "CitaTurConstancia.findByActivo", query = "SELECT t FROM CitaTurConstancia t WHERE t.activo = :activo"),
    @NamedQuery(name = "CitaTurConstancia.findByAudLogin", query = "SELECT t FROM CitaTurConstancia t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "CitaTurConstancia.findByAudNumIp", query = "SELECT t FROM CitaTurConstancia t WHERE t.audNumIp = :audNumIp")})
public class CitaTurConstancia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TUR_CONSTANCIA")
    @SequenceGenerator(name = "SEQ_TUR_CONSTANCIA", schema = "BDINTEGRADO", sequenceName = "SEQ_TUR_CONSTANCIA", allocationSize = 1)
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
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "MODALIDAD_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoGamac modalidadId;
    @JoinColumn(name = "ARMA_TEO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaAmaCatalogo armaTeoId;
    @JoinColumn(name = "ARMA_TIRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaAmaCatalogo armaTiroId;
    @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTurTurno turnoId;
    @JoinColumn(name = "COMPROBANTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTurComprobante comprobanteId;
    @JoinColumn(name = "EXA_TEO_PRAC", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoGamac exaTeoPrac;
    @JoinColumn(name = "EXA_TIRO", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoGamac exaTiro;
    @JoinColumn(name = "RESUL_GRAL", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoGamac resulGral;
    @JoinColumn(name = "TIPO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoGamac tipoArmaId;
    @JoinColumn(name = "LICENCIA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaLicenciaDeUso licenciaId;
    
    public CitaTurConstancia() {
    }

    public CitaTurConstancia(Long id) {
        this.id = id;
    }

    public CitaTurConstancia(Long id, String rutaFoto, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.rutaFoto = rutaFoto;
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

    public CitaTipoGamac getModalidadId() {
        return modalidadId;
    }

    public void setModalidadId(CitaTipoGamac modalidadId) {
        this.modalidadId = modalidadId;
    }

    public CitaAmaCatalogo getArmaTeoId() {
        return armaTeoId;
    }

    public void setArmaTeoId(CitaAmaCatalogo armaTeoId) {
        this.armaTeoId = armaTeoId;
    }

    public CitaAmaCatalogo getArmaTiroId() {
        return armaTiroId;
    }

    public void setArmaTiroId(CitaAmaCatalogo armaTiroId) {
        this.armaTiroId = armaTiroId;
    }

    public CitaTurTurno getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(CitaTurTurno turnoId) {
        this.turnoId = turnoId;
    }

    public CitaTurComprobante getComprobanteId() {
        return comprobanteId;
    }

    public void setComprobanteId(CitaTurComprobante comprobanteId) {
        this.comprobanteId = comprobanteId;
    }

    public CitaTipoGamac getExaTeoPrac() {
        return exaTeoPrac;
    }

    public void setExaTeoPrac(CitaTipoGamac exaTeoPrac) {
        this.exaTeoPrac = exaTeoPrac;
    }

    public CitaTipoGamac getExaTiro() {
        return exaTiro;
    }

    public void setExaTiro(CitaTipoGamac exaTiro) {
        this.exaTiro = exaTiro;
    }

    public CitaTipoGamac getResulGral() {
        return resulGral;
    }

    public void setResulGral(CitaTipoGamac resulGral) {
        this.resulGral = resulGral;
    }

    public CitaTipoGamac getTipoArmaId() {
        return tipoArmaId;
    }

    public void setTipoArmaId(CitaTipoGamac tipoArmaId) {
        this.tipoArmaId = tipoArmaId;
    }

    public AmaLicenciaDeUso getLicenciaId() {
        return licenciaId;
    }

    public void setLicenciaId(AmaLicenciaDeUso licenciaId) {
        this.licenciaId = licenciaId;
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
        if (!(object instanceof CitaTurConstancia)) {
            return false;
        }
        CitaTurConstancia other = (CitaTurConstancia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.citas.data.CitaTurConstancia[ id=" + id + " ]";
    }

}
