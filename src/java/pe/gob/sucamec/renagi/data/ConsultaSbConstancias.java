/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.data;

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
    @NamedQuery(name = "ConsultaSbConstancias.findAll", query = "SELECT s FROM ConsultaSbConstancias s"),
    @NamedQuery(name = "ConsultaSbConstancias.findById", query = "SELECT s FROM ConsultaSbConstancias s WHERE s.id = :id"),
    @NamedQuery(name = "ConsultaSbConstancias.findByNroConstancia", query = "SELECT s FROM ConsultaSbConstancias s WHERE s.nroConstancia = :nroConstancia"),
    @NamedQuery(name = "ConsultaSbConstancias.findByFechaEmision", query = "SELECT s FROM ConsultaSbConstancias s WHERE s.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "ConsultaSbConstancias.findByHashQr", query = "SELECT s FROM ConsultaSbConstancias s WHERE s.hashQr = :hashQr"),
    @NamedQuery(name = "ConsultaSbConstancias.findByActivo", query = "SELECT s FROM ConsultaSbConstancias s WHERE s.activo = :activo"),
    @NamedQuery(name = "ConsultaSbConstancias.findByAudLogin", query = "SELECT s FROM ConsultaSbConstancias s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "ConsultaSbConstancias.findByAudNumIp", query = "SELECT s FROM ConsultaSbConstancias s WHERE s.audNumIp = :audNumIp")})
public class ConsultaSbConstancias implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONSULTA_SB_CONSTANCIAS")
    @SequenceGenerator(name = "SEQ_CONSULTA_SB_CONSTANCIAS", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_CONSTANCIAS", allocationSize = 1)
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
    private ConsultaTipoBase areaId;
    @JoinColumn(name = "TIPO_CONSTANCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaTipoBase tipoConstanciaId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaSbUsuario usuarioId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaSbPersona personaId;
    @JoinColumn(name = "GERENTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaSbUsuario gerenteId;

    public ConsultaSbConstancias() {
    }

    public ConsultaSbConstancias(Long id) {
        this.id = id;
    }

    public ConsultaSbConstancias(Long id, Date fechaEmision, String hashQr, short activo, String audLogin, String audNumIp) {
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

    public ConsultaTipoBase getAreaId() {
        return areaId;
    }

    public void setAreaId(ConsultaTipoBase areaId) {
        this.areaId = areaId;
    }

    public ConsultaTipoBase getTipoConstanciaId() {
        return tipoConstanciaId;
    }

    public void setTipoConstanciaId(ConsultaTipoBase tipoConstanciaId) {
        this.tipoConstanciaId = tipoConstanciaId;
    }

    public ConsultaSbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(ConsultaSbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public ConsultaSbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(ConsultaSbPersona personaId) {
        this.personaId = personaId;
    }

    public ConsultaSbUsuario getGerenteId() {
        return gerenteId;
    }

    public void setGerenteId(ConsultaSbUsuario gerenteId) {
        this.gerenteId = gerenteId;
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
        if (!(object instanceof ConsultaSbConstancias)) {
            return false;
        }
        ConsultaSbConstancias other = (ConsultaSbConstancias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.renagi.data.ConsultaSbConstancias[ id=" + id + " ]";
    }
    
}
