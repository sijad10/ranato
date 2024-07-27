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
import pe.gob.sucamec.sel.gamac.data.GamacAmaMunicion;

/**
 *
 * @author locador192.ogtic
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_DETALLE_RESO_MUNICION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaDetalleResoMunicion.findAll", query = "SELECT a FROM AmaDetalleResoMunicion a"),
    @NamedQuery(name = "AmaDetalleResoMunicion.findById", query = "SELECT a FROM AmaDetalleResoMunicion a WHERE a.id = :id"),
    @NamedQuery(name = "AmaDetalleResoMunicion.findByCantidadSolicitada", query = "SELECT a FROM AmaDetalleResoMunicion a WHERE a.cantidadSolicitada = :cantidadSolicitada"),
    @NamedQuery(name = "AmaDetalleResoMunicion.findByCantidadAutorizada", query = "SELECT a FROM AmaDetalleResoMunicion a WHERE a.cantidadAutorizada = :cantidadAutorizada"),
    @NamedQuery(name = "AmaDetalleResoMunicion.findByActivo", query = "SELECT a FROM AmaDetalleResoMunicion a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaDetalleResoMunicion.findByAudLogin", query = "SELECT a FROM AmaDetalleResoMunicion a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaDetalleResoMunicion.findByAudNumIp", query = "SELECT a FROM AmaDetalleResoMunicion a WHERE a.audNumIp = :audNumIp")})
public class AmaDetalleResoMunicion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_DETALLE_RESO_MUNICION")
    @SequenceGenerator(name = "SEQ_AMA_DETALLE_RESO_MUNICION", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_DETALLE_RESO_MUNICION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CANTIDAD_SOLICITADA")
    private Integer cantidadSolicitada;
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
    @JoinColumn(name = "RESOLUCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaResolucion resolucionId;
    @JoinColumn(name = "MUNICION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaMunicion municionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detalleResoMuniId")
    private List<AmaDetalleResoMuniAdic> amaDetalleResoMuniAdicList;
    @JoinTable(schema = "BDINTEGRADO", name = "AMA_DET_RESO_MUNI_TIPO", joinColumns = {
        @JoinColumn(name = "DETA_RESMUNICION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "MODALIDAD_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<TipoGamac> tipoGamacList;

    public AmaDetalleResoMunicion() {
    }

    public AmaDetalleResoMunicion(Long id) {
        this.id = id;
    }

    public AmaDetalleResoMunicion(Long id, int cantidadAutorizada, short activo, String audLogin, String audNumIp) {
        this.id = id;
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

    public Integer getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(Integer cantidadSolicitada) {
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

    public AmaResolucion getResolucionId() {
        return resolucionId;
    }

    public void setResolucionId(AmaResolucion resolucionId) {
        this.resolucionId = resolucionId;
    }

    public GamacAmaMunicion getMunicionId() {
        return municionId;
    }

    public void setMunicionId(GamacAmaMunicion municionId) {
        this.municionId = municionId;
    }

    @XmlTransient
    public List<AmaDetalleResoMuniAdic> getAmaDetalleResoMuniAdicList() {
        return amaDetalleResoMuniAdicList;
    }

    public void setAmaDetalleResoMuniAdicList(List<AmaDetalleResoMuniAdic> amaDetalleResoMuniAdicList) {
        this.amaDetalleResoMuniAdicList = amaDetalleResoMuniAdicList;
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
        if (!(object instanceof AmaDetalleResoMunicion)) {
            return false;
        }
        AmaDetalleResoMunicion other = (AmaDetalleResoMunicion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaDetalleResoMunicion[ id=" + id + " ]";
    }

    @XmlTransient
    public List<TipoGamac> getTipoGamacList() {
        return tipoGamacList;
    }

    public void setTipoGamacList(List<TipoGamac> tipoGamacList) {
        this.tipoGamacList = tipoGamacList;
    }

}
