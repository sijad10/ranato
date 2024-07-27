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
@Table(name = "SSP_LOCAL_AUTORIZACION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspLocalAutorizacion.findAll", query = "SELECT s FROM SspLocalAutorizacion s"),
    @NamedQuery(name = "SspLocalAutorizacion.findById", query = "SELECT s FROM SspLocalAutorizacion s WHERE s.id = :id"),
    @NamedQuery(name = "SspLocalAutorizacion.findByDireccion", query = "SELECT s FROM SspLocalAutorizacion s WHERE s.direccion = :direccion"),
    @NamedQuery(name = "SspLocalAutorizacion.findByReferencia", query = "SELECT s FROM SspLocalAutorizacion s WHERE s.referencia = :referencia"),
    @NamedQuery(name = "SspLocalAutorizacion.findByGeoLatitud", query = "SELECT s FROM SspLocalAutorizacion s WHERE s.geoLatitud = :geoLatitud"),
    @NamedQuery(name = "SspLocalAutorizacion.findByGeoLongitud", query = "SELECT s FROM SspLocalAutorizacion s WHERE s.geoLongitud = :geoLongitud"),
    @NamedQuery(name = "SspLocalAutorizacion.findByNroFisico", query = "SELECT s FROM SspLocalAutorizacion s WHERE s.nroFisico = :nroFisico"),
    @NamedQuery(name = "SspLocalAutorizacion.findByFecha", query = "SELECT s FROM SspLocalAutorizacion s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspLocalAutorizacion.findByActivo", query = "SELECT s FROM SspLocalAutorizacion s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspLocalAutorizacion.findByAudLogin", query = "SELECT s FROM SspLocalAutorizacion s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspLocalAutorizacion.findByAudNumIp", query = "SELECT s FROM SspLocalAutorizacion s WHERE s.audNumIp = :audNumIp")})
public class SspLocalAutorizacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_LOCAL_AUTORIZACION")
    @SequenceGenerator(name = "SEQ_SSP_LOCAL_AUTORIZACION", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_LOCAL_AUTORIZACION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @Size(max = 200)
    @Column(name = "DIRECCION")
    private String direccion;
    
    @Size(max = 400)
    @Column(name = "REFERENCIA")
    private String referencia;
    
    @Size(max = 30)
    @Column(name = "GEO_LATITUD")
    private String geoLatitud;
    
    @Size(max = 30)
    @Column(name = "GEO_LONGITUD")
    private String geoLongitud;
    
    @Size(max = 9)
    @Column(name = "NRO_FISICO")
    private String nroFisico;
        
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
    
    @JoinColumn(name = "TIPO_UBICACION", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoUbicacionId;
    
    @JoinColumn(name = "DISTRITO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistritoGt distritoId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localId")
    private List<SspLicenciaMunicipal> sspLicenciaMunicipalList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localId")
    private List<SspTipoUsoLocal> sspTipoUsoLocalList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localId")
    private List<SspVisacion> sspVisacionList;
    

    public SspLocalAutorizacion() {
    }

    public SspLocalAutorizacion(Long id) {
        this.id = id;
    }

    public SspLocalAutorizacion(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getGeoLatitud() {
        return geoLatitud;
    }

    public void setGeoLatitud(String geoLatitud) {
        this.geoLatitud = geoLatitud;
    }

    public String getGeoLongitud() {
        return geoLongitud;
    }

    public void setGeoLongitud(String geoLongitud) {
        this.geoLongitud = geoLongitud;
    }

    public String getNroFisico() {
        return nroFisico;
    }

    public void setNroFisico(String nroFisico) {
        this.nroFisico = nroFisico;
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
    public List<SspLicenciaMunicipal> getSspLicenciaMunicipalList() {
        return sspLicenciaMunicipalList;
    }

    public void setSspLicenciaMunicipalList(List<SspLicenciaMunicipal> sspLicenciaMunicipalList) {
        this.sspLicenciaMunicipalList = sspLicenciaMunicipalList;
    }

    @XmlTransient
    public List<SspTipoUsoLocal> getSspTipoUsoLocalList() {
        return sspTipoUsoLocalList;
    }

    public void setSspTipoUsoLocalList(List<SspTipoUsoLocal> sspTipoUsoLocalList) {
        this.sspTipoUsoLocalList = sspTipoUsoLocalList;
    }

    @XmlTransient
    public List<SspVisacion> getSspVisacionList() {
        return sspVisacionList;
    }

    public void setSspVisacionList(List<SspVisacion> sspVisacionList) {
        this.sspVisacionList = sspVisacionList;
    }
    
    

    public TipoBaseGt getTipoUbicacionId() {
        return tipoUbicacionId;
    }

    public void setTipoUbicacionId(TipoBaseGt tipoUbicacionId) {
        this.tipoUbicacionId = tipoUbicacionId;
    }

    public SbDistritoGt getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(SbDistritoGt distritoId) {
        this.distritoId = distritoId;
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
        if (!(object instanceof SspLocalAutorizacion)) {
            return false;
        }
        SspLocalAutorizacion other = (SspLocalAutorizacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
        //return "pe.gob.sucamec.bdintegrado.data.SspLocalAutorizacion[ id=" + id + " ]";
    }
    
    
    
    
}
