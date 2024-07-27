/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.sel.epp.data.Almacen;
import pe.gob.sucamec.sel.epp.data.AlmacenAduanero;
import pe.gob.sucamec.sel.epp.data.LugarUsoUbigeo;
import pe.gob.sucamec.sel.epp.data.Polvorin;
import pe.gob.sucamec.sel.epp.data.PuertoAduanero;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_DISTRITO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbDistrito.findAll", query = "SELECT s FROM SbDistrito s"),
    @NamedQuery(name = "SbDistrito.findById", query = "SELECT s FROM SbDistrito s WHERE s.id = :id"),
    @NamedQuery(name = "SbDistrito.findByNombre", query = "SELECT s FROM SbDistrito s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbDistrito.findByActivo", query = "SELECT s FROM SbDistrito s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbDistrito.findByAudLogin", query = "SELECT s FROM SbDistrito s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbDistrito.findByAudNumIp", query = "SELECT s FROM SbDistrito s WHERE s.audNumIp = :audNumIp")})
public class SbDistrito implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distritoId")
    private List<Polvorin> polvorinList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ubigeoId")
    private List<PuertoAduanero> puertoAduaneroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ubigeoId")
    private List<Almacen> almacenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ubigeoId")
    private List<AlmacenAduanero> almacenAduaneroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distId")
    private List<LugarUsoUbigeo> lugarUsoUbigeoList;
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_DISTRITO")
    @SequenceGenerator(name = "SEQ_SB_DISTRITO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_DISTRITO", allocationSize = 1)
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
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distritoId")
    private List<SbDireccion> sbDireccionList;
    @JoinColumn(name = "PROVINCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbProvincia provinciaId;

    public SbDistrito() {
    }

    public SbDistrito(Long id) {
        this.id = id;
    }

    public SbDistrito(Long id, String nombre, short activo, String audLogin, String audNumIp) {
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
    public List<SbDireccion> getSbDireccionList() {
        return sbDireccionList;
    }

    public void setSbDireccionList(List<SbDireccion> sbDireccionList) {
        this.sbDireccionList = sbDireccionList;
    }

    public SbProvincia getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(SbProvincia provinciaId) {
        this.provinciaId = provinciaId;
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
        if (!(object instanceof SbDistrito)) {
            return false;
        }
        SbDistrito other = (SbDistrito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbDistrito[ id=" + id + " ]";
    }

    @XmlTransient
    public List<Polvorin> getPolvorinList() {
        return polvorinList;
    }

    public void setPolvorinList(List<Polvorin> polvorinList) {
        this.polvorinList = polvorinList;
    }

    @XmlTransient
    public List<PuertoAduanero> getPuertoAduaneroList() {
        return puertoAduaneroList;
    }

    public void setPuertoAduaneroList(List<PuertoAduanero> puertoAduaneroList) {
        this.puertoAduaneroList = puertoAduaneroList;
    }

    @XmlTransient
    public List<Almacen> getAlmacenList() {
        return almacenList;
    }

    public void setAlmacenList(List<Almacen> almacenList) {
        this.almacenList = almacenList;
    }

    @XmlTransient
    public List<AlmacenAduanero> getAlmacenAduaneroList() {
        return almacenAduaneroList;
    }

    public void setAlmacenAduaneroList(List<AlmacenAduanero> almacenAduaneroList) {
        this.almacenAduaneroList = almacenAduaneroList;
    }

    @XmlTransient
    public List<LugarUsoUbigeo> getLugarUsoUbigeoList() {
        return lugarUsoUbigeoList;
    }

    public void setLugarUsoUbigeoList(List<LugarUsoUbigeo> lugarUsoUbigeoList) {
        this.lugarUsoUbigeoList = lugarUsoUbigeoList;
    }
    
    public String getNombreCompleto() {
        return getProvinciaId().getDepartamentoId().getNombre() + " / " + getProvinciaId().getNombre() + " / " + getNombre();

    }
    
}
