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
@Table(name = "SB_DERIVACION_CYDOC", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbDerivacionCydoc.findAll", query = "SELECT s FROM SbDerivacionCydoc s"),
    @NamedQuery(name = "SbDerivacionCydoc.findById", query = "SELECT s FROM SbDerivacionCydoc s WHERE s.id = :id"),
    @NamedQuery(name = "SbDerivacionCydoc.findByCydocProcesoId", query = "SELECT s FROM SbDerivacionCydoc s WHERE s.cydocProcesoId = :cydocProcesoId"),
    @NamedQuery(name = "SbDerivacionCydoc.findByFechaInicio", query = "SELECT s FROM SbDerivacionCydoc s WHERE s.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "SbDerivacionCydoc.findByFechaFin", query = "SELECT s FROM SbDerivacionCydoc s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SbDerivacionCydoc.findByActivo", query = "SELECT s FROM SbDerivacionCydoc s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbDerivacionCydoc.findByAudLogin", query = "SELECT s FROM SbDerivacionCydoc s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbDerivacionCydoc.findByAudNumIp", query = "SELECT s FROM SbDerivacionCydoc s WHERE s.audNumIp = :audNumIp")})
public class SbDerivacionCydoc implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_DERIVACION_CYDOC")
    @SequenceGenerator(name = "SEQ_SB_DERIVACION_CYDOC", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_DERIVACION_CYDOC", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CYDOC_PROCESO_ID")
    private Long cydocProcesoId;
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "TIPO_PROCESO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoProcesoId;
    @JoinColumn(name = "SEDE_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt sedeId;
    @JoinColumn(name = "TIPO_ACCION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoAccionId;
    @JoinColumn(name = "USUARIO_DESTINO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuarioDestinoId;
    @JoinColumn(name = "SISTEMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbSistemaGt sistemaId;

    public SbDerivacionCydoc() {
    }

    public SbDerivacionCydoc(Long id) {
        this.id = id;
    }

    public SbDerivacionCydoc(Long id, Long cydocProcesoId, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.cydocProcesoId = cydocProcesoId;
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

    public Long getCydocProcesoId() {
        return cydocProcesoId;
    }

    public void setCydocProcesoId(Long cydocProcesoId) {
        this.cydocProcesoId = cydocProcesoId;
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

    public TipoBaseGt getTipoProcesoId() {
        return tipoProcesoId;
    }

    public void setTipoProcesoId(TipoBaseGt tipoProcesoId) {
        this.tipoProcesoId = tipoProcesoId;
    }

    public TipoBaseGt getSedeId() {
        return sedeId;
    }

    public void setSedeId(TipoBaseGt sedeId) {
        this.sedeId = sedeId;
    }

    public TipoBaseGt getTipoAccionId() {
        return tipoAccionId;
    }

    public void setTipoAccionId(TipoBaseGt tipoAccionId) {
        this.tipoAccionId = tipoAccionId;
    }

    public SbUsuarioGt getUsuarioDestinoId() {
        return usuarioDestinoId;
    }

    public void setUsuarioDestinoId(SbUsuarioGt usuarioDestinoId) {
        this.usuarioDestinoId = usuarioDestinoId;
    }

    public SbSistemaGt getSistemaId() {
        return sistemaId;
    }

    public void setSistemaId(SbSistemaGt sistemaId) {
        this.sistemaId = sistemaId;
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
        if (!(object instanceof SbDerivacionCydoc)) {
            return false;
        }
        SbDerivacionCydoc other = (SbDerivacionCydoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbDerivacionCydoc[ id=" + id + " ]";
    }
    
}
