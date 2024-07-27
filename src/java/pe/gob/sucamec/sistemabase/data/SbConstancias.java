/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.data;

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
import pe.gob.sucamec.sistemabase.data.SbTipo;
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_CONSTANCIAS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbConstancias.findAll", query = "SELECT s FROM SbConstancias s"),
    @NamedQuery(name = "SbConstancias.findById", query = "SELECT s FROM SbConstancias s WHERE s.id = :id"),
    @NamedQuery(name = "SbConstancias.findByNroConstancia", query = "SELECT s FROM SbConstancias s WHERE s.nroConstancia = :nroConstancia"),
    @NamedQuery(name = "SbConstancias.findByFechaEmision", query = "SELECT s FROM SbConstancias s WHERE s.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "SbConstancias.findByHashQr", query = "SELECT s FROM SbConstancias s WHERE s.hashQr = :hashQr"),
    @NamedQuery(name = "SbConstancias.findByActivo", query = "SELECT s FROM SbConstancias s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbConstancias.findByAudLogin", query = "SELECT s FROM SbConstancias s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbConstancias.findByAudNumIp", query = "SELECT s FROM SbConstancias s WHERE s.audNumIp = :audNumIp")})
public class SbConstancias implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_CONSTANCIAS")
    @SequenceGenerator(name = "SEQ_SB_CONSTANCIAS", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_CONSTANCIAS", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NRO_CONSTANCIA")
    private Long nroConstancia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
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
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo areaId;
    @JoinColumn(name = "TIPO_CONSTANCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoConstanciaId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaId;
    @JoinColumn(name = "GERENTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario gerenteId;
    @Column(name = "REFERENCIA_ID")
    private Long referenciaId;

    public SbConstancias() {
    }

    public SbConstancias(Long id) {
        this.id = id;
    }

    public SbConstancias(Long id, Date fechaEmision, String hashQr, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaEmision = fechaEmision;
        this.hashQr = hashQr;
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

    public Long getNroConstancia() {
        return nroConstancia;
    }

    public void setNroConstancia(Long nroConstancia) {
        this.nroConstancia = nroConstancia;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getHashQr() {
        return hashQr;
    }

    public void setHashQr(String hashQr) {
        this.hashQr = hashQr;
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

    public SbTipo getAreaId() {
        return areaId;
    }

    public void setAreaId(SbTipo areaId) {
        this.areaId = areaId;
    }

    public SbTipo getTipoConstanciaId() {
        return tipoConstanciaId;
    }

    public void setTipoConstanciaId(SbTipo tipoConstanciaId) {
        this.tipoConstanciaId = tipoConstanciaId;
    }

    public SbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public SbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersona personaId) {
        this.personaId = personaId;
    }

    public SbUsuario getGerenteId() {
        return gerenteId;
    }

    public void setGerenteId(SbUsuario gerenteId) {
        this.gerenteId = gerenteId;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
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
        if (!(object instanceof SbConstancias)) {
            return false;
        }
        SbConstancias other = (SbConstancias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemagamac.data.SbConstancias[ id=" + id + " ]";
    }
    
}
