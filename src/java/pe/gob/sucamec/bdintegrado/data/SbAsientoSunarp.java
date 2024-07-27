/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author locador772.ogtic
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_ASIENTO_SUNARP", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbAsientoSunarp.findAll", query = "SELECT s FROM SbAsientoSunarp s"),
    @NamedQuery(name = "SbAsientoSunarp.findById", query = "SELECT s FROM SbAsientoSunarp s WHERE s.id = :id"),
    @NamedQuery(name = "SbAsientoSunarp.findByNroAsiento", query = "SELECT s FROM SbAsientoSunarp s WHERE s.nroAsiento = :nroAsiento"),
    @NamedQuery(name = "SbAsientoSunarp.findByFecha", query = "SELECT s FROM SbAsientoSunarp s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SbAsientoSunarp.findByActivo", query = "SELECT s FROM SbAsientoSunarp s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbAsientoSunarp.findByAudLogin", query = "SELECT s FROM SbAsientoSunarp s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbAsientoSunarp.findByAudNumIp", query = "SELECT s FROM SbAsientoSunarp s WHERE s.audNumIp = :audNumIp")})
public class SbAsientoSunarp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_ASIENTO_SUNARP")
    @SequenceGenerator(name = "SEQ_SB_ASIENTO_SUNARP", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_ASIENTO_SUNARP", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 100)
    @Column(name = "NRO_ASIENTO")
    private String nroAsiento;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asientoId")
    private List<SbAsientoPersona> sbAsientoPersonaList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asientoId")
    private List<SbAsientoVehiculo> sbAsientoVehiculoList;
    
    @JoinColumn(name = "PARTIDA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPartidaSunarp partidaId;

    public SbAsientoSunarp() {
    }

    public SbAsientoSunarp(Long id) {
        this.id = id;
    }

    public SbAsientoSunarp(Long id, short activo, String audLogin, String audNumIp) {
        this.id = id;
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

    public String getNroAsiento() {
        return nroAsiento;
    }

    public void setNroAsiento(String nroAsiento) {
        this.nroAsiento = nroAsiento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
    public List<SbAsientoPersona> getSbAsientoPersonaList() {
        return sbAsientoPersonaList;
    }

    public void setSbAsientoPersonaList(List<SbAsientoPersona> sbAsientoPersonaList) {
        this.sbAsientoPersonaList = sbAsientoPersonaList;
    }

    public SbPartidaSunarp getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(SbPartidaSunarp partidaId) {
        this.partidaId = partidaId;
    }

    public List<SbAsientoVehiculo> getSbAsientoVehiculoList() {
        return sbAsientoVehiculoList;
    }

    public void setSbAsientoVehiculoList(List<SbAsientoVehiculo> sbAsientoVehiculoList) {
        this.sbAsientoVehiculoList = sbAsientoVehiculoList;
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
        if (!(object instanceof SbAsientoSunarp)) {
            return false;
        }
        SbAsientoSunarp other = (SbAsientoSunarp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbAsientoSunarp[ id=" + id + " ]";
    }
    
}
