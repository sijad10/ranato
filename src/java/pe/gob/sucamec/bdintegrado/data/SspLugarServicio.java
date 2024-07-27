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
@Table(name = "SSP_LUGAR_SERVICIO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspLugarServicio.findAll", query = "SELECT s FROM SspLugarServicio s"),
    @NamedQuery(name = "SspLugarServicio.findById", query = "SELECT s FROM SspLugarServicio s WHERE s.id = :id"),
    @NamedQuery(name = "SspLugarServicio.findByDireccion", query = "SELECT s FROM SspLugarServicio s WHERE s.direccion = :direccion"),
    @NamedQuery(name = "SspLugarServicio.findByReferencia", query = "SELECT s FROM SspLugarServicio s WHERE s.referencia = :referencia"),
    @NamedQuery(name = "SspLugarServicio.findByLatitud", query = "SELECT s FROM SspLugarServicio s WHERE s.latitud = :latitud"),
    @NamedQuery(name = "SspLugarServicio.findByLongitud", query = "SELECT s FROM SspLugarServicio s WHERE s.longitud = :longitud"),
    @NamedQuery(name = "SspLugarServicio.findByActivo", query = "SELECT s FROM SspLugarServicio s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspLugarServicio.findByAudLogin", query = "SELECT s FROM SspLugarServicio s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspLugarServicio.findByAudNumIp", query = "SELECT s FROM SspLugarServicio s WHERE s.audNumIp = :audNumIp")})
public class SspLugarServicio implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_LUGAR_SERVICIO")
    @SequenceGenerator(name = "SEQ_SSP_LUGAR_SERVICIO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_LUGAR_SERVICIO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;   
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "DIRECCION")
    private String direccion;
    @Size(max = 200)
    @Column(name = "REFERENCIA")
    private String referencia;
    @Size(max = 30)
    @Column(name = "LATITUD")
    private String latitud;
    @Size(max = 30)
    @Column(name = "LONGITUD")
    private String longitud;
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
    @JoinColumn(name = "CARTERA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspCarteraCliente carteraId;
    @JoinColumn(name = "DIST_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistritoGt distId;

    public SspLugarServicio() {
    }

    public SspLugarServicio(Long id) {
        this.id = id;
    }

    public SspLugarServicio(Long id, String direccion, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.direccion = direccion;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
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

    public SspCarteraCliente getCarteraId() {
        return carteraId;
    }

    public void setCarteraId(SspCarteraCliente carteraId) {
        this.carteraId = carteraId;
    }

    public SbDistritoGt getDistId() {
        return distId;
    }

    public void setDistId(SbDistritoGt distId) {
        this.distId = distId;
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
        if (!(object instanceof SspLugarServicio)) {
            return false;
        }
        SspLugarServicio other = (SspLugarServicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspLugarServicio[ id=" + id + " ]";
    }
    
}
