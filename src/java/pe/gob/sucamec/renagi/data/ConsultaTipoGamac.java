/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.data;

import java.io.Serializable;
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
import pe.gob.sucamec.renagi.data.ConsultaTurLicenciaReg;
//import pe.gob.sucamec.renagi.data.TurTurno;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TIPO_GAMAC", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsultaTipoGamac.findAll", query = "SELECT t FROM ConsultaTipoGamac t"),
    @NamedQuery(name = "ConsultaTipoGamac.findById", query = "SELECT t FROM ConsultaTipoGamac t WHERE t.id = :id"),
    @NamedQuery(name = "ConsultaTipoGamac.findByNombre", query = "SELECT t FROM ConsultaTipoGamac t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "ConsultaTipoGamac.findByAbreviatura", query = "SELECT t FROM ConsultaTipoGamac t WHERE t.abreviatura = :abreviatura"),
    @NamedQuery(name = "ConsultaTipoGamac.findByDescripcion", query = "SELECT t FROM ConsultaTipoGamac t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "ConsultaTipoGamac.findByCodProg", query = "SELECT t FROM ConsultaTipoGamac t WHERE t.codProg = :codProg"),
    @NamedQuery(name = "ConsultaTipoGamac.findByOrden", query = "SELECT t FROM ConsultaTipoGamac t WHERE t.orden = :orden"),
    @NamedQuery(name = "ConsultaTipoGamac.findByActivo", query = "SELECT t FROM ConsultaTipoGamac t WHERE t.activo = :activo"),
    @NamedQuery(name = "ConsultaTipoGamac.findByAudLogin", query = "SELECT t FROM ConsultaTipoGamac t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "ConsultaTipoGamac.findByAudNumIp", query = "SELECT t FROM ConsultaTipoGamac t WHERE t.audNumIp = :audNumIp")})
public class ConsultaTipoGamac implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_GAMAC")
    @SequenceGenerator(name = "SEQ_CONSULTA_TIPO_GAMAC", schema = "BDINTEGRADO", sequenceName = "SEQ_TIPO_GAMAC", allocationSize = 1)
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
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinTable(schema = "BDINTEGRADO", name = "AMA_CATALOGO_MODALIDAD", joinColumns = {
        @JoinColumn(name = "TIPO_GAMAC_MODALIDAD_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "GAMAC_CATALOGO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<ConsultaAmaCatalogo> amaCatalogoList;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoGuiaId")
    private List<AmaGuiaTransito> amaGuiaTransitoList;*/
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modalidadId")
    private List<ConsultaAmaTipoLicencia> amaTipoLicenciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<ConsultaAmaLicenciaDeUso> amaLicenciaDeUsoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modalidadId")
    private List<ConsultaAmaTarjetaPropiedad> amaTarjetaPropiedadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<ConsultaAmaTarjetaPropiedad> amaTarjetaPropiedadList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restriccionId")
    private List<ConsultaAmaTarjetaPropiedad> amaTarjetaPropiedadList2;
    @OneToMany(mappedBy = "tipoId")
    private List<ConsultaTipoGamac> tipoGamacList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private ConsultaTipoGamac tipoId;
    
    //REGULARIZACION
    /*@JoinTable(schema = "BDINTEGRADO", name = "TUR_MODA_LICEN_REG", joinColumns = {
        @JoinColumn(name = "MODALIDAD_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "LICENCIA_ID", referencedColumnName = "ID")})*/
    
    @ManyToMany
    private List<ConsultaTurLicenciaReg> turLicenciaRegList;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoTramiteId")
    private List<TurTurno> turTurnoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private List<TurTurno> turTurnoList1;*/
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoLicId")
    private List<ConsultaTurLicenciaReg> turLicenciaRegList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstado")
    private List<ConsultaTurLicenciaReg> turLicenciaRegList2;

    public ConsultaTipoGamac() {
    }

    public ConsultaTipoGamac(Long id) {
        this.id = id;
    }

    public ConsultaTipoGamac(Long id, String nombre, String abreviatura, String descripcion, String codProg, short orden, short activo, String audLogin, String audNumIp) {
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
    public List<ConsultaAmaCatalogo> getAmaCatalogoList() {
        return amaCatalogoList;
    }

    public void setAmaCatalogoList(List<ConsultaAmaCatalogo> amaCatalogoList) {
        this.amaCatalogoList = amaCatalogoList;
    }

    @XmlTransient
    public List<ConsultaAmaTarjetaPropiedad> getAmaTarjetaPropiedadList() {
        return amaTarjetaPropiedadList;
    }

    public void setAmaTarjetaPropiedadList(List<ConsultaAmaTarjetaPropiedad> amaTarjetaPropiedadList) {
        this.amaTarjetaPropiedadList = amaTarjetaPropiedadList;
    }

    @XmlTransient
    public List<ConsultaAmaTarjetaPropiedad> getAmaTarjetaPropiedadList1() {
        return amaTarjetaPropiedadList1;
    }

    public void setAmaTarjetaPropiedadList1(List<ConsultaAmaTarjetaPropiedad> amaTarjetaPropiedadList1) {
        this.amaTarjetaPropiedadList1 = amaTarjetaPropiedadList1;
    }

    @XmlTransient
    public List<ConsultaAmaTarjetaPropiedad> getAmaTarjetaPropiedadList2() {
        return amaTarjetaPropiedadList2;
    }

    public void setAmaTarjetaPropiedadList2(List<ConsultaAmaTarjetaPropiedad> amaTarjetaPropiedadList2) {
        this.amaTarjetaPropiedadList2 = amaTarjetaPropiedadList2;
    }

    /*@XmlTransient
    public List<AmaGuiaTransito> getAmaGuiaTransitoList() {
        return amaGuiaTransitoList;
    }

    public void setAmaGuiaTransitoList(List<AmaGuiaTransito> amaGuiaTransitoList) {
        this.amaGuiaTransitoList = amaGuiaTransitoList;
    }*/

    @XmlTransient
    public List<ConsultaAmaTipoLicencia> getAmaTipoLicenciaList() {
        return amaTipoLicenciaList;
    }

    public void setAmaTipoLicenciaList(List<ConsultaAmaTipoLicencia> amaTipoLicenciaList) {
        this.amaTipoLicenciaList = amaTipoLicenciaList;
    }

    @XmlTransient
    public List<ConsultaAmaLicenciaDeUso> getAmaLicenciaDeUsoList() {
        return amaLicenciaDeUsoList;
    }

    public void setAmaLicenciaDeUsoList(List<ConsultaAmaLicenciaDeUso> amaLicenciaDeUsoList) {
        this.amaLicenciaDeUsoList = amaLicenciaDeUsoList;
    }
    

    @XmlTransient
    public List<ConsultaTipoGamac> getTipoGamacList() {
        return tipoGamacList;
    }

    public void setTipoGamacList(List<ConsultaTipoGamac> tipoGamacList) {
        this.tipoGamacList = tipoGamacList;
    }

    public ConsultaTipoGamac getTipoId() {
        return tipoId;
    }

    public void setTipoId(ConsultaTipoGamac tipoId) {
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
        if (!(object instanceof ConsultaTipoGamac)) {
            return false;
        }
        ConsultaTipoGamac other = (ConsultaTipoGamac) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemagamac.data.TipoGamac[ id=" + id + " ]";
    }

    @XmlTransient
    public List<ConsultaTurLicenciaReg> getTurLicenciaRegList() {
        return turLicenciaRegList;
    }

    public void setTurLicenciaRegList(List<ConsultaTurLicenciaReg> turLicenciaRegList) {
        this.turLicenciaRegList = turLicenciaRegList;
    }

    /*@XmlTransient
    public List<TurTurno> getTurTurnoList() {
        return turTurnoList;
    }

    @XmlTransient
    public void setTurTurnoList(List<TurTurno> turTurnoList) {
        this.turTurnoList = turTurnoList;
    }

    public List<TurTurno> getTurTurnoList1() {
        return turTurnoList1;
    }

    public void setTurTurnoList1(List<TurTurno> turTurnoList1) {
        this.turTurnoList1 = turTurnoList1;
    }*/

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

}
