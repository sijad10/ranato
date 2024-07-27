/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.Collection;
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
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TIPO_GAMAC", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacTipoGamac.findAll", query = "SELECT g FROM GamacTipoGamac g"),
    @NamedQuery(name = "GamacTipoGamac.findById", query = "SELECT g FROM GamacTipoGamac g WHERE g.id = :id"),
    @NamedQuery(name = "GamacTipoGamac.findByNombre", query = "SELECT g FROM GamacTipoGamac g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "GamacTipoGamac.findByAbreviatura", query = "SELECT g FROM GamacTipoGamac g WHERE g.abreviatura = :abreviatura"),
    @NamedQuery(name = "GamacTipoGamac.findByDescripcion", query = "SELECT g FROM GamacTipoGamac g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "GamacTipoGamac.findByCodProg", query = "SELECT g FROM GamacTipoGamac g WHERE g.codProg = :codProg"),
    @NamedQuery(name = "GamacTipoGamac.findByOrden", query = "SELECT g FROM GamacTipoGamac g WHERE g.orden = :orden"),
    @NamedQuery(name = "GamacTipoGamac.findByActivo", query = "SELECT g FROM GamacTipoGamac g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacTipoGamac.findByAudLogin", query = "SELECT g FROM GamacTipoGamac g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacTipoGamac.findByAudNumIp", query = "SELECT g FROM GamacTipoGamac g WHERE g.audNumIp = :audNumIp")})
public class GamacTipoGamac implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_TIPO_GAMAC")
    @SequenceGenerator(name = "SEQ_GAMAC_TIPO_GAMAC", schema = "BDINTEGRADO", sequenceName = "SEQ_TIPO_GAMAC", allocationSize = 1)
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
    private Collection<GamacAmaCatalogo> gamacAmaCatalogoCollection;
    @OneToMany(mappedBy = "tipoPosicionId")
    private Collection<GamacAmaFoto> gamacAmaFotoCollection;
    @OneToMany(mappedBy = "denominacionId")
    private Collection<GamacAmaMunicion> gamacAmaMunicionCollection;
    @OneToMany(mappedBy = "tipoProyectilId")
    private Collection<GamacAmaMunicion> gamacAmaMunicionCollection1;
    @OneToMany(mappedBy = "tipoIgnicionId")
    private Collection<GamacAmaMunicion> gamacAmaMunicionCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoMunicionId")
    private Collection<GamacAmaMunicion> gamacAmaMunicionCollection3;
    @OneToMany(mappedBy = "tipoId")
    private Collection<GamacTipoGamac> gamacTipoGamacCollection;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoGamac tipoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private Collection<GamacAmaLicenciaDeUso> gamacAmaLicenciaDeUsoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipodocTransacId")
    private Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoTransaccionId")
    private Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection1;
    @OneToMany(mappedBy = "estadopresenId")
    private Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoclienteproveedorId")
    private Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPresentacionId")
    private Collection<GamacAmaAdqmunPresenta> gamacAmaAdqmunPresentaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private Collection<GamacAmaVerificarArma> gamacAmaVerificarArmaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection;
    @OneToMany(mappedBy = "tipoCp")
    private Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restriccionId")
    private Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modalidadId")
    private Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private Collection<GamacAmaArma> gamacAmaArmaCollection;
    @OneToMany(mappedBy = "situacionId")
    private Collection<GamacAmaArma> gamacAmaArmaCollection1;

    public GamacTipoGamac() {
    }

    public GamacTipoGamac(Long id) {
        this.id = id;
    }

    public GamacTipoGamac(Long id, String nombre, String abreviatura, String descripcion, String codProg, short orden, short activo, String audLogin, String audNumIp) {
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
    public Collection<GamacAmaCatalogo> getGamacAmaCatalogoCollection() {
        return gamacAmaCatalogoCollection;
    }

    public void setGamacAmaCatalogoCollection(Collection<GamacAmaCatalogo> gamacAmaCatalogoCollection) {
        this.gamacAmaCatalogoCollection = gamacAmaCatalogoCollection;
    }

    @XmlTransient
    public Collection<GamacAmaFoto> getGamacAmaFotoCollection() {
        return gamacAmaFotoCollection;
    }

    public void setGamacAmaFotoCollection(Collection<GamacAmaFoto> gamacAmaFotoCollection) {
        this.gamacAmaFotoCollection = gamacAmaFotoCollection;
    }

    @XmlTransient
    public Collection<GamacAmaMunicion> getGamacAmaMunicionCollection() {
        return gamacAmaMunicionCollection;
    }

    public void setGamacAmaMunicionCollection(Collection<GamacAmaMunicion> gamacAmaMunicionCollection) {
        this.gamacAmaMunicionCollection = gamacAmaMunicionCollection;
    }

    @XmlTransient
    public Collection<GamacAmaMunicion> getGamacAmaMunicionCollection1() {
        return gamacAmaMunicionCollection1;
    }

    public void setGamacAmaMunicionCollection1(Collection<GamacAmaMunicion> gamacAmaMunicionCollection1) {
        this.gamacAmaMunicionCollection1 = gamacAmaMunicionCollection1;
    }

    @XmlTransient
    public Collection<GamacAmaMunicion> getGamacAmaMunicionCollection2() {
        return gamacAmaMunicionCollection2;
    }

    public void setGamacAmaMunicionCollection2(Collection<GamacAmaMunicion> gamacAmaMunicionCollection2) {
        this.gamacAmaMunicionCollection2 = gamacAmaMunicionCollection2;
    }

    @XmlTransient
    public Collection<GamacAmaMunicion> getGamacAmaMunicionCollection3() {
        return gamacAmaMunicionCollection3;
    }

    public void setGamacAmaMunicionCollection3(Collection<GamacAmaMunicion> gamacAmaMunicionCollection3) {
        this.gamacAmaMunicionCollection3 = gamacAmaMunicionCollection3;
    }

    @XmlTransient
    public Collection<GamacTipoGamac> getGamacTipoGamacCollection() {
        return gamacTipoGamacCollection;
    }

    public void setGamacTipoGamacCollection(Collection<GamacTipoGamac> gamacTipoGamacCollection) {
        this.gamacTipoGamacCollection = gamacTipoGamacCollection;
    }

    public GamacTipoGamac getTipoId() {
        return tipoId;
    }

    public void setTipoId(GamacTipoGamac tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public Collection<GamacAmaLicenciaDeUso> getGamacAmaLicenciaDeUsoCollection() {
        return gamacAmaLicenciaDeUsoCollection;
    }

    public void setGamacAmaLicenciaDeUsoCollection(Collection<GamacAmaLicenciaDeUso> gamacAmaLicenciaDeUsoCollection) {
        this.gamacAmaLicenciaDeUsoCollection = gamacAmaLicenciaDeUsoCollection;
    }

    @XmlTransient
    public Collection<GamacAmaAdmunTransaccion> getGamacAmaAdmunTransaccionCollection() {
        return gamacAmaAdmunTransaccionCollection;
    }

    public void setGamacAmaAdmunTransaccionCollection(Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection) {
        this.gamacAmaAdmunTransaccionCollection = gamacAmaAdmunTransaccionCollection;
    }

    @XmlTransient
    public Collection<GamacAmaAdmunTransaccion> getGamacAmaAdmunTransaccionCollection1() {
        return gamacAmaAdmunTransaccionCollection1;
    }

    public void setGamacAmaAdmunTransaccionCollection1(Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection1) {
        this.gamacAmaAdmunTransaccionCollection1 = gamacAmaAdmunTransaccionCollection1;
    }

    @XmlTransient
    public Collection<GamacAmaAdmunTransaccion> getGamacAmaAdmunTransaccionCollection2() {
        return gamacAmaAdmunTransaccionCollection2;
    }

    public void setGamacAmaAdmunTransaccionCollection2(Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection2) {
        this.gamacAmaAdmunTransaccionCollection2 = gamacAmaAdmunTransaccionCollection2;
    }

    @XmlTransient
    public Collection<GamacAmaAdmunTransaccion> getGamacAmaAdmunTransaccionCollection3() {
        return gamacAmaAdmunTransaccionCollection3;
    }

    public void setGamacAmaAdmunTransaccionCollection3(Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection3) {
        this.gamacAmaAdmunTransaccionCollection3 = gamacAmaAdmunTransaccionCollection3;
    }

    @XmlTransient
    public Collection<GamacAmaAdqmunPresenta> getGamacAmaAdqmunPresentaCollection() {
        return gamacAmaAdqmunPresentaCollection;
    }

    public void setGamacAmaAdqmunPresentaCollection(Collection<GamacAmaAdqmunPresenta> gamacAmaAdqmunPresentaCollection) {
        this.gamacAmaAdqmunPresentaCollection = gamacAmaAdqmunPresentaCollection;
    }

    @XmlTransient
    public Collection<GamacAmaVerificarArma> getGamacAmaVerificarArmaCollection() {
        return gamacAmaVerificarArmaCollection;
    }

    public void setGamacAmaVerificarArmaCollection(Collection<GamacAmaVerificarArma> gamacAmaVerificarArmaCollection) {
        this.gamacAmaVerificarArmaCollection = gamacAmaVerificarArmaCollection;
    }

    @XmlTransient
    public Collection<GamacAmaTarjetaPropiedad> getGamacAmaTarjetaPropiedadCollection() {
        return gamacAmaTarjetaPropiedadCollection;
    }

    public void setGamacAmaTarjetaPropiedadCollection(Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection) {
        this.gamacAmaTarjetaPropiedadCollection = gamacAmaTarjetaPropiedadCollection;
    }

    @XmlTransient
    public Collection<GamacAmaTarjetaPropiedad> getGamacAmaTarjetaPropiedadCollection1() {
        return gamacAmaTarjetaPropiedadCollection1;
    }

    public void setGamacAmaTarjetaPropiedadCollection1(Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection1) {
        this.gamacAmaTarjetaPropiedadCollection1 = gamacAmaTarjetaPropiedadCollection1;
    }

    @XmlTransient
    public Collection<GamacAmaTarjetaPropiedad> getGamacAmaTarjetaPropiedadCollection2() {
        return gamacAmaTarjetaPropiedadCollection2;
    }

    public void setGamacAmaTarjetaPropiedadCollection2(Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection2) {
        this.gamacAmaTarjetaPropiedadCollection2 = gamacAmaTarjetaPropiedadCollection2;
    }

    @XmlTransient
    public Collection<GamacAmaTarjetaPropiedad> getGamacAmaTarjetaPropiedadCollection3() {
        return gamacAmaTarjetaPropiedadCollection3;
    }

    public void setGamacAmaTarjetaPropiedadCollection3(Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection3) {
        this.gamacAmaTarjetaPropiedadCollection3 = gamacAmaTarjetaPropiedadCollection3;
    }

    @XmlTransient
    public Collection<GamacAmaArma> getGamacAmaArmaCollection() {
        return gamacAmaArmaCollection;
    }

    public void setGamacAmaArmaCollection(Collection<GamacAmaArma> gamacAmaArmaCollection) {
        this.gamacAmaArmaCollection = gamacAmaArmaCollection;
    }

    @XmlTransient
    public Collection<GamacAmaArma> getGamacAmaArmaCollection1() {
        return gamacAmaArmaCollection1;
    }

    public void setGamacAmaArmaCollection1(Collection<GamacAmaArma> gamacAmaArmaCollection1) {
        this.gamacAmaArmaCollection1 = gamacAmaArmaCollection1;
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
        if (!(object instanceof GamacTipoGamac)) {
            return false;
        }
        GamacTipoGamac other = (GamacTipoGamac) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacTipoGamac[ id=" + id + " ]";
    }
    
}
