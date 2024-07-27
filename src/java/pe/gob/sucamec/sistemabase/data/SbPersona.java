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
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;
import pe.gob.sucamec.notificacion.data.NeDocumento;
import pe.gob.sucamec.sel.epp.data.Com;
import pe.gob.sucamec.sel.epp.data.Licencia;
import pe.gob.sucamec.sel.epp.data.Polvorin;
import pe.gob.sucamec.sel.epp.data.Registro;
import pe.gob.sucamec.sel.epp.data.RegistroGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.AmaLicenciaDeUso;

        
/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_PERSONA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbPersona.findAll", query = "SELECT s FROM SbPersona s"),
    @NamedQuery(name = "SbPersona.findById", query = "SELECT s FROM SbPersona s WHERE s.id = :id"),
    @NamedQuery(name = "SbPersona.findByRuc", query = "SELECT s FROM SbPersona s WHERE s.ruc = :ruc"),
    @NamedQuery(name = "SbPersona.findByRznSocial", query = "SELECT s FROM SbPersona s WHERE s.rznSocial = :rznSocial"),
    @NamedQuery(name = "SbPersona.findByNomCom", query = "SELECT s FROM SbPersona s WHERE s.nomCom = :nomCom"),
    @NamedQuery(name = "SbPersona.findByNumDoc", query = "SELECT s FROM SbPersona s WHERE s.numDoc = :numDoc and s.activo = 1"),
    @NamedQuery(name = "SbPersona.findByNumDocVal", query = "SELECT s FROM SbPersona s WHERE s.numDocVal = :numDocVal"),
    @NamedQuery(name = "SbPersona.findByApePat", query = "SELECT s FROM SbPersona s WHERE s.apePat = :apePat"),
    @NamedQuery(name = "SbPersona.findByApeMat", query = "SELECT s FROM SbPersona s WHERE s.apeMat = :apeMat"),
    @NamedQuery(name = "SbPersona.findByNombres", query = "SELECT s FROM SbPersona s WHERE s.nombres = :nombres"),
    @NamedQuery(name = "SbPersona.findByFechaNac", query = "SELECT s FROM SbPersona s WHERE s.fechaNac = :fechaNac"),
    @NamedQuery(name = "SbPersona.findByActivo", query = "SELECT s FROM SbPersona s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbPersona.findByAudLogin", query = "SELECT s FROM SbPersona s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbPersona.findByAudNumIp", query = "SELECT s FROM SbPersona s WHERE s.audNumIp = :audNumIp"),
    @NamedQuery(name = "SbPersona.findByNroCip", query = "SELECT s FROM SbPersona s WHERE s.nroCip = :nroCip")})
public class SbPersona implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<SbUsuarioGt> sbUsuarioGtList;

    @ManyToMany(mappedBy = "sbPersonaList")
    private List<Registro> registroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaId")
    private List<Com> comList;
    @OneToMany(mappedBy = "empresaTranspId")
    private List<RegistroGuiaTransito> registroGuiaTransitoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaId")
    private List<RegistroGuiaTransito> registroGuiaTransitoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<Licencia> licenciaList;
    @OneToMany(mappedBy = "representanteId")
    private List<Polvorin> polvorinList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "propietarioId")
    private List<Polvorin> polvorinList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaId")
    private List<Registro> registroList1;
    @OneToMany(mappedBy = "representanteId")
    private List<Registro> registroList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<NeDocumento> neDocumentoList;
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_PERSONA")
    @SequenceGenerator(name = "SEQ_SB_PERSONA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_PERSONA", allocationSize = 1)
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
    @Size(max = 12)
    @Column(name = "NRO_CIP")
    private String nroCip;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<SbDireccion> sbDireccionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaOriId")
    private List<SbRelacionPersona> sbRelacionPersonaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaDestId")
    private List<SbRelacionPersona> sbRelacionPersonaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<SbUsuario> sbUsuarioList;
    @OneToMany(mappedBy = "personaId")
    private List<SbMedioContacto> sbMedioContactoList;
    @JoinColumn(name = "EST_CIVIL_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbTipo estCivilId;
    /*@JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoId;*/
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoId;
    
    /*@JoinColumn(name = "GENERO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbTipo generoId;*/
    @JoinColumn(name = "GENERO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt generoId;
    
    //@JoinColumn(name = "TIPO_DOC", referencedColumnName = "ID")
    //@ManyToOne
    //private SbTipo tipoDoc;  
    @JoinColumn(name = "TIPO_DOC", referencedColumnName = "ID")
    @ManyToOne
    private SbTipo tipoDoc;
        
    @JoinColumn(name = "OCUPACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbTipo ocupacionId;
    @JoinColumn(name = "INSTITU_LAB_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbTipo instituLabId;
    @Column(name = "EN_ACTIVIDAD")
    private Short enActividad;
    @Column(name = "FECHA_REG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReg;
    @Column(name = "NRO_LICENCIA_CAF")
    private String nroLicenciaCaf;  
    @Column(name = "FEC_INICIO_SERVICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecInicioServicio;  
    @Column(name = "FEC_FIN_SERVICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecFinServicio;  
    @Column(name = "NRO_CE")
    private String nroCe;  
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaLicenciaId")
    private List<AmaLicenciaDeUso> amaLicenciaDeUsoList2;

    public SbPersona() {
    }

    public SbPersona(Long id) {
        this.id = id;
    }

    public SbPersona(Long id, short activo, String audLogin, String audNumIp) {
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
    public List<SbDireccion> getSbDireccionList() {
        return sbDireccionList;
    }

    public void setSbDireccionList(List<SbDireccion> sbDireccionList) {
        this.sbDireccionList = sbDireccionList;
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
    public List<SbUsuario> getSbUsuarioList() {
        return sbUsuarioList;
    }

    public void setSbUsuarioList(List<SbUsuario> sbUsuarioList) {
        this.sbUsuarioList = sbUsuarioList;
    }

    @XmlTransient
    public List<SbMedioContacto> getSbMedioContactoList() {
        return sbMedioContactoList;
    }

    public void setSbMedioContactoList(List<SbMedioContacto> sbMedioContactoList) {
        this.sbMedioContactoList = sbMedioContactoList;
    }

    public SbTipo getEstCivilId() {
        return estCivilId;
    }

    public void setEstCivilId(SbTipo estCivilId) {
        this.estCivilId = estCivilId;
    }

    public SbTipo getTipoId() {
        return tipoId;
    }

    public void setTipoId(SbTipo tipoId) {
        this.tipoId = tipoId;
    }

    public TipoBaseGt getGeneroId() {
        return generoId;
    }

    public void setGeneroId(TipoBaseGt generoId) {
        this.generoId = generoId;
    }

    public SbTipo getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(SbTipo tipoDoc) {
        this.tipoDoc = tipoDoc;
    }
    
    public String getNombreCompleto() {
        return nombres + " " + apePat + " " + apeMat;
    }

    public String setNombreCompleto() {
        return "";
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
        if (!(object instanceof SbPersona)) {
            return false;
        }
        SbPersona other = (SbPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbPersona[ id=" + id + " ]";
    }

    @XmlTransient
    public List<NeDocumento> getNeDocumentoList() {
        return neDocumentoList;
    }

    public void setNeDocumentoList(List<NeDocumento> neDocumentoList) {
        this.neDocumentoList = neDocumentoList;
    }

    @XmlTransient
    public List<Registro> getRegistroList() {
        return registroList;
    }

    public void setRegistroList(List<Registro> registroList) {
        this.registroList = registroList;
    }

    @XmlTransient
    public List<Com> getComList() {
        return comList;
    }

    public void setComList(List<Com> comList) {
        this.comList = comList;
    }

    @XmlTransient
    public List<RegistroGuiaTransito> getRegistroGuiaTransitoList() {
        return registroGuiaTransitoList;
    }

    public void setRegistroGuiaTransitoList(List<RegistroGuiaTransito> registroGuiaTransitoList) {
        this.registroGuiaTransitoList = registroGuiaTransitoList;
    }

    @XmlTransient
    public List<RegistroGuiaTransito> getRegistroGuiaTransitoList1() {
        return registroGuiaTransitoList1;
    }

    public void setRegistroGuiaTransitoList1(List<RegistroGuiaTransito> registroGuiaTransitoList1) {
        this.registroGuiaTransitoList1 = registroGuiaTransitoList1;
    }

    @XmlTransient
    public List<Licencia> getLicenciaList() {
        return licenciaList;
    }

    public void setLicenciaList(List<Licencia> licenciaList) {
        this.licenciaList = licenciaList;
    }

    @XmlTransient
    public List<Polvorin> getPolvorinList() {
        return polvorinList;
    }

    public void setPolvorinList(List<Polvorin> polvorinList) {
        this.polvorinList = polvorinList;
    }

    @XmlTransient
    public List<Polvorin> getPolvorinList1() {
        return polvorinList1;
    }

    public void setPolvorinList1(List<Polvorin> polvorinList1) {
        this.polvorinList1 = polvorinList1;
    }

    @XmlTransient
    public List<Registro> getRegistroList1() {
        return registroList1;
    }

    public void setRegistroList1(List<Registro> registroList1) {
        this.registroList1 = registroList1;
    }

    @XmlTransient
    public List<Registro> getRegistroList2() {
        return registroList2;
    }

    public void setRegistroList2(List<Registro> registroList2) {
        this.registroList2 = registroList2;
    }

    public String getApellidosyNombres() {
        return (apePat!=null?apePat.trim():"") + " " + (apeMat != null?apeMat.trim():"") + " " + (nombres!=null?nombres.trim():"");
    }
    
    /**
     * @return the nroCip
     */
    public String getNroCip() {
        return nroCip;
    }

    /**
     * @param nroCip the nroCip to set
     */
    public void setNroCip(String nroCip) {
        this.nroCip = nroCip;
    }

    public SbTipo getOcupacionId() {
        return ocupacionId;
    }

    public void setOcupacionId(SbTipo ocupacionId) {
        this.ocupacionId = ocupacionId;
    }

    public SbTipo getInstituLabId() {
        return instituLabId;
    }

    public void setInstituLabId(SbTipo instituLabId) {
        this.instituLabId = instituLabId;
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
    public List<SbUsuarioGt> getSbUsuarioGtList() {
        return sbUsuarioGtList;
    }

    public void setSbUsuarioGtList(List<SbUsuarioGt> sbUsuarioGtList) {
        this.sbUsuarioGtList = sbUsuarioGtList;
    }

    @XmlTransient
    public List<AmaLicenciaDeUso> getAmaLicenciaDeUsoList2() {
        return amaLicenciaDeUsoList2;
    }

    public void setAmaLicenciaDeUsoList2(List<AmaLicenciaDeUso> amaLicenciaDeUsoList2) {
        this.amaLicenciaDeUsoList2 = amaLicenciaDeUsoList2;
    }

    public String getDatosPerNat() {
        return numDoc + " - " + nombres + " " + apePat + " " + apeMat;
    }    
    
    public String getDatosPerJur() {
        return ruc + " - " + rznSocial;
    }
}
