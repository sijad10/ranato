/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.epp.data;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;


/**
 *
 * @author rmoscoso
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_EXPLOSIVO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Explosivo.findAll", query = "SELECT e FROM Explosivo e"),
    @NamedQuery(name = "Explosivo.findById", query = "SELECT e FROM Explosivo e WHERE e.id = :id"),
    @NamedQuery(name = "Explosivo.findByNombre", query = "SELECT e FROM Explosivo e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Explosivo.findByAbreviatura", query = "SELECT e FROM Explosivo e WHERE e.abreviatura = :abreviatura"),
    @NamedQuery(name = "Explosivo.findByActivo", query = "SELECT e FROM Explosivo e WHERE e.activo = :activo"),
    @NamedQuery(name = "Explosivo.findByAudLogin", query = "SELECT e FROM Explosivo e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "Explosivo.findByAudNumIp", query = "SELECT e FROM Explosivo e WHERE e.audNumIp = :audNumIp")})
public class Explosivo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_EXPLOSIVO")
    @SequenceGenerator(name = "SEQ_EPP_EXPLOSIVO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_EXPLOSIVO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
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
    @OneToMany(mappedBy = "explosivoId")
    private List<Explosivo> explosivoList;
    @JoinColumn(name = "EXPLOSIVO_ID", referencedColumnName = "ID")
    @ManyToOne
    private Explosivo explosivoId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivo tipoId;

    public Explosivo() {
    }

    public Explosivo(Long id) {
        this.id = id;
    }

    public Explosivo(Long id, String nombre, String abreviatura, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
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
    public List<Explosivo> getExplosivoList() {
        return explosivoList;
    }

    public void setExplosivoList(List<Explosivo> explosivoList) {
        this.explosivoList = explosivoList;
    }

    public Explosivo getExplosivoId() {
        return explosivoId;
    }

    public void setExplosivoId(Explosivo explosivoId) {
        this.explosivoId = explosivoId;
    }

    public TipoExplosivo getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoExplosivo tipoId) {
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
        if (!(object instanceof Explosivo)) {
            return false;
        }
        Explosivo other = (Explosivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.epp.data.Explosivo[ id=" + id + " ]";
    }
    
}
