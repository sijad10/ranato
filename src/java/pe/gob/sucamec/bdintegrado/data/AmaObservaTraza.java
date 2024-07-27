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
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_OBSERVA_TRAZA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaObservaTraza.findAll", query = "SELECT a FROM AmaObservaTraza a"),
    @NamedQuery(name = "AmaObservaTraza.findById", query = "SELECT a FROM AmaObservaTraza a WHERE a.id = :id"),
    @NamedQuery(name = "AmaObservaTraza.findByDescripObervacion", query = "SELECT a FROM AmaObservaTraza a WHERE a.descripObervacion = :descripObervacion"),
    @NamedQuery(name = "AmaObservaTraza.findByFechaObservacion", query = "SELECT a FROM AmaObservaTraza a WHERE a.fechaObservacion = :fechaObservacion"),
    @NamedQuery(name = "AmaObservaTraza.findByActivo", query = "SELECT a FROM AmaObservaTraza a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaObservaTraza.findByAudLogin", query = "SELECT a FROM AmaObservaTraza a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaObservaTraza.findByAudNumIp", query = "SELECT a FROM AmaObservaTraza a WHERE a.audNumIp = :audNumIp")})
public class AmaObservaTraza implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_OBSERVA_TRAZA")
    @SequenceGenerator(name = "SEQ_AMA_OBSERVA_TRAZA", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_OBSERVA_TRAZA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "DESCRIP_OBERVACION")
    private String descripObervacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_OBSERVACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaObservacion;
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
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioId;
    @JoinColumn(name = "RESOLUCION_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaResolucion resolucionId;
    @JoinColumn(name = "DOCUMENTO_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaDocumento documentoId;

    public AmaObservaTraza() {
    }

    public AmaObservaTraza(Long id) {
        this.id = id;
    }

    public AmaObservaTraza(Long id, String descripObervacion, Date fechaObservacion, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.descripObervacion = descripObervacion;
        this.fechaObservacion = fechaObservacion;
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

    public String getDescripObervacion() {
        return descripObervacion;
    }

    public void setDescripObervacion(String descripObervacion) {
        this.descripObervacion = descripObervacion;
    }

    public Date getFechaObservacion() {
        return fechaObservacion;
    }

    public void setFechaObservacion(Date fechaObservacion) {
        this.fechaObservacion = fechaObservacion;
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

    public SbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public AmaResolucion getResolucionId() {
        return resolucionId;
    }

    public void setResolucionId(AmaResolucion resolucionId) {
        this.resolucionId = resolucionId;
    }

    public AmaDocumento getDocumentoId() {
        return documentoId;
    }

    public void setDocumentoId(AmaDocumento documentoId) {
        this.documentoId = documentoId;
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
        if (!(object instanceof AmaObservaTraza)) {
            return false;
        }
        AmaObservaTraza other = (AmaObservaTraza) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaObservaTraza[ id=" + id + " ]";
    }
    
}
