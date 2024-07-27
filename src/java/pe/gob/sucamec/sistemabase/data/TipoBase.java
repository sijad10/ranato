/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.data;

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
import pe.gob.sucamec.bdintegrado.data.SbTupaRequisito;
import pe.gob.sucamec.bdintegrado.data.TicketArchivo;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TIPO_BASE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoBase.findAll", query = "SELECT t FROM TipoBase t"),
    @NamedQuery(name = "TipoBase.findById", query = "SELECT t FROM TipoBase t WHERE t.id = :id"),
    @NamedQuery(name = "TipoBase.findByNombre", query = "SELECT t FROM TipoBase t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoBase.findByAbreviatura", query = "SELECT t FROM TipoBase t WHERE t.abreviatura = :abreviatura"),
    @NamedQuery(name = "TipoBase.findByDescripcion", query = "SELECT t FROM TipoBase t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TipoBase.findByCodProg", query = "SELECT t FROM TipoBase t WHERE t.codProg = :codProg"),
    @NamedQuery(name = "TipoBase.findByOrden", query = "SELECT t FROM TipoBase t WHERE t.orden = :orden"),
    @NamedQuery(name = "TipoBase.findByActivo", query = "SELECT t FROM TipoBase t WHERE t.activo = :activo"),
    @NamedQuery(name = "TipoBase.findByAudLogin", query = "SELECT t FROM TipoBase t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TipoBase.findByAudNumIp", query = "SELECT t FROM TipoBase t WHERE t.audNumIp = :audNumIp")})
public class TipoBase implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_BASE")
    @SequenceGenerator(name = "SEQ_TIPO_BASE", schema = "BDINTEGRADO", sequenceName = "SEQ_TIPO_BASE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "COD_PROG")
    private String codProg;
    @Column(name = "ORDEN")
    private Integer orden;
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
    @OneToMany(mappedBy = "tipoId")
    private List<TicketArchivo> ticketArchivoList;
    @OneToMany(mappedBy = "areaId")
    private List<SbUsuario> sbUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbUsuario> sbUsuarioList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAutenId")
    private List<SbUsuario> sbUsuarioList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbLogUsuario> sbLogUsuarioList;
    
    @OneToMany(mappedBy = "ocupacionId")
    private List<SbPersona> sbPersonaList5;

    @OneToMany(mappedBy = "tipoId")
    private List<TipoBase> tipoBaseList;

    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBase tipoId;

    @OneToMany(mappedBy = "tipoDoc")
    private List<SbPersona> sbPersonaList;
    @OneToMany(mappedBy = "generoId")
    private List<SbPersona> sbPersonaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbPersona> sbPersonaList2;
    @OneToMany(mappedBy = "estCivilId")
    private List<SbPersona> sbPersonaList3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbRelacionPersona> sbRelacionPersonaList;    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbMedioContacto> sbMedioContactoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPrioridad")
    private List<SbMedioContacto> sbMedioContactoList1;
    @JoinTable(schema="BDINTEGRADO", name = "SB_TUPA_REQUISITO_FORMATO", joinColumns = {
        @JoinColumn(name = "FORMATO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TUPA_REQUISITO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbTupaRequisito> sbTupaRequisitoList;

    public List<SbTupaRequisito> getSbTupaRequisitoList() {
        return sbTupaRequisitoList;
    }

    public void setSbTupaRequisitoList(List<SbTupaRequisito> sbTupaRequisitoList) {
        this.sbTupaRequisitoList = sbTupaRequisitoList;
    }
    
    public TipoBase() {
    }

    public TipoBase(Long id) {
        this.id = id;
    }

    public TipoBase(Long id, String nombre, String codProg, short activo, String audLogin, String audNumIp) {
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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
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

    public List<SbPersona> getSbPersonaList5() {
        return sbPersonaList5;
    }

    @XmlTransient
    public List<TicketArchivo> getTicketArchivoList() {
        return ticketArchivoList;
    }

    public void setTicketArchivoList(List<TicketArchivo> ticketArchivoList) {
        this.ticketArchivoList = ticketArchivoList;
    }
    
    @XmlTransient
    public List<SbUsuario> getSbUsuarioList() {
        return sbUsuarioList;
    }

    public void setSbUsuarioList(List<SbUsuario> sbUsuarioList) {
        this.sbUsuarioList = sbUsuarioList;
    }

    @XmlTransient
    public List<SbMedioContacto> getSbMedioContactoList1() {
        return sbMedioContactoList1;
    }

    public void setSbMedioContactoList1(List<SbMedioContacto> sbMedioContactoList1) {
        this.sbMedioContactoList1 = sbMedioContactoList1;
    }

    @XmlTransient
    public List<SbUsuario> getSbUsuarioList1() {
        return sbUsuarioList1;
    }

    public void setSbUsuarioList1(List<SbUsuario> sbUsuarioList1) {
        this.sbUsuarioList1 = sbUsuarioList1;
    }

    @XmlTransient
    public List<SbUsuario> getSbUsuarioList2() {
        return sbUsuarioList2;
    }

    public void setSbUsuarioList2(List<SbUsuario> sbUsuarioList2) {
        this.sbUsuarioList2 = sbUsuarioList2;
    }

    @XmlTransient
    public List<SbLogUsuario> getSbLogUsuarioList() {
        return sbLogUsuarioList;
    }

    public void setSbLogUsuarioList(List<SbLogUsuario> sbLogUsuarioList) {
        this.sbLogUsuarioList = sbLogUsuarioList;
    }

    @XmlTransient
    public List<TipoBase> getTipoBaseList() {
        return tipoBaseList;
    }

    public void setTipoBaseList(List<TipoBase> tipoBaseList) {
        this.tipoBaseList = tipoBaseList;
    }

    public TipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBase tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public List<SbPersona> getSbPersonaList() {
        return sbPersonaList;
    }

    public void setSbPersonaList(List<SbPersona> sbPersonaList) {
        this.sbPersonaList = sbPersonaList;
    }

    @XmlTransient
    public List<SbPersona> getSbPersonaList1() {
        return sbPersonaList1;
    }

    public void setSbPersonaList1(List<SbPersona> sbPersonaList1) {
        this.sbPersonaList1 = sbPersonaList1;
    }

    @XmlTransient
    public List<SbPersona> getSbPersonaList2() {
        return sbPersonaList2;
    }

    public void setSbPersonaList2(List<SbPersona> sbPersonaList2) {
        this.sbPersonaList2 = sbPersonaList2;
    }

    @XmlTransient
    public List<SbPersona> getSbPersonaList3() {
        return sbPersonaList3;
    }

    public void setSbPersonaList3(List<SbPersona> sbPersonaList3) {
        this.sbPersonaList3 = sbPersonaList3;
    }

    @XmlTransient
    public List<SbRelacionPersona> getSbRelacionPersonaList() {
        return sbRelacionPersonaList;
    }

    public void setSbRelacionPersonaList(List<SbRelacionPersona> sbRelacionPersonaList) {
        this.sbRelacionPersonaList = sbRelacionPersonaList;
    }

    @XmlTransient
    public List<SbMedioContacto> getSbMedioContactoList() {
        return sbMedioContactoList;
    }

    public void setSbMedioContactoList(List<SbMedioContacto> sbMedioContactoList) {
        this.sbMedioContactoList = sbMedioContactoList;
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
        if (!(object instanceof TipoBase)) {
            return false;
        }
        TipoBase other = (TipoBase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemaInterno.data.TipoBase[ id=" + id + " ]";
    }

}
