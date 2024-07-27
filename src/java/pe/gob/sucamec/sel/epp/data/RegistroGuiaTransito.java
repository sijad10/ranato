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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "EPP_REGISTRO_GUIA_TRANSITO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegistroGuiaTransito.findAll", query = "SELECT r FROM RegistroGuiaTransito r"),
    @NamedQuery(name = "RegistroGuiaTransito.findById", query = "SELECT r FROM RegistroGuiaTransito r WHERE r.id = :id"),
    @NamedQuery(name = "RegistroGuiaTransito.findByFechaSalida", query = "SELECT r FROM RegistroGuiaTransito r WHERE r.fechaSalida = :fechaSalida"),
    @NamedQuery(name = "RegistroGuiaTransito.findByFechaLlegada", query = "SELECT r FROM RegistroGuiaTransito r WHERE r.fechaLlegada = :fechaLlegada"),
    @NamedQuery(name = "RegistroGuiaTransito.findByDescOtroTiptram", query = "SELECT r FROM RegistroGuiaTransito r WHERE r.descOtroTiptram = :descOtroTiptram"),
    @NamedQuery(name = "RegistroGuiaTransito.findByNroFactura", query = "SELECT r FROM RegistroGuiaTransito r WHERE r.nroFactura = :nroFactura"),
    @NamedQuery(name = "RegistroGuiaTransito.findByFechaFactura", query = "SELECT r FROM RegistroGuiaTransito r WHERE r.fechaFactura = :fechaFactura"),
    @NamedQuery(name = "RegistroGuiaTransito.findByInformacionAdi", query = "SELECT r FROM RegistroGuiaTransito r WHERE r.informacionAdi = :informacionAdi"),
    @NamedQuery(name = "RegistroGuiaTransito.findByCustodia", query = "SELECT r FROM RegistroGuiaTransito r WHERE r.custodia = :custodia"),
    @NamedQuery(name = "RegistroGuiaTransito.findByActivo", query = "SELECT r FROM RegistroGuiaTransito r WHERE r.activo = :activo"),
    @NamedQuery(name = "RegistroGuiaTransito.findByAudLogin", query = "SELECT r FROM RegistroGuiaTransito r WHERE r.audLogin = :audLogin"),
    @NamedQuery(name = "RegistroGuiaTransito.findByAudNumIp", query = "SELECT r FROM RegistroGuiaTransito r WHERE r.audNumIp = :audNumIp")})
public class RegistroGuiaTransito implements Serializable {

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
    @JoinTable(schema="BDINTEGRADO", name = "EPP_GUIA_TRANSITO_CONDUCTOR_DETA", joinColumns = {
        @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CONDUCTOR_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Licencia> licenciaList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_GUIA_TRANSITO_CUSTODIO_DETALLE", joinColumns = {
        @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CUSTODIO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Licencia> licenciaList1;
    @JoinColumn(name = "LUGAR_USO_DESTINO", referencedColumnName = "ID")
    @ManyToOne
    private LugarUso lugarUsoDestino;
    @JoinColumn(name = "DESTINO_ALMACEN", referencedColumnName = "ID")
    @ManyToOne
    private Almacen destinoAlmacen;
    @JoinColumn(name = "PUERTO_ADUANERO_ORIGEN", referencedColumnName = "ID")
    @ManyToOne
    private PuertoAduanero puertoAduaneroOrigen;
    @JoinColumn(name = "TIPO_MODIFICATORIA", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivo tipoModificatoria;
    @JoinColumn(name = "ORIGEN_ALMACEN", referencedColumnName = "ID")
    @ManyToOne
    private Almacen origenAlmacen;
    @JoinColumn(name = "RESOLUCION_ID", referencedColumnName = "ID")
    @ManyToOne
    private Resolucion resolucionId;
    @JoinColumn(name = "DESTINO_ALMACEN_ADUANA", referencedColumnName = "ID")
    @ManyToOne
    private AlmacenAduanero destinoAlmacenAduana;
    @JoinColumn(name = "ORIGEN_ALMACEN_ADUANA", referencedColumnName = "ID")
    @ManyToOne
    private AlmacenAduanero origenAlmacenAduana;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private Registro registroId;
    @JoinColumn(name = "TIPO_PROCESO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivo tipoProceso;
    @JoinColumn(name = "LUGAR_USO_ORIGEN", referencedColumnName = "ID")
    @ManyToOne
    private LugarUso lugarUsoOrigen;
    @JoinColumn(name = "LUGAR_USO_UBIGEO_DESTINO", referencedColumnName = "ID")
    @ManyToOne
    private LugarUsoUbigeo lugarUsoUbigeoDestino;
    @JoinColumn(name = "DESTINO_POLVORIN", referencedColumnName = "ID")
    @ManyToOne
    private Polvorin destinoPolvorin;
    @JoinColumn(name = "PUERTO_ADUANERO_DESTINO", referencedColumnName = "ID")
    @ManyToOne
    private PuertoAduanero puertoAduaneroDestino;
    @JoinColumn(name = "LUGAR_USO_UBIGEO_ORIGEN", referencedColumnName = "ID")
    @ManyToOne
    private LugarUsoUbigeo lugarUsoUbigeoOrigen;
    @JoinColumn(name = "TIPO_ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivo tipoEstado;
    @JoinColumn(name = "TIPO_TRANSPORTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivo tipoTransporte;
    @JoinColumn(name = "EMPRESA_TRANSP_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona empresaTranspId;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona empresaId;
    @JoinColumn(name = "TIPO_TRAMITE", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivo tipoTramite;
    @JoinColumn(name = "ORIGEN_POLVORIN", referencedColumnName = "ID")
    @ManyToOne
    private Polvorin origenPolvorin;

    public RegistroGuiaTransito() {
    }

    public RegistroGuiaTransito(Long id) {
        this.id = id;
    }

    public RegistroGuiaTransito(Long id, short custodia, short activo, String audLogin, String audNumIp) {
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
    public List<Licencia> getLicenciaList() {
        return licenciaList;
    }

    public void setLicenciaList(List<Licencia> licenciaList) {
        this.licenciaList = licenciaList;
    }

    @XmlTransient
    public List<Licencia> getLicenciaList1() {
        return licenciaList1;
    }

    public void setLicenciaList1(List<Licencia> licenciaList1) {
        this.licenciaList1 = licenciaList1;
    }

    public LugarUso getLugarUsoDestino() {
        return lugarUsoDestino;
    }

    public void setLugarUsoDestino(LugarUso lugarUsoDestino) {
        this.lugarUsoDestino = lugarUsoDestino;
    }

    public Almacen getDestinoAlmacen() {
        return destinoAlmacen;
    }

    public void setDestinoAlmacen(Almacen destinoAlmacen) {
        this.destinoAlmacen = destinoAlmacen;
    }

    public PuertoAduanero getPuertoAduaneroOrigen() {
        return puertoAduaneroOrigen;
    }

    public void setPuertoAduaneroOrigen(PuertoAduanero puertoAduaneroOrigen) {
        this.puertoAduaneroOrigen = puertoAduaneroOrigen;
    }

    public TipoExplosivo getTipoModificatoria() {
        return tipoModificatoria;
    }

    public void setTipoModificatoria(TipoExplosivo tipoModificatoria) {
        this.tipoModificatoria = tipoModificatoria;
    }

    public Almacen getOrigenAlmacen() {
        return origenAlmacen;
    }

    public void setOrigenAlmacen(Almacen origenAlmacen) {
        this.origenAlmacen = origenAlmacen;
    }

    public Resolucion getResolucionId() {
        return resolucionId;
    }

    public void setResolucionId(Resolucion resolucionId) {
        this.resolucionId = resolucionId;
    }

    public AlmacenAduanero getDestinoAlmacenAduana() {
        return destinoAlmacenAduana;
    }

    public void setDestinoAlmacenAduana(AlmacenAduanero destinoAlmacenAduana) {
        this.destinoAlmacenAduana = destinoAlmacenAduana;
    }

    public AlmacenAduanero getOrigenAlmacenAduana() {
        return origenAlmacenAduana;
    }

    public void setOrigenAlmacenAduana(AlmacenAduanero origenAlmacenAduana) {
        this.origenAlmacenAduana = origenAlmacenAduana;
    }

    public Registro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Registro registroId) {
        this.registroId = registroId;
    }

    public TipoExplosivo getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(TipoExplosivo tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public LugarUso getLugarUsoOrigen() {
        return lugarUsoOrigen;
    }

    public void setLugarUsoOrigen(LugarUso lugarUsoOrigen) {
        this.lugarUsoOrigen = lugarUsoOrigen;
    }

    public LugarUsoUbigeo getLugarUsoUbigeoDestino() {
        return lugarUsoUbigeoDestino;
    }

    public void setLugarUsoUbigeoDestino(LugarUsoUbigeo lugarUsoUbigeoDestino) {
        this.lugarUsoUbigeoDestino = lugarUsoUbigeoDestino;
    }

    public Polvorin getDestinoPolvorin() {
        return destinoPolvorin;
    }

    public void setDestinoPolvorin(Polvorin destinoPolvorin) {
        this.destinoPolvorin = destinoPolvorin;
    }

    public PuertoAduanero getPuertoAduaneroDestino() {
        return puertoAduaneroDestino;
    }

    public void setPuertoAduaneroDestino(PuertoAduanero puertoAduaneroDestino) {
        this.puertoAduaneroDestino = puertoAduaneroDestino;
    }

    public LugarUsoUbigeo getLugarUsoUbigeoOrigen() {
        return lugarUsoUbigeoOrigen;
    }

    public void setLugarUsoUbigeoOrigen(LugarUsoUbigeo lugarUsoUbigeoOrigen) {
        this.lugarUsoUbigeoOrigen = lugarUsoUbigeoOrigen;
    }

    public TipoExplosivo getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(TipoExplosivo tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public TipoExplosivo getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(TipoExplosivo tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public SbPersona getEmpresaTranspId() {
        return empresaTranspId;
    }

    public void setEmpresaTranspId(SbPersona empresaTranspId) {
        this.empresaTranspId = empresaTranspId;
    }

    public SbPersona getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersona empresaId) {
        this.empresaId = empresaId;
    }

    public TipoExplosivo getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoExplosivo tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public Polvorin getOrigenPolvorin() {
        return origenPolvorin;
    }

    public void setOrigenPolvorin(Polvorin origenPolvorin) {
        this.origenPolvorin = origenPolvorin;
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
        if (!(object instanceof RegistroGuiaTransito)) {
            return false;
        }
        RegistroGuiaTransito other = (RegistroGuiaTransito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.epp.data.RegistroGuiaTransito[ id=" + id + " ]";
    }
    
}
