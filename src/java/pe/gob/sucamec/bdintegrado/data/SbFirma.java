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
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_FIRMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbFirma.findAll", query = "SELECT s FROM SbFirma s"),
    @NamedQuery(name = "SbFirma.findById", query = "SELECT s FROM SbFirma s WHERE s.id = :id"),
    @NamedQuery(name = "SbFirma.findByNombreImg", query = "SELECT s FROM SbFirma s WHERE s.nombreImg = :nombreImg"),
    @NamedQuery(name = "SbFirma.findByFecha", query = "SELECT s FROM SbFirma s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SbFirma.findByActivo", query = "SELECT s FROM SbFirma s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbFirma.findByAudLogin", query = "SELECT s FROM SbFirma s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbFirma.findByAudNumIp", query = "SELECT s FROM SbFirma s WHERE s.audNumIp = :audNumIp")})
public class SbFirma implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_FIRMA")
    @SequenceGenerator(name = "SEQ_SB_FIRMA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_FIRMA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NOMBRE_IMG")
    private String nombreImg;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "firmaId")
    private List<SspFormatoCarne> sspFormatoCarneList;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBase areaId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersonaGt personaId;
    @Size(max = 200)
    @Column(name = "POSTFIRMA")
    private String postfirma;

    public SbFirma() {
    }

    public SbFirma(Long id) {
        this.id = id;
    }

    public SbFirma(Long id, String nombreImg) {
        this.id = id;
        this.nombreImg = nombreImg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreImg() {
        return nombreImg;
    }

    public void setNombreImg(String nombreImg) {
        this.nombreImg = nombreImg;
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

    public TipoBase getAreaId() {
        return areaId;
    }

    public void setAreaId(TipoBase areaId) {
        this.areaId = areaId;
    }

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
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
        if (!(object instanceof SbFirma)) {
            return false;
        }
        SbFirma other = (SbFirma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbFirma[ id=" + id + " ]";
    }
    
    public String getPostfirma() {
        return postfirma;
    }

    public void setPostfirma(String postfirma) {
        this.postfirma = postfirma;
    }

}
