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
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_ARMA_INVENTARIO_DIF" , catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaArmaInventarioDif.findAll", query = "SELECT a FROM AmaArmaInventarioDif a"),
    @NamedQuery(name = "AmaArmaInventarioDif.findById", query = "SELECT a FROM AmaArmaInventarioDif a WHERE a.id = :id"),
    @NamedQuery(name = "AmaArmaInventarioDif.findByFecha", query = "SELECT a FROM AmaArmaInventarioDif a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "AmaArmaInventarioDif.findByActivo", query = "SELECT a FROM AmaArmaInventarioDif a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaArmaInventarioDif.findByAudLogin", query = "SELECT a FROM AmaArmaInventarioDif a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaArmaInventarioDif.findByAudNumIp", query = "SELECT a FROM AmaArmaInventarioDif a WHERE a.audNumIp = :audNumIp")})
public class AmaArmaInventarioDif implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_ARMA_INVENTARIO_DIF")
    @SequenceGenerator(name = "SEQ_AMA_ARMA_INVENTARIO_DIF", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_ARMA_INVENTARIO_DIF", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
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
    @JoinColumn(name = "ARMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaArma armaId;
    @JoinColumn(name = "INVENTARIO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaInventarioArma inventarioArmaId;
    @JoinColumn(name = "SITUACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac situacionId;

    public AmaArmaInventarioDif() {
    }

    public AmaArmaInventarioDif(Long id) {
        this.id = id;
    }

    public AmaArmaInventarioDif(Long id, Date fecha, short activo, String audLogin, String audNumIp) {
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

    public AmaArma getArmaId() {
        return armaId;
    }

    public void setArmaId(AmaArma armaId) {
        this.armaId = armaId;
    }

    public AmaInventarioArma getInventarioArmaId() {
        return inventarioArmaId;
    }

    public void setInventarioArmaId(AmaInventarioArma inventarioArmaId) {
        this.inventarioArmaId = inventarioArmaId;
    }

    public TipoGamac getSituacionId() {
        return situacionId;
    }

    public void setSituacionId(TipoGamac situacionId) {
        this.situacionId = situacionId;
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
        if (!(object instanceof AmaArmaInventarioDif)) {
            return false;
        }
        AmaArmaInventarioDif other = (AmaArmaInventarioDif) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.bdintegrado.data.AmaArmaInventarioDif[ id=" + id + " ]";
    }
}
