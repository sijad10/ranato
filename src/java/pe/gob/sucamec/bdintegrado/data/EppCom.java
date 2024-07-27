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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.SbDepartamento;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_COM", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppCom.findAll", query = "SELECT e FROM EppCom e"),
    @NamedQuery(name = "EppCom.findById", query = "SELECT e FROM EppCom e WHERE e.id = :id"),
    @NamedQuery(name = "EppCom.findByNroCom", query = "SELECT e FROM EppCom e WHERE e.nroCom = :nroCom"),
    @NamedQuery(name = "EppCom.findByActivo", query = "SELECT e FROM EppCom e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppCom.findByAudLogin", query = "SELECT e FROM EppCom e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppCom.findByAudNumIp", query = "SELECT e FROM EppCom e WHERE e.audNumIp = :audNumIp")})
public class EppCom implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_COM")
    @SequenceGenerator(name = "SEQ_EPP_COM", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_COM", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 120)
    @Column(name = "NRO_COM")
    private String nroCom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
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
    @JoinTable(schema="BDINTEGRADO", name = "EPP_DETALLE_COM_LUGAR", joinColumns = {
        @JoinColumn(name = "COM_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "LUGAR_USO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppLugarUso> eppLugarUsoList;
    @JoinColumn(name = "ENTIDAD_EMISORA", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt entidadEmisora;
    @JoinColumn(name = "TIPO_ESTADO_1ER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoEstado1er;
    @JoinColumn(name = "TIPO_COM", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoCom;
    @JoinColumn(name = "TIPO_ESTADO_2DO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoEstado2do;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona empresaId;
    @JoinColumn(name = "DIRECCION_REGIONAL", referencedColumnName = "ID")
    @ManyToOne
    private SbDepartamento direccionRegional;
    @OneToMany(mappedBy = "comId")
    private List<EppCom> eppComList;
    @JoinColumn(name = "COM_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppCom comId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comId")
    private List<EppDetalleCom> eppDetalleComList;
    @OneToMany(mappedBy = "comId")
    private List<EppRegistro> eppRegistroList;
    @OneToMany(mappedBy = "comId")
    private List<EppPolvorin> eppPolvorinList;

    public EppCom() {
    }

    public EppCom(Long id) {
        this.id = id;
    }

    public EppCom(Long id, String nroCom, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroCom = nroCom;
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

    public String getNroCom() {
        return nroCom;
    }

    public void setNroCom(String nroCom) {
        this.nroCom = nroCom;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
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

    @XmlTransient
    public List<EppLugarUso> getEppLugarUsoList() {
        return eppLugarUsoList;
    }

    public void setEppLugarUsoList(List<EppLugarUso> eppLugarUsoList) {
        this.eppLugarUsoList = eppLugarUsoList;
    }

    public TipoExplosivoGt getEntidadEmisora() {
        return entidadEmisora;
    }

    public void setEntidadEmisora(TipoExplosivoGt entidadEmisora) {
        this.entidadEmisora = entidadEmisora;
    }

    public TipoExplosivoGt getTipoEstado1er() {
        return tipoEstado1er;
    }

    public void setTipoEstado1er(TipoExplosivoGt tipoEstado1er) {
        this.tipoEstado1er = tipoEstado1er;
    }

    public TipoExplosivoGt getTipoCom() {
        return tipoCom;
    }

    public void setTipoCom(TipoExplosivoGt tipoCom) {
        this.tipoCom = tipoCom;
    }

    public TipoExplosivoGt getTipoEstado2do() {
        return tipoEstado2do;
    }

    public void setTipoEstado2do(TipoExplosivoGt tipoEstado2do) {
        this.tipoEstado2do = tipoEstado2do;
    }

    public SbPersona getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersona empresaId) {
        this.empresaId = empresaId;
    }

    public SbDepartamento getDireccionRegional() {
        return direccionRegional;
    }

    public void setDireccionRegional(SbDepartamento direccionRegional) {
        this.direccionRegional = direccionRegional;
    }

    @XmlTransient
    public List<EppCom> getEppComList() {
        return eppComList;
    }

    public void setEppComList(List<EppCom> eppComList) {
        this.eppComList = eppComList;
    }

    public EppCom getComId() {
        return comId;
    }

    public void setComId(EppCom comId) {
        this.comId = comId;
    }

    @XmlTransient
    public List<EppDetalleCom> getEppDetalleComList() {
        return eppDetalleComList;
    }

    public void setEppDetalleComList(List<EppDetalleCom> eppDetalleComList) {
        this.eppDetalleComList = eppDetalleComList;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    @XmlTransient
    public List<EppPolvorin> getEppPolvorinList() {
        return eppPolvorinList;
    }

    public void setEppPolvorinList(List<EppPolvorin> eppPolvorinList) {
        this.eppPolvorinList = eppPolvorinList;
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
        if (!(object instanceof EppCom)) {
            return false;
        }
        EppCom other = (EppCom) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppCom[ id=" + id + " ]";
    }
    
}
