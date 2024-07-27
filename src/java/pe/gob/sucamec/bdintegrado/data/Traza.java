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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
 * @author rfernandezv
 */
@Entity
@Table(name = "TRAZA", catalog = "", schema = "TRAMDOC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Traza.findAll", query = "SELECT t FROM Traza t"),
    @NamedQuery(name = "Traza.findByIdTraza", query = "SELECT t FROM Traza t WHERE t.idTraza = :idTraza"),
    @NamedQuery(name = "Traza.findByIdPaso", query = "SELECT t FROM Traza t WHERE t.idPaso = :idPaso"),
    @NamedQuery(name = "Traza.findByActual", query = "SELECT t FROM Traza t WHERE t.actual = :actual"),
    @NamedQuery(name = "Traza.findByFechaCreacion", query = "SELECT t FROM Traza t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Traza.findByAccion", query = "SELECT t FROM Traza t WHERE t.accion = :accion"),
    @NamedQuery(name = "Traza.findByObservacion", query = "SELECT t FROM Traza t WHERE t.observacion = :observacion"),
    @NamedQuery(name = "Traza.findByActividad", query = "SELECT t FROM Traza t WHERE t.actividad = :actividad"),
    @NamedQuery(name = "Traza.findByOrden", query = "SELECT t FROM Traza t WHERE t.orden = :orden"),
    @NamedQuery(name = "Traza.findByFechaLimite", query = "SELECT t FROM Traza t WHERE t.fechaLimite = :fechaLimite"),
    @NamedQuery(name = "Traza.findByObservacionAdministrado", query = "SELECT t FROM Traza t WHERE t.observacionAdministrado = :observacionAdministrado"),
    @NamedQuery(name = "Traza.findByEstado", query = "SELECT t FROM Traza t WHERE t.estado = :estado")})
public class Traza implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TRAZA")
    private BigDecimal idTraza;
    @Column(name = "ID_PASO")
    private BigInteger idPaso;
    @Column(name = "ACTUAL")
    private Short actual;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ACCION")
    private String accion;
    @Size(max = 4000)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Size(max = 50)
    @Column(name = "ACTIVIDAD")
    private String actividad;
    @Column(name = "ORDEN")
    private BigInteger orden;
    @Column(name = "FECHA_LIMITE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLimite;
    @Size(max = 4000)
    @Column(name = "OBSERVACION_ADMINISTRADO")
    private String observacionAdministrado;
    @Column(name = "ESTADO")
    private Character estado;
    @JoinColumn(name = "REMITENTE", referencedColumnName = "ID_USUARIO")
    @ManyToOne
    private Usuario remitente;
    @JoinColumn(name = "REEMPLAZADO", referencedColumnName = "ID_USUARIO")
    @ManyToOne
    private Usuario reemplazado;
    @OneToMany(mappedBy = "padre")
    private List<Traza> trazaList;
    @JoinColumn(name = "PADRE", referencedColumnName = "ID_TRAZA")
    @ManyToOne
    private Traza padre;
    @JoinColumn(name = "ID_PROCESO", referencedColumnName = "ID_PROCESO")
    @ManyToOne
    private Proceso idProceso;
//    @JoinColumn(name = "PRIORIDAD", referencedColumnName = "ID_PARAMETRO")
//    @ManyToOne
//    private Parametro prioridad;
    @JoinColumn(name = "ID_EXPEDIENTE", referencedColumnName = "ID_EXPEDIENTE")
    @ManyToOne(optional = false)
    private Expediente idExpediente;

    public Traza() {
    }

    public Traza(BigDecimal idTraza) {
        this.idTraza = idTraza;
    }

    public Traza(BigDecimal idTraza, Date fechaCreacion, String accion) {
        this.idTraza = idTraza;
        this.fechaCreacion = fechaCreacion;
        this.accion = accion;
    }

    public BigDecimal getIdTraza() {
        return idTraza;
    }

    public void setIdTraza(BigDecimal idTraza) {
        this.idTraza = idTraza;
    }

    public BigInteger getIdPaso() {
        return idPaso;
    }

    public void setIdPaso(BigInteger idPaso) {
        this.idPaso = idPaso;
    }

    public Short getActual() {
        return actual;
    }

    public void setActual(Short actual) {
        this.actual = actual;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public BigInteger getOrden() {
        return orden;
    }

    public void setOrden(BigInteger orden) {
        this.orden = orden;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getObservacionAdministrado() {
        return observacionAdministrado;
    }

    public void setObservacionAdministrado(String observacionAdministrado) {
        this.observacionAdministrado = observacionAdministrado;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Usuario getRemitente() {
        return remitente;
    }

    public void setRemitente(Usuario remitente) {
        this.remitente = remitente;
    }

    public Usuario getReemplazado() {
        return reemplazado;
    }

    public void setReemplazado(Usuario reemplazado) {
        this.reemplazado = reemplazado;
    }

    @XmlTransient
    public List<Traza> getTrazaList() {
        return trazaList;
    }

    public void setTrazaList(List<Traza> trazaList) {
        this.trazaList = trazaList;
    }

    public Traza getPadre() {
        return padre;
    }

    public void setPadre(Traza padre) {
        this.padre = padre;
    }

    public Proceso getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Proceso idProceso) {
        this.idProceso = idProceso;
    }

//    public Parametro getPrioridad() {
//        return prioridad;
//    }
//
//    public void setPrioridad(Parametro prioridad) {
//        this.prioridad = prioridad;
//    }

    public Expediente getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(Expediente idExpediente) {
        this.idExpediente = idExpediente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTraza != null ? idTraza.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Traza)) {
            return false;
        }
        Traza other = (Traza) object;
        if ((this.idTraza == null && other.idTraza != null) || (this.idTraza != null && !this.idTraza.equals(other.idTraza))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.Traza[ idTraza=" + idTraza + " ]";
    }
    
}
