/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;

//import pe.gob.sucamec.bdintegrado.data.TipoGamac;
//import pe.gob.sucamec.sel.citas.data.CitaAmaModelos;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TUR_LICENCIA_REG", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaTurLicenciaReg.findAll", query = "SELECT t FROM CitaTurLicenciaReg t"),
    @NamedQuery(name = "CitaTurLicenciaReg.findById", query = "SELECT t FROM CitaTurLicenciaReg t WHERE t.id = :id"),
    @NamedQuery(name = "CitaTurLicenciaReg.findByNroExpediente", query = "SELECT t FROM CitaTurLicenciaReg t WHERE t.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "CitaTurLicenciaReg.findByNumLic", query = "SELECT t FROM CitaTurLicenciaReg t WHERE t.numLic = :numLic"),
    @NamedQuery(name = "CitaTurLicenciaReg.findByFecEmi", query = "SELECT t FROM CitaTurLicenciaReg t WHERE t.fecEmi = :fecEmi"),
    @NamedQuery(name = "CitaTurLicenciaReg.findByFecVen", query = "SELECT t FROM CitaTurLicenciaReg t WHERE t.fecVen = :fecVen"),
    @NamedQuery(name = "CitaTurLicenciaReg.findBySerie", query = "SELECT t FROM CitaTurLicenciaReg t WHERE t.serie = :serie"),
    @NamedQuery(name = "CitaTurLicenciaReg.findByDescripcion", query = "SELECT t FROM CitaTurLicenciaReg t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "CitaTurLicenciaReg.findByObservacion", query = "SELECT t FROM CitaTurLicenciaReg t WHERE t.observacion = :observacion"),
    @NamedQuery(name = "CitaTurLicenciaReg.findByActivo", query = "SELECT t FROM CitaTurLicenciaReg t WHERE t.activo = :activo"),
    @NamedQuery(name = "CitaTurLicenciaReg.findByAudLogin", query = "SELECT t FROM CitaTurLicenciaReg t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "CitaTurLicenciaReg.findByAudNumIp", query = "SELECT t FROM CitaTurLicenciaReg t WHERE t.audNumIp = :audNumIp")})
public class CitaTurLicenciaReg implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TUR_LICENCIA_REG")
    @SequenceGenerator(name = "SEQ_TUR_LICENCIA_REG", schema = "BDINTEGRADO", sequenceName = "SEQ_TUR_LICENCIA_REG", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NRO_EXPEDIENTE")
    private Long nroExpediente;
    //@Basic(optional = false)
    //@NotNull
    @Column(name = "NUM_LIC")
    private Long numLic;
    @Column(name = "FEC_EMI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecEmi;
    @Column(name = "FEC_VEN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecVen;
    @Size(max = 50)
    @Column(name = "SERIE")
    private String serie;
    @Size(max = 100)
    @Column(name = "MARCA_NOMBRE")
    private String marcaNombre;
    @Size(max = 100)
    @Column(name = "MODELO_NOMBRE")
    private String modeloNombre;
    @Size(max = 100)
    @Column(name = "CALIBRE_NOMBRE")
    private String calibreNombre;
    @JoinColumn(name = "TIPO_INTERNAMIENTO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoGamac tipoInternamientoId;
    @JoinColumn(name = "TIPO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaCatalogo tipoArmaId;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 200)
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
    @ManyToMany(mappedBy = "turLicenciaRegList")
    private List<CitaTipoGamac> tipoGamacList;
    @OneToMany(mappedBy = "armaId")
    private List<CitaTurFotoReg> turFotoRegList;
    @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTurTurno turnoId;
    @JoinColumn(name = "TUR_FOTO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTurFotoReg turFotoId;
    @JoinColumn(name = "TIPO_LIC_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoGamac tipoLicId;
    @JoinColumn(name = "TIPO_ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTipoGamac tipoEstado;
    @JoinColumn(name = "SITUACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoGamac situacionId;
    @JoinColumn(name = "TIPO_EVALUADO", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoBase tipoEvaluado;
//    @JoinColumn(name = "CALIBRE", referencedColumnName = "ID")
//    @ManyToOne
//    private AmaCatalogo calibre;
    @JoinColumn(name = "TIPO_ARTICULO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaAmaCatalogo tipoArticulo;
    @JoinColumn(name = "MARCA", referencedColumnName = "ID")
    @ManyToOne
    private AmaCatalogo marca;
//    @JoinColumn(name = "MODELO", referencedColumnName = "ID")
//    @ManyToOne
//    private AmaCatalogo modelo;
    @JoinColumn(name = "MODELO", referencedColumnName = "ID")
    @ManyToOne
    private AmaModelos modelo;
    @JoinColumn(name = "ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaArma armaId;
    @Column(name = "ACTUAL")
    private short actual;
    @Column(name = "NRO_LICENCIA_CAF")
    private String nroLicenciaCaf;      
    @Column(name = "ACTA_INTERNAMIENTO_ID")
    private Long actaInternamientoId;
    @JoinColumn(name = "TUR_COMPROBANTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTurComprobante comprobanteId;
    
    public CitaTurLicenciaReg() {
    }

    public CitaTurLicenciaReg(Long id) {
        this.id = id;
    }

    public CitaTurLicenciaReg(Long id, Long numLic, Date fecVen, String serie, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.numLic = numLic;
        this.fecVen = fecVen;
        this.serie = serie;
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

    public Long getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(Long nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public Long getNumLic() {
        return numLic;
    }

    public void setNumLic(Long numLic) {
        this.numLic = numLic;
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

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    @XmlTransient
    public List<CitaTipoGamac> getTipoGamacList() {
        return tipoGamacList;
    }

    public void setTipoGamacList(List<CitaTipoGamac> tipoGamacList) {
        this.tipoGamacList = tipoGamacList;
    }

    @XmlTransient
    public List<CitaTurFotoReg> getTurFotoRegList() {
        return turFotoRegList;
    }

    public void setTurFotoRegList(List<CitaTurFotoReg> turFotoRegList) {
        this.turFotoRegList = turFotoRegList;
    }

    public CitaTurTurno getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(CitaTurTurno turnoId) {
        this.turnoId = turnoId;
    }

    public CitaTurFotoReg getTurFotoId() {
        return turFotoId;
    }

    public void setTurFotoId(CitaTurFotoReg turFotoId) {
        this.turFotoId = turFotoId;
    }

    public CitaTipoGamac getTipoLicId() {
        return tipoLicId;
    }

    public void setTipoLicId(CitaTipoGamac tipoLicId) {
        this.tipoLicId = tipoLicId;
    }

    public CitaTipoGamac getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(CitaTipoGamac tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public CitaTipoGamac getSituacionId() {
        return situacionId;
    }

    public void setSituacionId(CitaTipoGamac situacionId) {
        this.situacionId = situacionId;
    }

    public CitaTipoBase getTipoEvaluado() {
        return tipoEvaluado;
    }

    public void setTipoEvaluado(CitaTipoBase tipoEvaluado) {
        this.tipoEvaluado = tipoEvaluado;
    }

    public AmaModelos getModelo() {
        return modelo;
    }

    public void setModelo(AmaModelos modelo) {
        this.modelo = modelo;
    }

//    public AmaCatalogo getCalibre() {
//        return calibre;
//    }
//
//    public void setCalibre(AmaCatalogo calibre) {
//        this.calibre = calibre;
//    }
    public CitaAmaCatalogo getTipoArticulo() {
        return tipoArticulo;
    }

    public void setTipoArticulo(CitaAmaCatalogo tipoArticulo) {
        this.tipoArticulo = tipoArticulo;
    }

    public AmaArma getArmaId() {
        return armaId;
    }

    public void setArmaId(AmaArma armaId) {
        this.armaId = armaId;
    }    

    public short getActual() {
        return actual;
    }

    public void setActual(short actual) {
        this.actual = actual;
    }

    public String getNroLicenciaCaf() {
        return nroLicenciaCaf;
    }

    public void setNroLicenciaCaf(String nroLicenciaCaf) {
        this.nroLicenciaCaf = nroLicenciaCaf;
    }

    public CitaTurComprobante getComprobanteId() {
        return comprobanteId;
    }

    public void setComprobanteId(CitaTurComprobante comprobanteId) {
        this.comprobanteId = comprobanteId;
    }

    public String getMarcaNombre() {
        return marcaNombre;
    }

    public void setMarcaNombre(String marcaNombre) {
        this.marcaNombre = marcaNombre;
    }

    public String getModeloNombre() {
        return modeloNombre;
    }

    public void setModeloNombre(String modeloNombre) {
        this.modeloNombre = modeloNombre;
    }

    public String getCalibreNombre() {
        return calibreNombre;
    }

    public void setCalibreNombre(String calibreNombre) {
        this.calibreNombre = calibreNombre;
    }

    public CitaTipoGamac getTipoInternamientoId() {
        return tipoInternamientoId;
    }

    public void setTipoInternamientoId(CitaTipoGamac tipoInternamientoId) {
        this.tipoInternamientoId = tipoInternamientoId;
    }

    public AmaCatalogo getTipoArmaId() {
        return tipoArmaId;
    }

    public void setTipoArmaId(AmaCatalogo tipoArmaId) {
        this.tipoArmaId = tipoArmaId;
    }

    public Long getActaInternamientoId() {
        return actaInternamientoId;
    }

    public void setActaInternamientoId(Long actaInternamientoId) {
        this.actaInternamientoId = actaInternamientoId;
    }

    public AmaCatalogo getMarca() {
        return marca;
    }

    public void setMarca(AmaCatalogo marca) {
        this.marca = marca;
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
        if (!(object instanceof CitaTurLicenciaReg)) {
            return false;
        }
        CitaTurLicenciaReg other = (CitaTurLicenciaReg) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.citas.data.CitaTurLicenciaReg[ id=" + id + " ]";
    }
    
}
