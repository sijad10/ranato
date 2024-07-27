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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_CS_SUSPENSION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbCsSuspension.findAll", query = "SELECT s FROM SbCsSuspension s"),
    @NamedQuery(name = "SbCsSuspension.findById", query = "SELECT s FROM SbCsSuspension s WHERE s.id = :id"),
    @NamedQuery(name = "SbCsSuspension.findByNroDocSustento", query = "SELECT s FROM SbCsSuspension s WHERE s.nroDocSustento = :nroDocSustento"),
    @NamedQuery(name = "SbCsSuspension.findByFecDocSustento", query = "SELECT s FROM SbCsSuspension s WHERE s.fecDocSustento = :fecDocSustento"),
    @NamedQuery(name = "SbCsSuspension.findByNroExpediente", query = "SELECT s FROM SbCsSuspension s WHERE s.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "SbCsSuspension.findByFechaInicio", query = "SELECT s FROM SbCsSuspension s WHERE s.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "SbCsSuspension.findByFechaFin", query = "SELECT s FROM SbCsSuspension s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SbCsSuspension.findByDescripcion", query = "SELECT s FROM SbCsSuspension s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SbCsSuspension.findByActivo", query = "SELECT s FROM SbCsSuspension s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbCsSuspension.findByAudLogin", query = "SELECT s FROM SbCsSuspension s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbCsSuspension.findByAudNumIp", query = "SELECT s FROM SbCsSuspension s WHERE s.audNumIp = :audNumIp")})
public class SbCsSuspension implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_CS_SUSPENSION")
    @SequenceGenerator(name = "SEQ_SB_CS_SUSPENSION", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_CS_SUSPENSION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NRO_DOC_SUSTENTO")
    private String nroDocSustento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FEC_DOC_SUSTENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecDocSustento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Size(max = 300)
    @Column(name = "DESCRIPCION")
    private String descripcion;
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
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoId;
    @JoinColumn(name = "TIPO_DOC_SUSTENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoDocSustentoId;
    @JoinColumn(name = "ESTABLECIMIENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbCsEstablecimiento establecimientoId;

    public SbCsSuspension() {
    }

    public SbCsSuspension(Long id) {
        this.id = id;
    }

    public SbCsSuspension(Long id, String nroDocSustento, Date fecDocSustento, String nroExpediente, Date fechaInicio, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroDocSustento = nroDocSustento;
        this.fecDocSustento = fecDocSustento;
        this.nroExpediente = nroExpediente;
        this.fechaInicio = fechaInicio;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public SbCsSuspension(Long id, String nroDocSustento, Date fecDocSustento, String nroExpediente, Date fechaInicio, Date fechaFin, String descripcion, short activo, String audLogin, String audNumIp, TipoBaseGt tipoId, TipoBaseGt tipoDocSustentoId, SbCsEstablecimiento establecimientoId) {
        this.id = id;
        this.nroDocSustento = nroDocSustento;
        this.fecDocSustento = fecDocSustento;
        this.nroExpediente = nroExpediente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
        this.tipoId = tipoId;
        this.tipoDocSustentoId = tipoDocSustentoId;
        this.establecimientoId = establecimientoId;
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNroDocSustento() {
        return nroDocSustento;
    }

    public void setNroDocSustento(String nroDocSustento) {
        this.nroDocSustento = nroDocSustento;
    }

    public Date getFecDocSustento() {
        return fecDocSustento;
    }

    public void setFecDocSustento(Date fecDocSustento) {
        this.fecDocSustento = fecDocSustento;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public TipoBaseGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBaseGt tipoId) {
        this.tipoId = tipoId;
    }

    public TipoBaseGt getTipoDocSustentoId() {
        return tipoDocSustentoId;
    }

    public void setTipoDocSustentoId(TipoBaseGt tipoDocSustentoId) {
        this.tipoDocSustentoId = tipoDocSustentoId;
    }

    public SbCsEstablecimiento getEstablecimientoId() {
        return establecimientoId;
    }

    public void setEstablecimientoId(SbCsEstablecimiento establecimientoId) {
        this.establecimientoId = establecimientoId;
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
        if (!(object instanceof SbCsSuspension)) {
            return false;
        }
        SbCsSuspension other = (SbCsSuspension) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbCsSuspension[ id=" + id + " ]";
    }
    
}
