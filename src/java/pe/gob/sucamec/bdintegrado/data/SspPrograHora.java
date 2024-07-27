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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "SSP_PROGRA_HORA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspPrograHora.findAll", query = "SELECT s FROM SspPrograHora s"),
    @NamedQuery(name = "SspPrograHora.findById", query = "SELECT s FROM SspPrograHora s WHERE s.id = :id"),
    @NamedQuery(name = "SspPrograHora.findByFecha", query = "SELECT s FROM SspPrograHora s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspPrograHora.findByHoraInicio", query = "SELECT s FROM SspPrograHora s WHERE s.horaInicio = :horaInicio"),
    @NamedQuery(name = "SspPrograHora.findByHoraFin", query = "SELECT s FROM SspPrograHora s WHERE s.horaFin = :horaFin"),
    @NamedQuery(name = "SspPrograHora.findByActivo", query = "SELECT s FROM SspPrograHora s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspPrograHora.findByAudLogin", query = "SELECT s FROM SspPrograHora s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspPrograHora.findByAudNumIp", query = "SELECT s FROM SspPrograHora s WHERE s.audNumIp = :audNumIp")})
public class SspPrograHora implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_PROGRA_HORA")
    @SequenceGenerator(name = "SEQ_SSP_PROGRA_HORA", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_PROGRA_HORA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "HORA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaInicio;
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
    @JoinColumn(name = "PROGRAMACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspProgramacion programacionId;
    @JoinColumn(name = "TIPO_CURSO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad tipoCurso;
    @Column(name = "FECHA_REG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReg;

    public SspPrograHora() {
    }

    public SspPrograHora(Long id) {
        this.id = id;
    }

    public SspPrograHora(Long id, short activo, String audLogin, String audNumIp) {
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

    public SspProgramacion getProgramacionId() {
        return programacionId;
    }

    public void setProgramacionId(SspProgramacion programacionId) {
        this.programacionId = programacionId;
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
        if (!(object instanceof SspPrograHora)) {
            return false;
        }
        SspPrograHora other = (SspPrograHora) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspPrograHora[ id=" + id + " ]";
    }
    
    public TipoSeguridad getTipoCurso() {
        return tipoCurso;
    }

    public void setTipoCurso(TipoSeguridad tipoCurso) {
        this.tipoCurso = tipoCurso;
    }
    
    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }
}
