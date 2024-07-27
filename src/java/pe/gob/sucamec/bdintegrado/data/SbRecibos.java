/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_RECIBOS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbRecibos.findAll", query = "SELECT s FROM SbRecibos s"),
    @NamedQuery(name = "SbRecibos.findById", query = "SELECT s FROM SbRecibos s WHERE s.id = :id"),
    @NamedQuery(name = "SbRecibos.findByCodTributo", query = "SELECT s FROM SbRecibos s WHERE s.codTributo = :codTributo"),
    @NamedQuery(name = "SbRecibos.findByTipoDocumento", query = "SELECT s FROM SbRecibos s WHERE s.tipoDocumento = :tipoDocumento"),
    @NamedQuery(name = "SbRecibos.findByNroDocumento", query = "SELECT s FROM SbRecibos s WHERE s.nroDocumento = :nroDocumento"),
    @NamedQuery(name = "SbRecibos.findByNroRegistros", query = "SELECT s FROM SbRecibos s WHERE s.nroRegistros = :nroRegistros"),
    @NamedQuery(name = "SbRecibos.findByImporte", query = "SELECT s FROM SbRecibos s WHERE s.importe = :importe"),
    @NamedQuery(name = "SbRecibos.findByFechaMovimiento", query = "SELECT s FROM SbRecibos s WHERE s.fechaMovimiento = :fechaMovimiento"),
    @NamedQuery(name = "SbRecibos.findByNroSecuencia", query = "SELECT s FROM SbRecibos s WHERE s.nroSecuencia = :nroSecuencia"),
    @NamedQuery(name = "SbRecibos.findByHora", query = "SELECT s FROM SbRecibos s WHERE s.hora = :hora"),
    @NamedQuery(name = "SbRecibos.findByCodOficina", query = "SELECT s FROM SbRecibos s WHERE s.codOficina = :codOficina"),
    @NamedQuery(name = "SbRecibos.findByCodCajero", query = "SELECT s FROM SbRecibos s WHERE s.codCajero = :codCajero"),
    @NamedQuery(name = "SbRecibos.findByNroExpediente", query = "SELECT s FROM SbRecibos s WHERE s.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "SbRecibos.findByDevolucion", query = "SELECT s FROM SbRecibos s WHERE s.devolucion = :devolucion"),
    @NamedQuery(name = "SbRecibos.findByCodBanco", query = "SELECT s FROM SbRecibos s WHERE s.codBanco = :codBanco"),
    @NamedQuery(name = "SbRecibos.findByActivo", query = "SELECT s FROM SbRecibos s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbRecibos.findByAudLogin", query = "SELECT s FROM SbRecibos s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbRecibos.findByAudNumIp", query = "SELECT s FROM SbRecibos s WHERE s.audNumIp = :audNumIp")})
public class SbRecibos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_RECIBOS")
    @SequenceGenerator(name = "SEQ_SB_RECIBOS",schema = "BDINTEGRADO", sequenceName = "SEQ_SB_RECIBOS", allocationSize = 1)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_TRIBUTO")
    private long codTributo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIPO_DOCUMENTO")
    private short tipoDocumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_DOCUMENTO")
    private String nroDocumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_REGISTROS")
    private long nroRegistros;
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
    private long hora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_OFICINA")
    private long codOficina;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_CAJERO")
    private long codCajero;
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
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")    
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reciboId")
    private List<SspSolicitudCese> sspSolicitudCeseList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reciboId")
    private List<SbReciboRegistro> sbReciboRegistroList;
    @JoinColumn(name = "PROCESO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbRecibosProceso procesoId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppRegistro registroId;
    @ManyToMany(mappedBy = "sbRecibosList")
    private List<EppGteRegistro> eppGteRegistroList;

    public SbRecibos() {
    }

    public SbRecibos(Long id) {
        this.id = id;
    }

    public SbRecibos(Long id, long codTributo, short tipoDocumento, String nroDocumento, long nroRegistros, Double importe, Date fechaMovimiento, Long nroSecuencia, long hora, long codOficina, long codCajero, short codBanco, short activo, String audLogin, String audNumIp) {
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

    public long getCodTributo() {
        return codTributo;
    }

    public void setCodTributo(long codTributo) {
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

    public long getNroRegistros() {
        return nroRegistros;
    }

    public void setNroRegistros(long nroRegistros) {
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

    public long getHora() {
        return hora;
    }

    public void setHora(long hora) {
        this.hora = hora;
    }

    public long getCodOficina() {
        return codOficina;
    }

    public void setCodOficina(long codOficina) {
        this.codOficina = codOficina;
    }

    public long getCodCajero() {
        return codCajero;
    }

    public void setCodCajero(long codCajero) {
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

    public SbRecibosProceso getProcesoId() {
        return procesoId;
    }

    public void setProcesoId(SbRecibosProceso procesoId) {
        this.procesoId = procesoId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }
    
    @XmlTransient
    public List<EppGteRegistro> getEppGteRegistroList() {
        return eppGteRegistroList;
    }

    public void setEppGteRegistroList(List<EppGteRegistro> eppGteRegistroList) {
        this.eppGteRegistroList = eppGteRegistroList;
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
        if (!(object instanceof SbRecibos)) {
            return false;
        }
        SbRecibos other = (SbRecibos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbRecibos[ id=" + id + " ]";
    }

    @XmlTransient
    public List<SspSolicitudCese> getSspSolicitudCeseList() {
        return sspSolicitudCeseList;
    }

    public void setSspSolicitudCeseList(List<SspSolicitudCese> sspSolicitudCeseList) {
        this.sspSolicitudCeseList = sspSolicitudCeseList;
    }
    
    @XmlTransient
    public List<SbReciboRegistro> getSbReciboRegistroList() {
        return sbReciboRegistroList;
    }

    public void setSbReciboRegistroList(List<SbReciboRegistro> sbReciboRegistroList) {
        this.sbReciboRegistroList = sbReciboRegistroList;
    }    
}
