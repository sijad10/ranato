/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
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
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_LOCAL", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppLocal.findAll", query = "SELECT e FROM EppLocal e"),
    @NamedQuery(name = "EppLocal.findById", query = "SELECT e FROM EppLocal e WHERE e.id = :id"),
    @NamedQuery(name = "EppLocal.findByDireccion", query = "SELECT e FROM EppLocal e WHERE e.direccion = :direccion"),
    @NamedQuery(name = "EppLocal.findByLatitud", query = "SELECT e FROM EppLocal e WHERE e.latitud = :latitud"),
    @NamedQuery(name = "EppLocal.findByLongitud", query = "SELECT e FROM EppLocal e WHERE e.longitud = :longitud"),
    @NamedQuery(name = "EppLocal.findByActivo", query = "SELECT e FROM EppLocal e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppLocal.findByAudLogin", query = "SELECT e FROM EppLocal e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppLocal.findByAudNumIp", query = "SELECT e FROM EppLocal e WHERE e.audNumIp = :audNumIp"),
    @NamedQuery(name = "EppLocal.findByCondEsDep", query = "SELECT e FROM EppLocal e WHERE e.condEsDep = :condEsDep"),
    @NamedQuery(name = "EppLocal.findByCapacidad", query = "SELECT e FROM EppLocal e WHERE e.capacidad = :capacidad")})
public class EppLocal implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_LOCAL")
    @SequenceGenerator(name = "SEQ_EPP_LOCAL", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_LOCAL", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "DIRECCION")
    private String direccion;
    @Size(max = 200)
    @Column(name = "LATITUD")
    private String latitud;
    @Size(max = 200)
    @Column(name = "LONGITUD")
    private String longitud;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "COND_ES_DEP")
    private short condEsDep;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CAPACIDAD")
    private Double capacidad;
    @ManyToMany(mappedBy = "eppLocalList")
    private List<EppRegistro> eppRegistroList;
    @OneToMany(mappedBy = "origLocalId")
    private List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList;
    @OneToMany(mappedBy = "destLocalId")
    private List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localId")
    private List<EppEspectaculo> eppEspectaculoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localId")
    private List<EppTallerdeposito> eppTallerdepositoList;
    @JoinColumn(name = "TIPO_PROC_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoProcId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaId;
    @JoinColumn(name = "UBIGEO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistrito ubigeoId;

    public EppLocal() {
    }

    public EppLocal(Long id) {
        this.id = id;
    }

    public EppLocal(Long id, String direccion, short activo, String audLogin, String audNumIp, short condEsDep) {
        this.id = id;
        this.direccion = direccion;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
        this.condEsDep = condEsDep;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
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

    public short getCondEsDep() {
        return condEsDep;
    }

    public void setCondEsDep(short condEsDep) {
        this.condEsDep = condEsDep;
    }

    public Double getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Double capacidad) {
        this.capacidad = capacidad;
    }

     public String getDireccionCompleta() {
        return ubigeoId.getNombreCompleto() + " / " + direccion;
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

    @XmlTransient
    public List<EppGuiaTransitoPiro> getEppGuiaTransitoPiroList1() {
        return eppGuiaTransitoPiroList1;
    }

    public void setEppGuiaTransitoPiroList1(List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList1) {
        this.eppGuiaTransitoPiroList1 = eppGuiaTransitoPiroList1;
    }

    @XmlTransient
    public List<EppEspectaculo> getEppEspectaculoList() {
        return eppEspectaculoList;
    }

    public void setEppEspectaculoList(List<EppEspectaculo> eppEspectaculoList) {
        this.eppEspectaculoList = eppEspectaculoList;
    }

    @XmlTransient
    public List<EppTallerdeposito> getEppTallerdepositoList() {
        return eppTallerdepositoList;
    }

    public void setEppTallerdepositoList(List<EppTallerdeposito> eppTallerdepositoList) {
        this.eppTallerdepositoList = eppTallerdepositoList;
    }

    public TipoBaseGt getTipoProcId() {
        return tipoProcId;
    }

    public void setTipoProcId(TipoBaseGt tipoProcId) {
        this.tipoProcId = tipoProcId;
    }

    public SbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersona personaId) {
        this.personaId = personaId;
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
        if (!(object instanceof EppLocal)) {
            return false;
        }
        EppLocal other = (EppLocal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppLocal[ id=" + id + " ]";
    }
    
}
