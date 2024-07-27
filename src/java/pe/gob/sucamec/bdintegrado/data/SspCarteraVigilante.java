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
import javax.persistence.EmbeddedId;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "SSP_CARTERA_VIGILANTE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspCarteraVigilante.findAll", query = "SELECT s FROM SspCarteraVigilante s"),
    @NamedQuery(name = "SspCarteraVigilante.findById", query = "SELECT s FROM SspCarteraVigilante s WHERE s.id = :id"),
    @NamedQuery(name = "SspCarteraVigilante.findByActualizacion", query = "SELECT s FROM SspCarteraVigilante s WHERE s.actualizacion = :actualizacion"),
    @NamedQuery(name = "SspCarteraVigilante.findByObservacion", query = "SELECT s FROM SspCarteraVigilante s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SspCarteraVigilante.findByActivo", query = "SELECT s FROM SspCarteraVigilante s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspCarteraVigilante.findByAudLogin", query = "SELECT s FROM SspCarteraVigilante s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspCarteraVigilante.findByAudNumIp", query = "SELECT s FROM SspCarteraVigilante s WHERE s.audNumIp = :audNumIp")})

public class SspCarteraVigilante implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_CARTERA_VIGILANTE")
    @SequenceGenerator(name = "SEQ_SSP_CARTERA_VIGILANTE", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_CARTERA_VIGILANTE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "ACTUALIZACION")
    private Character actualizacion;
    @Size(max = 400)
    @Column(name = "OBSERVACION")
    private String observacion;
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
    private SspCarteraCliente carteraId;
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
    @JoinColumn(name = "CARNE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SspCarne carneId;

    public SspCarteraVigilante() {
    }

    public SspCarteraVigilante(Long id) {
        this.id = id;
    }

    public SspCarteraVigilante(Long id, short activo, String audLogin, String audNumIp) {
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

    public Character getActualizacion() {
        return actualizacion;
    }

    public void setActualizacion(Character actualizacion) {
        this.actualizacion = actualizacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public SspCarteraCliente getCarteraId() {
        return carteraId;
    }

    public void setCarteraId(SspCarteraCliente carteraId) {
        this.carteraId = carteraId;
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
        if (!(object instanceof SspCarteraVigilante)) {
            return false;
        }
        SspCarteraVigilante other = (SspCarteraVigilante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspCarteraVigilante[ id=" + id + " ]";
    }
    
    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    public SspCarne getCarneId() {
        return carneId;
    }

    public void setCarneId(SspCarne carneId) {
        this.carneId = carneId;
    }
}
