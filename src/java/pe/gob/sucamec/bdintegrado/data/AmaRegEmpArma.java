/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_REG_EMP_ARMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaRegEmpArma.findAll", query = "SELECT a FROM AmaRegEmpArma a"),
    @NamedQuery(name = "AmaRegEmpArma.findById", query = "SELECT a FROM AmaRegEmpArma a WHERE a.id = :id"),
    @NamedQuery(name = "AmaRegEmpArma.findByExpedienteMaster", query = "SELECT a FROM AmaRegEmpArma a WHERE a.expedienteMaster = :expedienteMaster"),
    @NamedQuery(name = "AmaRegEmpArma.findByExpediente", query = "SELECT a FROM AmaRegEmpArma a WHERE a.expediente = :expediente"),
    @NamedQuery(name = "AmaRegEmpArma.findByLicDiscaId", query = "SELECT a FROM AmaRegEmpArma a WHERE a.licDiscaId = :licDiscaId"),
    @NamedQuery(name = "AmaRegEmpArma.findBySerie", query = "SELECT a FROM AmaRegEmpArma a WHERE a.serie = :serie"),
    @NamedQuery(name = "AmaRegEmpArma.findByModeloId", query = "SELECT a FROM AmaRegEmpArma a WHERE a.modeloId = :modeloId"),
    @NamedQuery(name = "AmaRegEmpArma.findByPesoArma", query = "SELECT a FROM AmaRegEmpArma a WHERE a.pesoArma = :pesoArma"),
    @NamedQuery(name = "AmaRegEmpArma.findByNombreFoto1Id", query = "SELECT a FROM AmaRegEmpArma a WHERE a.nombreFoto1Id = :nombreFoto1Id"),
    @NamedQuery(name = "AmaRegEmpArma.findByNombreFoto2Id", query = "SELECT a FROM AmaRegEmpArma a WHERE a.nombreFoto2Id = :nombreFoto2Id"),
    @NamedQuery(name = "AmaRegEmpArma.findByEstadoVerfica", query = "SELECT a FROM AmaRegEmpArma a WHERE a.estadoVerfica = :estadoVerfica"),
    @NamedQuery(name = "AmaRegEmpArma.findBySituacionArma", query = "SELECT a FROM AmaRegEmpArma a WHERE a.situacionArma = :situacionArma"),
    @NamedQuery(name = "AmaRegEmpArma.findByDescMotivo", query = "SELECT a FROM AmaRegEmpArma a WHERE a.descMotivo = :descMotivo"),
    @NamedQuery(name = "AmaRegEmpArma.findByAdjuntoDocId", query = "SELECT a FROM AmaRegEmpArma a WHERE a.adjuntoDocId = :adjuntoDocId"),
    @NamedQuery(name = "AmaRegEmpArma.findByActivo", query = "SELECT a FROM AmaRegEmpArma a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaRegEmpArma.findByAudLogin", query = "SELECT a FROM AmaRegEmpArma a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaRegEmpArma.findByAudNumIp", query = "SELECT a FROM AmaRegEmpArma a WHERE a.audNumIp = :audNumIp")})
public class AmaRegEmpArma implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_REG_EMP_ARMA")
    @SequenceGenerator(name = "SEQ_AMA_REG_EMP_ARMA", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_REG_EMP_ARMA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "EXPEDIENTE_MASTER")
    private String expedienteMaster;
    @Size(max = 20)
    @Column(name = "EXPEDIENTE")
    private String expediente;
    @Column(name = "LIC_DISCA_ID")
    private Long licDiscaId;
    @Size(max = 50)
    @Column(name = "SERIE")
    private String serie;
    @Column(name = "MODELO_ID")
    private Long modeloId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PESO_ARMA")
    private Double pesoArma;
    @Column(name = "NOMBRE_FOTO1_ID")
    private String nombreFoto1Id;
    @Column(name = "NOMBRE_FOTO2_ID")
    private String nombreFoto2Id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTADO_VERFICA")
    private Long estadoVerfica;
    @JoinColumn(name = "SITUACION_ARMA", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac situacionArma;
    @JoinColumn(name = "ESTADO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac estadoArmaId;
    @Size(max = 400)
    @Column(name = "DESC_MOTIVO")
    private String descMotivo;
    @Column(name = "ADJUNTO_DOC_ID")
    private String adjuntoDocId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersonaGt personaId;
    @Column(name = "FECHA_INCAUTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIncauta;
    @Size(max = 50)
    @Column(name = "ASUNTO_OBS_SIS")
    private String asuntoObsSis;
    @Size(max = 500)
    @Column(name = "DESC_OBS_SIS")
    private String descObsSis;
    @Column(name = "FECHA_OBS_SIS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaObsSis;
    @JoinColumn(name = "TIPO_ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac tipoEstadoId;
    @JoinColumn(name = "TIPO_EVALUACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac tipoEvaluacionId;
    @JoinTable(schema="BDINTEGRADO", name = "AMA_DOCUMENTO_REG", joinColumns = {
        @JoinColumn(name = "AMA_REG_EMP_ARMA_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "DOCUMENTO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<AmaDocumento> amaDocumentoList;
    @Column(name = "FECHA_SUB")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSub;
    @ManyToMany(mappedBy = "amaRegEmpArmaList")
    private List<AmaRegEmpProceso> amaRegEmpProcesoList;
    @JoinColumn(name = "PENAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt penalId;
    @JoinColumn(name = "REGION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt regionId;
    @JoinColumn(name = "TIPO_REGU_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac tipoReguId;
    @Size(max = 11)
    @Column(name = "NDOC_POSIBLE_PROP")
    private String ndocPosibleProp;
    @Size(max = 50)
    @Column(name = "SERIE_PROPUESTA")
    private String seriePropuesta;
    @Size(max = 400)
    @Column(name = "SUSTENTO_TP_SERIE_PROP")
    private String sustentoTpSerieProp;
    @JoinColumn(name = "TIPO_SUSTENTO_TP_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac tipoSustentoTpId;

    public AmaRegEmpArma() {
    }

    public AmaRegEmpArma(Long id) {
        this.id = id;
    }

    public AmaRegEmpArma(Long id, Long estadoVerfica, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.estadoVerfica = estadoVerfica;
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

    public String getExpedienteMaster() {
        return expedienteMaster;
    }

    public void setExpedienteMaster(String expedienteMaster) {
        this.expedienteMaster = expedienteMaster;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public Long getLicDiscaId() {
        return licDiscaId;
    }

    public void setLicDiscaId(Long licDiscaId) {
        this.licDiscaId = licDiscaId;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Long getModeloId() {
        return modeloId;
    }

    public void setModeloId(Long modeloId) {
        this.modeloId = modeloId;
    }

    public Double getPesoArma() {
        return pesoArma;
    }

    public void setPesoArma(Double pesoArma) {
        this.pesoArma = pesoArma;
    }

    public String getNombreFoto1Id() {
        return nombreFoto1Id;
    }

    public void setNombreFoto1Id(String nombreFoto1Id) {
        this.nombreFoto1Id = nombreFoto1Id;
    }

    public String getNombreFoto2Id() {
        return nombreFoto2Id;
    }

    public void setNombreFoto2Id(String nombreFoto2Id) {
        this.nombreFoto2Id = nombreFoto2Id;
    }

    public Long getEstadoVerfica() {
        return estadoVerfica;
    }

    public void setEstadoVerfica(Long estadoVerfica) {
        this.estadoVerfica = estadoVerfica;
    }

    public TipoGamac getSituacionArma() {
        return situacionArma;
    }

    public void setSituacionArma(TipoGamac situacionArma) {
        this.situacionArma = situacionArma;
    }

    public String getDescMotivo() {
        return descMotivo;
    }

    public void setDescMotivo(String descMotivo) {
        this.descMotivo = descMotivo;
    }

    public String getAdjuntoDocId() {
        return adjuntoDocId;
    }

    public void setAdjuntoDocId(String adjuntoDocId) {
        this.adjuntoDocId = adjuntoDocId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmaRegEmpArma)) {
            return false;
        }
        AmaRegEmpArma other = (AmaRegEmpArma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaRegEmpArma[ id=" + id + " ]";
    }
            
    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
    }

    public Date getFechaIncauta() {
        return fechaIncauta;
    }

    public void setFechaIncauta(Date fechaIncauta) {
        this.fechaIncauta = fechaIncauta;
    }
    
    public TipoGamac getEstadoArmaId() {
        return estadoArmaId;
    }

    public void setEstadoArmaId(TipoGamac estadoArmaId) {
        this.estadoArmaId = estadoArmaId;
    }
    public String getAsuntoObsSis() {
        return asuntoObsSis;
    }

    public void setAsuntoObsSis(String asuntoObsSis) {
        this.asuntoObsSis = asuntoObsSis;
    }

    public String getDescObsSis() {
        return descObsSis;
    }

    public void setDescObsSis(String descObsSis) {
        this.descObsSis = descObsSis;
    }

    public Date getFechaObsSis() {
        return fechaObsSis;
    }

    public void setFechaObsSis(Date fechaObsSis) {
        this.fechaObsSis = fechaObsSis;
    }

    public TipoGamac getTipoEvaluacionId() {
        return tipoEvaluacionId;
    }

    public void setTipoEvaluacionId(TipoGamac tipoEvaluacionId) {
        this.tipoEvaluacionId = tipoEvaluacionId;
    }

    public TipoGamac getTipoEstadoId() {
        return tipoEstadoId;
    }

    public void setTipoEstadoId(TipoGamac tipoEstadoId) {
        this.tipoEstadoId = tipoEstadoId;
    }

    @XmlTransient
    public List<AmaDocumento> getAmaDocumentoList() {
        return amaDocumentoList;
    }

    public void setAmaDocumentoList(List<AmaDocumento> amaDocumentoList) {
        this.amaDocumentoList = amaDocumentoList;
    }
    
    public Date getFechaSub() {
        return fechaSub;
    }

    public void setFechaSub(Date fechaSub) {
        this.fechaSub = fechaSub;
    }
    
    @XmlTransient
    public List<AmaRegEmpProceso> getAmaRegEmpProcesoList() {
        return amaRegEmpProcesoList;
    }

    public void setAmaRegEmpProcesoList(List<AmaRegEmpProceso> amaRegEmpProcesoList) {
        this.amaRegEmpProcesoList = amaRegEmpProcesoList;
    }
    
    public TipoBaseGt getPenalId() {
        return penalId;
    }

    public void setPenalId(TipoBaseGt penalId) {
        this.penalId = penalId;
    }

    public TipoBaseGt getRegionId() {
        return regionId;
    }

    public void setRegionId(TipoBaseGt regionId) {
        this.regionId = regionId;
    }
    
    public TipoGamac getTipoReguId() {
        return tipoReguId;
    }

    public void setTipoReguId(TipoGamac tipoReguId) {
        this.tipoReguId = tipoReguId;
    }
    
    public String getNdocPosibleProp() {
        return ndocPosibleProp;
    }

    public void setNdocPosibleProp(String ndocPosibleProp) {
        this.ndocPosibleProp = ndocPosibleProp;
    }
    
    public String getSeriePropuesta() {
        return seriePropuesta;
    }

    public void setSeriePropuesta(String seriePropuesta) {
        this.seriePropuesta = seriePropuesta;
    }

    public String getSustentoTpSerieProp() {
        return sustentoTpSerieProp;
    }

    public void setSustentoTpSerieProp(String sustentoTpSerieProp) {
        this.sustentoTpSerieProp = sustentoTpSerieProp;
    }
    
    public TipoGamac getTipoSustentoTpId() {
        return tipoSustentoTpId;
    }

    public void setTipoSustentoTpId(TipoGamac tipoSustentoTpId) {
        this.tipoSustentoTpId = tipoSustentoTpId;
    }
}
