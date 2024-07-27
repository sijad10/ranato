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
import java.math.BigDecimal;
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
@Table(name = "EPP_INTSAL_PIROTECNICO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppIntsalPirotecnico.findAll", query = "SELECT e FROM EppIntsalPirotecnico e"),
    @NamedQuery(name = "EppIntsalPirotecnico.findById", query = "SELECT e FROM EppIntsalPirotecnico e WHERE e.id = :id"),
    @NamedQuery(name = "EppIntsalPirotecnico.findByCantidad", query = "SELECT e FROM EppIntsalPirotecnico e WHERE e.cantidad = :cantidad"),
    @NamedQuery(name = "EppIntsalPirotecnico.findByPacking", query = "SELECT e FROM EppIntsalPirotecnico e WHERE e.packing = :packing"),
    @NamedQuery(name = "EppIntsalPirotecnico.findByNroCajas", query = "SELECT e FROM EppIntsalPirotecnico e WHERE e.nroCajas = :nroCajas"),
    @NamedQuery(name = "EppIntsalPirotecnico.findByCodigo", query = "SELECT e FROM EppIntsalPirotecnico e WHERE e.codigo = :codigo"),
    @NamedQuery(name = "EppIntsalPirotecnico.findByActivo", query = "SELECT e FROM EppIntsalPirotecnico e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppIntsalPirotecnico.findByAudLogin", query = "SELECT e FROM EppIntsalPirotecnico e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppIntsalPirotecnico.findByAudNumIp", query = "SELECT e FROM EppIntsalPirotecnico e WHERE e.audNumIp = :audNumIp")})
public class EppIntsalPirotecnico implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_INTSAL_PIROTECNICO")
    @SequenceGenerator(name = "SEQ_EPP_INTSAL_PIROTECNICO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_INTSAL_PIROTECNICO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private Double cantidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "PACKING")
    private String packing;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_CAJAS")
    private int nroCajas;
    @Size(max = 30)
    @Column(name = "CODIGO")
    private String codigo;
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
    @JoinColumn(name = "PIRONOMCOM_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppNombrecomercial pironomcomId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppInternaSalida registroId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "intersalPiroId")
    private List<EppResIntersalpiro> eppResIntersalpiroList;

    public EppIntsalPirotecnico() {
    }

    public EppIntsalPirotecnico(Long id) {
        this.id = id;
    }

    public EppIntsalPirotecnico(Long id, Double cantidad, String packing, int nroCajas, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.cantidad = cantidad;
        this.packing = packing;
        this.nroCajas = nroCajas;
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

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public int getNroCajas() {
        return nroCajas;
    }

    public void setNroCajas(int nroCajas) {
        this.nroCajas = nroCajas;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public EppNombrecomercial getPironomcomId() {
        return pironomcomId;
    }

    public void setPironomcomId(EppNombrecomercial pironomcomId) {
        this.pironomcomId = pironomcomId;
    }

    public EppInternaSalida getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppInternaSalida registroId) {
        this.registroId = registroId;
    }

    @XmlTransient
    public List<EppResIntersalpiro> getEppResIntersalpiroList() {
        return eppResIntersalpiroList;
    }

    public void setEppResIntersalpiroList(List<EppResIntersalpiro> eppResIntersalpiroList) {
        this.eppResIntersalpiroList = eppResIntersalpiroList;
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
        if (!(object instanceof EppIntsalPirotecnico)) {
            return false;
        }
        EppIntsalPirotecnico other = (EppIntsalPirotecnico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppIntsalPirotecnico[ id=" + id + " ]";
    }
    
}
