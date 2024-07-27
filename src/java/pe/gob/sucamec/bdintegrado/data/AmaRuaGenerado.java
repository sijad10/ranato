/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author gchavez
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_RUA_GENERADO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaRuaGenerado.findAll", query = "SELECT a FROM AmaRuaGenerado a"),
    @NamedQuery(name = "AmaRuaGenerado.findById", query = "SELECT a FROM AmaRuaGenerado a WHERE a.id = :id"),
    @NamedQuery(name = "AmaRuaGenerado.findByRuaGenerado", query = "SELECT a FROM AmaRuaGenerado a WHERE a.ruaGenerado = :ruaGenerado"),
    @NamedQuery(name = "AmaRuaGenerado.findByActivo", query = "SELECT a FROM AmaRuaGenerado a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaRuaGenerado.findByAudLogin", query = "SELECT a FROM AmaRuaGenerado a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaRuaGenerado.findByAudNumIp", query = "SELECT a FROM AmaRuaGenerado a WHERE a.audNumIp = :audNumIp")})
public class AmaRuaGenerado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_RUA_GENERADO")
    @SequenceGenerator(name = "SEQ_AMA_RUA_GENERADO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_RUA_GENERADO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac estadoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "RUA_GENERADO")
    private String ruaGenerado;
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
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @ManyToMany(mappedBy = "amaRuaGeneradoList")
    private List<AmaDetalleResoArma> amaDetalleResoArmaList;

    public AmaRuaGenerado() {
    }

    public AmaRuaGenerado(Long id) {
        this.id = id;
    }

    public AmaRuaGenerado(Long id, String ruaGenerado, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.ruaGenerado = ruaGenerado;
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

    public String getRuaGenerado() {
        return ruaGenerado;
    }

    public void setRuaGenerado(String ruaGenerado) {
        this.ruaGenerado = ruaGenerado;
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
    public List<AmaDetalleResoArma> getAmaDetalleResoArmaList() {
        return amaDetalleResoArmaList;
    }

    public void setAmaDetalleResoArmaList(List<AmaDetalleResoArma> amaDetalleResoArmaList) {
        this.amaDetalleResoArmaList = amaDetalleResoArmaList;
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
        if (!(object instanceof AmaRuaGenerado)) {
            return false;
        }
        AmaRuaGenerado other = (AmaRuaGenerado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaRuaGenerado[ id=" + id + " ]";
    }

    public TipoGamac getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoGamac estadoId) {
        this.estadoId = estadoId;
    }

}
