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
import pe.gob.sucamec.sel.gamac.data.GamacAmaMunicion;

/**
 *
 * @author locador192.ogtic
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_INGSALTD_MUNICION",catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaIngsaltdMunicion.findAll", query = "SELECT a FROM AmaIngsaltdMunicion a"),
    @NamedQuery(name = "AmaIngsaltdMunicion.findById", query = "SELECT a FROM AmaIngsaltdMunicion a WHERE a.id = :id"),
    @NamedQuery(name = "AmaIngsaltdMunicion.findByCantidad", query = "SELECT a FROM AmaIngsaltdMunicion a WHERE a.cantidad = :cantidad"),
    @NamedQuery(name = "AmaIngsaltdMunicion.findByActivo", query = "SELECT a FROM AmaIngsaltdMunicion a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaIngsaltdMunicion.findByAudLogin", query = "SELECT a FROM AmaIngsaltdMunicion a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaIngsaltdMunicion.findByAudNumIp", query = "SELECT a FROM AmaIngsaltdMunicion a WHERE a.audNumIp = :audNumIp")})
public class AmaIngsaltdMunicion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_INGSALTD_MUNICION")
    @SequenceGenerator(name = "SEQ_AMA_INGSALTD_MUNICION", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_INGSALTD_MUNICION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private int cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ingsaltdMunicionId")
    private List<AmaIngsaltdMuniAdic> amaIngsaltdMuniAdicList;
    @JoinColumn(name = "MUNICION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaMunicion municionId;
    @JoinColumn(name = "INGSALRUTAPER_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaIngsaltdRutaPersona ingsalrutaperId;
    @JoinColumn(name = "INGSALTD_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaIngsaltdArma ingsaltdArmaId;

    public AmaIngsaltdMunicion() {
    }

    public AmaIngsaltdMunicion(Long id) {
        this.id = id;
    }

    public AmaIngsaltdMunicion(Long id, int cantidad, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.cantidad = cantidad;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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
    public List<AmaIngsaltdMuniAdic> getAmaIngsaltdMuniAdicList() {
        return amaIngsaltdMuniAdicList;
    }

    public void setAmaIngsaltdMuniAdicList(List<AmaIngsaltdMuniAdic> amaIngsaltdMuniAdicList) {
        this.amaIngsaltdMuniAdicList = amaIngsaltdMuniAdicList;
    }

    public GamacAmaMunicion getMunicionId() {
        return municionId;
    }

    public void setMunicionId(GamacAmaMunicion municionId) {
        this.municionId = municionId;
    }

    public AmaIngsaltdRutaPersona getIngsalrutaperId() {
        return ingsalrutaperId;
    }

    public void setIngsalrutaperId(AmaIngsaltdRutaPersona ingsalrutaperId) {
        this.ingsalrutaperId = ingsalrutaperId;
    }

    public AmaIngsaltdArma getIngsaltdArmaId() {
        return ingsaltdArmaId;
    }

    public void setIngsaltdArmaId(AmaIngsaltdArma ingsaltdArmaId) {
        this.ingsaltdArmaId = ingsaltdArmaId;
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
        if (!(object instanceof AmaIngsaltdMunicion)) {
            return false;
        }
        AmaIngsaltdMunicion other = (AmaIngsaltdMunicion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaIngsaltdMunicion[ id=" + id + " ]";
    }
    
}
