/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author msalinas
 */
@Entity
@Table(name = "SB_RECIBOS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaSbRecibos.findAll", query = "SELECT s FROM CitaSbRecibos s"),
    @NamedQuery(name = "CitaSbRecibos.findById", query = "SELECT s FROM CitaSbRecibos s WHERE s.id = :id"),
    @NamedQuery(name = "CitaSbRecibos.findByCodTributo", query = "SELECT s FROM CitaSbRecibos s WHERE s.codTributo = :codTributo"),
    @NamedQuery(name = "CitaSbRecibos.findByTipoDocumento", query = "SELECT s FROM CitaSbRecibos s WHERE s.tipoDocumento = :tipoDocumento"),
    @NamedQuery(name = "CitaSbRecibos.findByNroDocumento", query = "SELECT s FROM CitaSbRecibos s WHERE s.nroDocumento = :nroDocumento"),
    @NamedQuery(name = "CitaSbRecibos.findByNroRegistros", query = "SELECT s FROM CitaSbRecibos s WHERE s.nroRegistros = :nroRegistros"),
    @NamedQuery(name = "CitaSbRecibos.findByImporte", query = "SELECT s FROM CitaSbRecibos s WHERE s.importe = :importe"),
    @NamedQuery(name = "CitaSbRecibos.findByFechaMovimiento", query = "SELECT s FROM CitaSbRecibos s WHERE s.fechaMovimiento = :fechaMovimiento"),
    @NamedQuery(name = "CitaSbRecibos.findByNroSecuencia", query = "SELECT s FROM CitaSbRecibos s WHERE s.nroSecuencia = :nroSecuencia"),
    @NamedQuery(name = "CitaSbRecibos.findByHora", query = "SELECT s FROM CitaSbRecibos s WHERE s.hora = :hora"),
    @NamedQuery(name = "CitaSbRecibos.findByCodOficina", query = "SELECT s FROM CitaSbRecibos s WHERE s.codOficina = :codOficina"),
    @NamedQuery(name = "CitaSbRecibos.findByCodCajero", query = "SELECT s FROM CitaSbRecibos s WHERE s.codCajero = :codCajero"),
    @NamedQuery(name = "CitaSbRecibos.findByNroExpediente", query = "SELECT s FROM CitaSbRecibos s WHERE s.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "CitaSbRecibos.findByDevolucion", query = "SELECT s FROM CitaSbRecibos s WHERE s.devolucion = :devolucion"),
    @NamedQuery(name = "CitaSbRecibos.findByCodBanco", query = "SELECT s FROM CitaSbRecibos s WHERE s.codBanco = :codBanco"),
    @NamedQuery(name = "CitaSbRecibos.findByProcesoId", query = "SELECT s FROM CitaSbRecibos s WHERE s.procesoId = :procesoId"),
    @NamedQuery(name = "CitaSbRecibos.findByRegistroId", query = "SELECT s FROM CitaSbRecibos s WHERE s.registroId = :registroId"),
    @NamedQuery(name = "CitaSbRecibos.findByActivo", query = "SELECT s FROM CitaSbRecibos s WHERE s.activo = :activo"),
    @NamedQuery(name = "CitaSbRecibos.findByAudLogin", query = "SELECT s FROM CitaSbRecibos s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "CitaSbRecibos.findByAudNumIp", query = "SELECT s FROM CitaSbRecibos s WHERE s.audNumIp = :audNumIp")})
public class CitaSbRecibos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
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
    @Size(min = 1, max = 20)
    @Column(name = "NRO_DOCUMENTO")
    private String nroDocumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_REGISTROS")
    private long nroRegistros;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "IMPORTE")
    private BigDecimal importe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_MOVIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMovimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_SECUENCIA")
    private long nroSecuencia;
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
    @Column(name = "PROCESO_ID")
    private long procesoId;
    @Column(name = "REGISTRO_ID")
    private Long registroId;
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
    private List<CitaSbReciboRegistro> sbReciboRegistroList;

    public CitaSbRecibos() {
    }

    public CitaSbRecibos(Long id) {
        this.id = id;
    }

    public CitaSbRecibos(Long id, long codTributo, short tipoDocumento, String nroDocumento, long nroRegistros, BigDecimal importe, Date fechaMovimiento, long nroSecuencia, long hora, long codOficina, long codCajero, short codBanco, long procesoId, short activo, String audLogin, String audNumIp) {
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
        this.procesoId = procesoId;
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

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public long getNroSecuencia() {
        return nroSecuencia;
    }

    public void setNroSecuencia(long nroSecuencia) {
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

    public long getProcesoId() {
        return procesoId;
    }

    public void setProcesoId(long procesoId) {
        this.procesoId = procesoId;
    }

    public Long getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Long registroId) {
        this.registroId = registroId;
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
    public List<CitaSbReciboRegistro> getSbReciboRegistroList() {
        return sbReciboRegistroList;
    }

    public void setSbReciboRegistroList(List<CitaSbReciboRegistro> sbReciboRegistroList) {
        this.sbReciboRegistroList = sbReciboRegistroList;
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
        if (!(object instanceof CitaSbRecibos)) {
            return false;
        }
        CitaSbRecibos other = (CitaSbRecibos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.CitaSbRecibos[ id=" + id + " ]";
    }
    
}
