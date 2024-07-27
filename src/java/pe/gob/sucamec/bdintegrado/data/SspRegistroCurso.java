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
@Table(name = "SSP_REGISTRO_CURSO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspRegistroCurso.findAll", query = "SELECT s FROM SspRegistroCurso s"),
    @NamedQuery(name = "SspRegistroCurso.findById", query = "SELECT s FROM SspRegistroCurso s WHERE s.id = :id"),
    @NamedQuery(name = "SspRegistroCurso.findByNroExpediente", query = "SELECT s FROM SspRegistroCurso s WHERE s.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "SspRegistroCurso.findByFechaRegistro", query = "SELECT s FROM SspRegistroCurso s WHERE s.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "SspRegistroCurso.findByActivo", query = "SELECT s FROM SspRegistroCurso s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspRegistroCurso.findByAudLogin", query = "SELECT s FROM SspRegistroCurso s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspRegistroCurso.findByAudNumIp", query = "SELECT s FROM SspRegistroCurso s WHERE s.audNumIp = :audNumIp")})
public class SspRegistroCurso implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_REGISTRO_CURSO")
    @SequenceGenerator(name = "SEQ_SSP_REGISTRO_CURSO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_REGISTRO_CURSO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroCursoId")
    private List<SspCursoEvento> sspCursoEventoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroCursoId")
    private List<SspProgramacion> sspProgramacionList;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad estadoId;
    @JoinColumn(name = "ADMINISTRADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt administradoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroCursoId")
    private List<SspAlumnoCurso> sspAlumnoCursoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "regcursoId")
    private List<SspInstructorModulo> sspInstructorModuloList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "regcursoId")
    private List<SspNotas> sspNotasList;
    @Size(max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
    @Size(max = 300)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Size(max = 60)
    @Column(name = "HASH_QR_CONST")
    private String hashQrConst;
    @Size(max = 500)
    @Column(name = "EXCEPCION")
    private String excepcion;
    @JoinColumn(name = "ESTADO_FISCA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad estadoFiscaId;
    @JoinColumn(name = "TIPO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoOpeId;
    @JoinColumn(name = "TIPO_CURSO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoCurso;
    @JoinColumn(name = "TIPO_SERVICIO", referencedColumnName = "ID")
    @ManyToOne
    private TipoSeguridad tipoServicio;
    /*@Column(name = "TIPO_SERVICIO")//TIPO_SERVICIO
    private Long tipoServicio;*/
    

    public SspRegistroCurso() {
    }

    public SspRegistroCurso(Long id) {
        this.id = id;
    }

    public SspRegistroCurso(Long id, Date fechaRegistro, short activo, String audLogin, String audNumIp) {
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

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
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

    @XmlTransient
    public List<SspCursoEvento> getSspCursoEventoList() {
        return sspCursoEventoList;
    }

    public void setSspCursoEventoList(List<SspCursoEvento> sspCursoEventoList) {
        this.sspCursoEventoList = sspCursoEventoList;
    }

    @XmlTransient
    public List<SspProgramacion> getSspProgramacionList() {
        return sspProgramacionList;
    }

    public void setSspProgramacionList(List<SspProgramacion> sspProgramacionList) {
        this.sspProgramacionList = sspProgramacionList;
    }

    public TipoSeguridad getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoSeguridad estadoId) {
        this.estadoId = estadoId;
    }

    public SbPersonaGt getAdministradoId() {
        return administradoId;
    }

    public void setAdministradoId(SbPersonaGt administradoId) {
        this.administradoId = administradoId;
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
        if (!(object instanceof SspRegistroCurso)) {
            return false;
        }
        SspRegistroCurso other = (SspRegistroCurso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspRegistroCurso[ id=" + id + " ]";
    }
    
    @XmlTransient
    public List<SspInstructorModulo> getSspInstructorModuloList() {
        return sspInstructorModuloList;
    }

    public void setSspInstructorModuloList(List<SspInstructorModulo> sspInstructorModuloList) {
        this.sspInstructorModuloList = sspInstructorModuloList;
    }

    @XmlTransient
    public List<SspNotas> getSspNotasList() {
        return sspNotasList;
    }

    public void setSspNotasList(List<SspNotas> sspNotasList) {
        this.sspNotasList = sspNotasList;
    }
    
    public String getHashQr() {
        return hashQr;
    }

    public void setHashQr(String hashQr) {
        this.hashQr = hashQr;
    }
    
    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getHashQrConst() {
        return hashQrConst;
    }

    public void setHashQrConst(String hashQrConst) {
        this.hashQrConst = hashQrConst;
    }

    public String getExcepcion() {
        return excepcion;
    }

    public void setExcepcion(String excepcion) {
        this.excepcion = excepcion;
    }
    
    public TipoSeguridad getEstadoFiscaId() {
        return estadoFiscaId;
    }

    public void setEstadoFiscaId(TipoSeguridad estadoFiscaId) {
        this.estadoFiscaId = estadoFiscaId;
    }
    
    public TipoBaseGt getTipoOpeId() {
        return tipoOpeId;
    }

    public void setTipoOpeId(TipoBaseGt tipoOpeId) {
        this.tipoOpeId = tipoOpeId;
    }    
    
    public TipoBaseGt getTipoCurso() {
        return tipoCurso;
    }

    public void setTipoCurso(TipoBaseGt tipoCurso) {
        this.tipoCurso = tipoCurso;
    }
    
    public TipoSeguridad getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoSeguridad tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    /*public Long getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(Long tipoServicio) {
        this.tipoServicio = tipoServicio;
    }*/


    
   
}
