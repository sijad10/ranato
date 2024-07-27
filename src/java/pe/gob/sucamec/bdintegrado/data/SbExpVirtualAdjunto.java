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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_EXP_VIRTUAL_ADJUNTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbExpVirtualAdjunto.findAll", query = "SELECT s FROM SbExpVirtualAdjunto s"),
    @NamedQuery(name = "SbExpVirtualAdjunto.findById", query = "SELECT s FROM SbExpVirtualAdjunto s WHERE s.id = :id"),
    @NamedQuery(name = "SbExpVirtualAdjunto.findByFechaRegistro", query = "SELECT s FROM SbExpVirtualAdjunto s WHERE s.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "SbExpVirtualAdjunto.findByNombre", query = "SELECT s FROM SbExpVirtualAdjunto s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbExpVirtualAdjunto.findByActivo", query = "SELECT s FROM SbExpVirtualAdjunto s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbExpVirtualAdjunto.findByAudLogin", query = "SELECT s FROM SbExpVirtualAdjunto s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbExpVirtualAdjunto.findByAudNumIp", query = "SELECT s FROM SbExpVirtualAdjunto s WHERE s.audNumIp = :audNumIp")})
public class SbExpVirtualAdjunto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_EXP_VIRTUAL_ADJUNTO")
    @SequenceGenerator(name = "SEQ_SB_EXP_VIRTUAL_ADJUNTO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_EXP_VIRTUAL_ADJUNTO", allocationSize = 1)
    @Column(name = "ID")    
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
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
    @JoinColumn(name = "EXP_VIRTUAL_REQUISITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbExpVirtualRequisito expVirtualRequisitoId;
    @Transient
    private byte[] adjuntoBytes;

    public byte[] getAdjuntoBytes() {
        return adjuntoBytes;
    }

    public void setAdjuntoBytes(byte[] adjuntoBytes) {
        this.adjuntoBytes = adjuntoBytes;
    }

    public SbExpVirtualAdjunto() {
    }

    public SbExpVirtualAdjunto(Long id) {
        this.id = id;
    }

    public SbExpVirtualAdjunto(Long id, Date fechaRegistro, String nombre, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaRegistro = fechaRegistro;
        this.nombre = nombre;
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

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public SbExpVirtualRequisito getExpVirtualRequisitoId() {
        return expVirtualRequisitoId;
    }

    public void setExpVirtualRequisitoId(SbExpVirtualRequisito expVirtualRequisitoId) {
        this.expVirtualRequisitoId = expVirtualRequisitoId;
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
        if (!(object instanceof SbExpVirtualAdjunto)) {
            return false;
        }
        SbExpVirtualAdjunto other = (SbExpVirtualAdjunto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.SbExpVirtualAdjunto[ id=" + id + " ]";
    }
    
}
