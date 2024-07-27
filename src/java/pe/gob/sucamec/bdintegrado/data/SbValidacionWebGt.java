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
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author mespinoza
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_VALIDACION_WEB", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbValidacionWeb.findAll", query = "SELECT s FROM SbValidacionWeb s"),
    @NamedQuery(name = "SbValidacionWeb.findById", query = "SELECT s FROM SbValidacionWeb s WHERE s.id = :id"),
    @NamedQuery(name = "SbValidacionWeb.findByUrlValidacion", query = "SELECT s FROM SbValidacionWeb s WHERE s.urlValidacion = :urlValidacion"),
    @NamedQuery(name = "SbValidacionWeb.findByFechaIni", query = "SELECT s FROM SbValidacionWeb s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SbValidacionWeb.findByFechaVal", query = "SELECT s FROM SbValidacionWeb s WHERE s.fechaVal = :fechaVal"),
    @NamedQuery(name = "SbValidacionWeb.findByFechaFin", query = "SELECT s FROM SbValidacionWeb s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SbValidacionWeb.findByActivo", query = "SELECT s FROM SbValidacionWeb s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbValidacionWeb.findByAudLogin", query = "SELECT s FROM SbValidacionWeb s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbValidacionWeb.findByAudNumIp", query = "SELECT s FROM SbValidacionWeb s WHERE s.audNumIp = :audNumIp")})
public class SbValidacionWebGt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_VALIDACION_WEB")
    @SequenceGenerator(name = "SEQ_SB_VALIDACION_WEB", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_VALIDACION_WEB", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 500)
    @Column(name = "URL_VALIDACION")
    private String urlValidacion;
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_VAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVal;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuarioId;

    public SbValidacionWebGt() {
    }

    public SbValidacionWebGt(Long id) {
        this.id = id;
    }

    public SbValidacionWebGt(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getUrlValidacion() {
        return urlValidacion;
    }

    public void setUrlValidacion(String urlValidacion) {
        this.urlValidacion = urlValidacion;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaVal() {
        return fechaVal;
    }

    public void setFechaVal(Date fechaVal) {
        this.fechaVal = fechaVal;
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

    public TipoBaseGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBaseGt tipoId) {
        this.tipoId = tipoId;
    }

    public SbUsuarioGt getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuarioGt usuarioId) {
        this.usuarioId = usuarioId;
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
        if (!(object instanceof SbValidacionWebGt)) {
            return false;
        }
        SbValidacionWebGt other = (SbValidacionWebGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.SbValidacionWeb[ id=" + id + " ]";
    }
    
}
