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
 * @author rchipana
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_DETALLE_RESO_MATREL", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaDetalleResoMatrel.findAll", query = "SELECT a FROM AmaDetalleResoMatrel a"),
    @NamedQuery(name = "AmaDetalleResoMatrel.findById", query = "SELECT a FROM AmaDetalleResoMatrel a WHERE a.id = :id"),
    @NamedQuery(name = "AmaDetalleResoMatrel.findByCantidadSolicitada", query = "SELECT a FROM AmaDetalleResoMatrel a WHERE a.cantidadSolicitada = :cantidadSolicitada"),
    @NamedQuery(name = "AmaDetalleResoMatrel.findByCantidadAutorizada", query = "SELECT a FROM AmaDetalleResoMatrel a WHERE a.cantidadAutorizada = :cantidadAutorizada"),
    @NamedQuery(name = "AmaDetalleResoMatrel.findByActivo", query = "SELECT a FROM AmaDetalleResoMatrel a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaDetalleResoMatrel.findByAudLogin", query = "SELECT a FROM AmaDetalleResoMatrel a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaDetalleResoMatrel.findByAudNumIp", query = "SELECT a FROM AmaDetalleResoMatrel a WHERE a.audNumIp = :audNumIp")})
public class AmaDetalleResoMatrel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_DETALLE_RESO_MATREL")
    @SequenceGenerator(name = "SEQ_AMA_DETALLE_RESO_MATREL", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_DETALLE_RESO_MATREL", allocationSize = 1)
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
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "resolmatrelId")
//    private List<AmaDetintsalmatrelImpexp> amaDetintsalmatrelImpexpList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detalleResoMatrelId")
    private List<AmaDetalleResoMrAdic> amaDetalleResoMrAdicList;
    @JoinColumn(name = "RESOLUCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaResolucion resolucionId;

    @JoinColumn(name = "MATERIAL_REL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaInventarioArtconexo materialRelId;
      @JoinColumn(name = "MODELO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaModelos modeloArmaId;
    @JoinColumn(name = "DET_RESO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaDetalleResoArma detResoArmaId;

    public AmaDetalleResoMatrel() {
    }

    public AmaDetalleResoMatrel(Long id) {
        this.id = id;
    }

    public AmaDetalleResoMatrel(Long id, int cantidadSolicitada, int cantidadAutorizada, short activo, String audLogin, String audNumIp) {
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

//    @XmlTransient
//    public List<AmaDetintsalmatrelImpexp> getAmaDetintsalmatrelImpexpList() {
//        return amaDetintsalmatrelImpexpList;
//    }
//
//    public void setAmaDetintsalmatrelImpexpList(List<AmaDetintsalmatrelImpexp> amaDetintsalmatrelImpexpList) {
//        this.amaDetintsalmatrelImpexpList = amaDetintsalmatrelImpexpList;
//    }
    @XmlTransient
    public List<AmaDetalleResoMrAdic> getAmaDetalleResoMrAdicList() {
        return amaDetalleResoMrAdicList;
    }

    public void setAmaDetalleResoMrAdicList(List<AmaDetalleResoMrAdic> amaDetalleResoMrAdicList) {
        this.amaDetalleResoMrAdicList = amaDetalleResoMrAdicList;
    }

    public AmaResolucion getResolucionId() {
        return resolucionId;
    }

    public void setResolucionId(AmaResolucion resolucionId) {
        this.resolucionId = resolucionId;
    }

    public AmaInventarioArtconexo getMaterialRelId() {
        return materialRelId;
    }

    public void setMaterialRelId(AmaInventarioArtconexo materialRelId) {
        this.materialRelId = materialRelId;
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
        if (!(object instanceof AmaDetalleResoMatrel)) {
            return false;
        }
        AmaDetalleResoMatrel other = (AmaDetalleResoMatrel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaDetalleResoMatrel[ id=" + id + " ]";
    }

    public AmaModelos getModeloArmaId() {
        return modeloArmaId;
    }

    public void setModeloArmaId(AmaModelos modeloArmaId) {
        this.modeloArmaId = modeloArmaId;
    }

    public AmaDetalleResoArma getDetResoArmaId() {
        return detResoArmaId;
    }

    public void setDetResoArmaId(AmaDetalleResoArma detResoArmaId) {
        this.detResoArmaId = detResoArmaId;
    }

}
