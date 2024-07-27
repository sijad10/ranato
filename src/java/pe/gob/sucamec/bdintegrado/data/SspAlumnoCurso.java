/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
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
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_ALUMNO_CURSO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspAlumnoCurso.findAll", query = "SELECT s FROM SspAlumnoCurso s"),
    @NamedQuery(name = "SspAlumnoCurso.findById", query = "SELECT s FROM SspAlumnoCurso s WHERE s.id = :id"),
    @NamedQuery(name = "SspAlumnoCurso.findByNotaFinal", query = "SELECT s FROM SspAlumnoCurso s WHERE s.notaFinal = :notaFinal"),
    @NamedQuery(name = "SspAlumnoCurso.findByEstadoFinal", query = "SELECT s FROM SspAlumnoCurso s WHERE s.estadoFinal = :estadoFinal"),
    @NamedQuery(name = "SspAlumnoCurso.findByAsistencia", query = "SELECT s FROM SspAlumnoCurso s WHERE s.asistencia = :asistencia"),
    @NamedQuery(name = "SspAlumnoCurso.findByActivo", query = "SELECT s FROM SspAlumnoCurso s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspAlumnoCurso.findByAudLogin", query = "SELECT s FROM SspAlumnoCurso s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspAlumnoCurso.findByAudNumIp", query = "SELECT s FROM SspAlumnoCurso s WHERE s.audNumIp = :audNumIp")})
public class SspAlumnoCurso implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_ALUMNO_CURSO")
    @SequenceGenerator(name = "SEQ_SSP_ALUMNO_CURSO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_ALUMNO_CURSO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;    
    @Column(name = "NOTA_FINAL")
    private Double notaFinal;
    @Column(name = "ESTADO_FINAL")
    private Short estadoFinal;
    @Column(name = "ASISTENCIA")
    private Short asistencia;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alumnoId")
    private List<SspNotas> sspNotasList;
    @JoinColumn(name = "REGISTRO_CURSO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistroCurso registroCursoId;
    @JoinColumn(name = "FOTO_ID", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private SspPersonaFoto fotoId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    @Size(max = 100)
    @Column(name = "OFICIO_MSIAP")
    private String oficioMsiap;
    @Column(name = "EVALUACION_PJ")
    private Short evaluacionPj;
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "FECHA_REG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReg;
    @JoinColumn(name = "TIPO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoOpeId;

    public SspAlumnoCurso() {
    }

    public SspAlumnoCurso(Long id) {
        this.id = id;
    }

    public SspAlumnoCurso(Long id, short activo, String audLogin, String audNumIp) {
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

    public Double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(Double notaFinal) {
        this.notaFinal = notaFinal;
    }

    public Short getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(Short estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public Short getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Short asistencia) {
        this.asistencia = asistencia;
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
    public List<SspNotas> getSspNotasList() {
        return sspNotasList;
    }

    public void setSspNotasList(List<SspNotas> sspNotasList) {
        this.sspNotasList = sspNotasList;
    }

    public SspRegistroCurso getRegistroCursoId() {
        return registroCursoId;
    }

    public void setRegistroCursoId(SspRegistroCurso registroCursoId) {
        this.registroCursoId = registroCursoId;
    }

    public SspPersonaFoto getFotoId() {
        return fotoId;
    }

    public void setFotoId(SspPersonaFoto fotoId) {
        this.fotoId = fotoId;
    }

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
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
        if (!(object instanceof SspAlumnoCurso)) {
            return false;
        }
        SspAlumnoCurso other = (SspAlumnoCurso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspAlumnoCurso[ id=" + id + " ]";
    }

    public String getOficioMsiap() {
        return oficioMsiap;
    }

    public void setOficioMsiap(String oficioMsiap) {
        this.oficioMsiap = oficioMsiap;
    }
    
    public Short getEvaluacionPj() {
        return evaluacionPj;
    }

    public void setEvaluacionPj(Short evaluacionPj) {
        this.evaluacionPj = evaluacionPj;
    }
    
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }
    
    public TipoBaseGt getTipoOpeId() {
        return tipoOpeId;
    }

    public void setTipoOpeId(TipoBaseGt tipoOpeId) {
        this.tipoOpeId = tipoOpeId;
    }    
}
