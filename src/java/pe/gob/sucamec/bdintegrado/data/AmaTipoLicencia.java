/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
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

/**
 *
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_TIPO_LICENCIA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaTipoLicencia.findAll", query = "SELECT a FROM AmaTipoLicencia a"),
    @NamedQuery(name = "AmaTipoLicencia.findById", query = "SELECT a FROM AmaTipoLicencia a WHERE a.id = :id"),
    @NamedQuery(name = "AmaTipoLicencia.findByNroExpediente", query = "SELECT a FROM AmaTipoLicencia a WHERE a.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "AmaTipoLicencia.findByActivo", query = "SELECT a FROM AmaTipoLicencia a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaTipoLicencia.findByAudLogin", query = "SELECT a FROM AmaTipoLicencia a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaTipoLicencia.findByAudNumIp", query = "SELECT a FROM AmaTipoLicencia a WHERE a.audNumIp = :audNumIp")})
public class AmaTipoLicencia implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_TIPO_LICENCIA")
    @SequenceGenerator(name = "SEQ_AMA_TIPO_LICENCIA", schema = "name=", sequenceName = "SEQ_AMA_TIPO_LICENCIA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
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
    @JoinColumn(name = "LICENCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaLicenciaDeUso licenciaId;
    @JoinColumn(name = "MODALIDAD_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac modalidadId;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac estadoId;
    @Column(name = "FECHA_NULIDAD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNulidad;
    @Size(max = 500)
    @Column(name = "MOTIVO_NULIDAD")
    private String motivoNulidad;
    @Size(max = 50)
    @Column(name = "RESOLUCION_NULIDAD")
    private String resolucionNulidad;
    @JoinColumn(name = "MOTIVO_NULIDAD_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac motivoNulidadId;    
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;

    public AmaTipoLicencia() {
    }

    public AmaTipoLicencia(Long id) {
        this.id = id;
    }

    public AmaTipoLicencia(Long id, String nroExpediente, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroExpediente = nroExpediente;
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

    public AmaLicenciaDeUso getLicenciaId() {
        return licenciaId;
    }

    public void setLicenciaId(AmaLicenciaDeUso licenciaId) {
        this.licenciaId = licenciaId;
    }

    public TipoGamac getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoGamac estadoId) {
        this.estadoId = estadoId;
    }

    public Date getFechaNulidad() {
        return fechaNulidad;
    }

    public void setFechaNulidad(Date fechaNulidad) {
        this.fechaNulidad = fechaNulidad;
    }

    public String getMotivoNulidad() {
        return motivoNulidad;
    }

    public void setMotivoNulidad(String motivoNulidad) {
        this.motivoNulidad = motivoNulidad;
    }

    public String getResolucionNulidad() {
        return resolucionNulidad;
    }

    public void setResolucionNulidad(String resolucionNulidad) {
        this.resolucionNulidad = resolucionNulidad;
    }

    public TipoGamac getMotivoNulidadId() {
        return motivoNulidadId;
    }

    public void setMotivoNulidadId(TipoGamac motivoNulidadId) {
        this.motivoNulidadId = motivoNulidadId;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
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
        if (!(object instanceof AmaTipoLicencia)) {
            return false;
        }
        AmaTipoLicencia other = (AmaTipoLicencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaTipoLicencia[ id=" + id + " ]";
    }

    /**
     * @return the modalidadId
     */
    public TipoGamac getModalidadId() {
        return modalidadId;
    }

    /**
     * @param modalidadId the modalidadId to set
     */
    public void setModalidadId(TipoGamac modalidadId) {
        this.modalidadId = modalidadId;
    }
    
}
