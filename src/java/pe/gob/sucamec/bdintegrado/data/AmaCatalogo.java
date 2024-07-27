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

/**
 *
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_CATALOGO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaCatalogo.findAll", query = "SELECT a FROM AmaCatalogo a"),
    @NamedQuery(name = "AmaCatalogo.findById", query = "SELECT a FROM AmaCatalogo a WHERE a.id = :id"),
    @NamedQuery(name = "AmaCatalogo.findByNombre", query = "SELECT a FROM AmaCatalogo a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "AmaCatalogo.findByAbreviatura", query = "SELECT a FROM AmaCatalogo a WHERE a.abreviatura = :abreviatura"),
    @NamedQuery(name = "AmaCatalogo.findByDescripcion", query = "SELECT a FROM AmaCatalogo a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "AmaCatalogo.findByCodProg", query = "SELECT a FROM AmaCatalogo a WHERE a.codProg = :codProg"),
    @NamedQuery(name = "AmaCatalogo.findByOrden", query = "SELECT a FROM AmaCatalogo a WHERE a.orden = :orden"),
    @NamedQuery(name = "AmaCatalogo.findByActivo", query = "SELECT a FROM AmaCatalogo a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaCatalogo.findByAudLogin", query = "SELECT a FROM AmaCatalogo a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaCatalogo.findByAudNumIp", query = "SELECT a FROM AmaCatalogo a WHERE a.audNumIp = :audNumIp")})
public class AmaCatalogo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_CATALOGO")
    @SequenceGenerator(name = "SEQ_AMA_CATALOGO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_CATALOGO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 200)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 15)
    @Column(name = "COD_PROG")
    private String codProg;
    @Column(name = "ORDEN")
    private Long orden;
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
    @ManyToMany(mappedBy = "amaCatalogoList")
    private List<AmaModelos> amaModelosList;
    @JoinTable(schema = "BDINTEGRADO", name = "AMA_DETALLE_CATALOGO", joinColumns = {
        @JoinColumn(name = "TIPO_CATALOGO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TIPO_TIPO_CATALOGO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<AmaCatalogo> amaCatalogoList;
    @ManyToMany(mappedBy = "amaCatalogoList")
    private List<AmaCatalogo> amaCatalogoList1;
    @OneToMany(mappedBy = "tipoId")
    private List<AmaCatalogo> amaCatalogoList2;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaCatalogo tipoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disposicionId")
    private List<AmaModelos> amaModelosList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoArmaId")
    private List<AmaModelos> amaModelosList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marcaId")
    private List<AmaModelos> amaModelosList3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marcaId")
    private List<AmaArma> amaArmaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoArmaId")
    private List<AmaArma> amaArmaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calibreId")
    private List<AmaArma> amaArmaList2;

    public AmaCatalogo() {
    }

    public AmaCatalogo(Long id) {
        this.id = id;
    }

    public AmaCatalogo(Long id, String nombre, short activo, String audLogin, String audNumIp) {
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodProg() {
        return codProg;
    }

    public void setCodProg(String codProg) {
        this.codProg = codProg;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
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
    public List<AmaModelos> getAmaModelosList() {
        return amaModelosList;
    }

    public void setAmaModelosList(List<AmaModelos> amaModelosList) {
        this.amaModelosList = amaModelosList;
    }

    @XmlTransient
    public List<AmaCatalogo> getAmaCatalogoList() {
        return amaCatalogoList;
    }

    public void setAmaCatalogoList(List<AmaCatalogo> amaCatalogoList) {
        this.amaCatalogoList = amaCatalogoList;
    }

    @XmlTransient
    public List<AmaCatalogo> getAmaCatalogoList1() {
        return amaCatalogoList1;
    }

    public void setAmaCatalogoList1(List<AmaCatalogo> amaCatalogoList1) {
        this.amaCatalogoList1 = amaCatalogoList1;
    }

    @XmlTransient
    public List<AmaCatalogo> getAmaCatalogoList2() {
        return amaCatalogoList2;
    }

    public void setAmaCatalogoList2(List<AmaCatalogo> amaCatalogoList2) {
        this.amaCatalogoList2 = amaCatalogoList2;
    }

    public AmaCatalogo getTipoId() {
        return tipoId;
    }

    public void setTipoId(AmaCatalogo tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public List<AmaModelos> getAmaModelosList1() {
        return amaModelosList1;
    }

    public void setAmaModelosList1(List<AmaModelos> amaModelosList1) {
        this.amaModelosList1 = amaModelosList1;
    }

    @XmlTransient
    public List<AmaModelos> getAmaModelosList2() {
        return amaModelosList2;
    }

    public void setAmaModelosList2(List<AmaModelos> amaModelosList2) {
        this.amaModelosList2 = amaModelosList2;
    }

    @XmlTransient
    public List<AmaModelos> getAmaModelosList3() {
        return amaModelosList3;
    }

    public void setAmaModelosList3(List<AmaModelos> amaModelosList3) {
        this.amaModelosList3 = amaModelosList3;
    }

    @XmlTransient
    public List<AmaArma> getAmaArmaList() {
        return amaArmaList;
    }

    public void setAmaArmaList(List<AmaArma> amaArmaList) {
        this.amaArmaList = amaArmaList;
    }

    @XmlTransient
    public List<AmaArma> getAmaArmaList1() {
        return amaArmaList1;
    }

    public void setAmaArmaList1(List<AmaArma> amaArmaList1) {
        this.amaArmaList1 = amaArmaList1;
    }

    @XmlTransient
    public List<AmaArma> getAmaArmaList2() {
        return amaArmaList2;
    }

    public void setAmaArmaList2(List<AmaArma> amaArmaList2) {
        this.amaArmaList2 = amaArmaList2;
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
        if (!(object instanceof AmaCatalogo)) {
            return false;
        }
        AmaCatalogo other = (AmaCatalogo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaCatalogo[ id=" + id + " ]";
    }
    
}
