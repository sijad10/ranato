/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
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
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author rfernandezv
 */
@Entity
@Table(name = "SB_CS_MEDICO_ESTABSAL", catalog = "", schema = "BDINTEGRADO")
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbCsMedicoEstabsal.findAll", query = "SELECT s FROM SbCsMedicoEstabsal s"),
    @NamedQuery(name = "SbCsMedicoEstabsal.findById", query = "SELECT s FROM SbCsMedicoEstabsal s WHERE s.id = :id"),
    @NamedQuery(name = "SbCsMedicoEstabsal.findByFechaIni", query = "SELECT s FROM SbCsMedicoEstabsal s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SbCsMedicoEstabsal.findByFechaFin", query = "SELECT s FROM SbCsMedicoEstabsal s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SbCsMedicoEstabsal.findByActivo", query = "SELECT s FROM SbCsMedicoEstabsal s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbCsMedicoEstabsal.findByAudLogin", query = "SELECT s FROM SbCsMedicoEstabsal s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbCsMedicoEstabsal.findByAudNumIp", query = "SELECT s FROM SbCsMedicoEstabsal s WHERE s.audNumIp = :audNumIp")})
public class SbCsMedicoEstabsal implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_CS_MEDICO_ESTABSAL")
    @SequenceGenerator(name = "SEQ_SB_CS_MEDICO_ESTABSAL", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_CS_MEDICO_ESTABSAL", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
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
    @JoinColumn(name = "CARGO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt cargoId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuarioId;
    @JoinColumn(name = "MEDICO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbCsMedico medicoId;
    @JoinColumn(name = "ESTABLECIMIENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbCsEstablecimiento establecimientoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HABILITADO")
    private short habilitado;
    @Column(name = "ES_DELEGADO")
    private Short esDelegado;
    @Column(name = "FECHA_INI_HAB")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIniHab;
    @Column(name = "FECHA_FIN_HAB")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinHab;

    public SbCsMedicoEstabsal() {
    }

    public SbCsMedicoEstabsal(Long id) {
        this.id = id;
    }

    public SbCsMedicoEstabsal(Long id, short activo, String audLogin, String audNumIp) {
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

    public TipoBaseGt getCargoId() {
        return cargoId;
    }

    public void setCargoId(TipoBaseGt cargoId) {
        this.cargoId = cargoId;
    }

    public SbUsuarioGt getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuarioGt usuarioId) {
        this.usuarioId = usuarioId;
    }

    public SbCsMedico getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(SbCsMedico medicoId) {
        this.medicoId = medicoId;
    }

    public SbCsEstablecimiento getEstablecimientoId() {
        return establecimientoId;
    }

    public void setEstablecimientoId(SbCsEstablecimiento establecimientoId) {
        this.establecimientoId = establecimientoId;
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
        if (!(object instanceof SbCsMedicoEstabsal)) {
            return false;
        }
        SbCsMedicoEstabsal other = (SbCsMedicoEstabsal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbCsMedicoEstabsal[ id=" + id + " ]";
    }
    
    public short getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(short habilitado) {
        this.habilitado = habilitado;
    }
    
    public Short getEsDelegado() {
        return esDelegado;
    }

    public void setEsDelegado(Short esDelegado) {
        this.esDelegado = esDelegado;
    }

    public Date getFechaIniHab() {
        return fechaIniHab;
    }

    public void setFechaIniHab(Date fechaIniHab) {
        this.fechaIniHab = fechaIniHab;
    }

    public Date getFechaFinHab() {
        return fechaFinHab;
    }

    public void setFechaFinHab(Date fechaFinHab) {
        this.fechaFinHab = fechaFinHab;
    }
    
    public boolean getEsDelegadoBoolean() {
        if(esDelegado == null || esDelegado == 0){
            return false;
        }
        return true;
    }

    public void setEsDelegadoBoolean(boolean esDelegado) {
        if(esDelegado){
            this.esDelegado = (short) 1;
        }else{
            this.esDelegado = (short) 0;
        }
    }
}
