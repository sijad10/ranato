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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_INVENTARIO_ACCESORIOS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaInventarioAccesorios.findAll", query = "SELECT a FROM AmaInventarioAccesorios a"),
    @NamedQuery(name = "AmaInventarioAccesorios.findById", query = "SELECT a FROM AmaInventarioAccesorios a WHERE a.id = :id"),
    @NamedQuery(name = "AmaInventarioAccesorios.findByCantidad", query = "SELECT a FROM AmaInventarioAccesorios a WHERE a.cantidad = :cantidad"),
    @NamedQuery(name = "AmaInventarioAccesorios.findByActivo", query = "SELECT a FROM AmaInventarioAccesorios a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaInventarioAccesorios.findByAudLogin", query = "SELECT a FROM AmaInventarioAccesorios a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaInventarioAccesorios.findByAudNumIp", query = "SELECT a FROM AmaInventarioAccesorios a WHERE a.audNumIp = :audNumIp")})
public class AmaInventarioAccesorios implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_INVENTARIO_ACCESORIOS")
    @SequenceGenerator(name = "SEQ_AMA_INVENTARIO_ACCESORIOS", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_INVENTARIO_ACCESORIOS", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private Long cantidad;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTADO_DEPOSITO")
    private short estadoDeposito;
    @JoinColumn(name = "ARTICULO_CONEXO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaInventarioArtconexo articuloConexoId;
    @JoinColumn(name = "INVENTARIO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaInventarioArma inventarioArmaId;
    @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaGuiaTransito guiaTransitoId;

    public AmaInventarioAccesorios() {
    }

    public AmaInventarioAccesorios(Long id) {
        this.id = id;
    }

    public AmaInventarioAccesorios(Long id, Long cantidad, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.cantidad = cantidad;
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

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
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

    public AmaInventarioArtconexo getArticuloConexoId() {
        return articuloConexoId;
    }

    public void setArticuloConexoId(AmaInventarioArtconexo articuloConexoId) {
        this.articuloConexoId = articuloConexoId;
    }

    public AmaInventarioArma getInventarioArmaId() {
        return inventarioArmaId;
    }

    public void setInventarioArmaId(AmaInventarioArma inventarioArmaId) {
        this.inventarioArmaId = inventarioArmaId;
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
        if (!(object instanceof AmaInventarioAccesorios)) {
            return false;
        }
        AmaInventarioAccesorios other = (AmaInventarioAccesorios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaInventarioAccesorios[ id=" + id + " ]";
    }

    /**
     * @return the estadoDeposito
     */
    public short getEstadoDeposito() {
        return estadoDeposito;
    }

    /**
     * @param estadoDeposito the estadoDeposito to set
     */
    public void setEstadoDeposito(short estadoDeposito) {
        this.estadoDeposito = estadoDeposito;
    }

    public AmaGuiaTransito getGuiaTransitoId() {
        return guiaTransitoId;
    }

    public void setGuiaTransitoId(AmaGuiaTransito guiaTransitoId) {
        this.guiaTransitoId = guiaTransitoId;
    }
    
}
