/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

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
 * @author mespinoza
 */
@Entity
@Table(name = "SB_PROVINCIA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbProvinciaGt.findAll", query = "SELECT s FROM SbProvinciaGt s"),
    @NamedQuery(name = "SbProvinciaGt.findById", query = "SELECT s FROM SbProvinciaGt s WHERE s.id = :id"),
    @NamedQuery(name = "SbProvinciaGt.findByNombre", query = "SELECT s FROM SbProvinciaGt s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbProvinciaGt.findByActivo", query = "SELECT s FROM SbProvinciaGt s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbProvinciaGt.findByAudLogin", query = "SELECT s FROM SbProvinciaGt s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbProvinciaGt.findByAudNumIp", query = "SELECT s FROM SbProvinciaGt s WHERE s.audNumIp = :audNumIp")})
public class SbProvinciaGt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "NOMBRE")
    private String nombre;
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
    @JoinColumn(name = "DEPARTAMENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDepartamentoGt departamentoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provinciaId")
    private List<SbDistritoGt> sbDistritoList;

    public SbProvinciaGt() {
    }

    public SbProvinciaGt(Long id) {
        this.id = id;
    }

    public SbProvinciaGt(Long id, String nombre, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public SbDepartamentoGt getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(SbDepartamentoGt departamentoId) {
        this.departamentoId = departamentoId;
    }

    @XmlTransient
    public List<SbDistritoGt> getSbDistritoList() {
        return sbDistritoList;
    }

    public void setSbDistritoList(List<SbDistritoGt> sbDistritoList) {
        this.sbDistritoList = sbDistritoList;
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
        if (!(object instanceof SbProvinciaGt)) {
            return false;
        }
        SbProvinciaGt other = (SbProvinciaGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.SbProvinciaGt[ id=" + id + " ]";
    }
    
}
