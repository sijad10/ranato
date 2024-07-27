/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_LOCAL", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspLocal.findAll", query = "SELECT s FROM SspLocal s"),
    @NamedQuery(name = "SspLocal.findById", query = "SELECT s FROM SspLocal s WHERE s.id = :id"),
    @NamedQuery(name = "SspLocal.findByNombreLocal", query = "SELECT s FROM SspLocal s WHERE s.nombreLocal = :nombreLocal"),
    @NamedQuery(name = "SspLocal.findByDireccion", query = "SELECT s FROM SspLocal s WHERE s.direccion = :direccion"),
    @NamedQuery(name = "SspLocal.findByReferencia", query = "SELECT s FROM SspLocal s WHERE s.referencia = :referencia"),
    @NamedQuery(name = "SspLocal.findByAforo", query = "SELECT s FROM SspLocal s WHERE s.aforo = :aforo"),
    @NamedQuery(name = "SspLocal.findByGeoLat", query = "SELECT s FROM SspLocal s WHERE s.geoLat = :geoLat"),
    @NamedQuery(name = "SspLocal.findByGeoLong", query = "SELECT s FROM SspLocal s WHERE s.geoLong = :geoLong"),
    @NamedQuery(name = "SspLocal.findByActivo", query = "SELECT s FROM SspLocal s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspLocal.findByAudLogin", query = "SELECT s FROM SspLocal s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspLocal.findByAudNumIp", query = "SELECT s FROM SspLocal s WHERE s.audNumIp = :audNumIp")})
public class SspLocal implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_LOCAL")
    @SequenceGenerator(name = "SEQ_SSP_LOCAL", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_LOCAL", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 200)
    @Column(name = "NOMBRE_LOCAL")
    private String nombreLocal;
    @Size(max = 1400)
    @Column(name = "CARACTERISTICA")
    private String caracteristicas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DIRECCION")
    private String direccion;
    @Size(max = 400)
    @Column(name = "REFERENCIA")
    private String referencia;
    @Column(name = "AFORO")
    private Long aforo;
    @Size(max = 30)
    @Column(name = "GEO_LAT")
    private String geoLat;
    @Size(max = 30)
    @Column(name = "GEO_LONG")
    private String geoLong;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localId")
    private List<SspProgramacion> sspProgramacionList;
    @JoinColumn(name = "TIPO_UBICACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoUbicacionId;
    @JoinColumn(name = "DISTRITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistritoGt distritoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localId")
    private List<SspLocalContacto> sspLocalContactoList;
    @Column(name = "FECHA_REG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReg;

    public SspLocal() {
    }

    public SspLocal(Long id) {
        this.id = id;
    }

    public SspLocal(Long id, String direccion, short activo, String audLogin, String audNumIp) {
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

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
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

    public Long getAforo() {
        return aforo;
    }

    public void setAforo(Long aforo) {
        this.aforo = aforo;
    }

    public String getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(String geoLat) {
        this.geoLat = geoLat;
    }

    public String getGeoLong() {
        return geoLong;
    }

    public void setGeoLong(String geoLong) {
        this.geoLong = geoLong;
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
    public List<SspProgramacion> getSspProgramacionList() {
        return sspProgramacionList;
    }

    public void setSspProgramacionList(List<SspProgramacion> sspProgramacionList) {
        this.sspProgramacionList = sspProgramacionList;
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

    @XmlTransient
    public List<SspLocalContacto> getSspLocalContactoList() {
        return sspLocalContactoList;
    }

    public void setSspLocalContactoList(List<SspLocalContacto> sspLocalContactoList) {
        this.sspLocalContactoList = sspLocalContactoList;
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
        if (!(object instanceof SspLocal)) {
            return false;
        }
        SspLocal other = (SspLocal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspLocal[ id=" + id + " ]";
    }

    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }
    
}
