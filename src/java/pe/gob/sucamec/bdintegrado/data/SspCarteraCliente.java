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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_CARTERA_CLIENTE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspCarteraCliente.findAll", query = "SELECT s FROM SspCarteraCliente s"),
    @NamedQuery(name = "SspCarteraCliente.findById", query = "SELECT s FROM SspCarteraCliente s WHERE s.id = :id"),
    @NamedQuery(name = "SspCarteraCliente.findByTipoOpeId", query = "SELECT s FROM SspCarteraCliente s WHERE s.tipoOpeId.id = :tipoOpeId"),
    @NamedQuery(name = "SspCarteraCliente.findBySedeId", query = "SELECT s FROM SspCarteraCliente s WHERE s.sedeId.id = :sedeId"),
    @NamedQuery(name = "SspCarteraCliente.findByMotivoContratoId", query = "SELECT s FROM SspCarteraCliente s WHERE s.motivoContratoId.id = :motivoContratoId"),
    @NamedQuery(name = "SspCarteraCliente.findByFecha", query = "SELECT s FROM SspCarteraCliente s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspCarteraCliente.findByFechaInicio", query = "SELECT s FROM SspCarteraCliente s WHERE s.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "SspCarteraCliente.findByFechaFin", query = "SELECT s FROM SspCarteraCliente s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SspCarteraCliente.findByExpediente", query = "SELECT s FROM SspCarteraCliente s WHERE s.expediente = :expediente"),
    @NamedQuery(name = "SspCarteraCliente.findByObservacion", query = "SELECT s FROM SspCarteraCliente s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SspCarteraCliente.findByDetalleMotivo", query = "SELECT s FROM SspCarteraCliente s WHERE s.detalleMotivo = :detalleMotivo"),
    @NamedQuery(name = "SspCarteraCliente.findByFlagEvaluacion", query = "SELECT s FROM SspCarteraCliente s WHERE s.flagEvaluacion = :flagEvaluacion"),
    @NamedQuery(name = "SspCarteraCliente.findByActivo", query = "SELECT s FROM SspCarteraCliente s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspCarteraCliente.findByAudLogin", query = "SELECT s FROM SspCarteraCliente s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspCarteraCliente.findByAudNumIp", query = "SELECT s FROM SspCarteraCliente s WHERE s.audNumIp = :audNumIp")})
public class SspCarteraCliente implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_CARTERA_CLIENTE")
    @SequenceGenerator(name = "SEQ_SSP_CARTERA_CLIENTE", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_CARTERA_CLIENTE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "EXPEDIENTE")
    private Long expediente;
    @Size(max = 300)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Size(max = 200)
    @Column(name = "DETALLE_MOTIVO")
    private String detalleMotivo;
    @Column(name = "FLAG_EVALUACION")
    private Character flagEvaluacion;
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
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
    @JoinTable(schema="BDINTEGRADO", name = "SSP_CARTERA_MODALIDAD", joinColumns = {
        @JoinColumn(name = "CARTERA_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<TipoSeguridad> tipoSeguridadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carteraId")
    private List<SspCarteraVigilante> sspCarteraVigilanteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carteraId")
    private List<SspCarteraEvento> sspCarteraEventoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carteraId")
    private List<SspCarteraArma> sspCarteraArmaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carteraId")
    private List<SspCarteraVehiculo> sspCarteraVehiculoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carteraId")
    private List<SspLugarServicio> sspLugarServicioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cartera")
    private List<SspDocumento> sspDocumentoList;
    @JoinColumn(name = "MOTIVO_CONTRATO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoSeguridad motivoContratoId;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad estadoId;
    @JoinColumn(name = "TIPO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad tipoOpeId;
    @OneToMany(mappedBy = "contratoId")
    private List<SspCarteraCliente> sspCarteraClienteList;
    @JoinColumn(name = "CONTRATO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SspCarteraCliente contratoId;
    @JoinColumn(name = "CLIENTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt clienteId;
    @JoinColumn(name = "REPR_ADMIN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt reprAdminId;
    @JoinColumn(name = "REPR_CLI_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt reprCliId;
    @JoinColumn(name = "ADMINISTRADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt administradoId;
    @JoinColumn(name = "SEDE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDireccionGt sedeId;
    @JoinColumn(name = "DIREC_CLIEN_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbDireccionGt direcClienId;
    @Size(max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
    @Column(name = "FECHA_FIN_ACTUALIZA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinActualiza;
    @JoinTable(schema="BDINTEGRADO", name = "SSP_CARTERA_PERSONA", joinColumns = {
        @JoinColumn(name = "CARTERA_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbPersonaGt> sbPersonaList;
    @Size(max = 400)
    @Column(name = "MOTIVO_FECHA")
    private String motivoFecha;
    @Column(name = "EVENTO")
    private Short evento;
    @Size(max = 200)
    @Column(name = "TIPO_EVENTO")
    private String tipoEvento;    
    @Column(name = "TODO_RECURSOS")
    private Short todoRecursos;

    public SspCarteraCliente() {
    }

    public SspCarteraCliente(Long id) {
        this.id = id;
    }

    public SspCarteraCliente(Long id, Date fechaInicio, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaInicio = fechaInicio;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getExpediente() {
        return expediente;
    }

    public void setExpediente(Long expediente) {
        this.expediente = expediente;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDetalleMotivo() {
        return detalleMotivo;
    }

    public void setDetalleMotivo(String detalleMotivo) {
        this.detalleMotivo = detalleMotivo;
    }

    public Character getFlagEvaluacion() {
        return flagEvaluacion;
    }

    public void setFlagEvaluacion(Character flagEvaluacion) {
        this.flagEvaluacion = flagEvaluacion;
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
    public List<TipoSeguridad> getTipoSeguridadList() {
        return tipoSeguridadList;
    }

    public void setTipoSeguridadList(List<TipoSeguridad> tipoSeguridadList) {
        this.tipoSeguridadList = tipoSeguridadList;
    }


    @XmlTransient
    public List<SspDocumento> getSspDocumentoList() {
        return sspDocumentoList;
    }

    public void setSspDocumentoList(List<SspDocumento> sspDocumentoList) {
        this.sspDocumentoList = sspDocumentoList;
    }
    
    public TipoSeguridad getMotivoContratoId() {
        return motivoContratoId;
    }

    public void setMotivoContratoId(TipoSeguridad motivoContratoId) {
        this.motivoContratoId = motivoContratoId;
    }

    public TipoSeguridad getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoSeguridad estadoId) {
        this.estadoId = estadoId;
    }

    public TipoSeguridad getTipoOpeId() {
        return tipoOpeId;
    }

    public void setTipoOpeId(TipoSeguridad tipoOpeId) {
        this.tipoOpeId = tipoOpeId;
    }

    @XmlTransient
    public List<SspCarteraCliente> getSspCarteraClienteList() {
        return sspCarteraClienteList;
    }

    public void setSspCarteraClienteList(List<SspCarteraCliente> sspCarteraClienteList) {
        this.sspCarteraClienteList = sspCarteraClienteList;
    }

    public SspCarteraCliente getContratoId() {
        return contratoId;
    }

    public void setContratoId(SspCarteraCliente contratoId) {
        this.contratoId = contratoId;
    }

    public SbPersonaGt getClienteId() {
        return clienteId;
    }

    public void setClienteId(SbPersonaGt clienteId) {
        this.clienteId = clienteId;
    }

    public SbPersonaGt getReprAdminId() {
        return reprAdminId;
    }

    public void setReprAdminId(SbPersonaGt reprAdminId) {
        this.reprAdminId = reprAdminId;
    }

    public SbPersonaGt getReprCliId() {
        return reprCliId;
    }

    public void setReprCliId(SbPersonaGt reprCliId) {
        this.reprCliId = reprCliId;
    }

    public SbPersonaGt getAdministradoId() {
        return administradoId;
    }

    public void setAdministradoId(SbPersonaGt administradoId) {
        this.administradoId = administradoId;
    }

    public SbDireccionGt getSedeId() {
        return sedeId;
    }

    public void setSedeId(SbDireccionGt sedeId) {
        this.sedeId = sedeId;
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
        if (!(object instanceof SspCarteraCliente)) {
            return false;
        }
        SspCarteraCliente other = (SspCarteraCliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspCarteraCliente[ id=" + id + " ]";
    }
    
     public SbDireccionGt getDirecClienId() {
        return direcClienId;
    }

    public void setDirecClienId(SbDireccionGt direcClienId) {
        this.direcClienId = direcClienId;
    }
    
    @XmlTransient
    public List<SspCarteraVigilante> getSspCarteraVigilanteList() {
        return sspCarteraVigilanteList;
    }

    public void setSspCarteraVigilanteList(List<SspCarteraVigilante> sspCarteraVigilanteList) {
        this.sspCarteraVigilanteList = sspCarteraVigilanteList;
    }

    @XmlTransient
    public List<SspCarteraEvento> getSspCarteraEventoList() {
        return sspCarteraEventoList;
    }

    public void setSspCarteraEventoList(List<SspCarteraEvento> sspCarteraEventoList) {
        this.sspCarteraEventoList = sspCarteraEventoList;
    }

    @XmlTransient
    public List<SspCarteraArma> getSspCarteraArmaList() {
        return sspCarteraArmaList;
    }

    public void setSspCarteraArmaList(List<SspCarteraArma> sspCarteraArmaList) {
        this.sspCarteraArmaList = sspCarteraArmaList;
    }

    @XmlTransient
    public List<SspCarteraVehiculo> getSspCarteraVehiculoList() {
        return sspCarteraVehiculoList;
    }

    public void setSspCarteraVehiculoList(List<SspCarteraVehiculo> sspCarteraVehiculoList) {
        this.sspCarteraVehiculoList = sspCarteraVehiculoList;
    }

    @XmlTransient
    public List<SspLugarServicio> getSspLugarServicioList() {
        return sspLugarServicioList;
    }

    public void setSspLugarServicioList(List<SspLugarServicio> sspLugarServicioList) {
        this.sspLugarServicioList = sspLugarServicioList;
    }
    
    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    public Date getFechaFinActualiza() {
        return fechaFinActualiza;
    }

    public void setFechaFinActualiza(Date fechaFinActualiza) {
        this.fechaFinActualiza = fechaFinActualiza;
    }
    
    public String getHashQr() {
        return hashQr;
    }

    public void setHashQr(String hashQr) {
        this.hashQr = hashQr;
    }
    
    @XmlTransient
    public List<SbPersonaGt> getSbPersonaList() {
        return sbPersonaList;
    }

    public void setSbPersonaList(List<SbPersonaGt> sbPersonaList) {
        this.sbPersonaList = sbPersonaList;
    }
    
    public String getMotivoFecha() {
        return motivoFecha;
    }

    public void setMotivoFecha(String motivoFecha) {
        this.motivoFecha = motivoFecha;
    }
    
    public Short getEvento() {
        return evento;
    }

    public void setEvento(Short evento) {
        this.evento = evento;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
    public Short getTodoRecursos() {
        return todoRecursos;
    }

    public void setTodoRecursos(Short todoRecursos) {
        this.todoRecursos = todoRecursos;
    }
    
    public boolean getTodoRecursosBoolean() {
        if(todoRecursos == null){
            return false;
        }else{
            return (todoRecursos == 1);
        }
    }
    
    public void setTodoRecursosBoolean(boolean todoRecursos) {
        this.todoRecursos = ((todoRecursos)?((short)1):((short)0));
    }
}
