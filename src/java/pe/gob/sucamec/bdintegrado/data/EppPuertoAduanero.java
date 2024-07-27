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
@Table(name = "EPP_PUERTO_ADUANERO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppPuertoAduanero.findAll", query = "SELECT e FROM EppPuertoAduanero e"),
    @NamedQuery(name = "EppPuertoAduanero.findById", query = "SELECT e FROM EppPuertoAduanero e WHERE e.id = :id"),
    @NamedQuery(name = "EppPuertoAduanero.findByAbreviatura", query = "SELECT e FROM EppPuertoAduanero e WHERE e.abreviatura = :abreviatura"),
    @NamedQuery(name = "EppPuertoAduanero.findByDireccion", query = "SELECT e FROM EppPuertoAduanero e WHERE e.direccion = :direccion"),
    @NamedQuery(name = "EppPuertoAduanero.findByNombre", query = "SELECT e FROM EppPuertoAduanero e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EppPuertoAduanero.findByNumeroDuaDam", query = "SELECT e FROM EppPuertoAduanero e WHERE e.numeroDuaDam = :numeroDuaDam"),
    @NamedQuery(name = "EppPuertoAduanero.findByActivo", query = "SELECT e FROM EppPuertoAduanero e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppPuertoAduanero.findByAudLogin", query = "SELECT e FROM EppPuertoAduanero e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppPuertoAduanero.findByAudNumIp", query = "SELECT e FROM EppPuertoAduanero e WHERE e.audNumIp = :audNumIp")})
public class EppPuertoAduanero implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_PUERTO_ADUANERO")
    @SequenceGenerator(name = "SEQ_EPP_PUERTO_ADUANERO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_PUERTO_ADUANERO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "DIRECCION")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 250)
    @Column(name = "NUMERO_DUA_DAM")
    private String numeroDuaDam;
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
    @ManyToMany(mappedBy = "eppPuertoAduaneroList")
    private List<EppRegistro> eppRegistroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "puertoAduaneroId")
    private List<EppRegistroIntSal> eppRegistroIntSalList;
    @OneToMany(mappedBy = "puertoAduaneroOrigen")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList;
    @OneToMany(mappedBy = "puertoAduaneroDestino")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "puertoAduaneroId")
    private List<EppAlmacenAduanero> eppAlmacenAduaneroList;
    @OneToMany(mappedBy = "origPuertoAduaId")
    private List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList;
    @OneToMany(mappedBy = "destPuertoAduaId")
    private List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList1;
    @JoinColumn(name = "VIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt viaId;
    @JoinColumn(name = "UBIGEO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistrito ubigeoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "puertoAduaneroId")
    private List<EppInternaSalida> eppInternaSalidaList;

    public EppPuertoAduanero() {
    }

    public EppPuertoAduanero(Long id) {
        this.id = id;
    }

    public EppPuertoAduanero(Long id, String direccion, String nombre, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.direccion = direccion;
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroDuaDam() {
        return numeroDuaDam;
    }

    public void setNumeroDuaDam(String numeroDuaDam) {
        this.numeroDuaDam = numeroDuaDam;
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

    @XmlTransient
    public List<EppAlmacenAduanero> getEppAlmacenAduaneroList() {
        return eppAlmacenAduaneroList;
    }

    public void setEppAlmacenAduaneroList(List<EppAlmacenAduanero> eppAlmacenAduaneroList) {
        this.eppAlmacenAduaneroList = eppAlmacenAduaneroList;
    }

    @XmlTransient
    public List<EppGuiaTransitoPiro> getEppGuiaTransitoPiroList() {
        return eppGuiaTransitoPiroList;
    }

    public void setEppGuiaTransitoPiroList(List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList) {
        this.eppGuiaTransitoPiroList = eppGuiaTransitoPiroList;
    }

    @XmlTransient
    public List<EppGuiaTransitoPiro> getEppGuiaTransitoPiroList1() {
        return eppGuiaTransitoPiroList1;
    }

    public void setEppGuiaTransitoPiroList1(List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList1) {
        this.eppGuiaTransitoPiroList1 = eppGuiaTransitoPiroList1;
    }

    public TipoBaseGt getViaId() {
        return viaId;
    }

    public void setViaId(TipoBaseGt viaId) {
        this.viaId = viaId;
    }

    public SbDistrito getUbigeoId() {
        return ubigeoId;
    }

    public void setUbigeoId(SbDistrito ubigeoId) {
        this.ubigeoId = ubigeoId;
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
        if (!(object instanceof EppPuertoAduanero)) {
            return false;
        }
        EppPuertoAduanero other = (EppPuertoAduanero) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.EppPuertoAduanero[ id=" + id + " ]";
    }
    
}
