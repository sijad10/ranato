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
@Table(name = "TIPO_GAMAC", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaTipoGamac.findAll", query = "SELECT t FROM CitaTipoGamac t"),
    @NamedQuery(name = "CitaTipoGamac.findById", query = "SELECT t FROM CitaTipoGamac t WHERE t.id = :id"),
    @NamedQuery(name = "CitaTipoGamac.findByNombre", query = "SELECT t FROM CitaTipoGamac t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "CitaTipoGamac.findByAbreviatura", query = "SELECT t FROM CitaTipoGamac t WHERE t.abreviatura = :abreviatura"),
    @NamedQuery(name = "CitaTipoGamac.findByDescripcion", query = "SELECT t FROM CitaTipoGamac t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "CitaTipoGamac.findByCodProg", query = "SELECT t FROM CitaTipoGamac t WHERE t.codProg = :codProg"),
    @NamedQuery(name = "CitaTipoGamac.findByOrden", query = "SELECT t FROM CitaTipoGamac t WHERE t.orden = :orden"),
    @NamedQuery(name = "CitaTipoGamac.findByActivo", query = "SELECT t FROM CitaTipoGamac t WHERE t.activo = :activo"),
    @NamedQuery(name = "CitaTipoGamac.findByAudLogin", query = "SELECT t FROM CitaTipoGamac t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "CitaTipoGamac.findByAudNumIp", query = "SELECT t FROM CitaTipoGamac t WHERE t.audNumIp = :audNumIp")})
public class CitaTipoGamac implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "COD_PROG")
    private String codProg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDEN")
    private short orden;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private List<CitaTurTurno> turTurnoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoTramiteId")
    private List<CitaTurTurno> turTurnoList1;
    @OneToMany(mappedBy = "exaTeoPrac")
    private List<CitaTurConstancia> turConstanciaList;
    @OneToMany(mappedBy = "exaTiro")
    private List<CitaTurConstancia> turConstanciaList1;
    @OneToMany(mappedBy = "resulGral")
    private List<CitaTurConstancia> turConstanciaList2;
    @OneToMany(mappedBy = "tipoId")
    private List<CitaTipoGamac> tipoGamacList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoGamac tipoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoArmaId")
    private List<CitaTurComprobante> turComprobanteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calibreId")
    private List<CitaTurComprobante> turComprobanteList1;
    @JoinTable(name = "AMA_CATALOGO_MODALIDAD", joinColumns = {
        @JoinColumn(name = "TIPO_GAMAC_MODALIDAD_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "GAMAC_CATALOGO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<CitaAmaCatalogo> amaCatalogoList;
    @ManyToMany
    private List<CitaTurLicenciaReg> turLicenciaRegList;

    public CitaTipoGamac() {
    }

    public CitaTipoGamac(Long id) {
        this.id = id;
    }

    public CitaTipoGamac(Long id, String nombre, String abreviatura, String descripcion, String codProg, short orden, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.descripcion = descripcion;
        this.codProg = codProg;
        this.orden = orden;
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

    public short getOrden() {
        return orden;
    }

    public void setOrden(short orden) {
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
    public List<CitaTurTurno> getTurTurnoList() {
        return turTurnoList;
    }

    public void setTurTurnoList(List<CitaTurTurno> turTurnoList) {
        this.turTurnoList = turTurnoList;
    }

    @XmlTransient
    public List<CitaTurTurno> getTurTurnoList1() {
        return turTurnoList1;
    }

    public void setTurTurnoList1(List<CitaTurTurno> turTurnoList1) {
        this.turTurnoList1 = turTurnoList1;
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
    public List<CitaTurConstancia> getTurConstanciaList2() {
        return turConstanciaList2;
    }

    public void setTurConstanciaList2(List<CitaTurConstancia> turConstanciaList2) {
        this.turConstanciaList2 = turConstanciaList2;
    }

    @XmlTransient
    public List<CitaTipoGamac> getTipoGamacList() {
        return tipoGamacList;
    }

    public void setTipoGamacList(List<CitaTipoGamac> tipoGamacList) {
        this.tipoGamacList = tipoGamacList;
    }
    
    @XmlTransient
    public List<CitaAmaCatalogo> getAmaCatalogoList() {
        return amaCatalogoList;
    }

    public void setAmaCatalogoList(List<CitaAmaCatalogo> amaCatalogoList) {
        this.amaCatalogoList = amaCatalogoList;
    }

    public CitaTipoGamac getTipoId() {
        return tipoId;
    }

    public void setTipoId(CitaTipoGamac tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public List<CitaTurComprobante> getTurComprobanteList() {
        return turComprobanteList;
    }

    public void setTurComprobanteList(List<CitaTurComprobante> turComprobanteList) {
        this.turComprobanteList = turComprobanteList;
    }

    @XmlTransient
    public List<CitaTurComprobante> getTurComprobanteList1() {
        return turComprobanteList1;
    }

    public void setTurComprobanteList1(List<CitaTurComprobante> turComprobanteList1) {
        this.turComprobanteList1 = turComprobanteList1;
    }

    @XmlTransient
    public List<CitaTurLicenciaReg> getTurLicenciaRegList() {
        return turLicenciaRegList;
    }

    public void setTurLicenciaRegList(List<CitaTurLicenciaReg> turLicenciaRegList) {
        this.turLicenciaRegList = turLicenciaRegList;
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
        if (!(object instanceof CitaTipoGamac)) {
            return false;
        }
        CitaTipoGamac other = (CitaTipoGamac) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.citas.data.CitaTipoGamac[ id=" + id + " ]";
    }

}
