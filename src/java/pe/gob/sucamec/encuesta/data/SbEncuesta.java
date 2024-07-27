/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.encuesta.data;

import java.io.Serializable;
import java.math.BigDecimal;
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
import pe.gob.sucamec.sistemabase.data.SbSistema;
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_ENCUESTA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbEncuesta.findAll", query = "SELECT s FROM SbEncuesta s"),
    @NamedQuery(name = "SbEncuesta.findById", query = "SELECT s FROM SbEncuesta s WHERE s.id = :id"),
    @NamedQuery(name = "SbEncuesta.findByNombre", query = "SELECT s FROM SbEncuesta s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbEncuesta.findByActivo", query = "SELECT s FROM SbEncuesta s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbEncuesta.findByAudLogin", query = "SELECT s FROM SbEncuesta s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbEncuesta.findByAudNumIp", query = "SELECT s FROM SbEncuesta s WHERE s.audNumIp = :audNumIp")})
public class SbEncuesta implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_ENCUESTA")
    @SequenceGenerator(name = "SEQ_SB_ENCUESTA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_ENCUESTA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
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
    @JoinColumn(name = "SISTEMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbSistema sistemaId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase tipoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "encuestaId")
    private List<SbPregunta> sbPreguntaList;
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "LIMITE_EN_MINUTOS")
    private Integer limiteEnMinutos;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "NOTA_APROBATORIA")
    private BigDecimal notaAprobatoria;
    @Size(min = 1, max = 500)
    @Column(name = "MENSAJE_ADMINISTRADO")
    private String mensajeAdministrado;   

    public SbEncuesta() {
    }

    public SbEncuesta(Long id) {
        this.id = id;
    }

    public SbEncuesta(Long id, String nombre, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public SbSistema getSistemaId() {
        return sistemaId;
    }

    public void setSistemaId(SbSistema sistemaId) {
        this.sistemaId = sistemaId;
    }

    public TipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBase tipoId) {
        this.tipoId = tipoId;
    }

    public String getMensajeAdministrado() {
        return mensajeAdministrado;
    }

    public void setMensajeAdministrado(String mensajeAdministrado) {
        this.mensajeAdministrado = mensajeAdministrado;
    }
    
    @XmlTransient
    public List<SbPregunta> getSbPreguntaList() {
        return sbPreguntaList;
    }

    public void setSbPreguntaList(List<SbPregunta> sbPreguntaList) {
        this.sbPreguntaList = sbPreguntaList;
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
        if (!(object instanceof SbEncuesta)) {
            return false;
        }
        SbEncuesta other = (SbEncuesta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.encuesta.data.SbEncuesta[ id=" + id + " ]";
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getLimiteEnMinutos() {
        return limiteEnMinutos;
    }

    public void setLimiteEnMinutos(Integer limiteEnMinutos) {
        this.limiteEnMinutos = limiteEnMinutos;
    }

    public BigDecimal getNotaAprobatoria() {
        return notaAprobatoria;
    }

    public void setNotaAprobatoria(BigDecimal notaAprobatoria) {
        this.notaAprobatoria = notaAprobatoria;
    }
    
}
