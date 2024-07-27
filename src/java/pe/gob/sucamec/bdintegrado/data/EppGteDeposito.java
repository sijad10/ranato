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
@Table(name = "EPP_GTE_DEPOSITO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppGteDeposito.findAll", query = "SELECT e FROM EppGteDeposito e"),
    @NamedQuery(name = "EppGteDeposito.findById", query = "SELECT e FROM EppGteDeposito e WHERE e.id = :id"),
    @NamedQuery(name = "EppGteDeposito.findByNroDeposito", query = "SELECT e FROM EppGteDeposito e WHERE e.nroDeposito = :nroDeposito"),
    @NamedQuery(name = "EppGteDeposito.findByFechaDeposito", query = "SELECT e FROM EppGteDeposito e WHERE e.fechaDeposito = :fechaDeposito"),
    @NamedQuery(name = "EppGteDeposito.findByImagenDeposito", query = "SELECT e FROM EppGteDeposito e WHERE e.imagenDeposito = :imagenDeposito"),
    @NamedQuery(name = "EppGteDeposito.findByActivo", query = "SELECT e FROM EppGteDeposito e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppGteDeposito.findByAudLogin", query = "SELECT e FROM EppGteDeposito e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppGteDeposito.findByAudNumIp", query = "SELECT e FROM EppGteDeposito e WHERE e.audNumIp = :audNumIp")})
public class EppGteDeposito implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_GTE_DEPOSITO")
    @SequenceGenerator(name = "SEQ_EPP_GTE_DEPOSITO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_GTE_DEPOSITO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_DEPOSITO")
    private Long nroDeposito;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MONTO_DEPOSITO")
    private Double montoDeposito;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_DEPOSITO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDeposito;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "IMAGEN_DEPOSITO")
    private String imagenDeposito;
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
    @JoinColumn(name = "GTE_REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppGteRegistro gteRegistroId;

    public EppGteDeposito() {
    }

    public EppGteDeposito(Long id) {
        this.id = id;
    }

    public EppGteDeposito(Long id, Long nroDeposito, Double montoDeposito, Date fechaDeposito, String imagenDeposito, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroDeposito = nroDeposito;
        this.montoDeposito = montoDeposito;
        this.fechaDeposito = fechaDeposito;
        this.imagenDeposito = imagenDeposito;
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

    public Long getNroDeposito() {
        return nroDeposito;
    }

    public void setNroDeposito(Long nroDeposito) {
        this.nroDeposito = nroDeposito;
    }

    public Double getMontoDeposito() {
        return montoDeposito;
    }

    public void setMontoDeposito(Double montoDeposito) {
        this.montoDeposito = montoDeposito;
    }

    public Date getFechaDeposito() {
        return fechaDeposito;
    }

    public void setFechaDeposito(Date fechaDeposito) {
        this.fechaDeposito = fechaDeposito;
    }

    public String getImagenDeposito() {
        return imagenDeposito;
    }

    public void setImagenDeposito(String imagenDeposito) {
        this.imagenDeposito = imagenDeposito;
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

    public EppGteRegistro getGteRegistroId() {
        return gteRegistroId;
    }

    public void setGteRegistroId(EppGteRegistro gteRegistroId) {
        this.gteRegistroId = gteRegistroId;
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
        if (!(object instanceof EppGteDeposito)) {
            return false;
        }
        EppGteDeposito other = (EppGteDeposito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.EppGteDeposito[ id=" + id + " ]";
    }
    
}
