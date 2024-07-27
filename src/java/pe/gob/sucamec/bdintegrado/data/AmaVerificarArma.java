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
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_VERIFICAR_ARMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaVerificarArma.findAll", query = "SELECT a FROM AmaVerificarArma a"),
    @NamedQuery(name = "AmaVerificarArma.findById", query = "SELECT a FROM AmaVerificarArma a WHERE a.id = :id"),
    @NamedQuery(name = "AmaVerificarArma.findByNroExpediente", query = "SELECT a FROM AmaVerificarArma a WHERE a.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "AmaVerificarArma.findByPesoActual", query = "SELECT a FROM AmaVerificarArma a WHERE a.pesoActual = :pesoActual"),
    @NamedQuery(name = "AmaVerificarArma.findByResultadoEvaluacion", query = "SELECT a FROM AmaVerificarArma a WHERE a.resultadoEvaluacion = :resultadoEvaluacion"),
    @NamedQuery(name = "AmaVerificarArma.findByFechaVerifica", query = "SELECT a FROM AmaVerificarArma a WHERE a.fechaVerifica = :fechaVerifica"),
    @NamedQuery(name = "AmaVerificarArma.findByActivo", query = "SELECT a FROM AmaVerificarArma a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaVerificarArma.findByAudLogin", query = "SELECT a FROM AmaVerificarArma a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaVerificarArma.findByAudNumIp", query = "SELECT a FROM AmaVerificarArma a WHERE a.audNumIp = :audNumIp")})
public class AmaVerificarArma implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_VERIFICAR_ARMA")
    @SequenceGenerator(name = "SEQ_AMA_VERIFICAR_ARMA", schema = "name=", sequenceName = "SEQ_AMA_VERIFICAR_ARMA", allocationSize = 1)
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
    @JoinColumn(name = "ARMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaArma armaId;
    @JoinColumn(name = "PERSONA_SOLICITA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaSolicitaId;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac estadoId;
    @JoinColumn(name = "UNIDAD_MEDIDA_ID", referencedColumnName = "ID")
    @ManyToOne
    private UnidadMedida unidadMedidaId;
    @OneToMany(mappedBy = "verificarFotoId")
    private List<AmaFoto> amaFotoList;

    public AmaVerificarArma() {
    }

    public AmaVerificarArma(Long id) {
        this.id = id;
    }

    public AmaVerificarArma(Long id, String resultadoEvaluacion, Date fechaVerifica, short activo, String audLogin, String audNumIp) {
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

    public AmaArma getArmaId() {
        return armaId;
    }

    public void setArmaId(AmaArma armaId) {
        this.armaId = armaId;
    }

    @XmlTransient
    public List<AmaFoto> getAmaFotoList() {
        return amaFotoList;
    }

    public void setAmaFotoList(List<AmaFoto> amaFotoList) {
        this.amaFotoList = amaFotoList;
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
        if (!(object instanceof AmaVerificarArma)) {
            return false;
        }
        AmaVerificarArma other = (AmaVerificarArma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaVerificarArma[ id=" + id + " ]";
    }

    /**
     * @return the personaSolicitaId
     */
    public SbPersonaGt getPersonaSolicitaId() {
        return personaSolicitaId;
    }

    /**
     * @param personaSolicitaId the personaSolicitaId to set
     */
    public void setPersonaSolicitaId(SbPersonaGt personaSolicitaId) {
        this.personaSolicitaId = personaSolicitaId;
    }

    /**
     * @return the estadoId
     */
    public TipoGamac getEstadoId() {
        return estadoId;
    }

    /**
     * @param estadoId the estadoId to set
     */
    public void setEstadoId(TipoGamac estadoId) {
        this.estadoId = estadoId;
    }

    /**
     * @return the unidadMedidaId
     */
    public UnidadMedida getUnidadMedidaId() {
        return unidadMedidaId;
    }

    /**
     * @param unidadMedidaId the unidadMedidaId to set
     */
    public void setUnidadMedidaId(UnidadMedida unidadMedidaId) {
        this.unidadMedidaId = unidadMedidaId;
    }
    
}
