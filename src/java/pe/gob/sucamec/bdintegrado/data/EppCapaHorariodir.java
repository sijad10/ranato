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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_CAPA_HORARIODIR", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppCapaHorariodir.findAll", query = "SELECT e FROM EppCapaHorariodir e"),
    @NamedQuery(name = "EppCapaHorariodir.findById", query = "SELECT e FROM EppCapaHorariodir e WHERE e.id = :id"),
    @NamedQuery(name = "EppCapaHorariodir.findByFecha", query = "SELECT e FROM EppCapaHorariodir e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "EppCapaHorariodir.findByHoraInicio", query = "SELECT e FROM EppCapaHorariodir e WHERE e.horaInicio = :horaInicio"),
    @NamedQuery(name = "EppCapaHorariodir.findByHoraFin", query = "SELECT e FROM EppCapaHorariodir e WHERE e.horaFin = :horaFin"),
    @NamedQuery(name = "EppCapaHorariodir.findByDireccion", query = "SELECT e FROM EppCapaHorariodir e WHERE e.direccion = :direccion"),
    @NamedQuery(name = "EppCapaHorariodir.findByActivo", query = "SELECT e FROM EppCapaHorariodir e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppCapaHorariodir.findByAudLogin", query = "SELECT e FROM EppCapaHorariodir e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppCapaHorariodir.findByAudNumIp", query = "SELECT e FROM EppCapaHorariodir e WHERE e.audNumIp = :audNumIp")})
public class EppCapaHorariodir implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_CAPA_HORARIODIR")
    @SequenceGenerator(name = "SEQ_EPP_CAPA_HORARIODIR", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_CAPA_HORARIODIR", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HORA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HORA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "DIRECCION")
    private String direccion;
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
    @ManyToMany(mappedBy = "eppCapaHorariodirList")
    private List<EppCertificado> eppCertificadoList;
    @JoinColumn(name = "CAPACITACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppCapacitacion capacitacionId;

    public EppCapaHorariodir() {
    }

    public EppCapaHorariodir(Long id) {
        this.id = id;
    }

    public EppCapaHorariodir(Long id, Date fecha, Date horaInicio, Date horaFin, String direccion, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.direccion = direccion;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
    public List<EppCertificado> getEppCertificadoList() {
        return eppCertificadoList;
    }

    public void setEppCertificadoList(List<EppCertificado> eppCertificadoList) {
        this.eppCertificadoList = eppCertificadoList;
    }

    public EppCapacitacion getCapacitacionId() {
        return capacitacionId;
    }

    public void setCapacitacionId(EppCapacitacion capacitacionId) {
        this.capacitacionId = capacitacionId;
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
        if (!(object instanceof EppCapaHorariodir)) {
            return false;
        }
        EppCapaHorariodir other = (EppCapaHorariodir) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppCapaHorariodir[ id=" + id + " ]";
    }
    
}
