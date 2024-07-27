/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
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
 * @author mespinoza
 */
@Entity
@Table(name = "SB_FERIADO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbFeriado.findAll", query = "SELECT s FROM SbFeriado s"),
    @NamedQuery(name = "SbFeriado.findById", query = "SELECT s FROM SbFeriado s WHERE s.id = :id"),
    @NamedQuery(name = "SbFeriado.findByFechaFeriado", query = "SELECT s FROM SbFeriado s WHERE s.fechaFeriado = :fechaFeriado"),
    @NamedQuery(name = "SbFeriado.findByActivo", query = "SELECT s FROM SbFeriado s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbFeriado.findByAudLogin", query = "SELECT s FROM SbFeriado s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbFeriado.findByAudNumIp", query = "SELECT s FROM SbFeriado s WHERE s.audNumIp = :audNumIp")})
public class SbFeriado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_FERIADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFeriado;
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
    @JoinColumn(name = "TIPO_FERIADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoFeriadoId;

    public SbFeriado() {
    }

    public SbFeriado(Long id) {
        this.id = id;
    }

    public SbFeriado(Long id, Date fechaFeriado, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaFeriado = fechaFeriado;
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

    public Date getFechaFeriado() {
        return fechaFeriado;
    }

    public void setFechaFeriado(Date fechaFeriado) {
        this.fechaFeriado = fechaFeriado;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbFeriado)) {
            return false;
        }
        SbFeriado other = (SbFeriado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.SbFeriado[ id=" + id + " ]";
    }
    
    public TipoBaseGt getTipoFeriadoId() {
        return tipoFeriadoId;
    }

    public void setTipoFeriadoId(TipoBaseGt tipoFeriadoId) {
        this.tipoFeriadoId = tipoFeriadoId;
    }    
}
