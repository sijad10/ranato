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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_FOTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaFoto.findAll", query = "SELECT a FROM AmaFoto a"),
    @NamedQuery(name = "AmaFoto.findById", query = "SELECT a FROM AmaFoto a WHERE a.id = :id"),
    @NamedQuery(name = "AmaFoto.findByNroExpediente", query = "SELECT a FROM AmaFoto a WHERE a.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "AmaFoto.findByActivo", query = "SELECT a FROM AmaFoto a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaFoto.findByAudLogin", query = "SELECT a FROM AmaFoto a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaFoto.findByAudNumIp", query = "SELECT a FROM AmaFoto a WHERE a.audNumIp = :audNumIp"),
    @NamedQuery(name = "AmaFoto.findByRutaFile", query = "SELECT a FROM AmaFoto a WHERE a.rutaFile = :rutaFile")})
public class AmaFoto implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_FOTO")
    @SequenceGenerator(name = "SEQ_AMA_FOTO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_FOTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
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
    @Size(max = 500)
    @Column(name = "RUTA_FILE")
    private String rutaFile;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fotoId")
    private List<AmaLicenciaDeUso> amaLicenciaDeUsoList;
    @JoinColumn(name = "VERIFICAR_FOTO_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaVerificarArma verificarFotoId;
    @JoinColumn(name = "TIPO_POSICION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac tipoPosicionId;

    public AmaFoto() {
    }

    public AmaFoto(Long id) {
        this.id = id;
    }

    public AmaFoto(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
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

    public String getRutaFile() {
        return rutaFile;
    }

    public void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    @XmlTransient
    public List<AmaLicenciaDeUso> getAmaLicenciaDeUsoList() {
        return amaLicenciaDeUsoList;
    }

    public void setAmaLicenciaDeUsoList(List<AmaLicenciaDeUso> amaLicenciaDeUsoList) {
        this.amaLicenciaDeUsoList = amaLicenciaDeUsoList;
    }

    public AmaVerificarArma getVerificarFotoId() {
        return verificarFotoId;
    }

    public void setVerificarFotoId(AmaVerificarArma verificarFotoId) {
        this.verificarFotoId = verificarFotoId;
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
        if (!(object instanceof AmaFoto)) {
            return false;
        }
        AmaFoto other = (AmaFoto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaFoto[ id=" + id + " ]";
    }

    /**
     * @return the tipoPosicionId
     */
    public TipoGamac getTipoPosicionId() {
        return tipoPosicionId;
    }

    /**
     * @param tipoPosicionId the tipoPosicionId to set
     */
    public void setTipoPosicionId(TipoGamac tipoPosicionId) {
        this.tipoPosicionId = tipoPosicionId;
    }
    
}
