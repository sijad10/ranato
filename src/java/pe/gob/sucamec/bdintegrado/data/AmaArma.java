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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.SbDireccion;

/**
 *
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_ARMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaArma.findAll", query = "SELECT a FROM AmaArma a"),
    @NamedQuery(name = "AmaArma.findById", query = "SELECT a FROM AmaArma a WHERE a.id = :id"),
    @NamedQuery(name = "AmaArma.findByNroRua", query = "SELECT a FROM AmaArma a WHERE a.nroRua = :nroRua"),
    @NamedQuery(name = "AmaArma.findBySerie", query = "SELECT a FROM AmaArma a WHERE a.serie = :serie"),
    @NamedQuery(name = "AmaArma.findByPesoInicial", query = "SELECT a FROM AmaArma a WHERE a.pesoInicial = :pesoInicial"),
    @NamedQuery(name = "AmaArma.findByDescripcion", query = "SELECT a FROM AmaArma a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "AmaArma.findByActivo", query = "SELECT a FROM AmaArma a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaArma.findByAudLogin", query = "SELECT a FROM AmaArma a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaArma.findByAudNumIp", query = "SELECT a FROM AmaArma a WHERE a.audNumIp = :audNumIp")})
public class AmaArma implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_ARMA")
    @SequenceGenerator(name = "SEQ_AMA_ARMA", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_ARMA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    /*@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)*/
    @Column(name = "NRO_RUA")
    private String nroRua;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "SERIE")
    private String serie;
    @Column(name = "PESO_INICIAL")
    private Long pesoInicial;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private short activo;
    @Size(max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Size(max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @ManyToMany(mappedBy = "amaArmaList")
    private List<AmaGuiaTransito> amaGuiaTransitoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "armaId")
    private List<AmaTarjetaPropiedad> amaTarjetaPropiedadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "armaId")
    private List<AmaVerificarArma> amaVerificarArmaList;
    @JoinColumn(name = "MARCA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaCatalogo marcaId;
    @JoinColumn(name = "TIPO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaCatalogo tipoArmaId;
    @JoinColumn(name = "CALIBRE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaCatalogo calibreId;
    @JoinColumn(name = "MODELO_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaModelos modeloId;
    @JoinColumn(name = "DIRECCION_SUCAMEC_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbDireccion direccionSucamecId;    
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac estadoId;
    @JoinColumn(name = "UNIDAD_MEDIDA_ID", referencedColumnName = "ID")
    @ManyToOne
    private UnidadMedida unidadMedidaId;
    @JoinColumn(name = "SITUACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac situacionId;
    @Column(name = "LICENCIA_DISCA")
    private Long licenciaDisca;

    public AmaArma() {
    }

    public AmaArma(Long id) {
        this.id = id;
    }

    public AmaArma(Long id, String nroRua, String serie, short activo) {
        this.id = id;
        this.nroRua = nroRua;
        this.serie = serie;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNroRua() {
        return nroRua;
    }

    public void setNroRua(String nroRua) {
        this.nroRua = nroRua;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Long getPesoInicial() {
        return pesoInicial;
    }

    public void setPesoInicial(Long pesoInicial) {
        this.pesoInicial = pesoInicial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
    public List<AmaGuiaTransito> getAmaGuiaTransitoList() {
        return amaGuiaTransitoList;
    }

    public void setAmaGuiaTransitoList(List<AmaGuiaTransito> amaGuiaTransitoList) {
        this.amaGuiaTransitoList = amaGuiaTransitoList;
    }

    @XmlTransient
    public List<AmaTarjetaPropiedad> getAmaTarjetaPropiedadList() {
        return amaTarjetaPropiedadList;
    }

    public void setAmaTarjetaPropiedadList(List<AmaTarjetaPropiedad> amaTarjetaPropiedadList) {
        this.amaTarjetaPropiedadList = amaTarjetaPropiedadList;
    }

    @XmlTransient
    public List<AmaVerificarArma> getAmaVerificarArmaList() {
        return amaVerificarArmaList;
    }

    public void setAmaVerificarArmaList(List<AmaVerificarArma> amaVerificarArmaList) {
        this.amaVerificarArmaList = amaVerificarArmaList;
    }

    public AmaCatalogo getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(AmaCatalogo marcaId) {
        this.marcaId = marcaId;
    }

    public AmaCatalogo getTipoArmaId() {
        return tipoArmaId;
    }

    public void setTipoArmaId(AmaCatalogo tipoArmaId) {
        this.tipoArmaId = tipoArmaId;
    }

    public AmaCatalogo getCalibreId() {
        return calibreId;
    }

    public void setCalibreId(AmaCatalogo calibreId) {
        this.calibreId = calibreId;
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
        if (!(object instanceof AmaArma)) {
            return false;
        }
        AmaArma other = (AmaArma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaArma[ id=" + id + " ]";
    }

    /**
     * @return the direccionSucamecId
     */
    public SbDireccion getDireccionSucamecId() {
        return direccionSucamecId;
    }

    /**
     * @param direccionSucamecId the direccionSucamecId to set
     */
    public void setDireccionSucamecId(SbDireccion direccionSucamecId) {
        this.direccionSucamecId = direccionSucamecId;
    }

    /**
     * @return the estadoId
     */
    public TipoGamac getEstadoId() {
        return estadoId;
    }

    /**
     * @param estadoId the estadoId to set
     */
    public void setEstadoId(TipoGamac estadoId) {
        this.estadoId = estadoId;
    }

    /**
     * @return the unidadMedidaId
     */
    public UnidadMedida getUnidadMedidaId() {
        return unidadMedidaId;
    }

    /**
     * @param unidadMedidaId the unidadMedidaId to set
     */
    public void setUnidadMedidaId(UnidadMedida unidadMedidaId) {
        this.unidadMedidaId = unidadMedidaId;
    }

    public TipoGamac getSituacionId() {
        return situacionId;
    }

    public void setSituacionId(TipoGamac situacionId) {
        this.situacionId = situacionId;
    }
    
    public Long getLicenciaDisca() {
        return licenciaDisca;
    }

    public void setLicenciaDisca(Long licenciaDisca) {
        this.licenciaDisca = licenciaDisca;
    }
}
