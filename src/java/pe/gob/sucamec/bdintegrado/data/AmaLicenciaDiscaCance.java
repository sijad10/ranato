/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author msalinas
 */
@Entity
@Table(name = "AMA_LICENCIA_DISCA_CANCE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaLicenciaDiscaCance.findAll", query = "SELECT a FROM AmaLicenciaDiscaCance a"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findById", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.id = :id"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByTipoDocId", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.tipoDocId = :tipoDocId"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByNroDoc", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.nroDoc = :nroDoc"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByFecDoc", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.fecDoc = :fecDoc"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByFecCan", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.fecCan = :fecCan"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByObserva", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.observa = :observa"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByMotivoCancId", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.motivoCancId = :motivoCancId"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByFlagDisca", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.flagDisca = :flagDisca"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByActual", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.actual = :actual"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByActivo", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByNroLic", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.nroLic = :nroLic"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByTipoArma", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.tipoArma = :tipoArma"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findBySerie", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.serie = :serie"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByMarca", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.marca = :marca"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByModelo", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.modelo = :modelo"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByCalibre", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.calibre = :calibre"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByPropietarioDisca", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.propietarioDisca = :propietarioDisca"),
    @NamedQuery(name = "AmaLicenciaDiscaCance.findByUsuario", query = "SELECT a FROM AmaLicenciaDiscaCance a WHERE a.usuario = :usuario")})
public class AmaLicenciaDiscaCance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_LICENCIA_DISCA_CANCE")
    @SequenceGenerator(name = "SEQ_AMA_LICENCIA_DISCA_CANCE", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_LICENCIA_DISCA_CANCE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TIPO_DOC_ID")
    private BigInteger tipoDocId;
    @Size(max = 25)
    @Column(name = "NRO_DOC")
    private String nroDoc;
    @Column(name = "FEC_DOC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecDoc;
    @Column(name = "FEC_CAN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCan;
    @Size(max = 500)
    @Column(name = "OBSERVA")
    private String observa;
    @Column(name = "MOTIVO_CANC_ID")
    private Long motivoCancId;
    @Column(name = "FLAG_DISCA")
    private Short flagDisca;
    @Column(name = "ACTUAL")
    private Short actual;
    @Column(name = "ACTIVO")
    private Short activo;
    @Size(max = 25)
    @Column(name = "NRO_LIC")
    private String nroLic;
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
    @Size(max = 50)
    @Column(name = "PROPIETARIO_DISCA")
    private String propietarioDisca;
    @Size(max = 50)
    @Column(name = "USUARIO")
    private String usuario;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuario usuarioId;
    @JoinColumn(name = "ID_LIC_MIGRA", referencedColumnName = "ID")
    @ManyToOne
    private AmaMigraDiscaFox idLicMigra;

    public AmaLicenciaDiscaCance() {
    }

    public AmaLicenciaDiscaCance(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getTipoDocId() {
        return tipoDocId;
    }

    public void setTipoDocId(BigInteger tipoDocId) {
        this.tipoDocId = tipoDocId;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public Date getFecDoc() {
        return fecDoc;
    }

    public void setFecDoc(Date fecDoc) {
        this.fecDoc = fecDoc;
    }

    public Date getFecCan() {
        return fecCan;
    }

    public void setFecCan(Date fecCan) {
        this.fecCan = fecCan;
    }

    public String getObserva() {
        return observa;
    }

    public void setObserva(String observa) {
        this.observa = observa;
    }

    public Long getMotivoCancId() {
        return motivoCancId;
    }

    public void setMotivoCancId(Long motivoCancId) {
        this.motivoCancId = motivoCancId;
    }

    public Short getFlagDisca() {
        return flagDisca;
    }

    public void setFlagDisca(Short flagDisca) {
        this.flagDisca = flagDisca;
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

    public String getNroLic() {
        return nroLic;
    }

    public void setNroLic(String nroLic) {
        this.nroLic = nroLic;
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

    public String getPropietarioDisca() {
        return propietarioDisca;
    }

    public void setPropietarioDisca(String propietarioDisca) {
        this.propietarioDisca = propietarioDisca;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public SbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public AmaMigraDiscaFox getIdLicMigra() {
        return idLicMigra;
    }

    public void setIdLicMigra(AmaMigraDiscaFox idLicMigra) {
        this.idLicMigra = idLicMigra;
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
        if (!(object instanceof AmaLicenciaDiscaCance)) {
            return false;
        }
        AmaLicenciaDiscaCance other = (AmaLicenciaDiscaCance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.AmaLicenciaDiscaCance[ id=" + id + " ]";
    }

    public AmaLicenciaDiscaCance(BigInteger tipoDocId, String nroDoc, Date fecDoc, Date fecCan, String observa, Long motivoCancId, Short flagDisca, Short actual, Short activo, String nroLic, String tipoArma, String serie, String marca, String modelo, String calibre, String propietarioDisca, String usuario, SbUsuario usuarioId, AmaMigraDiscaFox idLicMigra) {
        this.tipoDocId = tipoDocId;
        this.nroDoc = nroDoc;
        this.fecDoc = fecDoc;
        this.fecCan = fecCan;
        this.observa = observa;
        this.motivoCancId = motivoCancId;
        this.flagDisca = flagDisca;
        this.actual = actual;
        this.activo = activo;
        this.nroLic = nroLic;
        this.tipoArma = tipoArma;
        this.serie = serie;
        this.marca = marca;
        this.modelo = modelo;
        this.calibre = calibre;
        this.propietarioDisca = propietarioDisca;
        this.usuario = usuario;
        this.usuarioId = usuarioId;
        this.idLicMigra = idLicMigra;
    }  
    
}
