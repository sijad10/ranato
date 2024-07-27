/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_MODELOS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacAmaModelos.findAll", query = "SELECT g FROM GamacAmaModelos g"),
    @NamedQuery(name = "GamacAmaModelos.findById", query = "SELECT g FROM GamacAmaModelos g WHERE g.id = :id"),
    @NamedQuery(name = "GamacAmaModelos.findByModelo", query = "SELECT g FROM GamacAmaModelos g WHERE g.modelo = :modelo"),
    @NamedQuery(name = "GamacAmaModelos.findByLongCanon", query = "SELECT g FROM GamacAmaModelos g WHERE g.longCanon = :longCanon"),
    @NamedQuery(name = "GamacAmaModelos.findByLongArma", query = "SELECT g FROM GamacAmaModelos g WHERE g.longArma = :longArma"),
    @NamedQuery(name = "GamacAmaModelos.findByActivo", query = "SELECT g FROM GamacAmaModelos g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacAmaModelos.findByAudLogin", query = "SELECT g FROM GamacAmaModelos g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacAmaModelos.findByAudNumIp", query = "SELECT g FROM GamacAmaModelos g WHERE g.audNumIp = :audNumIp")})
public class GamacAmaModelos implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_AMA_MODELOS")
    @SequenceGenerator(name = "SEQ_GAMAC_AMA_MODELOS", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_MODELOS", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "MODELO")
    private String modelo;
    @Column(name = "LONG_CANON")
    private Long longCanon;
    @Column(name = "LONG_ARMA")
    private Long longArma;
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
    @JoinTable(schema="BDINTEGRADO", name = "AMA_MODELO_CALIBRE",
        joinColumns = {@JoinColumn(name = "MODELO_ID", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "CATALOGO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<GamacAmaCatalogo> gamacAmaCatalogoCollection;
    @JoinColumn(name = "TIPO_ARTICULO_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacAmaCatalogo tipoArticuloId;
    @JoinColumn(name = "DISPOSICION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaCatalogo disposicionId;
    @JoinColumn(name = "MARCA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaCatalogo marcaId;
    @JoinColumn(name = "TIPO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaCatalogo tipoArmaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modeloId")
    private Collection<GamacAmaArma> gamacAmaArmaCollection;

    public GamacAmaModelos() {
    }

    public GamacAmaModelos(Long id) {
        this.id = id;
    }

    public GamacAmaModelos(Long id, String modelo, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.modelo = modelo;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Long getLongCanon() {
        return longCanon;
    }

    public void setLongCanon(Long longCanon) {
        this.longCanon = longCanon;
    }

    public Long getLongArma() {
        return longArma;
    }

    public void setLongArma(Long longArma) {
        this.longArma = longArma;
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
    public Collection<GamacAmaCatalogo> getGamacAmaCatalogoCollection() {
        return gamacAmaCatalogoCollection;
    }

    public void setGamacAmaCatalogoCollection(Collection<GamacAmaCatalogo> gamacAmaCatalogoCollection) {
        this.gamacAmaCatalogoCollection = gamacAmaCatalogoCollection;
    }

    public GamacAmaCatalogo getTipoArticuloId() {
        return tipoArticuloId;
    }

    public void setTipoArticuloId(GamacAmaCatalogo tipoArticuloId) {
        this.tipoArticuloId = tipoArticuloId;
    }

    public GamacAmaCatalogo getDisposicionId() {
        return disposicionId;
    }

    public void setDisposicionId(GamacAmaCatalogo disposicionId) {
        this.disposicionId = disposicionId;
    }

    public GamacAmaCatalogo getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(GamacAmaCatalogo marcaId) {
        this.marcaId = marcaId;
    }

    public GamacAmaCatalogo getTipoArmaId() {
        return tipoArmaId;
    }

    public void setTipoArmaId(GamacAmaCatalogo tipoArmaId) {
        this.tipoArmaId = tipoArmaId;
    }

    @XmlTransient
    public Collection<GamacAmaArma> getGamacAmaArmaCollection() {
        return gamacAmaArmaCollection;
    }

    public void setGamacAmaArmaCollection(Collection<GamacAmaArma> gamacAmaArmaCollection) {
        this.gamacAmaArmaCollection = gamacAmaArmaCollection;
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
        if (!(object instanceof GamacAmaModelos)) {
            return false;
        }
        GamacAmaModelos other = (GamacAmaModelos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacAmaModelos[ id=" + id + " ]";
    }
    
}
