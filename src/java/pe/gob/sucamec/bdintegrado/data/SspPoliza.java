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
 * @author mpalomino
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_POLIZA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspPoliza.findAll", query = "SELECT s FROM SspPoliza s"),
    @NamedQuery(name = "SspPoliza.findById", query = "SELECT s FROM SspPoliza s WHERE s.id = :id"),
    @NamedQuery(name = "SspPoliza.findByRuc", query = "SELECT s FROM SspPoliza s WHERE s.ruc = :ruc"),
    @NamedQuery(name = "SspPoliza.findByEmpresaAseguradora", query = "SELECT s FROM SspPoliza s WHERE s.empresaAseguradora = :empresaAseguradora"),
    @NamedQuery(name = "SspPoliza.findByVigenciaIni", query = "SELECT s FROM SspPoliza s WHERE s.vigenciaIni = :vigenciaIni"),
    @NamedQuery(name = "SspPoliza.findByVigenciaFin", query = "SELECT s FROM SspPoliza s WHERE s.vigenciaFin = :vigenciaFin"),
    @NamedQuery(name = "SspPoliza.findByNroPoliza", query = "SELECT s FROM SspPoliza s WHERE s.nroPoliza = :nroPoliza"),
    @NamedQuery(name = "SspPoliza.findByMontoMaximo", query = "SELECT s FROM SspPoliza s WHERE s.montoMaximo = :montoMaximo"),
    @NamedQuery(name = "SspPoliza.findByArchivoPoliza", query = "SELECT s FROM SspPoliza s WHERE s.archivoPoliza = :archivoPoliza"),
    @NamedQuery(name = "SspPoliza.findByBienes", query = "SELECT s FROM SspPoliza s WHERE s.bienes = :bienes"),
    @NamedQuery(name = "SspPoliza.findByActivo", query = "SELECT s FROM SspPoliza s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspPoliza.findByAudLogin", query = "SELECT s FROM SspPoliza s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspPoliza.findByAudNumIp", query = "SELECT s FROM SspPoliza s WHERE s.audNumIp = :audNumIp")})
public class SspPoliza implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_POLIZA")
    @SequenceGenerator(name = "SEQ_SSP_POLIZA", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_POLIZA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;
    @Size(max = 11)
    @Column(name = "RUC")
    private String ruc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "EMPRESA_ASEGURADORA")
    private String empresaAseguradora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VIGENCIA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vigenciaIni;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VIGENCIA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vigenciaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NRO_POLIZA")
    private String nroPoliza;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "MONTO_MAXIMO")
    private BigDecimal montoMaximo;
    @Size(max = 200)
    @Column(name = "ARCHIVO_POLIZA")
    private String archivoPoliza;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "BIENES")
    private String bienes;
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
    @JoinColumn(name = "TIPO_MONEDA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoMoneda;

    public SspPoliza() {
    }

    public SspPoliza(Long id) {
        this.id = id;
    }

    public SspPoliza(Long id, String empresaAseguradora, Date vigenciaIni, Date vigenciaFin, String nroPoliza, BigDecimal montoMaximo, String bienes, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.empresaAseguradora = empresaAseguradora;
        this.vigenciaIni = vigenciaIni;
        this.vigenciaFin = vigenciaFin;
        this.nroPoliza = nroPoliza;
        this.montoMaximo = montoMaximo;
        this.bienes = bienes;
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

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getEmpresaAseguradora() {
        return empresaAseguradora;
    }

    public void setEmpresaAseguradora(String empresaAseguradora) {
        this.empresaAseguradora = empresaAseguradora;
    }

    public Date getVigenciaIni() {
        return vigenciaIni;
    }

    public void setVigenciaIni(Date vigenciaIni) {
        this.vigenciaIni = vigenciaIni;
    }

    public Date getVigenciaFin() {
        return vigenciaFin;
    }

    public void setVigenciaFin(Date vigenciaFin) {
        this.vigenciaFin = vigenciaFin;
    }

    public String getNroPoliza() {
        return nroPoliza;
    }

    public void setNroPoliza(String nroPoliza) {
        this.nroPoliza = nroPoliza;
    }

    public BigDecimal getMontoMaximo() {
        return montoMaximo;
    }

    public void setMontoMaximo(BigDecimal montoMaximo) {
        this.montoMaximo = montoMaximo;
    }

    public String getArchivoPoliza() {
        return archivoPoliza;
    }

    public void setArchivoPoliza(String archivoPoliza) {
        this.archivoPoliza = archivoPoliza;
    }

    public String getBienes() {
        return bienes;
    }

    public void setBienes(String bienes) {
        this.bienes = bienes;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SspPoliza)) {
            return false;
        }
        SspPoliza other = (SspPoliza) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspPoliza[ id=" + id + " ]";
    }

    public TipoBaseGt getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(TipoBaseGt tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }
    
}
