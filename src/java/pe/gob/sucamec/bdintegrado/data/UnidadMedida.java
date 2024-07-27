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
import javax.persistence.ManyToMany;
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
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "UNIDAD_MEDIDA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadMedida.findAll", query = "SELECT u FROM UnidadMedida u"),
    @NamedQuery(name = "UnidadMedida.findById", query = "SELECT u FROM UnidadMedida u WHERE u.id = :id"),
    @NamedQuery(name = "UnidadMedida.findByNombre", query = "SELECT u FROM UnidadMedida u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "UnidadMedida.findBySimbolo", query = "SELECT u FROM UnidadMedida u WHERE u.simbolo = :simbolo"),
    @NamedQuery(name = "UnidadMedida.findByHabilitado", query = "SELECT u FROM UnidadMedida u WHERE u.habilitado = :habilitado"),
    @NamedQuery(name = "UnidadMedida.findByActivo", query = "SELECT u FROM UnidadMedida u WHERE u.activo = :activo"),
    @NamedQuery(name = "UnidadMedida.findByAudLogin", query = "SELECT u FROM UnidadMedida u WHERE u.audLogin = :audLogin"),
    @NamedQuery(name = "UnidadMedida.findByAudNumIp", query = "SELECT u FROM UnidadMedida u WHERE u.audNumIp = :audNumIp")})
public class UnidadMedida implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_UNIDAD_MEDIDA")
    @SequenceGenerator(name = "SEQ_UNIDAD_MEDIDA", schema = "BDINTEGRADO", sequenceName = "SEQ_UNIDAD_MEDIDA", allocationSize = 1)
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
    @ManyToMany(mappedBy = "unidadMedidaList")
    private List<EppExplosivo> eppExplosivoList;
    @ManyToMany(mappedBy = "unidadMedidaList")
    private List<EppNombrecomercial> eppNombrecomercialList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unidadMedidaId")
    private List<EppLibroPerdida> eppLibroPerdidaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unidadMedidaId")
    private List<EppImpExpExplosivo> eppImpExpExplosivoList;

    public UnidadMedida() {
    }

    public UnidadMedida(Long id) {
        this.id = id;
    }

    public UnidadMedida(Long id, String nombre, String simbolo, Character habilitado, short activo, String audLogin, String audNumIp) {
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
    public List<EppExplosivo> getEppExplosivoList() {
        return eppExplosivoList;
    }

    public void setEppExplosivoList(List<EppExplosivo> eppExplosivoList) {
        this.eppExplosivoList = eppExplosivoList;
    }

    @XmlTransient
    public List<EppNombrecomercial> getEppNombrecomercialList() {
        return eppNombrecomercialList;
    }

    public void setEppNombrecomercialList(List<EppNombrecomercial> eppNombrecomercialList) {
        this.eppNombrecomercialList = eppNombrecomercialList;
    }

    @XmlTransient
    public List<EppLibroPerdida> getEppLibroPerdidaList() {
        return eppLibroPerdidaList;
    }

    public void setEppLibroPerdidaList(List<EppLibroPerdida> eppLibroPerdidaList) {
        this.eppLibroPerdidaList = eppLibroPerdidaList;
    }

    @XmlTransient
    public List<EppImpExpExplosivo> getEppImpExpExplosivoList() {
        return eppImpExpExplosivoList;
    }

    public void setEppImpExpExplosivoList(List<EppImpExpExplosivo> eppImpExpExplosivoList) {
        this.eppImpExpExplosivoList = eppImpExpExplosivoList;
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
        if (!(object instanceof UnidadMedida)) {
            return false;
        }
        UnidadMedida other = (UnidadMedida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.UnidadMedida[ id=" + id + " ]";
    }
    
}
