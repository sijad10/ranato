/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.encuesta.data;

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
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_RESPUESTA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbRespuesta.findAll", query = "SELECT s FROM SbRespuesta s"),
    @NamedQuery(name = "SbRespuesta.findById", query = "SELECT s FROM SbRespuesta s WHERE s.id = :id"),
    @NamedQuery(name = "SbRespuesta.findByRespuesta", query = "SELECT s FROM SbRespuesta s WHERE s.respuesta = :respuesta"),
    @NamedQuery(name = "SbRespuesta.findByFechaRegistro", query = "SELECT s FROM SbRespuesta s WHERE s.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "SbRespuesta.findByActivo", query = "SELECT s FROM SbRespuesta s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbRespuesta.findByAudLogin", query = "SELECT s FROM SbRespuesta s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbRespuesta.findByAudNumIp", query = "SELECT s FROM SbRespuesta s WHERE s.audNumIp = :audNumIp")})
public class SbRespuesta implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_RESPUESTA")
    @SequenceGenerator(name = "SEQ_SB_RESPUESTA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_RESPUESTA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 400)
    @Column(name = "RESPUESTA")
    private String respuesta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
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
    @JoinColumn(name = "PREGUNTA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPregunta preguntaId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioId;
    @JoinColumn(name = "ALTERNATIVA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbAlternativa alternativaId;

    public SbRespuesta() {
    }

    public SbRespuesta(Long id) {
        this.id = id;
    }

    public SbRespuesta(Long id, Date fechaRegistro, short activo, String audLogin, String audNumIp) {
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

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

    public SbPregunta getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(SbPregunta preguntaId) {
        this.preguntaId = preguntaId;
    }

    public SbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public SbAlternativa getAlternativaId() {
        return alternativaId;
    }

    public void setAlternativaId(SbAlternativa alternativaId) {
        this.alternativaId = alternativaId;
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
        if (!(object instanceof SbRespuesta)) {
            return false;
        }
        SbRespuesta other = (SbRespuesta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.encuesta.data.SbRespuesta[ id=" + id + " ]";
    }
}
