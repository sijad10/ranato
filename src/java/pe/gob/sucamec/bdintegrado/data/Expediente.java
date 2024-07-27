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
@Table(name = "EXPEDIENTE", catalog = "", schema = "TRAMDOC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Expediente.findAll", query = "SELECT e FROM Expediente e"),
    @NamedQuery(name = "Expediente.findByIdExpediente", query = "SELECT e FROM Expediente e WHERE e.idExpediente = :idExpediente"),
    @NamedQuery(name = "Expediente.findByNumero", query = "SELECT e FROM Expediente e WHERE e.numero = :numero"),
    @NamedQuery(name = "Expediente.findByEstado", query = "SELECT e FROM Expediente e WHERE e.estado = :estado"),
    @NamedQuery(name = "Expediente.findByFechaCreacion", query = "SELECT e FROM Expediente e WHERE e.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Expediente.findByTituloNotificacion", query = "SELECT e FROM Expediente e WHERE e.tituloNotificacion = :tituloNotificacion"),
    @NamedQuery(name = "Expediente.findByBloqueado", query = "SELECT e FROM Expediente e WHERE e.bloqueado = :bloqueado"),
    @NamedQuery(name = "Expediente.findByCopiado", query = "SELECT e FROM Expediente e WHERE e.copiado = :copiado"),
    @NamedQuery(name = "Expediente.findByTitulo", query = "SELECT e FROM Expediente e WHERE e.titulo = :titulo"),
    @NamedQuery(name = "Expediente.findByObservacionmp", query = "SELECT e FROM Expediente e WHERE e.observacionmp = :observacionmp"),
    @NamedQuery(name = "Expediente.findByNumeroDisca", query = "SELECT e FROM Expediente e WHERE e.numeroDisca = :numeroDisca")})
public class Expediente implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_EXPEDIENTE")
    private BigDecimal idExpediente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NUMERO")
    private String numero;
    @Column(name = "ESTADO")
    private Character estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 500)
    @Column(name = "TITULO_NOTIFICACION")
    private String tituloNotificacion;
    @Column(name = "BLOQUEADO")
    private Short bloqueado;
    @Column(name = "COPIADO")
    private Short copiado;
    @Size(max = 255)
    @Column(name = "TITULO")
    private String titulo;
    @Size(max = 4000)
    @Column(name = "OBSERVACIONMP")
    private String observacionmp;
    @Size(max = 50)
    @Column(name = "NUMERO_DISCA")
    private String numeroDisca;
    @Column(name = "DIAS_EN_ESPERA")
    private Long diasEnEspera;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idExpediente")
    private List<Traza> trazaList;
    @JoinColumn(name = "ID_PROCESO", referencedColumnName = "ID_PROCESO")
    @ManyToOne(optional = false)
    private Proceso idProceso;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    @ManyToOne(optional = false)
    private Usuario idUsuario;
    @OneToMany(mappedBy = "padre")
    private List<Expediente> expedienteList;
    @JoinColumn(name = "PADRE", referencedColumnName = "ID_EXPEDIENTE")
    @ManyToOne
    private Expediente padre;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID_CLIENTE")
    @ManyToOne
    private Cliente idCliente;    

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Expediente() {
    }

    public Expediente(BigDecimal idExpediente) {
        this.idExpediente = idExpediente;
    }

    public Expediente(BigDecimal idExpediente, String numero, Date fechaCreacion) {
        this.idExpediente = idExpediente;
        this.numero = numero;
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(BigDecimal idExpediente) {
        this.idExpediente = idExpediente;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public String getTituloNotificacion() {
        return tituloNotificacion;
    }

    public void setTituloNotificacion(String tituloNotificacion) {
        this.tituloNotificacion = tituloNotificacion;
    }

    public Short getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Short bloqueado) {
        this.bloqueado = bloqueado;
    }

    public Short getCopiado() {
        return copiado;
    }

    public void setCopiado(Short copiado) {
        this.copiado = copiado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getObservacionmp() {
        return observacionmp;
    }

    public void setObservacionmp(String observacionmp) {
        this.observacionmp = observacionmp;
    }

    public String getNumeroDisca() {
        return numeroDisca;
    }

    public void setNumeroDisca(String numeroDisca) {
        this.numeroDisca = numeroDisca;
    }

    public Proceso getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Proceso idProceso) {
        this.idProceso = idProceso;
    }

    @XmlTransient
    public List<Expediente> getExpedienteList() {
        return expedienteList;
    }

    public void setExpedienteList(List<Expediente> expedienteList) {
        this.expedienteList = expedienteList;
    }

    public Expediente getPadre() {
        return padre;
    }

    public void setPadre(Expediente padre) {
        this.padre = padre;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idExpediente != null ? idExpediente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Expediente)) {
            return false;
        }
        Expediente other = (Expediente) object;
        if ((this.idExpediente == null && other.idExpediente != null) || (this.idExpediente != null && !this.idExpediente.equals(other.idExpediente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.Expediente[ idExpediente=" + idExpediente + " ]";
    }

    @XmlTransient
    public List<Traza> getTrazaList() {
        return trazaList;
    }

    public void setTrazaList(List<Traza> trazaList) {
        this.trazaList = trazaList;
    }
    
    public Long getDiasEnEspera() {
        return diasEnEspera;
    }

    public void setDiasEnEspera(Long diasEnEspera) {
        this.diasEnEspera = diasEnEspera;
    }

}
