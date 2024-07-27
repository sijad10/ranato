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
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_REGISTRO_GUIA_TRANSITO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppRegistroGuiaTransito.findAll", query = "SELECT e FROM EppRegistroGuiaTransito e"),
    @NamedQuery(name = "EppRegistroGuiaTransito.findById", query = "SELECT e FROM EppRegistroGuiaTransito e WHERE e.id = :id"),
    @NamedQuery(name = "EppRegistroGuiaTransito.findByFechaSalida", query = "SELECT e FROM EppRegistroGuiaTransito e WHERE e.fechaSalida = :fechaSalida"),
    @NamedQuery(name = "EppRegistroGuiaTransito.findByFechaLlegada", query = "SELECT e FROM EppRegistroGuiaTransito e WHERE e.fechaLlegada = :fechaLlegada"),
    @NamedQuery(name = "EppRegistroGuiaTransito.findByDescOtroTiptram", query = "SELECT e FROM EppRegistroGuiaTransito e WHERE e.descOtroTiptram = :descOtroTiptram"),
    @NamedQuery(name = "EppRegistroGuiaTransito.findByNroFactura", query = "SELECT e FROM EppRegistroGuiaTransito e WHERE e.nroFactura = :nroFactura"),
    @NamedQuery(name = "EppRegistroGuiaTransito.findByFechaFactura", query = "SELECT e FROM EppRegistroGuiaTransito e WHERE e.fechaFactura = :fechaFactura"),
    @NamedQuery(name = "EppRegistroGuiaTransito.findByInformacionAdi", query = "SELECT e FROM EppRegistroGuiaTransito e WHERE e.informacionAdi = :informacionAdi"),
    @NamedQuery(name = "EppRegistroGuiaTransito.findByCustodia", query = "SELECT e FROM EppRegistroGuiaTransito e WHERE e.custodia = :custodia"),
    @NamedQuery(name = "EppRegistroGuiaTransito.findByActivo", query = "SELECT e FROM EppRegistroGuiaTransito e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppRegistroGuiaTransito.findByAudLogin", query = "SELECT e FROM EppRegistroGuiaTransito e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppRegistroGuiaTransito.findByAudNumIp", query = "SELECT e FROM EppRegistroGuiaTransito e WHERE e.audNumIp = :audNumIp")})
public class EppRegistroGuiaTransito implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_REGISTRO_GUIA_TRANSITO")
    @SequenceGenerator(name = "SEQ_EPP_REGISTRO_GUIA_TRANSITO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_REGISTRO_GUIA_TRANSITO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FECHA_SALIDA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSalida;
    @Column(name = "FECHA_LLEGADA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLlegada;
    @Size(max = 50)
    @Column(name = "DESC_OTRO_TIPTRAM")
    private String descOtroTiptram;
    @Size(max = 50)
    @Column(name = "NRO_FACTURA")
    private String nroFactura;
    @Column(name = "FECHA_FACTURA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFactura;
    @Size(max = 50)
    @Column(name = "INFORMACION_ADI")
    private String informacionAdi;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTODIA")
    private short custodia;
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
    @JoinTable(schema="BDINTEGRADO", name = "EPP_GTRANSITO_CUSTODIO_DETALLE", joinColumns = {
        @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CUSTODIO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppLicencia> eppLicenciaList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_GTRANSITO_POLICIA_DETALLE", joinColumns = {
        @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "POLICIA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppGuiaTransitoPolicia> eppGuiaTransitoPoliciaList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_GTRANSITO_VEHICULO_DETALLE", joinColumns = {
        @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "VEHICULO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppGuiaTransitoVehiculo> eppGuiaTransitoVehiculoList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_GTRANSITO_CONDUCTOR_DETA", joinColumns = {
        @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CONDUCTOR_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppLicencia> eppLicenciaList1;
    @JoinColumn(name = "TIPO_TRAMITE", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoTramite;
    @JoinColumn(name = "TIPO_PROCESO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoProceso;
    @JoinColumn(name = "TIPO_ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoEstado;
    @JoinColumn(name = "TIPO_MODIFICATORIA", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoModificatoria;
    @JoinColumn(name = "TIPO_TRANSPORTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoTransporte;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt empresaId;
    @JoinColumn(name = "EMPRESA_TRANSP_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersonaGt empresaTranspId;
    @JoinColumn(name = "RESOLUCION_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppResolucion resolucionId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EppRegistro registroId;
    @JoinColumn(name = "DESTINO_ALMACEN", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacen destinoAlmacen;
    @JoinColumn(name = "ORIGEN_ALMACEN", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacen origenAlmacen;
    @JoinColumn(name = "DESTINO_ALMACEN_ADUANA", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacenAduanero destinoAlmacenAduana;
    @JoinColumn(name = "ORIGEN_ALMACEN_ADUANA", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacenAduanero origenAlmacenAduana;
    @JoinColumn(name = "LUGAR_USO_DESTINO", referencedColumnName = "ID")
    @ManyToOne
    private EppLugarUso lugarUsoDestino;
    @JoinColumn(name = "LUGAR_USO_ORIGEN", referencedColumnName = "ID")
    @ManyToOne
    private EppLugarUso lugarUsoOrigen;
    @JoinColumn(name = "LUGAR_USO_UBIGEO_DESTINO", referencedColumnName = "ID")
    @ManyToOne
    private EppLugarUsoUbigeo lugarUsoUbigeoDestino;
    @JoinColumn(name = "LUGAR_USO_UBIGEO_ORIGEN", referencedColumnName = "ID")
    @ManyToOne
    private EppLugarUsoUbigeo lugarUsoUbigeoOrigen;
    @JoinColumn(name = "DESTINO_POLVORIN", referencedColumnName = "ID")
    @ManyToOne
    private EppPolvorin destinoPolvorin;
    @JoinColumn(name = "ORIGEN_POLVORIN", referencedColumnName = "ID")
    @ManyToOne
    private EppPolvorin origenPolvorin;
    @JoinColumn(name = "PUERTO_ADUANERO_ORIGEN", referencedColumnName = "ID")
    @ManyToOne
    private EppPuertoAduanero puertoAduaneroOrigen;
    @JoinColumn(name = "PUERTO_ADUANERO_DESTINO", referencedColumnName = "ID")
    @ManyToOne
    private EppPuertoAduanero puertoAduaneroDestino;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<EppExplosivoSolicitado> eppExplosivoSolicitadoList;
    @OneToMany(mappedBy = "guiaTransitoId")
    private List<EppLibroSaldo> eppLibroSaldoList;
    @JoinColumn(name = "DESTINO_FABRICA", referencedColumnName = "ID")
    @ManyToOne
    private EppLocal destinoFabrica;
    @JoinColumn(name = "ORIGEN_FABRICA", referencedColumnName = "ID")
    @ManyToOne
    private EppLocal origenFabrica;
    @JoinColumn(name = "DESTINO_ALQUILER", referencedColumnName = "ID")
    @ManyToOne
    private EppContratoAlqPolv destinoAlquiler;
    @JoinColumn(name = "ORIGEN_ALQUILER", referencedColumnName = "ID")
    @ManyToOne
    private EppContratoAlqPolv origenAlquiler;
    @Size(max = 500)
    @Column(name = "DOC_ADJUNTO")
    private String docAdjunto;
    

    public EppRegistroGuiaTransito() {
    }

    public EppRegistroGuiaTransito(Long id) {
        this.id = id;
    }

    public EppRegistroGuiaTransito(Long id, short custodia, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.custodia = custodia;
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

    public String getDescOtroTiptram() {
        return descOtroTiptram;
    }

    public void setDescOtroTiptram(String descOtroTiptram) {
        this.descOtroTiptram = descOtroTiptram;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public String getInformacionAdi() {
        return informacionAdi;
    }

    public void setInformacionAdi(String informacionAdi) {
        this.informacionAdi = informacionAdi;
    }

    public short getCustodia() {
        return custodia;
    }

    public void setCustodia(short custodia) {
        this.custodia = custodia;
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

    @XmlTransient
    public List<EppLicencia> getEppLicenciaList() {
        return eppLicenciaList;
    }

    public void setEppLicenciaList(List<EppLicencia> eppLicenciaList) {
        this.eppLicenciaList = eppLicenciaList;
    }

    @XmlTransient
    public List<EppGuiaTransitoPolicia> getEppGuiaTransitoPoliciaList() {
        return eppGuiaTransitoPoliciaList;
    }

    public void setEppGuiaTransitoPoliciaList(List<EppGuiaTransitoPolicia> eppGuiaTransitoPoliciaList) {
        this.eppGuiaTransitoPoliciaList = eppGuiaTransitoPoliciaList;
    }

    @XmlTransient
    public List<EppGuiaTransitoVehiculo> getEppGuiaTransitoVehiculoList() {
        return eppGuiaTransitoVehiculoList;
    }

    public void setEppGuiaTransitoVehiculoList(List<EppGuiaTransitoVehiculo> eppGuiaTransitoVehiculoList) {
        this.eppGuiaTransitoVehiculoList = eppGuiaTransitoVehiculoList;
    }

    @XmlTransient
    public List<EppLicencia> getEppLicenciaList1() {
        return eppLicenciaList1;
    }

    public void setEppLicenciaList1(List<EppLicencia> eppLicenciaList1) {
        this.eppLicenciaList1 = eppLicenciaList1;
    }

    public TipoExplosivoGt getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoExplosivoGt tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public TipoExplosivoGt getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(TipoExplosivoGt tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public TipoExplosivoGt getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(TipoExplosivoGt tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public TipoExplosivoGt getTipoModificatoria() {
        return tipoModificatoria;
    }

    public void setTipoModificatoria(TipoExplosivoGt tipoModificatoria) {
        this.tipoModificatoria = tipoModificatoria;
    }

    public TipoExplosivoGt getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(TipoExplosivoGt tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public SbPersonaGt getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersonaGt empresaId) {
        this.empresaId = empresaId;
    }

    public SbPersonaGt getEmpresaTranspId() {
        return empresaTranspId;
    }

    public void setEmpresaTranspId(SbPersonaGt empresaTranspId) {
        this.empresaTranspId = empresaTranspId;
    }

    public EppResolucion getResolucionId() {
        return resolucionId;
    }

    public void setResolucionId(EppResolucion resolucionId) {
        this.resolucionId = resolucionId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }

    public EppAlmacen getDestinoAlmacen() {
        return destinoAlmacen;
    }

    public void setDestinoAlmacen(EppAlmacen destinoAlmacen) {
        this.destinoAlmacen = destinoAlmacen;
    }

    public EppAlmacen getOrigenAlmacen() {
        return origenAlmacen;
    }

    public void setOrigenAlmacen(EppAlmacen origenAlmacen) {
        this.origenAlmacen = origenAlmacen;
    }

    public EppAlmacenAduanero getDestinoAlmacenAduana() {
        return destinoAlmacenAduana;
    }

    public void setDestinoAlmacenAduana(EppAlmacenAduanero destinoAlmacenAduana) {
        this.destinoAlmacenAduana = destinoAlmacenAduana;
    }

    public EppAlmacenAduanero getOrigenAlmacenAduana() {
        return origenAlmacenAduana;
    }

    public void setOrigenAlmacenAduana(EppAlmacenAduanero origenAlmacenAduana) {
        this.origenAlmacenAduana = origenAlmacenAduana;
    }

    public EppLugarUso getLugarUsoDestino() {
        return lugarUsoDestino;
    }

    public void setLugarUsoDestino(EppLugarUso lugarUsoDestino) {
        this.lugarUsoDestino = lugarUsoDestino;
    }

    public EppLugarUso getLugarUsoOrigen() {
        return lugarUsoOrigen;
    }

    public void setLugarUsoOrigen(EppLugarUso lugarUsoOrigen) {
        this.lugarUsoOrigen = lugarUsoOrigen;
    }

    public EppLugarUsoUbigeo getLugarUsoUbigeoDestino() {
        return lugarUsoUbigeoDestino;
    }

    public void setLugarUsoUbigeoDestino(EppLugarUsoUbigeo lugarUsoUbigeoDestino) {
        this.lugarUsoUbigeoDestino = lugarUsoUbigeoDestino;
    }

    public EppLugarUsoUbigeo getLugarUsoUbigeoOrigen() {
        return lugarUsoUbigeoOrigen;
    }

    public void setLugarUsoUbigeoOrigen(EppLugarUsoUbigeo lugarUsoUbigeoOrigen) {
        this.lugarUsoUbigeoOrigen = lugarUsoUbigeoOrigen;
    }

    public EppPolvorin getDestinoPolvorin() {
        return destinoPolvorin;
    }

    public void setDestinoPolvorin(EppPolvorin destinoPolvorin) {
        this.destinoPolvorin = destinoPolvorin;
    }

    public EppPolvorin getOrigenPolvorin() {
        return origenPolvorin;
    }

    public void setOrigenPolvorin(EppPolvorin origenPolvorin) {
        this.origenPolvorin = origenPolvorin;
    }

    public EppPuertoAduanero getPuertoAduaneroOrigen() {
        return puertoAduaneroOrigen;
    }

    public void setPuertoAduaneroOrigen(EppPuertoAduanero puertoAduaneroOrigen) {
        this.puertoAduaneroOrigen = puertoAduaneroOrigen;
    }

    public EppPuertoAduanero getPuertoAduaneroDestino() {
        return puertoAduaneroDestino;
    }

    public void setPuertoAduaneroDestino(EppPuertoAduanero puertoAduaneroDestino) {
        this.puertoAduaneroDestino = puertoAduaneroDestino;
    }

    @XmlTransient
    public List<EppExplosivoSolicitado> getEppExplosivoSolicitadoList() {
        return eppExplosivoSolicitadoList;
    }

    public void setEppExplosivoSolicitadoList(List<EppExplosivoSolicitado> eppExplosivoSolicitadoList) {
        this.eppExplosivoSolicitadoList = eppExplosivoSolicitadoList;
    }

    @XmlTransient
    public List<EppLibroSaldo> getEppLibroSaldoList() {
        return eppLibroSaldoList;
    }

    public void setEppLibroSaldoList(List<EppLibroSaldo> eppLibroSaldoList) {
        this.eppLibroSaldoList = eppLibroSaldoList;
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
        if (!(object instanceof EppRegistroGuiaTransito)) {
            return false;
        }
        EppRegistroGuiaTransito other = (EppRegistroGuiaTransito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppRegistroGuiaTransito[ id=" + id + " ]";
    }
    
    public EppLocal getDestinoFabrica() {
        return destinoFabrica;
    }

    public void setDestinoFabrica(EppLocal destinoFabrica) {
        this.destinoFabrica = destinoFabrica;
    }

    public EppLocal getOrigenFabrica() {
        return origenFabrica;
    }

    public void setOrigenFabrica(EppLocal origenFabrica) {
        this.origenFabrica = origenFabrica;
    }
    
     public EppContratoAlqPolv getDestinoAlquiler() {
        return destinoAlquiler;
    }

    public void setDestinoAlquiler(EppContratoAlqPolv destinoAlquiler) {
        this.destinoAlquiler = destinoAlquiler;
    }

    public EppContratoAlqPolv getOrigenAlquiler() {
        return origenAlquiler;
    }

    public void setOrigenAlquiler(EppContratoAlqPolv origenAlquiler) {
        this.origenAlquiler = origenAlquiler;
    }
    
    public String getDocAdjunto() {
        return docAdjunto;
    }

    public void setDocAdjunto(String docAdjunto) {
        this.docAdjunto = docAdjunto;
    }
}
