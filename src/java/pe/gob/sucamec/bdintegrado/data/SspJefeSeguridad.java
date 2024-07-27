/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

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
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author mpalomino
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_JEFE_SEGURIDAD", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspJefeSeguridad.findAll", query = "SELECT s FROM SspJefeSeguridad s"),
    @NamedQuery(name = "SspJefeSeguridad.findById", query = "SELECT s FROM SspJefeSeguridad s WHERE s.id = :id"),
    @NamedQuery(name = "SspJefeSeguridad.findByCargo", query = "SELECT s FROM SspJefeSeguridad s WHERE s.cargo = :cargo"),
    @NamedQuery(name = "SspJefeSeguridad.findByPrincipal", query = "SELECT s FROM SspJefeSeguridad s WHERE s.principal = :principal"),
    @NamedQuery(name = "SspJefeSeguridad.findByActivo", query = "SELECT s FROM SspJefeSeguridad s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspJefeSeguridad.findByAudLogin", query = "SELECT s FROM SspJefeSeguridad s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspJefeSeguridad.findByAudNumIp", query = "SELECT s FROM SspJefeSeguridad s WHERE s.audNumIp = :audNumIp")})
public class SspJefeSeguridad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_JEFE_SEGURIDAD")
    @SequenceGenerator(name = "SEQ_SSP_JEFE_SEGURIDAD", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_JEFE_SEGURIDAD", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 30)
    @Column(name = "CARGO")
    private String cargo;
    @Column(name = "PRINCIPAL")
    private Short principal;
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
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;

    public SspJefeSeguridad() {
    }

    public SspJefeSeguridad(Long id) {
        this.id = id;
    }

    public SspJefeSeguridad(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Short getPrincipal() {
        return principal;
    }

    public void setPrincipal(Short principal) {
        this.principal = principal;
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

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
    }

    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
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
        if (!(object instanceof SspJefeSeguridad)) {
            return false;
        }
        SspJefeSeguridad other = (SspJefeSeguridad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspJefeSeguridad[ id=" + id + " ]";
    }
    
}
