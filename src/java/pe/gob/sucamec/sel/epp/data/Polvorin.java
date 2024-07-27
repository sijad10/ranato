/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.epp.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.sistemabase.data.SbDistrito;
import pe.gob.sucamec.sistemabase.data.SbPersona;


/**
 *
 * @author rmoscoso
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_POLVORIN", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Polvorin.findAll", query = "SELECT p FROM Polvorin p"),
    @NamedQuery(name = "Polvorin.findById", query = "SELECT p FROM Polvorin p WHERE p.id = :id"),
    @NamedQuery(name = "Polvorin.findByResolucionId", query = "SELECT p FROM Polvorin p WHERE p.resolucionId = :resolucionId"),
    @NamedQuery(name = "Polvorin.findByIdExpediente", query = "SELECT p FROM Polvorin p WHERE p.idExpediente = :idExpediente"),
    @NamedQuery(name = "Polvorin.findByDescripcion", query = "SELECT p FROM Polvorin p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Polvorin.findByDireccion", query = "SELECT p FROM Polvorin p WHERE p.direccion = :direccion"),
    @NamedQuery(name = "Polvorin.findByArea", query = "SELECT p FROM Polvorin p WHERE p.area = :area"),
    @NamedQuery(name = "Polvorin.findByLargo", query = "SELECT p FROM Polvorin p WHERE p.largo = :largo"),
    @NamedQuery(name = "Polvorin.findByAncho", query = "SELECT p FROM Polvorin p WHERE p.ancho = :ancho"),
    @NamedQuery(name = "Polvorin.findByAlto", query = "SELECT p FROM Polvorin p WHERE p.alto = :alto"),
    @NamedQuery(name = "Polvorin.findByCapacidad", query = "SELECT p FROM Polvorin p WHERE p.capacidad = :capacidad"),
    @NamedQuery(name = "Polvorin.findByVolumen", query = "SELECT p FROM Polvorin p WHERE p.volumen = :volumen"),
    @NamedQuery(name = "Polvorin.findByLatitud", query = "SELECT p FROM Polvorin p WHERE p.latitud = :latitud"),
    @NamedQuery(name = "Polvorin.findByLongitud", query = "SELECT p FROM Polvorin p WHERE p.longitud = :longitud"),
    @NamedQuery(name = "Polvorin.findByNroAmbientes", query = "SELECT p FROM Polvorin p WHERE p.nroAmbientes = :nroAmbientes"),
    @NamedQuery(name = "Polvorin.findByContratoAlquiler", query = "SELECT p FROM Polvorin p WHERE p.contratoAlquiler = :contratoAlquiler"),
    @NamedQuery(name = "Polvorin.findByActivo", query = "SELECT p FROM Polvorin p WHERE p.activo = :activo"),
    @NamedQuery(name = "Polvorin.findByAudLogin", query = "SELECT p FROM Polvorin p WHERE p.audLogin = :audLogin"),
    @NamedQuery(name = "Polvorin.findByAudNumIp", query = "SELECT p FROM Polvorin p WHERE p.audNumIp = :audNumIp")})
public class Polvorin implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_POLVORIN")
    @SequenceGenerator(name = "SEQ_EPP_POLVORIN", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_POLVORIN", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RESOLUCION_ID")
    private Long resolucionId;
    @Column(name = "ID_EXPEDIENTE")
    private Long idExpediente;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 500)
    @Column(name = "DIRECCION")
    private String direccion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AREA")
    private Double area;
    @Column(name = "LARGO")
    private Double largo;
    @Column(name = "ANCHO")
    private Double ancho;
    @Column(name = "ALTO")
    private Double alto;
    @Column(name = "CAPACIDAD")
    private Double capacidad;
    @Column(name = "VOLUMEN")
    private Double volumen;
    @Column(name = "LATITUD")
    private Double latitud;
    @Column(name = "LONGITUD")
    private Double longitud;
    @Column(name = "NRO_AMBIENTES")
    private Short nroAmbientes;
    @Column(name = "CONTRATO_ALQUILER")
    private Long contratoAlquiler;
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
    @OneToMany(mappedBy = "destinoPolvorin")
    private List<RegistroGuiaTransito> registroGuiaTransitoList;
    @OneToMany(mappedBy = "origenPolvorin")
    private List<RegistroGuiaTransito> registroGuiaTransitoList1;
    @JoinColumn(name = "COM_ID", referencedColumnName = "ID")
    @ManyToOne
    private Com comId;
    @JoinColumn(name = "DISTRITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistrito distritoId;
    @JoinColumn(name = "REPRESENTANTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona representanteId;
    @JoinColumn(name = "PROPIETARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona propietarioId;
    @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registro registro;
    @JoinColumn(name = "TIPO_CANCELACION", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivo tipoCancelacion;
    @JoinColumn(name = "TIPO_PROPIETARIO_POLVORIN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivo tipoPropietarioPolvorinId;
    @JoinColumn(name = "TIPO_MODIFICATORIA", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivo tipoModificatoria;

    public Polvorin() {
    }

    public Polvorin(Long id) {
        this.id = id;
    }

    public Polvorin(Long id, Long resolucionId, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.resolucionId = resolucionId;
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

    public Long getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(Long idExpediente) {
        this.idExpediente = idExpediente;
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

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getLargo() {
        return largo;
    }

    public void setLargo(Double largo) {
        this.largo = largo;
    }

    public Double getAncho() {
        return ancho;
    }

    public void setAncho(Double ancho) {
        this.ancho = ancho;
    }

    public Double getAlto() {
        return alto;
    }

    public void setAlto(Double alto) {
        this.alto = alto;
    }

    public Double getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Double capacidad) {
        this.capacidad = capacidad;
    }

    public Double getVolumen() {
        return volumen;
    }

    public void setVolumen(Double volumen) {
        this.volumen = volumen;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
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

    public Com getComId() {
        return comId;
    }

    public void setComId(Com comId) {
        this.comId = comId;
    }

    public SbDistrito getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(SbDistrito distritoId) {
        this.distritoId = distritoId;
    }

    public SbPersona getRepresentanteId() {
        return representanteId;
    }

    public void setRepresentanteId(SbPersona representanteId) {
        this.representanteId = representanteId;
    }

    public SbPersona getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(SbPersona propietarioId) {
        this.propietarioId = propietarioId;
    }

    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public TipoExplosivo getTipoCancelacion() {
        return tipoCancelacion;
    }

    public void setTipoCancelacion(TipoExplosivo tipoCancelacion) {
        this.tipoCancelacion = tipoCancelacion;
    }

    public TipoExplosivo getTipoPropietarioPolvorinId() {
        return tipoPropietarioPolvorinId;
    }

    public void setTipoPropietarioPolvorinId(TipoExplosivo tipoPropietarioPolvorinId) {
        this.tipoPropietarioPolvorinId = tipoPropietarioPolvorinId;
    }

    public TipoExplosivo getTipoModificatoria() {
        return tipoModificatoria;
    }

    public void setTipoModificatoria(TipoExplosivo tipoModificatoria) {
        this.tipoModificatoria = tipoModificatoria;
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
        if (!(object instanceof Polvorin)) {
            return false;
        }
        Polvorin other = (Polvorin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.epp.data.Polvorin[ id=" + id + " ]";
    }
    
}
