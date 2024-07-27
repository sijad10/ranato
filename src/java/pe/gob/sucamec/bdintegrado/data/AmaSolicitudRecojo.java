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
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_SOLICITUD_RECOJO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaSolicitudRecojo.findAll", query = "SELECT a FROM AmaSolicitudRecojo a"),
    @NamedQuery(name = "AmaSolicitudRecojo.findById", query = "SELECT a FROM AmaSolicitudRecojo a WHERE a.id = :id"),
    @NamedQuery(name = "AmaSolicitudRecojo.findByCorrelativo", query = "SELECT a FROM AmaSolicitudRecojo a WHERE a.correlativo = :correlativo"),
    @NamedQuery(name = "AmaSolicitudRecojo.findByFechaEmision", query = "SELECT a FROM AmaSolicitudRecojo a WHERE a.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "AmaSolicitudRecojo.findByFechaVencimiento", query = "SELECT a FROM AmaSolicitudRecojo a WHERE a.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "AmaSolicitudRecojo.findByEstado", query = "SELECT a FROM AmaSolicitudRecojo a WHERE a.estado = :estado"),
    @NamedQuery(name = "AmaSolicitudRecojo.findByActivo", query = "SELECT a FROM AmaSolicitudRecojo a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaSolicitudRecojo.findByAudLogin", query = "SELECT a FROM AmaSolicitudRecojo a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaSolicitudRecojo.findByAudNumIp", query = "SELECT a FROM AmaSolicitudRecojo a WHERE a.audNumIp = :audNumIp")})
public class AmaSolicitudRecojo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_SOLICITUD_RECOJO")
    @SequenceGenerator(name = "SEQ_AMA_SOLICITUD_RECOJO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_SOLICITUD_RECOJO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CORRELATIVO")
    private Long correlativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
    @Column(name = "ESTADO")
    private Short estado;
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
    @Column(name = "FECHA_RETIRO_PROGRAMADA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRetiroProgramada;
    @JoinTable(schema="BDINTEGRADO", name = "AMA_SOLICITUD_INVENTARIO", joinColumns = {
        @JoinColumn(name = "SOLICITUD_RECOJO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "INVENTARIO_ARMA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<AmaInventarioArma> amaInventarioArmaList;
//    @JoinTable(schema = "BDINTEGRADO", name = "AMA_SOLICITUD_TP", joinColumns = {
//        @JoinColumn(name = "GAMAC_SOLICITUD_RECOJO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
//        @JoinColumn(name = "GAMAC_TARJETA_PROPIEDAD_ID", referencedColumnName = "ID")})
//    @ManyToMany
//    private List<AmaTarjetaPropiedad> amaTarjetaPropiedadList;
    @OneToMany(mappedBy = "solicitudRecojoId")
    private List<AmaGuiaTransito> amaGuiaTransitoList;
    @JoinColumn(name = "DIRECCION_ALMACEN_SUCAMEC_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDireccion direccionAlmacenSucamecId;
    @JoinColumn(name = "DIRECCION_LOCAL_ARMERIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDireccion direccionLocalArmeriaId;
    @JoinColumn(name = "PERSONA_ARMERIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaArmeriaId;
    @JoinColumn(name = "PERSONA_AUT_RECOJO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaAutRecojoId;
    @JoinColumn(name = "TIPO_SOLICITUD_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoSolicitudId;
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;

    public AmaSolicitudRecojo() {
    }

    public AmaSolicitudRecojo(Long id) {
        this.id = id;
    }

    public AmaSolicitudRecojo(Long id, Long correlativo, Date fechaEmision, Date fechaVencimiento, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.correlativo = correlativo;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
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

    public Long getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(Long correlativo) {
        this.correlativo = correlativo;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
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

//    @XmlTransient
//    public List<AmaTarjetaPropiedad> getAmaTarjetaPropiedadList() {
//        return amaTarjetaPropiedadList;
//    }
//
//    public void setAmaTarjetaPropiedadList(List<AmaTarjetaPropiedad> amaTarjetaPropiedadList) {
//        this.amaTarjetaPropiedadList = amaTarjetaPropiedadList;
//    }

    @XmlTransient
    public List<AmaGuiaTransito> getAmaGuiaTransitoList() {
        return amaGuiaTransitoList;
    }

    public void setAmaGuiaTransitoList(List<AmaGuiaTransito> amaGuiaTransitoList) {
        this.amaGuiaTransitoList = amaGuiaTransitoList;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
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
        if (!(object instanceof AmaSolicitudRecojo)) {
            return false;
        }
        AmaSolicitudRecojo other = (AmaSolicitudRecojo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaSolicitudRecojo[ id=" + id + " ]";
    }

    /**
     * @return the direccionAlmacenSucamecId
     */
    public SbDireccion getDireccionAlmacenSucamecId() {
        return direccionAlmacenSucamecId;
    }

    /**
     * @param direccionAlmacenSucamecId the direccionAlmacenSucamecId to set
     */
    public void setDireccionAlmacenSucamecId(SbDireccion direccionAlmacenSucamecId) {
        this.direccionAlmacenSucamecId = direccionAlmacenSucamecId;
    }

    /**
     * @return the direccionLocalArmeriaId
     */
    public SbDireccion getDireccionLocalArmeriaId() {
        return direccionLocalArmeriaId;
    }

    /**
     * @param direccionLocalArmeriaId the direccionLocalArmeriaId to set
     */
    public void setDireccionLocalArmeriaId(SbDireccion direccionLocalArmeriaId) {
        this.direccionLocalArmeriaId = direccionLocalArmeriaId;
    }

    /**
     * @return the personaArmeriaId
     */
    public SbPersona getPersonaArmeriaId() {
        return personaArmeriaId;
    }

    /**
     * @param personaArmeriaId the personaArmeriaId to set
     */
    public void setPersonaArmeriaId(SbPersona personaArmeriaId) {
        this.personaArmeriaId = personaArmeriaId;
    }

    /**
     * @return the personaAutRecojoId
     */
    public SbPersona getPersonaAutRecojoId() {
        return personaAutRecojoId;
    }

    /**
     * @param personaAutRecojoId the personaAutRecojoId to set
     */
    public void setPersonaAutRecojoId(SbPersona personaAutRecojoId) {
        this.personaAutRecojoId = personaAutRecojoId;
    }
    
    public Date getFechaRetiroProgramada() {
        return fechaRetiroProgramada;
    }

    public void setFechaRetiroProgramada(Date fechaRetiroProgramada) {
        this.fechaRetiroProgramada = fechaRetiroProgramada;
    }
    @XmlTransient
    public List<AmaInventarioArma> getAmaInventarioArmaList() {
        return amaInventarioArmaList;
    }

    public void setAmaInventarioArmaList(List<AmaInventarioArma> amaInventarioArmaList) {
        this.amaInventarioArmaList = amaInventarioArmaList;
    }

    public TipoGamac getTipoSolicitudId() {
        return tipoSolicitudId;
    }

    public void setTipoSolicitudId(TipoGamac tipoSolicitudId) {
        this.tipoSolicitudId = tipoSolicitudId;
    }
    
}
