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
@Table(name = "EPP_RES_INTERSALPIRO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppResIntersalpiro.findAll", query = "SELECT e FROM EppResIntersalpiro e"),
    @NamedQuery(name = "EppResIntersalpiro.findById", query = "SELECT e FROM EppResIntersalpiro e WHERE e.id = :id"),
    @NamedQuery(name = "EppResIntersalpiro.findByCantidad", query = "SELECT e FROM EppResIntersalpiro e WHERE e.cantidad = :cantidad"),
    @NamedQuery(name = "EppResIntersalpiro.findByActivo", query = "SELECT e FROM EppResIntersalpiro e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppResIntersalpiro.findByAudLogin", query = "SELECT e FROM EppResIntersalpiro e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppResIntersalpiro.findByAudNumIp", query = "SELECT e FROM EppResIntersalpiro e WHERE e.audNumIp = :audNumIp")})
public class EppResIntersalpiro implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_RES_INTERSALPIRO")
    @SequenceGenerator(name = "SEQ_EPP_RES_INTERSALPIRO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_RES_INTERSALPIRO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private Double cantidad;
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
    @JoinColumn(name = "RESOLUCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppResolucion resolucionId;
    @JoinColumn(name = "INTERSAL_PIRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppIntsalPirotecnico intersalPiroId;

    public EppResIntersalpiro() {
    }

    public EppResIntersalpiro(Long id) {
        this.id = id;
    }

    public EppResIntersalpiro(Long id, Double cantidad, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.cantidad = cantidad;
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

    public EppResolucion getResolucionId() {
        return resolucionId;
    }

    public void setResolucionId(EppResolucion resolucionId) {
        this.resolucionId = resolucionId;
    }

    public EppIntsalPirotecnico getIntersalPiroId() {
        return intersalPiroId;
    }

    public void setIntersalPiroId(EppIntsalPirotecnico intersalPiroId) {
        this.intersalPiroId = intersalPiroId;
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
        if (!(object instanceof EppResIntersalpiro)) {
            return false;
        }
        EppResIntersalpiro other = (EppResIntersalpiro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppResIntersalpiro[ id=" + id + " ]";
    }
    
}
