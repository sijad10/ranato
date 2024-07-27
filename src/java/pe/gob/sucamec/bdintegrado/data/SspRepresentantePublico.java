/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

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
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author locador772.ogtic
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_REPRESENTANTE_PUBLICO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspRepresentantePublico.findAll", query = "SELECT s FROM SspRepresentantePublico s"),
    @NamedQuery(name = "SspRepresentantePublico.findById", query = "SELECT s FROM SspRepresentantePublico s WHERE s.id = :id"),
    @NamedQuery(name = "SspRepresentantePublico.findByNombreDocuemnto", query = "SELECT s FROM SspRepresentantePublico s WHERE s.nombreDocuemnto = :nombreDocuemnto"),
    @NamedQuery(name = "SspRepresentantePublico.findByNumeroDocuemnto", query = "SELECT s FROM SspRepresentantePublico s WHERE s.numeroDocuemnto = :numeroDocuemnto"),
    @NamedQuery(name = "SspRepresentantePublico.findByFechaEmision", query = "SELECT s FROM SspRepresentantePublico s WHERE s.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "SspRepresentantePublico.findByNombreArchivo", query = "SELECT s FROM SspRepresentantePublico s WHERE s.nombreArchivo = :nombreArchivo"),
    @NamedQuery(name = "SspRepresentantePublico.findByFecha", query = "SELECT s FROM SspRepresentantePublico s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspRepresentantePublico.findByActivo", query = "SELECT s FROM SspRepresentantePublico s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspRepresentantePublico.findByAudLogin", query = "SELECT s FROM SspRepresentantePublico s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspRepresentantePublico.findByAudNumIp", query = "SELECT s FROM SspRepresentantePublico s WHERE s.audNumIp = :audNumIp")})
public class SspRepresentantePublico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_REPRESENTANTE_PUBLICO")
    @SequenceGenerator(name = "SEQ_SSP_REPRESENTANTE_PUBLICO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_REPRESENTANTE_PUBLICO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @Size(max = 200)
    @Column(name = "NOMBRE_DOCUEMNTO")
    private String nombreDocuemnto;
    
    @Size(max = 100)
    @Column(name = "NUMERO_DOCUEMNTO")
    private String numeroDocuemnto;
    
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    
    @Size(max = 20)
    @Column(name = "NOMBRE_ARCHIVO")
    private String nombreArchivo;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
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
    
    @JoinColumn(name = "TIPO_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoDocumentoId;
    
    @JoinColumn(name = "REPRESENTANTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbRelacionPersonaGt representanteId;

    public SspRepresentantePublico() {
    }

    public SspRepresentantePublico(Long id) {
        this.id = id;
    }

    public SspRepresentantePublico(Long id, Date fecha, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fecha = fecha;
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

    public String getNombreDocuemnto() {
        return nombreDocuemnto;
    }

    public void setNombreDocuemnto(String nombreDocuemnto) {
        this.nombreDocuemnto = nombreDocuemnto;
    }

    public String getNumeroDocuemnto() {
        return numeroDocuemnto;
    }

    public void setNumeroDocuemnto(String numeroDocuemnto) {
        this.numeroDocuemnto = numeroDocuemnto;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public TipoBaseGt getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public void setTipoDocumentoId(TipoBaseGt tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId;
    }

    

    public SbRelacionPersonaGt getRepresentanteId() {
        return representanteId;
    }

    public void setRepresentanteId(SbRelacionPersonaGt representanteId) {
        this.representanteId = representanteId;
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
        if (!(object instanceof SspRepresentantePublico)) {
            return false;
        }
        SspRepresentantePublico other = (SspRepresentantePublico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspRepresentantePublico[ id=" + id + " ]";
    }
    
}
