/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_CMPE_INSCRIPCION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppCmpeInscripcion.findAll", query = "SELECT e FROM EppCmpeInscripcion e"),
    @NamedQuery(name = "EppCmpeInscripcion.findById", query = "SELECT e FROM EppCmpeInscripcion e WHERE e.id = :id"),
    @NamedQuery(name = "EppCmpeInscripcion.findByFecha", query = "SELECT e FROM EppCmpeInscripcion e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "EppCmpeInscripcion.findByFotoRuta", query = "SELECT e FROM EppCmpeInscripcion e WHERE e.fotoRuta = :fotoRuta"),
    @NamedQuery(name = "EppCmpeInscripcion.findByActivo", query = "SELECT e FROM EppCmpeInscripcion e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppCmpeInscripcion.findByAudLogin", query = "SELECT e FROM EppCmpeInscripcion e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppCmpeInscripcion.findByAudNumIp", query = "SELECT e FROM EppCmpeInscripcion e WHERE e.audNumIp = :audNumIp")})
public class EppCmpeInscripcion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_CMPE_INSCRIPCION")
    @SequenceGenerator(name = "SEQ_EPP_CMPE_INSCRIPCION", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_CMPE_INSCRIPCION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 500)
    @Column(name = "FOTO_RUTA")
    private String fotoRuta;
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
    @JoinTable(schema="BDINTEGRADO", name = "EPP_CMPE_INSCRIP_ACT", joinColumns = {
        @JoinColumn(name = "CMPE_INSCRIPCION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TIPO_EXPLOSIVO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<TipoExplosivoGt> tipoExplosivoList;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    @JoinColumn(name = "PARTICIPANTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt participanteId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppRegistro registroId;
    @JoinColumn(name = "CAPACITACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppCapacitacion capacitacionId;

    public EppCmpeInscripcion() {
    }

    public EppCmpeInscripcion(Long id) {
        this.id = id;
    }

    public EppCmpeInscripcion(Long id, Date fecha, short activo, String audLogin, String audNumIp) {
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

    public String getFotoRuta() {
        return fotoRuta;
    }

    public void setFotoRuta(String fotoRuta) {
        this.fotoRuta = fotoRuta;
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
    public List<TipoExplosivoGt> getTipoExplosivoList() {
        return tipoExplosivoList;
    }

    public void setTipoExplosivoList(List<TipoExplosivoGt> tipoExplosivoList) {
        this.tipoExplosivoList = tipoExplosivoList;
    }

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
    }

    public SbPersonaGt getParticipanteId() {
        return participanteId;
    }

    public void setParticipanteId(SbPersonaGt participanteId) {
        this.participanteId = participanteId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }

    public EppCapacitacion getCapacitacionId() {
        return capacitacionId;
    }

    public void setCapacitacionId(EppCapacitacion capacitacionId) {
        this.capacitacionId = capacitacionId;
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
        if (!(object instanceof EppCmpeInscripcion)) {
            return false;
        }
        EppCmpeInscripcion other = (EppCmpeInscripcion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.EppCmpeInscripcion[ id=" + id + " ]";
    }
    
}
