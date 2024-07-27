/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
//import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_INVENTARIO_ARMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaInventarioArma.findAll", query = "SELECT a FROM AmaInventarioArma a"),
    @NamedQuery(name = "AmaInventarioArma.findById", query = "SELECT a FROM AmaInventarioArma a WHERE a.id = :id"),
    @NamedQuery(name = "AmaInventarioArma.findByCodigo", query = "SELECT a FROM AmaInventarioArma a WHERE a.codigo = :codigo"),
    @NamedQuery(name = "AmaInventarioArma.findByNroRua", query = "SELECT a FROM AmaInventarioArma a WHERE a.nroRua = :nroRua"),
    @NamedQuery(name = "AmaInventarioArma.findBySerie", query = "SELECT a FROM AmaInventarioArma a WHERE a.serie = :serie"),
    @NamedQuery(name = "AmaInventarioArma.findByAnaquel", query = "SELECT a FROM AmaInventarioArma a WHERE a.anaquel = :anaquel"),
    @NamedQuery(name = "AmaInventarioArma.findByFila", query = "SELECT a FROM AmaInventarioArma a WHERE a.fila = :fila"),
    @NamedQuery(name = "AmaInventarioArma.findByColumna", query = "SELECT a FROM AmaInventarioArma a WHERE a.columna = :columna"),
    @NamedQuery(name = "AmaInventarioArma.findByObservacion", query = "SELECT a FROM AmaInventarioArma a WHERE a.observacion = :observacion"),
    @NamedQuery(name = "AmaInventarioArma.findByCierreInventario", query = "SELECT a FROM AmaInventarioArma a WHERE a.cierreInventario = :cierreInventario"),
    @NamedQuery(name = "AmaInventarioArma.findByActivo", query = "SELECT a FROM AmaInventarioArma a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaInventarioArma.findByAudLogin", query = "SELECT a FROM AmaInventarioArma a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaInventarioArma.findByAudNumIp", query = "SELECT a FROM AmaInventarioArma a WHERE a.audNumIp = :audNumIp"),
    @NamedQuery(name = "AmaInventarioArma.findByFechaCierre", query = "SELECT a FROM AmaInventarioArma a WHERE a.fechaCierre = :fechaCierre")})
public class AmaInventarioArma implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_INVENTARIO_ARMA")
    @SequenceGenerator(name = "SEQ_AMA_INVENTARIO_ARMA", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_INVENTARIO_ARMA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private Long codigo;
    @Size(max = 25)
    @Column(name = "NRO_RUA")
    private String nroRua;
    @Size(max = 25)
    @Column(name = "SERIE")
    private String serie;
    @Size(max = 25)
    @Column(name = "ANAQUEL")
    private String anaquel;
    @Size(max = 25)
    @Column(name = "FILA")
    private String fila;
    @Size(max = 25)
    @Column(name = "COLUMNA")
    private String columna;
    @Size(max = 500)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CIERRE_INVENTARIO")
    private short cierreInventario;
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
    @Column(name = "FECHA_CIERRE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCierre;
    @Column(name = "ACTUAL")
    private short actual;
    @ManyToMany(mappedBy = "amaInventarioArmaList")
    private List<AmaSolicitudRecojo> amaSolicitudRecojoList;
    @ManyToMany(mappedBy = "amaInventarioArmaList")
    private List<AmaGuiaTransito> amaGuiaTransitoList;
    @JoinColumn(name = "ESTADOFUNCIONAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac estadofuncionalId;
    @JoinColumn(name = "ESTADOCONSERVACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac estadoconservacionId;
    @JoinColumn(name = "ESTADOSERIE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac estadoserieId;
    @JoinColumn(name = "AMBIENTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac ambienteId;
    @JoinColumn(name = "TIPO_INTERNAMIENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoInternamientoId;
    @JoinColumn(name = "ESTADO_CACHA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac estadoCachaId;
    @JoinColumn(name = "MATERIAL_GUARDAMANO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac materialGuardamanoId;
    @JoinColumn(name = "UMEDIDA_ID", referencedColumnName = "ID")
    @ManyToOne
    private UnidadMedida umedidaId;
    @JoinColumn(name = "NOVEDAD_CANON_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac novedadCanonId;
    @JoinColumn(name = "MATERIAL_CACHA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac materialCachaId;
    @JoinColumn(name = "MATERIAL_CULATA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac materialCulataId;
    @JoinColumn(name = "MATERIAL_CANTONERA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac materialCantoneraId;
    @JoinColumn(name = "MATERIAL_EMPUNADURA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac materialEmpunaduraId;
    @JoinColumn(name = "ESTADO_CANTONERA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac estadoCantoneraId;
    @JoinColumn(name = "ESTADO_CULATA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac estadoCulataId;
    @JoinColumn(name = "ESTADO_EMPUNADURA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac estadoEmpunaduraId;
    @JoinColumn(name = "ESTADO_GUARDAMANO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac estadoGuardamanoId;
    @JoinColumn(name = "PROPIETARIO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona propietarioId;
    @JoinColumn(name = "ALMACEN_SUCAMEC_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDireccion almacenSucamecId;
    @JoinColumn(name = "MODELO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaModelos modeloId;
    @JoinColumn(name = "SITUACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac situacionId;
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @JoinColumn(name = "MECANISMO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac mecanismoId;
    @JoinColumn(name = "EXTERIOR_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac exteriorId;
    @JoinColumn(name = "MODALIDAD_TIRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoGamac modalidadTiroId;
    @Column(name = "PESO")
    private BigDecimal peso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONDICION_ALMACEN")
    private short condicionAlmacen;
    @Size(max = 50)
    @Column(name = "FOTO1")
    private String foto1;
    @Size(max = 50)
    @Column(name = "FOTO2")
    private String foto2;
    @Size(max = 50)
    @Column(name = "FOTO3")
    private String foto3;
    @Column(name = "LICENCIA_DISCA_ID")
    private Long licenciaDiscaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventarioArmaId")
    private List<AmaInventarioAccesorios> amaInventarioAccesoriosList;
    @JoinColumn(name = "ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaArma armaId;
    @JoinColumn(name = "POSIBLE_TITULAR_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona posibleTitularId;
    @JoinColumn(name = "TARJETA_PROPIEDAD_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaTarjetaPropiedad tarjetaPropiedadId;
    @JoinColumn(name = "DISCA_FOX_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaMigraDiscaFox discaFoxId;
    
    public AmaInventarioArma() {
    }

    public AmaInventarioArma(Long id) {
        this.id = id;
    }

    public AmaInventarioArma(Long id, Long codigo, short cierreInventario, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.codigo = codigo;
        this.cierreInventario = cierreInventario;
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

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNroRua() {
        return nroRua;
    }

    public void setNroRua(String nroRua) {
        this.nroRua = nroRua;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getAnaquel() {
        return anaquel;
    }

    public void setAnaquel(String anaquel) {
        this.anaquel = anaquel;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getColumna() {
        return columna;
    }

    public void setColumna(String columna) {
        this.columna = columna;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public short getCierreInventario() {
        return cierreInventario;
    }

    public void setCierreInventario(short cierreInventario) {
        this.cierreInventario = cierreInventario;
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
    
    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }
    
    public TipoGamac getEstadoserieId() {
        return estadoserieId;
    }

    public void setEstadoserieId(TipoGamac estadoserieId) {
        this.estadoserieId = estadoserieId;
    }

    public TipoGamac getEstadoconservacionId() {
        return estadoconservacionId;
    }

    public void setEstadoconservacionId(TipoGamac estadoconservacionId) {
        this.estadoconservacionId = estadoconservacionId;
    }

    public TipoGamac getEstadofuncionalId() {
        return estadofuncionalId;
    }

    public void setEstadofuncionalId(TipoGamac estadofuncionalId) {
        this.estadofuncionalId = estadofuncionalId;
    }

    public SbPersona getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(SbPersona propietarioId) {
        this.propietarioId = propietarioId;
    }

    public SbDireccion getAlmacenSucamecId() {
        return almacenSucamecId;
    }

    public void setAlmacenSucamecId(SbDireccion almacenSucamecId) {
        this.almacenSucamecId = almacenSucamecId;
    }

    public AmaModelos getModeloId() {
        return modeloId;
    }

    public void setModeloId(AmaModelos modeloId) {
        this.modeloId = modeloId;
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
        if (!(object instanceof AmaInventarioArma)) {
            return false;
        }
        AmaInventarioArma other = (AmaInventarioArma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemagamac.data.AmaInventarioArma[ id=" + id + " ]";
    }

    public TipoGamac getAmbienteId() {
        return ambienteId;
    }

    public void setAmbienteId(TipoGamac ambienteId) {
        this.ambienteId = ambienteId;
    }

    /**
     * @return the amaGuiaTransitoList
     */
    public List<AmaGuiaTransito> getAmaGuiaTransitoList() {
        return amaGuiaTransitoList;
    }

    /**
     * @param amaGuiaTransitoList the amaGuiaTransitoList to set
     */
    public void setAmaGuiaTransitoList(List<AmaGuiaTransito> amaGuiaTransitoList) {
        this.amaGuiaTransitoList = amaGuiaTransitoList;
    }

    /**
     * @return the situacionId
     */
    public TipoGamac getSituacionId() {
        return situacionId;
    }

    /**
     * @param situacionId the situacionId to set
     */
    public void setSituacionId(TipoGamac situacionId) {
        this.situacionId = situacionId;
    }

    public TipoGamac getMecanismoId() {
        return mecanismoId;
    }

    public void setMecanismoId(TipoGamac mecanismoId) {
        this.mecanismoId = mecanismoId;
    }

    public TipoGamac getNovedadCanonId() {
        return novedadCanonId;
    }

    public void setNovedadCanonId(TipoGamac novedadCanonId) {
        this.novedadCanonId = novedadCanonId;
    }

    /**
     * @return the materialCachaId
     */
    public TipoGamac getMaterialCachaId() {
        return materialCachaId;
    }

    /**
     * @param materialCachaId the materialCachaId to set
     */
    public void setMaterialCachaId(TipoGamac materialCachaId) {
        this.materialCachaId = materialCachaId;
    }

    /**
     * @return the estadoCachaId
     */
    public TipoGamac getEstadoCachaId() {
        return estadoCachaId;
    }

    /**
     * @param estadoCachaId the estadoCachaId to set
     */
    public void setEstadoCachaId(TipoGamac estadoCachaId) {
        this.estadoCachaId = estadoCachaId;
    }

    /**
     * @return the modalidadTiroId
     */
    public TipoGamac getModalidadTiroId() {
        return modalidadTiroId;
    }

    /**
     * @param modalidadTiroId the modalidadTiroId to set
     */
    public void setModalidadTiroId(TipoGamac modalidadTiroId) {
        this.modalidadTiroId = modalidadTiroId;
    }
    
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * @return the exteriorId
     */
    public TipoGamac getExteriorId() {
        return exteriorId;
    }

    /**
     * @param exteriorId the exteriorId to set
     */
    public void setExteriorId(TipoGamac exteriorId) {
        this.exteriorId = exteriorId;
    }

    /**
     * @return the peso
     */
    public BigDecimal getPeso() {
        return peso;
    }

    /**
     * @param peso the peso to set
     */
    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }
    
    public UnidadMedida getUmedidaId() {
        return umedidaId;
    }

    public void setUmedidaId(UnidadMedida umedidaId) {
        this.umedidaId = umedidaId;
    }

    /**
     * @return the condicionAlmacen
     */
    public short getCondicionAlmacen() {
        return condicionAlmacen;
    }

    /**
     * @param condicionAlmacen the condicionAlmacen to set
     */
    public void setCondicionAlmacen(short condicionAlmacen) {
        this.condicionAlmacen = condicionAlmacen;
    }

    /**
     * @return the tipoInternamientoId
     */
    public TipoGamac getTipoInternamientoId() {
        return tipoInternamientoId;
    }

    /**
     * @param tipoInternamientoId the tipoInternamientoId to set
     */
    public void setTipoInternamientoId(TipoGamac tipoInternamientoId) {
        this.tipoInternamientoId = tipoInternamientoId;
    }

    public String getFoto1() {
        return foto1;
    }

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }

    public String getFoto2() {
        return foto2;
    }

    public void setFoto2(String foto2) {
        this.foto2 = foto2;
    }    

    /**
     * @return the materialGuardamanoId
     */
    public TipoGamac getMaterialGuardamanoId() {
        return materialGuardamanoId;
    }

    /**
     * @param materialGuardamanoId the materialGuardamanoId to set
     */
    public void setMaterialGuardamanoId(TipoGamac materialGuardamanoId) {
        this.materialGuardamanoId = materialGuardamanoId;
    }

    /**
     * @return the materialCulataId
     */
    public TipoGamac getMaterialCulataId() {
        return materialCulataId;
    }

    /**
     * @param materialCulataId the materialCulataId to set
     */
    public void setMaterialCulataId(TipoGamac materialCulataId) {
        this.materialCulataId = materialCulataId;
    }

    /**
     * @return the materialCantoneraId
     */
    public TipoGamac getMaterialCantoneraId() {
        return materialCantoneraId;
    }

    /**
     * @param materialCantoneraId the materialCantoneraId to set
     */
    public void setMaterialCantoneraId(TipoGamac materialCantoneraId) {
        this.materialCantoneraId = materialCantoneraId;
    }

    /**
     * @return the materialEmpunaduraId
     */
    public TipoGamac getMaterialEmpunaduraId() {
        return materialEmpunaduraId;
    }

    /**
     * @param materialEmpunaduraId the materialEmpunaduraId to set
     */
    public void setMaterialEmpunaduraId(TipoGamac materialEmpunaduraId) {
        this.materialEmpunaduraId = materialEmpunaduraId;
    }

    public TipoGamac getEstadoCantoneraId() {
        return estadoCantoneraId;
    }

    public void setEstadoCantoneraId(TipoGamac estadoCantoneraId) {
        this.estadoCantoneraId = estadoCantoneraId;
    }

    public TipoGamac getEstadoCulataId() {
        return estadoCulataId;
    }

    public void setEstadoCulataId(TipoGamac estadoCulataId) {
        this.estadoCulataId = estadoCulataId;
    }

    public TipoGamac getEstadoEmpunaduraId() {
        return estadoEmpunaduraId;
    }

    public void setEstadoEmpunaduraId(TipoGamac estadoEmpunaduraId) {
        this.estadoEmpunaduraId = estadoEmpunaduraId;
    }

    public TipoGamac getEstadoGuardamanoId() {
        return estadoGuardamanoId;
    }

    public void setEstadoGuardamanoId(TipoGamac estadoGuardamanoId) {
        this.estadoGuardamanoId = estadoGuardamanoId;
    }

    public String getFoto3() {
        return foto3;
    }

    public void setFoto3(String foto3) {
        this.foto3 = foto3;
    }

    public Long getLicenciaDiscaId() {
        return licenciaDiscaId;
    }

    public void setLicenciaDiscaId(Long licenciaDiscaId) {
        this.licenciaDiscaId = licenciaDiscaId;
    }
    @XmlTransient
    public List<AmaSolicitudRecojo> getAmaSolicitudRecojoList() {
        return amaSolicitudRecojoList;
    }

    public void setAmaSolicitudRecojoList(List<AmaSolicitudRecojo> amaSolicitudRecojoList) {
        this.amaSolicitudRecojoList = amaSolicitudRecojoList;
    }

    public short getActual() {
        return actual;
    }

    public void setActual(short actual) {
        this.actual = actual;
    }
    
    @XmlTransient
    public List<AmaInventarioAccesorios> getAmaInventarioAccesoriosList() {
        return amaInventarioAccesoriosList;
    }

    public void setAmaInventarioAccesoriosList(List<AmaInventarioAccesorios> amaInventarioAccesoriosList) {
        this.amaInventarioAccesoriosList = amaInventarioAccesoriosList;
    }

    public AmaArma getArmaId() {
        return armaId;
    }

    public void setArmaId(AmaArma armaId) {
        this.armaId = armaId;
    }

    public SbPersona getPosibleTitularId() {
        return posibleTitularId;
    }

    public void setPosibleTitularId(SbPersona posibleTitularId) {
        this.posibleTitularId = posibleTitularId;
    }

    public AmaTarjetaPropiedad getTarjetaPropiedadId() {
        return tarjetaPropiedadId;
    }

    public void setTarjetaPropiedadId(AmaTarjetaPropiedad tarjetaPropiedadId) {
        this.tarjetaPropiedadId = tarjetaPropiedadId;
    }

    public AmaMigraDiscaFox getDiscaFoxId() {
        return discaFoxId;
    }

    public void setDiscaFoxId(AmaMigraDiscaFox discaFoxId) {
        this.discaFoxId = discaFoxId;
    }
        
}
