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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import pe.gob.sucamec.sistemabase.data.SbDistrito;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_VEHICULO_DIRECS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppVehiculoDirecs.findAll", query = "SELECT e FROM EppVehiculoDirecs e"),
    @NamedQuery(name = "EppVehiculoDirecs.findById", query = "SELECT e FROM EppVehiculoDirecs e WHERE e.id = :id"),
    @NamedQuery(name = "EppVehiculoDirecs.findByDireccion", query = "SELECT e FROM EppVehiculoDirecs e WHERE e.direccion = :direccion"),
    @NamedQuery(name = "EppVehiculoDirecs.findByLatitud", query = "SELECT e FROM EppVehiculoDirecs e WHERE e.latitud = :latitud"),
    @NamedQuery(name = "EppVehiculoDirecs.findByLongitud", query = "SELECT e FROM EppVehiculoDirecs e WHERE e.longitud = :longitud"),
    @NamedQuery(name = "EppVehiculoDirecs.findByActivo", query = "SELECT e FROM EppVehiculoDirecs e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppVehiculoDirecs.findByAudLogin", query = "SELECT e FROM EppVehiculoDirecs e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppVehiculoDirecs.findByAudNumIp", query = "SELECT e FROM EppVehiculoDirecs e WHERE e.audNumIp = :audNumIp")})
public class EppVehiculoDirecs implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_VEHICULO_DIRECS")
    @SequenceGenerator(name = "SEQ_EPP_VEHICULO_DIRECS", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_VEHICULO_DIRECS", allocationSize = 1)
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
    @JoinColumn(name = "UBIGEO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistrito ubigeoId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppRegistro registroId;

    public EppVehiculoDirecs() {
    }

    public EppVehiculoDirecs(Long id) {
        this.id = id;
    }

    public EppVehiculoDirecs(Long id, String direccion, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.direccion = direccion;
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

    public SbDistrito getUbigeoId() {
        return ubigeoId;
    }

    public void setUbigeoId(SbDistrito ubigeoId) {
        this.ubigeoId = ubigeoId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
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
        if (!(object instanceof EppVehiculoDirecs)) {
            return false;
        }
        EppVehiculoDirecs other = (EppVehiculoDirecs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppVehiculoDirecs[ id=" + id + " ]";
    }
    
}
