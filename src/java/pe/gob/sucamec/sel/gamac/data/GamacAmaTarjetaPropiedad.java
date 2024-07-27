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
@Table(name = "AMA_TARJETA_PROPIEDAD", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findAll", query = "SELECT g FROM GamacAmaTarjetaPropiedad g"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findById", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.id = :id"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByArma", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.armaId = :armaId"),    
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByNroExpediente", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByNroResolucion", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.nroResolucion = :nroResolucion"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByFechaInscripcion", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.fechaInscripcion = :fechaInscripcion"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByFechaEmision", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByNumeroCp", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.numeroCp = :numeroCp"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByFechaPago", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.fechaPago = :fechaPago"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByDescripcion", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByFechaEntregaPropietario", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.fechaEntregaPropietario = :fechaEntregaPropietario"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByEmitido", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.emitido = :emitido"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByHashQr", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.hashQr = :hashQr"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByActivo", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByAudLogin", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacAmaTarjetaPropiedad.findByAudNumIp", query = "SELECT g FROM GamacAmaTarjetaPropiedad g WHERE g.audNumIp = :audNumIp")})
public class GamacAmaTarjetaPropiedad implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_AMA_TARJETA_PROPIEDAD")
    @SequenceGenerator(name = "SEQ_GAMAC_AMA_TARJETA_PROPIEDAD", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_TARJETA_PROPIEDAD", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Size(max = 10)
    @Column(name = "NRO_RESOLUCION")
    private String nroResolucion;
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
    @Column(name = "NUMERO_CP")
    private Long numeroCp;
    @Column(name = "FECHA_PAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "FECHA_ENTREGA_PROPIETARIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntregaPropietario;
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
    @OneToMany(mappedBy = "tarjetaPropiedadId")
    private Collection<GamacAmaAdmunDetalleTrans> gamacAmaAdmunDetalleTransCollection;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoGamac estadoId;
    @JoinColumn(name = "TIPO_CP", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoGamac tipoCp;
    @JoinColumn(name = "RESTRICCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoGamac restriccionId;
    @JoinColumn(name = "MODALIDAD_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoGamac modalidadId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbUsuario usuarioId;
    @JoinColumn(name = "PERSONA_VENDEDOR_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbPersona personaVendedorId;
    @JoinColumn(name = "PERSONA_COMPRADOR_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbPersona personaCompradorId;
    @JoinColumn(name = "DIRECCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbDireccion direccionId;
    @JoinColumn(name = "LICENCIA_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacAmaLicenciaDeUso licenciaId;
    @JoinColumn(name = "ARMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaArma armaId;

    public GamacAmaTarjetaPropiedad() {
    }

    public GamacAmaTarjetaPropiedad(Long id) {
        this.id = id;
    }

    public GamacAmaTarjetaPropiedad(Long id, String nroExpediente, Date fechaInscripcion, Date fechaEmision, short emitido, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroExpediente = nroExpediente;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaEmision = fechaEmision;
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

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public String getNroResolucion() {
        return nroResolucion;
    }

    public void setNroResolucion(String nroResolucion) {
        this.nroResolucion = nroResolucion;
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

    public Long getNumeroCp() {
        return numeroCp;
    }

    public void setNumeroCp(Long numeroCp) {
        this.numeroCp = numeroCp;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaEntregaPropietario() {
        return fechaEntregaPropietario;
    }

    public void setFechaEntregaPropietario(Date fechaEntregaPropietario) {
        this.fechaEntregaPropietario = fechaEntregaPropietario;
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

    @XmlTransient
    public Collection<GamacAmaAdmunDetalleTrans> getGamacAmaAdmunDetalleTransCollection() {
        return gamacAmaAdmunDetalleTransCollection;
    }

    public void setGamacAmaAdmunDetalleTransCollection(Collection<GamacAmaAdmunDetalleTrans> gamacAmaAdmunDetalleTransCollection) {
        this.gamacAmaAdmunDetalleTransCollection = gamacAmaAdmunDetalleTransCollection;
    }

    public GamacTipoGamac getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(GamacTipoGamac estadoId) {
        this.estadoId = estadoId;
    }

    public GamacTipoGamac getTipoCp() {
        return tipoCp;
    }

    public void setTipoCp(GamacTipoGamac tipoCp) {
        this.tipoCp = tipoCp;
    }

    public GamacTipoGamac getRestriccionId() {
        return restriccionId;
    }

    public void setRestriccionId(GamacTipoGamac restriccionId) {
        this.restriccionId = restriccionId;
    }

    public GamacTipoGamac getModalidadId() {
        return modalidadId;
    }

    public void setModalidadId(GamacTipoGamac modalidadId) {
        this.modalidadId = modalidadId;
    }

    public GamacSbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(GamacSbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public GamacSbPersona getPersonaVendedorId() {
        return personaVendedorId;
    }

    public void setPersonaVendedorId(GamacSbPersona personaVendedorId) {
        this.personaVendedorId = personaVendedorId;
    }

    public GamacSbPersona getPersonaCompradorId() {
        return personaCompradorId;
    }

    public void setPersonaCompradorId(GamacSbPersona personaCompradorId) {
        this.personaCompradorId = personaCompradorId;
    }

    public GamacSbDireccion getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(GamacSbDireccion direccionId) {
        this.direccionId = direccionId;
    }

    public GamacAmaLicenciaDeUso getLicenciaId() {
        return licenciaId;
    }

    public void setLicenciaId(GamacAmaLicenciaDeUso licenciaId) {
        this.licenciaId = licenciaId;
    }

    public GamacAmaArma getArmaId() {
        return armaId;
    }

    public void setArmaId(GamacAmaArma armaId) {
        this.armaId = armaId;
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
        if (!(object instanceof GamacAmaTarjetaPropiedad)) {
            return false;
        }
        GamacAmaTarjetaPropiedad other = (GamacAmaTarjetaPropiedad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacAmaTarjetaPropiedad[ id=" + id + " ]";
    }
    
}
