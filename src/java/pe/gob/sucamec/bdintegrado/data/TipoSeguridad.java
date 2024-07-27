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
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TIPO_SEGURIDAD", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoSeguridad.findAll", query = "SELECT t FROM TipoSeguridad t"),
    @NamedQuery(name = "TipoSeguridad.findById", query = "SELECT t FROM TipoSeguridad t WHERE t.id = :id"),
    @NamedQuery(name = "TipoSeguridad.findByTipoId", query = "SELECT t FROM TipoSeguridad t WHERE t.tipoId.id = :tipoId"),
    @NamedQuery(name = "TipoSeguridad.findByNombre", query = "SELECT t FROM TipoSeguridad t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoSeguridad.findByAbreviatura", query = "SELECT t FROM TipoSeguridad t WHERE t.abreviatura = :abreviatura"),
    @NamedQuery(name = "TipoSeguridad.findByDescripcion", query = "SELECT t FROM TipoSeguridad t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TipoSeguridad.findByCodProg", query = "SELECT t FROM TipoSeguridad t WHERE t.codProg = :codProg"),
    @NamedQuery(name = "TipoSeguridad.findByOrden", query = "SELECT t FROM TipoSeguridad t WHERE t.orden = :orden"),
    @NamedQuery(name = "TipoSeguridad.findByActivo", query = "SELECT t FROM TipoSeguridad t WHERE t.activo = :activo"),
    @NamedQuery(name = "TipoSeguridad.findByAudLogin", query = "SELECT t FROM TipoSeguridad t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TipoSeguridad.findByAudNumIp", query = "SELECT t FROM TipoSeguridad t WHERE t.audNumIp = :audNumIp")})
public class TipoSeguridad implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_SEGURIDAD")
    @SequenceGenerator(name = "SEQ_TIPO_SEGURIDAD", schema = "BDINTEGRADO", sequenceName = "SEQ_TIPO_SEGURIDAD", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "COD_PROG")
    private String codProg;
    @Column(name = "ORDEN")
    private Integer orden;
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
    @ManyToMany(mappedBy = "tipoSeguridadList")
    private List<SspCarteraCliente> sspCarteraClienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEventoId")
    private List<SspCarteraEvento> sspCarteraEventoList;
    @OneToMany(mappedBy = "motivoContratoId")
    private List<SspCarteraCliente> sspCarteraClienteList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<SspCarteraCliente> sspCarteraClienteList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoOpeId")
    private List<SspCarteraCliente> sspCarteraClienteList3;
    @OneToMany(mappedBy = "tipoId")
    private List<TipoSeguridad> tipoSeguridadList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoSeguridad tipoId;

    public TipoSeguridad() {
    }

    public TipoSeguridad(Long id) {
        this.id = id;
    }

    public TipoSeguridad(Long id, String nombre, String codProg, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.codProg = codProg;
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodProg() {
        return codProg;
    }

    public void setCodProg(String codProg) {
        this.codProg = codProg;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
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
    public List<SspCarteraCliente> getSspCarteraClienteList() {
        return sspCarteraClienteList;
    }

    public void setSspCarteraClienteList(List<SspCarteraCliente> sspCarteraClienteList) {
        this.sspCarteraClienteList = sspCarteraClienteList;
    }

    @XmlTransient
    public List<SspCarteraEvento> getSspCarteraEventoList() {
        return sspCarteraEventoList;
    }

    public void setSspCarteraEventoList(List<SspCarteraEvento> sspCarteraEventoList) {
        this.sspCarteraEventoList = sspCarteraEventoList;
    }

    @XmlTransient
    public List<SspCarteraCliente> getSspCarteraClienteList1() {
        return sspCarteraClienteList1;
    }

    public void setSspCarteraClienteList1(List<SspCarteraCliente> sspCarteraClienteList1) {
        this.sspCarteraClienteList1 = sspCarteraClienteList1;
    }

    @XmlTransient
    public List<SspCarteraCliente> getSspCarteraClienteList2() {
        return sspCarteraClienteList2;
    }

    public void setSspCarteraClienteList2(List<SspCarteraCliente> sspCarteraClienteList2) {
        this.sspCarteraClienteList2 = sspCarteraClienteList2;
    }

    @XmlTransient
    public List<SspCarteraCliente> getSspCarteraClienteList3() {
        return sspCarteraClienteList3;
    }

    public void setSspCarteraClienteList3(List<SspCarteraCliente> sspCarteraClienteList3) {
        this.sspCarteraClienteList3 = sspCarteraClienteList3;
    }

    @XmlTransient
    public List<TipoSeguridad> getTipoSeguridadList() {
        return tipoSeguridadList;
    }

    public void setTipoSeguridadList(List<TipoSeguridad> tipoSeguridadList) {
        this.tipoSeguridadList = tipoSeguridadList;
    }

    public TipoSeguridad getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoSeguridad tipoId) {
        this.tipoId = tipoId;
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
        if (!(object instanceof TipoSeguridad)) {
            return false;
        }
        TipoSeguridad other = (TipoSeguridad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.TipoSeguridad[ id=" + id + " ]";
    }
    
}
