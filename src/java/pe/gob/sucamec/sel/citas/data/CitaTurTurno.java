/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

import java.io.Serializable;
import java.math.BigDecimal;
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
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.bdintegrado.data.SbExpVirtualSolicitud;

/**
 *
 * @author rarevalo
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "TUR_TURNO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaTurTurno.findAll", query = "SELECT t FROM CitaTurTurno t"),
    @NamedQuery(name = "CitaTurTurno.findById", query = "SELECT t FROM CitaTurTurno t WHERE t.id = :id"),
    @NamedQuery(name = "CitaTurTurno.findByFechaTurno", query = "SELECT t FROM CitaTurTurno t WHERE t.fechaTurno = :fechaTurno"),
    @NamedQuery(name = "CitaTurTurno.findByNroTurno", query = "SELECT t FROM CitaTurTurno t WHERE t.nroTurno = :nroTurno"),
    @NamedQuery(name = "CitaTurTurno.findByFechaPrograma", query = "SELECT t FROM CitaTurTurno t WHERE t.fechaPrograma = :fechaPrograma"),
    @NamedQuery(name = "CitaTurTurno.findByFechaConfirma", query = "SELECT t FROM CitaTurTurno t WHERE t.fechaConfirma = :fechaConfirma"),
    @NamedQuery(name = "CitaTurTurno.findByClaveConsulta", query = "SELECT t FROM CitaTurTurno t WHERE t.claveConsulta = :claveConsulta"),
    @NamedQuery(name = "CitaTurTurno.findByMotivoTurno", query = "SELECT t FROM CitaTurTurno t WHERE t.motivoTurno = :motivoTurno"),
    @NamedQuery(name = "CitaTurTurno.findByAudLoginTurno", query = "SELECT t FROM CitaTurTurno t WHERE t.audLoginTurno = :audLoginTurno"),
    @NamedQuery(name = "CitaTurTurno.findByAudNumIpTurno", query = "SELECT t FROM CitaTurTurno t WHERE t.audNumIpTurno = :audNumIpTurno"),
    @NamedQuery(name = "CitaTurTurno.findByAudLoginPorte", query = "SELECT t FROM CitaTurTurno t WHERE t.audLoginPorte = :audLoginPorte"),
    @NamedQuery(name = "CitaTurTurno.findByAudNumIpPorte", query = "SELECT t FROM CitaTurTurno t WHERE t.audNumIpPorte = :audNumIpPorte"),
    @NamedQuery(name = "CitaTurTurno.findByActivo", query = "SELECT t FROM CitaTurTurno t WHERE t.activo = :activo"),
    @NamedQuery(name = "CitaTurTurno.findByAudLogin", query = "SELECT t FROM CitaTurTurno t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "CitaTurTurno.findByAudNumIp", query = "SELECT t FROM CitaTurTurno t WHERE t.audNumIp = :audNumIp")})
public class CitaTurTurno implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "VEZ_TURNO")
    private int vezTurno;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnoId")
    private List<TurMunicion> turMunicionList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TUR_TURNO")
    @SequenceGenerator(name = "SEQ_TUR_TURNO", schema = "BDINTEGRADO", sequenceName = "SEQ_TUR_TURNO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_TURNO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaTurno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_TURNO")
    private long nroTurno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_PROGRAMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPrograma;
    @Column(name = "FECHA_CONFIRMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaConfirma;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CLAVE_CONSULTA")
    private String claveConsulta;
    
    @Size(max = 20)
    @Column(name = "AUD_LOGIN_TURNO")
    private String audLoginTurno;
    @Size(max = 40)
    @Column(name = "AUD_NUM_IP_TURNO")
    private String audNumIpTurno;
    @Size(max = 20)
    @Column(name = "AUD_LOGIN_PORTE")
    private String audLoginPorte;
    @Size(max = 40)
    @Column(name = "AUD_NUM_IP_PORTE")
    private String audNumIpPorte;
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
    @Column(name = "ATENDIDO")
    private Short atendido;    
    @Column(name = "NRO_REPROGRAMACION")
    private Integer nroReprogramacion;
    @Size(max = 40)
    @Column(name = "COD_VERIFICA")
    private String codVerifica;
    @OneToMany(mappedBy = "turnoId")
    private List<CitaTurTurno> turTurnoList;
    @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTurTurno turnoId;
    @Column(name = "SECUENCIA_EMPOCE")
    private Long secuenciaEmpoce;
    @Column(name = "FECHA_EMPOCE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmpoce;
    
    @Column(name = "MOTIVO_TURNO")
    private String motivoTurno;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MONTO_EMPOCE")
    private BigDecimal montoEmpoce;
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @JoinTable(name = "TUR_TURNO_COMPRO", schema = "BDINTEGRADO", joinColumns = {
        @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "COMPROBANTE_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<CitaTurComprobante> turComprobanteList;
//    @JoinColumn(name = "SEDE_ID", referencedColumnName = "ID")
//    @ManyToOne(optional = false)
//    private TurSede sedeId;
    @JoinColumn(name = "PROGRAMACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTurProgramacion programacionId;
    @JoinColumn(name = "ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTipoGamac estado;
    @JoinColumn(name = "TIPO_TRAMITE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTipoGamac tipoTramiteId;
    @JoinColumn(name = "PER_PAGO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTurPersona perPagoId;
    @JoinColumn(name = "PER_EXAMEN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTurPersona perExamenId;
    @JoinColumn(name = "TIPO_SEDE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTipoBase tipoSedeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnoId")
    private List<CitaTurConstancia> turConstanciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnoId")
    private List<CitaAmaTurnoActa> amaTurnoActaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnoId")
    private List<CitaTurLicenciaReg> turLicenciaRegList;
    @Size(min = 1, max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;    
    @JoinColumn(name = "SUB_TRAMITE_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoGamac subTramiteId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnoId")
    private List<CitaSbTurnoExpediente> sbTurnoExpedienteList;
    @JoinColumn(name = "APP_REUNION_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoBase appReunionId;
    @Size(max = 350)
    @Column(name = "URL_APP_REUNION")
    private String urlAppReunion;
    @JoinColumn(name = "EXP_VIRTUAL_SOLICITUD_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbExpVirtualSolicitud expVirtualSolicitudId;

    public CitaTurTurno() {
    }

    public CitaTurTurno(Long id) {
        this.id = id;
    }

    public CitaTurTurno(Long id, Date fechaTurno, long nroTurno, Date fechaPrograma, String claveConsulta, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaTurno = fechaTurno;
        this.nroTurno = nroTurno;
        this.fechaPrograma = fechaPrograma;
        this.claveConsulta = claveConsulta;
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

    public Date getFechaTurno() {
        return fechaTurno;
    }

    public void setFechaTurno(Date fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public long getNroTurno() {
        return nroTurno;
    }

    public void setNroTurno(long nroTurno) {
        this.nroTurno = nroTurno;
    }

    public Date getFechaPrograma() {
        return fechaPrograma;
    }

    public void setFechaPrograma(Date fechaPrograma) {
        this.fechaPrograma = fechaPrograma;
    }

    public Date getFechaConfirma() {
        return fechaConfirma;
    }

    public void setFechaConfirma(Date fechaConfirma) {
        this.fechaConfirma = fechaConfirma;
    }

    public String getClaveConsulta() {
        return claveConsulta;
    }

    public void setClaveConsulta(String claveConsulta) {
        this.claveConsulta = claveConsulta;
    }

    public CitaTipoBase getAppReunionId() {
        return appReunionId;
    }

    public void setAppReunionId(CitaTipoBase appReunionId) {
        this.appReunionId = appReunionId;
    }

    public String getUrlAppReunion() {
        return urlAppReunion;
    }

    public void setUrlAppReunion(String urlAppReunion) {
        this.urlAppReunion = urlAppReunion;
    }

    public String getAudLoginTurno() {
        return audLoginTurno;
    }

    public void setAudLoginTurno(String audLoginTurno) {
        this.audLoginTurno = audLoginTurno;
    }

    public String getAudNumIpTurno() {
        return audNumIpTurno;
    }

    public void setAudNumIpTurno(String audNumIpTurno) {
        this.audNumIpTurno = audNumIpTurno;
    }

    public Short getAtendido() {
        return atendido;
    }

    public void setAtendido(Short atendido) {
        this.atendido = atendido;
    }

    public String getAudLoginPorte() {
        return audLoginPorte;
    }

    public void setAudLoginPorte(String audLoginPorte) {
        this.audLoginPorte = audLoginPorte;
    }

    public String getAudNumIpPorte() {
        return audNumIpPorte;
    }

    public void setAudNumIpPorte(String audNumIpPorte) {
        this.audNumIpPorte = audNumIpPorte;
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

    public Integer getVezTurno() {
        return vezTurno;
    }

    public void setVezTurno(Integer vezTurno) {
        this.vezTurno = vezTurno;
    }

    public Integer getNroReprogramacion() {
        return nroReprogramacion;
    }

    public void setNroReprogramacion(Integer nroReprogramacion) {
        this.nroReprogramacion = nroReprogramacion;
    }
    
    public String getCodVerifica() {
        return codVerifica;
    }

    public void setCodVerifica(String codVerifica) {
        this.codVerifica = codVerifica;
    }

    @XmlTransient
    public List<CitaTurTurno> getTurTurnoList() {
        return turTurnoList;
    }

    public void setTurTurnoList(List<CitaTurTurno> turTurnoList) {
        this.turTurnoList = turTurnoList;
    }

    public CitaTurTurno getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(CitaTurTurno turnoId) {
        this.turnoId = turnoId;
    }

    public Long getSecuenciaEmpoce() {
        return secuenciaEmpoce;
    }

    public void setSecuenciaEmpoce(Long secuenciaEmpoce) {
        this.secuenciaEmpoce = secuenciaEmpoce;
    }

    public Date getFechaEmpoce() {
        return fechaEmpoce;
    }

    public void setFechaEmpoce(Date fechaEmpoce) {
        this.fechaEmpoce = fechaEmpoce;
    }

    public BigDecimal getMontoEmpoce() {
        return montoEmpoce;
    }

    public void setMontoEmpoce(BigDecimal montoEmpoce) {
        this.montoEmpoce = montoEmpoce;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public String getMotivoTurno() {
        return motivoTurno;
    }

    public void setMotivoTurno(String motivoTurno) {
        this.motivoTurno = motivoTurno;
    }

    
    
    @XmlTransient
    public List<CitaTurComprobante> getTurComprobanteList() {
        return turComprobanteList;
    }

    public void setTurComprobanteList(List<CitaTurComprobante> turComprobanteList) {
        this.turComprobanteList = turComprobanteList;
    }

//    public TurSede getSedeId() {
//        return sedeId;
//    }
//
//    public void setSedeId(TurSede sedeId) {
//        this.sedeId = sedeId;
//    }

    public CitaTurProgramacion getProgramacionId() {
        return programacionId;
    }

    public void setProgramacionId(CitaTurProgramacion programacionId) {
        this.programacionId = programacionId;
    }

    public CitaTipoGamac getEstado() {
        return estado;
    }

    public void setEstado(CitaTipoGamac estado) {
        this.estado = estado;
    }

    public CitaTipoGamac getTipoTramiteId() {
        return tipoTramiteId;
    }

    public void setTipoTramiteId(CitaTipoGamac tipoTramiteId) {
        this.tipoTramiteId = tipoTramiteId;
    }

    public CitaTurPersona getPerPagoId() {
        return perPagoId;
    }

    public void setPerPagoId(CitaTurPersona perPagoId) {
        this.perPagoId = perPagoId;
    }

    public CitaTurPersona getPerExamenId() {
        return perExamenId;
    }

    public void setPerExamenId(CitaTurPersona perExamenId) {
        this.perExamenId = perExamenId;
    }

    public CitaTipoBase getTipoSedeId() {
        return tipoSedeId;
    }

    public void setTipoSedeId(CitaTipoBase tipoSedeId) {
        this.tipoSedeId = tipoSedeId;
    }

    public SbExpVirtualSolicitud getExpVirtualSolicitudId() {
        return expVirtualSolicitudId;
    }

    public void setExpVirtualSolicitudId(SbExpVirtualSolicitud expVirtualSolicitudId) {
        this.expVirtualSolicitudId = expVirtualSolicitudId;
    }
    
    @XmlTransient
    public List<CitaTurConstancia> getTurConstanciaList() {
        return turConstanciaList;
    }

    public void setTurConstanciaList(List<CitaTurConstancia> turConstanciaList) {
        this.turConstanciaList = turConstanciaList;
    }
    
    @XmlTransient
    public List<CitaAmaTurnoActa> getAmaTurnoActaList() {
        return amaTurnoActaList;
    }

    public void setAmaTurnoActaList(List<CitaAmaTurnoActa> amaTurnoActaList) {
        this.amaTurnoActaList = amaTurnoActaList;
    }
    
    @XmlTransient
    public List<CitaTurLicenciaReg> getTurLicenciaRegList() {
        return turLicenciaRegList;
    }

    public void setTurLicenciaRegList(List<CitaTurLicenciaReg> turLicenciaRegList) {
        this.turLicenciaRegList = turLicenciaRegList;
    }
    
    public String getHashQr() {
        return hashQr;
    }

    public void setHashQr(String hashQr) {
        this.hashQr = hashQr;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public CitaTipoGamac getSubTramiteId() {
        return subTramiteId;
    }

    public void setSubTramiteId(CitaTipoGamac subTramiteId) {
        this.subTramiteId = subTramiteId;
    }

    public List<CitaSbTurnoExpediente> getSbTurnoExpedienteList() {
        return sbTurnoExpedienteList;
    }

    public void setSbTurnoExpedienteList(List<CitaSbTurnoExpediente> sbTurnoExpedienteList) {
        this.sbTurnoExpedienteList = sbTurnoExpedienteList;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitaTurTurno)) {
            return false;
        }
        CitaTurTurno other = (CitaTurTurno) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.citas.data.CitaTurTurno[ id=" + id + " ]";
    }
    
    @XmlTransient
    public List<TurMunicion> getTurMunicionList() {
        return turMunicionList;
    }

    public void setTurMunicionList(List<TurMunicion> turMunicionList) {
        this.turMunicionList = turMunicionList;
    }

}