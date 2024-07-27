/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.turreg.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
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
import pe.gob.sucamec.sistemabase.data.TipoBase;
import pe.gob.sucamec.sistemabase.data.SbTipo;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TUR_PERSONA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TurPersona.findAll", query = "SELECT t FROM TurPersona t"),
    @NamedQuery(name = "TurPersona.findById", query = "SELECT t FROM TurPersona t WHERE t.id = :id"),
    @NamedQuery(name = "TurPersona.findByRuc", query = "SELECT t FROM TurPersona t WHERE t.ruc = :ruc"),
    @NamedQuery(name = "TurPersona.findByRznSocial", query = "SELECT t FROM TurPersona t WHERE t.rznSocial = :rznSocial"),
    @NamedQuery(name = "TurPersona.findByNomCom", query = "SELECT t FROM TurPersona t WHERE t.nomCom = :nomCom"),
    @NamedQuery(name = "TurPersona.findByNumDoc", query = "SELECT t FROM TurPersona t WHERE t.numDoc = :numDoc"),
    @NamedQuery(name = "TurPersona.findByNumDocVal", query = "SELECT t FROM TurPersona t WHERE t.numDocVal = :numDocVal"),
    @NamedQuery(name = "TurPersona.findByApePat", query = "SELECT t FROM TurPersona t WHERE t.apePat = :apePat"),
    @NamedQuery(name = "TurPersona.findByApeMat", query = "SELECT t FROM TurPersona t WHERE t.apeMat = :apeMat"),
    @NamedQuery(name = "TurPersona.findByNombres", query = "SELECT t FROM TurPersona t WHERE t.nombres = :nombres"),
    @NamedQuery(name = "TurPersona.findByFechaNac", query = "SELECT t FROM TurPersona t WHERE t.fechaNac = :fechaNac"),
    @NamedQuery(name = "TurPersona.findByActivo", query = "SELECT t FROM TurPersona t WHERE t.activo = :activo"),
    @NamedQuery(name = "TurPersona.findByAudLogin", query = "SELECT t FROM TurPersona t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TurPersona.findByAudNumIp", query = "SELECT t FROM TurPersona t WHERE t.audNumIp = :audNumIp")})
public class TurPersona implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TUR_PERSONA")
    @SequenceGenerator(name = "SEQ_TUR_PERSONA", schema = "BDINTEGRADO", sequenceName = "SEQ_TUR_PERSONA", allocationSize = 1)
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
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "perExamenId")
    private List<TurTurno> turTurnoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "perPagoId")
    private List<TurTurno> turTurnoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<TurDireccion> turDireccionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turPersonaId")
    private List<TurMedioContacto> turMedioContactoList;
    @JoinColumn(name = "TIPO_DOC", referencedColumnName = "ID")
    @ManyToOne
    private SbTipo tipoDoc;
    @JoinColumn(name = "GENERO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBase generoId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase tipoId;
    @JoinColumn(name = "EST_CIVIL_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBase estCivilId;
    @Column(name = "EN_ACTIVIDAD")
    private Short enActividad;
    @Column(name = "FEC_INICIO_SERVICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecInicioServicio;  
    @Column(name = "FEC_FIN_SERVICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecFinServicio;  
    @Size(max = 12)    
    @Column(name = "NRO_CIP")
    private String nroCip;    
    @JoinColumn(name = "INSTITU_LAB_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBase instituLabId;    
    
    public TurPersona() {
    }

    public TurPersona(Long id) {
        this.id = id;
    }

    public TurPersona(Long id, short activo, String audLogin, String audNumIp) {
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
        return nombres + " " + apePat + " " + apeMat;
    }
    
    public String getNombreCompletoDoc() {
        return nombres + " " + apePat + " " + apeMat + " / Doc: " + numDoc;
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
    public List<TurTurno> getTurTurnoList() {
        return turTurnoList;
    }

    public void setTurTurnoList(List<TurTurno> turTurnoList) {
        this.turTurnoList = turTurnoList;
    }

    @XmlTransient
    public List<TurTurno> getTurTurnoList1() {
        return turTurnoList1;
    }

    public void setTurTurnoList1(List<TurTurno> turTurnoList1) {
        this.turTurnoList1 = turTurnoList1;
    }

    @XmlTransient
    public List<TurDireccion> getTurDireccionList() {
        return turDireccionList;
    }

    public void setTurDireccionList(List<TurDireccion> turDireccionList) {
        this.turDireccionList = turDireccionList;
    }

    @XmlTransient
    public List<TurMedioContacto> getTurMedioContactoList() {
        return turMedioContactoList;
    }

    public void setTurMedioContactoList(List<TurMedioContacto> turMedioContactoList) {
        this.turMedioContactoList = turMedioContactoList;
    }

    public SbTipo getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(SbTipo tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public TipoBase getGeneroId() {
        return generoId;
    }

    public void setGeneroId(TipoBase generoId) {
        this.generoId = generoId;
    }

    public TipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBase tipoId) {
        this.tipoId = tipoId;
    }

    public TipoBase getEstCivilId() {
        return estCivilId;
    }

    public void setEstCivilId(TipoBase estCivilId) {
        this.estCivilId = estCivilId;
    }

    public Short getEnActividad() {
        return enActividad;
    }

    public void setEnActividad(Short enActividad) {
        this.enActividad = enActividad;
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

    public String getNroCip() {
        return nroCip;
    }

    public void setNroCip(String nroCip) {
        this.nroCip = nroCip;
    }

    public TipoBase getInstituLabId() {
        return instituLabId;
    }

    public void setInstituLabId(TipoBase instituLabId) {
        this.instituLabId = instituLabId;
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
        if (!(object instanceof TurPersona)) {
            return false;
        }
        TurPersona other = (TurPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.turreg.data.TurPersona[ id=" + id + " ]";
    }
    
}
