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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;

/**
 *
 * @author rfernandezv
 */
@Entity
@Table(name = "SB_CS_MEDICO", catalog = "", schema = "BDINTEGRADO")
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbCsMedico.findAll", query = "SELECT s FROM SbCsMedico s"),
    @NamedQuery(name = "SbCsMedico.findById", query = "SELECT s FROM SbCsMedico s WHERE s.id = :id"),
    @NamedQuery(name = "SbCsMedico.findByNroCmp", query = "SELECT s FROM SbCsMedico s WHERE s.nroCmp = :nroCmp"),
    @NamedQuery(name = "SbCsMedico.findByActivo", query = "SELECT s FROM SbCsMedico s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbCsMedico.findByAudLogin", query = "SELECT s FROM SbCsMedico s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbCsMedico.findByAudNumIp", query = "SELECT s FROM SbCsMedico s WHERE s.audNumIp = :audNumIp")})
public class SbCsMedico implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_CS_MEDICO")
    @SequenceGenerator(name = "SEQ_SB_CS_MEDICO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_CS_MEDICO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "NRO_CMP")
    private String nroCmp;
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
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuarioId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicoId")
    private List<SbCsMedicoEstabsal> sbCsMedicoEstabsalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicoId")
    private List<SbCsCertifMedico> sbCsCertifMedicoList;
    @Size(max = 50)
    @Column(name = "ADJUNTO_FIRMA")
    private String adjuntoFirma;
    @Column(name = "FECHA_ADJUNTO_FIRMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAdjuntoFirma;

    public SbCsMedico() {
    }

    public SbCsMedico(Long id) {
        this.id = id;
    }

    public SbCsMedico(Long id, String nroCmp, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroCmp = nroCmp;
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

    public String getNroCmp() {
        return nroCmp;
    }

    public void setNroCmp(String nroCmp) {
        this.nroCmp = nroCmp;
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

    public SbUsuarioGt getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuarioGt usuarioId) {
        this.usuarioId = usuarioId;
    }

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
    }

    @XmlTransient
    public List<SbCsMedicoEstabsal> getSbCsMedicoEstabsalList() {
        return sbCsMedicoEstabsalList;
    }

    public void setSbCsMedicoEstabsalList(List<SbCsMedicoEstabsal> sbCsMedicoEstabsalList) {
        this.sbCsMedicoEstabsalList = sbCsMedicoEstabsalList;
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
        if (!(object instanceof SbCsMedico)) {
            return false;
        }
        SbCsMedico other = (SbCsMedico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbCsMedico[ id=" + id + " ]";
    }

    @XmlTransient
    public List<SbCsCertifMedico> getSbCsCertifMedicoList() {
        return sbCsCertifMedicoList;
    }

    public void setSbCsCertifMedicoList(List<SbCsCertifMedico> sbCsCertifMedicoList) {
        this.sbCsCertifMedicoList = sbCsCertifMedicoList;
    }    
    
    public String getAdjuntoFirma() {
        return adjuntoFirma;
    }

    public void setAdjuntoFirma(String adjuntoFirma) {
        this.adjuntoFirma = adjuntoFirma;
    }

    public Date getFechaAdjuntoFirma() {
        return fechaAdjuntoFirma;
    }

    public void setFechaAdjuntoFirma(Date fechaAdjuntoFirma) {
        this.fechaAdjuntoFirma = fechaAdjuntoFirma;
    }
}
