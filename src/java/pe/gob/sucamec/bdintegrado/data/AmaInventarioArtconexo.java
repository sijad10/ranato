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
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_INVENTARIO_ARTCONEXO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaInventarioArtconexo.findAll", query = "SELECT a FROM AmaInventarioArtconexo a"),
    @NamedQuery(name = "AmaInventarioArtconexo.findById", query = "SELECT a FROM AmaInventarioArtconexo a WHERE a.id = :id"),
    @NamedQuery(name = "AmaInventarioArtconexo.findByNombre", query = "SELECT a FROM AmaInventarioArtconexo a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "AmaInventarioArtconexo.findByDescripcion", query = "SELECT a FROM AmaInventarioArtconexo a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "AmaInventarioArtconexo.findByActivo", query = "SELECT a FROM AmaInventarioArtconexo a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaInventarioArtconexo.findByAudLogin", query = "SELECT a FROM AmaInventarioArtconexo a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaInventarioArtconexo.findByAudNumIp", query = "SELECT a FROM AmaInventarioArtconexo a WHERE a.audNumIp = :audNumIp")})
public class AmaInventarioArtconexo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_INVENTARIO_ARTCONEXO")
    @SequenceGenerator(name = "SEQ_AMA_INVENTARIO_ARTCONEXO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_INVENTARIO_ARTCONEXO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 200)
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
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articuloConexoId")
    private List<AmaInventarioAccesorios> amaInventarioAccesoriosList;

    public AmaInventarioArtconexo() {
    }

    public AmaInventarioArtconexo(Long id) {
        this.id = id;
    }

    public AmaInventarioArtconexo(Long id, String nombre, short activo, String audLogin, String audNumIp) {
        this.id = id;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
    public List<AmaInventarioAccesorios> getAmaInventarioAccesoriosList() {
        return amaInventarioAccesoriosList;
    }

    public void setAmaInventarioAccesoriosList(List<AmaInventarioAccesorios> amaInventarioAccesoriosList) {
        this.amaInventarioAccesoriosList = amaInventarioAccesoriosList;
    }

    public TipoGamac getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoGamac tipoId) {
        this.tipoId = tipoId;
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
        if (!(object instanceof AmaInventarioArtconexo)) {
            return false;
        }
        AmaInventarioArtconexo other = (AmaInventarioArtconexo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaInventarioArtconexo[ id=" + id + " ]";
    }
    
}
