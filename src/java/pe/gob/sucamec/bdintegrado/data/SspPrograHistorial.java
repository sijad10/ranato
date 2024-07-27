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
@Table(name = "SSP_PROGRA_HISTORIAL", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspPrograHistorial.findAll", query = "SELECT s FROM SspPrograHistorial s"),
    @NamedQuery(name = "SspPrograHistorial.findById", query = "SELECT s FROM SspPrograHistorial s WHERE s.id = :id"),
    @NamedQuery(name = "SspPrograHistorial.findByInstructorId", query = "SELECT s FROM SspPrograHistorial s WHERE s.instructorId = :instructorId"),
    @NamedQuery(name = "SspPrograHistorial.findByLocalId", query = "SELECT s FROM SspPrograHistorial s WHERE s.localId = :localId"),
    @NamedQuery(name = "SspPrograHistorial.findByFecha", query = "SELECT s FROM SspPrograHistorial s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspPrograHistorial.findByMotivo", query = "SELECT s FROM SspPrograHistorial s WHERE s.motivo = :motivo"),
    @NamedQuery(name = "SspPrograHistorial.findByFechaHoraIncio", query = "SELECT s FROM SspPrograHistorial s WHERE s.fechaHoraIncio = :fechaHoraIncio"),
    @NamedQuery(name = "SspPrograHistorial.findByFechaHoraFin", query = "SELECT s FROM SspPrograHistorial s WHERE s.fechaHoraFin = :fechaHoraFin"),
    @NamedQuery(name = "SspPrograHistorial.findByActivo", query = "SELECT s FROM SspPrograHistorial s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspPrograHistorial.findByAudLogin", query = "SELECT s FROM SspPrograHistorial s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspPrograHistorial.findByAudNumIp", query = "SELECT s FROM SspPrograHistorial s WHERE s.audNumIp = :audNumIp")})
public class SspPrograHistorial implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_PROGRA_HISTORIAL")
    @SequenceGenerator(name = "SEQ_SSP_PROGRA_HISTORIAL", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_PROGRA_HISTORIAL", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private SspLocal localId;
    @JoinColumn(name = "INSTRUCTOR_ID", referencedColumnName = "ID")
    @ManyToOne
    private SspInstructor instructorId;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 400)
    @Column(name = "MOTIVO")
    private String motivo;
    @Column(name = "FECHA_HORA_INCIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraIncio;
    @Column(name = "FECHA_HORA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraFin;
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
    @JoinColumn(name = "PROGRAMACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspProgramacion programacionId;
    @OneToMany(mappedBy = "historialId")
    private List<SspPrograHistorial> sspPrograHistorialList;
    @JoinColumn(name = "HISTORIAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private SspPrograHistorial historialId;

    public SspPrograHistorial() {
    }

    public SspPrograHistorial(Long id) {
        this.id = id;
    }

    public SspPrograHistorial(Long id, short activo, String audLogin, String audNumIp) {
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

    public SspLocal getLocalId() {
        return localId;
    }

    public void setLocalId(SspLocal localId) {
        this.localId = localId;
    }

    public SspInstructor getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(SspInstructor instructorId) {
        this.instructorId = instructorId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getFechaHoraIncio() {
        return fechaHoraIncio;
    }

    public void setFechaHoraIncio(Date fechaHoraIncio) {
        this.fechaHoraIncio = fechaHoraIncio;
    }

    public Date getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(Date fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
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

    public SspProgramacion getProgramacionId() {
        return programacionId;
    }

    public void setProgramacionId(SspProgramacion programacionId) {
        this.programacionId = programacionId;
    }

    @XmlTransient
    public List<SspPrograHistorial> getSspPrograHistorialList() {
        return sspPrograHistorialList;
    }

    public void setSspPrograHistorialList(List<SspPrograHistorial> sspPrograHistorialList) {
        this.sspPrograHistorialList = sspPrograHistorialList;
    }

    public SspPrograHistorial getHistorialId() {
        return historialId;
    }

    public void setHistorialId(SspPrograHistorial historialId) {
        this.historialId = historialId;
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
        if (!(object instanceof SspPrograHistorial)) {
            return false;
        }
        SspPrograHistorial other = (SspPrograHistorial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspPrograHistorial[ id=" + id + " ]";
    }
    
}
