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
@Table(name = "EPP_COM", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacEppCom.findAll", query = "SELECT g FROM GamacEppCom g"),
    @NamedQuery(name = "GamacEppCom.findById", query = "SELECT g FROM GamacEppCom g WHERE g.id = :id"),
    @NamedQuery(name = "GamacEppCom.findByNroCom", query = "SELECT g FROM GamacEppCom g WHERE g.nroCom = :nroCom"),
    @NamedQuery(name = "GamacEppCom.findByFechaEmision", query = "SELECT g FROM GamacEppCom g WHERE g.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "GamacEppCom.findByFechaInicio", query = "SELECT g FROM GamacEppCom g WHERE g.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "GamacEppCom.findByFechaFin", query = "SELECT g FROM GamacEppCom g WHERE g.fechaFin = :fechaFin"),
    @NamedQuery(name = "GamacEppCom.findByNroExpediente", query = "SELECT g FROM GamacEppCom g WHERE g.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "GamacEppCom.findByActivo", query = "SELECT g FROM GamacEppCom g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacEppCom.findByAudLogin", query = "SELECT g FROM GamacEppCom g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacEppCom.findByAudNumIp", query = "SELECT g FROM GamacEppCom g WHERE g.audNumIp = :audNumIp")})
public class GamacEppCom implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_EPP_COM")
    @SequenceGenerator(name = "SEQ_GAMAC_EPP_COM", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_COM", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 120)
    @Column(name = "NRO_COM")
    private String nroCom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
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
    @OneToMany(mappedBy = "comId")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection;
    @JoinColumn(name = "TIPO_COM", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoExplosivo tipoCom;
    @JoinColumn(name = "TIPO_ESTADO_2DO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoExplosivo tipoEstado2do;
    @JoinColumn(name = "TIPO_ESTADO_1ER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoExplosivo tipoEstado1er;
    @JoinColumn(name = "ENTIDAD_EMISORA", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoExplosivo entidadEmisora;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbPersona empresaId;
    @JoinColumn(name = "DIRECCION_REGIONAL", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbDepartamento direccionRegional;
    @OneToMany(mappedBy = "comId")
    private Collection<GamacEppCom> gamacEppComCollection;
    @JoinColumn(name = "COM_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacEppCom comId;

    public GamacEppCom() {
    }

    public GamacEppCom(Long id) {
        this.id = id;
    }

    public GamacEppCom(Long id, String nroCom, Date fechaEmision, Date fechaInicio, Date fechaFin, String nroExpediente, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroCom = nroCom;
        this.fechaEmision = fechaEmision;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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

    public String getNroCom() {
        return nroCom;
    }

    public void setNroCom(String nroCom) {
        this.nroCom = nroCom;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
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

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
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
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection() {
        return gamacEppRegistroCollection;
    }

    public void setGamacEppRegistroCollection(Collection<GamacEppRegistro> gamacEppRegistroCollection) {
        this.gamacEppRegistroCollection = gamacEppRegistroCollection;
    }

    public GamacTipoExplosivo getTipoCom() {
        return tipoCom;
    }

    public void setTipoCom(GamacTipoExplosivo tipoCom) {
        this.tipoCom = tipoCom;
    }

    public GamacTipoExplosivo getTipoEstado2do() {
        return tipoEstado2do;
    }

    public void setTipoEstado2do(GamacTipoExplosivo tipoEstado2do) {
        this.tipoEstado2do = tipoEstado2do;
    }

    public GamacTipoExplosivo getTipoEstado1er() {
        return tipoEstado1er;
    }

    public void setTipoEstado1er(GamacTipoExplosivo tipoEstado1er) {
        this.tipoEstado1er = tipoEstado1er;
    }

    public GamacTipoExplosivo getEntidadEmisora() {
        return entidadEmisora;
    }

    public void setEntidadEmisora(GamacTipoExplosivo entidadEmisora) {
        this.entidadEmisora = entidadEmisora;
    }

    public GamacSbPersona getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(GamacSbPersona empresaId) {
        this.empresaId = empresaId;
    }

    public GamacSbDepartamento getDireccionRegional() {
        return direccionRegional;
    }

    public void setDireccionRegional(GamacSbDepartamento direccionRegional) {
        this.direccionRegional = direccionRegional;
    }

    @XmlTransient
    public Collection<GamacEppCom> getGamacEppComCollection() {
        return gamacEppComCollection;
    }

    public void setGamacEppComCollection(Collection<GamacEppCom> gamacEppComCollection) {
        this.gamacEppComCollection = gamacEppComCollection;
    }

    public GamacEppCom getComId() {
        return comId;
    }

    public void setComId(GamacEppCom comId) {
        this.comId = comId;
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
        if (!(object instanceof GamacEppCom)) {
            return false;
        }
        GamacEppCom other = (GamacEppCom) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacEppCom[ id=" + id + " ]";
    }
    
}
