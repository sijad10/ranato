/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.data;

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
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.encuesta.data.SbRespuesta;
import pe.gob.sucamec.notificacion.data.NeDocumento;
import pe.gob.sucamec.notificacion.data.NeEvento;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_USUARIO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbUsuario.findAll", query = "SELECT s FROM SbUsuario s"),
    @NamedQuery(name = "SbUsuario.findById", query = "SELECT s FROM SbUsuario s WHERE s.id = :id"),
    @NamedQuery(name = "SbUsuario.findByLogin", query = "SELECT s FROM SbUsuario s WHERE s.login = :login"),
    @NamedQuery(name = "SbUsuario.findByClave", query = "SELECT s FROM SbUsuario s WHERE s.clave = :clave"),
    @NamedQuery(name = "SbUsuario.findByFechaIni", query = "SELECT s FROM SbUsuario s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SbUsuario.findByFechaFin", query = "SELECT s FROM SbUsuario s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SbUsuario.findByDescripcion", query = "SELECT s FROM SbUsuario s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SbUsuario.findByNumDoc", query = "SELECT s FROM SbUsuario s WHERE s.numDoc = :numDoc"),
    @NamedQuery(name = "SbUsuario.findByNombres", query = "SELECT s FROM SbUsuario s WHERE s.nombres = :nombres"),
    @NamedQuery(name = "SbUsuario.findByApePat", query = "SELECT s FROM SbUsuario s WHERE s.apePat = :apePat"),
    @NamedQuery(name = "SbUsuario.findByApeMat", query = "SELECT s FROM SbUsuario s WHERE s.apeMat = :apeMat"),
    @NamedQuery(name = "SbUsuario.findByCorreo", query = "SELECT s FROM SbUsuario s WHERE s.correo = :correo"),
    @NamedQuery(name = "SbUsuario.findByActivo", query = "SELECT s FROM SbUsuario s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbUsuario.findByAudLogin", query = "SELECT s FROM SbUsuario s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbUsuario.findByAudNumIp", query = "SELECT s FROM SbUsuario s WHERE s.audNumIp = :audNumIp")})
public class SbUsuario implements Serializable {
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_USUARIO")
    @SequenceGenerator(name = "SEQ_SB_USUARIO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_USUARIO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private List<NeDocumento> neDocumentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private List<NeEvento> neEventoList;
    private static final Long serialVersionUID = 1L;
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
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD_USUARIO")
    private Long cantidadUsuario;
    @ManyToMany(mappedBy = "sbUsuarioList2")
    private List<SbPerfil> sbPerfilList2;
    @JoinTable(name = "SB_PERFIL_USUARIO", schema = "BDINTEGRADO", joinColumns = {
        @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PERFIL_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbPerfil> sbPerfilList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private List<SbValidacionWeb> sbValidacionWebList;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaId;
    @JoinColumn(name = "TIPO_AUTEN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoAutenId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbTipo areaId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoId;
    @OneToMany(mappedBy = "usuarioId")
    private List<SbUsuario> sbUsuarioList1;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuario usuarioId;
    @JoinColumn(name = "FORMA_REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbTipo formaRegistroId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private List<SbRegistroUsuario> sbRegistroUsuarioList;
    @JoinColumn(name = "JEFE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuario jefeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private List<SbRespuesta> sbRespuestaList;

    public SbUsuario() {
    }

    public SbUsuario(Long id) {
        this.id = id;
    }

    public SbUsuario(Long id, String login, String clave, Date fechaIni, String numDoc, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.login = login;
        this.clave = clave;
        this.fechaIni = fechaIni;
        this.numDoc = numDoc;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public List<SbPerfil> getSbPerfilList2() {
        return sbPerfilList2;
    }

    public void setSbPerfilList2(List<SbPerfil> sbPerfilList2) {
        this.sbPerfilList2 = sbPerfilList2;
    }

    public Long getCantidadUsuario() {
        return cantidadUsuario;
    }

    public void setCantidadUsuario(Long cantidadUsuario) {
        this.cantidadUsuario = cantidadUsuario;
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
    public List<SbPerfil> getSbPerfilList() {
        return sbPerfilList;
    }

    public void setSbPerfilList(List<SbPerfil> sbPerfilList) {
        this.sbPerfilList = sbPerfilList;
    }

    @XmlTransient
    public List<SbValidacionWeb> getSbValidacionWebList() {
        return sbValidacionWebList;
    }

    public void setSbValidacionWebList(List<SbValidacionWeb> sbValidacionWebList) {
        this.sbValidacionWebList = sbValidacionWebList;
    }
    
    @XmlTransient
    public List<SbRespuesta> getSbRespuestaList() {
        return sbRespuestaList;
    }

    public void setSbRespuestaList(List<SbRespuesta> sbRespuestaList) {
        this.sbRespuestaList = sbRespuestaList;
    }

    public SbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersona personaId) {
        this.personaId = personaId;
    }

    public SbTipo getTipoAutenId() {
        return tipoAutenId;
    }

    public void setTipoAutenId(SbTipo tipoAutenId) {
        this.tipoAutenId = tipoAutenId;
    }

    public SbTipo getAreaId() {
        return areaId;
    }

    public void setAreaId(SbTipo areaId) {
        this.areaId = areaId;
    }

    public SbTipo getTipoId() {
        return tipoId;
    }

    public void setTipoId(SbTipo tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public List<SbUsuario> getSbUsuarioList1() {
        return sbUsuarioList1;
    }

    public void setSbUsuarioList1(List<SbUsuario> sbUsuarioList1) {
        this.sbUsuarioList1 = sbUsuarioList1;
    }

    public SbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    @XmlTransient
    public List<SbRegistroUsuario> getSbRegistroUsuarioList() {
        return sbRegistroUsuarioList;
    }

    public void setSbRegistroUsuarioList(List<SbRegistroUsuario> sbRegistroUsuarioList) {
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
        if (!(object instanceof SbUsuario)) {
            return false;
        }
        SbUsuario other = (SbUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbUsuario[ id=" + id + " ]";
    }

    @XmlTransient
    public List<NeDocumento> getNeDocumentoList() {
        return neDocumentoList;
    }

    public void setNeDocumentoList(List<NeDocumento> neDocumentoList) {
        this.neDocumentoList = neDocumentoList;
    }

    @XmlTransient
    public List<NeEvento> getNeEventoList() {
        return neEventoList;
    }

    public void setNeEventoList(List<NeEvento> neEventoList) {
        this.neEventoList = neEventoList;
    }
    
    public SbUsuario getJefeId() {
        return jefeId;
    }

    public void setJefeId(SbUsuario jefeId) {
        this.jefeId = jefeId;
    }

    public SbTipo getFormaRegistroId() {
        return formaRegistroId;
    }

    public void setFormaRegistroId(SbTipo formaRegistroId) {
        this.formaRegistroId = formaRegistroId;
    }
}
