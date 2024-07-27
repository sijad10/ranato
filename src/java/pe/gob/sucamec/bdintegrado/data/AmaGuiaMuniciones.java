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
import pe.gob.sucamec.bdintegrado.data.UnidadMedida;
import pe.gob.sucamec.sel.gamac.data.GamacAmaMunicion;
/**
 *
 * @author gchavez
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_GUIA_MUNICIONES", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaGuiaMuniciones.findAll", query = "SELECT a FROM AmaGuiaMuniciones a"),
    @NamedQuery(name = "AmaGuiaMuniciones.findById", query = "SELECT a FROM AmaGuiaMuniciones a WHERE a.id = :id"),
    @NamedQuery(name = "AmaGuiaMuniciones.findByCantidad", query = "SELECT a FROM AmaGuiaMuniciones a WHERE a.cantidad = :cantidad"),
    @NamedQuery(name = "AmaGuiaMuniciones.findByPeso", query = "SELECT a FROM AmaGuiaMuniciones a WHERE a.peso = :peso"),
    @NamedQuery(name = "AmaGuiaMuniciones.findByActivo", query = "SELECT a FROM AmaGuiaMuniciones a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaGuiaMuniciones.findByAudLogin", query = "SELECT a FROM AmaGuiaMuniciones a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaGuiaMuniciones.findByAudNumIp", query = "SELECT a FROM AmaGuiaMuniciones a WHERE a.audNumIp = :audNumIp")})
public class AmaGuiaMuniciones implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_GUIA_MUNICIONES")
    @SequenceGenerator(name = "SEQ_AMA_GUIA_MUNICIONES", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_GUIA_MUNICIONES", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private Double cantidad;
    @Column(name = "PESO")
    private Double peso;
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
    @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaGuiaTransito guiaTransitoId;
    @JoinColumn(name = "MUNICION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaMunicion municionId;
    @JoinColumn(name = "UMEDIDA_ID", referencedColumnName = "ID")
    private UnidadMedida umedidaId;

    public AmaGuiaMuniciones() {
    }

    public AmaGuiaMuniciones(Long id) {
        this.id = id;
    }

    public AmaGuiaMuniciones(Long id, Double cantidad, short activo, String audLogin, String audNumIp) {
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

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
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

    public AmaGuiaTransito getGuiaTransitoId() {
        return guiaTransitoId;
    }

    public void setGuiaTransitoId(AmaGuiaTransito guiaTransitoId) {
        this.guiaTransitoId = guiaTransitoId;
    }

    public GamacAmaMunicion getMunicionId() {
        return municionId;
    }

    public void setMunicionId(GamacAmaMunicion municionId) {
        this.municionId = municionId;
    }

    public UnidadMedida getUmedidaId() {
        return umedidaId;
    }

    public void setUmedidaId(UnidadMedida umedidaId) {
        this.umedidaId = umedidaId;
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
        if (!(object instanceof AmaGuiaMuniciones)) {
            return false;
        }
        AmaGuiaMuniciones other = (AmaGuiaMuniciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaGuiaMuniciones[ id=" + id + " ]";
    }

}
