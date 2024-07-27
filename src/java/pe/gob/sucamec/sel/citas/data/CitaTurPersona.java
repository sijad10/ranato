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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author msalinas
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "TUR_PERSONA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaTurPersona.findAll", query = "SELECT p FROM CitaTurPersona p"),
    @NamedQuery(name = "CitaTurPersona.findById", query = "SELECT p FROM CitaTurPersona p WHERE p.id = :id"),
    @NamedQuery(name = "CitaTurPersona.findByRuc", query = "SELECT p FROM CitaTurPersona p WHERE p.ruc = :ruc"),
    @NamedQuery(name = "CitaTurPersona.findByRznSocial", query = "SELECT p FROM CitaTurPersona p WHERE p.rznSocial = :rznSocial"),
    @NamedQuery(name = "CitaTurPersona.findByNomCom", query = "SELECT p FROM CitaTurPersona p WHERE p.nomCom = :nomCom"),
    @NamedQuery(name = "CitaTurPersona.findByNumDoc", query = "SELECT p FROM CitaTurPersona p WHERE p.numDoc = :numDoc"),
    @NamedQuery(name = "CitaTurPersona.findByNumDocVal", query = "SELECT p FROM CitaTurPersona p WHERE p.numDocVal = :numDocVal"),
    @NamedQuery(name = "CitaTurPersona.findByApePat", query = "SELECT p FROM CitaTurPersona p WHERE p.apePat = :apePat"),
    @NamedQuery(name = "CitaTurPersona.findByApeMat", query = "SELECT p FROM CitaTurPersona p WHERE p.apeMat = :apeMat"),
    @NamedQuery(name = "CitaTurPersona.findByNombres", query = "SELECT p FROM CitaTurPersona p WHERE p.nombres = :nombres"),
    @NamedQuery(name = "CitaTurPersona.findByFechaNac", query = "SELECT p FROM CitaTurPersona p WHERE p.fechaNac = :fechaNac"),
    @NamedQuery(name = "CitaTurPersona.findByActivo", query = "SELECT p FROM CitaTurPersona p WHERE p.activo = :activo"),
    @NamedQuery(name = "CitaTurPersona.findByAudLogin", query = "SELECT p FROM CitaTurPersona p WHERE p.audLogin = :audLogin"),
    @NamedQuery(name = "CitaTurPersona.findByAudNumIp", query = "SELECT p FROM CitaTurPersona p WHERE p.audNumIp = :audNumIp")})
public class CitaTurPersona implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<CitaUsuario> usuarioList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TUR_PERSONA")
    @SequenceGenerator(name = "SEQ_TUR_PERSONA", schema = "BDINTEGRADO", sequenceName = "SEQ_TUR_PERSONA", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
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
    @JoinColumn(name = "TIPO_DOC", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoBase tipoDoc;
    @JoinColumn(name = "GENERO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoBase generoId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTipoBase tipoId;
    @JoinColumn(name = "EST_CIVIL_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTipoBase estCivilId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "perPagoId")
    private List<CitaTurTurno> turTurnoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "perExamenId")
    private List<CitaTurTurno> turTurnoList1;
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
    private CitaTipoBase instituLabId;    

    public CitaTurPersona() {
    }

    public CitaTurPersona(Long id) {
        this.id = id;
    }

    public CitaTurPersona(Long id, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public String getNombreCompleto() {
        return nombres + " " + apePat + " " + apeMat;
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

    public CitaTipoBase getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(CitaTipoBase tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public CitaTipoBase getGeneroId() {
        return generoId;
    }

    public void setGeneroId(CitaTipoBase generoId) {
        this.generoId = generoId;
    }

    public CitaTipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(CitaTipoBase tipoId) {
        this.tipoId = tipoId;
    }

    public CitaTipoBase getEstCivilId() {
        return estCivilId;
    }

    public void setEstCivilId(CitaTipoBase estCivilId) {
        this.estCivilId = estCivilId;
    }

    @XmlTransient
    public List<CitaTurTurno> getTurTurnoList() {
        return turTurnoList;
    }

    public void setTurTurnoList(List<CitaTurTurno> turTurnoList) {
        this.turTurnoList = turTurnoList;
    }

    @XmlTransient
    public List<CitaTurTurno> getTurTurnoList1() {
        return turTurnoList1;
    }

    public void setTurTurnoList1(List<CitaTurTurno> turTurnoList1) {
        this.turTurnoList1 = turTurnoList1;
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

    public CitaTipoBase getInstituLabId() {
        return instituLabId;
    }

    public void setInstituLabId(CitaTipoBase instituLabId) {
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
        if (!(object instanceof CitaTurPersona)) {
            return false;
        }
        CitaTurPersona other = (CitaTurPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.citas.data.CitaTurPersona[ id=" + id + " ]";
    }

    @XmlTransient
    public List<CitaUsuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<CitaUsuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

}
