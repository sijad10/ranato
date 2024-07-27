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
@Table(name = "SB_DEPARTAMENTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbDepartamentoGt.findAll", query = "SELECT s FROM SbDepartamentoGt s"),
    @NamedQuery(name = "SbDepartamentoGt.findById", query = "SELECT s FROM SbDepartamentoGt s WHERE s.id = :id"),
    @NamedQuery(name = "SbDepartamentoGt.findByNombre", query = "SELECT s FROM SbDepartamentoGt s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbDepartamentoGt.findByActivo", query = "SELECT s FROM SbDepartamentoGt s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbDepartamentoGt.findByAudLogin", query = "SELECT s FROM SbDepartamentoGt s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbDepartamentoGt.findByAudNumIp", query = "SELECT s FROM SbDepartamentoGt s WHERE s.audNumIp = :audNumIp")})
public class SbDepartamentoGt implements Serializable {

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
    @OneToMany(mappedBy = "direccionRegional")
    private List<EppCom> eppComList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departamentoId")
    private List<SbProvinciaGt> sbProvinciaList;

    public SbDepartamentoGt() {
    }

    public SbDepartamentoGt(Long id) {
        this.id = id;
    }

    public SbDepartamentoGt(Long id, String nombre, short activo, String audLogin, String audNumIp) {
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

    @XmlTransient
    public List<EppCom> getEppComList() {
        return eppComList;
    }

    public void setEppComList(List<EppCom> eppComList) {
        this.eppComList = eppComList;
    }

    @XmlTransient
    public List<SbProvinciaGt> getSbProvinciaList() {
        return sbProvinciaList;
    }

    public void setSbProvinciaList(List<SbProvinciaGt> sbProvinciaList) {
        this.sbProvinciaList = sbProvinciaList;
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
        if (!(object instanceof SbDepartamentoGt)) {
            return false;
        }
        SbDepartamentoGt other = (SbDepartamentoGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.SbDepartamentoGt[ id=" + id + " ]";
    }

}
