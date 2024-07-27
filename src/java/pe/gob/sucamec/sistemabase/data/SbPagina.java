/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_PAGINA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbPagina.findAll", query = "SELECT s FROM SbPagina s"),
    @NamedQuery(name = "SbPagina.findById", query = "SELECT s FROM SbPagina s WHERE s.id = :id"),
    @NamedQuery(name = "SbPagina.findByNombre", query = "SELECT s FROM SbPagina s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbPagina.findByDescripcion", query = "SELECT s FROM SbPagina s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SbPagina.findByUrl", query = "SELECT s FROM SbPagina s WHERE s.url = :url"),
    @NamedQuery(name = "SbPagina.findByFuncion", query = "SELECT s FROM SbPagina s WHERE s.funcion = :funcion"),
    @NamedQuery(name = "SbPagina.findByOrden", query = "SELECT s FROM SbPagina s WHERE s.orden = :orden"),
    @NamedQuery(name = "SbPagina.findByCategoria", query = "SELECT s FROM SbPagina s WHERE s.categoria = :categoria"),
    @NamedQuery(name = "SbPagina.findByMenu", query = "SELECT s FROM SbPagina s WHERE s.menu = :menu"),
    @NamedQuery(name = "SbPagina.findByActivo", query = "SELECT s FROM SbPagina s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbPagina.findByAudLogin", query = "SELECT s FROM SbPagina s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbPagina.findByAudNumIp", query = "SELECT s FROM SbPagina s WHERE s.audNumIp = :audNumIp")})
public class SbPagina implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_PAGINA")
    @SequenceGenerator(name = "SEQ_SB_PAGINA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_PAGINA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "URL")
    private String url;
    @Size(max = 300)
    @Column(name = "FUNCION")
    private String funcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDEN")
    private Long orden;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CATEGORIA")
    private short categoria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MENU")
    private short menu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinTable(name = "SB_PERFIL_PAGINA", schema = "BDINTEGRADO", joinColumns = {
        @JoinColumn(name = "PAGINA_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PERFIL_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbPerfil> sbPerfilList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paginaId")
    private List<SbRegla> sbReglaList;
    @OneToMany(mappedBy = "categoriaId")
    private List<SbPagina> sbPaginaList;
    @JoinColumn(name = "CATEGORIA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPagina categoriaId;
    @JoinColumn(name = "SISTEMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbSistema sistemaId;

    public SbPagina() {
    }

    public SbPagina(Long id) {
        this.id = id;
    }

    public SbPagina(Long id, String nombre, String url, Long orden, short categoria, short menu, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.url = url;
        this.orden = orden;
        this.categoria = categoria;
        this.menu = menu;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
    }

    public short getCategoria() {
        return categoria;
    }

    public void setCategoria(short categoria) {
        this.categoria = categoria;
    }

    public short getMenu() {
        return menu;
    }

    public void setMenu(short menu) {
        this.menu = menu;
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
    public List<SbPerfil> getSbPerfilList() {
        return sbPerfilList;
    }

    public void setSbPerfilList(List<SbPerfil> sbPerfilList) {
        this.sbPerfilList = sbPerfilList;
    }

    @XmlTransient
    public List<SbRegla> getSbReglaList() {
        return sbReglaList;
    }

    public void setSbReglaList(List<SbRegla> sbReglaList) {
        this.sbReglaList = sbReglaList;
    }

    @XmlTransient
    public List<SbPagina> getSbPaginaList() {
        return sbPaginaList;
    }

    public void setSbPaginaList(List<SbPagina> sbPaginaList) {
        this.sbPaginaList = sbPaginaList;
    }

    public SbPagina getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(SbPagina categoriaId) {
        this.categoriaId = categoriaId;
    }

    public SbSistema getSistemaId() {
        return sistemaId;
    }

    public void setSistemaId(SbSistema sistemaId) {
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
        if (!(object instanceof SbPagina)) {
            return false;
        }
        SbPagina other = (SbPagina) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbPagina[ id=" + id + " ]";
    }
    
}
