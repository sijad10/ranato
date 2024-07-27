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
@Table(name = "EPP_DETALLE_INT_SAL", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppDetalleIntSal.findAll", query = "SELECT e FROM EppDetalleIntSal e"),
    @NamedQuery(name = "EppDetalleIntSal.findById", query = "SELECT e FROM EppDetalleIntSal e WHERE e.id = :id"),
    @NamedQuery(name = "EppDetalleIntSal.findByCantidadSolicitada", query = "SELECT e FROM EppDetalleIntSal e WHERE e.cantidadSolicitada = :cantidadSolicitada"),
    @NamedQuery(name = "EppDetalleIntSal.findByCantidadAutorizada", query = "SELECT e FROM EppDetalleIntSal e WHERE e.cantidadAutorizada = :cantidadAutorizada"),
    @NamedQuery(name = "EppDetalleIntSal.findByTipoDetalle", query = "SELECT e FROM EppDetalleIntSal e WHERE e.tipoDetalle = :tipoDetalle"),
    @NamedQuery(name = "EppDetalleIntSal.findBySaldo", query = "SELECT e FROM EppDetalleIntSal e WHERE e.saldo = :saldo"),
    @NamedQuery(name = "EppDetalleIntSal.findByActivo", query = "SELECT e FROM EppDetalleIntSal e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppDetalleIntSal.findByAudLogin", query = "SELECT e FROM EppDetalleIntSal e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppDetalleIntSal.findByAudNumIp", query = "SELECT e FROM EppDetalleIntSal e WHERE e.audNumIp = :audNumIp")})
public class EppDetalleIntSal implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_DETALLE_INT_SAL")
    @SequenceGenerator(name = "SEQ_EPP_DETALLE_INT_SAL", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_DETALLE_INT_SAL", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CANTIDAD_SOLICITADA")
    private Double cantidadSolicitada;
    @Column(name = "CANTIDAD_AUTORIZADA")
    private Double cantidadAutorizada;
    @Column(name = "TIPO_DETALLE")
    private Character tipoDetalle;
    @Column(name = "SALDO")
    private Double saldo;
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
    private EppRegistroIntSal registroId;
    @JoinColumn(name = "EXPLOSIVO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppExplosivo explosivoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detInterSalId")
    private List<EppDetalleIntSalDet> eppDetalleIntSalDetList;

    public EppDetalleIntSal() {
    }

    public EppDetalleIntSal(Long id) {
        this.id = id;
    }

    public EppDetalleIntSal(Long id, short activo, String audLogin, String audNumIp) {
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

    public Double getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(Double cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public Double getCantidadAutorizada() {
        return cantidadAutorizada;
    }

    public void setCantidadAutorizada(Double cantidadAutorizada) {
        this.cantidadAutorizada = cantidadAutorizada;
    }

    public Character getTipoDetalle() {
        return tipoDetalle;
    }

    public void setTipoDetalle(Character tipoDetalle) {
        this.tipoDetalle = tipoDetalle;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
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

    public EppRegistroIntSal getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistroIntSal registroId) {
        this.registroId = registroId;
    }

    public EppExplosivo getExplosivoId() {
        return explosivoId;
    }

    public void setExplosivoId(EppExplosivo explosivoId) {
        this.explosivoId = explosivoId;
    }

    @XmlTransient
    public List<EppDetalleIntSalDet> getEppDetalleIntSalDetList() {
        return eppDetalleIntSalDetList;
    }

    public void setEppDetalleIntSalDetList(List<EppDetalleIntSalDet> eppDetalleIntSalDetList) {
        this.eppDetalleIntSalDetList = eppDetalleIntSalDetList;
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
        if (!(object instanceof EppDetalleIntSal)) {
            return false;
        }
        EppDetalleIntSal other = (EppDetalleIntSal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppDetalleIntSal[ id=" + id + " ]";
    }
    
}
