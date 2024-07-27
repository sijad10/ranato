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
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_CARNE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspCarne.findAll", query = "SELECT s FROM SspCarne s"),
    @NamedQuery(name = "SspCarne.findById", query = "SELECT s FROM SspCarne s WHERE s.id = :id"),
    @NamedQuery(name = "SspCarne.findByNroCarne", query = "SELECT s FROM SspCarne s WHERE s.nroCarne = :nroCarne"),
    @NamedQuery(name = "SspCarne.findByFechaIni", query = "SELECT s FROM SspCarne s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SspCarne.findByFechaFin", query = "SELECT s FROM SspCarne s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SspCarne.findByFechaEmision", query = "SELECT s FROM SspCarne s WHERE s.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "SspCarne.findByCese", query = "SELECT s FROM SspCarne s WHERE s.cese = :cese"),
    @NamedQuery(name = "SspCarne.findByFechaBaja", query = "SELECT s FROM SspCarne s WHERE s.fechaBaja = :fechaBaja"),
    @NamedQuery(name = "SspCarne.findByEmitida", query = "SELECT s FROM SspCarne s WHERE s.emitida = :emitida"),
    @NamedQuery(name = "SspCarne.findByHashQr", query = "SELECT s FROM SspCarne s WHERE s.hashQr = :hashQr"),
    @NamedQuery(name = "SspCarne.findByDetalleDupli", query = "SELECT s FROM SspCarne s WHERE s.detalleDupli = :detalleDupli"),
    @NamedQuery(name = "SspCarne.findByActivo", query = "SELECT s FROM SspCarne s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspCarne.findByAudLogin", query = "SELECT s FROM SspCarne s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspCarne.findByAudNumIp", query = "SELECT s FROM SspCarne s WHERE s.audNumIp = :audNumIp")})
public class SspCarne implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_CARNE")
    @SequenceGenerator(name = "SEQ_SSP_CARNE", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_CARNE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_CARNE")
    private int nroCarne;
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "CESE")
    private Short cese;
    @Column(name = "FECHA_BAJA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaBaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EMITIDA")
    private short emitida;
    @Size(max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
    @Size(max = 500)
    @Column(name = "DETALLE_DUPLI")
    private String detalleDupli;
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
    @JoinColumn(name = "MODALIDAD_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoSeguridad modalidadId;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad estadoId;
    @JoinColumn(name = "FOTO_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SspPersonaFoto fotoId;
    @JoinColumn(name = "VIGILANTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt vigilanteId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carneId")
    private List<SspRegistro> sspRegistroList;
    @JoinColumn(name = "FORMATO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SspFormatoCarne formatoId;
    @Column(name = "DIGITAL")
    private Short digital;

    public SspCarne() {
    }

    public SspCarne(Long id) {
        this.id = id;
    }

    public SspCarne(Long id, int nroCarne, short emitida, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroCarne = nroCarne;
        this.emitida = emitida;
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

    public int getNroCarne() {
        return nroCarne;
    }

    public void setNroCarne(int nroCarne) {
        this.nroCarne = nroCarne;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Short getCese() {
        return cese;
    }

    public void setCese(Short cese) {
        this.cese = cese;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public short getEmitida() {
        return emitida;
    }

    public void setEmitida(short emitida) {
        this.emitida = emitida;
    }

    public String getHashQr() {
        return hashQr;
    }

    public void setHashQr(String hashQr) {
        this.hashQr = hashQr;
    }

    public String getDetalleDupli() {
        return detalleDupli;
    }

    public void setDetalleDupli(String detalleDupli) {
        this.detalleDupli = detalleDupli;
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

    public TipoSeguridad getModalidadId() {
        return modalidadId;
    }

    public void setModalidadId(TipoSeguridad modalidadId) {
        this.modalidadId = modalidadId;
    }

    public TipoSeguridad getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoSeguridad estadoId) {
        this.estadoId = estadoId;
    }

    public SspPersonaFoto getFotoId() {
        return fotoId;
    }

    public void setFotoId(SspPersonaFoto fotoId) {
        this.fotoId = fotoId;
    }

    public SbPersonaGt getVigilanteId() {
        return vigilanteId;
    }

    public void setVigilanteId(SbPersonaGt vigilanteId) {
        this.vigilanteId = vigilanteId;
    }

    @XmlTransient
    public List<SspRegistro> getSspRegistroList() {
        return sspRegistroList;
    }

    public void setSspRegistroList(List<SspRegistro> sspRegistroList) {
        this.sspRegistroList = sspRegistroList;
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
        if (!(object instanceof SspCarne)) {
            return false;
        }
        SspCarne other = (SspCarne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspCarne[ id=" + id + " ]";
    }
    
    public SspFormatoCarne getFormatoId() {
        return formatoId;
    }

    public void setFormatoId(SspFormatoCarne formatoId) {
        this.formatoId = formatoId;
    }

    public Short getDigital() {
        return digital;
    }

    public void setDigital(Short digital) {
        this.digital = digital;
    }

}