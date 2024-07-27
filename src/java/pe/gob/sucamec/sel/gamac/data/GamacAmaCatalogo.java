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
@Table(name = "AMA_CATALOGO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacAmaCatalogo.findAll", query = "SELECT g FROM GamacAmaCatalogo g"),
    @NamedQuery(name = "GamacAmaCatalogo.findById", query = "SELECT g FROM GamacAmaCatalogo g WHERE g.id = :id"),
    @NamedQuery(name = "GamacAmaCatalogo.findByNombre", query = "SELECT g FROM GamacAmaCatalogo g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "GamacAmaCatalogo.findByAbreviatura", query = "SELECT g FROM GamacAmaCatalogo g WHERE g.abreviatura = :abreviatura"),
    @NamedQuery(name = "GamacAmaCatalogo.findByDescripcion", query = "SELECT g FROM GamacAmaCatalogo g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "GamacAmaCatalogo.findByCodProg", query = "SELECT g FROM GamacAmaCatalogo g WHERE g.codProg = :codProg"),
    @NamedQuery(name = "GamacAmaCatalogo.findByOrden", query = "SELECT g FROM GamacAmaCatalogo g WHERE g.orden = :orden"),
    @NamedQuery(name = "GamacAmaCatalogo.findByActivo", query = "SELECT g FROM GamacAmaCatalogo g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacAmaCatalogo.findByAudLogin", query = "SELECT g FROM GamacAmaCatalogo g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacAmaCatalogo.findByAudNumIp", query = "SELECT g FROM GamacAmaCatalogo g WHERE g.audNumIp = :audNumIp")})
public class GamacAmaCatalogo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_AMA_CATALOGO")
    @SequenceGenerator(name = "SEQ_GAMAC_AMA_CATALOGO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_CATALOGO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 200)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 15)
    @Column(name = "COD_PROG")
    private String codProg;
    @Column(name = "ORDEN")
    private Long orden;
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
    @JoinTable(schema="BDINTEGRADO", name = "AMA_DETALLE_CATALOGO", joinColumns = {
        @JoinColumn(name = "TIPO_CATALOGO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TIPO_TIPO_CATALOGO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<GamacAmaCatalogo> gamacAmaCatalogoCollection;
    @ManyToMany(mappedBy = "gamacAmaCatalogoCollection")
    private Collection<GamacAmaCatalogo> gamacAmaCatalogoCollection1;
    @ManyToMany(mappedBy = "gamacAmaCatalogoCollection")
    private Collection<GamacTipoGamac> gamacTipoGamacCollection;
    @ManyToMany(mappedBy = "gamacAmaCatalogoCollection")
    private Collection<GamacAmaModelos> gamacAmaModelosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calibrearmaId")
    private Collection<GamacAmaMunicion> gamacAmaMunicionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marcaId")
    private Collection<GamacAmaMunicion> gamacAmaMunicionCollection1;
    @OneToMany(mappedBy = "tipoArticuloId")
    private Collection<GamacAmaModelos> gamacAmaModelosCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disposicionId")
    private Collection<GamacAmaModelos> gamacAmaModelosCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marcaId")
    private Collection<GamacAmaModelos> gamacAmaModelosCollection3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoArmaId")
    private Collection<GamacAmaModelos> gamacAmaModelosCollection4;
    @OneToMany(mappedBy = "tipoId")
    private Collection<GamacAmaCatalogo> gamacAmaCatalogoCollection2;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacAmaCatalogo tipoId;
    @OneToMany(mappedBy = "marcaId")
    private Collection<GamacAmaArma> gamacAmaArmaCollection;
    @OneToMany(mappedBy = "calibreId")
    private Collection<GamacAmaArma> gamacAmaArmaCollection1;
    @OneToMany(mappedBy = "tipoArmaId")
    private Collection<GamacAmaArma> gamacAmaArmaCollection2;

    public GamacAmaCatalogo() {
    }

    public GamacAmaCatalogo(Long id) {
        this.id = id;
    }

    public GamacAmaCatalogo(Long id, String nombre, short activo, String audLogin, String audNumIp) {
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodProg() {
        return codProg;
    }

    public void setCodProg(String codProg) {
        this.codProg = codProg;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
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

    @XmlTransient
    public Collection<GamacAmaCatalogo> getGamacAmaCatalogoCollection1() {
        return gamacAmaCatalogoCollection1;
    }

    public void setGamacAmaCatalogoCollection1(Collection<GamacAmaCatalogo> gamacAmaCatalogoCollection1) {
        this.gamacAmaCatalogoCollection1 = gamacAmaCatalogoCollection1;
    }

    @XmlTransient
    public Collection<GamacTipoGamac> getGamacTipoGamacCollection() {
        return gamacTipoGamacCollection;
    }

    public void setGamacTipoGamacCollection(Collection<GamacTipoGamac> gamacTipoGamacCollection) {
        this.gamacTipoGamacCollection = gamacTipoGamacCollection;
    }

    @XmlTransient
    public Collection<GamacAmaModelos> getGamacAmaModelosCollection() {
        return gamacAmaModelosCollection;
    }

    public void setGamacAmaModelosCollection(Collection<GamacAmaModelos> gamacAmaModelosCollection) {
        this.gamacAmaModelosCollection = gamacAmaModelosCollection;
    }

    @XmlTransient
    public Collection<GamacAmaMunicion> getGamacAmaMunicionCollection() {
        return gamacAmaMunicionCollection;
    }

    public void setGamacAmaMunicionCollection(Collection<GamacAmaMunicion> gamacAmaMunicionCollection) {
        this.gamacAmaMunicionCollection = gamacAmaMunicionCollection;
    }

    @XmlTransient
    public Collection<GamacAmaMunicion> getGamacAmaMunicionCollection1() {
        return gamacAmaMunicionCollection1;
    }

    public void setGamacAmaMunicionCollection1(Collection<GamacAmaMunicion> gamacAmaMunicionCollection1) {
        this.gamacAmaMunicionCollection1 = gamacAmaMunicionCollection1;
    }

    @XmlTransient
    public Collection<GamacAmaModelos> getGamacAmaModelosCollection1() {
        return gamacAmaModelosCollection1;
    }

    public void setGamacAmaModelosCollection1(Collection<GamacAmaModelos> gamacAmaModelosCollection1) {
        this.gamacAmaModelosCollection1 = gamacAmaModelosCollection1;
    }

    @XmlTransient
    public Collection<GamacAmaModelos> getGamacAmaModelosCollection2() {
        return gamacAmaModelosCollection2;
    }

    public void setGamacAmaModelosCollection2(Collection<GamacAmaModelos> gamacAmaModelosCollection2) {
        this.gamacAmaModelosCollection2 = gamacAmaModelosCollection2;
    }

    @XmlTransient
    public Collection<GamacAmaModelos> getGamacAmaModelosCollection3() {
        return gamacAmaModelosCollection3;
    }

    public void setGamacAmaModelosCollection3(Collection<GamacAmaModelos> gamacAmaModelosCollection3) {
        this.gamacAmaModelosCollection3 = gamacAmaModelosCollection3;
    }

    @XmlTransient
    public Collection<GamacAmaModelos> getGamacAmaModelosCollection4() {
        return gamacAmaModelosCollection4;
    }

    public void setGamacAmaModelosCollection4(Collection<GamacAmaModelos> gamacAmaModelosCollection4) {
        this.gamacAmaModelosCollection4 = gamacAmaModelosCollection4;
    }

    @XmlTransient
    public Collection<GamacAmaCatalogo> getGamacAmaCatalogoCollection2() {
        return gamacAmaCatalogoCollection2;
    }

    public void setGamacAmaCatalogoCollection2(Collection<GamacAmaCatalogo> gamacAmaCatalogoCollection2) {
        this.gamacAmaCatalogoCollection2 = gamacAmaCatalogoCollection2;
    }

    public GamacAmaCatalogo getTipoId() {
        return tipoId;
    }

    public void setTipoId(GamacAmaCatalogo tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public Collection<GamacAmaArma> getGamacAmaArmaCollection() {
        return gamacAmaArmaCollection;
    }

    public void setGamacAmaArmaCollection(Collection<GamacAmaArma> gamacAmaArmaCollection) {
        this.gamacAmaArmaCollection = gamacAmaArmaCollection;
    }

    @XmlTransient
    public Collection<GamacAmaArma> getGamacAmaArmaCollection1() {
        return gamacAmaArmaCollection1;
    }

    public void setGamacAmaArmaCollection1(Collection<GamacAmaArma> gamacAmaArmaCollection1) {
        this.gamacAmaArmaCollection1 = gamacAmaArmaCollection1;
    }

    @XmlTransient
    public Collection<GamacAmaArma> getGamacAmaArmaCollection2() {
        return gamacAmaArmaCollection2;
    }

    public void setGamacAmaArmaCollection2(Collection<GamacAmaArma> gamacAmaArmaCollection2) {
        this.gamacAmaArmaCollection2 = gamacAmaArmaCollection2;
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
        if (!(object instanceof GamacAmaCatalogo)) {
            return false;
        }
        GamacAmaCatalogo other = (GamacAmaCatalogo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacAmaCatalogo[ id=" + id + " ]";
    }
    
}
