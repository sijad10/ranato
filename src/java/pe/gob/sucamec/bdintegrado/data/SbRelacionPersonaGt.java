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

/**
 *
 * @author mespinoza
 */
@Entity
@Table(name = "SB_RELACION_PERSONA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbRelacionPersonaGt.findAll", query = "SELECT s FROM SbRelacionPersonaGt s"),
    @NamedQuery(name = "SbRelacionPersonaGt.findById", query = "SELECT s FROM SbRelacionPersonaGt s WHERE s.id = :id"),
    @NamedQuery(name = "SbRelacionPersonaGt.findByFecha", query = "SELECT s FROM SbRelacionPersonaGt s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SbRelacionPersonaGt.findByObservacion", query = "SELECT s FROM SbRelacionPersonaGt s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SbRelacionPersonaGt.findByActivo", query = "SELECT s FROM SbRelacionPersonaGt s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbRelacionPersonaGt.findByAudLogin", query = "SELECT s FROM SbRelacionPersonaGt s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbRelacionPersonaGt.findByAudNumIp", query = "SELECT s FROM SbRelacionPersonaGt s WHERE s.audNumIp = :audNumIp")})
public class SbRelacionPersonaGt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_RELACION_PERSONA")
    @SequenceGenerator(name = "SEQ_SB_RELACION_PERSONA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_RELACION_PERSONA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 300)
    @Column(name = "OBSERVACION")
    private String observacion;
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
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoId;
    @JoinColumn(name = "PERSONA_ORI_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaOriId;
    @JoinColumn(name = "PERSONA_DEST_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaDestId;

    public SbRelacionPersonaGt() {
    }

    public SbRelacionPersonaGt(Long id) {
        this.id = id;
    }

    public SbRelacionPersonaGt(Long id, Date fecha, short activo, String audLogin, String audNumIp) {
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public TipoBaseGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBaseGt tipoId) {
        this.tipoId = tipoId;
    }

    public SbPersonaGt getPersonaOriId() {
        return personaOriId;
    }

    public void setPersonaOriId(SbPersonaGt personaOriId) {
        this.personaOriId = personaOriId;
    }

    public SbPersonaGt getPersonaDestId() {
        return personaDestId;
    }

    public void setPersonaDestId(SbPersonaGt personaDestId) {
        this.personaDestId = personaDestId;
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
        if (!(object instanceof SbRelacionPersonaGt)) {
            return false;
        }
        SbRelacionPersonaGt other = (SbRelacionPersonaGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.SbRelacionPersonaGt[ id=" + id + " ]";
    }
    
}
