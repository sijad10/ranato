/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.data;

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
@Table(name = "TIPO_BASE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsultaTipoBase.findAll", query = "SELECT g FROM ConsultaTipoBase g"),
    @NamedQuery(name = "ConsultaTipoBase.findById", query = "SELECT g FROM ConsultaTipoBase g WHERE g.id = :id"),
    @NamedQuery(name = "ConsultaTipoBase.findByNombre", query = "SELECT g FROM ConsultaTipoBase g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "ConsultaTipoBase.findByAbreviatura", query = "SELECT g FROM ConsultaTipoBase g WHERE g.abreviatura = :abreviatura"),
    @NamedQuery(name = "ConsultaTipoBase.findByDescripcion", query = "SELECT g FROM ConsultaTipoBase g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "ConsultaTipoBase.findByCodProg", query = "SELECT g FROM ConsultaTipoBase g WHERE g.codProg = :codProg"),
    @NamedQuery(name = "ConsultaTipoBase.findByOrden", query = "SELECT g FROM ConsultaTipoBase g WHERE g.orden = :orden"),
    @NamedQuery(name = "ConsultaTipoBase.findByActivo", query = "SELECT g FROM ConsultaTipoBase g WHERE g.activo = :activo"),
    @NamedQuery(name = "ConsultaTipoBase.findByAudLogin", query = "SELECT g FROM ConsultaTipoBase g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "ConsultaTipoBase.findByAudNumIp", query = "SELECT g FROM ConsultaTipoBase g WHERE g.audNumIp = :audNumIp")})
public class ConsultaTipoBase implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_TIPO_BASE")
    @SequenceGenerator(name = "SEQ_CONSULTA_TIPO_BASE", schema = "BDINTEGRADO", sequenceName = "SEQ_TIPO_BASE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "COD_PROG")
    private String codProg;
    @Column(name = "ORDEN")
    private Integer orden;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private Collection<ConsultaSbUsuario> gamacSbUsuarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAutenId")
    private Collection<ConsultaSbUsuario> gamacSbUsuarioCollection1;
    @OneToMany(mappedBy = "areaId")
    private Collection<ConsultaSbUsuario> gamacSbUsuarioCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private Collection<ConsultaSbDireccion> gamacSbDireccionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "viaId")
    private Collection<ConsultaSbDireccion> gamacSbDireccionCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zonaId")
    private Collection<ConsultaSbDireccion> gamacSbDireccionCollection2;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProId")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRegId")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoOpeId")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection2;*/
    @OneToMany(mappedBy = "tipoId")
    private Collection<ConsultaTipoBase> gamacTipoBaseCollection;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private ConsultaTipoBase tipoId;
    @OneToMany(mappedBy = "estCivilId")
    private Collection<ConsultaSbPersona> sbPersonaCollection;
    @OneToMany(mappedBy = "generoId")
    private Collection<ConsultaSbPersona> sbPersonaCollection1;
    @OneToMany(mappedBy = "tipoDoc")
    private Collection<ConsultaSbPersona> sbPersonaCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private Collection<ConsultaSbPersona> sbPersonaCollection3;
    @OneToMany(mappedBy = "instituLabId")
    private Collection<ConsultaSbPersona> sbPersonaCollection4;
    @OneToMany(mappedBy = "ocupacionId")
    private Collection<ConsultaSbPersona> sbPersonaCollection5;

    public ConsultaTipoBase() {
    }

    public ConsultaTipoBase(Long id) {
        this.id = id;
    }

    public ConsultaTipoBase(Long id, String nombre, String codProg, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.codProg = codProg;
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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
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
    public Collection<ConsultaSbUsuario> getGamacSbUsuarioCollection() {
        return gamacSbUsuarioCollection;
    }

    public void setGamacSbUsuarioCollection(Collection<ConsultaSbUsuario> gamacSbUsuarioCollection) {
        this.gamacSbUsuarioCollection = gamacSbUsuarioCollection;
    }

    @XmlTransient
    public Collection<ConsultaSbUsuario> getGamacSbUsuarioCollection1() {
        return gamacSbUsuarioCollection1;
    }

    public void setGamacSbUsuarioCollection1(Collection<ConsultaSbUsuario> gamacSbUsuarioCollection1) {
        this.gamacSbUsuarioCollection1 = gamacSbUsuarioCollection1;
    }

    @XmlTransient
    public Collection<ConsultaSbUsuario> getGamacSbUsuarioCollection2() {
        return gamacSbUsuarioCollection2;
    }

    public void setGamacSbUsuarioCollection2(Collection<ConsultaSbUsuario> gamacSbUsuarioCollection2) {
        this.gamacSbUsuarioCollection2 = gamacSbUsuarioCollection2;
    }

    @XmlTransient
    public Collection<ConsultaSbDireccion> getGamacSbDireccionCollection() {
        return gamacSbDireccionCollection;
    }

    public void setGamacSbDireccionCollection(Collection<ConsultaSbDireccion> gamacSbDireccionCollection) {
        this.gamacSbDireccionCollection = gamacSbDireccionCollection;
    }

    @XmlTransient
    public Collection<ConsultaSbDireccion> getGamacSbDireccionCollection1() {
        return gamacSbDireccionCollection1;
    }

    public void setGamacSbDireccionCollection1(Collection<ConsultaSbDireccion> gamacSbDireccionCollection1) {
        this.gamacSbDireccionCollection1 = gamacSbDireccionCollection1;
    }

    @XmlTransient
    public Collection<ConsultaSbDireccion> getGamacSbDireccionCollection2() {
        return gamacSbDireccionCollection2;
    }

    public void setGamacSbDireccionCollection2(Collection<ConsultaSbDireccion> gamacSbDireccionCollection2) {
        this.gamacSbDireccionCollection2 = gamacSbDireccionCollection2;
    }

    /*@XmlTransient
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection() {
        return gamacEppRegistroCollection;
    }

    public void setGamacEppRegistroCollection(Collection<GamacEppRegistro> gamacEppRegistroCollection) {
        this.gamacEppRegistroCollection = gamacEppRegistroCollection;
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
    }*/

    @XmlTransient
    public Collection<ConsultaTipoBase> getGamacTipoBaseCollection() {
        return gamacTipoBaseCollection;
    }

    public void setGamacTipoBaseCollection(Collection<ConsultaTipoBase> gamacTipoBaseCollection) {
        this.gamacTipoBaseCollection = gamacTipoBaseCollection;
    }

    public ConsultaTipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(ConsultaTipoBase tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public Collection<ConsultaSbPersona> getSbPersonaCollection() {
        return sbPersonaCollection;
    }

    public void setConsultaSbPersonaCollection(Collection<ConsultaSbPersona> sbPersonaCollection) {
        this.sbPersonaCollection = sbPersonaCollection;
    }

    @XmlTransient
    public Collection<ConsultaSbPersona> getSbPersonaCollection1() {
        return sbPersonaCollection1;
    }

    public void setSbPersonaCollection1(Collection<ConsultaSbPersona> sbPersonaCollection1) {
        this.sbPersonaCollection1 = sbPersonaCollection1;
    }

    @XmlTransient
    public Collection<ConsultaSbPersona> getSbPersonaCollection2() {
        return sbPersonaCollection2;
    }

    public void setSbPersonaCollection2(Collection<ConsultaSbPersona> sbPersonaCollection2) {
        this.sbPersonaCollection2 = sbPersonaCollection2;
    }

    @XmlTransient
    public Collection<ConsultaSbPersona> getSbPersonaCollection3() {
        return sbPersonaCollection3;
    }

    public void setSbPersonaCollection3(Collection<ConsultaSbPersona> sbPersonaCollection3) {
        this.sbPersonaCollection3 = sbPersonaCollection3;
    }

    @XmlTransient
    public Collection<ConsultaSbPersona> getSbPersonaCollection4() {
        return sbPersonaCollection4;
    }

    public void setSbPersonaCollection4(Collection<ConsultaSbPersona> sbPersonaCollection4) {
        this.sbPersonaCollection4 = sbPersonaCollection4;
    }

    @XmlTransient
    public Collection<ConsultaSbPersona> getSbPersonaCollection5() {
        return sbPersonaCollection5;
    }

    public void setSbPersonaCollection5(Collection<ConsultaSbPersona> sbPersonaCollection5) {
        this.sbPersonaCollection5 = sbPersonaCollection5;
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
        if (!(object instanceof ConsultaTipoBase)) {
            return false;
        }
        ConsultaTipoBase other = (ConsultaTipoBase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.renagi.data.ConsultaTipoBase[ id=" + id + " ]";
    }
    
}
