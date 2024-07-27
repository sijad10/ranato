/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
@Table(name = "EPP_DETALLE_AUT_USO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppDetalleAutUso.findAll", query = "SELECT e FROM EppDetalleAutUso e"),
    @NamedQuery(name = "EppDetalleAutUso.findById", query = "SELECT e FROM EppDetalleAutUso e WHERE e.id = :id"),
    @NamedQuery(name = "EppDetalleAutUso.findByCantidad", query = "SELECT e FROM EppDetalleAutUso e WHERE e.cantidad = :cantidad"),
    @NamedQuery(name = "EppDetalleAutUso.findBySaldo", query = "SELECT e FROM EppDetalleAutUso e WHERE e.saldo = :saldo"),
    @NamedQuery(name = "EppDetalleAutUso.findByCantidadUtilizada", query = "SELECT e FROM EppDetalleAutUso e WHERE e.cantidadUtilizada = :cantidadUtilizada"),
    @NamedQuery(name = "EppDetalleAutUso.findByActivo", query = "SELECT e FROM EppDetalleAutUso e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppDetalleAutUso.findByAudLogin", query = "SELECT e FROM EppDetalleAutUso e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppDetalleAutUso.findByAudNumIp", query = "SELECT e FROM EppDetalleAutUso e WHERE e.audNumIp = :audNumIp")})
public class EppDetalleAutUso implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_DETALLE_AUT_USO")
    @SequenceGenerator(name = "SEQ_EPP_DETALLE_AUT_USO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_DETALLE_AUT_USO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private Double cantidad;
    @Column(name = "SALDO")
    private Double saldo;
    @Column(name = "CANTIDAD_UTILIZADA")
    private Double cantidadUtilizada;
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
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppRegistro registroId;
    @JoinColumn(name = "EXPLOSIVO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppExplosivo explosivoId;

    public EppDetalleAutUso() {
    }

    public EppDetalleAutUso(Long id) {
        this.id = id;
    }

    public EppDetalleAutUso(Long id, Double cantidad, short activo, String audLogin, String audNumIp) {
        this.id = id;
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

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getCantidadUtilizada() {
        return cantidadUtilizada;
    }

    public void setCantidadUtilizada(Double cantidadUtilizada) {
        this.cantidadUtilizada = cantidadUtilizada;
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

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }

    public EppExplosivo getExplosivoId() {
        return explosivoId;
    }

    public void setExplosivoId(EppExplosivo explosivoId) {
        this.explosivoId = explosivoId;
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
        if (!(object instanceof EppDetalleAutUso)) {
            return false;
        }
        EppDetalleAutUso other = (EppDetalleAutUso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppDetalleAutUso[ id=" + id + " ]";
    }
    
}
