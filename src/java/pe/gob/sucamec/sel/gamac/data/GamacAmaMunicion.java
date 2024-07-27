/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;
import java.util.Collection;
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
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_MUNICION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacAmaMunicion.findAll", query = "SELECT g FROM GamacAmaMunicion g"),
    @NamedQuery(name = "GamacAmaMunicion.findById", query = "SELECT g FROM GamacAmaMunicion g WHERE g.id = :id"),
    @NamedQuery(name = "GamacAmaMunicion.findByDiametroProyectil", query = "SELECT g FROM GamacAmaMunicion g WHERE g.diametroProyectil = :diametroProyectil"),
    @NamedQuery(name = "GamacAmaMunicion.findByLargoMunicion", query = "SELECT g FROM GamacAmaMunicion g WHERE g.largoMunicion = :largoMunicion"),
    @NamedQuery(name = "GamacAmaMunicion.findByPesoMunicionGramos", query = "SELECT g FROM GamacAmaMunicion g WHERE g.pesoMunicionGramos = :pesoMunicionGramos"),
    @NamedQuery(name = "GamacAmaMunicion.findByPesoMunicionGranos", query = "SELECT g FROM GamacAmaMunicion g WHERE g.pesoMunicionGranos = :pesoMunicionGranos"),
    @NamedQuery(name = "GamacAmaMunicion.findByPolvoraGranos", query = "SELECT g FROM GamacAmaMunicion g WHERE g.polvoraGranos = :polvoraGranos"),
    @NamedQuery(name = "GamacAmaMunicion.findByVelocidadSalida", query = "SELECT g FROM GamacAmaMunicion g WHERE g.velocidadSalida = :velocidadSalida"),
    @NamedQuery(name = "GamacAmaMunicion.findByObservacion", query = "SELECT g FROM GamacAmaMunicion g WHERE g.observacion = :observacion"),
    @NamedQuery(name = "GamacAmaMunicion.findByActivo", query = "SELECT g FROM GamacAmaMunicion g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacAmaMunicion.findByAudLogin", query = "SELECT g FROM GamacAmaMunicion g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacAmaMunicion.findByAudNumIp", query = "SELECT g FROM GamacAmaMunicion g WHERE g.audNumIp = :audNumIp")})
public class GamacAmaMunicion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_AMA_MUNICION")
    @SequenceGenerator(name = "SEQ_GAMAC_AMA_MUNICION", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_MUNICION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "DIAMETRO_PROYECTIL")
    private Double diametroProyectil;
    @Column(name = "MODELO")
    private String modelo;
    @Column(name = "LARGO_MUNICION")
    private Double largoMunicion;
    @Column(name = "PESO_MUNICION_GRAMOS")
    private Double pesoMunicionGramos;
    @Column(name = "PESO_MUNICION_GRANOS")
    private Double pesoMunicionGranos;
    @Column(name = "POLVORA_GRANOS")
    private Double polvoraGranos;
    @Column(name = "VELOCIDAD_SALIDA")
    private Double velocidadSalida;
    @Size(max = 500)
    @Column(name = "OBSERVACION")
    private String observacion;
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
    @JoinColumn(name = "NRO_PERDIGON_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoGamac nroPerdigonId;
    @JoinColumn(name = "VELOCSALIDA_UM_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacUnidadMedida velocsalidaUmId;
    @JoinColumn(name = "DIAMLARGO_UM_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacUnidadMedida diamlargoUmId;
    @JoinColumn(name = "DENOMINACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoGamac denominacionId;
    @JoinColumn(name = "TIPO_PROYECTIL_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoGamac tipoProyectilId;
    @JoinColumn(name = "TIPO_IGNICION_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoGamac tipoIgnicionId;
    @JoinColumn(name = "TIPO_MUNICION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoGamac tipoMunicionId;
    @JoinColumn(name = "PAIS_FABRICANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbPais paisFabricanteId;
    @JoinColumn(name = "CALIBREARMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaCatalogo calibrearmaId;
    @JoinColumn(name = "MARCA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaCatalogo marcaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articuloMunicionId")
    private Collection<GamacAmaAdmunDetalleTrans> gamacAmaAdmunDetalleTransCollection;

    public GamacAmaMunicion() {
    }

    public GamacAmaMunicion(Long id) {
        this.id = id;
    }

    public GamacAmaMunicion(Long id, short activo, String audLogin, String audNumIp) {
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

    public Double getDiametroProyectil() {
        return diametroProyectil;
    }

    public void setDiametroProyectil(Double diametroProyectil) {
        this.diametroProyectil = diametroProyectil;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Double getLargoMunicion() {
        return largoMunicion;
    }

    public void setLargoMunicion(Double largoMunicion) {
        this.largoMunicion = largoMunicion;
    }

    public Double getPesoMunicionGramos() {
        return pesoMunicionGramos;
    }

    public void setPesoMunicionGramos(Double pesoMunicionGramos) {
        this.pesoMunicionGramos = pesoMunicionGramos;
    }

    public Double getPesoMunicionGranos() {
        return pesoMunicionGranos;
    }

    public void setPesoMunicionGranos(Double pesoMunicionGranos) {
        this.pesoMunicionGranos = pesoMunicionGranos;
    }

    public Double getPolvoraGranos() {
        return polvoraGranos;
    }

    public void setPolvoraGranos(Double polvoraGranos) {
        this.polvoraGranos = polvoraGranos;
    }

    public Double getVelocidadSalida() {
        return velocidadSalida;
    }

    public void setVelocidadSalida(Double velocidadSalida) {
        this.velocidadSalida = velocidadSalida;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public GamacTipoGamac getNroPerdigonId() {
        return nroPerdigonId;
    }

    public void setNroPerdigonId(GamacTipoGamac nroPerdigonId) {
        this.nroPerdigonId = nroPerdigonId;
    }

    public GamacUnidadMedida getVelocsalidaUmId() {
        return velocsalidaUmId;
    }

    public void setVelocsalidaUmId(GamacUnidadMedida velocsalidaUmId) {
        this.velocsalidaUmId = velocsalidaUmId;
    }

    public GamacUnidadMedida getDiamlargoUmId() {
        return diamlargoUmId;
    }

    public void setDiamlargoUmId(GamacUnidadMedida diamlargoUmId) {
        this.diamlargoUmId = diamlargoUmId;
    }

    public GamacTipoGamac getDenominacionId() {
        return denominacionId;
    }

    public void setDenominacionId(GamacTipoGamac denominacionId) {
        this.denominacionId = denominacionId;
    }

    public GamacTipoGamac getTipoProyectilId() {
        return tipoProyectilId;
    }

    public void setTipoProyectilId(GamacTipoGamac tipoProyectilId) {
        this.tipoProyectilId = tipoProyectilId;
    }

    public GamacTipoGamac getTipoIgnicionId() {
        return tipoIgnicionId;
    }

    public void setTipoIgnicionId(GamacTipoGamac tipoIgnicionId) {
        this.tipoIgnicionId = tipoIgnicionId;
    }

    public GamacTipoGamac getTipoMunicionId() {
        return tipoMunicionId;
    }

    public void setTipoMunicionId(GamacTipoGamac tipoMunicionId) {
        this.tipoMunicionId = tipoMunicionId;
    }

    public GamacSbPais getPaisFabricanteId() {
        return paisFabricanteId;
    }

    public void setPaisFabricanteId(GamacSbPais paisFabricanteId) {
        this.paisFabricanteId = paisFabricanteId;
    }

    public GamacAmaCatalogo getCalibrearmaId() {
        return calibrearmaId;
    }

    public void setCalibrearmaId(GamacAmaCatalogo calibrearmaId) {
        this.calibrearmaId = calibrearmaId;
    }

    public GamacAmaCatalogo getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(GamacAmaCatalogo marcaId) {
        this.marcaId = marcaId;
    }

    @XmlTransient
    public Collection<GamacAmaAdmunDetalleTrans> getGamacAmaAdmunDetalleTransCollection() {
        return gamacAmaAdmunDetalleTransCollection;
    }

    public void setGamacAmaAdmunDetalleTransCollection(Collection<GamacAmaAdmunDetalleTrans> gamacAmaAdmunDetalleTransCollection) {
        this.gamacAmaAdmunDetalleTransCollection = gamacAmaAdmunDetalleTransCollection;
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
        if (!(object instanceof GamacAmaMunicion)) {
            return false;
        }
        GamacAmaMunicion other = (GamacAmaMunicion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacAmaMunicion[ id=" + id + " ]";
    }
    
}
