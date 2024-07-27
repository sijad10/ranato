/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_LICENCIA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppLicencia.findAll", query = "SELECT e FROM EppLicencia e"),
    @NamedQuery(name = "EppLicencia.findById", query = "SELECT e FROM EppLicencia e WHERE e.id = :id"),
    @NamedQuery(name = "EppLicencia.findByNroLicencia", query = "SELECT e FROM EppLicencia e WHERE e.nroLicencia = :nroLicencia"),
    @NamedQuery(name = "EppLicencia.findByFecEmi", query = "SELECT e FROM EppLicencia e WHERE e.fecEmi = :fecEmi"),
    @NamedQuery(name = "EppLicencia.findByFecVenc", query = "SELECT e FROM EppLicencia e WHERE e.fecVenc = :fecVenc"),
    @NamedQuery(name = "EppLicencia.findByLicenciaConductor", query = "SELECT e FROM EppLicencia e WHERE e.licenciaConductor = :licenciaConductor"),
    @NamedQuery(name = "EppLicencia.findByEmitida", query = "SELECT e FROM EppLicencia e WHERE e.emitida = :emitida"),
    @NamedQuery(name = "EppLicencia.findByTipoOficioId", query = "SELECT e FROM EppLicencia e WHERE e.tipoOficioId = :tipoOficioId"),
    @NamedQuery(name = "EppLicencia.findByActivo", query = "SELECT e FROM EppLicencia e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppLicencia.findByAudLogin", query = "SELECT e FROM EppLicencia e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppLicencia.findByAudNumIp", query = "SELECT e FROM EppLicencia e WHERE e.audNumIp = :audNumIp"),
    @NamedQuery(name = "EppLicencia.findByHashQr", query = "SELECT e FROM EppLicencia e WHERE e.hashQr = :hashQr")})
public class EppLicencia implements Serializable {

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
    @Column(name = "TIPO_OFICIO_ID")
    private Long tipoOficioId;
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
    @ManyToMany(mappedBy = "eppLicenciaList")
    private List<EppLibroSaldo> eppLibroSaldoList;
    @ManyToMany(mappedBy = "eppLicenciaList")
    private List<EppRegistro> eppRegistroList;
    @ManyToMany(mappedBy = "eppLicenciaList")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList;
    @ManyToMany(mappedBy = "eppLicenciaList")
    private List<EppLibroUsoDiario> eppLibroUsoDiarioList;
    @ManyToMany(mappedBy = "eppLicenciaList1")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList1;
    @JoinColumn(name = "TIPO_EMISION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoEmisionId;
    @JoinColumn(name = "TIPO_LICENCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoLicenciaId;
    @JoinColumn(name = "TIPO_CARGO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoCargoId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppRegistro registroId;
    @JoinColumn(name = "FOTO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppFoto fotoId;
    
    @Basic(optional = false)    
    @Column(name = "DIGITAL")
    private short digital;

    public EppLicencia() {
    }

    public EppLicencia(Long id) {
        this.id = id;
    }

    public EppLicencia(Long id, Date fecEmi, Date fecVenc, short emitida, short activo, String audLogin, String audNumIp) {
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

    public short getDigital() {
        return digital;
    }

    public void setDigital(short digital) {
        this.digital = digital;
    }
    
    public short getEmitida() {
        return emitida;
    }

    public void setEmitida(short emitida) {
        this.emitida = emitida;
    }   

    public Long getTipoOficioId() {
        return tipoOficioId;
    }

    public void setTipoOficioId(Long tipoOficioId) {
        this.tipoOficioId = tipoOficioId;
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
    public List<EppLibroSaldo> getEppLibroSaldoList() {
        return eppLibroSaldoList;
    }

    public void setEppLibroSaldoList(List<EppLibroSaldo> eppLibroSaldoList) {
        this.eppLibroSaldoList = eppLibroSaldoList;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList() {
        return eppRegistroGuiaTransitoList;
    }

    public void setEppRegistroGuiaTransitoList(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList) {
        this.eppRegistroGuiaTransitoList = eppRegistroGuiaTransitoList;
    }

    @XmlTransient
    public List<EppLibroUsoDiario> getEppLibroUsoDiarioList() {
        return eppLibroUsoDiarioList;
    }

    public void setEppLibroUsoDiarioList(List<EppLibroUsoDiario> eppLibroUsoDiarioList) {
        this.eppLibroUsoDiarioList = eppLibroUsoDiarioList;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList1() {
        return eppRegistroGuiaTransitoList1;
    }

    public void setEppRegistroGuiaTransitoList1(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList1) {
        this.eppRegistroGuiaTransitoList1 = eppRegistroGuiaTransitoList1;
    }

    public TipoExplosivoGt getTipoEmisionId() {
        return tipoEmisionId;
    }

    public void setTipoEmisionId(TipoExplosivoGt tipoEmisionId) {
        this.tipoEmisionId = tipoEmisionId;
    }

    public TipoExplosivoGt getTipoLicenciaId() {
        return tipoLicenciaId;
    }

    public void setTipoLicenciaId(TipoExplosivoGt tipoLicenciaId) {
        this.tipoLicenciaId = tipoLicenciaId;
    }

    public TipoExplosivoGt getTipoCargoId() {
        return tipoCargoId;
    }

    public void setTipoCargoId(TipoExplosivoGt tipoCargoId) {
        this.tipoCargoId = tipoCargoId;
    }

    public SbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersona personaId) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EppLicencia)) {
            return false;
        }
        EppLicencia other = (EppLicencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppLicencia[ id=" + id + " ]";
    }
    
}
