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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_VEHICULO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspVehiculo.findAll", query = "SELECT s FROM SspVehiculo s"),
    @NamedQuery(name = "SspVehiculo.findById", query = "SELECT s FROM SspVehiculo s WHERE s.id = :id"),
    @NamedQuery(name = "SspVehiculo.findByPlaca", query = "SELECT s FROM SspVehiculo s WHERE s.placa = :placa"),
    @NamedQuery(name = "SspVehiculo.findByMarca", query = "SELECT s FROM SspVehiculo s WHERE s.marca = :marca"),
    @NamedQuery(name = "SspVehiculo.findByAnnio", query = "SELECT s FROM SspVehiculo s WHERE s.annio = :annio"),
    @NamedQuery(name = "SspVehiculo.findByTarjetaPropiedad", query = "SELECT s FROM SspVehiculo s WHERE s.tarjetaPropiedad = :tarjetaPropiedad"),
    @NamedQuery(name = "SspVehiculo.findByCertificado", query = "SELECT s FROM SspVehiculo s WHERE s.certificado = :certificado"),
    @NamedQuery(name = "SspVehiculo.findByActivo", query = "SELECT s FROM SspVehiculo s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspVehiculo.findByAudLogin", query = "SELECT s FROM SspVehiculo s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspVehiculo.findByAudNumIp", query = "SELECT s FROM SspVehiculo s WHERE s.audNumIp = :audNumIp")})
public class SspVehiculo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_VEHICULO")
    @SequenceGenerator(name = "SEQ_SSP_VEHICULO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_VEHICULO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PLACA")
    private String placa;
    @Size(max = 50)
    @Column(name = "MARCA")
    private String marca;
    @Column(name = "ANNIO")
    private Long annio;
    @Size(max = 20)
    @Column(name = "TARJETA_PROPIEDAD")
    private String tarjetaPropiedad;
    @Size(max = 50)
    @Column(name = "CERTIFICADO")
    private String certificado;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehiculoId")
    private List<SspCarteraVehiculo> sspCarteraVehiculoList;

    public SspVehiculo() {
    }

    public SspVehiculo(Long id) {
        this.id = id;
    }

    public SspVehiculo(Long id, String placa, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.placa = placa;
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

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Long getAnnio() {
        return annio;
    }

    public void setAnnio(Long annio) {
        this.annio = annio;
    }

    public String getTarjetaPropiedad() {
        return tarjetaPropiedad;
    }

    public void setTarjetaPropiedad(String tarjetaPropiedad) {
        this.tarjetaPropiedad = tarjetaPropiedad;
    }

    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
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
        if (!(object instanceof SspVehiculo)) {
            return false;
        }
        SspVehiculo other = (SspVehiculo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspVehiculo[ id=" + id + " ]";
    }
    
    @XmlTransient
    public List<SspCarteraVehiculo> getSspCarteraVehiculoList() {
        return sspCarteraVehiculoList;
    }

    public void setSspCarteraVehiculoList(List<SspCarteraVehiculo> sspCarteraVehiculoList) {
        this.sspCarteraVehiculoList = sspCarteraVehiculoList;
    }
    
}
