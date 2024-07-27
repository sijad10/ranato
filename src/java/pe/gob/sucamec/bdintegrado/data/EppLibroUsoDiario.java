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
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "EPP_LIBRO_USO_DIARIO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppLibroUsoDiario.findAll", query = "SELECT e FROM EppLibroUsoDiario e"),
    @NamedQuery(name = "EppLibroUsoDiario.findById", query = "SELECT e FROM EppLibroUsoDiario e WHERE e.id = :id"),
    @NamedQuery(name = "EppLibroUsoDiario.findByDia", query = "SELECT e FROM EppLibroUsoDiario e WHERE e.dia = :dia"),
    @NamedQuery(name = "EppLibroUsoDiario.findByCantidad", query = "SELECT e FROM EppLibroUsoDiario e WHERE e.cantidad = :cantidad"),
    @NamedQuery(name = "EppLibroUsoDiario.findByUnidadMedidaId", query = "SELECT e FROM EppLibroUsoDiario e WHERE e.unidadMedidaId = :unidadMedidaId"),
    @NamedQuery(name = "EppLibroUsoDiario.findByActivo", query = "SELECT e FROM EppLibroUsoDiario e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppLibroUsoDiario.findByAudLogin", query = "SELECT e FROM EppLibroUsoDiario e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppLibroUsoDiario.findByAudNumIp", query = "SELECT e FROM EppLibroUsoDiario e WHERE e.audNumIp = :audNumIp"),
    @NamedQuery(name = "EppLibroUsoDiario.findByNroLote", query = "SELECT e FROM EppLibroUsoDiario e WHERE e.nroLote = :nroLote")})
public class EppLibroUsoDiario implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_LIBRO_USO_DIARIO")
    @SequenceGenerator(name = "SEQ_EPP_LIBRO_USO_DIARIO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_LIBRO_USO_DIARIO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DIA")
    private Long dia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private Double cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNIDAD_MEDIDA_ID")
    private Long unidadMedidaId;
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
    @Size(max = 20)
    @Column(name = "NRO_LOTE")
    private String nroLote;
    @JoinTable(schema = "BDINTEGRADO", name = "EPP_LIBRO_MANIPULADORES", joinColumns = {
        @JoinColumn(name = "LIBRO_USO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "LICENCIA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppLicencia> eppLicenciaList;

    @JoinColumn(name = "TIPO_REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoRegistroId;

    @JoinColumn(name = "TIPO_FINALIDAD", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoFinalidad;
    
    @JoinColumn(name = "TIPO_DETALLE", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoDetalle;

    @JoinColumn(name = "TIPO_ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoEstado;
    @JoinColumn(name = "LIBRO_MES_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppLibroMes libroMesId;
    @JoinColumn(name = "EXPLOSIVO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppExplosivo explosivoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libroUsoDiarioId")
    private List<EppLibroDetalle> eppLibroDetalleList;

    @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppRegistroGuiaTransito guiaTransitoId;

    public EppLibroUsoDiario() {
    }

    public EppLibroUsoDiario(Long id) {
        this.id = id;
    }

    public EppLibroUsoDiario(Long id, Long dia, Double cantidad, Long unidadMedidaId, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.dia = dia;
        this.cantidad = cantidad;
        this.unidadMedidaId = unidadMedidaId;
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

    public Long getDia() {
        return dia;
    }

    public void setDia(Long dia) {
        this.dia = dia;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Long getUnidadMedidaId() {
        return unidadMedidaId;
    }

    public void setUnidadMedidaId(Long unidadMedidaId) {
        this.unidadMedidaId = unidadMedidaId;
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

    public String getNroLote() {
        return nroLote;
    }

    public void setNroLote(String nroLote) {
        this.nroLote = nroLote;
    }

    public EppRegistroGuiaTransito getGuiaTransitoId() {
        return guiaTransitoId;
    }

    public void setGuiaTransitoId(EppRegistroGuiaTransito guiaTransitoId) {
        this.guiaTransitoId = guiaTransitoId;
    }

    public TipoExplosivoGt getTipoFinalidad() {
        return tipoFinalidad;
    }

    public void setTipoFinalidad(TipoExplosivoGt tipoFinalidad) {
        this.tipoFinalidad = tipoFinalidad;
    }

    public TipoExplosivoGt getTipoDetalle() {
        return tipoDetalle;
    }

    public void setTipoDetalle(TipoExplosivoGt tipoDetalle) {
        this.tipoDetalle = tipoDetalle;
    }

    
    
    @XmlTransient
    public List<EppLicencia> getEppLicenciaList() {
        return eppLicenciaList;
    }

    public void setEppLicenciaList(List<EppLicencia> eppLicenciaList) {
        this.eppLicenciaList = eppLicenciaList;
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

    public EppLibroMes getLibroMesId() {
        return libroMesId;
    }

    public void setLibroMesId(EppLibroMes libroMesId) {
        this.libroMesId = libroMesId;
    }

    public EppExplosivo getExplosivoId() {
        return explosivoId;
    }

    public void setExplosivoId(EppExplosivo explosivoId) {
        this.explosivoId = explosivoId;
    }

    @XmlTransient
    public List<EppLibroDetalle> getEppLibroDetalleList() {
        return eppLibroDetalleList;
    }

    public void setEppLibroDetalleList(List<EppLibroDetalle> eppLibroDetalleList) {
        this.eppLibroDetalleList = eppLibroDetalleList;
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
        if (!(object instanceof EppLibroUsoDiario)) {
            return false;
        }
        EppLibroUsoDiario other = (EppLibroUsoDiario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppLibroUsoDiario[ id=" + id + " ]";
    }

}
