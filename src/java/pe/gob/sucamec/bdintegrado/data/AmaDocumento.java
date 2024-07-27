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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.sistemabase.data.SbTipo;


/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_DOCUMENTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaDocumento.findAll", query = "SELECT a FROM AmaDocumento a"),
    @NamedQuery(name = "AmaDocumento.findById", query = "SELECT a FROM AmaDocumento a WHERE a.id = :id"),
    @NamedQuery(name = "AmaDocumento.findByNumero", query = "SELECT a FROM AmaDocumento a WHERE a.numero = :numero"),
    @NamedQuery(name = "AmaDocumento.findByNroExpediente", query = "SELECT a FROM AmaDocumento a WHERE a.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "AmaDocumento.findByAsunto", query = "SELECT a FROM AmaDocumento a WHERE a.asunto = :asunto"),
    @NamedQuery(name = "AmaDocumento.findByFechaCreacion", query = "SELECT a FROM AmaDocumento a WHERE a.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "AmaDocumento.findByFechaFirma", query = "SELECT a FROM AmaDocumento a WHERE a.fechaFirma = :fechaFirma"),
    @NamedQuery(name = "AmaDocumento.findByActivo", query = "SELECT a FROM AmaDocumento a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaDocumento.findByAudLogin", query = "SELECT a FROM AmaDocumento a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaDocumento.findByAudNumIp", query = "SELECT a FROM AmaDocumento a WHERE a.audNumIp = :audNumIp")})
public class AmaDocumento implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_DOCUMENTO")
    @SequenceGenerator(name = "SEQ_AMA_DOCUMENTO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_DOCUMENTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 100)
    @Column(name = "NUMERO")
    private String numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "ASUNTO")
    private String asunto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "FECHA_FIRMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFirma;
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
    @JoinTable(schema="BDINTEGRADO", name = "AMA_DOCUMENTO_OBS", joinColumns = {
        @JoinColumn(name = "DOCUMENTO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "OBSERVACION_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<AmaObservacionDesc> amaObservacionDescList;
    @ManyToMany(mappedBy = "amaDocumentoList")
    private List<AmaRegEmpArma> amaRegEmpArmaList;
    @JoinColumn(name = "EVENTO_TRAZA_DOC_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac eventoTrazaDocId;
    @JoinColumn(name = "TIPO_DOCUMENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoDocumentoId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo areaId;
    @JoinColumn(name = "USUARIO_FIRMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuario usuarioFirmaId;
    @JoinColumn(name = "USUARIO_CREACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioCreacionId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaId;
    @JoinColumn(name = "REPRESENTANTE_LEGAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona representanteLegalId;
    @JoinColumn(name = "DIRECCION_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbDireccion direccionId;
    @OneToMany(mappedBy = "documentoId")
    private List<AmaObservaTraza> amaObservaTrazaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numOficioObsId")
    private List<AmaResolucion> amaResolucionList;

    public AmaDocumento() {
    }

    public AmaDocumento(Long id) {
        this.id = id;
    }

    public AmaDocumento(Long id, String nroExpediente, String asunto, Date fechaCreacion, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroExpediente = nroExpediente;
        this.asunto = asunto;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Date fechaFirma) {
        this.fechaFirma = fechaFirma;
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
    public List<AmaObservacionDesc> getAmaObservacionDescList() {
        return amaObservacionDescList;
    }

    public void setAmaObservacionDescList(List<AmaObservacionDesc> amaObservacionDescList) {
        this.amaObservacionDescList = amaObservacionDescList;
    }

    @XmlTransient
    public List<AmaRegEmpArma> getAmaRegEmpArmaList() {
        return amaRegEmpArmaList;
    }

    public void setAmaRegEmpArmaList(List<AmaRegEmpArma> amaRegEmpArmaList) {
        this.amaRegEmpArmaList = amaRegEmpArmaList;
    }

    public TipoGamac getEventoTrazaDocId() {
        return eventoTrazaDocId;
    }

    public void setEventoTrazaDocId(TipoGamac eventoTrazaDocId) {
        this.eventoTrazaDocId = eventoTrazaDocId;
    }

    public TipoGamac getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public void setTipoDocumentoId(TipoGamac tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId;
    }

    public SbTipo getAreaId() {
        return areaId;
    }

    public void setAreaId(SbTipo areaId) {
        this.areaId = areaId;
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

    public SbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersona personaId) {
        this.personaId = personaId;
    }

    public SbPersona getRepresentanteLegalId() {
        return representanteLegalId;
    }

    public void setRepresentanteLegalId(SbPersona representanteLegalId) {
        this.representanteLegalId = representanteLegalId;
    }

    public SbDireccion getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(SbDireccion direccionId) {
        this.direccionId = direccionId;
    }

    @XmlTransient
    public List<AmaObservaTraza> getAmaObservaTrazaList() {
        return amaObservaTrazaList;
    }

    public void setAmaObservaTrazaList(List<AmaObservaTraza> amaObservaTrazaList) {
        this.amaObservaTrazaList = amaObservaTrazaList;
    }

    @XmlTransient
    public List<AmaResolucion> getAmaResolucionList() {
        return amaResolucionList;
    }

    public void setAmaResolucionList(List<AmaResolucion> amaResolucionList) {
        this.amaResolucionList = amaResolucionList;
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
        if (!(object instanceof AmaDocumento)) {
            return false;
        }
        AmaDocumento other = (AmaDocumento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaDocumento[ id=" + id + " ]";
    }
    
}
