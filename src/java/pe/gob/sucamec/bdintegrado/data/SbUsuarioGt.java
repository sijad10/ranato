/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.sistemabase.data.SbTipo;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_USUARIO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbUsuarioGt.findAll", query = "SELECT s FROM SbUsuarioGt s"),
    @NamedQuery(name = "SbUsuarioGt.findById", query = "SELECT s FROM SbUsuarioGt s WHERE s.id = :id"),
    @NamedQuery(name = "SbUsuarioGt.findByLogin", query = "SELECT s FROM SbUsuarioGt s WHERE s.login = :login"),
    @NamedQuery(name = "SbUsuarioGt.findByClave", query = "SELECT s FROM SbUsuarioGt s WHERE s.clave = :clave"),
    @NamedQuery(name = "SbUsuarioGt.findByFechaIni", query = "SELECT s FROM SbUsuarioGt s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SbUsuarioGt.findByFechaFin", query = "SELECT s FROM SbUsuarioGt s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SbUsuarioGt.findByDescripcion", query = "SELECT s FROM SbUsuarioGt s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SbUsuarioGt.findByNumDoc", query = "SELECT s FROM SbUsuarioGt s WHERE s.numDoc = :numDoc"),
    @NamedQuery(name = "SbUsuarioGt.findByNombres", query = "SELECT s FROM SbUsuarioGt s WHERE s.nombres = :nombres"),
    @NamedQuery(name = "SbUsuarioGt.findByApePat", query = "SELECT s FROM SbUsuarioGt s WHERE s.apePat = :apePat"),
    @NamedQuery(name = "SbUsuarioGt.findByApeMat", query = "SELECT s FROM SbUsuarioGt s WHERE s.apeMat = :apeMat"),
    @NamedQuery(name = "SbUsuarioGt.findByCorreo", query = "SELECT s FROM SbUsuarioGt s WHERE s.correo = :correo"),
    @NamedQuery(name = "SbUsuarioGt.findByActivo", query = "SELECT s FROM SbUsuarioGt s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbUsuarioGt.findByAudLogin", query = "SELECT s FROM SbUsuarioGt s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbUsuarioGt.findByAudNumIp", query = "SELECT s FROM SbUsuarioGt s WHERE s.audNumIp = :audNumIp")})
public class SbUsuarioGt implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_USUARIO")
    @SequenceGenerator(name = "SEQ_SB_USUARIO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_USUARIO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "LOGIN")
    private String login;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CLAVE")
    private String clave;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Size(max = 300)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NUM_DOC")
    private String numDoc;
    @Size(max = 200)
    @Column(name = "NOMBRES")
    private String nombres;
    @Size(max = 200)
    @Column(name = "APE_PAT")
    private String apePat;
    @Size(max = 200)
    @Column(name = "APE_MAT")
    private String apeMat;
    @Size(max = 300)
    @Column(name = "CORREO")
    private String correo;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD_USUARIO")
    private Long cantidadUsuario;
    @JoinTable(name = "SB_PERFIL_USUARIO", schema = "BDINTEGRADO", joinColumns = {
        @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PERFIL_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbPerfilGt> sbPerfilList;
    @JoinColumn(name = "TIPO_AUTEN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoAutenId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbTipo areaId;
    @JoinColumn(name = "FORMA_REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt formaRegistroId;
    @OneToMany(mappedBy = "usuarioId")
    private List<SbUsuarioGt> sbUsuarioList;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuarioGt usuarioId;
    @OneToMany(mappedBy = "jefeId")
    private List<SbUsuarioGt> sbUsuarioList1;
    @JoinColumn(name = "JEFE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuarioGt jefeId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<EppLibroEventos> eppLibroEventosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private List<SbValidacionWebGt> sbValidacionWebList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<EppRegistroEvento> eppRegistroEventoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private List<SbRegistroUsuarioGt> sbRegistroUsuarioList;

    public SbUsuarioGt() {
    }

    public SbUsuarioGt(Long id) {
        this.id = id;
    }

    public SbUsuarioGt(Long id, String login, String clave, Date fechaIni, String numDoc, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.login = login;
        this.clave = clave;
        this.fechaIni = fechaIni;
        this.numDoc = numDoc;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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
    public List<SbPerfilGt> getSbPerfilList() {
        return sbPerfilList;
    }

    public void setSbPerfilList(List<SbPerfilGt> sbPerfilList) {
        this.sbPerfilList = sbPerfilList;
    }

    public TipoBaseGt getTipoAutenId() {
        return tipoAutenId;
    }

    public void setTipoAutenId(TipoBaseGt tipoAutenId) {
        this.tipoAutenId = tipoAutenId;
    }

    public TipoBaseGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBaseGt tipoId) {
        this.tipoId = tipoId;
    }

    public SbTipo getAreaId() {
        return areaId;
    }

    public void setAreaId(SbTipo areaId) {
        this.areaId = areaId;
    }

    @XmlTransient
    public List<SbUsuarioGt> getSbUsuarioGtList() {
        return sbUsuarioList;
    }

    public void setSbUsuarioGtList(List<SbUsuarioGt> sbUsuarioList) {
        this.sbUsuarioList = sbUsuarioList;
    }

    public SbUsuarioGt getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuarioGt usuarioId) {
        this.usuarioId = usuarioId;
    }

    @XmlTransient
    public List<SbUsuarioGt> getSbUsuarioGtList1() {
        return sbUsuarioList1;
    }

    public void setSbUsuarioGtList1(List<SbUsuarioGt> sbUsuarioList1) {
        this.sbUsuarioList1 = sbUsuarioList1;
    }

    public SbUsuarioGt getJefeId() {
        return jefeId;
    }

    public void setJefeId(SbUsuarioGt jefeId) {
        this.jefeId = jefeId;
    }

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
    }

    @XmlTransient
    public List<EppLibroEventos> getEppLibroEventosList() {
        return eppLibroEventosList;
    }

    public void setEppLibroEventosList(List<EppLibroEventos> eppLibroEventosList) {
        this.eppLibroEventosList = eppLibroEventosList;
    }

    @XmlTransient
    public List<SbValidacionWebGt> getSbValidacionWebList() {
        return sbValidacionWebList;
    }

    public void setSbValidacionWebList(List<SbValidacionWebGt> sbValidacionWebList) {
        this.sbValidacionWebList = sbValidacionWebList;
    }

    @XmlTransient
    public List<EppRegistroEvento> getEppRegistroEventoList() {
        return eppRegistroEventoList;
    }

    public void setEppRegistroEventoList(List<EppRegistroEvento> eppRegistroEventoList) {
        this.eppRegistroEventoList = eppRegistroEventoList;
    }

    @XmlTransient
    public List<SbRegistroUsuarioGt> getSbRegistroUsuarioList() {
        return sbRegistroUsuarioList;
    }

    public void setSbRegistroUsuarioList(List<SbRegistroUsuarioGt> sbRegistroUsuarioList) {
        this.sbRegistroUsuarioList = sbRegistroUsuarioList;
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
        if (!(object instanceof SbUsuarioGt)) {
            return false;
        }
        SbUsuarioGt other = (SbUsuarioGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.SbUsuarioGt[ id=" + id + " ]";
    }

    /**
     * @return the cantidadUsuario
     */
    public Long getCantidadUsuario() {
        return cantidadUsuario;
    }

    /**
     * @param cantidadUsuario the cantidadUsuario to set
     */
    public void setCantidadUsuario(Long cantidadUsuario) {
        this.cantidadUsuario = cantidadUsuario;
    }

    public TipoBaseGt getFormaRegistroId() {
        return formaRegistroId;
    }

    public void setFormaRegistroId(TipoBaseGt formaRegistroId) {
        this.formaRegistroId = formaRegistroId;
    }
    
    public String getNombresCompletosUsuario(){
        String nombre = nombres + " " + apePat + " " + ((apeMat != null)?apeMat:"");
        return nombre;
    }
}
