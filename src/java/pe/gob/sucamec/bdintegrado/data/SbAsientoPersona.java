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
import pe.gob.sucamec.sel.gamac.data.GamacSbPersona;

/**
 *
 * @author locador772.ogtic
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_ASIENTO_PERSONA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbAsientoPersona.findAll", query = "SELECT s FROM SbAsientoPersona s"),
    @NamedQuery(name = "SbAsientoPersona.findById", query = "SELECT s FROM SbAsientoPersona s WHERE s.id = :id"),
    @NamedQuery(name = "SbAsientoPersona.findByFecha", query = "SELECT s FROM SbAsientoPersona s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SbAsientoPersona.findByActivo", query = "SELECT s FROM SbAsientoPersona s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbAsientoPersona.findByAudLogin", query = "SELECT s FROM SbAsientoPersona s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbAsientoPersona.findByAudNumIp", query = "SELECT s FROM SbAsientoPersona s WHERE s.audNumIp = :audNumIp")})
public class SbAsientoPersona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_ASIENTO_PERSONA")
    @SequenceGenerator(name = "SEQ_SB_ASIENTO_PERSONA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_ASIENTO_PERSONA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
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
    
    @JoinColumn(name = "ASIENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbAsientoSunarp asientoId;
    
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt empresaId;

    public SbAsientoPersona() {
    }

    public SbAsientoPersona(Long id) {
        this.id = id;
    }

    public SbAsientoPersona(Long id, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public SbPersonaGt getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersonaGt empresaId) {
        this.empresaId = empresaId;
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

    public SbAsientoSunarp getAsientoId() {
        return asientoId;
    }

    public void setAsientoId(SbAsientoSunarp asientoId) {
        this.asientoId = asientoId;
    }

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
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
        if (!(object instanceof SbAsientoPersona)) {
            return false;
        }
        SbAsientoPersona other = (SbAsientoPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbAsientoPersona[ id=" + id + " ]";
    }
    
}
