/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author rfernandezv
 */
@Entity
@Table(name = "SB_CS_ESTABLECIMIENTO", catalog = "", schema = "BDINTEGRADO")
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbCsEstablecimiento.findAll", query = "SELECT s FROM SbCsEstablecimiento s"),
    @NamedQuery(name = "SbCsEstablecimiento.findById", query = "SELECT s FROM SbCsEstablecimiento s WHERE s.id = :id"),
    @NamedQuery(name = "SbCsEstablecimiento.findByNombre", query = "SELECT s FROM SbCsEstablecimiento s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbCsEstablecimiento.findByNroResFuncionamiento", query = "SELECT s FROM SbCsEstablecimiento s WHERE s.nroResFuncionamiento = :nroResFuncionamiento"),
    @NamedQuery(name = "SbCsEstablecimiento.findByFechaIni", query = "SELECT s FROM SbCsEstablecimiento s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SbCsEstablecimiento.findByFechaFin", query = "SELECT s FROM SbCsEstablecimiento s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SbCsEstablecimiento.findByActivo", query = "SELECT s FROM SbCsEstablecimiento s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbCsEstablecimiento.findByAudLogin", query = "SELECT s FROM SbCsEstablecimiento s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbCsEstablecimiento.findByAudNumIp", query = "SELECT s FROM SbCsEstablecimiento s WHERE s.audNumIp = :audNumIp")})
public class SbCsEstablecimiento implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_CS_ESTABLECIMIENTO")
    @SequenceGenerator(name = "SEQ_SB_CS_ESTABLECIMIENTO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_CS_ESTABLECIMIENTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NRO_RES_FUNCIONAMIENTO")
    private String nroResFuncionamiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HABILITADO")
    private short habilitado;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "establecimientoId")
    private List<SbCsMedicoEstabsal> sbCsMedicoEstabsalList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuarioId;
    @JoinColumn(name = "PROPIETARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt propietarioId;
    @JoinColumn(name = "DIRECCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDireccionGt direccionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "establecimientoId")
    private List<SbCsCertifsalud> sbCsCertifsaludList;    
    @Size( max = 50)
    @Column(name = "NRO_RES_MINSA")
    private String nroResMinsa;
    @Column(name = "FECHA_INI_RG_MINSA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIniRgMinsa;
    @Column(name = "FECHA_FIN_RG_MINSA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinRgMinsa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "establecimientoId")
    private List<SbCsHorario> sbCsHorarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "establecimientoId")
    private List<SbCsSuspension> sbCsSuspensionList;

    public SbCsEstablecimiento() {
    }

    public SbCsEstablecimiento(Long id) {
        this.id = id;
    }

    public SbCsEstablecimiento(Long id, String nombre, String nroResFuncionamiento, Date fechaIni, Date fechaFin, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.nroResFuncionamiento = nroResFuncionamiento;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
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

    public String getNroResFuncionamiento() {
        return nroResFuncionamiento;
    }

    public void setNroResFuncionamiento(String nroResFuncionamiento) {
        this.nroResFuncionamiento = nroResFuncionamiento;
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
    public List<SbCsMedicoEstabsal> getSbCsMedicoEstabsalList() {
        return sbCsMedicoEstabsalList;
    }

    public void setSbCsMedicoEstabsalList(List<SbCsMedicoEstabsal> sbCsMedicoEstabsalList) {
        this.sbCsMedicoEstabsalList = sbCsMedicoEstabsalList;
    }

    public TipoBaseGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBaseGt tipoId) {
        this.tipoId = tipoId;
    }

    public SbUsuarioGt getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuarioGt usuarioId) {
        this.usuarioId = usuarioId;
    }

    public SbPersonaGt getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(SbPersonaGt propietarioId) {
        this.propietarioId = propietarioId;
    }

    public SbDireccionGt getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(SbDireccionGt direccionId) {
        this.direccionId = direccionId;
    }

    @XmlTransient
    public List<SbCsCertifsalud> getSbCsCertifsaludList() {
        return sbCsCertifsaludList;
    }

    public void setSbCsCertifsaludList(List<SbCsCertifsalud> sbCsCertifsaludList) {
        this.sbCsCertifsaludList = sbCsCertifsaludList;
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
        if (!(object instanceof SbCsEstablecimiento)) {
            return false;
        }
        SbCsEstablecimiento other = (SbCsEstablecimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbCsEstablecimiento[ id=" + id + " ]";
    }
    
    public short getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(short habilitado) {
        this.habilitado = habilitado;
    }

    public String getNroResMinsa() {
        return nroResMinsa;
    }

    public void setNroResMinsa(String nroResMinsa) {
        this.nroResMinsa = nroResMinsa;
    }

    public Date getFechaIniRgMinsa() {
        return fechaIniRgMinsa;
    }

    public void setFechaIniRgMinsa(Date fechaIniRgMinsa) {
        this.fechaIniRgMinsa = fechaIniRgMinsa;
    }

    public Date getFechaFinRgMinsa() {
        return fechaFinRgMinsa;
    }

    public void setFechaFinRgMinsa(Date fechaFinRgMinsa) {
        this.fechaFinRgMinsa = fechaFinRgMinsa;
    }

    public List<SbCsHorario> getSbCsHorarioList() {
        return sbCsHorarioList;
    }

    public void setSbCsHorarioList(List<SbCsHorario> sbCsHorarioList) {
        this.sbCsHorarioList = sbCsHorarioList;
    }

    public List<SbCsSuspension> getSbCsSuspensionList() {
        return sbCsSuspensionList;
    }

    public void setSbCsSuspensionList(List<SbCsSuspension> sbCsSuspensionList) {
        this.sbCsSuspensionList = sbCsSuspensionList;
    }
    
    
}
