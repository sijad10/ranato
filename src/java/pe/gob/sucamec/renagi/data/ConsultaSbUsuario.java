/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.data;

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
    @NamedQuery(name = "ConsultaSbUsuario.findAll", query = "SELECT g FROM ConsultaSbUsuario g"),
    @NamedQuery(name = "ConsultaSbUsuario.findById", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.id = :id"),
    @NamedQuery(name = "ConsultaSbUsuario.findByLogin", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.login = :login"),
    @NamedQuery(name = "ConsultaSbUsuario.findByClave", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.clave = :clave"),
    @NamedQuery(name = "ConsultaSbUsuario.findByFechaIni", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.fechaIni = :fechaIni"),
    @NamedQuery(name = "ConsultaSbUsuario.findByFechaFin", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.fechaFin = :fechaFin"),
    @NamedQuery(name = "ConsultaSbUsuario.findByDescripcion", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "ConsultaSbUsuario.findByNumDoc", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.numDoc = :numDoc"),
    @NamedQuery(name = "ConsultaSbUsuario.findByNombres", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.nombres = :nombres"),
    @NamedQuery(name = "ConsultaSbUsuario.findByApePat", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.apePat = :apePat"),
    @NamedQuery(name = "ConsultaSbUsuario.findByApeMat", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.apeMat = :apeMat"),
    @NamedQuery(name = "ConsultaSbUsuario.findByCorreo", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.correo = :correo"),
    @NamedQuery(name = "ConsultaSbUsuario.findByCantidadUsuario", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.cantidadUsuario = :cantidadUsuario"),
    @NamedQuery(name = "ConsultaSbUsuario.findByActivo", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.activo = :activo"),
    @NamedQuery(name = "ConsultaSbUsuario.findByAudLogin", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "ConsultaSbUsuario.findByAudNumIp", query = "SELECT g FROM ConsultaSbUsuario g WHERE g.audNumIp = :audNumIp")})
public class ConsultaSbUsuario implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_SB_USUARIO")
    @SequenceGenerator(name = "SEQ_CONSULTA_SB_USUARIO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_USUARIO", allocationSize = 1)
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
    private ConsultaTipoBase tipoId;
    @JoinColumn(name = "TIPO_AUTEN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaTipoBase tipoAutenId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne
    private ConsultaTipoBase areaId;
    @OneToMany(mappedBy = "jefeId")
    private Collection<ConsultaSbUsuario> gamacSbUsuarioCollection;
    @JoinColumn(name = "JEFE_ID", referencedColumnName = "ID")
    @ManyToOne
    private ConsultaSbUsuario jefeId;
    @OneToMany(mappedBy = "usuarioId")
    private Collection<ConsultaSbUsuario> gamacSbUsuarioCollection1;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne
    private ConsultaSbUsuario usuarioId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaSbPersona personaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private Collection<ConsultaAmaLicenciaDeUso> gamacAmaLicenciaDeUsoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private Collection<ConsultaAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection;

    public ConsultaSbUsuario() {
    }

    public ConsultaSbUsuario(Long id) {
        this.id = id;
    }

    public ConsultaSbUsuario(Long id, String login, String clave, Date fechaIni, String numDoc, Long cantidadUsuario, short activo, String audLogin, String audNumIp) {
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

    public ConsultaTipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(ConsultaTipoBase tipoId) {
        this.tipoId = tipoId;
    }

    public ConsultaTipoBase getTipoAutenId() {
        return tipoAutenId;
    }

    public void setTipoAutenId(ConsultaTipoBase tipoAutenId) {
        this.tipoAutenId = tipoAutenId;
    }

    public ConsultaTipoBase getAreaId() {
        return areaId;
    }

    public void setAreaId(ConsultaTipoBase areaId) {
        this.areaId = areaId;
    }

    @XmlTransient
    public Collection<ConsultaSbUsuario> getGamacSbUsuarioCollection() {
        return gamacSbUsuarioCollection;
    }

    public void setGamacSbUsuarioCollection(Collection<ConsultaSbUsuario> gamacSbUsuarioCollection) {
        this.gamacSbUsuarioCollection = gamacSbUsuarioCollection;
    }

    public ConsultaSbUsuario getJefeId() {
        return jefeId;
    }

    public void setJefeId(ConsultaSbUsuario jefeId) {
        this.jefeId = jefeId;
    }

    @XmlTransient
    public Collection<ConsultaSbUsuario> getGamacSbUsuarioCollection1() {
        return gamacSbUsuarioCollection1;
    }

    public void setGamacSbUsuarioCollection1(Collection<ConsultaSbUsuario> gamacSbUsuarioCollection1) {
        this.gamacSbUsuarioCollection1 = gamacSbUsuarioCollection1;
    }

    public ConsultaSbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(ConsultaSbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public ConsultaSbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(ConsultaSbPersona personaId) {
        this.personaId = personaId;
    }

    @XmlTransient
    public Collection<ConsultaAmaLicenciaDeUso> getGamacAmaLicenciaDeUsoCollection() {
        return gamacAmaLicenciaDeUsoCollection;
    }

    public void setGamacAmaLicenciaDeUsoCollection(Collection<ConsultaAmaLicenciaDeUso> gamacAmaLicenciaDeUsoCollection) {
        this.gamacAmaLicenciaDeUsoCollection = gamacAmaLicenciaDeUsoCollection;
    }

    @XmlTransient
    public Collection<ConsultaAmaTarjetaPropiedad> getGamacAmaTarjetaPropiedadCollection() {
        return gamacAmaTarjetaPropiedadCollection;
    }

    public void setGamacAmaTarjetaPropiedadCollection(Collection<ConsultaAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection) {
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
        if (!(object instanceof ConsultaSbUsuario)) {
            return false;
        }
        ConsultaSbUsuario other = (ConsultaSbUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.renagi.data.ConsultaSbUsuario[ id=" + id + " ]";
    }
    
}
