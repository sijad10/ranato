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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_ESPECTACULO_PIROTEC", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppEspectaculoPirotec.findAll", query = "SELECT e FROM EppEspectaculoPirotec e"),
    @NamedQuery(name = "EppEspectaculoPirotec.findById", query = "SELECT e FROM EppEspectaculoPirotec e WHERE e.id = :id"),
    @NamedQuery(name = "EppEspectaculoPirotec.findByCantidad", query = "SELECT e FROM EppEspectaculoPirotec e WHERE e.cantidad = :cantidad"),
    @NamedQuery(name = "EppEspectaculoPirotec.findByPeso", query = "SELECT e FROM EppEspectaculoPirotec e WHERE e.peso = :peso"),
    @NamedQuery(name = "EppEspectaculoPirotec.findByActivo", query = "SELECT e FROM EppEspectaculoPirotec e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppEspectaculoPirotec.findByAudLogin", query = "SELECT e FROM EppEspectaculoPirotec e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppEspectaculoPirotec.findByAudNumIp", query = "SELECT e FROM EppEspectaculoPirotec e WHERE e.audNumIp = :audNumIp")})
public class EppEspectaculoPirotec implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_ESPECTACULO_PIROTEC")
    @SequenceGenerator(name = "SEQ_EPP_ESPECTACULO_PIROTEC", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_ESPECTACULO_PIROTEC", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private Double cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PESO")
    private Double peso;
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
    @JoinColumn(name = "ESPECTACULO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppEspectaculo espectaculoId;

    public EppEspectaculoPirotec() {
    }

    public EppEspectaculoPirotec(Long id) {
        this.id = id;
    }

    public EppEspectaculoPirotec(Long id, Double cantidad, Double peso, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.cantidad = cantidad;
        this.peso = peso;
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

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
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

    public EppEspectaculo getEspectaculoId() {
        return espectaculoId;
    }

    public void setEspectaculoId(EppEspectaculo espectaculoId) {
        this.espectaculoId = espectaculoId;
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
        if (!(object instanceof EppEspectaculoPirotec)) {
            return false;
        }
        EppEspectaculoPirotec other = (EppEspectaculoPirotec) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppEspectaculoPirotec[ id=" + id + " ]";
    }
    
}
