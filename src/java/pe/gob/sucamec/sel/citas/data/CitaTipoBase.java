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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.bdintegrado.data.Tickets;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author msalinas
 */
@Entity
@Customizer(pe.gob.sucamec.sel.citas.data.AuditoriaEntidad.class)
@Table(name = "TIPO_BASE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaTipoBase.findAll", query = "SELECT t FROM CitaTipoBase t"),
    @NamedQuery(name = "CitaTipoBase.findById", query = "SELECT t FROM CitaTipoBase t WHERE t.id = :id"),
    @NamedQuery(name = "CitaTipoBase.findByNombre", query = "SELECT t FROM CitaTipoBase t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "CitaTipoBase.findByAbreviatura", query = "SELECT t FROM CitaTipoBase t WHERE t.abreviatura = :abreviatura"),
    @NamedQuery(name = "CitaTipoBase.findByDescripcion", query = "SELECT t FROM CitaTipoBase t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "CitaTipoBase.findByCodProg", query = "SELECT t FROM CitaTipoBase t WHERE t.codProg = :codProg"),
    @NamedQuery(name = "CitaTipoBase.findByOrden", query = "SELECT t FROM CitaTipoBase t WHERE t.orden = :orden"),
    @NamedQuery(name = "CitaTipoBase.findByActivo", query = "SELECT t FROM CitaTipoBase t WHERE t.activo = :activo"),
    @NamedQuery(name = "CitaTipoBase.findByAudLogin", query = "SELECT t FROM CitaTipoBase t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "CitaTipoBase.findByAudNumIp", query = "SELECT t FROM CitaTipoBase t WHERE t.audNumIp = :audNumIp")})
public class CitaTipoBase implements Serializable {

    @OneToMany(mappedBy = "areaId")
    private List<SbUsuarioGt> sbUsuarioGtList;
    @OneToMany(mappedBy = "formaRegistroId")
    private List<SbUsuarioGt> sbUsuarioGtList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbUsuarioGt> sbUsuarioGtList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAutenId")
    private List<SbUsuarioGt> sbUsuarioGtList3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "area")
    private List<Tickets> ticketsList;
    @OneToMany(mappedBy = "estCivilId")
    private List<SbPersona> sbPersonaList;
    @OneToMany(mappedBy = "generoId")
    private List<SbPersona> sbPersonaList1;
    @OneToMany(mappedBy = "tipoDoc")
    private List<SbPersona> sbPersonaList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbPersona> sbPersonaList3;
    @OneToMany(mappedBy = "instituLabId")
    private List<SbPersona> sbPersonaList4;
    @OneToMany(mappedBy = "ocupacionId")
    private List<SbPersona> sbPersonaList5;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private List<CitaSbNumeracion> sbNumeracionList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAutenId")
    private List<CitaUsuario> usuarioList;
    @OneToMany(mappedBy = "areaId")
    private List<CitaUsuario> usuarioList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<CitaUsuario> usuarioList2;
    private static final long serialVersionUID = 1L;
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
    @OneToMany(mappedBy = "tipoDoc")
    private List<CitaTurPersona> personaList;
    @OneToMany(mappedBy = "generoId")
    private List<CitaTurPersona> personaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<CitaTurPersona> personaList2;
    @OneToMany(mappedBy = "estCivilId")
    private List<CitaTurPersona> personaList3;
    @OneToMany(mappedBy = "tipoId")
    private List<CitaTipoBase> tipoBaseList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoBase tipoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bancoId")
    private List<CitaTurComprobante> turComprobanteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoTurno")
    private List<CitaTurProgramacion> turProgramacionList;

    public CitaTipoBase() {
    }

    public CitaTipoBase(Long id) {
        this.id = id;
    }

    public CitaTipoBase(Long id, String nombre, String codProg, short activo, String audLogin, String audNumIp) {
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

    @XmlTransient
    public List<CitaTurPersona> getPersonaList() {
        return personaList;
    }

    public void setPersonaList(List<CitaTurPersona> personaList) {
        this.personaList = personaList;
    }

    @XmlTransient
    public List<CitaTurPersona> getPersonaList1() {
        return personaList1;
    }

    public void setPersonaList1(List<CitaTurPersona> personaList1) {
        this.personaList1 = personaList1;
    }

    @XmlTransient
    public List<CitaTurPersona> getPersonaList2() {
        return personaList2;
    }

    public void setPersonaList2(List<CitaTurPersona> personaList2) {
        this.personaList2 = personaList2;
    }

    @XmlTransient
    public List<CitaTurPersona> getPersonaList3() {
        return personaList3;
    }

    public void setPersonaList3(List<CitaTurPersona> personaList3) {
        this.personaList3 = personaList3;
    }

    @XmlTransient
    public List<CitaTipoBase> getTipoBaseList() {
        return tipoBaseList;
    }

    public void setTipoBaseList(List<CitaTipoBase> tipoBaseList) {
        this.tipoBaseList = tipoBaseList;
    }

    public CitaTipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(CitaTipoBase tipoId) {
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
    public List<CitaTurProgramacion> getTurProgramacionList() {
        return turProgramacionList;
    }

    public void setTurProgramacionList(List<CitaTurProgramacion> turProgramacionList) {
        this.turProgramacionList = turProgramacionList;
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
        if (!(object instanceof CitaTipoBase)) {
            return false;
        }
        CitaTipoBase other = (CitaTipoBase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.ciitas.data.CitaTipoBase[ id=" + id + " ]";
    }

    @XmlTransient
    public List<CitaUsuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<CitaUsuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @XmlTransient
    public List<CitaUsuario> getUsuarioList1() {
        return usuarioList1;
    }

    public void setUsuarioList1(List<CitaUsuario> usuarioList1) {
        this.usuarioList1 = usuarioList1;
    }

    @XmlTransient
    public List<CitaUsuario> getUsuarioList2() {
        return usuarioList2;
    }

    public void setUsuarioList2(List<CitaUsuario> usuarioList2) {
        this.usuarioList2 = usuarioList2;
    }

    @XmlTransient
    public List<CitaSbNumeracion> getSbNumeracionList() {
        return sbNumeracionList;
    }

    public void setSbNumeracionList(List<CitaSbNumeracion> sbNumeracionList) {
        this.sbNumeracionList = sbNumeracionList;
    }

    @XmlTransient
    public List<SbUsuarioGt> getSbUsuarioGtList() {
        return sbUsuarioGtList;
    }

    public void setSbUsuarioGtList(List<SbUsuarioGt> sbUsuarioGtList) {
        this.sbUsuarioGtList = sbUsuarioGtList;
    }

    @XmlTransient
    public List<SbUsuarioGt> getSbUsuarioGtList1() {
        return sbUsuarioGtList1;
    }

    public void setSbUsuarioGtList1(List<SbUsuarioGt> sbUsuarioGtList1) {
        this.sbUsuarioGtList1 = sbUsuarioGtList1;
    }

    @XmlTransient
    public List<SbUsuarioGt> getSbUsuarioGtList2() {
        return sbUsuarioGtList2;
    }

    public void setSbUsuarioGtList2(List<SbUsuarioGt> sbUsuarioGtList2) {
        this.sbUsuarioGtList2 = sbUsuarioGtList2;
    }

    @XmlTransient
    public List<SbUsuarioGt> getSbUsuarioGtList3() {
        return sbUsuarioGtList3;
    }

    public void setSbUsuarioGtList3(List<SbUsuarioGt> sbUsuarioGtList3) {
        this.sbUsuarioGtList3 = sbUsuarioGtList3;
    }

    @XmlTransient
    public List<Tickets> getTicketsList() {
        return ticketsList;
    }

    public void setTicketsList(List<Tickets> ticketsList) {
        this.ticketsList = ticketsList;
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
    public List<SbPersona> getSbPersonaList4() {
        return sbPersonaList4;
    }

    public void setSbPersonaList4(List<SbPersona> sbPersonaList4) {
        this.sbPersonaList4 = sbPersonaList4;
    }

    @XmlTransient
    public List<SbPersona> getSbPersonaList5() {
        return sbPersonaList5;
    }

    public void setSbPersonaList5(List<SbPersona> sbPersonaList5) {
        this.sbPersonaList5 = sbPersonaList5;
    }

}
