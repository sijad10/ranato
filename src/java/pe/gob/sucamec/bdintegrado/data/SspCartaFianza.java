/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author locador772.ogtic
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_CARTA_FIANZA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspCartaFianza.findAll", query = "SELECT s FROM SspCartaFianza s"),
    @NamedQuery(name = "SspCartaFianza.findById", query = "SELECT s FROM SspCartaFianza s WHERE s.id = :id"),
    @NamedQuery(name = "SspCartaFianza.findByNumero", query = "SELECT s FROM SspCartaFianza s WHERE s.numero = :numero"),
    @NamedQuery(name = "SspCartaFianza.findByMoneda", query = "SELECT s FROM SspCartaFianza s WHERE s.moneda = :moneda"),
    @NamedQuery(name = "SspCartaFianza.findByMonto", query = "SELECT s FROM SspCartaFianza s WHERE s.monto = :monto"),
    @NamedQuery(name = "SspCartaFianza.findByVigenciaInicio", query = "SELECT s FROM SspCartaFianza s WHERE s.vigenciaInicio = :vigenciaInicio"),
    @NamedQuery(name = "SspCartaFianza.findByVigenciaFin", query = "SELECT s FROM SspCartaFianza s WHERE s.vigenciaFin = :vigenciaFin"),
    @NamedQuery(name = "SspCartaFianza.findByNombreArchivo", query = "SELECT s FROM SspCartaFianza s WHERE s.nombreArchivo = :nombreArchivo"),
    @NamedQuery(name = "SspCartaFianza.findByFecha", query = "SELECT s FROM SspCartaFianza s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspCartaFianza.findByActivo", query = "SELECT s FROM SspCartaFianza s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspCartaFianza.findByAudLogin", query = "SELECT s FROM SspCartaFianza s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspCartaFianza.findByAudNumIp", query = "SELECT s FROM SspCartaFianza s WHERE s.audNumIp = :audNumIp")})
public class SspCartaFianza implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_CARTA_FIANZA")
    @SequenceGenerator(name = "SEQ_SSP_CARTA_FIANZA", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_CARTA_FIANZA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @Size(max = 26)
    @Column(name = "NUMERO")
    private String numero;
    
    @Column(name = "MONEDA")
    private Long moneda;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MONTO")
    private BigDecimal monto;
    
    @Column(name = "VIGENCIA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vigenciaInicio;
    
    @Column(name = "VIGENCIA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vigenciaFin;
    
    @Size(max = 20)
    @Column(name = "NOMBRE_ARCHIVO")
    private String nombreArchivo;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
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
    
    @JoinColumn(name = "TIPO_FINANCIERA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad tipoFinancieraId;
    
    @JoinColumn(name = "FINANCIERA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad financieraId;

    public SspCartaFianza() {
    }

    public SspCartaFianza(Long id) {
        this.id = id;
    }

    public SspCartaFianza(Long id, Date fecha, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fecha = fecha;
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

    public Long getMoneda() {
        return moneda;
    }

    public void setMoneda(Long moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Date getVigenciaInicio() {
        return vigenciaInicio;
    }

    public void setVigenciaInicio(Date vigenciaInicio) {
        this.vigenciaInicio = vigenciaInicio;
    }

    public Date getVigenciaFin() {
        return vigenciaFin;
    }

    public void setVigenciaFin(Date vigenciaFin) {
        this.vigenciaFin = vigenciaFin;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public TipoSeguridad getTipoFinancieraId() {
        return tipoFinancieraId;
    }

    public void setTipoFinancieraId(TipoSeguridad tipoFinancieraId) {
        this.tipoFinancieraId = tipoFinancieraId;
    }

    public TipoSeguridad getFinancieraId() {
        return financieraId;
    }

    public void setFinancieraId(TipoSeguridad financieraId) {
        this.financieraId = financieraId;
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
        if (!(object instanceof SspCartaFianza)) {
            return false;
        }
        SspCartaFianza other = (SspCartaFianza) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspCartaFianza[ id=" + id + " ]";
    }

}
