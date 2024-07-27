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

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_PERSONA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbPersonaGt.findAll", query = "SELECT s FROM SbPersonaGt s"),
    @NamedQuery(name = "SbPersonaGt.findById", query = "SELECT s FROM SbPersonaGt s WHERE s.id = :id"),
    @NamedQuery(name = "SbPersonaGt.findByRuc", query = "SELECT s FROM SbPersonaGt s WHERE s.ruc = :ruc"),
    @NamedQuery(name = "SbPersonaGt.findByRznSocial", query = "SELECT s FROM SbPersonaGt s WHERE s.rznSocial = :rznSocial"),
    @NamedQuery(name = "SbPersonaGt.findByNomCom", query = "SELECT s FROM SbPersonaGt s WHERE s.nomCom = :nomCom"),
    @NamedQuery(name = "SbPersonaGt.findByNumDoc", query = "SELECT s FROM SbPersonaGt s WHERE s.numDoc = :numDoc"),
    @NamedQuery(name = "SbPersonaGt.findByNumDocVal", query = "SELECT s FROM SbPersonaGt s WHERE s.numDocVal = :numDocVal"),
    @NamedQuery(name = "SbPersonaGt.findByApePat", query = "SELECT s FROM SbPersonaGt s WHERE s.apePat = :apePat"),
    @NamedQuery(name = "SbPersonaGt.findByApeMat", query = "SELECT s FROM SbPersonaGt s WHERE s.apeMat = :apeMat"),
    @NamedQuery(name = "SbPersonaGt.findByNombres", query = "SELECT s FROM SbPersonaGt s WHERE s.nombres = :nombres"),
    @NamedQuery(name = "SbPersonaGt.findByFechaNac", query = "SELECT s FROM SbPersonaGt s WHERE s.fechaNac = :fechaNac"),
    @NamedQuery(name = "SbPersonaGt.findByActivo", query = "SELECT s FROM SbPersonaGt s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbPersonaGt.findByAudLogin", query = "SELECT s FROM SbPersonaGt s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbPersonaGt.findByAudNumIp", query = "SELECT s FROM SbPersonaGt s WHERE s.audNumIp = :audNumIp")})
public class SbPersonaGt implements Serializable { 

    private static final long serialVersionUID = 1L;
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
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @Column(name = "NRO_CIP")
    private String nroCip;
    @Column(name = "EN_ACTIVIDAD")
    private Short enActividad;
    @Column(name = "FECHA_REG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReg;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<SbUsuarioGt> sbUsuarioList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "arrendadoraId")
    private List<EppContratoAlqPolv> eppContratoAlqPolvList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "arrendatariaId")
    private List<EppContratoAlqPolv> eppContratoAlqPolvList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<SbDireccionGt> sbDireccionList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaId")
    private List<EppCom> eppComList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaId")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList;
    
    @OneToMany(mappedBy = "empresaTranspId")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<EppLicencia> eppLicenciaList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList;
    
    @OneToMany(mappedBy = "persempresaId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList1;
    
    @OneToMany(mappedBy = "empresaTransporteId")
    private List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<EppCertificado> eppCertificadoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<EppCarne> eppCarneList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "beneficiarioId")
    private List<EppDepositoContrato> eppDepositoContratoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<EppLocal> eppLocalList;
    
    @JoinColumn(name = "GENERO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt generoId;
    
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoId;
    
    @JoinColumn(name = "INSTITU_LAB_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt instituLabId;
    
    @JoinColumn(name = "TIPO_DOC", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoDoc;
    
    @JoinColumn(name = "EST_CIVIL_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt estCivilId;
    
    @JoinColumn(name = "OCUPACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt ocupacionId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<EppInspector> eppInspectorList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaOriId")
    private List<SbRelacionPersonaGt> sbRelacionPersonaList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaDestId")    
    private List<SbRelacionPersonaGt> sbRelacionPersonaList1;
    
    @OneToMany(mappedBy = "empresaId")
    private List<EppDocumento> eppDocumentoList;
    
    @OneToMany(mappedBy = "autorId")
    private List<EppDocumento> eppDocumentoList1;
    
    @OneToMany(mappedBy = "representanteId")
    private List<EppRegistro> eppRegistroList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaId")
    private List<EppRegistro> eppRegistroList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "propietarioId")
    private List<EppPolvorin> eppPolvorinList;
    
    @OneToMany(mappedBy = "representanteId")
    private List<EppPolvorin> eppPolvorinList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<SbMedioContactoGt> sbMedioContactoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaId")
    private List<EppLibro> eppLibroList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "administradoId")
    private List<SspRegistroCurso> sspRegistroCursoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<SspAlumnoCurso> sspAlumnoCursoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaLicenciaId")
    private List<AmaLicenciaDeUso> amaLicenciaDeUsoList2;
    
    public SbPersonaGt() {
    }

    public SbPersonaGt(Long id) {
        this.id = id;
    }

    public SbPersonaGt(Long id, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public String getDatosPerNat() {
        return numDoc + " - " + nombres + " " + apePat + " " + apeMat;
    }

    public String getDatosPerJur() {
        return ruc + " - " + rznSocial;
    }

    public String getNombreCompleto() {
        return (nombres != null ? nombres.trim() : "") + " " + (apePat != null ? apePat.trim() : "") + " " + (apeMat != null ? apeMat.trim() : "");
    }

    public String getApellidosyNombres() {
        return (apePat != null ? apePat.trim() : "") + " " + (apeMat != null ? apeMat.trim() : "") + " " + (nombres != null ? nombres.trim() : "");
    }

    public String getNombreCompletoDoc() {
        return nombres + " " + apePat + " " + apeMat + " / Doc: " + numDoc;
    }

    public String getNombreCompletoRzn() {
        return rznSocial + " / Ruc: " + ruc;
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

    public String getNroCip() {
        return nroCip;
    }

    public void setNroCip(String nroCip) {
        this.nroCip = nroCip;
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

    @XmlTransient
    public List<SbUsuarioGt> getSbUsuarioList() {
        return sbUsuarioList;
    }

    public void setSbUsuarioList(List<SbUsuarioGt> sbUsuarioList) {
        this.sbUsuarioList = sbUsuarioList;
    }

    @XmlTransient
    public List<EppContratoAlqPolv> getEppContratoAlqPolvList() {
        return eppContratoAlqPolvList;
    }

    public void setEppContratoAlqPolvList(List<EppContratoAlqPolv> eppContratoAlqPolvList) {
        this.eppContratoAlqPolvList = eppContratoAlqPolvList;
    }

    @XmlTransient
    public List<EppContratoAlqPolv> getEppContratoAlqPolvList1() {
        return eppContratoAlqPolvList1;
    }

    public void setEppContratoAlqPolvList1(List<EppContratoAlqPolv> eppContratoAlqPolvList1) {
        this.eppContratoAlqPolvList1 = eppContratoAlqPolvList1;
    }

    @XmlTransient
    public List<SbDireccionGt> getSbDireccionList() {
        return sbDireccionList;
    }

    public void setSbDireccionList(List<SbDireccionGt> sbDireccionList) {
        this.sbDireccionList = sbDireccionList;
    }

    @XmlTransient
    public List<EppCom> getEppComList() {
        return eppComList;
    }

    public void setEppComList(List<EppCom> eppComList) {
        this.eppComList = eppComList;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList() {
        return eppRegistroGuiaTransitoList;
    }

    public void setEppRegistroGuiaTransitoList(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList) {
        this.eppRegistroGuiaTransitoList = eppRegistroGuiaTransitoList;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList1() {
        return eppRegistroGuiaTransitoList1;
    }

    public void setEppRegistroGuiaTransitoList1(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList1) {
        this.eppRegistroGuiaTransitoList1 = eppRegistroGuiaTransitoList1;
    }

    @XmlTransient
    public List<EppLicencia> getEppLicenciaList() {
        return eppLicenciaList;
    }

    public void setEppLicenciaList(List<EppLicencia> eppLicenciaList) {
        this.eppLicenciaList = eppLicenciaList;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList() {
        return eppInspeccionPolvorinList;
    }

    public void setEppInspeccionPolvorinList(List<EppInspeccionPolvorin> eppInspeccionPolvorinList) {
        this.eppInspeccionPolvorinList = eppInspeccionPolvorinList;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList1() {
        return eppInspeccionPolvorinList1;
    }

    public void setEppInspeccionPolvorinList1(List<EppInspeccionPolvorin> eppInspeccionPolvorinList1) {
        this.eppInspeccionPolvorinList1 = eppInspeccionPolvorinList1;
    }

    @XmlTransient
    public List<EppGuiaTransitoPiro> getEppGuiaTransitoPiroList() {
        return eppGuiaTransitoPiroList;
    }

    public void setEppGuiaTransitoPiroList(List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList) {
        this.eppGuiaTransitoPiroList = eppGuiaTransitoPiroList;
    }

    @XmlTransient
    public List<EppGuiaTransitoPiro> getEppGuiaTransitoPiroList1() {
        return eppGuiaTransitoPiroList1;
    }

    public void setEppGuiaTransitoPiroList1(List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList1) {
        this.eppGuiaTransitoPiroList1 = eppGuiaTransitoPiroList1;
    }

    @XmlTransient
    public List<EppCertificado> getEppCertificadoList() {
        return eppCertificadoList;
    }

    public void setEppCertificadoList(List<EppCertificado> eppCertificadoList) {
        this.eppCertificadoList = eppCertificadoList;
    }

    @XmlTransient
    public List<EppCarne> getEppCarneList() {
        return eppCarneList;
    }

    public void setEppCarneList(List<EppCarne> eppCarneList) {
        this.eppCarneList = eppCarneList;
    }

    @XmlTransient
    public List<EppDepositoContrato> getEppDepositoContratoList() {
        return eppDepositoContratoList;
    }

    public void setEppDepositoContratoList(List<EppDepositoContrato> eppDepositoContratoList) {
        this.eppDepositoContratoList = eppDepositoContratoList;
    }

    @XmlTransient
    public List<EppLocal> getEppLocalList() {
        return eppLocalList;
    }

    public void setEppLocalList(List<EppLocal> eppLocalList) {
        this.eppLocalList = eppLocalList;
    }

    public TipoBaseGt getGeneroId() {
        return generoId;
    }

    public void setGeneroId(TipoBaseGt generoId) {
        this.generoId = generoId;
    }

    public TipoBaseGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBaseGt tipoId) {
        this.tipoId = tipoId;
    }

    public TipoBaseGt getInstituLabId() {
        return instituLabId;
    }

    public void setInstituLabId(TipoBaseGt instituLabId) {
        this.instituLabId = instituLabId;
    }

    public TipoBaseGt getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoBaseGt tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public TipoBaseGt getEstCivilId() {
        return estCivilId;
    }

    public void setEstCivilId(TipoBaseGt estCivilId) {
        this.estCivilId = estCivilId;
    }

    @XmlTransient
    public List<EppInspector> getEppInspectorList() {
        return eppInspectorList;
    }

    public void setEppInspectorList(List<EppInspector> eppInspectorList) {
        this.eppInspectorList = eppInspectorList;
    }

    @XmlTransient
    public List<SbRelacionPersonaGt> getSbRelacionPersonaList() {
        return sbRelacionPersonaList;
    }

    public void setSbRelacionPersonaList(List<SbRelacionPersonaGt> sbRelacionPersonaList) {
        this.sbRelacionPersonaList = sbRelacionPersonaList;
    }

    @XmlTransient
    public List<SbRelacionPersonaGt> getSbRelacionPersonaList1() {
        return sbRelacionPersonaList1;
    }

    public void setSbRelacionPersonaList1(List<SbRelacionPersonaGt> sbRelacionPersonaList1) {
        this.sbRelacionPersonaList1 = sbRelacionPersonaList1;
    }

    @XmlTransient
    public List<EppDocumento> getEppDocumentoList() {
        return eppDocumentoList;
    }

    public void setEppDocumentoList(List<EppDocumento> eppDocumentoList) {
        this.eppDocumentoList = eppDocumentoList;
    }

    @XmlTransient
    public List<EppDocumento> getEppDocumentoList1() {
        return eppDocumentoList1;
    }

    public void setEppDocumentoList1(List<EppDocumento> eppDocumentoList1) {
        this.eppDocumentoList1 = eppDocumentoList1;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList1() {
        return eppRegistroList1;
    }

    public void setEppRegistroList1(List<EppRegistro> eppRegistroList1) {
        this.eppRegistroList1 = eppRegistroList1;
    }

    @XmlTransient
    public List<EppPolvorin> getEppPolvorinList() {
        return eppPolvorinList;
    }

    public void setEppPolvorinList(List<EppPolvorin> eppPolvorinList) {
        this.eppPolvorinList = eppPolvorinList;
    }

    @XmlTransient
    public List<EppPolvorin> getEppPolvorinList1() {
        return eppPolvorinList1;
    }

    public void setEppPolvorinList1(List<EppPolvorin> eppPolvorinList1) {
        this.eppPolvorinList1 = eppPolvorinList1;
    }

    @XmlTransient
    public List<SbMedioContactoGt> getSbMedioContactoList() {
        return sbMedioContactoList;
    }

    public void setSbMedioContactoList(List<SbMedioContactoGt> sbMedioContactoList) {
        this.sbMedioContactoList = sbMedioContactoList;
    }

    @XmlTransient
    public List<EppLibro> getEppLibroList() {
        return eppLibroList;
    }

    public void setEppLibroList(List<EppLibro> eppLibroList) {
        this.eppLibroList = eppLibroList;
    }

    @XmlTransient
    public List<SspRegistroCurso> getSspRegistroCursoList() {
        return sspRegistroCursoList;
    }

    public void setSspRegistroCursoList(List<SspRegistroCurso> sspRegistroCursoList) {
        this.sspRegistroCursoList = sspRegistroCursoList;
    }

    @XmlTransient
    public List<SspAlumnoCurso> getSspAlumnoCursoList() {
        return sspAlumnoCursoList;
    }

    public void setSspAlumnoCursoList(List<SspAlumnoCurso> sspAlumnoCursoList) {
        this.sspAlumnoCursoList = sspAlumnoCursoList;
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
        if (!(object instanceof SbPersonaGt)) {
            return false;
        }
        SbPersonaGt other = (SbPersonaGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.SbPersonaGt[ id=" + id + " ]";
    }
    
    /*public String getNombreCompleto() {
        return nombres + " " + apePat + " " + apeMat;
    }*/

    /**
     * @return the ocupacionId
     */
    public TipoBaseGt getOcupacionId() {
        return ocupacionId;
    }

    /**
     * @param ocupacionId the ocupacionId to set
     */
    public void setOcupacionId(TipoBaseGt ocupacionId) {
        this.ocupacionId = ocupacionId;
    }
    
    /*public String getApellidosyNombres() {
        return (apePat!=null?apePat.trim():"") + " " + (apeMat != null?apeMat.trim():"") + " " + (nombres!=null?nombres.trim():"");
    }*/
    public String getNombresYApellidos() {
        return (nombres!=null?nombres.trim():"") + " " + (apePat!=null?apePat.trim():"") + " " + (apeMat != null?apeMat.trim():"");
    }
    
    @XmlTransient
    public List<AmaLicenciaDeUso> getAmaLicenciaDeUsoList2() {
        return amaLicenciaDeUsoList2;
    }

    public void setAmaLicenciaDeUsoList2(List<AmaLicenciaDeUso> amaLicenciaDeUsoList2) {
        this.amaLicenciaDeUsoList2 = amaLicenciaDeUsoList2;
    }

}
