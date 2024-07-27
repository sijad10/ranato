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
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_LIBRO_EVENTOS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppLibroEventos.findAll", query = "SELECT e FROM EppLibroEventos e"),
    @NamedQuery(name = "EppLibroEventos.findById", query = "SELECT e FROM EppLibroEventos e WHERE e.id = :id"),
    @NamedQuery(name = "EppLibroEventos.findByFecha", query = "SELECT e FROM EppLibroEventos e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "EppLibroEventos.findByObservacion", query = "SELECT e FROM EppLibroEventos e WHERE e.observacion = :observacion"),
    @NamedQuery(name = "EppLibroEventos.findByActivo", query = "SELECT e FROM EppLibroEventos e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppLibroEventos.findByAudLogin", query = "SELECT e FROM EppLibroEventos e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppLibroEventos.findByAudNumIp", query = "SELECT e FROM EppLibroEventos e WHERE e.audNumIp = :audNumIp")})
public class EppLibroEventos implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_LIBRO_EVENTOS")
    @SequenceGenerator(name = "SEQ_EPP_LIBRO_EVENTOS", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_LIBRO_EVENTOS", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 200)
    @Column(name = "OBSERVACION")
    private String observacion;
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
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoId;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario userId;
    @JoinColumn(name = "LIBRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppLibro libroId;

    public EppLibroEventos() {
    }

    public EppLibroEventos(Long id) {
        this.id = id;
    }

    public EppLibroEventos(Long id, Date fecha, short activo, String audLogin, String audNumIp) {
        this.id = id;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public TipoExplosivoGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoExplosivoGt tipoId) {
        this.tipoId = tipoId;
    }

    public SbUsuario getUserId() {
        return userId;
    }

    public void setUserId(SbUsuario userId) {
        this.userId = userId;
    }

    public EppLibro getLibroId() {
        return libroId;
    }

    public void setLibroId(EppLibro libroId) {
        this.libroId = libroId;
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
        if (!(object instanceof EppLibroEventos)) {
            return false;
        }
        EppLibroEventos other = (EppLibroEventos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppLibroEventos[ id=" + id + " ]";
    }
    
}
