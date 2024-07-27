/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.epp.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import pe.gob.sucamec.sistemabase.data.SbPersona;


/**
 *
 * @author rmoscoso
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_LICENCIA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Licencia.findAll", query = "SELECT l FROM Licencia l"),
    @NamedQuery(name = "Licencia.findById", query = "SELECT l FROM Licencia l WHERE l.id = :id"),
    @NamedQuery(name = "Licencia.findByNroLicencia", query = "SELECT l FROM Licencia l WHERE l.nroLicencia = :nroLicencia"),
    @NamedQuery(name = "Licencia.findByFecEmi", query = "SELECT l FROM Licencia l WHERE l.fecEmi = :fecEmi"),
    @NamedQuery(name = "Licencia.findByFecVenc", query = "SELECT l FROM Licencia l WHERE l.fecVenc = :fecVenc"),
    @NamedQuery(name = "Licencia.findByLicenciaConductor", query = "SELECT l FROM Licencia l WHERE l.licenciaConductor = :licenciaConductor"),
    @NamedQuery(name = "Licencia.findByEmitida", query = "SELECT l FROM Licencia l WHERE l.emitida = :emitida"),
    @NamedQuery(name = "Licencia.findByActivo", query = "SELECT l FROM Licencia l WHERE l.activo = :activo"),
    @NamedQuery(name = "Licencia.findByAudLogin", query = "SELECT l FROM Licencia l WHERE l.audLogin = :audLogin"),
    @NamedQuery(name = "Licencia.findByAudNumIp", query = "SELECT l FROM Licencia l WHERE l.audNumIp = :audNumIp"),
    @NamedQuery(name = "Licencia.findByTipoOficioId", query = "SELECT l FROM Licencia l WHERE l.tipoOficioId = :tipoOficioId")})
public class Licencia implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_LICENCIA")
    @SequenceGenerator(name = "SEQ_EPP_LICENCIA", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_LICENCIA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NRO_LICENCIA")
    private Long nroLicencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FEC_EMI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecEmi;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FEC_VENC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecVenc;
    @Size(max = 20)
    @Column(name = "LICENCIA_CONDUCTOR")
    private String licenciaConductor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EMITIDA")
    private short emitida;
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
    @Column(name = "TIPO_OFICIO_ID")
    private Long tipoOficioId;
    @ManyToMany(mappedBy = "licenciaList")
    private List<RegistroGuiaTransito> registroGuiaTransitoList;
    @ManyToMany(mappedBy = "licenciaList1")
    private List<RegistroGuiaTransito> registroGuiaTransitoList1;
    @JoinColumn(name = "FOTO_ID", referencedColumnName = "ID")
    @ManyToOne
    private Foto fotoId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Registro registroId;
    @JoinColumn(name = "TIPO_EMISION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivo tipoEmisionId;
    @JoinColumn(name = "TIPO_LICENCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivo tipoLicenciaId;
    @JoinColumn(name = "TIPO_CARGO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivo tipoCargoId;

    public Licencia() {
    }

    public Licencia(Long id) {
        this.id = id;
    }

    public Licencia(Long id, Date fecEmi, Date fecVenc, short emitida, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fecEmi = fecEmi;
        this.fecVenc = fecVenc;
        this.emitida = emitida;
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

    public Long getNroLicencia() {
        return nroLicencia;
    }

    public void setNroLicencia(Long nroLicencia) {
        this.nroLicencia = nroLicencia;
    }

    public Date getFecEmi() {
        return fecEmi;
    }

    public void setFecEmi(Date fecEmi) {
        this.fecEmi = fecEmi;
    }

    public Date getFecVenc() {
        return fecVenc;
    }

    public void setFecVenc(Date fecVenc) {
        this.fecVenc = fecVenc;
    }

    public String getLicenciaConductor() {
        return licenciaConductor;
    }

    public void setLicenciaConductor(String licenciaConductor) {
        this.licenciaConductor = licenciaConductor;
    }

    public short getEmitida() {
        return emitida;
    }

    public void setEmitida(short emitida) {
        this.emitida = emitida;
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

    public Long getTipoOficioId() {
        return tipoOficioId;
    }

    public void setTipoOficioId(Long tipoOficioId) {
        this.tipoOficioId = tipoOficioId;
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

    public Foto getFotoId() {
        return fotoId;
    }

    public void setFotoId(Foto fotoId) {
        this.fotoId = fotoId;
    }

    public SbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersona personaId) {
        this.personaId = personaId;
    }

    public Registro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Registro registroId) {
        this.registroId = registroId;
    }

    public TipoExplosivo getTipoEmisionId() {
        return tipoEmisionId;
    }

    public void setTipoEmisionId(TipoExplosivo tipoEmisionId) {
        this.tipoEmisionId = tipoEmisionId;
    }

    public TipoExplosivo getTipoLicenciaId() {
        return tipoLicenciaId;
    }

    public void setTipoLicenciaId(TipoExplosivo tipoLicenciaId) {
        this.tipoLicenciaId = tipoLicenciaId;
    }

    public TipoExplosivo getTipoCargoId() {
        return tipoCargoId;
    }

    public void setTipoCargoId(TipoExplosivo tipoCargoId) {
        this.tipoCargoId = tipoCargoId;
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
        if (!(object instanceof Licencia)) {
            return false;
        }
        Licencia other = (Licencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.epp.data.Licencia[ id=" + id + " ]";
    }
    
}
