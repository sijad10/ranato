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
@Table(name = "SSP_VERSION_CARNE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspVersionCarne.findAll", query = "SELECT s FROM SspVersionCarne s"),
    @NamedQuery(name = "SspVersionCarne.findById", query = "SELECT s FROM SspVersionCarne s WHERE s.id = :id"),
    @NamedQuery(name = "SspVersionCarne.findByFondo", query = "SELECT s FROM SspVersionCarne s WHERE s.fondo = :fondo"),
    @NamedQuery(name = "SspVersionCarne.findByFecha", query = "SELECT s FROM SspVersionCarne s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspVersionCarne.findByActivo", query = "SELECT s FROM SspVersionCarne s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspVersionCarne.findByAudLogin", query = "SELECT s FROM SspVersionCarne s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspVersionCarne.findByAudNumIp", query = "SELECT s FROM SspVersionCarne s WHERE s.audNumIp = :audNumIp")})
public class SspVersionCarne implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_VERSION_CARNE")
    @SequenceGenerator(name = "SEQ_SSP_VERSION_CARNE", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_VERSION_CARNE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "FONDO")
    private String fondo;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "ACTIVO")
    private Short activo;
    @Size(max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Size(max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "versionId")
    private List<SspFormatoCarne> sspFormatoCarneList;

    public SspVersionCarne() {
    }

    public SspVersionCarne(Long id) {
        this.id = id;
    }

    public SspVersionCarne(Long id, String fondo) {
        this.id = id;
        this.fondo = fondo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFondo() {
        return fondo;
    }

    public void setFondo(String fondo) {
        this.fondo = fondo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Short getActivo() {
        return activo;
    }

    public void setActivo(Short activo) {
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
    public List<SspFormatoCarne> getSspFormatoCarneList() {
        return sspFormatoCarneList;
    }

    public void setSspFormatoCarneList(List<SspFormatoCarne> sspFormatoCarneList) {
        this.sspFormatoCarneList = sspFormatoCarneList;
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
        if (!(object instanceof SspVersionCarne)) {
            return false;
        }
        SspVersionCarne other = (SspVersionCarne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspVersionCarne[ id=" + id + " ]";
    }
    
}
