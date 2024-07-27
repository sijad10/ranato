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
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_PAIS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsultaSbPais.findAll", query = "SELECT g FROM ConsultaSbPais g"),
    @NamedQuery(name = "ConsultaSbPais.findById", query = "SELECT g FROM ConsultaSbPais g WHERE g.id = :id"),
    @NamedQuery(name = "ConsultaSbPais.findByNombre", query = "SELECT g FROM ConsultaSbPais g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "ConsultaSbPais.findByActivo", query = "SELECT g FROM ConsultaSbPais g WHERE g.activo = :activo"),
    @NamedQuery(name = "ConsultaSbPais.findByAudLogin", query = "SELECT g FROM ConsultaSbPais g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "ConsultaSbPais.findByAudNumIp", query = "SELECT g FROM ConsultaSbPais g WHERE g.audNumIp = :audNumIp")})
public class ConsultaSbPais implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_SB_PAIS")
    @SequenceGenerator(name = "SEQ_CONSULTA_SB_PAIS", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_PAIS", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "NOMBRE")
    private String nombre;
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
    /*@ManyToMany(mappedBy = "gamacSbPaisCollection")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection;
    @OneToMany(mappedBy = "paisFabricanteId")
    private Collection<GamacAmaMunicion> gamacAmaMunicionCollection;*/

    public ConsultaSbPais() {
    }

    public ConsultaSbPais(Long id) {
        this.id = id;
    }

    public ConsultaSbPais(Long id, String nombre, short activo, String audLogin, String audNumIp) {
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

    /*@XmlTransient
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection() {
        return gamacEppRegistroCollection;
    }

    public void setGamacEppRegistroCollection(Collection<GamacEppRegistro> gamacEppRegistroCollection) {
        this.gamacEppRegistroCollection = gamacEppRegistroCollection;
    }

    @XmlTransient
    public Collection<GamacAmaMunicion> getGamacAmaMunicionCollection() {
        return gamacAmaMunicionCollection;
    }

    public void setGamacAmaMunicionCollection(Collection<GamacAmaMunicion> gamacAmaMunicionCollection) {
        this.gamacAmaMunicionCollection = gamacAmaMunicionCollection;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConsultaSbPais)) {
            return false;
        }
        ConsultaSbPais other = (ConsultaSbPais) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.renagi.data.ConsultaSbPais[ id=" + id + " ]";
    }
    
}
