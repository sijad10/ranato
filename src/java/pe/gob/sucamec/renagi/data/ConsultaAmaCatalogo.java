/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.data;

import java.io.Serializable;
import java.util.Collection;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
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
//import pe.gob.sucamec.turreg.data.TurLicenciaReg;

/**
 *
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_CATALOGO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsultaAmaCatalogo.findAll", query = "SELECT a FROM ConsultaAmaCatalogo a"),
    @NamedQuery(name = "ConsultaAmaCatalogo.findById", query = "SELECT a FROM ConsultaAmaCatalogo a WHERE a.id = :id"),
    @NamedQuery(name = "ConsultaAmaCatalogo.findByNombre", query = "SELECT a FROM ConsultaAmaCatalogo a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "ConsultaAmaCatalogo.findByAbreviatura", query = "SELECT a FROM ConsultaAmaCatalogo a WHERE a.abreviatura = :abreviatura"),
    @NamedQuery(name = "ConsultaAmaCatalogo.findByDescripcion", query = "SELECT a FROM ConsultaAmaCatalogo a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "ConsultaAmaCatalogo.findByCodProg", query = "SELECT a FROM ConsultaAmaCatalogo a WHERE a.codProg = :codProg"),
    @NamedQuery(name = "ConsultaAmaCatalogo.findByOrden", query = "SELECT a FROM ConsultaAmaCatalogo a WHERE a.orden = :orden"),
    @NamedQuery(name = "ConsultaAmaCatalogo.findByActivo", query = "SELECT a FROM ConsultaAmaCatalogo a WHERE a.activo = :activo"),
    @NamedQuery(name = "ConsultaAmaCatalogo.findByAudLogin", query = "SELECT a FROM ConsultaAmaCatalogo a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "ConsultaAmaCatalogo.findByAudNumIp", query = "SELECT a FROM ConsultaAmaCatalogo a WHERE a.audNumIp = :audNumIp")})
public class ConsultaAmaCatalogo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_CATALOGO")
    @SequenceGenerator(name = "SEQ_CONSULTA_AMA_CATALOGO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_CATALOGO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 15)
    @Column(name = "COD_PROG")
    private String codProg;
    @Column(name = "ORDEN")
    private Short orden;
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
    
    /*@JoinTable(schema="BDINTEGRADO", name = "AMA_DETALLE_CATALOGO", joinColumns = {
        @JoinColumn(name = "TIPO_CATALOGO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TIPO_TIPO_CATALOGO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<ConsultaAmaCatalogo> amaCatalogoList;
    @ManyToMany(mappedBy = "amaCatalogoList")
    private List<ConsultaAmaCatalogo> amaCatalogoList1;
    @ManyToMany(mappedBy = "amaCatalogoList")
    private List<ConsultaTipoGamac> tipoGamacList;
    @OneToMany(mappedBy = "tipoId")
    private List<ConsultaAmaCatalogo> amaCatalogoList2;
    @ManyToMany(mappedBy = "gamacAmaCatalogoCollection")
    private Collection<ConsultaAmaModelos> gamacAmaModelosCollection;
    
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private ConsultaAmaCatalogo tipoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoArmaId")
    private List<ConsultaAmaArma> amaArmaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calibreId")
    private List<ConsultaAmaArma> amaArmaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marcaId")
    private List<ConsultaAmaArma> amaArmaList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modeloId")
    private List<ConsultaAmaArma> amaArmaList3;
    @OneToMany(mappedBy = "tipoArticuloId")
    private Collection<ConsultaAmaModelos> gamacAmaModelosCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disposicionId")
    private Collection<ConsultaAmaModelos> gamacAmaModelosCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marcaId")
    private Collection<ConsultaAmaModelos> gamacAmaModelosCollection3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoArmaId")
    private Collection<ConsultaAmaModelos> gamacAmaModelosCollection4;*/
    
    
    //REGULARIZACION
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calibre")
    private List<ConsultaTurLicenciaReg> turLicenciaRegList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoArticulo")
    private List<ConsultaTurLicenciaReg> turLicenciaRegList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marca")
    private List<ConsultaTurLicenciaReg> turLicenciaRegList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modelo")
    private List<ConsultaTurLicenciaReg> turLicenciaRegList3;

    public ConsultaAmaCatalogo() {
    }

    public ConsultaAmaCatalogo(Long id) {
        this.id = id;
    }

    public ConsultaAmaCatalogo(Long id, short activo, String audLogin, String audNumIp) {
        this.id = id;
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

    public Short getOrden() {
        return orden;
    }

    public void setOrden(Short orden) {
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

    /*
    @XmlTransient
    public List<ConsultaAmaCatalogo> getAmaCatalogoList() {
        return amaCatalogoList;
    }

    public void setAmaCatalogoList(List<ConsultaAmaCatalogo> amaCatalogoList) {
        this.amaCatalogoList = amaCatalogoList;
    }

    @XmlTransient
    public List<ConsultaAmaCatalogo> getAmaCatalogoList1() {
        return amaCatalogoList1;
    }

    public void setAmaCatalogoList1(List<ConsultaAmaCatalogo> amaCatalogoList1) {
        this.amaCatalogoList1 = amaCatalogoList1;
    }

    @XmlTransient
    public List<ConsultaTipoGamac> getTipoGamacList() {
        return tipoGamacList;
    }

    public void setTipoGamacList(List<ConsultaTipoGamac> tipoGamacList) {
        this.tipoGamacList = tipoGamacList;
    }

    @XmlTransient
    public List<ConsultaAmaCatalogo> getAmaCatalogoList2() {
        return amaCatalogoList2;
    }

    public void setAmaCatalogoList2(List<ConsultaAmaCatalogo> amaCatalogoList2) {
        this.amaCatalogoList2 = amaCatalogoList2;
    }

    @XmlTransient
    public Collection<ConsultaAmaModelos> getGamacAmaModelosCollection() {
        return gamacAmaModelosCollection;
    }

    public void setGamacAmaModelosCollection(Collection<ConsultaAmaModelos> gamacAmaModelosCollection) {
        this.gamacAmaModelosCollection = gamacAmaModelosCollection;
    }

    public Collection<ConsultaAmaModelos> getGamacAmaModelosCollection1() {
        return gamacAmaModelosCollection1;
    }

    public void setGamacAmaModelosCollection1(Collection<ConsultaAmaModelos> gamacAmaModelosCollection1) {
        this.gamacAmaModelosCollection1 = gamacAmaModelosCollection1;
    }

    public Collection<ConsultaAmaModelos> getGamacAmaModelosCollection2() {
        return gamacAmaModelosCollection2;
    }

    public void setGamacAmaModelosCollection2(Collection<ConsultaAmaModelos> gamacAmaModelosCollection2) {
        this.gamacAmaModelosCollection2 = gamacAmaModelosCollection2;
    }

    public Collection<ConsultaAmaModelos> getGamacAmaModelosCollection3() {
        return gamacAmaModelosCollection3;
    }

    public void setGamacAmaModelosCollection3(Collection<ConsultaAmaModelos> gamacAmaModelosCollection3) {
        this.gamacAmaModelosCollection3 = gamacAmaModelosCollection3;
    }

    public Collection<ConsultaAmaModelos> getGamacAmaModelosCollection4() {
        return gamacAmaModelosCollection4;
    }

    public void setGamacAmaModelosCollection4(Collection<ConsultaAmaModelos> gamacAmaModelosCollection4) {
        this.gamacAmaModelosCollection4 = gamacAmaModelosCollection4;
    }
    
    public ConsultaAmaCatalogo getTipoId() {
        return tipoId;
    }

    public void setTipoId(ConsultaAmaCatalogo tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public List<ConsultaAmaArma> getAmaArmaList() {
        return amaArmaList;
    }

    public void setAmaArmaList(List<ConsultaAmaArma> amaArmaList) {
        this.amaArmaList = amaArmaList;
    }

    @XmlTransient
    public List<ConsultaAmaArma> getAmaArmaList1() {
        return amaArmaList1;
    }

    public void setAmaArmaList1(List<ConsultaAmaArma> amaArmaList1) {
        this.amaArmaList1 = amaArmaList1;
    }

    @XmlTransient
    public List<ConsultaAmaArma> getAmaArmaList2() {
        return amaArmaList2;
    }

    public void setAmaArmaList2(List<ConsultaAmaArma> amaArmaList2) {
        this.amaArmaList2 = amaArmaList2;
    }

    @XmlTransient
    public List<ConsultaAmaArma> getAmaArmaList3() {
        return amaArmaList3;
    }

    public void setAmaArmaList3(List<ConsultaAmaArma> amaArmaList3) {
        this.amaArmaList3 = amaArmaList3;
    }
    */

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConsultaAmaCatalogo)) {
            return false;
        }
        ConsultaAmaCatalogo other = (ConsultaAmaCatalogo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.renagi.data.ConsultaAmaCatalogo[ id=" + id + " ]";
    }

    @XmlTransient
    public List<ConsultaTurLicenciaReg> getTurLicenciaRegList() {
        return turLicenciaRegList;
    }

    public void setTurLicenciaRegList(List<ConsultaTurLicenciaReg> turLicenciaRegList) {
        this.turLicenciaRegList = turLicenciaRegList;
    }

    @XmlTransient
    public List<ConsultaTurLicenciaReg> getTurLicenciaRegList1() {
        return turLicenciaRegList1;
    }

    public void setTurLicenciaRegList1(List<ConsultaTurLicenciaReg> turLicenciaRegList1) {
        this.turLicenciaRegList1 = turLicenciaRegList1;
    }

    @XmlTransient
    public List<ConsultaTurLicenciaReg> getTurLicenciaRegList2() {
        return turLicenciaRegList2;
    }

    public void setTurLicenciaRegList2(List<ConsultaTurLicenciaReg> turLicenciaRegList2) {
        this.turLicenciaRegList2 = turLicenciaRegList2;
    }

    @XmlTransient
    public List<ConsultaTurLicenciaReg> getTurLicenciaRegList3() {
        return turLicenciaRegList3;
    }

    public void setTurLicenciaRegList3(List<ConsultaTurLicenciaReg> turLicenciaRegList3) {
        this.turLicenciaRegList3 = turLicenciaRegList3;
    }
    
}
