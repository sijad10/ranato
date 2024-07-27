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
import pe.gob.sucamec.sistemabase.data.SbDistrito;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_CAPACITACION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppCapacitacion.findAll", query = "SELECT e FROM EppCapacitacion e"),
    @NamedQuery(name = "EppCapacitacion.findById", query = "SELECT e FROM EppCapacitacion e WHERE e.id = :id"),
    @NamedQuery(name = "EppCapacitacion.findByFechaInicio", query = "SELECT e FROM EppCapacitacion e WHERE e.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "EppCapacitacion.findByFechaFin", query = "SELECT e FROM EppCapacitacion e WHERE e.fechaFin = :fechaFin"),
    @NamedQuery(name = "EppCapacitacion.findByActivo", query = "SELECT e FROM EppCapacitacion e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppCapacitacion.findByAudLogin", query = "SELECT e FROM EppCapacitacion e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppCapacitacion.findByAudNumIp", query = "SELECT e FROM EppCapacitacion e WHERE e.audNumIp = :audNumIp"),
    @NamedQuery(name = "EppCapacitacion.findByNroVacantes", query = "SELECT e FROM EppCapacitacion e WHERE e.nroVacantes = :nroVacantes"),
    @NamedQuery(name = "EppCapacitacion.findByFechaLimInscrip", query = "SELECT e FROM EppCapacitacion e WHERE e.fechaLimInscrip = :fechaLimInscrip")})
public class EppCapacitacion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_CAPACITACION")
    @SequenceGenerator(name = "SEQ_EPP_CAPACITACION", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_CAPACITACION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
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
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_VACANTES")
    private int nroVacantes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_LIM_INSCRIP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLimInscrip;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_CAPACITACION_ACTIV", joinColumns = {
        @JoinColumn(name = "CAPACITACION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "ACTIVIDAD_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<TipoExplosivoGt> tipoExplosivoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "capacitacionId")
    private List<EppCertificado> eppCertificadoList;
    @JoinColumn(name = "SEDE_ORGANIZA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt sedeOrganizaId;
    @JoinColumn(name = "UBIGEO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistrito ubigeoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "capacitacionId")
    private List<EppCapaHorariodir> eppCapaHorariodirList;

    public EppCapacitacion() {
    }

    public EppCapacitacion(Long id) {
        this.id = id;
    }

    public EppCapacitacion(Long id, Date fechaInicio, Date fechaFin, short activo, String audLogin, String audNumIp, int nroVacantes, Date fechaLimInscrip) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
        this.nroVacantes = nroVacantes;
        this.fechaLimInscrip = fechaLimInscrip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getNroVacantes() {
        return nroVacantes;
    }

    public void setNroVacantes(int nroVacantes) {
        this.nroVacantes = nroVacantes;
    }

    public Date getFechaLimInscrip() {
        return fechaLimInscrip;
    }

    public void setFechaLimInscrip(Date fechaLimInscrip) {
        this.fechaLimInscrip = fechaLimInscrip;
    }

    @XmlTransient
    public List<TipoExplosivoGt> getTipoExplosivoList() {
        return tipoExplosivoList;
    }

    public void setTipoExplosivoList(List<TipoExplosivoGt> tipoExplosivoList) {
        this.tipoExplosivoList = tipoExplosivoList;
    }

    @XmlTransient
    public List<EppCertificado> getEppCertificadoList() {
        return eppCertificadoList;
    }

    public void setEppCertificadoList(List<EppCertificado> eppCertificadoList) {
        this.eppCertificadoList = eppCertificadoList;
    }

    public TipoBaseGt getSedeOrganizaId() {
        return sedeOrganizaId;
    }

    public void setSedeOrganizaId(TipoBaseGt sedeOrganizaId) {
        this.sedeOrganizaId = sedeOrganizaId;
    }

    public SbDistrito getUbigeoId() {
        return ubigeoId;
    }

    public void setUbigeoId(SbDistrito ubigeoId) {
        this.ubigeoId = ubigeoId;
    }

    @XmlTransient
    public List<EppCapaHorariodir> getEppCapaHorariodirList() {
        return eppCapaHorariodirList;
    }

    public void setEppCapaHorariodirList(List<EppCapaHorariodir> eppCapaHorariodirList) {
        this.eppCapaHorariodirList = eppCapaHorariodirList;
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
        if (!(object instanceof EppCapacitacion)) {
            return false;
        }
        EppCapacitacion other = (EppCapacitacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppCapacitacion[ id=" + id + " ]";
    }
    
}
