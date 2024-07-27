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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_OBSERVACION_DESC", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaObservacionDesc.findAll", query = "SELECT a FROM AmaObservacionDesc a"),
    @NamedQuery(name = "AmaObservacionDesc.findById", query = "SELECT a FROM AmaObservacionDesc a WHERE a.id = :id"),
    @NamedQuery(name = "AmaObservacionDesc.findByDescripcion", query = "SELECT a FROM AmaObservacionDesc a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "AmaObservacionDesc.findByActivo", query = "SELECT a FROM AmaObservacionDesc a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaObservacionDesc.findByAudLogin", query = "SELECT a FROM AmaObservacionDesc a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaObservacionDesc.findByAudNumIp", query = "SELECT a FROM AmaObservacionDesc a WHERE a.audNumIp = :audNumIp")})
public class AmaObservacionDesc implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_OBSERVACION_DESC")
    @SequenceGenerator(name = "SEQ_AMA_OBSERVACION_DESC", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_OBSERVACION_DESC", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "DESCRIPCION")
    private String descripcion;
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
    @ManyToMany(mappedBy = "amaObservacionDescList")
    private List<AmaDocumento> amaDocumentoList;
    @JoinColumn(name = "TIPO_OBSERVACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoObservacionId;
    @JoinColumn(name = "TIPO_PROCESO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoProcesoId;

    public AmaObservacionDesc() {
    }

    public AmaObservacionDesc(Long id) {
        this.id = id;
    }

    public AmaObservacionDesc(Long id, String descripcion, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
    public List<AmaDocumento> getAmaDocumentoList() {
        return amaDocumentoList;
    }

    public void setAmaDocumentoList(List<AmaDocumento> amaDocumentoList) {
        this.amaDocumentoList = amaDocumentoList;
    }

    public TipoGamac getTipoObservacionId() {
        return tipoObservacionId;
    }

    public void setTipoObservacionId(TipoGamac tipoObservacionId) {
        this.tipoObservacionId = tipoObservacionId;
    }

    public TipoBaseGt getTipoProcesoId() {
        return tipoProcesoId;
    }

    public void setTipoProcesoId(TipoBaseGt tipoProcesoId) {
        this.tipoProcesoId = tipoProcesoId;
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
        if (!(object instanceof AmaObservacionDesc)) {
            return false;
        }
        AmaObservacionDesc other = (AmaObservacionDesc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaObservacionDesc[ id=" + id + " ]";
    }
    
}
