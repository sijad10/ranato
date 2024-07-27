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
@Table(name = "TIPO_EXPLOSIVO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacTipoExplosivo.findAll", query = "SELECT g FROM GamacTipoExplosivo g"),
    @NamedQuery(name = "GamacTipoExplosivo.findById", query = "SELECT g FROM GamacTipoExplosivo g WHERE g.id = :id"),
    @NamedQuery(name = "GamacTipoExplosivo.findByNombre", query = "SELECT g FROM GamacTipoExplosivo g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "GamacTipoExplosivo.findByAbreviatura", query = "SELECT g FROM GamacTipoExplosivo g WHERE g.abreviatura = :abreviatura"),
    @NamedQuery(name = "GamacTipoExplosivo.findByDescripcion", query = "SELECT g FROM GamacTipoExplosivo g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "GamacTipoExplosivo.findByCodProg", query = "SELECT g FROM GamacTipoExplosivo g WHERE g.codProg = :codProg"),
    @NamedQuery(name = "GamacTipoExplosivo.findByOrden", query = "SELECT g FROM GamacTipoExplosivo g WHERE g.orden = :orden"),
    @NamedQuery(name = "GamacTipoExplosivo.findByActivo", query = "SELECT g FROM GamacTipoExplosivo g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacTipoExplosivo.findByAudLogin", query = "SELECT g FROM GamacTipoExplosivo g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacTipoExplosivo.findByAudNumIp", query = "SELECT g FROM GamacTipoExplosivo g WHERE g.audNumIp = :audNumIp")})
public class GamacTipoExplosivo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_TIPO_EXPLOSIVO")
    @SequenceGenerator(name = "SEQ_GAMAC_TIPO_EXPLOSIVO", schema = "BDINTEGRADO", sequenceName = "SEQ_TIPO_EXPLOSIVO", allocationSize = 1)
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
    @ManyToMany(mappedBy = "gamacTipoExplosivoCollection")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection;
    @OneToMany(mappedBy = "tipoId")
    private Collection<GamacTipoExplosivo> gamacTipoExplosivoCollection;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoExplosivo tipoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection1;
    @OneToMany(mappedBy = "tipoModificatoria")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoCom")
    private Collection<GamacEppCom> gamacEppComCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstado2do")
    private Collection<GamacEppCom> gamacEppComCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstado1er")
    private Collection<GamacEppCom> gamacEppComCollection2;
    @OneToMany(mappedBy = "entidadEmisora")
    private Collection<GamacEppCom> gamacEppComCollection3;

    public GamacTipoExplosivo() {
    }

    public GamacTipoExplosivo(Long id) {
        this.id = id;
    }

    public GamacTipoExplosivo(Long id, String nombre, String abreviatura, String descripcion, String codProg, short orden, short activo, String audLogin, String audNumIp) {
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
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection() {
        return gamacEppRegistroCollection;
    }

    public void setGamacEppRegistroCollection(Collection<GamacEppRegistro> gamacEppRegistroCollection) {
        this.gamacEppRegistroCollection = gamacEppRegistroCollection;
    }

    @XmlTransient
    public Collection<GamacTipoExplosivo> getGamacTipoExplosivoCollection() {
        return gamacTipoExplosivoCollection;
    }

    public void setGamacTipoExplosivoCollection(Collection<GamacTipoExplosivo> gamacTipoExplosivoCollection) {
        this.gamacTipoExplosivoCollection = gamacTipoExplosivoCollection;
    }

    public GamacTipoExplosivo getTipoId() {
        return tipoId;
    }

    public void setTipoId(GamacTipoExplosivo tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection1() {
        return gamacEppRegistroCollection1;
    }

    public void setGamacEppRegistroCollection1(Collection<GamacEppRegistro> gamacEppRegistroCollection1) {
        this.gamacEppRegistroCollection1 = gamacEppRegistroCollection1;
    }

    @XmlTransient
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection2() {
        return gamacEppRegistroCollection2;
    }

    public void setGamacEppRegistroCollection2(Collection<GamacEppRegistro> gamacEppRegistroCollection2) {
        this.gamacEppRegistroCollection2 = gamacEppRegistroCollection2;
    }

    @XmlTransient
    public Collection<GamacEppCom> getGamacEppComCollection() {
        return gamacEppComCollection;
    }

    public void setGamacEppComCollection(Collection<GamacEppCom> gamacEppComCollection) {
        this.gamacEppComCollection = gamacEppComCollection;
    }

    @XmlTransient
    public Collection<GamacEppCom> getGamacEppComCollection1() {
        return gamacEppComCollection1;
    }

    public void setGamacEppComCollection1(Collection<GamacEppCom> gamacEppComCollection1) {
        this.gamacEppComCollection1 = gamacEppComCollection1;
    }

    @XmlTransient
    public Collection<GamacEppCom> getGamacEppComCollection2() {
        return gamacEppComCollection2;
    }

    public void setGamacEppComCollection2(Collection<GamacEppCom> gamacEppComCollection2) {
        this.gamacEppComCollection2 = gamacEppComCollection2;
    }

    @XmlTransient
    public Collection<GamacEppCom> getGamacEppComCollection3() {
        return gamacEppComCollection3;
    }

    public void setGamacEppComCollection3(Collection<GamacEppCom> gamacEppComCollection3) {
        this.gamacEppComCollection3 = gamacEppComCollection3;
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
        if (!(object instanceof GamacTipoExplosivo)) {
            return false;
        }
        GamacTipoExplosivo other = (GamacTipoExplosivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacTipoExplosivo[ id=" + id + " ]";
    }
    
}
