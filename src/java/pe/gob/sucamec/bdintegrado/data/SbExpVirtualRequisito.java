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

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_EXP_VIRTUAL_REQUISITO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbExpVirtualRequisito.findAll", query = "SELECT s FROM SbExpVirtualRequisito s"),
    @NamedQuery(name = "SbExpVirtualRequisito.findById", query = "SELECT s FROM SbExpVirtualRequisito s WHERE s.id = :id"),
    @NamedQuery(name = "SbExpVirtualRequisito.findByFechaRegistro", query = "SELECT s FROM SbExpVirtualRequisito s WHERE s.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "SbExpVirtualRequisito.findByRespuestaRequisito", query = "SELECT s FROM SbExpVirtualRequisito s WHERE s.respuestaRequisito = :respuestaRequisito"),
    @NamedQuery(name = "SbExpVirtualRequisito.findByObservacionActual", query = "SELECT s FROM SbExpVirtualRequisito s WHERE s.observacionActual = :observacionActual"),
    @NamedQuery(name = "SbExpVirtualRequisito.findByActivo", query = "SELECT s FROM SbExpVirtualRequisito s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbExpVirtualRequisito.findByAudLogin", query = "SELECT s FROM SbExpVirtualRequisito s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbExpVirtualRequisito.findByAudNumIp", query = "SELECT s FROM SbExpVirtualRequisito s WHERE s.audNumIp = :audNumIp")})
public class SbExpVirtualRequisito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_EXP_VIRTUAL_REQUISITO")
    @SequenceGenerator(name = "SEQ_SB_EXP_VIRTUAL_REQUISITO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_EXP_VIRTUAL_REQUISITO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Size(max = 500)
    @Column(name = "RESPUESTA_REQUISITO")
    private String respuestaRequisito;
    @Size(max = 300)
    @Column(name = "OBSERVACION_ACTUAL")
    private String observacionActual;
    @Size(max = 300)
    @Column(name = "OBSERVACION_ANTERIOR")
    private String observacionAnterior;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_VERSION")
    private short nroVersion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTUAL")
    private short actual;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expVirtualRequisitoId")
    private List<SbExpVirtualAdjunto> sbExpVirtualAdjuntoList;
    @JoinColumn(name = "TUPA_REQUISITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTupaRequisito tupaRequisitoId;
    @JoinColumn(name = "EXP_VIRTUAL_SOLICITUD_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbExpVirtualSolicitud expVirtualSolicitudId;
    @JoinColumn(name = "USUARIO_RESPUESTA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbExpVirtualSolicitud usuarioRespuestaId;
 
    public SbExpVirtualRequisito() {
    }

    public SbExpVirtualRequisito(Long id) {
        this.id = id;
    }

    public SbExpVirtualRequisito(Long id, Date fechaRegistro, short activo, String audLogin, String audNumIp) {
        this.id = id;
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

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getRespuestaRequisito() {
        return respuestaRequisito;
    }

    public void setRespuestaRequisito(String respuestaRequisito) {
        this.respuestaRequisito = respuestaRequisito;
    }

    public String getObservacionActual() {
        return observacionActual;
    }

    public void setObservacionActual(String observacionActual) {
        this.observacionActual = observacionActual;
    }

    public String getObservacionAnterior() {
        return observacionAnterior;
    }

    public void setObservacionAnterior(String observacionAnterior) {
        this.observacionAnterior = observacionAnterior;
    }

    public short getNroVersion() {
        return nroVersion;
    }

    public void setNroVersion(short nroVersion) {
        this.nroVersion = nroVersion;
    }

    public short getActual() {
        return actual;
    }

    public void setActual(short actual) {
        this.actual = actual;
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
    public List<SbExpVirtualAdjunto> getSbExpVirtualAdjuntoList() {
        return sbExpVirtualAdjuntoList;
    }

    public void setSbExpVirtualAdjuntoList(List<SbExpVirtualAdjunto> sbExpVirtualAdjuntoList) {
        this.sbExpVirtualAdjuntoList = sbExpVirtualAdjuntoList;
    }

    public SbTupaRequisito getTupaRequisitoId() {
        return tupaRequisitoId;
    }

    public void setTupaRequisitoId(SbTupaRequisito tupaRequisitoId) {
        this.tupaRequisitoId = tupaRequisitoId;
    }

    public SbExpVirtualSolicitud getExpVirtualSolicitudId() {
        return expVirtualSolicitudId;
    }

    public void setExpVirtualSolicitudId(SbExpVirtualSolicitud expVirtualSolicitudId) {
        this.expVirtualSolicitudId = expVirtualSolicitudId;
    }

    public SbExpVirtualSolicitud getUsuarioRespuestaId() {
        return usuarioRespuestaId;
    }

    public void setUsuarioRespuestaId(SbExpVirtualSolicitud usuarioRespuestaId) {
        this.usuarioRespuestaId = usuarioRespuestaId;
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
        if (!(object instanceof SbExpVirtualRequisito)) {
            return false;
        }
        SbExpVirtualRequisito other = (SbExpVirtualRequisito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.SbExpVirtualRequisito[ id=" + id + " ]";
    }
    
}
