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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "SSP_INSTRUCTOR", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspInstructor.findAll", query = "SELECT s FROM SspInstructor s"),
    @NamedQuery(name = "SspInstructor.findById", query = "SELECT s FROM SspInstructor s WHERE s.id = :id"),
    @NamedQuery(name = "SspInstructor.findByNroFicha", query = "SELECT s FROM SspInstructor s WHERE s.nroFicha = :nroFicha"),
    @NamedQuery(name = "SspInstructor.findByFechaEmicion", query = "SELECT s FROM SspInstructor s WHERE s.fechaEmicion = :fechaEmicion"),
    @NamedQuery(name = "SspInstructor.findByFechaVencimiento", query = "SELECT s FROM SspInstructor s WHERE s.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "SspInstructor.findByActivo", query = "SELECT s FROM SspInstructor s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspInstructor.findByAudLogin", query = "SELECT s FROM SspInstructor s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspInstructor.findByAudNumIp", query = "SELECT s FROM SspInstructor s WHERE s.audNumIp = :audNumIp")})
public class SspInstructor implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_INSTRUCTOR")
    @SequenceGenerator(name = "SEQ_SSP_INSTRUCTOR", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_INSTRUCTOR", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NRO_FICHA")
    private Long nroFicha;
    @Column(name = "FECHA_EMICION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmicion;
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
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
    @JoinColumn(name = "ESTADO_INSTRU_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad estadoInstruId;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersonaGt empresaId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instructorId")
    private List<SspInstructorModulo> sspInstructorModuloList;
    @OneToMany(mappedBy = "instructorId")
    private List<SspProgramacion> sspProgramacionList;

    public SspInstructor() {
    }

    public SspInstructor(Long id) {
        this.id = id;
    }

    public SspInstructor(Long id, short activo, String audLogin, String audNumIp) {
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

    public Long getNroFicha() {
        return nroFicha;
    }

    public void setNroFicha(Long nroFicha) {
        this.nroFicha = nroFicha;
    }

    public Date getFechaEmicion() {
        return fechaEmicion;
    }

    public void setFechaEmicion(Date fechaEmicion) {
        this.fechaEmicion = fechaEmicion;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
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

    public TipoSeguridad getEstadoInstruId() {
        return estadoInstruId;
    }

    public void setEstadoInstruId(TipoSeguridad estadoInstruId) {
        this.estadoInstruId = estadoInstruId;
    }

    public SbPersonaGt getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersonaGt empresaId) {
        this.empresaId = empresaId;
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
        if (!(object instanceof SspInstructor)) {
            return false;
        }
        SspInstructor other = (SspInstructor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspInstructor[ id=" + id + " ]";
    }
 
    @XmlTransient
    public List<SspProgramacion> getSspProgramacionList() {
        return sspProgramacionList;
    }

    public void setSspProgramacionList(List<SspProgramacion> sspProgramacionList) {
        this.sspProgramacionList = sspProgramacionList;
    }
    
    @XmlTransient
    public List<SspInstructorModulo> getSspInstructorModuloList() {
        return sspInstructorModuloList;
    }

    public void setSspInstructorModuloList(List<SspInstructorModulo> sspInstructorModuloList) {
        this.sspInstructorModuloList = sspInstructorModuloList;
    }
}
