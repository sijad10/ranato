/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
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
@Table(name = "SSP_LICENCIA_MUNICIPAL", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspLicenciaMunicipal.findAll", query = "SELECT s FROM SspLicenciaMunicipal s"),
    @NamedQuery(name = "SspLicenciaMunicipal.findById", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.id = :id"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByNumeroLicencia", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.numeroLicencia = :numeroLicencia"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByTipoMunicipalidad", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.tipoMunicipalidad = :tipoMunicipalidad"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByMunicipalidadId", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.municipalidadId = :municipalidadId"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByFechaIni", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByFechaFin", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByIndeterminado", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.indeterminado = :indeterminado"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByGiroComercial", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.giroComercial = :giroComercial"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByNombreArchivo", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.nombreArchivo = :nombreArchivo"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByFecha", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByValidacionWs", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.validacionWs = :validacionWs"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByEstadoWs", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.estadoWs = :estadoWs"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByActivo", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByAudLogin", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspLicenciaMunicipal.findByAudNumIp", query = "SELECT s FROM SspLicenciaMunicipal s WHERE s.audNumIp = :audNumIp")})
public class SspLicenciaMunicipal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_LICENCIA_MUNICIPAL")
    @SequenceGenerator(name = "SEQ_SSP_LICENCIA_MUNICIPAL", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_LICENCIA_MUNICIPAL", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @Size(max = 50)
    @Column(name = "NUMERO_LICENCIA")
    private String numeroLicencia;
    
    @JoinColumn(name = "TIPO_MUNICIPALIDAD", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoMunicipalidad;
        
    @JoinColumn(name = "MUNICIPALIDAD_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt municipalidadId;
    
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    
    @Column(name = "INDETERMINADO")
    private Short indeterminado;
    
    @Size(max = 100)
    @Column(name = "GIRO_COMERCIAL")
    private String giroComercial;
    
    @Size(max = 20)
    @Column(name = "NOMBRE_ARCHIVO")
    private String nombreArchivo;
    
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    @Column(name = "VALIDACION_WS")
    private Long validacionWs;
    
    @Column(name = "ESTADO_WS")
    private Short estadoWs;
    
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
    
    @JoinColumn(name = "LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspLocalAutorizacion localId;

    public SspLicenciaMunicipal() {
    }

    public SspLicenciaMunicipal(Long id) {
        this.id = id;
    }

    public SspLicenciaMunicipal(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public TipoBaseGt getTipoMunicipalidad() {
        return tipoMunicipalidad;
    }

    public void setTipoMunicipalidad(TipoBaseGt tipoMunicipalidad) {
        this.tipoMunicipalidad = tipoMunicipalidad;
    }

    public TipoBaseGt getMunicipalidadId() {
        return municipalidadId;
    }

    public void setMunicipalidadId(TipoBaseGt municipalidadId) {
        this.municipalidadId = municipalidadId;
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

    public Short getIndeterminado() {
        return indeterminado;
    }

    public void setIndeterminado(Short indeterminado) {
        this.indeterminado = indeterminado;
    }

    public String getGiroComercial() {
        return giroComercial;
    }

    public void setGiroComercial(String giroComercial) {
        this.giroComercial = giroComercial;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public SspLocalAutorizacion getLocalId() {
        return localId;
    }

    public void setLocalId(SspLocalAutorizacion localId) {
        this.localId = localId;
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
        if (!(object instanceof SspLicenciaMunicipal)) {
            return false;
        }
        SspLicenciaMunicipal other = (SspLicenciaMunicipal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspLicenciaMunicipal[ id=" + id + " ]";
    }
    
}
