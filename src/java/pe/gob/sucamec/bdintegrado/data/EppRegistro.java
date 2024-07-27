/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
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
import pe.gob.sucamec.sistemabase.data.SbPais;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_REGISTRO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppRegistro.findAll", query = "SELECT e FROM EppRegistro e"),
    @NamedQuery(name = "EppRegistro.findById", query = "SELECT e FROM EppRegistro e WHERE e.id = :id"),
    @NamedQuery(name = "EppRegistro.findByFecha", query = "SELECT e FROM EppRegistro e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "EppRegistro.findByNroExpediente", query = "SELECT e FROM EppRegistro e WHERE e.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "EppRegistro.findByFechaIni", query = "SELECT e FROM EppRegistro e WHERE e.fechaIni = :fechaIni"),
    @NamedQuery(name = "EppRegistro.findByFechaFin", query = "SELECT e FROM EppRegistro e WHERE e.fechaFin = :fechaFin"),
    @NamedQuery(name = "EppRegistro.findByObservacion", query = "SELECT e FROM EppRegistro e WHERE e.observacion = :observacion"),
    @NamedQuery(name = "EppRegistro.findByActivo", query = "SELECT e FROM EppRegistro e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppRegistro.findByAudLogin", query = "SELECT e FROM EppRegistro e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppRegistro.findByAudNumIp", query = "SELECT e FROM EppRegistro e WHERE e.audNumIp = :audNumIp"),
    @NamedQuery(name = "EppRegistro.findBySemestre", query = "SELECT e FROM EppRegistro e WHERE e.semestre = :semestre"),
    @NamedQuery(name = "EppRegistro.findByFlagVenceNoti", query = "SELECT e FROM EppRegistro e WHERE e.flagVenceNoti = :flagVenceNoti")})
public class EppRegistro implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_REGISTRO")
    @SequenceGenerator(name = "SEQ_EPP_REGISTRO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_REGISTRO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
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
    @Column(name = "SEMESTRE")
    private Short semestre;
    @Column(name = "FLAG_VENCE_NOTI")
    private Short flagVenceNoti;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_IMP_EXP_PUERTO", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PUERTO_ADUANERO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppPuertoAduanero> eppPuertoAduaneroList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_REGISTRO_DOCUMENTO", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "DOCUMENTO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppDocumento> eppDocumentoList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_REGISTRO_TALLERDEPO", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TALLERDEPOSITO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppTallerdeposito> eppTallerdepositoList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_AUTORIZACION_EXPLOSIVO", joinColumns = {
        @JoinColumn(name = "AUTORIZACION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "EXPLOSIVO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppExplosivo> eppExplosivoList;
    @ManyToMany(mappedBy = "eppRegistroList")
    private List<EppProveedorCliente> eppProveedorClienteList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_REGISTRO_AUTORIZACION", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "LOCAL_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppLocal> eppLocalList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_REGISTRO_CERTIF", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CERTIFICADO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppCertificado> eppCertificadoList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_DETALLE_REGISTRO_LUGAR", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "LUGAR_USO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppLugarUso> eppLugarUsoList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_LICENCIA_REGISTRO", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "LICENCIA_MANIP_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppLicencia> eppLicenciaList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_IMP_EXP_ADUANA", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "ALMACEN_ADUANERO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppAlmacenAduanero> eppAlmacenAduaneroList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_IMP_EXP_PAISES", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PAIS_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbPais> sbPaisList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_IMP_EXP_VIA", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<TipoExplosivoGt> tipoExplosivoList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_IMP_EXP_ALMACEN", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "ALMACEN_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppAlmacen> eppAlmacenList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_REGISTRO_VEHICULO", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "VEHICULO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppGuiaTransitoVehiculo> eppGuiaTransitoVehiculoList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_REGISTRO_CARNE", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CARNE_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppCarne> eppCarneList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_POLVORIN_REGISTRO", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "POLVORIN_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppPolvorin> eppPolvorinList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private EppRegistroIntSal eppRegistroIntSal;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private EppRegistroGuiaTransito eppRegistroGuiaTransito;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<EppLicencia> eppLicenciaList1;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private EppGuiaTransitoPiro eppGuiaTransitoPiro;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private EppCertificado eppCertificado;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private EppResolucion eppResolucion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private EppCarne eppCarne;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<EppImpExpPirotecnico> eppImpExpPirotecnicoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<EppVehiculoDirecs> eppVehiculoDirecsList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private EppComercializacion eppComercializacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<EppImpExpExplosivo> eppImpExpExplosivoList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private EppEspectaculo eppEspectaculo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private EppTallerdeposito eppTallerdeposito;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<EppPlantaExplosivo> eppPlantaExplosivoList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private EppInternaSalida eppInternaSalida;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<EppRegistroEvento> eppRegistroEventoList;
    @JoinColumn(name = "TIPO_MODIFICATORIA", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoModificatoria;
    @JoinColumn(name = "ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt estado;
    @JoinColumn(name = "TIPO_REG_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoRegId;
    @JoinColumn(name = "TIPO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoOpeId;
    @JoinColumn(name = "TIPO_PRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoProId;
    @JoinColumn(name = "REPRESENTANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersonaGt representanteId;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt empresaId;
    @OneToMany(mappedBy = "registroOpeId")
    private List<EppRegistro> eppRegistroList;
    @JoinColumn(name = "REGISTRO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppRegistro registroOpeId;
    @OneToMany(mappedBy = "registroId")
    private List<EppRegistro> eppRegistroList1;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppRegistro registroId;
    @JoinColumn(name = "COM_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppCom comId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<EppDetalleAutUso> eppDetalleAutUsoList;
    @OneToMany(mappedBy = "regcompradorId")
    private List<EppLibroDetalle> eppLibroDetalleList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private EppPolvorin eppPolvorin;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registroId")
    private EppLibro eppLibro;
    @OneToMany(mappedBy = "registroId")
    private List<EppComprobante> eppComprobanteList;
    @OneToMany(mappedBy = "registroId")
    private List<SbRecibos> sbRecibosList;
    @OneToMany(mappedBy = "registroGtId")
    private List<EppGteRegistro> eppGteRegistroList;
    @JoinColumn(name = "USUARIO_FIRMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuario usuarioFirmaId;
    @JoinColumn(name = "USUARIO_EVALUADOR_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuario usuarioEvaluadorId;  
    public EppRegistro() {
    }

    public EppRegistro(Long id) {
        this.id = id;
    }

    public EppRegistro(Long id, Date fecha, String nroExpediente, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fecha = fecha;
        this.nroExpediente = nroExpediente;
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

    public Short getSemestre() {
        return semestre;
    }

    public void setSemestre(Short semestre) {
        this.semestre = semestre;
    }

    public Short getFlagVenceNoti() {
        return flagVenceNoti;
    }

    public void setFlagVenceNoti(Short flagVenceNoti) {
        this.flagVenceNoti = flagVenceNoti;
    }
    public SbUsuario getUsuarioFirmaId() {
        return usuarioFirmaId;
    }

    public void setUsuarioFirmaId(SbUsuario usuarioFirmaId) {
        this.usuarioFirmaId = usuarioFirmaId;
    }

    public SbUsuario getUsuarioEvaluadorId() {
        return usuarioEvaluadorId;
    }

    public void setUsuarioEvaluadorId(SbUsuario usuarioEvaluadorId) {
        this.usuarioEvaluadorId = usuarioEvaluadorId;
    }

    @XmlTransient
    public List<EppPuertoAduanero> getEppPuertoAduaneroList() {
        return eppPuertoAduaneroList;
    }

    public void setEppPuertoAduaneroList(List<EppPuertoAduanero> eppPuertoAduaneroList) {
        this.eppPuertoAduaneroList = eppPuertoAduaneroList;
    }

    @XmlTransient
    public List<EppDocumento> getEppDocumentoList() {
        return eppDocumentoList;
    }

    public void setEppDocumentoList(List<EppDocumento> eppDocumentoList) {
        this.eppDocumentoList = eppDocumentoList;
    }

    @XmlTransient
    public List<EppTallerdeposito> getEppTallerdepositoList() {
        return eppTallerdepositoList;
    }

    public void setEppTallerdepositoList(List<EppTallerdeposito> eppTallerdepositoList) {
        this.eppTallerdepositoList = eppTallerdepositoList;
    }

    @XmlTransient
    public List<EppExplosivo> getEppExplosivoList() {
        return eppExplosivoList;
    }

    public void setEppExplosivoList(List<EppExplosivo> eppExplosivoList) {
        this.eppExplosivoList = eppExplosivoList;
    }

    @XmlTransient
    public List<EppProveedorCliente> getEppProveedorClienteList() {
        return eppProveedorClienteList;
    }

    public void setEppProveedorClienteList(List<EppProveedorCliente> eppProveedorClienteList) {
        this.eppProveedorClienteList = eppProveedorClienteList;
    }

    @XmlTransient
    public List<EppLocal> getEppLocalList() {
        return eppLocalList;
    }

    public void setEppLocalList(List<EppLocal> eppLocalList) {
        this.eppLocalList = eppLocalList;
    }

    @XmlTransient
    public List<EppCertificado> getEppCertificadoList() {
        return eppCertificadoList;
    }

    public void setEppCertificadoList(List<EppCertificado> eppCertificadoList) {
        this.eppCertificadoList = eppCertificadoList;
    }

    @XmlTransient
    public List<EppLugarUso> getEppLugarUsoList() {
        return eppLugarUsoList;
    }

    public void setEppLugarUsoList(List<EppLugarUso> eppLugarUsoList) {
        this.eppLugarUsoList = eppLugarUsoList;
    }

    @XmlTransient
    public List<EppLicencia> getEppLicenciaList() {
        return eppLicenciaList;
    }

    public void setEppLicenciaList(List<EppLicencia> eppLicenciaList) {
        this.eppLicenciaList = eppLicenciaList;
    }

    @XmlTransient
    public List<EppAlmacenAduanero> getEppAlmacenAduaneroList() {
        return eppAlmacenAduaneroList;
    }

    public void setEppAlmacenAduaneroList(List<EppAlmacenAduanero> eppAlmacenAduaneroList) {
        this.eppAlmacenAduaneroList = eppAlmacenAduaneroList;
    }

    @XmlTransient
    public List<SbPais> getSbPaisList() {
        return sbPaisList;
    }

    public void setSbPaisList(List<SbPais> sbPaisList) {
        this.sbPaisList = sbPaisList;
    }

    @XmlTransient
    public List<TipoExplosivoGt> getTipoExplosivoList() {
        return tipoExplosivoList;
    }

    public void setTipoExplosivoList(List<TipoExplosivoGt> tipoExplosivoList) {
        this.tipoExplosivoList = tipoExplosivoList;
    }

    @XmlTransient
    public List<EppAlmacen> getEppAlmacenList() {
        return eppAlmacenList;
    }

    public void setEppAlmacenList(List<EppAlmacen> eppAlmacenList) {
        this.eppAlmacenList = eppAlmacenList;
    }

    @XmlTransient
    public List<EppGuiaTransitoVehiculo> getEppGuiaTransitoVehiculoList() {
        return eppGuiaTransitoVehiculoList;
    }

    public void setEppGuiaTransitoVehiculoList(List<EppGuiaTransitoVehiculo> eppGuiaTransitoVehiculoList) {
        this.eppGuiaTransitoVehiculoList = eppGuiaTransitoVehiculoList;
    }

    @XmlTransient
    public List<EppCarne> getEppCarneList() {
        return eppCarneList;
    }

    public void setEppCarneList(List<EppCarne> eppCarneList) {
        this.eppCarneList = eppCarneList;
    }

    @XmlTransient
    public List<EppPolvorin> getEppPolvorinList() {
        return eppPolvorinList;
    }

    public void setEppPolvorinList(List<EppPolvorin> eppPolvorinList) {
        this.eppPolvorinList = eppPolvorinList;
    }

    public EppRegistroIntSal getEppRegistroIntSal() {
        return eppRegistroIntSal;
    }

    public void setEppRegistroIntSal(EppRegistroIntSal eppRegistroIntSal) {
        this.eppRegistroIntSal = eppRegistroIntSal;
    }

    public EppRegistroGuiaTransito getEppRegistroGuiaTransito() {
        return eppRegistroGuiaTransito;
    }

    public void setEppRegistroGuiaTransito(EppRegistroGuiaTransito eppRegistroGuiaTransito) {
        this.eppRegistroGuiaTransito = eppRegistroGuiaTransito;
    }

    @XmlTransient
    public List<EppLicencia> getEppLicenciaList1() {
        return eppLicenciaList1;
    }

    public void setEppLicenciaList1(List<EppLicencia> eppLicenciaList1) {
        this.eppLicenciaList1 = eppLicenciaList1;
    }

    public EppGuiaTransitoPiro getEppGuiaTransitoPiro() {
        return eppGuiaTransitoPiro;
    }

    public void setEppGuiaTransitoPiro(EppGuiaTransitoPiro eppGuiaTransitoPiro) {
        this.eppGuiaTransitoPiro = eppGuiaTransitoPiro;
    }

    public EppCertificado getEppCertificado() {
        return eppCertificado;
    }

    public void setEppCertificado(EppCertificado eppCertificado) {
        this.eppCertificado = eppCertificado;
    }

    public EppResolucion getEppResolucion() {
        return eppResolucion;
    }

    public void setEppResolucion(EppResolucion eppResolucion) {
        this.eppResolucion = eppResolucion;
    }

    public EppCarne getEppCarne() {
        return eppCarne;
    }

    public void setEppCarne(EppCarne eppCarne) {
        this.eppCarne = eppCarne;
    }

    @XmlTransient
    public List<EppImpExpPirotecnico> getEppImpExpPirotecnicoList() {
        return eppImpExpPirotecnicoList;
    }

    public void setEppImpExpPirotecnicoList(List<EppImpExpPirotecnico> eppImpExpPirotecnicoList) {
        this.eppImpExpPirotecnicoList = eppImpExpPirotecnicoList;
    }

    @XmlTransient
    public List<EppVehiculoDirecs> getEppVehiculoDirecsList() {
        return eppVehiculoDirecsList;
    }

    public void setEppVehiculoDirecsList(List<EppVehiculoDirecs> eppVehiculoDirecsList) {
        this.eppVehiculoDirecsList = eppVehiculoDirecsList;
    }

    public EppComercializacion getEppComercializacion() {
        return eppComercializacion;
    }

    public void setEppComercializacion(EppComercializacion eppComercializacion) {
        this.eppComercializacion = eppComercializacion;
    }

    @XmlTransient
    public List<EppImpExpExplosivo> getEppImpExpExplosivoList() {
        return eppImpExpExplosivoList;
    }

    public void setEppImpExpExplosivoList(List<EppImpExpExplosivo> eppImpExpExplosivoList) {
        this.eppImpExpExplosivoList = eppImpExpExplosivoList;
    }

    public EppEspectaculo getEppEspectaculo() {
        return eppEspectaculo;
    }

    public void setEppEspectaculo(EppEspectaculo eppEspectaculo) {
        this.eppEspectaculo = eppEspectaculo;
    }

    public EppTallerdeposito getEppTallerdeposito() {
        return eppTallerdeposito;
    }

    public void setEppTallerdeposito(EppTallerdeposito eppTallerdeposito) {
        this.eppTallerdeposito = eppTallerdeposito;
    }

    @XmlTransient
    public List<EppPlantaExplosivo> getEppPlantaExplosivoList() {
        return eppPlantaExplosivoList;
    }

    public void setEppPlantaExplosivoList(List<EppPlantaExplosivo> eppPlantaExplosivoList) {
        this.eppPlantaExplosivoList = eppPlantaExplosivoList;
    }

    public EppInternaSalida getEppInternaSalida() {
        return eppInternaSalida;
    }

    public void setEppInternaSalida(EppInternaSalida eppInternaSalida) {
        this.eppInternaSalida = eppInternaSalida;
    }

    @XmlTransient
    public List<EppRegistroEvento> getEppRegistroEventoList() {
        return eppRegistroEventoList;
    }

    public void setEppRegistroEventoList(List<EppRegistroEvento> eppRegistroEventoList) {
        this.eppRegistroEventoList = eppRegistroEventoList;
    }

    public TipoExplosivoGt getTipoModificatoria() {
        return tipoModificatoria;
    }

    public void setTipoModificatoria(TipoExplosivoGt tipoModificatoria) {
        this.tipoModificatoria = tipoModificatoria;
    }

    public TipoExplosivoGt getEstado() {
        return estado;
    }

    public void setEstado(TipoExplosivoGt estado) {
        this.estado = estado;
    }

    public TipoBaseGt getTipoRegId() {
        return tipoRegId;
    }

    public void setTipoRegId(TipoBaseGt tipoRegId) {
        this.tipoRegId = tipoRegId;
    }

    public TipoBaseGt getTipoOpeId() {
        return tipoOpeId;
    }

    public void setTipoOpeId(TipoBaseGt tipoOpeId) {
        this.tipoOpeId = tipoOpeId;
    }

    public TipoBaseGt getTipoProId() {
        return tipoProId;
    }

    public void setTipoProId(TipoBaseGt tipoProId) {
        this.tipoProId = tipoProId;
    }

    public SbPersonaGt getRepresentanteId() {
        return representanteId;
    }

    public void setRepresentanteId(SbPersonaGt representanteId) {
        this.representanteId = representanteId;
    }

    public SbPersonaGt getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersonaGt empresaId) {
        this.empresaId = empresaId;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    public EppRegistro getRegistroOpeId() {
        return registroOpeId;
    }

    public void setRegistroOpeId(EppRegistro registroOpeId) {
        this.registroOpeId = registroOpeId;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList1() {
        return eppRegistroList1;
    }

    public void setEppRegistroList1(List<EppRegistro> eppRegistroList1) {
        this.eppRegistroList1 = eppRegistroList1;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }

    public EppCom getComId() {
        return comId;
    }

    public void setComId(EppCom comId) {
        this.comId = comId;
    }

    @XmlTransient
    public List<EppDetalleAutUso> getEppDetalleAutUsoList() {
        return eppDetalleAutUsoList;
    }

    public void setEppDetalleAutUsoList(List<EppDetalleAutUso> eppDetalleAutUsoList) {
        this.eppDetalleAutUsoList = eppDetalleAutUsoList;
    }

    @XmlTransient
    public List<EppLibroDetalle> getEppLibroDetalleList() {
        return eppLibroDetalleList;
    }

    public void setEppLibroDetalleList(List<EppLibroDetalle> eppLibroDetalleList) {
        this.eppLibroDetalleList = eppLibroDetalleList;
    }

    public EppPolvorin getEppPolvorin() {
        return eppPolvorin;
    }

    public void setEppPolvorin(EppPolvorin eppPolvorin) {
        this.eppPolvorin = eppPolvorin;
    }

    public EppLibro getEppLibro() {
        return eppLibro;
    }

    public void setEppLibro(EppLibro eppLibro) {
        this.eppLibro = eppLibro;
    }

    @XmlTransient
    public List<EppComprobante> getEppComprobanteList() {
        return eppComprobanteList;
    }

    public void setEppComprobanteList(List<EppComprobante> eppComprobanteList) {
        this.eppComprobanteList = eppComprobanteList;
    }

    @XmlTransient
    public List<SbRecibos> getSbRecibosList() {
        return sbRecibosList;
    }

    public void setSbRecibosList(List<SbRecibos> sbRecibosList) {
        this.sbRecibosList = sbRecibosList;
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
        if (!(object instanceof EppRegistro)) {
            return false;
        }
        EppRegistro other = (EppRegistro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppRegistro[ id=" + id + " ]";
    }

    @XmlTransient
    public List<EppGteRegistro> getEppGteRegistroList() {
        return eppGteRegistroList;
    }

    public void setEppGteRegistroList(List<EppGteRegistro> eppGteRegistroList) {
        this.eppGteRegistroList = eppGteRegistroList;
    }
    
}
