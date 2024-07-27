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
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_COMPROBANTE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppComprobante.findAll", query = "SELECT e FROM EppComprobante e"),
    @NamedQuery(name = "EppComprobante.findById", query = "SELECT e FROM EppComprobante e WHERE e.id = :id"),
    @NamedQuery(name = "EppComprobante.findByNroComp", query = "SELECT e FROM EppComprobante e WHERE e.nroComp = :nroComp"),
    @NamedQuery(name = "EppComprobante.findByFecComp", query = "SELECT e FROM EppComprobante e WHERE e.fecComp = :fecComp"),
    @NamedQuery(name = "EppComprobante.findByNroExpediente", query = "SELECT e FROM EppComprobante e WHERE e.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "EppComprobante.findByUtilizado", query = "SELECT e FROM EppComprobante e WHERE e.utilizado = :utilizado"),
    @NamedQuery(name = "EppComprobante.findByCantidad", query = "SELECT e FROM EppComprobante e WHERE e.cantidad = :cantidad"),
    @NamedQuery(name = "EppComprobante.findByActivo", query = "SELECT e FROM EppComprobante e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppComprobante.findByAudLogin", query = "SELECT e FROM EppComprobante e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppComprobante.findByAudNumIp", query = "SELECT e FROM EppComprobante e WHERE e.audNumIp = :audNumIp")})
public class EppComprobante implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_COMPROBANTE")
    @SequenceGenerator(name = "SEQ_EPP_COMPROBANTE", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_COMPROBANTE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "NRO_COMP")
    private String nroComp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FEC_COMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecComp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UTILIZADO")
    private short utilizado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private Double cantidad;
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
    @JoinColumn(name = "TIPO_COD_TRIBUTO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoCodTributoId;
    @JoinColumn(name = "TIPO_REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoRegistroId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppRegistro registroId;

    public EppComprobante() {
    }

    public EppComprobante(Long id) {
        this.id = id;
    }

    public EppComprobante(Long id, String nroComp, Date fecComp, String nroExpediente, short utilizado, Double cantidad, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroComp = nroComp;
        this.fecComp = fecComp;
        this.nroExpediente = nroExpediente;
        this.utilizado = utilizado;
        this.cantidad = cantidad;
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

    public String getNroComp() {
        return nroComp;
    }

    public void setNroComp(String nroComp) {
        this.nroComp = nroComp;
    }

    public Date getFecComp() {
        return fecComp;
    }

    public void setFecComp(Date fecComp) {
        this.fecComp = fecComp;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public short getUtilizado() {
        return utilizado;
    }

    public void setUtilizado(short utilizado) {
        this.utilizado = utilizado;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
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

    public TipoExplosivoGt getTipoCodTributoId() {
        return tipoCodTributoId;
    }

    public void setTipoCodTributoId(TipoExplosivoGt tipoCodTributoId) {
        this.tipoCodTributoId = tipoCodTributoId;
    }

    public TipoExplosivoGt getTipoRegistroId() {
        return tipoRegistroId;
    }

    public void setTipoRegistroId(TipoExplosivoGt tipoRegistroId) {
        this.tipoRegistroId = tipoRegistroId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
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
        if (!(object instanceof EppComprobante)) {
            return false;
        }
        EppComprobante other = (EppComprobante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppComprobante[ id=" + id + " ]";
    }
    
}
