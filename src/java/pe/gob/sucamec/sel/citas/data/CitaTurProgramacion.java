/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

import java.io.Serializable;
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
import org.eclipse.persistence.annotations.ReadOnly;

/**
 *
 * @author msalinas
 */
@ReadOnly
@Entity
@Table(name = "TUR_PROGRAMACION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaTurProgramacion.findAll", query = "SELECT t FROM CitaTurProgramacion t"),
    @NamedQuery(name = "CitaTurProgramacion.findById", query = "SELECT t FROM CitaTurProgramacion t WHERE t.id = :id"),
    @NamedQuery(name = "CitaTurProgramacion.findByHora", query = "SELECT t FROM CitaTurProgramacion t WHERE t.hora = :hora"),
    @NamedQuery(name = "CitaTurProgramacion.findByCantCupos", query = "SELECT t FROM CitaTurProgramacion t WHERE t.cantCupos = :cantCupos"),
    @NamedQuery(name = "CitaTurProgramacion.findByActivo", query = "SELECT t FROM CitaTurProgramacion t WHERE t.activo = :activo"),
    @NamedQuery(name = "CitaTurProgramacion.findByAudLogin", query = "SELECT t FROM CitaTurProgramacion t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "CitaTurProgramacion.findByAudNumIp", query = "SELECT t FROM CitaTurProgramacion t WHERE t.audNumIp = :audNumIp")})
public class CitaTurProgramacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
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
    private long cantCupos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programacionId")
    private List<CitaTurTurno> turTurnoList;
    @JoinColumn(name = "TIPO_TURNO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTipoBase tipoTurno;
    @JoinColumn(name = "SEDE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTipoBase sedeId;
    @JoinColumn(name = "TIPO_CITA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTipoBase tipoCitaId;

    public CitaTurProgramacion() {
    }

    public CitaTurProgramacion(Long id) {
        this.id = id;
    }

    public CitaTurProgramacion(Long id, String hora, long cantCupos, short activo, String audLogin, String audNumIp) {
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

    public long getCantCupos() {
        return cantCupos;
    }

    public void setCantCupos(long cantCupos) {
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
    public List<CitaTurTurno> getTurTurnoList() {
        return turTurnoList;
    }

    public void setTurTurnoList(List<CitaTurTurno> turTurnoList) {
        this.turTurnoList = turTurnoList;
    }

    public CitaTipoBase getTipoTurno() {
        return tipoTurno;
    }

    public void setTipoTurno(CitaTipoBase tipoTurno) {
        this.tipoTurno = tipoTurno;
    }

    public CitaTipoBase getSedeId() {
        return sedeId;
    }

    public void setSedeId(CitaTipoBase sedeId) {
        this.sedeId = sedeId;
    }

    public CitaTipoBase getTipoCitaId() {
        return tipoCitaId;
    }

    public void setTipoCitaId(CitaTipoBase tipoCitaId) {
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
        if (!(object instanceof CitaTurProgramacion)) {
            return false;
        }
        CitaTurProgramacion other = (CitaTurProgramacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.citas.data.CitaTurProgramacion[ id=" + id + " ]";
    }

}
