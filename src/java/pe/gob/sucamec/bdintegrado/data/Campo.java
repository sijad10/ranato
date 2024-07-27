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
 * @author rchipana
 */
@Entity
@Table(name = "CAMPO", catalog = "", schema = "TRAMDOC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Campo.findAll", query = "SELECT c FROM Campo c"),
    @NamedQuery(name = "Campo.findByIdCampo", query = "SELECT c FROM Campo c WHERE c.idCampo = :idCampo"),
    @NamedQuery(name = "Campo.findByNombre", query = "SELECT c FROM Campo c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Campo.findByDescripcion", query = "SELECT c FROM Campo c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "Campo.findByFechaCreacion", query = "SELECT c FROM Campo c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Campo.findByEtiqueta", query = "SELECT c FROM Campo c WHERE c.etiqueta = :etiqueta"),
    @NamedQuery(name = "Campo.findByBuscable", query = "SELECT c FROM Campo c WHERE c.buscable = :buscable"),
    @NamedQuery(name = "Campo.findByObligatorio", query = "SELECT c FROM Campo c WHERE c.obligatorio = :obligatorio"),
    @NamedQuery(name = "Campo.findByContenido", query = "SELECT c FROM Campo c WHERE c.contenido = :contenido")})
public class Campo implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CAMPO")
    private BigDecimal idCampo;
    @Size(max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 50)
    @Column(name = "ETIQUETA")
    private String etiqueta;
    @Column(name = "BUSCABLE")
    private Short buscable;
    @Column(name = "OBLIGATORIO")
    private Short obligatorio;
    @Size(max = 30)
    @Column(name = "CONTENIDO")
    private String contenido;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campo")
    private List<CampoPorDocumento> campoPorDocumentoList;

    public Campo() {
    }

    public Campo(BigDecimal idCampo) {
        this.idCampo = idCampo;
    }

    public BigDecimal getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(BigDecimal idCampo) {
        this.idCampo = idCampo;
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

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Short getBuscable() {
        return buscable;
    }

    public void setBuscable(Short buscable) {
        this.buscable = buscable;
    }

    public Short getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Short obligatorio) {
        this.obligatorio = obligatorio;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @XmlTransient
    public List<CampoPorDocumento> getCampoPorDocumentoList() {
        return campoPorDocumentoList;
    }

    public void setCampoPorDocumentoList(List<CampoPorDocumento> campoPorDocumentoList) {
        this.campoPorDocumentoList = campoPorDocumentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCampo != null ? idCampo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Campo)) {
            return false;
        }
        Campo other = (Campo) object;
        if ((this.idCampo == null && other.idCampo != null) || (this.idCampo != null && !this.idCampo.equals(other.idCampo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.Campo[ idCampo=" + idCampo + " ]";
    }
    
}
