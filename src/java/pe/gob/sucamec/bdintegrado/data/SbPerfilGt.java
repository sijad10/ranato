/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mespinoza
 */
@Entity
@Table(name = "SB_PERFIL", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbPerfil.findAll", query = "SELECT s FROM SbPerfil s"),
    @NamedQuery(name = "SbPerfil.findById", query = "SELECT s FROM SbPerfil s WHERE s.id = :id"),
    @NamedQuery(name = "SbPerfil.findByNombre", query = "SELECT s FROM SbPerfil s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbPerfil.findByCodProg", query = "SELECT s FROM SbPerfil s WHERE s.codProg = :codProg"),
    @NamedQuery(name = "SbPerfil.findByActivo", query = "SELECT s FROM SbPerfil s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbPerfil.findByAudLogin", query = "SELECT s FROM SbPerfil s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbPerfil.findByAudNumIp", query = "SELECT s FROM SbPerfil s WHERE s.audNumIp = :audNumIp")})
public class SbPerfilGt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
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
//    @JoinTable(name = "SB_PERFIL_USUARIO", schema = "BDINTEGRADO", joinColumns = {
//        @JoinColumn(name = "PERFIL_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
//        @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")})
//    @ManyToMany
//    private List<SbUsuarioGt> sbUsuarioList;
//    @ManyToMany(mappedBy = "sbPerfilList")
//    private List<SbPaginaGt> sbPaginaList;
//    @ManyToMany(mappedBy = "sbPerfilList")
//    private List<SbUsuarioGt> sbUsuarioList;
    @ManyToMany(mappedBy = "sbPerfilList")
    private List<SbPaginaGt> sbPaginaList;
    @JoinTable(name = "SB_PERFIL_USUARIO", schema = "BDINTEGRADO", joinColumns = {
        @JoinColumn(name = "PERFIL_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbUsuarioGt> sbUsuarioList2;
    @ManyToMany(mappedBy = "sbPerfilList")
    private List<SbUsuarioGt> sbUsuarioList;
    @OneToMany(mappedBy = "perfilId")
    private List<SbReglaGt> sbReglaList;
    @JoinColumn(name = "SISTEMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbSistemaGt sistemaId;

    public SbPerfilGt() {
    }

    public SbPerfilGt(Long id) {
        this.id = id;
    }

    public SbPerfilGt(Long id, String nombre, String codProg, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.codProg = codProg;
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
    public List<SbUsuarioGt> getSbUsuarioList() {
        return sbUsuarioList;
    }

    public void setSbUsuarioList(List<SbUsuarioGt> sbUsuarioList) {
        this.sbUsuarioList = sbUsuarioList;
    }

    @XmlTransient
    public List<SbPaginaGt> getSbPaginaList() {
        return sbPaginaList;
    }

    public void setSbPaginaList(List<SbPaginaGt> sbPaginaList) {
        this.sbPaginaList = sbPaginaList;
    }

    @XmlTransient
    public List<SbReglaGt> getSbReglaList() {
        return sbReglaList;
    }

    public void setSbReglaList(List<SbReglaGt> sbReglaList) {
        this.sbReglaList = sbReglaList;
    }

    public SbSistemaGt getSistemaId() {
        return sistemaId;
    }

    public void setSistemaId(SbSistemaGt sistemaId) {
        this.sistemaId = sistemaId;
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
        if (!(object instanceof SbPerfilGt)) {
            return false;
        }
        SbPerfilGt other = (SbPerfilGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.SbPerfil[ id=" + id + " ]";
    }

    /**
     * @return the sbUsuarioList2
     */
    public List<SbUsuarioGt> getSbUsuarioList2() {
        return sbUsuarioList2;
    }

    /**
     * @param sbUsuarioList2 the sbUsuarioList2 to set
     */
    public void setSbUsuarioList2(List<SbUsuarioGt> sbUsuarioList2) {
        this.sbUsuarioList2 = sbUsuarioList2;
    }
    
}
