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

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_CURSO_EVENTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspCursoEvento.findAll", query = "SELECT s FROM SspCursoEvento s"),
    @NamedQuery(name = "SspCursoEvento.findById", query = "SELECT s FROM SspCursoEvento s WHERE s.id = :id"),
    @NamedQuery(name = "SspCursoEvento.findByTipoEventoId", query = "SELECT s FROM SspCursoEvento s WHERE s.tipoEventoId = :tipoEventoId"),
    @NamedQuery(name = "SspCursoEvento.findByUserId", query = "SELECT s FROM SspCursoEvento s WHERE s.userId = :userId"),
    @NamedQuery(name = "SspCursoEvento.findByFecha", query = "SELECT s FROM SspCursoEvento s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspCursoEvento.findByObservacion", query = "SELECT s FROM SspCursoEvento s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SspCursoEvento.findByActivo", query = "SELECT s FROM SspCursoEvento s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspCursoEvento.findByAudLogin", query = "SELECT s FROM SspCursoEvento s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspCursoEvento.findByAudNumIp", query = "SELECT s FROM SspCursoEvento s WHERE s.audNumIp = :audNumIp")})
public class SspCursoEvento implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_CURSO_EVENTO")
    @SequenceGenerator(name = "SEQ_SSP_CURSO_EVENTO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_CURSO_EVENTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "TIPO_EVENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad tipoEventoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USER_ID")
    private Long userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 300)
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
    @JoinColumn(name = "REGISTRO_CURSO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistroCurso registroCursoId;

    public SspCursoEvento() {
    }

    public SspCursoEvento(Long id) {
        this.id = id;
    }

    public SspCursoEvento(Long id, Long userId, Date fecha, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.userId = userId;
        this.fecha = fecha;
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

    public TipoSeguridad getTipoEventoId() {
        return tipoEventoId;
    }

    public void setTipoEventoId(TipoSeguridad tipoEventoId) {
        this.tipoEventoId = tipoEventoId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public SspRegistroCurso getRegistroCursoId() {
        return registroCursoId;
    }

    public void setRegistroCursoId(SspRegistroCurso registroCursoId) {
        this.registroCursoId = registroCursoId;
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
        if (!(object instanceof SspCursoEvento)) {
            return false;
        }
        SspCursoEvento other = (SspCursoEvento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspCursoEvento[ id=" + id + " ]";
    }
    
}
