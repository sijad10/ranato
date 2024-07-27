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
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
import pe.gob.sucamec.sistemabase.data.SbPais;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbProvincia;
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author locador192.ogtic
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_INGSALTD_RUTA_PERSONA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaIngsaltdRutaPersona.findAll", query = "SELECT a FROM AmaIngsaltdRutaPersona a"),
    @NamedQuery(name = "AmaIngsaltdRutaPersona.findById", query = "SELECT a FROM AmaIngsaltdRutaPersona a WHERE a.id = :id"),
    @NamedQuery(name = "AmaIngsaltdRutaPersona.findByFechaHoraIngreso", query = "SELECT a FROM AmaIngsaltdRutaPersona a WHERE a.fechaHoraIngreso = :fechaHoraIngreso"),
    @NamedQuery(name = "AmaIngsaltdRutaPersona.findByFechaHoraSalida", query = "SELECT a FROM AmaIngsaltdRutaPersona a WHERE a.fechaHoraSalida = :fechaHoraSalida"),
    @NamedQuery(name = "AmaIngsaltdRutaPersona.findByVueloIngreso", query = "SELECT a FROM AmaIngsaltdRutaPersona a WHERE a.vueloIngreso = :vueloIngreso"),
    @NamedQuery(name = "AmaIngsaltdRutaPersona.findByVueloSalida", query = "SELECT a FROM AmaIngsaltdRutaPersona a WHERE a.vueloSalida = :vueloSalida"),
    @NamedQuery(name = "AmaIngsaltdRutaPersona.findByActivo", query = "SELECT a FROM AmaIngsaltdRutaPersona a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaIngsaltdRutaPersona.findByAudLogin", query = "SELECT a FROM AmaIngsaltdRutaPersona a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaIngsaltdRutaPersona.findByAudNumIp", query = "SELECT a FROM AmaIngsaltdRutaPersona a WHERE a.audNumIp = :audNumIp")})
public class AmaIngsaltdRutaPersona implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_INGSALTD_RUTA_PERSONA")
    @SequenceGenerator(name = "SEQ_AMA_INGSALTD_RUTA_PERSONA", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_INGSALTD_RUTA_PERSONA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FECHA_HORA_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraIngreso;
    @Column(name = "FECHA_HORA_SALIDA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraSalida;
    @Size(max = 30)
    @Column(name = "VUELO_INGRESO")
    private String vueloIngreso;
    @Size(max = 30)
    @Column(name = "VUELO_SALIDA")
    private String vueloSalida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinTable(name = "AMA_MIEMBRODELEGA_CIUDAD", schema = "BDINTEGRADO", joinColumns = {
        @JoinColumn(name = "MIEMBRO_DELEGACION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PROVINCIA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbProvincia> sbProvinciaList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoId;
    @JoinColumn(name = "TIPO_VUELO_SALIDA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac tipoVueloSalidaId;
    @JoinColumn(name = "TIPO_VUELO_INGRESO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac tipoVueloIngresoId;
    @JoinColumn(name = "CARGO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac cargoId;
    @JoinColumn(name = "VIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase viaId;
    @JoinColumn(name = "PERSONA_DELEGACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona personaDelegacionId;
    @JoinColumn(name = "MIEMBRO_DELEGACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona miembroDelegacionId;
    @JoinColumn(name = "PAIS_PROCEDEN_DESTINO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPais paisProcedenDestinoId;
    @JoinColumn(name = "PAIS_DELEGACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPais paisDelegacionId;
    @JoinColumn(name = "PUNTO_INGSAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppPuertoAduanero puntoIngsalId;
    @JoinColumn(name = "LICENCIA_USO_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaLicenciaDeUso licenciaUsoId;
    @JoinColumn(name = "INGSALTEMDEF_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaIngsalTemDef ingsaltemdefId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ingsalrutaperId")
    private List<AmaIngsaltdArma> amaIngsaltdArmaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ingsalrutaperId")
    private List<AmaIngsaltdMunicion> amaIngsaltdMunicionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ingsalrutaperId")
    private List<AmaIngsaltdMatrel> amaIngsaltdMatrelList;


    public AmaIngsaltdRutaPersona() {
    }

    public AmaIngsaltdRutaPersona(Long id) {
        this.id = id;
    }

    public AmaIngsaltdRutaPersona(Long id, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public AmaIngsaltdRutaPersona(AmaIngsaltdRutaPersona dog) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaHoraIngreso() {
        return fechaHoraIngreso;
    }

    public void setFechaHoraIngreso(Date fechaHoraIngreso) {
        this.fechaHoraIngreso = fechaHoraIngreso;
    }

    public Date getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(Date fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public String getVueloIngreso() {
        return vueloIngreso;
    }

    public void setVueloIngreso(String vueloIngreso) {
        this.vueloIngreso = vueloIngreso;
    }

    public String getVueloSalida() {
        return vueloSalida;
    }

    public void setVueloSalida(String vueloSalida) {
        this.vueloSalida = vueloSalida;
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
    public List<SbProvincia> getSbProvinciaList() {
        return sbProvinciaList;
    }

    public void setSbProvinciaList(List<SbProvincia> sbProvinciaList) {
        this.sbProvinciaList = sbProvinciaList;
    }

    public TipoGamac getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoGamac tipoId) {
        this.tipoId = tipoId;
    }

    public TipoGamac getTipoVueloSalidaId() {
        return tipoVueloSalidaId;
    }

    public void setTipoVueloSalidaId(TipoGamac tipoVueloSalidaId) {
        this.tipoVueloSalidaId = tipoVueloSalidaId;
    }

    public TipoGamac getTipoVueloIngresoId() {
        return tipoVueloIngresoId;
    }

    public void setTipoVueloIngresoId(TipoGamac tipoVueloIngresoId) {
        this.tipoVueloIngresoId = tipoVueloIngresoId;
    }

    public TipoGamac getCargoId() {
        return cargoId;
    }

    public void setCargoId(TipoGamac cargoId) {
        this.cargoId = cargoId;
    }

    public TipoBase getViaId() {
        return viaId;
    }

    public void setViaId(TipoBase viaId) {
        this.viaId = viaId;
    }

    public SbPersona getPersonaDelegacionId() {
        return personaDelegacionId;
    }

    public void setPersonaDelegacionId(SbPersona personaDelegacionId) {
        this.personaDelegacionId = personaDelegacionId;
    }

    public SbPersona getMiembroDelegacionId() {
        return miembroDelegacionId;
    }

    public void setMiembroDelegacionId(SbPersona miembroDelegacionId) {
        this.miembroDelegacionId = miembroDelegacionId;
    }

    public SbPais getPaisProcedenDestinoId() {
        return paisProcedenDestinoId;
    }

    public void setPaisProcedenDestinoId(SbPais paisProcedenDestinoId) {
        this.paisProcedenDestinoId = paisProcedenDestinoId;
    }

    public SbPais getPaisDelegacionId() {
        return paisDelegacionId;
    }

    public void setPaisDelegacionId(SbPais paisDelegacionId) {
        this.paisDelegacionId = paisDelegacionId;
    }

    public EppPuertoAduanero getPuntoIngsalId() {
        return puntoIngsalId;
    }

    public void setPuntoIngsalId(EppPuertoAduanero puntoIngsalId) {
        this.puntoIngsalId = puntoIngsalId;
    }

    public AmaLicenciaDeUso getLicenciaUsoId() {
        return licenciaUsoId;
    }

    public void setLicenciaUsoId(AmaLicenciaDeUso licenciaUsoId) {
        this.licenciaUsoId = licenciaUsoId;
    }

    public AmaIngsalTemDef getIngsaltemdefId() {
        return ingsaltemdefId;
    }

    public void setIngsaltemdefId(AmaIngsalTemDef ingsaltemdefId) {
        this.ingsaltemdefId = ingsaltemdefId;
    }

    @XmlTransient
    public List<AmaIngsaltdArma> getAmaIngsaltdArmaList() {
        return amaIngsaltdArmaList;
    }

    public void setAmaIngsaltdArmaList(List<AmaIngsaltdArma> amaIngsaltdArmaList) {
        this.amaIngsaltdArmaList = amaIngsaltdArmaList;
    }

    @XmlTransient
    public List<AmaIngsaltdMunicion> getAmaIngsaltdMunicionList() {
        return amaIngsaltdMunicionList;
    }

    public void setAmaIngsaltdMunicionList(List<AmaIngsaltdMunicion> amaIngsaltdMunicionList) {
        this.amaIngsaltdMunicionList = amaIngsaltdMunicionList;
    }
    @XmlTransient
    public List<AmaIngsaltdMatrel> getAmaIngsaltdMatrelList() {
        return amaIngsaltdMatrelList;
    }

    public void setAmaIngsaltdMatrelList(List<AmaIngsaltdMatrel> amaIngsaltdMatrelList) {
        this.amaIngsaltdMatrelList = amaIngsaltdMatrelList;
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
        if (!(object instanceof AmaIngsaltdRutaPersona)) {
            return false;
        }
        AmaIngsaltdRutaPersona other = (AmaIngsaltdRutaPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaIngsaltdRutaPersona[ id=" + id + " ]";
    }

}
