/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

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
@Table(name = "SSP_SERVICIO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspServicio.findAll", query = "SELECT s FROM SspServicio s"),
    @NamedQuery(name = "SspServicio.findById", query = "SELECT s FROM SspServicio s WHERE s.id = :id"),
    @NamedQuery(name = "SspServicio.findByFecha", query = "SELECT s FROM SspServicio s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspServicio.findByActivo", query = "SELECT s FROM SspServicio s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspServicio.findByAudLogin", query = "SELECT s FROM SspServicio s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspServicio.findByAudNumIp", query = "SELECT s FROM SspServicio s WHERE s.audNumIp = :audNumIp")})
public class SspServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_SERVICIO")
    @SequenceGenerator(name = "SEQ_SSP_SERVICIO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_SERVICIO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
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
    
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;
    
    @JoinColumn(name = "SERVICIO_PRESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad ServicioPrestadoId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicioId")
    private List<SspForma> sspFormaList;

    public SspServicio() {
    }

    public SspServicio(Long id) {
        this.id = id;
    }

    public SspServicio(Long id, Date fecha, short activo, String audLogin, String audNumIp) {
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

    @XmlTransient
    public List<SspForma> getSspFormaList() {
        return sspFormaList;
    }

    public void setSspFormaList(List<SspForma> sspFormaList) {
        this.sspFormaList = sspFormaList;
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
        if (!(object instanceof SspServicio)) {
            return false;
        }
        SspServicio other = (SspServicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspServicio[ id=" + id + " ]";
    }

    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
    }

    public TipoSeguridad getServicioPrestadoId() {
        return ServicioPrestadoId;
    }

    public void setServicioPrestadoId(TipoSeguridad ServicioPrestadoId) {
        this.ServicioPrestadoId = ServicioPrestadoId;
    }

    
    
}
