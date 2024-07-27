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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_FOTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppFoto.findAll", query = "SELECT e FROM EppFoto e"),
    @NamedQuery(name = "EppFoto.findById", query = "SELECT e FROM EppFoto e WHERE e.id = :id"),
    @NamedQuery(name = "EppFoto.findByRutaFile", query = "SELECT e FROM EppFoto e WHERE e.rutaFile = :rutaFile"),
    @NamedQuery(name = "EppFoto.findByActivo", query = "SELECT e FROM EppFoto e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppFoto.findByAudLogin", query = "SELECT e FROM EppFoto e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppFoto.findByAudNumIp", query = "SELECT e FROM EppFoto e WHERE e.audNumIp = :audNumIp")})
public class EppFoto implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_FOTO")
    @SequenceGenerator(name = "SEQ_EPP_FOTO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_FOTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 500)
    @Column(name = "RUTA_FILE")
    private String rutaFile;
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
    @OneToMany(mappedBy = "fotoId")
    private List<EppLicencia> eppLicenciaList;
    @OneToMany(mappedBy = "fotoId")
    private List<EppCertificado> eppCertificadoList;
    @OneToMany(mappedBy = "fotoId")
    private List<EppCarne> eppCarneList;

    public EppFoto() {
    }

    public EppFoto(Long id) {
        this.id = id;
    }

    public EppFoto(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getRutaFile() {
        return rutaFile;
    }

    public void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
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
    public List<EppLicencia> getEppLicenciaList() {
        return eppLicenciaList;
    }

    public void setEppLicenciaList(List<EppLicencia> eppLicenciaList) {
        this.eppLicenciaList = eppLicenciaList;
    }

    @XmlTransient
    public List<EppCertificado> getEppCertificadoList() {
        return eppCertificadoList;
    }

    public void setEppCertificadoList(List<EppCertificado> eppCertificadoList) {
        this.eppCertificadoList = eppCertificadoList;
    }

    @XmlTransient
    public List<EppCarne> getEppCarneList() {
        return eppCarneList;
    }

    public void setEppCarneList(List<EppCarne> eppCarneList) {
        this.eppCarneList = eppCarneList;
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
        if (!(object instanceof EppFoto)) {
            return false;
        }
        EppFoto other = (EppFoto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppFoto[ id=" + id + " ]";
    }
    
}
