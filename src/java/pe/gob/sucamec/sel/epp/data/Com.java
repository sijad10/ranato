/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.epp.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import pe.gob.sucamec.sistemabase.data.SbDepartamento;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author rmoscoso
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_COM", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Com.findAll", query = "SELECT c FROM Com c"),
    @NamedQuery(name = "Com.findById", query = "SELECT c FROM Com c WHERE c.id = :id"),
    @NamedQuery(name = "Com.findByNroCom", query = "SELECT c FROM Com c WHERE c.nroCom = :nroCom"),
    @NamedQuery(name = "Com.findByFechaVigencia", query = "SELECT c FROM Com c WHERE c.fechaVigencia = :fechaVigencia"),
    @NamedQuery(name = "Com.findByAudLogin", query = "SELECT c FROM Com c WHERE c.audLogin = :audLogin"),
    @NamedQuery(name = "Com.findByAudNumIp", query = "SELECT c FROM Com c WHERE c.audNumIp = :audNumIp")})
public class Com implements Serializable {

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
    @Column(name = "FECHA_VIGENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVigencia;
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
    private List<LugarUso> lugarUsoList;
    @OneToMany(mappedBy = "comId")
    private List<Com> comList;
    @JoinColumn(name = "COM_ID", referencedColumnName = "ID")
    @ManyToOne
    private Com comId;
    @JoinColumn(name = "DIRECCION_REGIONAL", referencedColumnName = "ID")
    @ManyToOne
    private SbDepartamento direccionRegional;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona empresaId;
    @JoinColumn(name = "ENTIDAD_EMISORA", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivo entidadEmisora;
    @JoinColumn(name = "TIPO_COM", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivo tipoCom;
    @JoinColumn(name = "TIPO_ESTADO_2DO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivo tipoEstado2do;
    @JoinColumn(name = "TIPO_ESTADO_1ER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivo tipoEstado1er;
    @OneToMany(mappedBy = "comId")
    private List<Polvorin> polvorinList;

    public Com() {
    }

    public Com(Long id) {
        this.id = id;
    }

    public Com(Long id, String nroCom, Date fechaVigencia, String audLogin, String audNumIp) {
        this.id = id;
        this.nroCom = nroCom;
        this.fechaVigencia = fechaVigencia;
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

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
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
    public List<LugarUso> getLugarUsoList() {
        return lugarUsoList;
    }

    public void setLugarUsoList(List<LugarUso> lugarUsoList) {
        this.lugarUsoList = lugarUsoList;
    }

    @XmlTransient
    public List<Com> getComList() {
        return comList;
    }

    public void setComList(List<Com> comList) {
        this.comList = comList;
    }

    public Com getComId() {
        return comId;
    }

    public void setComId(Com comId) {
        this.comId = comId;
    }

    public SbDepartamento getDireccionRegional() {
        return direccionRegional;
    }

    public void setDireccionRegional(SbDepartamento direccionRegional) {
        this.direccionRegional = direccionRegional;
    }

    public SbPersona getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersona empresaId) {
        this.empresaId = empresaId;
    }

    public TipoExplosivo getEntidadEmisora() {
        return entidadEmisora;
    }

    public void setEntidadEmisora(TipoExplosivo entidadEmisora) {
        this.entidadEmisora = entidadEmisora;
    }

    public TipoExplosivo getTipoCom() {
        return tipoCom;
    }

    public void setTipoCom(TipoExplosivo tipoCom) {
        this.tipoCom = tipoCom;
    }

    public TipoExplosivo getTipoEstado2do() {
        return tipoEstado2do;
    }

    public void setTipoEstado2do(TipoExplosivo tipoEstado2do) {
        this.tipoEstado2do = tipoEstado2do;
    }

    public TipoExplosivo getTipoEstado1er() {
        return tipoEstado1er;
    }

    public void setTipoEstado1er(TipoExplosivo tipoEstado1er) {
        this.tipoEstado1er = tipoEstado1er;
    }

    @XmlTransient
    public List<Polvorin> getPolvorinList() {
        return polvorinList;
    }

    public void setPolvorinList(List<Polvorin> polvorinList) {
        this.polvorinList = polvorinList;
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
        if (!(object instanceof Com)) {
            return false;
        }
        Com other = (Com) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.epp.data.Com[ id=" + id + " ]";
    }
    
}
