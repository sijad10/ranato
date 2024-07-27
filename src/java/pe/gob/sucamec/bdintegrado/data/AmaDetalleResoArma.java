/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author locador192.ogtic
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_DETALLE_RESO_ARMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaDetalleResoArma.findAll", query = "SELECT a FROM AmaDetalleResoArma a"),
    @NamedQuery(name = "AmaDetalleResoArma.findById", query = "SELECT a FROM AmaDetalleResoArma a WHERE a.id = :id"),
    @NamedQuery(name = "AmaDetalleResoArma.findByCantidadSolicitada", query = "SELECT a FROM AmaDetalleResoArma a WHERE a.cantidadSolicitada = :cantidadSolicitada"),
    @NamedQuery(name = "AmaDetalleResoArma.findByCantidadAutorizada", query = "SELECT a FROM AmaDetalleResoArma a WHERE a.cantidadAutorizada = :cantidadAutorizada"),
    @NamedQuery(name = "AmaDetalleResoArma.findByActivo", query = "SELECT a FROM AmaDetalleResoArma a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaDetalleResoArma.findByAudLogin", query = "SELECT a FROM AmaDetalleResoArma a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaDetalleResoArma.findByAudNumIp", query = "SELECT a FROM AmaDetalleResoArma a WHERE a.audNumIp = :audNumIp")})
public class AmaDetalleResoArma implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_DETALLE_RESO_ARMA")
    @SequenceGenerator(name = "SEQ_AMA_DETALLE_RESO_ARMA", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_DETALLE_RESO_ARMA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD_SOLICITADA")
    private int cantidadSolicitada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD_AUTORIZADA")
    private int cantidadAutorizada;
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
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinTable(name = "AMA_RESARMA_USO", schema = "BDINTEGRADO", joinColumns = {
        @JoinColumn(name = "RESOLARMA_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "USO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<TipoGamac> tipoGamacList;
    @JoinTable(name = "AMA_RESERVA_RUA", schema = "BDINTEGRADO", joinColumns = {
        @JoinColumn(name = "DET_RESO_ARMA_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "RUA_GENERADO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<AmaRuaGenerado> amaRuaGeneradoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detalleResoArmaId")
    private List<AmaDetalleResoArmaAdic> amaDetalleResoArmaAdicList;
    @JoinColumn(name = "RESOLUCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaResolucion resolucionId;
    @JoinColumn(name = "MODELO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaModelos modeloId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detResoArmaId")
    private List<AmaDetalleResoMatrel> amaDetalleResoMatrelList;

    public AmaDetalleResoArma() {
    }

    public AmaDetalleResoArma(Long id) {
        this.id = id;
    }

    public AmaDetalleResoArma(Long id, int cantidadSolicitada, int cantidadAutorizada, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.cantidadSolicitada = cantidadSolicitada;
        this.cantidadAutorizada = cantidadAutorizada;
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

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(int cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public int getCantidadAutorizada() {
        return cantidadAutorizada;
    }

    public void setCantidadAutorizada(int cantidadAutorizada) {
        this.cantidadAutorizada = cantidadAutorizada;
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
    public List<AmaDetalleResoArmaAdic> getAmaDetalleResoArmaAdicList() {
        return amaDetalleResoArmaAdicList;
    }

    public void setAmaDetalleResoArmaAdicList(List<AmaDetalleResoArmaAdic> amaDetalleResoArmaAdicList) {
        this.amaDetalleResoArmaAdicList = amaDetalleResoArmaAdicList;
    }

    @XmlTransient
    public List<TipoGamac> getTipoGamacList() {
        return tipoGamacList;
    }

    public void setTipoGamacList(List<TipoGamac> tipoGamacList) {
        this.tipoGamacList = tipoGamacList;
    }

    public AmaResolucion getResolucionId() {
        return resolucionId;
    }

    public void setResolucionId(AmaResolucion resolucionId) {
        this.resolucionId = resolucionId;
    }

    public AmaModelos getModeloId() {
        return modeloId;
    }

    public void setModeloId(AmaModelos modeloId) {
        this.modeloId = modeloId;
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
        if (!(object instanceof AmaDetalleResoArma)) {
            return false;
        }
        AmaDetalleResoArma other = (AmaDetalleResoArma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaDetalleResoArma[ id=" + id + " ]";
    }

    @XmlTransient
    public List<AmaDetalleResoMatrel> getAmaDetalleResoMatrelList() {
        return amaDetalleResoMatrelList;
    }

    public void setAmaDetalleResoMatrelList(List<AmaDetalleResoMatrel> amaDetalleResoMatrelList) {
        this.amaDetalleResoMatrelList = amaDetalleResoMatrelList;
    }

    public List<AmaRuaGenerado> getAmaRuaGeneradoList() {
        return amaRuaGeneradoList;
    }

    public void setAmaRuaGeneradoList(List<AmaRuaGenerado> amaRuaGeneradoList) {
        this.amaRuaGeneradoList = amaRuaGeneradoList;
    }

}
