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
import pe.gob.sucamec.turreg.data.TurLicenciaReg;

/**
 *
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TIPO_GAMAC", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoGamac.findAll", query = "SELECT t FROM TipoGamac t"),
    @NamedQuery(name = "TipoGamac.findById", query = "SELECT t FROM TipoGamac t WHERE t.id = :id"),
    @NamedQuery(name = "TipoGamac.findByNombre", query = "SELECT t FROM TipoGamac t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoGamac.findByAbreviatura", query = "SELECT t FROM TipoGamac t WHERE t.abreviatura = :abreviatura"),
    @NamedQuery(name = "TipoGamac.findByDescripcion", query = "SELECT t FROM TipoGamac t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TipoGamac.findByCodProg", query = "SELECT t FROM TipoGamac t WHERE t.codProg = :codProg"),
    @NamedQuery(name = "TipoGamac.findByOrden", query = "SELECT t FROM TipoGamac t WHERE t.orden = :orden"),
    @NamedQuery(name = "TipoGamac.findByActivo", query = "SELECT t FROM TipoGamac t WHERE t.activo = :activo"),
    @NamedQuery(name = "TipoGamac.findByAudLogin", query = "SELECT t FROM TipoGamac t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TipoGamac.findByAudNumIp", query = "SELECT t FROM TipoGamac t WHERE t.audNumIp = :audNumIp")})
public class TipoGamac implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_GAMAC")
    @SequenceGenerator(name = "SEQ_TIPO_GAMAC", schema = "name=", sequenceName = "SEQ_TIPO_GAMAC", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "COD_PROG")
    private String codProg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDEN")
    private short orden;
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
    @JoinTable(schema="BDINTEGRADO", name = "AMA_CATALOGO_MODALIDAD", joinColumns = {
        @JoinColumn(name = "TIPO_GAMAC_MODALIDAD_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "GAMAC_CATALOGO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<AmaCatalogo> amaCatalogoList;
    @OneToMany(mappedBy = "tipoId")
    private List<TipoGamac> tipoGamacList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac tipoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<AmaLicenciaDeUso> amaLicenciaDeUsoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<AmaVerificarArma> amaVerificarArmaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoGuiaId")
    private List<AmaGuiaTransito> amaGuiaTransitoList;
    @OneToMany(mappedBy = "tipoCp")
    private List<AmaTarjetaPropiedad> amaTarjetaPropiedadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<AmaTarjetaPropiedad> amaTarjetaPropiedadList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restriccionId")
    private List<AmaTarjetaPropiedad> amaTarjetaPropiedadList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modalidadId")
    private List<AmaTarjetaPropiedad> amaTarjetaPropiedadList3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modalidadId")
    private List<AmaTipoLicencia> amaTipoLicenciaList;
    @OneToMany(mappedBy = "tipoPosicionId")
    private List<AmaFoto> amaFotoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<AmaArma> amaArmaList;
//REGULARIZACION
    @JoinTable(schema = "BDINTEGRADO", name = "TUR_MODA_LICEN_REG", joinColumns = {
        @JoinColumn(name = "MODALIDAD_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "LICENCIA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<TurLicenciaReg> turLicenciaRegList;
    public TipoGamac() {
    }

    public TipoGamac(Long id) {
        this.id = id;
    }

    public TipoGamac(Long id, String nombre, String abreviatura, String descripcion, String codProg, short orden, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.descripcion = descripcion;
        this.codProg = codProg;
        this.orden = orden;
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

    public short getOrden() {
        return orden;
    }

    public void setOrden(short orden) {
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
    public List<AmaCatalogo> getAmaCatalogoList() {
        return amaCatalogoList;
    }

    public void setAmaCatalogoList(List<AmaCatalogo> amaCatalogoList) {
        this.amaCatalogoList = amaCatalogoList;
    }

    @XmlTransient
    public List<TipoGamac> getTipoGamacList() {
        return tipoGamacList;
    }

    public void setTipoGamacList(List<TipoGamac> tipoGamacList) {
        this.tipoGamacList = tipoGamacList;
    }

    public TipoGamac getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoGamac tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public List<AmaLicenciaDeUso> getAmaLicenciaDeUsoList() {
        return amaLicenciaDeUsoList;
    }

    public void setAmaLicenciaDeUsoList(List<AmaLicenciaDeUso> amaLicenciaDeUsoList) {
        this.amaLicenciaDeUsoList = amaLicenciaDeUsoList;
    }

    @XmlTransient
    public List<AmaVerificarArma> getAmaVerificarArmaList() {
        return amaVerificarArmaList;
    }

    public void setAmaVerificarArmaList(List<AmaVerificarArma> amaVerificarArmaList) {
        this.amaVerificarArmaList = amaVerificarArmaList;
    }

    @XmlTransient
    public List<AmaGuiaTransito> getAmaGuiaTransitoList() {
        return amaGuiaTransitoList;
    }

    public void setAmaGuiaTransitoList(List<AmaGuiaTransito> amaGuiaTransitoList) {
        this.amaGuiaTransitoList = amaGuiaTransitoList;
    }

    @XmlTransient
    public List<AmaTarjetaPropiedad> getAmaTarjetaPropiedadList() {
        return amaTarjetaPropiedadList;
    }

    public void setAmaTarjetaPropiedadList(List<AmaTarjetaPropiedad> amaTarjetaPropiedadList) {
        this.amaTarjetaPropiedadList = amaTarjetaPropiedadList;
    }

    @XmlTransient
    public List<AmaTarjetaPropiedad> getAmaTarjetaPropiedadList1() {
        return amaTarjetaPropiedadList1;
    }

    public void setAmaTarjetaPropiedadList1(List<AmaTarjetaPropiedad> amaTarjetaPropiedadList1) {
        this.amaTarjetaPropiedadList1 = amaTarjetaPropiedadList1;
    }

    @XmlTransient
    public List<AmaTarjetaPropiedad> getAmaTarjetaPropiedadList2() {
        return amaTarjetaPropiedadList2;
    }

    public void setAmaTarjetaPropiedadList2(List<AmaTarjetaPropiedad> amaTarjetaPropiedadList2) {
        this.amaTarjetaPropiedadList2 = amaTarjetaPropiedadList2;
    }

    @XmlTransient
    public List<AmaTarjetaPropiedad> getAmaTarjetaPropiedadList3() {
        return amaTarjetaPropiedadList3;
    }

    public void setAmaTarjetaPropiedadList3(List<AmaTarjetaPropiedad> amaTarjetaPropiedadList3) {
        this.amaTarjetaPropiedadList3 = amaTarjetaPropiedadList3;
    }

    @XmlTransient
    public List<AmaTipoLicencia> getAmaTipoLicenciaList() {
        return amaTipoLicenciaList;
    }

    public void setAmaTipoLicenciaList(List<AmaTipoLicencia> amaTipoLicenciaList) {
        this.amaTipoLicenciaList = amaTipoLicenciaList;
    }

    @XmlTransient
    public List<AmaFoto> getAmaFotoList() {
        return amaFotoList;
    }

    public void setAmaFotoList(List<AmaFoto> amaFotoList) {
        this.amaFotoList = amaFotoList;
    }

    @XmlTransient
    public List<AmaArma> getAmaArmaList() {
        return amaArmaList;
    }

    public void setAmaArmaList(List<AmaArma> amaArmaList) {
        this.amaArmaList = amaArmaList;
    }

    @XmlTransient
    public List<TurLicenciaReg> getTurLicenciaRegList() {
        return turLicenciaRegList;
    }

    public void setTurLicenciaRegList(List<TurLicenciaReg> turLicenciaRegList) {
        this.turLicenciaRegList = turLicenciaRegList;
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
        if (!(object instanceof TipoGamac)) {
            return false;
        }
        TipoGamac other = (TipoGamac) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.bdintegrado.data.TipoGamac[ id=" + id + " ]";
    }
    
}
