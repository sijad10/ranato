/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
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
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_LIBRO_MES", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppLibroMes.findAll", query = "SELECT e FROM EppLibroMes e"),
    @NamedQuery(name = "EppLibroMes.findById", query = "SELECT e FROM EppLibroMes e WHERE e.id = :id"),
    @NamedQuery(name = "EppLibroMes.findByUsoExplosivo", query = "SELECT e FROM EppLibroMes e WHERE e.usoExplosivo = :usoExplosivo"),
    @NamedQuery(name = "EppLibroMes.findByLibromOrden", query = "SELECT e FROM EppLibroMes e WHERE e.libromOrden = :libromOrden"),
    @NamedQuery(name = "EppLibroMes.findByLibromAnio", query = "SELECT e FROM EppLibroMes e WHERE e.libromAnio = :libromAnio"),
    @NamedQuery(name = "EppLibroMes.findByFechaCierre", query = "SELECT e FROM EppLibroMes e WHERE e.fechaCierre = :fechaCierre"),
    @NamedQuery(name = "EppLibroMes.findByObservacion", query = "SELECT e FROM EppLibroMes e WHERE e.observacion = :observacion"),
    @NamedQuery(name = "EppLibroMes.findByActivo", query = "SELECT e FROM EppLibroMes e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppLibroMes.findByAudLogin", query = "SELECT e FROM EppLibroMes e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppLibroMes.findByAudNumIp", query = "SELECT e FROM EppLibroMes e WHERE e.audNumIp = :audNumIp"),
    @NamedQuery(name = "EppLibroMes.findByNroExpediente", query = "SELECT e FROM EppLibroMes e WHERE e.nroExpediente = :nroExpediente")})
public class EppLibroMes implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_LIBRO_MES")
    @SequenceGenerator(name = "SEQ_EPP_LIBRO_MES", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_LIBRO_MES", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USO_EXPLOSIVO")
    private short usoExplosivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LIBROM_ORDEN")
    private Long libromOrden;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LIBROM_ANIO")
    private Long libromAnio;
    @Column(name = "FECHA_CIERRE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCierre;
    @Size(max = 200)
    @Column(name = "OBSERVACION")
    private String observacion;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;

    @Column(name = "SUBSANADO")
    private short subsanado;

    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libroMesId")
    private List<EppLibroPerdida> eppLibroPerdidaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libroMesId")
    private List<EppLibroUsoDiario> eppLibroUsoDiarioList;
    @JoinColumn(name = "TIPO_REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoRegistroId;
    @JoinColumn(name = "TIPO_ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoEstado;
    @JoinColumn(name = "MES_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt mesId;
    @JoinColumn(name = "LIBRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppLibro libroId;

    public EppLibroMes() {
    }

    public EppLibroMes(Long id) {
        this.id = id;
    }

    public EppLibroMes(Long id, short usoExplosivo, Long libromOrden, Long libromAnio, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.usoExplosivo = usoExplosivo;
        this.libromOrden = libromOrden;
        this.libromAnio = libromAnio;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public short getSubsanado() {
        return subsanado;
    }

    public void setSubsanado(short subsanado) {
        this.subsanado = subsanado;
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getUsoExplosivo() {
        return usoExplosivo;
    }

    public void setUsoExplosivo(short usoExplosivo) {
        this.usoExplosivo = usoExplosivo;
    }

    public Long getLibromOrden() {
        return libromOrden;
    }

    public void setLibromOrden(Long libromOrden) {
        this.libromOrden = libromOrden;
    }

    public Long getLibromAnio() {
        return libromAnio;
    }

    public void setLibromAnio(Long libromAnio) {
        this.libromAnio = libromAnio;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    @XmlTransient
    public List<EppLibroPerdida> getEppLibroPerdidaList() {
        return eppLibroPerdidaList;
    }

    public void setEppLibroPerdidaList(List<EppLibroPerdida> eppLibroPerdidaList) {
        this.eppLibroPerdidaList = eppLibroPerdidaList;
    }

    @XmlTransient
    public List<EppLibroUsoDiario> getEppLibroUsoDiarioList() {
        return eppLibroUsoDiarioList;
    }

    public void setEppLibroUsoDiarioList(List<EppLibroUsoDiario> eppLibroUsoDiarioList) {
        this.eppLibroUsoDiarioList = eppLibroUsoDiarioList;
    }

    public TipoExplosivoGt getTipoRegistroId() {
        return tipoRegistroId;
    }

    public void setTipoRegistroId(TipoExplosivoGt tipoRegistroId) {
        this.tipoRegistroId = tipoRegistroId;
    }

    public TipoExplosivoGt getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(TipoExplosivoGt tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public TipoBaseGt getMesId() {
        return mesId;
    }

    public void setMesId(TipoBaseGt mesId) {
        this.mesId = mesId;
    }

    public EppLibro getLibroId() {
        return libroId;
    }

    public void setLibroId(EppLibro libroId) {
        this.libroId = libroId;
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
        if (!(object instanceof EppLibroMes)) {
            return false;
        }
        EppLibroMes other = (EppLibroMes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppLibroMes[ id=" + id + " ]";
    }

}
