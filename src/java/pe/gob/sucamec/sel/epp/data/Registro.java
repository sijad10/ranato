/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.epp.data;

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
import javax.persistence.OneToOne;
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
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbTipo;
import pe.gob.sucamec.sistemabase.data.SbUsuario;


/**
 *
 * @author rmoscoso
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_REGISTRO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Registro.findAll", query = "SELECT r FROM Registro r"),
    @NamedQuery(name = "Registro.findById", query = "SELECT r FROM Registro r WHERE r.id = :id"),
    @NamedQuery(name = "Registro.findByFecha", query = "SELECT r FROM Registro r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "Registro.findByExpedienteId", query = "SELECT r FROM Registro r WHERE r.expedienteId = :expedienteId"),
    @NamedQuery(name = "Registro.findByFechaIni", query = "SELECT r FROM Registro r WHERE r.fechaIni = :fechaIni"),
    @NamedQuery(name = "Registro.findByFechaFin", query = "SELECT r FROM Registro r WHERE r.fechaFin = :fechaFin"),
    @NamedQuery(name = "Registro.findByActivo", query = "SELECT r FROM Registro r WHERE r.activo = :activo"),
    @NamedQuery(name = "Registro.findByObservacion", query = "SELECT r FROM Registro r WHERE r.observacion = :observacion"),
    @NamedQuery(name = "Registro.findByAudLogin", query = "SELECT r FROM Registro r WHERE r.audLogin = :audLogin"),
    @NamedQuery(name = "Registro.findByAudNumIp", query = "SELECT r FROM Registro r WHERE r.audNumIp = :audNumIp"),
    @NamedQuery(name = "Registro.findByFlagVenceNoti", query = "SELECT r FROM Registro r WHERE r.flagVenceNoti = :flagVenceNoti")})
public class Registro implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_REGISTRO")
    @SequenceGenerator(name = "SEQ_EPP_REGISTRO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_REGISTRO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXPEDIENTE_ID")
    private Long expedienteId;
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Size(max = 300)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @Column(name = "FLAG_VENCE_NOTI")
    private Short flagVenceNoti;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_REGISTRO_REPRESENTANTE", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "REPRESENTANTE_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbPersona> sbPersonaList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private RegistroGuiaTransito registroGuiaTransito;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<Licencia> licenciaList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registro")
    private Polvorin polvorin;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona empresaId;
    @JoinColumn(name = "REPRESENTANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona representanteId;       
    @OneToMany(mappedBy = "registroOpeId")
    private List<Registro> registroList;
    @JoinColumn(name = "REGISTRO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Registro registroOpeId;
    @OneToMany(mappedBy = "registroId")
    private List<Registro> registroList1;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private Registro registroId;
    @JoinColumn(name = "TIPO_REG_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoRegId;
    @JoinColumn(name = "TIPO_PRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoProId;
    @JoinColumn(name = "TIPO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoOpeId;
    @JoinColumn(name = "ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivo estado;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private Resolucion resolucion;

    public Registro() {
    }

    public Registro(Long id) {
        this.id = id;
    }

    public Registro(Long id, Date fecha, Long expedienteId, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fecha = fecha;
        this.expedienteId = expedienteId;
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

    public Long getExpedienteId() {
        return expedienteId;
    }

    public void setExpedienteId(Long expedienteId) {
        this.expedienteId = expedienteId;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public Short getFlagVenceNoti() {
        return flagVenceNoti;
    }

    public void setFlagVenceNoti(Short flagVenceNoti) {
        this.flagVenceNoti = flagVenceNoti;
    }

    @XmlTransient
    public List<SbPersona> getSbPersonaList() {
        return sbPersonaList;
    }

    public void setSbPersonaList(List<SbPersona> sbPersonaList) {
        this.sbPersonaList = sbPersonaList;
    }

    public RegistroGuiaTransito getRegistroGuiaTransito() {
        return registroGuiaTransito;
    }

    public void setRegistroGuiaTransito(RegistroGuiaTransito registroGuiaTransito) {
        this.registroGuiaTransito = registroGuiaTransito;
    }

    @XmlTransient
    public List<Licencia> getLicenciaList() {
        return licenciaList;
    }

    public void setLicenciaList(List<Licencia> licenciaList) {
        this.licenciaList = licenciaList;
    }

    public Polvorin getPolvorin() {
        return polvorin;
    }

    public void setPolvorin(Polvorin polvorin) {
        this.polvorin = polvorin;
    }

    public SbPersona getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersona empresaId) {
        this.empresaId = empresaId;
    }

    public SbPersona getRepresentanteId() {
        return representanteId;
    }

    public void setRepresentanteId(SbPersona representanteId) {
        this.representanteId = representanteId;
    }
    
    @XmlTransient
    public List<Registro> getRegistroList() {
        return registroList;
    }

    public void setRegistroList(List<Registro> registroList) {
        this.registroList = registroList;
    }

    public Registro getRegistroOpeId() {
        return registroOpeId;
    }

    public void setRegistroOpeId(Registro registroOpeId) {
        this.registroOpeId = registroOpeId;
    }

    @XmlTransient
    public List<Registro> getRegistroList1() {
        return registroList1;
    }

    public void setRegistroList1(List<Registro> registroList1) {
        this.registroList1 = registroList1;
    }

    public Registro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Registro registroId) {
        this.registroId = registroId;
    }

    public SbTipo getTipoRegId() {
        return tipoRegId;
    }

    public void setTipoRegId(SbTipo tipoRegId) {
        this.tipoRegId = tipoRegId;
    }

    public SbTipo getTipoProId() {
        return tipoProId;
    }

    public void setTipoProId(SbTipo tipoProId) {
        this.tipoProId = tipoProId;
    }

    public SbTipo getTipoOpeId() {
        return tipoOpeId;
    }

    public void setTipoOpeId(SbTipo tipoOpeId) {
        this.tipoOpeId = tipoOpeId;
    }

    public TipoExplosivo getEstado() {
        return estado;
    }

    public void setEstado(TipoExplosivo estado) {
        this.estado = estado;
    }

    public Resolucion getResolucion() {
        return resolucion;
    }

    public void setResolucion(Resolucion resolucion) {
        this.resolucion = resolucion;
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
        if (!(object instanceof Registro)) {
            return false;
        }
        Registro other = (Registro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.epp.data.Registro[ id=" + id + " ]";
    }
   
}
