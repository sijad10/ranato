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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "EPP_LIBRO_PERDIDA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppLibroPerdida.findAll", query = "SELECT e FROM EppLibroPerdida e"),
    @NamedQuery(name = "EppLibroPerdida.findById", query = "SELECT e FROM EppLibroPerdida e WHERE e.id = :id"),
    @NamedQuery(name = "EppLibroPerdida.findByFechaPerdida", query = "SELECT e FROM EppLibroPerdida e WHERE e.fechaPerdida = :fechaPerdida"),
    @NamedQuery(name = "EppLibroPerdida.findByOtroPerdida", query = "SELECT e FROM EppLibroPerdida e WHERE e.otroPerdida = :otroPerdida"),
    @NamedQuery(name = "EppLibroPerdida.findByCantidad", query = "SELECT e FROM EppLibroPerdida e WHERE e.cantidad = :cantidad"),
    @NamedQuery(name = "EppLibroPerdida.findByEstado", query = "SELECT e FROM EppLibroPerdida e WHERE e.estado = :estado"),
    @NamedQuery(name = "EppLibroPerdida.findByActivo", query = "SELECT e FROM EppLibroPerdida e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppLibroPerdida.findByAudLogin", query = "SELECT e FROM EppLibroPerdida e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppLibroPerdida.findByAudNumIp", query = "SELECT e FROM EppLibroPerdida e WHERE e.audNumIp = :audNumIp")})
public class EppLibroPerdida implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_LIBRO_PERDIDA")
    @SequenceGenerator(name = "SEQ_EPP_LIBRO_PERDIDA", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_LIBRO_PERDIDA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_PERDIDA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPerdida;
    @Size(max = 255)
    @Column(name = "OTRO_PERDIDA")
    private String otroPerdida;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private Double cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTADO")
    private short estado;
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
    @JoinColumn(name = "UNIDAD_MEDIDA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UnidadMedida unidadMedidaId;
    @JoinColumn(name = "TIPO_PERDIDA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoPerdida;
    @JoinColumn(name = "POLVORIN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppPolvorin polvorinId;
    @JoinColumn(name = "LIBRO_MES_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppLibroMes libroMesId;
    @JoinColumn(name = "EXPLOSIVO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppExplosivo explosivoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libroPerdidaId")
    private List<EppLibroAdjunto> eppLibroAdjuntoList;

    public EppLibroPerdida() {
    }

    public EppLibroPerdida(Long id) {
        this.id = id;
    }

    public EppLibroPerdida(Long id, Date fechaPerdida, Double cantidad, short estado, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaPerdida = fechaPerdida;
        this.cantidad = cantidad;
        this.estado = estado;
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

    public Date getFechaPerdida() {
        return fechaPerdida;
    }

    public void setFechaPerdida(Date fechaPerdida) {
        this.fechaPerdida = fechaPerdida;
    }

    public String getOtroPerdida() {
        return otroPerdida;
    }

    public void setOtroPerdida(String otroPerdida) {
        this.otroPerdida = otroPerdida;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
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

    public UnidadMedida getUnidadMedidaId() {
        return unidadMedidaId;
    }

    public void setUnidadMedidaId(UnidadMedida unidadMedidaId) {
        this.unidadMedidaId = unidadMedidaId;
    }

    public TipoExplosivoGt getTipoPerdida() {
        return tipoPerdida;
    }

    public void setTipoPerdida(TipoExplosivoGt tipoPerdida) {
        this.tipoPerdida = tipoPerdida;
    }

    public EppPolvorin getPolvorinId() {
        return polvorinId;
    }

    public void setPolvorinId(EppPolvorin polvorinId) {
        this.polvorinId = polvorinId;
    }

    public EppLibroMes getLibroMesId() {
        return libroMesId;
    }

    public void setLibroMesId(EppLibroMes libroMesId) {
        this.libroMesId = libroMesId;
    }

    public EppExplosivo getExplosivoId() {
        return explosivoId;
    }

    public void setExplosivoId(EppExplosivo explosivoId) {
        this.explosivoId = explosivoId;
    }

    @XmlTransient
    public List<EppLibroAdjunto> getEppLibroAdjuntoList() {
        return eppLibroAdjuntoList;
    }

    public void setEppLibroAdjuntoList(List<EppLibroAdjunto> eppLibroAdjuntoList) {
        this.eppLibroAdjuntoList = eppLibroAdjuntoList;
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
        if (!(object instanceof EppLibroPerdida)) {
            return false;
        }
        EppLibroPerdida other = (EppLibroPerdida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppLibroPerdida[ id=" + id + " ]";
    }
    
}
