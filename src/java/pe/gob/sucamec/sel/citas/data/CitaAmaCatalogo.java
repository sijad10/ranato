/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

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
 * @author msalinas
 */
@Entity
@Table(name = "AMA_CATALOGO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaAmaCatalogo.findAll", query = "SELECT a FROM CitaAmaCatalogo a"),
    @NamedQuery(name = "CitaAmaCatalogo.findById", query = "SELECT a FROM CitaAmaCatalogo a WHERE a.id = :id"),
    @NamedQuery(name = "CitaAmaCatalogo.findByNombre", query = "SELECT a FROM CitaAmaCatalogo a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "CitaAmaCatalogo.findByAbreviatura", query = "SELECT a FROM CitaAmaCatalogo a WHERE a.abreviatura = :abreviatura"),
    @NamedQuery(name = "CitaAmaCatalogo.findByDescripcion", query = "SELECT a FROM CitaAmaCatalogo a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "CitaAmaCatalogo.findByCodProg", query = "SELECT a FROM CitaAmaCatalogo a WHERE a.codProg = :codProg"),
    @NamedQuery(name = "CitaAmaCatalogo.findByOrden", query = "SELECT a FROM CitaAmaCatalogo a WHERE a.orden = :orden"),
    @NamedQuery(name = "CitaAmaCatalogo.findByActivo", query = "SELECT a FROM CitaAmaCatalogo a WHERE a.activo = :activo"),
    @NamedQuery(name = "CitaAmaCatalogo.findByAudLogin", query = "SELECT a FROM CitaAmaCatalogo a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "CitaAmaCatalogo.findByAudNumIp", query = "SELECT a FROM CitaAmaCatalogo a WHERE a.audNumIp = :audNumIp")})
public class CitaAmaCatalogo implements Serializable {

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
    @Size(max = 200)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 15)
    @Column(name = "COD_PROG")
    private String codProg;
    @Column(name = "ORDEN")
    private Long orden;
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
    @JoinTable(name = "AMA_DETALLE_CATALOGO", joinColumns = {
        @JoinColumn(name = "TIPO_CATALOGO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TIPO_TIPO_CATALOGO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<CitaAmaCatalogo> amaCatalogoList;
    @ManyToMany(mappedBy = "amaCatalogoList")
    private List<CitaAmaCatalogo> amaCatalogoList1;
    @ManyToMany(mappedBy = "amaCatalogoList")
    private List<CitaTipoGamac> tipoGamacList;
    @OneToMany(mappedBy = "armaTeoId")
    private List<CitaTurConstancia> turConstanciaList;
    @OneToMany(mappedBy = "armaTiroId")
    private List<CitaTurConstancia> turConstanciaList1;
    @OneToMany(mappedBy = "tipoId")
    private List<CitaAmaCatalogo> amaCatalogoList2;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaAmaCatalogo tipoId;

    public CitaAmaCatalogo() {
    }

    public CitaAmaCatalogo(Long id) {
        this.id = id;
    }

    public CitaAmaCatalogo(Long id, String nombre, short activo, String audLogin, String audNumIp) {
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
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

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
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
    public List<CitaAmaCatalogo> getAmaCatalogoList() {
        return amaCatalogoList;
    }

    public void setAmaCatalogoList(List<CitaAmaCatalogo> amaCatalogoList) {
        this.amaCatalogoList = amaCatalogoList;
    }

    @XmlTransient
    public List<CitaAmaCatalogo> getAmaCatalogoList1() {
        return amaCatalogoList1;
    }

    public void setAmaCatalogoList1(List<CitaAmaCatalogo> amaCatalogoList1) {
        this.amaCatalogoList1 = amaCatalogoList1;
    }

    @XmlTransient
    public List<CitaTipoGamac> getTipoGamacList() {
        return tipoGamacList;
    }

    public void setTipoGamacList(List<CitaTipoGamac> tipoGamacList) {
        this.tipoGamacList = tipoGamacList;
    }

    @XmlTransient
    public List<CitaTurConstancia> getTurConstanciaList() {
        return turConstanciaList;
    }

    public void setTurConstanciaList(List<CitaTurConstancia> turConstanciaList) {
        this.turConstanciaList = turConstanciaList;
    }

    @XmlTransient
    public List<CitaTurConstancia> getTurConstanciaList1() {
        return turConstanciaList1;
    }

    public void setTurConstanciaList1(List<CitaTurConstancia> turConstanciaList1) {
        this.turConstanciaList1 = turConstanciaList1;
    }

    @XmlTransient
    public List<CitaAmaCatalogo> getAmaCatalogoList2() {
        return amaCatalogoList2;
    }

    public void setAmaCatalogoList2(List<CitaAmaCatalogo> amaCatalogoList2) {
        this.amaCatalogoList2 = amaCatalogoList2;
    }

    public CitaAmaCatalogo getTipoId() {
        return tipoId;
    }

    public void setTipoId(CitaAmaCatalogo tipoId) {
        this.tipoId = tipoId;
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
        if (!(object instanceof CitaAmaCatalogo)) {
            return false;
        }
        CitaAmaCatalogo other = (CitaAmaCatalogo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.CitaAmaCatalogo[ id=" + id + " ]";
    }
    
}
