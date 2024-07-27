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
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import pe.gob.sucamec.sistemabase.data.TipoBase;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.sel.citas.data.TurMunicion;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TUR_TURNO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TurTurno.findAll", query = "SELECT t FROM TurTurno t"),
    @NamedQuery(name = "TurTurno.findById", query = "SELECT t FROM TurTurno t WHERE t.id = :id"),
    @NamedQuery(name = "TurTurno.findByFechaTurno", query = "SELECT t FROM TurTurno t WHERE t.fechaTurno = :fechaTurno"),
    @NamedQuery(name = "TurTurno.findByNroTurno", query = "SELECT t FROM TurTurno t WHERE t.nroTurno = :nroTurno"),
    @NamedQuery(name = "TurTurno.findByFechaPrograma", query = "SELECT t FROM TurTurno t WHERE t.fechaPrograma = :fechaPrograma"),
    @NamedQuery(name = "TurTurno.findByFechaConfirma", query = "SELECT t FROM TurTurno t WHERE t.fechaConfirma = :fechaConfirma"),
    @NamedQuery(name = "TurTurno.findByClaveConsulta", query = "SELECT t FROM TurTurno t WHERE t.claveConsulta = :claveConsulta"),
    @NamedQuery(name = "TurTurno.findByAudLoginTurno", query = "SELECT t FROM TurTurno t WHERE t.audLoginTurno = :audLoginTurno"),
    @NamedQuery(name = "TurTurno.findByAudNumIpTurno", query = "SELECT t FROM TurTurno t WHERE t.audNumIpTurno = :audNumIpTurno"),
    @NamedQuery(name = "TurTurno.findByAudLoginPorte", query = "SELECT t FROM TurTurno t WHERE t.audLoginPorte = :audLoginPorte"),
    @NamedQuery(name = "TurTurno.findByAudNumIpPorte", query = "SELECT t FROM TurTurno t WHERE t.audNumIpPorte = :audNumIpPorte"),
    @NamedQuery(name = "TurTurno.findByActivo", query = "SELECT t FROM TurTurno t WHERE t.activo = :activo"),
    @NamedQuery(name = "TurTurno.findByAudLogin", query = "SELECT t FROM TurTurno t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TurTurno.findByAudNumIp", query = "SELECT t FROM TurTurno t WHERE t.audNumIp = :audNumIp"),
    @NamedQuery(name = "TurTurno.findByVezTurno", query = "SELECT t FROM TurTurno t WHERE t.vezTurno = :vezTurno"),
    @NamedQuery(name = "TurTurno.findByNroReprogramacion", query = "SELECT t FROM TurTurno t WHERE t.nroReprogramacion = :nroReprogramacion"),
    @NamedQuery(name = "TurTurno.findByCodVerifica", query = "SELECT t FROM TurTurno t WHERE t.codVerifica = :codVerifica")})
public class TurTurno implements Serializable {

    private static final Long serialVersionUID = 1L;
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
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @Column(name = "ATENDIDO")
    private Short atendido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VEZ_TURNO")
    private int vezTurno;
    @Column(name = "NRO_REPROGRAMACION")
    private Integer nroReprogramacion;
    @Size(max = 40)
    @Column(name = "COD_VERIFICA")
    private String codVerifica;
    @JoinTable(name = "TUR_TURNO_COMPRO", schema = "BDINTEGRADO", joinColumns = {
        @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "COMPROBANTE_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<TurComprobante> turComprobanteList;
    @OneToMany(mappedBy = "turnoId")
    private List<TurTurno> turTurnoList;
    @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TurTurno turnoId;
    @JoinColumn(name = "PROGRAMACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TurProgramacion programacionId;
    @JoinColumn(name = "PER_EXAMEN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TurPersona perExamenId;
    @JoinColumn(name = "PER_PAGO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TurPersona perPagoId;
    @JoinColumn(name = "TIPO_TRAMITE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoTramiteId;
    @JoinColumn(name = "SUB_TRAMITE_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac subTramiteId;
    @JoinColumn(name = "ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac estado;
    @JoinColumn(name = "TIPO_SEDE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase tipoSedeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnoId")
    private List<TurRequisitosReg> turRequisitosRegList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnoId")
    private List<TurLicenciaReg> turLicenciaRegList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnoId")
    private List<TurOperacionReg> turOperacionRegList;
    @OneToMany(mappedBy = "turnoId")
    private List<TurConstancia> turConstanciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnoId")
    private List<AmaTurnoActa> amaTurnoActaList;
    @Size(min = 1, max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
    @JoinColumn(name = "APP_REUNION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBase appReunionId;
    @Size(max = 350)
    @Column(name = "URL_APP_REUNION")
    private String urlAppReunion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnoId")
    private List<TurMunicion> turMunicionList;

    public TurTurno() {
    }

    public TurTurno(Long id) {
        this.id = id;
    }

    public TurTurno(Long id, Date fechaTurno, long nroTurno, Date fechaPrograma, String claveConsulta, short activo, String audLogin, String audNumIp, int vezTurno) {
        this.id = id;
        this.fechaTurno = fechaTurno;
        this.nroTurno = nroTurno;
        this.fechaPrograma = fechaPrograma;
        this.claveConsulta = claveConsulta;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
        this.vezTurno = vezTurno;
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

    public int getVezTurno() {
        return vezTurno;
    }

    public void setVezTurno(int vezTurno) {
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
    public List<TurComprobante> getTurComprobanteList() {
        return turComprobanteList;
    }

    public void setTurComprobanteList(List<TurComprobante> turComprobanteList) {
        this.turComprobanteList = turComprobanteList;
    }

    @XmlTransient
    public List<TurTurno> getTurTurnoList() {
        return turTurnoList;
    }

    public void setTurTurnoList(List<TurTurno> turTurnoList) {
        this.turTurnoList = turTurnoList;
    }

    public TurTurno getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(TurTurno turnoId) {
        this.turnoId = turnoId;
    }

    public TurProgramacion getProgramacionId() {
        return programacionId;
    }

    public void setProgramacionId(TurProgramacion programacionId) {
        this.programacionId = programacionId;
    }

    public TurPersona getPerExamenId() {
        return perExamenId;
    }

    public void setPerExamenId(TurPersona perExamenId) {
        this.perExamenId = perExamenId;
    }

    public TurPersona getPerPagoId() {
        return perPagoId;
    }

    public void setPerPagoId(TurPersona perPagoId) {
        this.perPagoId = perPagoId;
    }

    public TipoGamac getTipoTramiteId() {
        return tipoTramiteId;
    }

    public void setTipoTramiteId(TipoGamac tipoTramiteId) {
        this.tipoTramiteId = tipoTramiteId;
    }

    public TipoGamac getSubTramiteId() {
        return subTramiteId;
    }

    public void setSubTramiteId(TipoGamac subTramiteId) {
        this.subTramiteId = subTramiteId;
    }

    public TipoGamac getEstado() {
        return estado;
    }

    public void setEstado(TipoGamac estado) {
        this.estado = estado;
    }

    public TipoBase getTipoSedeId() {
        return tipoSedeId;
    }

    public void setTipoSedeId(TipoBase tipoSedeId) {
        this.tipoSedeId = tipoSedeId;
    }

    @XmlTransient
    public List<TurRequisitosReg> getTurRequisitosRegList() {
        return turRequisitosRegList;
    }

    public void setTurRequisitosRegList(List<TurRequisitosReg> turRequisitosRegList) {
        this.turRequisitosRegList = turRequisitosRegList;
    }

    @XmlTransient
    public List<TurLicenciaReg> getTurLicenciaRegList() {
        return turLicenciaRegList;
    }

    public void setTurLicenciaRegList(List<TurLicenciaReg> turLicenciaRegList) {
        this.turLicenciaRegList = turLicenciaRegList;
    }

    @XmlTransient
    public List<TurOperacionReg> getTurOperacionRegList() {
        return turOperacionRegList;
    }

    public void setTurOperacionRegList(List<TurOperacionReg> turOperacionRegList) {
        this.turOperacionRegList = turOperacionRegList;
    }

    @XmlTransient
    public List<TurConstancia> getTurConstanciaList() {
        return turConstanciaList;
    }

    public void setTurConstanciaList(List<TurConstancia> turConstanciaList) {
        this.turConstanciaList = turConstanciaList;
    }

    public String getHashQr() {
        return hashQr;
    }

    public void setHashQr(String hashQr) {
        this.hashQr = hashQr;
    }

    public TipoBase getAppReunionId() {
        return appReunionId;
    }

    public void setAppReunionId(TipoBase appReunionId) {
        this.appReunionId = appReunionId;
    }

    public String getUrlAppReunion() {
        return urlAppReunion;
    }

    public void setUrlAppReunion(String urlAppReunion) {
        this.urlAppReunion = urlAppReunion;
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
        if (!(object instanceof TurTurno)) {
            return false;
        }
        TurTurno other = (TurTurno) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.turreg.data.TurTurno[ id=" + id + " ]";
    }
    @XmlTransient
    public List<AmaTurnoActa> getAmaTurnoActaList() {
        return amaTurnoActaList;
    }

    public void setAmaTurnoActaList(List<AmaTurnoActa> amaTurnoActaList) {
        this.amaTurnoActaList = amaTurnoActaList;
    }

    @XmlTransient
    public List<TurMunicion> getTurMunicionList() {
        return turMunicionList;
    }

    public void setTurMunicionList(List<TurMunicion> turMunicionList) {
        this.turMunicionList = turMunicionList;
    }    
    
}
