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
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "SB_RECIBOS_PROCESO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacSbRecibosProceso.findAll", query = "SELECT g FROM GamacSbRecibosProceso g"),
    @NamedQuery(name = "GamacSbRecibosProceso.findById", query = "SELECT g FROM GamacSbRecibosProceso g WHERE g.id = :id"),
    @NamedQuery(name = "GamacSbRecibosProceso.findByCodProceso", query = "SELECT g FROM GamacSbRecibosProceso g WHERE g.codProceso = :codProceso"),
    @NamedQuery(name = "GamacSbRecibosProceso.findByFechaIni", query = "SELECT g FROM GamacSbRecibosProceso g WHERE g.fechaIni = :fechaIni"),
    @NamedQuery(name = "GamacSbRecibosProceso.findByFechaFin", query = "SELECT g FROM GamacSbRecibosProceso g WHERE g.fechaFin = :fechaFin"),
    @NamedQuery(name = "GamacSbRecibosProceso.findByRutaArchivo", query = "SELECT g FROM GamacSbRecibosProceso g WHERE g.rutaArchivo = :rutaArchivo"),
    @NamedQuery(name = "GamacSbRecibosProceso.findByActivo", query = "SELECT g FROM GamacSbRecibosProceso g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacSbRecibosProceso.findByAudLogin", query = "SELECT g FROM GamacSbRecibosProceso g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacSbRecibosProceso.findByAudNumIp", query = "SELECT g FROM GamacSbRecibosProceso g WHERE g.audNumIp = :audNumIp")})
public class GamacSbRecibosProceso implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_SB_RECIBOS_PROCESO")
    @SequenceGenerator(name = "SEQ_GAMAC_SB_RECIBOS_PROCESO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_RECIBOS_PROCESO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_PROCESO")
    private short codProceso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Size(max = 4000)
    @Column(name = "RUTA_ARCHIVO")
    private String rutaArchivo;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procesoId")
    private Collection<GamacSbRecibos> gamacSbRecibosCollection;

    public GamacSbRecibosProceso() {
    }

    public GamacSbRecibosProceso(Long id) {
        this.id = id;
    }

    public GamacSbRecibosProceso(Long id, short codProceso, Date fechaIni, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.codProceso = codProceso;
        this.fechaIni = fechaIni;
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

    public short getCodProceso() {
        return codProceso;
    }

    public void setCodProceso(short codProceso) {
        this.codProceso = codProceso;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
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
    public Collection<GamacSbRecibos> getGamacSbRecibosCollection() {
        return gamacSbRecibosCollection;
    }

    public void setGamacSbRecibosCollection(Collection<GamacSbRecibos> gamacSbRecibosCollection) {
        this.gamacSbRecibosCollection = gamacSbRecibosCollection;
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
        if (!(object instanceof GamacSbRecibosProceso)) {
            return false;
        }
        GamacSbRecibosProceso other = (GamacSbRecibosProceso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacSbRecibosProceso[ id=" + id + " ]";
    }

}
