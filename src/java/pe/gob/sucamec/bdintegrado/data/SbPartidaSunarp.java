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
@Table(name = "SB_PARTIDA_SUNARP", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbPartidaSunarp.findAll", query = "SELECT s FROM SbPartidaSunarp s"),
    @NamedQuery(name = "SbPartidaSunarp.findById", query = "SELECT s FROM SbPartidaSunarp s WHERE s.id = :id"),
    @NamedQuery(name = "SbPartidaSunarp.findByLibroRegistral", query = "SELECT s FROM SbPartidaSunarp s WHERE s.libroRegistral = :libroRegistral"),
    @NamedQuery(name = "SbPartidaSunarp.findByPartidaRegistral", query = "SELECT s FROM SbPartidaSunarp s WHERE s.partidaRegistral = :partidaRegistral"),
    @NamedQuery(name = "SbPartidaSunarp.findByFechaRegistral", query = "SELECT s FROM SbPartidaSunarp s WHERE s.fechaRegistral = :fechaRegistral"),
    @NamedQuery(name = "SbPartidaSunarp.findByZonaRegistral", query = "SELECT s FROM SbPartidaSunarp s WHERE s.zonaRegistral = :zonaRegistral"),
    @NamedQuery(name = "SbPartidaSunarp.findByOficinaRegistral", query = "SELECT s FROM SbPartidaSunarp s WHERE s.oficinaRegistral = :oficinaRegistral"),
    @NamedQuery(name = "SbPartidaSunarp.findByEstadoPartida", query = "SELECT s FROM SbPartidaSunarp s WHERE s.estadoPartida = :estadoPartida"),
    @NamedQuery(name = "SbPartidaSunarp.findByValidacionWs", query = "SELECT s FROM SbPartidaSunarp s WHERE s.validacionWs = :validacionWs"),
    @NamedQuery(name = "SbPartidaSunarp.findByEstadoWs", query = "SELECT s FROM SbPartidaSunarp s WHERE s.estadoWs = :estadoWs"),
    @NamedQuery(name = "SbPartidaSunarp.findByFecha", query = "SELECT s FROM SbPartidaSunarp s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SbPartidaSunarp.findByActivo", query = "SELECT s FROM SbPartidaSunarp s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbPartidaSunarp.findByAudLogin", query = "SELECT s FROM SbPartidaSunarp s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbPartidaSunarp.findByAudNumIp", query = "SELECT s FROM SbPartidaSunarp s WHERE s.audNumIp = :audNumIp")})
public class SbPartidaSunarp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_PARTIDA_SUNARP")
    @SequenceGenerator(name = "SEQ_SB_PARTIDA_SUNARP", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_PARTIDA_SUNARP", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 100)
    @Column(name = "LIBRO_REGISTRAL")
    private String libroRegistral;
    @Size(max = 100)
    @Column(name = "PARTIDA_REGISTRAL")
    private String partidaRegistral;
    @Column(name = "FECHA_REGISTRAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistral;
    //@Column(name = "ZONA_REGISTRAL")
    //private TipoBaseGt zonaRegistral;
    //@Column(name = "OFICINA_REGISTRAL")
    //private TipoBaseGt oficinaRegistral;
    @JoinColumn(name = "ZONA_REGISTRAL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt zonaRegistral;
    @JoinColumn(name = "OFICINA_REGISTRAL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt oficinaRegistral;
        
    @Column(name = "ESTADO_PARTIDA")
    private Long estadoPartida;
    @Column(name = "VALIDACION_WS")
    private Long validacionWs;
    @Column(name = "ESTADO_WS")
    private Short estadoWs;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partidaId")
    private List<SbAsientoSunarp> sbAsientoSunarpList;

    public SbPartidaSunarp() {
    }

    public SbPartidaSunarp(Long id) {
        this.id = id;
    }

    public SbPartidaSunarp(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getLibroRegistral() {
        return libroRegistral;
    }

    public void setLibroRegistral(String libroRegistral) {
        this.libroRegistral = libroRegistral;
    }

    public String getPartidaRegistral() {
        return partidaRegistral;
    }

    public void setPartidaRegistral(String partidaRegistral) {
        this.partidaRegistral = partidaRegistral;
    }

    public Date getFechaRegistral() {
        return fechaRegistral;
    }

    public void setFechaRegistral(Date fechaRegistral) {
        this.fechaRegistral = fechaRegistral;
    }

    public TipoBaseGt getZonaRegistral() {
        return zonaRegistral;
    }

    public void setZonaRegistral(TipoBaseGt zonaRegistral) {
        this.zonaRegistral = zonaRegistral;
    }

    public TipoBaseGt getOficinaRegistral() {
        return oficinaRegistral;
    }

    public void setOficinaRegistral(TipoBaseGt oficinaRegistral) {
        this.oficinaRegistral = oficinaRegistral;
    }

    public Long getEstadoPartida() {
        return estadoPartida;
    }

    public void setEstadoPartida(Long estadoPartida) {
        this.estadoPartida = estadoPartida;
    }

    public Long getValidacionWs() {
        return validacionWs;
    }

    public void setValidacionWs(Long validacionWs) {
        this.validacionWs = validacionWs;
    }

    public Short getEstadoWs() {
        return estadoWs;
    }

    public void setEstadoWs(Short estadoWs) {
        this.estadoWs = estadoWs;
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
    public List<SbAsientoSunarp> getSbAsientoSunarpList() {
        return sbAsientoSunarpList;
    }

    public void setSbAsientoSunarpList(List<SbAsientoSunarp> sbAsientoSunarpList) {
        this.sbAsientoSunarpList = sbAsientoSunarpList;
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
        if (!(object instanceof SbPartidaSunarp)) {
            return false;
        }
        SbPartidaSunarp other = (SbPartidaSunarp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbPartidaSunarp[ id=" + id + " ]";
    }
    
}
