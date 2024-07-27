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
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_REGISTRO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacEppRegistro.findAll", query = "SELECT g FROM GamacEppRegistro g"),
    @NamedQuery(name = "GamacEppRegistro.findById", query = "SELECT g FROM GamacEppRegistro g WHERE g.id = :id"),
    @NamedQuery(name = "GamacEppRegistro.findByFecha", query = "SELECT g FROM GamacEppRegistro g WHERE g.fecha = :fecha"),
    @NamedQuery(name = "GamacEppRegistro.findByNroExpediente", query = "SELECT g FROM GamacEppRegistro g WHERE g.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "GamacEppRegistro.findByFechaIni", query = "SELECT g FROM GamacEppRegistro g WHERE g.fechaIni = :fechaIni"),
    @NamedQuery(name = "GamacEppRegistro.findByFechaFin", query = "SELECT g FROM GamacEppRegistro g WHERE g.fechaFin = :fechaFin"),
    @NamedQuery(name = "GamacEppRegistro.findByObservacion", query = "SELECT g FROM GamacEppRegistro g WHERE g.observacion = :observacion"),
    @NamedQuery(name = "GamacEppRegistro.findBySemestre", query = "SELECT g FROM GamacEppRegistro g WHERE g.semestre = :semestre"),
    @NamedQuery(name = "GamacEppRegistro.findByFlagVenceNoti", query = "SELECT g FROM GamacEppRegistro g WHERE g.flagVenceNoti = :flagVenceNoti"),
    @NamedQuery(name = "GamacEppRegistro.findByActivo", query = "SELECT g FROM GamacEppRegistro g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacEppRegistro.findByAudLogin", query = "SELECT g FROM GamacEppRegistro g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacEppRegistro.findByAudNumIp", query = "SELECT g FROM GamacEppRegistro g WHERE g.audNumIp = :audNumIp")})
public class GamacEppRegistro implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_EPP_REGISTRO")
    @SequenceGenerator(name = "SEQ_GAMAC_EPP_REGISTRO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_REGISTRO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Size(max = 300)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Column(name = "SEMESTRE")
    private Short semestre;
    @Column(name = "FLAG_VENCE_NOTI")
    private Short flagVenceNoti;
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
    @JoinTable(schema="BDINTEGRADO", name = "EPP_IMP_EXP_PAISES", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PAIS_ID", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<GamacSbPais> gamacSbPaisCollection;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_IMP_EXP_VIA", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<GamacTipoExplosivo> gamacTipoExplosivoCollection;
    @JoinColumn(name = "ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoExplosivo estado;
    @JoinColumn(name = "TIPO_MODIFICATORIA", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoExplosivo tipoModificatoria;
    @JoinColumn(name = "TIPO_PRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoBase tipoProId;
    @JoinColumn(name = "TIPO_REG_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoBase tipoRegId;
    @JoinColumn(name = "TIPO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoBase tipoOpeId;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbPersona empresaId;
    @JoinColumn(name = "REPRESENTANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbPersona representanteId;
    @OneToMany(mappedBy = "registroId")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacEppRegistro registroId;
    @OneToMany(mappedBy = "registroOpeId")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection1;
    @JoinColumn(name = "REGISTRO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacEppRegistro registroOpeId;
    @JoinColumn(name = "COM_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacEppCom comId;
    @OneToMany(mappedBy = "registroId")
    private Collection<GamacSbRecibos> gamacSbRecibosCollection;

    public GamacEppRegistro() {
    }

    public GamacEppRegistro(Long id) {
        this.id = id;
    }

    public GamacEppRegistro(Long id, Date fecha, String nroExpediente, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fecha = fecha;
        this.nroExpediente = nroExpediente;
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

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Short getSemestre() {
        return semestre;
    }

    public void setSemestre(Short semestre) {
        this.semestre = semestre;
    }

    public Short getFlagVenceNoti() {
        return flagVenceNoti;
    }

    public void setFlagVenceNoti(Short flagVenceNoti) {
        this.flagVenceNoti = flagVenceNoti;
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
    public Collection<GamacSbPais> getGamacSbPaisCollection() {
        return gamacSbPaisCollection;
    }

    public void setGamacSbPaisCollection(Collection<GamacSbPais> gamacSbPaisCollection) {
        this.gamacSbPaisCollection = gamacSbPaisCollection;
    }

    @XmlTransient
    public Collection<GamacTipoExplosivo> getGamacTipoExplosivoCollection() {
        return gamacTipoExplosivoCollection;
    }

    public void setGamacTipoExplosivoCollection(Collection<GamacTipoExplosivo> gamacTipoExplosivoCollection) {
        this.gamacTipoExplosivoCollection = gamacTipoExplosivoCollection;
    }

    public GamacTipoExplosivo getEstado() {
        return estado;
    }

    public void setEstado(GamacTipoExplosivo estado) {
        this.estado = estado;
    }

    public GamacTipoExplosivo getTipoModificatoria() {
        return tipoModificatoria;
    }

    public void setTipoModificatoria(GamacTipoExplosivo tipoModificatoria) {
        this.tipoModificatoria = tipoModificatoria;
    }

    public GamacTipoBase getTipoProId() {
        return tipoProId;
    }

    public void setTipoProId(GamacTipoBase tipoProId) {
        this.tipoProId = tipoProId;
    }

    public GamacTipoBase getTipoRegId() {
        return tipoRegId;
    }

    public void setTipoRegId(GamacTipoBase tipoRegId) {
        this.tipoRegId = tipoRegId;
    }

    public GamacTipoBase getTipoOpeId() {
        return tipoOpeId;
    }

    public void setTipoOpeId(GamacTipoBase tipoOpeId) {
        this.tipoOpeId = tipoOpeId;
    }

    public GamacSbPersona getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(GamacSbPersona empresaId) {
        this.empresaId = empresaId;
    }

    public GamacSbPersona getRepresentanteId() {
        return representanteId;
    }

    public void setRepresentanteId(GamacSbPersona representanteId) {
        this.representanteId = representanteId;
    }

    @XmlTransient
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection() {
        return gamacEppRegistroCollection;
    }

    public void setGamacEppRegistroCollection(Collection<GamacEppRegistro> gamacEppRegistroCollection) {
        this.gamacEppRegistroCollection = gamacEppRegistroCollection;
    }

    public GamacEppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(GamacEppRegistro registroId) {
        this.registroId = registroId;
    }

    @XmlTransient
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection1() {
        return gamacEppRegistroCollection1;
    }

    public void setGamacEppRegistroCollection1(Collection<GamacEppRegistro> gamacEppRegistroCollection1) {
        this.gamacEppRegistroCollection1 = gamacEppRegistroCollection1;
    }

    public GamacEppRegistro getRegistroOpeId() {
        return registroOpeId;
    }

    public void setRegistroOpeId(GamacEppRegistro registroOpeId) {
        this.registroOpeId = registroOpeId;
    }

    public GamacEppCom getComId() {
        return comId;
    }

    public void setComId(GamacEppCom comId) {
        this.comId = comId;
    }

    @XmlTransient
    public Collection<GamacSbRecibos> getGamacSbRecibosCollection() {
        return gamacSbRecibosCollection;
    }

    public void setGamacSbRecibosCollection(Collection<GamacSbRecibos> gamacSbRecibosCollection) {
        this.gamacSbRecibosCollection = gamacSbRecibosCollection;
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
        if (!(object instanceof GamacEppRegistro)) {
            return false;
        }
        GamacEppRegistro other = (GamacEppRegistro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacEppRegistro[ id=" + id + " ]";
    }
    
}
