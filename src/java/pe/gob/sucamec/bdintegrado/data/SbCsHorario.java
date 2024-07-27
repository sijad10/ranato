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
import javax.persistence.JoinTable;
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
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_CS_HORARIO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbCsHorario.findAll", query = "SELECT s FROM SbCsHorario s"),
    @NamedQuery(name = "SbCsHorario.findById", query = "SELECT s FROM SbCsHorario s WHERE s.id = :id"),
    @NamedQuery(name = "SbCsHorario.findByHoraInicio", query = "SELECT s FROM SbCsHorario s WHERE s.horaInicio = :horaInicio"),
    @NamedQuery(name = "SbCsHorario.findByHoraFin", query = "SELECT s FROM SbCsHorario s WHERE s.horaFin = :horaFin"),
    @NamedQuery(name = "SbCsHorario.findByActivo", query = "SELECT s FROM SbCsHorario s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbCsHorario.findByAudLogin", query = "SELECT s FROM SbCsHorario s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbCsHorario.findByAudNumIp", query = "SELECT s FROM SbCsHorario s WHERE s.audNumIp = :audNumIp")})
public class SbCsHorario implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_CS_HORARIO")
    @SequenceGenerator(name = "SEQ_SB_CS_HORARIO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_CS_HORARIO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
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
    @JoinTable(schema="BDINTEGRADO", name = "SB_CS_HORARIO_DIA", joinColumns = {
        @JoinColumn(name = "HORARIO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "DIA_SEMANA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<TipoBaseGt> tipoBaseList;
    @JoinColumn(name = "ESTABLECIMIENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbCsEstablecimiento establecimientoId;

    public SbCsHorario() {
    }

    public SbCsHorario(Long id, Date horaInicio, Date horaFin, short activo, String audLogin, String audNumIp, List<TipoBaseGt> tipoBaseList, SbCsEstablecimiento establecimientoId) {
        this.id = id;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
        this.tipoBaseList = tipoBaseList;
        this.establecimientoId = establecimientoId;
    }
    

    public SbCsHorario(Long id) {
        this.id = id;
    }

    public SbCsHorario(Long id, Date horaInicio, Date horaFin, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
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
    public List<TipoBaseGt> getTipoBaseList() {
        return tipoBaseList;
    }

    public void setTipoBaseList(List<TipoBaseGt> tipoBaseList) {
        this.tipoBaseList = tipoBaseList;
    }

    public SbCsEstablecimiento getEstablecimientoId() {
        return establecimientoId;
    }

    public void setEstablecimientoId(SbCsEstablecimiento establecimientoId) {
        this.establecimientoId = establecimientoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SbCsHorario)) {
            return false;
        }
        SbCsHorario other = (SbCsHorario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbCsHorario[ id=" + id + " ]";
    }
    
}
