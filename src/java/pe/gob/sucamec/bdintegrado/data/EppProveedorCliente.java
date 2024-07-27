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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
import pe.gob.sucamec.sistemabase.data.SbPais;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_PROVEEDOR_CLIENTE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppProveedorCliente.findAll", query = "SELECT e FROM EppProveedorCliente e"),
    @NamedQuery(name = "EppProveedorCliente.findById", query = "SELECT e FROM EppProveedorCliente e WHERE e.id = :id"),
    @NamedQuery(name = "EppProveedorCliente.findByRazonSocial", query = "SELECT e FROM EppProveedorCliente e WHERE e.razonSocial = :razonSocial"),
    @NamedQuery(name = "EppProveedorCliente.findByDireccion", query = "SELECT e FROM EppProveedorCliente e WHERE e.direccion = :direccion"),
    @NamedQuery(name = "EppProveedorCliente.findByCorreoElectronico", query = "SELECT e FROM EppProveedorCliente e WHERE e.correoElectronico = :correoElectronico"),
    @NamedQuery(name = "EppProveedorCliente.findByTelefono", query = "SELECT e FROM EppProveedorCliente e WHERE e.telefono = :telefono"),
    @NamedQuery(name = "EppProveedorCliente.findByActivo", query = "SELECT e FROM EppProveedorCliente e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppProveedorCliente.findByAudLogin", query = "SELECT e FROM EppProveedorCliente e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppProveedorCliente.findByAudNumIp", query = "SELECT e FROM EppProveedorCliente e WHERE e.audNumIp = :audNumIp")})
public class EppProveedorCliente implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_PROVEEDOR_CLIENTE")
    @SequenceGenerator(name = "SEQ_EPP_PROVEEDOR_CLIENTE", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_PROVEEDOR_CLIENTE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "RAZON_SOCIAL")
    private String razonSocial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DIRECCION")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "CORREO_ELECTRONICO")
    private String correoElectronico;
    @Size(max = 20)
    @Column(name = "TELEFONO")
    private String telefono;
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
    @JoinTable(schema="BDINTEGRADO", name = "EPP_IMP_EXP_CLIENTE", joinColumns = {
        @JoinColumn(name = "PROVEEDOR_CLIENTE_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppRegistro> eppRegistroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveedorClienteId")
    private List<EppRegistroIntSal> eppRegistroIntSalList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoId;
    @JoinColumn(name = "PAIS_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPais paisId;
    @OneToMany(mappedBy = "proveeClienId")
    private List<EppImpExpExplosivo> eppImpExpExplosivoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveedorClienteId")
    private List<EppInternaSalida> eppInternaSalidaList;

    public EppProveedorCliente() {
    }

    public EppProveedorCliente(Long id) {
        this.id = id;
    }

    public EppProveedorCliente(Long id, String razonSocial, String direccion, String correoElectronico, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.correoElectronico = correoElectronico;
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

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public TipoExplosivoGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoExplosivoGt tipoId) {
        this.tipoId = tipoId;
    }

    public SbPais getPaisId() {
        return paisId;
    }

    public void setPaisId(SbPais paisId) {
        this.paisId = paisId;
    }

    @XmlTransient
    public List<EppImpExpExplosivo> getEppImpExpExplosivoList() {
        return eppImpExpExplosivoList;
    }

    public void setEppImpExpExplosivoList(List<EppImpExpExplosivo> eppImpExpExplosivoList) {
        this.eppImpExpExplosivoList = eppImpExpExplosivoList;
    }

    @XmlTransient
    public List<EppInternaSalida> getEppInternaSalidaList() {
        return eppInternaSalidaList;
    }

    public void setEppInternaSalidaList(List<EppInternaSalida> eppInternaSalidaList) {
        this.eppInternaSalidaList = eppInternaSalidaList;
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
        if (!(object instanceof EppProveedorCliente)) {
            return false;
        }
        EppProveedorCliente other = (EppProveedorCliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppProveedorCliente[ id=" + id + " ]";
    }
    
}
