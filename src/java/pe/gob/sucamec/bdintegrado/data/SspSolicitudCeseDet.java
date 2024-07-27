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
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_SOLICITUD_CESE_DET", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspSolicitudCeseDet.findAll", query = "SELECT s FROM SspSolicitudCeseDet s"),
    @NamedQuery(name = "SspSolicitudCeseDet.findById", query = "SELECT s FROM SspSolicitudCeseDet s WHERE s.id = :id"),
    @NamedQuery(name = "SspSolicitudCeseDet.findByNroExpCese", query = "SELECT s FROM SspSolicitudCeseDet s WHERE s.nroExpCese = :nroExpCese"),
    @NamedQuery(name = "SspSolicitudCeseDet.findByNroCarne", query = "SELECT s FROM SspSolicitudCeseDet s WHERE s.nroCarne = :nroCarne"),
    @NamedQuery(name = "SspSolicitudCeseDet.findByNroExpDevo", query = "SELECT s FROM SspSolicitudCeseDet s WHERE s.nroExpDevo = :nroExpDevo"),
    @NamedQuery(name = "SspSolicitudCeseDet.findByEstadoDevoId", query = "SELECT s FROM SspSolicitudCeseDet s WHERE s.estadoDevoId = :estadoDevoId"),
    @NamedQuery(name = "SspSolicitudCeseDet.findByFechaDevolucion", query = "SELECT s FROM SspSolicitudCeseDet s WHERE s.fechaDevolucion = :fechaDevolucion"),
    @NamedQuery(name = "SspSolicitudCeseDet.findByDenunciaPol", query = "SELECT s FROM SspSolicitudCeseDet s WHERE s.denunciaPol = :denunciaPol"),
    @NamedQuery(name = "SspSolicitudCeseDet.findByActivo", query = "SELECT s FROM SspSolicitudCeseDet s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspSolicitudCeseDet.findByAudLogin", query = "SELECT s FROM SspSolicitudCeseDet s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspSolicitudCeseDet.findByAudNumIp", query = "SELECT s FROM SspSolicitudCeseDet s WHERE s.audNumIp = :audNumIp")})
public class SspSolicitudCeseDet implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_SOLICITUD_CESE_DET")
    @SequenceGenerator(name = "SEQ_SSP_SOLICITUD_CESE_DET", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_SOLICITUD_CESE_DET", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NRO_EXP_CESE")
    private String nroExpCese;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NRO_CARNE")
    private String nroCarne;
    @Size(max = 20)
    @Column(name = "NRO_EXP_DEVO")
    private String nroExpDevo;
    @Column(name = "ESTADO_DEVO_ID")
    private Long estadoDevoId;
    @Column(name = "FECHA_DEVOLUCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDevolucion;
    @Size(max = 50)
    @Column(name = "DENUNCIA_POL")
    private String denunciaPol;
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
    @JoinColumn(name = "SOLICITUD_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspSolicitudCese solicitudId;
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;

    public SspSolicitudCeseDet() {
    }

    public SspSolicitudCeseDet(Long id) {
        this.id = id;
    }

    public SspSolicitudCeseDet(Long id, String nroExpCese, String nroCarne, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroExpCese = nroExpCese;
        this.nroCarne = nroCarne;
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

    public String getNroExpCese() {
        return nroExpCese;
    }

    public void setNroExpCese(String nroExpCese) {
        this.nroExpCese = nroExpCese;
    }

    public String getNroCarne() {
        return nroCarne;
    }

    public void setNroCarne(String nroCarne) {
        this.nroCarne = nroCarne;
    }

    public String getNroExpDevo() {
        return nroExpDevo;
    }

    public void setNroExpDevo(String nroExpDevo) {
        this.nroExpDevo = nroExpDevo;
    }

    public Long getEstadoDevoId() {
        return estadoDevoId;
    }

    public void setEstadoDevoId(Long estadoDevoId) {
        this.estadoDevoId = estadoDevoId;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getDenunciaPol() {
        return denunciaPol;
    }

    public void setDenunciaPol(String denunciaPol) {
        this.denunciaPol = denunciaPol;
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

    public SspSolicitudCese getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(SspSolicitudCese solicitudId) {
        this.solicitudId = solicitudId;
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
        if (!(object instanceof SspSolicitudCeseDet)) {
            return false;
        }
        SspSolicitudCeseDet other = (SspSolicitudCeseDet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspSolicitudCeseDet[ id=" + id + " ]";
    }
    
    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    
}
