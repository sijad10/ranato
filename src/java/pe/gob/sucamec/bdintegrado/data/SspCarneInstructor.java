/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;

/**
 *
 * @author locador845.ogtic
 */
@Entity
@Table(name = "SSP_CARNE_INSTRUCTOR", catalog = "", schema = "BDINTEGRADO")
@NamedQueries({
    @NamedQuery(name = "SspCarneInstructor.findAll", query = "SELECT s FROM SspCarneInstructor s"),
    @NamedQuery(name = "SspCarneInstructor.findById", query = "SELECT s FROM SspCarneInstructor s WHERE s.id = :id"),
    @NamedQuery(name = "SspCarneInstructor.findByCarneId", query = "SELECT s FROM SspCarneInstructor s WHERE s.carneId = :carneId"),
    @NamedQuery(name = "SspCarneInstructor.findByFotoId", query = "SELECT s FROM SspCarneInstructor s WHERE s.fotoId = :fotoId"),
    @NamedQuery(name = "SspCarneInstructor.findByEstadoId", query = "SELECT s FROM SspCarneInstructor s WHERE s.estadoId = :estadoId"),
    @NamedQuery(name = "SspCarneInstructor.findByNroCarne", query = "SELECT s FROM SspCarneInstructor s WHERE s.nroCarne = :nroCarne"),
    @NamedQuery(name = "SspCarneInstructor.findByFechaEmision", query = "SELECT s FROM SspCarneInstructor s WHERE s.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "SspCarneInstructor.findByHashQr", query = "SELECT s FROM SspCarneInstructor s WHERE s.hashQr = :hashQr"),
    @NamedQuery(name = "SspCarneInstructor.findByFormatoId", query = "SELECT s FROM SspCarneInstructor s WHERE s.formatoId = :formatoId"),
    @NamedQuery(name = "SspCarneInstructor.findByDigital", query = "SELECT s FROM SspCarneInstructor s WHERE s.digital = :digital"),
    @NamedQuery(name = "SspCarneInstructor.findByObservacionAud", query = "SELECT s FROM SspCarneInstructor s WHERE s.observacionAud = :observacionAud"),
    @NamedQuery(name = "SspCarneInstructor.findByActivo", query = "SELECT s FROM SspCarneInstructor s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspCarneInstructor.findByAudLogin", query = "SELECT s FROM SspCarneInstructor s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspCarneInstructor.findByAudNumIp", query = "SELECT s FROM SspCarneInstructor s WHERE s.audNumIp = :audNumIp")})
public class SspCarneInstructor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_CARNE_ISNTRUCTOR")
    @SequenceGenerator(name = "SEQ_SSP_CARNE_ISNTRUCTOR", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_CARNE_ISNTRUCTOR", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CARNE_ID")
    private Long carneId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FOTO_ID")
    private long fotoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTADO_ID")
    private long estadoId;
    @Column(name = "NRO_CARNE")
    private Integer nroCarne;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Size(max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
    @Column(name = "FORMATO_ID")
    private Long formatoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DIGITAL")
    private short digital;
    @Size(max = 100)
    @Column(name = "OBSERVACION_AUD")
    private String observacionAud;
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
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;
    @JoinColumn(name = "INSTRUCTOR_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt instructorId;

    public SspCarneInstructor() {
    }

    public SspCarneInstructor(Long id) {
        this.id = id;
    }

    public SspCarneInstructor(Long id, long fotoId, long estadoId, Date fechaEmision, short digital, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fotoId = fotoId;
        this.estadoId = estadoId;
        this.fechaEmision = fechaEmision;
        this.digital = digital;
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

    public Long getCarneId() {
        return carneId;
    }

    public void setCarneId(Long carneId) {
        this.carneId = carneId;
    }

    public long getFotoId() {
        return fotoId;
    }

    public void setFotoId(long fotoId) {
        this.fotoId = fotoId;
    }

    public long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(long estadoId) {
        this.estadoId = estadoId;
    }

    public Integer getNroCarne() {
        return nroCarne;
    }

    public void setNroCarne(Integer nroCarne) {
        this.nroCarne = nroCarne;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getHashQr() {
        return hashQr;
    }

    public void setHashQr(String hashQr) {
        this.hashQr = hashQr;
    }

    public Long getFormatoId() {
        return formatoId;
    }

    public void setFormatoId(Long formatoId) {
        this.formatoId = formatoId;
    }

    public short getDigital() {
        return digital;
    }

    public void setDigital(short digital) {
        this.digital = digital;
    }

    public String getObservacionAud() {
        return observacionAud;
    }

    public void setObservacionAud(String observacionAud) {
        this.observacionAud = observacionAud;
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

    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
    }

    public SbPersonaGt getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(SbPersonaGt instructorId) {
        this.instructorId = instructorId;
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
        if (!(object instanceof SspCarneInstructor)) {
            return false;
        }
        SspCarneInstructor other = (SspCarneInstructor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "prueba.SspCarneInstructor[ id=" + id + " ]";
    }
    
}
