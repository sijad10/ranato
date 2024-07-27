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
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
@Table(name = "SSP_NOTAS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspNotas.findAll", query = "SELECT s FROM SspNotas s"),
    @NamedQuery(name = "SspNotas.findById", query = "SELECT s FROM SspNotas s WHERE s.id = :id"),
    @NamedQuery(name = "SspNotas.findByNota", query = "SELECT s FROM SspNotas s WHERE s.nota = :nota"),
    @NamedQuery(name = "SspNotas.findByEstado", query = "SELECT s FROM SspNotas s WHERE s.estado = :estado"),
    @NamedQuery(name = "SspNotas.findByObservacion", query = "SELECT s FROM SspNotas s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SspNotas.findByAsistencia", query = "SELECT s FROM SspNotas s WHERE s.asistencia = :asistencia"),
    @NamedQuery(name = "SspNotas.findByActivo", query = "SELECT s FROM SspNotas s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspNotas.findByAudLogin", query = "SELECT s FROM SspNotas s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspNotas.findByAudNumIp", query = "SELECT s FROM SspNotas s WHERE s.audNumIp = :audNumIp")})
public class SspNotas implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_NOTAS")
    @SequenceGenerator(name = "SEQ_SSP_NOTAS", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_NOTAS", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "NOTA")
    private Double nota;
    @Column(name = "ESTADO")
    private Short estado;
    @Size(max = 400)
    @Column(name = "OBSERVACION")
    private String observacion;
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
    @JoinColumn(name = "MODULO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspModulo moduloId;
    @JoinColumn(name = "ALUMNO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspAlumnoCurso alumnoId;
    @JoinColumn(name = "REGCURSO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistroCurso regcursoId;

    public SspNotas() {
    }

    public SspNotas(Long id) {
        this.id = id;
    }

    public SspNotas(Long id, short activo, String audLogin, String audNumIp) {
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

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }
    
    public Integer getNotaInt() {
        if(nota != null){
            return nota.intValue();
        }
        return null;
    }
    
    public void setNotaInt(Integer nota) {
        if(nota != null){
            this.nota = nota.doubleValue();
        }else{
            this.nota = null;
        }
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public SspModulo getModuloId() {
        return moduloId;
    }

    public void setModuloId(SspModulo moduloId) {
        this.moduloId = moduloId;
    }

    public SspAlumnoCurso getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(SspAlumnoCurso alumnoId) {
        this.alumnoId = alumnoId;
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
        if (!(object instanceof SspNotas)) {
            return false;
        }
        SspNotas other = (SspNotas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspNotas[ id=" + id + " ]";
    }
    
    public SspRegistroCurso getRegcursoId() {
        return regcursoId;
    }

    public void setRegcursoId(SspRegistroCurso regcursoId) {
        this.regcursoId = regcursoId;
    }
    
    public boolean getAsistenciaBoolean() {
        if(asistencia == null || asistencia == 0){
            return false;
        }
        return true;
    }

    public void setAsistenciaBoolean(boolean asistencia) {
        if(asistencia){
            this.asistencia = (short) 1;
        }else{
            this.asistencia = (short) 0;
        }
    }
}
