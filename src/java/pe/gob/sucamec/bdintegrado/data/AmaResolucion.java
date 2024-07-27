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
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_RESOLUCION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaResolucion.findAll", query = "SELECT a FROM AmaResolucion a"),
    @NamedQuery(name = "AmaResolucion.findById", query = "SELECT a FROM AmaResolucion a WHERE a.id = :id"),
    @NamedQuery(name = "AmaResolucion.findByNroExpediente", query = "SELECT a FROM AmaResolucion a WHERE a.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "AmaResolucion.findByNroResolucion", query = "SELECT a FROM AmaResolucion a WHERE a.nroResolucion = :nroResolucion"),
    @NamedQuery(name = "AmaResolucion.findByHashQr", query = "SELECT a FROM AmaResolucion a WHERE a.hashQr = :hashQr"),
    @NamedQuery(name = "AmaResolucion.findByActivo", query = "SELECT a FROM AmaResolucion a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaResolucion.findByAudLogin", query = "SELECT a FROM AmaResolucion a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaResolucion.findByAudNumIp", query = "SELECT a FROM AmaResolucion a WHERE a.audNumIp = :audNumIp")})
public class AmaResolucion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_RESOLUCION")
    @SequenceGenerator(name = "SEQ_AMA_RESOLUCION", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_RESOLUCION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Size(max = 100)
    @Column(name = "NRO_RESOLUCION")
    private String nroResolucion;
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
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "PROCESO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt procesoId;
    @JoinColumn(name = "TIPO_RESOLUCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoResolucionId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
    @Column(name = "FECHA_FIRMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFirma;
     @OneToMany(mappedBy = "resolucionId")
    private List<AmaObservaTraza> amaObservaTrazaList;
    @JoinColumn(name = "MOTIVO_RECTIFICACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac motivoRectificacionId;
    @JoinColumn(name = "NUM_OFICIO_OBS_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaDocumento numOficioObsId;
    @OneToMany(mappedBy = "resolucionId")
    private List<AmaResolucion> amaResolucionList;
    @JoinColumn(name = "RESOLUCION_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaResolucion resolucionId;
    @JoinColumn(name = "REPRESENTANTE_LEGAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona representanteLegalId;
    @JoinColumn(name = "USUARIO_FIRMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuario usuarioFirmaId;
    @JoinColumn(name = "USUARIO_CREACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioCreacionId;
    @JoinColumn(name = "TIPO_REG_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoRegId;
    @JoinColumn(name = "TIPO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoOpeId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt areaId;
    @JoinColumn(name = "EVENTO_TRAZA_RES_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac eventoTrazaResId;

    public AmaResolucion() {
    }

    public AmaResolucion(Long id) {
        this.id = id;
    }

    public AmaResolucion(Long id, short activo, String audLogin, String audNumIp) {
        this.id = id;
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

    public TipoBaseGt getProcesoId() {
        return procesoId;
    }

    public void setProcesoId(TipoBaseGt procesoId) {
        this.procesoId = procesoId;
    }

    public TipoGamac getTipoResolucionId() {
        return tipoResolucionId;
    }

    public void setTipoResolucionId(TipoGamac tipoResolucionId) {
        this.tipoResolucionId = tipoResolucionId;
    }

    public SbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersona personaId) {
        this.personaId = personaId;
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
        if (!(object instanceof AmaResolucion)) {
            return false;
        }
        AmaResolucion other = (AmaResolucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaResolucion[ id=" + id + " ]";
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Date fechaFirma) {
        this.fechaFirma = fechaFirma;
    }
    @XmlTransient
    public List<AmaObservaTraza> getAmaObservaTrazaList() {
        return amaObservaTrazaList;
    }

    public void setAmaObservaTrazaList(List<AmaObservaTraza> amaObservaTrazaList) {
        this.amaObservaTrazaList = amaObservaTrazaList;
    }

    public TipoGamac getMotivoRectificacionId() {
        return motivoRectificacionId;
    }

    public void setMotivoRectificacionId(TipoGamac motivoRectificacionId) {
        this.motivoRectificacionId = motivoRectificacionId;
    }

    public AmaDocumento getNumOficioObsId() {
        return numOficioObsId;
    }

    public void setNumOficioObsId(AmaDocumento numOficioObsId) {
        this.numOficioObsId = numOficioObsId;
    }

    @XmlTransient
    public List<AmaResolucion> getAmaResolucionList() {
        return amaResolucionList;
    }

    public void setAmaResolucionList(List<AmaResolucion> amaResolucionList) {
        this.amaResolucionList = amaResolucionList;
    }

    public AmaResolucion getResolucionId() {
        return resolucionId;
    }

    public void setResolucionId(AmaResolucion resolucionId) {
        this.resolucionId = resolucionId;
    }

    public SbPersona getRepresentanteLegalId() {
        return representanteLegalId;
    }

    public void setRepresentanteLegalId(SbPersona representanteLegalId) {
        this.representanteLegalId = representanteLegalId;
    }

    public SbUsuario getUsuarioFirmaId() {
        return usuarioFirmaId;
    }

    public void setUsuarioFirmaId(SbUsuario usuarioFirmaId) {
        this.usuarioFirmaId = usuarioFirmaId;
    }

    public SbUsuario getUsuarioCreacionId() {
        return usuarioCreacionId;
    }

    public void setUsuarioCreacionId(SbUsuario usuarioCreacionId) {
        this.usuarioCreacionId = usuarioCreacionId;
    }

    public TipoBaseGt getTipoRegId() {
        return tipoRegId;
    }

    public void setTipoRegId(TipoBaseGt tipoRegId) {
        this.tipoRegId = tipoRegId;
    }

    public TipoBaseGt getTipoOpeId() {
        return tipoOpeId;
    }

    public void setTipoOpeId(TipoBaseGt tipoOpeId) {
        this.tipoOpeId = tipoOpeId;
    }

    public TipoBaseGt getAreaId() {
        return areaId;
    }

    public void setAreaId(TipoBaseGt areaId) {
        this.areaId = areaId;
    }

    public TipoGamac getEventoTrazaResId() {
        return eventoTrazaResId;
    }

    public void setEventoTrazaResId(TipoGamac eventoTrazaResId) {
        this.eventoTrazaResId = eventoTrazaResId;
    }

}
