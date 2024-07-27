/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "SB_DISTRITO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbDistritoGt.findAll", query = "SELECT s FROM SbDistritoGt s"),
    @NamedQuery(name = "SbDistritoGt.findById", query = "SELECT s FROM SbDistritoGt s WHERE s.id = :id"),
    @NamedQuery(name = "SbDistritoGt.findByNombre", query = "SELECT s FROM SbDistritoGt s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbDistritoGt.findByActivo", query = "SELECT s FROM SbDistritoGt s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbDistritoGt.findByAudLogin", query = "SELECT s FROM SbDistritoGt s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbDistritoGt.findByAudNumIp", query = "SELECT s FROM SbDistritoGt s WHERE s.audNumIp = :audNumIp")})
public class SbDistritoGt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "NOMBRE")
    private String nombre;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distritoId")
    private List<SbDireccionGt> sbDireccionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ubigeoId")
    private List<EppAlmacenAduanero> eppAlmacenAduaneroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distritoId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distId")
    private List<EppLugarUsoUbigeo> eppLugarUsoUbigeoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ubigeoId")
    private List<EppPuertoAduanero> eppPuertoAduaneroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ubigeoId")
    private List<EppVehiculoDirecs> eppVehiculoDirecsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ubigeoId")
    private List<EppCapacitacion> eppCapacitacionList;
    @JoinColumn(name = "PROVINCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbProvinciaGt provinciaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ubigeoId")
    private List<EppLocal> eppLocalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ubigeoId")
    private List<EppAlmacen> eppAlmacenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distritoId")
    private List<EppPolvorin> eppPolvorinList;
    
    public SbDistritoGt() {
    }

    public SbDistritoGt(Long id) {
        this.id = id;
    }

    public SbDistritoGt(Long id, String nombre, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
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

    @XmlTransient
    public List<SbDireccionGt> getSbDireccionList() {
        return sbDireccionList;
    }

    public void setSbDireccionList(List<SbDireccionGt> sbDireccionList) {
        this.sbDireccionList = sbDireccionList;
    }

    @XmlTransient
    public List<EppAlmacenAduanero> getEppAlmacenAduaneroList() {
        return eppAlmacenAduaneroList;
    }

    public void setEppAlmacenAduaneroList(List<EppAlmacenAduanero> eppAlmacenAduaneroList) {
        this.eppAlmacenAduaneroList = eppAlmacenAduaneroList;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList() {
        return eppInspeccionPolvorinList;
    }

    public void setEppInspeccionPolvorinList(List<EppInspeccionPolvorin> eppInspeccionPolvorinList) {
        this.eppInspeccionPolvorinList = eppInspeccionPolvorinList;
    }

    @XmlTransient
    public List<EppLugarUsoUbigeo> getEppLugarUsoUbigeoList() {
        return eppLugarUsoUbigeoList;
    }

    public void setEppLugarUsoUbigeoList(List<EppLugarUsoUbigeo> eppLugarUsoUbigeoList) {
        this.eppLugarUsoUbigeoList = eppLugarUsoUbigeoList;
    }

    @XmlTransient
    public List<EppPuertoAduanero> getEppPuertoAduaneroList() {
        return eppPuertoAduaneroList;
    }

    public void setEppPuertoAduaneroList(List<EppPuertoAduanero> eppPuertoAduaneroList) {
        this.eppPuertoAduaneroList = eppPuertoAduaneroList;
    }

    @XmlTransient
    public List<EppVehiculoDirecs> getEppVehiculoDirecsList() {
        return eppVehiculoDirecsList;
    }

    public void setEppVehiculoDirecsList(List<EppVehiculoDirecs> eppVehiculoDirecsList) {
        this.eppVehiculoDirecsList = eppVehiculoDirecsList;
    }

    @XmlTransient
    public List<EppCapacitacion> getEppCapacitacionList() {
        return eppCapacitacionList;
    }

    public void setEppCapacitacionList(List<EppCapacitacion> eppCapacitacionList) {
        this.eppCapacitacionList = eppCapacitacionList;
    }

    public SbProvinciaGt getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(SbProvinciaGt provinciaId) {
        this.provinciaId = provinciaId;
    }

    @XmlTransient
    public List<EppLocal> getEppLocalList() {
        return eppLocalList;
    }

    public void setEppLocalList(List<EppLocal> eppLocalList) {
        this.eppLocalList = eppLocalList;
    }

    @XmlTransient
    public List<EppAlmacen> getEppAlmacenList() {
        return eppAlmacenList;
    }

    public void setEppAlmacenList(List<EppAlmacen> eppAlmacenList) {
        this.eppAlmacenList = eppAlmacenList;
    }

    @XmlTransient
    public List<EppPolvorin> getEppPolvorinList() {
        return eppPolvorinList;
    }

    public void setEppPolvorinList(List<EppPolvorin> eppPolvorinList) {
        this.eppPolvorinList = eppPolvorinList;
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
        if (!(object instanceof SbDistritoGt)) {
            return false;
        }
        SbDistritoGt other = (SbDistritoGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.SbDistritoGt[ id=" + id + " ]";
    }

    public String getNombreCompleto() {
        return getProvinciaId().getDepartamentoId().getNombre() + " / " + getProvinciaId().getNombre() + " / " + getNombre();

    }

}
