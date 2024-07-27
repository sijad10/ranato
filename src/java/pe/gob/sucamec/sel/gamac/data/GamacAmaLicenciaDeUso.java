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
@Table(name = "AMA_LICENCIA_DE_USO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findAll", query = "SELECT g FROM GamacAmaLicenciaDeUso g"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findById", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.id = :id"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByNroLicencia", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.nroLicencia = :nroLicencia"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByFechaInscripcion", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.fechaInscripcion = :fechaInscripcion"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByFechaEmision", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByFechaVencimietnto", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.fechaVencimietnto = :fechaVencimietnto"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByDescripcion", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByNroExpedienteAnula", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.nroExpedienteAnula = :nroExpedienteAnula"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByFechaAnula", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.fechaAnula = :fechaAnula"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByMotivoAnula", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.motivoAnula = :motivoAnula"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByEmitido", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.emitido = :emitido"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByHashQr", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.hashQr = :hashQr"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByActivo", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByAudLogin", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacAmaLicenciaDeUso.findByAudNumIp", query = "SELECT g FROM GamacAmaLicenciaDeUso g WHERE g.audNumIp = :audNumIp")})
public class GamacAmaLicenciaDeUso implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_AMA_LICENCIA_DE_USO")
    @SequenceGenerator(name = "SEQ_GAMAC_AMA_LICENCIA_DE_USO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_LICENCIA_DE_USO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_LICENCIA")
    private Long nroLicencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INSCRIPCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_VENCIMIETNTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimietnto;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE_ANULA")
    private String nroExpedienteAnula;
    @Column(name = "FECHA_ANULA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnula;
    @Size(max = 500)
    @Column(name = "MOTIVO_ANULA")
    private String motivoAnula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EMITIDO")
    private short emitido;
    @Size(max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
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
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoGamac estadoId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbUsuario usuarioId;
    @JoinColumn(name = "PERSONA_PADRE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbPersona personaPadreId;
    @JoinColumn(name = "PERSONA_LICENCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbPersona personaLicenciaId;
    @JoinColumn(name = "REPRESENTANTE_LEGAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbPersona representanteLegalId;
    @JoinColumn(name = "DIRECCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbDireccion direccionId;
    @JoinColumn(name = "FOTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaFoto fotoId;
    @OneToMany(mappedBy = "licenciaId")
    private Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection;
    @OneToMany(mappedBy = "licenciaId")
    private Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection;

    public GamacAmaLicenciaDeUso() {
    }

    public GamacAmaLicenciaDeUso(Long id) {
        this.id = id;
    }

    public GamacAmaLicenciaDeUso(Long id, Long nroLicencia, Date fechaInscripcion, Date fechaEmision, Date fechaVencimietnto, short emitido, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroLicencia = nroLicencia;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaEmision = fechaEmision;
        this.fechaVencimietnto = fechaVencimietnto;
        this.emitido = emitido;
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

    public Long getNroLicencia() {
        return nroLicencia;
    }

    public void setNroLicencia(Long nroLicencia) {
        this.nroLicencia = nroLicencia;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVencimietnto() {
        return fechaVencimietnto;
    }

    public void setFechaVencimietnto(Date fechaVencimietnto) {
        this.fechaVencimietnto = fechaVencimietnto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNroExpedienteAnula() {
        return nroExpedienteAnula;
    }

    public void setNroExpedienteAnula(String nroExpedienteAnula) {
        this.nroExpedienteAnula = nroExpedienteAnula;
    }

    public Date getFechaAnula() {
        return fechaAnula;
    }

    public void setFechaAnula(Date fechaAnula) {
        this.fechaAnula = fechaAnula;
    }

    public String getMotivoAnula() {
        return motivoAnula;
    }

    public void setMotivoAnula(String motivoAnula) {
        this.motivoAnula = motivoAnula;
    }

    public short getEmitido() {
        return emitido;
    }

    public void setEmitido(short emitido) {
        this.emitido = emitido;
    }

    public String getHashQr() {
        return hashQr;
    }

    public void setHashQr(String hashQr) {
        this.hashQr = hashQr;
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

    public GamacTipoGamac getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(GamacTipoGamac estadoId) {
        this.estadoId = estadoId;
    }

    public GamacSbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(GamacSbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public GamacSbPersona getPersonaPadreId() {
        return personaPadreId;
    }

    public void setPersonaPadreId(GamacSbPersona personaPadreId) {
        this.personaPadreId = personaPadreId;
    }

    public GamacSbPersona getPersonaLicenciaId() {
        return personaLicenciaId;
    }

    public void setPersonaLicenciaId(GamacSbPersona personaLicenciaId) {
        this.personaLicenciaId = personaLicenciaId;
    }

    public GamacSbPersona getRepresentanteLegalId() {
        return representanteLegalId;
    }

    public void setRepresentanteLegalId(GamacSbPersona representanteLegalId) {
        this.representanteLegalId = representanteLegalId;
    }

    public GamacSbDireccion getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(GamacSbDireccion direccionId) {
        this.direccionId = direccionId;
    }

    public GamacAmaFoto getFotoId() {
        return fotoId;
    }

    public void setFotoId(GamacAmaFoto fotoId) {
        this.fotoId = fotoId;
    }

    @XmlTransient
    public Collection<GamacAmaAdmunTransaccion> getGamacAmaAdmunTransaccionCollection() {
        return gamacAmaAdmunTransaccionCollection;
    }

    public void setGamacAmaAdmunTransaccionCollection(Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection) {
        this.gamacAmaAdmunTransaccionCollection = gamacAmaAdmunTransaccionCollection;
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
        if (!(object instanceof GamacAmaLicenciaDeUso)) {
            return false;
        }
        GamacAmaLicenciaDeUso other = (GamacAmaLicenciaDeUso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacAmaLicenciaDeUso[ id=" + id + " ]";
    }
    
}
