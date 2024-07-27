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
@Table(name = "EPP_DOCUMENTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppDocumento.findAll", query = "SELECT e FROM EppDocumento e"),
    @NamedQuery(name = "EppDocumento.findById", query = "SELECT e FROM EppDocumento e WHERE e.id = :id"),
    @NamedQuery(name = "EppDocumento.findByNumero", query = "SELECT e FROM EppDocumento e WHERE e.numero = :numero"),
    @NamedQuery(name = "EppDocumento.findByNroExpediente", query = "SELECT e FROM EppDocumento e WHERE e.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "EppDocumento.findByFechaEmision", query = "SELECT e FROM EppDocumento e WHERE e.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "EppDocumento.findByFechaIni", query = "SELECT e FROM EppDocumento e WHERE e.fechaIni = :fechaIni"),
    @NamedQuery(name = "EppDocumento.findByFechaFin", query = "SELECT e FROM EppDocumento e WHERE e.fechaFin = :fechaFin"),
    @NamedQuery(name = "EppDocumento.findByRuta", query = "SELECT e FROM EppDocumento e WHERE e.ruta = :ruta"),
    @NamedQuery(name = "EppDocumento.findByObservacion", query = "SELECT e FROM EppDocumento e WHERE e.observacion = :observacion"),
    @NamedQuery(name = "EppDocumento.findByUtilizado", query = "SELECT e FROM EppDocumento e WHERE e.utilizado = :utilizado"),
    @NamedQuery(name = "EppDocumento.findByActivo", query = "SELECT e FROM EppDocumento e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppDocumento.findByAudLogin", query = "SELECT e FROM EppDocumento e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppDocumento.findByAudNumIp", query = "SELECT e FROM EppDocumento e WHERE e.audNumIp = :audNumIp"),
    @NamedQuery(name = "EppDocumento.findByAsunto", query = "SELECT e FROM EppDocumento e WHERE e.asunto = :asunto"),
    @NamedQuery(name = "EppDocumento.findByDestComisariaId", query = "SELECT e FROM EppDocumento e WHERE e.destComisariaId = :destComisariaId"),
    @NamedQuery(name = "EppDocumento.findByFechaCreacion", query = "SELECT e FROM EppDocumento e WHERE e.fechaCreacion = :fechaCreacion")})
public class EppDocumento implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_DOCUMENTO")
    @SequenceGenerator(name = "SEQ_EPP_DOCUMENTO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_DOCUMENTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    
    
    @Column(name = "NUMERO")
    private String numero;
    @Basic(optional = false)
    
    
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Basic(optional = false)
    
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Size(max = 200)
    @Column(name = "RUTA")
    private String ruta;
    @Size(max = 200)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Basic(optional = false)
    
    @Column(name = "UTILIZADO")
    private short utilizado;
    @Basic(optional = false)
    
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @Size(max = 200)
    @Column(name = "ASUNTO")
    private String asunto;
    @Column(name = "DEST_COMISARIA_ID")
    private Long destComisariaId;
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @ManyToMany(mappedBy = "eppDocumentoList")
    private List<EppRegistro> eppRegistroList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_DOCUMENTO_OBS", joinColumns = {
        @JoinColumn(name = "DOCUMENTO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "OBSERVACION_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppObservacion> eppObservacionList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoId;
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt areaId;
    @JoinColumn(name = "DEST_AREA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt destAreaId;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersonaGt empresaId;
    @JoinColumn(name = "AUTOR_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersonaGt autorId;
    @OneToMany(mappedBy = "docAnexoId")
    private List<EppDocumento> eppDocumentoList;
    @JoinColumn(name = "DOC_ANEXO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppDocumento docAnexoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documentoId")
    private List<EppDocumentoAdjunto> eppDocumentoAdjuntoList;

    public EppDocumento() {
    }

    public EppDocumento(Long id) {
        this.id = id;
    }

    public EppDocumento(Long id, String numero, String nroExpediente, Date fechaEmision, short utilizado, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.numero = numero;
        this.nroExpediente = nroExpediente;
        this.fechaEmision = fechaEmision;
        this.utilizado = utilizado;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public short getUtilizado() {
        return utilizado;
    }

    public void setUtilizado(short utilizado) {
        this.utilizado = utilizado;
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

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Long getDestComisariaId() {
        return destComisariaId;
    }

    public void setDestComisariaId(Long destComisariaId) {
        this.destComisariaId = destComisariaId;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    @XmlTransient
    public List<EppObservacion> getEppObservacionList() {
        return eppObservacionList;
    }

    public void setEppObservacionList(List<EppObservacion> eppObservacionList) {
        this.eppObservacionList = eppObservacionList;
    }

    public TipoExplosivoGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoExplosivoGt tipoId) {
        this.tipoId = tipoId;
    }

    public TipoBaseGt getAreaId() {
        return areaId;
    }

    public void setAreaId(TipoBaseGt areaId) {
        this.areaId = areaId;
    }

    public TipoBaseGt getDestAreaId() {
        return destAreaId;
    }

    public void setDestAreaId(TipoBaseGt destAreaId) {
        this.destAreaId = destAreaId;
    }

    public SbPersonaGt getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersonaGt empresaId) {
        this.empresaId = empresaId;
    }

    public SbPersonaGt getAutorId() {
        return autorId;
    }

    public void setAutorId(SbPersonaGt autorId) {
        this.autorId = autorId;
    }

    @XmlTransient
    public List<EppDocumento> getEppDocumentoList() {
        return eppDocumentoList;
    }

    public void setEppDocumentoList(List<EppDocumento> eppDocumentoList) {
        this.eppDocumentoList = eppDocumentoList;
    }

    public EppDocumento getDocAnexoId() {
        return docAnexoId;
    }

    public void setDocAnexoId(EppDocumento docAnexoId) {
        this.docAnexoId = docAnexoId;
    }

    @XmlTransient
    public List<EppDocumentoAdjunto> getEppDocumentoAdjuntoList() {
        return eppDocumentoAdjuntoList;
    }

    public void setEppDocumentoAdjuntoList(List<EppDocumentoAdjunto> eppDocumentoAdjuntoList) {
        this.eppDocumentoAdjuntoList = eppDocumentoAdjuntoList;
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
        if (!(object instanceof EppDocumento)) {
            return false;
        }
        EppDocumento other = (EppDocumento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppDocumento[ id=" + id + " ]";
    }
    
}
