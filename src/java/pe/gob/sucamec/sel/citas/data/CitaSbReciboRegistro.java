/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author msalinas
 */
@Entity
@Table(name = "SB_RECIBO_REGISTRO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaSbReciboRegistro.findAll", query = "SELECT s FROM CitaSbReciboRegistro s"),
    @NamedQuery(name = "CitaSbReciboRegistro.findById", query = "SELECT s FROM CitaSbReciboRegistro s WHERE s.id = :id"),
    @NamedQuery(name = "CitaSbReciboRegistro.findByNroExpediente", query = "SELECT s FROM CitaSbReciboRegistro s WHERE s.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "CitaSbReciboRegistro.findByActivo", query = "SELECT s FROM CitaSbReciboRegistro s WHERE s.activo = :activo"),
    @NamedQuery(name = "CitaSbReciboRegistro.findByAudLogin", query = "SELECT s FROM CitaSbReciboRegistro s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "CitaSbReciboRegistro.findByAudNumIp", query = "SELECT s FROM CitaSbReciboRegistro s WHERE s.audNumIp = :audNumIp"),
    @NamedQuery(name = "CitaSbReciboRegistro.findByRegistroId", query = "SELECT s FROM CitaSbReciboRegistro s WHERE s.registroId = :registroId")})
public class CitaSbReciboRegistro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
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
    @Column(name = "REGISTRO_ID")
    private Long registroId;
    @JoinColumn(name = "RECIBO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaSbRecibos reciboId;

    public CitaSbReciboRegistro() {
    }

    public CitaSbReciboRegistro(Long id) {
        this.id = id;
    }

    public CitaSbReciboRegistro(Long id, String nroExpediente, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroExpediente = nroExpediente;
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

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
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

    public Long getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Long registroId) {
        this.registroId = registroId;
    }

    public CitaSbRecibos getReciboId() {
        return reciboId;
    }

    public void setReciboId(CitaSbRecibos reciboId) {
        this.reciboId = reciboId;
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
        if (!(object instanceof CitaSbReciboRegistro)) {
            return false;
        }
        CitaSbReciboRegistro other = (CitaSbReciboRegistro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.CitaSbReciboRegistro[ id=" + id + " ]";
    }
    
}
