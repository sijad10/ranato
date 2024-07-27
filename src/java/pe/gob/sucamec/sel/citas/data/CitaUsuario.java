/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author msalinas
 */
@Entity
@Table(name = "USUARIO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaUsuario.findAll", query = "SELECT u FROM CitaUsuario u"),
    @NamedQuery(name = "CitaUsuario.findById", query = "SELECT u FROM CitaUsuario u WHERE u.id = :id"),
    @NamedQuery(name = "CitaUsuario.findByLogin", query = "SELECT u FROM CitaUsuario u WHERE u.login = :login"),
    @NamedQuery(name = "CitaUsuario.findByClave", query = "SELECT u FROM CitaUsuario u WHERE u.clave = :clave"),
    @NamedQuery(name = "CitaUsuario.findByFechaIni", query = "SELECT u FROM CitaUsuario u WHERE u.fechaIni = :fechaIni"),
    @NamedQuery(name = "CitaUsuario.findByFechaFin", query = "SELECT u FROM CitaUsuario u WHERE u.fechaFin = :fechaFin"),
    @NamedQuery(name = "CitaUsuario.findByDescripcion", query = "SELECT u FROM CitaUsuario u WHERE u.descripcion = :descripcion"),
    @NamedQuery(name = "CitaUsuario.findByNumDoc", query = "SELECT u FROM CitaUsuario u WHERE u.numDoc = :numDoc"),
    @NamedQuery(name = "CitaUsuario.findByNombres", query = "SELECT u FROM CitaUsuario u WHERE u.nombres = :nombres"),
    @NamedQuery(name = "CitaUsuario.findByApePat", query = "SELECT u FROM CitaUsuario u WHERE u.apePat = :apePat"),
    @NamedQuery(name = "CitaUsuario.findByApeMat", query = "SELECT u FROM CitaUsuario u WHERE u.apeMat = :apeMat"),
    @NamedQuery(name = "CitaUsuario.findByCorreo", query = "SELECT u FROM CitaUsuario u WHERE u.correo = :correo"),
    @NamedQuery(name = "CitaUsuario.findByActivo", query = "SELECT u FROM CitaUsuario u WHERE u.activo = :activo"),
    @NamedQuery(name = "CitaUsuario.findByAudLogin", query = "SELECT u FROM CitaUsuario u WHERE u.audLogin = :audLogin"),
    @NamedQuery(name = "CitaUsuario.findByAudNumIp", query = "SELECT u FROM CitaUsuario u WHERE u.audNumIp = :audNumIp")})
public class CitaUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
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
    @OneToMany(mappedBy = "usuarioId")
    private List<CitaUsuario> usuarioList;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaUsuario usuarioId;
    @OneToMany(mappedBy = "jefeId")
    private List<CitaUsuario> usuarioList1;
    @JoinColumn(name = "JEFE_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaUsuario jefeId;
    @JoinColumn(name = "TIPO_AUTEN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTipoBase tipoAutenId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoBase areaId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTipoBase tipoId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTurPersona personaId;

    public CitaUsuario() {
    }

    public CitaUsuario(Long id) {
        this.id = id;
    }

    public CitaUsuario(Long id, String login, String clave, Date fechaIni, String numDoc, short activo, String audLogin, String audNumIp) {
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
    public List<CitaUsuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<CitaUsuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public CitaUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(CitaUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    @XmlTransient
    public List<CitaUsuario> getUsuarioList1() {
        return usuarioList1;
    }

    public void setUsuarioList1(List<CitaUsuario> usuarioList1) {
        this.usuarioList1 = usuarioList1;
    }

    public CitaUsuario getJefeId() {
        return jefeId;
    }

    public void setJefeId(CitaUsuario jefeId) {
        this.jefeId = jefeId;
    }

    public CitaTipoBase getTipoAutenId() {
        return tipoAutenId;
    }

    public void setTipoAutenId(CitaTipoBase tipoAutenId) {
        this.tipoAutenId = tipoAutenId;
    }

    public CitaTipoBase getAreaId() {
        return areaId;
    }

    public void setAreaId(CitaTipoBase areaId) {
        this.areaId = areaId;
    }

    public CitaTipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(CitaTipoBase tipoId) {
        this.tipoId = tipoId;
    }

    public CitaTurPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(CitaTurPersona personaId) {
        this.personaId = personaId;
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
        if (!(object instanceof CitaUsuario)) {
            return false;
        }
        CitaUsuario other = (CitaUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.citas.data.CitaUsuario[ id=" + id + " ]";
    }

}
