/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.turreg.data;

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
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TUR_REQUISITOS_REG", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TurRequisitosReg.findAll", query = "SELECT t FROM TurRequisitosReg t"),
    @NamedQuery(name = "TurRequisitosReg.findById", query = "SELECT t FROM TurRequisitosReg t WHERE t.id = :id"),
    @NamedQuery(name = "TurRequisitosReg.findByValorRequisito", query = "SELECT t FROM TurRequisitosReg t WHERE t.valorRequisito = :valorRequisito"),
    @NamedQuery(name = "TurRequisitosReg.findByFecha", query = "SELECT t FROM TurRequisitosReg t WHERE t.fecha = :fecha"),
    @NamedQuery(name = "TurRequisitosReg.findByVerificacionFisica", query = "SELECT t FROM TurRequisitosReg t WHERE t.verificacionFisica = :verificacionFisica"),
    @NamedQuery(name = "TurRequisitosReg.findByVerificacionReal", query = "SELECT t FROM TurRequisitosReg t WHERE t.verificacionReal = :verificacionReal"),
    @NamedQuery(name = "TurRequisitosReg.findByDescripcion", query = "SELECT t FROM TurRequisitosReg t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TurRequisitosReg.findByActivo", query = "SELECT t FROM TurRequisitosReg t WHERE t.activo = :activo"),
    @NamedQuery(name = "TurRequisitosReg.findByAudLogin", query = "SELECT t FROM TurRequisitosReg t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TurRequisitosReg.findByAudNumIp", query = "SELECT t FROM TurRequisitosReg t WHERE t.audNumIp = :audNumIp")})
public class TurRequisitosReg implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TUR_REQUISITOS_REG")
    @SequenceGenerator(name = "SEQ_TUR_REQUISITOS_REG", schema = "BDINTEGRADO", sequenceName = "SEQ_TUR_REQUISITOS_REG", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(min = 1, max = 200)
    @Column(name = "VALOR_REQUISITO")
    private String valorRequisito;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "VERIFICACION_FISICA")
    private Short verificacionFisica;
    @Column(name = "VERIFICACION_REAL")
    private Short verificacionReal;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
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
    @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TurTurno turnoId;
    @JoinColumn(name = "TIPO_REQUISITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase tipoRequisitoId;

    public TurRequisitosReg() {
    }

    public TurRequisitosReg(Long id) {
        this.id = id;
    }

    public TurRequisitosReg(Long id, String valorRequisito, Date fecha, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.valorRequisito = valorRequisito;
        this.fecha = fecha;
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

    public String getValorRequisito() {
        return valorRequisito;
    }

    public void setValorRequisito(String valorRequisito) {
        this.valorRequisito = valorRequisito;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Short getVerificacionFisica() {
        return verificacionFisica;
    }

    public void setVerificacionFisica(Short verificacionFisica) {
        this.verificacionFisica = verificacionFisica;
    }

    public Short getVerificacionReal() {
        return verificacionReal;
    }

    public void setVerificacionReal(Short verificacionReal) {
        this.verificacionReal = verificacionReal;
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

    public TurTurno getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(TurTurno turnoId) {
        this.turnoId = turnoId;
    }

    public TipoBase getTipoRequisitoId() {
        return tipoRequisitoId;
    }

    public void setTipoRequisitoId(TipoBase tipoRequisitoId) {
        this.tipoRequisitoId = tipoRequisitoId;
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
        if (!(object instanceof TurRequisitosReg)) {
            return false;
        }
        TurRequisitosReg other = (TurRequisitosReg) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.turreg.data.TurRequisitosReg[ id=" + id + " ]";
    }
    
}
