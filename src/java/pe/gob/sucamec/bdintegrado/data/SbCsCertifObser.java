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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_CS_CERTIF_OBSER", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbCsCertifObser.findAll", query = "SELECT s FROM SbCsCertifObser s"),
    @NamedQuery(name = "SbCsCertifObser.findById", query = "SELECT s FROM SbCsCertifObser s WHERE s.id = :id"),
    @NamedQuery(name = "SbCsCertifObser.findByFechaRegistro", query = "SELECT s FROM SbCsCertifObser s WHERE s.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "SbCsCertifObser.findByObservacion", query = "SELECT s FROM SbCsCertifObser s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SbCsCertifObser.findByExpediente", query = "SELECT s FROM SbCsCertifObser s WHERE s.expediente = :expediente"),
    @NamedQuery(name = "SbCsCertifObser.findByAdjunto", query = "SELECT s FROM SbCsCertifObser s WHERE s.adjunto = :adjunto"),
    @NamedQuery(name = "SbCsCertifObser.findByActivo", query = "SELECT s FROM SbCsCertifObser s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbCsCertifObser.findByAudLogin", query = "SELECT s FROM SbCsCertifObser s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbCsCertifObser.findByAudNumIp", query = "SELECT s FROM SbCsCertifObser s WHERE s.audNumIp = :audNumIp")})
public class SbCsCertifObser implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_CS_CERTIF_OBSER")
    @SequenceGenerator(name = "SEQ_SB_CS_CERTIF_OBSER", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_CS_CERTIF_OBSER", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Size(max = 20)
    @Column(name = "EXPEDIENTE")
    private String expediente;
    @Size(max = 50)
    @Column(name = "ADJUNTO")
    private String adjunto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private Long activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt estadoId;
    @JoinColumn(name = "MEDIO_COMUNICA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt medioComunicaId;
    @JoinColumn(name = "TIPO_OPERACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoOperacionId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioId;
    @JoinColumn(name = "CERTIFICADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbCsCertifsalud certificadoId;

    public SbCsCertifObser() {
    }

    public SbCsCertifObser(Long id) {
        this.id = id;
    }

    public SbCsCertifObser(Long id, Date fechaRegistro, String observacion, Long activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaRegistro = fechaRegistro;
        this.observacion = observacion;
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public Long getActivo() {
        return activo;
    }

    public void setActivo(Long activo) {
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

    public TipoBaseGt getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoBaseGt estadoId) {
        this.estadoId = estadoId;
    }

    public TipoBaseGt getMedioComunicaId() {
        return medioComunicaId;
    }

    public void setMedioComunicaId(TipoBaseGt medioComunicaId) {
        this.medioComunicaId = medioComunicaId;
    }

    public TipoBaseGt getTipoOperacionId() {
        return tipoOperacionId;
    }

    public void setTipoOperacionId(TipoBaseGt tipoOperacionId) {
        this.tipoOperacionId = tipoOperacionId;
    }

    public SbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public SbCsCertifsalud getCertificadoId() {
        return certificadoId;
    }

    public void setCertificadoId(SbCsCertifsalud certificadoId) {
        this.certificadoId = certificadoId;
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
        if (!(object instanceof SbCsCertifObser)) {
            return false;
        }
        SbCsCertifObser other = (SbCsCertifObser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbCsCertifObser[ id=" + id + " ]";
    }
    
}
