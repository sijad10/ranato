/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
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
 * @author rfernandezv
 */
@Entity
@Table(name = "UBIGEO", catalog = "", schema = "TRAMDOC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ubigeo.findAll", query = "SELECT u FROM Ubigeo u"),
    @NamedQuery(name = "Ubigeo.findByIdUbigeo", query = "SELECT u FROM Ubigeo u WHERE u.idUbigeo = :idUbigeo"),
    @NamedQuery(name = "Ubigeo.findByCodigo", query = "SELECT u FROM Ubigeo u WHERE u.codigo = :codigo"),
    @NamedQuery(name = "Ubigeo.findByNombre", query = "SELECT u FROM Ubigeo u WHERE u.nombre = :nombre")})
public class Ubigeo implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_UBIGEO")
    private BigDecimal idUbigeo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "CODIGO")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "NOMBRE")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ubigeo")
    private List<Sede> sedeList;
    @OneToMany(mappedBy = "padre")
    private List<Ubigeo> ubigeoList;
    @JoinColumn(name = "PADRE", referencedColumnName = "ID_UBIGEO")
    @ManyToOne
    private Ubigeo padre;
//    @JoinColumn(name = "TIPO_SERVICIO", referencedColumnName = "ID_PARAMETRO")
//    @ManyToOne
//    private Parametro tipoServicio;

    public Ubigeo() {
    }

    public Ubigeo(BigDecimal idUbigeo) {
        this.idUbigeo = idUbigeo;
    }

    public Ubigeo(BigDecimal idUbigeo, String codigo, String nombre) {
        this.idUbigeo = idUbigeo;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public BigDecimal getIdUbigeo() {
        return idUbigeo;
    }

    public void setIdUbigeo(BigDecimal idUbigeo) {
        this.idUbigeo = idUbigeo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Sede> getSedeList() {
        return sedeList;
    }

    public void setSedeList(List<Sede> sedeList) {
        this.sedeList = sedeList;
    }

    @XmlTransient
    public List<Ubigeo> getUbigeoList() {
        return ubigeoList;
    }

    public void setUbigeoList(List<Ubigeo> ubigeoList) {
        this.ubigeoList = ubigeoList;
    }

    public Ubigeo getPadre() {
        return padre;
    }

    public void setPadre(Ubigeo padre) {
        this.padre = padre;
    }

//    public Parametro getTipoServicio() {
//        return tipoServicio;
//    }
//
//    public void setTipoServicio(Parametro tipoServicio) {
//        this.tipoServicio = tipoServicio;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUbigeo != null ? idUbigeo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ubigeo)) {
            return false;
        }
        Ubigeo other = (Ubigeo) object;
        if ((this.idUbigeo == null && other.idUbigeo != null) || (this.idUbigeo != null && !this.idUbigeo.equals(other.idUbigeo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.Ubigeo[ idUbigeo=" + idUbigeo + " ]";
    }
    
}
