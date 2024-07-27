/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
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
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_DEPOSITO_CONTRATO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppDepositoContrato.findAll", query = "SELECT e FROM EppDepositoContrato e"),
    @NamedQuery(name = "EppDepositoContrato.findById", query = "SELECT e FROM EppDepositoContrato e WHERE e.id = :id"),
    @NamedQuery(name = "EppDepositoContrato.findByFechaInicio", query = "SELECT e FROM EppDepositoContrato e WHERE e.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "EppDepositoContrato.findByFechaVigencia", query = "SELECT e FROM EppDepositoContrato e WHERE e.fechaVigencia = :fechaVigencia"),
    @NamedQuery(name = "EppDepositoContrato.findByArea", query = "SELECT e FROM EppDepositoContrato e WHERE e.area = :area"),
    @NamedQuery(name = "EppDepositoContrato.findByActivo", query = "SELECT e FROM EppDepositoContrato e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppDepositoContrato.findByAudLogin", query = "SELECT e FROM EppDepositoContrato e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppDepositoContrato.findByAudNumIp", query = "SELECT e FROM EppDepositoContrato e WHERE e.audNumIp = :audNumIp")})
public class EppDepositoContrato implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_DEPOSITO_CONTRATO")
    @SequenceGenerator(name = "SEQ_EPP_DEPOSITO_CONTRATO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_DEPOSITO_CONTRATO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_VIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVigencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AREA")
    private Double area;
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
    @JoinColumn(name = "TIPO_CONTRATO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoContratoId;
    @JoinColumn(name = "BENEFICIARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona beneficiarioId;
    @JoinColumn(name = "DEPOSITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppTallerdeposito depositoId;
    @JoinColumn(name = "REPRESENTANTE_DEP_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppCarne representanteDepId;

    public EppDepositoContrato() {
    }

    public EppDepositoContrato(Long id) {
        this.id = id;
    }

    public EppDepositoContrato(Long id, Date fechaInicio, Date fechaVigencia, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaVigencia = fechaVigencia;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
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

    public TipoExplosivoGt getTipoContratoId() {
        return tipoContratoId;
    }

    public void setTipoContratoId(TipoExplosivoGt tipoContratoId) {
        this.tipoContratoId = tipoContratoId;
    }

    public SbPersona getBeneficiarioId() {
        return beneficiarioId;
    }

    public void setBeneficiarioId(SbPersona beneficiarioId) {
        this.beneficiarioId = beneficiarioId;
    }

    public EppTallerdeposito getDepositoId() {
        return depositoId;
    }

    public void setDepositoId(EppTallerdeposito depositoId) {
        this.depositoId = depositoId;
    }

    public EppCarne getRepresentanteDepId() {
        return representanteDepId;
    }

    public void setRepresentanteDepId(EppCarne representanteDepId) {
        this.representanteDepId = representanteDepId;
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
        if (!(object instanceof EppDepositoContrato)) {
            return false;
        }
        EppDepositoContrato other = (EppDepositoContrato) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppDepositoContrato[ id=" + id + " ]";
    }
    
}
