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
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_PUBLICACION_DOC", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbPublicacionDoc.findAll", query = "SELECT s FROM SbPublicacionDoc s"),
    @NamedQuery(name = "SbPublicacionDoc.findById", query = "SELECT s FROM SbPublicacionDoc s WHERE s.id = :id"),
    @NamedQuery(name = "SbPublicacionDoc.findByExpediente", query = "SELECT s FROM SbPublicacionDoc s WHERE s.expediente = :expediente"),
    @NamedQuery(name = "SbPublicacionDoc.findByTitulo", query = "SELECT s FROM SbPublicacionDoc s WHERE s.titulo = :titulo"),
    @NamedQuery(name = "SbPublicacionDoc.findByFecha", query = "SELECT s FROM SbPublicacionDoc s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SbPublicacionDoc.findByArchivo", query = "SELECT s FROM SbPublicacionDoc s WHERE s.archivo = :archivo"),
    @NamedQuery(name = "SbPublicacionDoc.findByNumeroDoc", query = "SELECT s FROM SbPublicacionDoc s WHERE s.numeroDoc = :numeroDoc"),
    @NamedQuery(name = "SbPublicacionDoc.findByFechaDoc", query = "SELECT s FROM SbPublicacionDoc s WHERE s.fechaDoc = :fechaDoc"),
    @NamedQuery(name = "SbPublicacionDoc.findByActivo", query = "SELECT s FROM SbPublicacionDoc s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbPublicacionDoc.findByAudLogin", query = "SELECT s FROM SbPublicacionDoc s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbPublicacionDoc.findByAudNumIp", query = "SELECT s FROM SbPublicacionDoc s WHERE s.audNumIp = :audNumIp")})
public class SbPublicacionDoc implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_PUBLICACION_DOC")
    @SequenceGenerator(name = "SEQ_SB_PUBLICACION_DOC", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_PUBLICACION_DOC", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "EXPEDIENTE")
    private String expediente;
    @Size(max = 200)
    @Column(name = "TITULO")
    private String titulo;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 100)
    @Column(name = "ARCHIVO")
    private String archivo;
    @Size(max = 100)
    @Column(name = "NUMERO_DOC")
    private String numeroDoc;
    @Column(name = "FECHA_DOC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDoc;
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
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase areaId;
    @JoinColumn(name = "TIPO_DOC", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase tipoDoc;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase estadoId;

    public SbPublicacionDoc() {
    }

    public SbPublicacionDoc(Long id) {
        this.id = id;
    }

    public SbPublicacionDoc(Long id, short activo, String audLogin, String audNumIp) {
        this.id = id;
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

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getNumeroDoc() {
        return numeroDoc;
    }

    public void setNumeroDoc(String numeroDoc) {
        this.numeroDoc = numeroDoc;
    }

    public Date getFechaDoc() {
        return fechaDoc;
    }

    public void setFechaDoc(Date fechaDoc) {
        this.fechaDoc = fechaDoc;
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

    public TipoBase getAreaId() {
        return areaId;
    }

    public void setAreaId(TipoBase areaId) {
        this.areaId = areaId;
    }

    public TipoBase getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoBase tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public TipoBase getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoBase estadoId) {
        this.estadoId = estadoId;
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
        if (!(object instanceof SbPublicacionDoc)) {
            return false;
        }
        SbPublicacionDoc other = (SbPublicacionDoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbPublicacionDoc[ id=" + id + " ]";
    }
    
}
