/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_CONTRATO_ALQ_POLV", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppContratoAlqPolv.findAll", query = "SELECT e FROM EppContratoAlqPolv e"),
    @NamedQuery(name = "EppContratoAlqPolv.findById", query = "SELECT e FROM EppContratoAlqPolv e WHERE e.id = :id"),
    @NamedQuery(name = "EppContratoAlqPolv.findByPlazo", query = "SELECT e FROM EppContratoAlqPolv e WHERE e.plazo = :plazo"),
    @NamedQuery(name = "EppContratoAlqPolv.findByFeciniAlq", query = "SELECT e FROM EppContratoAlqPolv e WHERE e.feciniAlq = :feciniAlq"),
    @NamedQuery(name = "EppContratoAlqPolv.findByFecfinAlq", query = "SELECT e FROM EppContratoAlqPolv e WHERE e.fecfinAlq = :fecfinAlq"),
    @NamedQuery(name = "EppContratoAlqPolv.findByActivo", query = "SELECT e FROM EppContratoAlqPolv e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppContratoAlqPolv.findByAudLogin", query = "SELECT e FROM EppContratoAlqPolv e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppContratoAlqPolv.findByAudNumIp", query = "SELECT e FROM EppContratoAlqPolv e WHERE e.audNumIp = :audNumIp"),
    @NamedQuery(name = "EppContratoAlqPolv.findByArea", query = "SELECT e FROM EppContratoAlqPolv e WHERE e.area = :area")})
public class EppContratoAlqPolv implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_CONTRATO_ALQ_POLV")
    @SequenceGenerator(name = "SEQ_EPP_CONTRATO_ALQ_POLV", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_CONTRATO_ALQ_POLV", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "PLAZO")
    private Long plazo;
    @Column(name = "FECINI_ALQ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feciniAlq;
    @Column(name = "FECFIN_ALQ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecfinAlq;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AREA")
    private Double area;
    @JoinColumn(name = "TIPOCONTRATO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipocontratoId;
    @JoinColumn(name = "ARRENDADORA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona arrendadoraId;
    @JoinColumn(name = "ARRENDATARIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona arrendatariaId;
    @JoinColumn(name = "POLVORIN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppPolvorin polvorinId;
    @JoinColumn(name = "FABRICA_ID",referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private EppRegistro registro;

    public EppContratoAlqPolv() {
    }

    public EppContratoAlqPolv(Long id) {
        this.id = id;
    }

    public EppContratoAlqPolv(Long id, short activo, String audLogin, String audNumIp) {
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

    public Long getPlazo() {
        return plazo;
    }

    public void setPlazo(Long plazo) {
        this.plazo = plazo;
    }

    public Date getFeciniAlq() {
        return feciniAlq;
    }

    public void setFeciniAlq(Date feciniAlq) {
        this.feciniAlq = feciniAlq;
    }

    public Date getFecfinAlq() {
        return fecfinAlq;
    }

    public void setFecfinAlq(Date fecfinAlq) {
        this.fecfinAlq = fecfinAlq;
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

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public TipoExplosivoGt getTipocontratoId() {
        return tipocontratoId;
    }

    public void setTipocontratoId(TipoExplosivoGt tipocontratoId) {
        this.tipocontratoId = tipocontratoId;
    }

    public SbPersona getArrendadoraId() {
        return arrendadoraId;
    }

    public void setArrendadoraId(SbPersona arrendadoraId) {
        this.arrendadoraId = arrendadoraId;
    }

    public SbPersona getArrendatariaId() {
        return arrendatariaId;
    }

    public void setArrendatariaId(SbPersona arrendatariaId) {
        this.arrendatariaId = arrendatariaId;
    }

    public EppPolvorin getPolvorinId() {
        return polvorinId;
    }

    public void setPolvorinId(EppPolvorin polvorinId) {
        this.polvorinId = polvorinId;
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
        if (!(object instanceof EppContratoAlqPolv)) {
            return false;
        }
        EppContratoAlqPolv other = (EppContratoAlqPolv) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppContratoAlqPolv[ id=" + id + " ]";
    }
    
    public EppRegistro getRegistro() {
        return registro;
    }

    public void setRegistro(EppRegistro registro) {
        this.registro = registro;
    }
    
}
