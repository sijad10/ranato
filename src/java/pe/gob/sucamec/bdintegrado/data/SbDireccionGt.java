/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_DIRECCION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbDireccionGt.findAll", query = "SELECT s FROM SbDireccionGt s"),
    @NamedQuery(name = "SbDireccionGt.findById", query = "SELECT s FROM SbDireccionGt s WHERE s.id = :id"),
    @NamedQuery(name = "SbDireccionGt.findByDireccion", query = "SELECT s FROM SbDireccionGt s WHERE s.direccion = :direccion"),
    @NamedQuery(name = "SbDireccionGt.findByNumero", query = "SELECT s FROM SbDireccionGt s WHERE s.numero = :numero"),
    @NamedQuery(name = "SbDireccionGt.findByReferencia", query = "SELECT s FROM SbDireccionGt s WHERE s.referencia = :referencia"),
    @NamedQuery(name = "SbDireccionGt.findByGeoLat", query = "SELECT s FROM SbDireccionGt s WHERE s.geoLat = :geoLat"),
    @NamedQuery(name = "SbDireccionGt.findByGeoLong", query = "SELECT s FROM SbDireccionGt s WHERE s.geoLong = :geoLong"),
    @NamedQuery(name = "SbDireccionGt.findByActivo", query = "SELECT s FROM SbDireccionGt s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbDireccionGt.findByAudLogin", query = "SELECT s FROM SbDireccionGt s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbDireccionGt.findByAudNumIp", query = "SELECT s FROM SbDireccionGt s WHERE s.audNumIp = :audNumIp")})
public class SbDireccionGt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_DIRECCION")
    @SequenceGenerator(name = "SEQ_SB_DIRECCION", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_DIRECCION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "DIRECCION")
    private String direccion;
    @Size(max = 20)
    @Column(name = "NUMERO")
    private String numero;
    @Size(max = 300)
    @Column(name = "REFERENCIA")
    private String referencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "GEO_LAT")
    private Double geoLat;
    @Column(name = "GEO_LONG")
    private Double geoLong;
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
    @JoinColumn(name = "VIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt viaId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoId;
    @JoinColumn(name = "ZONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt zonaId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    @JoinColumn(name = "DISTRITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistritoGt distritoId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt areaId;
    @JoinColumn(name = "PAIS_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPaisGt paisId;
    @ManyToMany(mappedBy = "sbDireccionInpeList")
    private List<TipoBaseGt> regionList;

    public SbDireccionGt() {
    }

    public SbDireccionGt(Long id) {
        this.id = id;
    }

    public SbDireccionGt(Long id, String direccion, short activo, String audLogin, String audNumIp) {
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Double getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(Double geoLat) {
        this.geoLat = geoLat;
    }

    public Double getGeoLong() {
        return geoLong;
    }

    public void setGeoLong(Double geoLong) {
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

    public TipoBaseGt getViaId() {
        return viaId;
    }

    public void setViaId(TipoBaseGt viaId) {
        this.viaId = viaId;
    }

    public TipoBaseGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBaseGt tipoId) {
        this.tipoId = tipoId;
    }

    public TipoBaseGt getZonaId() {
        return zonaId;
    }

    public void setZonaId(TipoBaseGt zonaId) {
        this.zonaId = zonaId;
    }

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
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
        if (!(object instanceof SbDireccionGt)) {
            return false;
        }
        SbDireccionGt other = (SbDireccionGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbDireccionGt[ id=" + id + " ]";
    }

    public TipoBaseGt getAreaId() {
        return areaId;
    }

    public void setAreaId(TipoBaseGt areaId) {
        this.areaId = areaId;
    }
    
    public SbPaisGt getPaisId() {
        return paisId;
    }

    public void setPaisId(SbPaisGt paisId) {
        this.paisId = paisId;
    }
    
    @XmlTransient
    public List<TipoBaseGt> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<TipoBaseGt> regionList) {
        this.regionList = regionList;
    }
    
}
