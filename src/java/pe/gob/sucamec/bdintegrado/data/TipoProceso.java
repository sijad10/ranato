/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author rfernandezv
 */
@Entity
@Table(name = "TIPO_PROCESO", catalog = "", schema = "TRAMDOC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoProceso.findAll", query = "SELECT t FROM TipoProceso t"),
    @NamedQuery(name = "TipoProceso.findByIdTipoProceso", query = "SELECT t FROM TipoProceso t WHERE t.idTipoProceso = :idTipoProceso"),
    @NamedQuery(name = "TipoProceso.findByNombre", query = "SELECT t FROM TipoProceso t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoProceso.findByEstado", query = "SELECT t FROM TipoProceso t WHERE t.estado = :estado"),
    @NamedQuery(name = "TipoProceso.findByFechaCreacion", query = "SELECT t FROM TipoProceso t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "TipoProceso.findByCodigo", query = "SELECT t FROM TipoProceso t WHERE t.codigo = :codigo"),
    @NamedQuery(name = "TipoProceso.findByAlerta", query = "SELECT t FROM TipoProceso t WHERE t.alerta = :alerta")})
public class TipoProceso implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPO_PROCESO")
    private BigDecimal idTipoProceso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "ESTADO")
    private Character estado;
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 20)
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "ALERTA")
    private Character alerta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoProceso")
    private List<Proceso> procesoList;

    public TipoProceso() {
    }

    public TipoProceso(BigDecimal idTipoProceso) {
        this.idTipoProceso = idTipoProceso;
    }

    public TipoProceso(BigDecimal idTipoProceso, String nombre) {
        this.idTipoProceso = idTipoProceso;
        this.nombre = nombre;
    }

    public BigDecimal getIdTipoProceso() {
        return idTipoProceso;
    }

    public void setIdTipoProceso(BigDecimal idTipoProceso) {
        this.idTipoProceso = idTipoProceso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Character getAlerta() {
        return alerta;
    }

    public void setAlerta(Character alerta) {
        this.alerta = alerta;
    }

    @XmlTransient
    public List<Proceso> getProcesoList() {
        return procesoList;
    }

    public void setProcesoList(List<Proceso> procesoList) {
        this.procesoList = procesoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoProceso != null ? idTipoProceso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoProceso)) {
            return false;
        }
        TipoProceso other = (TipoProceso) object;
        if ((this.idTipoProceso == null && other.idTipoProceso != null) || (this.idTipoProceso != null && !this.idTipoProceso.equals(other.idTipoProceso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.TipoProceso[ idTipoProceso=" + idTipoProceso + " ]";
    }
    
}
