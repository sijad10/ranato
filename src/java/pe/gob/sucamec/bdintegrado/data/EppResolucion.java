/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_RESOLUCION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppResolucion.findAll", query = "SELECT e FROM EppResolucion e"),
    @NamedQuery(name = "EppResolucion.findById", query = "SELECT e FROM EppResolucion e WHERE e.id = :id"),
    @NamedQuery(name = "EppResolucion.findByNumero", query = "SELECT e FROM EppResolucion e WHERE e.numero = :numero"),
    @NamedQuery(name = "EppResolucion.findByFecha", query = "SELECT e FROM EppResolucion e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "EppResolucion.findByFechaIni", query = "SELECT e FROM EppResolucion e WHERE e.fechaIni = :fechaIni"),
    @NamedQuery(name = "EppResolucion.findByFechaFin", query = "SELECT e FROM EppResolucion e WHERE e.fechaFin = :fechaFin"),
    @NamedQuery(name = "EppResolucion.findByObservacion", query = "SELECT e FROM EppResolucion e WHERE e.observacion = :observacion"),
    @NamedQuery(name = "EppResolucion.findByHashQr", query = "SELECT e FROM EppResolucion e WHERE e.hashQr = :hashQr"),
    @NamedQuery(name = "EppResolucion.findByActivo", query = "SELECT e FROM EppResolucion e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppResolucion.findByAudLogin", query = "SELECT e FROM EppResolucion e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppResolucion.findByAudNumIp", query = "SELECT e FROM EppResolucion e WHERE e.audNumIp = :audNumIp")})
public class EppResolucion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_RESOLUCION")
    @SequenceGenerator(name = "SEQ_EPP_RESOLUCION", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_RESOLUCION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
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
    @Size(max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
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
    @OneToMany(mappedBy = "resolucionId")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt estadoId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EppRegistro registroId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "resolucionId")
    private List<EppDetalleIntSalDet> eppDetalleIntSalDetList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "resolucionId")
    private List<EppResIntersalpiro> eppResIntersalpiroList;

    public EppResolucion() {
    }

    public EppResolucion(Long id) {
        this.id = id;
    }

    public EppResolucion(Long id, short activo, String audLogin, String audNumIp) {
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

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList() {
        return eppRegistroGuiaTransitoList;
    }

    public void setEppRegistroGuiaTransitoList(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList) {
        this.eppRegistroGuiaTransitoList = eppRegistroGuiaTransitoList;
    }

    public TipoExplosivoGt getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoExplosivoGt estadoId) {
        this.estadoId = estadoId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }

    @XmlTransient
    public List<EppDetalleIntSalDet> getEppDetalleIntSalDetList() {
        return eppDetalleIntSalDetList;
    }

    public void setEppDetalleIntSalDetList(List<EppDetalleIntSalDet> eppDetalleIntSalDetList) {
        this.eppDetalleIntSalDetList = eppDetalleIntSalDetList;
    }

    @XmlTransient
    public List<EppResIntersalpiro> getEppResIntersalpiroList() {
        return eppResIntersalpiroList;
    }

    public void setEppResIntersalpiroList(List<EppResIntersalpiro> eppResIntersalpiroList) {
        this.eppResIntersalpiroList = eppResIntersalpiroList;
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
        if (!(object instanceof EppResolucion)) {
            return false;
        }
        EppResolucion other = (EppResolucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppResolucion[ id=" + id + " ]";
    }
    
}
