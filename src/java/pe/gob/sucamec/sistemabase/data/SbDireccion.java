/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.data;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_DIRECCION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbDireccion.findAll", query = "SELECT s FROM SbDireccion s"),
    @NamedQuery(name = "SbDireccion.findById", query = "SELECT s FROM SbDireccion s WHERE s.id = :id"),
    @NamedQuery(name = "SbDireccion.findByDireccion", query = "SELECT s FROM SbDireccion s WHERE s.direccion = :direccion"),
    @NamedQuery(name = "SbDireccion.findByNumero", query = "SELECT s FROM SbDireccion s WHERE s.numero = :numero"),
    @NamedQuery(name = "SbDireccion.findByReferencia", query = "SELECT s FROM SbDireccion s WHERE s.referencia = :referencia"),
    @NamedQuery(name = "SbDireccion.findByGeoLat", query = "SELECT s FROM SbDireccion s WHERE s.geoLat = :geoLat"),
    @NamedQuery(name = "SbDireccion.findByGeoLong", query = "SELECT s FROM SbDireccion s WHERE s.geoLong = :geoLong"),
    @NamedQuery(name = "SbDireccion.findByActivo", query = "SELECT s FROM SbDireccion s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbDireccion.findByAudLogin", query = "SELECT s FROM SbDireccion s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbDireccion.findByAudNumIp", query = "SELECT s FROM SbDireccion s WHERE s.audNumIp = :audNumIp")})
public class SbDireccion implements Serializable {
    private static final Long serialVersionUID = 1L;
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
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "DISTRITO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbDistrito distritoId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaId;
    @JoinColumn(name = "ZONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo zonaId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoId;
    @JoinColumn(name = "VIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo viaId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbTipo areaId;
    @JoinColumn(name = "PAIS_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPais paisId;

    public SbDireccion() {
    }

    public SbDireccion(Long id) {
        this.id = id;
    }

    public SbDireccion(Long id, String direccion, short activo, String audLogin, String audNumIp) {
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

    public SbDistrito getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(SbDistrito distritoId) {
        this.distritoId = distritoId;
    }

    public SbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersona personaId) {
        this.personaId = personaId;
    }

    public SbTipo getZonaId() {
        return zonaId;
    }

    public void setZonaId(SbTipo zonaId) {
        this.zonaId = zonaId;
    }

    public SbTipo getTipoId() {
        return tipoId;
    }

    public void setTipoId(SbTipo tipoId) {
        this.tipoId = tipoId;
    }

    public SbTipo getViaId() {
        return viaId;
    }

    public void setViaId(SbTipo viaId) {
        this.viaId = viaId;
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
        if (!(object instanceof SbDireccion)) {
            return false;
        }
        SbDireccion other = (SbDireccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbDireccion[ id=" + id + " ]";
    }

    public SbTipo getAreaId() {
        return areaId;
    }

    public void setAreaId(SbTipo areaId) {
        this.areaId = areaId;
    }

    public SbPais getPaisId() {
        return paisId;
    }

    public void setPaisId(SbPais paisId) {
        this.paisId = paisId;
    }
    
}
