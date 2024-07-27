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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "UNIDAD_MEDIDA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacUnidadMedida.findAll", query = "SELECT g FROM GamacUnidadMedida g"),
    @NamedQuery(name = "GamacUnidadMedida.findById", query = "SELECT g FROM GamacUnidadMedida g WHERE g.id = :id"),
    @NamedQuery(name = "GamacUnidadMedida.findByNombre", query = "SELECT g FROM GamacUnidadMedida g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "GamacUnidadMedida.findBySimbolo", query = "SELECT g FROM GamacUnidadMedida g WHERE g.simbolo = :simbolo"),
    @NamedQuery(name = "GamacUnidadMedida.findByHabilitado", query = "SELECT g FROM GamacUnidadMedida g WHERE g.habilitado = :habilitado"),
    @NamedQuery(name = "GamacUnidadMedida.findByActivo", query = "SELECT g FROM GamacUnidadMedida g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacUnidadMedida.findByAudLogin", query = "SELECT g FROM GamacUnidadMedida g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacUnidadMedida.findByAudNumIp", query = "SELECT g FROM GamacUnidadMedida g WHERE g.audNumIp = :audNumIp")})
public class GamacUnidadMedida implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_UNIDAD_MEDIDA")
    @SequenceGenerator(name = "SEQ_GAMAC_UNIDAD_MEDIDA", schema = "BDINTEGRADO", sequenceName = "SEQ_UNIDAD_MEDIDA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "SIMBOLO")
    private String simbolo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HABILITADO")
    private Character habilitado;
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
    @OneToMany(mappedBy = "velocsalidaUmId")
    private Collection<GamacAmaMunicion> gamacAmaMunicionCollection;
    @OneToMany(mappedBy = "diamlargoUmId")
    private Collection<GamacAmaMunicion> gamacAmaMunicionCollection1;
    @OneToMany(mappedBy = "unidadMedidaId")
    private Collection<GamacAmaVerificarArma> gamacAmaVerificarArmaCollection;
    @OneToMany(mappedBy = "unidadMedidaId")
    private Collection<GamacAmaArma> gamacAmaArmaCollection;

    public GamacUnidadMedida() {
    }

    public GamacUnidadMedida(Long id) {
        this.id = id;
    }

    public GamacUnidadMedida(Long id, String nombre, String simbolo, Character habilitado, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.simbolo = simbolo;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public Character getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Character habilitado) {
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
    public Collection<GamacAmaVerificarArma> getGamacAmaVerificarArmaCollection() {
        return gamacAmaVerificarArmaCollection;
    }

    public void setGamacAmaVerificarArmaCollection(Collection<GamacAmaVerificarArma> gamacAmaVerificarArmaCollection) {
        this.gamacAmaVerificarArmaCollection = gamacAmaVerificarArmaCollection;
    }

    @XmlTransient
    public Collection<GamacAmaArma> getGamacAmaArmaCollection() {
        return gamacAmaArmaCollection;
    }

    public void setGamacAmaArmaCollection(Collection<GamacAmaArma> gamacAmaArmaCollection) {
        this.gamacAmaArmaCollection = gamacAmaArmaCollection;
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
        if (!(object instanceof GamacUnidadMedida)) {
            return false;
        }
        GamacUnidadMedida other = (GamacUnidadMedida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacUnidadMedida[ id=" + id + " ]";
    }
    
}
