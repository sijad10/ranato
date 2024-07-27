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
@Table(name = "EPP_DEPOSITO_AMBIENTES", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppDepositoAmbientes.findAll", query = "SELECT e FROM EppDepositoAmbientes e"),
    @NamedQuery(name = "EppDepositoAmbientes.findById", query = "SELECT e FROM EppDepositoAmbientes e WHERE e.id = :id"),
    @NamedQuery(name = "EppDepositoAmbientes.findByReferencia", query = "SELECT e FROM EppDepositoAmbientes e WHERE e.referencia = :referencia"),
    @NamedQuery(name = "EppDepositoAmbientes.findByArea", query = "SELECT e FROM EppDepositoAmbientes e WHERE e.area = :area"),
    @NamedQuery(name = "EppDepositoAmbientes.findByAltura", query = "SELECT e FROM EppDepositoAmbientes e WHERE e.altura = :altura"),
    @NamedQuery(name = "EppDepositoAmbientes.findByVolumen", query = "SELECT e FROM EppDepositoAmbientes e WHERE e.volumen = :volumen"),
    @NamedQuery(name = "EppDepositoAmbientes.findByCapacidad", query = "SELECT e FROM EppDepositoAmbientes e WHERE e.capacidad = :capacidad"),
    @NamedQuery(name = "EppDepositoAmbientes.findByActivo", query = "SELECT e FROM EppDepositoAmbientes e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppDepositoAmbientes.findByAudLogin", query = "SELECT e FROM EppDepositoAmbientes e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppDepositoAmbientes.findByAudNumIp", query = "SELECT e FROM EppDepositoAmbientes e WHERE e.audNumIp = :audNumIp")})
public class EppDepositoAmbientes implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_DEPOSITO_AMBIENTES")
    @SequenceGenerator(name = "SEQ_EPP_DEPOSITO_AMBIENTES", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_DEPOSITO_AMBIENTES", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "REFERENCIA")
    private String referencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "AREA")
    private Double area;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ALTURA")
    private Double altura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VOLUMEN")
    private Double volumen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CAPACIDAD")
    private Double capacidad;
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
    @JoinTable(schema="BDINTEGRADO", name = "EPP_DEPAMB_CARNE", joinColumns = {
        @JoinColumn(name = "DEPOSITO_AMBIENTES_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CARNE_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppCarne> eppCarneList;
    @JoinColumn(name = "TIPO_MATERIAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoMaterialId;
    @JoinColumn(name = "DEPOSITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppTallerdeposito depositoId;
////    @JoinColumn(name = "ADMINISTRADOR_ID", referencedColumnName = "ID")
//    @ManyToOne(optional = false)
   // private EppCarne administradorId;
    @OneToMany(mappedBy = "depositoAmbientesId")
    private List<EppPiroproyectado> eppPiroproyectadoList;

    public EppDepositoAmbientes() {
    }

    public EppDepositoAmbientes(Long id) {
        this.id = id;
    }

    public EppDepositoAmbientes(Long id, String referencia, Double area, Double altura, Double volumen, Double capacidad, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.referencia = referencia;
        this.area = area;
        this.altura = altura;
        this.volumen = volumen;
        this.capacidad = capacidad;
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

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getVolumen() {
        return volumen;
    }

    public void setVolumen(Double volumen) {
        this.volumen = volumen;
    }

    public Double getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Double capacidad) {
        this.capacidad = capacidad;
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
    public List<EppCarne> getEppCarneList() {
        return eppCarneList;
    }

    public void setEppCarneList(List<EppCarne> eppCarneList) {
        this.eppCarneList = eppCarneList;
    }

    public TipoExplosivoGt getTipoMaterialId() {
        return tipoMaterialId;
    }

    public void setTipoMaterialId(TipoExplosivoGt tipoMaterialId) {
        this.tipoMaterialId = tipoMaterialId;
    }

    public EppTallerdeposito getDepositoId() {
        return depositoId;
    }

    public void setDepositoId(EppTallerdeposito depositoId) {
        this.depositoId = depositoId;
    }

//    public EppCarne getAdministradorId() {
//        return administradorId;
//    }
//
//    public void setAdministradorId(EppCarne administradorId) {
//        this.administradorId = administradorId;
//    }

    @XmlTransient
    public List<EppPiroproyectado> getEppPiroproyectadoList() {
        return eppPiroproyectadoList;
    }

    public void setEppPiroproyectadoList(List<EppPiroproyectado> eppPiroproyectadoList) {
        this.eppPiroproyectadoList = eppPiroproyectadoList;
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
        if (!(object instanceof EppDepositoAmbientes)) {
            return false;
        }
        EppDepositoAmbientes other = (EppDepositoAmbientes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppDepositoAmbientes[ id=" + id + " ]";
    }
    
}
