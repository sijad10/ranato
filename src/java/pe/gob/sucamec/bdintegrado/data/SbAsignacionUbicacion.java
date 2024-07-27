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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import pe.gob.sucamec.sistemabase.data.SbDistrito;
import pe.gob.sucamec.sistemabase.data.SbProvincia;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_ASIGNACION_UBICACION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbAsignacionUbicacion.findAll", query = "SELECT s FROM SbAsignacionUbicacion s"),
    @NamedQuery(name = "SbAsignacionUbicacion.findById", query = "SELECT s FROM SbAsignacionUbicacion s WHERE s.id = :id"),
    @NamedQuery(name = "SbAsignacionUbicacion.findBySistemaId", query = "SELECT s FROM SbAsignacionUbicacion s WHERE s.sistemaId = :sistemaId"),
    @NamedQuery(name = "SbAsignacionUbicacion.findByActivo", query = "SELECT s FROM SbAsignacionUbicacion s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbAsignacionUbicacion.findByAudLogin", query = "SELECT s FROM SbAsignacionUbicacion s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbAsignacionUbicacion.findByAudNumIp", query = "SELECT s FROM SbAsignacionUbicacion s WHERE s.audNumIp = :audNumIp")})
public class SbAsignacionUbicacion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_ASIGNACION_UBICACION")
    @SequenceGenerator(name = "SEQ_SB_ASIGNACION_UBICACION", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_ASIGNACION_UBICACION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "SISTEMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbSistemaGt sistemaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "TIPO_PROCESO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoProcesoId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt areaId;
    @JoinColumn(name = "DEPARTAMENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDepartamentoGt departamentoId;
    @JoinColumn(name = "PROVINCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbProvincia provinciaId;
    @JoinColumn(name = "DISTRITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistrito distritoId;

    public SbAsignacionUbicacion() {
    }

    public SbAsignacionUbicacion(Long id) {
        this.id = id;
    }

    public SbAsignacionUbicacion(Long id, short activo, String audLogin, String audNumIp) {
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

    public SbSistemaGt getSistemaId() {
        return sistemaId;
    }

    public void setSistemaId(SbSistemaGt sistemaId) {
        this.sistemaId = sistemaId;
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

    public TipoBaseGt getTipoProcesoId() {
        return tipoProcesoId;
    }

    public void setTipoProcesoId(TipoBaseGt tipoProcesoId) {
        this.tipoProcesoId = tipoProcesoId;
    }

    public TipoBaseGt getAreaId() {
        return areaId;
    }

    public void setAreaId(TipoBaseGt areaId) {
        this.areaId = areaId;
    }

    public SbDepartamentoGt getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(SbDepartamentoGt departamentoId) {
        this.departamentoId = departamentoId;
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
        if (!(object instanceof SbAsignacionUbicacion)) {
            return false;
        }
        SbAsignacionUbicacion other = (SbAsignacionUbicacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbAsignacionUbicacion[ id=" + id + " ]";
    }
    
    public SbProvincia getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(SbProvincia provinciaId) {
        this.provinciaId = provinciaId;
    }

    public SbDistrito getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(SbDistrito distritoId) {
        this.distritoId = distritoId;
    }    
}
