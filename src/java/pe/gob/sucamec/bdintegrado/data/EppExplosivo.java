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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_EXPLOSIVO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppExplosivo.findAll", query = "SELECT e FROM EppExplosivo e"),
    @NamedQuery(name = "EppExplosivo.findById", query = "SELECT e FROM EppExplosivo e WHERE e.id = :id"),
    @NamedQuery(name = "EppExplosivo.findByNombre", query = "SELECT e FROM EppExplosivo e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EppExplosivo.findByAbreviatura", query = "SELECT e FROM EppExplosivo e WHERE e.abreviatura = :abreviatura"),
    @NamedQuery(name = "EppExplosivo.findByActivo", query = "SELECT e FROM EppExplosivo e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppExplosivo.findByAudLogin", query = "SELECT e FROM EppExplosivo e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppExplosivo.findByAudNumIp", query = "SELECT e FROM EppExplosivo e WHERE e.audNumIp = :audNumIp"),
    @NamedQuery(name = "EppExplosivo.findByTipoExplosivoId", query = "SELECT e FROM EppExplosivo e WHERE e.tipoExplosivoId = :tipoExplosivoId")})
public class EppExplosivo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_EXPLOSIVO")
    @SequenceGenerator(name = "SEQ_EPP_EXPLOSIVO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_EXPLOSIVO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
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
    @Column(name = "TIPO_EXPLOSIVO_ID")
    private Long tipoExplosivoId;
    @JoinTable(schema = "BDINTEGRADO", name = "EPP_EXPLOSIVO_UNIDAD", joinColumns = {
        @JoinColumn(name = "EXPLOSIVO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "UNIDAD_MEDIDA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<UnidadMedida> unidadMedidaList;
    @ManyToMany(mappedBy = "eppExplosivoList")
    private List<EppRegistro> eppRegistroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "explosivoId")
    private List<EppDetalleIntSal> eppDetalleIntSalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "explosivoId")
    private List<EppLibroPerdida> eppLibroPerdidaList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoId;
    /*@OneToMany(mappedBy = "explosivoId")
    private List<EppExplosivo> eppExplosivoList;*/
    @JoinColumn(name = "EXPLOSIVO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppExplosivo explosivoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "explosivoId")
    private List<EppExplosivoSolicitado> eppExplosivoSolicitadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "explosivoId")
    private List<EppDetalleCom> eppDetalleComList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "explosivoId")
    private List<EppImpExpExplosivo> eppImpExpExplosivoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "explosivoId")
    private List<EppPlantaExplosivo> eppPlantaExplosivoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "explosivoId")
    private List<EppLibroSaldo> eppLibroSaldoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "explosivoId")
    private List<EppLibroUsoDiario> eppLibroUsoDiarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "explosivoId")
    private List<EppDetalleAutUso> eppDetalleAutUsoList;
    @JoinTable(schema = "BDINTEGRADO", name = "EPP_EXPLOSIVO_COMPATIBLE", joinColumns = {
        @JoinColumn(name = "EXPLOSIVO1_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "EXPLOSIVO2_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppExplosivo> eppExplosivoList;

    @XmlTransient
    public List<EppExplosivo> getEppExplosivoList() {
        return eppExplosivoList;
    }

    public void setEppExplosivoList(List<EppExplosivo> eppExplosivoList) {
        this.eppExplosivoList = eppExplosivoList;
    }

    public EppExplosivo() {
    }

    public EppExplosivo(Long id) {
        this.id = id;
    }

    public EppExplosivo(Long id, String nombre, String abreviatura, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
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

    public Long getTipoExplosivoId() {
        return tipoExplosivoId;
    }

    public void setTipoExplosivoId(Long tipoExplosivoId) {
        this.tipoExplosivoId = tipoExplosivoId;
    }

    @XmlTransient
    public List<UnidadMedida> getUnidadMedidaList() {
        return unidadMedidaList;
    }

    public void setUnidadMedidaList(List<UnidadMedida> unidadMedidaList) {
        this.unidadMedidaList = unidadMedidaList;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    @XmlTransient
    public List<EppDetalleIntSal> getEppDetalleIntSalList() {
        return eppDetalleIntSalList;
    }

    public void setEppDetalleIntSalList(List<EppDetalleIntSal> eppDetalleIntSalList) {
        this.eppDetalleIntSalList = eppDetalleIntSalList;
    }

    @XmlTransient
    public List<EppLibroPerdida> getEppLibroPerdidaList() {
        return eppLibroPerdidaList;
    }

    public void setEppLibroPerdidaList(List<EppLibroPerdida> eppLibroPerdidaList) {
        this.eppLibroPerdidaList = eppLibroPerdidaList;
    }

    public TipoExplosivoGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoExplosivoGt tipoId) {
        this.tipoId = tipoId;
    }

    /*
    @XmlTransient
    public List<EppExplosivo> getEppExplosivoList() {
        return eppExplosivoList;
    }

    public void setEppExplosivoList(List<EppExplosivo> eppExplosivoList) {
        this.eppExplosivoList = eppExplosivoList;
    }*/
    public EppExplosivo getExplosivoId() {
        return explosivoId;
    }

    public void setExplosivoId(EppExplosivo explosivoId) {
        this.explosivoId = explosivoId;
    }

    @XmlTransient
    public List<EppExplosivoSolicitado> getEppExplosivoSolicitadoList() {
        return eppExplosivoSolicitadoList;
    }

    public void setEppExplosivoSolicitadoList(List<EppExplosivoSolicitado> eppExplosivoSolicitadoList) {
        this.eppExplosivoSolicitadoList = eppExplosivoSolicitadoList;
    }

    @XmlTransient
    public List<EppDetalleCom> getEppDetalleComList() {
        return eppDetalleComList;
    }

    public void setEppDetalleComList(List<EppDetalleCom> eppDetalleComList) {
        this.eppDetalleComList = eppDetalleComList;
    }

    @XmlTransient
    public List<EppImpExpExplosivo> getEppImpExpExplosivoList() {
        return eppImpExpExplosivoList;
    }

    public void setEppImpExpExplosivoList(List<EppImpExpExplosivo> eppImpExpExplosivoList) {
        this.eppImpExpExplosivoList = eppImpExpExplosivoList;
    }

    @XmlTransient
    public List<EppPlantaExplosivo> getEppPlantaExplosivoList() {
        return eppPlantaExplosivoList;
    }

    public void setEppPlantaExplosivoList(List<EppPlantaExplosivo> eppPlantaExplosivoList) {
        this.eppPlantaExplosivoList = eppPlantaExplosivoList;
    }

    @XmlTransient
    public List<EppLibroSaldo> getEppLibroSaldoList() {
        return eppLibroSaldoList;
    }

    public void setEppLibroSaldoList(List<EppLibroSaldo> eppLibroSaldoList) {
        this.eppLibroSaldoList = eppLibroSaldoList;
    }

    @XmlTransient
    public List<EppLibroUsoDiario> getEppLibroUsoDiarioList() {
        return eppLibroUsoDiarioList;
    }

    public void setEppLibroUsoDiarioList(List<EppLibroUsoDiario> eppLibroUsoDiarioList) {
        this.eppLibroUsoDiarioList = eppLibroUsoDiarioList;
    }

    @XmlTransient
    public List<EppDetalleAutUso> getEppDetalleAutUsoList() {
        return eppDetalleAutUsoList;
    }

    public void setEppDetalleAutUsoList(List<EppDetalleAutUso> eppDetalleAutUsoList) {
        this.eppDetalleAutUsoList = eppDetalleAutUsoList;
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
        if (!(object instanceof EppExplosivo)) {
            return false;
        }
        EppExplosivo other = (EppExplosivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppExplosivo[ id=" + id + " ]";
    }

}
