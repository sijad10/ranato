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
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_PIROTECNICO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppPirotecnico.findAll", query = "SELECT e FROM EppPirotecnico e"),
    @NamedQuery(name = "EppPirotecnico.findById", query = "SELECT e FROM EppPirotecnico e WHERE e.id = :id"),
    @NamedQuery(name = "EppPirotecnico.findByNombre", query = "SELECT e FROM EppPirotecnico e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EppPirotecnico.findByCodigo", query = "SELECT e FROM EppPirotecnico e WHERE e.codigo = :codigo"),
    @NamedQuery(name = "EppPirotecnico.findByAbreviatura", query = "SELECT e FROM EppPirotecnico e WHERE e.abreviatura = :abreviatura"),
    @NamedQuery(name = "EppPirotecnico.findByPesoCargapirica", query = "SELECT e FROM EppPirotecnico e WHERE e.pesoCargapirica = :pesoCargapirica"),
    @NamedQuery(name = "EppPirotecnico.findByPesoBruto", query = "SELECT e FROM EppPirotecnico e WHERE e.pesoBruto = :pesoBruto"),
    @NamedQuery(name = "EppPirotecnico.findByActivo", query = "SELECT e FROM EppPirotecnico e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppPirotecnico.findByAudLogin", query = "SELECT e FROM EppPirotecnico e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppPirotecnico.findByAudNumIp", query = "SELECT e FROM EppPirotecnico e WHERE e.audNumIp = :audNumIp")})
public class EppPirotecnico implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_PIROTECNICO")
    @SequenceGenerator(name = "SEQ_EPP_PIROTECNICO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_PIROTECNICO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 30)
    @Column(name = "CODIGO")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PESO_CARGAPIRICA")
    private Double pesoCargapirica;
    @Column(name = "PESO_BRUTO")
    private Double pesoBruto;
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
    @JoinTable(schema="BDINTEGRADO", name = "EPP_PIROEFECTO", joinColumns = {
        @JoinColumn(name = "PIROTECNICO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "EFECTO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<TipoExplosivoGt> tipoExplosivoList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_PIROINCOMPATIBLE", joinColumns = {
        @JoinColumn(name = "PIROTECNICO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PIROTECNICOINCOM_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppPirotecnico> eppPirotecnicoList;
    @ManyToMany(mappedBy = "eppPirotecnicoList")
    private List<EppPirotecnico> eppPirotecnicoList1;
    @JoinColumn(name = "TIPO_PRODPIRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoProdpiroId;
    @JoinColumn(name = "DIVGRUCOM_ONU_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt divgrucomOnuId;
    @JoinColumn(name = "NUMERO_ONU_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt numeroOnuId;
    @JoinColumn(name = "CLASIFPIRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppClasifPirotecnico clasifpiroId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pirotecnicoId")
    private List<EppNombrecomercial> eppNombrecomercialList;

    public EppPirotecnico() {
    }

    public EppPirotecnico(Long id) {
        this.id = id;
    }

    public EppPirotecnico(Long id, String nombre, String abreviatura, short activo, String audLogin, String audNumIp) {
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Double getPesoCargapirica() {
        return pesoCargapirica;
    }

    public void setPesoCargapirica(Double pesoCargapirica) {
        this.pesoCargapirica = pesoCargapirica;
    }

    public Double getPesoBruto() {
        return pesoBruto;
    }

    public void setPesoBruto(Double pesoBruto) {
        this.pesoBruto = pesoBruto;
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
    public List<TipoExplosivoGt> getTipoExplosivoList() {
        return tipoExplosivoList;
    }

    public void setTipoExplosivoList(List<TipoExplosivoGt> tipoExplosivoList) {
        this.tipoExplosivoList = tipoExplosivoList;
    }

    @XmlTransient
    public List<EppPirotecnico> getEppPirotecnicoList() {
        return eppPirotecnicoList;
    }

    public void setEppPirotecnicoList(List<EppPirotecnico> eppPirotecnicoList) {
        this.eppPirotecnicoList = eppPirotecnicoList;
    }

    @XmlTransient
    public List<EppPirotecnico> getEppPirotecnicoList1() {
        return eppPirotecnicoList1;
    }

    public void setEppPirotecnicoList1(List<EppPirotecnico> eppPirotecnicoList1) {
        this.eppPirotecnicoList1 = eppPirotecnicoList1;
    }

    public TipoExplosivoGt getTipoProdpiroId() {
        return tipoProdpiroId;
    }

    public void setTipoProdpiroId(TipoExplosivoGt tipoProdpiroId) {
        this.tipoProdpiroId = tipoProdpiroId;
    }

    public TipoExplosivoGt getDivgrucomOnuId() {
        return divgrucomOnuId;
    }

    public void setDivgrucomOnuId(TipoExplosivoGt divgrucomOnuId) {
        this.divgrucomOnuId = divgrucomOnuId;
    }

    public TipoExplosivoGt getNumeroOnuId() {
        return numeroOnuId;
    }

    public void setNumeroOnuId(TipoExplosivoGt numeroOnuId) {
        this.numeroOnuId = numeroOnuId;
    }

    public EppClasifPirotecnico getClasifpiroId() {
        return clasifpiroId;
    }

    public void setClasifpiroId(EppClasifPirotecnico clasifpiroId) {
        this.clasifpiroId = clasifpiroId;
    }

    @XmlTransient
    public List<EppNombrecomercial> getEppNombrecomercialList() {
        return eppNombrecomercialList;
    }

    public void setEppNombrecomercialList(List<EppNombrecomercial> eppNombrecomercialList) {
        this.eppNombrecomercialList = eppNombrecomercialList;
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
        if (!(object instanceof EppPirotecnico)) {
            return false;
        }
        EppPirotecnico other = (EppPirotecnico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppPirotecnico[ id=" + id + " ]";
    }
    
}
