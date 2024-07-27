/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_CARNE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppCarne.findAll", query = "SELECT e FROM EppCarne e"),
    @NamedQuery(name = "EppCarne.findById", query = "SELECT e FROM EppCarne e WHERE e.id = :id"),
    @NamedQuery(name = "EppCarne.findByNroCarne", query = "SELECT e FROM EppCarne e WHERE e.nroCarne = :nroCarne"),
    @NamedQuery(name = "EppCarne.findByFechaEmision", query = "SELECT e FROM EppCarne e WHERE e.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "EppCarne.findByFechaVencimiento", query = "SELECT e FROM EppCarne e WHERE e.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "EppCarne.findByLicenciaConductor", query = "SELECT e FROM EppCarne e WHERE e.licenciaConductor = :licenciaConductor"),
    @NamedQuery(name = "EppCarne.findByActivo", query = "SELECT e FROM EppCarne e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppCarne.findByAudLogin", query = "SELECT e FROM EppCarne e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppCarne.findByAudNumIp", query = "SELECT e FROM EppCarne e WHERE e.audNumIp = :audNumIp"),
    @NamedQuery(name = "EppCarne.findByHashQr", query = "SELECT e FROM EppCarne e WHERE e.hashQr = :hashQr")})
public class EppCarne implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_CARNE")
    @SequenceGenerator(name = "SEQ_EPP_CARNE", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_CARNE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_CARNE")
    private int nroCarne;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
    @Size(max = 20)
    @Column(name = "LICENCIA_CONDUCTOR")
    private String licenciaConductor;
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
    @Size(max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
    @ManyToMany(mappedBy = "eppCarneList")
    private List<EppRegistro> eppRegistroList;
    @ManyToMany(mappedBy = "eppCarneList")
    private List<EppDepositoAmbientes> eppDepositoAmbientesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conductorId")
    private List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList;
    @JoinColumn(name = "TIPO_ACTIVIDAD_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoActividadId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EppRegistro registroId;
    @JoinColumn(name = "FOTO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppFoto fotoId;
    @JoinColumn(name = "CERTIFICADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppCertificado certificadoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "representanteDepId")
    private List<EppDepositoContrato> eppDepositoContratoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "administradorId")
    private List<EppTallerdeposito> eppTallerdepositoList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "administradorId")
//    private List<EppDepositoAmbientes> eppDepositoAmbientesList1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EMITIDA")
    private short emitida;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "DIGITAL")
    private short digital;

    public EppCarne() {
    }

    public EppCarne(Long id) {
        this.id = id;
    }

    public EppCarne(Long id, int nroCarne, Date fechaEmision, Date fechaVencimiento, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroCarne = nroCarne;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
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

    public int getNroCarne() {
        return nroCarne;
    }

    public void setNroCarne(int nroCarne) {
        this.nroCarne = nroCarne;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getLicenciaConductor() {
        return licenciaConductor;
    }

    public void setLicenciaConductor(String licenciaConductor) {
        this.licenciaConductor = licenciaConductor;
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

    public String getHashQr() {
        return hashQr;
    }

    public void setHashQr(String hashQr) {
        this.hashQr = hashQr;
    }

    @XmlTransient
    public List<EppTallerdeposito> getEppTallerdepositoList() {
        return eppTallerdepositoList;
    }

    public void setEppTallerdepositoList(List<EppTallerdeposito> eppTallerdepositoList) {
        this.eppTallerdepositoList = eppTallerdepositoList;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    @XmlTransient
    public List<EppDepositoAmbientes> getEppDepositoAmbientesList() {
        return eppDepositoAmbientesList;
    }

    public void setEppDepositoAmbientesList(List<EppDepositoAmbientes> eppDepositoAmbientesList) {
        this.eppDepositoAmbientesList = eppDepositoAmbientesList;
    }

    @XmlTransient
    public List<EppGuiaTransitoPiro> getEppGuiaTransitoPiroList() {
        return eppGuiaTransitoPiroList;
    }

    public void setEppGuiaTransitoPiroList(List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList) {
        this.eppGuiaTransitoPiroList = eppGuiaTransitoPiroList;
    }

    public TipoExplosivoGt getTipoActividadId() {
        return tipoActividadId;
    }

    public void setTipoActividadId(TipoExplosivoGt tipoActividadId) {
        this.tipoActividadId = tipoActividadId;
    }

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }

    public EppFoto getFotoId() {
        return fotoId;
    }

    public void setFotoId(EppFoto fotoId) {
        this.fotoId = fotoId;
    }

    public EppCertificado getCertificadoId() {
        return certificadoId;
    }

    public void setCertificadoId(EppCertificado certificadoId) {
        this.certificadoId = certificadoId;
    }

    @XmlTransient
    public List<EppDepositoContrato> getEppDepositoContratoList() {
        return eppDepositoContratoList;
    }

    public void setEppDepositoContratoList(List<EppDepositoContrato> eppDepositoContratoList) {
        this.eppDepositoContratoList = eppDepositoContratoList;
    }

//    @XmlTransient
//    public List<EppDepositoAmbientes> getEppDepositoAmbientesList1() {
//        return eppDepositoAmbientesList1;
//    }
//
//    public void setEppDepositoAmbientesList1(List<EppDepositoAmbientes> eppDepositoAmbientesList1) {
//        this.eppDepositoAmbientesList1 = eppDepositoAmbientesList1;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EppCarne)) {
            return false;
        }
        EppCarne other = (EppCarne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppCarne[ id=" + id + " ]";
    }

    public short getEmitida() {
        return emitida;
    }

    public void setEmitida(short emitida) {
        this.emitida = emitida;
    }
    
    public short getDigital() {
        return digital;
    }

    public void setDigital(short digital) {
        this.digital = digital;
    }
}
