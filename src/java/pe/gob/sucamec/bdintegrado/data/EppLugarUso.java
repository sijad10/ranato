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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "EPP_LUGAR_USO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppLugarUso.findAll", query = "SELECT e FROM EppLugarUso e"),
    @NamedQuery(name = "EppLugarUso.findById", query = "SELECT e FROM EppLugarUso e WHERE e.id = :id"),
    @NamedQuery(name = "EppLugarUso.findByNombre", query = "SELECT e FROM EppLugarUso e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EppLugarUso.findByOtraUbicacion", query = "SELECT e FROM EppLugarUso e WHERE e.otraUbicacion = :otraUbicacion"),
    @NamedQuery(name = "EppLugarUso.findByLatitud", query = "SELECT e FROM EppLugarUso e WHERE e.latitud = :latitud"),
    @NamedQuery(name = "EppLugarUso.findByLongitud", query = "SELECT e FROM EppLugarUso e WHERE e.longitud = :longitud"),
    @NamedQuery(name = "EppLugarUso.findByActivo", query = "SELECT e FROM EppLugarUso e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppLugarUso.findByAudLogin", query = "SELECT e FROM EppLugarUso e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppLugarUso.findByAudNumIp", query = "SELECT e FROM EppLugarUso e WHERE e.audNumIp = :audNumIp")})
public class EppLugarUso implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_LUGAR_USO")
    @SequenceGenerator(name = "SEQ_EPP_LUGAR_USO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_LUGAR_USO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 250)
    @Column(name = "NOMBRE")
    private String nombre;
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
    @ManyToMany(mappedBy = "eppLugarUsoList")
    private List<EppRegistro> eppRegistroList;
    @ManyToMany(mappedBy = "eppLugarUsoList")
    private List<EppCom> eppComList;
    @OneToMany(mappedBy = "lugarUsoDestino")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList;
    @OneToMany(mappedBy = "lugarUsoOrigen")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList1;
    @JoinColumn(name = "TIPO_UBICACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoUbicacionId;
    @JoinColumn(name = "TIPO_LUGAR_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoLugarId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lugarUsoId")
    private List<EppLugarUsoUbigeo> eppLugarUsoUbigeoList;
    @OneToMany(mappedBy = "lugarUsoId")
    private List<EppLibroDetalle> eppLibroDetalleList;

    public EppLugarUso() {
    }

    public EppLugarUso(Long id) {
        this.id = id;
    }

    public EppLugarUso(Long id, short activo, String audLogin, String audNumIp) {
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
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    @XmlTransient
    public List<EppCom> getEppComList() {
        return eppComList;
    }

    public void setEppComList(List<EppCom> eppComList) {
        this.eppComList = eppComList;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList() {
        return eppRegistroGuiaTransitoList;
    }

    public void setEppRegistroGuiaTransitoList(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList) {
        this.eppRegistroGuiaTransitoList = eppRegistroGuiaTransitoList;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList1() {
        return eppRegistroGuiaTransitoList1;
    }

    public void setEppRegistroGuiaTransitoList1(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList1) {
        this.eppRegistroGuiaTransitoList1 = eppRegistroGuiaTransitoList1;
    }

    public TipoExplosivoGt getTipoUbicacionId() {
        return tipoUbicacionId;
    }

    public void setTipoUbicacionId(TipoExplosivoGt tipoUbicacionId) {
        this.tipoUbicacionId = tipoUbicacionId;
    }

    public TipoExplosivoGt getTipoLugarId() {
        return tipoLugarId;
    }

    public void setTipoLugarId(TipoExplosivoGt tipoLugarId) {
        this.tipoLugarId = tipoLugarId;
    }

    @XmlTransient
    public List<EppLugarUsoUbigeo> getEppLugarUsoUbigeoList() {
        return eppLugarUsoUbigeoList;
    }

    public void setEppLugarUsoUbigeoList(List<EppLugarUsoUbigeo> eppLugarUsoUbigeoList) {
        this.eppLugarUsoUbigeoList = eppLugarUsoUbigeoList;
    }

    @XmlTransient
    public List<EppLibroDetalle> getEppLibroDetalleList() {
        return eppLibroDetalleList;
    }

    public void setEppLibroDetalleList(List<EppLibroDetalle> eppLibroDetalleList) {
        this.eppLibroDetalleList = eppLibroDetalleList;
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
        if (!(object instanceof EppLugarUso)) {
            return false;
        }
        EppLugarUso other = (EppLugarUso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppLugarUso[ id=" + id + " ]";
    }
    
}
