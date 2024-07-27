/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.data;

import java.io.Serializable;
import java.util.List;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rarevalo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_ADMUN_DETALLE_TRANS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GamacAmaAdmunDetalleTrans.findAll", query = "SELECT g FROM GamacAmaAdmunDetalleTrans g"),
    @NamedQuery(name = "GamacAmaAdmunDetalleTrans.findById", query = "SELECT g FROM GamacAmaAdmunDetalleTrans g WHERE g.id = :id"),
    @NamedQuery(name = "GamacAmaAdmunDetalleTrans.findByCantidadEmpaques", query = "SELECT g FROM GamacAmaAdmunDetalleTrans g WHERE g.cantidadEmpaques = :cantidadEmpaques"),
    @NamedQuery(name = "GamacAmaAdmunDetalleTrans.findByMuniPorEmpaque", query = "SELECT g FROM GamacAmaAdmunDetalleTrans g WHERE g.muniPorEmpaque = :muniPorEmpaque"),
    @NamedQuery(name = "GamacAmaAdmunDetalleTrans.findByActivo", query = "SELECT g FROM GamacAmaAdmunDetalleTrans g WHERE g.activo = :activo"),
    @NamedQuery(name = "GamacAmaAdmunDetalleTrans.findByAudLogin", query = "SELECT g FROM GamacAmaAdmunDetalleTrans g WHERE g.audLogin = :audLogin"),
    @NamedQuery(name = "GamacAmaAdmunDetalleTrans.findByAudNumIp", query = "SELECT g FROM GamacAmaAdmunDetalleTrans g WHERE g.audNumIp = :audNumIp")})
public class GamacAmaAdmunDetalleTrans implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_ADMUN_DETALLE_TRANS")
    @SequenceGenerator(name = "SEQ_AMA_ADMUN_DETALLE_TRANS", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_ADMUN_DETALLE_TRANS", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @JoinColumn(name = "TRANSACCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaAdmunTransaccion transaccionId;
    
    @JoinColumn(name = "ARTICULO_MUNICION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GamacAmaMunicion articuloMunicionId;
    
    @JoinColumn(name = "TARJETA_PROPIEDAD_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacAmaTarjetaPropiedad tarjetaPropiedadId;
    
    //@Basic(optional = false)
    //@NotNull
    @Column(name = "CANTIDAD_EMPAQUES")
    private int cantidadEmpaques;
    
    //@Basic(optional = false)
    //@NotNull
    @Column(name = "MUNI_POR_EMPAQUE")
    private int muniPorEmpaque;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD_MUNICIONES")
    private int cantidadMuniciones;
    
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
    
    @Size(max = 15)
    @Column(name = "CODIGO_FABRICANTE")
    private String codigoFabricante;
    @Size(max = 14)
    @Column(name = "LOTE_FABRICACION")
    private String loteFabricacion;
    @Size(max = 25)
    @Column(name = "NRO_RUA")
    private String nroRua;
    @OneToMany(mappedBy = "devueltoId")
    private List<GamacAmaAdmunDetalleTrans> amaAdmunDetalleTransList;
    @JoinColumn(name = "DEVUELTO_ID", referencedColumnName = "ID")
    @ManyToOne
    private GamacAmaAdmunDetalleTrans devueltoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "amaAdmunDetalleId")
    private List<GamacAmaAdmunDevolucion> amaAdmunDevolucionList;
    
    public GamacAmaAdmunDetalleTrans() {
    }

    public GamacAmaAdmunDetalleTrans(Long id) {
        this.id = id;
    }

    public GamacAmaAdmunDetalleTrans(Long id, int cantidadEmpaques, int muniPorEmpaque, int cantidadMuniciones, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.cantidadEmpaques = cantidadEmpaques;
        this.muniPorEmpaque = muniPorEmpaque;
        this.cantidadMuniciones = cantidadMuniciones;
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
    
    public int getCantidadEmpaques() {
        return cantidadEmpaques;
    }

    public void setCantidadEmpaques(int cantidadEmpaques) {
        this.cantidadEmpaques = cantidadEmpaques;
    }

    public int getMuniPorEmpaque() {
        return muniPorEmpaque;
    }

    public void setMuniPorEmpaque(int muniPorEmpaque) {
        this.muniPorEmpaque = muniPorEmpaque;
    }

    public int getCantidadMuniciones() {
        return cantidadMuniciones;
    }

    public void setCantidadMuniciones(int cantidadMuniciones) {
        this.cantidadMuniciones = cantidadMuniciones;
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

    public GamacAmaTarjetaPropiedad getTarjetaPropiedadId() {
        return tarjetaPropiedadId;
    }

    public void setTarjetaPropiedadId(GamacAmaTarjetaPropiedad tarjetaPropiedadId) {
        this.tarjetaPropiedadId = tarjetaPropiedadId;
    }

    public GamacAmaMunicion getArticuloMunicionId() {
        return articuloMunicionId;
    }

    public void setArticuloMunicionId(GamacAmaMunicion articuloMunicionId) {
        this.articuloMunicionId = articuloMunicionId;
    }

    public GamacAmaAdmunTransaccion getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(GamacAmaAdmunTransaccion transaccionId) {
        this.transaccionId = transaccionId;
    }

    public String getCodigoFabricante() {
        return codigoFabricante;
    }

    public void setCodigoFabricante(String codigoFabricante) {
        this.codigoFabricante = codigoFabricante;
    }

    public String getLoteFabricacion() {
        return loteFabricacion;
    }

    public void setLoteFabricacion(String loteFabricacion) {
        this.loteFabricacion = loteFabricacion;
    }

    public String getNroRua() {
        return nroRua;
    }

    public void setNroRua(String nroRua) {
        this.nroRua = nroRua;
    }

    public List<GamacAmaAdmunDetalleTrans> getAmaAdmunDetalleTransList() {
        return amaAdmunDetalleTransList;
    }

    public void setAmaAdmunDetalleTransList(List<GamacAmaAdmunDetalleTrans> amaAdmunDetalleTransList) {
        this.amaAdmunDetalleTransList = amaAdmunDetalleTransList;
    }

    public GamacAmaAdmunDetalleTrans getDevueltoId() {
        return devueltoId;
    }

    public void setDevueltoId(GamacAmaAdmunDetalleTrans devueltoId) {
        this.devueltoId = devueltoId;
    }

    public List<GamacAmaAdmunDevolucion> getAmaAdmunDevolucionList() {
        return amaAdmunDevolucionList;
    }

    public void setAmaAdmunDevolucionList(List<GamacAmaAdmunDevolucion> amaAdmunDevolucionList) {
        this.amaAdmunDevolucionList = amaAdmunDevolucionList;
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
        if (!(object instanceof GamacAmaAdmunDetalleTrans)) {
            return false;
        }
        GamacAmaAdmunDetalleTrans other = (GamacAmaAdmunDetalleTrans) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.gamac.data.GamacAmaAdmunDetalleTrans[ id=" + id + " ]";
    }
    
}
