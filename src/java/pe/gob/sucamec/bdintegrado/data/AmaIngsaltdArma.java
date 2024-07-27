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
 * @author locador192.ogtic
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_INGSALTD_ARMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaIngsaltdArma.findAll", query = "SELECT a FROM AmaIngsaltdArma a"),
    @NamedQuery(name = "AmaIngsaltdArma.findById", query = "SELECT a FROM AmaIngsaltdArma a WHERE a.id = :id"),
    @NamedQuery(name = "AmaIngsaltdArma.findBySerie", query = "SELECT a FROM AmaIngsaltdArma a WHERE a.serie = :serie"),
    @NamedQuery(name = "AmaIngsaltdArma.findByCantidad", query = "SELECT a FROM AmaIngsaltdArma a WHERE a.cantidad = :cantidad"),
    @NamedQuery(name = "AmaIngsaltdArma.findByNroLicPaisProcede", query = "SELECT a FROM AmaIngsaltdArma a WHERE a.nroLicPaisProcede = :nroLicPaisProcede"),
    @NamedQuery(name = "AmaIngsaltdArma.findByCantidadCargador", query = "SELECT a FROM AmaIngsaltdArma a WHERE a.cantidadCargador = :cantidadCargador"),
    @NamedQuery(name = "AmaIngsaltdArma.findByLicenciaDiscaId", query = "SELECT a FROM AmaIngsaltdArma a WHERE a.licenciaDiscaId = :licenciaDiscaId"),
    @NamedQuery(name = "AmaIngsaltdArma.findByActivo", query = "SELECT a FROM AmaIngsaltdArma a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaIngsaltdArma.findByAudLogin", query = "SELECT a FROM AmaIngsaltdArma a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaIngsaltdArma.findByAudNumIp", query = "SELECT a FROM AmaIngsaltdArma a WHERE a.audNumIp = :audNumIp")})
public class AmaIngsaltdArma implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_INGSALTD_ARMA")
    @SequenceGenerator(name = "SEQ_AMA_INGSALTD_ARMA", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_INGSALTD_ARMA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "SERIE")
    private String serie;
    @Column(name = "CANTIDAD")
    private Integer cantidad;
    @Size(max = 30)
    @Column(name = "NRO_LIC_PAIS_PROCEDE")
    private String nroLicPaisProcede;
    @Column(name = "CANTIDAD_CARGADOR")
    private Integer cantidadCargador;
    @Column(name = "LICENCIA_DISCA_ID")
    private Long licenciaDiscaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ingsaltdArmaId")
    private List<AmaIngsaltdArmaAdic> amaIngsaltdArmaAdicList;
    @OneToMany(mappedBy = "ingsaltdArmaId")
    private List<AmaIngsaltdMunicion> amaIngsaltdMunicionList;
    @OneToMany(mappedBy = "ingsaltdArmaId")
    private List<AmaIngsaltdMatrel> amaIngsaltdMatrelList;
//    @OneToMany(mappedBy = "ingsaltdArmaId")
//    private List<AmaIngresotdMatrel> amaIngresotdMatrelList;
    @JoinColumn(name = "TARJETA_PROPIEDAD_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaTarjetaPropiedad tarjetaPropiedadId;
    @JoinColumn(name = "MODELO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaModelos modeloId;
    @JoinColumn(name = "INGSALRUTAPER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaIngsaltdRutaPersona ingsalrutaperId;
//    @OneToMany(mappedBy = "ingsaltdArmaId")
//    private List<AmaIngsaltdMunicion> amaIngsaltdMunicionList;

    public AmaIngsaltdArma() {
    }

    public AmaIngsaltdArma(Long id) {
        this.id = id;
    }

    public AmaIngsaltdArma(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getNroLicPaisProcede() {
        return nroLicPaisProcede;
    }

    public void setNroLicPaisProcede(String nroLicPaisProcede) {
        this.nroLicPaisProcede = nroLicPaisProcede;
    }

    public Integer getCantidadCargador() {
        return cantidadCargador;
    }

    public void setCantidadCargador(Integer cantidadCargador) {
        this.cantidadCargador = cantidadCargador;
    }

    public Long getLicenciaDiscaId() {
        return licenciaDiscaId;
    }

    public void setLicenciaDiscaId(Long licenciaDiscaId) {
        this.licenciaDiscaId = licenciaDiscaId;
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
//
//    @XmlTransient
//    public List<AmaIngresotdMatrel> getAmaIngresotdMatrelList() {
//        return amaIngresotdMatrelList;
//    }
//
//    public void setAmaIngresotdMatrelList(List<AmaIngresotdMatrel> amaIngresotdMatrelList) {
//        this.amaIngresotdMatrelList = amaIngresotdMatrelList;
//    }

    @XmlTransient
    public List<AmaIngsaltdArmaAdic> getAmaIngsaltdArmaAdicList() {
        return amaIngsaltdArmaAdicList;
    }

    public void setAmaIngsaltdArmaAdicList(List<AmaIngsaltdArmaAdic> amaIngsaltdArmaAdicList) {
        this.amaIngsaltdArmaAdicList = amaIngsaltdArmaAdicList;
    }

    public AmaTarjetaPropiedad getTarjetaPropiedadId() {
        return tarjetaPropiedadId;
    }

    public void setTarjetaPropiedadId(AmaTarjetaPropiedad tarjetaPropiedadId) {
        this.tarjetaPropiedadId = tarjetaPropiedadId;
    }

    public AmaModelos getModeloId() {
        return modeloId;
    }

    public void setModeloId(AmaModelos modeloId) {
        this.modeloId = modeloId;
    }

    public AmaIngsaltdRutaPersona getIngsalrutaperId() {
        return ingsalrutaperId;
    }

    public void setIngsalrutaperId(AmaIngsaltdRutaPersona ingsalrutaperId) {
        this.ingsalrutaperId = ingsalrutaperId;
    }

//    @XmlTransient
//    public List<AmaIngsaltdMunicion> getAmaIngsaltdMunicionList() {
//        return amaIngsaltdMunicionList;
//    }
//
//    public void setAmaIngsaltdMunicionList(List<AmaIngsaltdMunicion> amaIngsaltdMunicionList) {
//        this.amaIngsaltdMunicionList = amaIngsaltdMunicionList;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmaIngsaltdArma)) {
            return false;
        }
        AmaIngsaltdArma other = (AmaIngsaltdArma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaIngsaltdArma[ id=" + id + " ]";
    }

}
