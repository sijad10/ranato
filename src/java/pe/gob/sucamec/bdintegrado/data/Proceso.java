/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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

/**
 *
 * @author mhermoza
 */
@Entity
@Table(name = "PROCESO", catalog = "", schema = "TRAMDOC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proceso.findAll", query = "SELECT p FROM Proceso p")
    ,
    @NamedQuery(name = "Proceso.findByIdProceso", query = "SELECT p FROM Proceso p WHERE p.idProceso = :idProceso")
    ,
    @NamedQuery(name = "Proceso.findByNombre", query = "SELECT p FROM Proceso p WHERE p.nombre = :nombre")
    ,
    @NamedQuery(name = "Proceso.findByDescripcion", query = "SELECT p FROM Proceso p WHERE p.descripcion = :descripcion")
    ,
    @NamedQuery(name = "Proceso.findByFechaCreacion", query = "SELECT p FROM Proceso p WHERE p.fechaCreacion = :fechaCreacion")
    ,
    @NamedQuery(name = "Proceso.findByEstado", query = "SELECT p FROM Proceso p WHERE p.estado = :estado")
    ,
    @NamedQuery(name = "Proceso.findByNombreIntalio", query = "SELECT p FROM Proceso p WHERE p.nombreIntalio = :nombreIntalio")
    ,
    @NamedQuery(name = "Proceso.findByPlazo", query = "SELECT p FROM Proceso p WHERE p.plazo = :plazo")
    ,
    @NamedQuery(name = "Proceso.findByCliente", query = "SELECT p FROM Proceso p WHERE p.cliente = :cliente")
    ,
    @NamedQuery(name = "Proceso.findByTipoConfidencialidad", query = "SELECT p FROM Proceso p WHERE p.tipoConfidencialidad = :tipoConfidencialidad")})
public class Proceso implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PROCESO")
    private Long idProceso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "ESTADO")
    private Character estado;
    @Size(max = 100)
    @Column(name = "NOMBRE_INTALIO")
    private String nombreIntalio;
    @Column(name = "PLAZO")
    private BigInteger plazo;
    @Column(name = "CLIENTE")
    private Short cliente;
    @Size(max = 50)
    @Column(name = "TIPO_CONFIDENCIALIDAD")
    private String tipoConfidencialidad;
    @Column(name = "CREADOR_RESPONSABLE")
    private Short creadorResponsable;
    @Column(name = "CON_APROBACION")
    private Short conAprobacion;
    @JoinTable(schema = "BDINTEGRADO", name = "TRAMDOC", joinColumns = {
        @JoinColumn(name = "PROCESO", referencedColumnName = "ID_PROCESO")}, inverseJoinColumns = {
        @JoinColumn(name = "USUARIO", referencedColumnName = "ID_USUARIO")})
    @ManyToMany
    private List<Usuario> usuarioList;
    @ManyToMany(mappedBy = "procesoList1")
    private List<Usuario> usuarioList1;
    @OneToMany(mappedBy = "idProceso")
    private List<Traza> trazaList;
    @JoinColumn(name = "ID_TIPO_PROCESO", referencedColumnName = "ID_TIPO_PROCESO")
    @ManyToOne(optional = false)
    private TipoProceso idTipoProceso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProceso")
    private List<Expediente> expedienteList;

    public Proceso() {
    }

    public Proceso(Long idProceso) {
        this.idProceso = idProceso;
    }

    public Proceso(Long idProceso, String nombre, Date fechaCreacion) {
        this.idProceso = idProceso;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Long idProceso) {
        this.idProceso = idProceso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public String getNombreIntalio() {
        return nombreIntalio;
    }

    public void setNombreIntalio(String nombreIntalio) {
        this.nombreIntalio = nombreIntalio;
    }

    public BigInteger getPlazo() {
        return plazo;
    }

    public void setPlazo(BigInteger plazo) {
        this.plazo = plazo;
    }

    public Short getCliente() {
        return cliente;
    }

    public void setCliente(Short cliente) {
        this.cliente = cliente;
    }

    public String getTipoConfidencialidad() {
        return tipoConfidencialidad;
    }

    public void setTipoConfidencialidad(String tipoConfidencialidad) {
        this.tipoConfidencialidad = tipoConfidencialidad;
    }

    @XmlTransient
    public List<Expediente> getExpedienteList() {
        return expedienteList;
    }

    public void setExpedienteList(List<Expediente> expedienteList) {
        this.expedienteList = expedienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProceso != null ? idProceso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proceso)) {
            return false;
        }
        Proceso other = (Proceso) object;
        if ((this.idProceso == null && other.idProceso != null) || (this.idProceso != null && !this.idProceso.equals(other.idProceso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.Proceso[ idProceso=" + idProceso + " ]";
    }

    public Short getCreadorResponsable() {
        return creadorResponsable;
    }

    public void setCreadorResponsable(Short creadorResponsable) {
        this.creadorResponsable = creadorResponsable;
    }

    public Short getConAprobacion() {
        return conAprobacion;
    }

    public void setConAprobacion(Short conAprobacion) {
        this.conAprobacion = conAprobacion;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList1() {
        return usuarioList1;
    }

    public void setUsuarioList1(List<Usuario> usuarioList1) {
        this.usuarioList1 = usuarioList1;
    }

    @XmlTransient
    public List<Traza> getTrazaList() {
        return trazaList;
    }

    public void setTrazaList(List<Traza> trazaList) {
        this.trazaList = trazaList;
    }

    public TipoProceso getIdTipoProceso() {
        return idTipoProceso;
    }

    public void setIdTipoProceso(TipoProceso idTipoProceso) {
        this.idTipoProceso = idTipoProceso;
    }
}
