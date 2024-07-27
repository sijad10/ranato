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
@Table(name = "SSP_FORMATO_CARNE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspFormatoCarne.findAll", query = "SELECT s FROM SspFormatoCarne s"),
    @NamedQuery(name = "SspFormatoCarne.findById", query = "SELECT s FROM SspFormatoCarne s WHERE s.id = :id"),
    @NamedQuery(name = "SspFormatoCarne.findByFecha", query = "SELECT s FROM SspFormatoCarne s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspFormatoCarne.findByActivo", query = "SELECT s FROM SspFormatoCarne s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspFormatoCarne.findByAudLogin", query = "SELECT s FROM SspFormatoCarne s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspFormatoCarne.findByAudNumIp", query = "SELECT s FROM SspFormatoCarne s WHERE s.audNumIp = :audNumIp")})
public class SspFormatoCarne implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_FORMATO_CARNE")
    @SequenceGenerator(name = "SEQ_SSP_FORMATO_CARNE", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_FORMATO_CARNE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
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
    @JoinColumn(name = "VERSION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspVersionCarne versionId;
    @JoinColumn(name = "FIRMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbFirma firmaId;

    public SspFormatoCarne() {
    }

    public SspFormatoCarne(Long id) {
        this.id = id;
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

    public SspVersionCarne getVersionId() {
        return versionId;
    }

    public void setVersionId(SspVersionCarne versionId) {
        this.versionId = versionId;
    }

    public SbFirma getFirmaId() {
        return firmaId;
    }

    public void setFirmaId(SbFirma firmaId) {
        this.firmaId = firmaId;
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
        if (!(object instanceof SspFormatoCarne)) {
            return false;
        }
        SspFormatoCarne other = (SspFormatoCarne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspFormatoCarne[ id=" + id + " ]";
    }

}
