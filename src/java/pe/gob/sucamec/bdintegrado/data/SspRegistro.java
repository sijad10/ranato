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
import javax.persistence.FetchType;
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

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_REGISTRO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspRegistro.findAll", query = "SELECT s FROM SspRegistro s"),
    @NamedQuery(name = "SspRegistro.findById", query = "SELECT s FROM SspRegistro s WHERE s.id = :id"),
    @NamedQuery(name = "SspRegistro.findByFecha", query = "SELECT s FROM SspRegistro s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspRegistro.findByNroExpediente", query = "SELECT s FROM SspRegistro s WHERE s.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "SspRegistro.findByFechaIni", query = "SELECT s FROM SspRegistro s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SspRegistro.findByFechaFin", query = "SELECT s FROM SspRegistro s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SspRegistro.findByObservacion", query = "SELECT s FROM SspRegistro s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SspRegistro.findByActivo", query = "SELECT s FROM SspRegistro s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspRegistro.findByAudLogin", query = "SELECT s FROM SspRegistro s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspRegistro.findByAudNumIp", query = "SELECT s FROM SspRegistro s WHERE s.audNumIp = :audNumIp")})
public class SspRegistro implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_REGISTRO")
    @SequenceGenerator(name = "SEQ_SSP_REGISTRO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_REGISTRO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    
    @Size(max = 300)
    @Column(name = "OBSERVACION")
    private String observacion;
    
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
    
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad estadoId;
    
    @JoinColumn(name = "TIPO_REG_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoRegId;
    
    @JoinColumn(name = "SEDE_SUCAMEC", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt sedeSucamec;
    
    @JoinColumn(name = "TIPO_PRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoProId;
    
    @JoinColumn(name = "TIPO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoOpeId;
    
    @JoinColumn(name = "TIPO_AUT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoAutId;
    
    @OneToMany(mappedBy = "registroId")
    private List<SspRegistro> sspRegistroList;
    
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SspRegistro registroId;
    
    @JoinColumn(name = "CARNE_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SspCarne carneId;
    
    @JoinColumn(name = "COMPROBANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbRecibos comprobanteId;
    
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt empresaId;
   
    @JoinColumn(name = "REPRESENTANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersonaGt representanteId;
    
    @JoinColumn(name = "CARTA_FIANZA", referencedColumnName = "ID")
    @ManyToOne
    private SspCartaFianza cartaFianzaId;
        
    @JoinColumn(name = "REQ_RESOLUCION_ID", referencedColumnName = "ID")
    @ManyToOne
    private SspResolucion reqResolucionId;

    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspRequisito> sspRequisitoList;
   
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspRegistroEvento> sspRegistroEventoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspServicio> sspServicioList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspContacto> sspContactoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspLocalRegistro> sspLocalRegistroList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspParticipante> sspParticipanteList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspTipoUsoLocal> sspTipoUsoLocalList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspCefoespInstructor> sspCefoespInstructorList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspPoligono> sspPoligonoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspPoliza> sspPolizaList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspVehiculoDinero> sspVehiculoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspVehiculoCertificacion> sspVehiculoCertificacionList;

@OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspAgenciaFinanciera> sspAgenciaList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspJefeSeguridad> sspJefeSeguridadList;

    public List<SspPoligono> getSspPoligonoList() {
        return sspPoligonoList;
    }

    public void setSspPoligonoList(List<SspPoligono> sspPoligonoList) {
        this.sspPoligonoList = sspPoligonoList;
    }

    
    public List<SspCefoespInstructor> getSspCefoespInstructorList() {
        return sspCefoespInstructorList;
    }

    public void setSspCefoespInstructorList(List<SspCefoespInstructor> sspCefoespInstructorList) {
        this.sspCefoespInstructorList = sspCefoespInstructorList;
    }

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NRO_SOLICITIUD")
    private String nroSolicitiud;
    
    @JoinColumn(name = "USUARIO_CREACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuarioCreacionId;

    @JoinColumn(name = "USUARIO_RECEPCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private SbUsuarioGt usuarioRecepcionId;
    
    
    @JoinColumn(name = "CARNE_SOLICITUD_ID", referencedColumnName = "ID")
    @ManyToOne
    private SspRegistro carneSolicitudId;
    
    @JoinColumn(name = "MODALIDAD_VINCULADA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt modalidadVinculadaId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<SspRegistroRegDisca> sspRegistroRegDiscaList;
    
    public SspRegistro() {
    }

    public SspRegistro(Long id) {
        this.id = id;
    }

    public SspRegistro(Long id, Date fecha, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fecha = fecha;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public TipoSeguridad getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoSeguridad estadoId) {
        this.estadoId = estadoId;
    }

    public TipoBaseGt getTipoRegId() {
        return tipoRegId;
    }

    public void setTipoRegId(TipoBaseGt tipoRegId) {
        this.tipoRegId = tipoRegId;
    }

    public TipoBaseGt getSedeSucamec() {
        return sedeSucamec;
    }

    public void setSedeSucamec(TipoBaseGt sedeSucamec) {
        this.sedeSucamec = sedeSucamec;
    }

    public TipoBaseGt getTipoProId() {
        return tipoProId;
    }

    public void setTipoProId(TipoBaseGt tipoProId) {
        this.tipoProId = tipoProId;
    }

    public TipoBaseGt getTipoOpeId() {
        return tipoOpeId;
    }

    public void setTipoOpeId(TipoBaseGt tipoOpeId) {
        this.tipoOpeId = tipoOpeId;
    }

    @XmlTransient
    public List<SspRegistro> getSspRegistroList() {
        return sspRegistroList;
    }

    public void setSspRegistroList(List<SspRegistro> sspRegistroList) {
        this.sspRegistroList = sspRegistroList;
    }

    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
    }

    public SspCarne getCarneId() {
        return carneId;
    }

    public void setCarneId(SspCarne carneId) {
        this.carneId = carneId;
    }

    public SbRecibos getComprobanteId() {
        return comprobanteId;
    }

    public void setComprobanteId(SbRecibos comprobanteId) {
        this.comprobanteId = comprobanteId;
    }

    public SbPersonaGt getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersonaGt empresaId) {
        this.empresaId = empresaId;
    }

    public SbPersonaGt getRepresentanteId() {
        return representanteId;
    }

    public void setRepresentanteId(SbPersonaGt representanteId) {
        this.representanteId = representanteId;
    }

    @XmlTransient
    public List<SspRequisito> getSspRequisitoList() {
        return sspRequisitoList;
    }

    public void setSspRequisitoList(List<SspRequisito> sspRequisitoList) {
        this.sspRequisitoList = sspRequisitoList;
    }

    @XmlTransient
    public List<SspRegistroEvento> getSspRegistroEventoList() {
        return sspRegistroEventoList;
    }

    public void setSspRegistroEventoList(List<SspRegistroEvento> sspRegistroEventoList) {
        this.sspRegistroEventoList = sspRegistroEventoList;
    }

    @XmlTransient
    public List<SspServicio> getSspServicioList() {
        return sspServicioList;
    }

    public void setSspServicioList(List<SspServicio> sspServicioList) {
        this.sspServicioList = sspServicioList;
    }

    @XmlTransient
    public List<SspContacto> getSspContactoList() {
        return sspContactoList;
    }

    public void setSspContactoList(List<SspContacto> sspContactoList) {
        this.sspContactoList = sspContactoList;
    }

    @XmlTransient
    public List<SspLocalRegistro> getSspLocalRegistroList() {
        return sspLocalRegistroList;
    }

    public void setSspLocalRegistroList(List<SspLocalRegistro> sspLocalRegistroList) {
        this.sspLocalRegistroList = sspLocalRegistroList;
    }

    @XmlTransient
    public List<SspParticipante> getSspParticipanteList() {
        return sspParticipanteList;
    }

    public void setSspParticipanteList(List<SspParticipante> sspParticipanteList) {
        this.sspParticipanteList = sspParticipanteList;
    }

    @XmlTransient
    public List<SspTipoUsoLocal> getSspTipoUsoLocalList() {
        return sspTipoUsoLocalList;
    }

    public void setSspTipoUsoLocalList(List<SspTipoUsoLocal> sspTipoUsoLocalList) {
        this.sspTipoUsoLocalList = sspTipoUsoLocalList;
    }

    @XmlTransient
    public List<SspRegistroRegDisca> getSspRegistroRegDiscaList() {
        return sspRegistroRegDiscaList;
    }

    public void setSspRegistroRegDiscaList(List<SspRegistroRegDisca> sspRegistroRegDiscaList) {
        this.sspRegistroRegDiscaList = sspRegistroRegDiscaList;
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
        if (!(object instanceof SspRegistro)) {
            return false;
        }
        SspRegistro other = (SspRegistro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspRegistro[ id=" + id + " ]";
    }
    
    public String getNroSolicitiud() {
        return nroSolicitiud;
    }

    public void setNroSolicitiud(String nroSolicitiud) {
        this.nroSolicitiud = nroSolicitiud;
    }

    public SbUsuarioGt getUsuarioCreacionId() {
        return usuarioCreacionId;
    }

    public void setUsuarioCreacionId(SbUsuarioGt usuarioCreacionId) {
        this.usuarioCreacionId = usuarioCreacionId;
    }

    public SspCartaFianza getCartaFianzaId() {
        return cartaFianzaId;
    }

    public void setCartaFianzaId(SspCartaFianza cartaFianzaId) {
        this.cartaFianzaId = cartaFianzaId;
    }

    public TipoBaseGt getTipoAutId() {
        return tipoAutId;
    }

    public void setTipoAutId(TipoBaseGt tipoAutId) {
        this.tipoAutId = tipoAutId;
    }
    
    public SbUsuarioGt getUsuarioRecepcionId() {
        return usuarioRecepcionId;
    }

    public void setUsuarioRecepcionId(SbUsuarioGt usuarioRecepcionId) {
        this.usuarioRecepcionId = usuarioRecepcionId;
    }

    public SspRegistro getCarneSolicitudId() {
        return carneSolicitudId;
    }

    public void setCarneSolicitudId(SspRegistro carneSolicitudId) {
        this.carneSolicitudId = carneSolicitudId;
    }
    
    public TipoBaseGt getModalidadVinculadaId() {
        return modalidadVinculadaId;
    }

    public void setModalidadVinculadaId(TipoBaseGt modalidadVinculadaId) {
        this.modalidadVinculadaId = modalidadVinculadaId;
    }

    public List<SspPoliza> getSspPolizaList() {
        return sspPolizaList;
    }

    public void setSspPolizaList(List<SspPoliza> sspPolizaList) {
        this.sspPolizaList = sspPolizaList;
    }

    public List<SspVehiculoDinero> getSspVehiculoList() {
        return sspVehiculoList;
    }

    public void setSspVehiculoList(List<SspVehiculoDinero> sspVehiculoList) {
        this.sspVehiculoList = sspVehiculoList;
    }

    public List<SspVehiculoCertificacion> getSspVehiculoCertificacionList() {
        return sspVehiculoCertificacionList;
    }

    public void setSspVehiculoCertificacionList(List<SspVehiculoCertificacion> sspVehiculoCertificacionList) {
        this.sspVehiculoCertificacionList = sspVehiculoCertificacionList;
    }

    public SspResolucion getReqResolucionId() {
        return reqResolucionId;
    }

    public void setReqResolucionId(SspResolucion reqResolucionId) {
        this.reqResolucionId = reqResolucionId;
    }

    public List<SspAgenciaFinanciera> getSspAgenciaList() {
        return sspAgenciaList;
    }

    public void setSspAgenciaList(List<SspAgenciaFinanciera> sspAgenciaList) {
        this.sspAgenciaList = sspAgenciaList;
    }

    public List<SspJefeSeguridad> getSspJefeSeguridadList() {
        return sspJefeSeguridadList;
    }

    public void setSspJefeSeguridadList(List<SspJefeSeguridad> sspJefeSeguridadList) {
        this.sspJefeSeguridadList = sspJefeSeguridadList;
    }
  
    
}
