/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.data;

import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbPersona;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.bdintegrado.data.AmaFoto;

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
    @NamedQuery(name = "ConsultaAmaLicenciaDeUso.findAll", query = "SELECT a FROM ConsultaAmaLicenciaDeUso a"),
    @NamedQuery(name = "ConsultaAmaLicenciaDeUso.findById", query = "SELECT a FROM ConsultaAmaLicenciaDeUso a WHERE a.id = :id"),
    @NamedQuery(name = "ConsultaAmaLicenciaDeUso.findByNroLicencia", query = "SELECT a FROM ConsultaAmaLicenciaDeUso a WHERE a.nroLicencia = :nroLicencia"),
    @NamedQuery(name = "ConsultaAmaLicenciaDeUso.findByFechaInscripcion", query = "SELECT a FROM ConsultaAmaLicenciaDeUso a WHERE a.fechaInscripcion = :fechaInscripcion"),
    @NamedQuery(name = "ConsultaAmaLicenciaDeUso.findByFechaEmision", query = "SELECT a FROM ConsultaAmaLicenciaDeUso a WHERE a.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "ConsultaAmaLicenciaDeUso.findByFechaVencimietnto", query = "SELECT a FROM ConsultaAmaLicenciaDeUso a WHERE a.fechaVencimietnto = :fechaVencimietnto"),
    @NamedQuery(name = "ConsultaAmaLicenciaDeUso.findByDescripcion", query = "SELECT a FROM ConsultaAmaLicenciaDeUso a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "ConsultaAmaLicenciaDeUso.findByActivo", query = "SELECT a FROM ConsultaAmaLicenciaDeUso a WHERE a.activo = :activo"),
    @NamedQuery(name = "ConsultaAmaLicenciaDeUso.findByAudLogin", query = "SELECT a FROM ConsultaAmaLicenciaDeUso a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "ConsultaAmaLicenciaDeUso.findByAudNumIp", query = "SELECT a FROM ConsultaAmaLicenciaDeUso a WHERE a.audNumIp = :audNumIp")})
public class ConsultaAmaLicenciaDeUso implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_LICENCIA_DE_USO")
    @SequenceGenerator(name = "SEQ_CONSULTA_AMA_LICENCIA_DE_USO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_LICENCIA_DE_USO", allocationSize = 1)
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "EMITIDO")
    private short emitido;
    @Size(max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "licenciaId")
    private List<ConsultaAmaTipoLicencia> amaTipoLicenciaList;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaTipoGamac estadoId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioId;
    @JoinColumn(name = "PERSONA_PADRE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaPadreId;
    @JoinColumn(name = "REPRESENTANTE_LEGAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona representanteLegalId;
    @JoinColumn(name = "PERSONA_LICENCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaLicenciaId;
    @JoinColumn(name = "DIRECCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDireccion direccionId;
    @JoinColumn(name = "FOTO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private AmaFoto fotoId;

    public ConsultaAmaLicenciaDeUso() {
    }

    public ConsultaAmaLicenciaDeUso(Long id) {
        this.id = id;
    }

    public ConsultaAmaLicenciaDeUso(Long id, Long nroLicencia, Date fechaInscripcion, Date fechaEmision, Date fechaVencimietnto, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroLicencia = nroLicencia;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaEmision = fechaEmision;
        this.fechaVencimietnto = fechaVencimietnto;
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

    @XmlTransient
    public List<ConsultaAmaTipoLicencia> getAmaTipoLicenciaList() {
        return amaTipoLicenciaList;
    }

    public void setAmaTipoLicenciaList(List<ConsultaAmaTipoLicencia> amaTipoLicenciaList) {
        this.amaTipoLicenciaList = amaTipoLicenciaList;
    }

    public ConsultaTipoGamac getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(ConsultaTipoGamac estadoId) {
        this.estadoId = estadoId;
    }
    
    public SbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public SbPersona getPersonaPadreId() {
        return personaPadreId;
    }

    public void setPersonaPadreId(SbPersona personaPadreId) {
        this.personaPadreId = personaPadreId;
    }

    public SbPersona getRepresentanteLegalId() {
        return representanteLegalId;
    }

    public void setRepresentanteLegalId(SbPersona representanteLegalId) {
        this.representanteLegalId = representanteLegalId;
    }

    public SbPersona getPersonaLicenciaId() {
        return personaLicenciaId;
    }

    public void setPersonaLicenciaId(SbPersona personaLicenciaId) {
        this.personaLicenciaId = personaLicenciaId;
    }

    public SbDireccion getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(SbDireccion direccionId) {
        this.direccionId = direccionId;
    }
    
    /*
    public AmaFoto getFotoId() {
        return fotoId;
    }

    public void setFotoId(AmaFoto fotoId) {
        this.fotoId = fotoId;
    }*/

    public AmaFoto getFotoId() {
        return fotoId;
    }

    public void setFotoId(AmaFoto fotoId) {
        this.fotoId = fotoId;
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
        if (!(object instanceof ConsultaAmaLicenciaDeUso)) {
            return false;
        }
        ConsultaAmaLicenciaDeUso other = (ConsultaAmaLicenciaDeUso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.renagi.data.ConsultaAmaLicenciaDeUso[ id=" + id + " ]";
    }
    
}
