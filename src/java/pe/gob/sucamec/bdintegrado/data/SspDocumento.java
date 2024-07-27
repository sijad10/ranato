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
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_DOCUMENTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspDocumento.findAll", query = "SELECT s FROM SspDocumento s"),
    @NamedQuery(name = "SspDocumento.findById", query = "SELECT s FROM SspDocumento s WHERE s.id = :id"),
    @NamedQuery(name = "SspDocumento.findByNombre", query = "SELECT s FROM SspDocumento s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SspDocumento.findByActivo", query = "SELECT s FROM SspDocumento s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspDocumento.findByAudLogin", query = "SELECT s FROM SspDocumento s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspDocumento.findByAudNumIp", query = "SELECT s FROM SspDocumento s WHERE s.audNumIp = :audNumIp")})
public class SspDocumento implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_DOCUMENTO")
    @SequenceGenerator(name = "SEQ_SSP_DOCUMENTO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_DOCUMENTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
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
    @JoinColumn(name = "CARTERA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspCarteraCliente cartera;

    public SspDocumento() {
    }

    public SspDocumento(Long id) {
        this.id = id;
    }

    public SspDocumento(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public SspCarteraCliente getCartera() {
        return cartera;
    }

    public void setCartera(SspCarteraCliente cartera) {
        this.cartera = cartera;
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
        if (!(object instanceof SspDocumento)) {
            return false;
        }
        SspDocumento other = (SspDocumento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspDocumento[ id=" + id + " ]";
    }
    
}
