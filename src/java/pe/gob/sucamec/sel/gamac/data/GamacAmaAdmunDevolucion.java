/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.data;

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
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author MSALINAS
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_ADMUN_DEVOLUCION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacAmaAdmunDevolucion.findAll", query = "SELECT a FROM GamacAmaAdmunDevolucion a"),
    @NamedQuery(name = "GamacAmaAdmunDevolucion.findById", query = "SELECT a FROM GamacAmaAdmunDevolucion a WHERE a.id = :id"),
    @NamedQuery(name = "GamacAmaAdmunDevolucion.findByFechaDevolucion", query = "SELECT a FROM GamacAmaAdmunDevolucion a WHERE a.fechaDevolucion = :fechaDevolucion"),
    @NamedQuery(name = "GamacAmaAdmunDevolucion.findByMotivoDevolucion", query = "SELECT a FROM GamacAmaAdmunDevolucion a WHERE a.motivoDevolucion = :motivoDevolucion"),
    @NamedQuery(name = "GamacAmaAdmunDevolucion.findByActivo", query = "SELECT a FROM GamacAmaAdmunDevolucion a WHERE a.activo = :activo"),
    @NamedQuery(name = "GamacAmaAdmunDevolucion.findByAudLogin", query = "SELECT a FROM GamacAmaAdmunDevolucion a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "GamacAmaAdmunDevolucion.findByAudNumIp", query = "SELECT a FROM GamacAmaAdmunDevolucion a WHERE a.audNumIp = :audNumIp")})
public class GamacAmaAdmunDevolucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_ADMUN_DEVOLUCION")
    @SequenceGenerator(name = "SEQ_AMA_ADMUN_DEVOLUCION", schema = "BDINTEGRADO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FECHA_DEVOLUCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDevolucion;
    @Size(max = 20)
    @Column(name = "MOTIVO_DEVOLUCION")
    private String motivoDevolucion;
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
    @JoinColumn(name = "AMA_ADMUN_DETALLE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaAdmunDetalleTrans amaAdmunDetalleId;
    @JoinColumn(name = "SOLICITANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbPersona solicitanteId;

    public GamacAmaAdmunDevolucion() {
    }

    public GamacAmaAdmunDevolucion(Long id) {
        this.id = id;
    }

    public GamacAmaAdmunDevolucion(Long id, short activo, String audLogin, String audNumIp) {
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

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getMotivoDevolucion() {
        return motivoDevolucion;
    }

    public void setMotivoDevolucion(String motivoDevolucion) {
        this.motivoDevolucion = motivoDevolucion;
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

    public GamacAmaAdmunDetalleTrans getAmaAdmunDetalleId() {
        return amaAdmunDetalleId;
    }

    public void setAmaAdmunDetalleId(GamacAmaAdmunDetalleTrans amaAdmunDetalleId) {
        this.amaAdmunDetalleId = amaAdmunDetalleId;
    }

    public GamacSbPersona getSolicitanteId() {
        return solicitanteId;
    }

    public void setSolicitanteId(GamacSbPersona solicitanteId) {
        this.solicitanteId = solicitanteId;
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
        if (!(object instanceof GamacAmaAdmunDevolucion)) {
            return false;
        }
        GamacAmaAdmunDevolucion other = (GamacAmaAdmunDevolucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.AmaAdmunDevolucion[ id=" + id + " ]";
    }
    
}
