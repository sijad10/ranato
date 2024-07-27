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
import javax.persistence.JoinTable;
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
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_CERTIFICADO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppCertificado.findAll", query = "SELECT e FROM EppCertificado e"),
    @NamedQuery(name = "EppCertificado.findById", query = "SELECT e FROM EppCertificado e WHERE e.id = :id"),
    @NamedQuery(name = "EppCertificado.findByNroCertificado", query = "SELECT e FROM EppCertificado e WHERE e.nroCertificado = :nroCertificado"),
    @NamedQuery(name = "EppCertificado.findByFechaEmision", query = "SELECT e FROM EppCertificado e WHERE e.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "EppCertificado.findByFechaVencimiento", query = "SELECT e FROM EppCertificado e WHERE e.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "EppCertificado.findByActivo", query = "SELECT e FROM EppCertificado e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppCertificado.findByAudLogin", query = "SELECT e FROM EppCertificado e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppCertificado.findByAudNumIp", query = "SELECT e FROM EppCertificado e WHERE e.audNumIp = :audNumIp"),
    @NamedQuery(name = "EppCertificado.findByNotaEvaluacion", query = "SELECT e FROM EppCertificado e WHERE e.notaEvaluacion = :notaEvaluacion"),
    @NamedQuery(name = "EppCertificado.findByCondDioexamen", query = "SELECT e FROM EppCertificado e WHERE e.condDioexamen = :condDioexamen")})
public class EppCertificado implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_CERTIFICADO")
    @SequenceGenerator(name = "SEQ_EPP_CERTIFICADO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_CERTIFICADO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_CERTIFICADO")
    private int nroCertificado;
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
    @Column(name = "NOTA_EVALUACION")
    private Integer notaEvaluacion;
    @Column(name = "COND_DIOEXAMEN")
    private Short condDioexamen;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_CAPACITACION_ASIS", joinColumns = {
        @JoinColumn(name = "CERTIFICADO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CAPAHORARIO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppCapaHorariodir> eppCapaHorariodirList;
    @ManyToMany(mappedBy = "eppCertificadoList")
    private List<EppRegistro> eppRegistroList;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EppRegistro registroId;
    @JoinColumn(name = "FOTO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppFoto fotoId;
    @JoinColumn(name = "CAPACITACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppCapacitacion capacitacionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "certificadoId")
    private List<EppCarne> eppCarneList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_CERTIFICADO_ACTPIRO", joinColumns = {
        @JoinColumn(name = "CERTIFICADO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "ACTIVIDAD_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<TipoExplosivoGt> tipoExplosivoList;

    public EppCertificado() {
    }

    public EppCertificado(Long id) {
        this.id = id;
    }

    public EppCertificado(Long id, int nroCertificado, Date fechaEmision, Date fechaVencimiento, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroCertificado = nroCertificado;
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

    public int getNroCertificado() {
        return nroCertificado;
    }

    public void setNroCertificado(int nroCertificado) {
        this.nroCertificado = nroCertificado;
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

    public Integer getNotaEvaluacion() {
        return notaEvaluacion;
    }

    public void setNotaEvaluacion(Integer notaEvaluacion) {
        this.notaEvaluacion = notaEvaluacion;
    }

    public Short getCondDioexamen() {
        return condDioexamen;
    }

    public void setCondDioexamen(Short condDioexamen) {
        this.condDioexamen = condDioexamen;
    }

    @XmlTransient
    public List<EppCapaHorariodir> getEppCapaHorariodirList() {
        return eppCapaHorariodirList;
    }

    public void setEppCapaHorariodirList(List<EppCapaHorariodir> eppCapaHorariodirList) {
        this.eppCapaHorariodirList = eppCapaHorariodirList;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
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

    public EppCapacitacion getCapacitacionId() {
        return capacitacionId;
    }

    public void setCapacitacionId(EppCapacitacion capacitacionId) {
        this.capacitacionId = capacitacionId;
    }

    @XmlTransient
    public List<EppCarne> getEppCarneList() {
        return eppCarneList;
    }

    public void setEppCarneList(List<EppCarne> eppCarneList) {
        this.eppCarneList = eppCarneList;
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
        if (!(object instanceof EppCertificado)) {
            return false;
        }
        EppCertificado other = (EppCertificado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppCertificado[ id=" + id + " ]";
    }
    
     @XmlTransient
    public List<TipoExplosivoGt> getTipoExplosivoList() {
        return tipoExplosivoList;
    }

    public void setTipoExplosivoList(List<TipoExplosivoGt> tipoExplosivoList) {
        this.tipoExplosivoList = tipoExplosivoList;
    }
    
}
