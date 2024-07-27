/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_REG_EMP_PROCESO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaRegEmpProceso.findAll", query = "SELECT a FROM AmaRegEmpProceso a"),
    @NamedQuery(name = "AmaRegEmpProceso.findById", query = "SELECT a FROM AmaRegEmpProceso a WHERE a.id = :id"),
    @NamedQuery(name = "AmaRegEmpProceso.findByExpedienteMaster", query = "SELECT a FROM AmaRegEmpProceso a WHERE a.expedienteMaster = :expedienteMaster"),
    @NamedQuery(name = "AmaRegEmpProceso.findByFechaIni", query = "SELECT a FROM AmaRegEmpProceso a WHERE a.fechaIni = :fechaIni"),
    @NamedQuery(name = "AmaRegEmpProceso.findByFechaFin", query = "SELECT a FROM AmaRegEmpProceso a WHERE a.fechaFin = :fechaFin"),
    @NamedQuery(name = "AmaRegEmpProceso.findByEstado", query = "SELECT a FROM AmaRegEmpProceso a WHERE a.estado = :estado"),
    @NamedQuery(name = "AmaRegEmpProceso.findByActivo", query = "SELECT a FROM AmaRegEmpProceso a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaRegEmpProceso.findByAudLogin", query = "SELECT a FROM AmaRegEmpProceso a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaRegEmpProceso.findByAudNumIp", query = "SELECT a FROM AmaRegEmpProceso a WHERE a.audNumIp = :audNumIp")})
public class AmaRegEmpProceso implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_REG_EMP_PROCESO")
    @SequenceGenerator(name = "SEQ_AMA_REG_EMP_PROCESO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_REG_EMP_PROCESO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "EXPEDIENTE_MASTER")
    private String expedienteMaster;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTADO")
    private short estado;
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
    @JoinTable(schema="BDINTEGRADO", name = "AMA_REG_PRO_ARMA", joinColumns = {
        @JoinColumn(name = "REG_PROCESO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "REG_ARMA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<AmaRegEmpArma> amaRegEmpArmaList;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;

    public AmaRegEmpProceso() {
    }

    public AmaRegEmpProceso(Long id) {
        this.id = id;
    }

    public AmaRegEmpProceso(Long id, String expedienteMaster, Date fechaIni, short estado, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.expedienteMaster = expedienteMaster;
        this.fechaIni = fechaIni;
        this.estado = estado;
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

    public String getExpedienteMaster() {
        return expedienteMaster;
    }

    public void setExpedienteMaster(String expedienteMaster) {
        this.expedienteMaster = expedienteMaster;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
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
    public List<AmaRegEmpArma> getAmaRegEmpArmaList() {
        return amaRegEmpArmaList;
    }

    public void setAmaRegEmpArmaList(List<AmaRegEmpArma> amaRegEmpArmaList) {
        this.amaRegEmpArmaList = amaRegEmpArmaList;
    }

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
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
        if (!(object instanceof AmaRegEmpProceso)) {
            return false;
        }
        AmaRegEmpProceso other = (AmaRegEmpProceso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaRegEmpProceso[ id=" + id + " ]";
    }
    
}
