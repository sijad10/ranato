/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_ADMUN_TRANSACCION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacAmaAdmunTransaccion.findAll", query = "SELECT g FROM GamacAmaAdmunTransaccion g"),
    @NamedQuery(name = "GamacAmaAdmunTransaccion.findById", query = "SELECT g FROM GamacAmaAdmunTransaccion g WHERE g.id = :id"),
    @NamedQuery(name = "GamacAmaAdmunTransaccion.findByFechatransaccion", query = "SELECT g FROM GamacAmaAdmunTransaccion g WHERE g.fechatransaccion = :fechatransaccion"),
    @NamedQuery(name = "GamacAmaAdmunTransaccion.findByRgAutoClipro", query = "SELECT g FROM GamacAmaAdmunTransaccion g WHERE g.rgAutoClipro = :rgAutoClipro"),
    @NamedQuery(name = "GamacAmaAdmunTransaccion.findByRgAutotransf", query = "SELECT g FROM GamacAmaAdmunTransaccion g WHERE g.rgAutotransf = :rgAutotransf"),
    @NamedQuery(name = "GamacAmaAdmunTransaccion.findByNumdocTransac", query = "SELECT g FROM GamacAmaAdmunTransaccion g WHERE g.numdocTransac = :numdocTransac"),
    @NamedQuery(name = "GamacAmaAdmunTransaccion.findByCierreInventario", query = "SELECT g FROM GamacAmaAdmunTransaccion g WHERE g.cierreInventario = :cierreInventario"),
    @NamedQuery(name = "GamacAmaAdmunTransaccion.findByActivo", query = "SELECT g FROM GamacAmaAdmunTransaccion g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacAmaAdmunTransaccion.findByAudLogin", query = "SELECT g FROM GamacAmaAdmunTransaccion g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacAmaAdmunTransaccion.findByAudNumIp", query = "SELECT g FROM GamacAmaAdmunTransaccion g WHERE g.audNumIp = :audNumIp")})
public class GamacAmaAdmunTransaccion implements Serializable {

    private static final Long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_ADMUN_TRANSACCION")
    @SequenceGenerator(name = "SEQ_AMA_ADMUN_TRANSACCION", schema = "BDINTEGRADO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @JoinColumn(name = "TIPO_TRANSACCION_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoGamac tipoTransaccionId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHATRANSACCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechatransaccion;
    
    @JoinColumn(name = "AGENTECOMER_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbPersona agentecomerId;
    
    @JoinColumn(name = "TIPOCLIENTEPROVEEDOR_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoGamac tipoclienteproveedorId;
    
    @JoinColumn(name = "CLIENTEPROVEEDOR_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbPersona clienteproveedorId;
    
    @JoinColumn(name = "REPRESENTANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbPersona representanteId;
    
    @JoinColumn(name = "LOCALCOMERCIAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacSbDireccion localcomercialId;
    
    @JoinColumn(name = "LICENCIA_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacAmaLicenciaDeUso licenciaId;
    
    @Size(max = 50)
    @Column(name = "RG_AUTO_CLIPRO")
    private String rgAutoClipro;
    
    @Size(max = 50)
    @Column(name = "RG_AUTOTRANSF")
    private String rgAutotransf;
    
    @JoinColumn(name = "TIPODOC_TRANSAC_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacTipoGamac tipodocTransacId;
    
    @Size(max = 20)
    @Column(name = "NUMDOC_TRANSAC")
    private String numdocTransac;
    
    @JoinColumn(name = "DIR_DESTINO_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbDireccion dirDestinoId;
    
    @JoinColumn(name = "DIR_ORIGEN_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacSbDireccion dirOrigenId;
    
    @JoinColumn(name = "ESTADOPRESEN_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacTipoGamac estadopresenId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "CIERRE_INVENTARIO")
    private short cierreInventario;
    
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
    
    @Column(name = "NRO_CONSULTA_RENIEC")
    private String nroConsultaRENIEC;
    
    @Column(name = "FECHA_ANULACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;
    /*@ManyToMany(mappedBy = "gamacAmaAdmunTransaccionCollection")
    private Collection<GamacAmaAdqmunPresenta> gamacAmaAdqmunPresentaCollection;*/    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transaccionId")
    private Collection<GamacAmaAdmunDetalleTrans> gamacAmaAdmunDetalleTransCollection;
    @Size(max = 10)
    @Column(name = "DOCUMENTO_SUSTENTATORIO")
    private String documentoSustentatorio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transaccionId")
    private List<GamacAmaAdmunDetalleTrans> amaAdmunDetalleTransList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transacionDevId", fetch = FetchType.EAGER)
    private List<GamacAmaAdmunTransaccion> amaAdmunTransaccionList;
    @JoinColumn(name = "TRANSACION_DEV_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacAmaAdmunTransaccion transacionDevId;
    
    public GamacAmaAdmunTransaccion() {
    }

    public GamacAmaAdmunTransaccion(Long id) {
        this.id = id;
    }

    public GamacAmaAdmunTransaccion(Long id, Date fechatransaccion, short cierreInventario, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechatransaccion = fechatransaccion;
        this.cierreInventario = cierreInventario;
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

    public Date getFechatransaccion() {
        return fechatransaccion;
    }

    public void setFechatransaccion(Date fechatransaccion) {
        this.fechatransaccion = fechatransaccion;
    }

    public String getRgAutoClipro() {
        return rgAutoClipro;
    }

    public void setRgAutoClipro(String rgAutoClipro) {
        this.rgAutoClipro = rgAutoClipro;
    }

    public String getRgAutotransf() {
        return rgAutotransf;
    }

    public void setRgAutotransf(String rgAutotransf) {
        this.rgAutotransf = rgAutotransf;
    }

    public String getNumdocTransac() {
        return numdocTransac;
    }

    public void setNumdocTransac(String numdocTransac) {
        this.numdocTransac = numdocTransac;
    }

    public short getCierreInventario() {
        return cierreInventario;
    }

    public void setCierreInventario(short cierreInventario) {
        this.cierreInventario = cierreInventario;
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

    /*@XmlTransient
    public Collection<GamacAmaAdqmunPresenta> getGamacAmaAdqmunPresentaCollection() {
        return gamacAmaAdqmunPresentaCollection;
    }

    public void setGamacAmaAdqmunPresentaCollection(Collection<GamacAmaAdqmunPresenta> gamacAmaAdqmunPresentaCollection) {
        this.gamacAmaAdqmunPresentaCollection = gamacAmaAdqmunPresentaCollection;
    }*/

    public GamacTipoGamac getTipodocTransacId() {
        return tipodocTransacId;
    }

    public void setTipodocTransacId(GamacTipoGamac tipodocTransacId) {
        this.tipodocTransacId = tipodocTransacId;
    }

    public GamacTipoGamac getTipoTransaccionId() {
        return tipoTransaccionId;
    }

    public void setTipoTransaccionId(GamacTipoGamac tipoTransaccionId) {
        this.tipoTransaccionId = tipoTransaccionId;
    }

    public GamacTipoGamac getEstadopresenId() {
        return estadopresenId;
    }

    public void setEstadopresenId(GamacTipoGamac estadopresenId) {
        this.estadopresenId = estadopresenId;
    }

    public GamacTipoGamac getTipoclienteproveedorId() {
        return tipoclienteproveedorId;
    }

    public void setTipoclienteproveedorId(GamacTipoGamac tipoclienteproveedorId) {
        this.tipoclienteproveedorId = tipoclienteproveedorId;
    }

    public GamacSbPersona getAgentecomerId() {
        return agentecomerId;
    }

    public void setAgentecomerId(GamacSbPersona agentecomerId) {
        this.agentecomerId = agentecomerId;
    }

    public GamacSbPersona getRepresentanteId() {
        return representanteId;
    }

    public void setRepresentanteId(GamacSbPersona representanteId) {
        this.representanteId = representanteId;
    }

    public GamacSbPersona getClienteproveedorId() {
        return clienteproveedorId;
    }

    public void setClienteproveedorId(GamacSbPersona clienteproveedorId) {
        this.clienteproveedorId = clienteproveedorId;
    }

    public GamacSbDireccion getDirDestinoId() {
        return dirDestinoId;
    }

    public void setDirDestinoId(GamacSbDireccion dirDestinoId) {
        this.dirDestinoId = dirDestinoId;
    }

    public GamacSbDireccion getDirOrigenId() {
        return dirOrigenId;
    }

    public void setDirOrigenId(GamacSbDireccion dirOrigenId) {
        this.dirOrigenId = dirOrigenId;
    }

    public GamacSbDireccion getLocalcomercialId() {
        return localcomercialId;
    }

    public void setLocalcomercialId(GamacSbDireccion localcomercialId) {
        this.localcomercialId = localcomercialId;
    }

    public GamacAmaLicenciaDeUso getLicenciaId() {
        return licenciaId;
    }

    public void setLicenciaId(GamacAmaLicenciaDeUso licenciaId) {
        this.licenciaId = licenciaId;
    }

    public String getNroConsultaRENIEC() {
        return nroConsultaRENIEC;
    }

    public void setNroConsultaRENIEC(String nroConsultaRENIEC) {
        this.nroConsultaRENIEC = nroConsultaRENIEC;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public String getDocumentoSustentatorio() {
        return documentoSustentatorio;
    }

    public void setDocumentoSustentatorio(String documentoSustentatorio) {
        this.documentoSustentatorio = documentoSustentatorio;
    }

    public List<GamacAmaAdmunDetalleTrans> getAmaAdmunDetalleTransList() {
        return amaAdmunDetalleTransList;
    }

    public void setAmaAdmunDetalleTransList(List<GamacAmaAdmunDetalleTrans> amaAdmunDetalleTransList) {
        this.amaAdmunDetalleTransList = amaAdmunDetalleTransList;
    }

    public List<GamacAmaAdmunTransaccion> getAmaAdmunTransaccionList() {
        return amaAdmunTransaccionList;
    }

    public void setAmaAdmunTransaccionList(List<GamacAmaAdmunTransaccion> amaAdmunTransaccionList) {
        this.amaAdmunTransaccionList = amaAdmunTransaccionList;
    }

    public GamacAmaAdmunTransaccion getTransacionDevId() {
        return transacionDevId;
    }

    public void setTransacionDevId(GamacAmaAdmunTransaccion transacionDevId) {
        this.transacionDevId = transacionDevId;
    }

    @XmlTransient
    public Collection<GamacAmaAdmunDetalleTrans> getGamacAmaAdmunDetalleTransCollection() {
        return gamacAmaAdmunDetalleTransCollection;
    }

    public void setGamacAmaAdmunDetalleTransCollection(Collection<GamacAmaAdmunDetalleTrans> gamacAmaAdmunDetalleTransCollection) {
        this.gamacAmaAdmunDetalleTransCollection = gamacAmaAdmunDetalleTransCollection;
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
        if (!(object instanceof GamacAmaAdmunTransaccion)) {
            return false;
        }
        GamacAmaAdmunTransaccion other = (GamacAmaAdmunTransaccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacAmaAdmunTransaccion[ id=" + id + " ]";
    }
    
}
