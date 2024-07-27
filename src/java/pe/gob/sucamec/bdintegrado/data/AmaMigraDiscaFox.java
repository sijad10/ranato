/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author msalinas
 */
@Entity
@Table(name = "AMA_MIGRA_DISCA_FOX", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaMigraDiscaFox.findAll", query = "SELECT a FROM AmaMigraDiscaFox a"),
    @NamedQuery(name = "AmaMigraDiscaFox.findById", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.id = :id"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByTipoPropietario", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.tipoPropietario = :tipoPropietario"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByRucProp", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.rucProp = :rucProp"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByRznSocial", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.rznSocial = :rznSocial"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByDocProp", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.docProp = :docProp"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByApePat", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.apePat = :apePat"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByApeMat", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.apeMat = :apeMat"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByNombres", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.nombres = :nombres"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByNroCip", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.nroCip = :nroCip"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByInstitucion", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.institucion = :institucion"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByActividad", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.actividad = :actividad"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByNroLic", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.nroLic = :nroLic"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByTipoLicenciaId", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.tipoLicenciaId = :tipoLicenciaId"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByFecEmi", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.fecEmi = :fecEmi"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByFecVen", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.fecVen = :fecVen"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByEstadoLicenciaId", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.estadoLicenciaId = :estadoLicenciaId"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByNroExpe", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.nroExpe = :nroExpe"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByTipoArma", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.tipoArma = :tipoArma"),
    @NamedQuery(name = "AmaMigraDiscaFox.findBySerie", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.serie = :serie"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByMarca", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.marca = :marca"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByModelo", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.modelo = :modelo"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByCalibre", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.calibre = :calibre"),
    @NamedQuery(name = "AmaMigraDiscaFox.findBySituacionDisca", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.situacionDisca = :situacionDisca"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByDireccion", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.direccion = :direccion"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByDistritoId", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.distritoId = :distritoId"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByProvinciaId", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.provinciaId = :provinciaId"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByDepartamentoId", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.departamentoId = :departamentoId"),
    @NamedQuery(name = "AmaMigraDiscaFox.findBySistema", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.sistema = :sistema"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByActual", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.actual = :actual"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByActivo", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByCodArchivo", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.codArchivo = :codArchivo"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByNomDistrito", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.nomDistrito = :nomDistrito"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByNomProvincia", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.nomProvincia = :nomProvincia"),
    @NamedQuery(name = "AmaMigraDiscaFox.findByNomDepartamento", query = "SELECT a FROM AmaMigraDiscaFox a WHERE a.nomDepartamento = :nomDepartamento")})
public class AmaMigraDiscaFox implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_MIGRA_DISCA_FOX")
    @SequenceGenerator(name = "SEQ_AMA_MIGRA_DISCA_FOX", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_MIGRA_DISCA_FOX", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TIPO_PROPIETARIO")
    private Long tipoPropietario;
    @Size(max = 50)
    @Column(name = "RUC_PROP")
    private String rucProp;
    @Size(max = 100)
    @Column(name = "RZN_SOCIAL")
    private String rznSocial;
    @Size(max = 50)
    @Column(name = "DOC_PROP")
    private String docProp;
    @Size(max = 50)
    @Column(name = "APE_PAT")
    private String apePat;
    @Size(max = 50)
    @Column(name = "APE_MAT")
    private String apeMat;
    @Size(max = 100)
    @Column(name = "NOMBRES")
    private String nombres;
    @Size(max = 50)
    @Column(name = "NRO_CIP")
    private String nroCip;
    @Column(name = "INSTITUCION")
    private Long institucion;
    @Column(name = "ACTIVIDAD")
    private Short actividad;
    @Size(max = 25)
    @Column(name = "NRO_LIC")
    private String nroLic;
    @Column(name = "TIPO_LICENCIA_ID")
    private BigInteger tipoLicenciaId;
    @Column(name = "FEC_EMI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecEmi;
    @Column(name = "FEC_VEN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecVen;
    @Size(max = 25)
    @Column(name = "ESTADO_LICENCIA_ID")
    private String estadoLicenciaId;
    @Size(max = 25)
    @Column(name = "NRO_EXPE")
    private String nroExpe;
    @Size(max = 25)
    @Column(name = "TIPO_ARMA")
    private String tipoArma;
    @Size(max = 50)
    @Column(name = "SERIE")
    private String serie;
    @Size(max = 50)
    @Column(name = "MARCA")
    private String marca;
    @Size(max = 50)
    @Column(name = "MODELO")
    private String modelo;
    @Size(max = 50)
    @Column(name = "CALIBRE")
    private String calibre;
    @Column(name = "SITUACION_DISCA")
    private Long situacionDisca;
    @Size(max = 250)
    @Column(name = "DIRECCION")
    private String direccion;
    @Column(name = "DISTRITO_ID")
    private Long distritoId;
    @Column(name = "PROVINCIA_ID")
    private Long provinciaId;
    @Column(name = "DEPARTAMENTO_ID")
    private Long departamentoId;
    @Size(max = 25)
    @Column(name = "SISTEMA")
    private String sistema;
    @Column(name = "ACTUAL")
    private Short actual;
    @Column(name = "ACTIVO")
    private Short activo;
    @Size(max = 25)
    @Column(name = "COD_ARCHIVO")
    private String codArchivo;
    @Size(max = 50)
    @Column(name = "NOM_DISTRITO")
    private String nomDistrito;
    @Size(max = 50)
    @Column(name = "NOM_PROVINCIA")
    private String nomProvincia;
    @Size(max = 50)
    @Column(name = "NOM_DEPARTAMENTO")
    private String nomDepartamento;
    @Column(name = "ESTADO_ARMA_ID")
    private Long estadoArmaId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona personaId;
    @JoinColumn(name = "ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaArma armaId;
    @OneToMany(mappedBy = "idLicMigra")
    private List<AmaLicenciaDiscaCance> amaLicenciaDiscaCanceList;

    public AmaMigraDiscaFox() {
    }

    public AmaMigraDiscaFox(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTipoPropietario() {
        return tipoPropietario;
    }

    public void setTipoPropietario(Long tipoPropietario) {
        this.tipoPropietario = tipoPropietario;
    }

    public String getRucProp() {
        return rucProp;
    }

    public void setRucProp(String rucProp) {
        this.rucProp = rucProp;
    }

    public String getRznSocial() {
        return rznSocial;
    }

    public void setRznSocial(String rznSocial) {
        this.rznSocial = rznSocial;
    }

    public String getDocProp() {
        return docProp;
    }

    public void setDocProp(String docProp) {
        this.docProp = docProp;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getNroCip() {
        return nroCip;
    }

    public void setNroCip(String nroCip) {
        this.nroCip = nroCip;
    }

    public Long getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Long institucion) {
        this.institucion = institucion;
    }

    public Short getActividad() {
        return actividad;
    }

    public void setActividad(Short actividad) {
        this.actividad = actividad;
    }

    public String getNroLic() {
        return nroLic;
    }

    public void setNroLic(String nroLic) {
        this.nroLic = nroLic;
    }

    public BigInteger getTipoLicenciaId() {
        return tipoLicenciaId;
    }

    public void setTipoLicenciaId(BigInteger tipoLicenciaId) {
        this.tipoLicenciaId = tipoLicenciaId;
    }

    public Date getFecEmi() {
        return fecEmi;
    }

    public void setFecEmi(Date fecEmi) {
        this.fecEmi = fecEmi;
    }

    public Date getFecVen() {
        return fecVen;
    }

    public void setFecVen(Date fecVen) {
        this.fecVen = fecVen;
    }

    public String getEstadoLicenciaId() {
        return estadoLicenciaId;
    }

    public void setEstadoLicenciaId(String estadoLicenciaId) {
        this.estadoLicenciaId = estadoLicenciaId;
    }

    public String getNroExpe() {
        return nroExpe;
    }

    public void setNroExpe(String nroExpe) {
        this.nroExpe = nroExpe;
    }

    public String getTipoArma() {
        return tipoArma;
    }

    public void setTipoArma(String tipoArma) {
        this.tipoArma = tipoArma;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCalibre() {
        return calibre;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }

    public Long getSituacionDisca() {
        return situacionDisca;
    }

    public void setSituacionDisca(Long situacionDisca) {
        this.situacionDisca = situacionDisca;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(Long distritoId) {
        this.distritoId = distritoId;
    }

    public Long getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(Long provinciaId) {
        this.provinciaId = provinciaId;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    public Short getActual() {
        return actual;
    }

    public void setActual(Short actual) {
        this.actual = actual;
    }

    public Short getActivo() {
        return activo;
    }

    public void setActivo(Short activo) {
        this.activo = activo;
    }

    public String getCodArchivo() {
        return codArchivo;
    }

    public void setCodArchivo(String codArchivo) {
        this.codArchivo = codArchivo;
    }

    public String getNomDistrito() {
        return nomDistrito;
    }

    public void setNomDistrito(String nomDistrito) {
        this.nomDistrito = nomDistrito;
    }

    public String getNomProvincia() {
        return nomProvincia;
    }

    public void setNomProvincia(String nomProvincia) {
        this.nomProvincia = nomProvincia;
    }

    public String getNomDepartamento() {
        return nomDepartamento;
    }

    public Long getEstadoArmaId() {
        return estadoArmaId;
    }

    public void setEstadoArmaId(Long estadoArmaId) {
        this.estadoArmaId = estadoArmaId;
    }

    public void setNomDepartamento(String nomDepartamento) {
        this.nomDepartamento = nomDepartamento;
    }

    public SbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersona personaId) {
        this.personaId = personaId;
    }

    public AmaArma getArmaId() {
        return armaId;
    }

    public void setArmaId(AmaArma armaId) {
        this.armaId = armaId;
    }

    @XmlTransient
    public List<AmaLicenciaDiscaCance> getAmaLicenciaDiscaCanceList() {
        return amaLicenciaDiscaCanceList;
    }

    public void setAmaLicenciaDiscaCanceList(List<AmaLicenciaDiscaCance> amaLicenciaDiscaCanceList) {
        this.amaLicenciaDiscaCanceList = amaLicenciaDiscaCanceList;
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
        if (!(object instanceof AmaMigraDiscaFox)) {
            return false;
        }
        AmaMigraDiscaFox other = (AmaMigraDiscaFox) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.AmaMigraDiscaFox[ id=" + id + " ]";
    }
    
}
