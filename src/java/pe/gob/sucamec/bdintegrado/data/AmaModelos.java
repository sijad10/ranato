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
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_MODELOS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaModelos.findAll", query = "SELECT a FROM AmaModelos a"),
    @NamedQuery(name = "AmaModelos.findById", query = "SELECT a FROM AmaModelos a WHERE a.id = :id"),
    @NamedQuery(name = "AmaModelos.findByModelo", query = "SELECT a FROM AmaModelos a WHERE a.modelo = :modelo"),
    @NamedQuery(name = "AmaModelos.findByLongCanon", query = "SELECT a FROM AmaModelos a WHERE a.longCanon = :longCanon"),
    @NamedQuery(name = "AmaModelos.findByLongArma", query = "SELECT a FROM AmaModelos a WHERE a.longArma = :longArma"),
    @NamedQuery(name = "AmaModelos.findByActivo", query = "SELECT a FROM AmaModelos a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaModelos.findByAudLogin", query = "SELECT a FROM AmaModelos a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaModelos.findByAudNumIp", query = "SELECT a FROM AmaModelos a WHERE a.audNumIp = :audNumIp")})
public class AmaModelos implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_MODELOS")
    @SequenceGenerator(name = "SEQ_AMA_MODELOS", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_MODELOS", allocationSize = 1)
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
    @JoinTable(schema = "BDINTEGRADO", name = "AMA_MODELO_CALIBRE", joinColumns = {
        @JoinColumn(name = "MODELO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CATALOGO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<AmaCatalogo> amaCatalogoList;
    @JoinColumn(name = "DISPOSICION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaCatalogo disposicionId;
    @JoinColumn(name = "TIPO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaCatalogo tipoArmaId;
    @JoinColumn(name = "MARCA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaCatalogo marcaId;
    @OneToMany(mappedBy = "modeloId")
    private List<AmaArma> amaArmaList;
    @JoinColumn(name = "TIPO_ARTICULO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaCatalogo tipoArticuloId;
    
    public AmaModelos() {
    }

    public AmaModelos(Long id) {
        this.id = id;
    }

    public AmaModelos(Long id, String modelo, short activo, String audLogin, String audNumIp) {
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
    public List<AmaCatalogo> getAmaCatalogoList() {
        return amaCatalogoList;
    }

    public void setAmaCatalogoList(List<AmaCatalogo> amaCatalogoList) {
        this.amaCatalogoList = amaCatalogoList;
    }

    public AmaCatalogo getDisposicionId() {
        return disposicionId;
    }

    public void setDisposicionId(AmaCatalogo disposicionId) {
        this.disposicionId = disposicionId;
    }

    public AmaCatalogo getTipoArmaId() {
        return tipoArmaId;
    }

    public void setTipoArmaId(AmaCatalogo tipoArmaId) {
        this.tipoArmaId = tipoArmaId;
    }

    public AmaCatalogo getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(AmaCatalogo marcaId) {
        this.marcaId = marcaId;
    }

    @XmlTransient
    public List<AmaArma> getAmaArmaList() {
        return amaArmaList;
    }

    public void setAmaArmaList(List<AmaArma> amaArmaList) {
        this.amaArmaList = amaArmaList;
    }

    public AmaCatalogo getTipoArticuloId() {
        return tipoArticuloId;
    }

    public void setTipoArticuloId(AmaCatalogo tipoArticuloId) {
        this.tipoArticuloId = tipoArticuloId;
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
        if (!(object instanceof AmaModelos)) {
            return false;
        }
        AmaModelos other = (AmaModelos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaModelos[ id=" + id + " ]";
    }
    
}
