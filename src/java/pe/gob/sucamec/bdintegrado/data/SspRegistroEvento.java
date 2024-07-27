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
@Table(name = "SSP_REGISTRO_EVENTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspRegistroEvento.findAll", query = "SELECT s FROM SspRegistroEvento s"),
    @NamedQuery(name = "SspRegistroEvento.findById", query = "SELECT s FROM SspRegistroEvento s WHERE s.id = :id"),
    @NamedQuery(name = "SspRegistroEvento.findByFecha", query = "SELECT s FROM SspRegistroEvento s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspRegistroEvento.findByObservacion", query = "SELECT s FROM SspRegistroEvento s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SspRegistroEvento.findByActivo", query = "SELECT s FROM SspRegistroEvento s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspRegistroEvento.findByAudLogin", query = "SELECT s FROM SspRegistroEvento s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspRegistroEvento.findByAudNumIp", query = "SELECT s FROM SspRegistroEvento s WHERE s.audNumIp = :audNumIp")})
public class SspRegistroEvento implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_REGISTRO_EVENTO")
    @SequenceGenerator(name = "SEQ_SSP_REGISTRO_EVENTO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_REGISTRO_EVENTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 1000)
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
    @JoinColumn(name = "TIPO_EVENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad tipoEventoId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt userId;

    public SspRegistroEvento() {
    }

    public SspRegistroEvento(Long id) {
        this.id = id;
    }

    public SspRegistroEvento(Long id, Date fecha, short activo, String audLogin, String audNumIp) {
        this.id = id;
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

    public TipoSeguridad getTipoEventoId() {
        return tipoEventoId;
    }

    public void setTipoEventoId(TipoSeguridad tipoEventoId) {
        this.tipoEventoId = tipoEventoId;
    }

    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
    }

    public SbUsuarioGt getUserId() {
        return userId;
    }

    public void setUserId(SbUsuarioGt userId) {
        this.userId = userId;
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
        if (!(object instanceof SspRegistroEvento)) {
            return false;
        }
        SspRegistroEvento other = (SspRegistroEvento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspRegistroEvento[ id=" + id + " ]";
    }
    
}
