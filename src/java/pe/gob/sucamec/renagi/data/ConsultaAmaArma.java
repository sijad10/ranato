/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.data;

import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.bdintegrado.data.UnidadMedida;
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
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_ARMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsultaAmaArma.findAll", query = "SELECT a FROM ConsultaAmaArma a"),
    @NamedQuery(name = "ConsultaAmaArma.findById", query = "SELECT a FROM ConsultaAmaArma a WHERE a.id = :id"),
    @NamedQuery(name = "ConsultaAmaArma.findByNroRua", query = "SELECT a FROM ConsultaAmaArma a WHERE a.nroRua = :nroRua"),
    @NamedQuery(name = "ConsultaAmaArma.findBySerie", query = "SELECT a FROM ConsultaAmaArma a WHERE a.serie = :serie"),
    @NamedQuery(name = "ConsultaAmaArma.findByPesoInicial", query = "SELECT a FROM ConsultaAmaArma a WHERE a.pesoInicial = :pesoInicial"),
    @NamedQuery(name = "ConsultaAmaArma.findByDescripcion", query = "SELECT a FROM ConsultaAmaArma a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "ConsultaAmaArma.findByActivo", query = "SELECT a FROM ConsultaAmaArma a WHERE a.activo = :activo"),
    @NamedQuery(name = "ConsultaAmaArma.findByAudLogin", query = "SELECT a FROM ConsultaAmaArma a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "ConsultaAmaArma.findByAudNumIp", query = "SELECT a FROM ConsultaAmaArma a WHERE a.audNumIp = :audNumIp")})
public class ConsultaAmaArma implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_ARMA")
    @SequenceGenerator(name = "SEQ_CONSULTA_AMA_ARMA", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_ARMA", allocationSize = 1)
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
    /*@JoinTable(schema="BDINTEGRADO", name = "AMA_GUIA_TRANSITO_ARMAS", joinColumns = {
        @JoinColumn(name = "GAMAC_ARMA_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "GAMAC_GUIA_TRANSITO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<AmaGuiaTransito> amaGuiaTransitoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "armaId")
    private List<AmaVerificarArma> amaVerificarArmaList;*/
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "armaId")
    private List<ConsultaAmaTarjetaPropiedad> amaTarjetaPropiedadList;
    @JoinColumn(name = "UNIDAD_MEDIDA_ID", referencedColumnName = "ID")
    @ManyToOne
    private UnidadMedida unidadMedidaId;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaTipoGamac estadoId;
    @JoinColumn(name = "SITUACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private ConsultaTipoGamac situacionId;
    @JoinColumn(name = "DIRECCION_SUCAMEC_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbDireccion direccionSucamecId;
    @JoinColumn(name = "TIPO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaAmaCatalogo tipoArmaId;
    @JoinColumn(name = "CALIBRE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaAmaCatalogo calibreId;
    @JoinColumn(name = "MARCA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaAmaCatalogo marcaId;
    @JoinColumn(name = "MODELO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaAmaModelos modeloId;

    public ConsultaAmaArma() {
    }

    public ConsultaAmaArma(Long id) {
        this.id = id;
    }

    public ConsultaAmaArma(Long id, String nroRua, String serie, short activo) {
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

    /*
    @XmlTransient
    public List<AmaGuiaTransito> getAmaGuiaTransitoList() {
        return amaGuiaTransitoList;
    }

    public void setAmaGuiaTransitoList(List<AmaGuiaTransito> amaGuiaTransitoList) {
        this.amaGuiaTransitoList = amaGuiaTransitoList;
    }

    @XmlTransient
    public List<AmaVerificarArma> getAmaVerificarArmaList() {
        return amaVerificarArmaList;
    }

    public void setAmaVerificarArmaList(List<AmaVerificarArma> amaVerificarArmaList) {
        this.amaVerificarArmaList = amaVerificarArmaList;
    }
    */

    @XmlTransient
    public List<ConsultaAmaTarjetaPropiedad> getAmaTarjetaPropiedadList() {
        return amaTarjetaPropiedadList;
    }

    public void setAmaTarjetaPropiedadList(List<ConsultaAmaTarjetaPropiedad> amaTarjetaPropiedadList) {
        this.amaTarjetaPropiedadList = amaTarjetaPropiedadList;
    }

    public UnidadMedida getUnidadMedidaId() {
        return unidadMedidaId;
    }

    public void setUnidadMedidaId(UnidadMedida unidadMedidaId) {
        this.unidadMedidaId = unidadMedidaId;
    }

    public ConsultaTipoGamac getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(ConsultaTipoGamac estadoId) {
        this.estadoId = estadoId;
    }

    public ConsultaTipoGamac getSituacionId() {
        return situacionId;
    }

    public void setSituacionId(ConsultaTipoGamac situacionId) {
        this.situacionId = situacionId;
    }

    public SbDireccion getDireccionSucamecId() {
        return direccionSucamecId;
    }

    public void setDireccionSucamecId(SbDireccion direccionSucamecId) {
        this.direccionSucamecId = direccionSucamecId;
    }

    public ConsultaAmaCatalogo getTipoArmaId() {
        return tipoArmaId;
    }

    public void setTipoArmaId(ConsultaAmaCatalogo tipoArmaId) {
        this.tipoArmaId = tipoArmaId;
    }

    public ConsultaAmaCatalogo getCalibreId() {
        return calibreId;
    }

    public void setCalibreId(ConsultaAmaCatalogo calibreId) {
        this.calibreId = calibreId;
    }

    public ConsultaAmaCatalogo getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(ConsultaAmaCatalogo marcaId) {
        this.marcaId = marcaId;
    }

    public ConsultaAmaModelos getModeloId() {
        return modeloId;
    }

    public void setModeloId(ConsultaAmaModelos modeloId) {
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
        if (!(object instanceof ConsultaAmaArma)) {
            return false;
        }
        ConsultaAmaArma other = (ConsultaAmaArma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.renagi.data.ConsultaAmaArma[ id=" + id + " ]";
    }
    
}
