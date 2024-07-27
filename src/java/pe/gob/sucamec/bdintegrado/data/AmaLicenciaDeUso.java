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
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sel.citas.data.CitaTurConstancia;

/**
 *
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_LICENCIA_DE_USO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaLicenciaDeUso.findAll", query = "SELECT a FROM AmaLicenciaDeUso a"),
    @NamedQuery(name = "AmaLicenciaDeUso.findById", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.id = :id"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByNroLicencia", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.nroLicencia = :nroLicencia"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByFechaInscripcion", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.fechaInscripcion = :fechaInscripcion"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByFechaEmision", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByFechaVencimietnto", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.fechaVencimietnto = :fechaVencimietnto"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByDescripcion", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByNroExpedienteAnula", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.nroExpedienteAnula = :nroExpedienteAnula"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByFechaAnula", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.fechaAnula = :fechaAnula"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByMotivoAnula", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.motivoAnula = :motivoAnula"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByEmitido", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.emitido = :emitido"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByHashQr", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.hashQr = :hashQr"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByActivo", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByAudLogin", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaLicenciaDeUso.findByAudNumIp", query = "SELECT a FROM AmaLicenciaDeUso a WHERE a.audNumIp = :audNumIp")})
public class AmaLicenciaDeUso implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_LICENCIA_DE_USO")
    @SequenceGenerator(name = "SEQ_AMA_LICENCIA_DE_USO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_LICENCIA_DE_USO", allocationSize = 1)
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
    @JoinColumn(name = "FOTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaFoto fotoId;
    @JoinColumn(name = "DIRECCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDireccionGt direccionId;
    @JoinColumn(name = "PERSONA_PADRE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaPadreId;
    @JoinColumn(name = "PERSONA_LICENCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaLicenciaId;
    @JoinColumn(name = "REPRESENTANTE_LEGAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersonaGt representanteLegalId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuarioId;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac estadoId;
    @JoinColumn(name = "RESTRICCION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac restriccionId;
    @OneToMany(mappedBy = "licenciaId")
    private List<AmaTarjetaPropiedad> amaTarjetaPropiedadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "licenciaId")
    private List<AmaTipoLicencia> amaTipoLicenciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "licenciaId")
    private List<CitaTurConstancia> turConsyanciaList;
    @Column(name = "ACTUAL")
    private short actual;

    public AmaLicenciaDeUso() {
    }

    public AmaLicenciaDeUso(Long id) {
        this.id = id;
    }

    public AmaLicenciaDeUso(Long id, Long nroLicencia, Date fechaInscripcion, Date fechaEmision, Date fechaVencimietnto, short emitido, short activo, String audLogin, String audNumIp) {
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

    public AmaFoto getFotoId() {
        return fotoId;
    }

    public void setFotoId(AmaFoto fotoId) {
        this.fotoId = fotoId;
    }

    @XmlTransient
    public List<AmaTarjetaPropiedad> getAmaTarjetaPropiedadList() {
        return amaTarjetaPropiedadList;
    }

    public void setAmaTarjetaPropiedadList(List<AmaTarjetaPropiedad> amaTarjetaPropiedadList) {
        this.amaTarjetaPropiedadList = amaTarjetaPropiedadList;
    }

    @XmlTransient
    public List<AmaTipoLicencia> getAmaTipoLicenciaList() {
        return amaTipoLicenciaList;
    }

    public void setAmaTipoLicenciaList(List<AmaTipoLicencia> amaTipoLicenciaList) {
        this.amaTipoLicenciaList = amaTipoLicenciaList;
    }

    public List<CitaTurConstancia> getTurConsyanciaList() {
        return turConsyanciaList;
    }

    public void setTurConsyanciaList(List<CitaTurConstancia> turConsyanciaList) {
        this.turConsyanciaList = turConsyanciaList;
    }

    public short getActual() {
        return actual;
    }

    public void setActual(short actual) {
        this.actual = actual;
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
        if (!(object instanceof AmaLicenciaDeUso)) {
            return false;
        }
        AmaLicenciaDeUso other = (AmaLicenciaDeUso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaLicenciaDeUso[ id=" + id + " ]";
    }

    /**
     * @return the direccionId
     */
    public SbDireccionGt getDireccionId() {
        return direccionId;
    }

    /**
     * @param direccionId the direccionId to set
     */
    public void setDireccionId(SbDireccionGt direccionId) {
        this.direccionId = direccionId;
    }

    /**
     * @return the personaPadreId
     */
    public SbPersonaGt getPersonaPadreId() {
        return personaPadreId;
    }

    /**
     * @param personaPadreId the personaPadreId to set
     */
    public void setPersonaPadreId(SbPersonaGt personaPadreId) {
        this.personaPadreId = personaPadreId;
    }

    /**
     * @return the personaLicenciaId
     */
    public SbPersonaGt getPersonaLicenciaId() {
        return personaLicenciaId;
    }

    /**
     * @param personaLicenciaId the personaLicenciaId to set
     */
    public void setPersonaLicenciaId(SbPersonaGt personaLicenciaId) {
        this.personaLicenciaId = personaLicenciaId;
    }

    /**
     * @return the representanteLegalId
     */
    public SbPersonaGt getRepresentanteLegalId() {
        return representanteLegalId;
    }

    /**
     * @param representanteLegalId the representanteLegalId to set
     */
    public void setRepresentanteLegalId(SbPersonaGt representanteLegalId) {
        this.representanteLegalId = representanteLegalId;
    }

    /**
     * @return the usuarioId
     */
    public SbUsuarioGt getUsuarioId() {
        return usuarioId;
    }

    /**
     * @param usuarioId the usuarioId to set
     */
    public void setUsuarioId(SbUsuarioGt usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * @return the estadoId
     */
    public TipoGamac getEstadoId() {
        return estadoId;
    }

    /**
     * @param estadoId the estadoId to set
     */
    public void setEstadoId(TipoGamac estadoId) {
        this.estadoId = estadoId;
    }

    public TipoGamac getRestriccionId() {
        return restriccionId;
    }

    public void setRestriccionId(TipoGamac restriccionId) {
        this.restriccionId = restriccionId;
    }
    
}
