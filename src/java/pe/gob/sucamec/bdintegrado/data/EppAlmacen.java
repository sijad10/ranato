/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.SbDistrito;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_ALMACEN", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppAlmacen.findAll", query = "SELECT e FROM EppAlmacen e"),
    @NamedQuery(name = "EppAlmacen.findById", query = "SELECT e FROM EppAlmacen e WHERE e.id = :id"),
    @NamedQuery(name = "EppAlmacen.findByRazonSocial", query = "SELECT e FROM EppAlmacen e WHERE e.razonSocial = :razonSocial"),
    @NamedQuery(name = "EppAlmacen.findByDireccion", query = "SELECT e FROM EppAlmacen e WHERE e.direccion = :direccion"),
    @NamedQuery(name = "EppAlmacen.findByHabilitado", query = "SELECT e FROM EppAlmacen e WHERE e.habilitado = :habilitado"),
    @NamedQuery(name = "EppAlmacen.findByActivo", query = "SELECT e FROM EppAlmacen e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppAlmacen.findByAudLogin", query = "SELECT e FROM EppAlmacen e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppAlmacen.findByAudNumIp", query = "SELECT e FROM EppAlmacen e WHERE e.audNumIp = :audNumIp")})
public class EppAlmacen implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_ALMACEN")
    @SequenceGenerator(name = "SEQ_EPP_ALMACEN", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_ALMACEN", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "RAZON_SOCIAL")
    private String razonSocial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DIRECCION")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HABILITADO")
    private short habilitado;
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
    @ManyToMany(mappedBy = "eppAlmacenList")
    private List<EppRegistro> eppRegistroList;
    @OneToMany(mappedBy = "almDestId")
    private List<EppRegistroIntSal> eppRegistroIntSalList;
    @OneToMany(mappedBy = "almInspecId")
    private List<EppRegistroIntSal> eppRegistroIntSalList1;
    @OneToMany(mappedBy = "destinoAlmacen")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList;
    @OneToMany(mappedBy = "origenAlmacen")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList1;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoId;
    @JoinColumn(name = "UBIGEO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistrito ubigeoId;

    public EppAlmacen() {
    }

    public EppAlmacen(Long id) {
        this.id = id;
    }

    public EppAlmacen(Long id, String razonSocial, String direccion, short habilitado, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.habilitado = habilitado;
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

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public short getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(short habilitado) {
        this.habilitado = habilitado;
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
    public List<EppRegistroIntSal> getEppRegistroIntSalList1() {
        return eppRegistroIntSalList1;
    }

    public void setEppRegistroIntSalList1(List<EppRegistroIntSal> eppRegistroIntSalList1) {
        this.eppRegistroIntSalList1 = eppRegistroIntSalList1;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList() {
        return eppRegistroGuiaTransitoList;
    }

    public void setEppRegistroGuiaTransitoList(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList) {
        this.eppRegistroGuiaTransitoList = eppRegistroGuiaTransitoList;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList1() {
        return eppRegistroGuiaTransitoList1;
    }

    public void setEppRegistroGuiaTransitoList1(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList1) {
        this.eppRegistroGuiaTransitoList1 = eppRegistroGuiaTransitoList1;
    }

    public TipoExplosivoGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoExplosivoGt tipoId) {
        this.tipoId = tipoId;
    }

    public SbDistrito getUbigeoId() {
        return ubigeoId;
    }

    public void setUbigeoId(SbDistrito ubigeoId) {
        this.ubigeoId = ubigeoId;
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
        if (!(object instanceof EppAlmacen)) {
            return false;
        }
        EppAlmacen other = (EppAlmacen) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppAlmacen[ id=" + id + " ]";
    }
    
}
