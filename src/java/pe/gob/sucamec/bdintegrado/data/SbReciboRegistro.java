/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_RECIBO_REGISTRO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbReciboRegistro.findAll", query = "SELECT s FROM SbReciboRegistro s"),
    @NamedQuery(name = "SbReciboRegistro.findById", query = "SELECT s FROM SbReciboRegistro s WHERE s.id = :id"),
    @NamedQuery(name = "SbReciboRegistro.findByNroExpediente", query = "SELECT s FROM SbReciboRegistro s WHERE s.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "SbReciboRegistro.findByActivo", query = "SELECT s FROM SbReciboRegistro s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbReciboRegistro.findByAudLogin", query = "SELECT s FROM SbReciboRegistro s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbReciboRegistro.findByAudNumIp", query = "SELECT s FROM SbReciboRegistro s WHERE s.audNumIp = :audNumIp")})
public class SbReciboRegistro implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_RECIBO_REGISTRO")
    @SequenceGenerator(name = "SEQ_SB_RECIBO_REGISTRO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_RECIBO_REGISTRO", allocationSize = 1)
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
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "RECIBO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbRecibos reciboId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppRegistro registroId;


    public SbReciboRegistro() {
    }

    public SbReciboRegistro(Long id) {
        this.id = id;
    }

    public SbReciboRegistro(Long id, String nroExpediente, short activo, String audLogin, String audNumIp) {
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

    public SbRecibos getReciboId() {
        return reciboId;
    }

    public void setReciboId(SbRecibos reciboId) {
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
        if (!(object instanceof SbReciboRegistro)) {
            return false;
        }
        SbReciboRegistro other = (SbReciboRegistro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbReciboRegistro[ id=" + id + " ]";
    }
    
    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }
}
