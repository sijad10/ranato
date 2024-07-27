/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

import java.io.Serializable;
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
 * @author msalinas
 */
@Entity
@Table(name = "TUR_SEDE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaTurSede.findAll", query = "SELECT t FROM CitaTurSede t"),
    @NamedQuery(name = "CitaTurSede.findById", query = "SELECT t FROM CitaTurSede t WHERE t.id = :id"),
    @NamedQuery(name = "CitaTurSede.findByDescripcion", query = "SELECT t FROM CitaTurSede t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "CitaTurSede.findByDireccion", query = "SELECT t FROM CitaTurSede t WHERE t.direccion = :direccion"),
    @NamedQuery(name = "CitaTurSede.findByActivo", query = "SELECT t FROM CitaTurSede t WHERE t.activo = :activo"),
    @NamedQuery(name = "CitaTurSede.findByAudLogin", query = "SELECT t FROM CitaTurSede t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "CitaTurSede.findByAudNumIp", query = "SELECT t FROM CitaTurSede t WHERE t.audNumIp = :audNumIp"),
    @NamedQuery(name = "CitaTurSede.findByCodProg", query = "SELECT t FROM CitaTurSede t WHERE t.codProg = :codProg")})
public class CitaTurSede implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DIRECCION")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "COD_PROG")
    private String codProg;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sedeId")
//    private List<TurTurno> turTurnoList;
    /*@JoinColumn(name = "DISTRITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Distrito distritoId;*/

    public CitaTurSede() {
    }

    public CitaTurSede(Long id) {
        this.id = id;
    }

    public CitaTurSede(Long id, String descripcion, String direccion, short activo, String audLogin, String audNumIp, String codProg) {
        this.id = id;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
        this.codProg = codProg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getCodProg() {
        return codProg;
    }

    public void setCodProg(String codProg) {
        this.codProg = codProg;
    }

//    @XmlTransient
//    public List<TurTurno> getTurTurnoList() {
//        return turTurnoList;
//    }
//
//    public void setTurTurnoList(List<TurTurno> turTurnoList) {
//        this.turTurnoList = turTurnoList;
//    }

    /*public Distrito getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(Distrito distritoId) {
        this.distritoId = distritoId;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitaTurSede)) {
            return false;
        }
        CitaTurSede other = (CitaTurSede) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.citas.data.CitaTurSede[ id=" + id + " ]";
    }

}
