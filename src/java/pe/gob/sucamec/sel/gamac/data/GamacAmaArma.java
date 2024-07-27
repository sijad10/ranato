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
@Table(name = "AMA_ARMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacAmaArma.findAll", query = "SELECT g FROM GamacAmaArma g"),
    @NamedQuery(name = "GamacAmaArma.findById", query = "SELECT g FROM GamacAmaArma g WHERE g.id = :id"),
    @NamedQuery(name = "GamacAmaArma.findByNroRua", query = "SELECT g FROM GamacAmaArma g WHERE g.nroRua = :nroRua"),
    @NamedQuery(name = "GamacAmaArma.findBySerie", query = "SELECT g FROM GamacAmaArma g WHERE g.serie = :serie"),
    @NamedQuery(name = "GamacAmaArma.findByModeloIdOld", query = "SELECT g FROM GamacAmaArma g WHERE g.modeloIdOld = :modeloIdOld"),
    @NamedQuery(name = "GamacAmaArma.findByPesoInicial", query = "SELECT g FROM GamacAmaArma g WHERE g.pesoInicial = :pesoInicial"),
    @NamedQuery(name = "GamacAmaArma.findByDescripcion", query = "SELECT g FROM GamacAmaArma g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "GamacAmaArma.findByActivo", query = "SELECT g FROM GamacAmaArma g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacAmaArma.findByAudLogin", query = "SELECT g FROM GamacAmaArma g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacAmaArma.findByAudNumIp", query = "SELECT g FROM GamacAmaArma g WHERE g.audNumIp = :audNumIp"),
    @NamedQuery(name = "GamacAmaArma.findByLicenciaDisca", query = "SELECT g FROM GamacAmaArma g WHERE g.licenciaDisca = :licenciaDisca")})
public class GamacAmaArma implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_AMA_ARMA")
    @SequenceGenerator(name = "SEQ_GAMAC_AMA_ARMA", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_ARMA", allocationSize = 1)
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
    @Column(name = "MODELO_ID_OLD")
    private Long modeloIdOld;
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
    @Column(name = "LICENCIA_DISCA")
    private Long licenciaDisca;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "armaId")
    private Collection<GamacAmaVerificarArma> gamacAmaVerificarArmaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "armaId")
    private Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection;
    @JoinColumn(name = "UNIDAD_MEDIDA_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacUnidadMedida unidadMedidaId;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoGamac estadoId;
    @JoinColumn(name = "SITUACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoGamac situacionId;
    @JoinColumn(name = "DIRECCION_SUCAMEC_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbDireccion direccionSucamecId;
    @JoinColumn(name = "MODELO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaModelos modeloId;
    @JoinColumn(name = "MARCA_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacAmaCatalogo marcaId;
    @JoinColumn(name = "CALIBRE_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacAmaCatalogo calibreId;
    @JoinColumn(name = "TIPO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacAmaCatalogo tipoArmaId;

    public GamacAmaArma() {
    }

    public GamacAmaArma(Long id) {
        this.id = id;
    }

    public GamacAmaArma(Long id, String nroRua, String serie, short activo) {
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

    public Long getModeloIdOld() {
        return modeloIdOld;
    }

    public void setModeloIdOld(Long modeloIdOld) {
        this.modeloIdOld = modeloIdOld;
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

    public Long getLicenciaDisca() {
        return licenciaDisca;
    }

    public void setLicenciaDisca(Long licenciaDisca) {
        this.licenciaDisca = licenciaDisca;
    }

    @XmlTransient
    public Collection<GamacAmaVerificarArma> getGamacAmaVerificarArmaCollection() {
        return gamacAmaVerificarArmaCollection;
    }

    public void setGamacAmaVerificarArmaCollection(Collection<GamacAmaVerificarArma> gamacAmaVerificarArmaCollection) {
        this.gamacAmaVerificarArmaCollection = gamacAmaVerificarArmaCollection;
    }

    @XmlTransient
    public Collection<GamacAmaTarjetaPropiedad> getGamacAmaTarjetaPropiedadCollection() {
        return gamacAmaTarjetaPropiedadCollection;
    }

    public void setGamacAmaTarjetaPropiedadCollection(Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection) {
        this.gamacAmaTarjetaPropiedadCollection = gamacAmaTarjetaPropiedadCollection;
    }

    public GamacUnidadMedida getUnidadMedidaId() {
        return unidadMedidaId;
    }

    public void setUnidadMedidaId(GamacUnidadMedida unidadMedidaId) {
        this.unidadMedidaId = unidadMedidaId;
    }

    public GamacTipoGamac getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(GamacTipoGamac estadoId) {
        this.estadoId = estadoId;
    }

    public GamacTipoGamac getSituacionId() {
        return situacionId;
    }

    public void setSituacionId(GamacTipoGamac situacionId) {
        this.situacionId = situacionId;
    }

    public GamacSbDireccion getDireccionSucamecId() {
        return direccionSucamecId;
    }

    public void setDireccionSucamecId(GamacSbDireccion direccionSucamecId) {
        this.direccionSucamecId = direccionSucamecId;
    }

    public GamacAmaModelos getModeloId() {
        return modeloId;
    }

    public void setModeloId(GamacAmaModelos modeloId) {
        this.modeloId = modeloId;
    }

    public GamacAmaCatalogo getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(GamacAmaCatalogo marcaId) {
        this.marcaId = marcaId;
    }

    public GamacAmaCatalogo getCalibreId() {
        return calibreId;
    }

    public void setCalibreId(GamacAmaCatalogo calibreId) {
        this.calibreId = calibreId;
    }

    public GamacAmaCatalogo getTipoArmaId() {
        return tipoArmaId;
    }

    public void setTipoArmaId(GamacAmaCatalogo tipoArmaId) {
        this.tipoArmaId = tipoArmaId;
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
        if (!(object instanceof GamacAmaArma)) {
            return false;
        }
        GamacAmaArma other = (GamacAmaArma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacAmaArma[ id=" + id + " ]";
    }
    
}
