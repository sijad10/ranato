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
 * @author lbartolo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_CERTIF_PROVEE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspCertifProvee.findAll", query = "SELECT s FROM SspCertifProvee s"),
    @NamedQuery(name = "SspCertifProvee.findById", query = "SELECT s FROM SspCertifProvee s WHERE s.id = :id"),
    @NamedQuery(name = "SspCertifProvee.findByDocEmpresaCert", query = "SELECT s FROM SspCertifProvee s WHERE s.docEmpresaCert = :docEmpresaCert"),
    @NamedQuery(name = "SspCertifProvee.findByEmpresaCertCaract", query = "SELECT s FROM SspCertifProvee s WHERE s.empresaCertCaract = :empresaCertCaract"),
    @NamedQuery(name = "SspCertifProvee.findByFechaIni", query = "SELECT s FROM SspCertifProvee s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SspCertifProvee.findByFechaFin", query = "SELECT s FROM SspCertifProvee s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SspCertifProvee.findByNroCertificado", query = "SELECT s FROM SspCertifProvee s WHERE s.nroCertificado = :nroCertificado"),
    @NamedQuery(name = "SspCertifProvee.findByArchivoCertProv", query = "SELECT s FROM SspCertifProvee s WHERE s.archivoCertProv = :archivoCertProv"),
    @NamedQuery(name = "SspCertifProvee.findByActivo", query = "SELECT s FROM SspCertifProvee s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspCertifProvee.findByAudLogin", query = "SELECT s FROM SspCertifProvee s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspCertifProvee.findByAudNumIp", query = "SELECT s FROM SspCertifProvee s WHERE s.audNumIp = :audNumIp")})
public class SspCertifProvee implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_CERTIF_PROVEE")
    @SequenceGenerator(name = "SEQ_SSP_CERTIF_PROVEE", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_CERTIF_PROVEE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;
    
    @JoinColumn(name = "PAIS_ORIGEN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPaisGt paisId;
    
    @Size(max = 20)
    @Column(name = "DOC_EMPRESA_CERT")
    private String docEmpresaCert;
    
    @Size(max = 30)
    @Column(name = "EMPRESA_CERT_CARACT")
    private String empresaCertCaract;
    
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    
    @Size(max = 20)
    @Column(name = "NRO_CERTIFICADO")
    private String nroCertificado;
    @Size(max = 50)
    @Column(name = "ARCHIVO_CERT_PROV")
    private String archivoCertProv;
    
    @Column(name = "ACTIVO")
    private Short activo;
    
    @Size(max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    
    @Size(max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;

    public SspCertifProvee() {
    }

    public SspCertifProvee(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocEmpresaCert() {
        return docEmpresaCert;
    }

    public void setDocEmpresaCert(String docEmpresaCert) {
        this.docEmpresaCert = docEmpresaCert;
    }

    public String getEmpresaCertCaract() {
        return empresaCertCaract;
    }

    public void setEmpresaCertCaract(String empresaCertCaract) {
        this.empresaCertCaract = empresaCertCaract;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getNroCertificado() {
        return nroCertificado;
    }

    public void setNroCertificado(String nroCertificado) {
        this.nroCertificado = nroCertificado;
    }

    public String getArchivoCertProv() {
        return archivoCertProv;
    }

    public void setArchivoCertProv(String archivoCertProv) {
        this.archivoCertProv = archivoCertProv;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SspCertifProvee)) {
            return false;
        }
        SspCertifProvee other = (SspCertifProvee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspCertifProvee[ id=" + id + " ]";
    }

    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
    }

    public SbPaisGt getPaisId() {
        return paisId;
    }

    public void setPaisId(SbPaisGt paisId) {
        this.paisId = paisId;
    }
    
    
    
}
