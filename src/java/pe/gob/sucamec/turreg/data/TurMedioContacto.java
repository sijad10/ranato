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
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TUR_MEDIO_CONTACTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TurMedioContacto.findAll", query = "SELECT t FROM TurMedioContacto t"),
    @NamedQuery(name = "TurMedioContacto.findById", query = "SELECT t FROM TurMedioContacto t WHERE t.id = :id"),
    @NamedQuery(name = "TurMedioContacto.findByValorMedContac", query = "SELECT t FROM TurMedioContacto t WHERE t.valorMedContac = :valorMedContac"),
    @NamedQuery(name = "TurMedioContacto.findByDescripcion", query = "SELECT t FROM TurMedioContacto t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TurMedioContacto.findByActivo", query = "SELECT t FROM TurMedioContacto t WHERE t.activo = :activo"),
    @NamedQuery(name = "TurMedioContacto.findByAudLogin", query = "SELECT t FROM TurMedioContacto t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TurMedioContacto.findByAudNumIp", query = "SELECT t FROM TurMedioContacto t WHERE t.audNumIp = :audNumIp")})
public class TurMedioContacto implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TUR_MEDIO_CONTACTO")
    @SequenceGenerator(name = "SEQ_TUR_MEDIO_CONTACTO", schema = "BDINTEGRADO", sequenceName = "SEQ_TUR_MEDIO_CONTACTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "VALOR_MED_CONTAC")
    private String valorMedContac;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
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
    @JoinColumn(name = "TUR_PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TurPersona turPersonaId;
    @JoinColumn(name = "TIPO_MEDIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase tipoMedioId;

    public TurMedioContacto() {
    }

    public TurMedioContacto(Long id) {
        this.id = id;
    }

    public TurMedioContacto(Long id, String valorMedContac, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.valorMedContac = valorMedContac;
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

    public String getValorMedContac() {
        return valorMedContac;
    }

    public void setValorMedContac(String valorMedContac) {
        this.valorMedContac = valorMedContac;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public TurPersona getTurPersonaId() {
        return turPersonaId;
    }

    public void setTurPersonaId(TurPersona turPersonaId) {
        this.turPersonaId = turPersonaId;
    }

    public TipoBase getTipoMedioId() {
        return tipoMedioId;
    }

    public void setTipoMedioId(TipoBase tipoMedioId) {
        this.tipoMedioId = tipoMedioId;
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
        if (!(object instanceof TurMedioContacto)) {
            return false;
        }
        TurMedioContacto other = (TurMedioContacto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.turreg.data.TurMedioContacto[ id=" + id + " ]";
    }
    
}
