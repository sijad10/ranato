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
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_GUIA_TRANSITO_VEHICULO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppGuiaTransitoVehiculo.findAll", query = "SELECT e FROM EppGuiaTransitoVehiculo e"),
    @NamedQuery(name = "EppGuiaTransitoVehiculo.findById", query = "SELECT e FROM EppGuiaTransitoVehiculo e WHERE e.id = :id"),
    @NamedQuery(name = "EppGuiaTransitoVehiculo.findByPlaca", query = "SELECT e FROM EppGuiaTransitoVehiculo e WHERE e.placa = :placa"),
    @NamedQuery(name = "EppGuiaTransitoVehiculo.findByMarca", query = "SELECT e FROM EppGuiaTransitoVehiculo e WHERE e.marca = :marca"),
    @NamedQuery(name = "EppGuiaTransitoVehiculo.findByAnio", query = "SELECT e FROM EppGuiaTransitoVehiculo e WHERE e.anio = :anio"),
    @NamedQuery(name = "EppGuiaTransitoVehiculo.findByActivo", query = "SELECT e FROM EppGuiaTransitoVehiculo e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppGuiaTransitoVehiculo.findByAudLogin", query = "SELECT e FROM EppGuiaTransitoVehiculo e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppGuiaTransitoVehiculo.findByAudNumIp", query = "SELECT e FROM EppGuiaTransitoVehiculo e WHERE e.audNumIp = :audNumIp")})
public class EppGuiaTransitoVehiculo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_GUIA_TRANSITO_VEHICULO")
    @SequenceGenerator(name = "SEQ_EPP_GUIA_TRANSITO_VEHICULO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_GUIA_TRANSITO_VEHICULO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "PLACA")
    private String placa;
    @Size(max = 50)
    @Column(name = "MARCA")
    private String marca;
    @Column(name = "ANIO")
    private Long anio;
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
    @ManyToMany(mappedBy = "eppGuiaTransitoVehiculoList")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList;
    @ManyToMany(mappedBy = "eppGuiaTransitoVehiculoList")
    private List<EppRegistro> eppRegistroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "guiaTransitoVehiculoId")
    private List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList;

    public EppGuiaTransitoVehiculo() {
    }

    public EppGuiaTransitoVehiculo(Long id) {
        this.id = id;
    }

    public EppGuiaTransitoVehiculo(Long id, String placa, short activo, String audLogin, String audNumIp) {
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

    public Long getAnio() {
        return anio;
    }

    public void setAnio(Long anio) {
        this.anio = anio;
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

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList() {
        return eppRegistroGuiaTransitoList;
    }

    public void setEppRegistroGuiaTransitoList(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList) {
        this.eppRegistroGuiaTransitoList = eppRegistroGuiaTransitoList;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    @XmlTransient
    public List<EppGuiaTransitoPiro> getEppGuiaTransitoPiroList() {
        return eppGuiaTransitoPiroList;
    }

    public void setEppGuiaTransitoPiroList(List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList) {
        this.eppGuiaTransitoPiroList = eppGuiaTransitoPiroList;
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
        if (!(object instanceof EppGuiaTransitoVehiculo)) {
            return false;
        }
        EppGuiaTransitoVehiculo other = (EppGuiaTransitoVehiculo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppGuiaTransitoVehiculo[ id=" + id + " ]";
    }
    
}
