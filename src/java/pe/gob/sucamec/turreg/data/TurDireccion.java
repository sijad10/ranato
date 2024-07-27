/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.turreg.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import pe.gob.sucamec.sistemabase.data.SbDistrito;
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TUR_DIRECCION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TurDireccion.findAll", query = "SELECT t FROM TurDireccion t"),
    @NamedQuery(name = "TurDireccion.findById", query = "SELECT t FROM TurDireccion t WHERE t.id = :id"),
    @NamedQuery(name = "TurDireccion.findByDireccion", query = "SELECT t FROM TurDireccion t WHERE t.direccion = :direccion"),
    @NamedQuery(name = "TurDireccion.findByNumero", query = "SELECT t FROM TurDireccion t WHERE t.numero = :numero"),
    @NamedQuery(name = "TurDireccion.findByReferencia", query = "SELECT t FROM TurDireccion t WHERE t.referencia = :referencia"),
    @NamedQuery(name = "TurDireccion.findByActivo", query = "SELECT t FROM TurDireccion t WHERE t.activo = :activo"),
    @NamedQuery(name = "TurDireccion.findByAudLogin", query = "SELECT t FROM TurDireccion t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TurDireccion.findByAudNumIp", query = "SELECT t FROM TurDireccion t WHERE t.audNumIp = :audNumIp")})
public class TurDireccion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TUR_DIRECCION")
    @SequenceGenerator(name = "SEQ_TUR_DIRECCION", schema = "BDINTEGRADO", sequenceName = "SEQ_TUR_DIRECCION", allocationSize = 1)
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
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TurPersona personaId;
    @JoinColumn(name = "ZONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase zonaId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase tipoId;
    @JoinColumn(name = "VIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase viaId;
    @JoinColumn(name = "DISTRITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistrito distritoId;

    public TurDireccion() {
    }

    public TurDireccion(Long id) {
        this.id = id;
    }

    public TurDireccion(Long id, String direccion, short activo, String audLogin, String audNumIp) {
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

    public TurPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(TurPersona personaId) {
        this.personaId = personaId;
    }

    public TipoBase getZonaId() {
        return zonaId;
    }

    public void setZonaId(TipoBase zonaId) {
        this.zonaId = zonaId;
    }

    public TipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBase tipoId) {
        this.tipoId = tipoId;
    }

    public TipoBase getViaId() {
        return viaId;
    }

    public void setViaId(TipoBase viaId) {
        this.viaId = viaId;
    }

    public SbDistrito getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(SbDistrito distritoId) {
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
        if (!(object instanceof TurDireccion)) {
            return false;
        }
        TurDireccion other = (TurDireccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.turreg.data.TurDireccion[ id=" + id + " ]";
    }
    
}
