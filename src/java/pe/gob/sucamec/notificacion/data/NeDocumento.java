/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.notificacion.data;

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
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbTipo;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "NE_DOCUMENTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NeDocumento.findAll", query = "SELECT n FROM NeDocumento n"),
    @NamedQuery(name = "NeDocumento.findById", query = "SELECT n FROM NeDocumento n WHERE n.id = :id"),
    @NamedQuery(name = "NeDocumento.findByNroExp", query = "SELECT n FROM NeDocumento n WHERE n.nroExp = :nroExp"),
    @NamedQuery(name = "NeDocumento.findByAnoExp", query = "SELECT n FROM NeDocumento n WHERE n.anoExp = :anoExp"),
    @NamedQuery(name = "NeDocumento.findBySecExp", query = "SELECT n FROM NeDocumento n WHERE n.secExp = :secExp"),
    @NamedQuery(name = "NeDocumento.findByNroDoc", query = "SELECT n FROM NeDocumento n WHERE n.nroDoc = :nroDoc"),
    @NamedQuery(name = "NeDocumento.findByFechaDoc", query = "SELECT n FROM NeDocumento n WHERE n.fechaDoc = :fechaDoc"),
    @NamedQuery(name = "NeDocumento.findByReferencia", query = "SELECT n FROM NeDocumento n WHERE n.referencia = :referencia"),
    @NamedQuery(name = "NeDocumento.findByAsunto", query = "SELECT n FROM NeDocumento n WHERE n.asunto = :asunto"),
    @NamedQuery(name = "NeDocumento.findByActivo", query = "SELECT n FROM NeDocumento n WHERE n.activo = :activo"),
    @NamedQuery(name = "NeDocumento.findByAudLogin", query = "SELECT n FROM NeDocumento n WHERE n.audLogin = :audLogin"),
    @NamedQuery(name = "NeDocumento.findByAudNumIp", query = "SELECT n FROM NeDocumento n WHERE n.audNumIp = :audNumIp")})
public class NeDocumento implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NE_DOCUMENTO")
    @SequenceGenerator(name = "SEQ_NE_DOCUMENTO", schema = "BDINTEGRADO", sequenceName = "SEQ_NE_DOCUMENTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_EXP")
    private int nroExp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ANO_EXP")
    private int anoExp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEC_EXP")
    private int secExp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NRO_DOC")
    private String nroDoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_DOC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDoc;
    @Size(max = 200)
    @Column(name = "REFERENCIA")
    private String referencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 600)
    @Column(name = "ASUNTO")
    private String asunto;
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
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documentoId")
    private List<NeArchivo> neArchivoList;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoId;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo estadoId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo areaId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documentoId")
    private List<NeEvento> neEventoList;

    public NeDocumento() {
    }

    public NeDocumento(Long id) {
        this.id = id;
    }

    public NeDocumento(Long id, int nroExp, int anoExp, int secExp, String nroDoc, Date fechaDoc, String asunto, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroExp = nroExp;
        this.anoExp = anoExp;
        this.secExp = secExp;
        this.nroDoc = nroDoc;
        this.fechaDoc = fechaDoc;
        this.asunto = asunto;
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

    public int getNroExp() {
        return nroExp;
    }

    public void setNroExp(int nroExp) {
        this.nroExp = nroExp;
    }

    public int getAnoExp() {
        return anoExp;
    }

    public void setAnoExp(int anoExp) {
        this.anoExp = anoExp;
    }

    public int getSecExp() {
        return secExp;
    }

    public void setSecExp(int secExp) {
        this.secExp = secExp;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public Date getFechaDoc() {
        return fechaDoc;
    }

    public void setFechaDoc(Date fechaDoc) {
        this.fechaDoc = fechaDoc;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
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
    public List<NeArchivo> getNeArchivoList() {
        return neArchivoList;
    }

    public void setNeArchivoList(List<NeArchivo> neArchivoList) {
        this.neArchivoList = neArchivoList;
    }

    public SbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersona personaId) {
        this.personaId = personaId;
    }

    public SbTipo getTipoId() {
        return tipoId;
    }

    public void setTipoId(SbTipo tipoId) {
        this.tipoId = tipoId;
    }

    public SbTipo getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(SbTipo estadoId) {
        this.estadoId = estadoId;
    }

    public SbTipo getAreaId() {
        return areaId;
    }

    public void setAreaId(SbTipo areaId) {
        this.areaId = areaId;
    }

    public SbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    @XmlTransient
    public List<NeEvento> getNeEventoList() {
        return neEventoList;
    }

    public void setNeEventoList(List<NeEvento> neEventoList) {
        this.neEventoList = neEventoList;
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
        if (!(object instanceof NeDocumento)) {
            return false;
        }
        NeDocumento other = (NeDocumento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.notificacion.data.NeDocumento[ id=" + id + " ]";
    }

    /**
     * @return the nroExpediente
     */
    public String getNroExpediente() {
        return nroExpediente;
    }

    /**
     * @param nroExpediente the nroExpediente to set
     */
    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }
    
}
