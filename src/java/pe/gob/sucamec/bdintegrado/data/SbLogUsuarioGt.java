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
@Table(name = "SB_LOG_USUARIO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbLogUsuarioGt.findAll", query = "SELECT s FROM SbLogUsuarioGt s"),
    @NamedQuery(name = "SbLogUsuarioGt.findById", query = "SELECT s FROM SbLogUsuarioGt s WHERE s.id = :id"),
    @NamedQuery(name = "SbLogUsuarioGt.findByTabla", query = "SELECT s FROM SbLogUsuarioGt s WHERE s.tabla = :tabla"),
    @NamedQuery(name = "SbLogUsuarioGt.findByRegistro", query = "SELECT s FROM SbLogUsuarioGt s WHERE s.registro = :registro"),
    @NamedQuery(name = "SbLogUsuarioGt.findByDatos", query = "SELECT s FROM SbLogUsuarioGt s WHERE s.datos = :datos"),
    @NamedQuery(name = "SbLogUsuarioGt.findByFechaHora", query = "SELECT s FROM SbLogUsuarioGt s WHERE s.fechaHora = :fechaHora"),
    @NamedQuery(name = "SbLogUsuarioGt.findByAudLogin", query = "SELECT s FROM SbLogUsuarioGt s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbLogUsuarioGt.findByAudNumIp", query = "SELECT s FROM SbLogUsuarioGt s WHERE s.audNumIp = :audNumIp"),
    @NamedQuery(name = "SbLogUsuarioGt.findByPersonaId", query = "SELECT s FROM SbLogUsuarioGt s WHERE s.personaId = :personaId")})
public class SbLogUsuarioGt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 100)
    @Column(name = "TABLA")
    private String tabla;
    @Column(name = "REGISTRO")
    private Long registro;
    @Size(max = 500)
    @Column(name = "DATOS")
    private String datos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
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
    @Column(name = "PERSONA_ID")
    private Long personaId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoId;

    public SbLogUsuarioGt() {
    }

    public SbLogUsuarioGt(Long id) {
        this.id = id;
    }

    public SbLogUsuarioGt(Long id, Date fechaHora, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public Long getRegistro() {
        return registro;
    }

    public void setRegistro(Long registro) {
        this.registro = registro;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
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

    public Long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }

    public TipoBaseGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBaseGt tipoId) {
        this.tipoId = tipoId;
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
        if (!(object instanceof SbLogUsuarioGt)) {
            return false;
        }
        SbLogUsuarioGt other = (SbLogUsuarioGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.SbLogUsuario[ id=" + id + " ]";
    }
    
}
