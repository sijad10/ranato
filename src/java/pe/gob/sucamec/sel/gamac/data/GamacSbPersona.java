/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import pe.gob.sucamec.bdintegrado.data.SbAsientoPersona;
import pe.gob.sucamec.sistemabase.data.SbRelacionPersona;

/**
 *
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_PERSONA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacSbPersona.findAll", query = "SELECT g FROM GamacSbPersona g"),
    @NamedQuery(name = "GamacSbPersona.findById", query = "SELECT g FROM GamacSbPersona g WHERE g.id = :id"),
    @NamedQuery(name = "GamacSbPersona.findByRuc", query = "SELECT g FROM GamacSbPersona g WHERE g.ruc = :ruc"),
    @NamedQuery(name = "GamacSbPersona.findByRznSocial", query = "SELECT g FROM GamacSbPersona g WHERE g.rznSocial = :rznSocial"),
    @NamedQuery(name = "GamacSbPersona.findByNomCom", query = "SELECT g FROM GamacSbPersona g WHERE g.nomCom = :nomCom"),
    @NamedQuery(name = "GamacSbPersona.findByNumDoc", query = "SELECT g FROM GamacSbPersona g WHERE g.numDoc = :numDoc"),
    @NamedQuery(name = "GamacSbPersona.findByNumDocVal", query = "SELECT g FROM GamacSbPersona g WHERE g.numDocVal = :numDocVal"),
    @NamedQuery(name = "GamacSbPersona.findByApePat", query = "SELECT g FROM GamacSbPersona g WHERE g.apePat = :apePat"),
    @NamedQuery(name = "GamacSbPersona.findByApeMat", query = "SELECT g FROM GamacSbPersona g WHERE g.apeMat = :apeMat"),
    @NamedQuery(name = "GamacSbPersona.findByNombres", query = "SELECT g FROM GamacSbPersona g WHERE g.nombres = :nombres"),
    @NamedQuery(name = "GamacSbPersona.findByFechaNac", query = "SELECT g FROM GamacSbPersona g WHERE g.fechaNac = :fechaNac"),
    @NamedQuery(name = "GamacSbPersona.findByEnActividad", query = "SELECT g FROM GamacSbPersona g WHERE g.enActividad = :enActividad"),
    @NamedQuery(name = "GamacSbPersona.findByFechaReg", query = "SELECT g FROM GamacSbPersona g WHERE g.fechaReg = :fechaReg"),
    @NamedQuery(name = "GamacSbPersona.findByActivo", query = "SELECT g FROM GamacSbPersona g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacSbPersona.findByAudLogin", query = "SELECT g FROM GamacSbPersona g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacSbPersona.findByAudNumIp", query = "SELECT g FROM GamacSbPersona g WHERE g.audNumIp = :audNumIp"),
    @NamedQuery(name = "GamacSbPersona.findByNroCip", query = "SELECT g FROM GamacSbPersona g WHERE g.nroCip = :nroCip")})
public class GamacSbPersona implements Serializable {

    @Size(max = 25)
    @Column(name = "NRO_LICENCIA_CAF")
    private String nroLicenciaCaf;
    @Column(name = "FEC_INICIO_SERVICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecInicioServicio;
    @Column(name = "FEC_FIN_SERVICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecFinServicio;
    @Size(max = 20)
    @Column(name = "NRO_CE")
    private String nroCe;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<SbAsientoPersona> sbAsientoPersonaList;

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GAMAC_SB_PERSONA")
    @SequenceGenerator(name = "SEQ_GAMAC_SB_PERSONA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_PERSONA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 11)
    @Column(name = "RUC")
    private String ruc;
    @Size(max = 300)
    @Column(name = "RZN_SOCIAL")
    private String rznSocial;
    @Size(max = 300)
    @Column(name = "NOM_COM")
    private String nomCom;
    @Size(max = 20)
    @Column(name = "NUM_DOC")
    private String numDoc;
    @Size(max = 10)
    @Column(name = "NUM_DOC_VAL")
    private String numDocVal;
    @Size(max = 200)
    @Column(name = "APE_PAT")
    private String apePat;
    @Size(max = 200)
    @Column(name = "APE_MAT")
    private String apeMat;
    @Size(max = 200)
    @Column(name = "NOMBRES")
    private String nombres;
    @Column(name = "FECHA_NAC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNac;
    @Column(name = "EN_ACTIVIDAD")
    private Short enActividad;
    @Column(name = "FECHA_REG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReg;
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
    @Size(max = 12)
    @Column(name = "NRO_CIP")
    private String nroCip;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private Collection<GamacSbUsuario> gamacSbUsuarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private Collection<GamacSbDireccion> gamacSbDireccionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaOriId")
    private List<SbRelacionPersona> sbRelacionPersonaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaDestId")
    private List<SbRelacionPersona> sbRelacionPersonaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaId")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection;
    @OneToMany(mappedBy = "representanteId")
    private Collection<GamacEppRegistro> gamacEppRegistroCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaId")
    private Collection<GamacEppCom> gamacEppComCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaPadreId")
    private Collection<GamacAmaLicenciaDeUso> gamacAmaLicenciaDeUsoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaLicenciaId")
    private Collection<GamacAmaLicenciaDeUso> gamacAmaLicenciaDeUsoCollection1;
    @OneToMany(mappedBy = "representanteLegalId")
    private Collection<GamacAmaLicenciaDeUso> gamacAmaLicenciaDeUsoCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentecomerId")
    private Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection;
    @OneToMany(mappedBy = "representanteId")
    private Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clienteproveedorId")
    private Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaSolicitaId")
    private Collection<GamacAmaVerificarArma> gamacAmaVerificarArmaCollection;
    @JoinColumn(name = "EST_CIVIL_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoBase estCivilId;
    @JoinColumn(name = "GENERO_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoBase generoId;
    @JoinColumn(name = "TIPO_DOC", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoBase tipoDoc;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoBase tipoId;
    @JoinColumn(name = "INSTITU_LAB_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoBase instituLabId;
    @JoinColumn(name = "OCUPACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoBase ocupacionId;
    @OneToMany(mappedBy = "personaVendedorId")
    private Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaCompradorId")
    private Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection1;

    public GamacSbPersona() {
    }

    public GamacSbPersona(Long id) {
        this.id = id;
    }

    public GamacSbPersona(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRznSocial() {
        return rznSocial;
    }

    public void setRznSocial(String rznSocial) {
        this.rznSocial = rznSocial;
    }

    public String getNomCom() {
        return nomCom;
    }

    public void setNomCom(String nomCom) {
        this.nomCom = nomCom;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNumDocVal() {
        return numDocVal;
    }

    public void setNumDocVal(String numDocVal) {
        this.numDocVal = numDocVal;
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

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public Short getEnActividad() {
        return enActividad;
    }

    public void setEnActividad(Short enActividad) {
        this.enActividad = enActividad;
    }

    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
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

    public String getNroCip() {
        return nroCip;
    }

    public void setNroCip(String nroCip) {
        this.nroCip = nroCip;
    }

    @XmlTransient
    public Collection<GamacSbUsuario> getGamacSbUsuarioCollection() {
        return gamacSbUsuarioCollection;
    }

    public void setGamacSbUsuarioCollection(Collection<GamacSbUsuario> gamacSbUsuarioCollection) {
        this.gamacSbUsuarioCollection = gamacSbUsuarioCollection;
    }

    @XmlTransient
    public Collection<GamacSbDireccion> getGamacSbDireccionCollection() {
        return gamacSbDireccionCollection;
    }

    public void setGamacSbDireccionCollection(Collection<GamacSbDireccion> gamacSbDireccionCollection) {
        this.gamacSbDireccionCollection = gamacSbDireccionCollection;
    }

    @XmlTransient
    public List<SbRelacionPersona> getSbRelacionPersonaList() {
        return sbRelacionPersonaList;
    }

    public void setSbRelacionPersonaList(List<SbRelacionPersona> sbRelacionPersonaList) {
        this.sbRelacionPersonaList = sbRelacionPersonaList;
    }

    @XmlTransient
    public List<SbRelacionPersona> getSbRelacionPersonaList1() {
        return sbRelacionPersonaList1;
    }

    public void setSbRelacionPersonaList1(List<SbRelacionPersona> sbRelacionPersonaList1) {
        this.sbRelacionPersonaList1 = sbRelacionPersonaList1;
    }
    
    @XmlTransient
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection() {
        return gamacEppRegistroCollection;
    }

    public void setGamacEppRegistroCollection(Collection<GamacEppRegistro> gamacEppRegistroCollection) {
        this.gamacEppRegistroCollection = gamacEppRegistroCollection;
    }

    @XmlTransient
    public Collection<GamacEppRegistro> getGamacEppRegistroCollection1() {
        return gamacEppRegistroCollection1;
    }

    public void setGamacEppRegistroCollection1(Collection<GamacEppRegistro> gamacEppRegistroCollection1) {
        this.gamacEppRegistroCollection1 = gamacEppRegistroCollection1;
    }

    @XmlTransient
    public Collection<GamacEppCom> getGamacEppComCollection() {
        return gamacEppComCollection;
    }

    public void setGamacEppComCollection(Collection<GamacEppCom> gamacEppComCollection) {
        this.gamacEppComCollection = gamacEppComCollection;
    }

    @XmlTransient
    public Collection<GamacAmaLicenciaDeUso> getGamacAmaLicenciaDeUsoCollection() {
        return gamacAmaLicenciaDeUsoCollection;
    }

    public void setGamacAmaLicenciaDeUsoCollection(Collection<GamacAmaLicenciaDeUso> gamacAmaLicenciaDeUsoCollection) {
        this.gamacAmaLicenciaDeUsoCollection = gamacAmaLicenciaDeUsoCollection;
    }

    @XmlTransient
    public Collection<GamacAmaLicenciaDeUso> getGamacAmaLicenciaDeUsoCollection1() {
        return gamacAmaLicenciaDeUsoCollection1;
    }

    public void setGamacAmaLicenciaDeUsoCollection1(Collection<GamacAmaLicenciaDeUso> gamacAmaLicenciaDeUsoCollection1) {
        this.gamacAmaLicenciaDeUsoCollection1 = gamacAmaLicenciaDeUsoCollection1;
    }

    @XmlTransient
    public Collection<GamacAmaLicenciaDeUso> getGamacAmaLicenciaDeUsoCollection2() {
        return gamacAmaLicenciaDeUsoCollection2;
    }

    public void setGamacAmaLicenciaDeUsoCollection2(Collection<GamacAmaLicenciaDeUso> gamacAmaLicenciaDeUsoCollection2) {
        this.gamacAmaLicenciaDeUsoCollection2 = gamacAmaLicenciaDeUsoCollection2;
    }

    @XmlTransient
    public Collection<GamacAmaAdmunTransaccion> getGamacAmaAdmunTransaccionCollection() {
        return gamacAmaAdmunTransaccionCollection;
    }

    public void setGamacAmaAdmunTransaccionCollection(Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection) {
        this.gamacAmaAdmunTransaccionCollection = gamacAmaAdmunTransaccionCollection;
    }

    @XmlTransient
    public Collection<GamacAmaAdmunTransaccion> getGamacAmaAdmunTransaccionCollection1() {
        return gamacAmaAdmunTransaccionCollection1;
    }

    public void setGamacAmaAdmunTransaccionCollection1(Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection1) {
        this.gamacAmaAdmunTransaccionCollection1 = gamacAmaAdmunTransaccionCollection1;
    }

    @XmlTransient
    public Collection<GamacAmaAdmunTransaccion> getGamacAmaAdmunTransaccionCollection2() {
        return gamacAmaAdmunTransaccionCollection2;
    }

    public void setGamacAmaAdmunTransaccionCollection2(Collection<GamacAmaAdmunTransaccion> gamacAmaAdmunTransaccionCollection2) {
        this.gamacAmaAdmunTransaccionCollection2 = gamacAmaAdmunTransaccionCollection2;
    }

    @XmlTransient
    public Collection<GamacAmaVerificarArma> getGamacAmaVerificarArmaCollection() {
        return gamacAmaVerificarArmaCollection;
    }

    public void setGamacAmaVerificarArmaCollection(Collection<GamacAmaVerificarArma> gamacAmaVerificarArmaCollection) {
        this.gamacAmaVerificarArmaCollection = gamacAmaVerificarArmaCollection;
    }

    public GamacTipoBase getEstCivilId() {
        return estCivilId;
    }

    public void setEstCivilId(GamacTipoBase estCivilId) {
        this.estCivilId = estCivilId;
    }

    public GamacTipoBase getGeneroId() {
        return generoId;
    }

    public void setGeneroId(GamacTipoBase generoId) {
        this.generoId = generoId;
    }

    public GamacTipoBase getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(GamacTipoBase tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public GamacTipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(GamacTipoBase tipoId) {
        this.tipoId = tipoId;
    }

    public GamacTipoBase getInstituLabId() {
        return instituLabId;
    }

    public void setInstituLabId(GamacTipoBase instituLabId) {
        this.instituLabId = instituLabId;
    }

    public GamacTipoBase getOcupacionId() {
        return ocupacionId;
    }

    public void setOcupacionId(GamacTipoBase ocupacionId) {
        this.ocupacionId = ocupacionId;
    }

    @XmlTransient
    public Collection<GamacAmaTarjetaPropiedad> getGamacAmaTarjetaPropiedadCollection() {
        return gamacAmaTarjetaPropiedadCollection;
    }

    public void setGamacAmaTarjetaPropiedadCollection(Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection) {
        this.gamacAmaTarjetaPropiedadCollection = gamacAmaTarjetaPropiedadCollection;
    }

    @XmlTransient
    public Collection<GamacAmaTarjetaPropiedad> getGamacAmaTarjetaPropiedadCollection1() {
        return gamacAmaTarjetaPropiedadCollection1;
    }

    public void setGamacAmaTarjetaPropiedadCollection1(Collection<GamacAmaTarjetaPropiedad> gamacAmaTarjetaPropiedadCollection1) {
        this.gamacAmaTarjetaPropiedadCollection1 = gamacAmaTarjetaPropiedadCollection1;
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
        if (!(object instanceof GamacSbPersona)) {
            return false;
        }
        GamacSbPersona other = (GamacSbPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacSbPersona[ id=" + id + " ]";
    }

    public String getNroLicenciaCaf() {
        return nroLicenciaCaf;
    }

    public void setNroLicenciaCaf(String nroLicenciaCaf) {
        this.nroLicenciaCaf = nroLicenciaCaf;
    }

    public Date getFecInicioServicio() {
        return fecInicioServicio;
    }

    public void setFecInicioServicio(Date fecInicioServicio) {
        this.fecInicioServicio = fecInicioServicio;
    }

    public Date getFecFinServicio() {
        return fecFinServicio;
    }

    public void setFecFinServicio(Date fecFinServicio) {
        this.fecFinServicio = fecFinServicio;
    }

    public String getNroCe() {
        return nroCe;
    }

    public void setNroCe(String nroCe) {
        this.nroCe = nroCe;
    }

    @XmlTransient
    public List<SbAsientoPersona> getSbAsientoPersonaList() {
        return sbAsientoPersonaList;
    }

    public void setSbAsientoPersonaList(List<SbAsientoPersona> sbAsientoPersonaList) {
        this.sbAsientoPersonaList = sbAsientoPersonaList;
    }
    
}
