/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.turreg.data;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TUR_PROGRAMACION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TurProgramacion.findAll", query = "SELECT t FROM TurProgramacion t"),
    @NamedQuery(name = "TurProgramacion.findById", query = "SELECT t FROM TurProgramacion t WHERE t.id = :id"),
    @NamedQuery(name = "TurProgramacion.findByHora", query = "SELECT t FROM TurProgramacion t WHERE t.hora = :hora"),
    @NamedQuery(name = "TurProgramacion.findByCantCupos", query = "SELECT t FROM TurProgramacion t WHERE t.cantCupos = :cantCupos"),
    @NamedQuery(name = "TurProgramacion.findByActivo", query = "SELECT t FROM TurProgramacion t WHERE t.activo = :activo"),
    @NamedQuery(name = "TurProgramacion.findByAudLogin", query = "SELECT t FROM TurProgramacion t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TurProgramacion.findByAudNumIp", query = "SELECT t FROM TurProgramacion t WHERE t.audNumIp = :audNumIp")})
public class TurProgramacion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TUR_PROGRAMACION")
    @SequenceGenerator(name = "SEQ_TUR_PROGRAMACION", schema = "BDINTEGRADO", sequenceName = "SEQ_TUR_PROGRAMACION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "HORA")
    private String hora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANT_CUPOS")
    private Long cantCupos;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programacionId")
    private List<TurTurno> turTurnoList;
    @JoinColumn(name = "TIPO_TURNO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase tipoTurno;
    @JoinColumn(name = "SEDE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase sedeId;
    @JoinColumn(name = "TIPO_CITA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase tipoCitaId;
    

    public TurProgramacion() {
    }

    public TurProgramacion(Long id) {
        this.id = id;
    }

    public TurProgramacion(Long id, String hora, Long cantCupos, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.hora = hora;
        this.cantCupos = cantCupos;
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

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Long getCantCupos() {
        return cantCupos;
    }

    public void setCantCupos(Long cantCupos) {
        this.cantCupos = cantCupos;
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
    public List<TurTurno> getTurTurnoList() {
        return turTurnoList;
    }

    public void setTurTurnoList(List<TurTurno> turTurnoList) {
        this.turTurnoList = turTurnoList;
    }

    public TipoBase getTipoTurno() {
        return tipoTurno;
    }

    public void setTipoTurno(TipoBase tipoTurno) {
        this.tipoTurno = tipoTurno;
    }

    public TipoBase getSedeId() {
        return sedeId;
    }

    public void setSedeId(TipoBase sedeId) {
        this.sedeId = sedeId;
    }

    public TipoBase getTipoCitaId() {
        return tipoCitaId;
    }

    public void setTipoCitaId(TipoBase tipoCitaId) {
        this.tipoCitaId = tipoCitaId;
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
        if (!(object instanceof TurProgramacion)) {
            return false;
        }
        TurProgramacion other = (TurProgramacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.turreg.data.TurProgramacion[ id=" + id + " ]";
    }
    
}
