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
@Table(name = "SB_SISTEMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbSistema.findAll", query = "SELECT s FROM SbSistema s"),
    @NamedQuery(name = "SbSistema.findById", query = "SELECT s FROM SbSistema s WHERE s.id = :id"),
    @NamedQuery(name = "SbSistema.findByNombre", query = "SELECT s FROM SbSistema s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbSistema.findByDescripcion", query = "SELECT s FROM SbSistema s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SbSistema.findByCodProg", query = "SELECT s FROM SbSistema s WHERE s.codProg = :codProg"),
    @NamedQuery(name = "SbSistema.findByActivo", query = "SELECT s FROM SbSistema s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbSistema.findByAudLogin", query = "SELECT s FROM SbSistema s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbSistema.findByAudNumIp", query = "SELECT s FROM SbSistema s WHERE s.audNumIp = :audNumIp")})
public class SbSistemaGt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 1000)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 10)
    @Column(name = "COD_PROG")
    private String codProg;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sistemaId")
    private List<SbPaginaGt> sbPaginaList;
    @OneToMany(mappedBy = "sistemaId")
    private List<SbPerfilGt> sbPerfilList;

    public SbSistemaGt() {
    }

    public SbSistemaGt(Long id) {
        this.id = id;
    }

    public SbSistemaGt(Long id, String nombre, short activo, String audLogin, String audNumIp) {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodProg() {
        return codProg;
    }

    public void setCodProg(String codProg) {
        this.codProg = codProg;
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
    public List<SbPaginaGt> getSbPaginaList() {
        return sbPaginaList;
    }

    public void setSbPaginaList(List<SbPaginaGt> sbPaginaList) {
        this.sbPaginaList = sbPaginaList;
    }

    @XmlTransient
    public List<SbPerfilGt> getSbPerfilList() {
        return sbPerfilList;
    }

    public void setSbPerfilList(List<SbPerfilGt> sbPerfilList) {
        this.sbPerfilList = sbPerfilList;
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
        if (!(object instanceof SbSistemaGt)) {
            return false;
        }
        SbSistemaGt other = (SbSistemaGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.SbSistema[ id=" + id + " ]";
    }
    
}
