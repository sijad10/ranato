/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author ocastillo
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_RESOLUCION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspResolucion.findAll", query = "SELECT s FROM SspResolucion s"),
    @NamedQuery(name = "SspResolucion.findById", query = "SELECT s FROM SspResolucion s WHERE s.id = :id"),
    @NamedQuery(name = "SspResolucion.findByTipoResolucion", query = "SELECT s FROM SspResolucion s WHERE s.tipoResolucion = :tipoResolucion"),
    @NamedQuery(name = "SspResolucion.findByNumero", query = "SELECT s FROM SspResolucion s WHERE s.numero = :numero"),
    @NamedQuery(name = "SspResolucion.findByFecha", query = "SELECT s FROM SspResolucion s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspResolucion.findByFechaIni", query = "SELECT s FROM SspResolucion s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SspResolucion.findByFechaFin", query = "SELECT s FROM SspResolucion s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SspResolucion.findByObservacion", query = "SELECT s FROM SspResolucion s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SspResolucion.findByEstadoId", query = "SELECT s FROM SspResolucion s WHERE s.estadoId = :estadoId"),
    @NamedQuery(name = "SspResolucion.findByHashQr", query = "SELECT s FROM SspResolucion s WHERE s.hashQr = :hashQr"),
    @NamedQuery(name = "SspResolucion.findByActivo", query = "SELECT s FROM SspResolucion s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspResolucion.findByAudLogin", query = "SELECT s FROM SspResolucion s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspResolucion.findByAudNumIp", query = "SELECT s FROM SspResolucion s WHERE s.audNumIp = :audNumIp")})
public class SspResolucion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_RESOLUCION")
    @SequenceGenerator(name = "SEQ_SSP_RESOLUCION", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_RESOLUCION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;
    @Column(name = "TIPO_RESOLUCION")
    private Long tipoResolucion;
    @Size(max = 100)
    @Column(name = "NUMERO")
    private String numero;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Size(max = 300)
    @Column(name = "OBSERVACION")
    private String observacion;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad estadoId;
    @Size(max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;

    public SspResolucion() {
    }

    public SspResolucion(Long id) {
        this.id = id;
    }

    public SspResolucion(Long id, short activo, String audLogin, String audNumIp) {
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
   
    public Long getTipoResolucion() {
        return tipoResolucion;
    }

    public void setTipoResolucion(Long tipoResolucion) {
        this.tipoResolucion = tipoResolucion;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public String getHashQr() {
        return hashQr;
    }

    public void setHashQr(String hashQr) {
        this.hashQr = hashQr;
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

    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
    }

    public TipoSeguridad getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoSeguridad estadoId) {
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
        if (!(object instanceof SspResolucion)) {
            return false;
        }
        SspResolucion other = (SspResolucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspResolucion[ id=" + id + " ]";
    }
    
}
