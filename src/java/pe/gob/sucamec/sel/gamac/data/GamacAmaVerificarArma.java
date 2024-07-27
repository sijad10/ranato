/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_VERIFICAR_ARMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacAmaVerificarArma.findAll", query = "SELECT g FROM GamacAmaVerificarArma g"),
    @NamedQuery(name = "GamacAmaVerificarArma.findById", query = "SELECT g FROM GamacAmaVerificarArma g WHERE g.id = :id"),
    @NamedQuery(name = "GamacAmaVerificarArma.findByNroExpediente", query = "SELECT g FROM GamacAmaVerificarArma g WHERE g.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "GamacAmaVerificarArma.findByPesoActual", query = "SELECT g FROM GamacAmaVerificarArma g WHERE g.pesoActual = :pesoActual"),
    @NamedQuery(name = "GamacAmaVerificarArma.findByResultadoEvaluacion", query = "SELECT g FROM GamacAmaVerificarArma g WHERE g.resultadoEvaluacion = :resultadoEvaluacion"),
    @NamedQuery(name = "GamacAmaVerificarArma.findByFechaVerifica", query = "SELECT g FROM GamacAmaVerificarArma g WHERE g.fechaVerifica = :fechaVerifica"),
    @NamedQuery(name = "GamacAmaVerificarArma.findByActivo", query = "SELECT g FROM GamacAmaVerificarArma g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacAmaVerificarArma.findByAudLogin", query = "SELECT g FROM GamacAmaVerificarArma g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacAmaVerificarArma.findByAudNumIp", query = "SELECT g FROM GamacAmaVerificarArma g WHERE g.audNumIp = :audNumIp")})
public class GamacAmaVerificarArma implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_AMA_VERIFICAR_ARMA")
    @SequenceGenerator(name = "SEQ_GAMAC_AMA_VERIFICAR_ARMA", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_VERIFICAR_ARMA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Column(name = "PESO_ACTUAL")
    private Long pesoActual;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "RESULTADO_EVALUACION")
    private String resultadoEvaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_VERIFICA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVerifica;
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
    @OneToMany(mappedBy = "verificarFotoId")
    private Collection<GamacAmaFoto> gamacAmaFotoCollection;
    @JoinColumn(name = "UNIDAD_MEDIDA_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacUnidadMedida unidadMedidaId;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoGamac estadoId;
    @JoinColumn(name = "PERSONA_SOLICITA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbPersona personaSolicitaId;
    @JoinColumn(name = "ARMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaArma armaId;

    public GamacAmaVerificarArma() {
    }

    public GamacAmaVerificarArma(Long id) {
        this.id = id;
    }

    public GamacAmaVerificarArma(Long id, String resultadoEvaluacion, Date fechaVerifica, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.resultadoEvaluacion = resultadoEvaluacion;
        this.fechaVerifica = fechaVerifica;
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

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public Long getPesoActual() {
        return pesoActual;
    }

    public void setPesoActual(Long pesoActual) {
        this.pesoActual = pesoActual;
    }

    public String getResultadoEvaluacion() {
        return resultadoEvaluacion;
    }

    public void setResultadoEvaluacion(String resultadoEvaluacion) {
        this.resultadoEvaluacion = resultadoEvaluacion;
    }

    public Date getFechaVerifica() {
        return fechaVerifica;
    }

    public void setFechaVerifica(Date fechaVerifica) {
        this.fechaVerifica = fechaVerifica;
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
    public Collection<GamacAmaFoto> getGamacAmaFotoCollection() {
        return gamacAmaFotoCollection;
    }

    public void setGamacAmaFotoCollection(Collection<GamacAmaFoto> gamacAmaFotoCollection) {
        this.gamacAmaFotoCollection = gamacAmaFotoCollection;
    }

    public GamacUnidadMedida getUnidadMedidaId() {
        return unidadMedidaId;
    }

    public void setUnidadMedidaId(GamacUnidadMedida unidadMedidaId) {
        this.unidadMedidaId = unidadMedidaId;
    }

    public GamacTipoGamac getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(GamacTipoGamac estadoId) {
        this.estadoId = estadoId;
    }

    public GamacSbPersona getPersonaSolicitaId() {
        return personaSolicitaId;
    }

    public void setPersonaSolicitaId(GamacSbPersona personaSolicitaId) {
        this.personaSolicitaId = personaSolicitaId;
    }

    public GamacAmaArma getArmaId() {
        return armaId;
    }

    public void setArmaId(GamacAmaArma armaId) {
        this.armaId = armaId;
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
        if (!(object instanceof GamacAmaVerificarArma)) {
            return false;
        }
        GamacAmaVerificarArma other = (GamacAmaVerificarArma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacAmaVerificarArma[ id=" + id + " ]";
    }
    
}
