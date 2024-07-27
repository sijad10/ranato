/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.data;

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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_TARJETA_PROPIEDAD", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findAll", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a"),
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findById", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a WHERE a.id = :id"),
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findByNroExpediente", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a WHERE a.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findByNroResolucion", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a WHERE a.nroResolucion = :nroResolucion"),
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findByFechaInscripcion", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a WHERE a.fechaInscripcion = :fechaInscripcion"),
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findByFechaEmision", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a WHERE a.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findByNumeroCp", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a WHERE a.numeroCp = :numeroCp"),
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findByFechaPago", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a WHERE a.fechaPago = :fechaPago"),
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findByDescripcion", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findByFechaEntregaPropietario", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a WHERE a.fechaEntregaPropietario = :fechaEntregaPropietario"),
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findByActivo", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a WHERE a.activo = :activo"),
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findByAudLogin", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "ConsultaAmaTarjetaPropiedad.findByAudNumIp", query = "SELECT a FROM ConsultaAmaTarjetaPropiedad a WHERE a.audNumIp = :audNumIp")})
public class ConsultaAmaTarjetaPropiedad implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_TARJETA_PROPIEDAD")
    @SequenceGenerator(name = "SEQ_CONSULTA_AMA_TARJETA_PROPIEDAD", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_TARJETA_PROPIEDAD", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Size(min = 1, max = 10)
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
    @JoinColumn(name = "MODALIDAD_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaTipoGamac modalidadId;
    /*@ManyToMany(mappedBy = "amaTarjetaPropiedadList")
    private List<AmaSolicitudRecojo> amaSolicitudRecojoList;*/
    @JoinColumn(name = "TIPO_CP", referencedColumnName = "ID")
    @ManyToOne
    private ConsultaTipoGamac tipoCp;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaTipoGamac estadoId;
    @JoinColumn(name = "RESTRICCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaTipoGamac restriccionId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioId;
    @JoinColumn(name = "PERSONA_VENDEDOR_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona personaVendedorId;
    @JoinColumn(name = "PERSONA_COMPRADOR_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaCompradorId;
    @JoinColumn(name = "DIRECCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDireccion direccionId;
    @JoinColumn(name = "LICENCIA_ID", referencedColumnName = "ID")
    @ManyToOne
    private ConsultaAmaLicenciaDeUso licenciaId;
    @JoinColumn(name = "ARMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaAmaArma armaId;

    public ConsultaAmaTarjetaPropiedad() {
    }

    public ConsultaAmaTarjetaPropiedad(Long id) {
        this.id = id;
    }

    public ConsultaAmaTarjetaPropiedad(Long id, String nroExpediente, Date fechaInscripcion, Date fechaEmision, Long numeroCp, Date fechaPago, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroExpediente = nroExpediente;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaEmision = fechaEmision;
        this.numeroCp = numeroCp;
        this.fechaPago = fechaPago;
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

    public ConsultaTipoGamac getModalidadId() {
        return modalidadId;
    }

    public void setModalidadId(ConsultaTipoGamac modalidadId) {
        this.modalidadId = modalidadId;
    }

    public ConsultaTipoGamac getRestriccionId() {
        return restriccionId;
    }

    public void setRestriccionId(ConsultaTipoGamac restriccionId) {
        this.restriccionId = restriccionId;
    }
    
    public SbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public ConsultaTipoGamac getTipoCp() {
        return tipoCp;
    }

    public void setTipoCp(ConsultaTipoGamac tipoCp) {
        this.tipoCp = tipoCp;
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

    /*
    @XmlTransient
    public List<AmaSolicitudRecojo> getAmaSolicitudRecojoList() {
        return amaSolicitudRecojoList;
    }

    public void setAmaSolicitudRecojoList(List<AmaSolicitudRecojo> amaSolicitudRecojoList) {
        this.amaSolicitudRecojoList = amaSolicitudRecojoList;
    }
*/

    public ConsultaTipoGamac getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(ConsultaTipoGamac estadoId) {
        this.estadoId = estadoId;
    }

    public SbPersona getPersonaVendedorId() {
        return personaVendedorId;
    }

    public void setPersonaVendedorId(SbPersona personaVendedorId) {
        this.personaVendedorId = personaVendedorId;
    }

    public SbPersona getPersonaCompradorId() {
        return personaCompradorId;
    }

    public void setPersonaCompradorId(SbPersona personaCompradorId) {
        this.personaCompradorId = personaCompradorId;
    }

    public SbDireccion getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(SbDireccion direccionId) {
        this.direccionId = direccionId;
    }

    public ConsultaAmaLicenciaDeUso getLicenciaId() {
        return licenciaId;
    }

    public void setLicenciaId(ConsultaAmaLicenciaDeUso licenciaId) {
        this.licenciaId = licenciaId;
    }

    public ConsultaAmaArma getArmaId() {
        return armaId;
    }

    public void setArmaId(ConsultaAmaArma armaId) {
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
        if (!(object instanceof ConsultaAmaTarjetaPropiedad)) {
            return false;
        }
        ConsultaAmaTarjetaPropiedad other = (ConsultaAmaTarjetaPropiedad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemagamac.data.AmaTarjetaPropiedad[ id=" + id + " ]";
    }
    
}
