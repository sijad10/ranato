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
import java.math.BigDecimal;
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
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_RECIBOS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacSbRecibos.findAll", query = "SELECT g FROM GamacSbRecibos g"),
    @NamedQuery(name = "GamacSbRecibos.findById", query = "SELECT g FROM GamacSbRecibos g WHERE g.id = :id"),
    @NamedQuery(name = "GamacSbRecibos.findByCodTributo", query = "SELECT g FROM GamacSbRecibos g WHERE g.codTributo = :codTributo"),
    @NamedQuery(name = "GamacSbRecibos.findByTipoDocumento", query = "SELECT g FROM GamacSbRecibos g WHERE g.tipoDocumento = :tipoDocumento"),
    @NamedQuery(name = "GamacSbRecibos.findByNroDocumento", query = "SELECT g FROM GamacSbRecibos g WHERE g.nroDocumento = :nroDocumento"),
    @NamedQuery(name = "GamacSbRecibos.findByNroRegistros", query = "SELECT g FROM GamacSbRecibos g WHERE g.nroRegistros = :nroRegistros"),
    @NamedQuery(name = "GamacSbRecibos.findByImporte", query = "SELECT g FROM GamacSbRecibos g WHERE g.importe = :importe"),
    @NamedQuery(name = "GamacSbRecibos.findByFechaMovimiento", query = "SELECT g FROM GamacSbRecibos g WHERE g.fechaMovimiento = :fechaMovimiento"),
    @NamedQuery(name = "GamacSbRecibos.findByNroSecuencia", query = "SELECT g FROM GamacSbRecibos g WHERE g.nroSecuencia = :nroSecuencia"),
    @NamedQuery(name = "GamacSbRecibos.findByHora", query = "SELECT g FROM GamacSbRecibos g WHERE g.hora = :hora"),
    @NamedQuery(name = "GamacSbRecibos.findByCodOficina", query = "SELECT g FROM GamacSbRecibos g WHERE g.codOficina = :codOficina"),
    @NamedQuery(name = "GamacSbRecibos.findByCodCajero", query = "SELECT g FROM GamacSbRecibos g WHERE g.codCajero = :codCajero"),
    @NamedQuery(name = "GamacSbRecibos.findByNroExpediente", query = "SELECT g FROM GamacSbRecibos g WHERE g.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "GamacSbRecibos.findByDevolucion", query = "SELECT g FROM GamacSbRecibos g WHERE g.devolucion = :devolucion"),
    @NamedQuery(name = "GamacSbRecibos.findByCodBanco", query = "SELECT g FROM GamacSbRecibos g WHERE g.codBanco = :codBanco"),
    @NamedQuery(name = "GamacSbRecibos.findByActivo", query = "SELECT g FROM GamacSbRecibos g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacSbRecibos.findByAudLogin", query = "SELECT g FROM GamacSbRecibos g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacSbRecibos.findByAudNumIp", query = "SELECT g FROM GamacSbRecibos g WHERE g.audNumIp = :audNumIp")})
public class GamacSbRecibos implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_SB_RECIBOS")
    @SequenceGenerator(name = "SEQ_GAMAC_SB_RECIBOS", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_RECIBOS", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_TRIBUTO")
    private Long codTributo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIPO_DOCUMENTO")
    private short tipoDocumento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NRO_DOCUMENTO")
    private String nroDocumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_REGISTROS")
    private Long nroRegistros;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "IMPORTE")
    private Double importe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_MOVIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMovimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_SECUENCIA")
    private Long nroSecuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HORA")
    private Long hora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_OFICINA")
    private Long codOficina;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_CAJERO")
    private Long codCajero;
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Column(name = "DEVOLUCION")
    private Short devolucion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_BANCO")
    private short codBanco;
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
    @JoinColumn(name = "PROCESO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbRecibosProceso procesoId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacEppRegistro registroId;

    public GamacSbRecibos() {
    }

    public GamacSbRecibos(Long id) {
        this.id = id;
    }

    public GamacSbRecibos(Long id, Long codTributo, short tipoDocumento, String nroDocumento, Long nroRegistros, Double importe, Date fechaMovimiento, Long nroSecuencia, Long hora, Long codOficina, Long codCajero, short codBanco, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.codTributo = codTributo;
        this.tipoDocumento = tipoDocumento;
        this.nroDocumento = nroDocumento;
        this.nroRegistros = nroRegistros;
        this.importe = importe;
        this.fechaMovimiento = fechaMovimiento;
        this.nroSecuencia = nroSecuencia;
        this.hora = hora;
        this.codOficina = codOficina;
        this.codCajero = codCajero;
        this.codBanco = codBanco;
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

    public Long getCodTributo() {
        return codTributo;
    }

    public void setCodTributo(Long codTributo) {
        this.codTributo = codTributo;
    }

    public short getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(short tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public Long getNroRegistros() {
        return nroRegistros;
    }

    public void setNroRegistros(Long nroRegistros) {
        this.nroRegistros = nroRegistros;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public Long getNroSecuencia() {
        return nroSecuencia;
    }

    public void setNroSecuencia(Long nroSecuencia) {
        this.nroSecuencia = nroSecuencia;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }

    public Long getCodOficina() {
        return codOficina;
    }

    public void setCodOficina(Long codOficina) {
        this.codOficina = codOficina;
    }

    public Long getCodCajero() {
        return codCajero;
    }

    public void setCodCajero(Long codCajero) {
        this.codCajero = codCajero;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public Short getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(Short devolucion) {
        this.devolucion = devolucion;
    }

    public short getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(short codBanco) {
        this.codBanco = codBanco;
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

    public GamacSbRecibosProceso getProcesoId() {
        return procesoId;
    }

    public void setProcesoId(GamacSbRecibosProceso procesoId) {
        this.procesoId = procesoId;
    }

    public GamacEppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(GamacEppRegistro registroId) {
        this.registroId = registroId;
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
        if (!(object instanceof GamacSbRecibos)) {
            return false;
        }
        GamacSbRecibos other = (GamacSbRecibos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacSbRecibos[ id=" + id + " ]";
    }
    
}
