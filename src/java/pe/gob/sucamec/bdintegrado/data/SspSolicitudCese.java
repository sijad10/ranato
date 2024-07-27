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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_SOLICITUD_CESE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspSolicitudCese.findAll", query = "SELECT s FROM SspSolicitudCese s"),
    @NamedQuery(name = "SspSolicitudCese.findById", query = "SELECT s FROM SspSolicitudCese s WHERE s.id = :id"),
    @NamedQuery(name = "SspSolicitudCese.findByNroSolicitud", query = "SELECT s FROM SspSolicitudCese s WHERE s.nroSolicitud = :nroSolicitud"),
    @NamedQuery(name = "SspSolicitudCese.findByCantidadCese", query = "SELECT s FROM SspSolicitudCese s WHERE s.cantidadCese = :cantidadCese"),
    @NamedQuery(name = "SspSolicitudCese.findByFechaSolicitud", query = "SELECT s FROM SspSolicitudCese s WHERE s.fechaSolicitud = :fechaSolicitud"),
    @NamedQuery(name = "SspSolicitudCese.findByActivo", query = "SELECT s FROM SspSolicitudCese s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspSolicitudCese.findByAudLogin", query = "SELECT s FROM SspSolicitudCese s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspSolicitudCese.findByAudNumIp", query = "SELECT s FROM SspSolicitudCese s WHERE s.audNumIp = :audNumIp")})
public class SspSolicitudCese implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_SOLICITUD_CESE")
    @SequenceGenerator(name = "SEQ_SSP_SOLICITUD_CESE", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_SOLICITUD_CESE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NRO_SOLICITUD")
    private String nroSolicitud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD_CESE")
    private Long cantidadCese;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_SOLICITUD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
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
    @JoinColumn(name = "RECIBO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbRecibos reciboId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "solicitudId")
    private List<SspSolicitudCeseDet> sspSolicitudCeseDetList;
    @JoinColumn(name = "TIPO_SOLICITUD_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoSeguridad tipoSolicitudId;

    public SspSolicitudCese() {
    }

    public SspSolicitudCese(Long id) {
        this.id = id;
    }

    public SspSolicitudCese(Long id, String nroSolicitud, Long cantidadCese, Date fechaSolicitud, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroSolicitud = nroSolicitud;
        this.cantidadCese = cantidadCese;
        this.fechaSolicitud = fechaSolicitud;
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

    public String getNroSolicitud() {
        return nroSolicitud;
    }

    public void setNroSolicitud(String nroSolicitud) {
        this.nroSolicitud = nroSolicitud;
    }

    public Long getCantidadCese() {
        return cantidadCese;
    }

    public void setCantidadCese(Long cantidadCese) {
        this.cantidadCese = cantidadCese;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
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

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
    }

    @XmlTransient
    public List<SspSolicitudCeseDet> getSspSolicitudCeseDetList() {
        return sspSolicitudCeseDetList;
    }

    public void setSspSolicitudCeseDetList(List<SspSolicitudCeseDet> sspSolicitudCeseDetList) {
        this.sspSolicitudCeseDetList = sspSolicitudCeseDetList;
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
        if (!(object instanceof SspSolicitudCese)) {
            return false;
        }
        SspSolicitudCese other = (SspSolicitudCese) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspSolicitudCese[ id=" + id + " ]";
    }

    public SbRecibos getReciboId() {
        return reciboId;
    }

    public void setReciboId(SbRecibos reciboId) {
        this.reciboId = reciboId;
    }    
    
    public TipoSeguridad getTipoSolicitudId() {
        return tipoSolicitudId;
    }

    public void setTipoSolicitudId(TipoSeguridad tipoSolicitudId) {
        this.tipoSolicitudId = tipoSolicitudId;
    }
}
