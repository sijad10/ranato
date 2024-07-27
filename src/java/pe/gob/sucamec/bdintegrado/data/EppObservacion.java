/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_OBSERVACION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppObservacion.findAll", query = "SELECT e FROM EppObservacion e"),
    @NamedQuery(name = "EppObservacion.findById", query = "SELECT e FROM EppObservacion e WHERE e.id = :id"),
    @NamedQuery(name = "EppObservacion.findByNombre", query = "SELECT e FROM EppObservacion e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EppObservacion.findByDescripcion", query = "SELECT e FROM EppObservacion e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "EppObservacion.findByActivo", query = "SELECT e FROM EppObservacion e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppObservacion.findByAudLogin", query = "SELECT e FROM EppObservacion e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppObservacion.findByAudNumIp", query = "SELECT e FROM EppObservacion e WHERE e.audNumIp = :audNumIp")})
public class EppObservacion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_OBSERVACION")
    @SequenceGenerator(name = "SEQ_EPP_OBSERVACION", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_OBSERVACION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
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
    @ManyToMany(mappedBy = "eppObservacionList")
    private List<EppDocumento> eppDocumentoList;
    @JoinColumn(name = "TIPO_OBSERVACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoObservacionId;
    @JoinColumn(name = "TIPO_PROCESO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoProcesoId;

    public EppObservacion() {
    }

    public EppObservacion(Long id) {
        this.id = id;
    }

    public EppObservacion(Long id, String nombre, String descripcion, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    @XmlTransient
    public List<EppDocumento> getEppDocumentoList() {
        return eppDocumentoList;
    }

    public void setEppDocumentoList(List<EppDocumento> eppDocumentoList) {
        this.eppDocumentoList = eppDocumentoList;
    }

    public TipoBaseGt getTipoObservacionId() {
        return tipoObservacionId;
    }

    public void setTipoObservacionId(TipoBaseGt tipoObservacionId) {
        this.tipoObservacionId = tipoObservacionId;
    }

    public TipoBaseGt getTipoProcesoId() {
        return tipoProcesoId;
    }

    public void setTipoProcesoId(TipoBaseGt tipoProcesoId) {
        this.tipoProcesoId = tipoProcesoId;
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
        if (!(object instanceof EppObservacion)) {
            return false;
        }
        EppObservacion other = (EppObservacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppObservacion[ id=" + id + " ]";
    }
    
}
