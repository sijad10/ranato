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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_POLIGONO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspPoligono.findAll", query = "SELECT s FROM SspPoligono s"),
    @NamedQuery(name = "SspPoligono.findById", query = "SELECT s FROM SspPoligono s WHERE s.id = :id"),
    @NamedQuery(name = "SspPoligono.findByNroResolucion", query = "SELECT s FROM SspPoligono s WHERE s.nroResolucion = :nroResolucion"),
    @NamedQuery(name = "SspPoligono.findByFechaIni", query = "SELECT s FROM SspPoligono s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SspPoligono.findByFechaFin", query = "SELECT s FROM SspPoligono s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SspPoligono.findByAlquilado", query = "SELECT s FROM SspPoligono s WHERE s.alquilado = :alquilado"),
    @NamedQuery(name = "SspPoligono.findByActivo", query = "SELECT s FROM SspPoligono s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspPoligono.findByAudLogin", query = "SELECT s FROM SspPoligono s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspPoligono.findByAudNumIp", query = "SELECT s FROM SspPoligono s WHERE s.audNumIp = :audNumIp")})
public class SspPoligono implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_POLIGONO")
    @SequenceGenerator(name = "SEQ_SSP_POLIGONO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_POLIGONO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NRO_RESOLUCION")
    private String nroResolucion;
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "ALQUILADO")
    private Short alquilado;
    @Column(name = "ACTIVO")
    private Short activo;
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "PROPIETARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt propietarioId;
    @JoinColumn(name = "LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspLocalAutorizacion localId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;

    public SspPoligono() {
    }

    public SspPoligono(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNroResolucion() {
        return nroResolucion;
    }

    public void setNroResolucion(String nroResolucion) {
        this.nroResolucion = nroResolucion;
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

    public Short getAlquilado() {
        return alquilado;
    }

    public void setAlquilado(Short alquilado) {
        this.alquilado = alquilado;
    }

    public Short getActivo() {
        return activo;
    }

    public void setActivo(Short activo) {
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

    public SbPersonaGt getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(SbPersonaGt propietarioId) {
        this.propietarioId = propietarioId;
    }

    public SspLocalAutorizacion getLocalId() {
        return localId;
    }

    public void setLocalId(SspLocalAutorizacion localId) {
        this.localId = localId;
    }

    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
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
        if (!(object instanceof SspPoligono)) {
            return false;
        }
        SspPoligono other = (SspPoligono) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspPoligono[ id=" + id + " ]";
    }
    
}
