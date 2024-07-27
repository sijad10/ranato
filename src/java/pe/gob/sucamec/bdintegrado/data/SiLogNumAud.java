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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SI_LOG_NUM_AUD", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SiLogNumAud.findAll", query = "SELECT l FROM SiLogNumAud l"),
    @NamedQuery(name = "SiLogNumAud.findById", query = "SELECT l FROM SiLogNumAud l WHERE l.id = :id"),
    @NamedQuery(name = "SiLogNumAud.findByTabla", query = "SELECT l FROM SiLogNumAud l WHERE l.tabla = :tabla"),
    @NamedQuery(name = "SiLogNumAud.findByRegId", query = "SELECT l FROM SiLogNumAud l WHERE l.regId = :regId"),
    @NamedQuery(name = "SiLogNumAud.findByAccion", query = "SELECT l FROM SiLogNumAud l WHERE l.accion = :accion"),
    @NamedQuery(name = "SiLogNumAud.findByCampos", query = "SELECT l FROM SiLogNumAud l WHERE l.campos = :campos"),
    @NamedQuery(name = "SiLogNumAud.findByData", query = "SELECT l FROM SiLogNumAud l WHERE l.data = :data"),
    @NamedQuery(name = "SiLogNumAud.findByTimestamp", query = "SELECT l FROM SiLogNumAud l WHERE l.timestamp = :timestamp"),
    @NamedQuery(name = "SiLogNumAud.findByAudLogin", query = "SELECT l FROM SiLogNumAud l WHERE l.audLogin = :audLogin"),
    @NamedQuery(name = "SiLogNumAud.findByAudNumIp", query = "SELECT l FROM SiLogNumAud l WHERE l.audNumIp = :audNumIp")})
public class SiLogNumAud implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LOG")
    @SequenceGenerator(name = "SEQ_SI_LOG_NUM_AUD", schema = "BDINTEGRADO", sequenceName = "SEQ_SI_LOG_NUM_AUD", allocationSize = 1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TABLA")
    private String tabla;
    @Basic(optional = false)
    @NotNull
    @Column(name = "REG_ID")
    private long regId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ACCION")
    private String accion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "CAMPOS")
    private String campos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "DATA")
    private String data;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;

    public SiLogNumAud() {
    }

    public SiLogNumAud(Long id) {
        this.id = id;
    }

    public SiLogNumAud(Long id, String tabla, long regId, String accion, String campos, String data, Date timestamp, String audLogin, String audNumIp) {
        this.id = id;
        this.tabla = tabla;
        this.regId = regId;
        this.accion = accion;
        this.campos = campos;
        this.data = data;
        this.timestamp = timestamp;
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

    public long getRegId() {
        return regId;
    }

    public void setRegId(long regId) {
        this.regId = regId;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getCampos() {
        return campos;
    }

    public void setCampos(String campos) {
        this.campos = campos;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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
        if (!(object instanceof SiLogNumAud)) {
            return false;
        }
        SiLogNumAud other = (SiLogNumAud) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.si.data.Log[ id=" + id + " ]";
    }

}
