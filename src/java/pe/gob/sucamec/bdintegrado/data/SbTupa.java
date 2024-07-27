/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_TUPA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbTupa.findAll", query = "SELECT s FROM SbTupa s"),
    @NamedQuery(name = "SbTupa.findById", query = "SELECT s FROM SbTupa s WHERE s.id = :id"),
    @NamedQuery(name = "SbTupa.findByFechaVigencia", query = "SELECT s FROM SbTupa s WHERE s.fechaVigencia = :fechaVigencia"),
    @NamedQuery(name = "SbTupa.findByPlazo", query = "SELECT s FROM SbTupa s WHERE s.plazo = :plazo"),
    @NamedQuery(name = "SbTupa.findByImporte", query = "SELECT s FROM SbTupa s WHERE s.importe = :importe"),
    @NamedQuery(name = "SbTupa.findByVigente", query = "SELECT s FROM SbTupa s WHERE s.vigente = :vigente"),
    @NamedQuery(name = "SbTupa.findByActivo", query = "SELECT s FROM SbTupa s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbTupa.findByAudLogin", query = "SELECT s FROM SbTupa s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbTupa.findByAudNumIp", query = "SELECT s FROM SbTupa s WHERE s.audNumIp = :audNumIp")})
public class SbTupa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_TUPA")
    @SequenceGenerator(name = "SEQ_SB_TUPA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_TUPA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_VIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVigencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PLAZO_LIMA_DIAS")
    private int plazoLimaDias;
    @Column(name = "PLAZO_PROVINCIA_DIAS")
    private int plazoProvinciaDias;
    //@Basic(optional = false)
    //@NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PLAZO")
    private String plazo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "IMPORTE")
    private BigDecimal importe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VIGENTE")
    private short vigente;
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
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt areaId;
    @JoinColumn(name = "TUPA_PROCEDIMIENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tupaProcedimientoId;
    @JoinColumn(name = "CANAL_ATENCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt canalAtencionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procedimientoTupaId")
    private List<SbExpVirtualSolicitud> sbExpVirtualSolicitudList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procedimientoTupaId")
    private List<SbTupaRequisito> sbTupaRequisitoList;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_TRIBUTO")
    private long codTributo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VISIBLE_ENLACE_PROPIO")
    private short VisibleEnlacePropio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CYDOC_ID_PROCESO")
    private int cydocIdProceso;

    public SbTupa() {
    }

    public SbTupa(Long id) {
        this.id = id;
    }

    public SbTupa(Long id, Date fechaVigencia, String plazo, BigDecimal importe, short vigente, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaVigencia = fechaVigencia;
        this.plazo = plazo;
        this.importe = importe;
        this.vigente = vigente;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public int getPlazoLimaDias() {
        return plazoLimaDias;
    }

    public void setPlazoLimaDias(int plazoLimaDias) {
        this.plazoLimaDias = plazoLimaDias;
    }

    public int getPlazoProvinciaDias() {
        return plazoProvinciaDias;
    }

    public void setPlazoProvinciaDias(int plazoProvinciaDias) {
        this.plazoProvinciaDias = plazoProvinciaDias;
    }

    public short getVisibleEnlacePropio() {
        return VisibleEnlacePropio;
    }

    public void setVisibleEnlacePropio(short VisibleEnlacePropio) {
        this.VisibleEnlacePropio = VisibleEnlacePropio;
    }  

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCodTributo() {
        return codTributo;
    }

    public void setCodTributo(long codTributo) {
        this.codTributo = codTributo;
    }

    public int getCydocIdProceso() {
        return cydocIdProceso;
    }

    public void setCydocIdProceso(int cydocIdProceso) {
        this.cydocIdProceso = cydocIdProceso;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public short getVigente() {
        return vigente;
    }

    public void setVigente(short vigente) {
        this.vigente = vigente;
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

    public TipoBaseGt getAreaId() {
        return areaId;
    }

    public void setAreaId(TipoBaseGt areaId) {
        this.areaId = areaId;
    }

    public TipoBaseGt getTupaProcedimientoId() {
        return tupaProcedimientoId;
    }

    public void setTupaProcedimientoId(TipoBaseGt tupaProcedimientoId) {
        this.tupaProcedimientoId = tupaProcedimientoId;
    }

    public TipoBaseGt getCanalAtencionId() {
        return canalAtencionId;
    }

    public void setCanalAtencionId(TipoBaseGt canalAtencionId) {
        this.canalAtencionId = canalAtencionId;
    }

    @XmlTransient
    public List<SbExpVirtualSolicitud> getSbExpVirtualSolicitudList() {
        return sbExpVirtualSolicitudList;
    }

    public void setSbExpVirtualSolicitudList(List<SbExpVirtualSolicitud> sbExpVirtualSolicitudList) {
        this.sbExpVirtualSolicitudList = sbExpVirtualSolicitudList;
    }

    @XmlTransient
    public List<SbTupaRequisito> getSbTupaRequisitoList() {
        return sbTupaRequisitoList;
    }

    public void setSbTupaRequisitoList(List<SbTupaRequisito> sbTupaRequisitoList) {
        this.sbTupaRequisitoList = sbTupaRequisitoList;
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
        if (!(object instanceof SbTupa)) {
            return false;
        }
        SbTupa other = (SbTupa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.SbTupa[ id=" + id + " ]";
    }
    
}
