/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.SbDistrito;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author mespinoza
 */
@Entity
@Table(name = "EPP_POLVORIN", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppPolvorin.findAll", query = "SELECT e FROM EppPolvorin e"),
    @NamedQuery(name = "EppPolvorin.findById", query = "SELECT e FROM EppPolvorin e WHERE e.id = :id"),
    @NamedQuery(name = "EppPolvorin.findByResolucionId", query = "SELECT e FROM EppPolvorin e WHERE e.resolucionId = :resolucionId"),
    @NamedQuery(name = "EppPolvorin.findByDescripcion", query = "SELECT e FROM EppPolvorin e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "EppPolvorin.findByDireccion", query = "SELECT e FROM EppPolvorin e WHERE e.direccion = :direccion"),
    @NamedQuery(name = "EppPolvorin.findByArea", query = "SELECT e FROM EppPolvorin e WHERE e.area = :area"),
    @NamedQuery(name = "EppPolvorin.findByLargo", query = "SELECT e FROM EppPolvorin e WHERE e.largo = :largo"),
    @NamedQuery(name = "EppPolvorin.findByAncho", query = "SELECT e FROM EppPolvorin e WHERE e.ancho = :ancho"),
    @NamedQuery(name = "EppPolvorin.findByAlto", query = "SELECT e FROM EppPolvorin e WHERE e.alto = :alto"),
    @NamedQuery(name = "EppPolvorin.findByCapacidad", query = "SELECT e FROM EppPolvorin e WHERE e.capacidad = :capacidad"),
    @NamedQuery(name = "EppPolvorin.findByVolumen", query = "SELECT e FROM EppPolvorin e WHERE e.volumen = :volumen"),
    @NamedQuery(name = "EppPolvorin.findByLatitud", query = "SELECT e FROM EppPolvorin e WHERE e.latitud = :latitud"),
    @NamedQuery(name = "EppPolvorin.findByLongitud", query = "SELECT e FROM EppPolvorin e WHERE e.longitud = :longitud"),
    @NamedQuery(name = "EppPolvorin.findByNroAmbientes", query = "SELECT e FROM EppPolvorin e WHERE e.nroAmbientes = :nroAmbientes"),
    @NamedQuery(name = "EppPolvorin.findByContratoAlquiler", query = "SELECT e FROM EppPolvorin e WHERE e.contratoAlquiler = :contratoAlquiler"),
    @NamedQuery(name = "EppPolvorin.findByActivo", query = "SELECT e FROM EppPolvorin e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppPolvorin.findByAudLogin", query = "SELECT e FROM EppPolvorin e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppPolvorin.findByAudNumIp", query = "SELECT e FROM EppPolvorin e WHERE e.audNumIp = :audNumIp")})
public class EppPolvorin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "RESOLUCION_ID")
    private Long resolucionId;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 500)
    @Column(name = "DIRECCION")
    private String direccion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AREA")
    private BigDecimal area;
    @Column(name = "LARGO")
    private BigDecimal largo;
    @Column(name = "ANCHO")
    private BigDecimal ancho;
    @Column(name = "ALTO")
    private BigDecimal alto;
    @Column(name = "CAPACIDAD")
    private BigDecimal capacidad;
    @Column(name = "VOLUMEN")
    private BigDecimal volumen;
    @Column(name = "LATITUD")
    private BigDecimal latitud;
    @Column(name = "LONGITUD")
    private BigDecimal longitud;
    @Column(name = "NRO_AMBIENTES")
    private Short nroAmbientes;
    @Column(name = "CONTRATO_ALQUILER")
    private Long contratoAlquiler;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @ManyToMany(mappedBy = "eppPolvorinList")
    private List<EppLibro> eppLibroList;
    @ManyToMany(mappedBy = "eppPolvorinList")
    private List<EppRegistro> eppRegistroList;
    @OneToMany(mappedBy = "polvInspecId")
    private List<EppRegistroIntSal> eppRegistroIntSalList;
    @OneToMany(mappedBy = "polvDestId")
    private List<EppRegistroIntSal> eppRegistroIntSalList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "polvorinId")
    private List<EppLibroPerdida> eppLibroPerdidaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "polvorinId")
    private List<EppContratoAlqPolv> eppContratoAlqPolvList;
    @OneToMany(mappedBy = "destinoPolvorin")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList;
    @OneToMany(mappedBy = "origenPolvorin")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList1;
    @OneToMany(mappedBy = "polvorinId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList;
    @JoinColumn(name = "TIPO_PROPIETARIO_POLVORIN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoPropietarioPolvorinId;
    @JoinColumn(name = "TIPO_CANCELACION", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoCancelacion;
    @JoinColumn(name = "PROPIETARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona propietarioId;
    @JoinColumn(name = "REPRESENTANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona representanteId;
    @JoinColumn(name = "DISTRITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistrito distritoId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EppRegistro registroId;
    @JoinColumn(name = "COM_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppCom comId;

    public EppPolvorin() {
    }

    public EppPolvorin(Long id) {
        this.id = id;
    }

    public EppPolvorin(Long id, short activo, String audLogin, String audNumIp) {
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

    public Long getResolucionId() {
        return resolucionId;
    }

    public void setResolucionId(Long resolucionId) {
        this.resolucionId = resolucionId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getLargo() {
        return largo;
    }

    public void setLargo(BigDecimal largo) {
        this.largo = largo;
    }

    public BigDecimal getAncho() {
        return ancho;
    }

    public void setAncho(BigDecimal ancho) {
        this.ancho = ancho;
    }

    public BigDecimal getAlto() {
        return alto;
    }

    public void setAlto(BigDecimal alto) {
        this.alto = alto;
    }

    public BigDecimal getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(BigDecimal capacidad) {
        this.capacidad = capacidad;
    }

    public BigDecimal getVolumen() {
        return volumen;
    }

    public void setVolumen(BigDecimal volumen) {
        this.volumen = volumen;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public Short getNroAmbientes() {
        return nroAmbientes;
    }

    public void setNroAmbientes(Short nroAmbientes) {
        this.nroAmbientes = nroAmbientes;
    }

    public Long getContratoAlquiler() {
        return contratoAlquiler;
    }

    public void setContratoAlquiler(Long contratoAlquiler) {
        this.contratoAlquiler = contratoAlquiler;
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
    public List<EppLibro> getEppLibroList() {
        return eppLibroList;
    }

    public void setEppLibroList(List<EppLibro> eppLibroList) {
        this.eppLibroList = eppLibroList;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    @XmlTransient
    public List<EppRegistroIntSal> getEppRegistroIntSalList() {
        return eppRegistroIntSalList;
    }

    public void setEppRegistroIntSalList(List<EppRegistroIntSal> eppRegistroIntSalList) {
        this.eppRegistroIntSalList = eppRegistroIntSalList;
    }

    @XmlTransient
    public List<EppRegistroIntSal> getEppRegistroIntSalList1() {
        return eppRegistroIntSalList1;
    }

    public void setEppRegistroIntSalList1(List<EppRegistroIntSal> eppRegistroIntSalList1) {
        this.eppRegistroIntSalList1 = eppRegistroIntSalList1;
    }

    @XmlTransient
    public List<EppLibroPerdida> getEppLibroPerdidaList() {
        return eppLibroPerdidaList;
    }

    public void setEppLibroPerdidaList(List<EppLibroPerdida> eppLibroPerdidaList) {
        this.eppLibroPerdidaList = eppLibroPerdidaList;
    }

    @XmlTransient
    public List<EppContratoAlqPolv> getEppContratoAlqPolvList() {
        return eppContratoAlqPolvList;
    }

    public void setEppContratoAlqPolvList(List<EppContratoAlqPolv> eppContratoAlqPolvList) {
        this.eppContratoAlqPolvList = eppContratoAlqPolvList;
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

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList() {
        return eppInspeccionPolvorinList;
    }

    public void setEppInspeccionPolvorinList(List<EppInspeccionPolvorin> eppInspeccionPolvorinList) {
        this.eppInspeccionPolvorinList = eppInspeccionPolvorinList;
    }

    public TipoExplosivoGt getTipoPropietarioPolvorinId() {
        return tipoPropietarioPolvorinId;
    }

    public void setTipoPropietarioPolvorinId(TipoExplosivoGt tipoPropietarioPolvorinId) {
        this.tipoPropietarioPolvorinId = tipoPropietarioPolvorinId;
    }

    public TipoExplosivoGt getTipoCancelacion() {
        return tipoCancelacion;
    }

    public void setTipoCancelacion(TipoExplosivoGt tipoCancelacion) {
        this.tipoCancelacion = tipoCancelacion;
    }

    public SbPersona getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(SbPersona propietarioId) {
        this.propietarioId = propietarioId;
    }

    public SbPersona getRepresentanteId() {
        return representanteId;
    }

    public void setRepresentanteId(SbPersona representanteId) {
        this.representanteId = representanteId;
    }

    public SbDistrito getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(SbDistrito distritoId) {
        this.distritoId = distritoId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }

    public EppCom getComId() {
        return comId;
    }

    public void setComId(EppCom comId) {
        this.comId = comId;
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
        if (!(object instanceof EppPolvorin)) {
            return false;
        }
        EppPolvorin other = (EppPolvorin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppPolvorin[ id=" + id + " ]";
    }

}
