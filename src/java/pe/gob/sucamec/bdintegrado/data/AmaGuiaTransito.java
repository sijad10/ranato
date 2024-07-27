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
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbPersona;
//import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.SbUsuarioGt;

/**
 *
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_GUIA_TRANSITO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaGuiaTransito.findAll", query = "SELECT a FROM AmaGuiaTransito a"),
    @NamedQuery(name = "AmaGuiaTransito.findById", query = "SELECT a FROM AmaGuiaTransito a WHERE a.id = :id"),
    @NamedQuery(name = "AmaGuiaTransito.findByNroExpediente", query = "SELECT a FROM AmaGuiaTransito a WHERE a.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "AmaGuiaTransito.findByNroGuia", query = "SELECT a FROM AmaGuiaTransito a WHERE a.nroGuia = :nroGuia"),
    @NamedQuery(name = "AmaGuiaTransito.findByFechaEmision", query = "SELECT a FROM AmaGuiaTransito a WHERE a.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "AmaGuiaTransito.findByFechaVencimiento", query = "SELECT a FROM AmaGuiaTransito a WHERE a.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "AmaGuiaTransito.findByActivo", query = "SELECT a FROM AmaGuiaTransito a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaGuiaTransito.findByAudLogin", query = "SELECT a FROM AmaGuiaTransito a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaGuiaTransito.findByAudNumIp", query = "SELECT a FROM AmaGuiaTransito a WHERE a.audNumIp = :audNumIp"),
    @NamedQuery(name = "AmaGuiaTransito.findByHashQr", query = "SELECT a FROM AmaGuiaTransito a WHERE a.hashQr = :hashQr")})
public class AmaGuiaTransito implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_GUIA_TRANSITO")
    @SequenceGenerator(name = "SEQ_AMA_GUIA_TRANSITO", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_GUIA_TRANSITO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NRO_GUIA")
    private String nroGuia;
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
    @Size(max = 60)
    @Column(name = "HASH_QR")
    private String hashQr;
    @JoinTable(name = "AMA_GUIA_TRANSITO_ARMAS",schema = "BDINTEGRADO", joinColumns = {
        @JoinColumn(name = "GAMAC_GUIA_TRANSITO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "GAMAC_ARMA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<AmaArma> amaArmaList;
    @JoinTable(schema = "BDINTEGRADO", name = "AMA_GUIA_TRANSITO_INVENTARIO", joinColumns = {
        @JoinColumn(name = "GAMAC_GUIA_TRANSITO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "INVENTARIO_ARMA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<AmaInventarioArma> amaInventarioArmaList;

    @JoinTable(schema = "BDINTEGRADO", name = "AMA_ACTA_DOCUMENTO", joinColumns = {
        @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "DOCUMENTO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<AmaDocumento> amaDocumentoList;
    
    @JoinTable(schema = "BDINTEGRADO", name = "AMA_ACTA_INFRACTOR", joinColumns = {
        @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbPersona> sbPersonaList;
        
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuarioId;
    @JoinColumn(name = "SOLICITUD_RECOJO_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaSolicitudRecojo solicitudRecojoId;
    @JoinColumn(name = "DIRECCION_DESTINO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDireccion direccionDestinoId;
    @JoinColumn(name = "DIRECCION_ORIGEN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDireccion direccionOrigenId;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt estadoId;
    @JoinColumn(name = "TIPO_GUIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoGuiaId;    
    @Column(name = "FECHA_LLEGADA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLlegada;
    @Column(name = "FECHA_SALIDA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSalida;
    @JoinColumn(name = "TIPO_ORIGEN", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoOrigen;
    @JoinColumn(name = "TIPO_DESTINO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoDestino;
    @JoinColumn(name = "SOLICITANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona solicitanteId;
    @JoinColumn(name = "REPRESENTANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona representanteId;
    @JoinColumn(name = "GUIA_REFERENCIADA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaGuiaTransito guiaReferenciadaId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "guiaTransitoId")
    private List<AmaGuiaMuniciones> amaGuiaMunicionesList;
    @OneToMany(mappedBy = "guiaTransitoId")
    private List<AmaInventarioAccesorios> amaInventarioAccesoriosList;
    @Size(max = 500)
    @Column(name = "OBSERVACION")
    private String observacion;    
    @JoinColumn(name = "INSTITUCION_ORDENA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac institucionOrdenaId;
    @JoinColumn(name = "TIPO_REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoRegistroId;
    @JoinColumn(name = "DEVOLUCION_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaGuiaTransito devolucionId;
    @Size(max = 200)
    @Column(name = "RESOLUCION_CANCELACION")
    private String resoluCancelacion;
    @JoinColumn(name = "TIPO_INGRESO", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac tipoIngreso;
    @JoinColumn(name = "PROCEDENCIA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac procedenciaId;
    @Size(max = 200)
    @Column(name = "DEPENDENCIA_PROC")
    private String dependenciaProc;
    @Size(max = 200)
    @Column(name = "DEPENDENCIA_INST")
    private String dependenciaInst;
    @JoinColumn(name = "MOTIVO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac motivoId;
    @JoinColumn(name = "TIPO_OPERACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoOperacionId;
    @JoinColumn(name = "DESTINO_ALMACEN_ADUANA", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacenAduanero destinoAlmacenAduana;
    @JoinColumn(name = "DESTINO_PUERTO", referencedColumnName = "ID")
    @ManyToOne
    private EppPuertoAduanero destinoPuerto;
    @JoinColumn(name = "ORIGEN_ALMACEN_ADUANA", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacenAduanero origenAlmacenAduana;
    @JoinColumn(name = "ORIGEN_PUERTO", referencedColumnName = "ID")
    @ManyToOne
    private EppPuertoAduanero origenPuerto;
    @JoinColumn(name = "RESPONSABLE_INGSAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona responsableIngsalId;
    @Size(max = 50)
    @Column(name = "RG_REFERENCIADA")
    private String rgReferenciada;
    @Column(name = "FECHA_RESOLU_CANCELACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaResoluCancelacion;
    
    
    public AmaGuiaTransito() {
    }

    public AmaGuiaTransito(Long id) {
        this.id = id;
    }

    public AmaGuiaTransito(Long id, String nroGuia, Date fechaEmision, Date fechaVencimiento, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroGuia = nroGuia;
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

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public String getNroGuia() {
        return nroGuia;
    }

    public void setNroGuia(String nroGuia) {
        this.nroGuia = nroGuia;
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

    public String getHashQr() {
        return hashQr;
    }

    public void setHashQr(String hashQr) {
        this.hashQr = hashQr;
    }

    @XmlTransient
    public List<AmaArma> getAmaArmaList() {
        return amaArmaList;
    }

    public void setAmaArmaList(List<AmaArma> amaArmaList) {
        this.amaArmaList = amaArmaList;
    }

    public AmaSolicitudRecojo getSolicitudRecojoId() {
        return solicitudRecojoId;
    }

    public void setSolicitudRecojoId(AmaSolicitudRecojo solicitudRecojoId) {
        this.solicitudRecojoId = solicitudRecojoId;
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
        if (!(object instanceof AmaGuiaTransito)) {
            return false;
        }
        AmaGuiaTransito other = (AmaGuiaTransito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito[ id=" + id + " ]";
    }

    /**
     * @return the direccionDestinoId
     */
    public SbDireccion getDireccionDestinoId() {
        return direccionDestinoId;
    }

    /**
     * @param direccionDestinoId the direccionDestinoId to set
     */
    public void setDireccionDestinoId(SbDireccion direccionDestinoId) {
        this.direccionDestinoId = direccionDestinoId;
    }

    /**
     * @return the direccionOrigenId
     */
    public SbDireccion getDireccionOrigenId() {
        return direccionOrigenId;
    }

    /**
     * @param direccionOrigenId the direccionOrigenId to set
     */
    public void setDireccionOrigenId(SbDireccion direccionOrigenId) {
        this.direccionOrigenId = direccionOrigenId;
    }

    /**
     * @return the estadoId
     */
    public TipoBaseGt getEstadoId() {
        return estadoId;
    }

    /**
     * @param estadoId the estadoId to set
     */
    public void setEstadoId(TipoBaseGt estadoId) {
        this.estadoId = estadoId;
    }

    /**
     * @return the tipoGuiaId
     */
    public TipoGamac getTipoGuiaId() {
        return tipoGuiaId;
    }

    /**
     * @param tipoGuiaId the tipoGuiaId to set
     */
    public void setTipoGuiaId(TipoGamac tipoGuiaId) {
        this.tipoGuiaId = tipoGuiaId;
    }

    /**
     * @return the usuarioId
     */
    public SbUsuarioGt getUsuarioId() {
        return usuarioId;
    }

    /**
     * @param usuarioId the usuarioId to set
     */
    public void setUsuarioId(SbUsuarioGt usuarioId) {
        this.usuarioId = usuarioId;
    }

    @XmlTransient
    public List<AmaInventarioArma> getAmaInventarioArmaList() {
        return amaInventarioArmaList;
    }

    public void setAmaInventarioArmaList(List<AmaInventarioArma> amaInventarioArmaList) {
        this.amaInventarioArmaList = amaInventarioArmaList;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Date getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(Date fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public TipoGamac getTipoOrigen() {
        return tipoOrigen;
    }

    public void setTipoOrigen(TipoGamac tipoOrigen) {
        this.tipoOrigen = tipoOrigen;
    }

    public TipoGamac getTipoDestino() {
        return tipoDestino;
    }

    public void setTipoDestino(TipoGamac tipoDestino) {
        this.tipoDestino = tipoDestino;
    }
    
    /**
     * @return the solicitanteId
     */
    public SbPersona getSolicitanteId() {
        return solicitanteId;
    }

    /**
     * @param solicitanteId the solicitanteId to set
     */
    public void setSolicitanteId(SbPersona solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    /**
     * @return the representanteId
     */
    public SbPersona getRepresentanteId() {
        return representanteId;
    }

    /**
     * @param representanteId the representanteId to set
     */
    public void setRepresentanteId(SbPersona representanteId) {
        this.representanteId = representanteId;
    }

    /**
     * @return the guiaReferenciadaId
     */
    public AmaGuiaTransito getGuiaReferenciadaId() {
        return guiaReferenciadaId;
    }

    /**
     * @param guiaReferenciadaId the guiaReferenciadaId to set
     */
    public void setGuiaReferenciadaId(AmaGuiaTransito guiaReferenciadaId) {
        this.guiaReferenciadaId = guiaReferenciadaId;
    }

    /**
     * @return the sbPersonaList
     */
    public List<SbPersona> getSbPersonaList() {
        return sbPersonaList;
    }

    /**
     * @param sbPersonaList the sbPersonaList to set
     */
    public void setSbPersonaList(List<SbPersona> sbPersonaList) {
        this.sbPersonaList = sbPersonaList;
    }

    /**
     * @return the amaDocumentoList
     */
    public List<AmaDocumento> getAmaDocumentoList() {
        return amaDocumentoList;
    }

    /**
     * @param amaDocumentoList the amaDocumentoList to set
     */
    public void setAmaDocumentoList(List<AmaDocumento> amaDocumentoList) {
        this.amaDocumentoList = amaDocumentoList;
    }

    /**
     * @return the amaGuiaMunicionesList
     */
    public List<AmaGuiaMuniciones> getAmaGuiaMunicionesList() {
        return amaGuiaMunicionesList;
    }

    /**
     * @param amaGuiaMunicionesList the amaGuiaMunicionesList to set
     */
    public void setAmaGuiaMunicionesList(List<AmaGuiaMuniciones> amaGuiaMunicionesList) {
        this.amaGuiaMunicionesList = amaGuiaMunicionesList;
    }

    @XmlTransient
    public List<AmaInventarioAccesorios> getAmaInventarioAccesoriosList() {
        return amaInventarioAccesoriosList;
    }

    public void setAmaInventarioAccesoriosList(List<AmaInventarioAccesorios> amaInventarioAccesoriosList) {
        this.amaInventarioAccesoriosList = amaInventarioAccesoriosList;
    }

    /**
     * @return the observacion
     */
    public String getObservacion() {
        return observacion;
    }

    /**
     * @param observacion the observacion to set
     */
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    /**
     * @return the institucionOrdenaId
     */
    public TipoGamac getInstitucionOrdenaId() {
        return institucionOrdenaId;
    }

    /**
     * @param institucionOrdenaId the institucionOrdenaId to set
     */
    public void setInstitucionOrdenaId(TipoGamac institucionOrdenaId) {
        this.institucionOrdenaId = institucionOrdenaId;
    }

    /**
     * @return the tipoRegistroId
     */
    public TipoBaseGt getTipoRegistroId() {
        return tipoRegistroId;
    }

    /**
     * @param tipoRegistroId the tipoRegistroId to set
     */
    public void setTipoRegistroId(TipoBaseGt tipoRegistroId) {
        this.tipoRegistroId = tipoRegistroId;
    }

    public AmaGuiaTransito getDevolucionId() {
        return devolucionId;
    }

    public void setDevolucionId(AmaGuiaTransito devolucionId) {
        this.devolucionId = devolucionId;
    }

    public String getResoluCancelacion() {
        return resoluCancelacion;
    }

    public void setResoluCancelacion(String resoluCancelacion) {
        this.resoluCancelacion = resoluCancelacion;
    }    

    /**
     * @return the tipoIngreso
     */
    public TipoGamac getTipoIngreso() {
        return tipoIngreso;
    }

    /**
     * @param tipoIngreso the tipoIngreso to set
     */
    public void setTipoIngreso(TipoGamac tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }

    /**
     * @return the procedenciaId
     */
    public TipoGamac getProcedenciaId() {
        return procedenciaId;
    }

    /**
     * @param procedenciaId the procedenciaId to set
     */
    public void setProcedenciaId(TipoGamac procedenciaId) {
        this.procedenciaId = procedenciaId;
    }

    /**
     * @return the dependenciaProc
     */
    public String getDependenciaProc() {
        return dependenciaProc;
    }

    /**
     * @param dependenciaProc the dependenciaProc to set
     */
    public void setDependenciaProc(String dependenciaProc) {
        this.dependenciaProc = dependenciaProc;
    }

    /**
     * @return the dependenciaInst
     */
    public String getDependenciaInst() {
        return dependenciaInst;
    }

    /**
     * @param dependenciaInst the dependenciaInst to set
     */
    public void setDependenciaInst(String dependenciaInst) {
        this.dependenciaInst = dependenciaInst;
    }

    /**
     * @return the motivoId
     */
    public TipoGamac getMotivoId() {
        return motivoId;
    }

    /**
     * @param motivoId the motivoId to set
     */
    public void setMotivoId(TipoGamac motivoId) {
        this.motivoId = motivoId;
    }

    /**
     * @return the tipoOperacionId
     */
    public TipoBaseGt getTipoOperacionId() {
        return tipoOperacionId;
    }

    /**
     * @param tipoOperacionId the tipoOperacionId to set
     */
    public void setTipoOperacionId(TipoBaseGt tipoOperacionId) {
        this.tipoOperacionId = tipoOperacionId;
    }

    /**
     * @return the destinoAlmacenAduana
     */
    public EppAlmacenAduanero getDestinoAlmacenAduana() {
        return destinoAlmacenAduana;
    }

    /**
     * @param destinoAlmacenAduana the destinoAlmacenAduana to set
     */
    public void setDestinoAlmacenAduana(EppAlmacenAduanero destinoAlmacenAduana) {
        this.destinoAlmacenAduana = destinoAlmacenAduana;
    }

    /**
     * @return the destinoPuerto
     */
    public EppPuertoAduanero getDestinoPuerto() {
        return destinoPuerto;
    }

    /**
     * @param destinoPuerto the destinoPuerto to set
     */
    public void setDestinoPuerto(EppPuertoAduanero destinoPuerto) {
        this.destinoPuerto = destinoPuerto;
    }

    /**
     * @return the origenAlmacenAduana
     */
    public EppAlmacenAduanero getOrigenAlmacenAduana() {
        return origenAlmacenAduana;
    }

    /**
     * @param origenAlmacenAduana the origenAlmacenAduana to set
     */
    public void setOrigenAlmacenAduana(EppAlmacenAduanero origenAlmacenAduana) {
        this.origenAlmacenAduana = origenAlmacenAduana;
    }

    /**
     * @return the origenPuerto
     */
    public EppPuertoAduanero getOrigenPuerto() {
        return origenPuerto;
    }

    /**
     * @param origenPuerto the origenPuerto to set
     */
    public void setOrigenPuerto(EppPuertoAduanero origenPuerto) {
        this.origenPuerto = origenPuerto;
    }

    public SbPersona getResponsableIngsalId() {
        return responsableIngsalId;
    }

    public void setResponsableIngsalId(SbPersona responsableIngsalId) {
        this.responsableIngsalId = responsableIngsalId;
    }

    /**
     * @return the rgReferenciada
     */
    public String getRgReferenciada() {
        return rgReferenciada;
    }

    /**
     * @param rgReferenciada the rgReferenciada to set
     */
    public void setRgReferenciada(String rgReferenciada) {
        this.rgReferenciada = rgReferenciada;
    }

    public Date getFechaResoluCancelacion() {
        return fechaResoluCancelacion;
    }

    public void setFechaResoluCancelacion(Date fechaResoluCancelacion) {
        this.fechaResoluCancelacion = fechaResoluCancelacion;
    }
    
}
