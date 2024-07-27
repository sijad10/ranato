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
import java.util.Date;
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

/**
 *
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_USUARIO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacSbUsuario.findAll", query = "SELECT g FROM GamacSbUsuario g"),
    @NamedQuery(name = "GamacSbUsuario.findById", query = "SELECT g FROM GamacSbUsuario g WHERE g.id = :id"),
    @NamedQuery(name = "GamacSbUsuario.findByLogin", query = "SELECT g FROM GamacSbUsuario g WHERE g.login = :login"),
    @NamedQuery(name = "GamacSbUsuario.findByClave", query = "SELECT g FROM GamacSbUsuario g WHERE g.clave = :clave"),
    @NamedQuery(name = "GamacSbUsuario.findByFechaIni", query = "SELECT g FROM GamacSbUsuario g WHERE g.fechaIni = :fechaIni"),
    @NamedQuery(name = "GamacSbUsuario.findByFechaFin", query = "SELECT g FROM GamacSbUsuario g WHERE g.fechaFin = :fechaFin"),
    @NamedQuery(name = "GamacSbUsuario.findByDescripcion", query = "SELECT g FROM GamacSbUsuario g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "GamacSbUsuario.findByNumDoc", query = "SELECT g FROM GamacSbUsuario g WHERE g.numDoc = :numDoc"),
    @NamedQuery(name = "GamacSbUsuario.findByNombres", query = "SELECT g FROM GamacSbUsuario g WHERE g.nombres = :nombres"),
    @NamedQuery(name = "GamacSbUsuario.findByApePat", query = "SELECT g FROM GamacSbUsuario g WHERE g.apePat = :apePat"),
    @NamedQuery(name = "GamacSbUsuario.findByApeMat", query = "SELECT g FROM GamacSbUsuario g WHERE g.apeMat = :apeMat"),
    @NamedQuery(name = "GamacSbUsuario.findByCorreo", query = "SELECT g FROM GamacSbUsuario g WHERE g.correo = :correo"),
    @NamedQuery(name = "GamacSbUsuario.findByCantidadUsuario", query = "SELECT g FROM GamacSbUsuario g WHERE g.cantidadUsuario = :cantidadUsuario"),
    @NamedQuery(name = "GamacSbUsuario.findByActivo", query = "SELECT g FROM GamacSbUsuario g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacSbUsuario.findByAudLogin", query = "SELECT g FROM GamacSbUsuario g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacSbUsuario.findByAudNumIp", query = "SELECT g FROM GamacSbUsuario g WHERE g.audNumIp = :audNumIp")})
public class GamacSbUsuario implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_SB_USUARIO")
    @SequenceGenerator(name = "SEQ_GAMAC_SB_USUARIO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_USUARIO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "LOGIN")
    private String login;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CLAVE")
    private String clave;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Size(max = 300)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NUM_DOC")
    private String numDoc;
    @Size(max = 200)
    @Column(name = "NOMBRES")
    private String nombres;
    @Size(max = 200)
    @Column(name = "APE_PAT")
    private String apePat;
    @Size(max = 200)
    @Column(name = "APE_MAT")
    private String apeMat;
    @Size(max = 300)
    @Column(name = "CORREO")
    private String correo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD_USUARIO")
    private Long cantidadUsuario;
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
    private GamacTipoBase tipoId;
    @JoinColumn(name = "TIPO_AUTEN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoBase tipoAutenId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoBase areaId;
    @OneToMany(mappedBy = "jefeId")
    private Collection<GamacSbUsuario> gamacSbUsuarioCollection;
    @JoinColumn(name = "JEFE_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbUsuario jefeId;
    @OneToMany(mappedBy = "usuarioId")
    private Collection<GamacSbUsuario> gamacSbUsuarioCollection1;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbUsuario usuarioId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbPersona personaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private Collection<GamacAmaLicenciaDeUso> gamacAmaLicenciaDeUsoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection;

    public GamacSbUsuario() {
    }

    public GamacSbUsuario(Long id) {
        this.id = id;
    }

    public GamacSbUsuario(Long id, String login, String clave, Date fechaIni, String numDoc, Long cantidadUsuario, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.login = login;
        this.clave = clave;
        this.fechaIni = fechaIni;
        this.numDoc = numDoc;
        this.cantidadUsuario = cantidadUsuario;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Long getCantidadUsuario() {
        return cantidadUsuario;
    }

    public void setCantidadUsuario(Long cantidadUsuario) {
        this.cantidadUsuario = cantidadUsuario;
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

    public GamacTipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(GamacTipoBase tipoId) {
        this.tipoId = tipoId;
    }

    public GamacTipoBase getTipoAutenId() {
        return tipoAutenId;
    }

    public void setTipoAutenId(GamacTipoBase tipoAutenId) {
        this.tipoAutenId = tipoAutenId;
    }

    public GamacTipoBase getAreaId() {
        return areaId;
    }

    public void setAreaId(GamacTipoBase areaId) {
        this.areaId = areaId;
    }

    @XmlTransient
    public Collection<GamacSbUsuario> getGamacSbUsuarioCollection() {
        return gamacSbUsuarioCollection;
    }

    public void setGamacSbUsuarioCollection(Collection<GamacSbUsuario> gamacSbUsuarioCollection) {
        this.gamacSbUsuarioCollection = gamacSbUsuarioCollection;
    }

    public GamacSbUsuario getJefeId() {
        return jefeId;
    }

    public void setJefeId(GamacSbUsuario jefeId) {
        this.jefeId = jefeId;
    }

    @XmlTransient
    public Collection<GamacSbUsuario> getGamacSbUsuarioCollection1() {
        return gamacSbUsuarioCollection1;
    }

    public void setGamacSbUsuarioCollection1(Collection<GamacSbUsuario> gamacSbUsuarioCollection1) {
        this.gamacSbUsuarioCollection1 = gamacSbUsuarioCollection1;
    }

    public GamacSbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(GamacSbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public GamacSbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(GamacSbPersona personaId) {
        this.personaId = personaId;
    }

    @XmlTransient
    public Collection<GamacAmaLicenciaDeUso> getGamacAmaLicenciaDeUsoCollection() {
        return gamacAmaLicenciaDeUsoCollection;
    }

    public void setGamacAmaLicenciaDeUsoCollection(Collection<GamacAmaLicenciaDeUso> gamacAmaLicenciaDeUsoCollection) {
        this.gamacAmaLicenciaDeUsoCollection = gamacAmaLicenciaDeUsoCollection;
    }

    @XmlTransient
    public Collection<GamacAmaTarjetaPropiedad> getGamacAmaTarjetaPropiedadCollection() {
        return gamacAmaTarjetaPropiedadCollection;
    }

    public void setGamacAmaTarjetaPropiedadCollection(Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection) {
        this.gamacAmaTarjetaPropiedadCollection = gamacAmaTarjetaPropiedadCollection;
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
        if (!(object instanceof GamacSbUsuario)) {
            return false;
        }
        GamacSbUsuario other = (GamacSbUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacSbUsuario[ id=" + id + " ]";
    }
  
}
