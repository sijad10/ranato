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
@Table(name = "EPP_GTE_REGISTRO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppGteRegistro.findAll", query = "SELECT e FROM EppGteRegistro e"),
    @NamedQuery(name = "EppGteRegistro.findById", query = "SELECT e FROM EppGteRegistro e WHERE e.id = :id")
    })
public class EppGteRegistro implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_GTE_REGISTRO")
    @SequenceGenerator(name = "SEQ_EPP_GTE_REGISTRO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_GTE_REGISTRO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FABRICA_ORIGEN")
    private Long fabricaOrigen;
    @Column(name = "FABRICA_DESTINO")
    private Long fabricaDestino;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
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
    @JoinTable(schema="BDINTEGRADO", name = "EPP_GTE_REGISTRO_DOCUMENTO", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "DOCUMENTO_ID", referencedColumnName = "ID")})
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<EppDocumento> eppDocumentoList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_GTE_REGISTRO_RECIBO", joinColumns = {
        @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "RECIBO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbRecibos> sbRecibosList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_GTE_CONDUCTOR_DETALLE", joinColumns = {
        @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CONDUCTOR_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppLicencia> eppLicenciaList1;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_GTE_VEHICULO_DETALLE", joinColumns = {
        @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "VEHICULO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppGuiaTransitoVehiculo> eppGuiaTransitoVehiculoList;
    @JoinColumn(name = "TIPO_ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoEstado;
    @JoinColumn(name = "ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt estado;
    @JoinColumn(name = "TIPO_PROCESO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoProceso;
    @JoinColumn(name = "TIPO_TRANSPORTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoTransporte;
    @JoinColumn(name = "TIPO_TRAMITE", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoTramite;
    @JoinColumn(name = "TIPO_PRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoProId;
    @JoinColumn(name = "TIPO_OPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoOpeId;
    @JoinColumn(name = "TIPO_REG_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoRegId;
    @JoinColumn(name = "EMPRESA_TRANSP_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersonaGt empresaTranspId;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt empresaId;
    @JoinColumn(name = "RESOLUCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppResolucion resolucionId;
    @JoinColumn(name = "REGISTRO_GT_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppRegistro registroGtId;
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
    @JoinColumn(name = "LUGAR_USO_UBIGEO_ORIGEN", referencedColumnName = "ID")
    @ManyToOne
    private EppLugarUsoUbigeo lugarUsoUbigeoOrigen;
    @JoinColumn(name = "LUGAR_USO_UBIGEO_DESTINO", referencedColumnName = "ID")
    @ManyToOne
    private EppLugarUsoUbigeo lugarUsoUbigeoDestino;
    @JoinColumn(name = "ORIGEN_POLVORIN", referencedColumnName = "ID")
    @ManyToOne
    private EppPolvorin origenPolvorin;
    @JoinColumn(name = "DESTINO_POLVORIN", referencedColumnName = "ID")
    @ManyToOne
    private EppPolvorin destinoPolvorin;
    @JoinColumn(name = "PUERTO_ADUANERO_DESTINO", referencedColumnName = "ID")
    @ManyToOne
    private EppPuertoAduanero puertoAduaneroDestino;
    @JoinColumn(name = "PUERTO_ADUANERO_ORIGEN", referencedColumnName = "ID")
    @ManyToOne
    private EppPuertoAduanero puertoAduaneroOrigen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<EppGteExplosivoSolicita> eppGteExplosivoSolicitaList;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTODIA")
    private short custodia;
    @Size(max = 100)
    @Column(name = "NRO_SOLICITUD")
    private String nroSolicitud;
    @JoinColumn(name = "ORIGEN_FABRICA", referencedColumnName = "ID")
    @ManyToOne
    private EppLocal origenFabrica;
    @JoinColumn(name = "DESTINO_FABRICA", referencedColumnName = "ID")
    @ManyToOne
    private EppLocal destinoFabrica;
    @JoinColumn(name = "DESTINO_ALQUILER_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppContratoAlqPolv destinoAlquilerId;
    @JoinColumn(name = "ORIGEN_ALQUILER_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppContratoAlqPolv origenAlquilerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gteRegistroId")
    private List<EppGteDeposito> eppGteDepositoList;

    public EppGteRegistro() {
    }

    public EppGteRegistro(Long id) {
        this.id = id;
    }

    public EppGteRegistro(Long id, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }
    
    public String getNroSolicitud() {
        return nroSolicitud;
    }

    public void setNroSolicitud(String nroSolicitud) {
        this.nroSolicitud = nroSolicitud;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFabricaOrigen() {
        return fabricaOrigen;
    }

    public void setFabricaOrigen(Long fabricaOrigen) {
        this.fabricaOrigen = fabricaOrigen;
    }

    public Long getFabricaDestino() {
        return fabricaDestino;
    }

    public void setFabricaDestino(Long fabricaDestino) {
        this.fabricaDestino = fabricaDestino;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
    public List<EppDocumento> getEppDocumentoList() {
        return eppDocumentoList;
    }

    public void setEppDocumentoList(List<EppDocumento> eppDocumentoList) {
        this.eppDocumentoList = eppDocumentoList;
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

    @XmlTransient
    public List<SbRecibos> getSbRecibosList() {
        return sbRecibosList;
    }

    public void setSbRecibosList(List<SbRecibos> sbRecibosList) {
        this.sbRecibosList = sbRecibosList;
    }

    public TipoExplosivoGt getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(TipoExplosivoGt tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public TipoExplosivoGt getEstado() {
        return estado;
    }

    public void setEstado(TipoExplosivoGt estado) {
        this.estado = estado;
    }

    public TipoExplosivoGt getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(TipoExplosivoGt tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public TipoExplosivoGt getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(TipoExplosivoGt tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public TipoExplosivoGt getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoExplosivoGt tipoTramite) {
        this.tipoTramite = tipoTramite;
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

    public TipoBaseGt getTipoRegId() {
        return tipoRegId;
    }

    public void setTipoRegId(TipoBaseGt tipoRegId) {
        this.tipoRegId = tipoRegId;
    }

    public SbPersonaGt getEmpresaTranspId() {
        return empresaTranspId;
    }

    public void setEmpresaTranspId(SbPersonaGt empresaTranspId) {
        this.empresaTranspId = empresaTranspId;
    }

    public SbPersonaGt getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersonaGt empresaId) {
        this.empresaId = empresaId;
    }

    public EppResolucion getResolucionId() {
        return resolucionId;
    }

    public void setResolucionId(EppResolucion resolucionId) {
        this.resolucionId = resolucionId;
    }

    public EppRegistro getRegistroGtId() {
        return registroGtId;
    }

    public void setRegistroGtId(EppRegistro registroGtId) {
        this.registroGtId = registroGtId;
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

    public EppLugarUsoUbigeo getLugarUsoUbigeoOrigen() {
        return lugarUsoUbigeoOrigen;
    }

    public void setLugarUsoUbigeoOrigen(EppLugarUsoUbigeo lugarUsoUbigeoOrigen) {
        this.lugarUsoUbigeoOrigen = lugarUsoUbigeoOrigen;
    }

    public EppLugarUsoUbigeo getLugarUsoUbigeoDestino() {
        return lugarUsoUbigeoDestino;
    }

    public void setLugarUsoUbigeoDestino(EppLugarUsoUbigeo lugarUsoUbigeoDestino) {
        this.lugarUsoUbigeoDestino = lugarUsoUbigeoDestino;
    }

    public EppPolvorin getOrigenPolvorin() {
        return origenPolvorin;
    }

    public void setOrigenPolvorin(EppPolvorin origenPolvorin) {
        this.origenPolvorin = origenPolvorin;
    }

    public EppPolvorin getDestinoPolvorin() {
        return destinoPolvorin;
    }

    public void setDestinoPolvorin(EppPolvorin destinoPolvorin) {
        this.destinoPolvorin = destinoPolvorin;
    }

    public EppPuertoAduanero getPuertoAduaneroDestino() {
        return puertoAduaneroDestino;
    }

    public void setPuertoAduaneroDestino(EppPuertoAduanero puertoAduaneroDestino) {
        this.puertoAduaneroDestino = puertoAduaneroDestino;
    }

    public EppPuertoAduanero getPuertoAduaneroOrigen() {
        return puertoAduaneroOrigen;
    }

    public void setPuertoAduaneroOrigen(EppPuertoAduanero puertoAduaneroOrigen) {
        this.puertoAduaneroOrigen = puertoAduaneroOrigen;
    }
    
    public short getCustodia() {
        return custodia;
    }

    public void setCustodia(short custodia) {
        this.custodia = custodia;
    }

    @XmlTransient
    public List<EppGteExplosivoSolicita> getEppGteExplosivoSolicitaList() {
        return eppGteExplosivoSolicitaList;
    }

    public void setEppGteExplosivoSolicitaList(List<EppGteExplosivoSolicita> eppGteExplosivoSolicitaList) {
        this.eppGteExplosivoSolicitaList = eppGteExplosivoSolicitaList;
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
        if (!(object instanceof EppGteRegistro)) {
            return false;
        }
        EppGteRegistro other = (EppGteRegistro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.EppGteRegistro[ id=" + id + " ]";
    }
    
    public EppLocal getOrigenFabrica() {
        return origenFabrica;
    }

    public void setOrigenFabrica(EppLocal origenFabrica) {
        this.origenFabrica = origenFabrica;
    }

    public EppLocal getDestinoFabrica() {
        return destinoFabrica;
    }

    public void setDestinoFabrica(EppLocal destinoFabrica) {
        this.destinoFabrica = destinoFabrica;
    }
    
    public EppContratoAlqPolv getDestinoAlquilerId() {
        return destinoAlquilerId;
    }

    public void setDestinoAlquilerId(EppContratoAlqPolv destinoAlquilerId) {
        this.destinoAlquilerId = destinoAlquilerId;
    }

    public EppContratoAlqPolv getOrigenAlquilerId() {
        return origenAlquilerId;
    }

    public void setOrigenAlquilerId(EppContratoAlqPolv origenAlquilerId) {
        this.origenAlquilerId = origenAlquilerId;
    }
    
    @XmlTransient
    public List<EppGteDeposito> getEppGteDepositoList() {
        return eppGteDepositoList;
    }

    public void setEppGteDepositoList(List<EppGteDeposito> eppGteDepositoList) {
        this.eppGteDepositoList = eppGteDepositoList;
    }
    
}
