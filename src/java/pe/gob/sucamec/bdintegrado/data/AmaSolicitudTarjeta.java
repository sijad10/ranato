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
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_SOLICITUD_TARJETA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaSolicitudTarjeta.findAll", query = "SELECT a FROM AmaSolicitudTarjeta a"),
    @NamedQuery(name = "AmaSolicitudTarjeta.findById", query = "SELECT a FROM AmaSolicitudTarjeta a WHERE a.id = :id"),
    @NamedQuery(name = "AmaSolicitudTarjeta.findByNroExpediente", query = "SELECT a FROM AmaSolicitudTarjeta a WHERE a.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "AmaSolicitudTarjeta.findByFechaCreacion", query = "SELECT a FROM AmaSolicitudTarjeta a WHERE a.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "AmaSolicitudTarjeta.findByFechaTransmision", query = "SELECT a FROM AmaSolicitudTarjeta a WHERE a.fechaTransmision = :fechaTransmision"),
    @NamedQuery(name = "AmaSolicitudTarjeta.findByObservacion", query = "SELECT a FROM AmaSolicitudTarjeta a WHERE a.observacion = :observacion"),
    @NamedQuery(name = "AmaSolicitudTarjeta.findByActivo", query = "SELECT a FROM AmaSolicitudTarjeta a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaSolicitudTarjeta.findByAudLogin", query = "SELECT a FROM AmaSolicitudTarjeta a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaSolicitudTarjeta.findByAudNumIp", query = "SELECT a FROM AmaSolicitudTarjeta a WHERE a.audNumIp = :audNumIp")})
public class AmaSolicitudTarjeta implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_SOLICITUD_TARJETA")
    @SequenceGenerator(name = "SEQ_AMA_SOLICITUD_TARJETA", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_SOLICITUD_TARJETA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "FECHA_TRANSMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaTransmision;
    @Size(max = 500)
    @Column(name = "OBSERVACION")
    private String observacion;
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
    private TipoGamac estadoId;
    @JoinColumn(name = "TIPO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoOpeId;
    @JoinColumn(name = "TIPO_TRAMITE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoTramiteId;
    @JoinColumn(name = "USUARIO_CREACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuarioCreacionId;
    @JoinColumn(name = "COMPROBANTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbRecibos comprobanteId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    @JoinColumn(name = "REPRESENTANTE_LEGAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersonaGt representanteLegalId;
    @JoinColumn(name = "TARJETA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaTarjetaPropiedad tarjetaId;
    @OneToMany(mappedBy = "armaId")
    private List<AmaSolicitudTarjeta> amaSolicitudTarjetaList;
    @JoinColumn(name = "ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaSolicitudTarjeta armaId;
    @JoinColumn(name = "ACTA_INTERNAMIENTO_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaGuiaTransito actaInternamientoId;

    public AmaSolicitudTarjeta() {
    }

    public AmaSolicitudTarjeta(Long id) {
        this.id = id;
    }

    public AmaSolicitudTarjeta(Long id, Date fechaCreacion, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaTransmision() {
        return fechaTransmision;
    }

    public void setFechaTransmision(Date fechaTransmision) {
        this.fechaTransmision = fechaTransmision;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public TipoGamac getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoGamac estadoId) {
        this.estadoId = estadoId;
    }

    public TipoGamac getTipoOpeId() {
        return tipoOpeId;
    }

    public void setTipoOpeId(TipoGamac tipoOpeId) {
        this.tipoOpeId = tipoOpeId;
    }

    public TipoBaseGt getTipoTramiteId() {
        return tipoTramiteId;
    }

    public void setTipoTramiteId(TipoBaseGt tipoTramiteId) {
        this.tipoTramiteId = tipoTramiteId;
    }

    public SbUsuarioGt getUsuarioCreacionId() {
        return usuarioCreacionId;
    }

    public void setUsuarioCreacionId(SbUsuarioGt usuarioCreacionId) {
        this.usuarioCreacionId = usuarioCreacionId;
    }

    public SbRecibos getComprobanteId() {
        return comprobanteId;
    }

    public void setComprobanteId(SbRecibos comprobanteId) {
        this.comprobanteId = comprobanteId;
    }

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
    }

    public SbPersonaGt getRepresentanteLegalId() {
        return representanteLegalId;
    }

    public void setRepresentanteLegalId(SbPersonaGt representanteLegalId) {
        this.representanteLegalId = representanteLegalId;
    }

    public AmaTarjetaPropiedad getTarjetaId() {
        return tarjetaId;
    }

    public void setTarjetaId(AmaTarjetaPropiedad tarjetaId) {
        this.tarjetaId = tarjetaId;
    }

    @XmlTransient
    public List<AmaSolicitudTarjeta> getAmaSolicitudTarjetaList() {
        return amaSolicitudTarjetaList;
    }

    public void setAmaSolicitudTarjetaList(List<AmaSolicitudTarjeta> amaSolicitudTarjetaList) {
        this.amaSolicitudTarjetaList = amaSolicitudTarjetaList;
    }

    public AmaSolicitudTarjeta getArmaId() {
        return armaId;
    }

    public void setArmaId(AmaSolicitudTarjeta armaId) {
        this.armaId = armaId;
    }

    public AmaGuiaTransito getActaInternamientoId() {
        return actaInternamientoId;
    }

    public void setActaInternamientoId(AmaGuiaTransito actaInternamientoId) {
        this.actaInternamientoId = actaInternamientoId;
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
        if (!(object instanceof AmaSolicitudTarjeta)) {
            return false;
        }
        AmaSolicitudTarjeta other = (AmaSolicitudTarjeta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaSolicitudTarjeta[ id=" + id + " ]";
    }
    
}
