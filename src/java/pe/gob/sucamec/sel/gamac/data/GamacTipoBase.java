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
@Table(name = "TIPO_BASE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacTipoBase.findAll", query = "SELECT g FROM GamacTipoBase g"),
    @NamedQuery(name = "GamacTipoBase.findById", query = "SELECT g FROM GamacTipoBase g WHERE g.id = :id"),
    @NamedQuery(name = "GamacTipoBase.findByNombre", query = "SELECT g FROM GamacTipoBase g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "GamacTipoBase.findByAbreviatura", query = "SELECT g FROM GamacTipoBase g WHERE g.abreviatura = :abreviatura"),
    @NamedQuery(name = "GamacTipoBase.findByDescripcion", query = "SELECT g FROM GamacTipoBase g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "GamacTipoBase.findByCodProg", query = "SELECT g FROM GamacTipoBase g WHERE g.codProg = :codProg"),
    @NamedQuery(name = "GamacTipoBase.findByOrden", query = "SELECT g FROM GamacTipoBase g WHERE g.orden = :orden"),
    @NamedQuery(name = "GamacTipoBase.findByActivo", query = "SELECT g FROM GamacTipoBase g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacTipoBase.findByAudLogin", query = "SELECT g FROM GamacTipoBase g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacTipoBase.findByAudNumIp", query = "SELECT g FROM GamacTipoBase g WHERE g.audNumIp = :audNumIp")})
public class GamacTipoBase implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_TIPO_BASE")
    @SequenceGenerator(name = "SEQ_GAMAC_TIPO_BASE", schema = "BDINTEGRADO", sequenceName = "SEQ_TIPO_BASE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "COD_PROG")
    private String codProg;
    @Column(name = "ORDEN")
    private Integer orden;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private Collection<GamacSbUsuario> gamacSbUsuarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAutenId")
    private Collection<GamacSbUsuario> gamacSbUsuarioCollection1;
    @OneToMany(mappedBy = "areaId")
    private Collection<GamacSbUsuario> gamacSbUsuarioCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private Collection<GamacSbDireccion> gamacSbDireccionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "viaId")
    private Collection<GamacSbDireccion> gamacSbDireccionCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zonaId")
    private Collection<GamacSbDireccion> gamacSbDireccionCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProId")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRegId")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoOpeId")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection2;
    @OneToMany(mappedBy = "tipoId")
    private Collection<GamacTipoBase> gamacTipoBaseCollection;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoBase tipoId;
    @OneToMany(mappedBy = "estCivilId")
    private Collection<GamacSbPersona> gamacSbPersonaCollection;
    @OneToMany(mappedBy = "generoId")
    private Collection<GamacSbPersona> gamacSbPersonaCollection1;
    @OneToMany(mappedBy = "tipoDoc")
    private Collection<GamacSbPersona> gamacSbPersonaCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private Collection<GamacSbPersona> gamacSbPersonaCollection3;
    @OneToMany(mappedBy = "instituLabId")
    private Collection<GamacSbPersona> gamacSbPersonaCollection4;
    @OneToMany(mappedBy = "ocupacionId")
    private Collection<GamacSbPersona> gamacSbPersonaCollection5;

    public GamacTipoBase() {
    }

    public GamacTipoBase(Long id) {
        this.id = id;
    }

    public GamacTipoBase(Long id, String nombre, String codProg, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.codProg = codProg;
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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
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
    public Collection<GamacSbUsuario> getGamacSbUsuarioCollection() {
        return gamacSbUsuarioCollection;
    }

    public void setGamacSbUsuarioCollection(Collection<GamacSbUsuario> gamacSbUsuarioCollection) {
        this.gamacSbUsuarioCollection = gamacSbUsuarioCollection;
    }

    @XmlTransient
    public Collection<GamacSbUsuario> getGamacSbUsuarioCollection1() {
        return gamacSbUsuarioCollection1;
    }

    public void setGamacSbUsuarioCollection1(Collection<GamacSbUsuario> gamacSbUsuarioCollection1) {
        this.gamacSbUsuarioCollection1 = gamacSbUsuarioCollection1;
    }

    @XmlTransient
    public Collection<GamacSbUsuario> getGamacSbUsuarioCollection2() {
        return gamacSbUsuarioCollection2;
    }

    public void setGamacSbUsuarioCollection2(Collection<GamacSbUsuario> gamacSbUsuarioCollection2) {
        this.gamacSbUsuarioCollection2 = gamacSbUsuarioCollection2;
    }

    @XmlTransient
    public Collection<GamacSbDireccion> getGamacSbDireccionCollection() {
        return gamacSbDireccionCollection;
    }

    public void setGamacSbDireccionCollection(Collection<GamacSbDireccion> gamacSbDireccionCollection) {
        this.gamacSbDireccionCollection = gamacSbDireccionCollection;
    }

    @XmlTransient
    public Collection<GamacSbDireccion> getGamacSbDireccionCollection1() {
        return gamacSbDireccionCollection1;
    }

    public void setGamacSbDireccionCollection1(Collection<GamacSbDireccion> gamacSbDireccionCollection1) {
        this.gamacSbDireccionCollection1 = gamacSbDireccionCollection1;
    }

    @XmlTransient
    public Collection<GamacSbDireccion> getGamacSbDireccionCollection2() {
        return gamacSbDireccionCollection2;
    }

    public void setGamacSbDireccionCollection2(Collection<GamacSbDireccion> gamacSbDireccionCollection2) {
        this.gamacSbDireccionCollection2 = gamacSbDireccionCollection2;
    }

    @XmlTransient
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection() {
        return gamacEppRegistroCollection;
    }

    public void setGamacEppRegistroCollection(Collection<GamacEppRegistro> gamacEppRegistroCollection) {
        this.gamacEppRegistroCollection = gamacEppRegistroCollection;
    }

    @XmlTransient
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection1() {
        return gamacEppRegistroCollection1;
    }

    public void setGamacEppRegistroCollection1(Collection<GamacEppRegistro> gamacEppRegistroCollection1) {
        this.gamacEppRegistroCollection1 = gamacEppRegistroCollection1;
    }

    @XmlTransient
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection2() {
        return gamacEppRegistroCollection2;
    }

    public void setGamacEppRegistroCollection2(Collection<GamacEppRegistro> gamacEppRegistroCollection2) {
        this.gamacEppRegistroCollection2 = gamacEppRegistroCollection2;
    }

    @XmlTransient
    public Collection<GamacTipoBase> getGamacTipoBaseCollection() {
        return gamacTipoBaseCollection;
    }

    public void setGamacTipoBaseCollection(Collection<GamacTipoBase> gamacTipoBaseCollection) {
        this.gamacTipoBaseCollection = gamacTipoBaseCollection;
    }

    public GamacTipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(GamacTipoBase tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public Collection<GamacSbPersona> getGamacSbPersonaCollection() {
        return gamacSbPersonaCollection;
    }

    public void setGamacSbPersonaCollection(Collection<GamacSbPersona> gamacSbPersonaCollection) {
        this.gamacSbPersonaCollection = gamacSbPersonaCollection;
    }

    @XmlTransient
    public Collection<GamacSbPersona> getGamacSbPersonaCollection1() {
        return gamacSbPersonaCollection1;
    }

    public void setGamacSbPersonaCollection1(Collection<GamacSbPersona> gamacSbPersonaCollection1) {
        this.gamacSbPersonaCollection1 = gamacSbPersonaCollection1;
    }

    @XmlTransient
    public Collection<GamacSbPersona> getGamacSbPersonaCollection2() {
        return gamacSbPersonaCollection2;
    }

    public void setGamacSbPersonaCollection2(Collection<GamacSbPersona> gamacSbPersonaCollection2) {
        this.gamacSbPersonaCollection2 = gamacSbPersonaCollection2;
    }

    @XmlTransient
    public Collection<GamacSbPersona> getGamacSbPersonaCollection3() {
        return gamacSbPersonaCollection3;
    }

    public void setGamacSbPersonaCollection3(Collection<GamacSbPersona> gamacSbPersonaCollection3) {
        this.gamacSbPersonaCollection3 = gamacSbPersonaCollection3;
    }

    @XmlTransient
    public Collection<GamacSbPersona> getGamacSbPersonaCollection4() {
        return gamacSbPersonaCollection4;
    }

    public void setGamacSbPersonaCollection4(Collection<GamacSbPersona> gamacSbPersonaCollection4) {
        this.gamacSbPersonaCollection4 = gamacSbPersonaCollection4;
    }

    @XmlTransient
    public Collection<GamacSbPersona> getGamacSbPersonaCollection5() {
        return gamacSbPersonaCollection5;
    }

    public void setGamacSbPersonaCollection5(Collection<GamacSbPersona> gamacSbPersonaCollection5) {
        this.gamacSbPersonaCollection5 = gamacSbPersonaCollection5;
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
        if (!(object instanceof GamacTipoBase)) {
            return false;
        }
        GamacTipoBase other = (GamacTipoBase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacTipoBase[ id=" + id + " ]";
    }
    
}
