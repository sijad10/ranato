/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.data;

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
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_ACTA_DEFUNCION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbActaDefuncion.findAll", query = "SELECT s FROM SbActaDefuncion s"),
    @NamedQuery(name = "SbActaDefuncion.findById", query = "SELECT s FROM SbActaDefuncion s WHERE s.id = :id"),
    @NamedQuery(name = "SbActaDefuncion.findByFechaFallecimiento", query = "SELECT s FROM SbActaDefuncion s WHERE s.fechaFallecimiento = :fechaFallecimiento"),
    @NamedQuery(name = "SbActaDefuncion.findByActivo", query = "SELECT s FROM SbActaDefuncion s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbActaDefuncion.findByAudLogin", query = "SELECT s FROM SbActaDefuncion s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbActaDefuncion.findByAudNumIp", query = "SELECT s FROM SbActaDefuncion s WHERE s.audNumIp = :audNumIp")})
public class SbActaDefuncion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_ACTA_DEFUNCION")
    @SequenceGenerator(name = "SEQ_SB_ACTA_DEFUNCION", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_ACTA_DEFUNCION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_FALLECIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFallecimiento;
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Size(max = 50)
    @Column(name = "FOTO_ACTA")
    private String fotoActa;
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
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuario usuarioId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaId;

    public SbActaDefuncion() {
    }

    public SbActaDefuncion(Long id) {
        this.id = id;
    }

    public SbActaDefuncion(Long id, Date fechaFallecimiento, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaFallecimiento = fechaFallecimiento;
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

    public Date getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(Date fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFotoActa() {
        return fotoActa;
    }

    public void setFotoActa(String fotoActa) {
        this.fotoActa = fotoActa;
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

    public SbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuario usuarioId) {
        this.usuarioId = usuarioId;
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
        if (!(object instanceof SbActaDefuncion)) {
            return false;
        }
        SbActaDefuncion other = (SbActaDefuncion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemagamac.data.SbActaDefuncion[ id=" + id + " ]";
    }

}
