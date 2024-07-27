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
import pe.gob.sucamec.sistemabase.data.SbDistrito;
import pe.gob.sucamec.sistemabase.data.SbPais;

/**
 *
 * @author locador192.ogtic
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_EVENTO_DIPLO_DEPOR",catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaEventoDiploDepor.findAll", query = "SELECT a FROM AmaEventoDiploDepor a"),
    @NamedQuery(name = "AmaEventoDiploDepor.findById", query = "SELECT a FROM AmaEventoDiploDepor a WHERE a.id = :id"),
    @NamedQuery(name = "AmaEventoDiploDepor.findByNombre", query = "SELECT a FROM AmaEventoDiploDepor a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "AmaEventoDiploDepor.findByDireccion", query = "SELECT a FROM AmaEventoDiploDepor a WHERE a.direccion = :direccion"),
    @NamedQuery(name = "AmaEventoDiploDepor.findByFechaInicio", query = "SELECT a FROM AmaEventoDiploDepor a WHERE a.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "AmaEventoDiploDepor.findByFechaFin", query = "SELECT a FROM AmaEventoDiploDepor a WHERE a.fechaFin = :fechaFin"),
    @NamedQuery(name = "AmaEventoDiploDepor.findByActivo", query = "SELECT a FROM AmaEventoDiploDepor a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaEventoDiploDepor.findByAudLogin", query = "SELECT a FROM AmaEventoDiploDepor a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaEventoDiploDepor.findByAudNumIp", query = "SELECT a FROM AmaEventoDiploDepor a WHERE a.audNumIp = :audNumIp")})
public class AmaEventoDiploDepor implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_EVENTO_DIPLO_DEPOR")
    @SequenceGenerator(name = "SEQ_AMA_EVENTO_DIPLO_DEPOR", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_EVENTO_DIPLO_DEPOR", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 500)
    @Column(name = "DIRECCION")
    private String direccion;
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(mappedBy = "eventoDipDepId")
    private List<AmaIngsalTemDef> amaIngsalTemDefList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoId;
    @JoinColumn(name = "PAIS_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPais paisId;
    @JoinColumn(name = "DISTRITO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbDistrito distritoId;

    public AmaEventoDiploDepor() {
    }

    public AmaEventoDiploDepor(Long id) {
        this.id = id;
    }

    public AmaEventoDiploDepor(Long id, String nombre, short activo, String audLogin, String audNumIp) {
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
    public List<AmaIngsalTemDef> getAmaIngsalTemDefList() {
        return amaIngsalTemDefList;
    }

    public void setAmaIngsalTemDefList(List<AmaIngsalTemDef> amaIngsalTemDefList) {
        this.amaIngsalTemDefList = amaIngsalTemDefList;
    }

    public TipoGamac getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoGamac tipoId) {
        this.tipoId = tipoId;
    }

    public SbPais getPaisId() {
        return paisId;
    }

    public void setPaisId(SbPais paisId) {
        this.paisId = paisId;
    }

    public SbDistrito getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(SbDistrito distritoId) {
        this.distritoId = distritoId;
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
        if (!(object instanceof AmaEventoDiploDepor)) {
            return false;
        }
        AmaEventoDiploDepor other = (AmaEventoDiploDepor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaEventoDiploDepor[ id=" + id + " ]";
    }
    
}
