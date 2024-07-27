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
 * @author mpalomino
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_VEHICULO_DINERO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspVehiculoDinero.findAll", query = "SELECT s FROM SspVehiculoDinero s"),
    @NamedQuery(name = "SspVehiculoDinero.findById", query = "SELECT s FROM SspVehiculoDinero s WHERE s.id = :id"),
    @NamedQuery(name = "SspVehiculoDinero.findByCategoria", query = "SELECT s FROM SspVehiculoDinero s WHERE s.categoria = :categoria"),
    @NamedQuery(name = "SspVehiculoDinero.findByCarroceria", query = "SELECT s FROM SspVehiculoDinero s WHERE s.carroceria = :carroceria"),
    @NamedQuery(name = "SspVehiculoDinero.findByPlaca", query = "SELECT s FROM SspVehiculoDinero s WHERE s.placa = :placa"),
    @NamedQuery(name = "SspVehiculoDinero.findByArchivoRegistral", query = "SELECT s FROM SspVehiculoDinero s WHERE s.archivoRegistral = :archivoRegistral"),
    @NamedQuery(name = "SspVehiculoDinero.findByArrendado", query = "SELECT s FROM SspVehiculoDinero s WHERE s.arrendado = :arrendado"),
    @NamedQuery(name = "SspVehiculoDinero.findByArchivoArrendamiento", query = "SELECT s FROM SspVehiculoDinero s WHERE s.archivoArrendamiento = :archivoArrendamiento"),
    @NamedQuery(name = "SspVehiculoDinero.findByActivo", query = "SELECT s FROM SspVehiculoDinero s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspVehiculoDinero.findByAudLogin", query = "SELECT s FROM SspVehiculoDinero s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspVehiculoDinero.findByAudNumIp", query = "SELECT s FROM SspVehiculoDinero s WHERE s.audNumIp = :audNumIp")})
public class SspVehiculoDinero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_VEHICULO_DINERO")
    @SequenceGenerator(name = "SEQ_SSP_VEHICULO_DINERO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_VEHICULO_DINERO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CATEGORIA")
    private String categoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "CARROCERIA")
    private String carroceria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "PLACA")
    private String placa;
    @Size(max = 200)
    @Column(name = "ARCHIVO_REGISTRAL")
    private String archivoRegistral;
    @Column(name = "ARRENDADO")
    private Short arrendado;
    @Size(max = 200)
    @Column(name = "ARCHIVO_ARRENDAMIENTO")
    private String archivoArrendamiento;
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
    @JoinColumn(name = "INFORMACION_REGISTRAL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbAsientoSunarp informacionRegistral;
    @Size(max = 200)
    @Column(name = "DIRECCION")
    private String direccion;
    @JoinColumn(name = "DISTRITO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistritoGt distrito;

    public SspVehiculoDinero() {
    }

    public SspVehiculoDinero(Long id) {
        this.id = id;
    }

    public SspVehiculoDinero(Long id, String categoria, String carroceria, String placa, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.categoria = categoria;
        this.carroceria = carroceria;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCarroceria() {
        return carroceria;
    }

    public void setCarroceria(String carroceria) {
        this.carroceria = carroceria;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getArchivoRegistral() {
        return archivoRegistral;
    }

    public void setArchivoRegistral(String archivoRegistral) {
        this.archivoRegistral = archivoRegistral;
    }

    public Short getArrendado() {
        return arrendado;
    }

    public void setArrendado(Short arrendado) {
        this.arrendado = arrendado;
    }

    public String getArchivoArrendamiento() {
        return archivoArrendamiento;
    }

    public void setArchivoArrendamiento(String archivoArrendamiento) {
        this.archivoArrendamiento = archivoArrendamiento;
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
        if (!(object instanceof SspVehiculoDinero)) {
            return false;
        }
        SspVehiculoDinero other = (SspVehiculoDinero) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspVehiculoDinero[ id=" + id + " ]";
    }

    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
    }

    public SbAsientoSunarp getInformacionRegistral() {
        return informacionRegistral;
    }

    public void setInformacionRegistral(SbAsientoSunarp informacionRegistral) {
        this.informacionRegistral = informacionRegistral;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public SbDistritoGt getDistrito() {
        return distrito;
    }

    public void setDistrito(SbDistritoGt distrito) {
        this.distrito = distrito;
    }
    
}
