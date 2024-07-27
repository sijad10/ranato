/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.epp.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;


/**
 *
 * @author rmoscoso
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_LUGAR_USO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LugarUso.findAll", query = "SELECT l FROM LugarUso l"),
    @NamedQuery(name = "LugarUso.findById", query = "SELECT l FROM LugarUso l WHERE l.id = :id"),
    @NamedQuery(name = "LugarUso.findByNombre", query = "SELECT l FROM LugarUso l WHERE l.nombre = :nombre"),
    @NamedQuery(name = "LugarUso.findByActivo", query = "SELECT l FROM LugarUso l WHERE l.activo = :activo"),
    @NamedQuery(name = "LugarUso.findByOtraUbicacion", query = "SELECT l FROM LugarUso l WHERE l.otraUbicacion = :otraUbicacion"),
    @NamedQuery(name = "LugarUso.findByLatitud", query = "SELECT l FROM LugarUso l WHERE l.latitud = :latitud"),
    @NamedQuery(name = "LugarUso.findByLongitud", query = "SELECT l FROM LugarUso l WHERE l.longitud = :longitud"),
    @NamedQuery(name = "LugarUso.findByAudLogin", query = "SELECT l FROM LugarUso l WHERE l.audLogin = :audLogin"),
    @NamedQuery(name = "LugarUso.findByAudNumIp", query = "SELECT l FROM LugarUso l WHERE l.audNumIp = :audNumIp")})
public class LugarUso implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_LUGAR_USO")
    @SequenceGenerator(name = "SEQ_EPP_LUGAR_USO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_LUGAR_USO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 500)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Size(max = 50)
    @Column(name = "OTRA_UBICACION")
    private String otraUbicacion;
    @Size(max = 30)
    @Column(name = "LATITUD")
    private String latitud;
    @Size(max = 30)
    @Column(name = "LONGITUD")
    private String longitud;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @ManyToMany(mappedBy = "lugarUsoList")
    private List<Com> comList;
    @OneToMany(mappedBy = "lugarUsoDestino")
    private List<RegistroGuiaTransito> registroGuiaTransitoList;
    @OneToMany(mappedBy = "lugarUsoOrigen")
    private List<RegistroGuiaTransito> registroGuiaTransitoList1;
    @JoinColumn(name = "TIPO_LUGAR_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivo tipoLugarId;
    @JoinColumn(name = "TIPO_UBICACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivo tipoUbicacionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lugarUsoId")
    private List<LugarUsoUbigeo> lugarUsoUbigeoList;

    public LugarUso() {
    }

    public LugarUso(Long id) {
        this.id = id;
    }

    public LugarUso(Long id, short activo, String audLogin, String audNumIp) {
        this.id = id;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public String getOtraUbicacion() {
        return otraUbicacion;
    }

    public void setOtraUbicacion(String otraUbicacion) {
        this.otraUbicacion = otraUbicacion;
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
    public List<Com> getComList() {
        return comList;
    }

    public void setComList(List<Com> comList) {
        this.comList = comList;
    }

    @XmlTransient
    public List<RegistroGuiaTransito> getRegistroGuiaTransitoList() {
        return registroGuiaTransitoList;
    }

    public void setRegistroGuiaTransitoList(List<RegistroGuiaTransito> registroGuiaTransitoList) {
        this.registroGuiaTransitoList = registroGuiaTransitoList;
    }

    @XmlTransient
    public List<RegistroGuiaTransito> getRegistroGuiaTransitoList1() {
        return registroGuiaTransitoList1;
    }

    public void setRegistroGuiaTransitoList1(List<RegistroGuiaTransito> registroGuiaTransitoList1) {
        this.registroGuiaTransitoList1 = registroGuiaTransitoList1;
    }

    public TipoExplosivo getTipoLugarId() {
        return tipoLugarId;
    }

    public void setTipoLugarId(TipoExplosivo tipoLugarId) {
        this.tipoLugarId = tipoLugarId;
    }

    public TipoExplosivo getTipoUbicacionId() {
        return tipoUbicacionId;
    }

    public void setTipoUbicacionId(TipoExplosivo tipoUbicacionId) {
        this.tipoUbicacionId = tipoUbicacionId;
    }

    @XmlTransient
    public List<LugarUsoUbigeo> getLugarUsoUbigeoList() {
        return lugarUsoUbigeoList;
    }

    public void setLugarUsoUbigeoList(List<LugarUsoUbigeo> lugarUsoUbigeoList) {
        this.lugarUsoUbigeoList = lugarUsoUbigeoList;
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
        if (!(object instanceof LugarUso)) {
            return false;
        }
        LugarUso other = (LugarUso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.epp.data.LugarUso[ id=" + id + " ]";
    }
    
}
