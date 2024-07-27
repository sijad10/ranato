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
@Table(name = "TIPO_EXPLOSIVO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoExplosivo.findAll", query = "SELECT t FROM TipoExplosivo t"),
    @NamedQuery(name = "TipoExplosivo.findById", query = "SELECT t FROM TipoExplosivo t WHERE t.id = :id"),
    @NamedQuery(name = "TipoExplosivo.findByNombre", query = "SELECT t FROM TipoExplosivo t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoExplosivo.findByAbreviatura", query = "SELECT t FROM TipoExplosivo t WHERE t.abreviatura = :abreviatura"),
    @NamedQuery(name = "TipoExplosivo.findByDescripcion", query = "SELECT t FROM TipoExplosivo t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TipoExplosivo.findByCodProg", query = "SELECT t FROM TipoExplosivo t WHERE t.codProg = :codProg"),
    @NamedQuery(name = "TipoExplosivo.findByOrden", query = "SELECT t FROM TipoExplosivo t WHERE t.orden = :orden"),
    @NamedQuery(name = "TipoExplosivo.findByActivo", query = "SELECT t FROM TipoExplosivo t WHERE t.activo = :activo"),
    @NamedQuery(name = "TipoExplosivo.findByAudLogin", query = "SELECT t FROM TipoExplosivo t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TipoExplosivo.findByAudNumIp", query = "SELECT t FROM TipoExplosivo t WHERE t.audNumIp = :audNumIp")})
public class TipoExplosivo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_EXPLOSIVO")
    @SequenceGenerator(name = "SEQ_TIPO_EXPLOSIVO", schema = "BDINTEGRADO", sequenceName = "SEQ_TIPO_EXPLOSIVO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "COD_PROG")
    private String codProg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDEN")
    private short orden;
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
    @OneToMany(mappedBy = "entidadEmisora")
    private List<Com> comList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoCom")
    private List<Com> comList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstado2do")
    private List<Com> comList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstado1er")
    private List<Com> comList3;
    @OneToMany(mappedBy = "tipoModificatoria")
    private List<RegistroGuiaTransito> registroGuiaTransitoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProceso")
    private List<RegistroGuiaTransito> registroGuiaTransitoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstado")
    private List<RegistroGuiaTransito> registroGuiaTransitoList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoTransporte")
    private List<RegistroGuiaTransito> registroGuiaTransitoList3;
    @OneToMany(mappedBy = "tipoTramite")
    private List<RegistroGuiaTransito> registroGuiaTransitoList4;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEmisionId")
    private List<Licencia> licenciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoLicenciaId")
    private List<Licencia> licenciaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoCargoId")
    private List<Licencia> licenciaList2;
    @OneToMany(mappedBy = "tipoCancelacion")
    private List<Polvorin> polvorinList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPropietarioPolvorinId")
    private List<Polvorin> polvorinList1;
    @OneToMany(mappedBy = "tipoModificatoria")
    private List<Polvorin> polvorinList2;
    @OneToMany(mappedBy = "tipoLugarId")
    private List<LugarUso> lugarUsoList;
    @OneToMany(mappedBy = "tipoUbicacionId")
    private List<LugarUso> lugarUsoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<Almacen> almacenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private List<Registro> registroList;
    @OneToMany(mappedBy = "tipoId")
    private List<TipoExplosivo> tipoExplosivoList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivo tipoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<Resolucion> resolucionList;

    public TipoExplosivo() {
    }

    public TipoExplosivo(Long id) {
        this.id = id;
    }

    public TipoExplosivo(Long id, String nombre, String abreviatura, String descripcion, String codProg, short orden, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.descripcion = descripcion;
        this.codProg = codProg;
        this.orden = orden;
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

    public short getOrden() {
        return orden;
    }

    public void setOrden(short orden) {
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
    public List<Com> getComList() {
        return comList;
    }

    public void setComList(List<Com> comList) {
        this.comList = comList;
    }

    @XmlTransient
    public List<Com> getComList1() {
        return comList1;
    }

    public void setComList1(List<Com> comList1) {
        this.comList1 = comList1;
    }

    @XmlTransient
    public List<Com> getComList2() {
        return comList2;
    }

    public void setComList2(List<Com> comList2) {
        this.comList2 = comList2;
    }

    @XmlTransient
    public List<Com> getComList3() {
        return comList3;
    }

    public void setComList3(List<Com> comList3) {
        this.comList3 = comList3;
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

    @XmlTransient
    public List<RegistroGuiaTransito> getRegistroGuiaTransitoList2() {
        return registroGuiaTransitoList2;
    }

    public void setRegistroGuiaTransitoList2(List<RegistroGuiaTransito> registroGuiaTransitoList2) {
        this.registroGuiaTransitoList2 = registroGuiaTransitoList2;
    }

    @XmlTransient
    public List<RegistroGuiaTransito> getRegistroGuiaTransitoList3() {
        return registroGuiaTransitoList3;
    }

    public void setRegistroGuiaTransitoList3(List<RegistroGuiaTransito> registroGuiaTransitoList3) {
        this.registroGuiaTransitoList3 = registroGuiaTransitoList3;
    }

    @XmlTransient
    public List<RegistroGuiaTransito> getRegistroGuiaTransitoList4() {
        return registroGuiaTransitoList4;
    }

    public void setRegistroGuiaTransitoList4(List<RegistroGuiaTransito> registroGuiaTransitoList4) {
        this.registroGuiaTransitoList4 = registroGuiaTransitoList4;
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

    @XmlTransient
    public List<Licencia> getLicenciaList2() {
        return licenciaList2;
    }

    public void setLicenciaList2(List<Licencia> licenciaList2) {
        this.licenciaList2 = licenciaList2;
    }

    @XmlTransient
    public List<Polvorin> getPolvorinList() {
        return polvorinList;
    }

    public void setPolvorinList(List<Polvorin> polvorinList) {
        this.polvorinList = polvorinList;
    }

    @XmlTransient
    public List<Polvorin> getPolvorinList1() {
        return polvorinList1;
    }

    public void setPolvorinList1(List<Polvorin> polvorinList1) {
        this.polvorinList1 = polvorinList1;
    }

    @XmlTransient
    public List<Polvorin> getPolvorinList2() {
        return polvorinList2;
    }

    public void setPolvorinList2(List<Polvorin> polvorinList2) {
        this.polvorinList2 = polvorinList2;
    }

    @XmlTransient
    public List<LugarUso> getLugarUsoList() {
        return lugarUsoList;
    }

    public void setLugarUsoList(List<LugarUso> lugarUsoList) {
        this.lugarUsoList = lugarUsoList;
    }

    @XmlTransient
    public List<LugarUso> getLugarUsoList1() {
        return lugarUsoList1;
    }

    public void setLugarUsoList1(List<LugarUso> lugarUsoList1) {
        this.lugarUsoList1 = lugarUsoList1;
    }

    @XmlTransient
    public List<Almacen> getAlmacenList() {
        return almacenList;
    }

    public void setAlmacenList(List<Almacen> almacenList) {
        this.almacenList = almacenList;
    }

    @XmlTransient
    public List<Registro> getRegistroList() {
        return registroList;
    }

    public void setRegistroList(List<Registro> registroList) {
        this.registroList = registroList;
    }

    @XmlTransient
    public List<TipoExplosivo> getTipoExplosivoList() {
        return tipoExplosivoList;
    }

    public void setTipoExplosivoList(List<TipoExplosivo> tipoExplosivoList) {
        this.tipoExplosivoList = tipoExplosivoList;
    }

    public TipoExplosivo getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoExplosivo tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public List<Resolucion> getResolucionList() {
        return resolucionList;
    }

    public void setResolucionList(List<Resolucion> resolucionList) {
        this.resolucionList = resolucionList;
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
        if (!(object instanceof TipoExplosivo)) {
            return false;
        }
        TipoExplosivo other = (TipoExplosivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.epp.data.TipoExplosivo[ id=" + id + " ]";
    }
    
}
