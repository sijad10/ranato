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
@Table(name = "SSP_PERSONA_FOTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspPersonaFoto.findAll", query = "SELECT s FROM SspPersonaFoto s"),
    @NamedQuery(name = "SspPersonaFoto.findById", query = "SELECT s FROM SspPersonaFoto s WHERE s.id = :id"),
    @NamedQuery(name = "SspPersonaFoto.findByAlumnoId", query = "SELECT s FROM SspPersonaFoto s WHERE s.alumnoId = :alumnoId"),
    @NamedQuery(name = "SspPersonaFoto.findByNombreFoto", query = "SELECT s FROM SspPersonaFoto s WHERE s.nombreFoto = :nombreFoto"),
    @NamedQuery(name = "SspPersonaFoto.findByFecha", query = "SELECT s FROM SspPersonaFoto s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspPersonaFoto.findByActivo", query = "SELECT s FROM SspPersonaFoto s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspPersonaFoto.findByAudLogin", query = "SELECT s FROM SspPersonaFoto s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspPersonaFoto.findByAudNumIp", query = "SELECT s FROM SspPersonaFoto s WHERE s.audNumIp = :audNumIp")})

public class SspPersonaFoto implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_PERSONA_FOTO")
    @SequenceGenerator(name = "SEQ_SSP_PERSONA_FOTO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_PERSONA_FOTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "ALUMNO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SspAlumnoCurso alumnoId;
    @Size(max = 20)
    @Column(name = "NOMBRE_FOTO")
    private String nombreFoto;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
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
    @OneToMany(mappedBy = "fotoId")
    private List<SspAlumnoCurso> sspAlumnoCursoList;
    @JoinColumn(name = "MODULO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt moduloId;

    public SspPersonaFoto() {
    }

    public SspPersonaFoto(Long id) {
        this.id = id;
    }

    public SspPersonaFoto(Long id, short activo, String audLogin, String audNumIp) {
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

    public SspAlumnoCurso getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(SspAlumnoCurso alumnoId) {
        this.alumnoId = alumnoId;
    }

    public String getNombreFoto() {
        return nombreFoto;
    }

    public void setNombreFoto(String nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
    public List<SspAlumnoCurso> getSspAlumnoCursoList() {
        return sspAlumnoCursoList;
    }

    public void setSspAlumnoCursoList(List<SspAlumnoCurso> sspAlumnoCursoList) {
        this.sspAlumnoCursoList = sspAlumnoCursoList;
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
        if (!(object instanceof SspPersonaFoto)) {
            return false;
        }
        SspPersonaFoto other = (SspPersonaFoto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspPersonaFoto[ id=" + id + " ]";
    }
    
    public TipoBaseGt getModuloId() {
        return moduloId;
    }

    public void setModuloId(TipoBaseGt moduloId) {
        this.moduloId = moduloId;
    }

}
