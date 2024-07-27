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
@Table(name = "SB_PAIS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbPaisGt.findAll", query = "SELECT s FROM SbPaisGt s order by s.nombre asc"),
    @NamedQuery(name = "SbPaisGt.findById", query = "SELECT s FROM SbPaisGt s WHERE s.id = :id"),
    @NamedQuery(name = "SbPaisGt.findByNombre", query = "SELECT s FROM SbPaisGt s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbPaisGt.findByActivo", query = "SELECT s FROM SbPaisGt s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbPaisGt.findByAudLogin", query = "SELECT s FROM SbPaisGt s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbPaisGt.findByAudNumIp", query = "SELECT s FROM SbPaisGt s WHERE s.audNumIp = :audNumIp")})
public class SbPaisGt implements Serializable {

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
    @ManyToMany(mappedBy = "sbPaisList")
    private List<EppRegistro> eppRegistroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paisOrigDestId")
    private List<EppRegistroIntSal> eppRegistroIntSalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paisId")
    private List<EppProveedorCliente> eppProveedorClienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paisOrigDestId")
    private List<EppInternaSalida> eppInternaSalidaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paisId")
    private List<SbEmpresaExtranjera> sbEmpresaExtranjeraList;
    
    public SbPaisGt() {
    }

    public SbPaisGt(Long id) {
        this.id = id;
    }

    public SbPaisGt(Long id, String nombre, short activo, String audLogin, String audNumIp) {
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
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    @XmlTransient
    public List<EppRegistroIntSal> getEppRegistroIntSalList() {
        return eppRegistroIntSalList;
    }

    public void setEppRegistroIntSalList(List<EppRegistroIntSal> eppRegistroIntSalList) {
        this.eppRegistroIntSalList = eppRegistroIntSalList;
    }

    @XmlTransient
    public List<EppProveedorCliente> getEppProveedorClienteList() {
        return eppProveedorClienteList;
    }

    public void setEppProveedorClienteList(List<EppProveedorCliente> eppProveedorClienteList) {
        this.eppProveedorClienteList = eppProveedorClienteList;
    }

    @XmlTransient
    public List<EppInternaSalida> getEppInternaSalidaList() {
        return eppInternaSalidaList;
    }

    public void setEppInternaSalidaList(List<EppInternaSalida> eppInternaSalidaList) {
        this.eppInternaSalidaList = eppInternaSalidaList;
    }
    
    @XmlTransient
    public List<SbEmpresaExtranjera> getSbEmpresaExtranjeraList() {
        return sbEmpresaExtranjeraList;
    }

    public void setSbEmpresaExtranjeraList(List<SbEmpresaExtranjera> sbEmpresaExtranjeraList) {
        this.sbEmpresaExtranjeraList = sbEmpresaExtranjeraList;
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
        if (!(object instanceof SbPaisGt)) {
            return false;
        }
        SbPaisGt other = (SbPaisGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.SbPais[ id=" + id + " ]";
    }
    
}
