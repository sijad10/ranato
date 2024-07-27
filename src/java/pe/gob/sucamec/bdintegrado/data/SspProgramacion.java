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
@Table(name = "SSP_PROGRAMACION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspProgramacion.findAll", query = "SELECT s FROM SspProgramacion s"),
    @NamedQuery(name = "SspProgramacion.findById", query = "SELECT s FROM SspProgramacion s WHERE s.id = :id"),
    @NamedQuery(name = "SspProgramacion.findByFechaHoraIncio", query = "SELECT s FROM SspProgramacion s WHERE s.fechaHoraIncio = :fechaHoraIncio"),
    @NamedQuery(name = "SspProgramacion.findByFechaHoraFin", query = "SELECT s FROM SspProgramacion s WHERE s.fechaHoraFin = :fechaHoraFin"),
    @NamedQuery(name = "SspProgramacion.findByActivo", query = "SELECT s FROM SspProgramacion s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspProgramacion.findByAudLogin", query = "SELECT s FROM SspProgramacion s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspProgramacion.findByAudNumIp", query = "SELECT s FROM SspProgramacion s WHERE s.audNumIp = :audNumIp")})
public class SspProgramacion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_PROGRAMACION")
    @SequenceGenerator(name = "SEQ_SSP_PROGRAMACION", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_PROGRAMACION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programacionId")
    private List<SspPrograHora> sspPrograHoraList;
    @JoinColumn(name = "REGISTRO_CURSO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistroCurso registroCursoId;
    @JoinColumn(name = "MODULO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspModulo moduloId;
    @JoinColumn(name = "LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspLocal localId;
    @JoinColumn(name = "INSTRUCTOR_ID", referencedColumnName = "ID")
    @ManyToOne
    private SspInstructor instructorId;
    @Column(name = "ESTADO_VERIFICA")
    private Short estadoVerifica;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programacionId")
    private List<SspPrograHistorial> sspPrograHistorialList;
    @Size(max = 200)
    @Column(name = "ARCHIVO_ASISTENCIA")
    private String archivoAsistencia;
    @Size(max = 200)
    @Column(name = "ARCHIVO_NOTAS")
    private String archivoNotas;
    

    public SspProgramacion() {
    }

    public SspProgramacion(Long id) {
        this.id = id;
    }

    public SspProgramacion(Long id, short activo, String audLogin, String audNumIp) {
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

    @XmlTransient
    public List<SspPrograHora> getSspPrograHoraList() {
        return sspPrograHoraList;
    }

    public void setSspPrograHoraList(List<SspPrograHora> sspPrograHoraList) {
        this.sspPrograHoraList = sspPrograHoraList;
    }

    public SspRegistroCurso getRegistroCursoId() {
        return registroCursoId;
    }

    public void setRegistroCursoId(SspRegistroCurso registroCursoId) {
        this.registroCursoId = registroCursoId;
    }

    public SspModulo getModuloId() {
        return moduloId;
    }

    public void setModuloId(SspModulo moduloId) {
        this.moduloId = moduloId;
    }

    public SspLocal getLocalId() {
        return localId;
    }

    public void setLocalId(SspLocal localId) {
        this.localId = localId;
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
        if (!(object instanceof SspProgramacion)) {
            return false;
        }
        SspProgramacion other = (SspProgramacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspProgramacion[ id=" + id + " ]";
    }
    
    public SspInstructor getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(SspInstructor instructorId) {
        this.instructorId = instructorId;
    }
    
    public Short getEstadoVerifica() {
        return estadoVerifica;
    }

    public void setEstadoVerifica(Short estadoVerifica) {
        this.estadoVerifica = estadoVerifica;
    }
    
    @XmlTransient
    public List<SspPrograHistorial> getSspPrograHistorialList() {
        return sspPrograHistorialList;
    }

    public void setSspPrograHistorialList(List<SspPrograHistorial> sspPrograHistorialList) {
        this.sspPrograHistorialList = sspPrograHistorialList;
    }
    
    public String getArchivoAsistencia() {
        return archivoAsistencia;
    }

    public void setArchivoAsistencia(String archivoAsistencia) {
        this.archivoAsistencia = archivoAsistencia;
    }
    
    public String getArchivoNotas() {
        return archivoNotas;
    }

    public void setArchivoNotas(String archivoNotas) {
        this.archivoNotas = archivoNotas;
    }   
}
