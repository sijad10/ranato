/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "SSP_ALMACEN", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspAlmacen.findAll", query = "SELECT s FROM SspAlmacen s"),
    @NamedQuery(name = "SspAlmacen.findById", query = "SELECT s FROM SspAlmacen s WHERE s.id = :id"),
    @NamedQuery(name = "SspAlmacen.findByFecha", query = "SELECT s FROM SspAlmacen s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspAlmacen.findByActivo", query = "SELECT s FROM SspAlmacen s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspAlmacen.findByAudLogin", query = "SELECT s FROM SspAlmacen s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspAlmacen.findByAudNumIp", query = "SELECT s FROM SspAlmacen s WHERE s.audNumIp = :audNumIp"),
    @NamedQuery(name = "SspAlmacen.findByCantidad", query = "SELECT s FROM SspAlmacen s WHERE s.cantidad = :cantidad")})
public class SspAlmacen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_ALMACEN")
    @SequenceGenerator(name = "SEQ_SSP_ALMACEN", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_ALMACEN", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @Basic(optional = false)
    @NotNull
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
    
    @Column(name = "CANTIDAD")
    private BigInteger cantidad;
    
    @JoinColumn(name = "TIPO_ALMACEN", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad tipoAlmacenId;
    
    @JoinColumn(name = "LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspLocalAutorizacion localId;
    
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro sspRegistroId;

    public SspAlmacen() {
    }

    public SspAlmacen(Long id) {
        this.id = id;
    }

    public SspAlmacen(Long id, Date fecha, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fecha = fecha;
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

    public BigInteger getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigInteger cantidad) {
        this.cantidad = cantidad;
    }

    public TipoSeguridad getTipoAlmacenId() {
        return tipoAlmacenId;
    }

    public void setTipoAlmacenId(TipoSeguridad tipoAlmacenId) {
        this.tipoAlmacenId = tipoAlmacenId;
    }

    public SspLocalAutorizacion getLocalId() {
        return localId;
    }

    public void setLocalId(SspLocalAutorizacion localId) {
        this.localId = localId;
    }

    public SspRegistro getSspRegistroId() {
        return sspRegistroId;
    }

    public void setSspRegistroId(SspRegistro sspRegistroId) {
        this.sspRegistroId = sspRegistroId;
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
        if (!(object instanceof SspAlmacen)) {
            return false;
        }
        SspAlmacen other = (SspAlmacen) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspAlmacen[ id=" + id + " ]";
    }
    
}
