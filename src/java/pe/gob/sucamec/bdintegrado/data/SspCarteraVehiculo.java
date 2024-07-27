/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "SSP_CARTERA_VEHICULO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspCarteraVehiculo.findAll", query = "SELECT s FROM SspCarteraVehiculo s"),
    @NamedQuery(name = "SspCarteraVehiculo.findById", query = "SELECT s FROM SspCarteraVehiculo s WHERE s.id = :id"),
    @NamedQuery(name = "SspCarteraVehiculo.findByActualizacion", query = "SELECT s FROM SspCarteraVehiculo s WHERE s.actualizacion = :actualizacion"),
    @NamedQuery(name = "SspCarteraVehiculo.findByObservacion", query = "SELECT s FROM SspCarteraVehiculo s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SspCarteraVehiculo.findByActivo", query = "SELECT s FROM SspCarteraVehiculo s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspCarteraVehiculo.findByAudLogin", query = "SELECT s FROM SspCarteraVehiculo s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspCarteraVehiculo.findByAudNumIp", query = "SELECT s FROM SspCarteraVehiculo s WHERE s.audNumIp = :audNumIp")})
public class SspCarteraVehiculo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_CARTERA_VEHICULO")
    @SequenceGenerator(name = "SEQ_SSP_CARTERA_VEHICULO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_CARTERA_VEHICULO", allocationSize = 1)
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
    @JoinColumn(name = "VEHICULO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspVehiculo vehiculoId;
    @JoinColumn(name = "CARTERA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspCarteraCliente carteraId;
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;

    public SspCarteraVehiculo() {
    }

    public SspCarteraVehiculo(Long id) {
        this.id = id;
    }

    public SspCarteraVehiculo(Long id, short activo, String audLogin, String audNumIp) {
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

    public SspVehiculo getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(SspVehiculo vehiculoId) {
        this.vehiculoId = vehiculoId;
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
        if (!(object instanceof SspCarteraVehiculo)) {
            return false;
        }
        SspCarteraVehiculo other = (SspCarteraVehiculo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspCarteraVehiculo[ id=" + id + " ]";
    }
    
    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
