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
@Table(name = "EPP_EXPLOSIVO_SOLICITADO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppExplosivoSolicitado.findAll", query = "SELECT e FROM EppExplosivoSolicitado e"),
    @NamedQuery(name = "EppExplosivoSolicitado.findById", query = "SELECT e FROM EppExplosivoSolicitado e WHERE e.id = :id"),
    @NamedQuery(name = "EppExplosivoSolicitado.findByCantidadAutorizada", query = "SELECT e FROM EppExplosivoSolicitado e WHERE e.cantidadAutorizada = :cantidadAutorizada"),
    @NamedQuery(name = "EppExplosivoSolicitado.findByCantidad", query = "SELECT e FROM EppExplosivoSolicitado e WHERE e.cantidad = :cantidad"),
    @NamedQuery(name = "EppExplosivoSolicitado.findByCantidadExtornada", query = "SELECT e FROM EppExplosivoSolicitado e WHERE e.cantidadExtornada = :cantidadExtornada"),
    @NamedQuery(name = "EppExplosivoSolicitado.findByActivo", query = "SELECT e FROM EppExplosivoSolicitado e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppExplosivoSolicitado.findByAudLogin", query = "SELECT e FROM EppExplosivoSolicitado e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppExplosivoSolicitado.findByAudNumIp", query = "SELECT e FROM EppExplosivoSolicitado e WHERE e.audNumIp = :audNumIp")})
public class EppExplosivoSolicitado implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_EXPLOSIVO_SOLICITADO")
    @SequenceGenerator(name = "SEQ_EPP_EXPLOSIVO_SOLICITADO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_EXPLOSIVO_SOLICITADO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CANTIDAD_AUTORIZADA")
    private Double cantidadAutorizada;
    @Column(name = "CANTIDAD")
    private Double cantidad;
    @Column(name = "CANTIDAD_EXTORNADA")
    private Double cantidadExtornada;
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
    private EppRegistroGuiaTransito registroId;
    @JoinColumn(name = "EXPLOSIVO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppExplosivo explosivoId;

    public EppExplosivoSolicitado() {
    }

    public EppExplosivoSolicitado(Long id) {
        this.id = id;
    }

    public EppExplosivoSolicitado(Long id, short activo, String audLogin, String audNumIp) {
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

    public Double getCantidadAutorizada() {
        return cantidadAutorizada;
    }

    public void setCantidadAutorizada(Double cantidadAutorizada) {
        this.cantidadAutorizada = cantidadAutorizada;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getCantidadExtornada() {
        return cantidadExtornada;
    }

    public void setCantidadExtornada(Double cantidadExtornada) {
        this.cantidadExtornada = cantidadExtornada;
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

    public EppRegistroGuiaTransito getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistroGuiaTransito registroId) {
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
        if (!(object instanceof EppExplosivoSolicitado)) {
            return false;
        }
        EppExplosivoSolicitado other = (EppExplosivoSolicitado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppExplosivoSolicitado[ id=" + id + " ]";
    }
    
}
