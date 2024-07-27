/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.sistemabase.data.SbDistrito;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_EXP_VIRTUAL_SOLICITUD", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbExpVirtualSolicitud.findAll", query = "SELECT s FROM SbExpVirtualSolicitud s"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findById", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.id = :id"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByDireccion", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.direccion = :direccion"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByCorreo", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.correo = :correo"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByTelefonoContacto", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.telefonoContacto = :telefonoContacto"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByDocumentoNuevoRepLegal", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.documentoNuevoRepLegal = :documentoNuevoRepLegal"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByNotificacionViaSel", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.notificacionViaSel = :notificacionViaSel"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByNotificacionAlertaCelular", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.notificacionAlertaCelular = :notificacionAlertaCelular"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByNumeroSolicitud", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.numeroSolicitud = :numeroSolicitud"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByFechaRegistro", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByNroExpediente", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByFechaExpediente", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.fechaExpediente = :fechaExpediente"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByObservacion", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByActivo", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByAudLogin", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByAudNumIp", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.audNumIp = :audNumIp"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByFechaSolicitud", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.fechaSolicitud = :fechaSolicitud"),
    @NamedQuery(name = "SbExpVirtualSolicitud.findByDocumentoRefPersona", query = "SELECT s FROM SbExpVirtualSolicitud s WHERE s.documentoRefPersona = :documentoRefPersona")})
public class SbExpVirtualSolicitud implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_EXP_VIRTUAL_SOLICITUD")
    @SequenceGenerator(name = "SEQ_SB_EXP_VIRTUAL_SOLICITUD", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_EXP_VIRTUAL_SOLICITUD", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 350)
    @Column(name = "DIRECCION")
    private String direccion;
    @Size(min = 1, max = 350)
    @Column(name = "CORREO")
    private String correo;    
    @Size(max = 40)
    @Column(name = "TELEFONO_CONTACTO")
    private String telefonoContacto;
    @Size(max = 25)
    @Column(name = "DOCUMENTO_NUEVO_REP_LEGAL")
    private String documentoNuevoRepLegal;
    @Size(max = 25)
    @Column(name = "DOCUMENTO_REF_PERSONA")
    private String documentoRefPersona;
    @Size(max = 200)
    @Column(name = "APE_PAT_REF_PERSONA")
    private String apePatRefPersona;
    @Size(max = 200)
    @Column(name = "APE_MAT_REF_PERSONA")
    private String apeMatRefPersona;
    @Size(max = 200)
    @Column(name = "NOMBRES_REF_PERSONA")
    private String nombresRefPersona;    
    @Basic(optional = false)
    @NotNull
    @Column(name = "NOTIFICACION_VIA_SEL")
    private short notificacionViaSel;
    @Column(name = "NOTIFICACION_ALERTA_CELULAR")
    private Short notificacionAlertaCelular;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NUMERO_SOLICITUD")
    private String numeroSolicitud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "FECHA_SOLICITUD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSolicitud;
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Column(name = "FECHA_EXPEDIENTE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpediente;
    @Size(max = 300)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "TIPO_DOC_NUEVO_REP_LEGAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoDocNuevoRepLegalId;
    @JoinColumn(name = "TIPO_MEDIO_CONTACTO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoMedioContactoId;
    @JoinColumn(name = "SEDE_SUCAMEC_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt sedeSucamecId;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt estadoId;
    @JoinColumn(name = "USUARIO_CREACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioCreacionId;
    @JoinColumn(name = "USUARIO_RECEPCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioRecepcionId;
    @JoinColumn(name = "PROCEDIMIENTO_TUPA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTupa procedimientoTupaId;
    @JoinColumn(name = "COMPROBANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbRecibos comprobanteId;
    @JoinColumn(name = "PROCESO_TUPA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbProcesoTupa procesoTupaId;
    @JoinColumn(name = "ADMINISTRADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona administradoId;
    @JoinColumn(name = "REPRESENTANTE_LEGAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona representanteLegalId;
    @JoinColumn(name = "UBIGEO_DIRECCION_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbDistrito ubigeoDireccionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expVirtualSolicitudId")
    private List<SbExpVirtualRequisito> sbExpVirtualRequisitoList;
    @JoinColumn(name = "TIPO_DOC_REF_PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoDocRefPersonaId;

    public SbExpVirtualSolicitud() {
    }

    public SbExpVirtualSolicitud(Long id) {
        this.id = id;
    }

    public SbExpVirtualSolicitud(Long id, String direccion, short notificacionViaSel, String numeroSolicitud, Date fechaRegistro, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.direccion = direccion;
        this.correo = correo;
        this.notificacionViaSel = notificacionViaSel;
        this.numeroSolicitud = numeroSolicitud;
        this.fechaRegistro = fechaRegistro;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getDocumentoNuevoRepLegal() {
        return documentoNuevoRepLegal;
    }

    public void setDocumentoNuevoRepLegal(String documentoNuevoRepLegal) {
        this.documentoNuevoRepLegal = documentoNuevoRepLegal;
    }

    public short getNotificacionViaSel() {
        return notificacionViaSel;
    }

    public void setNotificacionViaSel(short notificacionViaSel) {
        this.notificacionViaSel = notificacionViaSel;
    }

    public Short getNotificacionAlertaCelular() {
        return notificacionAlertaCelular;
    }

    public void setNotificacionAlertaCelular(Short notificacionAlertaCelular) {
        this.notificacionAlertaCelular = notificacionAlertaCelular;
    }

    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public Date getFechaExpediente() {
        return fechaExpediente;
    }

    public void setFechaExpediente(Date fechaExpediente) {
        this.fechaExpediente = fechaExpediente;
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

    public TipoBaseGt getTipoDocNuevoRepLegalId() {
        return tipoDocNuevoRepLegalId;
    }

    public void setTipoDocNuevoRepLegalId(TipoBaseGt tipoDocNuevoRepLegalId) {
        this.tipoDocNuevoRepLegalId = tipoDocNuevoRepLegalId;
    }

    public TipoBaseGt getTipoMedioContactoId() {
        return tipoMedioContactoId;
    }

    public void setTipoMedioContactoId(TipoBaseGt tipoMedioContactoId) {
        this.tipoMedioContactoId = tipoMedioContactoId;
    }

    public TipoBaseGt getSedeSucamecId() {
        return sedeSucamecId;
    }

    public void setSedeSucamecId(TipoBaseGt sedeSucamecId) {
        this.sedeSucamecId = sedeSucamecId;
    }

    public TipoBaseGt getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoBaseGt estadoId) {
        this.estadoId = estadoId;
    }

    public SbUsuario getUsuarioCreacionId() {
        return usuarioCreacionId;
    }

    public void setUsuarioCreacionId(SbUsuario usuarioCreacionId) {
        this.usuarioCreacionId = usuarioCreacionId;
    }

    public SbUsuario getUsuarioRecepcionId() {
        return usuarioRecepcionId;
    }

    public void setUsuarioRecepcionId(SbUsuario usuarioRecepcionId) {
        this.usuarioRecepcionId = usuarioRecepcionId;
    }

    public SbTupa getProcedimientoTupaId() {
        return procedimientoTupaId;
    }

    public void setProcedimientoTupaId(SbTupa procedimientoTupaId) {
        this.procedimientoTupaId = procedimientoTupaId;
    }

    public SbRecibos getComprobanteId() {
        return comprobanteId;
    }

    public void setComprobanteId(SbRecibos comprobanteId) {
        this.comprobanteId = comprobanteId;
    }

    public SbProcesoTupa getProcesoTupaId() {
        return procesoTupaId;
    }

    public void setProcesoTupaId(SbProcesoTupa procesoTupaId) {
        this.procesoTupaId = procesoTupaId;
    }

    public SbPersona getAdministradoId() {
        return administradoId;
    }

    public void setAdministradoId(SbPersona administradoId) {
        this.administradoId = administradoId;
    }

    public SbPersona getRepresentanteLegalId() {
        return representanteLegalId;
    }

    public void setRepresentanteLegalId(SbPersona representanteLegalId) {
        this.representanteLegalId = representanteLegalId;
    }

    public SbDistrito getUbigeoDireccionId() {
        return ubigeoDireccionId;
    }

    public void setUbigeoDireccionId(SbDistrito ubigeoDireccionId) {
        this.ubigeoDireccionId = ubigeoDireccionId;
    }
    
    public String getDocumentoRefPersona() {
        return documentoRefPersona;
    }

    public void setDocumentoRefPersona(String documentoRefPersona) {
        this.documentoRefPersona = documentoRefPersona;
    }

    public String getApePatRefPersona() {
        return apePatRefPersona;
    }

    public void setApePatRefPersona(String apePatRefPersona) {
        this.apePatRefPersona = apePatRefPersona;
    }

    public String getApeMatRefPersona() {
        return apeMatRefPersona;
    }

    public void setApeMatRefPersona(String apeMatRefPersona) {
        this.apeMatRefPersona = apeMatRefPersona;
    }

    public String getNombresRefPersona() {
        return nombresRefPersona;
    }

    public void setNombresRefPersona(String nombresRefPersona) {
        this.nombresRefPersona = nombresRefPersona;
    }
    
    public TipoBaseGt getTipoDocRefPersonaId() {
        return tipoDocRefPersonaId;
    }

    public void setTipoDocRefPersonaId(TipoBaseGt tipoDocRefPersonaId) {
        this.tipoDocRefPersonaId = tipoDocRefPersonaId;
    }

    @XmlTransient
    public List<SbExpVirtualRequisito> getSbExpVirtualRequisitoList() {
        return sbExpVirtualRequisitoList;
    }

    public void setSbExpVirtualRequisitoList(List<SbExpVirtualRequisito> sbExpVirtualRequisitoList) {
        this.sbExpVirtualRequisitoList = sbExpVirtualRequisitoList;
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
        if (!(object instanceof SbExpVirtualSolicitud)) {
            return false;
        }
        SbExpVirtualSolicitud other = (SbExpVirtualSolicitud) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.SbExpVirtualSolicitud[ id=" + id + " ]";
    }
    
}