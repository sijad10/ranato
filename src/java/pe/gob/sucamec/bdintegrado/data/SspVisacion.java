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
@Table(name = "SSP_VISACION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspVisacion.findAll", query = "SELECT s FROM SspVisacion s"),
    @NamedQuery(name = "SspVisacion.findById", query = "SELECT s FROM SspVisacion s WHERE s.id = :id"),    
    //@NamedQuery(name = "SspVisacion.findByTipoVizacionId", query = "SELECT s FROM SspVisacion s WHERE s.tipoVizacionId.id = :id"),    
    @NamedQuery(name = "SspVisacion.findByNroVisacion", query = "SELECT s FROM SspVisacion s WHERE s.nroVisacion = :nroVisacion"),
    @NamedQuery(name = "SspVisacion.findByFechaVisacion", query = "SELECT s FROM SspVisacion s WHERE s.fechaVisacion = :fechaVisacion"),
    @NamedQuery(name = "SspVisacion.findByDetalle", query = "SELECT s FROM SspVisacion s WHERE s.detalle = :detalle"),
    @NamedQuery(name = "SspVisacion.findByNroOficio", query = "SELECT s FROM SspVisacion s WHERE s.nroOficio = :nroOficio"),
    @NamedQuery(name = "SspVisacion.findByFechaOficio", query = "SELECT s FROM SspVisacion s WHERE s.fechaOficio = :fechaOficio"),
    @NamedQuery(name = "SspVisacion.findByActivo", query = "SELECT s FROM SspVisacion s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspVisacion.findByAudLogin", query = "SELECT s FROM SspVisacion s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspVisacion.findByAudNumIp", query = "SELECT s FROM SspVisacion s WHERE s.audNumIp = :audNumIp")})
public class SspVisacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_VISACION")
    @SequenceGenerator(name = "SEQ_SSP_VISACION", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_VISACION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @JoinColumn(name = "LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspLocalAutorizacion localId;
        
    @JoinColumn(name = "TIPO_VIZACION", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoVizacionId;    
    
    @Column(name = "NRO_VISACION")
    private Integer nroVisacion;
    
    @Column(name = "FECHA_VISACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVisacion;
    
    @Column(name = "DETALLE")
    private String detalle;
    
    @Column(name = "NRO_OFICIO")
    private String nroOficio;
    
    @Column(name = "FECHA_OFICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaOficio;
    
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private short activo;
    
    @Basic(optional = false)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    
    @Basic(optional = false)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;

    public SspVisacion() {
    }

    public SspVisacion(Long id) {
        this.id = id;
    }

    public SspVisacion(Long id, short activo, String audLogin, String audNumIp) {
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

    public SspLocalAutorizacion getLocalId() {
        return localId;
    }

    public void setLocalId(SspLocalAutorizacion localId) {
        this.localId = localId;
    }

    public Integer getNroVisacion() {
        return nroVisacion;
    }

    public void setNroVisacion(Integer nroVisacion) {
        this.nroVisacion = nroVisacion;
    }

    public Date getFechaVisacion() {
        return fechaVisacion;
    }

    public void setFechaVisacion(Date fechaVisacion) {
        this.fechaVisacion = fechaVisacion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getNroOficio() {
        return nroOficio;
    }

    public void setNroOficio(String nroOficio) {
        this.nroOficio = nroOficio;
    }

    public Date getFechaOficio() {
        return fechaOficio;
    }

    public void setFechaOficio(Date fechaOficio) {
        this.fechaOficio = fechaOficio;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public TipoBaseGt getTipoVizacionId() {
        return tipoVizacionId;
    }

    public void setTipoVizacionId(TipoBaseGt tipoVizacionId) {
        this.tipoVizacionId = tipoVizacionId;
    }   
    

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SspVisacion)) {
            return false;
        }
        SspVisacion other = (SspVisacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication2.SspVisacion[ id=" + id + " ]";
    }
    
}
